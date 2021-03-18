package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EditEngineFeaturesFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingGameplayFeatures.class);

    /**
     * Adds a new genre to the EngineFeatures.txt file
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     */
    public static void addEngineFeature(Map<String, String> map) throws IOException {
        AnalyzeExistingEngineFeatures.analyzeEngineFeatures();
        LOGGER.info("Adding new engine feature...");
        File engineFeatureFile = Utils.getEngineFeaturesFile();
        if(engineFeatureFile.exists()){
            engineFeatureFile.delete();
        }
        engineFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(engineFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> existingEngineFeatures : AnalyzeExistingEngineFeatures.engineFeatures){
            bw.write("[ID]" + existingEngineFeatures.get("ID"));bw.write(System.getProperty("line.separator"));
            bw.write("[TYP]" + existingEngineFeatures.get("TYP"));bw.write(System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, existingEngineFeatures);
            bw.write("[DATE]" + existingEngineFeatures.get("DATE"));bw.write(System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + existingEngineFeatures.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[PRICE]" + existingEngineFeatures.get("PRICE"));bw.write(System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + existingEngineFeatures.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
            bw.write("[TECHLEVEL]" + existingEngineFeatures.get("TECHLEVEL"));bw.write(System.getProperty("line.separator"));
            bw.write("[PIC]" + existingEngineFeatures.get("PIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + existingEngineFeatures.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + existingEngineFeatures.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
            bw.write("[SOUND]" + existingEngineFeatures.get("SOUND"));bw.write(System.getProperty("line.separator"));
            bw.write("[TECH]" + existingEngineFeatures.get("TECH"));bw.write(System.getProperty("line.separator"));
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[ID]" + map.get("ID"));bw.write(System.getProperty("line.separator"));
        bw.write("[TYP]" + map.get("TYP"));bw.write(System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, map);
        bw.write("[DATE]" + map.get("DATE"));bw.write(System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + map.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
        bw.write("[PRICE]" + map.get("PRICE"));bw.write(System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + map.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
        bw.write("[TECHLEVEL]" + map.get("TECHLEVEL"));bw.write(System.getProperty("line.separator"));
        bw.write("[PIC]" + map.get("PIC"));bw.write(System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + map.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + map.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
        bw.write("[SOUND]" + map.get("SOUND"));bw.write(System.getProperty("line.separator"));
        bw.write("[TECH]" + map.get("TECH"));bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * Removes the input engine feature id from the EngineFeatures.txt file
     * @param engineFeatureName The gameplay feature id for which the gameplay feature should be removed
     */
    public static boolean removeEngineFeature(String engineFeatureName) throws IOException {
        int engineFeatureId = AnalyzeExistingEngineFeatures.getEngineFeatureIdByName(engineFeatureName);
        AnalyzeExistingEngineFeatures.analyzeEngineFeatures();
        LOGGER.info("Removing engine feature...");
        File engineFeatureFile = Utils.getEngineFeaturesFile();
        if (engineFeatureFile.exists()) {
            engineFeatureFile.delete();
        }
        engineFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(engineFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for (Map<String, String> existingEngineFeatures : AnalyzeExistingEngineFeatures.engineFeatures) {
            if (Integer.parseInt(existingEngineFeatures.get("ID")) != engineFeatureId) {
                bw.write("[ID]" + existingEngineFeatures.get("ID"));bw.write(System.getProperty("line.separator"));
                bw.write("[TYP]" + existingEngineFeatures.get("TYP"));bw.write(System.getProperty("line.separator"));
                TranslationManager.printLanguages(bw, existingEngineFeatures);
                bw.write("[DATE]" + existingEngineFeatures.get("DATE"));bw.write(System.getProperty("line.separator"));
                bw.write("[RES POINTS]" + existingEngineFeatures.get("RES POINTS"));bw.write(System.getProperty("line.separator"));
                bw.write("[PRICE]" + existingEngineFeatures.get("PRICE"));bw.write(System.getProperty("line.separator"));
                bw.write("[DEV COSTS]" + existingEngineFeatures.get("DEV COSTS"));bw.write(System.getProperty("line.separator"));
                bw.write("[TECHLEVEL]" + existingEngineFeatures.get("TECHLEVEL"));bw.write(System.getProperty("line.separator"));
                bw.write("[PIC]" + existingEngineFeatures.get("PIC"));bw.write(System.getProperty("line.separator"));
                bw.write("[GAMEPLAY]" + existingEngineFeatures.get("GAMEPLAY"));bw.write(System.getProperty("line.separator"));
                bw.write("[GRAPHIC]" + existingEngineFeatures.get("GRAPHIC"));bw.write(System.getProperty("line.separator"));
                bw.write("[SOUND]" + existingEngineFeatures.get("SOUND"));bw.write(System.getProperty("line.separator"));
                bw.write("[TECH]" + existingEngineFeatures.get("TECH"));bw.write(System.getProperty("line.separator"));
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
        WindowMain.checkActionAvailability();
        return true;
    }
}
