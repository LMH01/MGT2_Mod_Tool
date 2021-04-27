package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.interfaces.SimpleImporter;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeSharer extends AbstractSimpleSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureSharer.class);

    @Override
    public boolean exportMod(String name, boolean exportAsRestorePoint) {
        try {
            Map<String, String> map = ThemeFileAnalyzer.getSingleThemeByNameMap(name);
            String exportFolder;
            if(exportAsRestorePoint){
                exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
            }else{
                exportFolder = Utils.getMGT2ModToolExportFolder();
            }
            final String EXPORTED_PUBLISHER_MAIN_FOLDER_PATH = exportFolder + "//" + getExportFolder() + "//" + map.get("NAME EN").replaceAll("[^a-zA-Z0-9]", "");
            File fileExportFolderPath = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH);
            File fileExportedTheme = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//" + getFileName());
            if(fileExportedTheme.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.themeExportFailed.alreadyExported") + " " + name);
                return false;
            }else{
                fileExportFolderPath.mkdirs();
            }
            fileExportedTheme.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedTheme), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
            bw.write("[" + getTypeCaps() + " START]" + System.getProperty("line.separator"));
            bw.write("[VIOLENCE LEVEL]" + map.get("VIOLENCE LEVEL") + System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, map);
            bw.write("[GENRE COMB]" + ModManager.genreMod.getAnalyzer().getGenreNames(map.get("GENRE COMB")) + System.getProperty("line.separator"));
            bw.write("[" + getTypeCaps() + " END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer." + getMainTranslationKey() + ".exportSuccessful") + " " + name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] - " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage());
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws IOException {
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + I18n.INSTANCE.get("window.main.share.export.theme"));
        ThemeFileAnalyzer.analyzeThemeFiles();
        File fileThemeToImport = new File(importFolderPath + "\\" + getFileName());
        ArrayList<Integer> compatibleGenreIds = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int violenceRating = 0;
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileThemeToImport);
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE COMB")){
                ArrayList<String> compatibleGenreNames = Utils.getEntriesFromString(entry.getValue());
                for(String string : compatibleGenreNames){
                    compatibleGenreIds.add(ModManager.genreMod.getAnalyzer().getContentIdByName(string));
                }
            }else if(entry.getKey().equals("VIOLENCE LEVEL")){
                violenceRating = Integer.parseInt(entry.getValue());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }

        boolean themeCanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION"))){
                themeCanBeImported = true;
            }
        }
        if(!themeCanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + " - " + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION")));
            return I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + "\n" + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION"));
        }
        for(Map.Entry<Integer, String> entry : ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
            if(entry.getValue().equals(map.get("NAME EN"))){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.alreadyExists") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN"));
                LOGGER.info("Theme already exists - The theme name is already taken");
                return "false";
            }
        }
        try {
            if(showMessages){
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme") + "\n\n" + map.get("NAME EN"), I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    ModManager.themeMod.getEditor().addMod(map, compatibleGenreIds, violenceRating);
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + map.get("NAME EN") + " " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
                }
            }else{
                ModManager.themeMod.getEditor().addMod(map, compatibleGenreIds, violenceRating);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingHandler.unableToAddTheme") + ":" + map.get("NAME EN") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + e.getMessage(), I18n.INSTANCE.get("dialog.sharingHandler.unableToAddPublisher"), JOptionPane.ERROR_MESSAGE);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + ": " + map.get("NAME EN"));
        return "true";
    }

    @Override
    SimpleImporter getSimpleImporter() {
        return ModManager.themeMod.getEditor()::addMod;
    }

    /**
     * @deprecated Do not use this method. This method is not implemented
     */
    @Override
    @Deprecated
    String getOptionPaneMessage(String line) {
        return null;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.theme");
    }

    /**
     * @return Returns the themeFileGeAnalyzer
     */
    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.themeMod.getAnalyzerGe();
    }

    @Override
    public String getExportFolder() {
        return "Themes";
    }

    @Override
    public String getFileName() {
        return ModManager.themeMod.getFileName();
    }

    @Override
    public String getMainTranslationKey() {
        return "theme";
    }

    @Override
    public String getTypeCaps() {
        return "THEME";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return ModManager.themeMod.getCompatibleModToolVersions();
    }
}
