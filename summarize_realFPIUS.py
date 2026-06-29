import re
from pathlib import Path

base_dir = Path(r"data/truck-container/output/newOutput")

files = sorted(base_dir.glob("It-0-ALNS-realFPIUSrandom-*reqs-RealLoc-*.txt"),
               key=lambda p: p.name)

rows = []

for fp in files:
    m = re.match(r"It-0-ALNS-realFPIUSrandom-(\d+)reqs-RealLoc-(\d+)\.txt", fp.name)
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

    # iter 0
    parts0 = data_lines[0].split()
    init_time_ms = int(parts0[3])
    init_nbReject = int(parts0[5])
    init_nbTrucks = int(parts0[6])
    init_cost = float(parts0[4])
    init_elapsed_ms = init_time_ms - start_ms

    # iter 101
    iter101 = None
    for dl in data_lines:
        if dl.startswith("101 "):
            iter101 = dl.split()
            break
    if iter101 is None:
        iter101 = data_lines[-1].split()
    final_iter_time_ms = int(iter101[3])

    # end summary line
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

lines_out = []
lines_out.append("# FPIUSInit + ALNS - Summary\n")
lines_out.append("|   x  | y  | gr_init | gt_init | gc_init | gr_final | gt_final | gc_final | init_time (ms) | time (ms) |")
lines_out.append("|------|----|--------:|--------:|--------:|---------:|---------:|---------:|---------------:|----------:|")

for r in rows:
    x, y, init_r, init_t, init_c, fin_r, fin_t, fin_c, elapsed, init_elapsed = r
    lines_out.append(
        f"| {x:>4} | {y:>2} | {init_r:>7} | {init_t:>7} | {init_c:>7.0f} | {fin_r:>8} | {fin_t:>8} | {fin_c:>8.0f} | {init_elapsed:>14} | {elapsed:>10} |"
    )

result = "\n".join(lines_out)
out_path = base_dir / "summary_realFPIUS.md"
with open(out_path, 'w', encoding='utf-8') as f:
    f.write(result)

print(result)
print(f"\nSaved to: {out_path}")
