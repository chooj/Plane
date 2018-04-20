package application;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class TerrainChunk {
	
	private Rectangle ground;
	private ArrayList<Tree> trees;
	
	public TerrainChunk(double x, double z) {
		ground = new Rectangle(-200, -200, 1000, 1000);
		ground.setFill(Color.GREEN);
		ground.setRotationAxis(Rotate.X_AXIS);
		ground.setRotate(90);
		ground.setLayoutY(300);
		ground.setTranslateX(x);
		ground.setTranslateZ(z);
		trees = new ArrayList<Tree>();
		for (int i = 0; i < 4; i++) {
			double r1 = (Math.random()-0.5)*400, r2 = (Math.random()-0.5)*400;
			Tree t = new Tree(50 + (i%2)*500 + r1, -200 + Math.floor(i/2)*500 + r2);
			t.setLayout(x, z);
			trees.add(t);
		}
	}
	
	public void move(double x, double z) {
		ground.setTranslateX(x);
		ground.setTranslateZ(z);
		for (Tree t : trees) {
			t.setLayout(x - t.getX(), z - t.getZ());
		}
	}
	
	public Group components() {
		Group g = new Group();
		g.getChildren().add(ground);
		for (Tree t : trees) {
			g.getChildren().add(t.components());
		}
		return g;
	}
}
