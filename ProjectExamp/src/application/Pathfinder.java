package application;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Pathfinder {
	
	Room room;
	ArrayList<Point> pathList = new ArrayList<Point>();
	
	public Pathfinder(Room room) {
		this.room = room;		
	}
	
	public void findPath(Point src, Point dest) {
		Tile startTile = new Tile(src.getLocation());
		Tile destTile = new Tile(dest.getLocation());
		
    	List<Tile> openList = new ArrayList<Tile>();
    	HashSet<Tile> closedList = new HashSet<Tile>();
    	
    	openList.add(startTile);
    	
    	while(openList.size() > 0) {
    		Tile currentTile = openList.get(0);
    		for(int i = 1; i < openList.size(); i++) {
    			if(openList.get(i).getFValue() < currentTile.getFValue() 
    					|| openList.get(i).getFValue() == currentTile.getFValue() 
    					&& openList.get(i).getHCost() < currentTile.getHCost())
    				currentTile = openList.get(i);
    		}
    		
    		openList.remove(currentTile);
    		closedList.add(currentTile);
    		
    		if(currentTile.getPoint().equals(destTile.getPoint())) {
    			RetracePath();
    			return;
    		}
    		
    		for(Tile neighbor : room.getNeighbors(currentTile)) {
    			System.out.println("Tile in for loop n: "+neighbor.toString()); /*** Check test **/
    			System.out.println("Tile in for loop gCost: "+neighbor.getGCost()); /*** Check test **/
    			if(!(neighbor.getObstacleType() < 1) || closedList.contains(neighbor)) {
    				System.out.println("neighbor wall: " +neighbor.isWall());
    				System.out.println("neighbor in closedlist: " +closedList.contains(neighbor));
    				continue;
    			}
    			
    			int newCostNeighbor = currentTile.getGCost() + getDist(currentTile, neighbor);
    			System.out.println("New neighbor cost: "+newCostNeighbor); /*** Check test **/
    			System.out.println("neighbor Gcost: "+neighbor.gCost); /*** Check test **/
    			
    			if(newCostNeighbor < neighbor.getGCost() || !openList.contains(neighbor)) {
    				System.out.println("openList.contains(neighbor): "+openList.contains(neighbor)); /*** Check test **/
    				neighbor.setGCost(newCostNeighbor);
    				neighbor.setHCost(getDist(neighbor, destTile)); 
    				neighbor.setParent(currentTile); 
    				System.out.println("neighbor parent point: "+neighbor.getParent().getPoint()); /*** Check test **/
    				
    				if(!pathList.contains(neighbor.getParent().getPoint()))
    					pathList.add(neighbor.getParent().getPoint());
    				
    				if(!openList.contains(neighbor))
    					openList.add(neighbor);
    			}
    		}
    	}
    	
    	System.out.println("Closed list size is "+closedList.size());
    	Tile[] tarry = new Tile[closedList.size()];
    	closedList.toArray(tarry);
    	for(Tile t : tarry){
    		System.out.println(" list item is "+ t.getPoint());
    	}
    	
	}
	
	public List<Point> RetracePath() {
//		List<Point> path = new ArrayList<Point>();
//		List<Tile> tiles = new ArrayList<Tile>();
//		Tile currentT = dest;
//		
//		while(currentT != start) {
//			System.out.println("CurrentT retracepath: " + currentT.getPoint());
//			//System.out.println("CurrentT parent retracepath: " + currentT.getParent().getPoint());
//			path.add(currentT.getPoint());
//			tiles.add(currentT);
//			
//			currentT = currentT.getParent();
//		}
//		Collections.reverse(path);
//		System.out.println("Path size is " + path.size());
		
		Collections.reverse(pathList);
		return pathList;
	}
	
	public int getDist(Tile tileA, Tile tileB) {
		System.out.println("TileA in getDist: "+tileA.getPoint().x); /*** Check test **/
		System.out.println("TileA getDist: "+tileA.getPoint().y); /*** Check test **/
		int distX = Math.abs(tileA.getPoint().x - tileB.getPoint().x);
		int distY = Math.abs(tileA.getPoint().y - tileB.getPoint().y);
		
		System.out.println("getDist distX: "+distX); /*** Check test **/
		System.out.println("TileA getDist: "+distY); /*** Check test **/
		
		if(distX > distY)
			return 14 * distY + 10 * (distX - distY);
		
		return 14 * distX + 10 * (distY - distX);
	}

}
