package com.example.logcalculator;

public class SquareCalculator {

    double doubleDiaBig;
    double doubleDiaSmall;
    double cutWidth = 0.25;
    boolean inputMetric = true;
    boolean outputMetric = false;
    int preferredThickness = 1;

    public SquareCalculator(double bigDia, double smallDia) {
        doubleDiaBig = bigDia;
        doubleDiaSmall = smallDia;
    }

    public SquareCalculator() {
        doubleDiaBig = 0;
        doubleDiaSmall = 0;
    }

    private double convertToMetric(double inches) {
        return inches * 2.54;
    }

    private double convertToImperial(double cm) {
        return cm / 2.54;
    }

    private double roundToQuarter(double length) {
        return Math.round((length * 4) / 4);
    }

    public void setInputMetric(boolean inputMetric) {
        this.inputMetric = inputMetric;
    }

    public void setOutputMetric(boolean outputMetric) {
        this.outputMetric = outputMetric;
    }

    public void setPreferredThickness(int preferredThickness) {
        this.preferredThickness = preferredThickness;
    }

    public Yield getYield(double biggestDia, double smallestDia) {
        if (inputMetric) {
            doubleDiaBig = biggestDia;
            doubleDiaSmall = smallestDia;
        } else {
            doubleDiaBig = convertToMetric(biggestDia);
            doubleDiaSmall = convertToMetric(smallestDia);
        }
        double imperialSquare = roundToQuarter(convertToImperial(getEllipsSquare()));
        Yield commonYield = new Yield();
        if (preferredThickness == 1) {
            Yield plankYield = calculatePlanksOfOneInch(imperialSquare);
            commonYield.setOneInches(plankYield.getOneInches());
            commonYield.setSpill(plankYield.getSpill());
            commonYield.setPreferredThickness(preferredThickness);
        } else {
            Yield preferredYield = calculatePreferredPlanks(imperialSquare, preferredThickness);
            commonYield.setPreferredPlank(preferredYield.getPreferredPlank());
            commonYield.setOneInches(preferredYield.getOneInches());
            commonYield.setSpill(preferredYield.getSpill());
            commonYield.setPreferredThickness(preferredThickness);
        }
        if (outputMetric) {
            commonYield.setBlockSize(convertToMetric(imperialSquare));
            return commonYield;
        } else {
            commonYield.setBlockSize(imperialSquare);
            return commonYield;
        }
    }


    public double getEllipsSquare() {
        double radBig = doubleDiaBig / 2;
        double radSmall = doubleDiaSmall / 2;
        double divTop = 2 * radBig * radSmall;
        double divBottom = Math.sqrt(radBig * radBig + radSmall * radSmall);

        return divTop / divBottom;
    }

    public Yield calculatePlanksOfOneInch(double blockSize) {
        int preferredThickness = 1;
        int amountOneInch = (int) ((blockSize + cutWidth) / (1 + cutWidth));
        int cutAmount = amountOneInch - 1;
        double spill = blockSize - (amountOneInch + cutAmount * 0.25);
        return new Yield(amountOneInch, spill, blockSize, preferredThickness);
    }

    public Yield calculatePreferredPlanks(double blockSize, int preferredThickness) {
        int amountOfPreferred = (int) ((blockSize + cutWidth) / (preferredThickness + cutWidth));
        double rest = blockSize - amountOfPreferred * preferredThickness - ((amountOfPreferred - 1) * 0.25);
        int amountOneInch = 0;
        int cutAmount;
        double spill = 0;
        if (rest != 0) {
            amountOneInch = (int) ((rest + cutWidth) / (1 + cutWidth));
            cutAmount = amountOfPreferred + amountOneInch - 1;
            spill = blockSize - ((amountOfPreferred * preferredThickness + amountOneInch) + cutAmount * 0.25);
        } else {
            cutAmount = amountOfPreferred - 1;
            spill = blockSize - (amountOfPreferred * preferredThickness + cutAmount * 0.25);
        }
        return new Yield(amountOfPreferred, amountOneInch, spill, blockSize, preferredThickness);

    }

    public Yield calculateBestYield(double blockSize) {
        int preferredThickness = 2;
        int amountOfPreffered = (int) ((blockSize + cutWidth) / (preferredThickness + cutWidth));
        int amountOneInch = 0;
        double possibleOneInch;
        boolean evenOneInch = false;
        int cutAmount;
        double spill;
        double rest = blockSize - amountOfPreffered * preferredThickness - ((amountOfPreffered - 1) * 0.25);
        while (rest != 0 && amountOfPreffered > 0 && !evenOneInch) {
            possibleOneInch = (rest) % (1 + cutWidth);
            if (possibleOneInch == 0 || possibleOneInch == 1.0) {
                amountOneInch = (int) ((rest + cutWidth) / (1 + cutWidth));
                cutAmount = amountOfPreffered + amountOneInch - 1;
                spill = blockSize - ((amountOfPreffered * preferredThickness + amountOneInch) + cutAmount * 0.25);
                if (spill > 0 || spill < 0) {
                    evenOneInch = false;
                } else {
                    evenOneInch = true;
                    continue;
                }
            }
            if (amountOfPreffered % 2 != 0) {
                rest += 0.25;
            }
            amountOfPreffered--;
            rest += preferredThickness;

        }
        if (amountOfPreffered == 0) {
            amountOneInch = (int) ((blockSize + cutWidth) / (1 + cutWidth));
        }
        cutAmount = amountOfPreffered + amountOneInch - 1;
        spill = blockSize - ((amountOfPreffered * preferredThickness + amountOneInch) + cutAmount * 0.25);

        return new Yield(amountOfPreffered, amountOneInch, spill, blockSize, preferredThickness);
    }
}
