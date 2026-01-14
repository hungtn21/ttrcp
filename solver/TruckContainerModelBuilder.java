package solver;

import constraints.ContainerCapacityConstraint;
import constraints.ContainerCarriedByTrailerConstraint;
import constraints.MoocCapacityConstraint;
import vrp.AccumulatedWeightNodesVR;
import vrp.ConstraintSystemVR;
import vrp.VRManager;
import vrp.VarRoutesVR;
import vrp.entities.LexMultiValues;
import vrp.entities.Point;
import vrp.functions.TotalCostVR;
import vrp.invariants.EarliestArrivalTimeVR;
import vrp.constraints.CEarliestArrivalTimeVR;

public class TruckContainerModelBuilder {
	public void build(TruckContainerSolver solver) {
		createCoreVrpObjects(solver);
		registerRoutes(solver);
		registerClientPoints(solver);
		buildTimeWindows(solver);
		buildAccumulators(solver);
		buildConstraintsAndObjective(solver);
	}

	private void createCoreVrpObjects(TruckContainerSolver solver) {
		solver.mgr = new VRManager();
		solver.XR = new VarRoutesVR(solver.mgr);
		solver.S = new ConstraintSystemVR(solver.mgr);
	}

	private void registerRoutes(TruckContainerSolver solver) {
		for (int i = 0; i < solver.startPoints.size(); ++i)
			solver.XR.addRoute(solver.startPoints.get(i), solver.stopPoints.get(i));
	}

	private void registerClientPoints(TruckContainerSolver solver) {
		for (int i = 0; i < solver.pickupPoints.size(); ++i) {
			Point pickup = solver.pickupPoints.get(i);
			Point delivery = solver.deliveryPoints.get(i);
			solver.XR.addClientPoint(pickup);
			solver.XR.addClientPoint(delivery);
		}
		for (int i = 0; i < solver.startMoocPoints.size(); ++i) {
			solver.XR.addClientPoint(solver.startMoocPoints.get(i));
			solver.XR.addClientPoint(solver.stopMoocPoints.get(i));
		}
	}

	private void buildTimeWindows(TruckContainerSolver solver) {
		solver.eat = new EarliestArrivalTimeVR(solver.XR, solver.awm, solver.earliestAllowedArrivalTime,
				solver.serviceDuration);
		solver.cEarliest = new CEarliestArrivalTimeVR(solver.eat, solver.lastestAllowedArrivalTime);
	}

	private void buildAccumulators(TruckContainerSolver solver) {
		solver.accMoocInvr = new AccumulatedWeightNodesVR(solver.XR, solver.nwMooc);
		solver.accContainerInvr = new AccumulatedWeightNodesVR(solver.XR, solver.nwContainer);
	}

	private void buildConstraintsAndObjective(TruckContainerSolver solver) {
		solver.capContCtr = new ContainerCapacityConstraint(solver.XR, solver.accContainerInvr);
		solver.capMoocCtr = new MoocCapacityConstraint(solver.XR, solver.accMoocInvr);
		solver.contmoocCtr = new ContainerCarriedByTrailerConstraint(solver.XR, solver.accContainerInvr,
				solver.accMoocInvr);

		solver.S.post(solver.cEarliest);
		solver.S.post(solver.capContCtr);
		solver.S.post(solver.capMoocCtr);
		solver.S.post(solver.contmoocCtr);
		solver.objective = new TotalCostVR(solver.XR, solver.awm);
		solver.valueSolution = new LexMultiValues();
		solver.valueSolution.add(solver.S.violations());
		solver.valueSolution.add(solver.objective.getValue());
		solver.mgr.close();
	}
}
