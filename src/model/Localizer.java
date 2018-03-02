package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import Jama.Matrix;
import control.EstimatorInterface;

/*
 *  h running around the compass from 0 to 3 as NORTH-EAST-SOUTH-WEST. 
 */
public class Localizer implements EstimatorInterface {
	private final static int NORTH = 0;
	private final static int EAST = 1;
	private final static int SOUTH = 2;
	private final static int WEST = 3;

	private int rows, cols, head;
	//private Grid grid;
	private Robot robot;
	private SensorModel sm;

	private TransitionModel TModel;
	private Matrix Tmatrix;
	private Random random;
	private ForwardPredictioner fp;

	public Localizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		//grid = new Grid(rows, cols); // grid to keep track on where the robot's
										// true position is. Maybe not necessary

		// init T-matrix
		TModel = new TransitionModel(rows, cols);
		Tmatrix = TModel.initMatrix();

		// forward predictioner, also init f with 1/S for every entry in f
		fp = new ForwardPredictioner(cols, rows);
		fp.initF();
		// init robot position randomly
		random = new Random();
		int xStart = random.nextInt(cols);
		int yStart = random.nextInt(rows);
		int dir = new Random().nextInt(4);
		robot = new Robot(xStart, yStart, dir);
		//grid.setValue(xStart, yStart, 1);
		sm = new SensorModel(rows, cols, head);
	}

	@Override
	public int getNumRows() {
		return rows;
	}

	@Override
	public int getNumCols() {
		return cols;
	}

	@Override
	public int getNumHead() {
		return head;
	}

	@Override
	public void update() {
		// move robot
		boolean facingWall = false;
		int[] wallAt = { 0, 0, 0, 0 };
		if (robot.getX() == 0 && robot.getDirection() == WEST || robot.getY() == 0 && robot.getDirection() == NORTH
				|| robot.getX() == cols && robot.getDirection() == EAST
				|| robot.getY() == rows && robot.getDirection() == SOUTH) {
			facingWall = true;
		}
		if(robot.getX() == 0 && robot.getY() == 0) { //vänster topphörn
			wallAt[0]= 1;
			wallAt[3]=1;
		} else if (robot.getX() == cols && robot.getY() == 0) {//höger topphörn
			wallAt[0] = 1;
			wallAt[1] = 1;
		} else if(robot.getX() == 0 && robot.getY() == rows) {//vänster bottenhörn
			wallAt[2] = 1;
			wallAt[3] = 1;
		} else if(robot.getX() == cols && robot.getY() == rows) { //höger bottenhörn
			wallAt[1] = 1;
			wallAt[2] = 1;
		}
		if(robot.getX() == 0) {//vänster vägg
			wallAt[3] = 1;
		} else if(robot.getX() == cols) {//höger vägg
			wallAt[1] = 1;
		} else if (robot.getY() == 0) { //toppenvägg
			wallAt[0] = 1;
		} else if(robot.getY() == rows) { //bottenvägg
			wallAt[2] = 1;
		}
		//updates true position
		System.out.println("true pos be4: " + getCurrentTruePosition()[0] + " , " + getCurrentTruePosition()[1]);
		robot.moveOneStep(facingWall, wallAt);		
		//get current reading from sensor
		int[] sensorReading = getCurrentReading();
		System.out.println("true pos after: " + getCurrentTruePosition()[0] + " , " + getCurrentTruePosition()[1]);
		System.out.println("sensor reading: " + sensorReading[0] + " , " + sensorReading[1]);
		Point reading = new Point(sensorReading[0], sensorReading[1]);		
		//update f
		fp.fUpdate(sm.getMatix(reading), Tmatrix);
	}

	@Override
	public int[] getCurrentTruePosition() {
		return robot.getposition();
	}

	@Override
	public int[] getCurrentReading() {
		int[] truePosition = getCurrentTruePosition();
		double sensorProb = random.nextDouble();
		ArrayList<Point> sf = sm.getSF(truePosition[0], truePosition[1]);
		ArrayList<Point> ssf = sm.getSSF(truePosition[0], truePosition[1]);
		if (sensorProb <= 0.1) {
			return truePosition;
		} else if (sensorProb <= 0.1 + 0.05 * sf.size()) {
			int rand = random.nextInt(sf.size());
			int x = (int) sf.get(rand).getX();
			int y = (int) sf.get(rand).getY();
			return new int[] { x, y };
		} else if (sensorProb <= 0.1 + 0.05 * sf.size() + 0.025 * ssf.size()) {
			int rand = random.nextInt(ssf.size());
			int x = (int) ssf.get(rand).getX();
			int y = (int) ssf.get(rand).getY();
			return new int[] { x, y };
		}
		return null;
	}

	@Override
	public double getCurrentProb(int x, int y) {
		return fp.probForPosition(x, y);
	}

	@Override
	public double getOrXY(int rX, int rY, int x, int y, int h) {
		return sm.getOrXY(rX, rY, x, y, h);
	}

	@Override
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
		int stateFrom = TModel.getStateFromPosition(x, y, h);
		int stateTo = TModel.getStateFromPosition(nX, nY, nH);
		return Tmatrix.get(stateFrom, stateTo);
	}

}
