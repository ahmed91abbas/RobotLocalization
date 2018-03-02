package model;

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
	private Grid grid;
	private Robot robot;
	private SensorModel sm;
	private Matrix o;

	public Localizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		grid = new Grid(rows, cols); // grid to keep track on where the robot's
										// true position is.

		// init robot position randomly
		Random random = new Random();
		int xStart = random.nextInt(cols);
		int yStart = random.nextInt(rows);
		int dir = new Random().nextInt(4);
		robot = new Robot(xStart, yStart, dir);
		grid.setValue(xStart, yStart, 1);
		sm = new SensorModel(rows, cols, head);
		o = sm.getMatix();
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
		// TODO Auto-generated method stub
	}

	@Override
	public int[] getCurrentTruePosition() {
		return robot.getposition();
	}

	@Override
	public int[] getCurrentReading() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCurrentProb(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getOrXY(int rX, int rY, int x, int y, int h) {
		return sm.getOrXY(rX, rY, x, y, h);
	}

	@Override
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
		// TODO Auto-generated method stub
		return 0;
	}

}
