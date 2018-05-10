/*
 * FIX AUDIO FILES CRASHING PROGRAM
 * IMPROVE PLANE GRAPHICS
 * WRITE TRACKER FOR MISSILE CLASS
 * FIX MAP
 * SPACE OUT ENEMIES ...
 * CREATE ENDING */

package application;
	
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	
	public static Stage topStage;
	public static Scene scene, topScene;
	public static Pane pane, topPane;
	public static int width = 600, height = 600;
	public static boolean mousePressed = false, mouseReleased = false,
			keyPressed = false, keyReleased = false;
	public static boolean[] keyHeld = new boolean[768];
	public static double mouseX = -1.0, mouseY = -1.0;
	//
	public static Rectangle ground;
	public static PerspectiveCamera cam;
	public static Translate ct;
	public static Rotate cxr, czr;
	public static Plane plane;
	public static Terrain terrain;
	public static LinkedList<Enemy> enemies;
	public static Bullet[] bullets;
	public static int bInd = 0, bCount = 0, maxB = 30, mInd = 0;
	public static double lastBm;
	public static boolean firing = false, canFireMissile = true;
	public static Map map;
	public static Meter meter;
	public static Panel panel;
	public static Joystick joystick;
	public static Scope scope;
	// off / basic / on
	public static String terrainStyle = "off";
	public static double time = 120;
	public static int score, eNum = 5;
	public static Missile[] missiles;
	public static String whirFile = "whir.wav", shootFile = "shoot.wav",
			explodeFile = "explode.wav";
	public static Clip whirClip, shootClip, explodeClip;
	
	@Override
	public void start(Stage stage) {
		map = new Map(eNum);
		meter = new Meter();
		panel = new Panel();
		joystick = new Joystick();
		//
		pane = new Pane();
		scene = new Scene(pane, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.SKYBLUE);
		stage.setScene(scene);
		stage.setX(420);
		stage.setY(150);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		//
		topPane = new Pane();
		topScene = new Scene(topPane, 600, 600, Color.SKYBLUE);
		topStage = new Stage();
		topStage.setScene(topScene);
		topStage.setX(420);
		topStage.setY(150);
		topStage.initStyle(StageStyle.UNDECORATED);
		topStage.setOpacity(0.1);
		topStage.show();
		//
		initialize();
		//
		runAnimation();
	}
	
	public static void initialize() {
		plane = new Plane();
		if (!terrainStyle.equals("off")) {
			terrain = new Terrain(terrainStyle);
			pane.getChildren().add(terrain.components());
		}
		enemies = new LinkedList<Enemy>(null);
		for (int i = 0; i < eNum; i++) {
			Enemy newEnemy = new Enemy(300, 0, 20000);
			pane.getChildren().add(newEnemy.components());
			enemies.add(newEnemy);
		}
		bullets = new Bullet[100];
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new Bullet(eNum);
			pane.getChildren().add(bullets[i].components());
		}
		missiles = new Missile[10];
		for (int i = 0; i < missiles.length; i++) {
			missiles[i] = new Missile(eNum);
			pane.getChildren().add(missiles[i].components());
		}
		//
		cam = new PerspectiveCamera();
		cam.setNearClip(0.1);
		cam.setFarClip(2000);
		ct = new Translate(plane.getX(), 0, plane.getZ());
		cxr = new Rotate(plane.getAngle(), Rotate.Y_AXIS);
		czr = new Rotate(plane.getTilt(), Rotate.Z_AXIS);
		cam.getTransforms().addAll(ct, cxr, czr);
		scene.setCamera(cam);
		//
		scope = new Scope();
		topPane.getChildren().add(scope.components());
		//
		try {
			AudioInputStream whirStream = AudioSystem.getAudioInputStream(new File(whirFile));
			whirClip = AudioSystem.getClip();
			whirClip.open(whirStream);
			AudioInputStream shootStream = AudioSystem.getAudioInputStream(new File(shootFile));
			shootClip = AudioSystem.getClip();
			shootClip.open(shootStream);
			AudioInputStream explodeStream = AudioSystem.getAudioInputStream(new File(explodeFile));
			explodeClip = AudioSystem.getClip();
			explodeClip.open(explodeStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public static void draw() {
		if (!whirClip.isRunning()) {
			whirClip.start();
		}
		boolean boosting = false, firing = false;
		String acc = "", direc = "";
		String min = String.valueOf((int) Math.floor(time/60)),
				sec = (time % 60 < 10) ? "0"+(int) (time%60) : String.valueOf((int) time%60);
		String t = min + ":" + sec;
		if (time > 0) {
			time -= 0.001;
		} else {
			time = 0;
		}
		plane.update();
		if (keyHeld[31]) {
			firing = true;
			if (!shootClip.isRunning()) {
				shootClip.start();
			}
		} else {
			shootClip.stop();
		}
		if (keyHeld[37]) {
			plane.accelerate();
			acc = "a";
			if (keyHeld[15]) {
				plane.useBoost();
			}
		} else if (keyHeld[39]) {
			plane.decelerate();
			acc = "d";
		}
		if (!keyHeld[37] || !keyHeld[15]) {
			plane.reFillBoost();
		} else {
			boosting = true;
		}
		if (keyHeld[36]) {
			plane.rotateLeft();
			direc = "l";
		} else if (keyHeld[38]) {
			plane.rotateRight();
			direc = "r";
		} else {
			plane.rotateNone();
		}
		//
		meter.setData(plane.getAngle(), plane.getSpeed(), plane.getTilt());
		meter.update();
		//
		panel.setData(plane.boost(), maxB-bCount);
		panel.update();
		//
		joystick.setData(acc, direc, boosting, firing, t, score);
		joystick.update();
		//
		if (!terrainStyle.equals("off")) {
			terrain.setCoordinates(plane.getX(), plane.getZ());
			terrain.update();
		}
		//
		double[] ex = new double[eNum], ez = new double[eNum], ea = new double[eNum];
		boolean[] ed = new boolean[eNum];
		ListNode<Enemy> current = enemies.getFront();
		int i = 0;
		while (!(current == null)) {
			Enemy currentEnemy = current.getData();
			if (currentEnemy.offScreen()) {
				enemies.delete(current);
			} else {
				currentEnemy.setData(plane.getAngle(), firing);
				currentEnemy.update();
				if (!currentEnemy.getDead() && dist(currentEnemy.getX(), plane.getX(),
						currentEnemy.getZ(), plane.getZ()) > 50000) {
					currentEnemy.spawn(plane.getX(), plane.getZ(), 20000);
				}
				score += 100*currentEnemy.dead();
				if (currentEnemy.dead() > 0) {
					explodeClip.start();
				}
			}
			//
			ex[i] = currentEnemy.getX();
			ez[i] = currentEnemy.getZ();
			ea[i] = currentEnemy.getAngle();
			ed[i] = currentEnemy.getDead();
			//
			current = current.getNext();
			i++;
		}
		map.setData(plane.getX(),plane.getZ(), plane.getAngle(), ex, ez, ea, ed);
		map.update();
		//
		boolean shoot = false;
		if (keyHeld[31] && System.currentTimeMillis() - lastBm > 300 && bCount < maxB) {
			bullets[bInd].fire();
			bInd = ((bInd + 1) % bullets.length);
			lastBm = System.currentTimeMillis();
			bCount++;
			shoot = true;
		} else if (!keyHeld[31] && keyHeld[81]) {
			bCount = 0;
		}
		firing = false;
		for (Bullet b : bullets) {
			b.setData(plane.getX(), plane.getZ(), plane.getAngle(), ex, ez);
			b.update();
			int gh = b.getHit();
			if (b.getHit() > -1) {
				int counter = 0;
				ListNode<Enemy> healthPointer = enemies.getFront();
				while (counter < gh) {
					healthPointer = healthPointer.getNext();
					counter++;
				}
				healthPointer.getData().changeHealth();
			}
		}
		//
		if (keyHeld[69] && canFireMissile) {
			missiles[mInd].fire();
			mInd = (mInd+1) % missiles.length;
			canFireMissile = false;
		} else if (!keyHeld[69]) {
			canFireMissile = true;
		}
		for (Missile m : missiles) {
			m.setData(plane.getX(), plane.getZ(), plane.getAngle(), ex, ez, ea);
			m.update();
		}
		//
		ct.setX(plane.getX());
		ct.setZ(plane.getZ());
		cxr.setAngle(plane.getAngle());
		czr.setAngle(plane.getTilt());
		//
		if (shoot) {
			topStage.setOpacity(0.5);
		}
		if (topStage.getOpacity() > 0.1) {
			topStage.setOpacity(topStage.getOpacity()-0.0015);
		}
		scope.setData(plane.getTilt(), plane.boosting(), shoot);
		scope.update();
	}
	
	public static double dist(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
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
		topScene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = true;
			}
		});
		topScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = t.getSceneX();
				mouseY = t.getSceneY();
			}
		});
		topScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = t.getSceneX();
				mouseY = t.getSceneY();
			}
		});
		topScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = false;
				mouseReleased = true;
			}
		});
		topScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("deprecation")
			@Override
			public void handle(KeyEvent t) {
				keyPressed = true;
				keyHeld[t.getCode().impl_getCode() - 1] = true;
			}
		});
		topScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
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