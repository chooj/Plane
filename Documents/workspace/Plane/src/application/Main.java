/*
 * FIX MAP
 * FIX setLayout() IN TREE CLASS
 */

package application;
	
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public class Main extends Application {
	
	public static Scene scene;
	public static Pane pane;
	public static int width = 600, height = 600;
	public static boolean mousePressed = false, mouseReleased = false, keyPressed = false, keyReleased = false;
	public static boolean[] keyHeld = new boolean[768];
	public static double mouseX = -1.0, mouseY = -1.0;
	public static Rectangle ground;
	public static PerspectiveCamera cam;
	public static Translate ct;
	public static Rotate cxr, czr;
	public static Plane plane;
	public static Terrain terrain;
	//public static Map map;
	
	@Override
	public void start(Stage stage) {
		pane = new Pane();
		scene = new Scene(pane, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.SKYBLUE);
		stage.setScene(scene);
		stage.show();
		//
		initialize();
		//
		runAnimation();
	}
	
	public static void initialize() {
		//
		plane = new Plane();
		terrain = new Terrain();
		pane.getChildren().add(terrain.components());
		//map = new Map();
		//pane.getChildren().add(map.components());
		// camera
		cam = new PerspectiveCamera();
		cam.setNearClip(0.1);
		cam.setFarClip(2000);
		ct = new Translate(plane.getX(), 0, plane.getZ());
		cxr = new Rotate(plane.getAngle(), Rotate.Y_AXIS);
		czr = new Rotate(plane.getTilt(), Rotate.Z_AXIS);
		cam.getTransforms().addAll(ct, cxr, czr);
		scene.setCamera(cam);
	}
	
	public static void draw() {
		plane.update();
		if (keyHeld[37]) {
			plane.accelerate();
		} else if (keyHeld[39]) {
			plane.decelerate();
		}
		if (keyHeld[36]) {
			plane.rotateLeft();
		} else if (keyHeld[38]) {
			plane.rotateRight();
		} else {
			plane.rotateNone();
		}
		//
		terrain.setCoordinates(plane.getX(), plane.getZ());
		terrain.update();
		//
		ct.setX(plane.getX());
		ct.setZ(-plane.getZ());
		cxr.setAngle(plane.getAngle());
		czr.setAngle(plane.getTilt());
		//
		/*map.setMarkX(plane.getX());
		map.setMarkY(plane.getZ());
		map.configure(plane.getX(), plane.getZ(), plane.getAngle());
		map.update();*/
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Infinitely loops through the draw and utilities methods.
	 */
	public static void runAnimation() {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				updateUtilities();
				draw();
				updateReleased();
			}
		};
		Timeline tl = new Timeline(new KeyFrame(Duration.ONE, eh));
		tl.setCycleCount(Timeline.INDEFINITE);
		tl.play();
	}
	
	/**
	 * Updates all mouse variables and all key variables.
	 */
	public static void updateUtilities() {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = true;
			}
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = t.getSceneX();
				mouseY = t.getSceneY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = t.getSceneX();
				mouseY = t.getSceneY();
			}
		});
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = false;
				mouseReleased = true;
			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("deprecation")
			@Override
			public void handle(KeyEvent t) {
				keyPressed = true;
				keyHeld[t.getCode().impl_getCode() - 1] = true;
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@SuppressWarnings("deprecation")
			@Override
			public void handle(KeyEvent t) {
				keyPressed = false;
				keyReleased = true;
				keyHeld[t.getCode().impl_getCode() - 1] = false;
			}
		});
	}
	
	/**
	 * Sets mouseReleased and keyReleased to false at the end of each loop.
	 */
	public static void updateReleased() {
		mouseReleased = false;
		keyReleased = false;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
