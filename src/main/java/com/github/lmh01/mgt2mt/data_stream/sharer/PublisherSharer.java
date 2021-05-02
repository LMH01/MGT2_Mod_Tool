package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublisherSharer extends AbstractAdvancedSharer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherSharer.class);

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws IOException {
        getAnalyzer().analyzeFile();
        ModManager.genreMod.getAnalyzer().analyzeFile();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + getType());
        File fileToImport = new File(importFolderPath + "\\" + getFileName());
        HashMap<String, String> map = new HashMap<>();
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileToImport);
        map.put("ID", Integer.toString(getAnalyzer().getFreeId()));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE")){
                int genreID = ModManager.genreMod.getAnalyzer().getContentIdByName(entry.getValue());
                if(genreID == -1){
                    int randomGenreID = Utils.getRandomNumber(0, ModManager.genreMod.getAnalyzer().getFileContent().size()-1);
                    LOGGER.info("Genre list size: " + ModManager.genreMod.getAnalyzer().getFileContent().size());
                    map.put("GENRE", Integer.toString(randomGenreID));
                }else{
                    map.put("GENRE", Integer.toString(ModManager.genreMod.getAnalyzer().getContentIdByName(entry.getValue())));
                }
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + map.get("NAME EN"));
            return getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingContent : getAnalyzer().getFileContent()){
            for(Map.Entry<String, String> entry : existingContent.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + map.get("NAME EN"));
                    return "false";
                }
            }
        }
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(map)) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        File publisherImageFilePath = new File(importFolderPath + "//DATA//icon.png");
        map.put("PIC", Integer.toString(CompanyLogoAnalyzer.getLogoNumber()));
        if(addFeature){
            ModManager.publisherMod.getEditor().addMod(map, publisherImageFilePath.getPath());
            doOtherImportThings(importFolderPath, map.get("NAME EN"));
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("NAME EN"));
        }
        return "true";
    }

    @Override
    public void doOtherImportThings(String importFolderPath, String name) {

    }

    @Override
    public void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) throws IOException {
        File fileExportedPublisherIcon = new File(exportFolderDataPath + "//icon.png");
        if(!fileExportedPublisherIcon.exists()){
            new File(exportFolderDataPath).mkdirs();
        }
        File fileGenreIconToExport = new File(Utils.getMGT2CompanyLogosPath() + singleContentMap.get("PIC") + ".png");
        Files.copy(Paths.get(fileGenreIconToExport.getPath()),Paths.get(fileExportedPublisherIcon.getPath()));
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
        bw.write("[DEVELOPER]" + map.get("DEVELOPER") + System.getProperty("line.separator"));
        bw.write("[PUBLISHER]" + map.get("PUBLISHER") + System.getProperty("line.separator"));
        bw.write("[MARKET]" + map.get("MARKET") + System.getProperty("line.separator"));
        bw.write("[SHARE]" + map.get("SHARE") + System.getProperty("line.separator"));
        bw.write("[GENRE]" + ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(map.get("GENRE"))) + System.getProperty("line.separator"));
    }

    @Override
    public Importer getImporter() {
        return null;
    }

    @Override
    public String getOptionPaneMessage(Map<String, String> map) {
        return I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.date") + ": " + map.get("DATE") + "\n" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer") + ": " + map.get("DEVELOPER") + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher") + ": " + map.get("PUBLISHER") + "\n" +
                I18n.INSTANCE.get("commonText.marketShare") + ": " + map.get("MARKET") + "\n" +
                I18n.INSTANCE.get("commonText.share") + ": " + map.get("SHARE") + "\n" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + ModManager.genreMod.getAnalyzer().getContentNameById(Integer.parseInt(map.get("GENRE")));
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.publisher");
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.publisherMod.getAnalyzer();
    }

    @Override
    public String getExportFolder() {
        return "//Publisher//";
    }

    @Override
    public String getFileName() {
        return ModManager.publisherMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return "publisher";
    }

    @Override
    public String getTypeCaps() {
        return "PUBLISHER";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.publisherMod.getCompatibleModToolVersions();
    }
}
