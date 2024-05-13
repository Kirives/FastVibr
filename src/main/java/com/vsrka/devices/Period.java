package com.vsrka.devices;

import java.util.HashMap;
import java.util.List;

public class Period {

    HashMap<Integer,List<Double>> allParameters;

    public Period(HashMap<Integer, List<Double>> allParameters) {
        this.allParameters = allParameters;
    }
}
