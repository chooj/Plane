package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Missile implements GameObject {
	
	public Box missile, flame;
	private double x, y, z, r, xs, zs, px, pz, pr, speed = 2;
	private double[] ex, ez, er, eDist;
	private boolean fired;
	private int tracker = -1;
	
	public Missile(int eNum) {
		missile = new Box(40, 40, 80);
		PhongMaterial mm = new PhongMaterial();
		mm.setDiffuseColor(Color.WHITE);
		missile.setMaterial(mm);
		missile.setRotationAxis(Rotate.Y_AXIS);
		//
		flame = new Box(30, 30, 5);
		PhongMaterial fm = new PhongMaterial();
		fm.setDiffuseColor(Color.ORANGE);
		flame.setMaterial(fm);
		flame.setRotationAxis(Rotate.Y_AXIS);
		//
		fired = false;
		x = 0;
		y = 700;
		z = 0;
		r = 0;
		xs = 0;
		zs = 0;
		px = 0;
		pz = 0;
		pr = 0;
		//
		ex = new double[eNum];
		ez = new double[eNum];
		er = new double[eNum];
		eDist = new double[eNum];
	}
	
	public void setData(double px, double pz, double pr, double[] ex, double[] ez, double[] er) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		this.ex = ex;
		this.ez = ez;
		this.er = er;
	}
	
	public void fire() {
		fired = true;
	}
	
	private double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	private boolean gettingCloser(int i) {
		return dist(x, z, ex[i], ez[i]) < eDist[i];
	}
	
	private void checkDist() {
		for (int i = 0; i < eDist.length; i++) {
			eDist[i] = dist(x, z, ex[i], ez[i]);
		}
	}

	@Override
	public void update() {
		if (fired) {
			xs = speed*Math.sin(Math.toRadians(r));
			zs = speed*Math.cos(Math.toRadians(r));
			x += xs;
			z += zs;
			PhongMaterial fm = new PhongMaterial();
			fm.setDiffuseColor(Color.rgb(255, 125 + (int) Math.random()*130, (int) Math.random()*255));
			flame.setMaterial(fm);
			double distance = dist(x, z, px, pz);
			if (distance > 50000) {
				fired = false;
			}
			if (distance < 30000) {
				if (tracker == -1) {
					for (int i = 0; i < ex.length; i++) {
						if (dist(x, z, ex[i], ez[i]) < 8000) {
							tracker = i;
						}
					}
				} else {
					if (gettingCloser(tracker)) {
						if (r < er[tracker] + 180) {
							r++;
						} else if (r > er[tracker] + 180) {
							r--;
						}
					} else {
						if (r < er[tracker]) {
							r++;
						} else if (r > er[tracker]) {
							r--;
						}
					}
				}
			}
			y += (300 - y)/500;
			if (speed < 10) {
				speed += Math.pow(speed, 4)/10000;
			}
			checkDist();
		} else {
			x = px + 300*Math.cos(Math.toRadians(pr));
			z = pz - 300*Math.sin(Math.toRadians(pr));
			r = pr;
			y = 700;
			speed = 2;
			tracker = -1;
		}
		missile.setTranslateX(x);
		missile.setTranslateY(y);
		missile.setTranslateZ(z);
		missile.setRotate(r);
		//
		flame.setTranslateX(x - 45*Math.sin(Math.toRadians(r)));
		flame.setTranslateY(y);
		flame.setTranslateZ(z - 45*Math.cos(Math.toRadians(r)));
		flame.setRotate(r);
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(missile, flame);
		return g;
	}
	
}
