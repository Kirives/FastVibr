package com.vsrka;

import com.vsrka.readingAndWriting.*;
import com.vsrka.devices.Device;

import java.util.List;

import com.vsrka.workWithDevice.deviceCRUD;
import com.vsrka.readingAndWriting.workWithDatabase;

public class App {
    public static void main(String[] args) {
        workWithExcel reading = new workWithExcel();
        try {
            deviceCRUD test = new deviceCRUD();
            test.createDevice();
            test.calculateAll();
            System.out.println("Конец расчётов");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
