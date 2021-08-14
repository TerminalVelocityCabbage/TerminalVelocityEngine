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

}
