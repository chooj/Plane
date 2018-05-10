package application;

import javafx.scene.Group;
import javafx.scene.shape.Box;

public class Plane implements GameObject {
	
	private Box body;
	private double x, z, xs, zs, s, r, rs, boost, maxS;
	private boolean useBoost;
	
	public Plane() {
		x = 0;
		z = 0;
		xs = 0;
		zs = 0;
		s = 0.1;
		r = 0;
		rs = 0;
		boost = 100;
		maxS = 2;
		useBoost = false;
	}
	
	public double getX() {
		return x;
	}
	
	public double getZ() {
		return z;
	}
	
	public void accelerate() {
		s += (maxS - s) / 500;
	}
	
	public void decelerate() {
		s += (0.1 - s) / 200;
	}
	
	public void rotateLeft() {
		rs += (-0.025 + 0.004*s - rs)/1000;
	}
	
	public void rotateRight() {
		rs += (0.025 - 0.004*s - rs)/1000;
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
	
	public double getSpeed() {
		return s;
	}
	
	public void useBoost() {
		useBoost = true;
		if (boost > 0) {
			boost -= 0.03;
		}
	}
	
	public boolean boosting() {
		return useBoost && boost > 0;
	}
	
	public double boost() {
		return boost;
	}
	
	public void reFillBoost() {
		useBoost = false;
		if (boost < 100) {
			boost += 0.01;
		}
	}

	@Override
	public void update() {
		maxS = (useBoost && boost > 0) ? 2.5 : 2;
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