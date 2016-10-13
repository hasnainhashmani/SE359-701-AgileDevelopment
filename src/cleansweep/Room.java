package cleansweep;

import java.util.List;
import java.util.ArrayList;

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
	
	public Room(int width, int height){
		this.w=width;
		this.h=height;
		floor = new ArrayList<List<Tile>>();
		walls = new ArrayList<List<Integer>>();
		for (int y = 0; y<height; y++){
			floor.add(new ArrayList<Tile>());

			walls.add(new ArrayList<Integer>());
			walls.add(new ArrayList<Integer>());
			for (int x = 0; x<width+1; x++){
				floor.get(y).add(new Tile());
				walls.get(y*2).add(0);
				walls.get(y*2+1).add(0);
			}
		}
		walls.add(new ArrayList<Integer>());
		for (int x = 0; x<width+1; x++){
			walls.get(height*2).add(0);
		}
	}
	
	public void addTile(int x, int y, Tile t) throws Exception{
		//TODO replace x,y with Point?
		List<Tile> row = floor.get(y);
		while (row.size() < x+1){
			row.add(new Tile());
		}
		if (row.get(x).isPlaceholder()){
			row.remove(x);
			row.add(x, t);
		} else {
			System.out.println(row.get(x).isPlaceholder());
			System.out.println(floor);
			throw new Exception("Tile already on floor");
		}
	}
	
	public void addTile(Tile t){
		/* Convenience method for parser */
		for(int y=0; y<floor.size(); y++){
			if (floor.get(y).size()<this.w){
				floor.get(y).add(t);
				return;
			}
		}
	}
	
	public void addWall(int x, int y, int direction, int wType){
		//TODO replace x,y with Point?
		if (direction==DIR_N){
			walls.get(y*2).add(x,wType);
		} else if (direction==DIR_S) {
			walls.get((y*2)+2).add(x,wType);
		} else if (direction==DIR_W){
			walls.get((y*2)+1).add(x,wType);
		} else{
			walls.get((y*2)+1).add(x+1,wType);
		}
	}
	
	public String toString(){
		//simple output
		//TODO make this code less crap
		String s = "";
		for(int y = 0; y<floor.size(); y++){
			List<Tile> row = floor.get(y);
			for(int x=0; x<row.size(); x++){
				if (walls.get(y*2).get(x)>0){
					s+=" _"; //TODO doors
				}
				else{
					s+=" .";
				}
			}
			s+="\n";
			for(int x=0; x<row.size(); x++){
				if (walls.get((y*2)+1).get(x)>0){
					s+="|"; //TODO doors
				} else{
					s+=".";
				}
				s += row.get(x).getCarpetType();
			}
			if (walls.get((y*2)+1).get(row.size()-1)>0){
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
	//TODO: add objects
	
	public int[] wallsSurrounding(int x, int y){
		/* Returns the wall types surrounding the tile at x,y
		 * ORDER: N,W,E,S
		 * See constants (WALL_NONE, WALL_WALL, etc)
		 */
		//TODO
		int[] sides = {walls.get(y*2).get(x),
					   walls.get((y*2)+1).get(x),
					   walls.get((y*2)+1).get(x+1),
					   walls.get((y*2)+2).get(x)};
		return sides; //all four directions
	}
	
	public boolean hasDirtAt(int x, int y){
		//TODO error handle
		//TODO x,y replace with Point?
		return floor.get(y).get(x).hasDirt();
	}
	
	public void clean(int x, int y){
		//TODO error handle
		//TODO x,y replace with Point?
		floor.get(y).get(x).cleanTile();
	}
	
	public int getFloorTypeAt(int x, int y){
		//TODO error handle
		//TODO x,y replace with Point?
		return floor.get(y).get(x).getCarpetType();
	}
	
	public int getWidth(){return w;}
	public int getHeight(){return h;}
	
}