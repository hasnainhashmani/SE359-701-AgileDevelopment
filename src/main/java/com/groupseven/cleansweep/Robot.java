package main.java.com.groupseven.cleansweep;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Robot {
	
	private Room known; //TODO methods to access this? Right now GUI directly accesses it, possible unsafe
	private Room toExplore;	
	private Point pos;
	/*** Logger also create a log file (log.txt) in project workspace for record keeping 
	 *   Conig and Fine sensor data print to log.txt 
	 **/
	CleansweepLog logger; 
	
	public Robot(Room toExplore){
		this.setPos(toExplore.getStartingPosition());
		setKnown(new Room(toExplore.getWidth(), toExplore.getHeight()));
		getKnown().setStartingPos(pos);
		this.toExplore=toExplore;
		logger = new CleansweepLog();
		logger.getLOGGER().setLevel(Level.ALL);
		logger.getLOGGER().config("Robot Created, Starting position: [" + this.getPos().x + ", " + this.getPos().y + "]");
	}
	
	/**
	 * @return the known
	 */
	public Room getKnown() {
		return known;
	}

	/**
	 * @param known the known to set
	 */
	public void setKnown(Room known) {
		this.known = known;
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public void step(){
		//move one step in the simulation
		//This should happen whenever someone in the GUI requests it 
		//(e.g. press spacebar, click a button that says next, or once a second if we 'play' the simulation)
		//TODO save steps
		
		if (toExplore.hasDirtAt(this.getPos())) { //clean at the current position instead of moving
			toExplore.clean(this.getPos());
			
			if(!toExplore.hasDirtAt(this.getPos())){
				getKnown().clean(this.getPos());
			}
			
			return;
		} 
		else if (getKnown().hasDirtAt(this.getPos())) {			
			getKnown().clean(this.getPos());
			logger.getLOGGER().log(Level.INFO, "Robot Cleaning postion: [" + this.getPos().x + ", " + this.getPos().y + "]");
		}
	
		gatherData(); //check all the sensors, add room data to stored data
		Point p = getObjective(); //find the next place to clean (or, the charging station if we're out of fuel/dirt capacity)
		
		if(this.getPos().equals(p)) {
			
			return; //If there's nowhere to go, we're done
		}
		
		Point next = getPath(this.getPos(), p).get(0);     //Plot a path to the next objective
		logger.getLOGGER().log(Level.INFO, "position: [" + this.getPos().x +", "+ this.getPos().y+"] clean!!");
		logger.getLOGGER().log(Level.INFO, "Robot moving from position: [" + this.getPos().x +", "+ this.getPos().y+"] to position: [" + next.x + ", " + next.y + "]");
		
		this.setPos(new Point(next.x, next.y)); //update stored position
		//System.out.print(known); //This is handled by gui
	}
	
	public void forceMove(Point p){
		//This method allows someone in the GUI to move this robot's position arbitrarily (e.g. with arrow keys)
		//WARNING: verify that you're not going through walls gui-side. Also this method will be deprecated next iteration.
		if (getKnown().hasDirtAt(pos) && !toExplore.hasDirtAt(pos)){
			getKnown().clean(pos);
		}
		
		this.setPos(p);
		gatherData();
	}
	
	private void gatherData(){
		//gather sensor data
		//detect walls surrounding robot
		ArrayList<Point> visible = new ArrayList<Point>();
		visible.add(new Point(pos.x,pos.y));
		int[] surrounded = toExplore.wallsSurrounding(pos);
		int wall=0;
		
		for(int w = 0; w < 4; w++) {
			wall = surrounded[w];
			
			if (wall==Room.WALL_NONE || wall==Room.WALL_DOOROPEN){
				//n=0, w=1,e=2,s=3
				
				switch (w) { //TODO clean this up. Basically it checks if we're in the bounds
				case Room.DIR_N: 					
					visible.add(new Point(Math.min(Math.max(0,pos.x),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y-1),toExplore.getHeight()-1)));
					logger.getLOGGER().fine("North Sensor detecting open space");
					break;
				case Room.DIR_W: 					
					visible.add(new Point(Math.min(Math.max(0,pos.x-1),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y),toExplore.getHeight()-1)));
					logger.getLOGGER().fine("West Sensor detecting open space");
					break;
				case Room.DIR_E: 					
					visible.add(new Point(Math.min(Math.max(0,pos.x+1),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y),toExplore.getHeight()-1)));
					logger.getLOGGER().fine("East Sensor detecting open space");
					break;
				case Room.DIR_S: 					
					visible.add(new Point(Math.min(Math.max(0,pos.x),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y+1),toExplore.getHeight()-1)));
					logger.getLOGGER().fine("South Sensor detecting open space");
					break;
				}
			}
			else if(wall == Room.WALL_DOORCLOSED || wall == Room.WALL_WALL)
				logger.getLOGGER().log(Level.WARNING, "Robot sensor detecting a wall");
			
			getKnown().addWall(pos, w, wall);
		}
		//detect obstacles surrounding robot
		//detect carpet types surrounding robot
		
		for(Point p:visible) {
			
			if (getKnown().floorIsPlaceholder(p)) {
				
				if (toExplore.floorIsObstacle(p)==0) {
					getKnown().addTile(p, new Tile(toExplore.getFloorTypeAt(p), 1, toExplore.floorIsObstacle(p))); //set dirt to 1 unless we've explored it
				} else{
					getKnown().addTile(p, new Tile(toExplore.getFloorTypeAt(p), 0, toExplore.floorIsObstacle(p))); //set dirt to 0
				}
				
			}
			//TODO note we still have to check toExplore at that point when we go to clean
		}
	}
	
	private static int distance(Point a, Point b){
		//simple distance from A to B
		return (Math.abs(b.x-a.x)+Math.abs(b.y-a.y)); 
	}
	
	public Point getObjective(){
		//Returns the best point to travel to. Usually the nearest uncleaned tile
		//TODO prioritize finishing a room before moving on
		//If we have to, go to the charging station. Otherwise, find the nearest uncleaned block. 
		//For now, no charging required (and full dirt capacity) TODO
		int bestDistance=Integer.MAX_VALUE;
		Point bestTarget=pos; //TODO replace with charging station if necessary
		int tempDistance;
		Point tempPoint;
		
		for(int y = 0; y < toExplore.getHeight(); y++) {
			
			for(int x = 0; x < toExplore.getWidth(); x++) {
				
				tempPoint = new Point(x,y);
				if(getKnown().hasDirtAt(tempPoint)) {
					//tempDistance = distance(tempPoint, pos);
					tempDistance = getPath(pos,tempPoint).size();
					
					if (bestDistance>tempDistance) {
						bestDistance = tempDistance;
						bestTarget = tempPoint;
					}
				}
			}
		}
		
		return bestTarget;
	}	
	
	private boolean canMove(Point from,int direction){
		//checks if the motion from 'from' in 'direction' is obstructed by an obstacle or a wall. 
		//TODO handle out of bounds (normally walls bound the room but if the room file is mildly messed up, handle it)
		if(getKnown().wallsSurrounding(from)[direction]!=Room.WALL_NONE && getKnown().wallsSurrounding(from)[direction]!=Room.WALL_DOOROPEN) {
			return false;
		} 
		else {
			Point next = new Point(from.x,from.y);
			
			if (direction==Room.DIR_N){
				next.y-=1;
			} 
			else if (direction==Room.DIR_S) {
				next.y+=1;
			} 
			else if (direction==Room.DIR_W){
				next.x-=1;
			} 
			else {
				next.x+=1;
			}
			
			if(getKnown().floorIsObstacle(next)!=0) return false;
		}
		
		return true;
	}
	
	private ArrayList<Point> adjacent(Point p){
		//gets the points that can be moved to directly from p
		ArrayList<Point> adj = new ArrayList<Point>();
		
		if(p.x>0 && canMove(p,Room.DIR_W)) 
			adj.add(new Point(p.x-1,p.y));
		if(p.x<getKnown().getWidth()-1 && canMove(p,Room.DIR_E)) 
			adj.add(new Point(p.x+1,p.y));
		if(p.y>0 && canMove(p,Room.DIR_N)) 
			adj.add(new Point(p.x,p.y-1));
		if(p.y<getKnown().getHeight()-1 && canMove(p,Room.DIR_S)) 
			adj.add(new Point(p.x,p.y+1));
		
		return adj;
	}
	
	public List<Point> getPath(Point from, Point to){
		//pathfinding. returns a list of points, in order, that gets from 'from' to 'to. 
		//TODO handle fuel and movement costs in travel cost
		if(from.equals(to)) 
			return new ArrayList<Point>();
		if(adjacent(from).contains(to)) {
			ArrayList<Point> ret = new ArrayList<Point>();
			ret.add(to);
			return ret;
		}

		ArrayList<Point> path = new ArrayList<Point>();		
		ArrayList<Point> checked = new ArrayList<Point>();
		ArrayList<Point> toCheck = new ArrayList<Point>();
		toCheck.add(from);
		
		HashMap<Point, Point> cameFrom = new HashMap<Point, Point>();		
		HashMap<Point, Integer> travelCost = new HashMap<Point, Integer>();
		travelCost.put(from, 0);
		
		HashMap<Point, Integer> heurCost = new HashMap<Point, Integer>(); //TODO heuristic that includes fuel cost
		heurCost.put(from, distance(from,to));
		
		Point temp;
		int bestTemp=Integer.MAX_VALUE;
		Point bestPTemp = new Point(0,0);
		
		while (toCheck.size()>0) {
			
			bestTemp=Integer.MAX_VALUE;
			
			for(Point t: toCheck){
				
				if(travelCost.get(t)<bestTemp){
					bestTemp=travelCost.get(t);
					bestPTemp=t;
				}
			}
			
			temp = bestPTemp;
			checked.add(temp);
			toCheck.remove(temp);
			int tCost;
			
			for(Point a:adjacent(temp)){
				
				if(checked.contains(a)) {
					continue; //already checked, skip
				}
				
				tCost = travelCost.get(temp)+1;
				if(!toCheck.contains(a)) 
					toCheck.add(a);
				else if (tCost>travelCost.get(a))
					continue;
				
				cameFrom.put(a, temp);
				travelCost.put(a, tCost);
				heurCost.put(a, travelCost.get(a)+distance(a,to));
			}				
		}
		
		temp=to;
		
		while(!cameFrom.get(temp).equals(from)) {
			path.add(temp);
			temp = cameFrom.get(temp);
		}
		
		path.add(temp);
		Collections.reverse(path);
		return path;
	}
	
	public Point getPosition(){
		return new Point(pos.x,pos.y);
	}

}