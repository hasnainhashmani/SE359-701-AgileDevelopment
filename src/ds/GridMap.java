package ds;

import java.awt.Point;
import java.util.Random;

public class GridMap {
	int objectCount;
	int dimension;
	boolean[][] grid = new boolean[25][25];
	Point currentPosition;


	public GridMap(int dimension, int objectCount){
		this.dimension = dimension;
		this.objectCount = objectCount;
		placeobjects(); //Placing objects before anything else. So as to keep from placing ships on objects.
		currentPosition = placeRobot(); //Placing our robot
	}
	
	public void placeobjects(){
		Random o = new Random();
		int x = 0; int y=0;
		int icount=0;
		while(icount!=objectCount){
			x = o.nextInt(dimension); //Random x coordinate
			y = o.nextInt(dimension); //Random y coordinate
			if(grid[y][x]==false){
				grid[y][x] = true; // making sure there are exactly the number of objects and not to overlap an object
				icount++;			//adding to count when a place on the grid has been set to true
			}
		}
	}
	
	public Point placeRobot(){
		Random o = new Random();
		int x = 0; int y=0;
		boolean isPlaced = false;
		while(!isPlaced){
			x = o.nextInt(dimension); //Random x coordinate
			y = o.nextInt(dimension); //Random y coordinate
			if(grid[y][x] != true) isPlaced = true; // making sure robot doesn't start off on an object
		}
		return new Point(x,y);
	}

	public boolean[][] getMap() {
		// TODO Auto-generated method stub
		return grid;
	}
	
	/*public void printMap(){
		//Only for checking 
	for(int x=0; x<dimension; x++){
		for(int y=0; y<dimension; y++){
			if(grid[x][y]==true) System.out.print(" x ");
			else System.out.print(" o ");
		}
		System.out.println();
		}
	}*/

}
