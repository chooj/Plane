package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Scope implements GameObject {
	
	private double pt;
	private boolean boosting;
	private Circle big, lil;
	private Rectangle fire;
	
	public Scope() {
		big = new Circle(20);
		big.setFill(Color.TRANSPARENT);
		big.setStroke(Color.RED);
		big.setStrokeWidth(2);
		big.setCenterX(300);
		big.setCenterY(300);
		lil = new Circle(2);
		lil.setCenterX(300);
		lil.setCenterY(300);
		lil.setFill(Color.RED);
		fire = new Rectangle(290, 600, 0, 80);
		fire.setFill(Color.WHITE);
	}
	
	public void setData(double pt, boolean boosting, boolean shoot) {
		this.pt = pt;
		this.boosting = boosting;
		if (shoot) {
			fire.setX(300);
			fire.setY(600);
			fire.setWidth(20);
			fire.setHeight(80);
		}
		
	}

	@Override
	public void update() {
		double rand1 = (boosting) ? (Math.random()-0.5)*3 : 0,
				rand2 = (boosting) ? (Math.random()-0.5)*3 : 0;
		big.setCenterX(300 - 10*pt + rand1);
		big.setCenterY(300 + rand2);
		lil.setCenterX(big.getCenterX());
		lil.setCenterY(big.getCenterY());
		if (fire.getWidth() > 0) {
			fire.setRotate((big.getCenterX()-300)/10);
			fire.setX(fire.getX() + (big.getCenterX()-fire.getX())/80);
			fire.setY(fire.getY()-2);
			fire.setWidth(fire.getWidth()-0.15);
			fire.setHeight(fire.getHeight()+0.3);
			fire.setArcHeight(fire.getHeight()/2);
			fire.setArcWidth(fire.getWidth()/2);
		}
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(big, lil/*, fire*/);
		return g;
	}
	
}