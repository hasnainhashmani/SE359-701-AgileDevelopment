package cleansweep;

import java.util.List;
import java.util.ArrayList;

public class Room {
	private List<List<Tile>> floor;
	private List<List<Integer>> walls; //each index is the wall for the top/left of the floor tile
	
	public Room(int width, int height){
		floor = new ArrayList<List<Tile>>();
		walls = new ArrayList<List<Integer>>();
		ArrayList<Tile> f;
		ArrayList<Integer> w;
		for (int y = 0; y<height; y++){
			f = new ArrayList<Tile>();
			w = new ArrayList<Integer>();
			floor.add(f);
			walls.add(w);
		}
	}
	//TODO: add floor and walls (set up room), add objects
	
	public boolean[] obstacleBlocking(int x, int y){
		//TODO
		boolean[] sides = {false,false,false,false};
		return sides; //all four directions
	}
	
	public boolean hasDirtAt(int x, int y){
		//TODO error handle
		return floor.get(y).get(x).hasDirt();
	}
	
	public int getFloorTypeAt(int x, int y){
		//TODO error handle
		return floor.get(y).get(x).getCarpetType();
	}
	
	public void clean(int x, int y){
		floor.get(y).get(x).cleanTile();
	}
	
}