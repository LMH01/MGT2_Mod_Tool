package com.github.lmh01.mgt2mt.dataStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileContent {
    public static ArrayList<String> getScannedContentAsArrayList(File inputFile){
        ArrayList<String> listContent = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(inputFile);
            while(scanner.hasNextLine()){
                listContent.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listContent;
    }
}
