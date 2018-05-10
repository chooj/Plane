package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Panel implements GameObject {
	
	private Text ammoT;
	private Text[] placeT, messageT;
	private double boost;
	private int ammo;
	private Rectangle boostBar;
	
	public Panel() {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 298, 298, Color.grayRgb(100));
		Stage stage = new Stage();
		stage.setX(1025);
		stage.setY(150);
		stage.setWidth(300);
		stage.setHeight(300);
		stage.setScene(scene);
		stage.setAlwaysOnTop(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		//
		String[] texts = {"Boost", "Ammo"};
		String[] mTexts = {"Press SHIFT to use", "Press R to reload"};
		placeT = new Text[2];
		messageT = new Text[2];
		for (int i = 0; i < placeT.length; i++) {
			placeT[i] = new Text(texts[i]);
			placeT[i].setFont(new Font(18));
			placeT[i].setFill(Color.WHITE);
			placeT[i].setY(40 + i*150);
			placeT[i].setX(150 - placeT[i].getLayoutBounds().getWidth()/2);
			messageT[i] = new Text(mTexts[i]);
			messageT[i].setFont(new Font(15));
			messageT[i].setFill(Color.WHITE);
			messageT[i].setY(130 + i*150);
			messageT[i].setX(150 - messageT[i].getLayoutBounds().getWidth()/2);
		}
		//
		boostBar = new Rectangle(80, 60, 140, 40);
		boostBar.setFill(Color.rgb(0, 255, 0));
		//
		ammoT = new Text("30 / 30");
		ammoT.setFont(new Font(30));
		ammoT.setX(150 - ammoT.getLayoutBounds().getWidth()/2);
		ammoT.setY(240);
		ammoT.setFill(Color.WHITE);
		//
		Line l = new Line(0, 150, 300, 150);
		l.setStroke(Color.WHITE);
		Rectangle blank = new Rectangle(80, 60, 140, 40);
		blank.setFill(Color.TRANSPARENT);
		blank.setStroke(Color.BLACK);
		blank.setStrokeWidth(3);
		pane.getChildren().addAll(components(), l, blank);
	}
	
	public void setData(double boost, double ammo) {
		this.boost = boost;
		this.ammo = (int) ammo;
	}

	@Override
	public void update() {
		boostBar.setFill(Color.rgb((int) (255*(1-boost/100)), (int) (51*boost/20), 0));
		boostBar.setWidth(7*boost/5);
		ammoT.setText(ammo + " / 30");
		ammoT.setX(150 - ammoT.getLayoutBounds().getWidth()/2);
	}

	@Override
	public Group components() {
		Group g = new Group();
		for (int i = 0; i < placeT.length; i++) {
			g.getChildren().addAll(placeT[i], messageT[i]);
		}
		g.getChildren().addAll(boostBar, ammoT);
		return g;
	}

}