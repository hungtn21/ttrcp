# Vai trò các thư mục: Constraints, Models, Solver, VRP

Tài liệu ngắn giải thích mục đích và cách tương tác của bốn thư mục chính thường thấy trong một dự án tối ưu hóa vận tải (VRP).

## Constraints
- Chức năng: Chứa các định nghĩa ràng buộc của bài toán (hard/soft constraints).
- Nội dung điển hình: lớp/ hàm mô tả ràng buộc (capacity, time windows, precedence, vehicle eligibility, max distance), validator, chuyển đổi ràng buộc sang biểu diễn mà solver hiểu.
- Ghi chú: Ràng buộc nên được tách biệt theo loại và dễ cấu hình (tham số hóa).

## Models
- Chức năng: Mô tả dữ liệu và cấu trúc toán học của bài toán.
- Nội dung điển hình: lớp/struct cho Node, Vehicle, Route, ProblemInstance; ma trận khoảng cách/thời gian; hàm tính chi phí/mục tiêu; loader/serializer của instance (CSV/JSON).
- Ghi chú: Models là lớp trung tâm — giữ dữ liệu nguyên thủy và cung cấp API để xây dựng biến/biểu thức cho solver.

## Solver
- Chức năng: Cung cấp cách giải bài toán — gọi solver bên ngoài, hoặc triển khai thuật toán (exact, heuristic, metaheuristic).
- Nội dung điển hình: wrappers cho OR-Tools/Gurobi, triển khai Tabu Search / Simulated Annealing / Genetic Algorithm, API cấu hình solver (time limit, seed), parser kết quả, logging, tests hiệu năng.
- Ghi chú: Giữ giao diện trừu tượng để dễ thay đổi engine mà không sửa Models/Constraints.

## VRP
- Chức năng: Chứa logic chuyên biệt cho các biến thể VRP và các entrypoint ứng dụng.
- Nội dung điển hình: triển khai CVRP, VRPTW, PickupAndDelivery; generator dữ liệu thử nghiệm; scripts chạy thí nghiệm/benchmark; ví dụ và notebooks.
- Ghi chú: VRP thường phối hợp Models + Constraints + Solver để tạo pipeline giải quyết một instance cụ thể.

## Mối quan hệ và luồng xử lý
1. Models định nghĩa instance và dữ liệu (nodes, vehicles, ma trận).
2. Constraints áp lên Models để biểu diễn các điều kiện bài toán.
3. Solver nhận Model + Constraints, dịch sang biến/biểu thức phù hợp với engine và chạy tối ưu.
4. VRP tổ chức các biến thể, tạo instance mẫu, và gọi pipeline Model→Constraints→Solver để thu kết quả.

## Gợi ý tổ chức
- Giữ ràng buộc ở dạng khai báo (declarative) để dễ thử nghiệm.
- Tách rõ adapter giữa Models và các solver (giúp thay đổi engine nhanh chóng).
- Cung cấp ví dụ và unit tests cho từng thành phần: mô hình, ràng buộc, solver.

---
(Tệp này được tạo tự động: FOLDERS_ROLES.md)