package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class HardwareSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareFeatureSharer.class);
    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("TYP",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        if(map.containsKey("ONLY_HANDHELD")){
            EditHelper.printLine("ONLY_HANDHELD", map, bw);
        }
        if(map.containsKey("ONLY_STATIONARY")){
            EditHelper.printLine("ONLY_STATIONARY", map, bw);
        }
    }

    @Override
    public Importer getImporter() {
        return ModManager.hardwareMod.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map){
        boolean allowStationaryConsole = true;
        boolean allowPortableConsole = true;
        if(map.containsKey("ONLY_STATIONARY")){
            allowPortableConsole = false;
        }
        if(map.containsKey("ONLY_HANDHELD")){
            allowStationaryConsole = false;
        }
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.stationaryConsole")).append(": ");
        if(allowStationaryConsole){
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        }else{
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.portableConsole")).append(": ");
        if(allowPortableConsole){
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        }else{
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardware.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + ModManager.hardwareMod.getHardwareTypeNameById(Integer.parseInt(map.get("TYP"))) + "<br>" +
                lastPart.toString();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.hardwareMod.getBaseAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Hardware//";
    }

    @Override
    public String getFileName() {
        return ModManager.hardwareMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.hardwareMod.getMainTranslationKey();
    }

    @Override
    public String getTypeCaps() {
        return "HARDWARE";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.hardwareMod.getCompatibleModToolVersions();
    }
}
