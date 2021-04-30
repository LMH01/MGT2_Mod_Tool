package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class EngineFeatureSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureSharer.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.engineFeatureMod.getCompatibleModToolVersions();
    }

    @Override
    public Importer getImporter() {
        return ModManager.engineFeatureMod.getEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        String messageBody = I18n.INSTANCE.get("sharer.engineFeature.optionPaneMessage.main") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + ModManager.engineFeatureMod.getEngineFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + map.get("PRICE") + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "\n" +
                I18n.INSTANCE.get("commonText.techLevel") + ": " + map.get("TECHLEVEL") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + map.get("GAMEPLAY") + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + map.get("GRAPHIC") + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + map.get("SOUND") + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + map.get("TECH") + "\n";
        return messageBody;
    }

    @Override
    void doOtherImportThings(String importFolderPath, String name) {

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
        return ModManager.engineFeatureMod.getAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Engine features//";
    }

    @Override
    public String getFileName() {
        return ModManager.engineFeatureMod.getFileName();
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
