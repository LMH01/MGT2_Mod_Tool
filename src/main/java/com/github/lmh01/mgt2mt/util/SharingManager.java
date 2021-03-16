package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.interfaces.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import jdk.nashorn.internal.scripts.JO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);
    public static final String[] GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.0", "1.7.1", "1.8.0"};
    public static final String[] PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.6.0", "1.7.0", "1.7.1", "1.8.0"};
    public static final String[] THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen
    public static final String[] ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen
    public static final String[] GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {"1.7.1", "1.8.0"};//TODO Vor release, wenn tool version geändert 1.7.1 raus nehmen


    /**
     * Opens a gui where the user can select a folder from which the files should be imported. Only supports one type import.
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     */
    public static boolean importThings(String fileName, String importName, ReturnValue importFunction, String[] compatibleModToolVersions){
        return importThings(fileName, importName, importFunction, compatibleModToolVersions, true, null);
    }

    /**
     * Uses the import function to import the content of the import folder.
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFolder The import folder where the files are stored.
     */

    public static boolean importThings(String importName, ReturnValue importFunction, String[] compatibleModToolVersions, File importFolder){
        return importThings(null, importName, importFunction, compatibleModToolVersions, false, importFolder);
    }

    /**
     * Opens a gui where the user can select a folder from which the files should be imported. Only supports one type import.
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param askForImportFolder  If true the user will be asked to select a folder. If false the import folder will be used that passed as argument.
     * @param importFolder The import folder where the files are stored. The folder is used when ask for import folder is false.
     */
    private static boolean importThings(String fileName, String importName, ReturnValue importFunction, String[] compatibleModToolVersions, boolean askForImportFolder, File importFolder){
        try {
            if(askForImportFolder){
                ArrayList<String> importFolders = getImportFolderPath(fileName);
                try{
                    for(String folder : importFolders){
                        analyzeReturnValue(importName, importFunction.getReturnValue(folder), compatibleModToolVersions);
                    }
                }catch(NullPointerException ignored){

                }
            }else{
                analyzeReturnValue(importName, importFunction.getReturnValue(importFolder.getPath()), compatibleModToolVersions);
            }
            return true;
        }catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to import " + importName + ":\nThe file is corrupted or not compatible with the current Mod Manager Version", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Loads the contents of import file into a map and imports feature with this map
     * @param importFile This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. Engine feature, Gameplay feature
     * @param importFolderPath The folder where the importFile is located.
     * @param existingFeatureList The list where the existing features are listed. Eg. {@link AnalyzeExistingGameplayFeatures#gameplayFeatures}
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFunction The function that edits the file
     * @param freeId The function that returns the free id
     * @param changelogId The id that should be used when the changelog file is being edited
     * @param summary The summary function that should be used
     * @param showMessages True when the messages should be shown. False if not.
     * @return
     */
    public static String importGeneral(String importFile, String importName, String importFolderPath, List<Map<String, String>> existingFeatureList, String[] compatibleModToolVersions, Importer importFunction, FreeId freeId, int changelogId, Summary summary, boolean showMessages) throws IOException{
        File fileToImport = new File(importFolderPath + "\\" + importFile);
        Map<String, String> map = Utils.parseDataFile(fileToImport).get(0);
        map.put("ID", Integer.toString(freeId.getFreeId()));
        boolean CanBeImported = false;
        for(String string : compatibleModToolVersions){
            if(string.equals(map.get("MGT2MT VERSION"))){
                CanBeImported = true;
            }
        }
        if(!CanBeImported){
            return importName + " [" + map.get("NAME EN") + "] could not be imported:\n" + importName + " is not with the current mod tool version compatible\n" + importName + " was exported in version: " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingFeatures : existingFeatureList){
            for(Map.Entry<String, String> entry : existingFeatures.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    LOGGER.info(importName + " already exists - " + importName + " name is already taken");
                    return "false";
                }
            }
        }
        boolean addFeature;
        if(showMessages){
            addFeature = summary.showSummary(map);
        }else{
            addFeature = true;
        }
        if(addFeature){
            importFunction.importer(map);
            if(showMessages){
                JOptionPane.showMessageDialog(null, importName + " [" + map.get("NAME EN") + "] has been added successfully");
            }
            ChangeLog.addLogEntry(changelogId, map.get("NAME EN"));
            WindowMain.checkActionAvailability();
        }
        return "true";
    }

    /**
     * Prompts the user to select folders
     * @return Returns the folders as files.
     */
    public static ArrayList<File> getFoldersAsFile(){
        ArrayList<String> arrayList = getImportFolderPath("import", true);
        ArrayList<File> files = new ArrayList<>();
        try{
            for(String string : arrayList){
                files.add(new File(string));
            }
        }catch(NullPointerException ignored){
            return null;
        }
        return files;
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @return Returns the selected folders, ready for import
     */
    private static ArrayList<String> getImportFolderPath(String fileName){
        return getImportFolderPath(fileName, false);
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param skipCheckForContent True when the folder should not be checked if it contains the fileName. Useful when the return string should contain all folders.
     * @return Returns the selected folders, ready for import.
     */
    private static ArrayList<String> getImportFolderPath(String fileName, boolean skipCheckForContent){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            if(skipCheckForContent){
                fileChooser.setDialogTitle("Choose the folder(s) where the files are located that should be imported");
            }else{
                fileChooser.setDialogTitle("Choose the folder(s) where the " + fileName + " file is located.");
            }
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();
                ArrayList<String> importFolders = new ArrayList<>();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    String importFolder = files[i].getPath();
                    if(skipCheckForContent){
                        importFolders.add(importFolder);
                    }else{
                        if(Utils.doesFolderContainFile(importFolder, fileName)){
                            File fileGenreToImport = new File(importFolder + "//" + fileName);
                            BufferedReader br = new BufferedReader(new FileReader(fileGenreToImport));
                            String currentLine = br.readLine();
                            br.close();
                            if(currentLine.contains("[MGT2MT VERSION]")){
                                LOGGER.info("File seams to be valid.");
                                importFolders.add(importFolder);
                            }else{
                                JOptionPane.showMessageDialog(null, "The selected folder does not contain a valid " + fileName + " file.\nPlease select the correct folder.");
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "The selected folder does not contain the " + fileName + " file.\nPlease select the correct folder.");
                        }
                    }
                }
                return importFolders;
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Use this function to evaluate the return value from the respective import function.
     * @param importName The name that is written in some JOptionPanes. Eg. genre, publisher, theme
     * @param returnValue The return value from the import function
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     */
    private static void analyzeReturnValue(String importName, String returnValue, String[] compatibleModToolVersions){
        try{
            if(returnValue.equals("false")){
                JOptionPane.showMessageDialog(null, "The selected " + importName + " already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }else{
                if(!returnValue.equals("true")){
                    StringBuilder supportedModToolVersions = new StringBuilder();
                    for(String string : compatibleModToolVersions){
                        supportedModToolVersions.append("[").append(string).append("]");
                    }
                    JOptionPane.showMessageDialog(null, returnValue + "\nSupported versions: " + supportedModToolVersions.toString(), "Action unavailable", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to import " + importName + ":\nThe file is corrupted or not compatible with the current Mod Manager Version", "Action unavailable", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     */
    public static void importAll() {
        ArrayList<File> directories = getFoldersAsFile();
        ArrayList<File> engineFeatures = new ArrayList<>();
        ArrayList<File> gameplayFeatures = new ArrayList<>();
        ArrayList<File> genres = new ArrayList<>();
        ArrayList<File> publishers = new ArrayList<>();
        ArrayList<File> themes = new ArrayList<>();
        if(directories != null){
            try {
                for(File file : directories){
                    Path start = Paths.get(file.getPath());
                    try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
                        List<String> collect = stream
                                .map(String::valueOf)
                                .sorted()
                                .collect(Collectors.toList());

                        collect.forEach((string) -> {
                            if(string.contains("engineFeature.txt")){
                                LOGGER.info("engineFeature: " + string);
                                engineFeatures.add(new File(string));
                            }else if(string.contains("gameplayFeature.txt")){
                                LOGGER.info("gameplayFeature: " + string);
                                gameplayFeatures.add(new File(string));
                            }else if(string.contains("genre.txt")){
                                genres.add(new File(string));
                            }else if(string.contains("publisher.txt")){
                                publishers.add(new File(string));
                            }else if(string.contains("theme.txt")){
                                themes.add(new File(string));
                            }
                        });
                    }
                }
            } catch (IOException e)  {
                e.printStackTrace();
            }catch (NullPointerException ignored){

            }
            StringBuilder message = new StringBuilder();
            if(!engineFeatures.isEmpty() || !gameplayFeatures.isEmpty() || !genres.isEmpty() || !publishers.isEmpty() || !themes.isEmpty()) {
                message.append("<html>The following objects to import have been found:").append("<br>");
                if(!engineFeatures.isEmpty()){
                    message.append("Engine features: ").append(engineFeatures.size()).append("<br>");
                }
                if(!gameplayFeatures.isEmpty()){
                    message.append("Gameplay features: ").append(gameplayFeatures.size()).append("<br>");
                }
                if(!genres.isEmpty()){
                    message.append("Genres: ").append(genres.size()).append("<br>");
                }
                if(!publishers.isEmpty()){
                    message.append("Publishers: ").append(publishers.size()).append("<br>");
                }
                if(!themes.isEmpty()){
                    message.append("Themes: ").append(themes.size()).append("<br>");
                }
                message.append("<br>").append("Do you want to start the import process?");
                JLabel labelMessage = new JLabel(message.toString());
                JCheckBox checkBoxDisableImportPopups = new JCheckBox("Disable popups");
                checkBoxDisableImportPopups.setToolTipText("<html>Check to disable confirm messages that something can be imported.<br>If checked only error messages are shown");
                Object[] params = {labelMessage, checkBoxDisableImportPopups};

                if(JOptionPane.showConfirmDialog(null, params, "Import ready", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    boolean errorOccurred = false;
                    for(File file : engineFeatures){
                        if(!importThings("engine feature", (string) -> SharingHandler.importEngineFeature(string, !checkBoxDisableImportPopups.isSelected()), SharingManager.ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, file.getParentFile())){
                            errorOccurred = true;
                        }
                    }
                    for(File file : gameplayFeatures){
                        if(!importThings("gameplay feature",(string) -> SharingHandler.importGameplayFeature(string, !checkBoxDisableImportPopups.isSelected()), SharingManager.GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, file.getParentFile())){
                            errorOccurred = true;
                        }
                    }
                    for(File file : genres){
                        if(!importThings("genre",(string) -> SharingHandler.importGenre(string, !checkBoxDisableImportPopups.isSelected()), SharingManager.GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, file.getParentFile())){
                            errorOccurred = true;
                        }
                    }
                    for(File file : publishers){
                        if(!importThings("publisher", (string) -> SharingHandler.importPublisher(string, !checkBoxDisableImportPopups.isSelected()), SharingManager.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, file.getParentFile())){
                            errorOccurred = true;
                        }
                    }
                    for(File file : themes){
                        if(!importThings("theme", (string) -> SharingHandler.importTheme(string, !checkBoxDisableImportPopups.isSelected()), SharingManager.THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, file.getParentFile())){
                            errorOccurred = true;
                        }
                    }
                    if(errorOccurred){
                        JOptionPane.showMessageDialog(null, "Error while importing:\nSome features might not be properly imported.\nSee console for further information!", "Error while importing", JOptionPane.ERROR_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Import complete:\nAll features have been imported.", "Import complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "The folder(s) and it's subfolders do not contain things that could be imported.", "Unable to import", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        WindowMain.checkActionAvailability();
    }

    /**
     * Exports all available things when the user accepts.
     */
    public static void exportAll(){
        String[] customEngineFeatures = AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString();
        String[] customGameplayFeatures = AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString();
        String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
        String[] customPublishers = AnalyzeExistingPublishers.getCustomPublisherString();
        String[] customThemes = AnalyzeExistingThemes.getCustomThemesByAlphabet();
        final int ENTRIES_PER_LINE = 10;
        StringBuilder exportList = new StringBuilder();
        exportList.append(getExportListPart(customEngineFeatures, ENTRIES_PER_LINE, "Engine features"));
        exportList.append(getExportListPart(customGameplayFeatures, ENTRIES_PER_LINE, "Gameplay features"));
        exportList.append(getExportListPart(customGenres, ENTRIES_PER_LINE, "Genres"));
        exportList.append(getExportListPart(customPublishers, ENTRIES_PER_LINE, "Publishers"));
        exportList.append(getExportListPart(customThemes, ENTRIES_PER_LINE, "Themes"));
        if(JOptionPane.showConfirmDialog(null, "The following entries will be exported:\n\n" + exportList.toString(), "Export", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
            StringBuilder failedExports = new StringBuilder();
            try{
                failedExports.append(getExportFailed(SharingHandler::exportEngineFeature, customEngineFeatures, "Engine features"));
                failedExports.append(getExportFailed(SharingHandler::exportGameplayFeature, customGameplayFeatures, "Gameplay features"));
                failedExports.append(getExportFailed(SharingHandler::exportGenre, customGenres, "Genres"));
                failedExports.append(getExportFailed(SharingHandler::exportPublisher, customPublishers, "Publishers"));
                failedExports.append(getExportFailed(SharingHandler::exportTheme, customThemes, "Themes"));

                if(failedExports.toString().isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, "All entries have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, "The following entries have not been exported because they where already exported:\n\n" + failedExports.toString() + "\n\nDo you want to open the export folder?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                    }
                }
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Error while exporting:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * @param strings The array containing the entries
     * @param ENTRIES_PER_LINE How many entries should be displayed per line
     * @param exportName The name that should be written when a export item is found. Eg. Genre, Theme
     * @return Returns a string containing the entries from the array
     */
    private static String getExportListPart(String[] strings, final int ENTRIES_PER_LINE, String exportName){
        StringBuilder stringBuilder = new StringBuilder();
        if(strings.length > 0){
            stringBuilder.append(exportName + ": ");
        }
        boolean firstCustomGenre = true;
        int currentLineNumber = 1;
        for(String string : strings){
            if(firstCustomGenre){
                firstCustomGenre = false;
            }else{
                stringBuilder.append(", ");
            }
            if(currentLineNumber == ENTRIES_PER_LINE){
                stringBuilder.append(System.getProperty("line.separator"));
            }
            stringBuilder.append(string);
            currentLineNumber++;
        }
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }

    /**
     * Uses the input exporter to export a list of entries. This function may only be called by {@link SharingManager#exportAll()}.
     * @param exporter
     * @param strings The array containing the entries
     * @param exportName The name that should be written when a error occurs. Eg. Genre, Theme
     * @return Returns a string of errors if something failed to export
     */
    private static String getExportFailed(Exporter exporter, String[] strings, String exportName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstExportFailed = true;
        boolean exportFailed = false;
        for(String string : strings){
            if(!exporter.export(string)){
                if(firstExportFailed){
                    stringBuilder.append(exportName + ": ");
                }else{
                    stringBuilder.append(", ");
                }
                stringBuilder.append(string);
                firstExportFailed = false;
                exportFailed = true;
            }
        }
        if(exportFailed){
            stringBuilder.append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }
}
