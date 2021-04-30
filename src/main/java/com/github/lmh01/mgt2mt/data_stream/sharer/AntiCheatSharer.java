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

public class AntiCheatSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AntiCheatSharer.class);

    @Override
    public void doOtherImportThings(String importFolderPath, String name) {

    }

    @Override
    public void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) throws IOException {

    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
    }

    @Override
    public Importer getImporter() {
        return ModManager.antiCheatMod.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        StringBuilder message = new StringBuilder();
        message.append("<html>");
        message.append(I18n.INSTANCE.get("mod.antiCheat.addMod.optionPaneMessage.firstPart")).append("<br><br>");
        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(map.get("NAME EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.unlockDate")).append(": ").append(map.get("DATE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.price")).append(": ").append(map.get("PRICE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.developmentCost")).append(": ").append(map.get("DEV COSTS")).append("<br>");
        return message.toString();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.antiCheatMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.antiCheatMod.getBaseAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Anti_Cheat//";
    }

    @Override
    public String getFileName() {
        return ModManager.antiCheatMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.antiCheatMod.getMainTranslationKey();
    }

    @Override
    public String getTypeCaps() {
        return "ANTI_CHEAT";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.antiCheatMod.getCompatibleModToolVersions();
    }
}
