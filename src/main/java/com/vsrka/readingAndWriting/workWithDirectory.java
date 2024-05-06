package com.vsrka.readingAndWriting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class workWithDirectory {

    public static List<String> readingDirectory(String directoryName) {
        List<String> list = new ArrayList<String>();
        File dir = new File(directoryName);
        File[] files = dir.listFiles();
        for (File file : files) {
            list.add(directoryName+"\\"+file.getName());
        }
        return list;
    }
}
