package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EngineFeatureHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class EngineFeatureSharer extends AbstractAdvancedSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureSharer.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public Importer getImporter() {
        return EditorManager.engineFeatureEditor::addMod;
    }

    @Override
    String getOptionPaneMessage(Map<String, String> map) {//TODO Übersetzung hinzufügen
        String messageBody = I18n.INSTANCE.get("sharer.engineFeature.optionPaneMessage.main") + "\n\n" +
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
        return messageBody;
    }

    @Override
    void doOtherImportThings(String importFolderPath) {

    }

    @Override
    void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) {

    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.engineFeature");
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return AnalyzeManager.engineFeatureAnalyzer;
    }

    @Override
    public String getExportFolder() {
        return "//Engine features//";
    }

    @Override
    public String getFileName() {
        return "engineFeature.txt";
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        bw.write("[TYP]" + map.get("TYP") + System.getProperty("line.separator"));
        bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + map.get("RES POINTS") + System.getProperty("line.separator"));
        bw.write("[PRICE]" + map.get("PRICE") + System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + map.get("DEV COSTS") + System.getProperty("line.separator"));
        bw.write("[TECHLEVEL]" + map.get("TECHLEVEL") + System.getProperty("line.separator"));
        bw.write("[PIC]" + map.get("PIC") + System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + map.get("GAMEPLAY") + System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + map.get("GRAPHIC") + System.getProperty("line.separator"));
        bw.write("[SOUND]" + map.get("SOUND") + System.getProperty("line.separator"));
        bw.write("[TECH]" + map.get("TECH") + System.getProperty("line.separator"));
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public String getTypeCaps() {
        return "ENGINE FEATURE";
    }
}
