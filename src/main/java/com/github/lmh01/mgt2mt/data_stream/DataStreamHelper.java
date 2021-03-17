package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DataStreamHelper {

    /**
     * Searches the input file for the "NAME EN" key and returns its value. If not found returns -1.
     * @param file The file that should be searched for the key
     * @param charsetType Defines what charset the source file uses. Possible UTF_8BOM UTF_16LE
     */
    public static String getNameFromFile(File file, String charsetType) throws IOException {
        Map<Integer, String> map = Utils.getContentFromFile(file, charsetType);
        for(Map.Entry entry : map.entrySet()){
            if(entry.getValue().toString().contains("NAME EN")){
                return entry.getValue().toString().replace("[NAME EN]", "");
            }
        }
        return "-1";
    }
}
