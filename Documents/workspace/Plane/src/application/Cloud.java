package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Cloud implements GameObject {
	
	private Sphere[] cloud;
	private double r, s, y;
	public static double[] rad = {30, 25, 20, 18}, yDiff = {0, 5, 10, 11};
	
	public Cloud(double x, double z) {
		r = Math.random()*2*Math.PI;
		s = 1 + Math.random()*8;
		y = -2000 - Math.random()*1000;
		cloud = new Sphere[4];
		PhongMaterial sm = new PhongMaterial();
		sm.setDiffuseColor(Color.WHITE);
		Sphere s1 = new Sphere(rad[0]*s);
		move(s1, x, y, z);
		cloud[0] = s1;
		//
		Sphere s2 = new Sphere(rad[1]*s);
		move(s2, x - 20*Math.cos(r)*s, y + 5*s, z - 20*Math.cos(r)*s);
		cloud[1] = s2;
		//
		Sphere s3 = new Sphere(rad[2]*s);
		move(s3, x - 40*Math.cos(r)*s, y + 10*s, z - 40*Math.cos(r)*s);
		cloud[2] = s3;
		//
		Sphere s4 = new Sphere(rad[3]*s);
		move(s4, x + 25*Math.cos(r)*s, y + 11*s, z + 25*Math.cos(r)*s);
		cloud[3] = s4;
		for (Sphere s : cloud) {
			s.setMaterial(sm);
		}
	}
	
	public void setLayout(double x, double z) {
		for (Sphere s : cloud) {
			s.setTranslateX(s.getTranslateX() + x);
			s.setTranslateY(-5000);
			s.setTranslateZ(s.getTranslateZ() + z);
		}
	}
	
	public double getX() {
		return cloud[0].getTranslateX();
	}
	
	public double getZ() {
		return cloud[0].getTranslateZ();
	}
	
	public static void move(Sphere s, double x, double y, double z) {
		s.setTranslateX(x);
		s.setTranslateY(y);
		s.setTranslateZ(z);
	}

	@Override
	public void update() {
		for (int i = 0; i < cloud.length; i++) {
			cloud[i].setTranslateY(cloud[i].getTranslateY() +
					(y+yDiff[i]*s-cloud[i].getTranslateY())/10);
		}
	}

	@Override
	public Group components() {
		Group g = new Group();
		for (Sphere s : cloud) {
			g.getChildren().add(s);
		}
		return g;
	}
	
}
