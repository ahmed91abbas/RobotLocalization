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
	// private Grid grid;
	private Robot robot;
	private SensorModel sm;

	private TransitionModel TModel;
	private Matrix Tmatrix;
	private Random random;
	private ForwardPredictioner fp;
	private ArrayList<Integer> manhattanDistance;

	public Localizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;

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
		// grid.setValue(xStart, yStart, 1);
		sm = new SensorModel(rows, cols, head);
		manhattanDistance = new ArrayList<Integer>();
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
		robot.moveOneStep(facingWall, wallAt);		
		//get current reading from sensor
		int[] sensorReading = getCurrentReading();
		Point reading = new Point(sensorReading[0], sensorReading[1]);		
		//update f
		fp.fUpdate(sm.getMatix(reading), Tmatrix);
		
		manhattanDistance.add(manHattan());
		System.out.println("The average Manhattan distance so far = " + calculateAverage(manhattanDistance));
	}

	private double calculateAverage(ArrayList<Integer> distances) {
		double sum = 0.0;
		if (!distances.isEmpty()) {
			for (Integer distance : distances) {
				sum += distance;
			}
			return sum / distances.size();
		}
		return sum;
	}

	public int manHattan() {
		int[] truePos = getCurrentTruePosition();
		int tX = truePos[0];
		int tY = truePos[1];
		int[] sensorPos = getCurrentReading();
		int sX = sensorPos[0];
		int sY = sensorPos[1];
		int yDiff = Math.abs(tY - sY);
		int xDiff = Math.abs(tX - sX);
		int totalDist = yDiff + xDiff;
		return totalDist;
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
		return new int[] { -1, -1 };
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
