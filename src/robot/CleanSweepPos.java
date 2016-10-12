package Robot;

import java.awt.Point;
import java.util.ArrayList;

public class CleanSweepPos  {
	private int x;
	private int y;
	Point startPos = new Point(x, y);
	Point currentPos = new Point(x,y);
	Point nextPos = new Point(x,y);
	Direction direction;
	//get the start point
	public Point getStartPos(int x, int y){
		startPos.x = x;
		startPos.y = y;
		return startPos;
	}
	//get the current point
	public Point getCurrentPos(int x, int y){
		currentPos.x = x;
		currentPos.y = y;
		return currentPos;
	}
	//get the next point, direction.(left/right/up/down).getNextPos(x,y)
	public Point getNextPos(int x, int y){
		switch(direction){
		case left:
			nextPos.x = x - 1;
			nextPos.y = y;
			break;
		case right:
			nextPos.x = x + 1;
			nextPos.y = y;
			break;
		case down:
			nextPos.x = x;
			nextPos.y = y -1;
			break;
		case up:
			nextPos.x = x;
			nextPos.y = y + 1;
			break;
		}
		return nextPos;		
	}
	//return a list of clean point
	public ArrayList<Point> path(Point x){
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(x);
		return path;
	}
}
