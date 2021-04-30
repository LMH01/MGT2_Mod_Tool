package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlatformSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSharer.class);

    @Override
    public void doOtherImportThings(String importFolderPath, String name) {
        try{
            File importFolderPictureFolder = new File(importFolderPath + "//DATA//pictures//");
            if(importFolderPictureFolder.exists()){
                ArrayList<File> pictures = DataStreamHelper.getFilesInFolderWhiteList(importFolderPictureFolder.getPath(), ".png");
                Map<Integer, File> importPictureMap = new HashMap<>();
                for(File file : pictures){
                    importPictureMap.put(Integer.parseInt(file.getName().replaceAll("[^0-9]","")), file);
                }
                ModManager.platformMod.getEditor().addImageFiles(name, importPictureMap);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) throws IOException {
        File exportPictures = new File(exportFolderDataPath + "//pictures//");
        exportPictures.mkdirs();
        Map<Integer, File> picturesToExport = new HashMap<>();
        for(Map.Entry<String, String> entry : singleContentMap.entrySet()){
            if(entry.getKey().contains("PIC")){
                picturesToExport.put(Integer.parseInt(entry.getKey().replaceAll("[^0-9]","")), new File(Settings.mgt2FilePath + "//Mad Games Tycoon 2_Data//Extern//Icons_Platforms//" + entry.getValue()));
            }
        }
        for(Map.Entry<Integer, File> entry : picturesToExport.entrySet()){
            File outputFile = new File(exportPictures.getPath() + "//" + entry.getKey() + ".png");
            Files.copy(Paths.get(entry.getValue().getPath()), Paths.get(outputFile.getPath()));
        }
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        for(String string : TranslationManager.TRANSLATION_KEYS){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("MANUFACTURER " + string)){
                    bw.write("[MANUFACTURER " + string + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
            }
        }
        EditHelper.printLine("DATE",map, bw);
        if(map.containsKey("DATE END")){
            EditHelper.printLine("DATE END",map, bw);
        }
        EditHelper.printLine("PRICE",map, bw);
        EditHelper.printLine("DEV COSTS",map, bw);
        EditHelper.printLine("TECHLEVEL",map, bw);
        EditHelper.printLine("UNITS",map, bw);
        ArrayList<String> pictures = new ArrayList<>();
        ArrayList<String> pictureChangeYears = new ArrayList<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getKey().contains("PIC")){
                pictures.add(entry.getValue());
            }
            if(entry.getKey().contains("YEAR")){
                pictureChangeYears.add("[" + entry.getKey() + "]" + entry.getValue());
            }
        }
        for(String string : pictureChangeYears){
            bw.write(string);
            bw.write(System.getProperty("line.separator"));
        }
        ArrayList<Integer> gameplayFeatureIds = new ArrayList<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getKey().contains("NEED")){
                gameplayFeatureIds.add(Integer.parseInt(entry.getValue()));
            }
        }
        int numberOfRunsB = 1;
        for(Integer integer : gameplayFeatureIds){
            bw.write("[NEED-" + numberOfRunsB + "]" + integer);
            bw.write(System.getProperty("line.separator"));
            numberOfRunsB++;
        }
        EditHelper.printLine("COMPLEX",map, bw);
        EditHelper.printLine("INTERNET",map, bw);
        EditHelper.printLine("TYP",map, bw);
    }

    @Override
    public Importer getImporter() {
        return ModManager.platformMod.getBaseEditor()::addMod;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        StringBuilder message = new StringBuilder();
        message.append("<html>");
        message.append(I18n.INSTANCE.get("mod.platform.addPlatform.optionPaneMessage.firstPart")).append("<br><br>");
        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(map.get("NAME EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.manufacturer")).append(": ").append(map.get("MANUFACTURER EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.releaseDate")).append(": ").append(map.get("DATE")).append("<br>");
        if(map.containsKey("DATE END")){
            message.append(I18n.INSTANCE.get("commonText.productionEnd")).append(": ").append(map.get("DATE END")).append("<br>");
        }
        message.append(I18n.INSTANCE.get("commonText.devKitCost")).append(": ").append(map.get("PRICE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.developmentCost")).append(": ").append(map.get("DEV COSTS")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.techLevel")).append(": ").append(map.get("TECHLEVEL")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.units")).append(": ").append(map.get("UNITS")).append("<br>");
        ArrayList<Integer> gameplayFeatureIds = new ArrayList<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getKey().contains("NEED")){
                gameplayFeatureIds.add(Integer.parseInt(entry.getValue()));
            }
        }
        StringBuilder neededGameplayFeatures = new StringBuilder();
        int currentGameplayFeature = 0;
        boolean firstGameplayFeature = true;
        for(Integer integer : gameplayFeatureIds){
            if(firstGameplayFeature){
                firstGameplayFeature = false;
            }else{
                neededGameplayFeatures.append(", ");
            }
            if(currentGameplayFeature == 8){
                neededGameplayFeatures.append("<br>");
                currentGameplayFeature = 0;
            }
            neededGameplayFeatures.append(ModManager.gameplayFeatureMod.getBaseAnalyzer().getContentNameById(integer));
            currentGameplayFeature++;
        }
        message.append(I18n.INSTANCE.get("commonText.neededGameplayFeatures")).append(": ").append(neededGameplayFeatures.toString()).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.complexity")).append(": ").append(map.get("COMPLEX")).append("<br>");
        String internetMessageToPrint;
        if(map.get("INTERNET").equals("0")){
            internetMessageToPrint = Utils.getTranslatedValueFromBoolean(false);
        }else{
            internetMessageToPrint = Utils.getTranslatedValueFromBoolean(true);
        }
        message.append(I18n.INSTANCE.get("commonText.internet")).append(": ").append(internetMessageToPrint).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.type")).append(": ").append(ModManager.platformMod.getPlatformTypeStringById(Integer.parseInt(map.get("TYP")))).append("<br>");
        return message.toString();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.platformMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.platformMod.getBaseAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Platforms//";
    }

    @Override
    public String getFileName() {
        return ModManager.platformMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return ModManager.publisherMod.getMainTranslationKey();
    }

    @Override
    public String getTypeCaps() {
        return "PLATFORM";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.platformMod.getCompatibleModToolVersions();
    }
}
