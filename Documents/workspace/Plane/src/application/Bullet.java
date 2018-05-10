package application;

import javafx.scene.Group;

public class Bullet implements GameObject {
	
	private double x, z, xs, zs, px, pz, pr, speed = 100;
	private double[] ex, ez;
	private boolean fired;
	
	public Bullet(int eNum) {
		fired = false;
		x = 0;
		z = 0;
		xs = 0;
		zs = 0;
		px = 0;
		pz = 0;
		pr = 0;
		ex = new double[eNum];
		ez = new double[eNum];
	}
	
	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	public void setData(double px, double pz, double pr, double[] ex, double[] ez) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		this.ex = ex;
		this.ez = ez;
	}
	
	public void fire() {
		double rand = -2 + Math.random()*4;
		xs = speed*Math.sin(Math.toRadians(pr + rand));
		zs = speed*Math.cos(Math.toRadians(pr + rand));
		fired = true;
	}
	
	public boolean fired() {
		return fired;
	}
	
	public int getHit() {
		for (int i = 0; i < ex.length; i++) {
			if (x > ex[i] - 200 && x < ex[i] + 200 && z > ez[i] - 200 && z < ez[i] + 200) {
				fired = false;
				return i;
			}
		}
		return -1;
	}

	@Override
	public void update() {
		if (fired) {
			x += xs;
			z += zs;
			if (dist(x, z, px, pz) > 50000) {
				fired = false;
			}
		} else {
			x = px + 300*Math.cos(Math.toRadians(pr));
			z = pz - 300*Math.sin(Math.toRadians(pr));
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getZ() {
		return z;
	}

	@Override
	public Group components() {
		Group g = new Group();
		return g;
	}

}