package com.vsrka.devices;

import java.util.List;

public class Device {

    private String name;

    private List<Signal> allSignal;

    private List<Signal> trueSignal;

    private List<Signal> falseSignal;

    private int min;

    private int max;

    public Device(String name) {
        this.name = name;
    }

    public List<Signal> getAllSignal() {
        return allSignal;
    }

    public void setAllSignal(List<Signal> allSignal) {
        this.allSignal = allSignal;
    }

    public List<Signal> getTrueSignal() {
        return trueSignal;
    }

    public void setTrueSignal(List<Signal> trueSignal) {
        this.trueSignal = trueSignal;
    }

    public List<Signal> getFalseSignal() {
        return falseSignal;
    }

    public void setFalseSignal(List<Signal> falseSignal) {
        this.falseSignal = falseSignal;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
