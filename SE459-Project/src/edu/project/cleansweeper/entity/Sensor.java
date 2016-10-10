package edu.project.cleansweeper.entity;

public class Sensor {
	
	private static boolean objectDetect;
	
	public Sensor(){
		Sensor.setObjectDetect(false);
	}

	/**
	 * @return the objectDetect
	 */
	public static boolean isObjectDetect() {
		return objectDetect;
	}

	/**
	 * @param objectDetect the objectDetect to set
	 */
	public static void setObjectDetect(boolean objectDetect) {
		Sensor.objectDetect = objectDetect;
	}

}
