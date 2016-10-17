package cleansweep;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RobotDummy {
	
	private Room known;
	private Room toExplore;
	
	private Point pos;
	
	public RobotDummy(Room toExplore){
		pos=toExplore.getStartingPosition();
		known = new Room(toExplore.getWidth(), toExplore.getHeight());
		known.setStartingPos(pos);
		this.toExplore=toExplore;
	}
	
	public void step(){
		//move one step in the simulation
		//This should happen whenever someone in the GUI requests it 
		//(e.g. press spacebar, click a button that says next, or once a second if we 'play' the simulation)
		//eventually we can undo this and stuff
		gatherData();
		Point p = getObjective();
		Point next = getPath(pos,p).get(0);
		pos.x=next.x; //update stored position
		pos.y=next.y;
		System.out.print(known);
	}
	
	public void forceMove(Point p){
		//This method allows someone in the GUI to move this robot's position arbitrarily (e.g. with arrow keys)
		gatherData();
		pos=p;
		gatherData();
	}
	
	private void gatherData(){
		//gather sensor data
		//detect walls surrounding robot
		ArrayList<Point> visible = new ArrayList<Point>();
		visible.add(new Point(pos.x,pos.y));
		int[] surrounded = toExplore.wallsSurrounding(pos);
		int wall=0;
		for(int w =0;w<4;w++){
			wall = surrounded[w];
			if (wall==Room.WALL_NONE || wall==Room.WALL_DOOROPEN){
				//n=0, w=1,e=2,s=3
				switch (w){
				case 0: visible.add(new Point(pos.x,pos.y-1));
				case 1: visible.add(new Point(pos.x-1,pos.y));
				case 2: visible.add(new Point(pos.x+1,pos.y));
				case 3: visible.add(new Point(pos.x,pos.y+1));
				}
			}
			known.addWall(pos, w, wall);
		}
		//detect obstacles surrounding robot
		//detect carpet types surrounding robot
		
		for(Point p:visible){
			if (known.floorIsPlaceholder(p)){
			known.addTile(p, new Tile(toExplore.getFloorTypeAt(p), 1, toExplore.floorIsObstacle(p))); //set dirt to 1 unless we've explored it
			}
			//TODO note we still have to check toExplore at that point when we go to clean
		}
	}
	
	public Point getObjective(){
		//If we have to, go to the charging station. Otherwise, find the nearest uncleaned block. 
		//For now, no charging required (and full dirt capacity)
		return new Point(0,0);
	}	
	
	public List<Point> getPath(Point from, Point to){
		//pathfinding
		//returns a list of points, in order, that will get us from the start to the end
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(new Point(pos.x+1,pos.y+1)); //TODO this is a debug option
		return path;
	}
	
	public Point getPosition(){return new Point(pos.x,pos.y);}
	
}
