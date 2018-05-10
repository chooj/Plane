package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Map implements GameObject{
	
	private double px, pz, pr;
	private double[] ex, ez, er;
	private Polygon mark;
	private Polygon[] enemy;
	private Line[] lines;
	private boolean[] ed;
	
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
		ed = new boolean[eNum];
		//
		enemy = new Polygon[eNum];
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = new Polygon();
			enemy[i].getPoints().setAll(0.0, -15.0,
					-8.0, 15.0,
					0.0, 10.0,
					8.0, 15.0);
			enemy[i].setFill(Color.RED);
		}
		//
		mark = new Polygon();
		mark.getPoints().setAll(150.0, 135.0,
				142.0, 165.0,
				150.0, 160.0,
				158.0, 165.0);
		mark.setFill(Color.ORANGE);
		//
		pane.getChildren().add(components());
	}
	
	public void setData(double px, double pz, double pr, double[] ex, double[] ez,
			double[] er, boolean[] ed) {
		this.px = px;
		this.pz = pz;
		this.pr = pr;
		for (int i = 0; i < ex.length; i++) {
			this.ex[i] = ex[i];
			this.ez[i] = ez[i];
			this.er[i] = er[i];
			this.ed[i] = ed[i];
		}
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
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].setTranslateX(150 + (ex[i]-px)/100);
			enemy[i].setTranslateY(150 - (ez[i]-pz)/100);
			if (enemy[i].getTranslateX() < 20) {
				enemy[i].setTranslateX(20);
			} else if (enemy[i].getTranslateX() > 280) {
				enemy[i].setTranslateX(280);
			}
			if (enemy[i].getTranslateY() < 20) {
				enemy[i].setTranslateY(20);
			} else if (enemy[i].getTranslateY() > 280) {
				enemy[i].setTranslateY(280);
			}
			enemy[i].setRotate(enemy[i].getRotate() + (er[i]-enemy[i].getRotate())/80);
			mark.setRotate(pr);
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
		for (Polygon e : enemy) {
			g.getChildren().add(e);
		}
		g.getChildren().add(mark);
		return g;
	}

}