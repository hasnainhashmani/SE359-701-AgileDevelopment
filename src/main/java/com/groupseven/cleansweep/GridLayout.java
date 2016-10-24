package main.java.com.groupseven.cleansweep;

import java.awt.Point;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GridLayout extends Application{ 
	
	int scale = 40;
	String filename = "rooms/samplefloor.bmp"; //TODO make this command line option or something
	//Also try "pathingStressTest.bmp"
	ImageView robotImg;
	Pane root;
	Scene scene;
	Room room;
	Robot robot;

	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage gridStage) throws Exception {
		// TODO Auto-generated method stub
		//Random number generator for objects
		
		root = new AnchorPane();
		
		
		room = RoomParser.parseFile(filename);
		
		//drawMap();
		robot = new Robot(room);
		//Loading and placing our robot
		
		drawMap();
		
		//Button for resetting the game
		Button btn = new Button();
		btn.setText("Step");
		btn.setScaleX(1.5);
		btn.setScaleY(1.5);
		AnchorPane.setBottomAnchor(btn, 25.0);
		AnchorPane.setRightAnchor(btn, 290.0);
		btn.setOnAction(new EventHandler<ActionEvent>(){
			 
			public void handle(ActionEvent event) {
				robot.step();
				robotImg.setX(robot.getPosition().x*scale); //Moving robot image according to command
				robotImg.setY(robot.getPosition().y*scale); //
				drawMap();
			}
			
		});
		root.getChildren().add(btn);
		scene = new Scene(root, 625,700);
		gridStage.setScene(scene);
		gridStage.show();
		startGrid();
	}
	
	
	private void loadrobotImage() { //Placing the robot Image
		Image robotImage = new Image("images/robotImg.png", scale, scale, true, true);
		robotImg = new ImageView(robotImage);
		robotImg.setX(robot.getPosition().x*scale);
		robotImg.setY(robot.getPosition().y*scale);
		root.getChildren().add(robotImg);
	}
	

	private void startGrid() {
		// TODO Auto-generated method stub
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				Point oldP = robot.getPosition();
				switch(event.getCode()){
				case RIGHT:
					if (robot.getPosition().x<room.getWidth()-1) robot.forceMove(new Point(oldP.x+1,oldP.y));
					break;
				case LEFT:
					if (robot.getPosition().x>0) robot.forceMove(new Point(oldP.x-1,oldP.y));
					break;
				case DOWN:
					if (robot.getPosition().y<room.getHeight()-1) robot.forceMove(new Point(oldP.x,oldP.y+1));
					break;
				case UP:
					if (robot.getPosition().y>0) robot.forceMove(new Point(oldP.x,oldP.y-1));
					break;
				case S:
					robot.step();
					break;
				default:
					break;
				}
				robotImg.setX(robot.getPosition().x*scale); //Moving robot image according to command
				robotImg.setY(robot.getPosition().y*scale); //
				drawMap();
			}
		});
	}
	
	private Color[] colors= 	 {Color.BLACK,Color.rgb(255,255,176),Color.rgb(255,150,0),Color.rgb(230,100,50)};
	private Color[] colorWalls = {Color.BLACK,Color.rgb(255,0,0),Color.rgb(0,255,0)};

	private void drawMap() { //creating the map and objects.
		// TODO Auto-generated method stub
		Point p;
		for(int x=0; x<room.getWidth(); x++){
			for(int y=0; y<room.getHeight(); y++){
				p=new Point(x,y);

				Rectangle rect = new Rectangle(x*scale, y*scale, scale, scale);

				//room stuff goes here
				if(room.floorIsObstacle(p)>0){ //TODO stairs are different than obstacle
					rect.setFill(colors[0]);
					rect.setStroke(Color.rgb(50,50,50));
				} else {
					if(robot.getKnown().floorIsPlaceholder(p)){
						rect.setFill(colors[room.getFloorTypeAt(p)+1]); //floor tiles
						rect.setStroke(colors[room.getFloorTypeAt(p)+1].deriveColor(1.0, 1.0, 0.7, 1.0));
					}
					else{
					rect.setFill(colors[room.getFloorTypeAt(p)+1].deriveColor(60.0, 1.0, 1.0, 1.0)); //floor tiles
					rect.setStroke(colors[room.getFloorTypeAt(p)+1].deriveColor(60.0, 1.0, 0.7, 1.0));
					}
				}
				
				
				ArrayList<Line> wallLines = new ArrayList<Line>();
				int wall;
				int[] walls = room.wallsSurrounding(p);
				int[] wallsSeen = robot.getKnown().wallsSurrounding(p);
				boolean wallSeen;
				for(int w =0;w<4;w++){
					wall = walls[w];
					wallSeen=walls[w]==wallsSeen[w];
					if ((wall!=Room.WALL_NONE)){
						//n=0, w=1,e=2,s=3
						Line l = new Line();
						if (wallSeen){
							l.setStroke(colorWalls[wall-1].deriveColor(40.0, 1.0, 10.0, 1.0));
						}else{
							l.setStroke(colorWalls[wall-1]);
						}
						l.setStrokeWidth(4);
						
						switch (w){ 
						case 0:
							l.setStartX(x*scale);
							l.setEndX(x*scale+scale);
							l.setStartY(y*scale);
							l.setEndY(y*scale);
							break;
						case 1:
							l.setStartX(x*scale);
							l.setEndX(x*scale);
							l.setStartY(y*scale);
							l.setEndY(y*scale+scale);
							break;
						case 2:
							l.setStartX(x*scale+scale);
							l.setEndX(x*scale+scale);
							l.setStartY(y*scale);
							l.setEndY(y*scale+scale);
							break;
						case 3:
							l.setStartX(x*scale);
							l.setEndX(x*scale+scale);
							l.setStartY(y*scale+scale);
							l.setEndY(y*scale+scale);
							break;
								
						}
						wallLines.add(l);
					} 
					
				}

				
				root.getChildren().add(rect);
				
				for(Line l : wallLines){
					root.getChildren().add(l);
				}
				
				if (room.hasDirtAt(p)){
					Circle dirt = new Circle(x*scale+(scale/5),y*scale+(scale/5),scale/6,Color.BROWN);
					root.getChildren().add(dirt);
				}
				
				if (!robot.getKnown().hasDirtAt(p) && !robot.getKnown().floorIsPlaceholder(p)){
					Circle cleaned = new Circle(x*scale+(scale/5),(y+1)*scale-(scale/5),scale/6,Color.WHITE);
					root.getChildren().add(cleaned);
				}
				
				if(p.equals(robot.getObjective())){
					Circle target = new Circle((x+1)*scale-(scale/5),y*scale+(scale/5),scale/6,Color.BLUE);
					root.getChildren().add(target);
				}
				
				Line pathLine;
				Point tempPrevStep = robot.getPosition();
				for(Point pathStep: robot.getPath(robot.getPosition(),robot.getObjective())){
					pathLine = new Line(tempPrevStep.x*scale+(scale/2),tempPrevStep.y*scale+(scale/2),pathStep.x*scale+(scale/2),pathStep.y*scale+(scale/2));
					pathLine.setStroke(Color.CADETBLUE);
					pathLine.setStrokeWidth(3);
					tempPrevStep = pathStep;
					root.getChildren().add(pathLine);
				}

			}
		}
		loadrobotImage();
	}

}
