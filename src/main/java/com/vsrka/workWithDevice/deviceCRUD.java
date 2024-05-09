package com.vsrka.workWithDevice;

import com.vsrka.devices.*;
import com.vsrka.readingAndWriting.*;
import com.vsrka.mathFunction.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class deviceCRUD {

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
        if (!devices.containsKey(deviceName)){
            devices.put(deviceName, device);
        }
    }

    //Это вставка только all signal, пока работаю только с ним
    public void insertToDatabse() throws Exception {
        workWithDatabase database = new workWithDatabase();
        //database.insertString();
        for(Device device : devices.values()){
            //database.insertString();
            for(Signal signal : device.getAllSignal()){
                database.insertString();
                for(int i = 0; i < signal.getSignal().size(); i++){
                    database.insertDataBatch(device.getName(),"all",signal.getNumber(),0,i,0,signal.getSignal().get(i));
                }
                database.insertBatch();
            }
        }
        database.close();
    }

    public void testPar(){
        calculateParameter(devices.get("мн-12").getAllSignal().get(0).getSignal());
    }

    public List<Double> calculateParameter(List<Double> signal){
        List<Double> result = new ArrayList<>();

        result.add(FunctionV2.initialMoment(signal,1));
        result.add(FunctionV2.initialMoment(signal,2));
        result.add(FunctionV2.initialMoment(signal,3));
        result.add(FunctionV2.initialMoment(signal,4));
        result.add(FunctionV2.centralMoment(signal,2));
        result.add(FunctionV2.centralMoment(signal,3));
        result.add(FunctionV2.centralMoment(signal,4));
        result.add(FunctionV2.standardDeviation(signal));
        result.add(FunctionV2.minimum(signal));
        result.add(FunctionV2.maximum(signal));
        result.add(FunctionV2.skewness(signal));
        result.add(FunctionV2.kurtosis(signal));
        result.add(FunctionV2.median(signal));
        result.add(FunctionV2.meanInRange(signal));
        result.add(FunctionV2.stdInRange(signal));
        result.add(FunctionV2.variationCoeffInRange(signal));
        result.add(FunctionV2.stdErrorInRange(signal));

        return result;
    }


    // тут для 1го уровня
    public void calculateParameters() throws Exception {

        try {
            workWithDatabase database = new workWithDatabase();
            for (Device device : devices.values()) {
                for (Signal signal : device.getAllSignal()) {
                    database.insertString();
                    List<Double> signals = signal.getSignal();
                    int startIndex = signals.size() % 10000;
                    int intervalNumber = 0;
                    while (startIndex < signals.size()) {
                        List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                        for (int i = 0; i < result.size(); i++) {
                            database.insertDataBatch(device.getName(), "all", signal.getNumber(), 1, intervalNumber, i, result.get(i));
                        }
                        startIndex += 10;
                        intervalNumber++;
                    }
                    database.insertBatch();
                }
            }
            database.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    //тут определяем для второго уровня
    public void calculateParametersSecond() throws Exception {

        workWithDatabase database = new workWithDatabase();
        for (Device device : devices.values()) {
            for(Signal signal : device.getAllSignal()) {
                database.insertString();
                int startIntervalNumber = 0;


                for(int i = 0; i < 17; i++){
                    //Тут мы получаем уже конкретный номер характеристики и идём уже по номерам
                    List<Double> signals = database.getAllParameter(device.getName(),signal.getNumber(),1,i);
                    int startIndex = 0;
                    //Номер интервала как раз обнуляется с каждой карактеристикой
                    int intervalNumber = 0;
                    database.insertString();
                    while(startIndex < signals.size()){
                        List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                        for (int j = 0; j < result.size(); j++) {
                            database.insertDataBatch(device.getName(), "all", signal.getNumber(), 2, intervalNumber, i*17+j, result.get(j));
                        }
                        startIndex += 10;
                        intervalNumber++;
                    }
                    database.insertBatch();


                }
            }
        }
    }

    public void calculateParametersSecondV2() throws Exception {

        //workWithDatabase database = new workWithDatabase();
        for (Device device : devices.values()) {
            for(Signal signal : device.getAllSignal()) {
                //database.insertString();
                int startIntervalNumber = 0;
                IntStream.range(0, 17).parallel().forEach(i -> {
                    try {
                        workWithDatabase database = new workWithDatabase();
                        //Тут мы получаем уже конкретный номер характеристики и идём уже по номерам
                        List<Double> signals = database.getAllParameter(device.getName(),signal.getNumber(),1,i);
                        int startIndex = 0;
                        //Номер интервала как раз обнуляется с каждой карактеристикой
                        int intervalNumber = 0;
                        database.insertString();
                        while(startIndex < signals.size()){
                            List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                            for (int j = 0; j < result.size(); j++) {
                                database.insertDataBatch(device.getName(), "all", signal.getNumber(), 2, intervalNumber, i*17+j, result.get(j));
                            }
                            startIndex += 10;
                            intervalNumber++;
                        }
                        database.insertBatch();
                        database.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    //туту определяем для 3го уровня
    public void calculateParametersThird() throws Exception {

        workWithDatabase database = new workWithDatabase();
        for (Device device : devices.values()) {
            for(Signal signal : device.getAllSignal()) {
                database.insertString();
                int startIntervalNumber = 0;


                for(int i = 0; i < 289; i++){
                    //Тут мы получаем уже конкретный номер характеристики и идём уже по номерам
                    List<Double> signals = database.getAllParameter(device.getName(),signal.getNumber(),2,i);
                    int startIndex = 0;
                    //Номер интервала как раз обнуляется с каждой карактеристикой
                    int intervalNumber = 0;
                    database.insertString();
                    while(startIndex < signals.size()){
                        List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                        for (int j = 0; j < result.size(); j++) {
                            database.insertDataBatch(device.getName(), "all", signal.getNumber(), 3, intervalNumber, i*17+j, result.get(j));
                        }
                        startIndex += 10;
                        intervalNumber++;
                    }
                    database.insertBatch();


                }
            }
        }
    }

    public void calculateParametersThirdV2() throws Exception {

        //workWithDatabase database = new workWithDatabase();
        for (Device device : devices.values()) {
            for(Signal signal : device.getAllSignal()) {
                //database.insertString();
                workWithDatabase database = new workWithDatabase();
                HashMap<Integer,List<Double>> resultMap = database.getAllParameterV2(device.getName(),signal.getNumber(),2,1);
                int startIntervalNumber = 0;
                database.insertString();
                IntStream.range(0, 289).parallel().forEach(i -> {
                    try {
                        //workWithDatabase database = new workWithDatabase();
                        //Тут мы получаем уже конкретный номер характеристики и идём уже по номерам
                        List<Double> signals = resultMap.get(i);
                        int startIndex = 0;
                        //Номер интервала как раз обнуляется с каждой карактеристикой
                        int intervalNumber = 0;

                        while(startIndex < signals.size()){
                            List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                            for (int j = 0; j < result.size(); j++) {
                                database.insertDataBatch(device.getName(), "all", signal.getNumber(), 3, intervalNumber, i*17+j, result.get(j));
                            }
                            startIndex += 10;
                            intervalNumber++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                database.insertBatch();
                database.close();
            }
        }
    }


    //тут определяем параметры для 4го уровня
    public void calculateParametersFourth() throws Exception {

        //workWithDatabase database = new workWithDatabase();
        for (Device device : devices.values()) {
            for(Signal signal : device.getAllSignal()) {
                //database.insertString();
                int startIntervalNumber = 0;
                IntStream.range(0, 4913).parallel().forEach(i -> {
                    try {
                        workWithDatabase database = new workWithDatabase();
                        //Тут мы получаем уже конкретный номер характеристики и идём уже по номерам
                        List<Double> signals = database.getAllParameter(device.getName(),signal.getNumber(),3,i);
                        int startIndex = 0;
                        //Номер интервала как раз обнуляется с каждой карактеристикой
                        int intervalNumber = 0;
                        database.insertString();
                        while(startIndex < signals.size()){
                            List<Double> result = calculateParameter(signals.subList(startIndex, startIndex + 10));
                            for (int j = 0; j < result.size(); j++) {
                                database.insertDataBatch(device.getName(), "all", signal.getNumber(), 3, intervalNumber, i*17+j, result.get(j));
                            }
                            startIndex += 10;
                            intervalNumber++;
                        }
                        database.insertBatch();
                        database.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void findPearson() throws Exception {
        for(Device device : devices.values()){
            for(Signal signal : device.getAllSignal()) {
                workWithDatabase database = new workWithDatabase();
                HashMap<Integer,List<Double>> result = database.getParameterV3(device.getName(),signal.getNumber());
                System.out.println(result.size());
                database.close();
                IntStream.range(0, result.size()).parallel().forEach(i -> {
                    try {
                        workWithDatabase databaseRes = new workWithDatabase();
                        double pearsonResult = Math.abs(FunctionV2.pearsonCorrelation(signal.getCoefficients(), result.get(i)));
                        if (pearsonResult > 0.1) {
                            databaseRes.insertPearson(device.getName(),"all",signal.getNumber(),4,i,pearsonResult);
                        }
                        databaseRes.close();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }


    //искать из кучи последних характеристик
    public void findPearsonBatch() throws Exception {
        for(Device device : devices.values()){
            for(Signal signal : device.getAllSignal()) {
                workWithDatabase database = new workWithDatabase();
                HashMap<Integer,List<Double>> result = database.getParameterV6(device.getName(),signal.getNumber());
                System.out.println(result.size());
                database.insertPearsonString();
                IntStream.range(0, result.size()).parallel().forEach(i -> {
                    try {
                        double pearsonResult = Math.abs(FunctionV2.pearsonCorrelation(signal.getCoefficients(), result.get(i)));
                        if (pearsonResult > 0.1) {
                            database.insertPearsonDataBatch(device.getName(),"all",signal.getNumber(),1,i,pearsonResult);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                database.insertBatch();
                database.close();
            }
        }
    }



}

