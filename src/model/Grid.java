package model;

public class Grid {
	private double[][] grid;
	private int nbrRows;
	private int nbrCols;
	public Grid(int rows, int columns) {
		grid = new double[rows][columns];
		nbrRows = rows;
		nbrCols = columns;
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				grid[row][col] = 0.0;
			}
		}	
	}
	
	public double getValue(int x, int y) {
		return grid[x][y];
	}
	
	public void setValue(int x, int y, double value) {
		grid[x][y] = value;
	}
	
	public String toString() {
		String s = "";
		for (int row = 0; row < nbrRows;row++) {
			for (int col = 0; col < nbrCols; col++) {
				s += grid[row][col];
			} 
			s += "\n";
		}
		return s;
	}
}
