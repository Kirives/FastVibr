package com.vsrka;

import com.vsrka.readingAndWriting.*;
import com.vsrka.devices.Device;

import java.util.List;

import com.vsrka.workWithDevice.deviceCRUD;
import com.vsrka.readingAndWriting.workWithDatabase;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        workWithExcel reading = new workWithExcel();
        try {

            deviceCRUD test = new deviceCRUD();
            test.createDevice();
            //test.insertToDatabse();
            //test.testPar();
            //ФАЗА 1
            //test.calculateParameters();
            //test.calculateParametersSecondV2();
            //test.calculateParametersThirdV2();
            //test.calculateParametersFourthV2();
            test.findPearsonBatch();

            test.calculateAll();


            System.out.println("testDir");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
