package com.github.steevedroz.stockandco.controller;

import java.io.IOException;

import com.github.steevedroz.phonebookjava.PhoneBook;
import com.github.steevedroz.stockandco.model.Company;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CompanyDisplay extends GridPane {
    @FXML
    private Label name;
    @FXML
    private Label owned;

    private Company company;

    public CompanyDisplay(Company company) {
	this.company = company;
	try {
	    FXMLLoader loader = new FXMLLoader(
		    getClass().getResource("/com/github/steevedroz/stockandco/view/CompanyDisplay.fxml"));
	    loader.setController(this);
	    loader.setRoot(this);
	    loader.load();
	} catch (IOException exception) {
	    exception.printStackTrace();
	}
    }

    @FXML
    private void click(MouseEvent event) {
	((StockAndCo) PhoneBook.call("game")).setActiveCompany(company);
    }

    public void update() {
	owned.setText(String.format("%1$d/%2$d (+%3$s)", company.getOwned(), company.getMaxOwned(),
		ToolBox.MONEY_FORMAT.format(company.getShare() * company.getOwned())));
    }

    @FXML
    private void initialize() {
	name.setText(company.getName());
    }

    public Company getCompany() {
	return company;
    }
}
