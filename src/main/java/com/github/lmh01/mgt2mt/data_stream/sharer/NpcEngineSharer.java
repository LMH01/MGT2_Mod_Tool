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

public class NpcEngineSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcEngineSharer.class);
    @Override
    public void doOtherImportThings(String importFolderPath, String name) {

    }

    @Override
    public void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) {

    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        EditHelper.printLine("PLATFORM", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("SHARE", map, bw);
    }

    @Override
    public Importer getImporter() {
        return ModManager.npcEngineMod.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        StringBuilder message = new StringBuilder();
        message.append("<html>");
        message.append(I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.firstPart")).append("<br><br>");
        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(map.get("NAME EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.unlockDate")).append(": ").append(map.get("DATE")).append("<br>");
        if(map.get("GENRE").equals("MULTIPLE SELECTED")){
            message.append(I18n.INSTANCE.get("commonText.genre.upperCase")).append(": ").append((map.get("GENRE"))).append("<br>");
        }else{
            message.append(I18n.INSTANCE.get("commonText.genre.upperCase")).append(": ").append(ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(map.get("GENRE")))).append("<br>");
        }
        message.append(I18n.INSTANCE.get("commonText.platform.upperCase")).append(": ").append(ModManager.platformMod.getBaseAnalyzer().getContentNameById(Integer.parseInt(map.get("PLATFORM")))).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.price")).append(": ").append(map.get("PRICE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.profitShare")).append(": ").append(map.get("SHARE")).append("<br>");
        return message.toString();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.npcEngineMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.npcEngineMod.getBaseAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Npc_Engines//";
    }

    @Override
    public String getFileName() {
        return ModManager.npcEngineMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.npcEngineMod.getMainTranslationKey();
    }

    @Override
    public String getTypeCaps() {
        return "NPC_ENGINE";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.npcEngineMod.getCompatibleModToolVersions();
    }
}
