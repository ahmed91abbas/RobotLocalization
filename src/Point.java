
public class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (((Point) obj).getX() == this.x && ((Point) obj).getY() == this.y) {
			return true;
		}
		return false;
	}
}
