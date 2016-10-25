package com.groupseven.cleansweep;

public class Tile {
	//handles carpet type, dirt level, and obstacle on the floor.
	public static final int CARPET_BARE = 0;
	public static final int CARPET_LOW = 1;
	public static final int CARPET_HIGH = 2;
	
	public static final int OBSTACLE_NONE = 0;
	public static final int OBSTACLE_BLOCK = 1;
	public static final int OBSTACLE_STAIRS = 2;
	
	private int carpet;
	private int dirtLevel;
	private int obstacleType;
	private boolean placeholder;
	
	public Tile(){
		this.carpet=-1;
		this.dirtLevel=-1;
		this.obstacleType=-1;
		this.placeholder=true;
	}
	
	public Tile(int carpetType, int dirt, int obstacle){
		this.carpet=carpetType;
		this.dirtLevel=dirt;
		this.obstacleType=obstacle;
		this.placeholder=false;
	}
	
	public boolean isPlaceholder(){return placeholder;}
	
	public void cleanTile(){
		//if (dirtLevel<=0) throw new Exception(); //TODO exception type
		dirtLevel--;
	}
	
	public int getCarpetType(){return carpet;}
	public boolean hasDirt(){return dirtLevel>0;}
	public int getObstacleType(){return obstacleType;}
	
	
}