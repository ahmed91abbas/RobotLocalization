package model;

import java.util.ArrayList;
import java.util.Random;

public class Robot {
	private final static int NORTH = 0;
	private final static int EAST = 1;
	private final static int SOUTH = 2;
	private final static int WEST = 3;
	private int x;
	private int y;
	private int direction;

	public Robot(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		direction = dir;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int dir) {
		direction = dir;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] getposition() {
		int[] pos = new int[2];
		System.out.println(x + "   robot  " + y);
		pos[0] = x;
		pos[1] = y;
		return pos;
	}

	public void moveOneStep(boolean facingWall, int[] wallAt) {
		if (!facingWall) {
			double prob = Math.random();

			if (prob > 0.7) { // change direction
				Random random = new Random();
				int newDir = random.nextInt(4);
				while (newDir == direction) { // inte samma direction som innan
					newDir = random.nextInt(4);
				}
				direction = newDir;
			}
			switch (direction) {
			case NORTH:
				if (y == 0) {
					y = 3;
				} else {
					y = y - 1;
				}
				setY(y);
				break;
			case EAST:
				setX((x + 1) % 4);
				break;
			case SOUTH:
				setY((y + 1) % 4);
				break;
			case WEST:
				if (x == 0) {
					x = 3;
				} else {
					x = x - 1;
				}
				setX(x);
				break;
			}
		} else if (facingWall) {
			ArrayList<Integer> possibleNewDirections = new ArrayList<Integer>();
			for (int i = 0; i < 4; i++) {
				if (wallAt[i] == 0) { // ingen wall i riktning i
					possibleNewDirections.add(i);
				}
			}
			Random random = new Random();
			int index = random.nextInt(possibleNewDirections.size());
			direction = possibleNewDirections.get(index);

			switch (direction) {
			case NORTH:
				if (y == 0) {
					y = 3;
				} else {
					y = y - 1;
				}
				setY(y);
				break;
			case EAST:
				setX((x + 1) % 4);
				break;
			case SOUTH:
				setY((y + 1) % 4);
				break;
			case WEST:
				if (x == 0) {
					x = 3;
				} else {
					x = x - 1;
				}
				setX(x);
				break;
			}
		}
	}
}
