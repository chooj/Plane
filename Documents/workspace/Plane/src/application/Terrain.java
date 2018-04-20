package application;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Terrain implements GameObject {
	
	private double x, z;
	//private Rectangle ground;
	//private ArrayList<Tree> trees;
	private ArrayList<TerrainChunk> chunks;
	
	public Terrain() {
		x = 0;
		z = 0;
		chunks = new ArrayList<TerrainChunk>();
		// 1000x1000
		chunks.add(new TerrainChunk(-600, -600));
		chunks.add(new TerrainChunk(600, -600));
		chunks.add(new TerrainChunk(-600, 600));
		chunks.add(new TerrainChunk(600, 600));
		//
		/*ground = new Rectangle(-4700, -4700, 10000, 10000);
		ground.setFill(Color.GREEN);
		ground.setRotationAxis(Rotate.X_AXIS);
		ground.setRotate(90);
		ground.setLayoutY(300);
		trees = new ArrayList<Tree>();
		for (int i = 0; i < 400; i++) {
			double r1 = (Math.random()-0.5)*400, r2 = (Math.random()-0.5)*400;
			trees.add(new Tree(-4700 + (i%20)*500 + r1, -4700 + Math.floor(i/20)*500 + r2));
		}*/
	}
	
	public void setCoordinates(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void update() {
	}

	@Override
	public Group components() {
		Group g = new Group();
		for (TerrainChunk c : chunks) {
			g.getChildren().add(c.components());
		}
		/*g.getChildren().add(ground);
		for (Tree t : trees) {
			g.getChildren().add(t.components());
		}*/
		return g;
	}
	
}
