package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyzeExistingGameplayFeatures {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGameplayFeatures.class);
    public static List<Map<String, String>> gameplayFeatures;
    public static int maxGameplayFeatureId = 0;

    /**
     * Analyzes the file GameplayFeatures.txt and puts its values into the gameplayFeature list.
     */
    public static void analyzeGameplayFeatures() throws IOException {
        gameplayFeatures = Utils.parseDataFile(Utils.getGameplayFeaturesFile());
        int currentMaxGameplayFeatureId = 0;
        for (Map<String, String> map : gameplayFeatures) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    int currentId = Integer.parseInt(entry.getValue());
                    if (currentMaxGameplayFeatureId < currentId) {
                        currentMaxGameplayFeatureId = currentId;
                    }
                }
            }
        }
        maxGameplayFeatureId = currentMaxGameplayFeatureId;
        LOGGER.info("MaxGameplayFeatureId: " + maxGameplayFeatureId);
    }

    /**
     * @return Returns the next free gameplayFeature id.
     */
    public static int getFreeGameplayFeatureId(){
        return maxGameplayFeatureId+1;
    }

    /**
     * @return Returns a string containing all active gameplay features sorted by alphabet.
     */
    public static String[] getGameplayFeaturesByAlphabet(){
        ArrayList<String> arrayListAvailableGameplayFeaturesSorted = new ArrayList<>();
        for(Map<String, String> map : gameplayFeatures){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayListAvailableGameplayFeaturesSorted.add(entry.getValue());
                }
            }
        }
        Collections.sort(arrayListAvailableGameplayFeaturesSorted);
        String[] string = new String[arrayListAvailableGameplayFeaturesSorted.size()];
        arrayListAvailableGameplayFeaturesSorted.toArray(string);
        return string;
    }

    /**
     * Returns -1 when genre name does not exist.
     * @param gameplayFeatureName The gameplay feature name
     * @return Returns the genre id for the specified name.
     */
    public static int getGameplayFeatureIdByName(String gameplayFeatureName){
        int genreId = -1;
        for(Map<String, String> map : gameplayFeatures){
            if(map.get("NAME EN").equals(gameplayFeatureName)){
                genreId = Integer.parseInt(map.get("ID"));
            }
        }
        if(genreId == -1){
            LOGGER.info("Genre [" + gameplayFeatureName + "] does not exist");
        }else{
            LOGGER.info("Genre [" + gameplayFeatureName + "] has been found. Id: " + genreId);
        }
        return genreId;
    }
}
