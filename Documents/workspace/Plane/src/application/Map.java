package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Map implements GameObject {
	
	private double x, y;
	private Circle mark;
	private Rectangle map;
	private Line[] lines;
	
	public Map() {
		x = 0;
		y = 0;
		mark = new Circle(3);
		mark.setFill(Color.ORANGE);
		mark.setTranslateX(570);
		mark.setTranslateY(30);
		map = new Rectangle(550, 10, 40, 40);
		map.setFill(Color.WHITE);
		lines = new Line[10];
		for (int i = 0; i < lines.length/2; i++) {
			lines[i] = new Line(550 + i*10, 10, 550 + i*10, 50);
			lines[i].setStroke(Color.grayRgb(40));
			lines[i].setStrokeWidth(2);
		}
		for (int i = lines.length/2; i < lines.length; i++) {
			lines[i] = new Line(550, 10 + (i-5)*10, 590, 10 + (i-5)*10);
			lines[i].setStroke(Color.grayRgb(40));
			lines[i].setStrokeWidth(2);
		}
	}
	
	public void setMarkX(double x) {
		this.x = x;
	}
	
	public void setMarkY(double y) {
		this.y = y;
	}
	
	public void configure(double x, double z, double r) {
		setConfiguration(map, x, z, r);
		setConfiguration(mark, x, z, r);
		for (Line l : lines) {
			setConfiguration(l, x, z, r);
		}
	}
	
	public void setConfiguration(Node n, double x, double z, double r) {
		n.setTranslateX(x);
		n.setTranslateZ(z);
		n.setRotationAxis(Rotate.Y_AXIS);
		n.setRotate(r);
	}

	@Override
	public void update() {
		mark.setTranslateX(x/250 + 570);
		mark.setTranslateY(y/250 + 30);
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().add(map);
		for (Line l : lines) {
			g.getChildren().add(l);
		}
		g.getChildren().add(mark);
		return g;
	}

}
