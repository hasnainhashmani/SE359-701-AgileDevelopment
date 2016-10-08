package edu.project.cleansweeper.entity;

import edu.project.interfaces.Movable;

public class MovableImpl implements Movable{
	
	private Point currentLocation;
	
	public MovableImpl() {
		
	}

	@Override
	public void move(Point p) {
				
	}

	/**
	 * @return the currentLocation
	 */
	public Point getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * @param currentLocation the currentLocation to set
	 */
	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}

}
