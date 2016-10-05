import Tile;
import java.util.ArrayList;

public class Room {
	private List<List<Tile>> floor;
	//TODO walls
	
	public Room(){
		this.floor = new ArrayList<List<Tile>>();
	}
	
	public Room(String filename){
		//TODO parse file
	}
	
}