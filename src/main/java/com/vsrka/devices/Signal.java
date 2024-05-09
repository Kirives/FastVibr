package com.vsrka.devices;

import java.util.ArrayList;
import java.util.List;
import com.vsrka.mathFunction.FunctionV2;

public class Signal {

    private int number;

    private List<Double> signal;

    private List<Double> coefficients;

    public Signal(int number, List<Double> signal) {
        this.number = number;
        this.signal = signal;
        this.coefficients = calculateCoefficients();
    }

    private List<Double> calculateCoefficients() {
        int startIndex = getSignal().size() % 10000;
        List<Double> signal = getSignal();
        Double max = FunctionV2.maximum(signal.subList(startIndex, signal.size()));
        Double min = FunctionV2.minimum(signal.subList(startIndex, signal.size()));
        Double step = (max-min)/200;
        List<Double> coefficients = new ArrayList<Double>();
        int intervalNumber = getSignal().size() / 10000;

        for (int i = 0; i < intervalNumber; i++) {
            Double intervalMax= FunctionV2.maximum(signal.subList(startIndex,startIndex+10000));
            startIndex+=10000;
            int znam = (int) ((intervalMax-min)/step);
            if(i==0){
                coefficients.add((double) (znam/200.0));
            }else{
                coefficients.add(Math.max(coefficients.get(i-1),(double) (znam/200.0)));
            }
        }
        return coefficients;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Double> getSignal() {
        return signal;
    }

    public void setSignal(List<Double> signal) {
        this.signal = signal;
    }
}
