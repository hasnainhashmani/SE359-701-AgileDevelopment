package com.groupseven.cleansweep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;


public class Robot {
	
	private Room known; //TODO methods to access this? Right now GUI directly accesses it, possible unsafe
	private Room toExplore;	
	private Point pos;
	
	// Logger also create a log file (log.txt) in project workspace for record keeping 
	// Config and Fine sensor data print to log.txt 
	CleansweepLog logger; 
	
	// powerSupply used to return power amount
	private double powerSupply;
	
	// starting power
	private double power = 100.00;
	
	// power limit to start searching for charging station
	private final int powerlimit = 20;
	
	// pathRecord is used to record the path of cleansweep
	private ArrayList<Point> pathRecord;
	
	// record of charging stations
	private Collection<Point> chargingStations;
	
	//
	private int dirtCapacity = 0;
	private final int dirtCapacityLimit = 20;
	
	public Robot(Room toExplore){
		this.setPowerSupply(powerSupply);
		this.setPos(toExplore.getStartingPosition());
		setKnown(new Room(toExplore.getWidth(), toExplore.getHeight()));
		getKnown().setStartingPos(pos);
		this.toExplore=toExplore;
		logger = new CleansweepLog();
		logger.setLevel(Level.ALL);
		logger.config("Robot Created, Starting position: [" + this.getPos().x + ", " + this.getPos().y + "]");
		pathRecord = new ArrayList<Point>();
		this.setPowerSupply(power);
		this.setDirtCapacity(dirtCapacity);
		chargingStations = new ArrayList<Point>();		
	}
	
	/**
	 * @return the known room
	 */
	public Room getKnown() {
		return known;
	}

	/**
	 * @param known the room to set
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
	
	public double getPowerSupply() {
		return powerSupply;
	}
	
	public void setPowerSupply(double power){
		powerSupply = power;
	}
	
	public void addToPathRecord(Point p) {
		pathRecord.add(p);
	}
	
	public void displayPathRecord() {
		if(this.pathRecord.isEmpty()) {
			System.out.println("No path recorded yet!");
			logger.log(Level.INFO, "No path recorded yet!");
		}		
	}
	
	/**
	 * Used to calculate power from previous to current tile point
	 * @param i is previous tile point
	 * @param j is current tile point
	 */
	private void powerConsumption(double i, double j) {
		i++;
		j++;
		
		if(this.getPowerSupply() <= 1) {
			throw new UnsupportedOperationException("Cleansweep has no power!!");
		}
		
		double averageUnit = (i + j)/2;
		
		this.setPowerSupply(this.getPowerSupply() - averageUnit);		
	}
	
	/**
	 * Used to re-charge robot	
	 * @param ps is power amount
	 */
	public void rechargePowerSupply(double ps){
		this.setPowerSupply(ps);
	}

	public Collection<Point> getChargingStations() {
		return chargingStations;
	}

	public void addToChargingStations(Point p) {
		this.chargingStations.add(p);
	}
	
	public void setDirtCapacity(int dc) {
		this.dirtCapacity = dc;
	}
	
	public int getDirtCapacity() {
		return dirtCapacity;
	}
	
	private void dirtConsumption() {
		if(this.getDirtCapacity() == this.dirtCapacityLimit)
			logger.log(Level.WARNING, "Dirt Capcity Full!!");
		else
			this.setDirtCapacity(this.getDirtCapacity() + 1);
	}
	
	public boolean isDirtCapacityFull() {
		return this.getDirtCapacity() == dirtCapacityLimit;
	}
	
	public void emptyCleansweep() {
		this.setDirtCapacity(0);
	}
	
	private void capacityCheck() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		String ans;
		try {
			if(isDirtCapacityFull()) {
				
				do {
					System.out.print("Robot capcity full! Empty robot Yes or No: ");					
					ans = input.next();		
					System.out.println();
				}
				while(!ans.equalsIgnoreCase("yes"));
				
				this.setDirtCapacity(0);			
				System.out.println("Press Step to continue with robot!");
			}
		}
		catch(Throwable e) {
			this.capacityCheck();
		}
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
			this.getKnown().clean(this.getPos());
			logger.log(Level.INFO, "Robot Cleaning postion: [" + this.getPos().x + ", " + this.getPos().y + "]");
			
			if(!isDirtCapacityFull()){
				this.dirtConsumption();
				logger.log(Level.INFO, "Robot has dirt capacity of " + (dirtCapacityLimit - this.getDirtCapacity())  + " remaining!");
			}
		}
	
		gatherData(); //check all the sensors, add room data to stored data
		Point p = getObjective(); //find the next place to clean (or, the charging station if we're out of fuel/dirt capacity)
		
		// If robot power low and charging station found, re-charge robot
		if(this.getPowerSupply() < powerlimit && this.getPos().equals(p)) {

			this.known.getChargingStation(p).recharge(this);
			System.out.println("Robot recharging");
			logger.log(Level.INFO, "Robot recharging!!");
			System.out.println("Robot fully recharged");
			logger.log(Level.INFO, "Robot fully recharged!!");
			
			this.capacityCheck();
			
		}
		// If robot power is high but dirt capacity full and charging station found 
		else if(this.getPowerSupply() > powerlimit && this.isDirtCapacityFull() && this.getPos().equals(p)) {
			// Ask to empty dirt so robot can continue.
			this.capacityCheck();
		}
		
		
		//record the path
		addToPathRecord(p);		
		
		if(this.getPos().equals(p)) {
			
			return; //If there's nowhere to go, we're done
		}
		
		Point next = getPath(this.getPos(), p).get(0);     //Plot a path to the next objective
		logger.log(Level.INFO, "position: [" + this.getPos().x +", "+ this.getPos().y+"] clean!!");
		logger.log(Level.INFO, "Robot moving from position: [" + this.getPos().x +", "+ this.getPos().y+"] to position: [" + next.x + ", " + next.y + "]");
		powerConsumption(this.getKnown().getFloorTypeAt(this.getPos()), this.getKnown().getFloorTypeAt(next));
		logger.log(Level.INFO, "Robot power supply has " + this.getPowerSupply() + " units remaining");
		
		// if power is 20 units or less log re-charge warning
		if(this.getPowerSupply() <= powerlimit) {
			logger.log(Level.WARNING, "Cleansweep is running low on power, need to be recharged!");
			logger.log(Level.WARNING, "Searching for charging station at position: [" + p.x + ", " + p.y +"]");
		}
		
		this.setPos(new Point(next.x, next.y)); //update stored position
		gatherData(); 
		//System.out.print(known); //This is handled by gui
	}
	
	public void forceMove(Point p){
		//This method allows someone in the GUI to move this robot's position arbitrarily (e.g. with arrow keys)
		//WARNING: verify that you're not going through walls gui-side. Also this method might be deprecated next iteration.
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
			
			if (wall==Wall.WALL_NONE || wall==Wall.DOOR_OPEN){
				//n=0, w=1,e=2,s=3
				
				switch (w) { //TODO clean this up. Basically it checks if we're in the bounds
				case Room.DIR_N:
					Point np_n = new Point(Math.min(Math.max(0,pos.x),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y-1),toExplore.getHeight()-1));
					visible.add(np_n);
					logger.fine("North Sensor detecting open space");
					if(known.chargingStationExist(np_n)){
						if(!this.getChargingStations().contains(np_n))
							this.addToChargingStations(np_n);
						
						logger.log(Level.INFO, "Sensor detecting charging station at position " + np_n.getX() + " " +np_n.getY());
					}
					break;
				case Room.DIR_W: 
					Point np_w = new Point(Math.min(Math.max(0,pos.x-1),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y),toExplore.getHeight()-1));
					visible.add(np_w);
					logger.fine("West Sensor detecting open space");
					if(known.chargingStationExist(np_w)){
						if(!this.getChargingStations().contains(np_w))
							this.addToChargingStations(np_w);
						
						logger.log(Level.INFO, "Sensor detecting charging station at position " + np_w.getX() + " " +np_w.getY());
					}
					break;
				case Room.DIR_E: 	
					Point np_e = new Point(Math.min(Math.max(0,pos.x+1),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y),toExplore.getHeight()-1));
					visible.add(np_e);
					logger.fine("East Sensor detecting open space");
					if(known.chargingStationExist(np_e)){
						if(!this.getChargingStations().contains(np_e))
							this.addToChargingStations(np_e);
						
						logger.log(Level.INFO, "Sensor detecting charging station at position " + np_e.getX() + " " +np_e.getY());
					}
					break;
				case Room.DIR_S: 
					Point np_s = new Point(Math.min(Math.max(0,pos.x),toExplore.getWidth()-1),Math.min(Math.max(0,pos.y+1),toExplore.getHeight()-1));
					visible.add(np_s);
					logger.fine("South Sensor detecting open space");
					if(known.chargingStationExist(np_s)){
						if(!this.getChargingStations().contains(np_s))
							this.addToChargingStations(np_s);
						
						logger.log(Level.INFO, "Sensor detecting charging station at position " + np_s.getX() + " " +np_s.getY());
					}
					break;
				}
			}
			else if(wall == Wall.DOOR_CLOSED || wall == Wall.WALL_WALL)
				logger.log(Level.WARNING, "Robot sensor detecting a wall");
			
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
	
	/**
	 * getObjective updated
	 * if robot power is 20 units or less 
	 * search for nearest charging station
	 * @return nearest charging station point
	 */
	public Point getObjective(){
		
		//Returns the best point to travel to. Usually the nearest uncleaned tile
		//TODO prioritize finishing a room before moving on
		//If we have to, go to the charging station. Otherwise, find the nearest uncleaned block. 
		//For now, no charging required (and full dirt capacity) TODO
		int bestDistance=Integer.MAX_VALUE;
		Point bestTarget;
		int tempDistance;
		Point tempPoint;
		
		// determine whether robot have enough power and dirt capacity space to continue cleaning path
		if(!(this.getPowerSupply() <= powerlimit)) {
			
			// if robot power is greater than power limit but dirt capacity full go to charging station
			if(this.isDirtCapacityFull()) {
				bestTarget = pos; //TODO replace with charging station if necessary
				ArrayList<Point> stationsHolder = new ArrayList<Point>(this.getChargingStations());
				
				for(int i = 0; i < this.getChargingStations().size(); i++) {
					stationsHolder.set(i, new Point(stationsHolder.get(i).x,  stationsHolder.get(i).y, this.getPos()));
				}
				
				tempPoint = new ArrayList<Point>(this.getClosestChargingStation(stationsHolder, 1)).get(0);
				
				// Path to the nearest charging station
				for(int y = 0; y < toExplore.getHeight(); y++) {
					
					for(int x = 0; x < toExplore.getWidth(); x++) {				
						
						if(getPath(pos, tempPoint).size()>0) {
							//tempDistance = distance(tempPoint, pos);
							tempDistance = getPath(pos,tempPoint).size(); //TODO replace with fuel
							
							if (bestDistance>tempDistance) {
								bestDistance = tempDistance;
								bestTarget = tempPoint;
							}
						}
					}
				}
				
			}
			// if robot have enough power and dirt capacity space to continue cleaning path
			else{
			
				bestTarget=pos; //TODO replace with charging station if necessary
				// Path to the nearest unclean tile
				for(int y = 0; y < toExplore.getHeight(); y++) {
					
					for(int x = 0; x < toExplore.getWidth(); x++) {
						
						tempPoint = new Point(x,y);
						if(getKnown().hasDirtAt(tempPoint) && getPath(pos,tempPoint).size()>0) {
							//tempDistance = distance(tempPoint, pos);
							tempDistance = getPath(pos,tempPoint).size(); //TODO replace with fuel
							
							if (bestDistance>tempDistance) {
								bestDistance = tempDistance;
								bestTarget = tempPoint;
							}
						}
					}
				}	
			}
					
		}

		else {
			bestTarget = pos; //TODO replace with charging station if necessary
			ArrayList<Point> stationsHolder = new ArrayList<Point>(this.getChargingStations());
			
			for(int i = 0; i < this.getChargingStations().size(); i++) {
				stationsHolder.set(i, new Point(stationsHolder.get(i).x,  stationsHolder.get(i).y, this.getPos()));
			}
			
			tempPoint = new ArrayList<Point>(this.getClosestChargingStation(stationsHolder, 1)).get(0);
			
			// Path to the nearest charging station
			for(int y = 0; y < toExplore.getHeight(); y++) {
				
				for(int x = 0; x < toExplore.getWidth(); x++) {				
					
					if(getPath(pos, tempPoint).size()>0) {
						//tempDistance = distance(tempPoint, pos);
						tempDistance = getPath(pos,tempPoint).size(); //TODO replace with fuel
						
						if (bestDistance>tempDistance) {
							bestDistance = tempDistance;
							bestTarget = tempPoint;
						}
					}
				}
			}	
	
		}
		
		return bestTarget;
	}	
	
	public boolean canMove(Point from,int direction){
		//checks if the motion from 'from' in 'direction' is obstructed by an obstacle or a wall. 
		//TODO handle out of bounds (normally walls bound the room but if the room file is mildly messed up, handle it)
		if(getKnown().wallsSurrounding(from)[direction]!=Wall.WALL_NONE && getKnown().wallsSurrounding(from)[direction]!=Wall.DOOR_OPEN) {
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
				
				tCost = travelCost.get(temp)+1; //todo fuel cost
				if(!toCheck.contains(a)) 
					toCheck.add(a);
				else if (tCost>travelCost.get(a))
					continue;
				
				cameFrom.put(a, temp);
				travelCost.put(a, tCost);
				heurCost.put(a, travelCost.get(a)+distance(a,to));
			}				
		}
		
		if(checked.indexOf(to)==-1) return new ArrayList<Point>();
		
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
	
	/**
	 * 
	 * @param points
	 * @param n
	 * @return the closest charging station point
	 */
    public Collection<Point> getClosestChargingStation(Collection<Point> points, int n) {
        PriorityQueue<Point> queue = new PriorityQueue<Point>(n);

        for (Point point : points) {
            if (queue.size() < n) {
                queue.offer(point);
            } else {
                if (queue.peek().compareTo(point) < 0) {
                    queue.poll();
                    queue.offer(point);
                }
            }
        }

        return queue;
    }

}


