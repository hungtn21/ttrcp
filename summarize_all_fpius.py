import re, statistics
from pathlib import Path

base_dir = Path(r"data/truck-container/output/newOutput")

# Run definitions: label -> file prefix
runs = {
    'FPIUS1': 'realFPIUS',
    'FPIUS2': '2FPIUS',
    'FPIUS3': '3FPIUS',
    'FPIUS4': '4FPIUS',
    'FPIUS5': '5FPIUS',
    'FPIUS6': '6FPIUS',
    'FPIUS7': '7FPIUS',
    'FPIUS8': '8FPIUS',
    'FPIUS9': '9FPIUS',
    'FPIUS10': '10FPIUS',
}

def extract_data(prefix):
    """Parse all txt files for a given prefix, return list of (x, y, ...) tuples."""
    pattern = re.compile(rf"It-0-ALNS-{re.escape(prefix)}random-(\d+)reqs-RealLoc-(\d+)\.txt")
    rows = []
    for fp in sorted(base_dir.glob(f"It-0-ALNS-{prefix}random-*reqs-RealLoc-*.txt")):
        m = pattern.match(fp.name)
        if not m:
            continue
        x = int(m.group(1))
        y = int(m.group(2))

        with open(fp, 'r') as f:
            lines = f.readlines()

        start_ms_match = re.search(r'ms=(\d+)', lines[0])
        start_ms = int(start_ms_match.group(1)) if start_ms_match else 0

        data_lines = []
        for line in lines:
            stripped = line.strip()
            if re.match(r'^\d+\s+-?\d+\s+-?\d+\s+\d{10,}\s+[\d.]+', stripped):
                data_lines.append(stripped)

        parts0 = data_lines[0].split()
        init_time_ms = int(parts0[3])
        init_nbReject = int(parts0[5])
        init_nbTrucks = int(parts0[6])
        init_cost = float(parts0[4])
        init_elapsed_ms = init_time_ms - start_ms

        iter101 = None
        for dl in data_lines:
            if dl.startswith("101 "):
                iter101 = dl.split()
                break
        if iter101 is None:
            iter101 = data_lines[-1].split()
        final_iter_time_ms = int(iter101[3])

        end_line = None
        for line in reversed(lines):
            if line.strip():
                end_line = line.strip()
                break

        final_rejected = int(re.search(r'#RejectedReqs\s*=\s*(\d+)', end_line).group(1))
        final_trucks = int(re.search(r'nb\s*Trucks\s*=\s*(\d+)', end_line).group(1))
        final_cost = float(re.search(r'cost\s*=\s*([\d.]+)', end_line).group(1))

        elapsed_ms = final_iter_time_ms - start_ms

        rows.append((x, y, init_nbReject, init_nbTrucks, init_cost,
                     final_rejected, final_trucks, final_cost, elapsed_ms, init_elapsed_ms))

    rows.sort(key=lambda r: (r[0], r[1]))
    return rows

def write_summary_md(rows, label, out_path):
    """Write a single-run summary markdown file."""
    lines_out = []
    lines_out.append(f"# FPIUSInit + ALNS - {label} Summary\n")
    lines_out.append("|   x  | y  | gr_init | gt_init | gc_init | gr_final | gt_final | gc_final | init_time (ms) | time (ms) |")
    lines_out.append("|------|----|--------:|--------:|--------:|---------:|---------:|---------:|---------------:|----------:|")

    for r in rows:
        x, y, init_r, init_t, init_c, fin_r, fin_t, fin_c, elapsed, init_elapsed = r
        lines_out.append(
            f"| {x:>4} | {y:>2} | {init_r:>7} | {init_t:>7} | {init_c:>7.0f} | {fin_r:>8} | {fin_t:>8} | {fin_c:>8.0f} | {init_elapsed:>14} | {elapsed:>10} |"
        )

    result = "\n".join(lines_out)
    with open(out_path, 'w', encoding='utf-8') as f:
        f.write(result)
    print(f"Saved: {out_path}")

# ===== Step 1: Generate individual summaries for runs 2-10 =====
# Run 1 already exists as summary_realFPIUS.md
all_data = {}
all_data['FPIUS1'] = {}

# Read existing FPIUS1 summary
fp1_path = base_dir / "summary_realFPIUS.md"
with open(fp1_path, 'r') as f:
    for line in f:
        m = re.match(r'\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*([\d.]+)\s*\|\s*(\d+)\s*\|\s*(\d+)\s*\|\s*([\d.]+)\s*\|\s*(\d+)\s*\|\s*(\d+)', line)
        if m:
            x, y = int(m.group(1)), int(m.group(2))
            all_data['FPIUS1'][(x, y)] = {
                'gc_init': float(m.group(5)),
                'gr_init': int(m.group(3)),
                'gt_init': int(m.group(4)),
                'gc_final': float(m.group(8)),
                'gr_final': int(m.group(6)),
                'gt_final': int(m.group(7)),
                'time': int(m.group(10)),
                'init_t': int(m.group(9)),
            }

for label, prefix in runs.items():
    if label == 'FPIUS1':
        continue  # already done

    rows = extract_data(prefix)
    all_data[label] = {}
    for r in rows:
        x, y = r[0], r[1]
        all_data[label][(x, y)] = {
            'gc_init': r[4],
            'gr_init': r[2],
            'gt_init': r[3],
            'gc_final': r[7],
            'gr_final': r[5],
            'gt_final': r[6],
            'time': r[8],
            'init_t': r[9],
        }

    v = int(label.replace('FPIUS', ''))
    out_path = base_dir / f"summary_realFPIUS_v{v}.md"
    write_summary_md(rows, label, out_path)

# ===== Step 2: Create aggregated 10-run mean summary =====
keys = sorted(all_data['FPIUS1'].keys())
labels = list(all_data.keys())

lines_out = []
lines_out.append("# FPIUSInit + ALNS - Tong hop 10 lan chay (mean)\n")
lines_out.append("|   x  | y  | gr_init | gt_init | gc_init | gr_final | gt_final | gc_final | init_time (ms) | time (ms) |")
lines_out.append("|------|----|--------:|--------:|--------:|---------:|---------:|---------:|---------------:|----------:|")

for k in keys:
    gr_init_mean = statistics.mean([all_data[l][k]['gr_init'] for l in labels])
    gt_init_mean = statistics.mean([all_data[l][k]['gt_init'] for l in labels])
    gc_init_mean = statistics.mean([all_data[l][k]['gc_init'] for l in labels])
    gr_final_mean = statistics.mean([all_data[l][k]['gr_final'] for l in labels])
    gt_final_mean = statistics.mean([all_data[l][k]['gt_final'] for l in labels])
    gc_final_mean = statistics.mean([all_data[l][k]['gc_final'] for l in labels])
    time_mean = int(statistics.mean([all_data[l][k]['time'] for l in labels]))
    init_t_mean = int(statistics.mean([all_data[l][k]['init_t'] for l in labels]))

    lines_out.append(
        f"| {k[0]:>4} | {k[1]:>2} | {gr_init_mean:>7.1f} | {gt_init_mean:>7.1f} | {gc_init_mean:>7.0f} | {gr_final_mean:>8.1f} | {gt_final_mean:>8.1f} | {gc_final_mean:>8.0f} | {init_t_mean:>14} | {time_mean:>10} |"
    )

result = "\n".join(lines_out)
out_path = base_dir / "summary_realFPIUS_10runs.md"
with open(out_path, 'w', encoding='utf-8') as f:
    f.write(result)
print(f"\nSaved aggregated: {out_path}")
print(result)
