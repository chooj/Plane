package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Meter implements GameObject {

	private Polygon tilt;
	private Rectangle[] back;
	private Text[] backT, dataT;
	private Line[] horizL;
	private double pr, ps, pa;
	
	public Meter() {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 298, 298, Color.grayRgb(100));
		Stage stage = new Stage();
		stage.setX(115);
		stage.setY(455);
		stage.setWidth(300);
		stage.setHeight(300);
		stage.setScene(scene);
		stage.setAlwaysOnTop(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		//
		Rectangle rect = new Rectangle(50, 50, 120, 200);
		rect.setArcWidth(120);
		rect.setArcHeight(120);
		rect.setFill(Color.rgb(35,  222,  224));
		//
		tilt = new Polygon();
		tilt.getPoints().addAll(new Double[] {50.0, 150.0, 50.0, 191.0});
		for (int i = 0; i < 180; i++) {
			tilt.getPoints().addAll(new Double[] {110.0 + 60*Math.cos(Math.toRadians(i)),
					190.0 + 60*Math.sin(Math.toRadians(i))});
		}
		tilt.getPoints().addAll(new Double[] {170.0, 190.0, 170.0, 150.0});
		tilt.setFill(Color.rgb(173,  52,  0));
		//
		String[] texts = {"Speed", "Direction", "Tilt"};
		back = new Rectangle[3];
		backT = new Text[3];
		dataT = new Text[3];
		horizL = new Line[3];
		for (int i = 0; i < back.length; i++) {
			back[i] = new Rectangle(200, 50 + i*80, 80, 50);
			back[i].setFill(Color.grayRgb(50));
			//
			backT[i] = new Text(texts[i]);
			backT[i].setFont(new Font(10));
			backT[i].setFill(Color.grayRgb(240));
			backT[i].setTranslateX(240 - backT[i].getLayoutBounds().getWidth()/2);
			backT[i].setTranslateY(70 + i*80);
			//
			dataT[i] = new Text();
			dataT[i].setFont(new Font(12));
			dataT[i].setFill(Color.grayRgb(240));
			dataT[i].setTranslateY(88 + i*80);
			//
			horizL[i] = new Line(70, 110 + i*40, 150, 110 + i*40);
			horizL[i].setStroke(Color.grayRgb(255));
		}
		//
		Line l = new Line(110, 51, 110, 249);
		l.setStroke(Color.grayRgb(255));
		//
		pane.getChildren().addAll(rect, tilt, components(), l);
	}
	
	public void setData(double pr, double ps, double pa) {
		this.pr = pr;
		this.ps = ps;
		this.pa = pa;
	}
	
	@Override
	public void update() {
		dataT[0].setText(((int) (ps*2000))/10 + " m/s");
		dataT[1].setText(((((int) pr*10)/10 + 36000000) % 360) + "º");
		dataT[2].setText(((int) pa*10)/10 + "º");
		for (Text t : dataT) {
			t.setTranslateX(240 - t.getLayoutBounds().getWidth()/2);
		}
		tilt.getPoints().set(1, 150.0 + 2*pa);
		tilt.getPoints().set(tilt.getPoints().size()-1, 150.0 - 2*pa);
	}

	@Override
	public Group components() {
		Group g = new Group();
		for (int i = 0; i < back.length; i++) {
			g.getChildren().addAll(back[i], backT[i], dataT[i], horizL[i]);
		}
		return g;
	}

}