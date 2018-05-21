package application;

/*import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;*/

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Enemy implements GameObject {
	
	private Box body, wingL, wingR, propel, pit, tail;
	private Box[] pieces = new Box[6];
	private double x = 300, y = 300, z = 10000, ys = -1, r = 0, maxS = 5.0, minS = 2.5,
			s = maxS, rg = 0, lastRm = -Double.MAX_VALUE, lastHm = -Double.MAX_VALUE,
			lastR = 0, rDiff = 0, lastSm = -Double.MAX_VALUE, pursueRand = Math.random(),
			px, pz, pr;
	private Sphere[] smoke = new Sphere[20];
	private double[] direc = new double[6], smokeRs = new double[smoke.length];
	private int health = 100, damage = 10, sInd = 0, id;
	private boolean pFiring = false, sentDead = false, tracking = false;
	private Rotate pRot, tilt;
	private PhongMaterial[] sm = new PhongMaterial[2];
	
	public Enemy(double x, double z, double dist, int id) {
		spawn(x, z, dist);
		this.id = id;
		//
		tilt = new Rotate(0, Rotate.Z_AXIS);
		//
		body = new Box(100, 100, 600);
		PhongMaterial em = new PhongMaterial();
		em.setDiffuseColor(Color.ORANGE);
		body.setMaterial(em);
		move(body, x, y, z);
		body.getTransforms().add(tilt);
		body.setRotationAxis(Rotate.Y_AXIS);
		pieces[0] = body;
		//
		wingL = new Box(160, 50, 400);
		PhongMaterial wm = new PhongMaterial();
		wm.setDiffuseColor(Color.BROWN);
		wingL.setMaterial(wm);
		move(wingL, x - 60, y, z);
		Rotate wlRot = new Rotate(20, Rotate.Y_AXIS);
		wingL.getTransforms().addAll(wlRot, tilt);
		wingL.setRotationAxis(Rotate.Y_AXIS);
		pieces[1] = wingL;
		//
		wingR = new Box(160, 50, 400);
		wingR.setMaterial(wm);
		move(wingR, x + 60, y, z);
		wingR.setRotationAxis(Rotate.Y_AXIS);
		Rotate wrRot = new Rotate(-20, Rotate.Y_AXIS);
		wingR.getTransforms().addAll(wrRot, tilt);
		pieces[2] = wingR;
		//
		propel = new Box(400, 40, 10);
		propel.setMaterial(em);
		move(propel, x, y, z + 320);
		propel.setRotationAxis(Rotate.Y_AXIS);
		pRot = new Rotate(0, Rotate.Z_AXIS);
		propel.getTransforms().addAll(pRot, tilt);
		pieces[3] = propel;
		//
		pit = new Box(70, 140, 140);
		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseColor(Color.grayRgb(50));
		/*try {
			pm.setDiffuseMap(new Image(new FileInputStream("image.jpg")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		pit.setMaterial(pm);
		move(pit, x, y + 50, z + 170);
		pit.setRotationAxis(Rotate.Y_AXIS);
		Rotate cRot = new Rotate(45, Rotate.X_AXIS);
		pit.getTransforms().addAll(cRot, tilt);
		pieces[4] = pit;
		//
		tail = new Box(70, 170, 170);
		tail.setMaterial(wm);
		move(tail, x, y - 100, z - 200);
		tail.setRotationAxis(Rotate.Y_AXIS);
		Rotate tRot = new Rotate(30, Rotate.X_AXIS);
		tail.getTransforms().addAll(tRot, tilt);
		pieces[5] = tail;
		//
		sm[0] = new PhongMaterial();
		sm[0].setDiffuseColor(Color.grayRgb(100));
		sm[1] = new PhongMaterial();
		sm[1].setDiffuseColor(Color.rgb(255, 25, 0));
		for (int i = 0; i < smoke.length; i++) {
			smoke[i] = new Sphere(30);
			smoke[i].setMaterial(sm[1]);
			move(smoke[i], x, y, z);
			smokeRs[i] = -Math.random()*0.2 - 0.1;
		}
		//
		for (int i = 0; i < direc.length; i++) {
			direc[i] = i*Math.PI*2/direc.length + Math.random()*1;
		}
		//
		tilt = new Rotate(0, Rotate.Z_AXIS);
		for (Box b : pieces) {
			b.getTransforms().add(tilt);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void spawn(double dist) {
		spawn(px, pz, dist);
	}
	
	public void spawn(double x, double z, double dist) {
		double rot = Math.random()*Math.PI*2;
		this.x = x + dist*Math.sin(rot);
		this.z = z + dist*Math.cos(rot);
		r = Math.toDegrees(rot) + 100*((Math.random() > 0.5) ? 1 : -1);
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

	public void setData(double px, double pz, double pr, boolean pFiring) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		this.pFiring = pFiring;
	}
	
	private void regroup() {
		double rand = Math.random() - 0.5;
		rg = r + ((rand < 0) ? -1 : 1) * (45 + Math.random()*10);
		s = maxS;
		lastRm = System.currentTimeMillis();
	}
	
	public void changeHealth() {
		health -= damage;
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
	
	private boolean smokeOut() {
		boolean b = false;
		for (Sphere s : smoke) {
			if (s.getTranslateY() < 300) {
				b = true;
			}
		}
		return b;
	}
	
	private boolean smokeVisible() {
		boolean b = false;
		for (Sphere s : smoke) {
			if (s.getRadius() > 0) {
				b = true;
			}
		}
		return b;
	}
	
	private double angleWPlane() {
		return Math.toDegrees(Math.atan((pz-z)/(px-x)));
	}
	
	private boolean behindPlane() {
		return ((pr+3600000)%360)-(angleWPlane()) > 90;
	}

	@Override
	public void update() {
		if (!getDead()) {
			double realRot = body.getRotate() + (r-body.getRotate())/50;
			if (System.currentTimeMillis()-lastRm < 1000) {
				s = maxS;
			} else {
				s += (minS-s)/10;
			}
			x += s*Math.sin(Math.toRadians(r));
			z += s*Math.cos(Math.toRadians(r));
			r += (rg-r)/400;
			rDiff = r - lastR;
			//
			if (underFire() && System.currentTimeMillis() - lastRm > 8000) {
				regroup();
			}
			//
			tilt.setAngle(rDiff*100);
			//
			move(body, x, y, z);
			move(propel, x + 320*Math.sin(Math.toRadians(r)), y,
					z + 320*Math.cos(Math.toRadians(r)));
			pRot.setAngle(pRot.getAngle() + 3);
			move(wingL, x - 60*Math.cos(Math.toRadians(r)), y, z + 60*Math.sin(Math.toRadians(r)));
			move(wingR, x + 60*Math.cos(Math.toRadians(r)), y, z - 60*Math.sin(Math.toRadians(r)));
			move(pit, x + 170*Math.sin(Math.toRadians(r)), y - 50, z + 170*Math.cos(Math.toRadians(r)));
			move(tail, x - 250*Math.sin(Math.toRadians(r)), y - 70, z - 250*Math.cos(Math.toRadians(r)));
			for (Box b : pieces) {
				b.setRotate(realRot);
			}
			y += (300 - y) / 50;
			ys = -1;
			lastR = r;
			//
			if (health < 100) {
				if (System.currentTimeMillis()-lastSm > 100 || !smokeOut()) {
					sInd = (sInd+1) % smoke.length;
					smoke[sInd].setTranslateY(299);
					smokeRs[sInd] = -Math.random()*0.2 - 0.1;
					double diff = System.currentTimeMillis()-lastHm,
							timeFact = 1 - ((diff < 500) ? 0.5*diff/500 : 0.5);
					smoke[sInd].setRadius(timeFact*(80 + (1-health/90)*40));
					smoke[sInd].setMaterial((Math.random() > ((diff < 500) ? 0.8 : 0.2))
							? sm[0] : sm[1]);
					lastSm = System.currentTimeMillis();
				}
				for (int i = 0; i < smoke.length; i++) {
					Sphere s = smoke[i];
					double yPos = s.getTranslateY();
					if (yPos > -1500 && yPos < 300) {
						move(s, s.getTranslateX(), s.getTranslateY()-1, s.getTranslateZ());
						s.setRadius(s.getRadius()+smokeRs[sInd]);
					} else {
						move(s, x, 300, z);
					}
				}
			} else {
				for (Sphere s : smoke) {
					move(s, x, 300, z);
				}
			}
			//
			/*if (!behindPlane()) {
				pursueRand = Math.random();
			} else {
				if (pursueRand >= 0) {
					rg = angleWPlane();
					System.out.println(rg);
					tracking = true;
				}
			}*/
		} else {
			for (int i = 0; i < pieces.length; i++) {
				pieces[i].setRotationAxis(Rotate.Z_AXIS);
				if (body.getScaleX() > 0) {
					shrink(pieces[i], 0.0012);
				}
			}
			double bgty = body.getTranslateY();
			if (bgty < Double.MAX_VALUE) {
				ys += 0.005;
				for (int i = 0; i < pieces.length; i++) {
					move(pieces[i], pieces[i].getTranslateX() + Math.cos(direc[i]), bgty + ys,
							pieces[i].getTranslateZ() + Math.sin(direc[i]));
					pieces[i].setRotate(body.getRotate() + 0.1);
				}
			}
			if (smokeVisible()) {
				for (Sphere s : smoke) {
					s.setRadius(s.getRadius()-0.1);
					s.setTranslateY(s.getTranslateY()-0.1);
				}
			}
		}
	}
	
	public boolean tracking() {
		return tracking;
	}
	
	public boolean offScreen() {
		return body.getTranslateY() > 10000;
	}
	
	private void move(Node b, double x, double y, double z) {
		b.setTranslateX(x);
		b.setTranslateY(y);
		b.setTranslateZ(z);
	}
	
	private void shrink(Box b, double r) {
		b.setScaleX(b.getScaleX() - r);
		b.setScaleY(b.getScaleY() - r);
		b.setScaleZ(b.getScaleZ() - r);
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(body, propel, wingL, wingR, pit, tail);
		for (Sphere s : smoke) {
			g.getChildren().add(s);
		}
		return g;
	}

}