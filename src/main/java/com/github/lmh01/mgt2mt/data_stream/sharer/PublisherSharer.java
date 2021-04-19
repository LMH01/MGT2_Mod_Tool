package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
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

public class PublisherSharer extends AbstractAdvancedSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherSharer.class);

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws IOException {
        getAnalyzer().analyzeFile();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + getType());
        File fileToImport = new File(importFolderPath + "\\" + getFileName());
        HashMap<String, String> map = new HashMap<>();
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileToImport);
        map.put("ID", Integer.toString(getAnalyzer().getFreeId()));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE")){
                int genreID = AnalyzeManager.genreAnalyzer.getContentIdByName(entry.getValue());
                if(genreID == -1){
                    int randomGenreID = Utils.getRandomNumber(0, AnalyzeManager.genreAnalyzer.getFileContent().size()-1);
                    LOGGER.info("Genre list size: " + AnalyzeManager.genreAnalyzer.getFileContent().size());
                    map.put("GENRE", Integer.toString(randomGenreID));
                }else{
                    map.put("GENRE", Integer.toString(AnalyzeManager.genreAnalyzer.getContentIdByName(entry.getValue())));
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
            EditorManager.publisherEditor.addMod(map, publisherImageFilePath.getPath());
            doOtherImportThings(importFolderPath);
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + ": " + map.get("NAME EN"));
        }
        return "true";
    }

    @Override
    void doOtherImportThings(String importFolderPath) {

    }

    @Override
    void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) throws IOException {
        File fileExportedPublisherIcon = new File(exportFolderDataPath + "//icon.png");
        File fileGenreIconToExport = new File(Utils.getMGT2CompanyLogosPath() + singleContentMap.get("PIC") + ".png");
        Files.copy(Paths.get(fileGenreIconToExport.getPath()),Paths.get(fileExportedPublisherIcon.getPath()));
    }

    @Override
    void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        TranslationManager.printLanguages(bw, map);
        bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
        bw.write("[DEVELOPER]" + map.get("DEVELOPER") + System.getProperty("line.separator"));
        bw.write("[PUBLISHER]" + map.get("PUBLISHER") + System.getProperty("line.separator"));
        bw.write("[MARKET]" + map.get("MARKET") + System.getProperty("line.separator"));
        bw.write("[SHARE]" + map.get("SHARE") + System.getProperty("line.separator"));
        bw.write("[GENRE]" + AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(map.get("GENRE"))) + System.getProperty("line.separator"));
    }

    @Override
    Importer getImporter() {
        return null;
    }

    @Override
    String getOptionPaneMessage(Map<String, String> map) {//TODO Übersetzung hinzufügen
        return "Add this publisher?\n" +
                "\nName: " + map.get("NAME EN") +
                "\nDate: " + map.get("DATE") +
                "\nPic: See top left" +
                "\nDeveloper: " + map.get("DEVELOPER") +
                "\nPublisher: " + map.get("PUBLISHER") +
                "\nMarketShare: " + map.get("MARKET") +
                "\nShare: " + map.get("SHARE") +
                "\nGenre: " + AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(map.get("GENRE")));
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
        return AnalyzeManager.publisherAnalyzer;
    }

    @Override
    public String getExportFolder() {
        return "//Publisher//";
    }

    @Override
    public String getFileName() {
        return "publisher.txt";
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
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.6.0", "1.7.0", "1.7.1", "1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }
}
