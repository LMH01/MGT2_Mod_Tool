package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class SharingHandler {
    //Contains functions with which exports are written to file and imports are read and imported
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingHandler.class);

    /**
     * Exports the theme that stand in the map
     * @param themeNameEn The english name of the theme that should be exported
     * @return Returns true when the theme has been exported successfully. Returns false when the theme has already been exported.
     */
    public static boolean exportTheme(String themeNameEn, boolean exportAsRestorePoint) throws IOException {
        Map<String, String> map = ThemeFileAnalyzer.getSingleThemeByNameMap(themeNameEn);
        String exportFolder;
        if(exportAsRestorePoint){
            exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
        }else{
            exportFolder = Utils.getMGT2ModToolExportFolder();
        }
        final String EXPORTED_PUBLISHER_MAIN_FOLDER_PATH = exportFolder + "//Themes//" + map.get("NAME EN").replaceAll("[^a-zA-Z0-9]", "");
        File fileExportFolderPath = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH);
        File fileExportedTheme = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//theme.txt");
        if(fileExportedTheme.exists()){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.themeExportFailed.alreadyExported") + " " + themeNameEn);
            return false;
        }else{
            fileExportFolderPath.mkdirs();
        }
        fileExportedTheme.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedTheme), StandardCharsets.UTF_8));
        bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.write("[THEME START]" + System.getProperty("line.separator"));
        bw.write("[VIOLENCE LEVEL]" + map.get("VIOLENCE LEVEL") + System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, map);
        bw.write("[GENRE COMB]" + getGenreNames(map.get("GENRE COMB")) + System.getProperty("line.separator"));
        bw.close();
        ChangeLog.addLogEntry(23, map.get("NAME EN"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.themeExportSuccessful") + " " + themeNameEn);
        return true;
    }

    /**
     * Imports the specified theme.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the theme has been imported successfully. Returns "false" when the publisher already exists. Returns mod tool version of import theme when theme is not compatible with current mod tool version.
     */
    public static String importTheme(String importFolderPath, boolean showMessages) throws IOException{
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + I18n.INSTANCE.get("window.main.share.export.theme"));
        ThemeFileAnalyzer.analyzeThemeFiles();
        File fileThemeToImport = new File(importFolderPath + "\\theme.txt");
        ArrayList<Integer> compatibleGenreIds = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int violenceRating = 0;
        List<Map<String, String>> list = DataStreamHelper.parseDataFile(fileThemeToImport);
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE COMB")){
                ArrayList<String> compatibleGenreNames = Utils.getEntriesFromString(entry.getValue());
                for(String string : compatibleGenreNames){
                    compatibleGenreIds.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
                }
            }else if(entry.getKey().equals("VIOLENCE LEVEL")){
                violenceRating = Integer.parseInt(entry.getValue());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }

        boolean themeCanBeImported = false;
        for(String string : SharingManager.THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
            if(string.equals(map.get("MGT2MT VERSION"))){
                themeCanBeImported = true;
            }
        }
        if(!themeCanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + " - " + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION")));
            return I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + "\n" + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION"));
        }
        for(Map.Entry<Integer, String> entry : AnalyzeManager.themeFileEnAnalyzer.getFileContent().entrySet()){
            if(entry.getValue().equals(map.get("NAME EN"))){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.alreadyExists") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN"));
                LOGGER.info("Theme already exists - The theme name is already taken");
                return "false";
            }
        }
        try {
            if(showMessages){
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme") + "\n\n" + map.get("NAME EN"), I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    EditorManager.themeEditor.addMod(map, compatibleGenreIds, violenceRating);
                    ChangeLog.addLogEntry(24, map.get("NAME EN"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + map.get("NAME EN") + " " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
                }
            }else{
                EditorManager.themeEditor.addMod(map, compatibleGenreIds, violenceRating);
                ChangeLog.addLogEntry(24, map.get("NAME EN"));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingHandler.unableToAddTheme") + ":" + map.get("NAME EN") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + e.getMessage(), I18n.INSTANCE.get("dialog.sharingHandler.unableToAddPublisher"), JOptionPane.ERROR_MESSAGE);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + ": " + map.get("NAME EN"));
        return "true";
    }

    /**
     * @param genreNumbersRaw The string containing the genre ids that should be transformed
     * @return Returns a list of genre names
     */
    private static String getGenreNames(String genreNumbersRaw){
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString().replaceAll("[^0-9]", ""));
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(AnalyzeManager.genreAnalyzer.getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentNumber);
                }
            }
            charPosition++;
        }
        return genreNames.toString();
    }
}
