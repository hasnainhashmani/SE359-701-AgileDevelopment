
public class Tile {
	//handles carpet type, dirt level, and obstacle on the floor.
	public final int CARPET_BARE = 0;
	public final int CARPET_LOW = 1;
	public final int CARPET_HIGH = 2;
	
	public final int OBSTACLE_NONE = 0;
	public final int OBSTACLE_BLOCK = 1;
	public final int OBSTACLE_STAIRS = 2;
	
	private int carpet;
	private int dirtLevel;
	private int obstacleType;
	
	public Tile(){
		this.carpet=0;
		this.dirtLevel=0;
		this.obstacleType=0;
	}
	
	public Tile(int carpetType, int dirt, int obstable){
		this.carpet=carpetType;
		this.dirtLevel=dirt;
		this.obstacleType=obstacle;
	}
	
	public void cleanTile(){
		if (dirtLevel<=0) throw new Exception(); //TODO exception type
		dirtLevel--;
	}
	
	public int getCarpetType(){
		return carpet;
	}
	
	public boolean hasDirt(){
		return dirtLevel>0;
	}
	
	public int getObstacleType(){
		return obstacleType;
	}
	
	
}