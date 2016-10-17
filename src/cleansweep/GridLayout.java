package cleansweep;

import java.awt.Point;
import java.util.Random;

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
import javafx.stage.Stage;

public class GridLayout extends Application{
	int dimension=25; 
	int scale = 25;
	ImageView robotImg;
	Pane root;
	Scene scene;
	Room r;
	RobotDummy robot;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage gridStage) throws Exception {
		// TODO Auto-generated method stub
		//Random number generator for objects
		
		root = new AnchorPane();
		drawMap();
		
		r = RoomParser.parseFile("samplefloor.bmp");
		robot = new RobotDummy(r);
		//Loading and placing our robot
		loadrobotImage();
		
		
		//Button for resetting the game
		Button btn = new Button();
		btn.setText("Step");
		btn.setScaleX(1.5);
		btn.setScaleY(1.5);
		AnchorPane.setBottomAnchor(btn, 25.0);
		AnchorPane.setRightAnchor(btn, 290.0);
		btn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				robot.step();
				robotImg.setX(robot.getPosition().x*scale); //Moving robot image according to command
				robotImg.setY(robot.getPosition().y*scale); //
			}
			
		});
		root.getChildren().add(btn);
		scene = new Scene(root, 625,700);
		gridStage.setScene(scene);
		gridStage.show();
		startGrid();
	}
	
	
	private void loadrobotImage() { //Placing the robot Image
		// TODO Auto-generated method stub
		Image robotImage = new Image("File:robotImg.jpg", scale, scale, true, true);
		robotImg = new ImageView(robotImage);
		robotImg.setX(robot.getPosition().x*scale);
		robotImg.setY(robot.getPosition().y*scale);
		root.getChildren().add(robotImg);
	}
	
	private void loadObjectImage(int x, int y) { //Placing the object Image on the coordinates x and y
		// TODO Auto-generated method stub
		Image objectImage = new Image("File:objectImg.jpg", scale, scale, true, true);
		ImageView objectImg = new ImageView(objectImage);
		objectImg.setX(x*scale);
		objectImg.setY(y*scale);
		root.getChildren().add(objectImg);
	}
	

	private void startGrid() {
		// TODO Auto-generated method stub
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				Point oldP = robot.getPosition();
				switch(event.getCode()){
				
				case RIGHT:
					robot.forceMove(new Point(oldP.x+1,oldP.y));
					break;
				case LEFT:
					robot.forceMove(new Point(oldP.x-1,oldP.y));
					break;
				case DOWN:
					robot.forceMove(new Point(oldP.x,oldP.y+1));
					break;
				case UP:
					robot.forceMove(new Point(oldP.x,oldP.y-1));
					break;
				case S:
					robot.step();
					break;
				default:
					break;
				}
				robotImg.setX(robot.getPosition().x*scale); //Moving robot image according to command
				robotImg.setY(robot.getPosition().y*scale); //
			}
		});
	}
	

	private void drawMap() { //creating the map and objects.
		// TODO Auto-generated method stu
		for(int x=0; x<dimension; x++){
			for(int y=0; y<dimension; y++){
				Rectangle rect = new Rectangle(x*scale, y*scale, scale, scale);
				rect.setStroke(Color.BLACK);
				//room stuff goes here
				rect.setFill(Color.TURQUOISE); //Map Color
				root.getChildren().add(rect);
			}
		}
	}
	
	

}
