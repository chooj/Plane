package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Tree implements GameObject {
	
	private Box trunk, leaves;
	private double yDiff;

	public Tree(double x, double z) {
		trunk = new Box(50, 75 + Math.random()*50, 50);
		PhongMaterial tm = new PhongMaterial();
		tm.setDiffuseColor(Color.BROWN);
		trunk.setMaterial(tm);
		trunk.setTranslateX(x);
		trunk.setTranslateZ(z);
		trunk.setTranslateY(1100 - trunk.getHeight()/2);
		//
		leaves = new Box(60 + Math.random()*40, 60 + Math.random()*40, 60 + Math.random()*40);
		PhongMaterial lm = new PhongMaterial();
		lm.setDiffuseColor(Color.YELLOWGREEN);
		leaves.setMaterial(lm);
		leaves.setTranslateX(x);
		leaves.setTranslateZ(z);
		leaves.setTranslateY(1100 - trunk.getHeight());
		yDiff = 0;
	}

	@Override
	public void update() {
		yDiff -= yDiff/30;
		trunk.setLayoutY(yDiff);
		leaves.setLayoutY(yDiff);
	}
	
	public double getX() {
		return trunk.getTranslateX();
	}
	
	public double getZ() {
		return trunk.getTranslateZ();
	}
	
	public void setLayout(double nx, double nz) {
		trunk.setTranslateX(trunk.getTranslateX() + nx);
		trunk.setTranslateZ(trunk.getTranslateZ() + nz);
		leaves.setTranslateX(leaves.getTranslateX() + nx);
		leaves.setTranslateZ(leaves.getTranslateZ() + nz);
		yDiff = trunk.getHeight() + leaves.getHeight()/2;
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(trunk, leaves);
		return g;
	}
	
	
}