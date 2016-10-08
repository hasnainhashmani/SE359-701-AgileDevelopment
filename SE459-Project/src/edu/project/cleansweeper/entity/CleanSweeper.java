package edu.project.cleansweeper.entity;

import edu.project.interfaces.Movable;

public class CleanSweeper implements Movable{
	
	private Movable movable;
	
//	private static final Sensor FrontSensor;
//	private static final Sensor BackSensor;
//	private static final Sensor leftSensor;
//	private static final Sensor RightSensor;
	
	public CleanSweeper() {
		movable = new MovableImpl();
	}

	@Override
	public void move(Point newLocation) {
		movable.move(newLocation);
	}

}
