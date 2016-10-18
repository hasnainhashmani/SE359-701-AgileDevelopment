package application;

import java.awt.Point;

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
	
	public Point point;
    public Tile parent;
    public int gCost; //points from start
    public int hCost; //distance from target
    private boolean isWall; 		
	
	public Tile(){
		this.carpet=-1;
		this.dirtLevel=-1;
		this.obstacleType=-1;
		this.placeholder=true;
		this.setParent(parent);
		this.setPoint(point);
		this.setWall(this.getObstacleType());
	}
	
	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public Tile(int carpetType, int dirt, int obstacle){
		this.carpet=carpetType;
		this.dirtLevel=dirt;
		this.obstacleType=obstacle;
		this.placeholder=false;
//		this.point = p;
		this.setWall(this.getObstacleType());
	}
	
	public Tile(Point p) {
		this.point = p;
	}
	
    public Tile(int carpetType, int dirt, int obstacle, Point p) {
		this.carpet=carpetType;
		this.dirtLevel=dirt;
		this.obstacleType=obstacle;
		this.placeholder=false;
		this.point = p;
		this.setWall(this.getObstacleType());
	}

	public void setPoint(Point point) {
        this.point = point;
    }
    
    public Point getPoint() {
    	return point;
    }
	
	public boolean isPlaceholder(){return placeholder;}
	
	public void cleanTile(){
		//if (dirtLevel<=0) throw new Exception(); //TODO exception type
		dirtLevel--;
	}
	
	public int getCarpetType(){return carpet;}
	public boolean hasDirt(){return dirtLevel>0;}
	public int getObstacleType(){return obstacleType;}



    /**
     * Used for setting the starting node value to 0
     */
	
	public void setWall(int obstacleType) {
		
		if(obstacleType < 1)
			this.isWall = false;
		else
			this.isWall = true;
		
	}
	
	public boolean isWall(){
		return isWall;
	}
    public void setGCost(int amount) {
        this.gCost = amount;
    }
    
    public int getGCost() {
        return gCost;
    }
    
    public void setHCost(int amount) {
        this.hCost = amount;
    }
    
    public int getHCost() {
        return hCost;
    }

    public int getFValue() {
        return this.gCost + this.hCost;
    }
    
    public String toString() {
    	return String.format("This tile Point: x=%d, y=%d", this.point.x, this.point.y);
    }
	
}