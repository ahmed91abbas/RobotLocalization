package model;

import Jama.Matrix;

public class ForwardPredictioner {

	private int cols, rows, S;
	private Matrix f;

	public ForwardPredictioner(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		S = rows * cols * 4;
	}

	public void initF() {
		f = new Matrix(S, 1);

		for (int i = 0; i < S; i++) {
			f.set(i, 0, 1 / S); // all states are equally possible in beginning
		}
		
	}

	public void fUpdate(Matrix O, Matrix T) {
		f = O.times(T.transpose()).times(f);
		f = normalize(f);
	}

	public double probForPosition(int x, int y) {
		double prob = 0.0;
		System.out.println(f.getRowDimension());
		for(int i = 0; i < 4; i++) {
			prob += f.get(getStateFromPosition(x,y ,i), 0); //snurra runt i rutan ty vi bryr oss inte om direction, addera alla sannolikheter
		}
		return prob;
	}
	public Matrix normalize(Matrix matrix) {
		double sum = 0.0;
			for (int j = 0; j < matrix.getRowDimension(); j++) {
				sum += matrix.get(j, 0);
			}
		
			for (int j = 0; j < matrix.getRowDimension(); j++) {
				matrix.set(j, 0, matrix.get(j, 0)/sum);
			
		}
		return matrix;
	}
	public int getStateFromPosition(int x, int y, int direction) { //varje x ökar 4. För att komma en rad ner multiplicera med y*cols*4. 
		return  4*x + direction + 4*(y*cols);
	}

}
