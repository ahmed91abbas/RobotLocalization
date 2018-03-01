package model;

import Jama.Matrix;

public class TransitionMatrix {
	private int rows;
	private int cols;
	
	private int S;
	private double [][] array;
	Matrix matrix; 
	public TransitionMatrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		S  = rows*cols*4;
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
		array[getStateFromPosition(0,0,0)][getStateFromPosition(1,0,1)] = 0.5;  //(0,0, N) -> (1,0, E)
		array[getStateFromPosition(0,0,0)][getStateFromPosition(0,1,2)] = 0.5; 	//(0,0, N) -> (0,1, S)
		//topp vänster hörn E
		array[getStateFromPosition(0,0,1)][getStateFromPosition(1,0,1)] = 0.7;  
		array[getStateFromPosition(0,0,1)][getStateFromPosition(0,1,2)] = 0.3; 	
		
		//topp vänster hörn S
		array[getStateFromPosition(0,0,2)][getStateFromPosition(0,1,2)] = 0.7;  
		array[getStateFromPosition(0,0,2)][getStateFromPosition(1,0,1)] = 0.3; 	
		
		//topp vänster hörn E
		array[getStateFromPosition(0,0,3)][getStateFromPosition(1,0,1)] = 0.5;  
		array[getStateFromPosition(0,0,3)][getStateFromPosition(0,1,2)] = 0.5; 	
		
		//topp höger hörn N
		array[getStateFromPosition(cols, 0, 0)][getStateFromPosition(cols-1,0, 3)] = 0.5; //(col, 0, N) -> (col-1, 0, 3)
		array[getStateFromPosition(cols, 0, 0)][getStateFromPosition(cols,1, 2)] = 0.5; //(col, 0, N) -> (col-1, 0, 3)
		
		//topp höger hörn E
		array[getStateFromPosition(cols, 0, 1)][getStateFromPosition(cols-1,0,3)] = 0.5; //(col, 0, N) -> (col-1, 0, 3)
		array[getStateFromPosition(cols, 0, 1)][getStateFromPosition(cols,1,2)] = 0.5;
		//topp höger hörn S
		array[getStateFromPosition(cols, 0, 2)][getStateFromPosition(cols,1,2)] = 0.7; 
		array[getStateFromPosition(cols, 0, 2)][getStateFromPosition(cols-1,0,3)] = 0.3;
		//topp höger hörn W
		array[getStateFromPosition(cols, 0, 3)][getStateFromPosition(cols-1,0,3)] = 0.7; 
		array[getStateFromPosition(cols, 0, 3)][getStateFromPosition(cols,1,2)] = 0.3;
		
		//vänster bottenhörn N
		array[getStateFromPosition(0, rows, 0)][getStateFromPosition(0,rows-1,0)] = 0.7; 
		array[getStateFromPosition(0, rows, 0)][getStateFromPosition(1,rows,1)] = 0.3;
		//vänster bottenhörn E
		array[getStateFromPosition(0, rows, 1)][getStateFromPosition(0,rows-1,0)] = 0.3; 
		array[getStateFromPosition(0, rows, 1)][getStateFromPosition(1,rows,1)] = 0.7;
		//vänster bottenhörn S
		array[getStateFromPosition(0, rows, 2)][getStateFromPosition(0,rows-1,0)] = 0.5; 
		array[getStateFromPosition(0, rows, 2)][getStateFromPosition(1,rows,1)] = 0.5;
		//vänster bottenhörn W
		array[getStateFromPosition(0, rows, 3)][getStateFromPosition(0,rows-1,0)] = 0.5; 
		array[getStateFromPosition(0, rows, 3)][getStateFromPosition(1,rows,1)] = 0.5;
	
		
		//Höger bottenhörn N
		array[getStateFromPosition(cols, rows, 0)][getStateFromPosition(cols,rows-1,0)] = 0.7; 
		array[getStateFromPosition(cols, rows, 0)][getStateFromPosition(cols-1,rows,3)] = 0.3;
		
		//Höger bottenhörn E
		array[getStateFromPosition(cols, rows, 1)][getStateFromPosition(cols,rows-1,0)] = 0.5; 
		array[getStateFromPosition(cols, rows, 1)][getStateFromPosition(cols-1,rows,3)] = 0.5;
		
		//Höger bottenhörn S
		array[getStateFromPosition(cols, rows, 2)][getStateFromPosition(cols,rows-1,0)] = 0.5; 
		array[getStateFromPosition(cols, rows, 2)][getStateFromPosition(cols-1,rows,3)] = 0.5;
		
		//Höger bottenhörn W
		array[getStateFromPosition(cols, rows, 3)][getStateFromPosition(cols-1,rows,3)] = 0.7; 
		array[getStateFromPosition(cols, rows, 3)][getStateFromPosition(cols,rows-1,0)] = 0.3;
		
		
		//the northern wall
		for (int i = 1; i < cols -1; i++) {
			//N
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i-1,0,3)] = 1.0/3; //W
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i+1,0,1)] = 1.0/3; //E
			array[getStateFromPosition(i,0,0)][getStateFromPosition(i,1,2)] = 1.0/3; //S

			//E
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i+1,0,1)] = 0.7; // E
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i-1,0,3)] = 0.15; // W
			array[getStateFromPosition(i,0,1)][getStateFromPosition(i,1,2)] = 0.15;	//S
			
			//S
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i,1,2)] = 0.7; //S
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i+1,0,1)] = 0.15; //E
			array[getStateFromPosition(i,0,2)][getStateFromPosition(i-1,0,3)] = 0.15;//W
			
			//W
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i-1,0,3)] = 0.7; //W
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i+1,0,1)] = 0.15; //E
			array[getStateFromPosition(i,0,3)][getStateFromPosition(i,1,2)] = 0.7; //S
		}
		
		//the wall of the south
		for ( int i = 1; i < cols - 1; i++) {
			//N
			array[getStateFromPosition(i,rows,0)][getStateFromPosition(i,rows-1,0)] = 0.7; //N
			array[getStateFromPosition(i,rows,0)][getStateFromPosition(i-1,rows,3)] = 0.15; //W
			array[getStateFromPosition(i,rows,0)][getStateFromPosition(i+1,rows,1)] = 0.15; //E
			
			
			//E
			array[getStateFromPosition(i,rows,1)][getStateFromPosition(i+1,rows,1)] = 0.7; //E
			array[getStateFromPosition(i,rows,1)][getStateFromPosition(i,rows-1,0)] = 0.15; //N
			array[getStateFromPosition(i,rows,1)][getStateFromPosition(i-1,rows,3)] = 0.15; //W
			
			//S
			array[getStateFromPosition(i,rows,2)][getStateFromPosition(i,rows-1,0)] = 1.0/3; //N
			array[getStateFromPosition(i,rows,2)][getStateFromPosition(i+1,rows,1)] = 1.0/3; //E
			array[getStateFromPosition(i,rows,2)][getStateFromPosition(i-1,rows,3)] = 1.0/3; //W
			
			//W
			array[getStateFromPosition(i,rows,3)][getStateFromPosition(i-1,rows,3)] = 0.7; //W
			array[getStateFromPosition(i,rows,3)][getStateFromPosition(i+1,rows,1)] = 0.15; //E
			array[getStateFromPosition(i,rows,3)][getStateFromPosition(i,rows-1,0)] = 0.15; //N
		}
		
		//The wall of the East, do not breach
		for ( int i = 1; i < rows - 1; i++) {
			//N
			array[getStateFromPosition(0,i,0)][getStateFromPosition(0,i-1,0)] = 0.7; //N
			array[getStateFromPosition(0,i,0)][getStateFromPosition(1,i,1)] = 0.15; //E
			array[getStateFromPosition(0,i,0)][getStateFromPosition(0,i+1,2)] = 0.15; //S
			
			//E
			array[getStateFromPosition(0,i,1)][getStateFromPosition(1,i,1)] = 0.7; //E
			array[getStateFromPosition(0,i,1)][getStateFromPosition(0,i-1,0)] = 0.15; //N
			array[getStateFromPosition(0,i,1)][getStateFromPosition(0,i+1,2)] = 0.15; //S
			
			//S
			array[getStateFromPosition(0,i,2)][getStateFromPosition(0,i+1,2)] = 0.7; //S
			array[getStateFromPosition(0,i,2)][getStateFromPosition(0,i-1,0)] = 0.7; //N
			array[getStateFromPosition(0,i,2)][getStateFromPosition(1,i,1)] = 0.7; //E
			
			//W
			array[getStateFromPosition(0,i,3)][getStateFromPosition(0,i-1,0)] = 1.0/3; //N
			array[getStateFromPosition(0,i,3)][getStateFromPosition(1,i,1)] = 1.0/3; //E
			array[getStateFromPosition(0,i,3)][getStateFromPosition(0,i+1,2)] = 1.0/3; //S
		}
		
		//the right wall
		for (int i = 1; i < rows -1 ; i++) {
			//N
			array[getStateFromPosition(cols,i,0)][getStateFromPosition(cols,i-1,0)] = 0.7; //N
			array[getStateFromPosition(cols,i,0)][getStateFromPosition(cols-1,i,3)] = 0.15; //W
			array[getStateFromPosition(cols,i,0)][getStateFromPosition(cols,i+1,2)] = 0.15; //S
			//E
			array[getStateFromPosition(cols,i,1)][getStateFromPosition(cols,i-1,0)] = 1.0/3; //N
			array[getStateFromPosition(cols,i,1)][getStateFromPosition(cols,i+1,2)] = 1.0/3; //S
			array[getStateFromPosition(cols,i,1)][getStateFromPosition(cols-1,i,3)] = 1.0/3; //W

			//S
			array[getStateFromPosition(cols,i,2)][getStateFromPosition(cols,i-1,0)] = 0.15; //N
			array[getStateFromPosition(cols,i,2)][getStateFromPosition(cols,i+1,2)] = 0.7; //S
			array[getStateFromPosition(cols,i,2)][getStateFromPosition(cols-1,i,3)] = 0.15; //W

			//W
			array[getStateFromPosition(cols,i,3)][getStateFromPosition(cols-1,i,3)] = 0.7; //W
			array[getStateFromPosition(cols,i,3)][getStateFromPosition(cols,i-1,0)] = 0.15; //N
			array[getStateFromPosition(cols,i,3)][getStateFromPosition(cols,i+1,2)] = 0.15; //S
		}
		//"Middle of the matrix, rest
		
		for( int i = 1; i < cols -1; i++) {
			for( int j = 1; j < rows -1; j++) {
				//N
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i,j-1,0)] = 0.7; //N
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i+1,j,1)] = 0.1; //E
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i,j+1,2)] = 0.1; //S
				array[getStateFromPosition(i,j,0)][getStateFromPosition(i-1,j,3)] = 0.1; //W
				
				
				//E
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i+1,j,1)] = 0.7; //E
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i,j-1,0)] = 0.1; //N
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i,j+1,2)] = 0.1; //S
				array[getStateFromPosition(i,j,1)][getStateFromPosition(i-1,j,3)] = 0.1; //W
				
				//S
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i,j+1,2)] = 0.7; //S
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i,j-1,0)] = 0.1; //N
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i+1,j,1)] = 0.1; //E
				array[getStateFromPosition(i,j,2)][getStateFromPosition(i-1,j,3)] = 0.1; //W
				
				//W
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i-1,j,3)] = 0.7; //W
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i,j-1,0)] = 0.1; //N
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i+1,j,1)] = 0.1; //E
				array[getStateFromPosition(i,j,3)][getStateFromPosition(i,j+1,2)] = 0.1; //S

			}
		}
		matrix = new Matrix(array);
		return matrix;
	}
	
	
	
	
	
	
}
