package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
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
        AnalyzeManager.engineFeatureAnalyzer.analyzeFile();
        LOGGER.info("Adding new engine feature...");
        File engineFeatureFile = Utils.getEngineFeaturesFile();
        if(engineFeatureFile.exists()){
            engineFeatureFile.delete();
        }
        engineFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(engineFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for(Map<String, String> existingEngineFeatures : AnalyzeManager.engineFeatureAnalyzer.getFileContent()){
            EditHelper.printLine("ID", existingEngineFeatures, bw);
            EditHelper.printLine("TYP", existingEngineFeatures, bw);
            TranslationManager.printLanguages(bw, existingEngineFeatures);
            EditHelper.printLine("DATE", existingEngineFeatures, bw);
            EditHelper.printLine("RES POINTS", existingEngineFeatures, bw);
            EditHelper.printLine("PRICE", existingEngineFeatures, bw);
            EditHelper.printLine("DEV COSTS", existingEngineFeatures, bw);
            EditHelper.printLine("TECHLEVEL", existingEngineFeatures, bw);
            EditHelper.printLine("PIC", existingEngineFeatures, bw);
            EditHelper.printLine("GAMEPLAY", existingEngineFeatures, bw);
            EditHelper.printLine("GRAPHIC", existingEngineFeatures, bw);
            EditHelper.printLine("SOUND", existingEngineFeatures, bw);
            EditHelper.printLine("TECH", existingEngineFeatures, bw);
            bw.write(System.getProperty("line.separator"));
        }
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
        bw.write(System.getProperty("line.separator"));
        bw.write("[EOF]");
        bw.close();
    }

    /**
     * Removes the input engine feature id from the EngineFeatures.txt file
     * @param engineFeatureName The gameplay feature id for which the gameplay feature should be removed
     */
    public static boolean removeEngineFeature(String engineFeatureName) throws IOException {
        int engineFeatureId = AnalyzeManager.engineFeatureAnalyzer.getContentIdByName(engineFeatureName);
        AnalyzeManager.engineFeatureAnalyzer.analyzeFile();
        LOGGER.info("Removing engine feature...");
        File engineFeatureFile = Utils.getEngineFeaturesFile();
        if (engineFeatureFile.exists()) {
            engineFeatureFile.delete();
        }
        engineFeatureFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(engineFeatureFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        for (Map<String, String> existingEngineFeatures : AnalyzeManager.engineFeatureAnalyzer.getFileContent()) {
            if (Integer.parseInt(existingEngineFeatures.get("ID")) != engineFeatureId) {
                EditHelper.printLine("ID", existingEngineFeatures, bw);
                EditHelper.printLine("TYP", existingEngineFeatures, bw);
                TranslationManager.printLanguages(bw, existingEngineFeatures);
                EditHelper.printLine("DATE", existingEngineFeatures, bw);
                EditHelper.printLine("RES POINTS", existingEngineFeatures, bw);
                EditHelper.printLine("PRICE", existingEngineFeatures, bw);
                EditHelper.printLine("DEV COSTS", existingEngineFeatures, bw);
                EditHelper.printLine("TECHLEVEL", existingEngineFeatures, bw);
                EditHelper.printLine("PIC", existingEngineFeatures, bw);
                EditHelper.printLine("GAMEPLAY", existingEngineFeatures, bw);
                EditHelper.printLine("GRAPHIC", existingEngineFeatures, bw);
                EditHelper.printLine("SOUND", existingEngineFeatures, bw);
                EditHelper.printLine("TECH", existingEngineFeatures, bw);
                bw.write(System.getProperty("line.separator"));
            }
        }
        bw.write("[EOF]");
        bw.close();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.engineFeature") + " - " + engineFeatureName);
        return true;
    }
}
