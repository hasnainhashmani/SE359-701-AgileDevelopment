package application;

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class Room {
	
	public static final int WALL_NONE = 0; //TODO replace with enum
	public static final int WALL_WALL = 1;
	public static final int WALL_DOORCLOSED = 2;
	public static final int WALL_DOOROPEN = 3;
	
	public static final int DIR_N = 0; //TODO make this match the other direction enum
	public static final int DIR_W = 1;
	public static final int DIR_E = 2;
	public static final int DIR_S = 3;
	
	private List<List<Tile>> floor; /* h*w */
	private List<List<Integer>> walls; /* walls is 2*h+1 by w+1 */
	private int w;
	private int h;
	
	private Point startingPos;
	
	public Room(int width, int height){
		this.w=width;
		this.h=height;
		this.startingPos=new Point(0,0);
		floor = new ArrayList<List<Tile>>();
		walls = new ArrayList<List<Integer>>();
		for (int y = 0; y<height; y++){
			floor.add(new ArrayList<Tile>());

			walls.add(new ArrayList<Integer>());
			walls.add(new ArrayList<Integer>());
			for (int x = 0; x<width; x++){
				floor.get(y).add(new Tile());
				walls.get(y*2).add(0);
				walls.get(y*2+1).add(0);
			}
			walls.get(y*2).add(0);
			walls.get(y*2+1).add(0);
		}
		walls.add(new ArrayList<Integer>());
		for (int x = 0; x<width+1; x++){
			walls.get(height*2).add(0);
		}
	}
	
	public void addTile(Point p, Tile t){
		//todo a bit more error handling?
		int tempX = p.x; 
		int tempY = p.y;
		List<Tile> row = floor.get(tempY);
		while (row.size() < tempX+1){
			Tile til = new Tile();
			til.setPoint(p);
			row.add(til);
		}
		if (row.get(tempX).isPlaceholder()){
			row.remove(tempX);
			row.add(tempX, t);
		} else {
			System.out.println(row.get(tempX).isPlaceholder());
			System.out.println(floor);
			throw new RuntimeException("Tile already on floor " + p.x + " " + p.y);
		}
	}

	public Point getStartingPosition(){
		/*returns the starting position of the robot*/
		return startingPos; //TODO fix unsafe
	}
	
	public void setStartingPos(Point p){
		//TODO error handle
		startingPos = p;
	}
	
	public void addWall(Point p, int direction, int wType){
		//todo error handle
		int tempX=p.x;
		int tempY=p.y;
		if (direction==DIR_N){
			walls.get(tempY*2).add(tempX,wType);
		} else if (direction==DIR_S) {
			walls.get((tempY*2)+2).add(tempX,wType);
		} else if (direction==DIR_W){
			walls.get((tempY*2)+1).add(tempX,wType);
		} else{
			walls.get((tempY*2)+1).add(tempX+1,wType);
		}
	}
	
	public String toString(){
		//simple output
		//TODO make this code less crap
		String s = "";
		for(int y = 0; y<floor.size(); y++){
			List<Tile> row = floor.get(y);

			for(int x=0; x<row.size(); x++){
				if (new Point(x,y).equals(this.getStartingPosition())){
					s+="P";
					if (walls.get(y*2).get(x)>0){
						s+="_"; //TODO doors
					}
					else{
						s+=".";
					}
				}
				else{if (walls.get(y*2).get(x)>0){
					s+=" _"; //TODO doors
				}
				else{
					s+=" .";
				}}
			}
			s+="\n";
			for(int x=0; x<row.size(); x++){
				if (walls.get((y*2)+1).get(x)>0){
					s+="|"; //TODO doors
				} else{
					s+=".";
				}
				s += row.get(x).getCarpetType()+1;
			}
			if (walls.get((y*2)+1).get(row.size())>0){
				s+="|"; //TODO doors
			} else{
				s+=".";
			}
			s += "\n";
		}
		for(int x=0; x<w+1; x++){
			if (walls.get(h*2).get(x)>0){
				s+=" _"; //TODO doors
			}
			else{s+=" .";}
		}
		return s;
	}
	
	public int[] wallsSurrounding(Point p){
		/* Returns the wall types surrounding the tile at p
		 * ORDER: N,W,E,S
		 * See constants (WALL_NONE, WALL_WALL, etc)
		 */
		int tX = p.x;
		int tY = p.y;
		int[] sides = {walls.get(tY*2).get(tX),
					   walls.get((tY*2)+1).get(tX),
					   walls.get((tY*2)+1).get(tX+1),
					   walls.get((tY*2)+2).get(tX)};
		return sides; //all four directions
	}
	
	public boolean hasDirtAt(Point p){
		//TODO error handle
		return floor.get(p.y).get(p.x).hasDirt();
	}
	
	public void clean(Point p){
		//TODO error handle
		floor.get(p.y).get(p.x).cleanTile();
	}
	
	public int getFloorTypeAt(Point p){
		//TODO error handle
		return floor.get(p.y).get(p.x).getCarpetType();
	}
	
	public int floorIsObstacle(Point p){
		return floor.get(p.y).get(p.x).getObstacleType();
	}
	
	public boolean floorIsPlaceholder(Point p){
		return floor.get(p.y).get(p.x).isPlaceholder();
	}
	
	public int getWidth(){return w;}
	public int getHeight(){return h;}
	
	public List<List<Tile>> getFloorArry() {
		return floor;
	}
	
	public List<Tile> getNeighbors(Tile point) {
		List<Tile> neighbors = new ArrayList<Tile>();
		
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				if(x == 0 && y == 0)
					continue;
				
				int checkX = point.getPoint().x + x;
				int checkY = point.getPoint().y + y;
				
				if(checkX >= 0 && checkX < this.getWidth() && checkY >= 0 && checkY < this.getHeight()){
					Tile t = new Tile(new Point(checkX, checkY));
					System.out.println("Neighbor point is "+t.getPoint());
					System.out.println("Tile at point is "+t);
					neighbors.add(t);
				}
			}
			
		}
		
		return neighbors;			
	}
	
}