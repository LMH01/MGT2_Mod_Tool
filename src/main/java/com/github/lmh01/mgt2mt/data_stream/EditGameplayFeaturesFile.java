package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
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
            bw.write("[ID]" + map.get("ID"));bw.write(System.getProperty("line.separator"));
            bw.write("[TYP]" + map.get("TYP"));bw.write(System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, map);
            bw.write("[DATE]" + map.get("DATE"));bw.write(System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + map.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PRICE]" + map.get("PRICE"));bw.write(System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + map.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PIC]" + map.get("PIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + map.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + map.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[SOUND]" + map.get("SOUND"));bw.write(System.getProperty("line.separator"));
            bw.write("[TECH]" + map.get("TECH"));bw.write(System.getProperty("line.separator"));
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
