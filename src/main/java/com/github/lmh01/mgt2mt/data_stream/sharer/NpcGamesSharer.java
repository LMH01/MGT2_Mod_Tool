package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.interfaces.SimpleImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class NpcGamesSharer extends AbstractSimpleSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcGamesSharer.class);

    @Override
    public SimpleImporter getSimpleImporter() {
        return ModManager.npcGamesMod.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(String line) {
        StringBuilder message = new StringBuilder();
        StringBuilder name = new StringBuilder();
        StringBuilder genreIdsRaw = new StringBuilder();
        StringBuilder genreNamesToDisplay = new StringBuilder();
        ArrayList<Integer> genreIds = new ArrayList<>();
        message.append("<html>");
        boolean nameComplete = false;
        for(Character character : line.toCharArray()){
            if(!nameComplete){
                if(character.toString().equals("<")){
                    nameComplete = true;
                    message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(name.toString()).append("<br>");
                }
            }
            if(!nameComplete){
                name.append(character);
            }else{
                genreIdsRaw.append(character);
            }
        }
        ArrayList<String> genreIdsAsString = Utils.getEntriesFromString(genreIdsRaw.toString());
        for(String string : genreIdsAsString){
            try{
                genreIds.add(Integer.parseInt(string));
            }catch(NumberFormatException ignored){

            }
        }
        int currentInt = 0;
        boolean firstInt = true;
        for(Integer integer : genreIds){
            if(firstInt){
                firstInt = false;
            }else{
                genreNamesToDisplay.append(", ");
            }
            if(currentInt == 8){
                genreNamesToDisplay.append("<br>");
                currentInt = 0;
            }
            genreNamesToDisplay.append(ModManager.genreMod.getBaseAnalyzer().getContentNameById(integer));
            currentInt++;
        }
        message.append(ModManager.genreMod.getTypePlural()).append(": ").append(genreNamesToDisplay.toString());
        return message.toString();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.npcGamesMod.getType();
    }

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.npcGamesMod.getBaseAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Npc_Games//";
    }

    @Override
    public String getFileName() {
        return ModManager.npcGamesMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.npcGamesMod.getMainTranslationKey();
    }

    @Override
    public String getTypeCaps() {
        return "NPC_GAME";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.npcGamesMod.getCompatibleModToolVersions();
    }
}
