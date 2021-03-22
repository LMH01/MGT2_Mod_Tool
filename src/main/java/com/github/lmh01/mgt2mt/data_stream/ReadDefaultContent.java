package com.github.lmh01.mgt2mt.data_stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ReadDefaultContent {
    public static String[] getDefaultLicences() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("default_content/default_licences.txt"), StandardCharsets.UTF_8));
        String currentLine;
        while((currentLine=reader.readLine()) != null){
            arrayList.add(currentLine.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim());
        }
        reader.close();
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }
}
