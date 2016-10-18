package cleansweeptests;
 
import org.junit.*;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import application.Tile;


public class TileTest {
	//simple test to verify tile functions work
	//this one might not be necessary eventually but it also verifies that JUnit works on my machine
	
	@Test
	public void testTile(){
		Tile t = new Tile(0,3,0, null);
		assertEquals(t.getCarpetType(), 0);
	}
	
	//TODO more tests
}