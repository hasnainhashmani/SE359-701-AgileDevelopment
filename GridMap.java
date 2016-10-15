import java.awt.Point;
import java.util.Random;

public class GridMap {
	static int dimension;
	static boolean[][] grid = new boolean[25][25];
	static Point currentPosition;

	public GridMap(int dimension){
		GridMap.dimension = dimension;
		currentPosition = placeRobot(); //Placing our robot
	}
	
	public Point placeRobot(){
		Random o = new Random();
		int x = 0; int y=0;
		boolean isPlaced = false;
		while(!isPlaced){
			x = o.nextInt(dimension); //Random x coordinate
			y = o.nextInt(dimension); //Random y coordinate
			if(grid[y][x] != true) isPlaced = true;
		}
		return new Point(x,y);
	}

	public static boolean[][] getMap() {
		// TODO Auto-generated method stub
		return grid;
	}
}
