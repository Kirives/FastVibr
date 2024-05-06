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
            test.testPar();

            System.out.println("testDir");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
