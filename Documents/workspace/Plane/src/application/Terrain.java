package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Terrain implements GameObject {
	
	private double x, z;
	private Circle ground;
	private Tree[] trees;
	private Cloud[] clouds;
	private String style;
	
	public Terrain(String style) {
		this.style = style;
		x = 0;
		z = 0;
		ground = new Circle(300, 300, 7070);
		ground.setFill(Color.GREEN);
		ground.setRotationAxis(Rotate.X_AXIS);
		ground.setRotate(90);
		ground.setLayoutY(800);
		//
		if (style.equals("on")) {
			trees = new Tree[400];
			for (int i = 0; i < trees.length; i++) {
				trees[i] = new Tree(-4650 + (i%20)*500 + Math.random()*400,
						-4650 + Math.floor(i/20)*500 + Math.random()*400);
			}
		}
		//
		clouds = new Cloud[100];
		for (int i = 0; i < clouds.length; i++) {
			clouds[i] = new Cloud(-20700 + (i%10)*4000 + Math.random()*3000,
					-20700 + Math.floor(i/10)*4000 + Math.random()*3000);
		}
	}
	
	public void setCoordinates(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void update() {
		ground.setTranslateX(x);
		ground.setTranslateZ(z);
		if (style.equals("on")) {
			for (Tree t : trees) {
				if (t.getX() - x > 5000) {
					t.setLayout(-10000, 0);
				} else if (t.getX() - x < -5000) {
					t.setLayout(10000, 0);
				}
				if (t.getZ() - z > 5000) {
					t.setLayout(0,  -10000);
				} else if (t.getZ() - z < -5000) {
					t.setLayout(0,  10000);
				}
				t.update();
			}
		}
		for (Cloud c : clouds) {
			if (c.getX() - x > 20000) {
				c.setLayout(-40000, 0);
			} else if (c.getX() - x < -20000) {
				c.setLayout(40000, 0);
			}
			if (c.getZ() - z > 20000) {
				c.setLayout(0,  -40000);
			} else if (c.getZ() - z < -20000) {
				c.setLayout(0,  40000);
			}
			c.update();
		}
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().add(ground);
		if (style.equals("on")) {
			for (Tree t : trees) {
				g.getChildren().add(t.components());
			}
		}
		for (Cloud c : clouds) {
			g.getChildren().add(c.components());
		}
		return g;
	}
	
}