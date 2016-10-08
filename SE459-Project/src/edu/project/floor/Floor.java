package edu.project.floor;

public class Floor {
	
	private int numOfRooms;
	
	public Floor(int rooms) {
		this.setNumOfRooms(rooms);		
	}

	/**
	 * @return the numOfRooms
	 */
	public int getNumOfRooms() {
		return numOfRooms;
	}

	/**
	 * @param numOfRooms the numOfRooms to set
	 */
	public void setNumOfRooms(int numOfRooms) {
		this.numOfRooms = numOfRooms;
	}
}
