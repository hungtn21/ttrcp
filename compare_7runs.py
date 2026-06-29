import re, statistics
from pathlib import Path

base = Path(r'data/truck-container/output/newOutput')
files = {
    'CI1': base / 'summary_cheapestinsertioninit.md',
    'CI2': base / 'summary_cheapestinsertioninit_v2.md',
    'CI3': base / 'summary_ci3.md',
    'CI4': base / 'summary_ci4.md',
    'CI5': base / 'summary_ci5.md',
    'CI6': base / 'summary_ci6.md',
    'CI7': base / 'summary_ci7.md',
}

def read_summary(fp):
    rows = {}
    with open(fp) as f:
        for line in f:
            m = re.match(r'\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)\s*\|\s*([\d.]+)', line)
            if m:
                x, y = int(m.group(1)), int(m.group(2))
                rows[(x,y)] = {
                    'gc_final': float(m.group(8)),
                    'gr_final': int(m.group(6)),
                    'gt_final': int(m.group(7)),
                    'time': int(m.group(10)),
                    'init_t': int(m.group(9)),
                }
    return rows

all_data = {}
for label, fp in files.items():
    if fp.exists():
        all_data[label] = read_summary(fp)

keys = sorted(all_data['CI1'].keys())
labels = list(all_data.keys())

# ===== COST comparison =====
print('## COST (gc_final): min / median / mean / max / std')
print('|   x  | y  |    min | median |   mean |    max |   std | spread% | best run(s) |')
print('|------|----|-------:|-------:|-------:|-------:|------:|--------:|-------------|')
for k in keys:
    vals = [all_data[l][k]['gc_final'] for l in labels]
    mn, md, avg, mx = min(vals), statistics.median(vals), statistics.mean(vals), max(vals)
    std = statistics.stdev(vals) if len(vals) > 1 else 0
    spread = (mx - mn) / md * 100 if md > 0 else 0
    best = [l for l in labels if abs(all_data[l][k]['gc_final'] - mn) < 1]
    print('| {:>4} | {:>2} | {:>6.0f} | {:>6.0f} | {:>6.0f} | {:>6.0f} | {:>5.0f} | {:>6.0f}% | {} |'.format(
        k[0], k[1], mn, md, avg, mx, std, spread, ','.join(best)))

# ===== Per-size cost stats =====
print('\n## COST trung binh theo co du lieu (mean +- std)')
print('|   x  |    CI1 |    CI2 |    CI3 |    CI4 |    CI5 |    CI6 |    CI7 | overall mean +- std |')
print('|------|-------:|-------:|-------:|-------:|-------:|-------:|-------:|--------------------:|')
for x in sorted(set(k[0] for k in keys)):
    row = '| {:>4} |'.format(x)
    all_vals = []
    for l in labels:
        vals = [all_data[l][k]['gc_final'] for k in keys if k[0]==x]
        row += ' {:>7.0f} |'.format(sum(vals)/len(vals))
        all_vals.extend(vals)
    row += ' {:>7.0f} +- {:>5.0f} |'.format(statistics.mean(all_vals), statistics.stdev(all_vals))
    print(row)

# ===== TIME comparison =====
print('\n## THOI GIAN (giay): min / median / mean / max')
print('|   x  | y  |    min | median |   mean |    max |   std | best run(s) |')
print('|------|----|-------:|-------:|-------:|-------:|------:|-------------|')
for k in keys:
    vals = [all_data[l][k]['time']/1000 for l in labels]
    mn, md, avg, mx = min(vals), statistics.median(vals), statistics.mean(vals), max(vals)
    std = statistics.stdev(vals) if len(vals) > 1 else 0
    best = [l for l in labels if abs(all_data[l][k]['time']/1000 - mn) < 0.5]
    print('| {:>4} | {:>2} | {:>6.1f} | {:>6.1f} | {:>6.1f} | {:>6.1f} | {:>5.1f} | {} |'.format(
        k[0], k[1], mn, md, avg, mx, std, ','.join(best)))

# ===== Overall time per size =====
print('\n## THOI GIAN trung binh theo co (giay)')
print('|   x  |  CI1 |  CI2 |  CI3 |  CI4 |  CI5 |  CI6 |  CI7 | overall median |')
print('|------|-----:|-----:|-----:|-----:|-----:|-----:|-----:|---------------:|')
for x in sorted(set(k[0] for k in keys)):
    row = '| {:>4} |'.format(x)
    all_vals = []
    for l in labels:
        vals = [all_data[l][k]['time']/1000 for k in keys if k[0]==x]
        row += ' {:>5.1f} |'.format(sum(vals)/len(vals))
        all_vals.extend(vals)
    row += ' {:>6.1f} |'.format(statistics.median(all_vals))
    print(row)

# ===== GR/GT differences =====
print('\n## gr_final / gt_final khac biet giua 7 lan chay')
diffs = []
for k in keys:
    grs = set(all_data[l][k]['gr_final'] for l in labels)
    gts = set(all_data[l][k]['gt_final'] for l in labels)
    if len(grs) > 1 or len(gts) > 1:
        diffs.append((k, grs, gts))
if diffs:
    for k, grs, gts in diffs:
        print('  x={} y={}: gr_final = {}, gt_final = {}'.format(k[0], k[1], sorted(grs), sorted(gts)))
    print('  Total: {}/30 instances differ'.format(len(diffs)))
else:
    print('  All identical across 7 runs')

# ===== Win count =====
print('\n## Best-cost wins per run')
wins = {l: 0 for l in labels}
ties = 0
for k in keys:
    vals = [(all_data[l][k]['gc_final'], l) for l in labels]
    mn = min(v[0] for v in vals)
    best_runs = [v[1] for v in vals if abs(v[0] - mn) < 1]
    if len(best_runs) == 1:
        wins[best_runs[0]] += 1
    else:
        ties += 1
for l in labels:
    print('  {}: {} wins'.format(l, wins[l]))
print('  tie: {}'.format(ties))
