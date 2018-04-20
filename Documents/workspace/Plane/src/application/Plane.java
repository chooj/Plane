package application;

import javafx.scene.Group;
import javafx.scene.shape.Box;

public class Plane implements GameObject {
	
	private Box body;
	private double x, z, xs, zs, s, r, rs;
	
	public Plane() {
		x = 0;
		z = 0;
		xs = 0;
		zs = 0;
		s = 0.1;
		r = 0;
		rs = 0;
	}
	
	public double getX() {
		return x;
	}
	
	public double getZ() {
		return z;
	}
	
	public void accelerate() {
		s += (2 - s) / 500;
	}
	
	public void decelerate() {
		s += (0.1 - s) / 200;
	}
	
	public void rotateLeft() {
		rs += (-0.03 + 0.007*s - rs)/1000;
	}
	
	public void rotateRight() {
		rs += (0.03 - 0.007*s - rs)/1000;
	}
	
	public void rotateNone() {
		rs = 499*rs/500;
	}
	
	public double getAngle() {
		return r;
	}
	
	public double getTilt() {
		return rs*300;
	}

	@Override
	public void update() {
		xs = s*Math.sin(Math.toRadians(r));
		zs = s*Math.cos(Math.toRadians(r));
		x += xs;
		z += zs;
		r += rs;
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().add(body);
		return g;
	}

}
