import java.util.ArrayList;

public class SensorReadings {
	private int cols;
	private int rows;

	public SensorReadings(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}

	public Point getReading(int x, int y) { //TODO 
		double prob = Math.random();

		if (prob < 0.1) {// true location
			return new Point(x, y);
		}
		return new Point(0,0); // --> WARNIGN TEMP
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

}
