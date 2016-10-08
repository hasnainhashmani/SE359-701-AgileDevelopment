package edu.project.interfaces;

import edu.project.floor.Door;

public interface Room {
		
	public void setLength(int length);
	public int getLength();
	public void setWidth(int width);
	public int getWidth();
	public double getArea();
	public void setEntranceDoorOpen(Door door);
	public boolean isEntranceDoorOpen();

}
