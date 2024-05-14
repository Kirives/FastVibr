package com.vsrka.devices;

import java.util.ArrayList;
import java.util.List;
import com.vsrka.mathFunction.FunctionV2;

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

//    private List<Double> calculateCoefficients() {
//        int startIndex = getSignal().size() % 10000;
//        List<Double> signal = getSignal();
//        Double max = FunctionV2.maximum(signal.subList(startIndex, signal.size()));
//        Double min = FunctionV2.minimum(signal.subList(startIndex, signal.size()));
//        Double step = (max-min)/200;
//        List<Double> coefficients = new ArrayList<Double>();
//        int intervalNumber = getSignal().size() / 10000;
//
//        for (int i = 0; i < intervalNumber; i++) {
//            Double intervalMax= FunctionV2.maximum(signal.subList(startIndex,startIndex+10000));
//            startIndex+=10000;
//            int znam = (int) ((intervalMax-min)/step);
//            if(i==0){
//                coefficients.add((double) (znam/200.0));
//            }else{
//                coefficients.add(Math.max(coefficients.get(i-1),(double) (znam/200.0)));
//            }
//        }
//        return coefficients;
//    }

    //Надо переделать определение интервалов
    private List<Double> calculateCoefficients() {
        int startIndex = getSignal().size() % (SIZE_N * 4 - 3);
        int colIndex = (getSignal().size() - startIndex) / (SIZE_N * 4 - 3);
        List<Double> coefficients = new ArrayList<>(colIndex+10);
        Double max = FunctionV2.maximum(signal.subList(startIndex, signal.size()));
        Double min = FunctionV2.minimum(signal.subList(startIndex, signal.size()));
        Double step = (max-min)/200;
        int stepSignal = SIZE_N * 4 - 3;
        for(int i=0;i<colIndex;i++) {
            Double intervalMax = FunctionV2.maximum(getSignal().subList(startIndex+stepSignal*i, startIndex + stepSignal*(i+1)));
            int znam = (int) ((intervalMax-min)/step);
            if(i==0){
                coefficients.add((double) (znam/200.0));
            }else{
                coefficients.add(Math.max(coefficients.get(i-1),(double) (znam/200.0)));
            }
            if(i==258){
                System.out.println(1);
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
