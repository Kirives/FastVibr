package com.vsrka.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Period {

    HashMap<Integer,List<Double>> allParameters;
    private HashMap<Integer,List<Double>> FirstLevelParameters;
    private HashMap<Integer,List<Double>> SecondLevelParameters;
    private HashMap<Integer,List<Double>> ThirdLevelParameters;
    private HashMap<Integer,List<Double>> FourthLevelParameters;

    public Period(){
        FirstLevelParameters = new HashMap<>(20);
        SecondLevelParameters = new HashMap<>(300);
        ThirdLevelParameters = new HashMap<>(5000);
        FourthLevelParameters = new HashMap<>(85000);
    }

    public void setFirstLevelParameters(HashMap<Integer, List<Double>> firstLevelParameters) {
        for(int i=0;i<firstLevelParameters.size();i++){
            List<Double> curr = FirstLevelParameters.getOrDefault(i,new ArrayList<>());
            curr.add(firstLevelParameters.get(i).get(firstLevelParameters.get(i).size()-1));
            FirstLevelParameters.put(i,curr);
        }
    }

    public void setSecondLevelParameters(HashMap<Integer, List<Double>> secondLevelParameters) {
        for(int i=0;i<secondLevelParameters.size();i++){
            List<Double> curr = SecondLevelParameters.getOrDefault(i,new ArrayList<>());
            curr.add(secondLevelParameters.get(i).get(secondLevelParameters.get(i).size()-1));
            SecondLevelParameters.put(i,curr);
        }
    }

    public void setThirdLevelParameters(HashMap<Integer, List<Double>> thirdLevelParameters) {
        for(int i=0;i<thirdLevelParameters.size();i++){
            List<Double> curr = ThirdLevelParameters.getOrDefault(i,new ArrayList<>());
            curr.add(thirdLevelParameters.get(i).get(thirdLevelParameters.get(i).size()-1));
            ThirdLevelParameters.put(i,curr);
        }
    }

    public void setFourthLevelParameters(HashMap<Integer, List<Double>> fourthLevelParameters) {
        for(int i=0;i<fourthLevelParameters.size();i++){
            List<Double> curr = FourthLevelParameters.getOrDefault(i,new ArrayList<>());
            curr.add(fourthLevelParameters.get(i).get(fourthLevelParameters.get(i).size()-1));
            FourthLevelParameters.put(i,curr);
        }
    }
}
