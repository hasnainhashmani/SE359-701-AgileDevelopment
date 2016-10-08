package edu.project.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import edu.project.floor.Bedroom;
import edu.project.floor.Door;
import edu.project.interfaces.Room;

public class RoomTest {
	
	Room room;
	
	@Before
	public void beforeEach(){
		room = new Bedroom(8, 8);
		room.setEntranceDoorOpen(Door.open);
	}
	
	@After
	public void afterEach(){
		room = null;
	}
	
	@Test
	public void testRoomArea() {
		assertTrue(room.getArea() == 64);
	}
	
	@Test
	public void testRoomLength() {
		assertTrue(room.getLength() == 8);
	}
	
	@Test
	public void testRoomWidth() {
		assertTrue(room.getWidth() == 8);
	}
	
	@Test
	public void testOpenEntranceDoors() {		
		assertTrue(room.isEntranceDoorOpen());
	}
	
	@Test
	public void testClosedEntranceDoors() {
		room.setEntranceDoorOpen(Door.close);
		assertFalse(room.isEntranceDoorOpen());
	}
	
	@Test
	public void testClosedClosetDoors() {		
		assertFalse(((Bedroom) room).isClosetDoorOpen());
	}
	
	@Test
	public void testOpenClosetDoors() {
		Bedroom bedroom = (Bedroom) room;
		bedroom.setClosetDoorOpen(Door.open);		
		assertTrue(bedroom.isClosetDoorOpen());
	}

}
