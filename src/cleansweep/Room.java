package cleansweep;

import java.util.List;
import java.util.ArrayList;

public class Room {
	
	public static final int WALL_NONE = 0;
	public static final int WALL_WALL = 1;
	public static final int WALL_DOORCLOSED = 2;
	public static final int WALL_DOOROPEN = 3;
	
	public static final int DIR_N = 0;
	public static final int DIR_W = 1;
	public static final int DIR_E = 2;
	public static final int DIR_S = 3;
	
	private List<List<Tile>> floor;
	private List<List<Integer>> walls;
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
		}
		walls.add(new ArrayList<Integer>());
	}
	
	public void addTile(Tile t){
		//TODO This is a bit awkward, maybe refactor into a constructor?
		for(int y=0; y<floor.size(); y++){
			if (floor.get(y).size()<this.w){
				floor.get(y).add(t);
				return;
			}
		}
	}
	
	public void addWall(int x, int y, int direction, int wType){
		//TODO This is a bit awkward, maybe refactor into a constructor?
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
					s+="  ";
				}
			}
			s+="\n";
			for(int x=0; x<row.size(); x++){
				if (walls.get((y*2)+1).get(x)>0){
					s+="|"; //TODO doors
				} else{
					s+=" ";
				}
				s += row.get(x).getCarpetType();
			}
			if (walls.get((y*2)+1).get(row.size())>0){
				s+="|"; //TODO doors
			} else{
				s+=" ";
			}
			s += "\n";
		}
		for(int x=0; x<w; x++){
			if (walls.get(h*2).get(x)>0){
				s+=" _"; //TODO doors
			}
			else{s+="  ";}
		}
		return s;
	}
	//TODO: add objects
	
	public boolean[] obstacleBlocking(int x, int y){
		//TODO
		boolean[] sides = {false,false,false,false};
		return sides; //all four directions
	}
	
	public boolean hasDirtAt(int x, int y){
		//TODO error handle
		return floor.get(y).get(x).hasDirt();
	}
	
	public void clean(int x, int y){
		//TODO error handle
		floor.get(y).get(x).cleanTile();
	}
	
	public int getFloorTypeAt(int x, int y){
		//TODO error handle
		return floor.get(y).get(x).getCarpetType();
	}
	
	public int getWidth(){return w;}
	public int getHeight(){return h;}
	
}