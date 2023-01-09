package com.terminalvelocitycabbage.engine.utils;

public class NumUtils {

    public static int orderOfMultiple(int number, int factor) {
        int currentOrder = 0;
        do {
            if (number % factor != 0) {
                return -1;
            }
            number /= 4;
            currentOrder++;
        } while (number > 1);
        return currentOrder;
    }

    public static int getRotatedInt(int number, int rotations) {
        int digits = numberOfDigits(number);
        int powTen = (int) Math.pow(10, digits - 1);

        for (int i = 0; i < rotations - 1; i++) {
            int firstDigit = number / powTen;
            number = ((number * 10) + firstDigit) - (firstDigit * powTen * 10);
        }

        return number;
    }

    public static int numberOfDigits(int number) {
        int digits = 0;
        while (number > 0) {
            digits++;
            number /= 10;
        }
        return digits;
    }

    public static float clampf(float a, float mn, float mx) {
        return a < mn ? mn : Math.min(a, mx);
    }

}
