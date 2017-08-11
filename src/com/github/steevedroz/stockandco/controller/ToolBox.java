package com.github.steevedroz.stockandco.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.steevedroz.phonebookjava.PhoneBook;
import com.github.steevedroz.stockandco.model.Company;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ToolBox extends VBox {
    public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");
    private Label moneyLabel;
    private List<CompanyDisplay> companyDisplays;

    public ToolBox() {
	companyDisplays = new ArrayList<CompanyDisplay>();
	moneyLabel = new Label();
	getChildren().add(moneyLabel);
    }

    public double getTotalIncome() {
	double totalIncome = 0;
	for (CompanyDisplay companyDisplay : companyDisplays) {
	    totalIncome += companyDisplay.getCompany().getOwned() * companyDisplay.getCompany().getShare();
	}
	return totalIncome + ((StockAndCo) PhoneBook.call("game")).getMarketImpact();
    }

    public void setMoney(double money) {
	moneyLabel.setText(
		String.format("%1$s (+%2$s)", MONEY_FORMAT.format(money), MONEY_FORMAT.format(getTotalIncome())));
    }

    public void setCompanies(List<Company> companies) {
	getChildren().removeAll(companyDisplays);
	companyDisplays = new ArrayList<CompanyDisplay>();

	for (Company company : companies) {
	    CompanyDisplay companyDisplay = new CompanyDisplay(company);
	    companyDisplays.add(companyDisplay);
	    getChildren().add(companyDisplay);
	}
    }

    public void update() {
	for (CompanyDisplay companyDisplay : companyDisplays) {
	    companyDisplay.update();
	}
    }

    public void select(Company activeCompany) {
	for (CompanyDisplay companyDisplay : companyDisplays) {
	    if (companyDisplay.getCompany() == activeCompany) {
		companyDisplay.getStyleClass().add("selected");
	    } else {
		companyDisplay.getStyleClass().remove("selected");
	    }
	}
    }
}
