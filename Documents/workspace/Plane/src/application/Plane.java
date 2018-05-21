package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Plane implements GameObject {
	
	private Box body, wingL, wingR, propel, pit, tail;
	private Box[] pieces = new Box[6];
	private Rotate pRot;
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
		//
		body = new Box(100, 100, 600);
		PhongMaterial em = new PhongMaterial();
		em.setDiffuseColor(Color.grayRgb(50));
		body.setMaterial(em);
		move(body, x, 300, z);
		body.setRotationAxis(Rotate.Y_AXIS);
		pieces[0] = body;
		//
		wingL = new Box(160, 50, 400);
		PhongMaterial wm = new PhongMaterial();
		wm.setDiffuseColor(Color.BLACK);
		wingL.setMaterial(wm);
		move(wingL, x - 60, 300, z);
		Rotate wlRot = new Rotate(20, Rotate.Y_AXIS);
		wingL.getTransforms().addAll(wlRot);
		wingL.setRotationAxis(Rotate.Y_AXIS);
		pieces[1] = wingL;
		//
		wingR = new Box(160, 50, 400);
		wingR.setMaterial(wm);
		move(wingR, x + 60, 300, z);
		wingR.setRotationAxis(Rotate.Y_AXIS);
		Rotate wrRot = new Rotate(-20, Rotate.Y_AXIS);
		wingR.getTransforms().addAll(wrRot);
		pieces[2] = wingR;
		//
		propel = new Box(400, 40, 10);
		propel.setMaterial(em);
		move(propel, x, 300, z + 320);
		propel.setRotationAxis(Rotate.Y_AXIS);
		pRot = new Rotate(0, Rotate.Z_AXIS);
		propel.getTransforms().add(pRot);
		pieces[3] = propel;
		//
		pit = new Box(70, 140, 140);
		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseColor(Color.AQUA);
		pit.setMaterial(pm);
		move(pit, x, 350, z + 170);
		pit.setRotationAxis(Rotate.Y_AXIS);
		Rotate cRot = new Rotate(45, Rotate.X_AXIS);
		pit.getTransforms().add(cRot);
		pieces[4] = pit;
		//
		tail = new Box(70, 170, 170);
		tail.setMaterial(wm);
		move(tail, x, 200, z - 200);
		tail.setRotationAxis(Rotate.Y_AXIS);
		Rotate tRot = new Rotate(30, Rotate.X_AXIS);
		tail.getTransforms().add(tRot);
		pieces[5] = tail;
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
		rs += (-0.04 + 0.008*s - rs)/1000;
	}
	
	public void rotateRight() {
		rs += (0.04 - 0.008*s - rs)/1000;
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
	
	private void move(Node b, double x, double y, double z) {
		b.setTranslateX(x);
		b.setTranslateY(y);
		b.setTranslateZ(z);
	}

	@Override
	public void update() {
		maxS = (useBoost && boost > 0) ? 2.5 : 2;
		xs = s*Math.sin(Math.toRadians(r));
		zs = s*Math.cos(Math.toRadians(r));
		x += xs;
		z += zs;
		r += rs;
		//
		double cos = Math.cos(Math.toRadians(r)), sin = Math.sin(Math.toRadians(r));
		double xa = 300*cos + 1000*sin,
				za = -300*sin + 1000*cos;
		move(body, x + xa, 700, z + za);
		move(propel, x + 320*Math.sin(Math.toRadians(r)) + xa, 700,
				z + 320*Math.cos(Math.toRadians(r)) + za);
		pRot.setAngle(pRot.getAngle() + 3);
		move(wingL, x - 60*Math.cos(Math.toRadians(r)) + xa, 700,
				z + 60*Math.sin(Math.toRadians(r)) + za);
		move(wingR, x + 60*Math.cos(Math.toRadians(r)) + xa, 700,
				z - 60*Math.sin(Math.toRadians(r)) + za);
		move(pit, x + 170*Math.sin(Math.toRadians(r)) + xa, 650,
				z + 170*Math.cos(Math.toRadians(r)) + za);
		move(tail, x - 250*Math.sin(Math.toRadians(r)) + xa, 630,
				z - 250*Math.cos(Math.toRadians(r)) + za);
		for (Box b : pieces) {
			b.setRotate(r);
		}
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(body, propel, wingL, wingR, pit, tail);
		return g;
	}

}