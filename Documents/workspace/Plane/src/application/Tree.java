package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
//import javafx.scene.shape.Sphere;

public class Tree implements GameObject {
	
	private Box trunk, leaves;
	//private Sphere leaves, l1, l2;
	
	public Tree(double x, double z) {
		trunk = new Box(50, 100, 50);
		PhongMaterial tm = new PhongMaterial();
		tm.setDiffuseColor(Color.BROWN);
		trunk.setMaterial(tm);
		trunk.setTranslateX(x);
		trunk.setTranslateZ(z);
		trunk.setTranslateY(550);
		//
		leaves = new Box(80, 80, 80);
		PhongMaterial lm = new PhongMaterial();
		lm.setDiffuseColor(Color.GREEN);
		leaves.setMaterial(lm);
		leaves.setTranslateX(x);
		leaves.setTranslateZ(z);
		leaves.setTranslateY(500);
		/*leaves = new Sphere(50);
		PhongMaterial lm = new PhongMaterial();
		lm.setDiffuseColor(Color.GREEN);
		leaves.setMaterial(lm);
		leaves.setTranslateX(x);
		leaves.setTranslateZ(z);
		leaves.setTranslateY(500);
		//
		l1 = new Sphere(30);
		l1.setMaterial(lm);
		l2 = new Sphere(30);
		l2.setMaterial(lm);
		l1.setTranslateX(x + 35);
		l1.setTranslateZ(z);
		l2.setTranslateX(x - 30);
		l2.setTranslateZ(z - 20);
		l1.setTranslateY(500);
		l2.setTranslateY(510);*/
	}

	@Override
	public void update() {
		// blow in the wind?
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
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(trunk, leaves);
		return g;
	}
	
	
}
