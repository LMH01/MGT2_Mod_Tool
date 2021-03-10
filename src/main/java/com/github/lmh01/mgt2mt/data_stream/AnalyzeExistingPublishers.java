package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyzeExistingPublishers {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingPublishers.class);
    private static List<Map<String, String>> publisherList;
    public static int maxThemeID = 0;

    public static void analyzePublisherFile() throws IOException {
        publisherList = Utils.parseDataFile(Utils.getPublisherFile());
        maxThemeID = publisherList.stream()
                .map(map -> Integer.parseInt(map.get("ID")))
                .max(Integer::compareTo)
                .orElse(0);
        LOGGER.info("Max Number: " + maxThemeID);
    }

    /**
     * @return Returns the listMap containing the contents of the publisher file.
     */
    public static List<Map<String, String>> getListMap(){
        return publisherList;
    }
    public static String[] getPublisherString(){
        ArrayList<String> arrayListActivePublishers = new ArrayList<>();
        List<Map<String, String>> listPublishers = getListMap();
        for(int i=0; i<listPublishers.size(); i++){
            Map<String, String> map = listPublishers.get(i);
            arrayListActivePublishers.add(map.get("NAME EN"));
        }
        Collections.sort(arrayListActivePublishers);
        String[] string = new String[arrayListActivePublishers.size()];
        arrayListActivePublishers.toArray(string);
        return string;
    }
    public static String[] getCustomPublisherString(){
        ArrayList<String> arrayListActivePublishers = new ArrayList<>();
        List<Map<String, String>> listPublishers = getListMap();
        for(int i=71; i<listPublishers.size(); i++){
            Map<String, String> map = listPublishers.get(i);
            arrayListActivePublishers.add(map.get("NAME EN"));
        }
        Collections.sort(arrayListActivePublishers);
        String[] string = new String[arrayListActivePublishers.size()];
        arrayListActivePublishers.toArray(string);
        return string;
    }
}