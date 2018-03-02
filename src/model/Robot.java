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
				setY(y - 1);
				break;
			case EAST:
				setX(x + 1);
				break;
			case SOUTH:
				setY(y + 1);
				break;
			case WEST:
				setX(x - 1);
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
			int index = random.nextInt(possibleNewDirections.size() + 1);
			direction = possibleNewDirections.get(index);

			switch (direction) {
			case NORTH:
				setY(y - 1);
				break;
			case EAST:
				setX(x + 1);
				break;
			case SOUTH:
				setY(y + 1);
				break;
			case WEST:
				setX(x - 1);
				break;
			}
		}
	}
}
