import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

// ========== vrp.enums.PointType ==========
enum PointType {
	STARTING_ROUTE,
	TERMINATING_ROUTE,
	CLIENT
}

// ========== vrp.AccumulatedWeightNodesVR ==========
class AccumulatedWeightNodesVR implements InvariantVR {

	protected VarRoutesVR XR;
	protected VRManager mgr;
	protected NodeWeightsManager nwm;
	
	protected double[] sumWeights;
	protected HashMap<Point, Integer> map;
	
	public AccumulatedWeightNodesVR(VarRoutesVR XR, NodeWeightsManager nwm){
		this.nwm = nwm;
		this.XR = XR;
		this.mgr = XR.getVRManager();
		post();
	}
	
	private void post(){
		sumWeights = new double[XR.getTotalNbPoints()];
		map = new HashMap<Point, Integer>();
		ArrayList<Point> points = XR.getAllPoints();
		for (int i = 0; i < points.size(); i++) {
			map.put(points.get(i), i);
		}
		for(int k= 1; k <= XR.getNbRoutes(); k++){
			Point p = XR.startPoint(k);
			sumWeights[getIndex(p)] = nwm.getWeight(p);
		}
		
		mgr.post(this);
	}
	
	protected int getIndex(Point p) {
		return map.get(p);
	}
	
	public double getWeights(Point p){
		return nwm.getWeight(p);
	}
	public double getSumWeights(Point p){
		return sumWeights[getIndex(p)];
	}
	public VarRoutesVR getVarRoutesVR(){
		return this.XR;
	}
	
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return mgr;
	}
	
	public String name() {
		return "AccumulatedWeightNodesVR";
	}
	public void setAccumulatedWeightStartPoint(int k, double w){
		Point sp = XR.startPoint(k);
		nwm.setWeight(sp, w);		
		sumWeights[getIndex(sp)] = w;
	}
	// update sumWeight of points of route k
    protected void update(int k) {
    	//System.out.println(name() + "::update(" + k + ")");
    	Point sp = XR.getStartingPointOfRoute(k);
        Point tp = XR.getTerminatingPointOfRoute(k);
        //sumWeights[getIndex(sp)] = nwm.getWeight(sp);
        for (Point u = sp; u != tp; u = XR.next(u)){
        	sumWeights[getIndex(XR.next(u))] = sumWeights[getIndex(u)] + nwm.getWeight(XR.next(u));
        }
    }
    
	
	public void initPropagation() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= XR.getNbRoutes(); i++) {
			update(i);
		}
	}

	// x is before y on the same route
	// remove (x, next[x]) and (y,next[y])
	// add (x,y) and (next[x],next[y])
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		System.out.println(name() + "::propagateTwoOptMoveOneRoute NOT IMPLEMENTED YET");
		System.exit(-1);
	}

	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x)); 
    	oldR.add(XR.oldRoute(y));
    	for (int r : oldR) {
    		update(r);
    	}
	
	}

	
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		if (XR.next(x) == y) {
    		propagateTwoPointsMove(y, x, XR.prev(x), XR.prev(x));
    	} else if (XR.next(y) == x) {
    		propagateTwoPointsMove(x, y, XR.prev(y), XR.prev(y));
    	} else {
    		propagateTwoPointsMove(x, y, XR.prev(y), XR.prev(x));
    	}
	}

	
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}	

	
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
        update(XR.oldRoute(y));
	}

	
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x1));
        update(XR.oldRoute(y));
	}

	
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x1));
        update(XR.oldRoute(y));
	}

	
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x1));
		update(XR.oldRoute(x2));
	}
	
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	for (int r : oldR) {
    		update(r);
    	}
	}
	
	public void propagateThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	oldR.add(XR.oldRoute(x3)); 
    	oldR.add(XR.oldRoute(y3));
    	for (int r : oldR) {
    		update(r);
    	}
	}
	
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1,
			Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	oldR.add(XR.oldRoute(x3)); 
    	oldR.add(XR.oldRoute(y3));
    	oldR.add(XR.oldRoute(x4)); 
    	oldR.add(XR.oldRoute(y4));
    	for (int r : oldR) {
    		update(r);
    	}
	}
	
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.route(y));
	}

	
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
		sumWeights[getIndex(x)] = 0;
	}
	
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		update(XR.route(y1));
	}

	
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x1));
		sumWeights[getIndex(x1)] = sumWeights[getIndex(x2)] = 0;
	}
	
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x));
		sumWeights[getIndex(x)] = 0;
		if (XR.oldRoute(x) != XR.oldRoute(z)) {
			update(XR.oldRoute(z));
		}
	}
	
	
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		for (int i = 0; i < x.size(); i++) {
			Point p = x.get(i);
			Point q = y.get(i);
			if (q != CBLSVR.NULL_POINT) {
				oldR.add(XR.oldRoute(p));
				oldR.add(XR.oldRoute(q));
			} else {
				oldR.add(XR.oldRoute(p));
				sumWeights[getIndex(p)] = 0;
			}
		}
		for (int r : oldR) {
			if (r != Constants.NULL_POINT) {
				update(r);
			}
    	}
	}
	
	public String toString() {
		String s = "";
    	for (int k = 1; k <= XR.getNbRoutes(); k++) {
    		s += "route[" + k + "] : ";
    		Point x = XR.getStartingPointOfRoute(k);
    		while (x != XR.getTerminatingPointOfRoute(k)) {
    			s += x.getID() + " (" + sumWeights[getIndex(x)] + ") ";
    			x = XR.next(x);
    		}
    		s += x.getID() + " (" + sumWeights[getIndex(x)] + ") ";
    		s += "\n";
    	}
    	return s;
	}
}

// ========== vrp.CBLSVR ==========
class CBLSVR {
	public static final int MAX_INT = 2147483647;
	public static final double EPSILON = 0.0000000001;
	public static final Point NULL_POINT = new Point(-1);
	public static boolean equal(double a, double b){
		return Math.abs(a-b) < EPSILON;
	}
}

// ========== vrp.Checkin ==========
class Checkin {
	private int driverID;
	private int count;
	
	public Checkin(int driverID, int count){
		super();
		this.driverID = driverID;
		this.count = count;
	}
	
	public int getDriverID(){
		return this.driverID;
	}
	public void setDriverID(int driverID){
		this.driverID = driverID;
	}

	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}
	public Checkin(){
		super();
	}
}

// ========== vrp.Constants ==========
class Constants {
	public static final int NULL_POINT = -1;
	public static final int MAX_INT = 2147483647;
}

// ========== vrp.ConstraintSystemVR ==========
class ConstraintSystemVR implements IConstraintVR {
	
	private ArrayList<IConstraintVR> _constraints;
	private int _violations;
	private VRManager _mgr;
	
	public ConstraintSystemVR(VRManager mgr){
		_constraints = new ArrayList<IConstraintVR>();
		this._mgr = mgr;
		mgr.postConstraintSystemVR(this);
	}
	public void post(IConstraintVR f){
		_constraints.add(f);
	}
	//@Override
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return _mgr;
	}

	//@Override
	public int violations() {
		// TODO Auto-generated method stub
		return _violations;
	}

	//@Override
	public int evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateOnePointMove(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoPointsMove(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove1(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove2(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove3(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove4(x, y);
		return eval;

	}

	//@Override
	public int evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove5(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove6(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoOptMove7(x, y);
		return eval;
	}

	//@Override
	public int evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) {
			eval += f.evaluateTwoOptMove8(x, y);
		}
		return eval;
	}

	//@Override
	public int evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateOrOptMove1(x1, x2, y);
		return eval;
	}

	//@Override
	public int evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateOrOptMove1(x1, x2, y);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove1(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove2(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove3(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove4(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove5(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove6(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove7(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreeOptMove8(x,y,z);
		return eval;
	}

	//@Override
	public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateCrossExchangeMove(x1,y1,x2,y2);
		return eval;
	}

	//@Override
	public void initPropagation() {
		// TODO Auto-generated method stub
		_violations = 0;
		for(IConstraintVR f : _constraints) _violations += f.violations();
		//System.out.println(name() + "::initPropagation, violations = " + _violations);
	}

	//@Override
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		initPropagation();
	}

	//@Override
	public String name(){
		return "ConstraintSystemVR";
	}

	//@Override
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public void propagateThreePointsMove(Point x1, Point x2, Point x3, Point y1, Point y2,
			Point y3) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1,
			Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	
	//@Override
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	
	//@Override
	public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateTwoPointsMove(x1, x2, y1, y2);
		return eval;
	}
	//@Override
	public int evaluateThreePointsMove(Point x1, Point x2, Point x3, Point y1, Point y2,
			Point y3) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateThreePointsMove(x1, x2, x3, y1, y2, y3);
		return eval;
	}
	//@Override
	public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1,
			Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateFourPointsMove(x1, x2, x3, x4, y1, y2, y3, y4);
		return eval;
	}
	//@Override
	public int evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateAddOnePoint(x, y);
		return eval;
	}
	//@Override
	public int evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateRemoveOnePoint(x);
		return eval;
	}
	
	//@Override
	public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateAddTwoPoints(x1, y1, x2, y2);
		return eval;
	}
	//@Override
	public int evaluateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) eval += f.evaluateRemoveTwoPoints(x1, x2);
		return eval;
	}
	
	//@Override
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public int evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) {
			//System.out.println(f.name() + " " + f.evaluateAddRemovePoints(x, y, z));
			eval += f.evaluateAddRemovePoints(x, y, z);
		}
		return eval;
	}
	//@Override
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		initPropagation();
	}
	//@Override
	public int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		int eval = 0;
		for(IConstraintVR f : _constraints) {
			eval += f.evaluateKPointsMove(x, y);
		}
		return eval;
	}
	@Override
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}
}

// ========== vrp.IConstraintVR ==========
interface IConstraintVR extends InvariantVR {
	/*
	 * return the value of the function
	 */
    public int violations();

    /*
     * query the evaluation of different moves
	 */

    // move of type a [Groer et al., 2010]
    // move customer x to from route of x to route of y; insert x into the position between y and next[y]
    // x and y are not depot
    // remove (prev[x],x), (x, next[x]), (y,next[y])
    // insert (prev[x], next[x]), (y,x), (x, next[y])
    public int evaluateOnePointMove(Point x, Point y);

    // move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depot, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next[y])
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
    public int evaluateTwoPointsMove(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next[y])
    public int evaluateTwoOptMove1(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[x],next[y])
    public int evaluateTwoOptMove2(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[y],next[x])
    public int evaluateTwoOptMove3(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[y],next[x])
    public int evaluateTwoOptMove4(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (y,next[x])
    public int evaluateTwoOptMove5(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (y,next[x])
    public int evaluateTwoOptMove6(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (next[x],y)
    public int evaluateTwoOptMove7(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (next[x],y)
    public int evaluateTwoOptMove8(Point x, Point y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
    public int evaluateOrOptMove1(Point x1, Point x2, Point y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
    public int evaluateOrOptMove2(Point x1, Point x2, Point y);


    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,z) and (next[y], next[x]) and(y, next[z])
    public int evaluateThreeOptMove1(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (z,x) and (next[x], next[y]) and(next[z],y)
    public int evaluateThreeOptMove2(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,y) and (next[x], z) and(next[y], next[z])
    public int evaluateThreeOptMove3(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (y,x) and (z,next[x]) and(next[z], next[y])
    public int evaluateThreeOptMove4(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,next[x]) and(y, next[z])
    public int evaluateThreeOptMove5(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (next[x],z) and(next[z],y)
    public int evaluateThreeOptMove6(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,y) and(next[x], next[z])
    public int evaluateThreeOptMove7(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (y,z) and(next[z], next[x])
    public int evaluateThreeOptMove8(Point x, Point y, Point z);


    // move of type g [Groer et al., 2010]
    // x1 and y1 are on the same route, x1 is before y1
    // x2 and y2 are on the same route, x2 is before y2
    // remove (x1,next[x1]) and (y1, next[y1])
    // remove (x2, next[x2]) and (y2, next[y2])
    // insert (x1, next[x2]) and (y2, next[y1])
    // insert (x2, next[x1]) and (y1, next[y2])
    public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2);
/*
	public int evaluateTwoPointsDifferentRouteMove(int x1, int y1, int x2, int y2);
	
    public int evaluateTwoPointsExchangeMove(int x1, int y1, int x2, int y2, int z1, int t1, int z2, int t2);
*/
	// remove x1, x2 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
    public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2);
    	
	// remove x1, x2, x3 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
    public int evaluateThreePointsMove (Point x1, Point x2, Point x3, Point y1, Point y2, Point y3);
    
    
	// remove x1, x2, x3, x4 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
	// re-insert x4 between y4 and next[y4]
    public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1, Point y2, Point y3, Point y4);
    
    // remove x[0...x.size()-1] from current routes
 	// re-insert x[i] right-after y[i], forall i = 0,...,x.size()-1
 	// application: Large Neighborhood Search
 	// if y[i] = CBLSVR.NULL_POINT, then x[i] is removed from current routes
    int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y);
     
 // add the point x between y and next[y]
    int evaluateAddOnePoint(Point x, Point y);
    
    // remove the point x from its current route
    int evaluateRemoveOnePoint(Point x);
    
    // add the point x1 between y1 and next[y1]
    // add the point x2 between y2 and next[y2]
    // y1 and y2 are on the same route and index[y1] < index[y2]
    // if y1 == y2, the Point x2 is added right-after the Point x1.
    public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2);
    
    // remove two points x1 and x2 from its current route
    // x1 and x2 are on the same route and index[x1] < index[x2]
    public int evaluateRemoveTwoPoints(Point x1, Point x2);
    
    int evaluateAddRemovePoints(Point x, Point y, Point z);
}

// ========== vrp.IDistanceManager ==========
interface IDistanceManager {
	public double getDistance(Point x, Point y);
}

// ========== vrp.IFunctionVR ==========
interface IFunctionVR extends InvariantVR{

	/*
	 * return the value of the function
	 */
    double getValue();

    /*
     * query the evaluation of different moves
	 */

    // x is before y on the same route
 	// remove (x, next[x]) and (y,next[y])
 	// add (x,y) and (next[x],next[y])
 	double evaluateTwoOptMoveOneRoute(Point x, Point y);
 	
 	
    // move of type a [Groer et al., 2010]
    // move customer x to from route of x to route of y; insert x into the position between y and next[y]
    // x and y are not depot
    // remove (prev[x],x), (x, next[x]), (y,next[y])
    // insert (prev[x], next[x]), (y,x), (x, next[y])
    double evaluateOnePointMove(Point x, Point y);

    // move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depot, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next[y])
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
    double evaluateTwoPointsMove(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next[y])
    double evaluateTwoOptMove1(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[x],next[y])
    double evaluateTwoOptMove2(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[y],next[x])
    double evaluateTwoOptMove3(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[y],next[x])
    double evaluateTwoOptMove4(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (y,next[x])
    double evaluateTwoOptMove5(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (y,next[x])
    double evaluateTwoOptMove6(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (next[x],y)
    double evaluateTwoOptMove7(Point x, Point y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not the depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (next[x],y)
    double evaluateTwoOptMove8(Point x, Point y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
    double evaluateOrOptMove1(Point x1, Point x2, Point y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
    double evaluateOrOptMove2(Point x1, Point x2, Point y);


    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,z) and (next[y], next[x]) and(y, next[z])
    double evaluateThreeOptMove1(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (z,x) and (next[x], next[y]) and(next[z],y)
    double evaluateThreeOptMove2(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,y) and (next[x], z) and(next[y], next[z])
    double evaluateThreeOptMove3(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (y,x) and (z,next[x]) and(next[z], next[y])
    double evaluateThreeOptMove4(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,next[x]) and(y, next[z])
    double evaluateThreeOptMove5(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (next[x],z) and(next[z],y)
    double evaluateThreeOptMove6(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,y) and(next[x], next[z])
    double evaluateThreeOptMove7(Point x, Point y, Point z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (y,z) and(next[z], next[x])
    double evaluateThreeOptMove8(Point x, Point y, Point z);


    // move of type g [Groer et al., 2010]
    // x1 and y1 are on the same route, x1 is before y1
    // x2 and y2 are on the same route, x2 is before y2
    // remove (x1,next[x1]) and (y1, next[y1])
    // remove (x2, next[x2]) and (y2, next[y2])
    // insert (x1, next[x2]) and (y2, next[y1])
    // insert (x2, next[x1]) and (y1, next[y2])
    double evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2);
/*
    double evaluateTwoPointsDifferentRouteMove(int x1, int y1, int x2, int y2);
	
    double evaluateTwoPointsExchangeMove(int x1, int y1, int x2, int y2, int z1, int t1, int z2, int t2);
*/
	// remove x1, x2 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
    double evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2);
    	
	// remove x1, x2, x3 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
    double evaluateThreePointsMove (Point x1, Point x2, Point x3, Point y1, Point y2, Point y3);
    
    
	// remove x1, x2, x3, x4 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
	// re-insert x4 between y4 and next[y4]
    double evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1, Point y2, Point y3, Point y4);

    // remove x[0...x.size()-1] from current routes
 	// re-insert x[i] right-after y[i], forall i = 0,...,x.size()-1
 	// application: Large Neighborhood Search
 	// if y[i] = CBLSVR.NULL_POINT, then x[i] is removed from current routes
    double evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y);
     
 // add the point x between y and next[y]
    double evaluateAddOnePoint(Point x, Point y);
    
    // remove the point x from its current route
    double evaluateRemoveOnePoint(Point x);
    
    // add the point x1 between y1 and next[y1]
    // add the point x2 between y2 and next[y2]
    // y1 and y2 are on the same route and index[y1] < index[y2]
    // if y1 == y2, the Point x2 is added right-after the Point x1.
    double evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2);
    
    // remove two points x1 and x2 from its current route
    // x1 and x2 are on the same route and index[x1] < index[x2]
    double evaluateRemoveTwoPoints(Point x1, Point x2);
    
    double evaluateAddRemovePoints(Point x, Point y, Point z);
    
    /*
	 * Perform moves and propagate impact
	 */

	/*
	 * this method is called when the manager is closed
	 * implementing classes implement this method for initializing the data structure maintained
	 */
    /*
	public void initPropagation();

	
    // move of type a [Groer et al., 2010]
    // move customer x to from route of x to route of y; insert x into the position between y and next[y]
    // x and y are not the depot
    void propagateOnePointMove(int x, int y);

    // move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depots, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
    void propagateTwoPointsMove(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next(y))
    void propagateTwoOptMove1(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[x],next(y))
    void propagateTwoOptMove2(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[y],next(x))
    void propagateTwoOptMove3(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[y],next(x))
    void propagateTwoOptMove4(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (y,next[x])
    void propagateTwoOptMove5(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (y,next[x])
    void propagateTwoOptMove6(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (next[x],y)
    void propagateTwoOptMove7(int x, int y);

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (next[x],y)
    void propagateTwoOptMove8(int x, int y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
    void propagateOrOptMove1(int x1, int x2, int y);

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
    void propagateOrOptMove2(int x1, int x2, int y);


    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,z) and (next[y], next[x]) and(y, next[z])
    void propagateThreeOptMove1(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (z,x) and (next[x], next[y]) and(next[z],y)
    void propagateThreeOptMove2(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,y) and (next[x], z) and(next[y], next[z])
    void propagateThreeOptMove3(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (y,x) and (z,next[x]) and(next[z], next[y])
    void propagateThreeOptMove4(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,next[x]) and(y, next[z])
    void propagateThreeOptMove5(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (next[x],z) and(next[z],y)
    void propagateThreeOptMove6(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,y) and(next[x], next[z])
    void propagateThreeOptMove7(int x, int y, int z);

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (y,z) and(next[z], next[x])
    void propagateThreeOptMove8(int x, int y, int z);


    // move of type g [Groer et al., 2010]
    // x1 and y1 are on the same route, x1 is before y1
    // x2 and y2 are on the same route, x2 is before y2
    // remove (x1,next[x1]) and (y1, next[y1])
    // remove (x2, next[x2]) and (y2, next[y2])
    // insert (x1, next[x2]) and (y2, next[y1])
    // insert (x2, next[x1]) and (y1, next[y2])
    void propagateCrossExchangeMove(int x1, int y1, int x2, int y2);
	*/
}

// ========== vrp.Intervals ==========
class Intervals {
	private String dateStart;
	private String dateEnd;
	
	public Intervals(String dateStart, String dateEnd){
		super();
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	public String getDateStart(){
		return this.dateStart;
	}
	public void setDateStart(String dateStart){
		this.dateStart = dateStart;
	}

	public String getDateEnd(){
		return this.dateEnd;
	}
	public void setDateEnd(String dateEnd){
		this.dateEnd = dateEnd;
	}
	public Intervals(){
		super();
	}
}

// ========== vrp.InvariantVR ==========
interface InvariantVR {
	/*
	 * return the VRPManager
	 */
	public VRManager getVRManager();

	/*
	 * Perform moves and propagate impact
	 */

	/*
	 * this method is called when the manager is closed implementing classes
	 * implement this method for initializing the data structure maPointained
	 */
	public void initPropagation();

	// x is before y on the same route
	// remove (x, next[x]) and (y,next[y])
	// add (x,y) and (next[x],next[y])
	void propagateTwoOptMoveOneRoute(Point x, Point y);

	// move of type a [Groer et al., 2010]
	// move customer x to from route of x to route of y; insert x Pointo the
	// position between y and next[y]
	// x and y are not the depot
	void propagateOnePointMove(Point x, Point y);

	// move of type b [Groer et al., 2010]
	// x and y are on the same route and are not the depots, y locates before x
	// on the route
	// remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
	// insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
	void propagateTwoPointsMove(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,y) and (next[x],next(y))
	void propagateTwoOptMove1(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (y,x) and (next[x],next(y))
	void propagateTwoOptMove2(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,y) and (next[y],next(x))
	void propagateTwoOptMove3(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (y,x) and (next[y],next(x))
	void propagateTwoOptMove4(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,next[y]) and (y,next[x])
	void propagateTwoOptMove5(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (next[y],x) and (y,next[x])
	void propagateTwoOptMove6(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,next[y]) and (next[x],y)
	void propagateTwoOptMove7(Point x, Point y);

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (next[y],x) and (next[x],y)
	void propagateTwoOptMove8(Point x, Point y);

	// move of type d [Groer et al., 2010]
	// move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the
	// route containing y
	// remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
	// add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
	void propagateOrOptMove1(Point x1, Point x2, Point y);

	// move of type d [Groer et al., 2010]
	// move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the
	// route containing y
	// remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
	// add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
	void propagateOrOptMove2(Point x1, Point x2, Point y);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,z) and (next[y], next[x]) and(y, next[z])
	void propagateThreeOptMove1(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (z,x) and (next[x], next[y]) and(next[z],y)
	void propagateThreeOptMove2(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,y) and (next[x], z) and(next[y], next[z])
	void propagateThreeOptMove3(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (y,x) and (z,next[x]) and(next[z], next[y])
	void propagateThreeOptMove4(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,next[y]) and (z,next[x]) and(y, next[z])
	void propagateThreeOptMove5(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (next[y],x) and (next[x],z) and(next[z],y)
	void propagateThreeOptMove6(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,next[y]) and (z,y) and(next[x], next[z])
	void propagateThreeOptMove7(Point x, Point y, Point z);

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (next[y],x) and (y,z) and(next[z], next[x])
	void propagateThreeOptMove8(Point x, Point y, Point z);

	// move of type g [Groer et al., 2010]
	// x1 and y1 are on the same route, x1 is before y1
	// x2 and y2 are on the same route, x2 is before y2
	// remove (x1,next[x1]) and (y1, next[y1])
	// remove (x2, next[x2]) and (y2, next[y2])
	// insert (x1, next[x2]) and (y2, next[y1])
	// insert (x2, next[x1]) and (y1, next[y2])
	void propagateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2);

	/*
	 * // new proposed moves // x1, y1 are on route k1, x1 is before y1 // x2,
	 * y2 are on route k2 (k2 != k1), x2 is before y2 // remove x1, y1 from
	 * route k1 // insert x1 between x2 and next[x2] // insert y1 between y2 and
	 * next[y2]
	 * 
	 * void propagateTwoPoPointsDifferentRouteMove(Point x1, Point y1, Point x2,
	 * Point y2);
	 * 
	 * // new proposed moves // x1, y1, z1, t1 are on route k1 // x2, y2, z2, t2
	 * are on route k2 // k1 != k2 // x1, y1, x2, y2, z1, t1, z2, t2 are
	 * alldifferent // remove x1, y1 from route k1 // remove x2, y2 from route
	 * k2 // insert x1 between z2 and next[z2] // insert y1 between t2 and
	 * next[t2] // insert x2 between z1 and next[z1] // insert y2 between t1 and
	 * next[t1]
	 * 
	 * void propagateTwoPoPointsExchangeMove(Point x1, Point y1, Point x2, Point
	 * y2, Point z1, Point t1, Point z2, Point t2);
	 */
	// remove x1, x2 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2);

	// remove x1, x2, x3 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3);

	// remove x1, x2, x3, x4 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
	// re-insert x4 between y4 and next[y4]
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4);

	// remove x[0...x.size()-1] from current routes
	// re-insert x[i] right-after y[i], forall i = 0,...,x.size()-1
	// application: Large Neighborhood Search
	// if y[i] = CBLSVR.NULL_POINT, then x[i] is removed from current routes
    public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y);
    
    // add the Point x between y and next[y]
    public void propagateAddOnePoint(Point x, Point y);
    
    // remove the Point x from its current route
    public void propagateRemoveOnePoint(Point x);
    
    // add the Point x1 between y1 and next[y1]
    // add the Point x2 between y2 and next[y2]
    // y1 and y2 are on the same route and index[y1] < index[y2]
    // if y1 == y2, the Point x2 is added right-after the Point x1.
    public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2);
    
    // remove two points x1 and x2 from its current route
    // x1 and x2 are on the same route and index[x1] < index[x2]
    public void propagateRemoveTwoPoints(Point x1, Point x2);
    
    public void propagateAddRemovePoints(Point x, Point y, Point z);
    
    
    
    public String name();
}

// ========== vrp.ValueRoutesVR ==========
class ValueRoutesVR {
	private HashMap<Point, Point> next;
	private HashMap<Point, Point> prev;
	private HashMap<Point, Integer> route;
	private VarRoutesVR XR;
	private ArrayList<Point> allPoints;
	
	public ValueRoutesVR(VarRoutesVR XR){
		this.XR = XR;
		this.next = new HashMap<Point, Point>();
		this.prev = new HashMap<Point, Point>();
		this.route = new HashMap<Point, Integer>();
		
		allPoints = XR.getAllPoints();
		for(Point p : allPoints){
			next.put(p, XR.next(p));
			prev.put(p, XR.prev(p));
			route.put(p, XR.route(p));
		}
	}
	public Point next(Point p){
		return next.get(p);
	}
	public Point prev(Point p){
		return prev.get(p);
	}
	public int route(Point p){
		return route.get(p);
	}
	public void store(){
		for(Point p : allPoints){
			next.put(p, XR.next(p));
			prev.put(p, XR.prev(p));
			route.put(p, XR.route(p));
		}
	}
	public String toString() {
		String s = "";
		for(int k = 1; k <= XR.getNbRoutes(); k++){
			s += "route[" + k + "] = ";
			Point x = XR.getStartingPointOfRoute(k);
			while(x != XR.getTerminatingPointOfRoute(k)){
				s = s + x.getID() + " " + " -> ";
				x = next.get(x);
			}
			s = s + x.getID() + "\n";
		}
		return s;
	}
}

// ========== vrp.VarRoutesVR ==========
class VarRoutesVR{

	private int[] next;
	private int[] prev;
	private int[] route;
	private int N;
	private int K;
	private int n;
	
	// store old values of next, prev, route
	private int[] old_next;
	private int[] old_prev;
	private int[] old_route;
	
	private int[] index;
	
	private PointType[] pointType;
	private ArrayList<Point> startingPoints;
	private ArrayList<Point> terminatingPoints;
	private ArrayList<Point> clientPoints;
	private ArrayList<Point> allPoints;
	private HashMap<Point, Integer> mPoint2Index;
	private int maxNbPoints;
	private VRManager mgr;
	
	private final int MAXP = 1000;
	
	public VarRoutesVR(VRManager mgr){
		maxNbPoints = MAXP;
		
		clientPoints = new ArrayList<Point>();
		startingPoints = new ArrayList<Point>();
		terminatingPoints = new ArrayList<Point>();
		allPoints = new ArrayList<Point>();
		N = 0;
		K = 0; n = 0;
		next = new int[maxNbPoints];
		prev = new int[maxNbPoints];
		route = new int[maxNbPoints];
		
		old_next = new int[maxNbPoints];
		old_prev = new int[maxNbPoints];
		old_route = new int[maxNbPoints];
		
		index = new int[maxNbPoints];
		pointType = new PointType[maxNbPoints];
		
		mPoint2Index = new HashMap<Point, Integer>();
		
		this.mgr = mgr;
		mgr.post(this);
	}
	
	private void scaleUp() {
		maxNbPoints += MAXP;
		int[] _next = new int[maxNbPoints];
		int[] _prev = new int[maxNbPoints];
		int[] _route = new int[maxNbPoints];
		int[] _index = new int[maxNbPoints];
		int[] _old_next = new int[maxNbPoints];
		int[] _old_prev = new int[maxNbPoints];
		int[] _old_route = new int[maxNbPoints];
		PointType[] _pointType = new PointType[maxNbPoints];
		System.arraycopy(next, 0, _next, 0, next.length);
		System.arraycopy(prev, 0, _prev, 0, next.length);
		System.arraycopy(route, 0, _route, 0, next.length);
		System.arraycopy(index, 0, _index, 0, next.length);
		System.arraycopy(old_next, 0, _old_next, 0, next.length);
		System.arraycopy(old_prev, 0, _old_prev, 0, next.length);
		System.arraycopy(old_route, 0, _old_route, 0, next.length);
		System.arraycopy(pointType, 0, _pointType, 0, pointType.length);
		next = _next;
		prev = _prev;
		route = _route;
		index = _index;
		old_next = _old_next;
		old_prev = _old_prev;
		old_route = _old_route;
		pointType = _pointType;
	}
	
	public void addRoute(Point sp, Point tp){
		if (N + 2 > maxNbPoints) {
			scaleUp();
		}
		
		K++;
		allPoints.add(sp);
		startingPoints.add(sp);
		mPoint2Index.put(sp, N++);
		pointType[N - 1] = PointType.STARTING_ROUTE;
		
		allPoints.add(tp);
		terminatingPoints.add(tp);
		mPoint2Index.put(tp, N++);
		pointType[N - 1] = PointType.TERMINATING_ROUTE;
		
		next[N - 2] = N - 1;
        prev[N - 1] = N - 2;
        prev[N - 2] = next[N - 1] = Constants.NULL_POINT;
        route[N - 2] = route[N - 1] = K;
        old_next[N - 2] = old_prev[N - 2] = old_route[N - 2] = Constants.NULL_POINT;
        old_next[N - 1] = old_prev[N - 1] = old_route[N - 1] = Constants.NULL_POINT;
        update(K);
	}
	
	public void addClientPoint(Point p){
		if(mPoint2Index.get(p) != null) return;
		if (N + 1 > maxNbPoints) {
			scaleUp();
		}
		allPoints.add(p);
		clientPoints.add(p);
		mPoint2Index.put(p, N++);
		pointType[N - 1] = PointType.CLIENT;
		n++;
		
		next[N - 1] = prev[N - 1] = index[N - 1] = route[N - 1] = Constants.NULL_POINT;
		old_next[N - 1] = old_prev[N - 1] = old_route[N - 1] = Constants.NULL_POINT;
	}
	
	public ArrayList<Point> getAllPoints(){
		return allPoints;
	}
	
	public ArrayList<Point> getClientPoints() {
		return clientPoints;
	}
	
	public ArrayList<Point> getStartingPoints() {
		return startingPoints;
	}
	
	public ArrayList<Point> getTerminatingPoints() {
		return terminatingPoints;
	}
	
	public void setValue(ValueRoutesVR val){
		copySolution();
		for (Point p : allPoints) {
			int x = getIndex(p);
			if (val.next(p) != null) {
				next[x] = getIndex(val.next(p));
			}
			if (val.prev(p) != null) {
				prev[x] = getIndex(val.prev(p));
			}
			route[x] = val.route(p);
		}	
		for(int k= 1; k <= getNbRoutes(); k++){
			update(k);
		}
		
		mgr.initPropagation();
	}
	
	public String toString(){
		String s = "";
		for(int k = 1; k <= K; k++){
			s += "route[" + k + "] = ";
			Point x = getStartingPointOfRoute(k);
			while(x != getTerminatingPointOfRoute(k)){
				s = s + x.getID() + " " + " -> ";
				x = next(x);
			}
			s = s + x.getID() + "\n";
		}
		return s;
	}
	public String routeString(int k){
		String s = "";
		for(Point p = startPoint(k); p != endPoint(k); p = next(p)){
			s += p.ID + " -> ";
		}
		s += endPoint(k).ID;
		return s;
	}
	// return the number of points
	public int getTotalNbPoints(){
		return N;
	}
	public int getNbClients(){
		return n;
	}
	public int getNbRoutes(){
		return K;
	}
	
	public int getIndex(Point p) {
		if( mPoint2Index.get(p) != null) return mPoint2Index.get(p);
		return Constants.NULL_POINT;
	}
	public Point startPoint(int k){
		return getStartingPointOfRoute(k);
	}
	public Point endPoint(int k){
		return getTerminatingPointOfRoute(k);
	}
	public Point getStartingPointOfRoute(int k){
		return (k <= 0 || k > K) ? null : startingPoints.get(k-1);
	}
	
	public Point getTerminatingPointOfRoute(int k){
		return (k <= 0 || k > K) ? null : terminatingPoints.get(k-1);
	}
	
	public VRManager getVRManager(){
		return this.mgr;
	}
	
	// add the point o to the end of the route k
    private void addPoint2Route(int k, int u) {
        next[prev[getIndex(getTerminatingPointOfRoute(k))]] = u;
        prev[u] = prev[getIndex(getTerminatingPointOfRoute(k))];
        next[u] = getIndex(getTerminatingPointOfRoute(k));
        prev[getIndex(getTerminatingPointOfRoute(k))] = u;
        route[u] = k;
    }
    
    // remove the point u from its current route
    private void removePointFromRoute(int u) {
        next[prev[u]] = next[u];
        prev[next[u]] = prev[u];
    }

    // reverse the direction of path from s to t on their route
    private void reverse(int s, int t) {
        while (s != t) {
            int tmp = next[s];
            next[s] = prev[s];
            prev[s] = tmp;
            s = next[s];
        }
        int tmp = next[s];
        next[s] = prev[s];
        prev[s] = tmp;
    }

    private void update(int k) {
    	int s = getIndex(getStartingPointOfRoute(k));
    	int t = getIndex(getTerminatingPointOfRoute(k));
    	index[s] = 0;
    	for (int x = s; x != t; x = next[x]) {
    		index[next[x]] = index[x] + 1;
    	}
    }
    
    public Point next(Point x) {
    	int idx = getIndex(x);
    	if(idx == Constants.NULL_POINT) return null;
    	return (next[idx] == Constants.NULL_POINT) ? null : allPoints.get(next[getIndex(x)]);
    }
    
    public Point prev(Point x) {
    	int idx = getIndex(x);
    	if(idx == Constants.NULL_POINT) return null;
    	return (prev[idx] == Constants.NULL_POINT) ? null : allPoints.get(prev[getIndex(x)]);
    }
    
    public int route(Point x) {
    	//System.out.println(name() + "::route of point " + x.ID);
    	if(getIndex(x) == Constants.NULL_POINT) return Constants.NULL_POINT;
    	return route[getIndex(x)];
    }
    
    public int index(Point x) {
    	if(getIndex(x) == Constants.NULL_POINT) return Constants.NULL_POINT;
    	return index[getIndex(x)];
    }
    
    public Point oldNext(Point x) {
    	int idx = getIndex(x);
    	return (old_next[idx] == Constants.NULL_POINT) ? null : allPoints.get(old_next[getIndex(x)]);
    }
    
    public Point oldPrev(Point x) {
    	int idx = getIndex(x);
    	return (old_prev[idx] == Constants.NULL_POINT) ? null : allPoints.get(old_prev[getIndex(x)]);
    }
    
    public int oldRoute(Point x) {
    	if(getIndex(x) == Constants.NULL_POINT) return Constants.NULL_POINT;
    	return old_route[getIndex(x)];
    }
    
	public String name(){
		return "VarRoutesVR";
	}
	
	public boolean isBefore(Point x, Point y) {
		int idx = getIndex(x);
		int idy = getIndex(y);
		return route[idx] == route[idy] && index[idx] < index[idy];
	}
	public ArrayList<Point> collectCurrentClientPointsOnRoute(){
		ArrayList<Point> L = new ArrayList<Point>();
		for(int k = 1; k <= this.getNbRoutes(); k++){
			for(Point p = next(startPoint(k)); p != endPoint(k); p = next(p)){
				L.add(p);
			}
		}
		return L;
	}
	public ArrayList<Point> collectCurrentClientAndStartPointsOnRoute(){
		ArrayList<Point> L = new ArrayList<Point>();
		for(int k = 1; k <= this.getNbRoutes(); k++){
			for(Point p = startPoint(k); p != endPoint(k); p = next(p)){
				L.add(p);
			}
		}
		return L;
	}
	public ArrayList<Point> collectCurrentPointsOnRoute(){
		ArrayList<Point> L = new ArrayList<Point>();
		for(int k = 1; k <= this.getNbRoutes(); k++){
			for(Point p = startPoint(k); p != endPoint(k); p = next(p)){
				L.add(p);
			}
			L.add(endPoint(k));
		}
		return L;
	}
	private void initRandom(){
		
		initStartingTerminatingPoints();
		
		//System.out.println(name() + "::setRandom, add points to a random route");
		Random rand = new Random();
        for (int i = 0; i < n; i++) {
        	addPoint2Route(rand.nextInt(K) + 1, getIndex(clientPoints.get(i)));
        }
        copySolution();
        
        //System.out.println(name() + "::setRandom, update route");
        
        for (int i = 1; i <= K; i++) {
        	update(i);
        }
//		Random rand = new Random();
//		int k = rand.nextInt(10) + 1;
//		for (int i = 0; i < k; i++) {
//			int idx = rand.nextInt(N);
//			int idy = rand.nextInt(N);
//			while (!checkPerformTwoPointsMove(allPoints.get(idx), allPoints.get(idy))) {
//				idx = rand.nextInt(N);
//				idy = rand.nextInt(N);
//			}
//			mgr.performTwoPointsMove(allPoints.get(idx), allPoints.get(idy));
//		}
	}
	
	private void initStartingTerminatingPoints(){
		for (int i = 1; i <= K; i++) {
			int ids = getIndex(getStartingPointOfRoute(i));
			int idt = getIndex(getTerminatingPointOfRoute(i));
            next[ids] = idt;
            prev[idt] = ids;
            prev[ids] = next[idt] = Constants.NULL_POINT;
            route[ids] = route[idt] = i;
        }
	}
	
	public void initSequential(){
		initStartingTerminatingPoints();
		Random rand = new Random();
		int nbClientsPerRoute = n/K;
		int client = 0;	
		for(int k = 1; k < K; k++){
			for(int j = 1; j <= nbClientsPerRoute; j++) {
				addPoint2Route(k, getIndex(clientPoints.get(client++)));
			}
		}
		while(client < n){
			addPoint2Route(K, getIndex(clientPoints.get(client++)));
		}
        
        copySolution();
  
        for (int i = 1; i <= K; i++) {
        	update(i);
        }
        mgr.initPropagation();
	}
	
	
	public void setRandom(){
		initRandom();
		mgr.initPropagation();
	}
	public void initTimeWindowRandom(HashMap<Point,Point>clientpair){
		
		initStartingTerminatingPoints();
		
		Random rand = new Random();
        for (int i = 0; i < n; i++) {
        	Point p = clientPoints.get(i);
        	if(clientpair.containsKey(p))
        	{
        		int k = rand.nextInt(K) + 1;
        		addPoint2Route(k, getIndex(p));
        		addPoint2Route(k, getIndex(clientpair.get(p)));
        	}
        	
        }
        copySolution();       
        for (int i = 1; i <= K; i++) {
        	update(i);
        }
	}
	public void setTimeWindow(HashMap<Point,Point>clientpair)
	{
		initTimeWindowRandom(clientpair);
		mgr.initPropagation();
	}
	
	private void copySolution() {
		System.arraycopy(next, 0, old_next, 0, next.length);
        System.arraycopy(prev, 0, old_prev, 0, prev.length);
        System.arraycopy(route, 0, old_route, 0, route.length);
	}
	
	public boolean isStartingPoint(Point p){
		int v = getIndex(p);
		return v >= 0 && pointType[v] == PointType.STARTING_ROUTE;
	}
	
	public boolean isTerminatingPoint(Point p){
		int v = getIndex(p);
		return v >= 0 && pointType[v] == PointType.TERMINATING_ROUTE;
	}
	
	public boolean isClientPoint(Point p){
		int v = getIndex(p);
		return v >= 0 && pointType[v] == PointType.CLIENT;
	}
	
    public boolean checkPerformTwoOptMoveOneRoute(Point x, Point y){
    	// x and y are in the same route, x is before y
    	// remove (x,next[x]) and (y,next[y])
    	// add (x,y) and (next[x],next[y]), reverse path from y to next[x]
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	//if(idx != idy) return false;
    	if(!isBefore(x, y)) return false;
    	if(y == endPoint(route(y))) return false;    		
    	return true;
    	
    }
    public void performTwoOptMoveOneRoute(Point x, Point y){
    	// x and y are in the same route, x is before y
    	// remove (x,next[x]) and (y,next[y])
    	// add (x,y) and (next[x],next[y]), reverse path from y to next[x]
    	if(!checkPerformTwoOptMoveOneRoute(x,y)){
    		System.out.println(name() + ":: Error performTwoOptMoveOneRoute: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMoveOneRoute(idx, idy);
    }
    private void performTwoOptMoveOneRoute(int x, int y){
    	// x and y are in the same route, x is before y
    	// remove (x,next[x]) and (y,next[y])
    	// add (x,y) and (next[x],next[y]), reverse path from y to next[x]
    	copySolution();
    	int nx = next[x];
    	int ny = next[y];
    	reverse(y,nx);
    	next[x] = y;
    	prev[y] = x;
    	next[nx] = ny;
    	prev[ny] = nx;
    	int rX = route[x];
    	update(rX); 
    }

    
    // move of type a [Groer et al., 2010]
    // move customer x to from route of x to route of y; insert x into the position between y and next[y]
    // x and y are not the depot
	public boolean checkPerformOnePointMove(Point x, Point y) {
		int idx = getIndex(x);
		int idy = getIndex(y);
		boolean ok1 = isClientPoint(x);
		boolean ok2 = (isStartingPoint(y) || isClientPoint(y));
		boolean ok3 =  x != y;
		boolean ok4 = route[idx] != Constants.NULL_POINT && route[idy] != Constants.NULL_POINT && next(y)!= x;
		return ok1 && ok2 && ok3 && ok4;
	}
	
	public void performOnePointMove(Point x, Point y){
		if (!checkPerformOnePointMove(x, y)) {
    		System.out.println(name() + ":: Error performOnePointMove: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
		int idx = getIndex(x);
		int idy = getIndex(y);
		performOnePointMove(idx, idy);
	}
	
    private void performOnePointMove(int x, int y){
    	copySolution();
    	move(x, y);
    	HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(old_route[x]); 
    	oldR.add(old_route[y]);
    	for (int r : oldR) {
    		update(r);
    	}
    }
    // move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depots, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
    public boolean checkPerformTwoPointsMove(Point x, Point y) {
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	return (isClientPoint(x) && isClientPoint(y) && x != y && route[idx] != Constants.NULL_POINT && route[idy] != Constants.NULL_POINT);
    }
    
    public void performTwoPointsMove(Point x, Point y){
    	if (!checkPerformTwoPointsMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoPointsMove: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoPointsMove(idx, idy);
    }
    
    private void performTwoPointsMove(int x, int y){
    	if (next[x] == y) {
    		performTwoPointsMove(y, x, prev[x], prev[x]);
    	} else if (next[y] == x) {
    		performTwoPointsMove(x, y, prev[y], prev[y]);
    	} else {
    		performTwoPointsMove(x, y, prev[y], prev[x]);
    	}
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next(y))
    public boolean checkPerformTwoOptMove(Point x, Point y) {
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	return (isClientPoint(x) && isClientPoint(y) && route[idx] != route[idy] && route[idx] != Constants.NULL_POINT && route[idy] != Constants.NULL_POINT);
    	//return (isClientPoint(x) && isClientPoint(y) && route[idx] != Constants.NULL_POINT && route[idy] != Constants.NULL_POINT);
    }
    
    public void performTwoOptMove1(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove1: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove1(idx, idy);
    }
    
    private void performTwoOptMove1(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y]; 
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(y, startRY);
        reverse(endRX, nextX);
        next[nextX] = nextY;
        prev[nextY] = nextX;
        next[x] = y;
        prev[y] = x;

        next[startRY] = next[endRX];
        prev[next[endRX]] = startRY;
        prev[endRX] = prev[startRY];
        next[prev[startRY]] = endRX;
        next[endRX] = prev[startRY] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[x],next(y))
    public void performTwoOptMove2(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove2: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove2(idx, idy);
    }
    
    private void performTwoOptMove2(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(x, startRX);
        reverse(endRX, nextX);
        next[nextX] = nextY;
        prev[nextY] = nextX;
        next[y] = x;
        prev[x] = y;

        next[startRX] = next[startRY];
        prev[next[startRY]] = startRX;
        prev[endRX] = prev[startRX];
        next[prev[startRX]] = endRX;
        next[startRY] = next[endRX];
        prev[next[endRX]] = startRY;
        prev[startRX] = next[endRX] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[y],next(x))
    public void performTwoOptMove3(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove3: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove3(idx, idy);
    }
    	
    public void performTwoOptMove3(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(y, startRY);
        reverse(endRY, nextY);
        next[nextY] = nextX;
        prev[nextX] = nextY;
        next[x] = y;
        prev[y] = x;

        prev[endRY] = prev[endRX];
        next[prev[endRX]] = endRY;
        prev[endRX] = prev[startRY];
        next[prev[startRY]] = endRX;
        next[startRY] = next[endRY];
        prev[next[endRY]] = startRY;
        prev[startRY] = next[endRY] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (y,x) and (next[y],next(x))
    public void performTwoOptMove4(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove4: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove4(idx, idy);
    }
    
    private void performTwoOptMove4(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));
        
        reverse(x, startRX);
        reverse(endRY, nextY);
        next[nextY] = nextX;
        prev[nextX] = nextY;
        next[y] = x;
        prev[x] = y;

        prev[endRY] = prev[endRX];
        next[prev[endRX]] = endRY;
        next[startRX] = next[startRY];
        prev[next[startRY]] = startRX;
        prev[endRX] = prev[startRX];
        next[prev[startRX]] = endRX;
        next[startRY] = next[endRY];
        prev[next[endRY]] = startRY;
        prev[startRX] = next[endRY] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (y,next[x])
    public void performTwoOptMove5(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove5: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove5(idx, idy);
    }
    
    private void performTwoOptMove5(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        next[x] = nextY;
        prev[nextY] = x;
        next[y] = nextX;
        prev[nextX] = y;

        int tmp = prev[endRX];
        prev[endRX] = prev[endRY];
        prev[endRY] = tmp;
        next[prev[endRX]] = endRX;
        next[prev[endRY]] = endRY;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (y,next[x])
    public void performTwoOptMove6(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove6: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove6(idx, idy);
    }
    
    private void performTwoOptMove6(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(x, startRX);
        reverse(endRY, nextY);
        next[y] = nextX;
        prev[nextX] = y;
        next[nextY] = x;
        prev[x] = nextY;

        prev[endRY] = prev[endRX];
        next[prev[endRX]] = endRY;
        next[startRX] = next[endRY];
        prev[next[endRY]] = startRX;
        prev[endRX] = prev[startRX];
        next[prev[startRX]] = endRX;
        next[endRY] = prev[startRX] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,next[y]) and (next[x],y)
    public void performTwoOptMove7(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove7: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove7(idx, idy);
    }
    
    private void performTwoOptMove7(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(endRX, nextX);
        reverse(y, startRY);
        next[x] = nextY;
        prev[nextY] = x;
        next[nextX] = y;
        prev[y] = nextX;

        prev[endRX] = prev[endRY];
        next[prev[endRY]] = endRX;
        next[startRY] = next[endRX];
        prev[next[endRX]] = startRY;
        prev[endRY] = prev[startRY];
        next[prev[startRY]] = endRY;
        next[endRX] = prev[startRY] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (next[y],x) and (next[x],y)
    public void performTwoOptMove8(Point x, Point y){
    	if (!checkPerformTwoOptMove(x, y)) {
    		System.out.println(name() + ":: Error performTwoOptMove8: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performTwoOptMove8(idx, idy);
    }
    
    private void performTwoOptMove8(int x, int y){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int startRX = getIndex(getStartingPointOfRoute(route[x]));
        int startRY = getIndex(getStartingPointOfRoute(route[y]));
        int endRX = getIndex(getTerminatingPointOfRoute(route[x]));
        int endRY = getIndex(getTerminatingPointOfRoute(route[y]));

        reverse(endRX, startRX);
        reverse(endRY, startRY);
        next[nextX] = y;
        prev[y] = nextX;
        next[nextY] = x;
        prev[x] = nextY;

        next[startRX] = next[endRY];
        prev[next[endRY]] = startRX;
        next[startRY] = next[endRX];
        prev[next[endRX]] = startRY;
        prev[endRX] = prev[startRX];
        next[prev[startRX]] = endRX;
        prev[endRY] = prev[startRY];
        next[prev[startRY]] = endRY;
        next[endRX] = next[endRY] = Constants.NULL_POINT;
        prev[startRX] = prev[startRY] = Constants.NULL_POINT;

        int rX = route[x];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
    public boolean checkPerformOrOptMove(Point x1, Point x2, Point y) {
    	int idx1 = getIndex(x1);
    	int idx2 = getIndex(x2);
    	int idy = getIndex(y);
    	if (!isClientPoint(x1) || route[idx1] == Constants.NULL_POINT) {
    		return false;
    	}
    	if (!isClientPoint(x2) || route[idx2] == Constants.NULL_POINT) {
    		return false;
    	}
    	if (isTerminatingPoint(y) || route[idy] == Constants.NULL_POINT) {
    		return false;
    	}
    	return (route[idx1] == route[idx2] && index[idx1] < index[idx2] && route[idx1] != route[idy]);
    }
    
    public void performOrOptMove1(Point x1, Point x2, Point y){
    	if (!checkPerformOrOptMove(x1, x2, y)) {
    		System.out.println(name() + ":: Error performOrOptMove1: " + x1 + " " + x2 + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idx2 = getIndex(x2);
    	int idy = getIndex(y);
    	performOrOptMove1(idx1, idx2, idy);
    }
    
    private void performOrOptMove1(int x1, int x2, int y){
    	copySolution();
    	
        int prevX1 = prev[x1];
        int nextX2 = next[x2];
        int nextY = next[y];

        next[prevX1] = nextX2;
        prev[nextX2] = prevX1;
        next[x2] = nextY;
        prev[nextY] = x2;
        next[y] = x1;
        prev[x1] = y;

        int rX = route[x1];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }

    // move of type d [Groer et al., 2010]
    // move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the route containing y
    // remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
    // add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
    public void performOrOptMove2(Point x1, Point x2, Point y){
    	if (!checkPerformOrOptMove(x1, x2, y)) {
    		System.out.println(name() + ":: Error performOrOptMove2: " + x1 + " " + x2 + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idx2 = getIndex(x2);
    	int idy = getIndex(y);
    	performOrOptMove2(idx1, idx2, idy);
    }
    
    private void performOrOptMove2(int x1, int x2, int y){
    	copySolution();
    	
        int prevX1 = prev[x1];
        int nextX2 = next[x2];
        int nextY = next[y];

        reverse(x2, x1);
        next[prevX1] = nextX2;
        prev[nextX2] = prevX1;
        next[x1] = nextY;
        prev[nextY] = x1;
        next[y] = x2;
        prev[x2] = y;

        int rX = route[x1];
        int rY = route[y];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	update(rX);
    	update(rY);
    }


    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,z) and (next[y], next[x]) and(y, next[z])
    public boolean checkPerformThreeOptMove(Point x, Point y, Point z) {
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	if (!isClientPoint(x)) {
    		return false;
    	}
    	if (!isClientPoint(z)) {
    		return false;
    	}
    	if (!isClientPoint(y)) {
    		return false;
    	}
    	//System.out.println(route[x] + " " + route[y] + " " + route[z] + " " + index[x] + " " + index[y] + " " + index[z]);
    	return route[idx] != Constants.NULL_POINT && route[idx] == route[idy] && route[idx] == route[idz] && index[idx] < index[idy] && index[idy] < index[idz]; 
    }
    
    public void performThreeOptMove1(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove1: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove1(idx, idy, idz);
    }
    
    private void performThreeOptMove1(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];

        reverse(z, nextY);
        next[x] = z;
        prev[z] = x;
        next[nextY] = nextX;
        prev[nextX] = nextY;
        next[y] = nextZ;
        prev[nextZ] = y;
    	
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (z,x) and (next[x], next[y]) and(next[z],y)
    public void performThreeOptMove2(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove2: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove2(idx, idy, idz);
    }
    
    private void performThreeOptMove2(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];
        int startR = getIndex(getStartingPointOfRoute(route[x]));
        int endR = getIndex(getTerminatingPointOfRoute(route[x]));

        reverse(endR, nextZ);
        reverse(x, startR);
        reverse(y, nextX);

        next[nextZ] = y;
        prev[y] = nextZ;
        next[nextX] = nextY;
        prev[nextY] = nextX;
        next[z] = x;
        prev[x] = z;
        next[startR] = next[endR];
        prev[next[endR]] = startR;
        prev[endR] = prev[startR];
        next[prev[startR]] = endR;
        next[endR] = prev[startR] = Constants.NULL_POINT;
        
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,y) and (next[x], z) and(next[y], next[z])
    public void performThreeOptMove3(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove3: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove3(idx, idy, idz);
    }
    
    private void performThreeOptMove3(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];

        reverse(y, nextX);
        reverse(z, nextY);

        next[x] = y;
        prev[y] = x;
        next[nextX] = z;
        prev[z] = nextX;
        next[nextY] = nextZ;
        prev[nextZ] = nextY;
    	
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (y,x) and (z,next[x]) and(next[z], next[y])
    public void performThreeOptMove4(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove4: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove4(idx, idy, idz);
    }
    
    private void performThreeOptMove4(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];
        int startR = getIndex(getStartingPointOfRoute(route[x]));
        int endR = getIndex(getTerminatingPointOfRoute(route[x]));

        reverse(endR, nextZ);
        reverse(x, startR);

        next[y] = x;
        prev[x] = y;
        next[z] = nextX;
        prev[nextX] = z;
        next[nextZ] = nextY;
        prev[nextY] = nextZ;
        next[startR] = next[endR];
        prev[next[endR]] = startR;
        prev[endR] = prev[startR];
        next[prev[startR]] = endR;
        next[endR] = prev[startR] = Constants.NULL_POINT;
        
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,next[x]) and(y, next[z])
    public void performThreeOptMove5(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove5: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove5(idx, idy, idz);
    }
    
    private void performThreeOptMove5(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];

        next[x] = nextY;
        prev[nextY] = x;
        next[z] = nextX;
        prev[nextX] = z;
        next[y] = nextZ;
        prev[nextZ] = y;
    	
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (next[x],z) and(next[z],y)
    public void performThreeOptMove6(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove6: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove6(idx, idy, idz);
    }
    
    private void performThreeOptMove6(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];
        int startR = getIndex(getStartingPointOfRoute(route[x]));
        int endR = getIndex(getTerminatingPointOfRoute(route[x]));

        reverse(endR, nextZ);
        reverse(y, nextX);
        reverse(z, nextY);
        reverse(x, startR);

        next[nextY] = x;
        prev[x] = nextY;
        next[nextX] = z;
        prev[z] = nextX;
        next[nextZ] = y;
        prev[y] = nextZ;
        next[startR] = next[endR];
        prev[next[endR]] = startR;
        prev[endR] = prev[startR];
        next[prev[startR]] = endR;
        next[endR] = prev[startR] = Constants.NULL_POINT;
        
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (x,next[y]) and (z,y) and(next[x], next[z])
    public void performThreeOptMove7(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove7: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove7(idx, idy, idz);
    }
    
    private void performThreeOptMove7(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];

        reverse(y, nextX);

        next[x] = nextY;
        prev[nextY] = x;
        next[z] = y;
        prev[y] = z;
        next[nextX] = nextZ;
        prev[nextZ] = nextX;
    	
        update(route[x]);
    }

    // move of type e [Groer et al., 2010]
    // x, y, z are on the same route in that order (x is before y, y is before z)
    // remove (x, next[x]), (y, next[y]), and (z, next[z])
    // insert (next[y],x) and (y,z) and(next[z], next[x])
    public void performThreeOptMove8(Point x, Point y, Point z){
    	if (!checkPerformThreeOptMove(x, y, z)) {
    		System.out.println(name() + ":: Error performThreeOptMove8: " + x + " " + y + " " + z + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performThreeOptMove8(idx, idy, idz);
    }
    
    private void performThreeOptMove8(int x, int y, int z){
    	copySolution();
    	
        int nextX = next[x];
        int nextY = next[y];
        int nextZ = next[z];
        int startR = getIndex(getStartingPointOfRoute(route[x]));
        int endR = getIndex(getTerminatingPointOfRoute(route[x]));

        reverse(endR, nextZ);
        reverse(z, nextY);
        reverse(x, startR);

        next[nextY] = x;
        prev[x] = nextY;
        next[y] = z;
        prev[z] = y;
        next[nextZ] = nextX;
        prev[nextX] = nextZ;
        next[startR] = next[endR];
        prev[next[endR]] = startR;
        prev[endR] = prev[startR];
        next[prev[startR]] = endR;
        next[endR] = prev[startR] = Constants.NULL_POINT;
    	
        update(route[x]);
    }


    // move of type g [Groer et al., 2010]
    // x1 and y1 are on the same route, x1 is before y1
    // x2 and y2 are on the same route, x2 is before y2
    // remove (x1,next[x1]) and (y1, next[y1])
    // remove (x2, next[x2]) and (y2, next[y2])
    // insert (x1, next[x2]) and (y2, next[y1])
    // insert (x2, next[x1]) and (y1, next[y2])
    public boolean checkPerformCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	if (!isClientPoint(y1)) {
    		return false;
    	}
    	if (!isClientPoint(y2)) {
    		return false;
    	}
    	if (route[idx1] == route[idx2] || route[idx1] == Constants.NULL_POINT || route[idx2] == Constants.NULL_POINT) {
    		return false;
    	}
    	return (route[idx1] == route[idy1] && route[idx2] == route[idy2] && index[idx1] < index[idy1] && index[idx2] < index[idy2]);
    }
    
    public void performCrossExchangeMove(Point x1, Point y1, Point x2, Point y2){
    	if (!checkPerformCrossExchangeMove(x1, y1, x2, y2)) {
    		System.out.println(name() + ":: Error performCrossExchangeMove: " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	performCrossExchangeMove(idx1, idy1, idx2, idy2);
    }
    
    private void performCrossExchangeMove(int x1, int y1, int x2, int y2){
    	copySolution();
    	
        int nextX1 = next[x1];
        int nextY1 = next[y1];
        int nextX2 = next[x2];
        int nextY2 = next[y2];

        next[x1] = nextX2;
        prev[nextX2] = x1;
        next[x2] = nextX1;
        prev[nextX1] = x2;
        next[y1] = nextY2;
        prev[nextY2] = y1;
        next[y2] = nextY1;
        prev[nextY1] = y2;

        int rX = route[x1];
        int rY = route[x2];
        for (int u = getIndex(getStartingPointOfRoute(rX)); u != getIndex(getTerminatingPointOfRoute(rX)); u = next[u]) {
        	route[u] = rX;
        }
        for (int u = getIndex(getStartingPointOfRoute(rY)); u != getIndex(getTerminatingPointOfRoute(rY)); u = next[u]) {
        	route[u] = rY;
        }
    	
        update(rX);
        update(rY);
    }
    
    public boolean checkPerformTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
    	Point[] x = {x1, x2};
    	Point[] y = {y1, y2};
    	for (int i = 0; i < 2; i++) {
    		for (int j = 0; j < 2; j++) {
    			if (x[i] == y[j]) {
    				return false;
    			}
    		}
    		if (!isClientPoint(x[i]) || route[getIndex(x[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    		if ((!isClientPoint(y[i]) && !isStartingPoint(y[i])) || route[getIndex(y[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    	}
    	return x1 != x2;
    }  
    
    public boolean checkPerformThreePointsMove(Point x1, Point x2, Point x3, Point y1, Point y2, Point y3) {
    	Point[] x = {x1, x2, x3};
    	Point[] y = {y1, y2, y3};
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 3; j++) {
    			if (x[i] == y[j]) {
    				return false;
    			}
    		}
    		for (int j = i + 1; j < 3; j++) {
    			if (x[i] == x[j]) {
    				return false;
    			}
    		}
    		if (!isClientPoint(x[i]) || route[getIndex(x[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    		if ((!isClientPoint(y[i]) && !isStartingPoint(y[i])) || route[getIndex(y[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    	}
    	return true;
    } 
    
    public boolean checkPerformFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1, Point y2, Point y3, Point y4) {
    	Point[] x = {x1, x2, x3, x4};
    	Point[] y = {y1, y2, y3, y4};
    	for (int i = 0; i < 4; i++) {
    		for (int j = 0; j < 4; j++) {
    			if (x[i] == y[j]) {
    				return false;
    			}
    		}
    		for (int j = i + 1; j < 4; j++) {
    			if (x[i] == x[j]) {
    				return false;
    			}
    		}
    		if (!isClientPoint(x[i]) || route[getIndex(x[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    		if ((!isClientPoint(y[i]) && !isStartingPoint(y[i])) || route[getIndex(y[i])] == Constants.NULL_POINT) {
    			return false;
    		}
    	}
    	return true;
    } 
    
    private void move(int x, int y) {
    	if (route[x] != Constants.NULL_POINT) {
        	next[prev[x]] = next[x];
        	prev[next[x]] = prev[x];
    	}
    	route[x] = route[y];
    	next[x] = next[y];
    	prev[next[y]] = x;
    	prev[x] = y;
    	next[y] = x;
    }

	// remove x1, x2 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]	
    public void performTwoPointsMove(Point x1, Point x2, Point y1, Point y2){
    	if (!checkPerformTwoPointsMove(x1, x2, y1, y2)) {
    		System.out.println(name() + ":: Error performTwoPointsMove: " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	performTwoPointsMove(idx1, idx2, idy1, idy2);
    }
    
    private void performTwoPointsMove(int x1, int x2, int y1, int y2){
    	copySolution();
    	move(x2, y2);
    	move(x1, y1);
    	HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(old_route[x1]); 
    	oldR.add(old_route[y1]);
    	oldR.add(old_route[x2]); 
    	oldR.add(old_route[y2]);
    	for (int r : oldR) {
    		update(r);
    	}
    }
    
 // remove x1, x2, x3 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
    public void performThreePointsMove (Point x1, Point x2, Point x3, Point y1, Point y2, Point y3){
    	if (!checkPerformThreePointsMove(x1, x2, x3, y1, y2, y3)) {
    		System.out.println(name() + ":: Error performThreePointsMove: " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	int idx3 = getIndex(x3);
    	int idy3 = getIndex(y3);
    	performThreePointsMove(idx1, idx2, idx3, idy1, idy2, idy3);
    }
    
    private void performThreePointsMove(int x1, int x2, int x3, int y1, int y2, int y3){
    	copySolution();
    	move(x3, y3);
    	move(x2, y2);
    	move(x1, y1);
    	HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(old_route[x1]); 
    	oldR.add(old_route[y1]);
    	oldR.add(old_route[x2]); 
    	oldR.add(old_route[y2]);
    	oldR.add(old_route[x3]); 
    	oldR.add(old_route[y3]);
    	for (int r : oldR) {
    		update(r);
    	}
    }

	// remove x1, x2, x3, x4 from their current routes
	// re-insert x1 between y1 and next[y1]
	// re-insert x2 between y2 and next[y2]
	// re-insert x3 between y3 and next[y3]
	// re-insert x4 between y4 and next[y4]
    public void performFourPointsMove(Point x1, Point x2, Point x3, Point x4, Point y1, Point y2, Point y3, Point y4){
    	if (!checkPerformFourPointsMove(x1, x2, x3, x4, y1, y2, y3, y4)) {
    		System.out.println(name() + ":: Error performFourPointsMove: " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + " " + x4 + " " + y4 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	int idx3 = getIndex(x3);
    	int idy3 = getIndex(y3);
    	int idx4 = getIndex(x4);
    	int idy4 = getIndex(y4);
    	performFourPointsMove(idx1, idx2, idx3, idx4, idy1, idy2, idy3, idy4);
    }
    
    private void performFourPointsMove(int x1, int x2, int x3, int x4, int y1, int y2, int y3, int y4){
    	copySolution();
    	move(x4, y4);
    	move(x3, y3);
    	move(x2, y2);
    	move(x1, y1);
    	HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(old_route[x1]); 
    	oldR.add(old_route[y1]);
    	oldR.add(old_route[x2]); 
    	oldR.add(old_route[y2]);
    	oldR.add(old_route[x3]); 
    	oldR.add(old_route[y3]);
    	oldR.add(old_route[x4]); 
    	oldR.add(old_route[y4]);
    	for (int r : oldR) {
    		update(r);
    	}
    }
    public boolean contains(Point x){
    	boolean ok = getIndex(x) != Constants.NULL_POINT;
    	if(!ok) return ok;
    	return route[getIndex(x)] != Constants.NULL_POINT;
    }
    public boolean checkPerformAddOnePoint(Point x, Point y) {
    	return (route[getIndex(x)] == Constants.NULL_POINT && isClientPoint(x) && (isClientPoint(y) || 
    			isStartingPoint(y)) && route[getIndex(y)] != Constants.NULL_POINT);
    }
    
    public boolean checkPerformAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
    	return (route[getIndex(x1)] == Constants.NULL_POINT && isClientPoint(x1) && (isClientPoint(y1) ||
    			isStartingPoint(y1)) && route[getIndex(y1)] != Constants.NULL_POINT
    			&& route[getIndex(x2)] == Constants.NULL_POINT && isClientPoint(x2) && (isClientPoint(y2) || 
    	    	isStartingPoint(y2)) && route[getIndex(y2)] != Constants.NULL_POINT
    	    	&& route[getIndex(y1)] == route[getIndex(y2)] && index[getIndex(y1)] <= index[getIndex(y2)]);
    }
    
    public void performAddOnePoint(Point x, Point y){
    	// add point x between y and next[y]
    	if (!checkPerformAddOnePoint(x, y)) {
    		System.out.println(name() + ":: Error performAddOneMove: " + x + " " + y + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	performAddOnePoint(idx, idy);
    }
    
    private void performAddOnePoint(int x, int y){
    	copySolution();
    	prev[next[y]] = x;
    	next[x] = next[y];
    	prev[x] = y;
    	next[y] = x;
    	route[x] = route[y];
    	update(route[x]);
    }
    
    private void performAddTwoPoint(int x1, int y1, int x2, int y2){
    	copySolution();
    	if(y1 != y2){
    		prev[next[y1]] = x1;
        	next[x1] = next[y1];
        	prev[x1] = y1;
        	next[y1] = x1;
        	route[x1] = route[y1];
        	
    		prev[next[y2]] = x2;
        	next[x2] = next[y2];
        	prev[x2] = y2;
        	next[y2] = x2;
        	route[x2] = route[y2];
    	}
    	else{
    		prev[next[y1]] = x2;
    		next[x2] = next[y1];
    		prev[x2] = x1;
    		next[x1] = x2;
    		prev[x1] = y1;
    		next[y1] = x1;
    		route[x1] = route[y1];
    		route[x2] = route[y1];
    	}
    	update(route[x1]);
    }
    
    public void performAddTwoPoints(Point x1, Point y1, Point x2, Point y2){
    	if (!checkPerformAddTwoPoints(x1, y1, x2, y2)) {
    		System.out.println(name() + ":: Error performAddTwoPoints: " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idy1 = getIndex(y1);
    	int idx2 = getIndex(x2);
    	int idy2 = getIndex(y2);
    	performAddTwoPoint(idx1, idy1, idx2, idy2);
    }
    
    public boolean checkPerformRemoveOnePoint(Point x) {
    	if(route[getIndex(x)] == Constants.NULL_POINT){
    		System.out.println("Null point");
    	}
    	if(!isClientPoint(x)){
    		System.out.println("not client point");
    	}
    	return (route[getIndex(x)] != Constants.NULL_POINT && isClientPoint(x));
    }
    
    public boolean checkPerformRemoveTwoPoints(Point x1, Point x2) {
    	return (route[getIndex(x1)] != Constants.NULL_POINT && isClientPoint(x1)
    			&& route[getIndex(x2)] != Constants.NULL_POINT && isClientPoint(x2)
    			&& index[getIndex(x1)] < index[getIndex(x2)]);
    }
    
    public void performRemoveOnePoint(Point x){
    	// remove x from its current route
    	if (!checkPerformRemoveOnePoint(x)) {
    		System.out.println(name() + ":: Error performRemoveOneMove: " + x + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	copySolution();
    	next[prev[idx]] = next[idx];
    	prev[next[idx]] = prev[idx];
    	next[idx] = prev[idx] = route[idx] = Constants.NULL_POINT;
    	update(old_route[idx]);
    	index[idx] = Constants.NULL_POINT;
    }
    
    public void performRemoveTwoPoints(Point x1, Point x2){
    	// remove x from its current route
    	if (!checkPerformRemoveTwoPoints(x1, x2)) {
    		System.out.println(name() + ":: Error performRemoveTwoPoints: " + x1 + " " + x2 + "\n" + toString());
    		System.exit(-1);
    	}
    	int idx1 = getIndex(x1);
    	int idx2 = getIndex(x2);
    	copySolution();
    	next[prev[idx1]] = next[idx1];
    	prev[next[idx1]] = prev[idx1];
    	next[idx1] = prev[idx1] = route[idx1] = Constants.NULL_POINT;
    	update(old_route[idx1]);
    	index[idx1] = Constants.NULL_POINT;
    	
    	next[prev[idx2]] = next[idx2];
    	prev[next[idx2]] = prev[idx2];
    	next[idx2] = prev[idx2] = route[idx2] = Constants.NULL_POINT;
    	update(old_route[idx2]);
    	index[idx2] = Constants.NULL_POINT;
    }
    
    public boolean checkPerformAddRemovePoints(Point x, Point y, Point z){
    	// x is not starting, or terminating points
    	// z is not a terminating point
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	if (route[idx] == Constants.NULL_POINT || isStartingPoint(x) || isTerminatingPoint(x)) {
    		return false;
    	}
    	if (route[idy] != Constants.NULL_POINT || route[idz] == Constants.NULL_POINT || isTerminatingPoint(z)) {
    		return false;
    	}
    	return x != z;
    }
    
    public void performAddRemovePoints(Point x, Point y, Point z){
    	//remove x from its current route
    	// add y between z and next[z]
    	//TODO by HoangNT
    	if(!checkPerformAddRemovePoints(x, y, z)){
    		System.out.println(name() + "::performAddRemovePoints(" + x + "," + y + "," + z + ") -> Error, ");
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	int idz = getIndex(z);
    	performAddRemovePoints(idx, idy, idz);
    }
    
    private void performAddRemovePoints(int x, int y, int z){
    	copySolution();
    	if (prev[x] == z) {
    		next[y] = next[x];
    		prev[y] = prev[x];
    		next[z] = y;
    		prev[next[x]] = y;
    		route[y] = route[x];
    		update(route[z]);
    	} else {
    		next[prev[x]] = next[x];
    		prev[next[x]] = prev[x];
    		next[y] = next[z];
    		prev[next[y]] = y;
    		prev[y] = z;
    		next[z] = y;
    		route[y] = route[z];
    		update(route[z]);
    		if (route[x] != route[z]) {
    			update(route[x]);
    		}
    	}
    	index[x] = next[x] = prev[x] = route[x] = Constants.NULL_POINT;
    }
    
    public boolean checkPerformRemoveSequencePoints(Point x, Point y){
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	if (index[idx] == Constants.NULL_POINT || index[idy] == Constants.NULL_POINT) {
    		return false;
    	}
    	return isBefore(x,y);
    }
    
    public void performRemoveSequencePoints(Point x, Point y){
    	// remove points from next[x] to prev[y]
    	// set next[x] = y
    	if(!checkPerformRemoveSequencePoints(x, y)){
    		System.out.println(name() + "::performRemoveSequecePoints(" + x + "," + y + ") -> Error, " + x + " is not before " + y);
    		System.exit(-1);
    	}
    	int idx = getIndex(x);
    	int idy = getIndex(y);
    	for(int v = next[idx]; v != idy; v = next[v]){
    		index[v] = Constants.NULL_POINT;
    	}
    	copySolution();
    	next[idx] = idy;
    	prev[idy] = idx;
    	update(route[idx]);
    }
    
    public boolean checkPerformKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
    	if (x.size() != y.size()) {
    		return false;
    	}
    	//HashSet<Point> s = new HashSet<Point>(x);
    	//if (x.size() != s.size()) {
    		//return false;
    	//}
    	
    	for (Point p : y) {
    		if (p != CBLSVR.NULL_POINT && (route[getIndex(p)] == Constants.NULL_POINT || isTerminatingPoint(p))) {
    			return false;
    		}
    	}
    	HashSet<Point> set = new HashSet<Point>(y);
    	for (Point p : x) {
    		if ((!isClientPoint(p)) || set.contains(p)) {
    			return false;
    		}
    	}
    	for(int i = 0; i < x.size(); i++){
    		Point px = x.get(i);
    		Point py = y.get(i);
    		int ix = getIndex(px);
    		int iy = getIndex(py);
    		if(ix == Constants.NULL_POINT && iy == Constants.NULL_POINT) return false;
    		if(next(py) == px) return false;
    		
    		//if(route[ix] == Constants.NULL_POINT && route[iy] == Constants.NULL_POINT) return false;
    	}
    	return true;
    }
    
    public void performKPointsMove(ArrayList<Point> x, ArrayList<Point> y){
    	// remove x[0...x.size()-1] from current routes
    	// re-insert x[i] right-after y[i], forall i = 0,...,x.size()-1
    	// application: Large Neighborhood Search
    	// if y[i] = CBLSVR.NULL_POINT, then x[i] is removed from current routes
    	// if y[i1] = y[i2] = ...= y[ik], then re-insert x[i1], x[i2], ..., x[ik] in that order right-after y[i1] 
    	if (!checkPerformKPointsMove(x, y)) {
    		System.out.println(name() + "::performKPointsMove -> Error");
    		System.exit(-1);
    	}
    	copySolution();
    	HashSet<Integer> oldR = new HashSet<Integer>();
    	for (int i = x.size() - 1; i >= 0; i--) {
    		Point p = x.get(i);
    		Point q = y.get(i);
    		if (q != CBLSVR.NULL_POINT) {
    			oldR.add(oldRoute(q));
    			oldR.add(oldRoute(p));
    			int idx = getIndex(p);
    			int idy = getIndex(q);
    			move(idx, idy);
    		} else {
    			oldR.add(oldRoute(p));
    			int idx = getIndex(p);
    			if (route[idx] != Constants.NULL_POINT) {
    				prev[next[idx]] = prev[idx];
    				next[prev[idx]] = next[idx];
    			}
    			index[idx] = next[idx] = prev[idx] = route[idx] = Constants.NULL_POINT;
    		}
    	}
    	for (int r : oldR) {
    		if (r != Constants.NULL_POINT) {
    			update(r);
    		}
    	}
    }
    
    
    public ArrayList<Point> collectClientPointsOnRoutes(){
    	ArrayList<Point> L = new ArrayList<Point>();
    	for(Point p: clientPoints){
    		if(contains(p))
    			L.add(p);
    	}
    	return L;
    }
    
    public int getNbClientPointsOnRoutes(){
    	int sz = 0;
    	for(Point p: clientPoints){
    		if(contains(p))
    			sz++;
    	}
    	return sz;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VRManager mgr = new VRManager();
		VarRoutesVR XR = new VarRoutesVR(mgr);
		Point s1 = new Point(1);
		Point t1 = new Point(2);
		Point s2 = new Point(3);
		Point t2 = new Point(4);
		
		ArrayList<Point> points = new ArrayList<Point>();
		XR.addRoute(s1, t1);
		XR.addRoute(s2, t2);
		for(int i = 5; i <= 12; i++){
			Point p = new Point(i);
			points.add(p);
			XR.addClientPoint(p);
		}		
		mgr.close();
		Point p5 = points.get(0);
		Point p6 = points.get(1);
		Point p7 = points.get(2);
		Point p8 = points.get(3);
		Point p9 = points.get(4);
		Point p10 = points.get(5);
		Point p11 = points.get(6);
		Point p12 = points.get(7);
		
		System.out.println(XR.toString());
		mgr.performAddOnePoint(points.get(0), s1);
		mgr.performAddOnePoint(points.get(1), points.get(0));
		mgr.performAddOnePoint(points.get(2), points.get(1));
		mgr.performAddOnePoint(points.get(3), s2);
		mgr.performAddOnePoint(points.get(4), points.get(3));
		mgr.performAddOnePoint(points.get(5), points.get(4));
		mgr.performAddOnePoint(p11, p7);
		mgr.performAddOnePoint(p12, p10);
		
		System.out.println(XR.toString());
		
		mgr.performTwoOptMove5(p6, p9);
		System.out.println(XR.toString());
	}

}

// ========== vrp.VRManager ==========
class VRManager {

	private ArrayList<InvariantVR> invariants;
	private VarRoutesVR X;
	private ArrayList<ConstraintSystemVR> constraintSystem;

	public VRManager() {
		invariants = new ArrayList<InvariantVR>();
		X = null;
		constraintSystem = new ArrayList<ConstraintSystemVR>();
	}

	public void postConstraintSystemVR(ConstraintSystemVR S) {
		constraintSystem.add(S);
	}

	public void printInvariants() {
		for (InvariantVR I : invariants) {
			System.out.println(I.name());
		}
	}

	public VarRoutesVR getVarRoutesVR() {
		return X;
	}

	public void post(InvariantVR f) {
		// System.out.println("***** " + f.name());
		invariants.add(f);
	}

	public void post(VarRoutesVR XR) {
		if (X != null) {
			System.out
					.println(name()
							+ "::post(VarRoutesVR X),  EXCEPTION, another VarRoutesVR is already posted");
			System.out.println(X.toString());
			exit(-1);
		}
		// System.out.println(name() + "::post(VarRoutesVR)");
		this.X = XR;
	}

	public void initPropagation() {
		for (InvariantVR f : invariants) {
			// System.out.println(f.name());
			f.initPropagation();
		}
	}

	public void close() {
		for (ConstraintSystemVR S : constraintSystem)
			invariants.add(S);

		// System.out.println(name() + "::close, invariants = ");
		// printInvariants();

		initPropagation();

	}
    public void performTwoOptMoveOneRoute(Point x, Point y){
    	// x and y are in the same route, x is before y
    	// remove (x,next[x]) and (y,next[y])
    	// add (x,y) and (next[x],next[y]), reverse path from y to next[x]
		X.performTwoOptMoveOneRoute(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMoveOneRoute(x, y);
		}
    
    		
    }

	public void performRemoveAllClientPoints() {
		for (int k = 1; k <= X.getNbRoutes(); k++) {
			Point p = X.next(X.startPoint(k));
			while (p != X.endPoint(k)) {
				performRemoveOnePoint(p);
				//System.out.println(name() + "::performRemoveOnePoint(" + p.ID
				//		+ " on route " + k + "), XR = " + X.toString());
				p = X.next(X.startPoint(k));
			}
		}
	}

	// move of type a [Groer et al., 2010]
	// move customer x to from route of x to route of y; insert x into the
	// position between y and next[y]
	// x and y are not the depot
	public void performOnePointMove(Point x, Point y) {
		X.performOnePointMove(x, y);
		for (InvariantVR f : invariants) {
			f.propagateOnePointMove(x, y);
		}
	}

	// move of type b [Groer et al., 2010]
	// x and y are on the same route and are not the depots, y locates before x
	// on the route
	// remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
	// insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
	public void performTwoPointsMove(Point x, Point y) {
		X.performTwoPointsMove(x, y);
		Iterator<InvariantVR> it = invariants.iterator();
		for (InvariantVR f : invariants) {
			f.propagateTwoPointsMove(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,y) and (next[x],next(y))
	public void performTwoOptMove1(Point x, Point y) {
		X.performTwoOptMove1(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove1(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (y,x) and (next[x],next(y))
	public void performTwoOptMove2(Point x, Point y) {
		X.performTwoOptMove2(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove2(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,y) and (next[y],next(x))
	public void performTwoOptMove3(Point x, Point y) {
		X.performTwoOptMove3(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove3(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (y,x) and (next[y],next(x))
	public void performTwoOptMove4(Point x, Point y) {
		X.performTwoOptMove4(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove4(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,next[y]) and (y,next[x])
	public void performTwoOptMove5(Point x, Point y) {
		X.performTwoOptMove5(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove5(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (next[y],x) and (y,next[x])
	public void performTwoOptMove6(Point x, Point y) {
		X.performTwoOptMove6(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove6(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (x,next[y]) and (next[x],y)
	public void performTwoOptMove7(Point x, Point y) {
		X.performTwoOptMove7(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove7(x, y);
		}
	}

	// move of type c [Groer et al., 2010]
	// x and y are on different routes and are not depots
	// remove (x,next[x]) and (y,next[y])
	// insert (next[y],x) and (next[x],y)
	public void performTwoOptMove8(Point x, Point y) {
		X.performTwoOptMove8(x, y);
		for (InvariantVR f : invariants) {
			f.propagateTwoOptMove8(x, y);
		}
	}

	// move of type d [Groer et al., 2010]
	// move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the
	// route containing y
	// remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
	// add (y, x1) and (x2, next[y]) and (prev[x1], next[x2])
	public void performOrOptMove1(Point x1, Point x2, Point y) {
		X.performOrOptMove1(x1, x2, y);
		for (InvariantVR f : invariants) {
			f.propagateOrOptMove1(x1, x2, y);
		}
	}

	// move of type d [Groer et al., 2010]
	// move the sequence <x1,next[x1],..., prev[x2], x2> of length len to the
	// route containing y
	// remove (prev[x1],x1) and (x2,next[x2]), and (y,next[y])
	// add (y, x2) and (x1, next[y]) and (prev[x1], next[x2])
	public void performOrOptMove2(Point x1, Point x2, Point y) {
		X.performOrOptMove2(x1, x2, y);
		for (InvariantVR f : invariants) {
			f.propagateOrOptMove2(x1, x2, y);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,z) and (next[y], next[x]) and(y, next[z])
	public void performThreeOptMove1(Point x, Point y, Point z) {
		X.performThreeOptMove1(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove1(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (z,x) and (next[x], next[y]) and(next[z],y)
	public void performThreeOptMove2(Point x, Point y, Point z) {
		X.performThreeOptMove2(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove2(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,y) and (next[x], z) and(next[y], next[z])
	public void performThreeOptMove3(Point x, Point y, Point z) {
		X.performThreeOptMove3(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove3(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (y,x) and (z,next[x]) and(next[z], next[y])
	public void performThreeOptMove4(Point x, Point y, Point z) {
		X.performThreeOptMove4(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove4(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,next[y]) and (z,next[x]) and(y, next[z])
	public void performThreeOptMove5(Point x, Point y, Point z) {
		X.performThreeOptMove5(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove5(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (next[y],x) and (next[x],z) and(next[z],y)
	public void performThreeOptMove6(Point x, Point y, Point z) {
		X.performThreeOptMove6(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove6(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (x,next[y]) and (z,y) and(next[x], next[z])
	public void performThreeOptMove7(Point x, Point y, Point z) {
		X.performThreeOptMove7(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove7(x, y, z);
		}
	}

	// move of type e [Groer et al., 2010]
	// x, y, z are on the same route in that order (x is before y, y is before
	// z)
	// remove (x, next[x]), (y, next[y]), and (z, next[z])
	// insert (next[y],x) and (y,z) and(next[z], next[x])
	public void performThreeOptMove8(Point x, Point y, Point z) {
		X.performThreeOptMove8(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateThreeOptMove8(x, y, z);
		}
	}

	// move of type g [Groer et al., 2010]
	// x1 and y1 are on the same route, x1 is before y1
	// x2 and y2 are on the same route, x2 is before y2
	// remove (x1,next[x1]) and (y1, next[y1])
	// remove (x2, next[x2]) and (y2, next[y2])
	// insert (x1, next[x2]) and (y2, next[y1])
	// insert (x2, next[x1]) and (y1, next[y2])
	public void performCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		X.performCrossExchangeMove(x1, y1, x2, y2);
		for (InvariantVR f : invariants) {
			f.propagateCrossExchangeMove(x1, y1, x2, y2);
		}
	}

	public void performTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		X.performTwoPointsMove(x1, x2, y1, y2);
		;
		for (InvariantVR f : invariants) {
			f.propagateTwoPointsMove(x1, x2, y1, y2);
			;
		}
	}

	public void performThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		X.performThreePointsMove(x1, x2, x3, y1, y2, y3);
		;
		for (InvariantVR f : invariants) {
			f.propagateThreePointsMove(x1, x2, x3, y1, y2, y3);
		}
	}

	public void performFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		X.performFourPointsMove(x1, x2, x3, x4, y1, y2, y3, y4);
		for (InvariantVR f : invariants) {
			f.propagateFourPointsMove(x1, x2, x3, x4, y1, y2, y3, y4);
		}
	}

	public void performAddOnePoint(Point x, Point y) {
		X.performAddOnePoint(x, y);
		for (InvariantVR f : invariants) {
			f.propagateAddOnePoint(x, y);
		}
	}

	public void performRemoveOnePoint(Point x) {
		X.performRemoveOnePoint(x);
		for (InvariantVR f : invariants) {
			f.propagateRemoveOnePoint(x);
		}
	}
	
	public void performAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		X.performAddTwoPoints(x1, y1, x2, y2);
		for (InvariantVR f : invariants) {
			f.propagateAddTwoPoints(x1, y1, x2, y2);
		}
	}

	public void performRemoveTwoPoints(Point x1, Point x2) {
		X.performRemoveTwoPoints(x1, x2);
		for (InvariantVR f : invariants) {
			f.propagateRemoveTwoPoints(x1, x2);
		}
	}

	public void performAddRemovePoints(Point x, Point y, Point z) {
		X.performAddRemovePoints(x, y, z);
		for (InvariantVR f : invariants) {
			f.propagateAddRemovePoints(x, y, z);
		}
	}

	public void performKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		X.performKPointsMove(x, y);
		for (InvariantVR f : invariants) {
			f.propagateKPointsMove(x, y);
		}
	}

	public String name() {
		return "VRManager";
	}

	public void exit(int code) {
		System.out.println(name() + "::exit(" + code + ")");
		System.exit(code);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

// ========== vrp.constraints.CEarliestArrivalTimeVR ==========
class CEarliestArrivalTimeVR implements IConstraintVR {
	
	private HashMap<Point, Integer> latestAllowedArrivalTime;
	private VarRoutesVR XR;
	private EarliestArrivalTimeVR eat;
	private int violations;
	private HashMap<Point, Double> earliestArrivalTime;
	private HashMap<Point, Integer> vio;

	// temporary data structure
	private HashMap<Point, Point> t_next;
	public CEarliestArrivalTimeVR(EarliestArrivalTimeVR eat, HashMap<Point, Integer> latestAllowedArrivalTime){
		this.eat = eat;
		this.latestAllowedArrivalTime = latestAllowedArrivalTime;
		
		earliestArrivalTime = eat.getEarliestArrivalTime();
		XR = eat.getVarRouteVR();
		t_next = new HashMap<Point,Point>();
		vio = new HashMap<Point,Integer>();
		getVRManager().post(this);
	}
	
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return XR.getVRManager();
	}

	private int computeViolations(Point i) {
		double v = earliestArrivalTime.get(i) <= latestAllowedArrivalTime.get(i) ? 0
					: earliestArrivalTime.get(i) - latestAllowedArrivalTime.get(i);
		return (int) Math.ceil(v);
	}

	private int computeViolations(Point i, double arrivalTime) {
		double v = arrivalTime <= latestAllowedArrivalTime.get(i) ? 0 : arrivalTime
				- latestAllowedArrivalTime.get(i);
		return (int) Math.ceil(v);
	}
	
	
	public void initPropagation() {
		// TODO Auto-generated method stub
		violations = 0;
		vio = new HashMap<Point, Integer>();
		t_next = new HashMap<Point, Point>();
		int nr = XR.getNbRoutes();
		for(int k = 1; k <= nr; ++k)
		{
			Point s = XR.getStartingPointOfRoute(k);
			do{		
				int svio  = computeViolations(s);
				violations += svio;
				vio.put(s, svio);
				//System.out.println(s+"("+svio+","+eat.getEarliestArrivalTime(s)+") ");
				if(XR.isTerminatingPoint(s))
					break;
				s  = XR.next(s);
			}while(true);
			//System.out.println();
		}
	}

	private void propagate(int k)
	{
		for(Point v = XR.getStartingPointOfRoute(k); v!= XR.getTerminatingPointOfRoute(k); v = XR.oldNext(v))
		{
			violations -= vio.get(v);
			vio.put(v,computeViolations(v));
			violations += vio.get(v);
		}
	}
	
	private void propagateAddPoint(int k)
	{
		for(Point v = XR.getStartingPointOfRoute(k); v!= XR.getTerminatingPointOfRoute(k); v = XR.next(v)){
			violations -= vio.get(v);
			vio.put(v,computeViolations(v));
			violations += vio.get(v);
		}
	}
	
	private void propagateRemovePoint(int k)
	{
		for(Point v = XR.getStartingPointOfRoute(k); v!= XR.getTerminatingPointOfRoute(k); v = XR.oldNext(v)){
			if(XR.route(v) == Constants.NULL_POINT)
				violations -= vio.get(v);
			else{
				violations -= vio.get(v);
				vio.put(v,computeViolations(v));
				violations += vio.get(v);
			}
		}
	}
	
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		if (kx == ky) {
			propagate(kx);
		} else {
			propagate(kx);
			propagate(ky);
		}
	}

	
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		propagate(kx);
	}

	
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int ky = XR.oldRoute(y);
		
		propagate(kx);
		propagate(ky);
	}

	
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x));
		st.add(XR.oldRoute(y));
		st.add(XR.oldRoute(y));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(x2));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(x2));
		st.add(XR.oldRoute(y1));
		st.add(XR.oldRoute(y2));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(x2));
		st.add(XR.oldRoute(x3));
		st.add(XR.oldRoute(y1));
		st.add(XR.oldRoute(y2));
		st.add(XR.oldRoute(y3));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		TreeSet<Integer>st = new TreeSet<Integer>();
		st.add(XR.oldRoute(x1));
		st.add(XR.oldRoute(x2));
		st.add(XR.oldRoute(y1));
		st.add(XR.oldRoute(y2));
		st.add(XR.oldRoute(x3));
		st.add(XR.oldRoute(y3));
		st.add(XR.oldRoute(x4));
		st.add(XR.oldRoute(y4));
		for(Integer k : st)
			propagate(k);
	}

	
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int k = XR.oldRoute(y);
		vio.put(x, 0);
		propagateAddPoint(k);
	}

	
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		int k = XR.oldRoute(x);
		propagateRemovePoint(k);
	}
	
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2){
		int k = XR.oldRoute(y1);
		vio.put(x1, 0);
		vio.put(x2, 0);
		propagateAddPoint(k);
	}

	public void propagateRemoveTwoPoints(Point x1, Point x2){
		int k = XR.oldRoute(x1);
		propagateRemovePoint(k);
	}
	
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub

	}

	
	public String name() {
		// TODO Auto-generated method stub
		return "CEarliestArrivalTimeVR";
	}

	
	public int violations() {
		// TODO Auto-generated method stub
		return violations;
	}

	void getSegment(Point begin,Point end)
	{
		Point v = begin;
		while(v!=end)
		{
			t_next.put(v , XR.next(v));
			v = XR.next(v);
		}
	}
	void getRevSegment(Point begin,Point end)
	{
		Point v = begin;
		while(v!=end)
		{
			//System.out.println(v);
			t_next.put(v , XR.prev(v));
			v = XR.prev(v);
		}
	}
	
	int calDeltaSegment(Point begin,Point end)
	{
		//System.out.println("start cal delta");
		Point v = begin;
		int delta = 0;
		double dt = eat.getEarliestArrivalTime(v)
				+ eat.getServiceDuration(v);

		while (v != end) {
			//System.out.println(v);
			Point nv = t_next.get(v);
			double at = dt + eat.getTravelTime(v,nv);
			delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv, at);
			//System.out.println(vio.get(nv)+"  -   "+computeViolations(nv,at));
			dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);

			v = nv;
		}
		return delta;
	}
	
	public int evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		//System.out.println(x+"  "+y);
		//System.out.println(XR.index(x)+"  "+XR.index(y));
		int kx = XR.route(x);
		int ky = XR.route(y);
		int delta = 0;
		if (kx == ky) {
			Point v,nv;
			if (XR.index(x) < XR.index(y)) {
				v = XR.prev(x);
				nv = XR.next(x);
				t_next.put(v,nv);
				v = nv;
				while (v != y) {
					t_next.put(v,XR.next(v));
					v = XR.next(v);
				}
				t_next.put(y,x);
				t_next.put(x,XR.next(y));
				v = XR.next(y);
				while (v != XR.getTerminatingPointOfRoute(kx)) {
					t_next.put(v,XR.next(v));
					v = XR.next(v);
				}
				v = XR.prev(x);
				
			} else {
				v = y;
				if(XR.next(v)!=x)
				{
					t_next.put(v, x);
					t_next.put(x, XR.next(v));
					v = XR.next(y);
					while(v != XR.prev(x)){
						t_next.put(v, XR.next(v));
						v = XR.next(v);
					}
					t_next.put(XR.prev(x),XR.next(x));
					getSegment(XR.next(x), XR.endPoint(kx));
					v = y;
				}
				else{
					return 0;
				}
			}
			double dt = eat.getEarliestArrivalTime(v)
					+ eat.getServiceDuration(v);
			while (v != XR.getTerminatingPointOfRoute(kx)) {
				nv = t_next.get(v);
				double at = dt + eat.getTravelTime(v,nv);
				delta = delta - vio.get(nv);
				delta = delta + computeViolations(nv, at);
				dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
						.getEarliestAllowedArrivalTime(nv) : at )
						+ eat.getServiceDuration(nv);

				v = nv;
			}

		} else {
			Point v,nv;
			// process route kx
			v = XR.prev(x);
			double dt = eat.getEarliestArrivalTime(v)
					+ eat.getServiceDuration(v);
			nv = XR.next(x);
			double at = dt + eat.getTravelTime(v,nv);
			delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv,at);
			v = nv;
			dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);

			while (v != XR.getTerminatingPointOfRoute(kx)) {
				nv = XR.next(v);
				at = dt + eat.getTravelTime(v,nv);
				delta = delta - vio.get(nv);
				delta = delta + computeViolations(nv, at);
				dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
						.getEarliestAllowedArrivalTime(nv) : at )
						+ eat.getServiceDuration(nv);

				v = nv;
			}
			
			// process route ky
			v = y;
			dt = eat.getEarliestArrivalTime(v)
					+ eat.getServiceDuration(v);
			nv = x;
			at = dt + eat.getTravelTime(v,nv);
			delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv,at);
			
			v = x;
			dt = ( at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);
			nv = XR.next(y);
			at = dt + eat.getTravelTime(v,nv);
			delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv,at);
			
			v = nv;
			dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);
			while (v != XR.getTerminatingPointOfRoute(ky)) {
				nv = XR.next(v);
				at = dt + eat.getTravelTime(v,nv);
				delta = delta - vio.get(nv);
				delta = delta + computeViolations(nv, at);
				dt = ( at < eat.getEarliestAllowedArrivalTime(nv) ? eat
						.getEarliestAllowedArrivalTime(nv) : at )
						+ eat.getServiceDuration(nv);

				v = nv;
			}
			
		}
		return delta;
	}

	// move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depots, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
	
	public int evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.oldRoute(x);
		int delta = 0;
		Point v,nv;
		if (XR.index(x) < XR.index(y)) {
			v = XR.prev(x);
			nv = XR.next(x);
			t_next.put(v,nv);
			v = nv;
			while (v != y) {
				t_next.put(v,XR.next(v));
				v = XR.next(v);
			}
			t_next.put(y,x);
			t_next.put(x,XR.next(y));
			v = XR.next(y);
			while (v != XR.getTerminatingPointOfRoute(kx)) {
				t_next.put(v,XR.next(v));
				v = XR.next(v);
			}
			v = XR.prev(x);
			
		} else {
			v = y;
			if(XR.next(v)!=x)
			{
				t_next.put(v, x);
				t_next.put(x, XR.next(v));
				v = XR.next(y);
				while(v != XR.prev(x)){
					t_next.put(v, XR.next(v));
					v = XR.next(v);
				}
				t_next.put(XR.prev(x),XR.next(x));
				getSegment(XR.next(x), XR.endPoint(kx));
				v = y;
			}
			else{
				return 0;
			}
		}
		double dt = eat.getEarliestArrivalTime(v)
				+ eat.getServiceDuration(v);
		while (v != XR.getTerminatingPointOfRoute(kx)) {
			nv = t_next.get(v);
			double at = dt + eat.getTravelTime(v,nv);
			delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv, at);
			dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);

			v = nv;
		}
		return delta;
	}

	// move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next(y))
	
	public int evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub

		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(x,y);
		
		getRevSegment(y,XR.getStartingPointOfRoute(ky));
		
		
		t_next.put(nx,ny);
		getRevSegment(XR.getTerminatingPointOfRoute(kx),nx);
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		int delta  = 0;
		
		delta += calDeltaSegment(x, XR.getStartingPointOfRoute(ky));
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(kx), XR.getTerminatingPointOfRoute(ky));
		
		return delta;
	}

	
	public int evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(y, x);
		
		getRevSegment(x,XR.getStartingPointOfRoute(kx));
		
		
		t_next.put(nx,ny);
		getRevSegment(XR.getTerminatingPointOfRoute(kx),nx);
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		int delta  = 0;
		
		delta += calDeltaSegment(y, XR.getStartingPointOfRoute(kx));
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(kx), XR.getTerminatingPointOfRoute(ky));
		
		return delta;
	}

	
	public int evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(x,y);
		
		getRevSegment(y,XR.getStartingPointOfRoute(ky));
		
		
		t_next.put(ny, nx);
		getRevSegment(XR.getTerminatingPointOfRoute(ky),ny);
		getSegment(nx,XR.getTerminatingPointOfRoute(kx));
		
		int delta  = 0;
		
		delta += calDeltaSegment(x, XR.getStartingPointOfRoute(ky));
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(ky), XR.getTerminatingPointOfRoute(kx));
		
		return delta;
	}

	
	public int evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(y, x);
		
		getRevSegment(x,XR.getStartingPointOfRoute(kx));
		
		
		t_next.put(ny,nx);
		getRevSegment(XR.getTerminatingPointOfRoute(ky),ny);
		
		getSegment(nx,XR.getTerminatingPointOfRoute(kx));

		int delta  = 0;
		delta += calDeltaSegment(y, XR.getStartingPointOfRoute(kx));
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(ky), XR.getTerminatingPointOfRoute(kx));
		
		return delta;
	}

	
	public int evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);

		t_next.put(x, ny);
		
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		
		t_next.put(y, nx);
		//getRevSegment(XR.getTerminatingPointOfRoute(ky),ny);
		getSegment(nx,XR.getTerminatingPointOfRoute(kx));

		int delta  = 0;
		delta += calDeltaSegment(y, XR.getTerminatingPointOfRoute(kx));
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(ky));
		
		return delta;
	}

	
	public int evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(ny, x);
		
		getRevSegment(x,XR.getStartingPointOfRoute(kx));
		getRevSegment(XR.getTerminatingPointOfRoute(ky),ny);
		
		t_next.put(y, nx);
		getSegment(nx,XR.getTerminatingPointOfRoute(kx));
		
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(ky), XR.getStartingPointOfRoute(kx));
		delta += calDeltaSegment(nx,XR.getTerminatingPointOfRoute(kx));

		return delta;
	}

	
	public int evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(x, ny);
		
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		t_next.put(nx, y);
		getRevSegment(XR.getTerminatingPointOfRoute(kx),nx);
		getRevSegment(y,XR.getStartingPointOfRoute(ky));
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(kx), XR.getStartingPointOfRoute(ky));
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(ky));
		
		return delta;
	}

	
	public int evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x);
		int ky = XR.route(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		
		t_next.put(ny, x);
		
		getRevSegment(x,XR.getStartingPointOfRoute(kx));
		getRevSegment(XR.getTerminatingPointOfRoute(ky),ny);
		
		t_next.put(nx, y);
		getRevSegment(XR.getTerminatingPointOfRoute(kx),nx);
		getRevSegment(y,XR.getStartingPointOfRoute(ky));
		
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(kx),XR.getStartingPointOfRoute(ky));
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(ky),XR.getStartingPointOfRoute(kx));
		
		return delta;
	}

	
	public int evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x1);
		int ky = XR.route(y);
		Point px1 = XR.prev(x1);
		Point nx2 = XR.next(x2);
		Point ny = XR.next(y);
		
		t_next.put(px1,nx2);
		getSegment(nx2, XR.getTerminatingPointOfRoute(kx));
		
		t_next.put(y, x1);
		getSegment(x1,x2);
		t_next.put(x2,ny);
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		int delta  = 0;
		delta += calDeltaSegment(px1, XR.getTerminatingPointOfRoute(kx));
		delta += calDeltaSegment(y, XR.getTerminatingPointOfRoute(ky));

		return delta;
	}

	
	public int evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		int kx = XR.route(x1);
		int ky = XR.route(y);
		Point px1 = XR.prev(x1);
		Point nx2 = XR.next(x2);
		Point ny = XR.next(y);
		
		t_next.put(px1, nx2);
		getSegment(nx2, XR.getTerminatingPointOfRoute(kx));
		
		t_next.put(y, x2);
		getRevSegment(x2,x1);
		t_next.put(x1, ny);
		getSegment(ny,XR.getTerminatingPointOfRoute(ky));
		
		int delta  = 0;
		delta += calDeltaSegment(px1, XR.getTerminatingPointOfRoute(kx));
		delta += calDeltaSegment(y, XR.getTerminatingPointOfRoute(ky));
		
		return delta;
	}

	
	public int evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		
		t_next.put(x,z);
		getRevSegment(z,ny);
		t_next.put(ny,nx);
		getSegment(nx,y);
		t_next.put(y,nz);
		getSegment(nz,XR.getTerminatingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(k));
		return delta;
	}

	
	public int evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		
		getRevSegment(XR.getTerminatingPointOfRoute(k),nz);
		t_next.put(nz, y);
		getRevSegment(y,nx);
		t_next.put(nx,ny);
		getSegment(ny,z);
		t_next.put(z, x);
		getRevSegment(x,XR.getStartingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(k), XR.getStartingPointOfRoute(k));
		return delta;
	}

	
	public int evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

	
		t_next.put(x,y);
		getRevSegment(y,nx);
		t_next.put(nx, z);
		getRevSegment(z,ny);
		t_next.put(ny,nz);
		getSegment(nz,XR.getTerminatingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(k));
		
		return delta;
	}

	
	public int evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		
		getRevSegment(XR.getTerminatingPointOfRoute(k),nz);
		t_next.put(nz,ny);
		getSegment(ny,z);
		t_next.put(z, nx);
		getSegment(nx,y);
		t_next.put(y,x);
		getRevSegment(x,XR.getStartingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(k), XR.getStartingPointOfRoute(k));

		return delta;
	}

	
	public int evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		t_next.put(x, ny);
		getSegment(ny,z);
		t_next.put(z,nx);
		getSegment(nx,y);
		t_next.put(y,nz);
		getSegment(nz,XR.getTerminatingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(k));
		
		return delta;
	}

	
	public int evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		getRevSegment(XR.getTerminatingPointOfRoute(k),nz);
		t_next.put(nz, y);
		getRevSegment(y,nx);
		t_next.put(nx, z);
		getRevSegment(z,ny);
		t_next.put(ny, x);
		getRevSegment(x,XR.getStartingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(k), XR.getStartingPointOfRoute(k));

		return delta;
	}

	
	public int evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		t_next.put(x, ny);
		getSegment(ny,z);
		t_next.put(z,y);
		getRevSegment(y,nx);
		t_next.put(nx, nz);
		getSegment(nz,XR.getTerminatingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(x, XR.getTerminatingPointOfRoute(k));
		
		return delta;
	}

	
	public int evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		Point nz = XR.next(z);

		getRevSegment(XR.getTerminatingPointOfRoute(k),nz);
		t_next.put(nz, nx);
		getSegment(nx,y);
		t_next.put(y, z);
		getRevSegment(z,ny);
		t_next.put(ny, x);
		getRevSegment(x,XR.getStartingPointOfRoute(k));
		
		int delta  = 0;
		delta += calDeltaSegment(XR.getTerminatingPointOfRoute(k), XR.getStartingPointOfRoute(k));
		
		return delta;
	}

	
	public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		Point nx1 = XR.next(x1);
		Point ny1 = XR.next(y1);
		Point nx2 = XR.next(x2);
		Point ny2 = XR.next(y2);
		int k1 = XR.route(x1);
		int k2 = XR.route(x2);
		
		t_next.put(x1, nx2);
		getSegment(nx2, y2);
		t_next.put(y2,ny1);
		getSegment(ny1, XR.getTerminatingPointOfRoute(k1));
		
		t_next.put(x2, nx1);
		getSegment(nx1, y1);
		t_next.put(y1, ny2);
		getSegment(ny2, XR.getTerminatingPointOfRoute(k2));
		
		int delta  = 0;
		delta += calDeltaSegment(x1, XR.getTerminatingPointOfRoute(k1));
		delta += calDeltaSegment(x2, XR.getTerminatingPointOfRoute(k2));
	
		return delta;
	}

	    // remove x1, x2 from their current routes
		// x1 , x2 in same route , index x1 < index x2
		// y1, y2 in same route , index y1 < index y2
		// route of x1 != route of y1
		// re-insert x1 between y1 and next[y1]
		// re-insert x2 between y2 and next[y2]	
		
		public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
			// TODO Auto-generated method stub
			ArrayList<Point> x = new ArrayList<Point>();
			ArrayList<Point> y = new ArrayList<Point>();
			x.add(x1);
			x.add(x2);
			y.add(y1);
			y.add(y2);
			return evaluateKPointsMove(x, y);
		}

	
	public int evaluateThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		// TODO Auto-generated method stub
		ArrayList<Point> x = new ArrayList<Point>();
		ArrayList<Point> y = new ArrayList<Point>();
		x.add(x1);
		x.add(x2);
		x.add(x3);
		y.add(y1);
		y.add(y2);
		y.add(y3);
		return evaluateKPointsMove(x, y);
	}

	
	public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		ArrayList<Point> x = new ArrayList<Point>();
		ArrayList<Point> y = new ArrayList<Point>();
		x.add(x1);
		x.add(x2);
		x.add(x3);
		y.add(y1);
		y.add(y2);
		y.add(y3);
		return evaluateKPointsMove(x, y);
	}

	
	public int evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int k = XR.route(y);
		Point ny = XR.next(y);
		t_next.put(y, x);
		t_next.put(x, ny);
		vio.put(x, 0);
		getSegment(ny, XR.getTerminatingPointOfRoute(k));
		int delta = 0;
		//System.out.println("tnexxt");
		//for(int i  =0; i < t_next.size(); i++)
			//System.out.println(t_next.get(i));
		delta += calDeltaSegment(y, XR.getTerminatingPointOfRoute(k));
		return delta;
	}

	
	public int evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		int k = XR.route(x);
		Point px = XR.prev(x);
		Point nx = XR.next(x);
		t_next.put(px,nx);
		getSegment(nx, XR.getTerminatingPointOfRoute(k));
		
		return calDeltaSegment(px, XR.getTerminatingPointOfRoute(k));
	}

	public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2){
		int k = XR.route(y1);
		Point ny1 = XR.next(y1);
		t_next.put(y1, x1);
		vio.put(x1, 0);
		vio.put(x2, 0);
		if(y1 != y2){
			t_next.put(x1, ny1);
			getSegment(ny1, y2);
			Point ny2 = XR.next(y2);
			t_next.put(y2, x2);
			t_next.put(x2, ny2);
			getSegment(ny2, XR.getTerminatingPointOfRoute(k));
		}
		else{
			t_next.put(x1, x2);
			t_next.put(x2, ny1);
			getSegment(ny1, XR.getTerminatingPointOfRoute(k));
		}
		return calDeltaSegment(y1, XR.getTerminatingPointOfRoute(k));
	}
	
	public int evaluateRemoveTwoPoints(Point x1, Point x2){
		int k = XR.route(x1);
		Point px1 = XR.prev(x1);
		Point nx1 = XR.next(x1);
		Point px2 = XR.prev(x2);
		Point nx2 = XR.next(x2);
		if(x2 == nx1){
			t_next.put(px1, nx2);
			getSegment(nx2, XR.getTerminatingPointOfRoute(k));
		}
		else{
			t_next.put(px1, nx1);
			getSegment(nx1, px2);
			t_next.put(px2, nx2);
			getSegment(nx2, XR.getTerminatingPointOfRoute(k));
		}
		
		return calDeltaSegment(px1, XR.getTerminatingPointOfRoute(k));
	}
	
	public int evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		HashSet<Integer> st = new HashSet<Integer>();
		for(Point p : x)
			st.add(XR.oldRoute(p));
		for(Point p : y)
			if( p != CBLSVR.NULL_POINT)
				st.add(XR.oldRoute(p));
		for(int k : st)
		{
			propagate(k);
		}
	}
	private int evaluateVioRoute(int k,Set<Point>out,ArrayList<Point>in,ArrayList<Point>preIn)
	{
		//System.out.println(k+","+KPointsMove.array2String(in)+","+KPointsMove.array2String(preIn));
		//System.out.print("Out : ");
		//for(Point p : out)
			//System.out.print(p.getID()+" ");
		//System.out.println();
		int delta = 0;
		Point s = XR.getStartingPointOfRoute(k);
		Point pre = s;
		delta -= vio.get(s);
		while(!XR.isTerminatingPoint(s))
		{
			Point ns = XR.next(s);
			delta -= vio.get(ns);
			if(!out.contains(ns))
			{
				t_next.put(pre, ns);
				pre = ns;
			}
			s = ns;
		}
		for(int i = in.size()-1; i >= 0; --i)
		{
			Point inp = in.get(i);
			Point preP = preIn.get(i);
			Point nex = t_next.get(preP);
			t_next.put(preP, inp);
			t_next.put(inp, nex);
		}
		Point v = XR.getStartingPointOfRoute(k);
		double dt = eat.getEarliestArrivalTime(v)
				+ eat.getServiceDuration(v);

		while (v != XR.endPoint(k)) {
			//System.out.println(v);
			Point nv = t_next.get(v);
			double at = dt + eat.getTravelTime(v,nv);
			//delta = delta - vio.get(nv);
			delta = delta + computeViolations(nv, at);
			dt = (at < eat.getEarliestAllowedArrivalTime(nv) ? eat
					.getEarliestAllowedArrivalTime(nv) : at )
					+ eat.getServiceDuration(nv);
			v = nv;
		}
		return delta;
	}
	
	public int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		HashMap<Integer,Set<Point>> mout= new HashMap<Integer,Set<Point>>();
		HashMap<Integer,ArrayList<Point>> min = new HashMap<Integer,ArrayList<Point>>();
		HashMap<Integer,ArrayList<Point>> mPrein = new HashMap<Integer,ArrayList<Point>>();
		Set<Integer> sk = new HashSet<Integer>();
		for(int i=0;i<x.size();++i)
		{
			Point px = x.get(i);
			Point py = y.get(i);
			int kx = 0;
			if(XR.contains(px))
			{
				kx = XR.route(px);
				sk.add(kx);
				if(mout.containsKey(kx))
				{
					mout.get(kx).add(px);
				}
				else{
					Set<Point> o = new HashSet<Point>();
					o.add(px);
					mout.put(kx, o);
				}
			}
			if(XR.contains(py))
			{
				int ky = XR.route(py);
				sk.add(ky);
				if(min.containsKey(ky))
				{
					min.get(ky).add(px);
					mPrein.get(ky).add(py);
				}
				else{
					ArrayList<Point>tin = new ArrayList<Point>();
					tin.add(px);
					ArrayList<Point>tprein = new ArrayList<Point>();
					tprein.add(py);
					min.put(ky, tin);
					mPrein.put(ky, tprein);
				}
			}
		}
		int delta = 0;
		for(int k : sk)
		{
			Set<Point>out = new HashSet<Point>();
			ArrayList<Point> in = new ArrayList<Point>();
			ArrayList<Point> prein = new ArrayList<Point>();
			if(mout.containsKey(k))
				out = mout.get(k);
			if(min.containsKey(k))
			{
				in = min.get(k);
				prein = mPrein.get(k);
			}
			delta += evaluateVioRoute(k, out, in, prein);
		}
		//System.out.println(delta);
		return delta;
	}

	@Override
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

}

// ========== vrp.entities.ArcWeightsManager ==========
class ArcWeightsManager implements IDistanceManager{
	private ArrayList<Point> points;
	private HashMap<Point, Integer> map;
	private double[][] weights;
	
	public ArcWeightsManager(ArrayList<Point> points){
		this.points = points;
		map = new HashMap<Point, Integer>();
		for(int i = 0; i < points.size(); i++){
			map.put(points.get(i), i);
			//System.out.println(name() + "::constructor, map.put(" + points.get(i).ID + "," + i + ")");
		}
		weights = new double[points.size()][points.size()];
	}
	public String name(){
		return "ArcWeightsManager";
	}
	public void setWeight(Point p1, Point p2, double w){
		//System.out.println(name() + "::setWeight p1 = " + p1.ID + ", p2 = " + p2.ID + 
		//		", map p1 = " + map.get(p1) + ", map p2 = " + map.get(p2) + ", w = " + w);
		weights[map.get(p1)][map.get(p2)] = w;
	}
	public double getWeight(Point p1, Point p2){
		return weights[map.get(p1)][map.get(p2)];
	}

	public double getDistance(Point x, Point y){
		return getWeight(x,y);
	}
	public double[][] getWeight() {
		return weights;
	}
	public ArrayList<Point> getPoints(){
		return this.points;
	}

}

// ========== vrp.entities.LexMultiValues ==========
class LexMultiValues {
	private ArrayList<Double> values;
	public LexMultiValues(){
		values = new ArrayList<Double>();
	}
	public LexMultiValues(LexMultiValues V){
		values = new ArrayList<Double>();
		for(int i = 0; i < V.size(); i++)
			values.add(V.get(i));
	}
	public LexMultiValues(ArrayList<Double> values){
		this.values = values;
	}
	public LexMultiValues(double v){
		values = new ArrayList<Double>();
		values.add(v);
	}
	public LexMultiValues(double v1, double v2){
		values = new ArrayList<Double>();
		values.add(v1);
		values.add(v2);
	}
	public void fill(int sz, double v){
		values.clear();
		for(int i = 0; i < sz; i++)
			values.add(v);
	}
	public int size(){
		return values.size();
	}
	public void clear(){
		values.clear();
	}
	public void add(double v){
		values.add(v);
	}
	public double get(int i){
		return values.get(i);
	}
	public LexMultiValues plus(LexMultiValues mv){
		ArrayList<Double> A = new ArrayList<Double>();
		for(int i = 0; i < size(); i++)
			A.add(get(i) + mv.get(i));
		return new LexMultiValues(A);
	}
	public boolean lt(LexMultiValues V){
		for(int i = 0; i < values.size(); i++){
			double x = values.get(i);
			double y = V.get(i);
			if (!CBLSVR.equal(x, y)) {
				return x < y; 
			}
		}
		return false;
	}
	public boolean lt(double v){
		for(int i = 0; i < values.size(); i++){
			double x = values.get(i);
			if (!CBLSVR.equal(x, v)) {
				return x < v; 
			}
		}
		return false;
	}
	
	public boolean leq(LexMultiValues V){
		for(int i = 0; i < values.size(); i++){
			double x = values.get(i);
			double y = V.get(i);
			if (!CBLSVR.equal(x, y)) {
				return x < y; 
			}
		}
		return true;
	}
	
	public boolean eq(LexMultiValues V){
		for(int i = 0; i < values.size(); i++){
			double x = values.get(i);
			double y = V.get(i);
			if (!CBLSVR.equal(x, y)) {
				return false; 
			}
		}
		return true;
	}
	public void set(LexMultiValues v){
		values.clear();
		for(int i = 0; i < v.size(); i++){
			values.add(v.get(i));
		}
	}
	public String toString(){
		String s = "";
		for(int i = 0; i < values.size(); i++)
			s = s + values.get(i) + ", ";
		return s;
	}
}

// ========== vrp.entities.NodeWeightsManager ==========
class NodeWeightsManager {
	protected ArrayList<Point> points;
	protected double[] weights;
	protected HashMap<Point, Integer> map;
	public NodeWeightsManager(ArrayList<Point> points){
		this.points = points;
		map = new HashMap<Point, Integer>();
		for(int i = 0; i < points.size(); i++)
			map.put(points.get(i), i);
		//weights = new double[points.size()];
		weights = new double[points.size() < 100 ? 100 : points.size()];
	}
	
	private void scaleUp(){
		double[] t_w = new double[2*weights.length];
		System.arraycopy(weights, 0, t_w, 0, weights.length);
		weights = t_w;
	}
	public void addPoint(Point p){
		if(weights.length == points.size()) scaleUp();
		points.add(p);
		map.put(p, points.size()-1);
	}

	public double getWeight(Point p){
		return weights[map.get(p)];
	}
	public void setWeight(Point p, double w){
		weights[map.get(p)] = w;
	}
	public ArrayList<Point> getPoints(){
		return this.points;
	}
	
	public String name(){
		return "NodeWeightManager";
	}
	public void print(){
		for(int i = 0; i < points.size(); i++){
			System.out.println(name() + "::NodeWeightManager::print, point " + points.get(i).ID);
		}
	}
}

// ========== vrp.entities.Point ==========
class Point {
	public int ID;
    double x, y;
    String locationCode;
    //ArrayList<Integer> bucketIDs;
	public Point(int ID, double x, double y){
    	this.ID = ID;
    	this.x = x; this.y = y;
    	//this.bucketIDs = new ArrayList<Integer>();
    }
    public Point(){
    	this.ID = 0;this.x = 0;this.y = 0;
    }
    public Point(int ID){
    	this.ID = ID; this.x = 0; this.y = 0;
    }
    
    public Point(int ID, String locationCode){
    	this.ID = ID;
    	this.locationCode = locationCode;
    	this.x = 0;
    	this.y = 0;
    }
    
    public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public void setID(int iD) {
		ID = iD;
	}

    public int getID() {
    	return ID;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
//    public ArrayList<Integer> getBucketIDs(){
//    	return bucketIDs;
//    }
//    
//    public void setBucketIDs(ArrayList<Integer> bkIDs){
//    	this.bucketIDs = bkIDs;
//    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    double degrees(Point p) {
        double X = p.x - x;
        double Y = p.y - y;
        double d = Math.toDegrees(Math.atan2(Y, X));
        if (d < 0) d += 360;
        return d;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        //this.bucketIDs = new ArrayList<Integer>();
    }

    public double distance(Point p) {
        return Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
    }
    public double mahattanDistance(Point p){
    	Point p1 = new Point(x,p.y);
    	return distance(p1) + p1.distance(p);
    }
    public Point clone(){
    	return new Point(ID,x,y);
    }
    public String toString(){
    	DecimalFormat df = new DecimalFormat("#.00");
    	return ID + " (" + df.format(x) + "," + df.format(y) + ")";
    }
    public static void main(String[] argn) {
        System.out.print(Math.toDegrees(Math.atan2(-0.5, 1)));
    }
}

// ========== vrp.functions.TotalCostVR ==========
class TotalCostVR implements IFunctionVR {

	private VarRoutesVR XR;
	private VRManager mgr;
	private ArcWeightsManager awm;

	private double value;
	private double[] costLeft;
	private double[] costRight;

	HashMap<Point, Integer> map;

	public TotalCostVR(VarRoutesVR XR, ArcWeightsManager awm) {
		this.XR = XR;
		this.awm = awm;
		mgr = XR.getVRManager();
		post();
	}

	private void post() {
		costLeft = new double[XR.getTotalNbPoints()];
		costRight = new double[XR.getTotalNbPoints()];
		ArrayList<Point> points = XR.getAllPoints();
		map = new HashMap<Point, Integer>();
		for (int i = 0; i < points.size(); i++) {
			map.put(points.get(i), i);
		}
		mgr.post(this);
	}

	private int getIndex(Point p) {
		return map.get(p);
	}

	private double getCostLeft(Point p) {
		return costLeft[getIndex(p)];
	}

	private double getCostRight(Point p) {
		return costRight[getIndex(p)];
	}

	private double getCost(Point p, Point q) {
		return awm.getWeight(p, q);
	}

	private void update(int k) {
		Point sp = XR.getStartingPointOfRoute(k);
		Point tp = XR.getTerminatingPointOfRoute(k);
		costRight[getIndex(sp)] = 0;
		costLeft[getIndex(tp)] = 0;
		if(XR.next(sp) == tp){
			costRight[getIndex(tp)] = 0;
			costLeft[getIndex(sp)] = 0;
			return;
		}
		for (Point u = sp; u != tp; u = XR.next(u)) {
			costRight[getIndex(XR.next(u))] = costRight[getIndex(u)]
					+ awm.getWeight(u, XR.next(u));
		}
		
		for (Point u = tp; u != sp; u = XR.prev(u)) {
			costLeft[getIndex(XR.prev(u))] = costLeft[getIndex(u)]
					+ awm.getWeight(u, XR.prev(u));
		}
	}

	private double calc(Point s, Point t) {
		if (XR.route(s) != XR.route(t)) {
			System.out.println(name() + "::calc(" + s + "," + t
					+ ") EXCEPTION, " + s + " and " + t
					+ " are not the the same route");
			mgr.exit(-1);
		}
		return (XR.isBefore(s, t)) ? getCostRight(t) - getCostRight(s)
				: getCostLeft(t) - getCostLeft(s);
	}

	// @Override
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return mgr;
	}

	// @Override
	public void initPropagation() {
		// TODO Auto-generated method stub
		value = 0;
		for (int i = 1; i <= XR.getNbRoutes(); i++) {
			update(i);
			value += getCostRight(XR.getTerminatingPointOfRoute(i));
		}
	}

	// @Override
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x));
		oldR.add(XR.oldRoute(y));
		for (int r : oldR) {
			value -= getCostRight(XR.getTerminatingPointOfRoute(r));
			update(r);
			value += getCostRight(XR.getTerminatingPointOfRoute(r));
		}
	}

	// @Override
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		if (XR.next(x) == y) {
			propagateTwoPointsMove(y, x, XR.prev(x), XR.prev(x));
		} else if (XR.next(y) == x) {
			propagateTwoPointsMove(x, y, XR.prev(y), XR.prev(y));
		} else {
			propagateTwoPointsMove(x, y, XR.prev(y), XR.prev(x));
		}
	}

	// @Override
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(x)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x1));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double oldY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(x1));
		update(XR.oldRoute(y));
		double newX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double newY = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		double oldV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		update(XR.oldRoute(y));
		double newV = getCostRight(XR
				.getTerminatingPointOfRoute(XR.oldRoute(y)));
		value += newV - oldV;
	}

	// @Override
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x2)));
		update(XR.oldRoute(x1));
		update(XR.oldRoute(x2));
		double newX = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x1)));
		double newY = getCostRight(XR.getTerminatingPointOfRoute(XR
				.oldRoute(x2)));
		value += newX + newY - oldX - oldY;
	}

	// @Override
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1));
		oldR.add(XR.oldRoute(y1));
		oldR.add(XR.oldRoute(x2));
		oldR.add(XR.oldRoute(y2));
		for (int r : oldR) {
			value -= getCostRight(XR.getTerminatingPointOfRoute(r));
			update(r);
			value += getCostRight(XR.getTerminatingPointOfRoute(r));
		}
	}

	// @Override
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1));
		oldR.add(XR.oldRoute(y1));
		oldR.add(XR.oldRoute(x2));
		oldR.add(XR.oldRoute(y2));
		oldR.add(XR.oldRoute(x3));
		oldR.add(XR.oldRoute(y3));
		for (int r : oldR) {
			value -= getCostRight(XR.getTerminatingPointOfRoute(r));
			update(r);
			value += getCostRight(XR.getTerminatingPointOfRoute(r));
		}
	}

	// @Override
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1));
		oldR.add(XR.oldRoute(y1));
		oldR.add(XR.oldRoute(x2));
		oldR.add(XR.oldRoute(y2));
		oldR.add(XR.oldRoute(x3));
		oldR.add(XR.oldRoute(y3));
		oldR.add(XR.oldRoute(x4));
		oldR.add(XR.oldRoute(y4));
		for (int r : oldR) {
			value -= getCostRight(XR.getTerminatingPointOfRoute(r));
			update(r);
			value += getCostRight(XR.getTerminatingPointOfRoute(r));
		}
	}

	// @Override
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int r = XR.route(y);
		value -= getCostRight(XR.getTerminatingPointOfRoute(r));
		update(r);
		value += getCostRight(XR.getTerminatingPointOfRoute(r));
	}

	// @Override
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		int r = XR.oldRoute(x);
		value -= getCostRight(XR.getTerminatingPointOfRoute(r));
		update(r);
		value += getCostRight(XR.getTerminatingPointOfRoute(r));
	}
	
	//@Override
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int r = XR.route(y1);
		value -= getCostRight(XR.getTerminatingPointOfRoute(r));
		update(r);
		value += getCostRight(XR.getTerminatingPointOfRoute(r));
	}

	//@Override
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		int r = XR.oldRoute(x1);
		value -= getCostRight(XR.getTerminatingPointOfRoute(r));
		update(r);
		value += getCostRight(XR.getTerminatingPointOfRoute(r));
	}

	// @Override
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		int r = XR.oldRoute(x);
		value -= getCostRight(XR.getTerminatingPointOfRoute(r));
		update(r);
		value += getCostRight(XR.getTerminatingPointOfRoute(r));
		if (XR.oldRoute(x) != XR.oldRoute(z)) {
			r = XR.oldRoute(z);
			value -= getCostRight(XR.getTerminatingPointOfRoute(r));
			update(r);
			value += getCostRight(XR.getTerminatingPointOfRoute(r));
		}
	}

	// @Override
	public String name() {
		// TODO Auto-generated method stub
		return "TotalCostVR";
	}

	// @Override
	public double getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	// x is before y on the same route
	// remove (x, next[x]) and (y,next[y])
	// add (x,y) and (next[x],next[y])
	public double evaluateTwoOptMoveOneRoute(Point x, Point y) {
		if (!XR.checkPerformTwoOptMoveOneRoute(x, y)) {
			System.out.println(name()
					+ "::evaluateTwoOptMoveOneRoute, check failed");
			System.exit(-1);
		}
		// int idx = getIndex(x);
		// int idy = getIndex(y);
		Point nx = XR.next(x);
		Point ny = XR.next(y);
		return awm.getWeight(x, y) + awm.getWeight(nx, ny) + calc(y, nx)
				- (awm.getWeight(x, nx) + awm.getWeight(y, ny) + calc(nx, y));

	}

	// x is before y on the same route
	// remove (x, next[x]) and (y,next[y])
	// add (x,y) and (next[x],next[y])

	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		if (!XR.checkPerformTwoOptMoveOneRoute(x, y)) {
			System.out.println(name()
					+ "::propagateTwoOptMoveOneRoute, check failed");
			System.exit(-1);
		}
		int k = XR.oldRoute(x);
		value -= getCostRight(XR.getTerminatingPointOfRoute(k));
		update(k);
		value += getCostRight(XR.getTerminatingPointOfRoute(k));
	}

	// @Override
	public double evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformOnePointMove(x, y)) {
			System.out.println(name() + ":: Error evaluateOnePointMove: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		int n = 1;
		Point[] xx = { x };
		Point[] yy = { y };
		double eval = 0;
		HashSet<Point> V = new HashSet<Point>();
		for (int i = 0; i < n; i++) {
			Point prev = XR.prev(xx[i]);
			while (V.contains(prev)) {
				prev = XR.prev(prev);
			}
			Point next = XR.next(xx[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			eval += getCost(prev, next) - getCost(prev, xx[i])
					- getCost(xx[i], next);
			V.add(xx[i]);
		}
		HashMap<Point, Point> M = new HashMap<Point, Point>();
		for (int i = 0; i < n; i++) {
			Point next = XR.next(yy[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			Point prev = yy[i];
			if (M.containsKey(yy[i])) {
				prev = M.get(yy[i]);
			}
			eval += getCost(prev, xx[i]) + getCost(xx[i], next)
					- getCost(prev, next);
			M.put(yy[i], xx[i]);
		}
		return eval;
	}

	// @Override
	public double evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoPointsMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoPointsMove: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		if (XR.next(x) == y) {
			return evaluateTwoPointsMove(y, x, XR.prev(x), XR.prev(x));
		} else if (XR.next(y) == x) {
			return evaluateTwoPointsMove(x, y, XR.prev(y), XR.prev(y));
		} else {
			return evaluateTwoPointsMove(x, y, XR.prev(y), XR.prev(x));
		}
	}

	// @Override
	public double evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove1: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCostRight(x)
				+ getCost(x, y)
				+ calc(y, XR.next(XR.getStartingPointOfRoute(XR.route(y))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(y))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		double newY = 0;
		if (XR.next(x) == XR.getTerminatingPointOfRoute(XR.route(x))) {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)), XR.next(y));
		} else {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.next(x)) + getCost(XR.next(x), XR.next(y));
		}
		newY += calc(XR.next(y), XR.getTerminatingPointOfRoute(XR.route(y)));
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove2: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCost(XR.getStartingPointOfRoute(XR.route(x)),
				XR.next(XR.getStartingPointOfRoute(XR.route(y))))
				+ calc(XR.next(XR.getStartingPointOfRoute(XR.route(y))), y)
				+ getCost(y, x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		double newY = 0;
		if (XR.next(x) == XR.getTerminatingPointOfRoute(XR.route(x))) {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)), XR.next(y));
		} else {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.next(x)) + getCost(XR.next(x), XR.next(y));
		}
		newY += calc(XR.next(y), XR.getTerminatingPointOfRoute(XR.route(y)));
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove3: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCostRight(x)
				+ getCost(x, y)
				+ calc(y, XR.next(XR.getStartingPointOfRoute(XR.route(y))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(y))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		double newY = 0;
		if (XR.next(x) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.next(x));
			} else {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
						+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR
								.route(y))), XR.next(y))
						+ getCost(XR.next(y), XR.next(x));
			}
			newY += calc(XR.next(x),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ getCost(
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		} else {
			if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.getTerminatingPointOfRoute(XR.route(y)));
			} else {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
						+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR
								.route(y))), XR.next(y))
						+ getCost(XR.next(y),
								XR.getTerminatingPointOfRoute(XR.route(y)));
			}
		}

		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove4: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCost(XR.getStartingPointOfRoute(XR.route(x)),
				XR.next(XR.getStartingPointOfRoute(XR.route(y))))
				+ calc(XR.next(XR.getStartingPointOfRoute(XR.route(y))), y)
				+ getCost(y, x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		double newY = 0;
		if (XR.next(x) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.next(x));
			} else {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
						+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR
								.route(y))), XR.next(y))
						+ getCost(XR.next(y), XR.next(x));
			}
			newY += calc(XR.next(x),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ getCost(
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		} else {
			if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.getTerminatingPointOfRoute(XR.route(y)));
			} else {
				newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
						XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
						+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR
								.route(y))), XR.next(y))
						+ getCost(XR.next(y),
								XR.getTerminatingPointOfRoute(XR.route(y)));
			}
		}
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove5: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = 0;
		if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
			newX = getCostRight(x)
					+ getCost(x, XR.getTerminatingPointOfRoute(XR.route(x)));
		} else {
			newX = getCostRight(x)
					+ getCost(x, XR.next(y))
					+ calc(XR.next(y),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
					+ getCost(
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))),
							XR.getTerminatingPointOfRoute(XR.route(x)));
		}
		double newY = 0;
		if (XR.next(x) == XR.getTerminatingPointOfRoute(XR.route(x))) {
			newY = getCostRight(y)
					+ getCost(y, XR.getTerminatingPointOfRoute(XR.route(y)));
		} else {
			newY = getCostRight(y)
					+ getCost(y, XR.next(x))
					+ calc(XR.next(x),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ getCost(
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		}
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove6: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = 0;
		if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
			newX = getCost(XR.getStartingPointOfRoute(XR.route(x)), x)
					+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(x)));
		} else {
			newX = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
					+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))),
							XR.next(y))
					+ getCost(XR.next(y), x)
					+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(x)));
		}
		double newY = 0;
		if (XR.next(x) == XR.getTerminatingPointOfRoute(XR.route(x))) {
			newY = getCostRight(y)
					+ getCost(y, XR.getTerminatingPointOfRoute(XR.route(y)));
		} else {
			newY = getCostRight(y)
					+ getCost(y, XR.next(x))
					+ calc(XR.next(x),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ getCost(
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		}
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove7: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		return evaluateTwoOptMove6(y, x);
	}

	// @Override
	public double evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoOptMove(x, y)) {
			System.out.println(name() + ":: Error evaluateTwoOptMove8: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = 0;
		if (XR.next(y) == XR.getTerminatingPointOfRoute(XR.route(y))) {
			newX = getCost(XR.getStartingPointOfRoute(XR.route(x)), x)
					+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(x)));
		} else {
			newX = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))))
					+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR.route(y))),
							XR.next(y))
					+ getCost(XR.next(y), x)
					+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
							XR.getTerminatingPointOfRoute(XR.route(x)));
		}
		double newY = 0;
		if (XR.next(x) == XR.getTerminatingPointOfRoute(XR.route(x))) {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)), y)
					+ calc(y, XR.next(XR.getStartingPointOfRoute(XR.route(y))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(y))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		} else {
			newY = getCost(XR.getStartingPointOfRoute(XR.route(y)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					+ calc(XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))),
							XR.next(x))
					+ getCost(XR.next(x), y)
					+ calc(y, XR.next(XR.getStartingPointOfRoute(XR.route(y))))
					+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(y))),
							XR.getTerminatingPointOfRoute(XR.route(y)));
		}
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformOrOptMove(x1, x2, y)) {
			System.out.println(name() + ":: Error evaluateOrOptMove1: " + x1
					+ " " + x2 + " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x1)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCostRight(XR.prev(x1))
				+ getCost(XR.prev(x1), XR.next(x2))
				+ calc(XR.next(x2), XR.getTerminatingPointOfRoute(XR.route(x1)));
		double newY = getCostRight(y) + getCost(y, x1) + calc(x1, x2)
				+ getCost(x2, XR.next(y))
				+ calc(XR.next(y), XR.getTerminatingPointOfRoute(XR.route(y)));
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformOrOptMove(x1, x2, y)) {
			System.out.println(name() + ":: Error evaluateOrOptMove2: " + x1
					+ " " + x2 + " " + y + "\n" + XR.toString());
			System.exit(-1);
		}

		double oldX = getCostRight(XR.getTerminatingPointOfRoute(XR.route(x1)));
		double oldY = getCostRight(XR.getTerminatingPointOfRoute(XR.route(y)));
		double newX = getCostRight(XR.prev(x1))
				+ getCost(XR.prev(x1), XR.next(x2))
				+ calc(XR.next(x2), XR.getTerminatingPointOfRoute(XR.route(x1)));
		double newY = getCostRight(y) + getCost(y, x2) + calc(x2, x1)
				+ getCost(x1, XR.next(y))
				+ calc(XR.next(y), XR.getTerminatingPointOfRoute(XR.route(y)));
		return newX + newY - oldX - oldY;
	}

	// @Override
	public double evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove1: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double newV = getCostRight(x);
		newV += getCost(x, z) + calc(z, XR.next(y));
		newV += getCost(XR.next(y), XR.next(x)) + calc(XR.next(x), y);
		newV += getCost(y, XR.next(z))
				+ calc(XR.next(z), XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove2: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double d1 = 0;
		double d2 = 0;
		if (XR.next(z) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			d1 = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					- getCost(XR.getTerminatingPointOfRoute(XR.route(x)),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))));
			d2 = getCostLeft(XR.next(z)) + d1 + getCost(XR.next(z), y);
		} else {
			d2 = getCost(XR.getStartingPointOfRoute(XR.route(x)), y);
		}
		double newV = d2;
		newV += calc(y, XR.next(x));
		newV += getCost(XR.next(x), XR.next(y)) + calc(XR.next(y), z);
		newV += getCost(z, x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));

	}

	// @Override
	public double evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove3: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double newV = getCostRight(x);
		newV += getCost(x, y) + calc(y, XR.next(x));
		newV += getCost(XR.next(x), z) + calc(z, XR.next(y));
		newV += getCost(XR.next(y), XR.next(z))
				+ calc(XR.next(z), XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove4: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double d1 = 0;
		double d2 = 0;
		if (XR.next(z) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			d1 = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					- getCost(XR.getTerminatingPointOfRoute(XR.route(x)),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))));
			d2 = getCostLeft(XR.next(z)) + d1 + getCost(XR.next(z), XR.next(y));
		} else {
			d2 = getCost(XR.getStartingPointOfRoute(XR.route(x)), XR.next(y));
		}
		double newV = d2 + calc(XR.next(y), z);
		newV += getCost(z, XR.next(x)) + calc(XR.next(x), y);
		newV += getCost(y, x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove5: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double newV = getCostRight(x);
		newV += getCost(x, XR.next(y)) + calc(XR.next(y), z);
		newV += getCost(z, XR.next(x)) + calc(XR.next(x), y);
		newV += getCost(y, XR.next(z))
				+ calc(XR.next(z), XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove6: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double d1 = 0;
		double d2 = 0;
		if (XR.next(z) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			d1 = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					- getCost(XR.getTerminatingPointOfRoute(XR.route(x)),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))));
			d2 = getCostLeft(XR.next(z)) + d1 + getCost(XR.next(z), y);
		} else {
			d2 = getCost(XR.getStartingPointOfRoute(XR.route(x)), y);
		}
		double newV = d2 + calc(y, XR.next(x));
		newV += getCost(XR.next(x), z) + calc(z, XR.next(y));
		newV += getCost(XR.next(y), x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove7: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double newV = getCostRight(x);
		newV += getCost(x, XR.next(y)) + calc(XR.next(y), z);
		newV += getCost(z, y) + calc(y, XR.next(x));
		newV += getCost(XR.next(x), XR.next(z))
				+ calc(XR.next(z), XR.getTerminatingPointOfRoute(XR.route(x)));

		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreeOptMove(x, y, z)) {
			System.out.println(name() + ":: Error evaluateThreeOptMove8: " + x
					+ " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double d1 = 0;
		double d2 = 0;
		if (XR.next(z) != XR.getTerminatingPointOfRoute(XR.route(x))) {
			d1 = getCost(XR.getStartingPointOfRoute(XR.route(x)),
					XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))))
					- getCost(XR.getTerminatingPointOfRoute(XR.route(x)),
							XR.prev(XR.getTerminatingPointOfRoute(XR.route(x))));
			d2 = getCostLeft(XR.next(z)) + d1 + getCost(XR.next(z), XR.next(x));
		} else {
			d2 = getCost(XR.getStartingPointOfRoute(XR.route(x)), XR.next(x));
		}
		double newV = d2 + calc(XR.next(x), y);
		newV += getCost(y, z) + calc(z, XR.next(y));
		newV += getCost(XR.next(y), x)
				+ calc(x, XR.next(XR.getStartingPointOfRoute(XR.route(x))))
				+ getCost(XR.next(XR.getStartingPointOfRoute(XR.route(x))),
						XR.getTerminatingPointOfRoute(XR.route(x)));
		return newV - getCostRight(XR.getTerminatingPointOfRoute(XR.route(x)));
	}

	// @Override
	public double evaluateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformCrossExchangeMove(x1, y1, x2, y2)) {
			System.out.println(name() + ":: Error evaluateCrossExchangeMove: "
					+ x1 + " " + y1 + " " + x2 + " " + y2 + "\n"
					+ XR.toString());
			System.exit(-1);
		}

		double oldX1 = calc(x1, XR.next(y1));
		double oldX2 = calc(x2, XR.next(y2));
		double newX1 = getCost(x1, XR.next(x2)) + calc(XR.next(x2), y2)
				+ getCost(y2, XR.next(y1));
		double newX2 = getCost(x2, XR.next(x1)) + calc(XR.next(x1), y1)
				+ getCost(y1, XR.next(y2));
		return newX1 + newX2 - oldX1 - oldX2;
	}

	// @Override
	public double evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformTwoPointsMove(x1, x2, y1, y2)) {
			System.out.println(name() + ":: Error evaluateTwoPointsMove: " + x1
					+ " " + y1 + " " + x2 + " " + y2 + "\n" + XR.toString());
			System.exit(-1);
		}
		int n = 2;
		Point[] xx = { x1, x2 };
		Point[] yy = { y1, y2 };
		double eval = 0;
		HashSet<Point> V = new HashSet<Point>();
		for (int i = 0; i < n; i++) {
			Point prev = XR.prev(xx[i]);
			while (V.contains(prev)) {
				prev = XR.prev(prev);
			}
			Point next = XR.next(xx[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			eval += getCost(prev, next) - getCost(prev, xx[i])
					- getCost(xx[i], next);
			V.add(xx[i]);
		}
		HashMap<Point, Point> M = new HashMap<Point, Point>();
		for (int i = 0; i < n; i++) {
			Point next = XR.next(yy[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			Point prev = yy[i];
			if (M.containsKey(yy[i])) {
				prev = M.get(yy[i]);
			}
			eval += getCost(prev, xx[i]) + getCost(xx[i], next)
					- getCost(prev, next);
			M.put(yy[i], xx[i]);
		}
		return eval;
	}

	// @Override
	public double evaluateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformThreePointsMove(x1, x2, x3, y1, y2, y3)) {
			System.out.println(name() + ":: Error evaluateThreePointsMove: "
					+ x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3
					+ "\n" + XR.toString());
			System.exit(-1);
		}
		int n = 3;
		Point[] xx = { x1, x2, x3 };
		Point[] yy = { y1, y2, y3 };
		double eval = 0;
		HashSet<Point> V = new HashSet<Point>();
		for (int i = 0; i < n; i++) {
			Point prev = XR.prev(xx[i]);
			while (V.contains(prev)) {
				prev = XR.prev(prev);
			}
			Point next = XR.next(xx[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			eval += getCost(prev, next) - getCost(prev, xx[i])
					- getCost(xx[i], next);
			V.add(xx[i]);
		}
		HashMap<Point, Point> M = new HashMap<Point, Point>();
		for (int i = 0; i < n; i++) {
			Point next = XR.next(yy[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			Point prev = yy[i];
			if (M.containsKey(yy[i])) {
				prev = M.get(yy[i]);
			}
			eval += getCost(prev, xx[i]) + getCost(xx[i], next)
					- getCost(prev, next);
			M.put(yy[i], xx[i]);
		}
		return eval;
	}

	// @Override
	public double evaluateFourPointsMove(Point x1, Point x2, Point x3,
			Point x4, Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformFourPointsMove(x1, x2, x3, x4, y1, y2, y3, y4)) {
			System.out.println(name() + ":: Error evaluateFourPointsMove: "
					+ x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3
					+ " " + x4 + " " + y4 + "\n" + XR.toString());
			System.exit(-1);
		}
		int n = 4;
		Point[] xx = { x1, x2, x3, x4 };
		Point[] yy = { y1, y2, y3, y4 };
		double eval = 0;
		HashSet<Point> V = new HashSet<Point>();
		for (int i = 0; i < n; i++) {
			Point prev = XR.prev(xx[i]);
			while (V.contains(prev)) {
				prev = XR.prev(prev);
			}
			Point next = XR.next(xx[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			eval += getCost(prev, next) - getCost(prev, xx[i])
					- getCost(xx[i], next);
			V.add(xx[i]);
		}
		HashMap<Point, Point> M = new HashMap<Point, Point>();
		for (int i = 0; i < n; i++) {
			Point next = XR.next(yy[i]);
			while (V.contains(next)) {
				next = XR.next(next);
			}
			Point prev = yy[i];
			if (M.containsKey(yy[i])) {
				prev = M.get(yy[i]);
			}
			eval += getCost(prev, xx[i]) + getCost(xx[i], next)
					- getCost(prev, next);
			M.put(yy[i], xx[i]);
		}
		return eval;
	}

	// @Override
	public double evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformAddOnePoint(x, y)) {
			System.out.println(name() + ":: Error evaluateAddOnePoint: " + x
					+ " " + y + "\n" + XR.toString());
			System.exit(-1);
		}
		return getCost(y, x) + getCost(x, XR.next(y)) - getCost(y, XR.next(y));
	}

	// @Override
	public double evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformRemoveOnePoint(x)) {
			System.out.println(name() + ":: Error evaluate RemoveOnePoint: "
					+ x + "\n" + XR.toString());
			System.exit(-1);
		}
		return getCost(XR.prev(x), XR.next(x)) - getCost(XR.prev(x), x)
				- getCost(x, XR.next(x));
	}
	
	//@Override
	public double evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformAddTwoPoints(x1, y1, x2, y2)) {
			System.out.println(name() + ":: Error evaluateAddTwoPoints: " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n" + XR.toString());
    		System.exit(-1);
		}
		if(y1 == y2){
			return getCost(y1, x1) + getCost(x1, x2) + getCost(x2, XR.next(y1)) - getCost(y1, XR.next(y1));
		}
		return getCost(y1, x1) + getCost(x1, XR.next(y1)) - getCost(y1, XR.next(y1))
			+ getCost(y2, x2) + getCost(x2, XR.next(y2)) - getCost(y2, XR.next(y2));
	}
	
	//@Override
	public double evaluateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformRemoveTwoPoints(x1, x2)) {
			System.out.println(name() + ":: Error evaluate RemoveTwoPoints: " + x1 + " " + x2 + "\n" + XR.toString());
    		System.exit(-1);
		}
		if(x2 == XR.next(x1)){
			return getCost(XR.prev(x1), XR.next(x2)) - getCost(XR.prev(x1), x1)
					- getCost(x1, x2) - getCost(x2, XR.next(x2));
		}
		return getCost(XR.prev(x1), XR.next(x1)) - getCost(XR.prev(x1), x1) - getCost(x1, XR.next(x1))
				+ getCost(XR.prev(x2), XR.next(x2)) - getCost(XR.prev(x2), x2) - getCost(x2, XR.next(x2));
	}

	// @Override
	public double evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformAddRemovePoints(x, y, z)) {
			System.out.println(name() + ":: Error evaluate AddRemovePoints: "
					+ x + " " + y + " " + z + "\n" + XR.toString());
			System.exit(-1);
		}
		double eval = 0;
		eval -= getCost(XR.prev(x), x) + getCost(x, XR.next(x));
		eval += getCost(XR.prev(x), XR.next(x));
		if (XR.prev(x) == z) {
			eval += getCost(XR.prev(x), y) + getCost(y, XR.next(x));
			eval -= getCost(XR.prev(x), XR.next(x));
		} else {
			eval += getCost(z, y) + getCost(y, XR.next(z));
			eval -= getCost(z, XR.next(z));
		}
		return eval;
	}

	// @Override
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		for (int i = 0; i < x.size(); i++) {
			Point p = x.get(i);
			Point q = y.get(i);
			if (q != CBLSVR.NULL_POINT) {
				oldR.add(XR.oldRoute(p));
				oldR.add(XR.oldRoute(q));
			} else {
				oldR.add(XR.oldRoute(p));
				costRight[getIndex(p)] = costLeft[getIndex(p)] = 0;
			}
		}
		for (int r : oldR) {
			if (r != Constants.NULL_POINT) {
				value -= getCostRight(XR.getTerminatingPointOfRoute(r));
				update(r);
				value += getCostRight(XR.getTerminatingPointOfRoute(r));
			}
		}
	}

	// @Override
	public double evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		if (!XR.checkPerformKPointsMove(x, y)) {
			System.out.println(name() + ":: Error evaluateKPointsMove: \n"
					+ XR.toString());
			System.exit(-1);
		}
		int n = x.size();
		double eval = 0;
		HashSet<Point> V = new HashSet<Point>();
		for (int i = 0; i < n; i++) {
			if (XR.route(x.get(i)) != Constants.NULL_POINT) {
				Point prev = XR.prev(x.get(i));
				while (V.contains(prev)) {
					prev = XR.prev(prev);
				}
				Point next = XR.next(x.get(i));
				while (V.contains(next)) {
					next = XR.next(next);
				}
				eval += getCost(prev, next) - getCost(prev, x.get(i))
						- getCost(x.get(i), next);
				V.add(x.get(i));
			}
		}
		HashMap<Point, Point> M = new HashMap<Point, Point>();
		for (int i = 0; i < n; i++) {
			if (y.get(i) != CBLSVR.NULL_POINT) {
				Point next = XR.next(y.get(i));
				while (V.contains(next)) {
					next = XR.next(next);
				}
				Point prev = y.get(i);
				if (M.containsKey(y.get(i))) {
					prev = M.get(y.get(i));
				}
				eval += getCost(prev, x.get(i)) + getCost(x.get(i), next)
						- getCost(prev, next);
				M.put(y.get(i), x.get(i));
			}
		}
		return eval;
	}

	public static void main(String[] avgr) {
		int N = 60;
		int n = 50;
		int K = 5;
		Point[] p = new Point[N];
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < N; i++) {
			p[i] = new Point(i, rand.nextInt(50), rand.nextInt(50));
		}
		VRManager mgr = new VRManager();
		VarRoutesVR XR = new VarRoutesVR(mgr);
		for (int i = 0; i < n; i++) {
			XR.addClientPoint(p[i]);
		}
		for (int i = n; i < N - K; i++) {
			XR.addRoute(p[i], p[i + K]);
		}
		XR.initSequential();
		ArcWeightsManager awm = new ArcWeightsManager(XR.getAllPoints());
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p.length; j++) {
				awm.setWeight(p[i], p[j], p[i].distance(p[j]));
			}
		}
		IFunctionVR f = new TotalCostVR(XR, awm);

		mgr.close();

		int iter = 0;
		double oldV = 0;
		double newV = 0;
		double delta = 0;
		while (iter < 10000) {
			System.out.println(iter++ + "\n" + XR + "\n");
			// for (int i = 0; i < N; i++) {
			oldV = f.getValue();
			// }
			// int x1 = rand.nextInt(N);
			// int x2 = rand.nextInt(N);
			// int y1 = rand.nextInt(N);
			// int y2 = rand.nextInt(N);
			// int x3 = rand.nextInt(N);
			// int y3 = rand.nextInt(N);
			// int x4 = rand.nextInt(N);
			// int y4 = rand.nextInt(N);
			// while (!XR.checkPerformFourPointsMove(p[x1], p[x2], p[x3], p[x4],
			// p[y1], p[y2], p[y3], p[y4])) {
			// x1 = rand.nextInt(N);
			// y1 = rand.nextInt(N);
			// x2 = rand.nextInt(N);
			// y2 = rand.nextInt(N);
			// x3 = rand.nextInt(N);
			// y3 = rand.nextInt(N);
			// x4 = rand.nextInt(N);
			// y4 = rand.nextInt(N);
			// }
			//
			// System.out.println(p[x1] + " " + p[y1] + " " );
			// //for (int i = 0; i < N; i++) {
			// delta = f.evaluateFourPointsMove(p[x1], p[x2], p[x3], p[x4],
			// p[y1], p[y2], p[y3], p[y4]);
			// //}
			// mgr.performFourPointsMove(p[x1], p[x2], p[x3], p[x4], p[y1],
			// p[y2], p[y3], p[y4]);
			// //for (int i = 0; i < N; i++) {
			// newV = f.getValue();
			// //}
			ArrayList<Point> x = new ArrayList<Point>();
			ArrayList<Point> y = new ArrayList<Point>();
			int count = rand.nextInt(5) + 5;
			for (int i = 0; i < count; i++) {
				x.add(p[rand.nextInt(N)]);
				if (rand.nextInt(5) == 0) {
					y.add(CBLSVR.NULL_POINT);
				} else {
					y.add(p[rand.nextInt(N)]);
				}
			}
			while (!XR.checkPerformKPointsMove(x, y)) {
				x.clear();
				y.clear();
				count = rand.nextInt(5) + 5;
				for (int i = 0; i < count; i++) {
					x.add(p[rand.nextInt(N)]);
					if (rand.nextInt(5) == 0) {
						y.add(CBLSVR.NULL_POINT);
					} else {
						y.add(p[rand.nextInt(N)]);
					}
				}
			}
			System.out.println(count);
			for (int i = 0; i < count; i++) {
				System.out.println(x.get(i) + " " + y.get(i));
			}
			// for (int i = 0; i < N; i++) {
			delta = f.evaluateKPointsMove(x, y);
			// }
			mgr.performKPointsMove(x, y);
			// for (int i = 0; i < N; i++) {
			newV = f.getValue();
			// }
			System.out.println(XR);
			// for (int i = 0; i < N; i++) {
			if (Math.abs(oldV + delta - newV) > 1e-6) {
				System.out.println("WTFFFFFFFFFFFFFFFFFFF " + " " + oldV + " "
						+ delta + " " + newV);
				System.exit(-1);
			}
			// }
		}
		System.out.println("Okkkkkkkkkkkkkk");
	}

}

// ========== vrp.invariants.EarliestArrivalTimeVR ==========
class EarliestArrivalTimeVR implements InvariantVR {
	VarRoutesVR XR;
	ArcWeightsManager awm;
	HashMap<Point, Integer> earliestAllowedArrivalTime;
	HashMap<Point, Integer> serviceDuration;
	private HashMap<Point, Double> earliestArrivalTime;
	public EarliestArrivalTimeVR(VarRoutesVR XR, ArcWeightsManager awm, 
			HashMap<Point, Integer> earliestAllowedArrivalTime, HashMap<Point, Integer> serviceDuration){
		this.XR = XR;
		this.awm = awm;
		this.earliestAllowedArrivalTime = earliestAllowedArrivalTime;
		this.serviceDuration = serviceDuration;
		earliestArrivalTime = new HashMap<Point,Double>();
		getVRManager().post(this);
	}
	
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return XR.getVRManager();
	}

	
	
	public void initPropagation() {
		// TODO Auto-generated method stub
		int nr = XR.getNbRoutes();
		for(int i=1;i<=nr;++i)
			update(i);
	}

	private void update(int k){
		Point s = XR.getStartingPointOfRoute(k);
		earliestArrivalTime.put( s ,1.0*earliestAllowedArrivalTime.get(s));

		for(Point x = s; x != XR.getTerminatingPointOfRoute(k); x = XR.next(x)){
			Point nx = XR.next(x);
			//System.out.println(earliestArrivalTime.get(x) + "  "+serviceDuration.get(x) + "   "+ awm.getDistance(x, nx));
			double tnx = earliestArrivalTime.get(x) + serviceDuration.get(x) + awm.getDistance(x, nx);
			double tmp = tnx > earliestAllowedArrivalTime.get(nx) ? 
					tnx : earliestAllowedArrivalTime.get(nx);
				earliestArrivalTime.put(nx, tmp);
			//System.out.println(x+"  "+nx+"  "+awm.getDistance(x, nx)+"   "+tmp);
		}
	}
	public HashMap<Point,Double> getEarliestArrivalTime(){
		return earliestArrivalTime;
	}
	public double getEarliestArrivalTime(Point v){
		return earliestArrivalTime.get(v);
	}
	
	public HashMap<Point,Integer> getEarliestAllowedArrivalTime(){
		return earliestAllowedArrivalTime;
	}
	public VarRoutesVR getVarRouteVR()
	{
		return XR;
	}
	public double getServiceDuration(Point v)
	{
		return serviceDuration.get(v);
	}
	public HashMap<Point,Integer> getServiceDuration()
	{
		return serviceDuration;
	}
	public double getTravelTime(Point x,Point y)
	{
		return awm.getDistance(x, y);
	}
	public double getEarliestAllowedArrivalTime(Point v)
	{
		return earliestAllowedArrivalTime.get(v);
	}
	void updateFromPoint(Point x)
	{
		//System.out.println(name() + "::updateFromPoint(" + x.ID + "), earliestArrivalTime = " + earliestArrivalTime.get(x));
		Point pX = XR.prev(x);
		Point p = pX;
		Point nP = x;
		if(XR.isStartingPoint(x))
		{
			p = x;
			nP = XR.next(x);
		}
		//System.out.println(name() + "::updateFromPoint(" + x.ID + "), earliestArrivalTime = " + earliestArrivalTime.get(x) + ", p = " + p.ID + ", nP = " + nP.ID);
		do{
			double curTime = earliestArrivalTime.get(p) + serviceDuration.get(p);
			if(earliestAllowedArrivalTime.get(nP) > curTime + awm.getDistance(p, nP))
			{
				earliestArrivalTime.put(nP, 1.0*earliestAllowedArrivalTime.get(nP));
			}
			else{
				earliestArrivalTime.put(nP,curTime + awm.getDistance(p, nP)) ;
			}
			//System.out.println(name() + "::updateFromPoint(" + x.ID + "), set earliestArrivalTime(" + nP.ID + ") = " + earliestArrivalTime.get(nP));
			p = nP;
			nP = XR.next(p);
		}while(!XR.isTerminatingPoint(p));
		/*
		System.out.println(name() + "::updateFromPoint(" + x.ID + "), after propagate: ");
		for(int k = 1; k <= XR.getNbRoutes(); k++){
			for(Point z = XR.startPoint(k); z != XR.endPoint(k); z = XR.next(z)){
				System.out.println("earliestArrivalTime(" + z.ID + ") = " + earliestArrivalTime.get(z));
			}
			Point z = XR.endPoint(k);
			System.out.println("earliestArrivalTime(" + z.ID + ") = " + earliestArrivalTime.get(z));
		}
		*/
	}
	// x is before y on the same route
	// remove (x, next[x]) and (y,next[y])
	// add (x,y) and (next[x],next[y])
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		System.out.println(name() + "::propagateTwoOptMoveOneRoute NOT IMPLEMENTED YET");
		System.exit(-1);
	}

	
	// move of type a [Groer et al., 2010]
    // move customer x to from route of x to route of y; insert x into the position between y and next[y]
    // x and y are not the depot
	
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		Point oldNexX = XR.oldNext(x);
		updateFromPoint(oldNexX);
		updateFromPoint(x);
	}

	// move of type b [Groer et al., 2010]
    // x and y are on the same route and are not the depots, y locates before x on the route
    // remove (prev[x],x) and (x,next[x]) and (prev[y], y) and (y, next(y)
    // insert (x,prev[y]) and (next[y],x) and (next[x],y) and (y, prev[x])
	
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		update(XR.route(x));
	}

	// move of type c [Groer et al., 2010]
    // x and y are on different routes and are not depots
    // remove (x,next[x]) and (y,next[y])
    // insert (x,y) and (next[x],next(y))
	
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(XR.oldNext(x));
	}

	
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(y);
		updateFromPoint(XR.oldNext(x));
	}

	
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(XR.oldNext(y));
	}

	
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(y);
		updateFromPoint(XR.oldNext(y));
	}

	
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(y);
	}

	
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(y));
		updateFromPoint(y);
	}

	
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(XR.oldNext(x));
	}

	
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(y));
		updateFromPoint(XR.oldNext(x));
	}

	
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldPrev(x1));
		updateFromPoint(y);
		updateFromPoint(x2);
	}

	
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		update(XR.oldRoute(x1));
		update(XR.oldRoute(y));
	}

	
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(XR.oldNext(y));
		updateFromPoint(y);
	}

	
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(z));
		updateFromPoint(XR.oldNext(x));
		updateFromPoint(z);
	}

	
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(XR.oldNext(x));
		updateFromPoint(XR.oldNext(y));
	}

	
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(z));
		updateFromPoint(z);
		updateFromPoint(y);
	}

	
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(z);
		updateFromPoint(y);
	}

	
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(z));
		updateFromPoint(XR.oldNext(x));
		updateFromPoint(XR.oldNext(y));
	}

	
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
		updateFromPoint(z);
		updateFromPoint(XR.oldNext(x));
	}

	
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(z));
		updateFromPoint(y);
		updateFromPoint(XR.oldNext(y));
	}

	
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		updateFromPoint(x1);
		updateFromPoint(x2);
		updateFromPoint(y1);
		updateFromPoint(y2);
	}

	
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	for (int r : oldR) {
    		update(r);
    	}
	}

	
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
    	oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	oldR.add(XR.oldRoute(x3)); 
    	oldR.add(XR.oldRoute(y3));
    	for (int r : oldR) {
    		update(r);
    	}
	}

	
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		HashSet<Integer> oldR = new HashSet<Integer>();
		oldR.add(XR.oldRoute(x1)); 
    	oldR.add(XR.oldRoute(y1));
    	oldR.add(XR.oldRoute(x2)); 
    	oldR.add(XR.oldRoute(y2));
    	oldR.add(XR.oldRoute(x3)); 
    	oldR.add(XR.oldRoute(y3));
    	oldR.add(XR.oldRoute(x4)); 
    	oldR.add(XR.oldRoute(y4));
    	for (int r : oldR) {
    		update(r);
    	}
	}

	
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		updateFromPoint(x);
	}

	
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		updateFromPoint(XR.oldNext(x));
	}

	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		updateFromPoint(x1);
	}

	
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		if(x2 != XR.oldNext(x1))
			updateFromPoint(XR.oldNext(x1));
		else
			updateFromPoint(XR.oldNext(x2));
	}
	
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	
	public String name() {
		// TODO Auto-generated method stub
		return "EarliestArrivalTimeVR";
	}
	
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		/*
		HashMap<Integer,Point> mp = new HashMap<Integer,Point>();
		int i;
		for(i=0;i<x.size();++i)
		{
			Point p = x.get(i);
			if(y.get(i) == CBLSVR.NULL_POINT)
				continue;
			int k = XR.route(p);
			int ind = XR.index(p);
			if(mp.containsKey(k))
			{
				if(XR.index(mp.get(k)) > ind)
					mp.put(k, p);
			}
			else{
				mp.put(k, p);
			}
		}
		Set<Integer> keyset = mp.keySet();
		for(int k : keyset)
		{
			updateFromPoint(mp.get(k));
		}
		*/
		//System.out.print(name() + "::propagateKPointsMove, XR = " + XR.toString() + ", x = ");
		//for(Point p: x) System.out.print(p.ID + ", ");
		//System.out.print(", y = "); for(Point p: y) System.out.print(p.ID + ", ");
		//System.out.println();
		
		Set<Integer> st = new HashSet<Integer>();
		for(Point p : x)
			st.add(XR.oldRoute(p));
		for(Point p : y){
			st.add(XR.oldRoute(p));
		}
		for(int k : st){
			if(k != Constants.NULL_POINT)
				update(k);
		}
	}

}

// ========== vrp.utils.DateTimeUtils ==========
class DateTimeUtils {
	public static String extendDateTime(String dt, int s){// s seconds
		long d = dateTime2Int(dt);
		return unixTimeStamp2DateTime(d + s);
	}
	public static String date2YYYYMMDD(Date d){
		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD");
		
		String strDate = df.format(d).toString();
		String s = d.getDate() + "";
		if(s.length() == 1) s = "0" + s;
		strDate = strDate.substring(0,8) + s;
		return strDate;
	}

	public static Date convertYYYYMMDD2Date(String dt){
		try{
			//System.out.println(name() + "::dateTime2Int, dt = " + dt);
			//String[] s = dt.split(":");
			//if(s.length < 3) dt += ":00";// add :ss to hh:mm --> hh:mm::ss
			dt += " 00:00:00";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(dt);
			return date;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	// return the distance dt1 - dt2 (in seconds)
	public static long distance(String dt1, String dt2){
		return dateTime2Int(dt1) - dateTime2Int(dt2);
	}
	public static String meanDatetime(String dt1, String dt2){
		long u1 = dateTime2Int(dt1);
		long u2 = dateTime2Int(dt2);
		long u = (u1+u2)/2;
		return unixTimeStamp2DateTime(u);
	}
	// in seconds
	public static long dateTime2Int(String dt){
		// convert datetime to int (seconds), datetime example is 2016-10-04 10:30:15
		/*
		String[] s = dt.split(" ");
		String[] d = s[0].split("-");
		int year = Integer.valueOf(d[0]);
		int month = Integer.valueOf(d[1]);
		int day = Integer.valueOf(d[2]);
		String[] t = s[1].split(":");
		int hour = Integer.valueOf(t[0]);
		int minute = Integer.valueOf(t[1]);
		int second = Integer.valueOf(t[2]);
		*/
		try{
			//System.out.println(name() + "::dateTime2Int, dt = " + dt);
			String[] s = dt.split(":");
			if(s.length < 3) dt += ":00";// add :ss to hh:mm --> hh:mm::ss
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(dt);
			return date.getTime()/1000;// in seconds
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	public static String unixTimeStamp2DateTime(long dt){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//dt is measured in seconds and must be converted into milliseconds
		return dateFormat.format(dt*1000);
	}
	
	public static String second2HMS(int hms){
		//String s = "";
		int h = hms/3600;
		int m = (hms - h*3600)/60;
		int s = hms - h*3600 - m*60;
		return h + ":" + m + ":" + s;
	}
	public static long computeStartTimePoint(long early1, long late1, long traveltime, long early2, long late2){
		//[early1,late1] is the time windows of start point
		//[early2,late2] is the time windows of end point
		// travel time from start point to end point
		if(early1 + traveltime >= early2) return early1;
		return early2 - traveltime;
	}
	
	public static int getHour(String dt){
		// date time dt is of format yyyy-mm-dd hh:mm:ss
		// return hour
		String[] s = dt.split(" ");
		String[] s1 = s[1].split(":");
		return Integer.valueOf(s1[0].trim());
	}
	public static int getMinute(String dt){
		// date time dt is of format yyyy-mm-dd hh:mm:ss
		// return hour
		String[] s = dt.split(" ");
		String[] s1 = s[1].split(":");
		return Integer.valueOf(s1[1].trim());
	}
	public static String name(){
		return "DateTimeUtils";
	}
	public static boolean isHighTraffic(String dt){
		int hour = getHour(dt);
		int minute = getMinute(dt);
		int hm = hour*60 + minute;
		//System.out.println(name() + "::isHighTraffic, hm = " + hm);
		return (8*60 <= hm && hm <= 9*60 || 16*60 <= hm && hm <= 18*60);
	}
	
	public static String currentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		Date date = new Date();
		
		//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		String DT = dateFormat.format(date);
		System.out.println(DT);
		String[] s = DT.split(":");
		return s[0] + s[1] + s[2];
	}
	public static void main(String[] args){
		String DT1 = "2016-10-04 10:30:15";
		String DT2 = "2016-10-04 10:31:10";
		
		long t1 = (long)DateTimeUtils.dateTime2Int(DT1);
		long t2 = (long)DateTimeUtils.dateTime2Int(DT2);
		long t = t1 - t2;
		String dt1 = DateTimeUtils.unixTimeStamp2DateTime(t1);
		
		System.out.println("t1 = " + t1 + ", t2 = " + t2 + ", t = " + t + ", dt1 = " + dt1);
		String dt = "2016-03-10 10:30:03";
		System.out.println("hour of " + dt + " is " + DateTimeUtils.getHour(dt));
		
		System.out.println(meanDatetime(DT1, DT2));
		
		System.out.println(DateTimeUtils.unixTimeStamp2DateTime(Integer.MAX_VALUE));
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		Date date = new Date();
		
		//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		String DT = dateFormat.format(date);
		System.out.println(DT);
		
		System.out.println(DateTimeUtils.currentDate());
		
		System.out.println(DateTimeUtils.extendDateTime(dt, 1800));
	}
}

// ========== models.places.DepotContainer ==========
class DepotContainer {
    private String code;
	private String locationCode;
	private int pickupContainerDuration;
	private int deliveryContainerDuration;
	private boolean returnedContainer;
	public boolean getReturnedContainer() {
		return returnedContainer;
	}
	public void setReturnedContainer(boolean returnedContainer) {
		this.returnedContainer = returnedContainer;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public int getPickupContainerDuration() {
		return pickupContainerDuration;
	}
	public void setPickupContainerDuration(int pickupContainerDuration) {
		this.pickupContainerDuration = pickupContainerDuration;
	}
	public int getDeliveryContainerDuration() {
		return deliveryContainerDuration;
	}
	public void setDeliveryContainerDuration(int deliveryContainerDuration) {
		this.deliveryContainerDuration = deliveryContainerDuration;
	}
	public DepotContainer(String code, String locationCode,
			int pickupContainerDuration, int deliveryContainerDuration) {
		super();
		this.code = code;
		this.locationCode = locationCode;
		this.pickupContainerDuration = pickupContainerDuration;
		this.deliveryContainerDuration = deliveryContainerDuration;
	}
	public DepotContainer() {
		super();
		// TODO Auto-generated constructor stub
	}
}

// ========== models.places.DepotMooc ==========
class DepotMooc{
	private String code;
	private String locationCode;
	private int pickupMoocDuration;
	private int deliveryMoocDuration;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public int getPickupMoocDuration() {
		return pickupMoocDuration;
	}
	public void setPickupMoocDuration(int pickupMoocDuration) {
		this.pickupMoocDuration = pickupMoocDuration;
	}
	public int getDeliveryMoocDuration() {
		return deliveryMoocDuration;
	}
	public void setDeliveryMoocDuration(int deliveryMoocDuration) {
		this.deliveryMoocDuration = deliveryMoocDuration;
	}
	public DepotMooc(String code, String locationCode, int pickupMoocDuration,
			int deliveryMoocDuration) {
		super();
		this.code = code;
		this.locationCode = locationCode;
		this.pickupMoocDuration = pickupMoocDuration;
		this.deliveryMoocDuration = deliveryMoocDuration;
	}
	public DepotMooc() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
		
}

// ========== models.places.DepotTruck ==========
class DepotTruck {
	private String code;
	private String locationCode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public DepotTruck(String code, String locationCode) {
		super();
		this.code = code;
		this.locationCode = locationCode;
	}
	public DepotTruck() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}

// ========== models.places.Port ==========
class Port {
	private String code;
	private String locationCode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public Port(String code, String locationCode) {
		super();
		this.code = code;
		this.locationCode = locationCode;
	}
	public Port() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.places.ShipCompany ==========
class ShipCompany {
	private String code;
	private String[] containerDepotCodes;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String[] getContainerDepotCodes() {
		return containerDepotCodes;
	}
	public void setContainerDepotCodes(String[] containerDepotCodes) {
		this.containerDepotCodes = containerDepotCodes;
	}
	public ShipCompany(String code, String[] containerDepotCodes) {
		super();
		this.code = code;
		this.containerDepotCodes = containerDepotCodes;
	}
	public ShipCompany() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.places.Warehouse ==========
class Warehouse {
	private String code;
	private String locationCode;
	private int hardConstraintType;
	private int vehicleConstraintType;
	private int[] drivers;
	private int[] vehicles;
	private Checkin[] checkin;
	private Intervals[] breaktimes;	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public int getHardConstraintType() {
		return hardConstraintType;
	}
	public void setHardConstraintType(int hardConstraintType) {
		this.hardConstraintType = hardConstraintType;
	}
	public int getVehicleConstraintType() {
		return vehicleConstraintType;
	}
	public void setVehicleConstraintType(int vehicleConstraintType) {
		this.vehicleConstraintType = vehicleConstraintType;
	}
	public int[] getDrivers() {
		return drivers;
	}
	public void setDrivers(int[] drivers) {
		this.drivers = drivers;
	}
	public int[] getVehicles() {
		return vehicles;
	}
	public void setVehicles(int[] vehicles) {
		this.vehicles = vehicles;
	}
	public Checkin[] getCheckin(){
		return checkin;
	}
	public void setCheckin(Checkin[] checkin){
		this.checkin = checkin;
	}
	public Intervals[] getBreaktimes(){
		return this.breaktimes;
	}
	public void setBreaktimes(Intervals[] breaktimes){
		this.breaktimes = breaktimes;
	}
	public Warehouse(String code, String locationCode,
			int hardConstraintType,
			int vehicleConstraintType,
			int[] drivers,
			int[] vehicles,
			Checkin[] checkin,
			Intervals[] breaktimes) {
		super();
		this.code = code;
		this.locationCode = locationCode;
		this.hardConstraintType = hardConstraintType;
		this.vehicleConstraintType = vehicleConstraintType;
		this.drivers = drivers;
		this.vehicles = vehicles;
		this.checkin = checkin;
		this.breaktimes = breaktimes;
	}
	public Warehouse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.equipments.Container ==========
class Container {
	
	private String code;
	private double weight;
	private String categoryCode;
	private String depotContainerCode;
	private String[] returnDepotCodes;// possible depots when finishing services
	private boolean importedContainer;
	private String shipCompanyCode;
	
	
	public Container(String code, double weight, String categoryCode,
			String depotContainerCode, String[] returnDepotCodes,
			boolean importedContainer) {
		super();
		this.code = code;
		this.weight = weight;
		this.categoryCode = categoryCode;
		this.depotContainerCode = depotContainerCode;
		this.returnDepotCodes = returnDepotCodes;
		this.importedContainer = importedContainer;
	}
	public boolean isImportedContainer() {
		return importedContainer;
	}
	public void setImportedContainer(boolean importedContainer) {
		this.importedContainer = importedContainer;
	}
	public Container(String code, double weight, String categoryCode,
			String depotContainerCode, String[] returnDepotCodes) {
		super();
		this.code = code;
		this.weight = weight;
		this.categoryCode = categoryCode;
		this.depotContainerCode = depotContainerCode;
		this.returnDepotCodes = returnDepotCodes;
	}
	public String[] getReturnDepotCodes() {
		return returnDepotCodes;
	}
	public void setReturnDepotCodes(String[] returnDepotCodes) {
		this.returnDepotCodes = returnDepotCodes;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getDepotContainerCode() {
		return depotContainerCode;
	}
	public void setDepotContainerCode(String depotContainerCode) {
		this.depotContainerCode = depotContainerCode;
	}
	
	public String getShipCompanyCode() {
		return shipCompanyCode;
	}
	public void setShipCompanyCode(String shipCompanyCode) {
		this.shipCompanyCode = shipCompanyCode;
	}
	
	public Container() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.equipments.Mooc ==========
class Mooc {
	private int id;
	private String code;
	private String category;// 20, 40, 45
	private int categoryId;
	private double weight;//20, 40, 45
	private String status;
	private int statusId;
	private String depotMoocCode;
	private String depotMoocLocationCode;
	private String[] returnDepotCodes;// possible depots when finishing services
	private Intervals[] intervals;
	
	public Mooc(int id, String code, String category, int categoryId,
			double weight, String status, int statusId,
			String depotMoocCode, String depotMoocLocationCode, String[] returnDepotCodes, Intervals[] intervals) {
		super();
		this.id = id;
		this.code = code;
		this.category = category;
		this.categoryId = categoryId;
		this.weight = weight;
		this.status = status;
		this.statusId = statusId;
		this.depotMoocCode = depotMoocCode;
		this.depotMoocLocationCode = depotMoocLocationCode;
		this.returnDepotCodes = returnDepotCodes;
		this.intervals = intervals;
	}
	public String[] getReturnDepotCodes() {
		return returnDepotCodes;
	}
	public void setReturnDepotCodes(String[] returnDepotCodes) {
		this.returnDepotCodes = returnDepotCodes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getDepotMoocCode() {
		return depotMoocCode;
	}
	public void setDepotMoocCode(String depotMoocCode) {
		this.depotMoocCode = depotMoocCode;
	}
	public String getDepotMoocLocationCode() {
		return depotMoocLocationCode;
	}
	public void setDepotMoocLocationCode(String depotMoocLocationCode) {
		this.depotMoocLocationCode = depotMoocLocationCode;
	}
	public Intervals[] getIntervals() {
		return intervals;
	}
	public void setIntervals(Intervals[] intervals) {
		this.intervals = intervals;
	}
	
	public Mooc() {
		super();
		// TODO Auto-generated constructor stub
	}
		
}

// ========== models.equipments.MoocGroup ==========
class MoocGroup {
	private int id;
	private String code;
	private MoocPacking[] packing;
	
	public MoocGroup(int id, String code, MoocPacking[] packing){
		this.id = id;
		this.code = code;
		this.packing = packing;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public MoocPacking[] getPacking(){
		return this.packing;
	}
	
	public void setPacking(MoocPacking[] packing){
		this.packing = packing;
	}

	public MoocGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.equipments.MoocPacking ==========
class MoocPacking {
	private String contTypeCode;
	private int contTypeQuantity;
	public MoocPacking(String conTypeCode, int conTypeQuantity){
		this.contTypeCode = conTypeCode;
		this.contTypeQuantity = conTypeQuantity;
	}
	
	public String getContTypeCode(){
		return this.contTypeCode;
	}
	
	public void setContTypeCode(String contTypeCode){
		this.contTypeCode = contTypeCode;
	}
	
	public int getContTypeQuantity(){
		return this.contTypeQuantity;
	}
	
	public void setContTypeQuantity(int contTypeQuantity){
		this.contTypeQuantity = contTypeQuantity;
	}

	public MoocPacking() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}

// ========== models.equipments.Truck ==========
class Truck {
	private int id;
	private String code;
	private double weight;
	private int driverID;
	private String driverCode;
	private String driverName;
	private String depotTruckCode;
	private String depotTruckLocationCode;
	private String startWorkingTime;
	private String endWorkingTime;
	private String status;
	private String[] returnDepotCodes;// possible depots when finishing services
	private Intervals[] intervals;
	
	public Truck(int id, String code, double weight,
			int driverID, String driverCode, String driverName,
			String depotTruckCode, String depotTruckLocationCode, String startWorkingTime,
			String endWorkingTime, String status,
			String[] returnDepotCodes, Intervals[] intervals) {
		super();
		this.id = id;
		this.code = code;
		this.weight = weight;
		this.driverID = driverID;
		this.driverCode = driverCode;
		this.driverName = driverName;
		this.depotTruckCode = depotTruckCode;
		this.depotTruckLocationCode = depotTruckLocationCode;
		this.startWorkingTime = startWorkingTime;
		this.endWorkingTime = endWorkingTime;
		this.status = status;
		this.returnDepotCodes = returnDepotCodes;
		this.intervals = intervals;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String[] getReturnDepotCodes() {
		return returnDepotCodes;
	}
	public void setReturnDepotCodes(String[] returnDepotCodes) {
		this.returnDepotCodes = returnDepotCodes;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getDriverID(){
		return this.driverID;
	}
	public void setDriverID(int driverID){
		this.driverID = driverID;
	}
	public String getDriverCode() {
		return driverCode;
	}
	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public String getDepotTruckCode() {
		return depotTruckCode;
	}
	public void setDepotTruckCode(String depotTruckCode) {
		this.depotTruckCode = depotTruckCode;
	}
	public String getDepotTruckLocationCode() {
		return depotTruckLocationCode;
	}
	public void setDepotTruckLocationCode(String depotTruckLocationCode) {
		this.depotTruckLocationCode = depotTruckLocationCode;
	}
	public String getStartWorkingTime() {
		return startWorkingTime;
	}
	public void setStartWorkingTime(String startWorkingTime) {
		this.startWorkingTime = startWorkingTime;
	}
	public String getEndWorkingTime() {
		return endWorkingTime;
	}
	public void setEndWorkingTime(String endWorkingTime) {
		this.endWorkingTime = endWorkingTime;
	}
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public Intervals[] getIntervals() {
		return intervals;
	}
	public void setIntervals(Intervals[] intervals) {
		this.intervals = intervals;
	}
	public Truck() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.input.ConfigParam ==========
class ConfigParam {
	private int cutMoocDuration;
	private int linkMoocDuration;
	private int hourPrev;
	private int hourPost;
	private String strategy;
	private boolean constraintWarehouseTractor;
	private boolean constraintWarehouseDriver;
	private boolean constraintWarehouseVendor;
	private boolean constraintWarehouseBreaktimes;
	private boolean constraintWarehouseHard;
	private boolean constraintDriverBalance;
	private int unlinkEmptyContainerDuration;
	private int unlinkLoadedContainerDuration;
	private int linkEmptyContainerDuration;
	private int linkLoadedContainerDuration;
	private String currentTime;
	
	
	public ConfigParam(int cutMoocDuration, int linkMoocDuration,
			int hourPrev, int hourPost,
			String strategy,
			boolean constraintWarehouseTractor,
			boolean constraintWarehouseDriver,
			boolean constraintWarehouseVendor,
			boolean constraintWarehouseBreaktimes,
			boolean constraintWarehouseHard,
			boolean constraintDriverBalance,
			int unlinkEmptyContainerDuration,
			int unlinkLoadedContainerDuration,
			int linkEmptyContainerDuration,
			int linkLoadedContainerDuration,
			String currentTime) {
		super();
		this.cutMoocDuration = cutMoocDuration;
		this.linkMoocDuration = linkMoocDuration;
		this.hourPrev = hourPrev;
		this.hourPost = hourPost;
		this.strategy = strategy;
		this.constraintWarehouseTractor = constraintWarehouseTractor;
		this.constraintWarehouseDriver = constraintWarehouseDriver;
		this.constraintWarehouseVendor = constraintWarehouseVendor;
		this.constraintWarehouseBreaktimes = constraintWarehouseBreaktimes;
		this.constraintWarehouseHard = constraintWarehouseHard;
		this.constraintDriverBalance = constraintDriverBalance;
		this.unlinkEmptyContainerDuration = unlinkEmptyContainerDuration;
		this.unlinkLoadedContainerDuration = unlinkLoadedContainerDuration;
		this.linkEmptyContainerDuration = linkEmptyContainerDuration;
		this.linkLoadedContainerDuration = linkLoadedContainerDuration;
		this.currentTime = currentTime;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public int getCutMoocDuration() {
		return cutMoocDuration;
	}
	public void setCutMoocDuration(int cutMoocDuration) {
		this.cutMoocDuration = cutMoocDuration;
	}
	public int getLinkMoocDuration() {
		return linkMoocDuration;
	}
	public void setLinkMoocDuration(int linkMoocDuration) {
		this.linkMoocDuration = linkMoocDuration;
	}
	public ConfigParam(int cutMoocDuration, int linkMoocDuration) {
		super();
		this.cutMoocDuration = cutMoocDuration;
		this.linkMoocDuration = linkMoocDuration;
	}
	public int getHourPrev(){
		return hourPrev;
	}
	public void setHourPrev(int hourPrev){
		this.hourPrev = hourPrev;
	}
	public int getHourPost(){
		return hourPost;
	}
	public void setHourPost(int hourPost){
		this.hourPost = hourPost;
	}
	public boolean getConstraintWarehouseTractor(){
		return this.constraintWarehouseTractor;
	}
	public void setConstraintWarehouseTractor(boolean constraintWarehouseTractor){
		this.constraintWarehouseTractor = constraintWarehouseTractor;
	}
	public boolean getConstraintWarehouseDriver(){
		return this.constraintWarehouseDriver;
	}
	public void setConstraintWarehouseDriver(boolean constraintWarehouseDriver){
		this.constraintWarehouseDriver = constraintWarehouseDriver;
	}
	public boolean getConstraintWarehouseVendor(){
		return this.constraintWarehouseVendor;
	}
	public void setConstraintWarehouseVendor(boolean constraintWarehouseVendor){
		this.constraintWarehouseVendor = constraintWarehouseVendor;
	}
	public boolean getConstraintWarehouseBreaktimes(){
		return this.constraintWarehouseBreaktimes;
	}
	public void setConstraintWarehouseBreaktimes(boolean constraintWarehouseBreaktimes){
		this.constraintWarehouseBreaktimes = constraintWarehouseBreaktimes;
	}
	public boolean getConstraintWarehouseHard(){
		return this.constraintWarehouseHard;
	}
	public void setConstraintWarehouseHard(boolean constraintWarehouseHard){
		this.constraintWarehouseHard = constraintWarehouseHard;
	}
	public boolean getConstraintDriverBalance(){
		return this.constraintDriverBalance;
	}
	public void setConstraintDriverBalance(boolean constraintDriverBalance){
		this.constraintDriverBalance = constraintDriverBalance;
	}
	public int getUnlinkEmptyContainerDuration(){
		return this.unlinkEmptyContainerDuration;
	}
	public void setUnlinkEmptyContainerDuration(int unlinkEmptyContainerDuration){
		this.unlinkEmptyContainerDuration = unlinkEmptyContainerDuration;
	}
	public int getUnlinkLoadedContainerDuration(){
		return this.unlinkLoadedContainerDuration;
	}
	public void setUnlinkLoadedContainerDuration(int unlinkLoadedContainerDuration){
		this.unlinkLoadedContainerDuration = unlinkLoadedContainerDuration;
	}
	public int getLinkEmptyContainerDuration(){
		return this.linkEmptyContainerDuration;
	}
	public void setLinkEmptyContainerDuration(int linkEmptyContainerDuration){
		this.linkEmptyContainerDuration = linkEmptyContainerDuration;
	}
	public int getLinkLoadedContainerDuration(){
		return this.linkLoadedContainerDuration;
	}
	public void setLinkLoadedContainerDuration(int linkLoadedContainerDuration){
		this.linkLoadedContainerDuration = linkLoadedContainerDuration;
	}

	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public ConfigParam() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.input.ContainerTruckMoocInput ==========
class ContainerTruckMoocInput {
	private ExportContainerTruckMoocRequest[] exRequests;
	private ImportContainerTruckMoocRequest[] imRequests;
	private WarehouseTransportRequest[] warehouseRequests;
	
	private ExportEmptyRequests[] exEmptyRequests;
	private ExportLadenRequests[] exLadenRequests;
	private ImportEmptyRequests[] imEmptyRequests;
	private ImportLadenRequests[] imLadenRequests;
	
	
	private ShipCompany[] companies;
	private DepotContainer[] depotContainers;
	private DepotMooc[] depotMoocs;
	private DepotTruck[] depotTrucks;
	private Warehouse[] warehouses;
	private Truck[] trucks;
	private Mooc[] moocs;
	private MoocGroup[] moocGroup;
	private Port[] ports;
	
	private Container[] containers;
	
	DistanceElement[] distance;
	DistanceElement[] travelTime;

	private ConfigParam params;
	

	
	

	public ContainerTruckMoocInput(
			ExportContainerTruckMoocRequest[] exRequests,
			ImportContainerTruckMoocRequest[] imRequests,
			WarehouseTransportRequest[] warehouseRequests,
			ExportEmptyRequests[] exEmptyRequests,
			ExportLadenRequests[] exLadenRequests,
			ImportEmptyRequests[] imEmptyRequests,
			ImportLadenRequests[] imLadenRequests, ShipCompany[] companies,
			DepotContainer[] depotContainers, DepotMooc[] depotMoocs,
			DepotTruck[] depotTrucks, Warehouse[] warehouses, Truck[] trucks,
			Mooc[] moocs, MoocGroup[] moocGroup, Port[] ports, Container[] containers,
			DistanceElement[] distance, DistanceElement[] travelTime,
			ConfigParam params) {
		super();
		this.exRequests = exRequests;
		this.imRequests = imRequests;
		this.warehouseRequests = warehouseRequests;
		this.exEmptyRequests = exEmptyRequests;
		this.exLadenRequests = exLadenRequests;
		this.imEmptyRequests = imEmptyRequests;
		this.imLadenRequests = imLadenRequests;
		this.companies = companies;
		this.depotContainers = depotContainers;
		this.depotMoocs = depotMoocs;
		this.depotTrucks = depotTrucks;
		this.warehouses = warehouses;
		this.trucks = trucks;
		this.moocs = moocs;
		this.moocGroup = moocGroup;
		this.ports = ports;
		this.containers = containers;
		this.distance = distance;
		this.travelTime = travelTime;
		this.params = params;
	}


	public ExportEmptyRequests[] getExEmptyRequests() {
		return exEmptyRequests;
	}


	public void setExEmptyRequests(ExportEmptyRequests[] exEmptyRequests) {
		this.exEmptyRequests = exEmptyRequests;
	}


	public ExportLadenRequests[] getExLadenRequests() {
		return exLadenRequests;
	}


	public void setExLadenRequests(ExportLadenRequests[] exLadenRequests) {
		this.exLadenRequests = exLadenRequests;
	}


	public ImportEmptyRequests[] getImEmptyRequests() {
		return imEmptyRequests;
	}


	public void setImEmptyRequests(ImportEmptyRequests[] imEmptyRequests) {
		this.imEmptyRequests = imEmptyRequests;
	}


	public ImportLadenRequests[] getImLadenRequests() {
		return imLadenRequests;
	}


	public void setImLadenRequests(ImportLadenRequests[] imLadenRequests) {
		this.imLadenRequests = imLadenRequests;
	}

	public Port[] getPorts() {
		return ports;
	}


	public void setPorts(Port[] ports) {
		this.ports = ports;
	}








	public ContainerTruckMoocInput(
			ExportContainerTruckMoocRequest[] exRequests,
			ImportContainerTruckMoocRequest[] imRequests,
			WarehouseTransportRequest[] warehouseRequests,
			ShipCompany[] companies, DepotContainer[] depotContainers,
			DepotMooc[] depotMoocs, DepotTruck[] depotTrucks,
			Warehouse[] warehouses, Truck[] trucks, Mooc[] moocs, MoocGroup[] moocGroup,
			Port[] ports, Container[] containers, DistanceElement[] distance,
			DistanceElement[] travelTime, ConfigParam params) {
		super();
		this.exRequests = exRequests;
		this.imRequests = imRequests;
		this.warehouseRequests = warehouseRequests;
		this.companies = companies;
		this.depotContainers = depotContainers;
		this.depotMoocs = depotMoocs;
		this.depotTrucks = depotTrucks;
		this.warehouses = warehouses;
		this.trucks = trucks;
		this.moocs = moocs;
		this.moocGroup = moocGroup;
		this.ports = ports;
		this.containers = containers;
		this.distance = distance;
		this.travelTime = travelTime;
		this.params = params;
	}


	public Warehouse[] getWarehouses() {
		return warehouses;
	}


	public void setWarehouses(Warehouse[] warehouses) {
		this.warehouses = warehouses;
	}


	public ExportContainerTruckMoocRequest[] getExRequests() {
		return exRequests;
	}


	public void setExRequests(ExportContainerTruckMoocRequest[] exRequests) {
		this.exRequests = exRequests;
	}


	public ImportContainerTruckMoocRequest[] getImRequests() {
		return imRequests;
	}


	public void setImRequests(ImportContainerTruckMoocRequest[] imRequests) {
		this.imRequests = imRequests;
	}


	public WarehouseTransportRequest[] getWarehouseRequests() {
		return warehouseRequests;
	}


	public void setWarehouseRequests(WarehouseTransportRequest[] warehouseRequests) {
		this.warehouseRequests = warehouseRequests;
	}


	public ShipCompany[] getCompanies() {
		return companies;
	}


	public void setCompanies(ShipCompany[] companies) {
		this.companies = companies;
	}


	public DepotContainer[] getDepotContainers() {
		return depotContainers;
	}


	public void setDepotContainers(DepotContainer[] depotContainers) {
		this.depotContainers = depotContainers;
	}


	public DepotMooc[] getDepotMoocs() {
		return depotMoocs;
	}


	public void setDepotMoocs(DepotMooc[] depotMoocs) {
		this.depotMoocs = depotMoocs;
	}


	public DepotTruck[] getDepotTrucks() {
		return depotTrucks;
	}


	public void setDepotTrucks(DepotTruck[] depotTrucks) {
		this.depotTrucks = depotTrucks;
	}


	public Truck[] getTrucks() {
		return trucks;
	}


	public void setTrucks(Truck[] trucks) {
		this.trucks = trucks;
	}


	public Mooc[] getMoocs() {
		return moocs;
	}


	public void setMoocs(Mooc[] moocs) {
		this.moocs = moocs;
	}

	public MoocGroup[] getMoocGroup(){
		return this.moocGroup;
	}
	
	public void setMoocGroup(MoocGroup[] moocGroup){
		this.moocGroup = moocGroup;
	}

	public Container[] getContainers() {
		return containers;
	}


	public void setContainers(Container[] containers) {
		this.containers = containers;
	}


	public DistanceElement[] getDistance() {
		return distance;
	}


	public void setDistance(DistanceElement[] distance) {
		this.distance = distance;
	}


	public DistanceElement[] getTravelTime() {
		return travelTime;
	}


	public void setTravelTime(DistanceElement[] travelTime) {
		this.travelTime = travelTime;
	}


	public ConfigParam getParams() {
		return params;
	}


	public void setParams(ConfigParam params) {
		this.params = params;
	}


	public ContainerTruckMoocInput() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}

// ========== models.input.DistanceElement ==========
class DistanceElement {
	private String srcCode;
	private String destCode;
	private boolean isDriverBalance;
	private int[] drivers;
	private double distance;
	private double travelTime;
	private double d;
	private double t;
	
	
	public DistanceElement(String srcCode, String destCode, 
			boolean isDriverBalance,
			int[] drivers,
			double distance,
			double travelTime,
			double d,
			double t) {
		super();
		this.srcCode = srcCode;
		this.destCode = destCode;
		this.isDriverBalance = isDriverBalance;
		this.drivers = drivers;
		this.distance = distance;
		this.travelTime = travelTime;
		this.d = d;
		this.t = t;
	}
	public double getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(double travelTime) {
		this.travelTime = travelTime;
	}
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getDestCode() {
		return destCode;
	}
	public void setDestCode(String destCode) {
		this.destCode = destCode;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public boolean getIsDriverBalance(){
		return isDriverBalance;
	}
	public void setDriverBalance(boolean isDriverBalance) {
		this.isDriverBalance = isDriverBalance;
	}
	public void setIsDriverBalance(boolean isDriverBalance){
		this.isDriverBalance = isDriverBalance;
	}
	public int[] getDrivers(){
		return this.drivers;
	}
	public void setDrivers(int[] drivers){
		this.drivers = drivers;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public DistanceElement(String srcCode, String destCode, double distance) {
		super();
		this.srcCode = srcCode;
		this.destCode = destCode;
		this.distance = distance;
	}
	public DistanceElement() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.output.StatisticInformation ==========
class StatisticInformation {
    private int totalRequests;
	private int totalRejectedRequests;
	private double totalDistance;
	private int numberTrucks;
	
	public StatisticInformation(int totalRequests, int totalRejectedRequests, double totalDistance, int numberTrucks){
		super();
		this.totalRequests = totalRequests;
		this.totalRejectedRequests = totalRejectedRequests;
		this.totalDistance = totalDistance;
		this.numberTrucks = numberTrucks;
	}
	
	
	public StatisticInformation(){
		super();
		
	}
	
	public int getTotalRequests() {
		return totalRequests;
	}
	public void setTotalRequests(int totalRequests) {
		this.totalRequests = totalRequests;
	}
	public int getTotalRejectedRequests() {
		return totalRejectedRequests;
	}


	public void setTotalRejectedRequests(int totalRejectedRequests) {
		this.totalRejectedRequests = totalRejectedRequests;
	}


	public double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
	public int getNumberTrucks() {
		return numberTrucks;
	}
	public void setNumberTrucks(int numberTrucks) {
		this.numberTrucks = numberTrucks;
	}
}

// ========== models.output.TruckContainerSolution ==========
class TruckContainerSolution {
    private ArrayList<ArrayList<Point>> _route;
	private ArrayList<Point> _rejectPickupPoints;
	private ArrayList<Point> _rejectDeliveryPoints;
	private HashMap<Integer, Integer> _group2marked;
	private HashMap<Point, Integer> _point2Group;

	private double _cost;
	private int _nbTrucks;
	private int _nbReject;
	
	public TruckContainerSolution(VarRoutesVR XR, ArrayList<Point> rejectPickupPoints, 
			ArrayList<Point> rejectDeliveryPoints, double cost, int nbTrucks, int nbReject,
			HashMap<Point, Integer> point2Group, HashMap<Integer, Integer> group2marked){
		this._rejectPickupPoints = new ArrayList<Point>();
		this._rejectDeliveryPoints = new ArrayList<Point>();
		this._group2marked = new HashMap<Integer, Integer>();
		this._point2Group = new HashMap<Point, Integer>();
		
		for(int i=0; i<rejectPickupPoints.size(); i++){
			_rejectPickupPoints.add(rejectPickupPoints.get(i));
		}
		
		for(int i=0; i<rejectDeliveryPoints.size(); i++){
			_rejectDeliveryPoints.add(rejectDeliveryPoints.get(i));
		}
		
		for(int key : group2marked.keySet()){
			_group2marked.put(key, group2marked.get(key));
		}
		
		for(Point key : point2Group.keySet()){
			_point2Group.put(key, point2Group.get(key));
		}
		
		_route = new ArrayList<ArrayList<Point>>();
		
		int K = XR.getNbRoutes();
		
		for(int k=1; k<=K; k++){
			ArrayList<Point> route_k = new ArrayList<Point>();
			Point x = XR.getStartingPointOfRoute(k);
			for(; x != XR.getTerminatingPointOfRoute(k); x = XR.next(x)){
				route_k.add(x);
			}
			route_k.add(x);
			_route.add(route_k);
		}
		
		this._cost = cost;
		this._nbTrucks = nbTrucks;
		this._nbReject = nbReject;
	}
	
	public void copy2XR(VarRoutesVR XR){
		int K = XR.getNbRoutes();
		VRManager mgr = XR.getVRManager();
		mgr.performRemoveAllClientPoints();
		
		for(int k=1; k<=K; k++){
			ArrayList<Point> route_k = _route.get(k-1);
			for(int i=0; i<route_k.size()-2; i++){
				mgr.performAddOnePoint(route_k.get(i+1),route_k.get(i));
			}
		}
	}

	public ArrayList<ArrayList<Point>> get_route() {
		return _route;
	}

	public void set_route(ArrayList<ArrayList<Point>> _route) {
		this._route = _route;
	}

	public ArrayList<Point> get_rejectPickupPoints() {
		return _rejectPickupPoints;
	}

	public HashMap<Point, Integer> get_point2Group() {
		return _point2Group;
	}

	public void set_point2Group(HashMap<Point, Integer> _point2Group) {
		this._point2Group = _point2Group;
	}

	public void set_rejectPickupPoints(ArrayList<Point> _rejectPickupPoints) {
		this._rejectPickupPoints = _rejectPickupPoints;
	}

	public ArrayList<Point> get_rejectDeliveryPoints() {
		return _rejectDeliveryPoints;
	}

	public void set_rejectDeliveryPoints(ArrayList<Point> _rejectDeliveryPoints) {
		this._rejectDeliveryPoints = _rejectDeliveryPoints;
	}

	public double get_cost() {
		return _cost;
	}

	public void set_cost(double _cost) {
		this._cost = _cost;
	}

	public int get_nbTrucks() {
		return _nbTrucks;
	}

	public void set_nbTrucks(int _nbTrucks) {
		this._nbTrucks = _nbTrucks;
	}
	
	public int get_nbReject() {
		return _nbReject;
	}

	public void set_nbReject(int _nbReject) {
		this._nbReject = _nbReject;
	}

	public HashMap<Integer, Integer> get_group2marked() {
		return _group2marked;
	}

	public void set_group2marked(HashMap<Integer, Integer> _group2marked) {
		this._group2marked = _group2marked;
	}
	
	public int getNbRejectedRequests(){
		Set<Integer> grs = new HashSet<Integer>();
		for(int i = 0; i < _rejectPickupPoints.size(); i++){
			Point pickup = _rejectPickupPoints.get(i);
			int groupId = _point2Group.get(pickup);
			
			if(_group2marked.get(groupId) == 1)
				continue;
			grs.add(groupId);
		}
		return grs.size();
	}
}

// ========== models.output.TruckMoocContainerOutputJson ==========
class TruckMoocContainerOutputJson {
    private TruckRoute[] truckRoutes;
	private ExportEmptyRequests[] unscheduledExEmptyRequests;
	private ExportLadenRequests[] unscheduledExLadenRequests;
	private ImportEmptyRequests[] unscheduledImEmptyRequests;
	private ImportLadenRequests[] unscheduledImLadenRequests;
	private StatisticInformation statisticInformation;
	
	public TruckMoocContainerOutputJson(TruckRoute[] truckRoutes,
			ExportEmptyRequests[] unscheduledExEmptyRequests,
			ExportLadenRequests[] unscheduledExLadenRequests,
			ImportEmptyRequests[] unscheduledImEmptyRequests,
			ImportLadenRequests[] unscheduledImLadenRequests,
			StatisticInformation statisticInformation) {
		super();
		this.truckRoutes = truckRoutes;
		this.unscheduledExEmptyRequests = unscheduledExEmptyRequests;
		this.unscheduledExLadenRequests = unscheduledExLadenRequests;
		this.unscheduledImEmptyRequests = unscheduledImEmptyRequests;
		this.unscheduledImLadenRequests = unscheduledImLadenRequests;
		this.statisticInformation = statisticInformation;
	}

	public TruckRoute[] getTruckRoutes() {
		return truckRoutes;
	}

	public void setTruckRoutes(TruckRoute[] truckRoutes) {
		this.truckRoutes = truckRoutes;
	}

	public ExportEmptyRequests[] getUnscheduledExEmptyRequests() {
		return unscheduledExEmptyRequests;
	}

	public void setUnscheduledExEmptyRequests(
			ExportEmptyRequests[] unscheduledExEmptyRequests) {
		this.unscheduledExEmptyRequests = unscheduledExEmptyRequests;
	}

	public ExportLadenRequests[] getUnscheduledExLadenRequests() {
		return unscheduledExLadenRequests;
	}

	public void setUnscheduledExLadenRequests(
			ExportLadenRequests[] unscheduledExLadenRequests) {
		this.unscheduledExLadenRequests = unscheduledExLadenRequests;
	}

	public ImportEmptyRequests[] getUnscheduledImEmptyRequests() {
		return unscheduledImEmptyRequests;
	}

	public void setUnscheduledImEmptyRequests(
			ImportEmptyRequests[] unscheduledImEmptyRequests) {
		this.unscheduledImEmptyRequests = unscheduledImEmptyRequests;
	}

	public ImportLadenRequests[] getUnscheduledImLadenRequests() {
		return unscheduledImLadenRequests;
	}

	public void setUnscheduledImLadenRequests(
			ImportLadenRequests[] unscheduledImLadenRequests) {
		this.unscheduledImLadenRequests = unscheduledImLadenRequests;
	}

	public StatisticInformation getStatisticInformation() {
		return statisticInformation;
	}

	public void setStatisticInformation(StatisticInformation statisticInformation) {
		this.statisticInformation = statisticInformation;
	}
}

// ========== models.requests.DeliveryWarehouseInfo ==========
class DeliveryWarehouseInfo {
	private String wareHouseCode;
	private String earlyDateTimeUnloadAtWarehouse;
	private String lateDateTimeUnloadAtWarehouse;
	private int unloadDuration;
	private int detachLoadedMoocContainerDuration;
	
	// 2nd segment (Warehouse -> depot, Empty)
	private String earlyPickupEmptyContainerAtWarehouse;
	private String latePickupEmptyContainerAtWarehouse;
	private int attachEmptyMoocContainerDuration;
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public String getEarlyDateTimeUnloadAtWarehouse() {
		return earlyDateTimeUnloadAtWarehouse;
	}
	public void setEarlyDateTimeUnloadAtWarehouse(
			String earlyDateTimeUnloadAtWarehouse) {
		this.earlyDateTimeUnloadAtWarehouse = earlyDateTimeUnloadAtWarehouse;
	}
	public String getLateDateTimeUnloadAtWarehouse() {
		return lateDateTimeUnloadAtWarehouse;
	}
	public void setLateDateTimeUnloadAtWarehouse(
			String lateDateTimeUnloadAtWarehouse) {
		this.lateDateTimeUnloadAtWarehouse = lateDateTimeUnloadAtWarehouse;
	}
	public int getUnloadDuration() {
		return unloadDuration;
	}
	public void setUnloadDuration(int unloadDuration) {
		this.unloadDuration = unloadDuration;
	}
	public int getDetachLoadedMoocContainerDuration() {
		return detachLoadedMoocContainerDuration;
	}
	public void setDetachLoadedMoocContainerDuration(
			int detachLoadedMoocContainerDuration) {
		this.detachLoadedMoocContainerDuration = detachLoadedMoocContainerDuration;
	}
	public String getEarlyPickupEmptyContainerAtWarehouse() {
		return earlyPickupEmptyContainerAtWarehouse;
	}
	public void setEarlyPickupEmptyContainerAtWarehouse(
			String earlyPickupEmptyContainerAtWarehouse) {
		this.earlyPickupEmptyContainerAtWarehouse = earlyPickupEmptyContainerAtWarehouse;
	}
	public String getLatePickupEmptyContainerAtWarehouse() {
		return latePickupEmptyContainerAtWarehouse;
	}
	public void setLatePickupEmptyContainerAtWarehouse(
			String latePickupEmptyContainerAtWarehouse) {
		this.latePickupEmptyContainerAtWarehouse = latePickupEmptyContainerAtWarehouse;
	}
	public int getAttachEmptyMoocContainerDuration() {
		return attachEmptyMoocContainerDuration;
	}
	public void setAttachEmptyMoocContainerDuration(
			int attachEmptyMoocContainerDuration) {
		this.attachEmptyMoocContainerDuration = attachEmptyMoocContainerDuration;
	}
	public DeliveryWarehouseInfo(String wareHouseCode,
			String earlyDateTimeUnloadAtWarehouse,
			String lateDateTimeUnloadAtWarehouse, int unloadDuration,
			int detachLoadedMoocContainerDuration,
			String earlyPickupEmptyContainerAtWarehouse,
			String latePickupEmptyContainerAtWarehouse,
			int attachEmptyMoocContainerDuration) {
		super();
		this.wareHouseCode = wareHouseCode;
		this.earlyDateTimeUnloadAtWarehouse = earlyDateTimeUnloadAtWarehouse;
		this.lateDateTimeUnloadAtWarehouse = lateDateTimeUnloadAtWarehouse;
		this.unloadDuration = unloadDuration;
		this.detachLoadedMoocContainerDuration = detachLoadedMoocContainerDuration;
		this.earlyPickupEmptyContainerAtWarehouse = earlyPickupEmptyContainerAtWarehouse;
		this.latePickupEmptyContainerAtWarehouse = latePickupEmptyContainerAtWarehouse;
		this.attachEmptyMoocContainerDuration = attachEmptyMoocContainerDuration;
	}
	public DeliveryWarehouseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.requests.ExportContainerRequest ==========
class ExportContainerRequest {
	private String orderItemID;
	private String orderID;
	private String orderCode;
	private boolean isSwap;
	private String orderItemSwapID;
	private String shipCompanyCode;
	private String depotContainerCode;// depotContainer
	private String containerCategory;// 20, 40, 45
	private String containerCode;
	private String containerNo;
	private double weight;
	private String earlyDateTimePickupAtDepot;
	private String lateDateTimePickupAtDepot;
	
	//private String wareHouseCode;
	//private String earlyDateTimeLoadAtWarehouse;
	//private String lateDateTimeLoadAtWarehouse;
	private int loadDuration;
	//private int detachEmptyMoocContainerDuration;
	
	//private String earlyDateTimePickupLoadedContainerAtWarehouse;
	//private String lateDateTimePickupLoadedContainerAtWarehouse;
	//private int attachLoadedMoocContainerDuration;
	private PickupWarehouseInfo[] pickupWarehouses;
	
	private String portCode;
	private String earlyDateTimeUnloadAtPort;
	private String lateDateTimeUnloadAtPort;
	private int unloadDuration;
	private String customerCode;
	private String customerName;
	
	private int rejectCode;
	
	//private String planSegment;// "1","2","12","13","123",...
	public String getLateDateTimeLoadAtWarehouse(){
		String s = pickupWarehouses[0].getLateDateTimeLoadAtWarehouse();
		for(int i = 1; i < pickupWarehouses.length; i++){
			if(DateTimeUtils.dateTime2Int(s) < DateTimeUtils.dateTime2Int(pickupWarehouses[i].getLateDateTimeLoadAtWarehouse()))
				s = pickupWarehouses[i].getLateDateTimeLoadAtWarehouse();
		}
		return s;
	}
	public String getEarlyDateTimeLoadAtWarehouse(){
		String s = pickupWarehouses[0].getEarlyDateTimeLoadAtWarehouse();
		for(int i = 1; i < pickupWarehouses.length; i++){
			if(DateTimeUtils.dateTime2Int(s) > DateTimeUtils.dateTime2Int(pickupWarehouses[i].getEarlyDateTimeLoadAtWarehouse()))
				s = pickupWarehouses[i].getEarlyDateTimeLoadAtWarehouse();
		}
		return s;
	}
	public ExportContainerRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExportContainerRequest(String orderItemID, boolean isSwap, String orderItemSwapID, String shipCompanyCode,
			String depotContainerCode, String containerCategory, 
			String containerCode, String containerNo, double weight,
			String earlyDateTimePickupAtDepot,
			String lateDateTimePickupAtDepot,
			int loadDuration,
			PickupWarehouseInfo[] pickupWarehouses, String portCode,
			String earlyDateTimeUnloadAtPort, String lateDateTimeUnloadAtPort,
			int unloadDuration, String customerCode, String customerName) {
		super();
		this.orderItemID = orderItemID;
		this.isSwap = isSwap;
		this.orderItemSwapID = orderItemSwapID;
		this.shipCompanyCode = shipCompanyCode;
		this.depotContainerCode = depotContainerCode;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerNo = containerNo;
		this.weight = weight;
		this.earlyDateTimePickupAtDepot = earlyDateTimePickupAtDepot;
		this.lateDateTimePickupAtDepot = lateDateTimePickupAtDepot;
		this.loadDuration = loadDuration;
		this.pickupWarehouses = pickupWarehouses;
		this.portCode = portCode;
		this.earlyDateTimeUnloadAtPort = earlyDateTimeUnloadAtPort;
		this.lateDateTimeUnloadAtPort = lateDateTimeUnloadAtPort;
		this.unloadDuration = unloadDuration;
		this.customerCode = customerCode;
		this.customerName = customerName;
	}

	public String getOrderItemID() {
		return orderItemID;
	}

	public void setOrderItemID(String orderItemID) {
		this.orderItemID = orderItemID;
	}

	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getShipCompanyCode() {
		return shipCompanyCode;
	}

	public boolean getIsSwap(){
		return this.isSwap;
	}
	public void setIsSwap(boolean isSwap){
		this.isSwap = isSwap;
	}
	public String getOrderItemSwapID(){
		return this.orderItemSwapID;
	}
	public void setSwap(boolean isSwap) {
		this.isSwap = isSwap;
	}
	public void setOrderItemSwapID(String orderItemSwapID){
		this.orderItemSwapID = orderItemSwapID;
	}
	public void setShipCompanyCode(String shipCompanyCode) {
		this.shipCompanyCode = shipCompanyCode;
	}

	public String getDepotContainerCode() {
		return depotContainerCode;
	}

	public void setDepotContainerCode(String depotContainerCode) {
		this.depotContainerCode = depotContainerCode;
	}

	public String getContainerCategory() {
		return containerCategory;
	}

	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}

	public String getContainerCode() {
		return containerCode;
	}
	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getEarlyDateTimePickupAtDepot() {
		return earlyDateTimePickupAtDepot;
	}

	public void setEarlyDateTimePickupAtDepot(String earlyDateTimePickupAtDepot) {
		this.earlyDateTimePickupAtDepot = earlyDateTimePickupAtDepot;
	}

	public String getLateDateTimePickupAtDepot() {
		return lateDateTimePickupAtDepot;
	}

	public void setLateDateTimePickupAtDepot(String lateDateTimePickupAtDepot) {
		this.lateDateTimePickupAtDepot = lateDateTimePickupAtDepot;
	}
	
	public int getLoadDuration() {
		return loadDuration;
	}

	public void setLoadDuration(int loadDuration) {
		this.loadDuration = loadDuration;
	}

	public PickupWarehouseInfo[] getPickupWarehouses() {
		return pickupWarehouses;
	}

	public void setPickupWarehouses(PickupWarehouseInfo[] pickupWarehouses) {
		this.pickupWarehouses = pickupWarehouses;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getEarlyDateTimeUnloadAtPort() {
		return earlyDateTimeUnloadAtPort;
	}

	public void setEarlyDateTimeUnloadAtPort(String earlyDateTimeUnloadAtPort) {
		this.earlyDateTimeUnloadAtPort = earlyDateTimeUnloadAtPort;
	}

	public String getLateDateTimeUnloadAtPort() {
		return lateDateTimeUnloadAtPort;
	}

	public void setLateDateTimeUnloadAtPort(String lateDateTimeUnloadAtPort) {
		this.lateDateTimeUnloadAtPort = lateDateTimeUnloadAtPort;
	}

	public int getUnloadDuration() {
		return unloadDuration;
	}

	public void setUnloadDuration(int unloadDuration) {
		this.unloadDuration = unloadDuration;
	}
	
	public String getCustomerCode(){
		return this.customerCode;
	}
	
	public void setCustomerCode(String customerCode){
		this.customerCode = customerCode;
	}
	
	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}
	public int getRejectCode() {
		return rejectCode;
	}
	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}
	
	
}

// ========== models.requests.ExportContainerTruckMoocRequest ==========
class ExportContainerTruckMoocRequest {
	private String orderID;
	private String orderCode;
	private ExportContainerRequest[] containerRequest;
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public ExportContainerRequest[] getContainerRequest() {
		return containerRequest;
	}
	public void setContainerRequest(ExportContainerRequest[] containerRequest) {
		this.containerRequest = containerRequest;
	}
	public ExportContainerTruckMoocRequest(String orderID, String orderCode,
			ExportContainerRequest[] containerRequest) {
		super();
		this.orderID = orderID;
		this.orderCode = orderCode;
		this.containerRequest = containerRequest;
	}
	public ExportContainerTruckMoocRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}

// ========== models.requests.ExportEmptyRequests ==========
class ExportEmptyRequests {
	private int id;
	private boolean isBreakRomooc;
	private String containerCategory;
	private String containerCode;
	private String containerType;
	private String containerNo;
	private String orderCode;
	private String customerCode;
	private String customerName;
	private String requestDate;
	private String earlyDateTimePickupAtDepot;
	private String lateDateTimePickupAtDepot;
	private String earlyDateTimeLoadAtWarehouse;
	private String lateDateTimeLoadAtWarehouse;
	private String moocCode;
	private double weight;
	private String depotContainerCode;
	private String wareHouseCode;
	private int linkContainerDuration;// thoi gian de dua cont. rong len mooc
	private int rejectCode;
	private int prevStatusID;
	
	public ExportEmptyRequests(int id, boolean isBreakRomooc, String containerCategory,
			String containerCode, String containerType, String containerNo, String orderCode, String customerCode,
			String customerName, String requestDate, String earlyDateTimePickupAtDepot,
			String lateDateTimePickupAtDepot,
			String earlyDateTimeLoadAtWarehouse,
			String lateDateTimeLoadAtWarehouse, String moocCode, double weight,
			String depotContainerCode, String wareHouseCode,
			int linkContainerDuration,
			int prevStatusID) {
		super();
		this.id = id;
		this.isBreakRomooc = isBreakRomooc;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerType = containerType;
		this.containerNo = containerNo;
		this.orderCode = orderCode;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.requestDate = requestDate;
		this.earlyDateTimePickupAtDepot = earlyDateTimePickupAtDepot;
		this.lateDateTimePickupAtDepot = lateDateTimePickupAtDepot;
		this.earlyDateTimeLoadAtWarehouse = earlyDateTimeLoadAtWarehouse;
		this.lateDateTimeLoadAtWarehouse = lateDateTimeLoadAtWarehouse;
		this.moocCode = moocCode;
		this.weight = weight;
		this.depotContainerCode = depotContainerCode;
		this.wareHouseCode = wareHouseCode;
		this.linkContainerDuration = linkContainerDuration;
		this.prevStatusID = prevStatusID;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getLinkContainerDuration() {
		return linkContainerDuration;
	}
	public void setLinkContainerDuration(int linkContainerDuration) {
		this.linkContainerDuration = linkContainerDuration;
	}
	public boolean getIsBreakRomooc() {
		return isBreakRomooc;
	}
	public void setIsBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}
	public void setBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}
	public String getContainerCategory() {
		return containerCategory;
	}
	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}
	public String getContainerCode() {
		return containerCode;
	}
	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}
	public String getContainerType() {
		return containerType;
	}
	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getEarlyDateTimePickupAtDepot() {
		return earlyDateTimePickupAtDepot;
	}
	public void setEarlyDateTimePickupAtDepot(String earlyDateTimePickupAtDepot) {
		this.earlyDateTimePickupAtDepot = earlyDateTimePickupAtDepot;
	}
	public String getLateDateTimePickupAtDepot() {
		return lateDateTimePickupAtDepot;
	}
	public void setLateDateTimePickupAtDepot(String lateDateTimePickupAtDepot) {
		this.lateDateTimePickupAtDepot = lateDateTimePickupAtDepot;
	}
	public String getEarlyDateTimeLoadAtWarehouse() {
		return earlyDateTimeLoadAtWarehouse;
	}
	public void setEarlyDateTimeLoadAtWarehouse(String earlyDateTimeLoadAtWarehouse) {
		this.earlyDateTimeLoadAtWarehouse = earlyDateTimeLoadAtWarehouse;
	}
	public String getLateDateTimeLoadAtWarehouse() {
		return lateDateTimeLoadAtWarehouse;
	}
	public void setLateDateTimeLoadAtWarehouse(String lateDateTimeLoadAtWarehouse) {
		this.lateDateTimeLoadAtWarehouse = lateDateTimeLoadAtWarehouse;
	}
	public String getMoocCode() {
		return moocCode;
	}
	public void setMoocCode(String moocCode) {
		this.moocCode = moocCode;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getDepotContainerCode() {
		return depotContainerCode;
	}
	public void setDepotContainerCode(String depotContainerCode) {
		this.depotContainerCode = depotContainerCode;
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	
	public int getRejectCode() {
		return rejectCode;
	}
	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}
	public int getPrevStatusID() {
		return prevStatusID;
	}
	public void setPrevStatusID(int prevStatusID) {
		this.prevStatusID = prevStatusID;
	}
	//	public ExportEmptyRequests(boolean isBreakRomooc, String containerCategory,
//			String containerCode, String containerNo, String orderCode, String customerCode,
//			String requestDate, String earlyDateTimePickupAtDepot,
//			String lateDateTimePickupAtDepot,
//			String earlyDateTimeLoadAtWarehouse,
//			String lateDateTimeLoadAtWarehouse, String moocCode,
//			String depotContainerCode, String wareHouseCode) {
//		super();
//		this.isBreakRomooc = isBreakRomooc;
//		this.containerCategory = containerCategory;
//		this.containerCode = containerCode;
//		this.containerNo = containerNo;
//		this.orderCode = orderCode;
//		this.customerCode = customerCode;
//		this.requestDate = requestDate;
//		this.earlyDateTimePickupAtDepot = earlyDateTimePickupAtDepot;
//		this.lateDateTimePickupAtDepot = lateDateTimePickupAtDepot;
//		this.earlyDateTimeLoadAtWarehouse = earlyDateTimeLoadAtWarehouse;
//		this.lateDateTimeLoadAtWarehouse = lateDateTimeLoadAtWarehouse;
//		this.moocCode = moocCode;
//		this.depotContainerCode = depotContainerCode;
//		this.wareHouseCode = wareHouseCode;
//	}
	public ExportEmptyRequests() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.requests.ExportLadenRequests ==========
class ExportLadenRequests {
	private int id;
	private boolean isBreakRomooc;
	private String containerCategory;
	private String containerCode;
	private String containerType;
	private String containerNo;
	private String orderCode;
	private String customerCode;
	private String customerName;
	private String requestDate;
	private String earlyDateTimeAttachAtWarehouse;
	private String lateDateTimeUnloadAtPort;
	private String moocCode;
	private double weight;
	private String wareHouseCode;
	private String portCode;
	private int linkContainerAtWarehouseDuration;
	private int releaseLoadedContainerAtPortDuration;
	private int rejectCode;
	private int prevStatusID;
	

	public ExportLadenRequests(int id, boolean isBreakRomooc, String containerCategory,
			String containerCode, String containerType, String containerNo, String orderCode, String customerCode,
			String customerName, String requestDate, String earlyDateTimeAttachAtWarehouse,
			String lateDateTimeUnloadAtPort,
			String moocCode, double weight, String wareHouseCode,
			String portCode, int linkContainerAtWarehouseDuration,
			int releaseLoadedContainerAtPortDuration,
			int prevStatusID) {
		super();
		this.id = id;
		this.isBreakRomooc = isBreakRomooc;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerType = containerType;
		this.containerNo = containerNo;
		this.orderCode = orderCode;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.requestDate = requestDate;
		this.earlyDateTimeAttachAtWarehouse = earlyDateTimeAttachAtWarehouse;
		this.lateDateTimeUnloadAtPort = lateDateTimeUnloadAtPort;
		this.moocCode = moocCode;
		this.weight = weight;
		this.wareHouseCode = wareHouseCode;
		this.portCode = portCode;
		this.linkContainerAtWarehouseDuration = linkContainerAtWarehouseDuration;
		this.releaseLoadedContainerAtPortDuration = releaseLoadedContainerAtPortDuration;
		this.prevStatusID = prevStatusID;
	}
	
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public boolean getIsBreakRomooc() {
		return isBreakRomooc;
	}


	public void setBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}

	public void setIsBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}


	public String getContainerCategory() {
		return containerCategory;
	}


	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}
	
	public String getContainerCode() {
		return containerCode;
	}


	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getContainerNo() {
		return containerNo;
	}


	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}


	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getRequestDate() {
		return requestDate;
	}


	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}


	public String getEarlyDateTimeAttachAtWarehouse() {
		return earlyDateTimeAttachAtWarehouse;
	}


	public void setEarlyDateTimeAttachAtWarehouse(
			String earlyDateTimeAttachAtWarehouse) {
		this.earlyDateTimeAttachAtWarehouse = earlyDateTimeAttachAtWarehouse;
	}

	public String getLateDateTimeUnloadAtPort() {
		return lateDateTimeUnloadAtPort;
	}


	public void setLateDateTimeUnloadAtPort(String lateDateTimeUnloadAtPort) {
		this.lateDateTimeUnloadAtPort = lateDateTimeUnloadAtPort;
	}


	public String getMoocCode() {
		return moocCode;
	}


	public void setMoocCode(String moocCode) {
		this.moocCode = moocCode;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public String getWareHouseCode() {
		return wareHouseCode;
	}


	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}


	public String getPortCode() {
		return portCode;
	}


	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}


	public int getLinkContainerAtWarehouseDuration() {
		return linkContainerAtWarehouseDuration;
	}


	public void setLinkContainerAtWarehouseDuration(
			int linkContainerAtWarehouseDuration) {
		this.linkContainerAtWarehouseDuration = linkContainerAtWarehouseDuration;
	}


	public int getReleaseLoadedContainerAtPortDuration() {
		return releaseLoadedContainerAtPortDuration;
	}


	public void setReleaseLoadedContainerAtPortDuration(
			int releaseLoadedContainerAtPortDuration) {
		this.releaseLoadedContainerAtPortDuration = releaseLoadedContainerAtPortDuration;
	}

	public int getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}

	public int getPrevStatusID() {
		return prevStatusID;
	}

	public void setPrevStatusID(int prevStatusID) {
		this.prevStatusID = prevStatusID;
	}

	public ExportLadenRequests() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.requests.ImportContainerRequest ==========
class ImportContainerRequest {
	private String orderItemID;
	private String orderID;
	private String orderCode;
	private boolean isSwap;
	private String orderItemSwapID;
	// 1st segment (Port -> warehouse, Laden)
	private String shipCompanyCode;
	private String[] depotContainerCode;
	private String containerCategory;// 20, 40, 45
	private String containerCode;
	private String containerNo;
	private double weight;
	private String portCode;
	private String earlyDateTimePickupAtPort;
	private String lateDateTimePickupAtPort;
	private int loadDuration;
	private String customerCode;
	private String customerName;
	
	
	//private String wareHouseCode;
	//private String earlyDateTimeUnloadAtWarehouse;
	//private String lateDateTimeUnloadAtWarehouse;
	private int unloadDuration;
	//private int detachLoadedMoocContainerDuration;
	
	// 2nd segment (Warehouse -> depot, Empty)
	//private String earlyPickupEmptyContainerAtWarehouse;
	//private String latePickupEmptyContainerAtWarehouse;
	//private int attachEmptyMoocContainerDuration;
	private DeliveryWarehouseInfo[] deliveryWarehouses;
	
	
	private String earlyDateTimeDeliveryAtDepot;
	private String lateDateTimeDeliveryAtDepot;
	
	private int rejectCode;
	
	//private String levelRequest;// "1": only 1st requests, "2": only 2nd request, "12": both 1st and 2nd requests

	public ImportContainerRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImportContainerRequest(String orderItemID, boolean isSwap, String orderItemSwapID, String shipCompanyCode,
			String[] depotContainerCode, String containerCategory,
			String containerCode, String containerNo, double weight, String portCode,
			String earlyDateTimePickupAtPort, String lateDateTimePickupAtPort,
			int loadDuration, DeliveryWarehouseInfo[] deliveryWarehouses,
			String earlyDateTimeDeliveryAtDepot,
			String lateDateTimeDeliveryAtDepot, String customerCode, String customerName) {
		super();
		this.orderItemID = orderItemID;
		this.isSwap = isSwap;
		this.orderItemSwapID = orderItemSwapID;
		this.shipCompanyCode = shipCompanyCode;
		this.depotContainerCode = depotContainerCode;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerNo = containerNo;
		this.weight = weight;
		this.portCode = portCode;
		this.earlyDateTimePickupAtPort = earlyDateTimePickupAtPort;
		this.lateDateTimePickupAtPort = lateDateTimePickupAtPort;
		this.loadDuration = loadDuration;
		this.deliveryWarehouses = deliveryWarehouses;
		this.earlyDateTimeDeliveryAtDepot = earlyDateTimeDeliveryAtDepot;
		this.lateDateTimeDeliveryAtDepot = lateDateTimeDeliveryAtDepot;
		this.customerCode = customerCode;
		this.customerName = customerName;
	}
	
	public String getLateDateTimeUnloadAtWarehouse(){
		String s = deliveryWarehouses[0].getLateDateTimeUnloadAtWarehouse();
		for(int i = 1; i < deliveryWarehouses.length; i++){
			if(DateTimeUtils.dateTime2Int(s) < DateTimeUtils.dateTime2Int(deliveryWarehouses[i].getLateDateTimeUnloadAtWarehouse()))
				s = deliveryWarehouses[i].getLateDateTimeUnloadAtWarehouse();
		}
		return s;
	}
	
	public String getEarlyDateTimeUnloadAtWarehouse(){
		String s = deliveryWarehouses[0].getEarlyDateTimeUnloadAtWarehouse();
		for(int i = 1; i < deliveryWarehouses.length; i++){
			if(DateTimeUtils.dateTime2Int(s) > DateTimeUtils.dateTime2Int(deliveryWarehouses[i].getEarlyDateTimeUnloadAtWarehouse()))
				s = deliveryWarehouses[i].getEarlyDateTimeUnloadAtWarehouse();
		}
		return s;
	}

	public String getOrderItemID() {
		return orderItemID;
	}

	public void setSwap(boolean isSwap) {
		this.isSwap = isSwap;
	}

	public void setOrderItemID(String orderItemID) {
		this.orderItemID = orderItemID;
	}
	
	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public boolean getIsSwap(){
		return this.isSwap;
	}
	public void setIsSwap(boolean isSwap){
		this.isSwap = isSwap;
	}
	public String getOrderItemSwapID(){
		return this.orderItemSwapID;
	}
	public void setOrderItemSwapID(String orderItemSwapID){
		this.orderItemSwapID = orderItemSwapID;
	}
	public String getShipCompanyCode() {
		return shipCompanyCode;
	}

	public void setShipCompanyCode(String shipCompanyCode) {
		this.shipCompanyCode = shipCompanyCode;
	}

	public String[] getDepotContainerCode() {
		return depotContainerCode;
	}

	public void setDepotContainerCode(String[] depotContainerCode) {
		this.depotContainerCode = depotContainerCode;
	}

	public String getContainerCategory() {
		return containerCategory;
	}

	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getEarlyDateTimePickupAtPort() {
		return earlyDateTimePickupAtPort;
	}

	public void setEarlyDateTimePickupAtPort(String earlyDateTimePickupAtPort) {
		this.earlyDateTimePickupAtPort = earlyDateTimePickupAtPort;
	}

	public String getLateDateTimePickupAtPort() {
		return lateDateTimePickupAtPort;
	}

	public void setLateDateTimePickupAtPort(String lateDateTimePickupAtPort) {
		this.lateDateTimePickupAtPort = lateDateTimePickupAtPort;
	}

	public int getLoadDuration() {
		return loadDuration;
	}

	public void setLoadDuration(int loadDuration) {
		this.loadDuration = loadDuration;
	}

	public DeliveryWarehouseInfo[] getDeliveryWarehouses() {
		return deliveryWarehouses;
	}

	public void setDeliveryWarehouses(DeliveryWarehouseInfo[] deliveryWarehouses) {
		this.deliveryWarehouses = deliveryWarehouses;
	}

	public String getEarlyDateTimeDeliveryAtDepot() {
		return earlyDateTimeDeliveryAtDepot;
	}

	public void setEarlyDateTimeDeliveryAtDepot(String earlyDateTimeDeliveryAtDepot) {
		this.earlyDateTimeDeliveryAtDepot = earlyDateTimeDeliveryAtDepot;
	}

	public String getLateDateTimeDeliveryAtDepot() {
		return lateDateTimeDeliveryAtDepot;
	}

	public void setLateDateTimeDeliveryAtDepot(String lateDateTimeDeliveryAtDepot) {
		this.lateDateTimeDeliveryAtDepot = lateDateTimeDeliveryAtDepot;
	}
	
	public int getUnloadDuration() {
		return unloadDuration;
	}

	public void setUnloadDuration(int unloadDuration) {
		this.unloadDuration = unloadDuration;
	}
	
	public String getCustomerCode(){
		return this.customerCode;
	}
	
	public void setCustomerCode(String customerCode){
		this.customerCode = customerCode;
	}
	
	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public int getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}
	
}

// ========== models.requests.ImportContainerTruckMoocRequest ==========
class ImportContainerTruckMoocRequest {
	private String orderID;
	private String orderCode;
	private ImportContainerRequest[] containerRequest;
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public ImportContainerRequest[] getContainerRequest() {
		return containerRequest;
	}
	public void setContainerRequest(ImportContainerRequest[] containerRequest) {
		this.containerRequest = containerRequest;
	}
	public ImportContainerTruckMoocRequest(String orderID, String orderCode,
			ImportContainerRequest[] containerRequest) {
		super();
		this.orderID = orderID;
		this.orderCode = orderCode;
		this.containerRequest = containerRequest;
	}
	public ImportContainerTruckMoocRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

// ========== models.requests.ImportEmptyRequests ==========
class ImportEmptyRequests {
	private int id;
	private boolean isBreakRomooc;
	private String containerCategory;
	private String containerCode;
	private String containerType;
	private String containerNo;
	private String orderCode;
	private String customerCode;
	private String customerName;
	private String requestDate;
	private String earlyDateTimeAttachAtWarehouse;
	private String lateDateTimeReturnEmptyAtDepot;
	private String moocCode;
	private double weight;
	private String wareHouseCode;
	private String depotContainerCode;
	private int linkContainerDuration;// thoi gian dat cont. len mooc
	private int rejectCode;
	private int prevStatusID;
	

	public ImportEmptyRequests(int id, boolean isBreakRomooc, String containerCategory,
			String containerCode, String containerType, String containerNo, String orderCode, String customerCode,
			String customerName, String requestDate, String earlyDateTimeAttachAtWarehouse,
			String lateDateTimeReturnEmptyAtDepot, String moocCode,
			double weight, String wareHouseCode, String depotContainerCode,
			int linkContainerDuration,
			int prevStatusID) {
		super();
		this.id = id;
		this.isBreakRomooc = isBreakRomooc;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerType = containerType;
		this.containerNo = containerNo;
		this.orderCode = orderCode;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.requestDate = requestDate;
		this.earlyDateTimeAttachAtWarehouse = earlyDateTimeAttachAtWarehouse;
		this.lateDateTimeReturnEmptyAtDepot = lateDateTimeReturnEmptyAtDepot;
		this.moocCode = moocCode;
		this.weight = weight;
		this.wareHouseCode = wareHouseCode;
		this.depotContainerCode = depotContainerCode;
		this.linkContainerDuration = linkContainerDuration;
		this.prevStatusID = prevStatusID;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public boolean getIsBreakRomooc() {
		return isBreakRomooc;
	}


	public void setBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}

	public void setIsBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}


	public String getContainerCategory() {
		return containerCategory;
	}


	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}


	public String getContainerCode() {
		return containerCode;
	}


	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getContainerNo() {
		return containerNo;
	}


	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	
	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getRequestDate() {
		return requestDate;
	}


	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}


	public String getEarlyDateTimeAttachAtWarehouse() {
		return earlyDateTimeAttachAtWarehouse;
	}


	public void setEarlyDateTimeAttachAtWarehouse(
			String earlyDateTimeAttachAtWarehouse) {
		this.earlyDateTimeAttachAtWarehouse = earlyDateTimeAttachAtWarehouse;
	}


	public String getLateDateTimeReturnEmptyAtDepot() {
		return lateDateTimeReturnEmptyAtDepot;
	}


	public void setLateDateTimeReturnEmptyAtDepot(
			String lateDateTimeReturnEmptyAtDepot) {
		this.lateDateTimeReturnEmptyAtDepot = lateDateTimeReturnEmptyAtDepot;
	}


	public String getMoocCode() {
		return moocCode;
	}


	public void setMoocCode(String moocCode) {
		this.moocCode = moocCode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getWareHouseCode() {
		return wareHouseCode;
	}


	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}


	public String getDepotContainerCode() {
		return depotContainerCode;
	}


	public void setDepotContainerCode(String depotContainerCode) {
		this.depotContainerCode = depotContainerCode;
	}


	public int getLinkContainerDuration() {
		return linkContainerDuration;
	}


	public void setLinkContainerDuration(int linkContainerDuration) {
		this.linkContainerDuration = linkContainerDuration;
	}

	public int getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}

	public int getPrevStatusID() {
		return prevStatusID;
	}

	public void setPrevStatusID(int prevStatusID) {
		this.prevStatusID = prevStatusID;
	}

	public ImportEmptyRequests() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}

// ========== models.requests.ImportLadenRequests ==========
class ImportLadenRequests {
	private int id;
	private boolean isBreakRomooc;
	private String containerCategory;
	private String containerCode;
	private String containerType;
	private String containerNo;
	private String orderCode;
	private String customerCode;
	private String customerName;
	private String requestDate;
	private String earlyDateTimePickupAtPort;
	private String lateDateTimePickupAtPort;
	private String earlyDateTimeUnloadAtWarehouse;
	private String lateDateTimeUnloadAtWarehouse;
	private String moocCode;
	private double weight;
	private String portCode;
	private String wareHouseCode;
	private int linkLoadedContainerAtPortDuration;
	
	private int rejectCode;
	private int prevStatusID;
	

	public ImportLadenRequests(int id, boolean isBreakRomooc, String containerCategory,
			String containerCode, String containerType, String containerNo, String orderCode, String customerCode,
			String customerName, String requestDate, String earlyDateTimePickupAtPort,
			String lateDateTimePickupAtPort,
			String earlyDateTimeUnloadAtWarehouse,
			String lateDateTimeUnloadAtWarehouse, String moocCode,
			double weight, String portCode, String wareHouseCode,
			int linkLoadedContainerAtPortDuration,
			int prevStatusID) {
		super();
		this.id = id;
		this.isBreakRomooc = isBreakRomooc;
		this.containerCategory = containerCategory;
		this.containerCode = containerCode;
		this.containerType = containerType;
		this.containerNo = containerNo;
		this.orderCode = orderCode;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.requestDate = requestDate;
		this.earlyDateTimePickupAtPort = earlyDateTimePickupAtPort;
		this.lateDateTimePickupAtPort = lateDateTimePickupAtPort;
		this.earlyDateTimeUnloadAtWarehouse = earlyDateTimeUnloadAtWarehouse;
		this.lateDateTimeUnloadAtWarehouse = lateDateTimeUnloadAtWarehouse;
		this.moocCode = moocCode;
		this.weight = weight;
		this.portCode = portCode;
		this.wareHouseCode = wareHouseCode;
		this.linkLoadedContainerAtPortDuration = linkLoadedContainerAtPortDuration;
		this.prevStatusID = prevStatusID;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public boolean getIsBreakRomooc() {
		return isBreakRomooc;
	}



	public void setBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}

	public void setIsBreakRomooc(boolean isBreakRomooc) {
		this.isBreakRomooc = isBreakRomooc;
	}



	public String getContainerCategory() {
		return containerCategory;
	}



	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getContainerNo() {
		return containerNo;
	}



	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}



	public String getOrderCode() {
		return orderCode;
	}



	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}



	public String getCustomerCode() {
		return customerCode;
	}



	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getRequestDate() {
		return requestDate;
	}



	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}



	public String getEarlyDateTimePickupAtPort() {
		return earlyDateTimePickupAtPort;
	}



	public void setEarlyDateTimePickupAtPort(String earlyDateTimePickupAtPort) {
		this.earlyDateTimePickupAtPort = earlyDateTimePickupAtPort;
	}



	public String getLateDateTimePickupAtPort() {
		return lateDateTimePickupAtPort;
	}



	public void setLateDateTimePickupAtPort(String lateDateTimePickupAtPort) {
		this.lateDateTimePickupAtPort = lateDateTimePickupAtPort;
	}



	public String getEarlyDateTimeUnloadAtWarehouse() {
		return earlyDateTimeUnloadAtWarehouse;
	}



	public void setEarlyDateTimeUnloadAtWarehouse(
			String earlyDateTimeUnloadAtWarehouse) {
		this.earlyDateTimeUnloadAtWarehouse = earlyDateTimeUnloadAtWarehouse;
	}



	public String getLateDateTimeUnloadAtWarehouse() {
		return lateDateTimeUnloadAtWarehouse;
	}



	public void setLateDateTimeUnloadAtWarehouse(
			String lateDateTimeUnloadAtWarehouse) {
		this.lateDateTimeUnloadAtWarehouse = lateDateTimeUnloadAtWarehouse;
	}



	public String getMoocCode() {
		return moocCode;
	}



	public void setMoocCode(String moocCode) {
		this.moocCode = moocCode;
	}



	public double getWeight() {
		return weight;
	}



	public void setWeight(double weight) {
		this.weight = weight;
	}



	public String getPortCode() {
		return portCode;
	}



	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}



	public String getWareHouseCode() {
		return wareHouseCode;
	}



	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}



	public int getLinkLoadedContainerAtPortDuration() {
		return linkLoadedContainerAtPortDuration;
	}



	public void setLinkLoadedContainerAtPortDuration(
			int linkLoadedContainerAtPortDuration) {
		this.linkLoadedContainerAtPortDuration = linkLoadedContainerAtPortDuration;
	}

	public int getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}

	public int getPrevStatusID() {
		return prevStatusID;
	}

	public void setPrevStatusID(int prevStatusID) {
		this.prevStatusID = prevStatusID;
	}

	public ImportLadenRequests() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

// ========== models.requests.PickupWarehouseInfo ==========
class PickupWarehouseInfo {
	private String wareHouseCode;
	private String earlyDateTimeLoadAtWarehouse;
	private String lateDateTimeLoadAtWarehouse;
	private int loadDuration;
	private int detachEmptyMoocContainerDuration;
	
	private String earlyDateTimePickupLoadedContainerAtWarehouse;
	private String lateDateTimePickupLoadedContainerAtWarehouse;
	private int attachLoadedMoocContainerDuration;
	public PickupWarehouseInfo(String wareHouseCode,
			String earlyDateTimeLoadAtWarehouse,
			String lateDateTimeLoadAtWarehouse, int loadDuration,
			int detachEmptyMoocContainerDuration,
			String earlyDateTimePickupLoadedContainerAtWarehouse,
			String lateDateTimePickupLoadedContainerAtWarehouse,
			int attachLoadedMoocContainerDuration) {
		super();
		this.wareHouseCode = wareHouseCode;
		this.earlyDateTimeLoadAtWarehouse = earlyDateTimeLoadAtWarehouse;
		this.lateDateTimeLoadAtWarehouse = lateDateTimeLoadAtWarehouse;
		this.loadDuration = loadDuration;
		this.detachEmptyMoocContainerDuration = detachEmptyMoocContainerDuration;
		this.earlyDateTimePickupLoadedContainerAtWarehouse = earlyDateTimePickupLoadedContainerAtWarehouse;
		this.lateDateTimePickupLoadedContainerAtWarehouse = lateDateTimePickupLoadedContainerAtWarehouse;
		this.attachLoadedMoocContainerDuration = attachLoadedMoocContainerDuration;
	}
	public PickupWarehouseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public String getEarlyDateTimeLoadAtWarehouse() {
		return earlyDateTimeLoadAtWarehouse;
	}
	public void setEarlyDateTimeLoadAtWarehouse(String earlyDateTimeLoadAtWarehouse) {
		this.earlyDateTimeLoadAtWarehouse = earlyDateTimeLoadAtWarehouse;
	}
	public String getLateDateTimeLoadAtWarehouse() {
		return lateDateTimeLoadAtWarehouse;
	}
	public void setLateDateTimeLoadAtWarehouse(String lateDateTimeLoadAtWarehouse) {
		this.lateDateTimeLoadAtWarehouse = lateDateTimeLoadAtWarehouse;
	}
	public int getLoadDuration() {
		return loadDuration;
	}
	public void setLoadDuration(int loadDuration) {
		this.loadDuration = loadDuration;
	}
	public int getDetachEmptyMoocContainerDuration() {
		return detachEmptyMoocContainerDuration;
	}
	public void setDetachEmptyMoocContainerDuration(
			int detachEmptyMoocContainerDuration) {
		this.detachEmptyMoocContainerDuration = detachEmptyMoocContainerDuration;
	}
	public String getEarlyDateTimePickupLoadedContainerAtWarehouse() {
		return earlyDateTimePickupLoadedContainerAtWarehouse;
	}
	public void setEarlyDateTimePickupLoadedContainerAtWarehouse(
			String earlyDateTimePickupLoadedContainerAtWarehouse) {
		this.earlyDateTimePickupLoadedContainerAtWarehouse = earlyDateTimePickupLoadedContainerAtWarehouse;
	}
	public String getLateDateTimePickupLoadedContainerAtWarehouse() {
		return lateDateTimePickupLoadedContainerAtWarehouse;
	}
	public void setLateDateTimePickupLoadedContainerAtWarehouse(
			String lateDateTimePickupLoadedContainerAtWarehouse) {
		this.lateDateTimePickupLoadedContainerAtWarehouse = lateDateTimePickupLoadedContainerAtWarehouse;
	}
	public int getAttachLoadedMoocContainerDuration() {
		return attachLoadedMoocContainerDuration;
	}
	public void setAttachLoadedMoocContainerDuration(
			int attachLoadedMoocContainerDuration) {
		this.attachLoadedMoocContainerDuration = attachLoadedMoocContainerDuration;
	}
	
	
}

// ========== models.requests.WarehouseContainerTransportRequest ==========
class WarehouseContainerTransportRequest {
	private String orderItemID;
	private String orderID;
	private String orderCode;
	// 1st segment (Depot to warehouse: empty)
	private String containerCategory;
	private String containerCode;
	private String containerNo;
	private double weight;
	
	private String shipCompanyCode;
	private String fromWarehouseCode;
	private String earlyDateTimeLoad;
	private String lateDateTimeLoad;
	private int loadDuration;
	private int detachEmptyMoocContainerDurationFromWarehouse;
	
	// 2nd segment (warehouse 1 -> warehouse 2: laden)
	private String earlyDateTimePickupLoadedContainerFromWarehouse;
	private String lateDateTimePickupLoadedContainerFromWarehouse;
	private int attachLoadedMoocContainerDurationFromWarehouse;
	
	private String toWarehouseCode;
	private String earlyDateTimeUnload;
	private String lateDateTimeUnload;
	private int unloadDuration;
	private int detachLoadedMoocContainerDurationToWarehouse;
	
	// 3th segment (warehouse -> depot: empty)
	private String earlyDateTimePickupEmptyContainerToWarehouse;
	private String lateDateTimePickupEmptyContainerToWarehouse;
	private int attachEmptyMoocContainerDurationToWarehouse;
	private String[] returnDepotContainerCodes;
	private String customerCode;
	private String customerName;
	
	private String levelRequest;// "1": only 1st, "2": only 2nd segment, "3": only 3th segment
								// "12": only 1st and 2nd, "23": only 2nd and 3th segments
								// "123": both 1st, 2nd and 3th segments
	
	private String getDepotContainerCode;
	private String returnDepotContainerCode;
	
	private int rejectCode;


	public WarehouseContainerTransportRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public WarehouseContainerTransportRequest(
										String orderItemID,
										String containerCategory,
										String containerCode,
										String containerNo,
										double weight,
										String shipCompanyCode,
										String fromWarehouseCode,
										String earlyDateTimeLoad,
										String lateDateTimeLoad,
										int loadDuration,
										int detachEmptyMoocContainerDurationFromWarehouse,
										String earlyDateTimePickupLoadedContainerFromWarehouse,
										String lateDateTimePickupLoadedContainerFromWarehouse,
										int attachLoadedMoocContainerDurationFromWarehouse,
										String toWarehouseCode,
										String earlyDateTimeUnload,
										String lateDateTimeUnload,
										int unloadDuration,
										int detachLoadedMoocContainerDurationToWarehouse,
										String earlyDateTimePickupEmptyContainerToWarehouse,
										String lateDateTimePickupEmptyContainerToWarehouse,
										int attachEmptyMoocContainerDurationToWarehouse,
										String[] returnDepotContainerCodes,
										String levelRequest,
										String customerCode,
										String customerName,
										String getDepotContainerCode,
										String returnDepotContainerCode) {
									super();
									this.orderItemID = orderItemID;
									this.containerCategory = containerCategory;
									this.containerCode = containerCode;
									this.containerNo = containerNo;
									this.weight = weight;
									this.shipCompanyCode = shipCompanyCode;
									this.fromWarehouseCode = fromWarehouseCode;
									this.earlyDateTimeLoad = earlyDateTimeLoad;
									this.lateDateTimeLoad = lateDateTimeLoad;
									this.loadDuration = loadDuration;
									this.detachEmptyMoocContainerDurationFromWarehouse = detachEmptyMoocContainerDurationFromWarehouse;
									this.earlyDateTimePickupLoadedContainerFromWarehouse = earlyDateTimePickupLoadedContainerFromWarehouse;
									this.lateDateTimePickupLoadedContainerFromWarehouse = lateDateTimePickupLoadedContainerFromWarehouse;
									this.attachLoadedMoocContainerDurationFromWarehouse = attachLoadedMoocContainerDurationFromWarehouse;
									this.toWarehouseCode = toWarehouseCode;
									this.earlyDateTimeUnload = earlyDateTimeUnload;
									this.lateDateTimeUnload = lateDateTimeUnload;
									this.unloadDuration = unloadDuration;
									this.detachLoadedMoocContainerDurationToWarehouse = detachLoadedMoocContainerDurationToWarehouse;
									this.earlyDateTimePickupEmptyContainerToWarehouse = earlyDateTimePickupEmptyContainerToWarehouse;
									this.lateDateTimePickupEmptyContainerToWarehouse = lateDateTimePickupEmptyContainerToWarehouse;
									this.attachEmptyMoocContainerDurationToWarehouse = attachEmptyMoocContainerDurationToWarehouse;
									this.returnDepotContainerCodes = returnDepotContainerCodes;
									this.levelRequest = levelRequest;
									this.customerCode = customerCode;
									this.customerName = customerName;
									this.getDepotContainerCode = getDepotContainerCode;
									this.returnDepotContainerCode = returnDepotContainerCode;
								}




	public String[] getReturnDepotContainerCodes() {
		return returnDepotContainerCodes;
	}




	public void setReturnDepotContainerCodes(String[] returnDepotContainerCodes) {
		this.returnDepotContainerCodes = returnDepotContainerCodes;
	}
	
	public String getGetDepotContainerCode() {
		return getDepotContainerCode;
	}

	public void setGetDepotContainerCode(String getDepotContainerCode) {
		this.getDepotContainerCode = getDepotContainerCode;
	}
	
	public String getReturnDepotContainerCode() {
		return returnDepotContainerCode;
	}

	public void setReturnDepotContainerCode(String returnDepotContainerCode) {
		this.returnDepotContainerCode = returnDepotContainerCode;
	}




	public String getOrderItemID() {
		return orderItemID;
	}


	public void setOrderItemID(String orderItemID) {
		this.orderItemID = orderItemID;
	}

	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getContainerCategory() {
		return containerCategory;
	}


	public void setContainerCategory(String containerCategory) {
		this.containerCategory = containerCategory;
	}

	public String getContainerCode() {
		return containerCode;
	}
	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public String getShipCompanyCode() {
		return shipCompanyCode;
	}


	public void setShipCompanyCode(String shipCompanyCode) {
		this.shipCompanyCode = shipCompanyCode;
	}


	public String getFromWarehouseCode() {
		return fromWarehouseCode;
	}


	public void setFromWarehouseCode(String fromWarehouseCode) {
		this.fromWarehouseCode = fromWarehouseCode;
	}


	public String getEarlyDateTimeLoad() {
		return earlyDateTimeLoad;
	}


	public void setEarlyDateTimeLoad(String earlyDateTimeLoad) {
		this.earlyDateTimeLoad = earlyDateTimeLoad;
	}


	public String getLateDateTimeLoad() {
		return lateDateTimeLoad;
	}


	public void setLateDateTimeLoad(String lateDateTimeLoad) {
		this.lateDateTimeLoad = lateDateTimeLoad;
	}


	public int getLoadDuration() {
		return loadDuration;
	}


	public void setLoadDuration(int loadDuration) {
		this.loadDuration = loadDuration;
	}


	public int getDetachEmptyMoocContainerDurationFromWarehouse() {
		return detachEmptyMoocContainerDurationFromWarehouse;
	}


	public void setDetachEmptyMoocContainerDurationFromWarehouse(
			int detachEmptyMoocContainerDurationFromWarehouse) {
		this.detachEmptyMoocContainerDurationFromWarehouse = detachEmptyMoocContainerDurationFromWarehouse;
	}


	public String getEarlyDateTimePickupLoadedContainerFromWarehouse() {
		return earlyDateTimePickupLoadedContainerFromWarehouse;
	}


	public void setEarlyDateTimePickupLoadedContainerFromWarehouse(
			String earlyDateTimePickupLoadedContainerFromWarehouse) {
		this.earlyDateTimePickupLoadedContainerFromWarehouse = earlyDateTimePickupLoadedContainerFromWarehouse;
	}


	public String getLateDateTimePickupLoadedContainerFromWarehouse() {
		return lateDateTimePickupLoadedContainerFromWarehouse;
	}


	public void setLateDateTimePickupLoadedContainerFromWarehouse(
			String lateDateTimePickupLoadedContainerFromWarehouse) {
		this.lateDateTimePickupLoadedContainerFromWarehouse = lateDateTimePickupLoadedContainerFromWarehouse;
	}


	public int getAttachLoadedMoocContainerDurationFromWarehouse() {
		return attachLoadedMoocContainerDurationFromWarehouse;
	}


	public void setAttachLoadedMoocContainerDurationFromWarehouse(
			int attachLoadedMoocContainerDurationFromWarehouse) {
		this.attachLoadedMoocContainerDurationFromWarehouse = attachLoadedMoocContainerDurationFromWarehouse;
	}


	public String getToWarehouseCode() {
		return toWarehouseCode;
	}


	public void setToWarehouseCode(String toWarehouseCode) {
		this.toWarehouseCode = toWarehouseCode;
	}


	public String getEarlyDateTimeUnload() {
		return earlyDateTimeUnload;
	}


	public void setEarlyDateTimeUnload(String earlyDateTimeUnload) {
		this.earlyDateTimeUnload = earlyDateTimeUnload;
	}


	public String getLateDateTimeUnload() {
		return lateDateTimeUnload;
	}


	public void setLateDateTimeUnload(String lateDateTimeUnload) {
		this.lateDateTimeUnload = lateDateTimeUnload;
	}


	public int getUnloadDuration() {
		return unloadDuration;
	}


	public void setUnloadDuration(int unloadDuration) {
		this.unloadDuration = unloadDuration;
	}


	public int getDetachLoadedMoocContainerDurationToWarehouse() {
		return detachLoadedMoocContainerDurationToWarehouse;
	}


	public void setDetachLoadedMoocContainerDurationToWarehouse(
			int detachLoadedMoocContainerDurationToWarehouse) {
		this.detachLoadedMoocContainerDurationToWarehouse = detachLoadedMoocContainerDurationToWarehouse;
	}


	public String getEarlyDateTimePickupEmptyContainerToWarehouse() {
		return earlyDateTimePickupEmptyContainerToWarehouse;
	}


	public void setEarlyDateTimePickupEmptyContainerToWarehouse(
			String earlyDateTimePickupEmptyContainerToWarehouse) {
		this.earlyDateTimePickupEmptyContainerToWarehouse = earlyDateTimePickupEmptyContainerToWarehouse;
	}


	public String getLateDateTimePickupEmptyContainerToWarehouse() {
		return lateDateTimePickupEmptyContainerToWarehouse;
	}


	public void setLateDateTimePickupEmptyContainerToWarehouse(
			String lateDateTimePickupEmptyContainerToWarehouse) {
		this.lateDateTimePickupEmptyContainerToWarehouse = lateDateTimePickupEmptyContainerToWarehouse;
	}


	public int getAttachEmptyMoocContainerDurationToWarehouse() {
		return attachEmptyMoocContainerDurationToWarehouse;
	}


	public void setAttachEmptyMoocContainerDurationToWarehouse(
			int attachEmptyMoocContainerDurationToWarehouse) {
		this.attachEmptyMoocContainerDurationToWarehouse = attachEmptyMoocContainerDurationToWarehouse;
	}


	public String getLevelRequest() {
		return levelRequest;
	}


	public void setLevelRequest(String levelRequest) {
		this.levelRequest = levelRequest;
	}
	
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
	public String getCustomerName(){
		return this.customerName;
	}
	
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public int getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(int rejectCode) {
		this.rejectCode = rejectCode;
	}
	
	
}

// ========== models.requests.WarehouseTransportRequest ==========
class WarehouseTransportRequest {
	private String orderID;
	private String orderCode;
	private WarehouseContainerTransportRequest[] warehouseContainerTransportRequests;
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public WarehouseContainerTransportRequest[] getWarehouseContainerTransportRequests() {
		return warehouseContainerTransportRequests;
	}
	public void setWarehouseContainerTransportRequests(
			WarehouseContainerTransportRequest[] warehouseContainerTransportRequests) {
		this.warehouseContainerTransportRequests = warehouseContainerTransportRequests;
	}
	public WarehouseTransportRequest(
			String orderID,
			String orderCode,
			WarehouseContainerTransportRequest[] warehouseContainerTransportRequests) {
		super();
		this.orderID = orderID;
		this.orderCode = orderCode;
		this.warehouseContainerTransportRequests = warehouseContainerTransportRequests;
	}
	public WarehouseTransportRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}

// ========== models.routing.RouteElement ==========
class RouteElement {
    private String locationCode;
	private String action;
	
	private String arrivalTime;
	private String departureTime;
	private int travelTime;
	
	
	public RouteElement(String locationCode, String action,
			String arrivalTime, String departureTime, int travelTime){
		super();
		this.locationCode = locationCode;
		this.action = action;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.travelTime = travelTime;
	}
	public RouteElement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocationCode(){
		return this.locationCode;
	}
	public void setLocationCode(String locationCode){
		this.locationCode = locationCode;
	}
	public String getAction(){
		return this.action;
	}
	public void setAction(String action){
		this.action = action;
	}
	
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
}

// ========== models.routing.TruckRoute ==========
class TruckRoute {
    private Truck truck;
	private int nbStops;
	private int travelTime;
	private RouteElement[] nodes;

	public TruckRoute(Truck truck, int nbStops,
			int travelTime,
			RouteElement[] nodes){
		super();
		this.truck = truck;
		this.nbStops = nbStops;
		this.travelTime = travelTime;
		this.nodes = nodes;
	}
	public TruckRoute() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Truck getTruck() {
		return truck;
	}
	public void setTruck(Truck truck) {
		this.truck = truck;
	}
	public int getNbStops() {
		return nbStops;
	}
	public void setNbStops(int nbStops) {
		this.nbStops = nbStops;
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
	public RouteElement[] getNodes() {
		return nodes;
	}
	public void setNodes(RouteElement[] nodes) {
		this.nodes = nodes;
	}
}

// ========== constraints.ContainerCapacityConstraint ==========
class ContainerCapacityConstraint implements IConstraintVR {
	private VarRoutesVR XR;
	private AccumulatedWeightNodesVR accContainerInvr;
	private int violations;
	
	public ContainerCapacityConstraint(VarRoutesVR XR, AccumulatedWeightNodesVR accContainerInvr){
		this.XR = XR;
		this.accContainerInvr = accContainerInvr;
		XR.getVRManager().post(this);
	}

	@Override
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initPropagation() {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int wY = (int)(accContainerInvr.getSumWeights(p));
			if(wY > 2)
				violations += wY - 2;
		}
	}

	@Override
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y1);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int wY = (int)(accContainerInvr.getSumWeights(p));
			if(wY > 2)
				violations += wY - 2;
		}
	}

	@Override
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return violations;
	}

	@Override
	public int evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int vio = 0;

		for(Point p = y; p != XR.getTerminatingPointOfRoute(XR.route(y)); p = XR.next(p)){
			int wY = (int)(accContainerInvr.getSumWeights(p) 
					+ accContainerInvr.getWeights(x));
			if(wY > 2)
				vio += wY - 2;
		}
		
		return vio;
	}

	@Override
	public int evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int vio = 0;
		for(Point p = y1; p != XR.next(y2); p = XR.next(p)){
			int wY1 = (int)(accContainerInvr.getSumWeights(p) + accContainerInvr.getWeights(x1));
			if(wY1 > 2)
				vio += wY1 - 2;
		}
		for(Point p = y2; p != XR.getTerminatingPointOfRoute(XR.route(y1)); p = XR.next(p)){
			int wY2 = (int)(accContainerInvr.getSumWeights(p) 
					+ accContainerInvr.getWeights(x1)
					+ accContainerInvr.getWeights(x2));
			if(wY2 > 2)
				vio += wY2 - 2;
		}
		
		return vio;
	}

	@Override
	public int evaluateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}
}

// ========== constraints.ContainerCarriedByTrailerConstraint ==========
class ContainerCarriedByTrailerConstraint implements IConstraintVR {
	private VarRoutesVR XR;
	private AccumulatedWeightNodesVR accContainerInvr;
	private AccumulatedWeightNodesVR accMoocInvr;
	private int violations;
	
	public ContainerCarriedByTrailerConstraint(VarRoutesVR XR, 
			AccumulatedWeightNodesVR accContainerInvr,
			AccumulatedWeightNodesVR accMoocInvr){
		this.XR = XR;
		this.accContainerInvr = accContainerInvr;
		this.accMoocInvr = accMoocInvr;
		XR.getVRManager().post(this);
	}
	
	@Override
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initPropagation() {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int cY1 = (int)(accContainerInvr.getSumWeights(p));
			int mY1 = (int)(accMoocInvr.getSumWeights(p));
			if(mY1 < cY1)
				violations += cY1 - mY1;
		}
	}

	@Override
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y1);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int cY1 = (int)(accContainerInvr.getSumWeights(p));
			int mY1 = (int)(accMoocInvr.getSumWeights(p));
			if(mY1 < cY1)
				violations += cY1 - mY1;
		}
	}

	@Override
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return violations;
	}

	@Override
	public int evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int vio = 0;

		for(Point p = y; p != XR.getTerminatingPointOfRoute(XR.route(y)); p = XR.next(p)){
			int cY1 = (int)(accContainerInvr.getSumWeights(p) + accContainerInvr.getWeights(x));
			int mY1 = (int)(accMoocInvr.getSumWeights(p) + accMoocInvr.getWeights(x));
			if(mY1 < cY1)
				vio += cY1 - mY1;
		}
		return vio;
	}

	@Override
	public int evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int vio = 0;

//		for(Point p = y1; p != XR.next(y2); p = XR.next(p)){
//			int cY1 = (int)(accContainerInvr.getSumWeights(p) + accContainerInvr.getWeights(x1));
//			int mY1 = (int)(accMoocInvr.getSumWeights(p) + accMoocInvr.getWeights(x1));
//			if(mY1 < cY1)
//				vio += cY1 - mY1;
//		}
//		for(Point p = y2; p != XR.getTerminatingPointOfRoute(XR.route(y1)); p = XR.next(p)){
//			int cY2 = (int)(accContainerInvr.getSumWeights(p) 
//					+ accContainerInvr.getWeights(x1)
//					+ accContainerInvr.getWeights(x2));
//			int mY2 = (int)(accMoocInvr.getSumWeights(p) 
//					+ accMoocInvr.getWeights(x1)
//					+ accMoocInvr.getWeights(x2));
//			if(mY2 < cY2)
//				vio += cY2 - mY2;
//		}
		
		return vio;
	}

	@Override
	public int evaluateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

}

// ========== constraints.MoocCapacityConstraint ==========
class MoocCapacityConstraint implements IConstraintVR {
	private VarRoutesVR XR;
	private AccumulatedWeightNodesVR accMoocInvr;
	private int violations;
	
	public MoocCapacityConstraint(VarRoutesVR XR, AccumulatedWeightNodesVR accMoocInvr){
		this.XR = XR;
		this.accMoocInvr = accMoocInvr;
		XR.getVRManager().post(this);
	}

	@Override
	public VRManager getVRManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initPropagation() {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateTwoOptMoveOneRoute(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateCrossExchangeMove(Point x1, Point y1, Point x2,
			Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateThreePointsMove(Point x1, Point x2, Point x3,
			Point y1, Point y2, Point y3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int wY = (int)(accMoocInvr.getSumWeights(p));
			if(wY > 2)
				violations += wY - 2;
		}
	}

	@Override
	public void propagateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		violations = 0;
		int r = XR.route(y1);
		for(Point p = XR.getStartingPointOfRoute(r); p != XR.getTerminatingPointOfRoute(r); p = XR.next(p)){
			int wY = (int)(accMoocInvr.getSumWeights(p));
			if(wY > 2)
				violations += wY - 2;
		}
	}

	@Override
	public void propagateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		violations = 0;
	}

	@Override
	public void propagateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return violations;
	}

	@Override
	public int evaluateOnePointMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove1(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove2(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove3(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove4(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove5(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove6(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove7(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoOptMove8(Point x, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove1(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateOrOptMove2(Point x1, Point x2, Point y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove1(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove2(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove3(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove4(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove5(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove6(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove7(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreeOptMove8(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateCrossExchangeMove(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateTwoPointsMove(Point x1, Point x2, Point y1, Point y2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateThreePointsMove(Point x1, Point x2, Point x3, Point y1,
			Point y2, Point y3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateFourPointsMove(Point x1, Point x2, Point x3, Point x4,
			Point y1, Point y2, Point y3, Point y4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateKPointsMove(ArrayList<Point> x, ArrayList<Point> y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddOnePoint(Point x, Point y) {
		// TODO Auto-generated method stub
		int vio = 0;

		for(Point p = y; p != XR.getTerminatingPointOfRoute(XR.route(y)); p = XR.next(p)){
			int wY = (int)(accMoocInvr.getSumWeights(p) 
					+ accMoocInvr.getWeights(x));
			if(wY > 2)
				vio += wY - 2;
		}
		
		return vio;
	}

	@Override
	public int evaluateRemoveOnePoint(Point x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddTwoPoints(Point x1, Point y1, Point x2, Point y2) {
		// TODO Auto-generated method stub
		int vio = 0;
//		for(Point p = y1; p != XR.next(y2); p = XR.next(p)){
//			int wY1 = (int)(accMoocInvr.getSumWeights(p) + accMoocInvr.getWeights(x1));
//			if(wY1 > 1)
//				vio += wY1 - 1;
//		}
//		for(Point p = y2; p != XR.getTerminatingPointOfRoute(XR.route(y1)); p = XR.next(p)){
//			int wY2 = (int)(accMoocInvr.getSumWeights(p) 
//					+ accMoocInvr.getWeights(x1)
//					+ accMoocInvr.getWeights(x2));
//			if(wY2 > 2)
//				vio += wY2 - 2;
//		}
		
		return vio;
	}

	@Override
	public int evaluateRemoveTwoPoints(Point x1, Point x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evaluateAddRemovePoints(Point x, Point y, Point z) {
		// TODO Auto-generated method stub
		return 0;
	}
}

// ========== solver.init.FPIUSInit ==========
/**
 * FPIUS (First Possible Insertion with Unscheduled Set) initialization strategy.
 * Implements the InitializationStrategy interface.
 */
class FPIUSInit implements InitializationStrategy {
	
	@Override
	public void initialize(TruckContainerSolver solver) {
		firstPossibleInitFPIUS(solver);
	}
	
	public void firstPossibleInitFPIUS(TruckContainerSolver solver) {
		Stack<String> stack = new Stack<String>();
		for (int r = solver.XR.getNbRoutes(); r >= 1; r--) {
			String s = "" + r;
			stack.push(s);
		}

		for (int i = 0; i < solver.pickup2Delivery.size(); i++) {
			System.out.println("req " + i + "/" + solver.pickup2Delivery.size());
			Point pickup = solver.pickupPoints.get(i);
			int groupId = solver.point2Group.get(pickup);
			if (solver.XR.route(pickup) != Constants.NULL_POINT || solver.group2marked.get(groupId) == 1)
				continue;
			Point delivery = solver.deliveryPoints.get(i);
			// add the request to route
			boolean isAdded = false;
			for (int k = stack.size() - 1; k >= 0; k--) {
				if (isAdded)
					break;
				int r = Integer.parseInt(stack.get(k));
				Point st = solver.XR.getStartingPointOfRoute(r);

				int groupTruck = solver.point2Group.get(st);
				if (solver.group2marked.get(groupTruck) == 1 && solver.XR.index(solver.XR.getTerminatingPointOfRoute(r)) <= 1)
					continue;
				for (Point p = st; p != solver.XR.getTerminatingPointOfRoute(r); p = solver.XR.next(p)) {
					if (isAdded)
						break;
					for (Point q = p; q != solver.XR.getTerminatingPointOfRoute(r); q = solver.XR.next(q)) {
						solver.mgr.performAddTwoPoints(pickup, p, delivery, q);
						insertMoocToRoutes(solver, r);
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
					}
				}
			}
		}

		for (int i = 0; i < solver.pickup2Delivery.size(); i++) {
			Point pickup = solver.pickupPoints.get(i);
			if (solver.XR.route(pickup) == Constants.NULL_POINT && !solver.rejectPickupPoints.contains(pickup)) {
				solver.rejectPickupPoints.add(pickup);
				solver.rejectDeliveryPoints.add(solver.pickup2Delivery.get(pickup));
			}
		}
		insertMoocForAllRoutes(solver);
	}

	private Point getBestStartMoocForRequest(TruckContainerSolver solver, int r, Point p, Point pickup) {
		Point bestMooc = null;
		double min_d = Double.MAX_VALUE;
		for (int i = 0; i < solver.startMoocPoints.size(); i++) {
			Point stMooc = solver.startMoocPoints.get(i);
			int groupMooc = solver.point2Group.get(stMooc);
			if (solver.group2marked.get(groupMooc) == 1 || solver.XR.route(stMooc) != Constants.NULL_POINT)
				continue;
			double d = solver.getTravelTime(p.getLocationCode(), stMooc.getLocationCode())
					+ solver.getTravelTime(stMooc.getLocationCode(), pickup.getLocationCode());
			if (d < min_d) {
				min_d = d;
				bestMooc = stMooc;
			}
		}
		return bestMooc;
	}

	public void insertMoocToRoutes(TruckContainerSolver solver, int r) {
		Point st = solver.XR.getStartingPointOfRoute(r);
		Point stMooc = null;
		Point enMooc = null;
		for (Point p = solver.XR.next(st); p != solver.XR.getTerminatingPointOfRoute(r); p = solver.XR.next(p)) {
			if (solver.accMoocInvr.getSumWeights(solver.XR.prev(p)) <= 0) {
				stMooc = getBestStartMoocForRequest(solver, r, solver.XR.prev(p), p);
				if (stMooc == null)
					continue;
				solver.mgr.performAddOnePoint(stMooc, solver.XR.prev(p));
				int groupMooc = solver.point2Group.get(stMooc);
				solver.group2marked.put(groupMooc, 1);
				enMooc = solver.start2stopMoocPoint.get(stMooc);
			}
		}
		if (solver.accMoocInvr.getSumWeights(solver.XR.getTerminatingPointOfRoute(r)) > 0 && enMooc != null) {
			solver.mgr.performAddOnePoint(enMooc, solver.XR.prev(solver.XR.getTerminatingPointOfRoute(r)));
		}
	}

	public void removeMoocOnRoutes(TruckContainerSolver solver, int r) {
		Point x = solver.XR.getStartingPointOfRoute(r);
		Point next_x = solver.XR.next(x);
		while (next_x != solver.XR.getTerminatingPointOfRoute(r)) {
			x = next_x;
			next_x = solver.XR.next(x);
			if (solver.startMoocPoints.contains(x) || solver.stopMoocPoints.contains(x)) {
				solver.mgr.performRemoveOnePoint(x);
				int groupMooc = solver.point2Group.get(x);
				solver.group2marked.put(groupMooc, 0);
			}
		}
	}

	private Point getBestMoocForRequest(TruckContainerSolver solver, Point curStMooc, Point p, Point np, Point q,
			Point nq) {
		Point bestMooc = curStMooc;
		double min_d = Double.MAX_VALUE;
		for (int i = 0; i < solver.startMoocPoints.size(); i++) {
			Point stMooc = solver.startMoocPoints.get(i);
			Point enMooc = solver.start2stopMoocPoint.get(stMooc);
			int groupMooc = solver.point2Group.get(stMooc);
			if ((solver.group2marked.get(groupMooc) == 1 || solver.XR.route(stMooc) != Constants.NULL_POINT)
					&& stMooc != curStMooc)
				continue;
			double d = solver.getTravelTime(p.getLocationCode(), stMooc.getLocationCode())
					+ solver.getTravelTime(stMooc.getLocationCode(), np.getLocationCode())
					+ solver.getTravelTime(q.getLocationCode(), enMooc.getLocationCode())
					+ solver.getTravelTime(enMooc.getLocationCode(), nq.getLocationCode());
			if (d < min_d) {
				min_d = d;
				bestMooc = stMooc;
			}
		}
		return bestMooc;
	}

	public void removeAllMoocFromRoutes(TruckContainerSolver solver) {
		for (int i = 0; i < solver.startMoocPoints.size(); i++) {
			Point st = solver.startMoocPoints.get(i);
			Point tp = solver.start2stopMoocPoint.get(st);
			if (solver.XR.route(st) != Constants.NULL_POINT) {
				solver.mgr.performRemoveOnePoint(st);
				int groupMooc = solver.point2Group.get(st);
				solver.group2marked.put(groupMooc, 0);
			}
			if (solver.XR.route(tp) != Constants.NULL_POINT) {
				solver.mgr.performRemoveOnePoint(tp);
				int groupMooc = solver.point2Group.get(tp);
				solver.group2marked.put(groupMooc, 0);
			}
		}
	}

	public void insertMoocForAllRoutes(TruckContainerSolver solver) {
		removeAllMoocFromRoutes(solver);
		for (int r = 1; r <= solver.XR.getNbRoutes(); r++) {
			Point st = solver.XR.getStartingPointOfRoute(r);
			Point stMooc = null;
			Point preP = null;
			Point nextP = null;
			Point enMooc = null;
			for (Point p = solver.XR.next(st); p != solver.XR.getTerminatingPointOfRoute(r); p = solver.XR.next(p)) {
				if (solver.accMoocInvr.getSumWeights(solver.XR.prev(p)) <= 0) {
					stMooc = getBestStartMoocForRequest(solver, r, solver.XR.prev(p), p);
					if (stMooc == null)
						continue;
					preP = solver.XR.prev(p);
					nextP = p;
					solver.mgr.performAddOnePoint(stMooc, solver.XR.prev(p));
					int groupMooc = solver.point2Group.get(stMooc);
					solver.group2marked.put(groupMooc, 1);
				}
			}
			if (solver.accMoocInvr.getSumWeights(solver.XR.getTerminatingPointOfRoute(r)) > 0) {
				Point enPoint = solver.XR.prev(solver.XR.getTerminatingPointOfRoute(r));
				Point newStMooc = getBestMoocForRequest(solver, stMooc, preP, nextP, enPoint,
						solver.XR.getTerminatingPointOfRoute(r));
				if (newStMooc != stMooc) {
					solver.mgr.performRemoveOnePoint(stMooc);
					int groupMooc = solver.point2Group.get(stMooc);
					solver.group2marked.put(groupMooc, 0);
					solver.mgr.performAddOnePoint(newStMooc, preP);
					groupMooc = solver.point2Group.get(newStMooc);
					solver.group2marked.put(groupMooc, 1);
				}
				enMooc = solver.start2stopMoocPoint.get(newStMooc);
				solver.mgr.performAddOnePoint(enMooc, enPoint);
			}
		}
	}
}

// ========== solver.init.InitializationStrategy ==========
interface InitializationStrategy {
    void initialize(TruckContainerSolver solver);
}

// ========== solver.opt.ALNS ==========
/**
 * ALNS (Adaptive Large Neighborhood Search) optimization strategy.
 * Implements the OptimizationStrategy interface.
 */
class ALNS implements OptimizationStrategy {
	private final TruckContainerSolver solver;
	private final FPIUSInit initialSolutionBuilder;

	public ALNS(TruckContainerSolver solver) {
		this.solver = solver;
		this.initialSolutionBuilder = new FPIUSInit();
	}
	
	@Override
	public void optimize(TruckContainerSolver solver, String outputFile) {
		// Use this.solver field which was set in constructor
		adaptiveSearchOperators(outputFile);
	}

	public void initParamsForALNS() {
		solver.nChosed = new HashMap<Point, Integer>();
		solver.removeAllowed = new HashMap<Point, Boolean>();
		for (int i = 0; i < solver.pickupPoints.size(); i++) {
			Point pi = solver.pickupPoints.get(i);
			solver.nChosed.put(pi, 0);
			solver.removeAllowed.put(pi, true);

			Point pj = solver.pickup2Delivery.get(pi);
			solver.nChosed.put(pj, 0);
			solver.removeAllowed.put(pj, true);
		}
	}

	public int getNbUsedTrucks() {
		int nb = 0;
		for (int r = 1; r <= solver.XR.getNbRoutes(); r++) {
			if (solver.XR.index(solver.XR.getTerminatingPointOfRoute(r)) > 3)
				nb++;
		}
		return nb;
	}

	public int getNbRejectedRequests() {
		Set<Integer> grs = new HashSet<Integer>();
		for (int i = 0; i < solver.rejectPickupPoints.size(); i++) {
			Point pickup = solver.rejectPickupPoints.get(i);
			int groupId = solver.point2Group.get(pickup);

			if (solver.group2marked.get(groupId) == 1)
				continue;
			grs.add(groupId);
		}
		return grs.size();
	}

	public void adaptiveSearchOperators(String outputfile) {
		int it = 0;
		int iS = 0;

		// Ensure ALNS bookkeeping maps are ready
		initParamsForALNS();

		// insertion operators selection probabilities
		double[] pti = new double[solver.nInsertionOperators];
		// removal operators selection probabilities
		double[] ptd = new double[solver.nRemovalOperators];

		// wi - number of times used during last iteration
		int[] wi = new int[solver.nInsertionOperators];
		int[] wd = new int[solver.nRemovalOperators];

		// pi_i - score of operator
		int[] si = new int[solver.nInsertionOperators];
		int[] sd = new int[solver.nRemovalOperators];

		// init probabilites
		for (int i = 0; i < solver.nInsertionOperators; i++) {
			pti[i] = 1.0 / solver.nInsertionOperators;
			wi[i] = 1;
			si[i] = 0;
		}
		for (int i = 0; i < solver.nRemovalOperators; i++) {
			ptd[i] = 1.0 / solver.nRemovalOperators;
			wd[i] = 1;
			sd[i] = 0;
		}

		SearchOptimumSolution opt = new SearchOptimumSolution(solver);

		double best_cost = solver.objective.getValue();

		TruckContainerSolution best_solution = new TruckContainerSolution(solver.XR, solver.rejectPickupPoints,
				solver.rejectDeliveryPoints, best_cost, getNbUsedTrucks(), getNbRejectedRequests(),
				solver.point2Group, solver.group2marked);

		double start_search_time = System.currentTimeMillis();
		try {
			FileOutputStream write = new FileOutputStream(outputfile, true);
			PrintWriter fo = new PrintWriter(write);
			fo.println("time limit = " + solver.timeLimit + ", nbIters = " + solver.nIter + ", maxStable = " + solver.maxStable);
			fo.println("#Request = " + TruckContainerSolver.nRequest);
			fo.println("iter=====insertion=====removal=====time=====cost=====nbReject=====nbTrucks");
			fo.println("0 -1 -1 " + " " + System.currentTimeMillis() / 1000 + " " + best_cost + " "
					+ getNbRejectedRequests() + " " + getNbUsedTrucks());
			fo.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		while ((System.currentTimeMillis() - start_search_time) < solver.timeLimit && it++ < solver.nIter) {
			System.out.println("nb of iterator: " + it);
			double current_cost = solver.objective.getValue();
			int current_nbTrucks = getNbUsedTrucks();
			TruckContainerSolution current_solution = new TruckContainerSolution(solver.XR, solver.rejectPickupPoints,
					solver.rejectDeliveryPoints, current_cost, current_nbTrucks, solver.getNbRejectedRequests(),
					solver.point2Group, solver.group2marked);

			// Was solver.removeAllMoocFromRoutes(); but it moved to builder
			initialSolutionBuilder.removeAllMoocFromRoutes(solver);

			int i_selected_removal = -1;
			if (iS >= solver.maxStable) {
				opt.allRemoval();
				iS = 0;
			} else {
				i_selected_removal = get_operator(ptd);
				wd[i_selected_removal]++;
				switch (i_selected_removal) {
					case 0:
						opt.routeRemoval();
						break;
					case 1:
						opt.randomRequestRemoval();
						break;
					case 2:
						opt.shaw_removal();
						break;
					case 3:
						opt.worst_removal();
						break;
					case 4:
						opt.forbidden_removal(0);
						break;
					case 5:
						opt.forbidden_removal(1);
						break;
					case 6:
						opt.forbidden_removal(2);
						break;
					case 7:
						opt.forbidden_removal(3);
						break;
				}
			}

			int i_selected_insertion = get_operator(pti);
			wi[i_selected_insertion]++;
			switch (i_selected_insertion) {
				case 0:
					opt.greedyInsertion();
					break;
				case 1:
					opt.greedyInsertionWithNoise();
					break;
				case 2:
					opt.regret_n_insertion(2);
					break;
				case 3:
					opt.first_possible_insertion();
					break;
				case 4:
					opt.sort_before_insertion(0);
					break;
				case 5:
					opt.sort_before_insertion(1);
					break;
				case 6:
					opt.sort_before_insertion(2);
					break;
				case 7:
					opt.sort_before_insertion(3);
					break;
			}

			int new_nb_reject_points = solver.rejectPickupPoints.size();
			double new_cost = solver.objective.getValue();
			int new_nbTrucks = getNbUsedTrucks();
			int current_nb_reject_points = current_solution.get_rejectPickupPoints().size();

			if (new_nb_reject_points < current_nb_reject_points
					|| (new_nb_reject_points == current_nb_reject_points && new_cost < current_cost)) {
				int best_nb_reject_points = best_solution.get_rejectPickupPoints().size();

				if (new_nb_reject_points < best_nb_reject_points
						|| (new_nb_reject_points == best_nb_reject_points && new_cost < best_cost)) {

					best_cost = new_cost;
					best_solution = new TruckContainerSolution(solver.XR, solver.rejectPickupPoints, solver.rejectDeliveryPoints,
							new_cost, new_nbTrucks, new_nb_reject_points, solver.point2Group, solver.group2marked);
					try {
						FileOutputStream write = new FileOutputStream(outputfile, true);
						PrintWriter fo = new PrintWriter(write);
						fo.println(it + " " + i_selected_insertion + " " + i_selected_removal + " "
								+ System.currentTimeMillis() / 1000 + " " + best_cost + " " + getNbRejectedRequests()
								+ " " + getNbUsedTrucks());
						fo.close();
					} catch (Exception e) {
						System.out.println(e);
					}
					si[i_selected_insertion] += solver.sigma1;
					if (i_selected_removal >= 0)
						sd[i_selected_removal] += solver.sigma1;
				} else {
					si[i_selected_insertion] += solver.sigma2;
					if (i_selected_removal >= 0)
						sd[i_selected_removal] += solver.sigma2;
				}
			}
			/*
			 * if new solution has cost worst than current solution
			 * because XR is new solution
			 * copy current solution back if reject
			 */
			else {
				si[i_selected_insertion] += solver.sigma3;
				if (i_selected_removal >= 0)
					sd[i_selected_removal] += solver.sigma3;
				double v = Math.exp(-(new_cost - current_cost) / solver.temperature);
				double e = Math.random();
				if (e >= v) {
					current_solution.copy2XR(solver.XR);
					solver.group2marked = current_solution.get_group2marked();
					solver.rejectPickupPoints = current_solution.get_rejectPickupPoints();
					solver.rejectDeliveryPoints = current_solution.get_rejectDeliveryPoints();
				}
				iS++;
			}

			solver.temperature = solver.cooling_rate * solver.temperature;

			// update probabilities
			if (it % solver.nw == 0) {
				for (int i = 0; i < solver.nInsertionOperators; i++) {
					pti[i] = Math.max(0.0001, pti[i] * (1 - solver.rp) + solver.rp * si[i] / wi[i]);
				}

				for (int i = 0; i < solver.nRemovalOperators; i++) {
					ptd[i] = Math.max(0.0001, ptd[i] * (1 - solver.rp) + solver.rp * sd[i] / wd[i]);
				}
			}
		}

		best_solution.copy2XR(solver.XR);
		solver.group2marked = best_solution.get_group2marked();

		solver.rejectPickupPoints = best_solution.get_rejectPickupPoints();
		solver.rejectDeliveryPoints = best_solution.get_rejectDeliveryPoints();
		try {
			FileOutputStream write = new FileOutputStream(outputfile, true);
			PrintWriter fo = new PrintWriter(write);
			fo.println(it + " -1 -1 " + System.currentTimeMillis() / 1000 + " " + best_cost + " "
					+ getNbRejectedRequests() + " " + getNbUsedTrucks());
			fo.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private int get_operator(double[] p) {
		int n = p.length;
		double[] s = new double[n];
		s[0] = 0 + p[0];

		for (int i = 1; i < n; i++)
			s[i] = s[i - 1] + p[i];

		double r = s[n - 1] * Math.random();

		if (r >= 0 && r <= s[0])
			return 0;

		for (int i = 1; i < n; i++) {
			if (r > s[i - 1] && r <= s[i])
				return i;
		}
		return -1;
	}
}

// ========== solver.opt.OptimizationStrategy ==========
interface OptimizationStrategy {
    void optimize(TruckContainerSolver solver, String outputFile);
}

// ========== solver.opt.SearchOptimumSolution ==========
class SearchOptimumSolution {
	TruckContainerSolver tcs;
	private final FPIUSInit initialSolutionBuilder;
	
	public SearchOptimumSolution(TruckContainerSolver tcs){
		super();
		this.tcs = tcs;
		this.initialSolutionBuilder = new FPIUSInit();
	}
	
	public void allRemoval(){
		System.out.println("all removal");
		tcs.mgr.performRemoveAllClientPoints();
		for(int i = 0; i < tcs.pickupPoints.size(); i++){
			Point pickup = tcs.pickupPoints.get(i);
			if(!tcs.rejectPickupPoints.contains(pickup)){
				tcs.rejectPickupPoints.add(pickup);
				tcs.rejectDeliveryPoints.add(tcs.pickup2Delivery.get(pickup));
			}
		}
		for(int k : tcs.group2marked.keySet())
			tcs.group2marked.put(k, 0);
	}
	
	public void routeRemoval(){
		Random r = new Random();
		int k = r.nextInt(tcs.XR.getNbRoutes()) + 1;
		System.out.println("routeRemoval: index of removed route = " + k);
		Point x = tcs.XR.getStartingPointOfRoute(k);
		Point next_x = tcs.XR.next(x);
		while(next_x != tcs.XR.getTerminatingPointOfRoute(k)){
			x = next_x;
			next_x = tcs.XR.next(x);
			tcs.mgr.performRemoveOnePoint(x);
			tcs.group2marked.put(tcs.point2Group.get(x), 0);
			if(tcs.point2Type.get(x) != tcs.START_MOOC
				&& tcs.point2Type.get(x) != tcs.END_MOOC){	
				if(tcs.pickup2Delivery.keySet().contains(x))
					tcs.rejectPickupPoints.add(x);
				else 
					tcs.rejectDeliveryPoints.add(x);
			}
			tcs.nChosed.put(x, tcs.nChosed.get(x)+1);
		}
		int groupTruck = tcs.point2Group.get(tcs.XR.getStartingPointOfRoute(k));
		tcs.group2marked.put(groupTruck, 0);
	}

	public void randomRequestRemoval(){
		Random R = new Random();
		int n = R.nextInt(tcs.upper_removal-tcs.lower_removal+1) + tcs.lower_removal;
		System.out.println("randomReqRemoval:number of removed request = " + n);
		if(n >= tcs.pickupPoints.size()){
			tcs.mgr.performRemoveAllClientPoints();
			for(int i = 0; i < tcs.pickupPoints.size(); i++){
				Point pickup = tcs.pickupPoints.get(i);
				if(!tcs.rejectPickupPoints.contains(pickup)){
					tcs.rejectPickupPoints.add(pickup);
					tcs.rejectDeliveryPoints.add(tcs.pickup2Delivery.get(pickup));
				}
			}
			for(int k : tcs.group2marked.keySet())
				tcs.group2marked.put(k, 0);
		}
		else{
			int i = 0;
			int c = 0;
			while(i < n && c++ < tcs.pickupPoints.size()){
				if(tcs.rejectPickupPoints.size() == tcs.pickupPoints.size())
					break;

				int rand = R.nextInt(tcs.pickupPoints.size());
				Point pickup = tcs.pickupPoints.get(rand);
				int ridx = tcs.XR.route(pickup);
				if(ridx == Constants.NULL_POINT)
					continue;
				if(!tcs.removeAllowed.get(pickup))
					continue;
				Point delivery = tcs.pickup2Delivery.get(tcs.pickupPoints.get(rand));
				tcs.mgr.performRemoveTwoPoints(pickup, delivery);
				tcs.rejectPickupPoints.add(pickup);
				tcs.rejectDeliveryPoints.add(delivery);
				tcs.group2marked.put(tcs.point2Group.get(pickup), 0);
				tcs.group2marked.put(tcs.point2Group.get(delivery), 0);
				if(tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(ridx)) <= 1){
					int groupTruck = tcs.point2Group.get(tcs.XR.getStartingPointOfRoute(ridx));
					tcs.group2marked.put(groupTruck, 0);
				}
				i++;
				tcs.nChosed.put(pickup, tcs.nChosed.get(pickup)+1);
				tcs.nChosed.put(delivery, tcs.nChosed.get(delivery)+1);
			}
		}
	}
	
	public void shaw_removal(){
		Random R = new Random();
		int nRemove = R.nextInt(tcs.upper_removal-tcs.lower_removal+1) + tcs.lower_removal;
		
		System.out.println("Shaw removal : number of request removed = " + nRemove);
		
		int ipRemove;
		
		/*
		 * select randomly request r1 and its delivery dr1
		 */
		Point r1 = null;
		int c = 0;
		do{
			if(tcs.rejectPickupPoints.size() == tcs.pickupPoints.size()
				|| c++ < tcs.pickupPoints.size()){
				r1 = null;
				break;
			}
			ipRemove = R.nextInt(tcs.pickupPoints.size());
			r1 = tcs.pickupPoints.get(ipRemove);	
		}while(nRemove > 0 && (tcs.rejectPickupPoints.contains(r1) || !tcs.removeAllowed.get(r1)));
		
		Point dr1 = null;
		if(r1 != null)
			dr1 = tcs.pickup2Delivery.get(r1);
		
		/*
		 * Remove request most related with r1
		 */
		int inRemove = 0;
		while(inRemove++ != nRemove && r1 != null && dr1 != null){
			
			Point removedPickup = null;
			Point removedDelivery = null;
			double relatedMin =  Double.MAX_VALUE;
			
			int routeOfR1 = tcs.XR.route(r1);
			/*
			 * Compute arrival time at request r1 and its delivery dr1
			 */
			System.out.println(r1 + " " + inRemove);
			System.out.println(routeOfR1);
			double arrivalTimeR1 = tcs.eat.getEarliestArrivalTime(tcs.XR.prev(r1))+
					tcs.serviceDuration.get(tcs.XR.prev(r1))+
					tcs.awm.getDistance(tcs.XR.prev(r1), r1);
			
			double serviceTimeR1 = 1.0*tcs.earliestAllowedArrivalTime.get(r1);
			serviceTimeR1 = arrivalTimeR1 > serviceTimeR1 ? arrivalTimeR1 : serviceTimeR1;
			
			double depatureTimeR1 = serviceTimeR1 + tcs.serviceDuration.get(r1);
			
			double arrivalTimeDR1 = tcs.eat.getEarliestArrivalTime(tcs.XR.prev(dr1))+
					tcs.serviceDuration.get(tcs.XR.prev(dr1))+
					tcs.awm.getDistance(tcs.XR.prev(dr1), dr1);
			
			double serviceTimeDR1 = 1.0*tcs.earliestAllowedArrivalTime.get(dr1);
			serviceTimeDR1 = arrivalTimeDR1 > serviceTimeDR1 ? arrivalTimeDR1 : serviceTimeDR1;
			
			double depatureTimeDR1 = serviceTimeDR1 + tcs.serviceDuration.get(dr1);
			
			tcs.rejectPickupPoints.add(r1);
			tcs.rejectDeliveryPoints.add(dr1);
			tcs.nChosed.put(r1, tcs.nChosed.get(r1)+1);
			tcs.nChosed.put(dr1, tcs.nChosed.get(dr1)+1);
			
			int ridx = tcs.XR.route(r1);
			tcs.group2marked.put(tcs.point2Group.get(r1), 0);
			tcs.group2marked.put(tcs.point2Group.get(dr1), 0);
			tcs.mgr.performRemoveTwoPoints(r1, dr1);
			
			if(tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(ridx)) <= 1){
				int groupTruck = tcs.point2Group.get(tcs.XR.getStartingPointOfRoute(ridx));
				tcs.group2marked.put(groupTruck, 0);
			}
			/*
			 * find the request is the most related with r1
			 */
			for(int k=1; k<=tcs.XR.getNbRoutes(); k++){
				Point x = tcs.XR.getStartingPointOfRoute(k);
				for(x = tcs.XR.next(x); x != tcs.XR.getTerminatingPointOfRoute(k); x = tcs.XR.next(x)){
					if(!tcs.removeAllowed.get(x))
						continue;
					Point dX = tcs.pickup2Delivery.get(x);
					if(dX == null)
						continue;
					
					/*
					 * Compute arrival time of x and its delivery dX
					 */
					double arrivalTimeX = tcs.eat.getEarliestArrivalTime(tcs.XR.prev(x))+
							tcs.serviceDuration.get(tcs.XR.prev(x))+
							tcs.awm.getDistance(tcs.XR.prev(x), x);
					
					double serviceTimeX = 1.0*tcs.earliestAllowedArrivalTime.get(x);
					serviceTimeX = arrivalTimeX > serviceTimeX ? arrivalTimeX : serviceTimeX;
					
					double depatureTimeX = serviceTimeX + tcs.serviceDuration.get(x);
					
					double arrivalTimeDX =  tcs.eat.getEarliestArrivalTime(tcs.XR.prev(dX))+
							tcs.serviceDuration.get(tcs.XR.prev(dX))+
							tcs.awm.getDistance(tcs.XR.prev(dX), dX);
					
					double serviceTimeDX = 1.0*tcs.earliestAllowedArrivalTime.get(dX);
					serviceTimeDX = arrivalTimeDX > serviceTimeDX ? arrivalTimeDX : serviceTimeDX;
					
					double depatureTimeDX = serviceTimeDX + tcs.serviceDuration.get(dX);
					
					/*
					 * Compute related between r1 and x
					 */
					int lr1x;
					if(routeOfR1 == k){
						lr1x = 1;
					}else{
						lr1x = -1;
					}
					
					double related = tcs.shaw1st*(tcs.awm.getDistance(r1, x) + tcs.awm.getDistance(dX, dr1))+
							tcs.shaw2nd*(Math.abs(depatureTimeR1-depatureTimeX) + Math.abs(depatureTimeDX-depatureTimeDR1))+
							tcs.shaw3rd*lr1x;
					if(related < relatedMin){
						relatedMin = related;
						removedPickup = x;
						removedDelivery = dX;
					}
				}
			}
			
			r1 = removedPickup;
			dr1 = removedDelivery;
			if(r1 != null){
				tcs.nChosed.put(r1, tcs.nChosed.get(r1)+1);
				tcs.nChosed.put(dr1, tcs.nChosed.get(dr1)+1);
			}
		}
		
	}
	
	public void worst_removal(){
		Random R = new Random();
		int nRemove = R.nextInt(tcs.upper_removal-tcs.lower_removal+1) + tcs.lower_removal;
		System.out.println("worstRemoval: nRemove = " + nRemove);
		
		int inRemove = 0;
		int c = 0;
		while(inRemove++ != nRemove && c++ < tcs.pickupPoints.size()){
			if(tcs.rejectPickupPoints.size() == tcs.pickupPoints.size())
				break;
			double maxCost = Double.MIN_VALUE;
			Point removedPickup = null;
			Point removedDelivery = null;
			
			for(int k=1; k<=tcs.XR.getNbRoutes(); k++){
				Point x = tcs.XR.getStartingPointOfRoute(k);
				for(x = tcs.XR.next(x); x != tcs.XR.getTerminatingPointOfRoute(k); x = tcs.XR.next(x)){
					
					if(!tcs.removeAllowed.get(x))
						continue;
					
					Point dX = tcs.pickup2Delivery.get(x);
					if(dX == null){
						continue;
					}
					
					double cost = tcs.objective.evaluateRemoveTwoPoints(x, dX);
					if(cost > maxCost){
						maxCost = cost;
						removedPickup = x;
						removedDelivery = dX;
					}
				}
			}
			
			if(removedDelivery == null || removedPickup == null)
				break;
			int ridx = tcs.XR.route(removedPickup);
			tcs.rejectPickupPoints.add(removedPickup);
			tcs.rejectDeliveryPoints.add(removedDelivery);
			
			tcs.nChosed.put(removedDelivery, tcs.nChosed.get(removedDelivery)+1);
			tcs.nChosed.put(removedPickup, tcs.nChosed.get(removedPickup)+1);
			
			tcs.group2marked.put(tcs.point2Group.get(removedPickup), 0);
			tcs.group2marked.put(tcs.point2Group.get(removedDelivery), 0);	
			tcs.mgr.performRemoveTwoPoints(removedPickup, removedDelivery);
			if(tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(ridx)) <= 1){
				int groupTruck = tcs.point2Group.get(tcs.XR.getStartingPointOfRoute(ridx));
				tcs.group2marked.put(groupTruck, 0);
			}
		}
	}
	
	public void forbidden_removal(int nRemoval){
		
		System.out.println("forbidden_removal");
		
		for(int i=0; i < tcs.pickupPoints.size(); i++){
			Point pi = tcs.pickupPoints.get(i);
			Point pj = tcs.pickup2Delivery.get(pi);
			
			if(tcs.nChosed.get(pi) > tcs.nTabu){
				tcs.removeAllowed.put(pi, false);
				tcs.removeAllowed.put(pj, false);
			}
		}
		
		switch(nRemoval){
			case 0: routeRemoval(); break;
			case 1: randomRequestRemoval(); break;
			case 2: shaw_removal(); break;
			case 3: worst_removal(); break;
		}
		
		for(int i=0; i < tcs.pickupPoints.size(); i++){
			Point pi = tcs.pickupPoints.get(i);
			tcs.removeAllowed.put(pi, true);
			Point pj = tcs.pickup2Delivery.get(pi);
			tcs.removeAllowed.put(pj, true);
		}
	}
	
	public void greedyInsertion(){
		System.out.println("greedyInsertion");
		int c = 0;
		for(int i = 0; i < tcs.rejectPickupPoints.size(); i++){
			Point pickup = tcs.rejectPickupPoints.get(i);
			int groupId = tcs.point2Group.get(pickup);
			
			if(tcs.XR.route(pickup) != Constants.NULL_POINT
				|| tcs.group2marked.get(groupId) == 1)
				continue;
			//System.out.println(c++);
			Point delivery = tcs.pickup2Delivery.get(pickup);
			//add the request to route
			Point pre_pick = null;
			Point pre_delivery = null;
			double best_objective = Double.MAX_VALUE;
			for(int r = 1; r <= tcs.XR.getNbRoutes(); r++){
				Point st = tcs.XR.getStartingPointOfRoute(r);
				
				int groupTruck = tcs.point2Group.get(st);
				if(tcs.group2marked.get(groupTruck) == 1 
						&& tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(r)) <= 1)
					continue;
				for(Point p = st; p != tcs.XR.getTerminatingPointOfRoute(r); p = tcs.XR.next(p)){
					for(Point q = p; q != tcs.XR.getTerminatingPointOfRoute(r); q = tcs.XR.next(q)){
//						if((tcs.XR.prev(p)!= null && tcs.XR.prev(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1)
//								|| (tcs.XR.next(p) != null && tcs.XR.next(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1))
//								System.out.println("bug");
						tcs.mgr.performAddTwoPoints(pickup, p, delivery, q);
							initialSolutionBuilder.insertMoocToRoutes(tcs, r);
						if(tcs.S.violations() == 0){
							double cost = tcs.objective.getValue();
							if( cost < best_objective){
								best_objective = cost;
								pre_pick = p;
								pre_delivery = q;
							}
						}
						tcs.mgr.performRemoveTwoPoints(pickup, delivery);
							initialSolutionBuilder.removeMoocOnRoutes(tcs, r);
					}
				}
			}
			if(pre_pick != null && pre_delivery != null){
				tcs.mgr.performAddTwoPoints(pickup, pre_pick, delivery, pre_delivery);
				Point st = tcs.XR.getStartingPointOfRoute(tcs.XR.route(pre_pick));
				tcs.rejectPickupPoints.remove(pickup);
				tcs.rejectDeliveryPoints.remove(delivery);
				int groupTruck = tcs.point2Group.get(st);
				tcs.group2marked.put(groupTruck, 1);
				tcs.group2marked.put(groupId, 1);
				i--;
			}
		}
		initialSolutionBuilder.insertMoocForAllRoutes(tcs);
	}
	
	public void greedyInsertionWithNoise(){
		System.out.println("greedyInsertionWithNoise");
//		HashMap<Truck, Integer> truck2marked = new HashMap<Truck, Integer>();
//		Truck[] trucks = tcs.input.getTrucks();
//		for(int i = 0; i < trucks.length; i++)
//			truck2marked.put(trucks[i], 0);
//		for(int r = 1; r <= tcs.XR.getNbRoutes(); r++){
//			if(tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(r)) > 1){
//				Point st = tcs.XR.getStartingPointOfRoute(r);
//				Truck truck = tcs.startPoint2Truck.get(st);
//				truck2marked.put(truck, 1);
//			}
//		}
		
		for(int i = 0; i < tcs.rejectPickupPoints.size(); i++){
			Point pickup = tcs.rejectPickupPoints.get(i);
			int groupId = tcs.point2Group.get(pickup);
			
			if(tcs.XR.route(pickup) != Constants.NULL_POINT
				|| tcs.group2marked.get(groupId) == 1)
				continue;
			Point delivery = tcs.pickup2Delivery.get(pickup);
			//add the request to route
			Point pre_pick = null;
			Point pre_delivery = null;
			double best_objective = Double.MAX_VALUE;
			for(int r = 1; r <= tcs.XR.getNbRoutes(); r++){
				Point st = tcs.XR.getStartingPointOfRoute(r);

				int groupTruck = tcs.point2Group.get(st);

				if(tcs.group2marked.get(groupTruck) == 1 && tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(r)) <= 1)
					continue;
				
				for(Point p = st; p != tcs.XR.getTerminatingPointOfRoute(r); p = tcs.XR.next(p)){
					for(Point q = p; q != tcs.XR.getTerminatingPointOfRoute(r); q = tcs.XR.next(q)){
//						if((tcs.XR.prev(p)!= null && tcs.XR.prev(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1)
//								|| (tcs.XR.next(p) != null && tcs.XR.next(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1))
//								System.out.println("bug");
						tcs.mgr.performAddTwoPoints(pickup, p, delivery, q);
						initialSolutionBuilder.insertMoocToRoutes(tcs, r);
						
						if(tcs.S.violations() == 0){
							double cost = tcs.objective.getValue();
							double ran = Math.random()*2-1;
							cost += TruckContainerSolver.MAX_TRAVELTIME*0.1*ran;
							if( cost < best_objective){
								best_objective = cost;
								pre_pick = p;
								pre_delivery = q;
							}
						}
						tcs.mgr.performRemoveTwoPoints(pickup, delivery);
						initialSolutionBuilder.removeMoocOnRoutes(tcs, r);
					}
				}
			}
			if(pre_pick != null && pre_delivery != null){
				tcs.mgr.performAddTwoPoints(pickup, pre_pick, delivery, pre_delivery);
				Point st = tcs.XR.getStartingPointOfRoute(tcs.XR.route(pre_pick));
				int groupTruck = tcs.point2Group.get(st);
				tcs.group2marked.put(groupTruck, 1);
				tcs.rejectPickupPoints.remove(pickup);
				tcs.rejectDeliveryPoints.remove(delivery);
				tcs.group2marked.put(groupId, 1);
				i--;
			}
		}
		initialSolutionBuilder.insertMoocForAllRoutes(tcs);
	}
	
	public void regret_n_insertion(int n){
		System.out.println("regret insertion n = " + n);
		
		for(int i = 0; i < tcs.rejectPickupPoints.size(); i++){
			Point pickup = tcs.rejectPickupPoints.get(i);
			int groupId = tcs.point2Group.get(pickup);
			
			if(tcs.XR.route(pickup) != Constants.NULL_POINT
				|| tcs.group2marked.get(groupId) == 1)
				continue;
			Point delivery = tcs.pickup2Delivery.get(pickup);
			//add the request to route
			Point pre_pick = null;
			Point pre_delivery = null;
			double n_best_objective[] = new double[n];
			double best_regret_value = Double.MIN_VALUE;
			
			for(int it=0; it<n; it++){
				n_best_objective[it] = Double.MAX_VALUE;
			}

			for(int r = 1; r <= tcs.XR.getNbRoutes(); r++){
				Point st = tcs.XR.getStartingPointOfRoute(r);
				int groupTruck = tcs.point2Group.get(st);

				if(tcs.group2marked.get(groupTruck) == 1 && tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(r)) <= 1)
					continue;
				
				for(Point p = st; p != tcs.XR.getTerminatingPointOfRoute(r); p = tcs.XR.next(p)){
					for(Point q = p; q != tcs.XR.getTerminatingPointOfRoute(r); q = tcs.XR.next(q)){
//						if((tcs.XR.prev(p)!= null && tcs.XR.prev(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1)
//								|| (tcs.XR.next(p) != null && tcs.XR.next(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1))
//								System.out.println("bug");
						tcs.mgr.performAddTwoPoints(pickup, p, delivery, q);
						initialSolutionBuilder.insertMoocToRoutes(tcs, r);
						if(tcs.S.violations() == 0){
							double cost = tcs.objective.getValue();
							for(int it=0; it<n; it++){
								if(n_best_objective[it] > cost){
									for(int it2 = n-1; it2 > it; it2--){
										n_best_objective[it2] = n_best_objective[it2-1];
									}
									n_best_objective[it] = cost;
									break;
								}
							}
							double regret_value = 0;
							for(int it=1; it<n; it++){
								regret_value += Math.abs(n_best_objective[it] - n_best_objective[0]);
							}
							if(regret_value > best_regret_value){
								best_regret_value = regret_value;
								pre_pick = p;
								pre_delivery = q;
							}
						}
						tcs.mgr.performRemoveTwoPoints(pickup, delivery);
						initialSolutionBuilder.removeMoocOnRoutes(tcs, r);
					}
				}
			}
			if(pre_pick != null && pre_delivery != null){
				tcs.mgr.performAddTwoPoints(pickup, pre_pick, delivery, pre_delivery);
				Point st = tcs.XR.getStartingPointOfRoute(tcs.XR.route(pre_pick));
				int groupTruck = tcs.point2Group.get(st);
				tcs.group2marked.put(groupTruck, 1);
				tcs.rejectPickupPoints.remove(pickup);
				tcs.rejectDeliveryPoints.remove(delivery);
				tcs.group2marked.put(groupId, 1);
				i--;
			}
		}
		initialSolutionBuilder.insertMoocForAllRoutes(tcs);
	}
	
	public void first_possible_insertion(){
		System.out.println("first_possible_insertion");
		
		for(int i = 0; i < tcs.rejectPickupPoints.size(); i++){
			Point pickup = tcs.rejectPickupPoints.get(i);
			int groupId = tcs.point2Group.get(pickup);
			
			if(tcs.XR.route(pickup) != Constants.NULL_POINT
				|| tcs.group2marked.get(groupId) == 1)
				continue;
			Point delivery = tcs.pickup2Delivery.get(pickup);
			//add the request to route
			Point pre_pick = null;
			Point pre_delivery = null;
			double best_objective = Double.MAX_VALUE;
			boolean finded = false;
			for(int r = 1; r <= tcs.XR.getNbRoutes(); r++){
				if(finded)
					break;
				Point st = tcs.XR.getStartingPointOfRoute(r);
				int groupTruck = tcs.point2Group.get(st);
				if(tcs.group2marked.get(groupTruck) == 1 && tcs.XR.index(tcs.XR.getTerminatingPointOfRoute(r)) <= 1)
					continue;
				
				for(Point p = st; p != tcs.XR.getTerminatingPointOfRoute(r); p = tcs.XR.next(p)){
					if(finded)
						break;
					for(Point q = p; q != tcs.XR.getTerminatingPointOfRoute(r); q = tcs.XR.next(q)){
//						if((tcs.XR.prev(p)!= null && tcs.XR.prev(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1)
//								|| (tcs.XR.next(p) != null && tcs.XR.next(p).getID() % 2 == 1
//								&& p.getID() % 2 == 1
//								&& pickup.getID() % 2 == 1))
//								System.out.println("bug");
						tcs.mgr.performAddTwoPoints(pickup, p, delivery, q);
						initialSolutionBuilder.insertMoocToRoutes(tcs, r);
						if(tcs.S.violations() == 0){
							double cost = tcs.objective.getValue();
							if( cost < best_objective){
								initialSolutionBuilder.removeMoocOnRoutes(tcs, r);
								tcs.group2marked.put(groupTruck, 1);
								tcs.rejectPickupPoints.remove(pickup);
								tcs.rejectDeliveryPoints.remove(delivery);
								tcs.group2marked.put(groupId, 1);
								finded = true;
								i--;
								break;
							}
						}
						tcs.mgr.performRemoveTwoPoints(pickup, delivery);
						initialSolutionBuilder.removeMoocOnRoutes(tcs, r);
					}
				}
			}
		}
		initialSolutionBuilder.insertMoocForAllRoutes(tcs);
	}
	
	public void sort_before_insertion(int iInsertion){
		System.out.println("sort_before_insertion");
		
		sort_reject_people();
		
		switch(iInsertion){
			case 0: greedyInsertion(); break;
			case 1: greedyInsertionWithNoise(); break;
			case 2: regret_n_insertion(2); break;
			case 3: first_possible_insertion(); break;
		}
		
		Collections.shuffle(tcs.rejectPickupPoints);
	}
	
	private void sort_reject_people(){
		HashMap<Point, Integer> time_flexibility = new HashMap<Point, Integer>();
		
		for(int i = 0; i < tcs.rejectPickupPoints.size(); i++){
			Point pickup = tcs.rejectPickupPoints.get(i);
			Point delivery = tcs.pickup2Delivery.get(pickup);
			
			int lp = tcs.lastestAllowedArrivalTime.get(pickup);
			int ud = tcs.earliestAllowedArrivalTime.get(delivery);
			
			time_flexibility.put(pickup, ud-lp);
		}
		
		List<Point> keys = new ArrayList<Point>(time_flexibility.keySet());
		List<Integer> values = new ArrayList<Integer>(time_flexibility.values());
		Collections.sort(values);
		
		ArrayList<Point> rejectPointSorted = new ArrayList<Point>();
		for(int i = 0; i < values.size(); i++){
			int v = values.get(i);
			for(int j = 0; j < keys.size(); j++){
				Point p = keys.get(j);
				int vs = time_flexibility.get(p);
				if(vs == v){
					rejectPointSorted.add(p);
					keys.remove(p);
					break;
				}
			}
		}
		tcs.rejectPickupPoints = rejectPointSorted;
	}
}

// ========== solver.DataMapper ==========
class DataMapper {
    
    private ContainerTruckMoocInput input;
    
    // Maps and data structures
    public String[] locationCodes;
    public HashMap<String, Integer> mLocationCode2Index;
    public double[][] distance;
    public double[][] travelTime;
    
    public HashMap<String, Truck> mCode2Truck;
    public HashMap<String, Mooc> mCode2Mooc;
    public HashMap<String, Container> mCode2Container;
    public HashMap<String, DepotContainer> mCode2DepotContainer;
    public HashMap<String, DepotTruck> mCode2DepotTruck;
    public HashMap<String, DepotMooc> mCode2DepotMooc;
    public HashMap<String, Warehouse> mCode2Warehouse;
    public HashMap<String, Port> mCode2Port;
    public ArrayList<Container> additionalContainers;
    
    public DataMapper() {
        // Constructor
    }
    
    /**
     * Reads and parses input data from JSON file and populates solver fields.
     * This method directly fills all solver properties, eliminating the need for loadData().
     * 
     * @param fileName Path to the input JSON file
     * @param solver The TruckContainerSolver to populate with data
     * @return The parsed ContainerTruckMoocInput object
     */
    public ContainerTruckMoocInput readData(String fileName, TruckContainerSolver solver) {
        try {
            Gson g = new Gson();
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            input = g.fromJson(in, ContainerTruckMoocInput.class);
            in.close();
            
            InputAnalyzer IA = new InputAnalyzer();
            IA.standardize(input);
            
            mapData(fileName);
            
            // Directly populate solver fields
            solver.input = input;
            solver.locationCodes = this.locationCodes;
            solver.mLocationCode2Index = this.mLocationCode2Index;
            solver.distance = this.distance;
            solver.travelTime = this.travelTime;
            solver.mCode2Truck = this.mCode2Truck;
            solver.mCode2Mooc = this.mCode2Mooc;
            solver.mCode2Container = this.mCode2Container;
            solver.mCode2DepotContainer = this.mCode2DepotContainer;
            solver.mCode2DepotTruck = this.mCode2DepotTruck;
            solver.mCode2DepotMooc = this.mCode2DepotMooc;
            solver.mCode2Warehouse = this.mCode2Warehouse;
            solver.mCode2Port = this.mCode2Port;
            solver.additionalContainers = this.additionalContainers;
            
            return input;
        } catch (Exception e) {
            System.out.println("Error reading data: " + e);
            return null;
        }
    }
    
    private void mapData(String dataFileName) {
        processContainers();
        processLocationCodes();
        processDistanceAndTravelTime();
        processDepotContainers();
        processDepotMoocs();
        processDepotTrucks();
        processWarehouses();
        processMoocs();
        processTrucks();
        processContainerMapping();
        processPorts();
    }
    
    private void processContainers() {
        additionalContainers = new ArrayList<Container>();
        int idxCode = -1;
        
        if (input.getImEmptyRequests() != null) {
            for (int i = 0; i < input.getImEmptyRequests().length; i++) {
                ImportEmptyRequests R = input.getImEmptyRequests()[i];
                idxCode++;
                String code = "A-" + idxCode;
                String depotContainerCode = null;
                
                if (R.getDepotContainerCode() != null)
                    depotContainerCode = R.getDepotContainerCode();
                else {
                    int idx = 0;
                    depotContainerCode = input.getDepotContainers()[idx].getCode();
                }
                
                String[] returnDepot = new String[1];
                returnDepot[0] = depotContainerCode;
                
                Container c = new Container(code, (int) R.getWeight(),
                        R.getContainerCategory(), depotContainerCode, returnDepot);
                additionalContainers.add(c);
                R.setContainerCode(code);
            }
        }
        
        ArrayList<String> containerCodes = new ArrayList<String>();
        Container[] temp = input.getContainers();
        ArrayList<Container> cL = new ArrayList<Container>();
        
        for (int i = 0; i < temp.length; i++) {
            if (!containerCodes.contains(temp[i].getCode())) {
                containerCodes.add(temp[i].getCode());
                cL.add(temp[i]);
            }
        }
        
        Container[] L = new Container[cL.size() + additionalContainers.size()];
        for (int i = 0; i < cL.size(); i++) {
            L[i] = cL.get(i);
            L[i].setImportedContainer(false);
        }
        for (int i = 0; i < additionalContainers.size(); i++) {
            L[i + cL.size()] = additionalContainers.get(i);
            L[i + cL.size()].setImportedContainer(true);
        }
        input.setContainers(L);
    }
    
    private void processLocationCodes() {
        HashSet<String> s_locationCode = new HashSet<String>();
        for (int i = 0; i < input.getDistance().length; i++) {
            DistanceElement e = input.getDistance()[i];
            s_locationCode.add(e.getSrcCode());
            s_locationCode.add(e.getDestCode());
        }
        
        locationCodes = new String[s_locationCode.size()];
        mLocationCode2Index = new HashMap<String, Integer>();
        int idx = -1;
        for (String lc : s_locationCode) {
            idx++;
            locationCodes[idx] = lc;
            mLocationCode2Index.put(lc, idx);
        }
    }
    
    private void processDistanceAndTravelTime() {
        int size = locationCodes.length;
        distance = new double[size][size];
        travelTime = new double[size][size];
        
        for (int i = 0; i < input.getDistance().length; i++) {
            DistanceElement e = input.getDistance()[i];
            int is = mLocationCode2Index.get(e.getSrcCode());
            int id = mLocationCode2Index.get(e.getDestCode());
            distance[is][id] = e.getDistance();
            travelTime[is][id] = e.getTravelTime();
        }
    }
    
    private void processDepotContainers() {
        ArrayList<DepotContainer> dcL = new ArrayList<DepotContainer>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getDepotContainers().length; i++) {
            if (!codes.contains(input.getDepotContainers()[i].getCode())) {
                codes.add(input.getDepotContainers()[i].getCode());
                dcL.add(input.getDepotContainers()[i]);
            }
        }
        
        DepotContainer[] dpc = new DepotContainer[dcL.size()];
        for (int i = 0; i < dcL.size(); i++)
            dpc[i] = dcL.get(i);
        input.setDepotContainers(dpc);
        
        mCode2DepotContainer = new HashMap<String, DepotContainer>();
        for (int i = 0; i < input.getDepotContainers().length; i++) {
            mCode2DepotContainer.put(input.getDepotContainers()[i].getCode(),
                    input.getDepotContainers()[i]);
        }
    }
    
    private void processDepotMoocs() {
        ArrayList<DepotMooc> depotMoocList = new ArrayList<DepotMooc>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getDepotMoocs().length; i++) {
            if (!codes.contains(input.getDepotMoocs()[i].getCode())) {
                codes.add(input.getDepotMoocs()[i].getCode());
                depotMoocList.add(input.getDepotMoocs()[i]);
            }
        }
        
        DepotMooc[] dpm = new DepotMooc[depotMoocList.size()];
        for (int i = 0; i < depotMoocList.size(); i++)
            dpm[i] = depotMoocList.get(i);
        input.setDepotMoocs(dpm);
        
        mCode2DepotMooc = new HashMap<String, DepotMooc>();
        for (int i = 0; i < input.getDepotMoocs().length; i++) {
            mCode2DepotMooc.put(input.getDepotMoocs()[i].getCode(),
                    input.getDepotMoocs()[i]);
        }
    }
    
    private void processDepotTrucks() {
        ArrayList<DepotTruck> depotTruckList = new ArrayList<DepotTruck>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getDepotTrucks().length; i++) {
            if (!codes.contains(input.getDepotTrucks()[i].getCode())) {
                codes.add(input.getDepotTrucks()[i].getCode());
                depotTruckList.add(input.getDepotTrucks()[i]);
            }
        }
        
        DepotTruck[] dpt = new DepotTruck[depotTruckList.size()];
        for (int i = 0; i < depotTruckList.size(); i++)
            dpt[i] = depotTruckList.get(i);
        input.setDepotTrucks(dpt);
        
        mCode2DepotTruck = new HashMap<String, DepotTruck>();
        for (int i = 0; i < input.getDepotTrucks().length; i++) {
            mCode2DepotTruck.put(input.getDepotTrucks()[i].getCode(),
                    input.getDepotTrucks()[i]);
        }
    }
    
    private void processWarehouses() {
        ArrayList<Warehouse> whList = new ArrayList<Warehouse>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getWarehouses().length; i++) {
            if (!codes.contains(input.getWarehouses()[i].getCode())) {
                codes.add(input.getWarehouses()[i].getCode());
                whList.add(input.getWarehouses()[i]);
            }
        }
        
        Warehouse[] whs = new Warehouse[whList.size()];
        for (int i = 0; i < whList.size(); i++)
            whs[i] = whList.get(i);
        input.setWarehouses(whs);
        
        mCode2Warehouse = new HashMap<String, Warehouse>();
        for (int i = 0; i < input.getWarehouses().length; i++) {
            mCode2Warehouse.put(input.getWarehouses()[i].getCode(),
                    input.getWarehouses()[i]);
        }
    }
    
    private void processMoocs() {
        ArrayList<Mooc> moocList = new ArrayList<Mooc>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getMoocs().length; i++) {
            if (!codes.contains(input.getMoocs()[i].getCode())) {
                codes.add(input.getMoocs()[i].getCode());
                moocList.add(input.getMoocs()[i]);
            }
        }
        
        Mooc[] ms = new Mooc[moocList.size()];
        for (int i = 0; i < moocList.size(); i++)
            ms[i] = moocList.get(i);
        input.setMoocs(ms);
        
        mCode2Mooc = new HashMap<String, Mooc>();
        for (int i = 0; i < input.getMoocs().length; i++) {
            Mooc mooc = input.getMoocs()[i];
            mCode2Mooc.put(mooc.getCode(), mooc);
        }
    }
    
    private void processTrucks() {
        ArrayList<Truck> truckList = new ArrayList<Truck>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getTrucks().length; i++) {
            if (!codes.contains(input.getTrucks()[i].getCode())) {
                codes.add(input.getTrucks()[i].getCode());
                truckList.add(input.getTrucks()[i]);
            }
        }
        
        Truck[] ts = new Truck[truckList.size()];
        for (int i = 0; i < truckList.size(); i++)
            ts[i] = truckList.get(i);
        input.setTrucks(ts);
        
        mCode2Truck = new HashMap<String, Truck>();
        for (int i = 0; i < input.getTrucks().length; i++) {
            Truck truck = input.getTrucks()[i];
            mCode2Truck.put(truck.getCode(), truck);
        }
    }
    
    private void processContainerMapping() {
        mCode2Container = new HashMap<String, Container>();
        for (int i = 0; i < input.getContainers().length; i++) {
            Container c = input.getContainers()[i];
            mCode2Container.put(c.getCode(), c);
        }
    }
    
    private void processPorts() {
        ArrayList<Port> portList = new ArrayList<Port>();
        ArrayList<String> codes = new ArrayList<String>();
        
        for (int i = 0; i < input.getPorts().length; i++) {
            if (!codes.contains(input.getPorts()[i].getCode())) {
                codes.add(input.getPorts()[i].getCode());
                portList.add(input.getPorts()[i]);
            }
        }
        
        Port[] ps = new Port[portList.size()];
        for (int i = 0; i < portList.size(); i++)
            ps[i] = portList.get(i);
        input.setPorts(ps);
        
        mCode2Port = new HashMap<String, Port>();
        for (int i = 0; i < input.getPorts().length; i++) {
            mCode2Port.put(input.getPorts()[i].getCode(), input.getPorts()[i]);
        }
    }
    
    // Getters for all processed data
    public ContainerTruckMoocInput getInput() {
        return input;
    }
}

// ========== solver.InputAnalyzer ==========
class InputAnalyzer {
	public static final int OFFSET_SECOND = 259200;
	public String analyze(ContainerTruckMoocInput input){
		
		return "OK";
	}
	public void standardize(ContainerTruckMoocInput input){
		// set startWorkingTime of trucks if NULL
		int minDateTime = Integer.MAX_VALUE;
		if(input.getExRequests() != null){
			for(int i = 0; i < input.getExRequests().length; i++){
				ExportContainerTruckMoocRequest R = input.getExRequests()[i];
				if(R.getContainerRequest() != null){
					for(int j = 0; j< R.getContainerRequest().length; j++){
						ExportContainerRequest r = R.getContainerRequest()[j];
						if(r.getEarlyDateTimePickupAtDepot() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimePickupAtDepot());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getEarlyDateTimeUnloadAtPort() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeUnloadAtPort());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimeLoadAtWarehouse() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeLoadAtWarehouse());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimePickupAtDepot() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimePickupAtDepot());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimeUnloadAtPort() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeUnloadAtPort());
							if(dt < minDateTime) minDateTime = dt;
						}
					}
				}
			}
		}
		
		if(input.getImRequests() != null){
			for(int i = 0; i < input.getImRequests().length; i++){
				ImportContainerTruckMoocRequest R = input.getImRequests()[i];
				if(R.getContainerRequest() != null){
					for(int j = 0; j < R.getContainerRequest().length; j++){
						ImportContainerRequest r = R.getContainerRequest()[j];
						if(r.getEarlyDateTimeDeliveryAtDepot() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeDeliveryAtDepot());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getEarlyDateTimePickupAtPort() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimePickupAtPort());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimeDeliveryAtDepot() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeDeliveryAtDepot());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimePickupAtPort() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimePickupAtPort());
							if(dt < minDateTime) minDateTime = dt;
						}
					}
				}
			}
		}
		
		if(input.getImEmptyRequests() != null){
			for(int i = 0; i < input.getImEmptyRequests().length; i++){
				ImportEmptyRequests r = input.getImEmptyRequests()[i];
				if(r.getEarlyDateTimeAttachAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeAttachAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}

				if(r.getLateDateTimeReturnEmptyAtDepot() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeReturnEmptyAtDepot());
					if(dt < minDateTime) minDateTime = dt;
				}
			}
		}
		if(input.getImLadenRequests() != null){
			for(int i= 0; i < input.getImLadenRequests().length; i++){
				ImportLadenRequests r = input.getImLadenRequests()[i];
//				if(r.getRequestDate() != null){
//					int dt = (int)DateTimeUtils.dateTime2Int(r.getRequestDate());
//					if(dt < minDateTime) minDateTime = dt;
//				}
				if(r.getLateDateTimePickupAtPort() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimePickupAtPort());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getLateDateTimeUnloadAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeUnloadAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getEarlyDateTimePickupAtPort() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimePickupAtPort());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getEarlyDateTimeUnloadAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeUnloadAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}
			}
		}
		
		if(input.getExEmptyRequests() != null){
			for(int i = 0; i < input.getExEmptyRequests().length; i++){
				ExportEmptyRequests r = input.getExEmptyRequests()[i];
//				if(r.getRequestDate() != null){
//					int dt = (int)DateTimeUtils.dateTime2Int(r.getRequestDate());
//					if(dt < minDateTime) minDateTime = dt;
//				}
				if(r.getEarlyDateTimeLoadAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeLoadAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getEarlyDateTimePickupAtDepot() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimePickupAtDepot());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getLateDateTimeLoadAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeLoadAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getLateDateTimePickupAtDepot() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimePickupAtDepot());
					if(dt < minDateTime) minDateTime = dt;
				}
			}
		}
		if(input.getExLadenRequests() != null){
			for(int i = 0; i < input.getExLadenRequests().length; i++){
				ExportLadenRequests r = input.getExLadenRequests()[i];
//				if(r.getRequestDate() != null){
//					int dt = (int)DateTimeUtils.dateTime2Int(r.getRequestDate());
//					if(dt < minDateTime) minDateTime = dt;
//				}
//				if(r.getEarlyDateTimeUnloadAtPort() != null){
//					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeUnloadAtPort());
//					if(dt < minDateTime) minDateTime = dt;
//				}
				if(r.getEarlyDateTimeAttachAtWarehouse() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeAttachAtWarehouse());
					if(dt < minDateTime) minDateTime = dt;
				}
				if(r.getLateDateTimeUnloadAtPort() != null){
					int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeUnloadAtPort());
					if(dt < minDateTime) minDateTime = dt;
				}
			}
		}
		
		if(input.getWarehouseRequests() != null){
			for(int i = 0; i < input.getWarehouseRequests().length; i++){
				WarehouseTransportRequest R = input.getWarehouseRequests()[i];
				if(R.getWarehouseContainerTransportRequests() != null){
					for(int j = 0; j< R.getWarehouseContainerTransportRequests().length; j++){
						WarehouseContainerTransportRequest r = R.getWarehouseContainerTransportRequests()[j];
						if(r.getEarlyDateTimeLoad() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeLoad());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getEarlyDateTimeUnload() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getEarlyDateTimeUnload());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimeLoad() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeLoad());
							if(dt < minDateTime) minDateTime = dt;
						}
						if(r.getLateDateTimeUnload() != null){
							int dt = (int)DateTimeUtils.dateTime2Int(r.getLateDateTimeUnload());
							if(dt < minDateTime) minDateTime = dt;
						}
					}
				}
			}
		}
		
		if(input.getTrucks() != null){
			for(int i = 0; i < input.getTrucks().length; i++){
				Truck truck = input.getTrucks()[i];
				if(truck.getStartWorkingTime() == null){
					truck.setStartWorkingTime(DateTimeUtils.unixTimeStamp2DateTime(minDateTime - OFFSET_SECOND));
				}
			}
		}
	}
	
}

// ========== solver.TruckContainerInitializer ==========
class TruckContainerInitializer {

	public void init(TruckContainerSolver solver) {
		loadRequestsAndCounts(solver);
		initCollections(solver);

		int id = 0;
		int groupId = 0;

		IdAndGroup cursor = new IdAndGroup(id, groupId);
		cursor = buildTruckPoints(solver, cursor);
		cursor = buildMoocPoints(solver, cursor);
		cursor = buildExportEmptyRequestPoints(solver, cursor);
		cursor = buildExportLadenRequestPoints(solver, cursor);
		cursor = buildImportEmptyRequestPoints(solver, cursor);
		cursor = buildImportLadenRequestPoints(solver, cursor);

		buildWeightManagersAndMaxTravelTime(solver);
	}

	private void loadRequestsAndCounts(TruckContainerSolver solver) {
		solver.exEmptyRequests = solver.input.getExEmptyRequests();
		solver.exLadenRequests = solver.input.getExLadenRequests();
		solver.imEmptyRequests = solver.input.getImEmptyRequests();
		solver.imLadenRequests = solver.input.getImLadenRequests();

		solver.nRequest = solver.exEmptyRequests.length + solver.exLadenRequests.length + solver.imEmptyRequests.length
				+ solver.imLadenRequests.length;
		solver.nVehicle = solver.input.getTrucks().length;
	}

	private void initCollections(TruckContainerSolver solver) {
		solver.points = new ArrayList<Point>();
		solver.earliestAllowedArrivalTime = new HashMap<Point, Integer>();
		solver.serviceDuration = new HashMap<Point, Integer>();
		solver.lastestAllowedArrivalTime = new HashMap<Point, Integer>();

		solver.pickupPoints = new ArrayList<Point>();
		solver.deliveryPoints = new ArrayList<Point>();
		solver.rejectPickupPoints = new ArrayList<Point>();
		solver.rejectDeliveryPoints = new ArrayList<Point>();
		solver.startPoints = new ArrayList<Point>();
		solver.stopPoints = new ArrayList<Point>();
		solver.startMoocPoints = new ArrayList<Point>();
		solver.stopMoocPoints = new ArrayList<Point>();
		solver.point2Type = new HashMap<Point, String>();

		solver.pickup2Delivery = new HashMap<Point, Point>();
		solver.delivery2Pickup = new HashMap<Point, Point>();

		solver.start2stopMoocPoint = new HashMap<Point, Point>();
		solver.stop2startMoocPoint = new HashMap<Point, Point>();

		solver.startPoint2Truck = new HashMap<Point, Truck>();
		solver.startPoint2Mooc = new HashMap<Point, Mooc>();

		solver.point2Group = new HashMap<Point, Integer>();
		solver.group2marked = new HashMap<Integer, Integer>();

		solver.group2EE = new HashMap<Integer, ExportEmptyRequests>();
		solver.group2EL = new HashMap<Integer, ExportLadenRequests>();
		solver.group2IE = new HashMap<Integer, ImportEmptyRequests>();
		solver.group2IL = new HashMap<Integer, ImportLadenRequests>();

		solver.point2moocWeight = new HashMap<Point, Integer>();
		solver.point2containerWeight = new HashMap<Point, Integer>();

		solver.route2DeliveryMooc = new HashMap<Integer, Point>();
	}

	private IdAndGroup buildTruckPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.nVehicle; i++) {
			Truck truck = solver.input.getTrucks()[i];
			groupId++;
			solver.group2marked.put(groupId, 0);
			for (int j = 0; j < truck.getReturnDepotCodes().length; j++) {
				id++;
				Point sp = new Point(id, truck.getDepotTruckLocationCode());

				solver.points.add(sp);
				solver.startPoints.add(sp);
				solver.point2Type.put(sp, TruckContainerSolver.START_TRUCK);
				solver.startPoint2Truck.put(sp, truck);

				solver.point2Group.put(sp, groupId);

				solver.earliestAllowedArrivalTime.put(sp,
						(int) (DateTimeUtils.dateTime2Int(truck.getStartWorkingTime())));
				solver.serviceDuration.put(sp, 0);
				solver.lastestAllowedArrivalTime.put(sp, solver.INF_TIME);

				id++;
				DepotTruck depotTruck = solver.mCode2DepotTruck.get(truck.getReturnDepotCodes()[j]);
				Point tp = new Point(id, depotTruck.getLocationCode());
				solver.points.add(tp);
				solver.stopPoints.add(tp);
				solver.point2Type.put(tp, TruckContainerSolver.END_TRUCK);

				solver.point2Group.put(tp, groupId);

				solver.earliestAllowedArrivalTime.put(tp,
						(int) (DateTimeUtils.dateTime2Int(solver.input.getTrucks()[i].getStartWorkingTime())));
				solver.serviceDuration.put(tp, 0);
				solver.lastestAllowedArrivalTime.put(tp, solver.INF_TIME);

				solver.point2moocWeight.put(sp, 0);
				solver.point2moocWeight.put(tp, 0);

				solver.point2containerWeight.put(sp, 0);
				solver.point2containerWeight.put(tp, 0);
			}
		}

		return new IdAndGroup(id, groupId);
	}

	private IdAndGroup buildMoocPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.input.getMoocs().length; i++) {
			Mooc mooc = solver.input.getMoocs()[i];
			groupId++;
			solver.group2marked.put(groupId, 0);
			for (int j = 0; j < mooc.getReturnDepotCodes().length; j++) {
				id++;
				Point sp = new Point(id, mooc.getDepotMoocLocationCode());
				solver.points.add(sp);
				solver.startMoocPoints.add(sp);
				solver.point2Type.put(sp, TruckContainerSolver.START_MOOC);
				solver.startPoint2Mooc.put(sp, mooc);

				solver.point2Group.put(sp, groupId);

				solver.earliestAllowedArrivalTime.put(sp, 0);
				solver.serviceDuration.put(sp, solver.input.getParams().getLinkMoocDuration());
				solver.lastestAllowedArrivalTime.put(sp, solver.INF_TIME);

				id++;
				String moocCode = mooc.getReturnDepotCodes()[j];
				DepotMooc depotMooc = solver.mCode2DepotMooc.get(moocCode);
				Point tp = new Point(id, depotMooc.getLocationCode());
				solver.points.add(tp);
				solver.stopMoocPoints.add(tp);
				solver.point2Type.put(tp, TruckContainerSolver.END_MOOC);
				solver.point2Group.put(tp, groupId);

				solver.earliestAllowedArrivalTime.put(tp, 0);
				solver.serviceDuration.put(tp, 0);
				solver.lastestAllowedArrivalTime.put(tp, solver.INF_TIME);

				solver.start2stopMoocPoint.put(sp, tp);
				solver.stop2startMoocPoint.put(tp, sp);

				solver.point2moocWeight.put(sp, 2);
				solver.point2moocWeight.put(tp, -2);

				solver.point2containerWeight.put(sp, 0);
				solver.point2containerWeight.put(tp, 0);
			}
		}

		return new IdAndGroup(id, groupId);
	}

	private IdAndGroup buildExportEmptyRequestPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.exEmptyRequests.length; i++) {
			groupId++;
			solver.group2marked.put(groupId, 0);
			solver.group2EE.put(groupId, solver.exEmptyRequests[i]);
			for (int j = 0; j < solver.input.getContainers().length; j++) {
				Container c = solver.input.getContainers()[j];
				if (c.isImportedContainer())
					continue;

				// kept for parity with original implementation
				DepotContainer depotCont = solver.mCode2DepotContainer.get(c.getDepotContainerCode());
				id++;
				Point pickup = new Point(id, c.getDepotContainerCode());
				id++;
				Warehouse wh = solver.mCode2Warehouse.get(solver.exEmptyRequests[i].getWareHouseCode());
				Point delivery = new Point(id, wh.getLocationCode());

				solver.points.add(pickup);
				solver.points.add(delivery);

				solver.pickupPoints.add(pickup);
				solver.deliveryPoints.add(delivery);

				solver.pickup2Delivery.put(pickup, delivery);
				solver.delivery2Pickup.put(delivery, pickup);

				solver.point2moocWeight.put(pickup, 0);
				if (solver.exEmptyRequests[i].getIsBreakRomooc())
					solver.point2moocWeight.put(delivery, -2);
				else
					solver.point2moocWeight.put(delivery, 0);

				solver.point2containerWeight.put(pickup, 1);
				solver.point2containerWeight.put(delivery, -1);
				if (solver.exEmptyRequests[i].getContainerType() != null
						&& solver.exEmptyRequests[i].getContainerType().equals("40")) {
					solver.point2containerWeight.put(pickup, 2);
					solver.point2containerWeight.put(delivery, -2);
				}

				solver.point2Type.put(pickup, TruckContainerSolver.START_CONT);
				solver.point2Type.put(delivery, TruckContainerSolver.WH_DELIVERY_EMPTYCONT);

				solver.point2Group.put(pickup, groupId);
				solver.point2Group.put(delivery, groupId);

				int early = 0;
				int latest = solver.INF_TIME;
				if (solver.exEmptyRequests[i].getEarlyDateTimePickupAtDepot() != null)
					early = (int) (DateTimeUtils.dateTime2Int(solver.exEmptyRequests[i].getEarlyDateTimePickupAtDepot()));
				if (solver.exEmptyRequests[i].getLateDateTimePickupAtDepot() != null)
					latest = (int) (DateTimeUtils.dateTime2Int(solver.exEmptyRequests[i].getLateDateTimePickupAtDepot()));
				solver.earliestAllowedArrivalTime.put(pickup, early);
				solver.serviceDuration.put(pickup, solver.input.getParams().getLinkEmptyContainerDuration());
				solver.lastestAllowedArrivalTime.put(pickup, latest);

				early = 0;
				latest = solver.INF_TIME;
				if (solver.exEmptyRequests[i].getEarlyDateTimeLoadAtWarehouse() != null)
					early = (int) (DateTimeUtils.dateTime2Int(solver.exEmptyRequests[i].getEarlyDateTimeLoadAtWarehouse()));
				if (solver.exEmptyRequests[i].getLateDateTimeLoadAtWarehouse() != null)
					latest = (int) (DateTimeUtils.dateTime2Int(solver.exEmptyRequests[i].getLateDateTimeLoadAtWarehouse()));
				solver.earliestAllowedArrivalTime.put(delivery, early);
				solver.serviceDuration.put(delivery, (int) (solver.input.getParams().getUnlinkEmptyContainerDuration()));
				solver.lastestAllowedArrivalTime.put(delivery, latest);
			}
		}

		return new IdAndGroup(id, groupId);
	}

	private IdAndGroup buildExportLadenRequestPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.exLadenRequests.length; i++) {
			groupId++;
			solver.group2marked.put(groupId, 0);
			solver.group2EL.put(groupId, solver.exLadenRequests[i]);
			id++;
			Warehouse wh = solver.mCode2Warehouse.get(solver.exLadenRequests[i].getWareHouseCode());
			Point pickup = new Point(id, wh.getLocationCode());
			id++;
			Port port = solver.mCode2Port.get(solver.exLadenRequests[i].getPortCode());
			Point delivery = new Point(id, port.getLocationCode());

			solver.points.add(pickup);
			solver.points.add(delivery);

			solver.pickupPoints.add(pickup);
			solver.deliveryPoints.add(delivery);

			solver.pickup2Delivery.put(pickup, delivery);
			solver.delivery2Pickup.put(delivery, pickup);

			solver.point2Type.put(pickup, TruckContainerSolver.WH_PICKUP_FULLCONT);
			solver.point2Type.put(delivery, TruckContainerSolver.PORT_DELIVERY_FULLCONT);

			solver.point2Group.put(pickup, groupId);
			solver.point2Group.put(delivery, groupId);

			solver.point2moocWeight.put(pickup, 0);
			if (solver.exLadenRequests[i].getIsBreakRomooc())
				solver.point2moocWeight.put(delivery, -2);
			else
				solver.point2moocWeight.put(delivery, 0);

			solver.point2containerWeight.put(pickup, 1);
			solver.point2containerWeight.put(delivery, -1);
			if (solver.exLadenRequests[i].getContainerType() != null
					&& solver.exLadenRequests[i].getContainerType().equals("40")) {
				solver.point2containerWeight.put(pickup, 2);
				solver.point2containerWeight.put(delivery, -2);
			}

			int early = 0;
			int latest = solver.INF_TIME;
			if (solver.exLadenRequests[i].getEarlyDateTimeAttachAtWarehouse() != null)
				early = (int) (DateTimeUtils.dateTime2Int(solver.exLadenRequests[i].getEarlyDateTimeAttachAtWarehouse()));

			solver.earliestAllowedArrivalTime.put(pickup, early);
			solver.serviceDuration.put(pickup, solver.input.getParams().getLinkLoadedContainerDuration());
			solver.lastestAllowedArrivalTime.put(pickup, latest);

			early = 0;
			latest = solver.INF_TIME;
			if (solver.exLadenRequests[i].getLateDateTimeUnloadAtPort() != null)
				latest = (int) (DateTimeUtils.dateTime2Int(solver.exLadenRequests[i].getLateDateTimeUnloadAtPort()));
			solver.earliestAllowedArrivalTime.put(delivery, early);
			solver.serviceDuration.put(delivery, (int) (solver.input.getParams().getUnlinkLoadedContainerDuration()));
			solver.lastestAllowedArrivalTime.put(delivery, latest);
		}

		return new IdAndGroup(id, groupId);
	}

	private IdAndGroup buildImportEmptyRequestPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.imEmptyRequests.length; i++) {
			groupId++;
			solver.group2marked.put(groupId, 0);
			solver.group2IE.put(groupId, solver.imEmptyRequests[i]);
			for (int j = 0; j < solver.input.getDepotContainers().length; j++) {
				DepotContainer depotCont = solver.input.getDepotContainers()[j];
				id++;
				Warehouse wh = solver.mCode2Warehouse.get(solver.imEmptyRequests[i].getWareHouseCode());
				Point pickup = new Point(id, wh.getLocationCode());
				id++;

				Point delivery = new Point(id, depotCont.getLocationCode());

				solver.points.add(pickup);
				solver.points.add(delivery);

				solver.pickupPoints.add(pickup);
				solver.deliveryPoints.add(delivery);

				solver.pickup2Delivery.put(pickup, delivery);
				solver.delivery2Pickup.put(delivery, pickup);

				solver.point2moocWeight.put(pickup, 0);
				solver.point2moocWeight.put(delivery, 0);

				solver.point2containerWeight.put(pickup, 1);
				solver.point2containerWeight.put(delivery, -1);
				if (solver.imEmptyRequests[i].getContainerType() != null
						&& solver.imEmptyRequests[i].getContainerType().equals("40")) {
					solver.point2containerWeight.put(pickup, 2);
					solver.point2containerWeight.put(delivery, -2);
				}

				solver.point2Type.put(pickup, TruckContainerSolver.WH_PICKUP_EMPTYCONT);
				solver.point2Type.put(delivery, TruckContainerSolver.END_CONT);

				solver.point2Group.put(pickup, groupId);
				solver.point2Group.put(delivery, groupId);

				int early = 0;
				int latest = solver.INF_TIME;
				if (solver.imEmptyRequests[i].getEarlyDateTimeAttachAtWarehouse() != null)
					early = (int) (DateTimeUtils.dateTime2Int(solver.imEmptyRequests[i].getEarlyDateTimeAttachAtWarehouse()));
				solver.earliestAllowedArrivalTime.put(pickup, early);
				solver.serviceDuration.put(pickup, solver.input.getParams().getLinkEmptyContainerDuration());
				solver.lastestAllowedArrivalTime.put(pickup, latest);

				early = 0;
				latest = solver.INF_TIME;

				if (solver.imEmptyRequests[i].getLateDateTimeReturnEmptyAtDepot() != null)
					latest = (int) (DateTimeUtils.dateTime2Int(solver.imEmptyRequests[i].getLateDateTimeReturnEmptyAtDepot()));
				solver.earliestAllowedArrivalTime.put(delivery, early);
				solver.serviceDuration.put(delivery, (int) (solver.input.getParams().getUnlinkEmptyContainerDuration()));
				solver.lastestAllowedArrivalTime.put(delivery, latest);
			}
		}

		return new IdAndGroup(id, groupId);
	}

	private IdAndGroup buildImportLadenRequestPoints(TruckContainerSolver solver, IdAndGroup cursor) {
		int id = cursor.id;
		int groupId = cursor.groupId;

		for (int i = 0; i < solver.imLadenRequests.length; i++) {
			groupId++;
			solver.group2marked.put(groupId, 0);
			solver.group2IL.put(groupId, solver.imLadenRequests[i]);
			id++;
			Port port = solver.mCode2Port.get(solver.imLadenRequests[i].getPortCode());
			Point pickup = new Point(id, port.getLocationCode());

			id++;
			Warehouse wh = solver.mCode2Warehouse.get(solver.imLadenRequests[i].getWareHouseCode());
			Point delivery = new Point(id, wh.getLocationCode());

			solver.points.add(pickup);
			solver.points.add(delivery);

			solver.pickupPoints.add(pickup);
			solver.deliveryPoints.add(delivery);

			solver.pickup2Delivery.put(pickup, delivery);
			solver.delivery2Pickup.put(delivery, pickup);

			solver.point2moocWeight.put(pickup, 0);
			if (solver.imLadenRequests[i].getIsBreakRomooc())
				solver.point2moocWeight.put(delivery, -2);
			else
				solver.point2moocWeight.put(delivery, 0);

			solver.point2containerWeight.put(pickup, 1);
			solver.point2containerWeight.put(delivery, -1);
			if (solver.imLadenRequests[i].getContainerType() != null
					&& solver.imLadenRequests[i].getContainerType().equals("40")) {
				solver.point2containerWeight.put(pickup, 2);
				solver.point2containerWeight.put(delivery, -2);
			}

			solver.point2Type.put(pickup, TruckContainerSolver.PORT_PICKUP_FULLCONT);
			solver.point2Type.put(delivery, TruckContainerSolver.WH_DELIVERY_FULLCONT);

			solver.point2Group.put(pickup, groupId);
			solver.point2Group.put(delivery, groupId);

			int early = 0;
			int latest = solver.INF_TIME;
			if (solver.imLadenRequests[i].getEarlyDateTimePickupAtPort() != null)
				early = (int) (DateTimeUtils.dateTime2Int(solver.imLadenRequests[i].getEarlyDateTimePickupAtPort()));
			if (solver.imLadenRequests[i].getLateDateTimePickupAtPort() != null)
				latest = (int) (DateTimeUtils.dateTime2Int(solver.imLadenRequests[i].getLateDateTimePickupAtPort()));
			solver.earliestAllowedArrivalTime.put(pickup, early);
			solver.serviceDuration.put(pickup, solver.input.getParams().getLinkLoadedContainerDuration());
			solver.lastestAllowedArrivalTime.put(pickup, latest);

			early = 0;
			latest = solver.INF_TIME;
			if (solver.imLadenRequests[i].getEarlyDateTimeUnloadAtWarehouse() != null)
				early = (int) (DateTimeUtils.dateTime2Int(solver.imLadenRequests[i].getEarlyDateTimeUnloadAtWarehouse()));
			if (solver.imLadenRequests[i].getLateDateTimeUnloadAtWarehouse() != null)
				latest = (int) (DateTimeUtils.dateTime2Int(solver.imLadenRequests[i].getLateDateTimeUnloadAtWarehouse()));

			solver.earliestAllowedArrivalTime.put(delivery, early);
			solver.serviceDuration.put(delivery, (int) (solver.input.getParams().getUnlinkLoadedContainerDuration()));
			solver.lastestAllowedArrivalTime.put(delivery, latest);
		}

		return new IdAndGroup(id, groupId);
	}

	private void buildWeightManagersAndMaxTravelTime(TruckContainerSolver solver) {
		solver.nwMooc = new NodeWeightsManager(solver.points);
		solver.nwContainer = new NodeWeightsManager(solver.points);
		solver.awm = new ArcWeightsManager(solver.points);
		double max_time = Double.MIN_VALUE;
		for (int i = 0; i < solver.points.size(); i++) {
			for (int j = 0; j < solver.points.size(); j++) {
				double tmp_cost = solver.getTravelTime(solver.points.get(i).getLocationCode(),
						solver.points.get(j).getLocationCode());
				solver.awm.setWeight(solver.points.get(i), solver.points.get(j), tmp_cost);
				max_time = tmp_cost > max_time ? tmp_cost : max_time;
			}
			solver.nwMooc.setWeight(solver.points.get(i), solver.point2moocWeight.get(solver.points.get(i)));
			solver.nwContainer.setWeight(solver.points.get(i), solver.point2containerWeight.get(solver.points.get(i)));
		}
		TruckContainerSolver.MAX_TRAVELTIME = max_time;
	}

	private static final class IdAndGroup {
		final int id;
		final int groupId;

		IdAndGroup(int id, int groupId) {
			this.id = id;
			this.groupId = groupId;
		}
	}
}

// ========== solver.TruckContainerModelBuilder ==========
class TruckContainerModelBuilder {
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

// ========== MAIN CLASS: solver.TruckContainerSolver ==========
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
	public ArrayList<Point> startMoocPoints;
	public ArrayList<Point> stopMoocPoints;
	public HashMap<Point, String> point2Type;
	
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
	
	public ArcWeightsManager awm;
	public VRManager mgr;
	public VarRoutesVR XR;
	public ConstraintSystemVR S;
	public IFunctionVR objective;
	CEarliestArrivalTimeVR ceat;
	LexMultiValues valueSolution;
	public EarliestArrivalTimeVR eat;
	CEarliestArrivalTimeVR cEarliest;
	ContainerCapacityConstraint capContCtr;
	MoocCapacityConstraint capMoocCtr;
	ContainerCarriedByTrailerConstraint contmoocCtr;
	
	NodeWeightsManager nwMooc;
	NodeWeightsManager nwContainer;
	public AccumulatedWeightNodesVR accMoocInvr;
	AccumulatedWeightNodesVR accContainerInvr;
	public HashMap<Point, IFunctionVR> accDisF;
	
	public HashMap<Point, Integer> nChosed;
	public HashMap<Point, Boolean> removeAllowed;
	
	public int nRemovalOperators = 8;
	public int nInsertionOperators = 8;
	
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
	public int timeLimit = 36000000;
	public int nIter = 30000;
	public int maxStable = 1000;
	
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
		this.initializationStrategy = new FPIUSInit();
		this.optimizationStrategy = new ALNS(this);
	}
	
	/**
	 * Reads data from file and automatically populates all solver properties.
	 * This is a convenience method that eliminates the need for separate loadData() call.
	 * 
	 * @param fileName Path to the input JSON file
	 */
	public void readData(String fileName) {
		dataMapper.readData(fileName, this);
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

	private static void writeStartInfo(String outputFileTxt) {
		try {
			FileOutputStream write = new FileOutputStream(outputFileTxt);
			PrintWriter fo = new PrintWriter(write);
			long startMs = System.currentTimeMillis();
			fo.println("Starting time = " + DateTimeUtils.unixTimeStamp2DateTime(startMs / 1000) + " (ms=" + startMs + ")"
					+ ", total reqs = " + nRequest + ", total truck = " + nVehicle);
			fo.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public int getNbUsedTrucks(){
		return new ALNS(this).getNbUsedTrucks();
	}
	
	public int getNbRejectedRequests(){
		return new ALNS(this).getNbRejectedRequests();
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
					String fileName = "random-" + nbReq[j] + "reqs-RealLoc-" + i;
					String dataFileName = fileName + ".txt";
					
					String outputALNSfileTxt = k +"-ALNS-" + fileName + ".txt";
					String outputALNSfileJson = k +"-ALNS-" + fileName + ".json";
					
					TruckContainerSolver solver = new TruckContainerSolver();
					solver.readData(dataFileName);
					solver.init();
					solver.stateModel();

					writeStartInfo(outputALNSfileTxt);

					solver.setInitializationStrategy(new FPIUSInit());
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

					solver.setOptimizationStrategy(new ALNS(solver));
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
