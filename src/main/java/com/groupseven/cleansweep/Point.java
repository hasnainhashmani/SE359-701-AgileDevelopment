package com.groupseven.cleansweep;

import java.awt.geom.Point2D;

public class Point extends Point2D implements Comparable<Point>{
	
	int x;
	int y;
	Point startingPoint;
	
	double distance;

    public Point(int x, int y, Point startingPoint) {
        this.x = x;
        this.y = y;
        this.startingPoint = startingPoint;
        this.distance = Math.hypot(x - startingPoint.x, y - startingPoint.y);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
    public int compareTo(Point other) {
        return java.lang.Double.valueOf(other.distance).compareTo(distance);
    }

	@Override
	public void setLocation(double x, double y) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		 return getClass().getName() + "[x=" + x + ",y=" + y + "]";
	}
	
}
