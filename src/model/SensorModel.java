package model;

import java.awt.Point;
import java.util.ArrayList;

import Jama.Matrix;

public class SensorModel {
	
	private Matrix o;
	private int rows, cols, head, nbr_states, nbr_readings;
	private double PtrueLocation = 0.1;
	private double PsurroundingFields = 0.05;
	private double PsecondarySurroundingFields = 0.025;

	public SensorModel(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		nbr_states = rows * cols * 4;
		nbr_readings = rows * cols + 1;
		o = init_o();
	}
	
	public Matrix getMatix() {
		return o;
	}

	private Matrix init_o() {
		Matrix o = new Matrix(nbr_readings, nbr_states);
		for (int reading = 0; reading < nbr_readings; reading++) {
			for (int state = 0; state < nbr_states; state++) {
				int rX = reading % rows;
				int rY = reading / rows;
				if (reading == rows * cols) { //filled a matrix, add nothing reading
					rX = -1;
					rY = -1;
				}
				int x = state / head;
				int y = state / (head*rows);
				int h = state % head;
				double value = calcOrXY(rX, rY, x, y, h);
				o.set(reading, state, value);
			}
		}
		return o;
	}
	
	private double calcOrXY(int rX, int rY, int x, int y, int h){
		if (rX == x && rY == y) //sensor reading equal real location
			return PtrueLocation;
		ArrayList<Point> sf = getSF(x, y);
		if (sf.contains(new Point(rX, rY))) //in surrounding field
			return PsurroundingFields;
		ArrayList<Point> ssf = getSSF(x, y);
		if (ssf.contains(new Point(rX, rY))) //in secoandry surrounding field
			return PsecondarySurroundingFields;
		if (rX == -1 || rY == -1) { //reading nothing
			return 1 - PtrueLocation - sf.size() * PsurroundingFields - ssf.size() * PsecondarySurroundingFields;
		}
		return 0;
	}
	
	//Surrounding fields
	public ArrayList<Point> getSF(int x, int y) {
		ArrayList<Point> sf = new ArrayList<Point>();
		int xx,yy;
		//add up
		xx = x - 1;
		yy = y;
		if (xx >= 0)
			sf.add(new Point(xx,yy));
		//add up-left
		xx = x - 1;
		yy = y - 1;
		if (xx >= 0 && yy >= 0)
			sf.add(new Point(xx,yy));
		//add up-right
		xx = x - 1;
		yy = y + 1;
		if (xx >= 0 && yy < cols)
			sf.add(new Point(xx,yy));
		//add left
		xx = x;
		yy = y - 1;
		if (yy >= 0)
			sf.add(new Point(xx,yy));
		// add down
		xx = x + 1;
		yy = y;
		if (xx < rows)
			sf.add(new Point(xx,yy));
		//add down-left
		xx = x + 1;
		yy = y - 1;
		if (xx < rows && yy >= 0)
			sf.add(new Point(xx,yy));
		//add down-right
		xx = x + 1;
		yy = y + 1;
		if (xx < rows && yy < cols)
			sf.add(new Point(xx,yy));
		//add right
		xx = x;
		yy = y + 1;
		if (yy < cols)
			sf.add(new Point(xx,yy));
		return sf;
	}
	
//	Secondary surrounding fields
	public ArrayList<Point> getSSF(int x, int y) {
		ArrayList<Point> ssf = new ArrayList<Point>();
		int xx,yy;
		//add up
		xx = x - 2;
		yy = y;
		if (xx >= 0)
			ssf.add(new Point(xx,yy));
		//add up-left
		xx = x - 2;
		yy = y - 2;
		if (xx >= 0 && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add up-second-left
		xx = x - 2;
		yy = y - 1;
		if (xx >= 0 && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add up-right
		xx = x - 2;
		yy = y + 2;
		if (xx >= 0 && yy < cols)
			ssf.add(new Point(xx,yy));
		//add up-second-right
		xx = x - 2;
		yy = y + 1;
		if (xx >= 0 && yy < cols)
			ssf.add(new Point(xx,yy));
		//add left
		xx = x;
		yy = y - 2;
		if (yy >= 0)
			ssf.add(new Point(xx,yy));
		//add left up
		xx = x - 1;
		yy = y - 2;
		if (xx >= 0 && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add left down
		xx = x + 1;
		yy = y - 2;
		if (xx < rows && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add down
		xx = x + 2;
		yy = y;
		if (xx < rows)
			ssf.add(new Point(xx,yy));
		//add down-left
		xx = x + 2;
		yy = y - 2;
		if (xx < rows && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add down-second-left
		xx = x + 2;
		yy = y - 1;
		if (xx < rows && yy >= 0)
			ssf.add(new Point(xx,yy));
		//add down-right
		xx = x + 2;
		yy = y + 2;
		if (xx < rows && yy < cols)
			ssf.add(new Point(xx,yy));
		//add down-second-right
		xx = x + 2;
		yy = y + 1;
		if (xx < rows && yy < cols)
			ssf.add(new Point(xx,yy));
		//add right
		xx = x;
		yy = y + 2;
		if (yy < cols)
			ssf.add(new Point(xx,yy));
		//add right up
		xx = x + 1;
		yy = y + 2;
		if (xx < rows && yy < cols)
			ssf.add(new Point(xx,yy));
		//add right down
		xx = x - 1;
		yy = y + 2;
		if (xx >= 0 && yy < cols)
			ssf.add(new Point(xx,yy));
		
		
		return ssf;
	}
	
	public double getOrXY(int rX, int rY, int x, int y, int h) {
		int reading = rX + rY*rows;
		if (rX == -1 && rY == -1)
			reading = rows * cols;
		int state = h + x * head + y * head * rows;
		return o.get(reading, state);
	}
}
