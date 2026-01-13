import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;

import constraints.*;
import models.equipments.*;
import models.input.ContainerTruckMoocInput;
import models.output.StatisticInformation;
import models.output.TruckMoocContainerOutputJson;
import models.places.*;
import models.requests.*;
import models.routing.RouteElement;
import models.routing.TruckRoute;
import vrp.*;
import vrp.constraints.CEarliestArrivalTimeVR;
import vrp.entities.*;
import vrp.invariants.EarliestArrivalTimeVR;
import vrp.utils.DateTimeUtils;

public class TruckContainerSolver {
    public ContainerTruckMoocInput input;
	public DataMapper dataMapper;
	
	// Strategy pattern for algorithms
	private InitializationStrategy initializationStrategy;
	private OptimizationStrategy optimizationStrategy;
	
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
		// Set default strategies
		this.initializationStrategy = new TruckContainerInitialSolutionBuilder();
		this.optimizationStrategy = new TruckContainerALNSRunner(this);
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

	public void stateModel(){
		new TruckContainerModelBuilder().build(this);
	}

	/**
	 * Sets the initialization strategy.
	 * 
	 * @param strategy The initialization strategy to use
	 */
	public void setInitializationStrategy(InitializationStrategy strategy) {
		this.initializationStrategy = strategy;
	}
	
	/**
	 * Sets the optimization strategy.
	 * 
	 * @param strategy The optimization strategy to use
	 */
	public void setOptimizationStrategy(OptimizationStrategy strategy) {
		this.optimizationStrategy = strategy;
	}
	
	/**
	 * Initializes the solution using the configured initialization strategy.
	 * Default strategy is FPIUS (set in constructor).
	 */
	public void initializeSolution() {
		initializationStrategy.initialize(this);
	}
	
	/**
	 * @deprecated Use initializeSolution() with strategy pattern instead.
	 * This method is kept for backward compatibility.
	 */
	@Deprecated
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

	public void initParamsForALNS(){
		new TruckContainerALNSRunner(this).initParamsForALNS();
	}

	public int getNbUsedTrucks(){
		return new TruckContainerALNSRunner(this).getNbUsedTrucks();
	}
	
	public int getNbRejectedRequests(){
		return new TruckContainerALNSRunner(this).getNbRejectedRequests();
	}
	
	/**
	 * Optimizes the solution using the configured optimization strategy.
	 * Default strategy is ALNS (set in constructor).
	 * 
	 * @param outputfile Path to the output file for logging results
	 */
	public void optimizeSolution(String outputfile) {
		optimizationStrategy.optimize(this, outputfile);
	}
	
	/**
	 * @deprecated Use optimizeSolution() with strategy pattern instead.
	 * This method is kept for backward compatibility.
	 */
	@Deprecated
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
					
					solver.init();
					
					solver.stateModel();

					writeStartInfo(outputALNSfileTxt);

					solver.setInitializationStrategy(new TruckContainerInitialSolutionBuilder());
					solver.initializeSolution();
					
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

					solver.setOptimizationStrategy(new TruckContainerALNSRunner(solver));
					solver.optimizeSolution(outputALNSfileTxt);
					
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
