package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.helper.EngineFeatureHelper;
import com.github.lmh01.mgt2mt.util.helper.GameplayFeatureHelper;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Summaries {
    //Contains functions that show messages to the user, asking to confirm the import/addition of feature

    /**
     * Opens a message to the user asking to confirm the addition of engine feature
     * @param map The map containing the values
     * @return Returns true if user clicked ok. Returns false if user clicked something else
     */
    public static boolean showEngineFeatureMessage(Map<String, String> map){
        String messageBody = "Your engine feature is ready:\n\n" +
                "Name: " + map.get("NAME EN") + "\n" +
                "Description: " + map.get("DESC EN") + "\n" +
                "Unlock date: " + map.get("DATE") + "\n" +
                "Type: " + EngineFeatureHelper.getEngineFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                "Research point cost: " + map.get("RES POINTS") + "\n" +
                "Research cost " + map.get("PRICE") + "\n" +
                "Development cost: " + map.get("DEV COSTS") + "\n" +
                "Tech level: " + map.get("TECHLEVEL") + "\n" +
                "\n*Points*\n\n" +
                "Gameplay: " + map.get("GAMEPLAY") + "\n" +
                "Graphic: " + map.get("GRAPHIC") + "\n" +
                "Sound: " + map.get("SOUND") + "\n" +
                "Tech: " + map.get("TECH") + "\n";
        return JOptionPane.showConfirmDialog(null, messageBody, "Add gameplay feature?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean showGameplayFeatureMessage(Map<String, String> map){
        if(!map.get("BAD").matches(".*\\d.*")){
            ArrayList<String> badGenreNames = Utils.getEntriesFromString(map.get("BAD"));
            ArrayList<String> goodGenreNames = Utils.getEntriesFromString(map.get("GOOD"));
            ArrayList<Integer> badGenreIds = new ArrayList<>();
            ArrayList<Integer> goodGenreIds = new ArrayList<>();
            for(String string : badGenreNames){
                badGenreIds.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
            }
            for(String string : goodGenreNames){
                goodGenreIds.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
            }
            map.remove("BAD");
            map.remove("GOOD");
            map.put("BAD", Utils.transformArrayListToString(badGenreIds));
            map.put("GOOD", Utils.transformArrayListToString(goodGenreIds));
        }
        StringBuilder badGenresFeatures = new StringBuilder();
        boolean firstBadFeature = true;
        if(map.get("BAD").equals("")){
            badGenresFeatures.append("None");
        }else{
            for(String string : Utils.getEntriesFromString(map.get("BAD"))){
                if(!firstBadFeature){
                    badGenresFeatures.append(", ");
                }else{
                    firstBadFeature = false;
                }
                badGenresFeatures.append(AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(string)));
            }
        }
        StringBuilder goodGenresFeatures = new StringBuilder();
        boolean firstGoodFeature = true;
        if(map.get("GOOD").equals("")){
            goodGenresFeatures.append("None");
        }else{
            for(String string : Utils.getEntriesFromString(map.get("GOOD"))){
                if(!firstGoodFeature){
                    goodGenresFeatures.append(", ");
                }else{
                    firstGoodFeature = false;
                }
                goodGenresFeatures.append(AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(string)));
            }
        }
        String arcadeCompatibility = "yes";
        String mobileCompatibility = "yes";
        if(map.get("NO_ARCADE") != null){
            arcadeCompatibility = "no";
        }
        if(map.get("NO_MOBILE") != null){
            mobileCompatibility = "no";
        }
        String messageBody = "Your gameplay feature is ready:\n\n" +
                "Name: " + map.get("NAME EN") + "\n" +
                "Description: " + map.get("DESC EN") + "\n" +
                "Unlock date: " + map.get("DATE") + "\n" +
                "Type: " + GameplayFeatureHelper.getGameplayFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                "Research point cost: " + map.get("RES POINTS") + "\n" +
                "Research cost " + map.get("PRICE") + "\n" +
                "Development cost: " + map.get("DEV COSTS") + "\n" +
                "\n*Bad genres*\n\n" + badGenresFeatures.toString() + "\n" +
                "\n*Good genres*\n\n" + goodGenresFeatures.toString() + "\n" +
                "\n*Points*\n\n" +
                "Gameplay: " + map.get("GAMEPLAY") + "\n" +
                "Graphic: " + map.get("GRAPHIC") + "\n" +
                "Sound: " + map.get("SOUND") + "\n" +
                "Tech: " + map.get("TECH") + "\n\n" +
                "Arcade compatibility: " + arcadeCompatibility + "\n" +
                "Mobile compatibility: " + mobileCompatibility + "\n";
        return JOptionPane.showConfirmDialog(null, messageBody, "Add gameplay feature?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
