package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Joystick implements GameObject {

	private  String acc, direc, time;
	private boolean boost, firing;
	private Circle joystick, fire;
	private Rectangle b1, b2;
	private Text timeT, scoreT;
	private double a = 0, r = 0;
	private int score = 0;
	
	public Joystick() {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 298, 298, Color.grayRgb(100));
		Stage stage = new Stage();
		stage.setX(1025);
		stage.setY(455);
		stage.setWidth(300);
		stage.setHeight(300);
		stage.setScene(scene);
		stage.setAlwaysOnTop(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		//
		joystick = new Circle(20);
		joystick.setCenterX(100);
		joystick.setCenterY(100);
		joystick.setFill(Color.grayRgb(80));
		//
		fire = new Circle(10);
		fire.setCenterX(100);
		fire.setCenterY(100);
		fire.setFill(Color.RED);
		//
		b1 = new Rectangle(242, 150, 16, 0);
		b1.setFill(Color.grayRgb(100));
		b2 = new Rectangle(220, 140, 60, 20);
		b2.setFill(Color.grayRgb(120));
		//
		timeT = new Text("");
		timeT.setFont(Font.font("Monospaced", 30));
		timeT.setFill(Color.FIREBRICK);
		timeT.setY(100);
		//
		scoreT = new Text("");
		scoreT.setFont(Font.font("Monospaced", 30));
		scoreT.setFill(Color.FIREBRICK);
		scoreT.setY(50);
		//
		Circle c = new Circle(80);
		c.setCenterX(100);
		c.setCenterY(200);
		c.setFill(Color.grayRgb(40));
		//
		Rectangle r = new Rectangle(240, 50, 20, 200);
		r.setFill(Color.grayRgb(40));
		//
		Rectangle r2 = new Rectangle(20, 70, 120, 40);
		r2.setFill(Color.grayRgb(40));
		//
		Rectangle r3 = new Rectangle(20, 20, 120, 40);
		r3.setFill(Color.grayRgb(40));
		//
		pane.getChildren().addAll(c, r, r2, r3, components());
	}
	
	public void setData(String acc, String direc, boolean boost, boolean firing, String time, int score) {
		this.acc = acc;
		this.direc = direc;
		this.boost = boost;
		this.firing = firing;
		this.time = time;
		this.score = score;
	}
	
	@Override
	public void update() {
		double rot = 0, amp = 0;
		if (!acc.equals("") || !direc.equals("")) {
			amp = 50;
		}
		if (direc.equals("r")) {
			rot = acc.equals("a") ? 40 : acc.equals("d") ? 140 : 90;
		} else if (direc.equals("l")) {
			rot = acc.equals("a") ? -40 : acc.equals("d") ? 220 : 270;
		} else if (acc.equals("d")) {
			rot = 180;
		}
		a += (amp-a)/100;
		r += (rot-r)/100;
		joystick.setCenterX(100 + a*Math.sin(Math.toRadians(r)));
		joystick.setCenterY(200 - a*Math.cos(Math.toRadians(r)));
		fire.setCenterX(joystick.getCenterX());
		fire.setCenterY(joystick.getCenterY());
		fire.setFill(firing ? Color.FIREBRICK : Color.RED);
		//
		b2.setY(b2.getY() + ((boost ? 80 : 140)-b2.getY())/300);
		b1.setY(b2.getY());
		b1.setHeight(150 - b2.getY());
		//
		timeT.setText(time);
		timeT.setX(135 - timeT.getLayoutBounds().getWidth());
		//
		scoreT.setText(String.valueOf(score));
		while (scoreT.getText().length() < 5) {
			scoreT.setText("0" + scoreT.getText());
		}
		scoreT.setX(135 - scoreT.getLayoutBounds().getWidth());
	}

	@Override
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(joystick, fire, b1, b2, timeT, scoreT);
		return g;
	}

}