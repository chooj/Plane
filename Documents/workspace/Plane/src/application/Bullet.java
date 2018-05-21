package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Bullet implements GameObject {
	
	private double x, z, xs, zs, px, pz, pr, r, speed = 200;
	private double[] ex, ez;
	private boolean[] ed;
	private boolean fired;
	private Box bullet;
	
	public Bullet(int eNum) {
		fired = false;
		x = 0;
		z = 0;
		xs = 0;
		zs = 0;
		px = 0;
		pz = 0;
		pr = 0;
		r = 0;
		ex = new double[eNum];
		ez = new double[eNum];
		ed = new boolean[eNum];
		//
		bullet = new Box(50, 50, 1600);
		PhongMaterial bm = new PhongMaterial();
		bm.setDiffuseColor(Color.WHITE);
		bullet.setMaterial(bm);
		bullet.setRotationAxis(Rotate.Y_AXIS);
		bullet.setTranslateY(700);
	}
	
	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	public void setData(double px, double pz, double pr, double[] ex, double[] ez, boolean[] ed) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		this.ex = ex;
		this.ez = ez;
		this.ed = ed;
	}
	
	public void fire() {
		double rand = -0.5 + Math.random()*1;
		xs = speed*Math.sin(Math.toRadians(pr + rand));
		zs = speed*Math.cos(Math.toRadians(pr + rand));
		fired = true;
	}
	
	public boolean fired() {
		return fired;
	}
	
	public int getHit() {
		for (int i = 0; i < ex.length; i++) {
			if (!ed[i] && x > ex[i] - 200 && x < ex[i] + 200 && z > ez[i] - 200 && z < ez[i] + 200) {
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
			bullet.setDepth(1600);
		} else {
			double cos = Math.cos(Math.toRadians(pr)), sin = Math.sin(Math.toRadians(pr));
			x = px + 300*cos + 1501*sin;
			z = pz - 300*sin + 1501*cos;
			r = pr;
			bullet.setDepth(0);
		}
		bullet.setTranslateX(x);
		bullet.setTranslateZ(z);
		bullet.setRotate(r);
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
		g.getChildren().add(bullet);
		return g;
	}

}