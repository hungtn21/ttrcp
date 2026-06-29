# Giải thích chi tiết `solver/init/FPIUSInit.java`

## 1) Tổng quan vai trò của class

`FPIUSInit` là chiến lược khởi tạo nghiệm ban đầu cho bài toán định tuyến xe tải–romooc–container, theo kiểu:

- **FPIUS = First Possible Insertion with Unscheduled Set**
- Ý tưởng chính: duyệt từng request theo thứ tự, thử chèn vào các route hiện có tại vị trí đầu tiên thỏa ràng buộc.
- Trong lúc thử/chèn request, class còn xử lý việc gắn/tháo romooc tạm thời để kiểm tra tính khả thi.
- Sau khi kết thúc bước khởi tạo, class chuẩn hóa lại romooc trên tất cả route.

Class implement `InitializationStrategy`, nên được gọi qua `initialize(...)` từ solver.

---

## 2) `initialize(TruckContainerSolver solver)`

### Mục đích

Điểm vào chuẩn của chiến lược khởi tạo. Hàm chỉ ủy quyền cho `firstPossibleInitFPIUS`.

### Logic

```java
public void initialize(TruckContainerSolver solver) {
    firstPossibleInitFPIUS(solver);
}
```

- Không chứa logic nghiệp vụ riêng.
- Giữ interface thống nhất để có thể thay thế bằng chiến lược khởi tạo khác.

---

## 3) `firstPossibleInitFPIUS(TruckContainerSolver solver)`

### Mục đích

Tạo nghiệm ban đầu bằng cách:

1. Duyệt từng request pickup/delivery.
2. Thử chèn vào các route ở các vị trí có thể.
3. Chấp nhận phương án đầu tiên không vi phạm ràng buộc.
4. Đánh dấu request không chèn được là rejected.
5. Cuối cùng chèn romooc “ổn định” lại cho toàn bộ route.

### Giải thích từng đoạn

#### Đoạn A: Khởi tạo stack route

```java
Stack<String> stack = new Stack<String>();
for (int r = solver.XR.getNbRoutes(); r >= 1; r--) {
    String s = "" + r;
    stack.push(s);
}
```

- Tạo danh sách route để thử chèn.
- Route id được đẩy vào `stack`.
- Dùng `String` rồi parse lại `int` là cách cũ của code này.

#### Đoạn B: Duyệt từng request

```java
for (int i = 0; i < solver.pickup2Delivery.size(); i++) {
    ...
    Point pickup = solver.pickupPoints.get(i);
    int groupId = solver.point2Group.get(pickup);
    if (solver.XR.route(pickup) != Constants.NULL_POINT || solver.group2marked.get(groupId) == 1)
        continue;
    Point delivery = solver.deliveryPoints.get(i);
```

- Lấy cặp `pickup/delivery` theo index.
- Bỏ qua request đã nằm trên route hoặc group đã được đánh dấu.
- `group2marked` được dùng để quản lý trạng thái group (truck/request/mooc).

#### Đoạn C: Thử chèn request vào từng route theo thứ tự ưu tiên stack

```java
boolean isAdded = false;
for (int k = stack.size() - 1; k >= 0; k--) {
    if (isAdded) break;
    int r = Integer.parseInt(stack.get(k));
    Point st = solver.XR.getStartingPointOfRoute(r);

    int groupTruck = solver.point2Group.get(st);
    if (solver.group2marked.get(groupTruck) == 1 && solver.XR.index(solver.XR.getTerminatingPointOfRoute(r)) <= 1)
        continue;
```

- Duyệt route từ cuối stack về đầu.
- Nếu route thuộc truck đã marked nhưng route rỗng (index điểm cuối <= 1), bỏ qua.
- `isAdded` giúp dừng sớm khi đã chèn thành công.

#### Đoạn D: Duyệt mọi cặp vị trí chèn `(p, q)` trong route

```java
for (Point p = st; p != solver.XR.getTerminatingPointOfRoute(r); p = solver.XR.next(p)) {
    if (isAdded) break;
    for (Point q = p; q != solver.XR.getTerminatingPointOfRoute(r); q = solver.XR.next(q)) {
        solver.mgr.performAddTwoPoints(pickup, p, delivery, q);
        insertMoocToRoutes(solver, r);
```

- `performAddTwoPoints(pickup, p, delivery, q)` nghĩa là:
  - chèn `pickup` sau `p`
  - chèn `delivery` sau `q`
- Gọi `insertMoocToRoutes` để tạm chèn romooc phù hợp, rồi mới kiểm tra ràng buộc.

#### Đoạn E: Kiểm tra khả thi và commit/rollback

```java
if (solver.S.violations() == 0) {
    solver.group2marked.put(groupTruck, 1);
    solver.group2marked.put(groupId, 1);
    stack.remove(stack.get(k));
    String s = "" + r;
    stack.push(s);
    isAdded = true;
    removeMoocOnRoutes(solver, r);
    break;
}
solver.mgr.performRemoveTwoPoints(pickup, delivery);
removeMoocOnRoutes(solver, r);
```

- Nếu không vi phạm:
  - đánh dấu truck group + request group đã dùng.
  - đưa route vừa dùng xuống cuối stack (cơ chế ưu tiên route mới dùng gần đây).
  - **quan trọng:** vẫn gọi `removeMoocOnRoutes` để xóa romooc tạm.
- Nếu vi phạm:
  - rollback pickup/delivery vừa chèn.
  - xóa romooc tạm.

> Điểm then chốt: trong vòng thử nghiệm, romooc được thêm/xóa tạm để kiểm tra feasibility.

#### Đoạn F: Đánh dấu request bị từ chối

```java
for (int i = 0; i < solver.pickup2Delivery.size(); i++) {
    Point pickup = solver.pickupPoints.get(i);
    if (solver.XR.route(pickup) == Constants.NULL_POINT && !solver.rejectPickupPoints.contains(pickup)) {
        solver.rejectPickupPoints.add(pickup);
        solver.rejectDeliveryPoints.add(solver.pickup2Delivery.get(pickup));
    }
}
```

- Request nào vẫn chưa thuộc route sau quá trình chèn thì đưa vào danh sách reject.

#### Đoạn G: Chèn romooc chuẩn cho toàn bộ route

```java
insertMoocForAllRoutes(solver);
```

- Sau khi đã có cấu trúc request trên route, mới gắn romooc lại một cách nhất quán cho tất cả route.

---

## 4) `getBestStartMoocForRequest(...)`

### Mục đích

Chọn điểm bắt đầu romooc (`stMooc`) tốt nhất để chèn trước một request tại vị trí đang xét.

### Logic từng đoạn

1. Khởi tạo `bestMooc = null`, `min_d = +∞`.
2. Duyệt toàn bộ `startMoocPoints`.
3. Bỏ qua mooc đã marked hoặc đã nằm trên route.
4. Tính chi phí xấp xỉ:
   - từ điểm trước vị trí chèn (`p`) -> `stMooc`
   - từ `stMooc` -> điểm pickup đang xét
5. Chọn mooc có tổng chi phí nhỏ nhất.

```java
double d = travel(p, stMooc) + travel(stMooc, pickup);
```

Hàm trả về `Point` start mooc tốt nhất hoặc `null` nếu không có mooc khả dụng.

---

## 5) `insertMoocToRoutes(TruckContainerSolver solver, int r)`

### Mục đích

Trong **một route cụ thể**, chèn romooc tạm để route không thiếu mooc tại các đoạn cần container.

### Logic từng đoạn

#### Đoạn A: Duyệt dọc route

```java
for (Point p = XR.next(st); p != endRoute; p = XR.next(p)) {
    if (accMoocInvr.getSumWeights(XR.prev(p)) <= 0) {
        ...
    }
}
```

- `accMoocInvr` là tải tích lũy mooc.
- Nếu trước điểm `p` mà tải mooc `<= 0`, tức đang thiếu mooc cho đoạn tiếp theo.

#### Đoạn B: Chèn start mooc khi thiếu

```java
stMooc = getBestStartMoocForRequest(...);
if (stMooc == null) continue;
mgr.performAddOnePoint(stMooc, XR.prev(p));
group2marked[groupMooc] = 1;
enMooc = start2stopMoocPoint.get(stMooc);
```

- Chọn mooc gần nhất theo heuristic.
- Chèn `START_MOOC` ngay trước vùng đang thiếu.
- Lấy điểm kết thúc tương ứng `END_MOOC`.

#### Đoạn C: Đóng romooc ở cuối route nếu còn đang mang

```java
if (accMoocInvr.getSumWeights(terminatingPoint) > 0 && enMooc != null) {
    mgr.performAddOnePoint(enMooc, XR.prev(terminatingPoint));
}
```

- Nếu đến cuối route vẫn còn mooc đang mang (`>0`) thì chèn `END_MOOC`.

---

## 6) `removeMoocOnRoutes(TruckContainerSolver solver, int r)`

### Mục đích

Xóa toàn bộ điểm mooc (start/stop) trên route `r` để rollback phần chèn mooc tạm.

### Logic

1. Duyệt từng điểm trên route.
2. Nếu điểm thuộc `startMoocPoints` hoặc `stopMoocPoints`:
   - xóa khỏi route bằng `performRemoveOnePoint`
   - unmark group mooc về `0`.

Hàm này thường đi cùng `insertMoocToRoutes` trong các vòng thử để giữ route “sạch” sau mỗi phương án.

---

## 7) `getBestMoocForRequest(...)`

### Mục đích

Khi route đã có start mooc tạm, hàm này đánh giá có nên đổi sang mooc khác để tổng quãng đường tốt hơn, xét cả điểm bắt đầu và điểm trả mooc.

### Logic từng đoạn

1. Mặc định giữ `bestMooc = curStMooc`.
2. Duyệt mọi `startMoocPoints`.
3. Loại mooc đã bận/đã đánh dấu, **trừ** mooc hiện tại `curStMooc`.
4. Tính chi phí 4 chặng:
   - `p -> stMooc`
   - `stMooc -> np`
   - `q -> enMooc`
   - `enMooc -> nq`
5. Chọn mooc có tổng chi phí nhỏ nhất.

Đây là bước tối ưu cục bộ để đồng thời cải thiện cả vị trí lấy và trả mooc.

---

## 8) `removeAllMoocFromRoutes(TruckContainerSolver solver)`

### Mục đích

Xóa sạch mọi mooc đang nằm trên mọi route trước khi tái chèn đồng bộ.

### Logic

1. Duyệt từng `startMoocPoint`.
2. Lấy `tp` tương ứng qua `start2stopMoocPoint`.
3. Nếu `st` đang nằm trên route -> remove + unmark.
4. Nếu `tp` đang nằm trên route -> remove + unmark.

Hàm dùng nhiều trong ALNS trước các pha destroy/repair để tránh carry-over trạng thái mooc cũ.

---

## 9) `insertMoocForAllRoutes(TruckContainerSolver solver)`

### Mục đích

Sau khi bố trí xong các request, hàm này gắn mooc lại cho **toàn bộ route** một cách nhất quán và có tối ưu nhẹ.

### Logic từng đoạn

#### Đoạn A: Reset trước khi chèn lại

```java
removeAllMoocFromRoutes(solver);
```

- Đảm bảo không còn mooc cũ ảnh hưởng.

#### Đoạn B: Duyệt từng route để chèn start mooc tại các đoạn thiếu

```java
for (r = 1..nbRoutes) {
    ...
    for (p trên route) {
        if (accMoocInvr.getSumWeights(prev(p)) <= 0) {
            stMooc = getBestStartMoocForRequest(...);
            if (stMooc == null) continue;
            preP = prev(p);
            nextP = p;
            add stMooc sau preP;
            mark group mooc;
        }
    }
```

- Tương tự logic tạm thời nhưng ở mức toàn cục.
- Lưu `preP/nextP` để phục vụ bước tối ưu thay mooc.

#### Đoạn C: Nếu cuối route còn đang mang mooc thì tối ưu lựa chọn mooc và chèn điểm trả

```java
if (accMoocInvr.getSumWeights(terminating) > 0) {
    enPoint = prev(terminating);
    newStMooc = getBestMoocForRequest(...);
    if (newStMooc != stMooc) {
        remove stMooc cũ; unmark;
        add newStMooc tại preP; mark;
    }
    enMooc = start2stopMoocPoint.get(newStMooc);
    add enMooc tại enPoint;
}
```

- Có thể thay start mooc hiện tại bằng mooc khác tốt hơn (xét cả đầu/cuối).
- Cuối cùng chèn `END_MOOC` tương ứng.

---

## 10) Tóm tắt cơ chế quan trọng trong file

1. **Chèn request trước, chèn mooc tạm để kiểm tra vi phạm.**
2. **Nếu phương án không đạt thì rollback cả request lẫn mooc tạm.**
3. **Kết thúc khởi tạo, chèn mooc chuẩn hóa cho mọi route.**
4. `group2marked` đóng vai trò “resource lock” cho truck/request/mooc.
5. Heuristic chọn mooc hiện tại dựa trên tổng thời gian di chuyển gần nhất, không phải tối ưu toàn cục.

---

## 11) Lưu ý kỹ thuật / điểm cần cẩn trọng

- `Stack<String>` + parse `int` là thiết kế cũ, không tối ưu type-safety.
- `r` trong `getBestStartMoocForRequest(...)` hiện không dùng.
- Nếu không có mooc khả dụng (`stMooc == null`), thuật toán chỉ `continue`, có thể dẫn đến route không nhận được request ở một số vị trí.
- Việc thêm/xóa mooc liên tục trong quá trình thử giúp giữ tính đúng ràng buộc, nhưng có thể tốn thời gian khi dữ liệu lớn.

