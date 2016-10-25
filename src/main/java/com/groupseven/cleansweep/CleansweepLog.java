package com.groupseven.cleansweep;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CleansweepLog {
	
	private static final Logger LOGGER = Logger.getLogger(CleansweepLog.class.getName());
	private FileHandler handler;
	
	public CleansweepLog() {
		String fileName = "log.txt";
		File file = new File(fileName);
		
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			
			LOGGER.setLevel(Level.ALL);
			handler = new FileHandler(fileName, true);
			ConsoleHandler consoleHandler = new ConsoleHandler();
			handler.setLevel(Level.ALL);
			LOGGER.addHandler(handler);
			LOGGER.addHandler(consoleHandler);
			SimpleFormatter sf = new SimpleFormatter();			
			handler.setFormatter(sf);			
			LOGGER.setUseParentHandlers(false);
		} 
		catch (SecurityException e) {
			LOGGER.log(Level.SEVERE,"Security Exception was thrown", e);
		} 
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "ERROR: Creating a new file!", e);
		}
	}

	/**
	 * @return the lOGGER
	 */
	public Logger getLOGGER() {
		return LOGGER;
	}
	
	public void setLOGGERLevel(Level level) {
		LOGGER.setLevel(level);
	}

}
