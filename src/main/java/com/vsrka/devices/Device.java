package com.vsrka.devices;

import java.util.List;

public class Device {

    private String name;

    private List<Signal> allSignal;

    private List<Signal> trueSignal;

    private List<Signal> falseSignal;

    public Device(String name) {
        this.name = name;
    }

    public List<Signal> getAllSignal() {
        return allSignal;
    }

    public void setAllSignal(List<Signal> allSignal) {
        this.allSignal = allSignal;
    }

    public void setTrueSignal(List<Signal> trueSignal) {
        this.trueSignal = trueSignal;
    }

    public void setFalseSignal(List<Signal> falseSignal) {
        this.falseSignal = falseSignal;
    }

    public String getName() {
        return name;
    }
}
