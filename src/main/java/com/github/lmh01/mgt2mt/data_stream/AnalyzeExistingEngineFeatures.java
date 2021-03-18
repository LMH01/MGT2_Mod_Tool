package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyzeExistingEngineFeatures {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGameplayFeatures.class);
    public static List<Map<String, String>> engineFeatures;
    public static int maxEngineFeatureId = 0;

    /**
     * Analyzes the file GameplayFeatures.txt and puts its values into the gameplayFeature list.
     */
    public static void analyzeEngineFeatures() throws IOException {
        engineFeatures = DataStreamHelper.parseDataFile(Utils.getEngineFeaturesFile());
        int currentMaxEngineFeatureId = 0;
        for (Map<String, String> map : engineFeatures) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    int currentId = Integer.parseInt(entry.getValue());
                    if (currentMaxEngineFeatureId < currentId) {
                        currentMaxEngineFeatureId = currentId;
                    }
                }
            }
        }
        maxEngineFeatureId = currentMaxEngineFeatureId;
        LOGGER.info("MaxEngineFeatureId: " + maxEngineFeatureId);
    }

    /**
     * @return Returns the next free gameplayFeature id.
     */
    public static int getFreeEngineFeatureId(){
        return maxEngineFeatureId +1;
    }

    /**
     * @return Returns a string containing all active engine features sorted by alphabet.
     */
    public static String[] getEngineFeaturesByAlphabet(){
        ArrayList<String> arrayListAvailableEngineFeaturesSorted = new ArrayList<>();
        for(Map<String, String> map : engineFeatures){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayListAvailableEngineFeaturesSorted.add(entry.getValue());
                }
            }
        }
        Collections.sort(arrayListAvailableEngineFeaturesSorted);
        String[] string = new String[arrayListAvailableEngineFeaturesSorted.size()];
        arrayListAvailableEngineFeaturesSorted.toArray(string);
        return string;
    }

    /**
     * Returns -1 when engine feature name does not exist.
     * @param engineFeatureName The engine feature name
     * @return Returns the engine feature id for the specified name.
     */
    public static int getEngineFeatureIdByName(String engineFeatureName){
        int genreId = -1;
        for(Map<String, String> map : engineFeatures){
            if(map.get("NAME EN").equals(engineFeatureName)){
                genreId = Integer.parseInt(map.get("ID"));
            }
        }
        if(genreId == -1){
            LOGGER.info("Engine feature [" + engineFeatureName + "] does not exist");
        }else{
            LOGGER.info("Engine feature [" + engineFeatureName + "] has been found. Id: " + genreId);
        }
        return genreId;
    }

    /**
     * @return Returns a String[] containing all custom engine features
     */
    public static String[] getCustomEngineFeaturesString(){
        ArrayList<String> arrayListActiveEngineFeatures = new ArrayList<>();
        List<Map<String, String>> listEngineFeatures = engineFeatures;
        for(int i=58; i<listEngineFeatures.size(); i++){
            Map<String, String> map = listEngineFeatures.get(i);
            arrayListActiveEngineFeatures.add(map.get("NAME EN"));
        }
        Collections.sort(arrayListActiveEngineFeatures);
        String[] string = new String[arrayListActiveEngineFeatures.size()];
        arrayListActiveEngineFeatures.toArray(string);
        return string;
    }

    /**
     * @param engineFeatureNameEn The engine feature for which the map should be returned.
     * @return Returns a map containing all values for the specified engine feature.
     */
    public static Map<String, String> getSingleEngineFeatureByNameMap(String engineFeatureNameEn){
        List<Map<String, String>> list = engineFeatures;
        Map<String, String> mapSingleEngineFeature = null;
        int engineFeaturePosition = getEngineFeatureIdByName(engineFeatureNameEn);
        for(int i=0; i<list.size(); i++){
            if(i == engineFeaturePosition){
                mapSingleEngineFeature = list.get(i);
            }
        }
        return mapSingleEngineFeature;
    }
}
