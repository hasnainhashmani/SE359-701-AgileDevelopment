package ds;

import java.awt.Point;
import java.util.Observable;

import javafx.scene.image.ImageView;

public class Robot extends Observable{
	Point currentPosition;
	int dimension;
	ImageView robotImg;
	boolean[][] grid;
	
	public Robot(GridMap gridpane){
		this.currentPosition = gridpane.currentPosition;
		this.dimension = gridpane.dimension;
		grid = gridpane.getMap();
	}
	
	public Point getLocation(){
		return currentPosition;
	}

	public void goWest() {
		// TODO Auto-generated method stub
	////Not to exceed boundary or go over objects
		if((currentPosition.x!=0)&&(grid[currentPosition.y][currentPosition.x-1]!=true)){
			currentPosition.x--;
			setChanged();
			notifyObservers(currentPosition); //Notifying the observers
		}
	}

	public void goEast() {
		// TODO Auto-generated method stub
		////Not to exceed boundary or go over objects
		if((currentPosition.x!=dimension-1)&&(grid[currentPosition.y][currentPosition.x+1]!=true)){
			currentPosition.x++;
			setChanged();
			notifyObservers(currentPosition); //Notifying the observers
		}
	}

	public void goSouth() {
		// TODO Auto-generated method stub
	////Not to exceed boundary or go over objects
		if((currentPosition.y!=dimension-1)&&(grid[currentPosition.y+1][currentPosition.x]!=true)){
			currentPosition.y++;
			setChanged();
			notifyObservers(currentPosition); //Notifying the observers
		}
	}

	public void goNorth() {
		// TODO Auto-generated method stub
	////Not to exceed boundary or go over objects
		if((currentPosition.y!=0)&&(grid[currentPosition.y-1][currentPosition.x]!=true)){
			currentPosition.y--;
			setChanged();
			notifyObservers(currentPosition); //Notifying the observers
		}
	}
	
}
