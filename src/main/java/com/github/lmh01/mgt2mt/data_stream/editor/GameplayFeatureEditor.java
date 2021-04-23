package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameplayFeatureEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureEditor.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature");
    }

    @Override
    public File getFileToEdit() {
        return Utils.getGameplayFeaturesFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
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
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return AnalyzeManager.gameplayFeatureAnalyzer;
    }

    /**
     * Edits the GameplayFeatures.txt file to add genre id to the input gameplay feature
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId The genre id that should be used
     * @param goodFeature True when the id should be added as good to the feature. False when it should be added as bad.
     */
    public void addGenreId(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean goodFeature) throws IOException {
        editGenreIdAllocation(gameplayFeaturesIdsToEdit, genreId, true, goodFeature);
    }

    /**
     * Edits the GameplayFeatures.txt file to remove genre id from the input gameplay feature
     * @param genreId The genre id that should be used
     */
    public void removeGenreId(int genreId) throws IOException {
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
    private void editGenreIdAllocation(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean addGenreId, boolean goodFeature) throws IOException {
        LOGGER.info("Editing GameplayFeatures.txt file");
        File gameplayFeaturesFile = Utils.getGameplayFeaturesFile();
        if(gameplayFeaturesFile.exists()){
            gameplayFeaturesFile.delete();
        }
        gameplayFeaturesFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeaturesFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> map : AnalyzeManager.gameplayFeatureAnalyzer.getFileContent()) {
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
            if(map.get("NO_ARCADE") != null){
                bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
            }
            if(map.get("NO_MOBILE") != null){
                bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
            }
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[EOF]");
        bw.close();
    }
}
