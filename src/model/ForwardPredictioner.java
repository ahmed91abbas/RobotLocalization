package model;

import Jama.Matrix;

public class ForwardPredictioner {

	private int cols, rows, S;
	private Matrix f;

	public ForwardPredictioner(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		S = rows * cols * 4;
		f = new Matrix(S, 1);
	}

	public void initF() {
		for (int i = 0; i < S; i++) {
			System.out.println(S);
			f.set(i, 0, 1.0/S); // all states are equally possible in beginning
		}
		for(int i = 0; i< 64; i++) {
			System.out.println(f.get(i, 0));
		}
		
	}

	public void fUpdate(Matrix O, Matrix T) {
		if(O.getColumnDimension() != T.getRowDimension()) {
			System.out.println("THIS SOME WRING on so many levels");
			System.exit(1);
		}
		Matrix tTransposed = T.transpose();
		//System.out.println("SIZE OF F " + f.getRowDimension());
		f = O.times(tTransposed).times(f);
		//System.out.println("AFTER MUL" + f.getRowDimension());

		f = normalize(f);
	}

	public double probForPosition(int x, int y) {
		double prob = 0.0;
		double sum = 0;
		
		System.out.println(f.getRowDimension());
		System.out.println(f.getColumnDimension());

//		for(int i = 0; i < f.getRowDimension(); i++) {
//			sum += f.get(i,0);
//		}
	//	System.out.println("SUM OF F: " + sum);
		for(int i = 0; i < 4; i++) {
			prob += f.get(getStateFromPosition(x,y,i), 0); //snurra runt i rutan ty vi bryr oss inte om direction, addera alla sannolikheter
		}
		//System.out.println("PROB FOR" + x + "," +y + " is: " + prob);
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
