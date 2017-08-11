package com.github.steevedroz.stockandco.controller;

import java.util.ArrayList;
import java.util.List;

import com.github.steevedroz.stockandco.model.Company;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Graph extends Pane {
    private long lastUpdate;

    private List<Circle> circles;
    private Label priceTag;

    public Graph() {
	lastUpdate = 0;
	circles = new ArrayList<Circle>();
	priceTag = new Label();
	getChildren().add(priceTag);
    }

    public void update(Company company, long now) {
	if (lastUpdate + StockAndCo.TIME_INTERVAL <= now) {
	    lastUpdate = now;

	    company.update();
	    addPoint(company);
	}
	moveCircles();
    }

    private void moveCircles() {
	List<Circle> toBeRemoved = new ArrayList<Circle>();
	for (Circle circle : circles) {
	    circle.setCenterX(circle.getCenterX() - 0.2);
	    if (circle.getCenterX() < -circle.getRadius() / 2) {
		toBeRemoved.add(circle);
	    }
	}

	for (Circle circle : toBeRemoved) {
	    getChildren().remove(circle);
	    circles.remove(circle);
	}
    }

    private void addPoint(Company company) {
	Circle sellCircle = new Circle(getWidth() - 100,
		getHeight() - (company.getPrice() - company.getCommission()) * company.getDisplayRatio(getHeight()), 2,
		Color.GREEN);
	circles.add(sellCircle);
	getChildren().add(sellCircle);
	Circle buyCircle = new Circle(getWidth() - 100,
		getHeight() - company.getPrice() * company.getDisplayRatio(getHeight()), 2, Color.RED);
	circles.add(buyCircle);
	getChildren().add(buyCircle);

	priceTag.setText(String.format("§%1$d (%2$s)", Math.round(company.getPrice()), company.getName()));
	priceTag.setLayoutX(getWidth() - 100);
	priceTag.setLayoutY(getHeight() - company.getPrice() * company.getDisplayRatio(getHeight()));
    }

    public void wipe() {
	for (Circle circle : circles) {
	    getChildren().remove(circle);
	}
	circles.clear();
	priceTag.setText("");
    }
}
