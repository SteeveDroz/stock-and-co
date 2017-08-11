package com.github.steevedroz.stockandco.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.steevedroz.phonebookjava.PhoneBook;
import com.github.steevedroz.stockandco.model.Company;
import com.github.steevedroz.stockandco.model.NotEnoughMoneyException;
import com.github.steevedroz.stockandco.model.NotEnoughStockException;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class StockAndCo extends BorderPane {
    public static final long TIME_INTERVAL = (long) 1e9;

    private long lastUpdate;

    @FXML
    private Graph graph;
    @FXML
    private Label tendency;
    @FXML
    private ToolBox toolBox;

    private List<Company> nextCompanies;
    private List<Company> companies;
    private AnimationTimer timer;

    private double money;

    private Company activeCompany;
    private double marketImpact;

    public StockAndCo() {
	PhoneBook.addEntry("game", this);
	lastUpdate = 0;
	money = 20;
	nextCompanies = new ArrayList<Company>();
	companies = new ArrayList<Company>();

	nextCompanies.add(new Company("Pebbles", 20, 0.1));
	nextCompanies.add(new Company("Wood", 35, 0.2));
	nextCompanies.add(new Company("Iron", 100, 0.5));
	nextCompanies.add(new Company("Jewels", 1000, 1));
	nextCompanies.add(new Company("Marvelium", 10000, 10));
	Company theWorld = new Company("The World", 1000000000, 1000);
	theWorld.setMaxOwned(1);
	nextCompanies.add(theWorld);

	timer = new AnimationTimer() {

	    @Override
	    public void handle(long now) {
		update(now);
	    }
	};
	timer.start();

	marketImpact = 0.01;

	try {
	    FXMLLoader loader = new FXMLLoader(
		    getClass().getResource("/com/github/steevedroz/stockandco/view/StockAndCo.fxml"));
	    loader.setController(this);
	    loader.setRoot(this);
	    loader.load();
	} catch (IOException exception) {
	    exception.printStackTrace();
	}
    }

    @FXML
    private void initialize() {
	addCompany();
	setActiveCompany(companies.get(0));
    }

    @FXML
    private void up(ActionEvent event) {
	activeCompany.addSavedTendency(marketImpact);
	tendency.setText("" + activeCompany.getSavedTendency());
    }

    @FXML
    private void down(ActionEvent event) {
	activeCompany.addSavedTendency(-marketImpact);
	tendency.setText("" + activeCompany.getSavedTendency());

    }

    @FXML
    private void buyOne(ActionEvent event) {
	try {
	    money = activeCompany.buy(money);
	    toolBox.setMoney(money);
	    toolBox.update();
	} catch (NotEnoughMoneyException e) {
	    // TODO Not enough money
	}
    }

    @FXML
    private void buyAll(ActionEvent event) {
	try {
	    money = activeCompany.buyAll(money);
	    toolBox.setMoney(money);
	    toolBox.update();
	} catch (NotEnoughMoneyException e) {
	}
    }

    @FXML
    private void sellOne(ActionEvent event) {
	try {
	    money += activeCompany.sell();
	    toolBox.setMoney(money);
	    toolBox.update();
	} catch (NotEnoughStockException e) {
	}
    }

    @FXML
    private void sellAll(ActionEvent event) {
	try {
	    money += activeCompany.sellAll();
	    toolBox.setMoney(money);
	    toolBox.update();
	} catch (NotEnoughStockException e) {
	}
    }

    @FXML
    private void improveImpact(ActionEvent event) {
	if (money >= marketImpact * 1000 && marketImpact < 2.0) {
	    money -= marketImpact * 1000;
	    marketImpact = Math.min(marketImpact + 0.01, 1);
	    toolBox.setMoney(money);
	}
    }

    public List<Company> getCompanies() {
	return companies;
    }

    public Company getActiveCompany() {
	return activeCompany;
    }

    public double getMarketImpact() {
	return marketImpact;
    }

    public void setActiveCompany(Company activeCompany) {
	this.activeCompany = activeCompany;
	graph.wipe();
	toolBox.select(activeCompany);
    }

    private void addCompany() {
	for (Company company : companies) {
	    company.setMaxOwned(company.getMaxOwned() * 2);
	}
	companies.add(nextCompanies.remove(0));
	toolBox.setCompanies(companies);
	toolBox.select(activeCompany);
	toolBox.update();
    }

    private void update(long now) {
	graph.update(activeCompany, now);
	tendency.setText("" + Math.round(100 * activeCompany.getSavedTendency()) / 100.0);
	if (lastUpdate + TIME_INTERVAL <= now) {
	    lastUpdate = now;
	    for (Company company : companies) {
		money += company.getShare() * company.getOwned();
	    }
	    money += marketImpact;
	    toolBox.setMoney(money);

	    if (allCompaniesAreFull()) {
		addCompany();
	    }
	}
    }

    private boolean allCompaniesAreFull() {
	for (Company company : companies) {
	    if (company.getOwned() < company.getMaxOwned()) {
		return false;
	    }
	}
	return true;
    }
}
