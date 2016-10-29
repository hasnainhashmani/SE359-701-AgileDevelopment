package com.groupseven.cleansweep;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.groupseven.cleansweep.Robot;
import com.groupseven.cleansweep.Room;
import com.groupseven.cleansweep.RoomParser;
import com.groupseven.cleansweep.Tile;

public class RobotTest {
	@Test
	public void sampleFloorTest() throws IOException {
		String s = "rooms/samplefloor.bmp";
		Room room = RoomParser.parseFile(s);
		Robot robot = new Robot(room);
		Point start = robot.getPosition();
		robot.step();
		robot.step();
		robot.step();// from [2,5] to [1,5]
		Point end = robot.getPosition();
		robot.getPath(start, end);	
	}
	@Test
	public void test(){
		int height = 10;
		int width = 10;
		int carpetType = 1;
		int dirt = 1;
		int obstacleType =1;
		Tile t = new Tile(carpetType,dirt,obstacleType);
		Room toExplore = new Room(height, width);
		List<Point> toExplorepoints = new ArrayList<Point>();
		Point startPoint = new Point(0,0);
		toExplorepoints.add(startPoint);
		Point temp1 = new Point(0,1);
		toExplorepoints.add(new Point(temp1));
		Point temp2 = new Point(1,0);
		toExplorepoints.add(temp2);
		Point temp3 = new Point(1,1);
		for(Point p: toExplorepoints){
			toExplore.addTile(p, t);
		}
		toExplore.addTile(temp3, new Tile(1,0,1));
		//add all direction walls on temp1
		toExplore.addWall(temp1, 3, 1);
		toExplore.addWall(temp1, 1, 1);
		toExplore.addWall(temp1, 2, 1);
		toExplore.addWall(temp1, 0, 1);
		//add wall east
		toExplore.addWall(temp2, 2, 2);
		Robot newRobot = new Robot(toExplore);
		assertTrue(toExplore.hasDirtAt(startPoint));
		newRobot.step();//since toExplore has dirt, clean instead of moving
		assertFalse(toExplore.hasDirtAt(startPoint)); // no dirt after step
		newRobot.forceMove(temp1); // forcemove to temp1
		assertFalse(newRobot.canMove(temp1, 1));	//since all wall-wall around temp1, can not move temp1 to next point
		newRobot.forceMove(temp2);
		newRobot.forceMove(temp3);
	}
}