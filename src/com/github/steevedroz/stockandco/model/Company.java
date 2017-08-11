package com.github.steevedroz.stockandco.model;

import java.util.Random;

import com.github.steevedroz.util.DoubleUtil;

public class Company {
    private static final Random RANDOM = new Random();
    private String name;
    private double price;
    private double commission;
    private double share;
    private double tendency;
    private double min;
    private double max;
    private double savedTendency;

    private int owned;
    private int maxOwned;

    public Company(String name, double price, double share) {
	this.name = name;
	this.price = price;
	this.commission = 10;
	this.share = share;
	this.maxOwned = 10;
	this.min = this.price / 2;
	this.max = 3 * this.min;
	this.tendency = 0;
	this.owned = 0;
    }

    public void addSavedTendency(double value) {
	savedTendency = Math.min(Math.max(savedTendency + value, -1), 1);
    }

    public String getName() {
	return name;
    }

    public double getPrice() {
	return price;
    }

    public double getCommission() {
	return commission;
    }

    public double getShare() {
	return share;
    }

    public double getTendency() {
	return tendency;
    }

    public double getSavedTendency() {
	return savedTendency;
    }

    public int getOwned() {
	return owned;
    }

    public int getMaxOwned() {
	return maxOwned;
    }

    public void setMaxOwned(int maxOwned) {
	this.maxOwned = maxOwned;
    }

    public void update() {
	updateTendency();
	price += 10 * (savedTendency / 4 + tendency / 2);
	savedTendency = DoubleUtil.weightedAverage(savedTendency, tendency, 0.1);
    }

    private void updateTendency() {
	double percentage = (price - min) / (max - min);
	tendency = RANDOM.nextDouble() - percentage;
    }

    public double getDisplayRatio(double height) {
	return height / (4 * min);
    }

    public double buy(double money, int amount) throws NotEnoughMoneyException {
	if (money < price * amount) {
	    throw new NotEnoughMoneyException();
	}
	amount = Math.min(amount, maxOwned - owned);
	owned += amount;
	return money - price * amount;
    }

    public double buy(double money) throws NotEnoughMoneyException {
	return buy(money, 1);
    }

    public double buyAll(double money) throws NotEnoughMoneyException {
	return buy(money, (int) (money / price));
    }

    public double sell(int amount) throws NotEnoughStockException {
	if (owned < amount) {
	    throw new NotEnoughStockException();
	}
	owned -= amount;
	return amount * Math.max(price - commission, 0);
    }

    public double sell() throws NotEnoughStockException {
	return sell(1);
    }

    public double sellAll() throws NotEnoughStockException {
	return sell(owned);
    }
}
