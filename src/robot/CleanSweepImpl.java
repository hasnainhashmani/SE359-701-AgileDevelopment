package Robot;

import java.awt.Point;

public interface CleanSweepImpl {
		//check if hasDirt
		boolean hasDirt(Point x);
		//move func
		Direction move();
		//clean func
		void clean(Point x);
		//check where is a obstacle 
		Direction obstacleBlocking(Point x);
}
