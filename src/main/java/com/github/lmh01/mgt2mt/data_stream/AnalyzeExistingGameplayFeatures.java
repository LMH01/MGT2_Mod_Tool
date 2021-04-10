package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
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
        gameplayFeatures = DataStreamHelper.parseDataFile(Utils.getGameplayFeaturesFile());
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
     * Returns -1 when gameplay feature name does not exist.
     * @param gameplayFeatureName The gameplay feature name
     * @return Returns the gameplay feature id for the specified name.
     */
    public static int getGameplayFeatureIdByName(String gameplayFeatureName){
        int genreId = -1;
        for(Map<String, String> map : gameplayFeatures){
            if(map.get("NAME EN").equals(gameplayFeatureName)){
                genreId = Integer.parseInt(map.get("ID"));
            }
        }
        if(genreId == -1){
            LOGGER.info("Gameplay feature [" + gameplayFeatureName + "] does not exist");
        }else{
            LOGGER.info("Gameplay feature [" + gameplayFeatureName + "] has been found. Id: " + genreId);
        }
        return genreId;
    }

    /**
     * @param id The id
     * @return Returns the specified genre name by id.
     * @throws ArrayIndexOutOfBoundsException Is thrown when the requested genre id does not exist in the map.
     */
    public static String getGameplayFeatureNameById(int id) throws ArrayIndexOutOfBoundsException{
        return getGameplayFeatureNamesInUse().get(id);
    }

    /**
     * @return Returns an ArrayList containing all genre names that are in use.
     */
    public static ArrayList<String> getGameplayFeatureNamesInUse(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(Map<String, String> map : gameplayFeatures){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayList.add(entry.getValue());
                }
            }
        }
        return arrayList;
    }

    /**
     * @return Returns a String[] containing all custom gameplay features
     */
    public static String[] getCustomGameplayFeaturesString(){
        ArrayList<String> arrayListActiveGameplayFeatures = new ArrayList<>();
        List<Map<String, String>> listGameplayFeatures = gameplayFeatures;
        ProgressBarHelper.initializeProgressBar(61, listGameplayFeatures.size(), I18n.INSTANCE.get("progressBar.moddedGameplayFeatures"));
        for(int i=61; i<listGameplayFeatures.size(); i++){
            Map<String, String> map = listGameplayFeatures.get(i);
            arrayListActiveGameplayFeatures.add(map.get("NAME EN"));
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedGameplayFeatureFound") + " " + map.get("NAME EN"));
            ProgressBarHelper.setValue(i);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedGameplayFeaturesComplete"));
        ProgressBarHelper.resetProgressBar();
        Collections.sort(arrayListActiveGameplayFeatures);
        String[] string = new String[arrayListActiveGameplayFeatures.size()];
        arrayListActiveGameplayFeatures.toArray(string);
        return string;
    }

    /**
     * @param gameplayFeatureNameEn The gameplay feature for which the map should be returned.
     * @return Returns a map containing all values for the specified gameplay feature.
     */
    public static Map<String, String> getSingleGameplayFeatureByNameMap(String gameplayFeatureNameEn){
        List<Map<String, String>> list = gameplayFeatures;
        Map<String, String> mapSingleGameplayFeature = null;
        int gameplayFeaturePosition = getGameplayFeatureIdByName(gameplayFeatureNameEn);
        for(int i=0; i<list.size(); i++){
            if(i == gameplayFeaturePosition){
                mapSingleGameplayFeature = list.get(i);
            }
        }
        return mapSingleGameplayFeature;
    }
}
