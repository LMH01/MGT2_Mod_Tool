package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditGameplayFeaturesFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);

    /**
     * Adds a new genre to the GameplayFeatures.txt file
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     */
    public static void addGameplayFeature(Map<String, String> map) throws IOException {
        AnalyzeExistingGameplayFeatures.analyzeGameplayFeatures();
        LOGGER.info("Adding new gameplay feature...");
        File gameplayFeatureFile = Utils.getGameplayFeaturesFile();
        if(gameplayFeatureFile.exists()){
            gameplayFeatureFile.delete();
        }
        gameplayFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> existingGameplayFeatures : AnalyzeExistingGameplayFeatures.gameplayFeatures){
            EditHelper.printLine("ID", existingGameplayFeatures, bw);
            EditHelper.printLine("TYP", existingGameplayFeatures, bw);
            TranslationManager.printLanguages(bw, existingGameplayFeatures);
            EditHelper.printLine("DATE", existingGameplayFeatures, bw);
            EditHelper.printLine("RES POINTS", existingGameplayFeatures, bw);
            EditHelper.printLine("PRICE", existingGameplayFeatures, bw);
            EditHelper.printLine("DEV COSTS", existingGameplayFeatures, bw);
            EditHelper.printLine("PIC", existingGameplayFeatures, bw);
            EditHelper.printLine("GAMEPLAY", existingGameplayFeatures, bw);
            EditHelper.printLine("GRAPHIC", existingGameplayFeatures, bw);
            EditHelper.printLine("SOUND", existingGameplayFeatures, bw);
            EditHelper.printLine("TECH", existingGameplayFeatures, bw);
            existingGameplayFeatures.putIfAbsent("GOOD", "");
            existingGameplayFeatures.putIfAbsent("BAD", "");
            EditHelper.printLine("GOOD", existingGameplayFeatures, bw);
            EditHelper.printLine("BAD", existingGameplayFeatures, bw);
            if(existingGameplayFeatures.get("NO_ARCADE") != null){
                bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
            }
            if(existingGameplayFeatures.get("NO_MOBILE") != null){
                bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
            }
            bw.write(System.getProperty("line.separator"));
        }
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
        map.putIfAbsent("GOOD", "");
        map.putIfAbsent("BAD", "");
        EditHelper.printLine("GOOD", map, bw);
        EditHelper.printLine("BAD", map, bw);
        if(map.get("NO_ARCADE") != null){
            bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
        }
        if(map.get("NO_MOBILE") != null){
            bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
        }
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * Removes the input gameplay feature id from the GameplayFeatures.txt file
     * @param gameplayFeatureName The gameplay feature name that should be removed
     */
    public static boolean removeGameplayFeature(String gameplayFeatureName) throws IOException {
        int gameplayFeatureId = AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(gameplayFeatureName);
        AnalyzeExistingGameplayFeatures.analyzeGameplayFeatures();
        LOGGER.info("Removing gameplay feature...");
        File gameplayFeatureFile = Utils.getGameplayFeaturesFile();
        if(gameplayFeatureFile.exists()){
            gameplayFeatureFile.delete();
        }
        gameplayFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> existingGameplayFeatures : AnalyzeExistingGameplayFeatures.gameplayFeatures){
            if(Integer.parseInt(existingGameplayFeatures.get("ID")) != gameplayFeatureId){
                EditHelper.printLine("ID", existingGameplayFeatures, bw);
                EditHelper.printLine("TYP", existingGameplayFeatures, bw);
                TranslationManager.printLanguages(bw, existingGameplayFeatures);
                EditHelper.printLine("DATE", existingGameplayFeatures, bw);
                EditHelper.printLine("RES POINTS", existingGameplayFeatures, bw);
                EditHelper.printLine("PRICE", existingGameplayFeatures, bw);
                EditHelper.printLine("DEV COSTS", existingGameplayFeatures, bw);
                EditHelper.printLine("PIC", existingGameplayFeatures, bw);
                EditHelper.printLine("GAMEPLAY", existingGameplayFeatures, bw);
                EditHelper.printLine("GRAPHIC", existingGameplayFeatures, bw);
                EditHelper.printLine("SOUND", existingGameplayFeatures, bw);
                EditHelper.printLine("TECH", existingGameplayFeatures, bw);
                existingGameplayFeatures.putIfAbsent("GOOD", "");
                existingGameplayFeatures.putIfAbsent("BAD", "");
                EditHelper.printLine("GOOD", existingGameplayFeatures, bw);
                EditHelper.printLine("BAD", existingGameplayFeatures, bw);
                if(existingGameplayFeatures.get("NO_ARCADE") != null){
                    bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
                }
                if(existingGameplayFeatures.get("NO_MOBILE") != null){
                    bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
                }
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.gameplayFeature") + " - " + gameplayFeatureName);
        return true;
    }

    /**
     * Edits the GameplayFeatures.txt file to add genre id to the input gameplay feature
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId The genre id that should be used
     * @param goodFeature True when the id should be added as good to the feature. False when it should be added as bad.
     */
    public static void addGenreId(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean goodFeature) throws IOException {
        editGenreIdAllocation(gameplayFeaturesIdsToEdit, genreId, true, goodFeature);
    }

    /**
     * Edits the GameplayFeatures.txt file to remove genre id from the input gameplay feature
     * @param genreId The genre id that should be used
     */
    public static void removeGenreId(int genreId) throws IOException {
        Set<Integer> set = new HashSet<>();
        editGenreIdAllocation(set, genreId, false, false);
    }

    /**
     * Edits the GameplayFeatures.txt file to add/remove genre ids to/from the input gameplay feature
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId The genre id that should be used
     * @param addGenreId True when the genre id should be added. False when the genre id should be removed.
     * @param goodFeature True when the id should be added as good to the feature. False when it should be added as bad.
     */
    private static void editGenreIdAllocation(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean addGenreId, boolean goodFeature) throws IOException {
        LOGGER.info("Editing GameplayFeatures.txt file");
        File gameplayFeaturesFile = Utils.getGameplayFeaturesFile();
        if(gameplayFeaturesFile.exists()){
            gameplayFeaturesFile.delete();
        }
        gameplayFeaturesFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeaturesFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> map : AnalyzeExistingGameplayFeatures.gameplayFeatures) {
            boolean activeGameplayFeature = false;
            for(Integer integer : gameplayFeaturesIdsToEdit){
                if(map.get("ID").equals(Integer.toString(integer))){
                    activeGameplayFeature = true;
                }
            }
            EditHelper.printLine("ID", map, bw);
            EditHelper.printLine("TYP", map, bw);
            TranslationManager.printLanguages(bw, map);
            EditHelper.printLine("DATE", map, bw);
            EditHelper.printLine("RES POINTS", map, bw);
            EditHelper.printLine("PRICE", map, bw);
            EditHelper.printLine("DEV COSTS", map, bw);
            EditHelper.printLine("PIC", map, bw);
            EditHelper.printLine("GAMEPLAY", map, bw);
            EditHelper.printLine("GRAPHIC", map, bw);
            EditHelper.printLine("SOUND", map, bw);
            EditHelper.printLine("TECH", map, bw);
            String mapValueBad = "";
            String mapValueGood = "";
            if(map.get("BAD") != null){
                mapValueBad = map.get("BAD");
            }
            if(map.get("GOOD") != null){
                mapValueGood = map.get("GOOD");
            }
            if(activeGameplayFeature || !addGenreId){
                if(addGenreId){
                    if(goodFeature){
                        bw.write("[GOOD]" + mapValueGood);bw.write("<" + genreId + ">");bw.write(System.getProperty("line.separator"));
                        bw.write("[BAD]" + mapValueBad);bw.write(System.getProperty("line.separator"));
                    }else{
                        bw.write("[GOOD]" + mapValueGood);bw.write(System.getProperty("line.separator"));
                        bw.write("[BAD]" + mapValueBad);bw.write("<" + genreId + ">");bw.write(System.getProperty("line.separator"));
                    }
                }else{
                    bw.write("[GOOD]" + mapValueGood.replace("<" + genreId + ">", ""));bw.write(System.getProperty("line.separator"));
                    bw.write("[BAD]" + mapValueBad.replace("<" + genreId + ">", ""));bw.write(System.getProperty("line.separator"));
                }
            }else{
                bw.write("[GOOD]" + mapValueGood);bw.write(System.getProperty("line.separator"));
                bw.write("[BAD]" + mapValueBad);bw.write(System.getProperty("line.separator"));
            }
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[EOF]");
        bw.close();
    }

}
