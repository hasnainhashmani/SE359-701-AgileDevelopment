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
		GridMap grid_layout;
		Pane root;
		Scene scene;
		Robot robot;
		
		public static void main(String[] args){
			launch(args);
		}
		
		@Override
		public void start(Stage sStage) throws Exception {
			// TODO Auto-generated method stub
			Random o = new Random();
			
			grid_layout = new grid_layout(dimension);
			
			//Anchor Pane 
			root = new AnchorPane();
			drawGrid();
			
			//Loading and placing our robot-zombie
			robot = new Robot(robot);
			loadRImage();
			
			//Button for resetting the gridpaine
			Button btn = new Button();
			btn.setText("Reset");
			btn.setScaleX(1.5);
			btn.setScaleY(1.5);
			AnchorPane.setBottomAnchor(btn, 25.0);
			AnchorPane.setRightAnchor(btn, 290.0);
			btn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub

					//Resets the view
					Random o = new Random();
					
					grid_layout = new GridMap(dimension);

					drawGrid();
					
					robot = new Robot(robot);
					loadRImage();
					
				}
				
			});

			root.getChildren().add(btn);
			scene = new Scene(root, 625,700);
			gridStage.setTitle("Simulation");
			gridStage.setScene(scene);
			gridStage.show();
			startGrid();
		}
		
		
		private void loadRobotImage() { //Placing the robot icon on the grid
			// TODO Auto-generated method stub
			Image robotImage = new Image("File:src\\robot.png", scale, scale, true, true);   /*add any robot/vaccum/color/ icon you want and 
																							put it in the same directory as the code */
			robotImage = new ImageView(robotImage);
			robot.setX(robot.getLocation().x*scale);
			robot.setY(robot.getLocation().y*scale);
			root.getChildren().add(robotImage);
		}
		
		
		

		private void startGrid() {
			// TODO Auto-generated method stub
			scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

				@Override
				public void handle(KeyEvent event) {
					// TODO Auto-generated method stub
					switch(event.getCode()){
					case RIGHT:
						robot.goEast();
						break;
					case LEFT:
						robot.goWest();							//right now im testing the robot and having it move around with the keyboard
						break;									// you can ignore this if you like, Its simply for me to check if it moves
					case DOWN:
						robot.goSouth();
						break;
					case UP:
						robot.goNorth();
						break;
					default:
						break;
					}
					robotImg.setX(robot.getLocation().x*scale); //Moving robot icon according to command
					robotImg.setY(robot.getLocation().y*scale); 
				}
			});
		}
		

		//Draws the grid

		private void drawGrid() {
			// TODO Auto-generated method stub
			boolean[][] grid = GridLayout.getMap();
			for(int x=0; x<dimension; x++){
				for(int y=0; y<dimension; y++){
					Rectangle rect = new Rectangle(x*scale, y*scale, scale, scale);
					rect.setStroke(Color.BLACK);
						continue;
					}
					else rect.setFill(Color.TURQUOISE); //Color of background, choose whichever makes it easier.
					root.getChildren().add(rect);
				}
			}
		}
		
		

	}

}
