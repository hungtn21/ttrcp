# Giải thích chi tiết các file trong thư mục `constraints`

Tệp này mô tả mục đích, cách hoạt động chính và những chỗ cần chú ý trong từng file hiện có trong thư mục `constraints`.

---

## ContainerCapacityConstraint.java
- Mục đích: Kiểm tra sức chứa (số container) trên từng route và báo vi phạm nếu vượt ngưỡng.
- Tham số (constructor): (VarRoutesVR XR, AccumulatedWeightNodesVR accContainerInvr)
  - XR: biến đại diện cho các route/điểm trong giải pháp hiện tại.
  - accContainerInvr: trợ giúp để lấy "tổng trọng số" (ở đây dùng cho số container) tại các node/điểm và phần tích lũy theo route.
- Hành vi chính:
  - Hàm `propagateAddOnePoint`, `propagateAddTwoPoints` tính lại `violations` cho route chứa điểm đích.
  - `evaluateAddOnePoint` và `evaluateAddTwoPoints` trả về số vi phạm ước tính nếu thêm điểm (dùng để đánh giá move trước khi áp dụng).
  - Ngưỡng hiện đang hard-coded là `2` (nếu wY > 2 thì tính vi phạm = wY - 2).
- Những phương thức còn TODO / chưa triển khai: gần như toàn bộ các phương thức propagate* và evaluate* khác, `getVRManager()` và `name()` trả null. Chỉ một số trường hợp add-point/ add-two-points đã có logic.
- Ghi chú và hướng tiếp cận:
  - Ngưỡng 2 nên đưa thành hằng số cấu hình.
  - Cần triển khai `getVRManager()` để trả XR.getVRManager() nếu muốn hoạt động đúng với framework.
  - Việc nhiều phương thức chưa triển khai nghĩa là constraint hiện chỉ kiểm tra một số move nhất định; để chính xác, phải hoàn thiện tất cả các evaluate*/propagate* cần thiết.

---

## ContainerCarriedByTrailerConstraint.java
- Mục đích: Đảm bảo số container trên trailer (mooc) đủ để mang các container được xếp lên — tức container tổng không vượt quá khả năng mooc (kiểm tra tính tương thích container ⇄ mooc).
- Tham số (constructor): (VarRoutesVR XR, AccumulatedWeightNodesVR accContainerInvr, AccumulatedWeightNodesVR accMoocInvr)
  - accContainerInvr: tổng container yêu cầu dọc route.
  - accMoocInvr: tổng năng lực/khả năng mang của mooc trên đoạn tương ứng.
- Hành vi chính:
  - `propagateAddOnePoint` & `propagateAddTwoPoints` tính `violations` bằng tổng chênh lệch (nếu mooc capacity < container demand) dọc route: if mY1 < cY1 then violations += cY1 - mY1.
  - `evaluateAddOnePoint` cũng đánh giá tăng vi phạm khi thêm điểm, bằng cách cộng weights(x) vào cả accContainer và accMooc tại các điểm sau vị trí thêm.
- Những phương thức còn TODO: hầu hết các propagate*/evaluate* khác cùng `getVRManager()` và `name()` chưa trả giá trị.
- Ghi chú:
  - Logic hiện so sánh tổng container yêu cầu vs khả năng mooc ở từng node trong route; điều này cho thấy mô hình dùng hai accumulator song hành — một cho yêu cầu container và một cho khả năng tải mooc.
  - Một số đoạn validate cho thêm hai điểm bị comment (có thể do đang tinh chỉnh cách lặp qua đoạn route khi chèn 2 điểm).
  - Cần kiểm tra chuẩn hóa units (weights vs counts) để tránh nhầm lẫn kiểu số thực/ nguyên.

---

## MoocCapacityConstraint.java
- Mục đích: Kiểm soát khả năng chứa (capacity) của mooc (trailer) trên mỗi route; nếu vượt ngưỡng sẽ tạo vi phạm.
- Tham số (constructor): (VarRoutesVR XR, AccumulatedWeightNodesVR accMoocInvr)
  - accMoocInvr: accumulator cho trọng lượng/khả năng mooc dọc route.
- Hành vi chính:
  - `propagateAddOnePoint`, `propagateAddTwoPoints` tính `violations` bằng tổng (wY - 2) khi tổng mooc tại các điểm > 2 (ngưỡng 2 hard-coded).
  - `evaluateAddOnePoint` đánh giá vi phạm tương ứng khi thêm điểm (cộng accMoocInvr.getWeights(x)).
- Những phương thức còn TODO: phần lớn các evaluate*/propagate* khác, `getVRManager()`, `name()`.
- Ghi chú:
  - Tương tự `ContainerCapacityConstraint`, ngưỡng là 2 và nên cấu hình lại.
  - Một số evaluateAddTwoPoints có code comment-out — cần xem lại logic chèn hai điểm và ranh giới đoạn route trong vòng lặp.

---

## Điểm chung và cách đọc mã
- Cấu trúc chung: tất cả class implement `IConstraintVR` và đăng ký với `VarRoutesVR` thông qua `XR.getVRManager().post(this)` trong constructor.
- Hai loại hàm cần phân biệt:
  - propagate*: cập nhật trạng thái internal khi một move thực sự được áp dụng (dùng cho propagation incremental).
  - evaluate*: tính chi phí/vi phạm nếu một move được áp dụng (dùng để đánh giá moves trong local search trước khi áp dụng).
- Các lớp phụ thuộc chính: `VarRoutesVR` (truy cập cấu trúc route), `AccumulatedWeightNodesVR` (tính trọng số tích lũy), `Point` (node/điểm trên route).

## Khuyến nghị phát triển / kiểm tra
1. Triển khai `getVRManager()` trả về `XR.getVRManager()` cho từng constraint.
2. Bổ sung `name()` để dễ debug/log.
3. Đưa các ngưỡng (2) thành hằng số hoặc lấy từ cấu hình instance.
4. Hoàn thiện tất cả các `evaluate*` và `propagate*` cần thiết tùy vào các move thuật toán search dùng (2-opt, 3-opt, Or-opt, cross-exchange, ...).
5. Viết unit tests nhỏ cho mỗi constraint:
   - Tạo một VarRoutesVR mẫu, set trường hợp đơn giản (1 route), kiểm tra evaluateAddOnePoint/evaluateAddTwoPoints/propagateAddOnePoint thay đổi `violations` như mong đợi.
6. Xem xét chuẩn hóa tên biến: accMoocInvr/accContainerInvr rõ ràng, và document units (containers vs weight) để tránh nhầm lẫn.

---

Nếu muốn, có thể tiếp theo: (chọn một)
- Tạo và commit file README này vào repo (đã tạo).  
- Mình có thể liệt kê các điểm cụ thể (dòng) nơi cần sửa hoặc tạo các unit test mẫu.  
- Hoặc thực hiện thay đổi nhỏ (ví dụ: implement getVRManager() và name()) ngay bây giờ.

