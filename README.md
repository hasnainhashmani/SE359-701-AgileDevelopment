# Repository for SE 359, Agile Development Group 7 (CleanSweep)

There are three packages:
com.groupseven.robot is the Control System. Its methods (for gathering sensor data and moving) can be accessed directly, mainly through the step() and gatherdata() function. Given accurate sensor data (from a SensorSim or, in a real life environment, the hardware sensors), it should function normally, exploring and cleaning the entire floor. 

It features navigation, dirt detection and cleaning, and power management, as well as activity logging. 

com.groupseven.sensorsim is the Sensor Simulator. It takes in input files representing real-world environments and provides sensor data to the Control System (in a limited way, the Robot does not have full access to the virtual room unless it moves and explores within it). Some room layout files have been provided, but to make your own, consult RoomParser documentation for technical instructions. 

com.groupseven.cleansweeplib has code that is shared between both the Control System and Sensor Data. The most important part of this is the Room class (with subclasses), which acts as a virtual representation of a real room. The Sensor Simulator uses a Room to store all the data from the layout file, and the Control System creates an empty Room and populates it with data it receives from the Sensor Simulator. 

Note: To run Application with Maven first run mvn clean, then run mvn compile and lastly run mvn exec:java.
Note: To run ant build file through maven use maven command "mvn package". 
Note: to run Cobertura through maven use maven command "mvn site" then open target\site\index.html. 
Eclipse also has a great plugin EclEmma that handles code coverage

