package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeExistingLicences {
    public static Map<Integer, String> existingLicences = new HashMap<>();

    public static void analyze() throws IOException {
        existingLicences = DataStreamHelper.getContentFromFile(Utils.getLicenceFile(), "UTF_8BOM");
    }

    /**
     * @return Returns a string containing the licence names sorted by alphabet
     */
    public static String[] getLicenceNames(){
        ArrayList<String> licenceNames = new ArrayList<>();
        for(int i=1; i<=existingLicences.size(); i++){
            licenceNames.add(existingLicences.get(i).replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim());
        }
        Collections.sort(licenceNames);
        String[] strings = new String[licenceNames.size()];
        licenceNames.toArray(strings);
        return strings;
    }

    /**
     * @param name The name that should be searched
     * @return Returns the position of the input name in the file
     */
    public static int getLicenceIdByName(String name){
        for(Map.Entry<Integer, String> entry : existingLicences.entrySet()){
            if(entry.getValue().contains(name)){
                return entry.getKey();
            }
        }
        return -1;
    }
}