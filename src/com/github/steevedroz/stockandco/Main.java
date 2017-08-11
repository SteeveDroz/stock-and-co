package com.github.steevedroz.stockandco;

import com.github.steevedroz.stockandco.controller.StockAndCo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
	try {
	    StockAndCo root = new StockAndCo();
	    Scene scene = new Scene(root, 600, 400);
	    scene.getStylesheets().add(
		    getClass().getResource("/com/github/steevedroz/stockandco/view/application.css").toExternalForm());
	    primaryStage.setScene(scene);
//	    primaryStage.setResizable(false);
	    primaryStage.show();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	launch(args);
    }
}
