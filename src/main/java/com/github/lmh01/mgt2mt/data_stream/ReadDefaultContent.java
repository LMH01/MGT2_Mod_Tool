package com.github.lmh01.mgt2mt.data_stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ReadDefaultContent {

    /**
     * @deprecated use {@link ReadDefaultContent#getDefault(String)} instead
     */
    @Deprecated
    public static String[] getDefaultPublisher() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("default_content/default_publisher.txt")), StandardCharsets.UTF_8));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            arrayList.add(currentLine);
        }
        reader.close();
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }

    public static String[] getDefault(String fileName) throws IOException{
        return getDefault(fileName, null);
    }
    public static String[] getDefault(String fileName, Replacer replacer) throws IOException{
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("default_content/" + fileName)), StandardCharsets.UTF_8));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if(replacer != null){
                arrayList.add(replacer.replace(currentLine));
            }else{
                arrayList.add(currentLine);
            }
        }
        reader.close();
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }
}
