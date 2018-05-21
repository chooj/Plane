package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Map implements GameObject{
	
	private double px, pz, pr;
	private double[] ex, ez, er;
	private int[] ecd, ergb;
	private Polygon mark;
	private Circle[] enemy;
	private Line[] lines;
	private boolean[] ed, et;
	private Circle c;
	
	public Map(int eNum) {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 298, 298, Color.rgb(150, 200, 50));
		Stage stage = new Stage();
		stage.setX(115);
		stage.setY(150);
		stage.setWidth(300);
		stage.setHeight(300);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		//
		lines = new Line[32];
		for (int i = 0; i < lines.length; i++) {
			if (i < lines.length/2) {
				lines[i] = new Line(43*i, -100, 43*i, 400);
			} else {
				lines[i] = new Line(-100, 43*i, 400, 43*i);
			}
			lines[i].setStroke(Color.grayRgb(120));
		}
		ex = new double[eNum];
		ez = new double[eNum];
		er = new double[eNum];
		ecd = new int[eNum];
		ergb = new int[eNum];
		ed = new boolean[eNum];
		et = new boolean[eNum];
		//
		enemy = new Circle[eNum];
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = new Circle(0, 0, 5);
			enemy[i].setFill(Color.RED);
			ecd[i] = 1;
			ergb[i] = 1;
		}
		//
		mark = new Polygon();
		mark.getPoints().setAll(150.0, 135.0,
				142.0, 165.0,
				150.0, 160.0,
				158.0, 165.0);
		mark.setFill(Color.ORANGE);
		//
		c = new Circle(150, 150, 180);
		c.setFill(Color.TRANSPARENT);
		c.setStroke(Color.grayRgb(100));
		c.setStrokeWidth(70);
		//
		pane.getChildren().addAll(components());
	}
	
	public void setData(double px, double pz, double pr, double[] ex, double[] ez,
			double[] er, boolean[] ed, boolean[] et) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		for (int i = 0; i < ex.length; i++) {
			this.ex[i] = ex[i];
			this.ez[i] = ez[i];
			this.er[i] = er[i];
			this.ed[i] = ed[i];
			this.et[i] = et[i];
		}
	}
	
	public double dist(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	@Override
	public void update() {
		for (int i = 0; i < lines.length; i++) {
			if (i < lines.length/2) {
				double x = (43*i - px/50 + 34300000) % 343;
				lines[i].setStartX(x);
				lines[i].setEndX(x);
			} else {
				double y = (43*i + pz/50 + 34300000) % 343;
				lines[i].setStartY(y);
				lines[i].setEndY(y);
			}
		}
		mark.setRotate(pr);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].setTranslateX(150 + (ex[i]-px)/200);
			enemy[i].setTranslateY(150 - (ez[i]-pz)/200);
			double norm = dist(enemy[i].getTranslateX(), 150, enemy[i].getTranslateY(), 150);
			if (et[i]) {
				enemy[i].setFill(Color.rgb(255, ergb[i]+=ecd[i], ergb[i]));
				if (ergb[i] > 254 || ergb[i] < 1) {
					ecd[i] *= -1;
				}
			} else {
				enemy[i].setFill(Color.RED);
				ergb[i] = 1;
				ecd[i] = 1;
			}
			if (norm > 145) {
				enemy[i].setTranslateX(150 + 145*(enemy[i].getTranslateX()-150)/norm);
				enemy[i].setTranslateY(150 + 145*(enemy[i].getTranslateY()-150)/norm);
			}
			if (ed[i] && enemy[i].getOpacity() > 0) {
				enemy[i].setOpacity(enemy[i].getOpacity() - 0.001);
			}
		}
	}

	@Override
	public Group components() {
		Group g = new Group();
		for (Line l : lines) {
			g.getChildren().add(l);
		}
		g.getChildren().add(c);
		for (Circle e : enemy) {
			g.getChildren().add(e);
		}
		g.getChildren().add(mark);
		return g;
	}

}