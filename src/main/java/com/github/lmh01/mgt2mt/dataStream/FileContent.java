package com.github.lmh01.mgt2mt.dataStream;

import java.io.*;
import java.util.ArrayList;

public class FileContent {
    public static ArrayList<String> getScannedContentAsArrayList(File inputFile){
        ArrayList<String> listContent = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(inputFile), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                listContent.add(currentLine);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listContent;
    }
}
