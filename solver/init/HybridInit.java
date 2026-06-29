package solver.init;

import java.util.ArrayList;
import java.util.List;

import solver.TruckContainerSolver;
import vrp.Constants;
import vrp.entities.Point;

public class HybridInit implements InitializationStrategy {

    @Override
    public void initialize(TruckContainerSolver solver) {
        FPIUSInit fpius = new FPIUSInit();

        // Bước 1: Phân loại requests thành "khó" và "dễ"
        List<Integer> hardRequests = new ArrayList<>();
        List<Integer> easyRequests = new ArrayList<>();
        
        double avgTW = computeAvgTimeWindow(solver);
        for (int i = 0; i < solver.pickupPoints.size(); i++) {
            Point p = solver.pickupPoints.get(i);
            Point d = solver.deliveryPoints.get(i);
            double tw = (solver.lastestAllowedArrivalTime.get(p) - solver.earliestAllowedArrivalTime.get(p))
                      + (solver.lastestAllowedArrivalTime.get(d) - solver.earliestAllowedArrivalTime.get(d));
            if (tw < avgTW)
                hardRequests.add(i);  // time window hẹp hơn trung bình
            else
                easyRequests.add(i);
        }

        // Bước 2: Chèn requests "khó" bằng Cheapest Insertion (cẩn thận hơn)
        cheapestInsert(solver, hardRequests, fpius);

        // Bước 3: Chèn requests "dễ" bằng FPIUS gốc (nhanh)
        fpius.firstPossibleInitFPIUS(solver); // FPIUS sẽ tự skip requests đã được chèn

        fpius.insertMoocForAllRoutes(solver);
    }

    private double computeAvgTimeWindow(TruckContainerSolver solver) {
        double sum = 0;
        for (Point p : solver.pickupPoints) {
            sum += solver.lastestAllowedArrivalTime.get(p) 
                 - solver.earliestAllowedArrivalTime.get(p);
        }
        return sum / solver.pickupPoints.size();
    }

    private void cheapestInsert(TruckContainerSolver solver, 
                                 List<Integer> indices, 
                                 FPIUSInit helper) {
        // Sort khó nhất trước
        indices.sort((a, b) -> {
            Point pa = solver.pickupPoints.get(a), da = solver.deliveryPoints.get(a);
            Point pb = solver.pickupPoints.get(b), db = solver.deliveryPoints.get(b);
            double twA = (solver.lastestAllowedArrivalTime.get(pa) - solver.earliestAllowedArrivalTime.get(pa))
                       + (solver.lastestAllowedArrivalTime.get(da) - solver.earliestAllowedArrivalTime.get(da));
            double twB = (solver.lastestAllowedArrivalTime.get(pb) - solver.earliestAllowedArrivalTime.get(pb))
                       + (solver.lastestAllowedArrivalTime.get(db) - solver.earliestAllowedArrivalTime.get(db));
            return Double.compare(twA, twB);
        });

        for (int i : indices) {
            Point pickup   = solver.pickupPoints.get(i);
            Point delivery = solver.deliveryPoints.get(i);
            int groupId    = solver.point2Group.get(pickup);

            if (solver.XR.route(pickup) != Constants.NULL_POINT
                    || solver.group2marked.get(groupId) == 1)
                continue;

            double bestDelta   = Double.MAX_VALUE;
            Point  bestPrePick = null, bestPreDel = null;
            int    bestRoute   = -1;

            for (int r = 1; r <= solver.XR.getNbRoutes(); r++) {
                double baseCost = solver.objective.getValue(); // snapshot trước khi thử
                Point st = solver.XR.getStartingPointOfRoute(r);
                int groupTruck = solver.point2Group.get(st);
                if (solver.group2marked.get(groupTruck) == 1
                        && solver.XR.index(solver.XR.getTerminatingPointOfRoute(r)) <= 1)
                    continue;

                for (Point p = st; p != solver.XR.getTerminatingPointOfRoute(r); p = solver.XR.next(p)) {
                    for (Point q = p; q != solver.XR.getTerminatingPointOfRoute(r); q = solver.XR.next(q)) {
                        solver.mgr.performAddTwoPoints(pickup, p, delivery, q);
                        helper.insertMoocToRoutes(solver, r);
                        if (solver.S.violations() == 0) {
                            double delta = solver.objective.getValue() - baseCost;
                            if (delta < bestDelta) {
                                bestDelta = delta;
                                bestPrePick = p;
                                bestPreDel  = q;
                                bestRoute   = r;
                            }
                        }
                        solver.mgr.performRemoveTwoPoints(pickup, delivery);
                        helper.removeMoocOnRoutes(solver, r);
                    }
                }
            }

            if (bestPrePick != null) {
                solver.mgr.performAddTwoPoints(pickup, bestPrePick, delivery, bestPreDel);
                Point st = solver.XR.getStartingPointOfRoute(bestRoute);
                solver.group2marked.put(solver.point2Group.get(st), 1);
                solver.group2marked.put(groupId, 1);
            }
            // Không add vào rejectList — để FPIUS thử lại ở bước 3
        }
    }
}
