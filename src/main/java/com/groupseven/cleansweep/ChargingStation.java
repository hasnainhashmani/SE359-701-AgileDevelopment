package com.groupseven.cleansweep;


public class ChargingStation {
	
	private final double charge = 100.00;
	private Tile stationLocation;
	
	public ChargingStation(){		
		this.setStationLocation(stationLocation);
	}
	
	public void recharge(Robot robot) {
		robot.rechargePowerSupply(charge);
	}

	public void setStationLocation(Tile t) {
		stationLocation = t;
	}
	
	public Tile getStation() {
		return stationLocation;
	}

}
