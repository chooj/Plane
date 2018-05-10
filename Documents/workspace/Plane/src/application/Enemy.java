package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Enemy implements GameObject {
	
	private Box body, wings, propel, hBar;
	private Box[] bBorders;
	private double x = 300, y = 300, z = 10000, ys = -1, as = 1, r = 0, maxS = 10.0, minS = 2.5,
			s = maxS, rg = 0, pr, lastRm = -Double.MAX_VALUE, lastHm = -Double.MAX_VALUE;
	private double[] direc = new double[3];
	private int health = 100;
	private boolean pFiring = false, sentDead = false;
	private Rotate pRot;
	
	public Enemy(double x, double z, double dist) {
		spawn(x, z, dist);
		//
		body = new Box(100, 100, 600);
		PhongMaterial em = new PhongMaterial();
		em.setDiffuseColor(Color.ORANGE);
		body.setMaterial(em);
		move(body, x, y, z);
		body.setRotationAxis(Rotate.Y_AXIS);
		//
		PhongMaterial wm = new PhongMaterial();
		wm.setDiffuseColor(Color.BROWN);
		wings = new Box(400, 50, 200);
		wings.setMaterial(wm);
		move(wings, x, y, z + 200);
		wings.setRotationAxis(Rotate.Y_AXIS);
		//
		propel = new Box(400, 40, 10);
		propel.setMaterial(em);
		move(propel, x, y, z + 320);
		propel.setRotationAxis(Rotate.Y_AXIS);
		pRot = new Rotate(0, Rotate.Z_AXIS);
		propel.getTransforms().add(pRot);
		//
		hBar = new Box(100, 30, 5);
		PhongMaterial hm = new PhongMaterial();
		hm.setDiffuseColor(Color.rgb(221, 0, 38));
		hBar.setMaterial(hm);
		move(hBar, x, y - 100, z);
		hBar.setRotationAxis(Rotate.Y_AXIS);
		//
		bBorders = new Box[2];
		for (int i = 0; i < bBorders.length; i++) {
			bBorders[i] = new Box(110, 5, 10);
			PhongMaterial bm = new PhongMaterial();
			bm.setDiffuseColor(Color.BLACK);
			bBorders[i].setMaterial(bm);
			move(bBorders[i], x, y - 117 + (i%2)*34, z);
			bBorders[i].setRotationAxis(Rotate.Y_AXIS);
		}
		//
		for (int i = 0; i < direc.length; i++) {
			direc[i] = Math.random()*Math.PI*2;
		}
	}
	
	public void spawn(double x, double z, double dist) {
		double rot = Math.random()*Math.PI*2, posneg = Math.random()-0.5;
		this.x = x + dist*Math.sin(rot);
		this.z = z + dist*Math.cos(rot);
		r = Math.toDegrees(rot) + 20*((posneg > 0) ? 30 : -30);
		rg = r;
		health = 100;
		y = -100;
	}
	
	public boolean getDead() {
		return health <= 0;
	}
	
	public double getX() {
		return x;
	}

	public double getZ() {
		return z;
	}
	
	public double getAngle() {
		return r;
	}

	public void setData(double pr, boolean pFiring) {
		this.pr = pr;
		this.pFiring = pFiring;
	}
	
	private void regroup() {
		double rand = Math.random() - 0.5;
		rg = r + ((rand < 0) ? -1 : 1) * (60 + Math.random()*20);
		s = maxS;
		lastRm = System.currentTimeMillis();
	}
	
	public void changeHealth() {
		health -= 5;
		lastHm = System.currentTimeMillis();
	}
	
	private boolean underFire() {
		return pFiring && System.currentTimeMillis() - lastHm < 500;
	}
	
	public int dead() {
		if (health <= 0 && !sentDead) {
			sentDead = true;
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void update() {
		if (!getDead()) {
			double realRot = body.getRotate() + (r-body.getRotate())/50;
			s += (minS-s)/10;
			x += s*Math.sin(Math.toRadians(r));
			z += s*Math.cos(Math.toRadians(r));
			r += (rg-r)/400;
			//
			if (underFire() && System.currentTimeMillis() - lastRm > 8000) {
				regroup();
			}
			//
			move(body, x, y, z);
			body.setRotate(realRot);
			move(propel, x + 320*Math.sin(Math.toRadians(r)), y,
					z + 320*Math.cos(Math.toRadians(r)));
			pRot.setAngle(pRot.getAngle() + 3);
			propel.setRotate(realRot);
			move(wings, x + 200*Math.sin(Math.toRadians(r)), y, z + 200*Math.cos(Math.toRadians(r)));
			wings.setRotate(realRot);
			//
			move(hBar, x, y - 100, z);
			hBar.setRotate(pr);
			hBar.setWidth(health);
			//
			for (int i = 0; i < bBorders.length; i++) {
				move(bBorders[i], x, y - 117 + (i%2)*34, z);
				bBorders[i].setRotate(pr);
			}
			y += (300 - y) / 50;
			ys = -1;
		} else {
			body.setRotationAxis(Rotate.Z_AXIS);
			wings.setRotationAxis(Rotate.Z_AXIS);
			propel.setRotationAxis(Rotate.Z_AXIS);
			double bgty = body.getTranslateY();
			if (bgty < Double.MAX_VALUE) {
				ys += 0.005;
				as = 1;
				move(body, body.getTranslateX() + Math.cos(direc[0])*as, bgty + ys,
						body.getTranslateZ() + Math.sin(direc[0])*as);
				move(wings, wings.getTranslateX() + Math.cos(direc[1])*as, body.getTranslateY(),
						wings.getTranslateZ() + Math.sin(direc[1])*as);
				move(propel, propel.getTranslateX() + Math.cos(direc[2])*as, body.getTranslateY(),
						propel.getTranslateZ() + Math.sin(direc[2])*as);
				body.setRotate(body.getRotate() + 0.1);
				wings.setRotate(body.getRotate() + 0.1);
				propel.setRotate(body.getRotate() + 0.1);
				
			}
			if (body.getScaleX() > 0) {
				shrink(body, 0.0012);
				shrink(wings, 0.0012);
				shrink(propel, 0.0012);
			}
			move(hBar, 0, Double.MAX_VALUE, 0);
			for (Box b : bBorders) {
				b.setTranslateY(Double.MAX_VALUE);
			}
		}
	}
	
	public boolean offScreen() {
		return body.getTranslateY() > 10000;
	}
	
	private static void move(Box b, double x, double y, double z) {
		b.setTranslateX(x);
		b.setTranslateY(y);
		b.setTranslateZ(z);
	}
	
	private static void shrink(Box b, double r) {
		b.setScaleX(b.getScaleX() - r);
		b.setScaleY(b.getScaleY() - r);
		b.setScaleZ(b.getScaleZ() - r);
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(body, propel, wings, hBar);
		for (Box b : bBorders) {
			g.getChildren().add(b);
		}
		return g;
	}

}