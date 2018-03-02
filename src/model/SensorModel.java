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
		ArrayList<Point> sf = getL1s(x, y);
		if (sf.contains(new Point(rX, rY))) //in surrounding field
			return PsurroundingFields;
		ArrayList<Point> ssf = getL2s(x, y);
		if (ssf.contains(new Point(rX, rY))) //in secoandry surrounding field
			return PsecondarySurroundingFields;
		if( rX==-1 || rY ==-1) { //reading nothing
			return 1 - PtrueLocation - sf.size() * PsurroundingFields - ssf.size() * PsecondarySurroundingFields;
		}
		return 0;
	}
	
	
	public ArrayList<Point> getL1s(int x, int y) {
		ArrayList<Point> l1 = new ArrayList<Point>();
		if (x == 0 && y == 0) { // vänster topphörn
			l1.add(new Point(0, 1));
			l1.add(new Point(1, 0));
			l1.add(new Point(1, 1));
			return l1;
		} else if (x == cols && y == 0) { // höger topphörn
			l1.add(new Point(cols - 1, 0));
			l1.add(new Point(cols - 1, 1));
			l1.add(new Point(cols, 1));
			return l1;
		} else if (x == 0 && y == rows) {// vänster bottenhörn
			l1.add(new Point(0, rows - 1));
			l1.add(new Point(1, rows - 1));
			l1.add(new Point(1, rows));
			return l1;
		} else if (x == cols && y == rows) { // höger bottenhörn
			l1.add(new Point(cols - 1, rows));
			l1.add(new Point(cols - 1, rows - 1));
			l1.add(new Point(cols, rows - 1));
			return l1;
		}

		if (x == 0) { // vänster vägg men inte hörn
			l1.add(new Point(x, y - 1));
			l1.add(new Point(x, y + 1));
			l1.add(new Point(x + 1, y - 1));
			l1.add(new Point(x + 1, y));
			l1.add(new Point(x + 1, y + 1));
			return l1;
		} else if (x == cols) { // höger vägg men inte hörn
			l1.add(new Point(cols, y - 1));
			l1.add(new Point(cols, y + 1));
			l1.add(new Point(cols - 1, y - 1));
			l1.add(new Point(cols - 1, y));
			l1.add(new Point(cols - 1, y + 1));
			return l1;
		} else if (y == 0) { // toppenvägg men inte hörn
			l1.add(new Point(x - 1, y));
			l1.add(new Point(x + 1, y));
			l1.add(new Point(x - 1, y + 1));
			l1.add(new Point(x, y + 1));
			l1.add(new Point(x + 1, y + 1));
			return l1;
		} else if (y == rows) { // bottenvägg men inte hörn
			l1.add(new Point(x - 1, rows));
			l1.add(new Point(x + 1, rows));
			l1.add(new Point(x - 1, rows - 1));
			l1.add(new Point(x, rows - 1));
			l1.add(new Point(x + 1, rows - 1));
			return l1;
		}

		// har vi kommit hit så är vi inte i kanterna eller hörnen av matrisen och vi
		// tar alla punkter runt omkring punkten.
		for (int i = 0; i < 3; i++) {
			l1.add(new Point(x - 1 + 1, y - 1));
			l1.add(new Point(x - 1 + i, y + 1));
		}
		l1.add(new Point(x - 1, y));
		l1.add(new Point(x + 1, y));
		return l1;
	}

	public ArrayList<Point> getL2s(int x, int y) {
		ArrayList<Point> l2 = new ArrayList<Point>();
		Point p;
		for (int i = 0; i < 5; i++) { //rader
			p = new Point(x - 2 + i, y - 2); // raden ovanför punkten
			if (p.getX() >= 0 && p.getX() < cols && y >= 0) {
				l2.add(p);
			}
			p = new Point(x-2+i, y+2); //raden nedanför punkten
			if(p.getX() >= 0 && p.getX() < cols && y <rows) {
				l2.add(p);
			}
		}
		for (int i = 0; i <3;i++ ) { //kolumner
			p = new Point(x-2, y-1+i); //vänstra kolumnen
			if(p.getX() >= 0 && p.getY() >= 0 && p.getY() < rows) {
				l2.add(p);
			}
			p = new Point(x+2, y-1+i); //högra kolumenen
			if(p.getX() < cols && p.getY() >= 0 && p.getY() < rows) {
				l2.add(p);
			}
		}
		return l2;
	}
	
	public double getOrXY(int rX, int rY, int x, int y, int h) {
		int reading = rX + rY*rows;
		if (rX == -1 && rY == -1)
			reading = rows * cols;
		int state = h + x * head + y * head * rows;
		return o.get(reading, state);
	}
}
