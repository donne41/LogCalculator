package com.example.logcalculator;

public class Yield {
    private double blockSize;
    private int preferredPlank;
    private int oneInches;
    private double spill;
    private double preferredThickness;

    public Yield(int amountPreferred, int amountOne, double spill, double blockSize, double thickness) {
        preferredPlank = amountPreferred;
        oneInches = amountOne;
        this.spill = spill;
        this.blockSize = blockSize;
        preferredThickness = thickness;
    }

    public Yield(int amountOne, double spill, double blockSize, double preferredThickness) {
        this(0, amountOne, spill, blockSize, preferredThickness);
    }

    public Yield() {
        this(0, 0, 0, 0);
    }

    public double getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(double blockSize) {
        this.blockSize = blockSize;
    }

    public int getPreferredPlank() {
        return preferredPlank;
    }

    public void setPreferredPlank(int preferredPlank) {
        this.preferredPlank = preferredPlank;
    }

    public double getPreferredThickness() {
        return preferredThickness;
    }

    public void setPreferredThickness(double preferredThickness) {
        this.preferredThickness = preferredThickness;
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
