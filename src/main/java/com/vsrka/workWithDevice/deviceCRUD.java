package com.vsrka.workWithDevice;

import com.vsrka.devices.*;
import com.vsrka.readingAndWriting.*;
import com.vsrka.mathFunction.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void calculateParameter(List<Double> signal){
        //начальные моменты
        long startTime = System.nanoTime();
        System.out.println(FunctionV1.calculateMoment(signal,1));
        System.out.println(FunctionV1.calculateMoment(signal,2));
        System.out.println(FunctionV1.calculateMoment(signal,3));
        System.out.println(FunctionV1.calculateMoment(signal,4));
        //System.out.println(FunctionV1.calculateCentralMoment(signal,1));
        System.out.println(FunctionV1.calculateCentralMoment(signal,2));
        System.out.println(FunctionV1.calculateCentralMoment(signal,3));
        System.out.println(FunctionV1.calculateCentralMoment(signal,4));
        System.out.println(FunctionV1.calculateStandardDeviation(signal));
        System.out.println(FunctionV1.calculateMinimum(signal));
        System.out.println(FunctionV1.calculateMaximum(signal));
        System.out.println(FunctionV1.calculateSkewness(signal));
        //System.out.println(FunctionV1.calculateKurtosis(signal));
        //System.out.println(FunctionV1.calculateMedian(signal));
//        System.out.println(FunctionV1.calculateMeanOnInterval(signal));
//        System.out.println(FunctionV1.calculateStdDevOnInterval(signal));
//        System.out.println(FunctionV1.calculateCoefficientOfVariationOnInterval(signal));
//        System.out.println(FunctionV1.calculateStandardErrorOnInterval(signal));
        long endTime = System.nanoTime();
        System.out.print("Time ");
        System.out.println(endTime-startTime);
        startTime = System.nanoTime();
        System.out.println(FunctionV2.initialMoment(signal,1));
        System.out.println(FunctionV2.initialMoment(signal,2));
        System.out.println(FunctionV2.initialMoment(signal,3));
        System.out.println(FunctionV2.initialMoment(signal,4));
        //System.out.println(FunctionV2.centralMoment(signal,1));
        System.out.println(FunctionV2.centralMoment(signal,2));
        System.out.println(FunctionV2.centralMoment(signal,3));
        System.out.println(FunctionV2.centralMoment(signal,4));
        System.out.println(FunctionV2.standardDeviation(signal));
        System.out.println(FunctionV2.minimum(signal));
        System.out.println(FunctionV2.maximum(signal));
        System.out.println(FunctionV2.skewness(signal));
        System.out.println(FunctionV2.kurtosis(signal));
        System.out.println(FunctionV2.median(signal));
        //тут вроде считает
        System.out.println(FunctionV2.meanInRange(signal));
        System.out.println(FunctionV2.stdInRange(signal));
        System.out.println(FunctionV2.variationCoeffInRange(signal));
        System.out.println(FunctionV2.stdErrorInRange(signal));
        endTime = System.nanoTime();
        System.out.print("Time ");
        System.out.println(endTime-startTime);
    }

}

