package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedModOld;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameplayFeatureSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureSharer.class);

    @Override
    public Importer getImporter() {
        return ModManager.gameplayFeatureModOld.getEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        Map<String, String> workingMap = new HashMap<>(map);
        if(!workingMap.get("BAD").matches(".*\\d.*")){
            ArrayList<String> badGenreNames = Utils.getEntriesFromString(workingMap.get("BAD"));
            ArrayList<String> goodGenreNames = Utils.getEntriesFromString(workingMap.get("GOOD"));
            ArrayList<Integer> badGenreIds = new ArrayList<>();
            ArrayList<Integer> goodGenreIds = new ArrayList<>();
            for(String string : badGenreNames){
                try{
                    badGenreIds.add(Integer.parseInt(string));
                }catch(NumberFormatException e){
                    int numberToAdd = ModManager.genreModOld.getAnalyzer().getContentIdByName(string);
                    if(numberToAdd != -1){
                        badGenreIds.add(numberToAdd);
                    }
                }
            }
            for(String string : goodGenreNames){
                try{
                    goodGenreIds.add(Integer.parseInt(string));
                }catch(NumberFormatException e){
                    int numberToAdd = ModManager.genreModOld.getAnalyzer().getContentIdByName(string);
                    if(numberToAdd != -1){
                        goodGenreIds.add(numberToAdd);
                    }
                }
            }
            workingMap.remove("BAD");
            workingMap.remove("GOOD");
            workingMap.put("BAD", Utils.transformArrayListToString(badGenreIds));
            workingMap.put("GOOD", Utils.transformArrayListToString(goodGenreIds));
        }
        StringBuilder badGenresFeatures = new StringBuilder();
        boolean firstBadFeature = true;
        if(workingMap.get("BAD").equals("")){
            badGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        }else{
            for(String string : Utils.getEntriesFromString(workingMap.get("BAD"))){
                if(!firstBadFeature){
                    badGenresFeatures.append(", ");
                }else{
                    firstBadFeature = false;
                }
                badGenresFeatures.append(ModManager.genreModOld.getAnalyzer().getContentNameById(Integer.parseInt(string)));
            }
        }
        StringBuilder goodGenresFeatures = new StringBuilder();
        boolean firstGoodFeature = true;
        if(workingMap.get("GOOD").equals("")){
            goodGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        }else{
            for(String string : Utils.getEntriesFromString(workingMap.get("GOOD"))){
                if(!firstGoodFeature){
                    goodGenresFeatures.append(", ");
                }else{
                    firstGoodFeature = false;
                }
                goodGenresFeatures.append(ModManager.genreModOld.getAnalyzer().getContentNameById(Integer.parseInt(string)));
            }
        }
        String arcadeCompatibility = I18n.INSTANCE.get("commonText.yes");
        String mobileCompatibility = I18n.INSTANCE.get("commonText.yes");
        if(workingMap.get("NO_ARCADE") != null){
            arcadeCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        if(workingMap.get("NO_MOBILE") != null){
            mobileCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        return I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + workingMap.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + workingMap.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + workingMap.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + ModManager.gameplayFeatureModOld.getGameplayFeatureNameByTypeId(Integer.parseInt(workingMap.get("TYP"))) + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + workingMap.get("RES POINTS") + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + workingMap.get("PRICE") + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + workingMap.get("DEV COSTS") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.badGenres") + "*\n\n" + badGenresFeatures + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.goodGenres") + "*\n\n" + goodGenresFeatures + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + workingMap.get("GAMEPLAY") + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + workingMap.get("GRAPHIC") + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + workingMap.get("SOUND") + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + workingMap.get("TECH") + "\n" +
                I18n.INSTANCE.get("commonText.arcadeCompatibility") + ": " + arcadeCompatibility + "\n" +
                I18n.INSTANCE.get("commonText.mobileCompatibility") + ": " + mobileCompatibility + "\n";
    }

    @Override
    public AbstractAdvancedModOld getAdvancedMod() {
        return ModManager.gameplayFeatureModOld;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        bw.write("[TYP]" + map.get("TYP") + System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, map);
        bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + map.get("RES POINTS") + System.getProperty("line.separator"));
        bw.write("[PRICE]" + map.get("PRICE") + System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + map.get("DEV COSTS") + System.getProperty("line.separator"));
        bw.write("[PIC]" + map.get("PIC") + System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + map.get("GAMEPLAY") + System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + map.get("GRAPHIC") + System.getProperty("line.separator"));
        bw.write("[SOUND]" + map.get("SOUND") + System.getProperty("line.separator"));
        bw.write("[TECH]" + map.get("TECH") + System.getProperty("line.separator"));
        if(map.get("BAD") != null){
            bw.write("[BAD]" + ModManager.genreModOld.getAnalyzer().getGenreNames(map.get("BAD")) + System.getProperty("line.separator"));
        }else{
            bw.write("[BAD]" + "" + System.getProperty("line.separator"));
        }
        if(map.get("GOOD") != null){
            bw.write("[GOOD]" + ModManager.genreModOld.getAnalyzer().getGenreNames(map.get("GOOD")) + System.getProperty("line.separator"));
        }else{
            bw.write("[GOOD]" + "" + System.getProperty("line.separator"));
        }
        if(map.get("NO_ARCADE") != null){
            bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
        }
        if(map.get("NO_MOBILE") != null){
            bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
        }
    }

    @Override
    public String getImportExportFileName() {
        return "gameplayFeature.txt";
    }

    @Override
    public String getTypeCaps() {
        return "GAMEPLAY FEATURE";
    }
}
