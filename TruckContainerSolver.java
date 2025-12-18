import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.google.gson.Gson;

import constraints.*;
import models.equipments.*;
import models.input.ContainerTruckMoocInput;
import models.output.StatisticInformation;
import models.output.TruckContainerSolution;
import models.output.TruckMoocContainerOutputJson;
import models.places.*;
import models.requests.*;
import models.routing.RouteElement;
import models.routing.TruckRoute;
import vrp.*;
import vrp.constraints.CEarliestArrivalTimeVR;
import vrp.entities.*;
import vrp.functions.TotalCostVR;
import vrp.invariants.EarliestArrivalTimeVR;
import vrp.utils.DateTimeUtils;

public class TruckContainerSolver {
    public ContainerTruckMoocInput input;
	public DataMapper dataMapper;
	
	ArrayList<Point> points;
	public ArrayList<Point> pickupPoints;
	public ArrayList<Point> deliveryPoints;
	public ArrayList<Point> rejectPickupPoints;
	public ArrayList<Point> rejectDeliveryPoints;
	ArrayList<Point> startPoints;
	ArrayList<Point> stopPoints;
	ArrayList<Point> startMoocPoints;
	ArrayList<Point> stopMoocPoints;
	HashMap<Point, String> point2Type;
	
	public HashMap<Point, Integer> earliestAllowedArrivalTime;
	public HashMap<Point, Integer> serviceDuration;
	public HashMap<Point, Integer> lastestAllowedArrivalTime;
	public HashMap<Point,Point> pickup2DeliveryOfGood;
	public HashMap<Point,Point> pickup2DeliveryOfPeople;
	public HashMap<Point, Point> pickup2Delivery;
	public HashMap<Point,Point> delivery2Pickup;
	
	public HashMap<Point, Point> start2stopMoocPoint;
	public HashMap<Point,Point> stop2startMoocPoint;
	
	public HashMap<Point, Truck> startPoint2Truck;
	public HashMap<Point, Mooc> startPoint2Mooc;
	
	public HashMap<Point, Integer> point2Group;
	public HashMap<Integer, Integer> group2marked;
	public HashMap<Integer, ExportEmptyRequests> group2EE;
	public HashMap<Integer, ExportLadenRequests> group2EL;
	public HashMap<Integer, ImportEmptyRequests> group2IE;
	public HashMap<Integer, ImportLadenRequests> group2IL;
	
	public HashMap<Point, Integer> point2moocWeight;
	public HashMap<Point, Integer> point2containerWeight;
	
	public HashMap<Integer, Point> route2DeliveryMooc;
	
	public static int nVehicle;
	public static int nRequest;
	
	
	public String[] locationCodes;
	public HashMap<String, Integer> mLocationCode2Index;
	public double[][] distance;// distance[i][j] is the distance from location
								// index i to location index j
	public double[][] travelTime;// travelTime[i][j] is the travel time from
									// location index i to location index j
	
	public HashMap<String, Truck> mCode2Truck;
	public HashMap<String, Mooc> mCode2Mooc;
	public HashMap<String, Container> mCode2Container;
	public HashMap<String, DepotContainer> mCode2DepotContainer;
	public HashMap<String, DepotTruck> mCode2DepotTruck;
	public HashMap<String, DepotMooc> mCode2DepotMooc;
	public HashMap<String, Warehouse> mCode2Warehouse;
	public HashMap<String, Port> mCode2Port;
	public ArrayList<Container> additionalContainers;
	
	public ExportEmptyRequests[] exEmptyRequests;
	public ExportLadenRequests[] exLadenRequests;
	public ImportEmptyRequests[] imEmptyRequests;
	public ImportLadenRequests[] imLadenRequests;
	
	ArcWeightsManager awm;
	VRManager mgr;
	VarRoutesVR XR;
	ConstraintSystemVR S;
	IFunctionVR objective;
	CEarliestArrivalTimeVR ceat;
	LexMultiValues valueSolution;
	EarliestArrivalTimeVR eat;
	CEarliestArrivalTimeVR cEarliest;
	ContainerCapacityConstraint capContCtr;
	MoocCapacityConstraint capMoocCtr;
	ContainerCarriedByTrailerConstraint contmoocCtr;
	
	NodeWeightsManager nwMooc;
	NodeWeightsManager nwContainer;
	AccumulatedWeightNodesVR accMoocInvr;
	AccumulatedWeightNodesVR accContainerInvr;
	HashMap<Point, IFunctionVR> accDisF;
	
	HashMap<Point, Integer> nChosed;
	HashMap<Point, Boolean> removeAllowed;
	
	int nRemovalOperators = 8;
	int nInsertionOperators = 8;
	
	//parameters
	public int lower_removal;
	public int upper_removal;
	public float sigma1 = 5;
	public float sigma2 = 1;
	public float sigma3 = (float)0.01;
	public double rp = 0.1;
	public int nw = 1;
	public double shaw1st = 0.5;
	public double shaw2nd = 0.2;
	public double shaw3rd = 0.1;
	public double temperature = 200;
	public double cooling_rate = 0.9995;
	public int nTabu = 5;
	int timeLimit = 36000000;
	int nIter = 30000;
	int maxStable = 1000;
	
	int INF_TIME = Integer.MAX_VALUE;
	public static double MAX_TRAVELTIME;
	public static final String START_TRUCK 	= "START_TRUCK";
	public static final String END_TRUCK 	= "END_TRUCK";
	public static final String START_MOOC 	= "PICKUP_MOOC";
	public static final String END_MOOC 	= "DELIVERY_MOOC";
	public static final String START_CONT 	= "PICKUP_EMPTYCONT";
	public static final String END_CONT 	= "DELIVERY_EMPTYCONT";
	public static final String PORT_PICKUP_EMPTYCONT	= "PORT_PICKUP_EMPTYCONT";
	public static final String PORT_PICKUP_FULLCONT		= "PORT_PICKUP_FULLCONT";
	public static final String PORT_DELIVERY_EMPTYCONT	= "PORT_DELIVERY_EMPTYCONT";
	public static final String PORT_DELIVERY_FULLCONT	= "PORT_DELIVERY_FULLCONT";
	public static final String WH_PICKUP_EMPTYCONT 	= "WH_PICKUP_EMPTYCONT";
	public static final String WH_PICKUP_FULLCONT 	= "WH_PICKUP_FULLCONT";
	public static final String WH_DELIVERY_EMPTYCONT = "WH_DELIVERY_EMPTYCONT";
	public static final String WH_DELIVERY_FULLCONT 	= "WH_DELIVERY_FULLCONT";
	
	public TruckContainerSolver(){
		dataMapper = new DataMapper();
	}
	
	public void loadData() {
		if (dataMapper.getInput() != null) {
			this.input = dataMapper.getInput();
			this.locationCodes = dataMapper.locationCodes;
			this.mLocationCode2Index = dataMapper.mLocationCode2Index;
			this.distance = dataMapper.distance;
			this.travelTime = dataMapper.travelTime;
			this.mCode2Truck = dataMapper.mCode2Truck;
			this.mCode2Mooc = dataMapper.mCode2Mooc;
			this.mCode2Container = dataMapper.mCode2Container;
			this.mCode2DepotContainer = dataMapper.mCode2DepotContainer;
			this.mCode2DepotTruck = dataMapper.mCode2DepotTruck;
			this.mCode2DepotMooc = dataMapper.mCode2DepotMooc;
			this.mCode2Warehouse = dataMapper.mCode2Warehouse;
			this.mCode2Port = dataMapper.mCode2Port;
			this.additionalContainers = dataMapper.additionalContainers;
		}
	}

	public int getTravelTime(String src, String dest) {
		if (mLocationCode2Index.get(src) == null
				|| mLocationCode2Index.get(dest) == null) {
			 System.out.println("::getTravelTime, src " + src +
			 " OR dest " + dest + " NOT COMPLETE, INPUT ERROR??????");
			//return 1000;

		}

		int is = mLocationCode2Index.get(src);
		int id = mLocationCode2Index.get(dest);
		return (int) travelTime[is][id];
	}
	
	public void init(){
		new TruckContainerInitializer().init(this);
	}
	// Test if initialization is correct
	// /**
	//  * Runtime sanity checks to verify that {@link #init()} finished successfully.
	//  * Throws a descriptive exception on the first detected inconsistency.
	//  */
	// public void sanityCheckInit() {
	// 	if (input == null) {
	// 		throw new IllegalStateException("sanityCheckInit: input is null (did you call loadData()?)");
	// 	}
	// 	if (mLocationCode2Index == null || travelTime == null) {
	// 		throw new IllegalStateException("sanityCheckInit: travel-time matrix not loaded (did you call loadData()?)");
	// 	}
	// 	if (points == null || points.isEmpty()) {
	// 		throw new IllegalStateException("sanityCheckInit: points is null/empty after init()");
	// 	}
	// 	if (pickupPoints == null || deliveryPoints == null || pickup2Delivery == null || delivery2Pickup == null) {
	// 		throw new IllegalStateException("sanityCheckInit: pickup/delivery collections not initialized");
	// 	}
	// 	if (pickupPoints.size() != deliveryPoints.size()) {
	// 		throw new IllegalStateException("sanityCheckInit: pickupPoints.size != deliveryPoints.size ("
	// 				+ pickupPoints.size() + " vs " + deliveryPoints.size() + ")");
	// 	}
	// 	if (point2Type == null || point2Group == null || earliestAllowedArrivalTime == null || serviceDuration == null
	// 			|| lastestAllowedArrivalTime == null || point2moocWeight == null || point2containerWeight == null) {
	// 		throw new IllegalStateException("sanityCheckInit: core point maps not initialized");
	// 	}
	// 	if (awm == null || nwMooc == null || nwContainer == null) {
	// 		throw new IllegalStateException("sanityCheckInit: weight managers not initialized");
	// 	}

	// 	for (Point p : points) {
	// 		if (p == null) {
	// 			throw new IllegalStateException("sanityCheckInit: null point in points list");
	// 		}
	// 		String lc = p.getLocationCode();
	// 		if (lc == null) {
	// 			throw new IllegalStateException("sanityCheckInit: point " + p.ID + " has null locationCode");
	// 		}
	// 		if (!mLocationCode2Index.containsKey(lc)) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckInit: locationCode '" + lc + "' not found in mLocationCode2Index (point " + p.ID + ")");
	// 		}
	// 		if (!point2Type.containsKey(p)) {
	// 			throw new IllegalStateException("sanityCheckInit: missing point2Type for point " + p.ID);
	// 		}
	// 		if (!point2Group.containsKey(p)) {
	// 			throw new IllegalStateException("sanityCheckInit: missing point2Group for point " + p.ID);
	// 		}
	// 		if (!earliestAllowedArrivalTime.containsKey(p) || !serviceDuration.containsKey(p)
	// 				|| !lastestAllowedArrivalTime.containsKey(p)) {
	// 			throw new IllegalStateException("sanityCheckInit: missing time-window data for point " + p.ID);
	// 		}
	// 		if (!point2moocWeight.containsKey(p) || !point2containerWeight.containsKey(p)) {
	// 			throw new IllegalStateException("sanityCheckInit: missing weight data for point " + p.ID);
	// 		}
	// 	}

	// 	// Verify pickup-delivery bijection on the constructed pickup/delivery sets
	// 	for (Point pu : pickupPoints) {
	// 		Point del = pickup2Delivery.get(pu);
	// 		if (del == null) {
	// 			throw new IllegalStateException("sanityCheckInit: pickup has no mapped delivery, pickup point " + pu.ID);
	// 		}
	// 		Point back = delivery2Pickup.get(del);
	// 		if (back != pu) {
	// 			throw new IllegalStateException("sanityCheckInit: delivery2Pickup mismatch for pickup " + pu.ID
	// 					+ " (delivery " + del.ID + ")");
	// 		}
	// 	}

	// 	if (MAX_TRAVELTIME <= 0) {
	// 		throw new IllegalStateException("sanityCheckInit: MAX_TRAVELTIME not computed (" + MAX_TRAVELTIME + ")");
	// 	}
	// }

	public void stateModel(){
		new TruckContainerModelBuilder().build(this);
	}

	// Test if VRP model construction is correct
	/**
	 * Runtime sanity checks to verify that {@link #stateModel()} finished successfully.
	 * Throws a descriptive exception on the first detected inconsistency.
	 */
	// public void sanityCheckStateModel() {
	// 	if (mgr == null || XR == null || S == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: mgr/XR/S is null (did you call stateModel()?)");
	// 	}
	// 	if (mgr.getVarRoutesVR() != XR) {
	// 		throw new IllegalStateException("sanityCheckStateModel: VRManager does not reference XR");
	// 	}
	// 	if (S.getVRManager() != mgr) {
	// 		throw new IllegalStateException("sanityCheckStateModel: ConstraintSystemVR does not reference mgr");
	// 	}
	// 	if (startPoints == null || stopPoints == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: startPoints/stopPoints not initialized (did init() run?)");
	// 	}
	// 	if (XR.getNbRoutes() != startPoints.size()) {
	// 		throw new IllegalStateException(
	// 				"sanityCheckStateModel: XR route count mismatch (XR=" + XR.getNbRoutes() + ", expected="
	// 						+ startPoints.size() + ")");
	// 	}

	// 	// Ensure all required points were registered into XR
	// 	for (int i = 0; i < startPoints.size(); i++) {
	// 		Point sp = startPoints.get(i);
	// 		Point tp = stopPoints.get(i);
	// 		if (XR.getIndex(sp) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException("sanityCheckStateModel: startPoint not registered in XR (id=" + sp.ID + ")");
	// 		}
	// 		if (XR.getIndex(tp) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException("sanityCheckStateModel: stopPoint not registered in XR (id=" + tp.ID + ")");
	// 		}
	// 	}
	// 	for (int i = 0; i < pickupPoints.size(); i++) {
	// 		Point pu = pickupPoints.get(i);
	// 		Point de = deliveryPoints.get(i);
	// 		if (XR.getIndex(pu) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckStateModel: pickup point not registered in XR (id=" + pu.ID + ")");
	// 		}
	// 		if (XR.getIndex(de) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckStateModel: delivery point not registered in XR (id=" + de.ID + ")");
	// 		}
	// 	}
	// 	for (int i = 0; i < startMoocPoints.size(); i++) {
	// 		Point sm = startMoocPoints.get(i);
	// 		Point em = stopMoocPoints.get(i);
	// 		if (XR.getIndex(sm) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckStateModel: startMoocPoint not registered in XR (id=" + sm.ID + ")");
	// 		}
	// 		if (XR.getIndex(em) == Constants.NULL_POINT) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckStateModel: stopMoocPoint not registered in XR (id=" + em.ID + ")");
	// 		}
	// 	}

	// 	if (eat == null || cEarliest == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: time-window invariants/constraints not initialized");
	// 	}
	// 	if (accMoocInvr == null || accContainerInvr == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: accumulators not initialized");
	// 	}
	// 	if (capContCtr == null || capMoocCtr == null || contmoocCtr == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: capacity/trailer constraints not initialized");
	// 	}
	// 	if (objective == null) {
	// 		throw new IllegalStateException("sanityCheckStateModel: objective is null");
	// 	}
	// 	if (valueSolution == null || valueSolution.size() < 2) {
	// 		throw new IllegalStateException("sanityCheckStateModel: valueSolution not initialized (size="
	// 				+ (valueSolution == null ? "null" : valueSolution.size()) + ")");
	// 	}
	// 	double obj = objective.getValue();
	// 	if (Double.isNaN(obj) || Double.isInfinite(obj)) {
	// 		throw new IllegalStateException("sanityCheckStateModel: objective value is invalid: " + obj);
	// 	}
	// 	// Constraints may be violated at this stage; we only check it is well-defined.
	// 	int vio = S.violations();
	// 	if (vio < 0) {
	// 		throw new IllegalStateException("sanityCheckStateModel: violations is negative: " + vio);
	// 	}
	// }

	public void firstPossibleInitFPIUS(){
		new TruckContainerInitialSolutionBuilder().firstPossibleInitFPIUS(this);
	}

	private static void writeStartInfo(String outputFileTxt) {
		try {
			FileOutputStream write = new FileOutputStream(outputFileTxt);
			PrintWriter fo = new PrintWriter(write);
			fo.println("Starting time = " + DateTimeUtils.unixTimeStamp2DateTime(System.currentTimeMillis() / 1000)
					+ ", total reqs = " + nRequest + ", total truck = " + nVehicle);
			fo.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// // Test if writeStartInfo wrote a valid header line
	// public static void sanityCheckWriteStartInfo(String outputFileTxt) {
	// 	Path p = Path.of(outputFileTxt);
	// 	if (!Files.exists(p)) {
	// 		throw new IllegalStateException("sanityCheckWriteStartInfo: output file not found: " + outputFileTxt);
	// 	}
	// 	try {
	// 		String line = Files.readAllLines(p, StandardCharsets.UTF_8).stream().findFirst().orElse("");
	// 		if (line == null || line.trim().isEmpty()) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckWriteStartInfo: first line is empty in output file: " + outputFileTxt);
	// 		}
	// 		if (!line.startsWith("Starting time = ")) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckWriteStartInfo: unexpected header format, got: '" + line + "'");
	// 		}
	// 		if (!line.contains(", total reqs = ") || !line.contains(", total truck = ")) {
	// 			throw new IllegalStateException(
	// 					"sanityCheckWriteStartInfo: missing counters in header, got: '" + line + "'");
	// 		}
	// 	} catch (Exception e) {
	// 		throw new IllegalStateException("sanityCheckWriteStartInfo: cannot read/validate file: " + outputFileTxt, e);
	// 	}
	// }

	// // Test if firstPossibleInitFPIUS built an initial solution consistently
	// public void sanityCheckFirstPossibleInitFPIUS() {
	// 	if (XR == null || mgr == null || S == null) {
	// 		throw new IllegalStateException(
	// 				"sanityCheckFirstPossibleInitFPIUS: XR/mgr/S is null (did you call stateModel()?)");
	// 	}
	// 	if (pickupPoints == null || deliveryPoints == null || pickup2Delivery == null) {
	// 		throw new IllegalStateException("sanityCheckFirstPossibleInitFPIUS: pickup/delivery not initialized");
	// 	}
	// 	if (rejectPickupPoints == null || rejectDeliveryPoints == null) {
	// 		throw new IllegalStateException("sanityCheckFirstPossibleInitFPIUS: reject lists not initialized");
	// 	}
	// 	if (rejectPickupPoints.size() != rejectDeliveryPoints.size()) {
	// 		throw new IllegalStateException(
	// 				"sanityCheckFirstPossibleInitFPIUS: rejectPickupPoints.size != rejectDeliveryPoints.size ("
	// 						+ rejectPickupPoints.size() + " vs " + rejectDeliveryPoints.size() + ")");
	// 	}

	// 	// Each request is either routed (pickup+delivery on some route) or rejected
	// 	for (int i = 0; i < pickupPoints.size(); i++) {
	// 		Point pu = pickupPoints.get(i);
	// 		Point de = deliveryPoints.get(i);
	// 		int rPu = XR.route(pu);
	// 		int rDe = XR.route(de);

	// 		if (rPu != Constants.NULL_POINT || rDe != Constants.NULL_POINT) {
	// 			if (rPu == Constants.NULL_POINT || rDe == Constants.NULL_POINT) {
	// 				throw new IllegalStateException("sanityCheckFirstPossibleInitFPIUS: pickup/delivery route mismatch ("
	// 						+ pu.ID + "->" + de.ID + ")");
	// 			}
	// 			if (rPu != rDe) {
	// 				throw new IllegalStateException("sanityCheckFirstPossibleInitFPIUS: pickup/delivery on different routes ("
	// 						+ pu.ID + " in " + rPu + ", " + de.ID + " in " + rDe + ")");
	// 			}
	// 			if (rejectPickupPoints.contains(pu) || rejectDeliveryPoints.contains(de)) {
	// 				throw new IllegalStateException(
	// 						"sanityCheckFirstPossibleInitFPIUS: routed request appears in reject lists ("
	// 								+ pu.ID + "->" + de.ID + ")");
	// 			}
	// 		} else {
	// 			// If unrouted, should be rejected (as in the end of firstPossibleInitFPIUS)
	// 			if (!rejectPickupPoints.contains(pu)) {
	// 				throw new IllegalStateException(
	// 						"sanityCheckFirstPossibleInitFPIUS: unrouted pickup not present in reject list ("
	// 								+ pu.ID + ")");
	// 			}
	// 			Point mappedDelivery = pickup2Delivery.get(pu);
	// 			if (mappedDelivery != null && !rejectDeliveryPoints.contains(mappedDelivery)) {
	// 				throw new IllegalStateException(
	// 						"sanityCheckFirstPossibleInitFPIUS: rejectDeliveryPoints missing mapped delivery (pickup " + pu.ID
	// 								+ ", delivery " + mappedDelivery.ID + ")");
	// 			}
	// 		}
	// 	}

	// 	// Mooc points should be inserted in pairs (start and its corresponding stop)
	// 	if (startMoocPoints != null && stopMoocPoints != null && start2stopMoocPoint != null) {
	// 		for (int i = 0; i < startMoocPoints.size(); i++) {
	// 			Point st = startMoocPoints.get(i);
	// 			Point tp = start2stopMoocPoint.get(st);
	// 			if (tp == null)
	// 				continue;
	// 			boolean stInRoute = XR.route(st) != Constants.NULL_POINT;
	// 			boolean tpInRoute = XR.route(tp) != Constants.NULL_POINT;
	// 			if (stInRoute != tpInRoute) {
	// 				throw new IllegalStateException(
	// 						"sanityCheckFirstPossibleInitFPIUS: mooc start/stop not paired in routes (start " + st.ID
	// 								+ " inRoute=" + stInRoute + ", stop " + tp.ID + " inRoute=" + tpInRoute + ")");
	// 			}
	// 		}
	// 	}
	// }

	// public void printInitialSolutionSummary(String label, int maxRoutesToPrint) {
	// 	if (XR == null) {
	// 		System.out.println("printInitialSolutionSummary: XR is null (did you call stateModel()?)");
	// 		return;
	// 	}
	// 	int totalRoutes = XR.getNbRoutes();
	// 	int routesToPrint = Math.max(0, Math.min(maxRoutesToPrint, totalRoutes));
	// 	int rejected = (rejectPickupPoints == null) ? -1 : rejectPickupPoints.size();
	// 	int violations = (S == null) ? -1 : S.violations();
	// 	double obj = (objective == null) ? Double.NaN : objective.getValue();
	// 	System.out.println("=== Initial solution: " + label + " ===");
	// 	System.out.println(
	// 			"routes=" + totalRoutes + ", clients=" + XR.getNbClients() + ", rejected=" + rejected + ", violations="
	// 					+ violations + ", objective=" + obj);
	// 	for (int r = 1; r <= routesToPrint; r++) {
	// 		Point tp = XR.getTerminatingPointOfRoute(r);
	// 		int sz = (tp == null) ? -1 : (XR.index(tp) == Constants.NULL_POINT ? -1 : Math.max(0, XR.index(tp) - 1));
	// 		System.out.println("route[" + r + "] size=" + sz + " :: " + XR.routeString(r));
	// 	}
	// 	if (routesToPrint < totalRoutes) {
	// 		System.out.println("... (" + (totalRoutes - routesToPrint) + " more routes not printed)");
	// 	}
	// }

	public void initParamsForALNS(){
		new TruckContainerALNSRunner(this).initParamsForALNS();
	}

	public int getNbUsedTrucks(){
		return new TruckContainerALNSRunner(this).getNbUsedTrucks();
	}
	
	public int getNbRejectedRequests(){
		return new TruckContainerALNSRunner(this).getNbRejectedRequests();
	}
	
	public void adaptiveSearchOperators(String outputfile){	
		new TruckContainerALNSRunner(this).adaptiveSearchOperators(outputfile);
	}
	
	public void printSolution(String outputfile){
		String s = "";

		int K = XR.getNbRoutes();
		int cost = 0;
		for(int k=1; k<=K; k++){
			s += "route[" + k + "] = ";
			Point x = XR.getStartingPointOfRoute(k);
			for(; x != XR.getTerminatingPointOfRoute(k); x = XR.next(x)){
				s = s + x.getLocationCode() + " (" + point2Type.get(x) + ") -> ";
//				System.out.println("p1 = " + x.getLocationCode()
//						+ ", p2 = " + XR.next(x).getLocationCode() 
//						+ ", cost = " + awm.getDistance(x, XR.next(x)));
			}
			x = XR.getTerminatingPointOfRoute(k);
			s = s + x.getLocationCode()  + " (" + point2Type.get(x) + ")" + "\n";
		}		
		System.out.println(s);
		
		
		int nbR = getNbRejectedRequests();
		int nB = getNbUsedTrucks();
		System.out.println("Search done. At end search number of reject points = " + nbR
				+ ", nb Trucks = " + nB
				+ ",  cost = " + objective.getValue());
		long t = System.currentTimeMillis();
		try{
			FileOutputStream write = new FileOutputStream(outputfile, true);
			PrintWriter fo = new PrintWriter(write);
			fo.println(s);
			fo.println("end time = " + DateTimeUtils.unixTimeStamp2DateTime(t/1000)
					+ ", #RejectedReqs = " + nbR
					+ ", nb Trucks = " + nB
					+ ", cost = " + objective.getValue());
			
			fo.close();
		}catch(Exception e){
			
		}
		
	}
	
	public TruckMoocContainerOutputJson createFormatedSolution() {
		ArrayList<TruckRoute> brArr = new ArrayList<TruckRoute>();

		int nbTrucks = 0;
		for (int r = 1; r <= XR.getNbRoutes(); r++) {
			int nb = XR.index(XR.getTerminatingPointOfRoute(r)) + 1;
			Truck truck = startPoint2Truck.get(XR.getStartingPointOfRoute(r));
			
			if(nb <= 2)
				continue;
			
			double d = 0;
			int nbPers = 0;
			Point st = XR.getStartingPointOfRoute(r);
			Point en = XR.getTerminatingPointOfRoute(r);
			
			int g = 0;
			RouteElement[] nodes = new RouteElement[nb];

			for(Point p = st; p != XR
					.getTerminatingPointOfRoute(r); p = XR.next(p)) {			

				nodes[g] = new RouteElement(p.getLocationCode(), point2Type.get(p),
						DateTimeUtils.unixTimeStamp2DateTime((long)(eat.getEarliestArrivalTime(p))),
						DateTimeUtils.unixTimeStamp2DateTime((long)(eat.getEarliestArrivalTime(p) + serviceDuration.get(p))), 
						(int)awm.getWeight(p, XR.next(p)));
				g++;
			}
			
			

			nodes[g] = new RouteElement(XR.getTerminatingPointOfRoute(r).getLocationCode(),
					point2Type.get(XR.getTerminatingPointOfRoute(r)),
					DateTimeUtils.unixTimeStamp2DateTime((long)eat.getEarliestArrivalTime(en)),
					DateTimeUtils.unixTimeStamp2DateTime((long)(eat.getEarliestArrivalTime(en) + serviceDuration.get(en))), 0);
			
			TruckRoute br = new TruckRoute(truck, nb, (int)objective.getValue(), nodes);
			brArr.add(br);
			nbTrucks++;
		}
		
		TruckRoute[] truckRoutes = new TruckRoute[brArr.size()];
		for(int i = 0; i < brArr.size(); i++)
			truckRoutes[i] = brArr.get(i);
		
		HashSet<ExportEmptyRequests> unscheduledEE = new HashSet<ExportEmptyRequests>();
		HashSet<ExportLadenRequests> unscheduledEL = new HashSet<ExportLadenRequests>();
		HashSet<ImportEmptyRequests> unscheduledIE = new HashSet<ImportEmptyRequests>();
		HashSet<ImportLadenRequests> unscheduledIL = new HashSet<ImportLadenRequests>();
		int nbRejects = 0;
		for(int i = 0; i < rejectPickupPoints.size(); i++){
			int groupId = point2Group.get(rejectPickupPoints.get(i));
			if(group2marked.get(groupId) == 0
					&& group2EE.get(groupId) != null)
				unscheduledEE.add(group2EE.get(groupId));
			else if(group2marked.get(groupId) == 0
					&& group2EL.get(groupId) != null)
				unscheduledEL.add(group2EL.get(groupId));
			else if(group2marked.get(groupId) == 0
					&& group2IE.get(groupId) != null)
				unscheduledIE.add(group2IE.get(groupId));
			else if(group2marked.get(groupId) == 0
					&& group2IL.get(groupId) != null)
				unscheduledIL.add(group2IL.get(groupId));
		}
		ExportEmptyRequests[] ee = new ExportEmptyRequests[unscheduledEE.size()];
		ExportLadenRequests[] el = new ExportLadenRequests[unscheduledEL.size()];
		ImportEmptyRequests[] ie = new ImportEmptyRequests[unscheduledIE.size()];
		ImportLadenRequests[] il = new ImportLadenRequests[unscheduledIL.size()];

		int i = 0;
		for(ExportEmptyRequests r : unscheduledEE){
			ee[i] = r;
			i++;
		}
		i = 0;
		for(ImportEmptyRequests r : unscheduledIE){
			ie[i] = r;
			i++;
		}
		i = 0;
		for(ExportLadenRequests r : unscheduledEL){
			el[i] = r;
			i++;
		}
		i=0;
		for(ImportLadenRequests r : unscheduledIL){
			il[i] = r;
			i++;
		}
		int totalRejectReqs = unscheduledEE.size() + unscheduledEL.size()
				+ unscheduledIE.size() + unscheduledIL.size();
		
		StatisticInformation statisticInformation = new StatisticInformation(
				this.nRequest,totalRejectReqs, objective.getValue(), nbTrucks);
		
		return new TruckMoocContainerOutputJson(truckRoutes, 
				ee, el, ie, il, statisticInformation);
	}
	
	
    public static void main(String[] args){
		int[] nbReq = new int[]{20};
		for(int k = 0; k < 1; k++){
			for(int i = 0; i < 1; i++){
				for(int j = 0; j < nbReq.length; j++){
					String dir = "data/truck-container/";
			
					String fileName = "random-" + nbReq[j] + "reqs-RealLoc-" + i;
					String dataFileName = dir + "input/" + fileName + ".txt";
					
					String outputALNSfileTxt = dir + "output/newOutput/It-" + k +"-ALNS-" + fileName + ".txt";
					String outputALNSfileJson = dir + "output/newOutput/It-" + k +"-ALNS-" + fileName + ".json";
					
					TruckContainerSolver solver = new TruckContainerSolver();
					solver.dataMapper.readData(dataFileName);
					solver.loadData();
					
					// Test if data loaded successfully
					// if (solver.input != null) {
					// 	System.out.println("Data loaded successfully!");
					// 	System.out.println("Number of trucks: " + solver.input.getTrucks().length);
					// 	System.out.println("Number of containers: " + solver.input.getContainers().length);
					// 	System.out.println("Number of locations: " + solver.locationCodes.length);
					// } else {
					// 	System.out.println("Failed to load data!");
					// }

					solver.init();
					
					// Test if initialization completed successfully
					// solver.sanityCheckInit();
					// System.out.println("init OK: " + fileName + " | points=" + solver.points.size()
					// 		+ ", pickups=" + solver.pickupPoints.size() + ", MAX_TRAVELTIME=" + MAX_TRAVELTIME);
                
					solver.stateModel();

					// Test if VRP model constructed successfully
					// solver.sanityCheckStateModel();
					// System.out.println("stateModel OK: " + fileName + " | routes=" + solver.XR.getNbRoutes()
					// 		+ ", clients=" + solver.XR.getNbClients() + ", violations=" + solver.S.violations()
					// 		+ ", objective=" + solver.objective.getValue());

					writeStartInfo(outputALNSfileTxt);

					// Test if writeStartInfo wrote a valid header line
					// sanityCheckWriteStartInfo(outputALNSfileTxt);

					solver.firstPossibleInitFPIUS();

					// Test if initial solution built successfully
					// solver.sanityCheckFirstPossibleInitFPIUS();
					// System.out.println("firstPossibleInitFPIUS OK: " + fileName);
					// solver.printInitialSolutionSummary(fileName, 5);

					solver.timeLimit = 3600000;
					solver.nIter = 100;
					
					solver.nRemovalOperators = 8;
					solver.nInsertionOperators = 8;
					
					solver.lower_removal = (int) 0.01*nRequest;
					solver.upper_removal = (int) 0.25*nRequest;
					solver.sigma1 = 5;
					solver.sigma2 = 1;
					solver.sigma3 = (int)0.01;
					
					solver.rp = 0.1;
					solver.nw = 1;
					solver.shaw1st = 0.5;
					solver.shaw2nd = 0.2;
					solver.	shaw3rd = 0.1;
			
					solver.temperature = 200;
					solver.cooling_rate = 0.9995;
					solver.nTabu = 5;

					solver.adaptiveSearchOperators(outputALNSfileTxt);
					solver.printSolution(outputALNSfileTxt);

					Gson g = new Gson();
					TruckMoocContainerOutputJson solution = solver.createFormatedSolution();
					try{
						String out = g.toJson(solution);
						BufferedWriter writer = new BufferedWriter(new FileWriter(outputALNSfileJson));
					    writer.write(out);
					     
					    writer.close();
					}catch(Exception e){
						System.out.println(e);
					}
				}
            }
        }
    }
}
