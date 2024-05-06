package com.vsrka.mathFunction;

import java.util.DoubleSummaryStatistics;
import java.util.List;

//Пусть будет, но не используется
public class FunctionV1 {

    public static double calculateMoment(List<Double> signal, int k) {
        double sum = 0.0;
        for (double x : signal) {
            sum += Math.pow(x, k);
        }
        return sum / signal.size();
    }

    public static double calculateCentralMoment(List<Double> signal, int k) {
        double mean = signal.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        double sum = 0.0;
        for (double x : signal) {
            sum += Math.pow(x - mean, k);
        }
        return sum / signal.size();
    }

    public static double calculateStandardDeviation(List<Double> signal) {
        return Math.sqrt(calculateCentralMoment(signal, 2));
    }

    public static double calculateMinimum(List<Double> signal) {
        return signal.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
    }

    public static double calculateMaximum(List<Double> signal) {
        return signal.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
    }

    public static double calculateSkewness(List<Double> signal) {
        double stdDev = calculateStandardDeviation(signal);
        double moment3 = calculateCentralMoment(signal, 3);
        return moment3 / (stdDev * stdDev * stdDev);
    }

    public static double calculateKurtosis(List<Double> signal) {
        double stdDev = calculateStandardDeviation(signal);
        double moment4 = calculateCentralMoment(signal, 4);
        return moment4 / (stdDev * stdDev * stdDev * stdDev) - 3;
    }

    public static double calculateMedian(List<Double> signal) {
        DoubleSummaryStatistics stats = signal.stream().mapToDouble(Double::doubleValue).summaryStatistics();
        return stats.getCount() % 2 == 0 ? (stats.getMax() + stats.getMin()) / 2 : signal.get((int) (stats.getCount() / 2));
    }

    public static double calculateMeanOnInterval(List<Double> signal) {
        return signal.stream().mapToDouble(x -> (x - calculateMinimum(signal)) / (calculateMaximum(signal) - calculateMinimum(signal))).average().getAsDouble();
    }

    public static double calculateStdDevOnInterval(List<Double> signal) {
        double mean = calculateMeanOnInterval(signal);
        double sum = 0.0;
        for (double x : signal) {
            double y = (x - calculateMinimum(signal)) / (calculateMaximum(signal) - calculateMinimum(signal));
            sum += Math.pow(y - mean, 2);
        }
        return Math.sqrt(sum / signal.size());
    }

    public static double calculateCoefficientOfVariationOnInterval(List<Double> signal) {
        double mean = calculateMeanOnInterval(signal);
        double stdDev = calculateStdDevOnInterval(signal);
        return stdDev / mean;
    }

    public static double calculateStandardErrorOnInterval(List<Double> signal) {
        double stdDev = calculateStdDevOnInterval(signal);
        return stdDev / Math.sqrt(signal.size());
    }

}
