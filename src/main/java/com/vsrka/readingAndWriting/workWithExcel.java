package com.vsrka.readingAndWriting;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vsrka.devices.Signal;
import org.apache.poi.util.IOUtils;


public class workWithExcel {

    //Чтение конкретно одного файла эксель и создание сигнала
    public static List<Signal> readingFromExcel(String fileName) throws Exception {
        IOUtils.setByteArrayMaxOverride(500000000);
        List<Signal> signals = new ArrayList<>();
        FileInputStream file = new FileInputStream(new File(fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int columnCount = sheet.getRow(0).getLastCellNum();

        // Create signal lists for each column
        List<List<Double>> signalLists = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            signalLists.add(new ArrayList<>());
        }

        // Use a streaming API to read the file row by row
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            for (int i = 0; i < columnCount; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    // Handle missing cells
                    continue;
                }
                double value = cell.getNumericCellValue();
                // Add value to the corresponding signal list
                signalLists.get(i).add(value);
            }
        }

        // Create signal objects and add them to the list
        for (int i = 0; i < columnCount; i++) {
            signals.add(new Signal(i, signalLists.get(i)));
        }

        file.close();
        return signals;
    }

}
