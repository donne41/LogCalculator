package com.example.logcalculator;

public class Yield {
    private int twoInches;
    private int oneInches;
    private double spill;

    public Yield(int amountTwo, int amountOne, double spill) {
        twoInches = amountTwo;
        oneInches = amountOne;
        this.spill = spill;
    }

    public Yield(int amountTwo, int amountOne) {
        this(amountTwo, amountOne, 0);
    }

    public Yield() {
        this(0, 0, 0);
    }

    public int getTwoInches() {
        return twoInches;
    }

    public void setTwoInches(int twoInches) {
        this.twoInches = twoInches;
    }

    public int getOneInches() {
        return oneInches;
    }

    public void setOneInches(int oneInches) {
        this.oneInches = oneInches;
    }

    public double getSpill() {
        return spill;
    }

    public void setSpill(double spill) {
        this.spill = spill;
    }
}
