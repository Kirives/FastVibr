package com.vsrka.devices;

import java.util.ArrayList;
import java.util.List;

import com.vsrka.mathFunction.Function;

public class Signal {

    static int SIZE_N = 500;

    private int number;

    private List<Double> signal;

    private List<Double> coefficients;

    public Signal(int number, List<Double> signal) {
        this.number = number;
        this.signal = signal;
        this.coefficients = calculateCoefficients();
    }

    private List<Double> calculateCoefficients() {
        int startIndex = getSignal().size() % (SIZE_N * 4 - 3);
        int colIndex = (getSignal().size() - startIndex) / (SIZE_N * 4 - 3);
        List<Double> coefficients = new ArrayList<>(colIndex + 10);
        Double max = Function.maximum(signal.subList(startIndex, signal.size()));
        Double min = Function.minimum(signal.subList(startIndex, signal.size()));
        Double step = (max - min) / 200;
        int stepSignal = SIZE_N * 4 - 3;
        for (int i = 0; i < colIndex; i++) {
            Double intervalMax = Function.maximum(getSignal().subList(startIndex + stepSignal * i, startIndex + stepSignal * (i + 1)));
            int znam = (int) ((intervalMax - min) / step);
            if (i == 0) {
                coefficients.add((double) (znam / 200.0));
            } else {
                coefficients.add(Math.max(coefficients.get(i - 1), (double) (znam / 200.0)));
            }
            if (i == 258) {
                System.out.println(1);
            }
        }
        return coefficients;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public int getNumber() {
        return number;
    }

    public List<Double> getSignal() {
        return signal;
    }
}
