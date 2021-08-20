package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedModOld;
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

public class HardwareFeatureSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareFeatureSharer.class);
    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("QUALITY", map, bw);
        if(map.containsKey("ONLY_STATIONARY")){
            bw.write("[ONLY_STATIONARY]");
            bw.write(System.getProperty("line.separator"));
        }
        if(map.containsKey("NEEDINTERNET")){
            bw.write("[NEEDINTERNET]");
            bw.write(System.getProperty("line.separator"));
        }
    }

    @Override
    public Importer getImporter() {
        return ModManager.hardwareFeatureModOld.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.needInternet")).append(": ");
        if(map.containsKey("NEEDINTERNET")){
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        }else{
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.stationary")).append(": ");
        if(map.containsKey("ONLY_STATIONARY")){
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        }else{
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardwareFeature.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("commonText.quality") + ": " + map.get("QUALITY") + "<br>" +
                lastPart;
    }

    @Override
    public AbstractAdvancedModOld getAdvancedMod() {
        return ModManager.hardwareFeatureModOld;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getImportExportFileName() {
        return "HardwareFeatures.txt";
    }

    @Override
    public String getTypeCaps() {
        return "HARDWARE_FEATURE";
    }
}
