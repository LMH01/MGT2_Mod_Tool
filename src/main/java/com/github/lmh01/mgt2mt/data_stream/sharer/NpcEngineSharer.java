package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
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
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        bw.write("[GENRE]" + ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(map.get("GENRE"))));bw.write(System.getProperty("line.separator"));
        bw.write("[PLATFORM]" + ModManager.platformMod.getBaseAnalyzer().getContentNameById(Integer.parseInt(map.get("PLATFORM"))));bw.write(System.getProperty("line.separator"));
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("SHARE", map, bw);
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) {
        int genreId = ModManager.genreMod.getAnalyzer().getContentIdByName(map.get("GENRE"));
        if(genreId == -1){
            map.replace("GENRE", Integer.toString(Utils.getRandomNumber(0, ModManager.genreMod.getAnalyzer().getFileContent().size()-1)));
        }else{
            map.replace("GENRE", Integer.toString(genreId));
        }
        int platformId = ModManager.platformMod.getBaseAnalyzer().getContentIdByName(map.get("PLATFORM"));
        if(platformId == -1){
            map.replace("PLATFORM", Integer.toString(Utils.getRandomNumber(0, ModManager.platformMod.getBaseAnalyzer().getFileContent().size()-1)));
        }else{
            map.replace("PLATFORM", Integer.toString(platformId));
        }
        return super.getChangedImportMap(map);
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
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.npcEngineMod;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getImportExportFileName() {
        return "npcEngine.txt";
    }

    @Override
    public String getTypeCaps() {
        return "NPC_ENGINE";
    }
}
