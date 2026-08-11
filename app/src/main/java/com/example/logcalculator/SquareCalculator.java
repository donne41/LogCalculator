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

    private void checkUnit(boolean isMetric) {
        if (isMetric) {

        } else {

        }
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
        return calculatePlanks(imperialSquare, preferredThickness);
        //switch outpuutunits

    }


    public double getEllipsSquare() {
        double radBig = doubleDiaBig / 2;
        double radSmall = doubleDiaSmall / 2;
        double divTop = 2 * radBig * radSmall;
        double divBottom = Math.sqrt(radBig * radBig + radSmall * radSmall);

        return divTop / divBottom;
    }

    public double getInchBlock(double cmBlock) {
        return Math.round((cmBlock / 2.54) * 4.0) / 4.0;
    }

    public Yield calculatePlanks(double blockSize, int preferredThickness) {

        int amountTwoInch = (int) ((blockSize + cutWidth) / (2 + cutWidth));
        int amountOneInch = 0;
        double possibleOneInch = 0;
        boolean evenOneInch = false;
        int cutAmount;
        double spill;
        double rest = blockSize - amountTwoInch * 2 - ((amountTwoInch - 1) * 0.25);
        while (rest != 0 && amountTwoInch > 0 && !evenOneInch) {
            possibleOneInch = (rest) % (1 + cutWidth);
            if (possibleOneInch == 0 || possibleOneInch == 1.0) {
                amountOneInch = (int) ((rest + cutWidth) / (1 + cutWidth));
                cutAmount = amountTwoInch + amountOneInch - 1;
                spill = blockSize - ((amountTwoInch * 2 + amountOneInch * 1) + cutAmount * 0.25);
                if (spill > 0 || spill < 0) {
                    evenOneInch = false;
                } else {
                    evenOneInch = true;
                    continue;
                }
            }
            if (amountTwoInch % 2 != 0) {
                rest += 0.25;
            }
            amountTwoInch--;
            rest += 2;

        }
        if (amountTwoInch == 0) {
            amountOneInch = (int) ((blockSize + cutWidth) / (1 + cutWidth));
        }
        cutAmount = amountTwoInch + amountOneInch - 1;
        spill = blockSize - ((amountTwoInch * 2 + amountOneInch * 1) + cutAmount * 0.25);

        return new Yield(amountTwoInch, amountOneInch, spill);
    }
}
