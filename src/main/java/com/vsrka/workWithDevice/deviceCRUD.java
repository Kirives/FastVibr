package com.vsrka.workWithDevice;

import com.vsrka.devices.*;
import com.vsrka.readingAndWriting.*;
import com.vsrka.mathFunction.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class deviceCRUD {

    static int SIZE_N = 500;

    HashMap<String, Device> devices;

    public deviceCRUD() {
        this.devices = new HashMap<>();
    }

    public void createDevice() {
        List<String> fileName = workWithDirectory.readingDirectory("C:\\\\Users\\\\User\\\\Desktop\\\\Диплом\\\\Java\\\\untitled\\\\ExcelSignals");
        try {
            for (String file : fileName) {
                insertSignal(file);
            }
            System.out.println("fg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Это или создание девайся + вставка или вставка в уже готовый синал
    private void insertSignal(String file) throws Exception {
        String words[] = file.split("\\\\");
        String fullDeviceName = words[words.length - 1];
        words = fullDeviceName.split("_");
        String deviceName = words[words.length - 2];
        Device device = devices.getOrDefault(deviceName, new Device(deviceName));
        switch (words[words.length - 1]) {
            case ("true.xlsx"):
                device.setTrueSignal(workWithExcel.readingFromExcel(file));
                break;
            case ("false.xlsx"):
                device.setFalseSignal(workWithExcel.readingFromExcel(file));
                break;
            case ("all.xlsx"):
                device.setAllSignal(workWithExcel.readingFromExcel(file));
                break;
        }
        if (!devices.containsKey(deviceName)) {
            devices.put(deviceName, device);
        }
    }

    public List<Double> calculateParameter(List<Double> signal) {
        List<Double> result = new ArrayList<>();

        result.add(Function.initialMoment(signal, 1));
        result.add(Function.initialMoment(signal, 2));
        result.add(Function.initialMoment(signal, 3));
        result.add(Function.initialMoment(signal, 4));
        result.add(Function.centralMoment(signal, 2));
        result.add(Function.centralMoment(signal, 3));
        result.add(Function.centralMoment(signal, 4));
        result.add(Function.standardDeviation(signal));
        result.add(Function.minimum(signal));
        result.add(Function.maximum(signal));
        result.add(Function.skewness(signal));
        result.add(Function.kurtosis(signal));
        result.add(Function.median(signal));
        result.add(Function.meanInRange(signal));
        result.add(Function.stdInRange(signal));
        result.add(Function.variationCoeffInRange(signal));
        result.add(Function.stdErrorInRange(signal));

        return result;
    }

    public void calculateAll() throws Exception {
        for (Device device : devices.values()) {
            for (Signal signal : device.getAllSignal()) {

                List<Double> currSignal = signal.getSignal();
                int startIndex = currSignal.size() % (SIZE_N * 4 - 3);
                int colIndex = (currSignal.size() - startIndex) / (SIZE_N * 4 - 3);
                Period period = new Period();
                int stepSignal = SIZE_N * 4 - 3;

                for (int i = 0; i < colIndex; i++) {
                    //Потом с каждого хешмапа надо собрать последние
                    //Сначала должно идти вычисление первого уровня
                    HashMap<Integer, List<Double>> firstParameter = new HashMap<>();
                    firstParameter = calculateAllFirstLVL(currSignal.subList(startIndex + stepSignal * i, startIndex + stepSignal * (i + 1)));
                    HashMap<Integer, List<Double>> secondParameter = new HashMap<>();
                    secondParameter = calculateAllSecondLVL(firstParameter);
                    HashMap<Integer, List<Double>> thirdParameter = new HashMap<>();
                    thirdParameter = calculateAllSecondLVL(secondParameter);
                    HashMap<Integer, List<Double>> fourthParameter = new HashMap<>();
                    fourthParameter = calculateAllSecondLVL(thirdParameter);
                    period.setFirstLevelParameters(firstParameter);
                    period.setSecondLevelParameters(secondParameter);
                    period.setThirdLevelParameters(thirdParameter);
                    period.setFourthLevelParameters(fourthParameter);

                    System.out.println("Прогресс: " + i + "/" + colIndex);
                }

                try {
                    period.saveToJson(device.getName() + signal.getNumber() + ".json");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Тут все характеристики для каждого периода собраны, поэтому нужно считать коэф корреляции
                //Это считается он
                findPearsonAll(period.getFirstLevelParameters(), signal.getCoefficients(), device.getName(), signal.getNumber(), 1);
                findPearsonAll(period.getSecondLevelParameters(), signal.getCoefficients(), device.getName(), signal.getNumber(), 2);
                findPearsonAll(period.getThirdLevelParameters(), signal.getCoefficients(), device.getName(), signal.getNumber(), 3);
                findPearsonAll(period.getFourthLevelParameters(), signal.getCoefficients(), device.getName(), signal.getNumber(), 4);
                System.out.println("END");
            }
        }
    }

    private HashMap<Integer, List<Double>> calculateAllFirstLVL(List<Double> signal) throws Exception {
        HashMap<Integer, List<Double>> result = new HashMap<>();

        for (int i = 0; i < signal.size() - SIZE_N + 1; i++) {
            List<Double> resultPar = calculateParameter(signal.subList(i, SIZE_N + i));
            for (int j = 0; j < resultPar.size(); j++) {
                List<Double> curr = result.getOrDefault(j, new ArrayList<>());
                curr.add(resultPar.get(j));
                result.put(j, curr);
            }
        }

        return result;
    }

    private HashMap<Integer, List<Double>> calculateAllSecondLVL(HashMap<Integer, List<Double>> parameters) throws Exception {

        HashMap<Integer, List<Double>> result = new HashMap<>();

        IntStream.range(0, parameters.size()).parallel().forEach(z -> {
            try {
                List<Double> listPar = parameters.get(z);
                for (int i = 0; i < listPar.size() - SIZE_N + 1; i++) {
                    List<Double> resultPar = calculateParameter(listPar.subList(i, SIZE_N + i));
                    for (int j = 0; j < resultPar.size(); j++) {
                        int number = z * 17 + j;
                        synchronized (result) {
                            List<Double> curr = result.getOrDefault(number, new ArrayList<>());
                            curr.add(resultPar.get(j));
                            result.put(number, curr);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private void findPearsonAll(HashMap<Integer, List<Double>> parameters, List<Double> coefficients, String deviceName, int numberSignal, int intervalLevel) throws Exception {
        workWithDatabase database = new workWithDatabase();
        database.insertPearsonString();
        IntStream.range(0, parameters.size()).parallel().forEach(i -> {
            try {
                double pearsonResult = Math.abs(Function.pearsonCorrelation(coefficients, parameters.get(i)));
                if (pearsonResult > 0.1) {
                    database.insertPearsonDataBatch(deviceName, "all", numberSignal, intervalLevel, i, pearsonResult);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        database.insertBatch();
        database.close();
    }
}

