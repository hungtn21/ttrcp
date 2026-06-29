import re, statistics
from pathlib import Path

base = Path(r'data/truck-container/output/newOutput')

def read_summary(fp):
    rows = {}
    with open(fp, 'r') as f:
        for line in f:
            m = re.match(r'\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*(\d+)\s*\|\s*(\d+)', line)
            if m:
                x, y = int(m.group(1)), int(m.group(2))
                rows[(x, y)] = {
                    'gr_init': float(m.group(3)), 'gt_init': float(m.group(4)), 'gc_init': float(m.group(5)),
                    'gr_final': float(m.group(6)), 'gt_final': float(m.group(7)), 'gc_final': float(m.group(8)),
                    'init_t': int(m.group(9)), 'time': int(m.group(10)),
                }
    return rows

ci = read_summary(base / 'summary_cheapestinsertioninit_10runs.md')
fp = read_summary(base / 'summary_realFPIUS_10runs.md')

keys = sorted(ci.keys())

# ===== 1. Side-by-side cost & time per instance =====
print('## SO SANH CHI TIET: CheapestInsertionInit (CI) vs FPIUSInit (FP)\n')
print('|   x  | y  | CI gc_final | FP gc_final | FP/CI | CI time(s) | FP time(s) | FP/CI time | CI final gr/gt | FP final gr/gt |')
print('|------|----|------------:|------------:|------:|-----------:|-----------:|-----------:|---------------:|---------------:|')
for k in keys:
    ci_gc = ci[k]['gc_final']; fp_gc = fp[k]['gc_final']
    ci_t = ci[k]['time']/1000; fp_t = fp[k]['time']/1000
    ratio_gc = fp_gc / ci_gc if ci_gc > 0 else 0
    ratio_t = fp_t / ci_t if ci_t > 0 else 0
    ci_grgt = f"{ci[k]['gr_final']:.0f}/{ci[k]['gt_final']:.0f}"
    fp_grgt = f"{fp[k]['gr_final']:.0f}/{fp[k]['gt_final']:.0f}"
    print(f"| {k[0]:>4} | {k[1]:>2} | {ci_gc:>11.0f} | {fp_gc:>11.0f} | {ratio_gc:>5.2f} | {ci_t:>10.1f} | {fp_t:>10.1f} | {ratio_t:>10.1f} | {ci_grgt:>14} | {fp_grgt:>14} |")

# ===== 2. Init quality =====
print('\n## CHAT LUONG KHOI TAO: CheapestInsertionInit vs FPIUSInit\n')
print('|   x  | y  | CI gc_init | FP gc_init | FP/CI | CI init_t(ms) | FP init_t(ms) |')
print('|------|----|-----------:|-----------:|------:|--------------:|--------------:|')
for k in keys:
    ratio = fp[k]['gc_init'] / ci[k]['gc_init'] if ci[k]['gc_init'] > 0 else 0
    print(f"| {k[0]:>4} | {k[1]:>2} | {ci[k]['gc_init']:>10.0f} | {fp[k]['gc_init']:>10.0f} | {ratio:>5.2f} | {ci[k]['init_t']:>13} | {fp[k]['init_t']:>13} |")

# ===== 3. Per-size averages =====
print('\n## TRUNG BINH THEO CO DU LIEU\n')
print('|   x  | CI gc_init | FP gc_init | FP/CI init | CI gc_final | FP gc_final | FP/CI final | CI time(s) | FP time(s) | FP/CI time |')
print('|------|-----------:|-----------:|-----------:|------------:|------------:|------------:|-----------:|-----------:|-----------:|')
for x in sorted(set(k[0] for k in keys)):
    ci_gc_init = [ci[k]['gc_init'] for k in keys if k[0]==x]
    fp_gc_init = [fp[k]['gc_init'] for k in keys if k[0]==x]
    ci_gc_fin = [ci[k]['gc_final'] for k in keys if k[0]==x]
    fp_gc_fin = [fp[k]['gc_final'] for k in keys if k[0]==x]
    ci_t = [ci[k]['time']/1000 for k in keys if k[0]==x]
    fp_t = [fp[k]['time']/1000 for k in keys if k[0]==x]
    r_init = statistics.mean(fp_gc_init) / statistics.mean(ci_gc_init)
    r_final = statistics.mean(fp_gc_fin) / statistics.mean(ci_gc_fin)
    r_time = statistics.mean(fp_t) / statistics.mean(ci_t) if statistics.mean(ci_t) > 0 else 0
    print(f"| {x:>4} | {statistics.mean(ci_gc_init):>10.0f} | {statistics.mean(fp_gc_init):>10.0f} | {r_init:>10.2f} | {statistics.mean(ci_gc_fin):>11.0f} | {statistics.mean(fp_gc_fin):>11.0f} | {r_final:>11.2f} | {statistics.mean(ci_t):>10.1f} | {statistics.mean(fp_t):>10.1f} | {r_time:>10.1f} |")

# ===== 4. Improvement from init to final =====
print('\n## KHA NANG CAI THIEN CUA ALNS\n')
print('|   x  | y  | CI init->final | CI improve% | FP init->final | FP improve% |')
print('|------|----|---------------:|------------:|---------------:|------------:|')
for k in keys:
    ci_imp = ci[k]['gc_init'] - ci[k]['gc_final']
    fp_imp = fp[k]['gc_init'] - fp[k]['gc_final']
    ci_pct = ci_imp / ci[k]['gc_init'] * 100 if ci[k]['gc_init'] > 0 else 0
    fp_pct = fp_imp / fp[k]['gc_init'] * 100 if fp[k]['gc_init'] > 0 else 0
    print(f"| {k[0]:>4} | {k[1]:>2} | {ci_imp:>14.0f} | {ci_pct:>11.1f}% | {fp_imp:>14.0f} | {fp_pct:>11.1f}% |")

# ===== 5. Win/loss/tie count =====
print('\n## THANG/THUA: CI vs FPIUS (gc_final)')
ci_wins = fp_wins = ties = 0
for k in keys:
    if ci[k]['gc_final'] < fp[k]['gc_final']:
        ci_wins += 1
    elif fp[k]['gc_final'] < ci[k]['gc_final']:
        fp_wins += 1
    else:
        ties += 1
print(f'  CI wins: {ci_wins}/30')
print(f'  FP wins: {fp_wins}/30')
print(f'  Tie:     {ties}/30')

print('\n## THANG/THUA: CI vs FPIUS (time)')
ci_wins = fp_wins = ties = 0
for k in keys:
    if ci[k]['time'] < fp[k]['time']:
        ci_wins += 1
    elif fp[k]['time'] < ci[k]['time']:
        fp_wins += 1
    else:
        ties += 1
print(f'  CI wins: {ci_wins}/30')
print(f'  FP wins: {fp_wins}/30')
print(f'  Tie:     {ties}/30')

# ===== 6. Overall summary =====
all_ci_gc = [ci[k]['gc_final'] for k in keys]
all_fp_gc = [fp[k]['gc_final'] for k in keys]
all_ci_t = [ci[k]['time']/1000 for k in keys]
all_fp_t = [fp[k]['time']/1000 for k in keys]

print('\n## TONG KET (30 instances)')
print(f'  CI   gc_final mean: {statistics.mean(all_ci_gc):.0f}')
print(f'  FPIUS gc_final mean: {statistics.mean(all_fp_gc):.0f}')
print(f'  FP/CI cost ratio: {statistics.mean(all_fp_gc)/statistics.mean(all_ci_gc):.2f}')
print(f'  CI   time mean: {statistics.mean(all_ci_t):.1f}s')
print(f'  FPIUS time mean: {statistics.mean(all_fp_t):.1f}s')
print(f'  FP/CI time ratio: {statistics.mean(all_fp_t)/statistics.mean(all_ci_t):.1f}')
