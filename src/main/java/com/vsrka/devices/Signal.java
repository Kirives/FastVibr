package com.vsrka.devices;

import java.util.List;

public class Signal {

    private int number;

    private List<Double> signal;

    public Signal(int number, List<Double> signal) {
        this.number = number;
        this.signal = signal;
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
