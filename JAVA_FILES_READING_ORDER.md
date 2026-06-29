# Danh sách thứ tự đọc các file Java - Dự án TTRCP VRP

Tài liệu này liệt kê tất cả file Java, sắp xếp theo thứ tự logic để học (base entities trước, phụ thuộc sau), và ước tính thời gian đọc từng file.

---

## 📋 Nguyên tắc xếp hạng

- **Tier 1 (BASE)**: Enum, hằng số, entity cơ bản — chỉ import java.* standard, không import từ project khác
- **Tier 2 (DEPENDENCIES)**: Phụ thuộc Tier 1 — import từ Point, Intervals, Container, v.v.
- **Tier 3 (INTERFACES)**: Interface định nghĩa hành vi — InvariantVR, IConstraintVR, IFunctionVR
- **Tier 4 (CORE LOGIC)**: VarRoutesVR, ValueRoutesVR — quản lý tuyến đường
- **Tier 5 (MANAGERS)**: VRManager, ConstraintSystemVR, AccumulatedWeightNodesVR — tổng hợp/phối hợp

---

## 🔴 TIER 1: BASE ENTITIES (Đọc trước — Không import project)

| # | File | Dòng | Import project | Độ khó | Thời gian | Ghi chú |
|----|------|------|---|---|---|---|
| 1 | `vrp/enums/PointType.java` | 7 | 0 | ⭐ | **1 phút** | Enum (DEPOT, CUSTOMER) |
| 2 | `vrp/Constants.java` | 6 | 0 | ⭐ | **1 phút** | Hằng số |
| 3 | `vrp/Intervals.java` | 30 | 0 | ⭐ | **2 phút** | Khoảng thời gian (start, end) |
| 4 | `vrp/Checkin.java` | 30 | 0 | ⭐ | **2 phút** | Thông tin nhân viên check-in |
| 5 | `vrp/entities/Point.java` | 98 | 1 | ⭐⭐ | **5 phút** | ⭐ QUAN TRỌNG: Node cơ bản (id, x, y, weight, ...) |
| 6 | `models/places/Port.java` | 29 | 0 | ⭐ | **1 phút** | Cảng (properties cơ bản) |
| 7 | `models/places/DepotTruck.java` | 30 | 0 | ⭐ | **1 phút** | Kho xe tải |
| 8 | `models/places/DepotMooc.java` | 49 | 0 | ⭐ | **2 phút** | Kho mooc/trailer |
| 9 | `models/places/DepotContainer.java` | 52 | 0 | ⭐ | **2 phút** | Kho container |
| 10 | `models/places/ShipCompany.java` | 29 | 0 | ⭐ | **1 phút** | Công ty vận chuyển |
| 11 | `models/equipments/Container.java` | 84 | 0 | ⭐⭐ | **2 phút** | Container (id, size, weight) |
| 12 | `models/equipments/MoocPacking.java` | 35 | 0 | ⭐ | **1 phút** | Thông tin xếp hàng mooc |
| 13 | `models/routing/RouteElement.java` | 58 | 0 | ⭐ | **2 phút** | Phần tử route (Point + metadata) |

**Subtotal Tier 1: ~25 phút**

---

## 🟠 TIER 2: DEPENDENCIES CƠ BẢN (Phụ thuộc Tier 1)

| # | File | Dòng | Import | Độ khó | Thời gian | Phụ thuộc |
|----|------|------|--------|---|---|---|
| 14 | `vrp/IDistanceManager.java` | 7 | Point | ⭐ | **1 phút** | Interface: lấy khoảng cách |
| 15 | `vrp/entities/NodeWeightsManager.java` | 48 | Point | ⭐⭐ | **5 phút** | Quản lý trọng lượng từng node |
| 16 | `vrp/entities/ArcWeightsManager.java` | 45 | Point, IDistanceManager | ⭐⭐ | **5 phút** | Quản lý trọng lượng cạnh (arc) |
| 17 | `vrp/CBLSVR.java` | 13 | Point | ⭐ | **2 phút** | Hằng số CBL, utility functions |
| 18 | `vrp/entities/LexMultiValues.java` | 105 | CBLSVR | ⭐⭐⭐ | **10 phút** | So sánh Lexicographic — PHỨC TẠP |
| 19 | `models/equipments/Mooc.java` | 107 | Intervals | ⭐⭐ | **5 phút** | Trailer/Mooc (capacity, weight, time windows) |
| 20 | `models/equipments/Truck.java` | 126 | Intervals | ⭐⭐ | **5 phút** | Xe tải (capacity, cost, time windows) |
| 21 | `models/equipments/MoocGroup.java` | 44 | MoocPacking | ⭐ | **2 phút** | Nhóm mooc |
| 22 | `models/places/Warehouse.java` | 86 | Checkin, Intervals | ⭐⭐ | **5 phút** | Kho hàng (delivery info) |
| 23 | `models/routing/TruckRoute.java` | 49 | Truck, RouteElement | ⭐ | **2 phút** | Tuyến đường xe tải |

**Subtotal Tier 2: ~42 phút**

---

## 🟡 TIER 3: INTERFACES & INVARIANTS (Core VRP Framework)

**⚠️ ĐỌC KỸ NHỮNG FILE NÀY — CHÚNG ĐỊNH NGHĨA HÀNH VI**

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 24 | `vrp/InvariantVR.java` | 235 | Point, ArrayList | ⭐⭐⭐ | **15 phút** | Interface: bất biến/invariant của route — 50+ phương thức |
| 25 | `vrp/IConstraintVR.java` | 196 | Point, ArrayList | ⭐⭐⭐ | **15 phút** | Interface: ràng buộc — 60+ phương thức (propagate*, evaluate*) |
| 26 | `vrp/IFunctionVR.java` | 345 | Point, ArrayList | ⭐⭐⭐⭐ | **20 phút** | Interface: hàm mục tiêu — 30+ phương thức, FILE LỚN |

**Subtotal Tier 3: ~50 phút**

---

## 🔵 TIER 4: CENTRAL ROUTE MANAGEMENT (Lõi VRP)

**⚠️ FILE CỰC KỲ PHỨC TẠP — CẦN TẬP TRUNG CAO ĐỘ**

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 27 | `vrp/VarRoutesVR.java` | **1983** | Point, ArrayList, HashMap, HashSet, Random, PointType | ⭐⭐⭐⭐⭐ | **20+ phút** | 🔴 **FILE NÀY RẤT LỚN & PHỨC TẠP** — Quản lý tuyến đường: next[], prev[], route[], index[], pointType[]; perform*Move() methods (1-opt, 2-opt, 3-opt, Or-opt, Cross-exchange) |
| 28 | `vrp/ValueRoutesVR.java` | 59 | VarRoutesVR, HashMap, Point | ⭐⭐ | **5 phút** | Bản sao giá trị để lưu trạng thái |

**Subtotal Tier 4: ~25 phút**

---

## 🟢 TIER 5: MANAGERS & SYSTEMS (Tổng hợp & Phối hợp)

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 29 | `vrp/VRManager.java` | 420 | ArrayList, Iterator, VarRoutesVR, InvariantVR, ConstraintSystemVR | ⭐⭐⭐ | **15 phút** | Điều phối toàn bộ: invariants, constraints, functions |
| 30 | `vrp/AccumulatedWeightNodesVR.java` | 355 | ArrayList, HashMap, HashSet, NodeWeightsManager, VarRoutesVR, InvariantVR | ⭐⭐⭐ | **15 phút** | Implement InvariantVR: tính trọng lượng tích lũy (accumulation) |
| 31 | `vrp/ConstraintSystemVR.java` | 469 | ArrayList, Point, IConstraintVR, VRManager | ⭐⭐⭐ | **15 phút** | Implement IConstraintVR: quản lý hệ thống ràng buộc |
| 32 | `vrp/invariants/EarliestArrivalTimeVR.java` | (?) | VarRoutesVR, InvariantVR, Intervals | ⭐⭐⭐ | **15 phút** | Invariant: thời gian đến sớm nhất |
| 33 | `vrp/constraints/CEarliestArrivalTimeVR.java` | (?) | VarRoutesVR, IConstraintVR, Intervals | ⭐⭐⭐ | **15 phút** | Constraint: kiểm tra time windows |

**Subtotal Tier 5: ~75 phút**

---

## 🟣 TIER 6: MODELS & REQUESTS (Dữ liệu đầu vào)

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 34 | `models/input/ConfigParam.java` | (?) | (?) | ⭐⭐ | **5 phút** | Cấu hình tham số |
| 35 | `models/input/DistanceElement.java` | (?) | (?) | ⭐ | **2 phút** | Phần tử ma trận khoảng cách |
| 36 | `models/input/ContainerTruckMoocInput.java` | (?) | (?) | ⭐⭐ | **10 phút** | Input: Container, Truck, Mooc |
| 37 | `models/requests/ImportContainerRequest.java` | (?) | (?) | ⭐ | **2 phút** | Request nhập container |
| 38 | `models/requests/ExportContainerRequest.java` | (?) | (?) | ⭐ | **2 phút** | Request xuất container |
| 39 | `models/requests/ImportLadenRequests.java` | (?) | (?) | ⭐ | **2 phút** | Request nhập hàng |
| 40 | `models/requests/ExportLadenRequests.java` | (?) | (?) | ⭐ | **2 phút** | Request xuất hàng |
| 41 | `models/requests/ImportEmptyRequests.java` | (?) | (?) | ⭐ | **2 phút** | Request nhập container rỗng |
| 42 | `models/requests/ExportEmptyRequests.java` | (?) | (?) | ⭐ | **2 phút** | Request xuất container rỗng |
| 43 | `models/requests/WarehouseTransportRequest.java` | (?) | (?) | ⭐⭐ | **5 phút** | Request vận chuyển kho |
| 44 | `models/requests/WarehouseContainerTransportRequest.java` | (?) | (?) | ⭐⭐ | **5 phút** | Request vận chuyển container kho |
| 45 | `models/requests/ImportContainerTruckMoocRequest.java` | (?) | (?) | ⭐⭐ | **5 phút** | Request nhập container/truck/mooc |
| 46 | `models/requests/ExportContainerTruckMoocRequest.java` | (?) | (?) | ⭐⭐ | **5 phút** | Request xuất container/truck/mooc |
| 47 | `models/requests/PickupWarehouseInfo.java` | (?) | (?) | ⭐ | **2 phút** | Thông tin pickup kho |
| 48 | `models/requests/DeliveryWarehouseInfo.java` | (?) | (?) | ⭐ | **2 phút** | Thông tin delivery kho |

**Subtotal Tier 6: ~60 phút**

---

## 🟣 TIER 7: SOLVERS & OPTIMIZERS (Thuật toán giải pháp)

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 49 | `solver/TruckContainerModelBuilder.java` | (?) | (?) | ⭐⭐⭐ | **15 phút** | Xây dựng model từ input |
| 50 | `solver/TruckContainerInitializer.java` | (?) | (?) | ⭐⭐ | **10 phút** | Khởi tạo solution ban đầu |
| 51 | `solver/TruckContainerSolver.java` | (?) | (?) | ⭐⭐⭐ | **15 phút** | Solver chính |
| 52 | `solver/DataMapper.java` | (?) | (?) | ⭐⭐ | **10 phút** | Map dữ liệu input → model |
| 53 | `solver/InputAnalyzer.java` | (?) | (?) | ⭐⭐ | **10 phút** | Phân tích input |
| 54 | `solver/init/InitializationStrategy.java` | (?) | (?) | ⭐⭐ | **5 phút** | Interface: chiến lược khởi tạo |
| 55 | `solver/init/FPIUSInit.java` | (?) | (?) | ⭐⭐⭐ | **15 phút** | Thuật toán khởi tạo FPIUS |
| 56 | `solver/opt/OptimizationStrategy.java` | (?) | (?) | ⭐⭐ | **5 phút** | Interface: chiến lược tối ưu |
| 57 | `solver/opt/SearchOptimumSolution.java` | (?) | (?) | ⭐⭐⭐ | **15 phút** | Tìm kiếm giải pháp tối ưu |
| 58 | `solver/opt/ALNS.java` | (?) | (?) | ⭐⭐⭐ | **20 phút** | Adaptive Large Neighborhood Search |

**Subtotal Tier 7: ~120 phút**

---

## 🟣 TIER 8: OUTPUT & CONSTRAINTS (Kết quả & Constraints chi tiết)

| # | File | Dòng | Import | Độ khó | Thời gian | Ghi chú |
|----|------|------|--------|---|---|---|
| 59 | `models/output/TruckContainerSolution.java` | (?) | (?) | ⭐⭐ | **10 phút** | Solution result |
| 60 | `models/output/StatisticInformation.java` | (?) | (?) | ⭐ | **5 phút** | Thống kê |
| 61 | `models/output/TruckMoocContainerOutputJson.java` | (?) | (?) | ⭐⭐ | **10 phút** | Output JSON |
| 62 | `constraints/ContainerCapacityConstraint.java` | 451 | VarRoutesVR, AccumulatedWeightNodesVR, IConstraintVR, Point | ⭐⭐⭐ | **15 phút** | Constraint: dung tích container |
| 63 | `constraints/ContainerCarriedByTrailerConstraint.java` | 462 | VarRoutesVR, AccumulatedWeightNodesVR (x2), IConstraintVR, Point | ⭐⭐⭐ | **15 phút** | Constraint: container trên trailer |
| 64 | `constraints/MoocCapacityConstraint.java` | 452 | VarRoutesVR, AccumulatedWeightNodesVR, IConstraintVR, Point | ⭐⭐⭐ | **15 phút** | Constraint: dung tích mooc |

**Subtotal Tier 8: ~85 phút**

---

## 📊 TỔNG HỢP THỜI GIAN

| Tier | Tên | Thời gian | Ghi chú |
|------|-----|----------|--------|
| 1 | BASE ENTITIES | ~25 phút | ✅ ĐỌC TRƯỚC TIÊN |
| 2 | DEPENDENCIES CƠ BẢN | ~42 phút | ✅ ĐỌC THỨ HAI |
| 3 | INTERFACES | ~50 phút | ✅ ĐỌC THỨ BA (định nghĩa hành vi) |
| 4 | CORE ROUTE MANAGEMENT | ~25 phút | ⚠️ ĐỦ PHỨC TẠP — VarRoutesVR là file lớn (1983 dòng) |
| 5 | MANAGERS | ~75 phút | ✅ ĐỌC SAU TIER 4 |
| 6 | MODELS & REQUESTS | ~60 phút | 🔄 Có thể đọc song song Tier 5 |
| 7 | SOLVERS | ~120 phút | 🔄 Nặng, đọc cuối |
| 8 | OUTPUT & CONSTRAINTS | ~85 phút | 🔄 Nặng, đọc cuối |
| | **TỔNG CỘNG** | **~482 phút (~8 giờ)** | **Khuyến cáo: 2-3 ngày, 3-4 giờ/ngày** |

---

## 🎯 RECOMMENDED READING PLAN

### **Ngày 1: KIẾN THỨC CƠ BẢN (Tier 1+2) — ~65 phút**
1. PointType, Constants, Intervals, Checkin (7 phút)
2. Point, Container, RouteElement, Warehouse, Truck, Mooc (27 phút)
3. Port, Depot*, MoocGroup, TruckRoute (7 phút)
4. NodeWeightsManager, ArcWeightsManager, LexMultiValues (20 phút)
5. IDistanceManager, CBLSVR (3 phút)

✅ **Mục tiêu**: Hiểu cấu trúc dữ liệu & entity cơ bản

---

### **Ngày 2: FRAMEWORK VRP (Tier 3+4) — ~75 phút**
1. InvariantVR, IConstraintVR, IFunctionVR (50 phút) ← ĐỌC KỸ
2. VarRoutesVR (20+ phút) ← **FILE LỚN NHẤT**
3. ValueRoutesVR (5 phút)

✅ **Mục tiêu**: Hiểu interface định nghĩa hành vi + quản lý route

---

### **Ngày 3: MANAGERS & SYSTEMS (Tier 5+6) — ~135 phút**
1. VRManager, AccumulatedWeightNodesVR, ConstraintSystemVR (45 phút)
2. EarliestArrivalTimeVR, CEarliestArrivalTimeVR (30 phút)
3. ConfigParam, ContainerTruckMoocInput, các Request classes (40 phút)
4. Các Warehouse request classes (20 phút)

✅ **Mục tiêu**: Hiểu phối hợp giữa invariants, constraints, và dữ liệu input

---

### **Ngày 4+: SOLVERS & DETAILS (Tier 7+8) — ~205 phút**
1. InputAnalyzer, DataMapper, TruckContainerModelBuilder (35 phút)
2. InitializationStrategy, FPIUSInit (20 phút)
3. OptimizationStrategy, SearchOptimumSolution, ALNS (50 phút)
4. ContainerCapacityConstraint, ContainerCarriedByTrailerConstraint, MoocCapacityConstraint (45 phút)
5. Solution & Output classes (15 phút)

✅ **Mục tiêu**: Hiểu thuật toán giải pháp & ràng buộc cụ thể

---

## ⚠️ NHỮNG FILE KHIÊN "NGÁO"

| File | Vấn đề | Lời khuyên |
|------|--------|----------|
| `VarRoutesVR.java` | 1983 dòng, 50+ phương thức move (2-opt, 3-opt, Or-opt, Cross-exchange) | Đọc từng section (perform1-opt, perform2-opt, etc.), vẽ sơ đồ |
| `IFunctionVR.java` | 345 dòng, 30+ phương thức | In ra, đánh dấu nhóm phương thức tương tự |
| `IConstraintVR.java` | 196 dòng, 60+ phương thức | Nhóm: propagate* vs evaluate* |
| `InvariantVR.java` | 235 dòng, 50+ phương thức | Nhóm: tính toán vs query |
| `ConstraintSystemVR.java` | 469 dòng, phối hợp tất cả constraint | Đọc kỹ cách nó gọi từng constraint |

---

## ✅ TIPS ĐỌC HIỆU QUẢ

1. **In ra code** — dễ ghi chú và vẽ sơ đồ
2. **Vẽ dependency graph** — giúp hiểu cách các class kết nối
3. **Tạo bảng so sánh** — ví dụ: so sánh performTwoOptMove1 vs performTwoOptMove2
4. **Thử tạo UML diagram** — class diagram cho Tier 1-5
5. **Code review nhỏ** — sau mỗi tier, viết lại 1-2 method từ trí nhớ

---

**Tệp này được tạo tự động: JAVA_FILES_READING_ORDER.md**
