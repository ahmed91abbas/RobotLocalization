package model;

import Jama.Matrix;

public class TransitionModel {
	private int rows;
	private int cols;
	
	private int S;
	private double [][] array;
	Matrix matrix; 
	public TransitionModel(int rows, int cols) {
		S  = rows*cols*4;
		this.rows = rows-1;
		this.cols = cols-1;
		array = new double[S][S];
	} 
	
	
	public int getStateFromPosition(int x, int y, int direction) { //varje x ökar 4. För att komma en rad ner multiplicera med y*cols*4. 
		return  4*x + direction + 4*(y*cols);
	}
	public int[] getPositionFromState(int state) {
		int y = state / 4*cols; //ex: state= 37   . ... . 37/4*8 == 37 / 32 = 1. Dv rad 1. 77/32=2.4 = 2, dvs rad två!!
		state -= y*4*cols; //rempve y coord from state
		int x = state / 4;
		state -= state / 4;
		int direction = state;
		int[] position = {x,y,direction};
		return position;
	}
	
	// init the matrix, very jobbigt... puh
	public Matrix initMatrix() {
		
		//topp vänster hörn N
		array[getStateFromPosition(0,0,0)][getStateFromPosition(1,0,2)] = 0.5;  //S
		array[getStateFromPosition(0,0,0)][getStateFromPosition(0,1,1)] = 0.5; //E 	
		//topp vänster hörn E
		array[getStateFromPosition(0,0,1)][getStateFromPosition(0,1,1)] = 0.7;  
		array[getStateFromPosition(0,0,1)][getStateFromPosition(1,0,2)] = 0.3; 	
		
		//topp vänster hörn S
		array[getStateFromPosition(0,0,2)][getStateFromPosition(1,0,2)] = 0.7;   //S
		array[getStateFromPosition(0,0,2)][getStateFromPosition(0,1,1)] = 0.3; 	//E
		
		//topp vänster hörn E
		array[getStateFromPosition(0,0,3)][getStateFromPosition(1,0,2)] = 0.5;  //S
		array[getStateFromPosition(0,0,3)][getStateFromPosition(0,1,1)] = 0.5; 	//E
		
		//topp höger hörn N
		array[getStateFromPosition(0, cols, 0)][getStateFromPosition(0,cols-1, 3)] = 0.5; //W
		array[getStateFromPosition(0, cols, 0)][getStateFromPosition(1,cols, 2)] = 0.5; // S
		
		//topp höger hörn E
		array[getStateFromPosition(0, cols, 1)][getStateFromPosition(0,cols-1,3)] = 0.5; //W
		array[getStateFromPosition(0, cols, 1)][getStateFromPosition(1,cols,2)] = 0.5; //S
		//topp höger hörn S
		array[getStateFromPosition(0, cols, 2)][getStateFromPosition(1,cols,2)] = 0.7; //S
		array[getStateFromPosition(0, cols, 2)][getStateFromPosition(0,cols-1,3)] = 0.3; //W
		//topp höger hörn W
		array[getStateFromPosition(0, cols, 3)][getStateFromPosition(0,cols-1,3)] = 0.7;  //W
		array[getStateFromPosition(0, cols, 3)][getStateFromPosition(1,cols,2)] = 0.3; //S
		
		//vänster bottenhörn N
		array[getStateFromPosition(rows, 0, 0)][getStateFromPosition(rows-1,0,0)] = 0.7; //N
		array[getStateFromPosition(rows, 0, 0)][getStateFromPosition(rows,1,1)] = 0.3; //E
		//vänster bottenhörn E
		array[getStateFromPosition(rows, 0, 1)][getStateFromPosition(rows,1,1)] = 0.7; //E
		array[getStateFromPosition(rows, 0, 1)][getStateFromPosition(rows-1,0,0)] = 0.3; //N 
		
		//vänster bottenhörn S
		array[getStateFromPosition(rows, 0, 2)][getStateFromPosition(rows-1, 0,0)] = 0.5;  //N
		array[getStateFromPosition(rows, 0, 2)][getStateFromPosition(rows, 1,1)] = 0.5; //E
		//vänster bottenhörn W
		array[getStateFromPosition(rows, 0, 3)][getStateFromPosition(rows-1,0,0)] = 0.5; //N 
		array[getStateFromPosition(rows, 0, 3)][getStateFromPosition(rows,1,1)] = 0.5; //E
	
		
		//Höger bottenhörn N
		array[getStateFromPosition(rows, cols, 0)][getStateFromPosition(rows-1, cols,0)] = 0.7; //N
		array[getStateFromPosition(rows, cols, 0)][getStateFromPosition(rows, cols-1,3)] = 0.3; //W
		
		//Höger bottenhörn E
		array[getStateFromPosition(rows, cols, 1)][getStateFromPosition(rows-1,cols,0)] = 0.5; //N
		array[getStateFromPosition(rows, cols, 1)][getStateFromPosition(rows,cols-1,3)] = 0.5; //W
		
		//Höger bottenhörn S
		array[getStateFromPosition(rows, cols, 2)][getStateFromPosition(rows-1,cols,0)] = 0.5;  //N
		array[getStateFromPosition(rows, cols, 2)][getStateFromPosition(rows,cols-1,3)] = 0.5; //W
		
		//Höger bottenhörn W
		array[getStateFromPosition(rows, cols, 3)][getStateFromPosition(rows,cols-1, 3)] = 0.7; //W
		array[getStateFromPosition(rows, cols, 3)][getStateFromPosition(rows-1,cols,0)] = 0.3; //N
		
		
		//the northern wall
		for (int i = 1; i < cols -1; i++) {
			//N
			array[getStateFromPosition(0,i,0)][getStateFromPosition(0,i-1,3)] = 1.0/3; //W
			array[getStateFromPosition(0,i,0)][getStateFromPosition(0,i+1,1)] = 1.0/3; //E
			array[getStateFromPosition(0,i,0)][getStateFromPosition(1,i,2)] = 1.0/3; //S

			//E
			array[getStateFromPosition(0,i,1)][getStateFromPosition(0,i+1,1)] = 0.7; // E
			array[getStateFromPosition(0,i,1)][getStateFromPosition(0,i-1,3)] = 0.15; // W
			array[getStateFromPosition(0,i,1)][getStateFromPosition(1,i,2)] = 0.15;	//S
			
			//S
			array[getStateFromPosition(0,i,2)][getStateFromPosition(1,i,2)] = 0.7; //S
			array[getStateFromPosition(0,i,2)][getStateFromPosition(0,i+1,1)] = 0.15; //E
			array[getStateFromPosition(0,i,2)][getStateFromPosition(0,i-1,3)] = 0.15;//W
			
			//W
			array[getStateFromPosition(0,i,3)][getStateFromPosition(0,i-1,3)] = 0.7; //W
			array[getStateFromPosition(0,i,3)][getStateFromPosition(0,i+1,1)] = 0.15; //E
			array[getStateFromPosition(0,i,3)][getStateFromPosition(1,i,2)] = 0.15; //S
		}
		
		//the wall of the south
		for ( int i = 1; i < cols - 1; i++) {
			//N
			array[getStateFromPosition(rows,i,0)][getStateFromPosition(rows-1,i,0)] = 0.7; //N
			array[getStateFromPosition(rows,i,0)][getStateFromPosition(rows,i-1,3)] = 0.15; //W
			array[getStateFromPosition(rows,i,0)][getStateFromPosition(rows,i+1,1)] = 0.15; //E
			
			
			//E
			array[getStateFromPosition(rows,i,1)][getStateFromPosition(rows,i+1,1)] = 0.7; //E
			array[getStateFromPosition(rows,i,1)][getStateFromPosition(rows-1,i,0)] = 0.15; //N
			array[getStateFromPosition(rows,i,1)][getStateFromPosition(rows,i-1,3)] = 0.15; //W
			
			//S
			array[getStateFromPosition(rows,i,2)][getStateFromPosition(rows-1,i,0)] = 1.0/3; //N
			array[getStateFromPosition(rows,i,2)][getStateFromPosition(rows,i+1,1)] = 1.0/3; //E
			array[getStateFromPosition(rows,i,2)][getStateFromPosition(rows,i-1,3)] = 1.0/3; //W
			
			//W
			array[getStateFromPosition(rows,i,3)][getStateFromPosition(rows,i-1,3)] = 0.7; //W
			array[getStateFromPosition(rows,i,3)][getStateFromPosition(rows,i+1,1)] = 0.15; //E
			array[getStateFromPosition(rows,i,3)][getStateFromPosition(rows-1,i,0)] = 0.15; //N
		}
		
		//The wall of the west, do not breach
		for ( int i = 1; i < rows - 1; i++) {
			//N
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i-1,0,0)] = 0.7; //N
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i,1,1)] = 0.15; //E
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i+1,0,2)] = 0.15; //S
			
			//E
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i,1,1)] = 0.7; //E
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i-1,0,0)] = 0.15; //N
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i+1,0,2)] = 0.15; //S
			
			//S
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i+1,0,2)] = 0.7; //S
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i-1,0,0)] = 0.7; //N
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i,1,1)] = 0.7; //E
			
			//W
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i-1,0,0)] = 1.0/3; //N
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i,1,1)] = 1.0/3; //E
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i+1,0,2)] = 1.0/3; //S
		}
		
		//the eastern  wall
		for (int i = 1; i < rows -1 ; i++) {
			//N
			array[getStateFromPosition(i,cols,0)][getStateFromPosition(i-1,cols,0)] = 0.7; //N
			array[getStateFromPosition(i,cols,0)][getStateFromPosition(i,cols-1,3)] = 0.15; //W
			array[getStateFromPosition(i,cols,0)][getStateFromPosition(i+1,cols,2)] = 0.15; //S
			//E
			array[getStateFromPosition(i,cols,1)][getStateFromPosition(i-1,cols,0)] = 1.0/3; //N
			array[getStateFromPosition(i,cols,1)][getStateFromPosition(i+1,cols,2)] = 1.0/3; //S
			array[getStateFromPosition(i,cols,1)][getStateFromPosition(i,cols-1,3)] = 1.0/3; //W

			//S
			array[getStateFromPosition(i,cols,2)][getStateFromPosition(i-1,cols,0)] = 0.15; //N
			array[getStateFromPosition(i,cols,2)][getStateFromPosition(i+1,cols,2)] = 0.7; //S
			array[getStateFromPosition(i,cols,2)][getStateFromPosition(i,cols-1,3)] = 0.15; //W

			//W
			array[getStateFromPosition(i,cols,3)][getStateFromPosition(i,cols-1,3)] = 0.7; //W
			array[getStateFromPosition(i,cols,3)][getStateFromPosition(i-1,cols,0)] = 0.15; //N
			array[getStateFromPosition(i,cols,3)][getStateFromPosition(i+1,cols,2)] = 0.15; //S
		}
		//"Middle of the matrix, rest
		
		for( int i = 1; i < rows -1; i++) {
			for( int j = 1; j < cols -1; j++) {
				//N
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i-1,j,0)] = 0.7; //N
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i,j+1,1)] = 0.1; //E
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i+1,j,2)] = 0.1; //S
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i,j-1,3)] = 0.1; //W
				
				
				//E
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i,j+1,1)] = 0.7; //E
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i-1,j,0)] = 0.1; //N
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i+1,j,2)] = 0.1; //S
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i,j-1,3)] = 0.1; //W
				
				//S
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i+1,j,2)] = 0.7; //S
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i-1,j,0)] = 0.1; //N
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i,j+1,1)] = 0.1; //E
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i,j-1,3)] = 0.1; //W
				
				//W
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i,j-1,3)] = 0.7; //W
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i-1,j,0)] = 0.1; //N
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i,j+1,1)] = 0.1; //E
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i+1,j,2)] = 0.1; //S

			}
		}
		matrix = new Matrix(array);
		return matrix;
	}
	
	
	
	
	
	
}
