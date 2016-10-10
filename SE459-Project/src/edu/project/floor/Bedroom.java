package edu.project.floor;

import edu.project.interfaces.Room;

public class Bedroom implements Room{
	
	private int length;
    private int width;
    private Door entranceDoor;
    private Door closetDoor;
	private int[][] grid;
	
	public Bedroom(int length, int width) {
		this.setLength(length);
		this.setWidth(width);
		this.setEntranceDoorOpen(entranceDoor);
		this.setClosetDoorOpen(closetDoor);
		grid = new int[length][width];
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public double getArea() {
		return (double) this.getLength() * this.getWidth();
	}

	/**
	 * @return true if entranceDoor is open, false otherwise
	 */
	public boolean isEntranceDoorOpen() {
		
		if(this.entranceDoor == Door.open) {
			System.out.println("Entrance Doors to room are open");
			return true;
		}
		else {
			System.out.println("Entrance Doors to room are close");
			return false;
		}
	}

	/**
	 * @param entranceDoor the entranceDoor to set
	 */
	public void setEntranceDoorOpen(Door entranceDoor) {
		this.entranceDoor = entranceDoor;
	}
	
	/**
	 * @return true if closetDoor is open, false otherwise
	 */
	public boolean isClosetDoorOpen() {
		
		if(this.closetDoor == Door.open) {
			System.out.println("Closet Doors in room are open");
			return true;
		}
		else {
			System.out.println("Closet Doors in room are close");
			return false;
		}
	}

	/**
	 * @param door the ClosetDoor to set
	 */
	public void setClosetDoorOpen(Door door) {
		this.closetDoor = door;
	}

	/**
	 * @return the grid
	 */
	public int[][] getGrid() {
		return grid;
	}

}
