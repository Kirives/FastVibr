package com.vsrka.mathFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//Используется-правильна-работает
public class FunctionV2 {

    public static double initialMoment(List<Double> data, int k) {
        double sum = 0;
        for (double x : data) {
            sum += Math.pow(x, k);
        }
        return sum / data.size();
    }

    public static double centralMoment(List<Double> data, int k) {
        double mean = initialMoment(data, 1);
        double sum = 0;
        for (double x : data) {
            sum += Math.pow(x - mean, k);
        }
        return sum / data.size();
    }

    public static double standardDeviation(List<Double> data) {
        return Math.sqrt(centralMoment(data, 2));
    }

    public static double minimum(List<Double> data) {
        return Collections.min(data);
    }

    public static double maximum(List<Double> data) {
        return Collections.max(data);
    }

    public static double skewness(List<Double> data) {
        double std = standardDeviation(data);
        return centralMoment(data, 3) / Math.pow(std, 3);
    }

    public static double kurtosis(List<Double> data) {
        double std = standardDeviation(data);
        return centralMoment(data, 4) / Math.pow(std, 4) - 3;
    }

    public static double median(List<Double> data) {
        List<Double> sorted = new ArrayList<>(data);
        Collections.sort(sorted);
        int size = sorted.size();
        if (size % 2 == 1) {
            return sorted.get(size / 2);
        } else {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2;
        }
    }


    public static double meanInRange(List<Double> data) {
        double min=minimum(data);
        double max = maximum(data);
        List<Double> transformed = data.stream()
                .map(x -> (x - min) / (max - min))
                .collect(Collectors.toList());
        return initialMoment(transformed, 1);
    }

    public static double stdInRange(List<Double> data) {
        double min=minimum(data);
        double max = maximum(data);
        double mean = meanInRange(data);
        List<Double> transformed = data.stream()
                .map(x -> (x - min) / (max - min))
                .collect(Collectors.toList());
        double sum = 0;
        for (double y : transformed) {
            sum += Math.pow(y - mean, 2);
        }
        return Math.sqrt(sum / (data.size() - 1));
    }

    public static double variationCoeffInRange(List<Double> data) {
        double min=minimum(data);
        double max = maximum(data);
        double mean = meanInRange(data);
        double std = stdInRange(data);
        return std / mean;
    }

    public static double stdErrorInRange(List<Double> data) {
        double min=minimum(data);
        double max = maximum(data);
        double std = stdInRange(data);
        return std / Math.sqrt(data.size());
    }


}
