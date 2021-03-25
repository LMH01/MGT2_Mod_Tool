package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.interfaces.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);
    public static final String[] GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3"};
    public static final String[] PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {MadGamesTycoon2ModTool.VERSION,"1.6.0", "1.7.0", "1.7.1", "1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3"};
    public static final String[] THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3"};
    public static final String[] ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3"};
    public static final String[] GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS = {MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3"};
    public static final String[] LICENCE_IMPORT_COMPATIBLE_MOD_VERSIONS = {MadGamesTycoon2ModTool.VERSION, "1.10.0"};

    /**
     * Uses the import function to import the content of the import folder.
     * @param importName The name that is written in some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFolder The import folder where the files are stored.
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */

    public static boolean importThings(String importName, ReturnValue importFunction, String[] compatibleModToolVersions, File importFolder, boolean showAlreadyExistMessage){
        return !importThings(null, importName, importFunction, compatibleModToolVersions, false, importFolder, showAlreadyExistMessage);
    }

    /**
     * Opens a gui where the user can select a folder from which the files should be imported. Only supports one type import.
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param askForImportFolder  If true the user will be asked to select a folder. If false the import folder will be used that passed as argument.
     * @param importFolder The import folder where the files are stored. The folder is used when ask for import folder is false.
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */
    private static boolean importThings(String fileName, String importName, ReturnValue importFunction, String[] compatibleModToolVersions, boolean askForImportFolder, File importFolder, boolean showAlreadyExistMessage){
        try {
            if(askForImportFolder){
                ArrayList<String> importFolders = getImportFolderPath(fileName);
                try{
                    for(String folder : importFolders){
                        analyzeReturnValue(importName, importFunction.getReturnValue(folder), compatibleModToolVersions, showAlreadyExistMessage);
                    }
                }catch(NullPointerException ignored){

                }
            }else{
                analyzeReturnValue(importName, importFunction.getReturnValue(importFolder.getPath()), compatibleModToolVersions, showAlreadyExistMessage);
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
     * @return Returns true when the import was successful. Returns false if not. Returns a string containing version numbers when import file is not compatible with current mgt2mt version.
     */
    public static String importGeneral(String importFile, String importName, String importFolderPath, List<Map<String, String>> existingFeatureList, String[] compatibleModToolVersions, Importer importFunction, FreeId freeId, int changelogId, Summary summary, boolean showMessages) throws IOException{
        File fileToImport = new File(importFolderPath + "\\" + importFile);
        Map<String, String> map = DataStreamHelper.parseDataFile(fileToImport).get(0);
        map.put("ID", Integer.toString(freeId.getFreeId()));
        boolean CanBeImported = false;
        for(String string : compatibleModToolVersions){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
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
            FileFilter fileFilter = new FileFilter() {//File filter to only show .zip files.
                @Override
                public boolean accept(File f) {
                    if(f.getName().contains(".zip")){
                        return true;
                    }
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".zip files";
                }
            };
            fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(fileFilter);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();
                ArrayList<String> importFolders = new ArrayList<>();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    String importFolder = files[i].getPath();
                    if(skipCheckForContent){
                        importFolders.add(importFolder);
                    }else{
                        if(DataStreamHelper.doesFolderContainFile(importFolder, fileName)){
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
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */
    private static void analyzeReturnValue(String importName, String returnValue, String[] compatibleModToolVersions, boolean showAlreadyExistMessage){
        try{
            if(returnValue.equals("false")){
                if(showAlreadyExistMessage){
                    JOptionPane.showMessageDialog(null, "The selected " + importName + " already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                }
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
        ArrayList<File> licences = new ArrayList<>();
        ArrayList<String> engineFeatureNames = new ArrayList<>();
        ArrayList<String> gameplayFeatureNames = new ArrayList<>();
        ArrayList<String> genreNames = new ArrayList<>();
        ArrayList<String> publisherNames = new ArrayList<>();
        ArrayList<String> themeNames = new ArrayList<>();
        ArrayList<String> licenceNames = new ArrayList<>();
        AtomicBoolean someThingsNotCompatible = new AtomicBoolean(false);
        AtomicInteger currentZipArchiveNumber = new AtomicInteger();
        if(directories != null){
            try {
                for(int i=0; i<directories.size(); i++){
                    File file = directories.get(i);
                    Path start = Paths.get(file.getPath());
                    try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
                        List<String> collect = stream
                                .map(String::valueOf)
                                .sorted()
                                .collect(Collectors.toList());

                        collect.forEach((string) -> {
                            if(string.contains("engineFeature.txt")){
                                addIfCompatible(string, engineFeatures, engineFeatureNames, ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, someThingsNotCompatible);
                            }else if(string.contains("gameplayFeature.txt")){
                                addIfCompatible(string, gameplayFeatures, gameplayFeatureNames, GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, someThingsNotCompatible);
                            }else if(string.contains("genre.txt")){
                                addIfCompatible(string, genres, genreNames, GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, someThingsNotCompatible);
                            }else if(string.contains("publisher.txt")){
                                addIfCompatible(string, publishers, publisherNames, PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, someThingsNotCompatible);
                            }else if(string.contains("theme.txt")){
                                addIfCompatible(string, themes, themeNames, THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, someThingsNotCompatible);
                            }else if(string.contains("licence.txt")){
                                addIfCompatible(string, licences, licenceNames, LICENCE_IMPORT_COMPATIBLE_MOD_VERSIONS, someThingsNotCompatible);
                            }else if(string.endsWith(".zip")){
                                if(JOptionPane.showConfirmDialog(null, "A .zip archive has been found:\n\n" + string + "\n\nWould you like to extract it and search it for mods?", "Zip archive found", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                    try {
                                        File extractedFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//" + currentZipArchiveNumber);
                                        DataStreamHelper.unzip(string, extractedFolder);
                                        currentZipArchiveNumber.getAndIncrement();
                                        directories.add(extractedFolder);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        if(JOptionPane.showConfirmDialog(null, "Error while extracting zip archive:\n\n" + string + "\n\nException: " + e.getMessage() + "\n\nWould you like to continue anyway", "Error while extracting", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
                                            return;
                                        }
                                    }
                                }
                            }
                            if(Settings.enableDebugLogging){
                                LOGGER.info("current file: " + string);
                            }
                        });
                    }
                }
            } catch (IOException e)  {
                e.printStackTrace();
            }catch (NullPointerException ignored){

            }
            JPanel panelEngineFeatures = new JPanel();
            JPanel panelGameplayFeatures = new JPanel();
            JPanel panelGenres = new JPanel();
            JPanel panelPublishers = new JPanel();
            JPanel panelThemes = new JPanel();
            JPanel panelLicences = new JPanel();
            AtomicReference<ArrayList<Integer>> selectedEntriesEngineFeatures = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<Integer>> selectedEntriesGameplayFeatures = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<Integer>> selectedEntriesGenres = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<Integer>> selectedEntriesPublishers = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<Integer>> selectedEntriesThemes = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<Integer>> selectedEntriesLicences = new AtomicReference<>(new ArrayList<>());
            AtomicBoolean disableEngineFeatureImport = new AtomicBoolean(true);
            AtomicBoolean disableGameplayFeatureImport = new AtomicBoolean(true);
            AtomicBoolean disableGenreImport = new AtomicBoolean(true);
            AtomicBoolean disablePublisherImport = new AtomicBoolean(true);
            AtomicBoolean disableThemeImport = new AtomicBoolean(true);
            AtomicBoolean disableLicenceImport = new AtomicBoolean(true);
            if(!engineFeatures.isEmpty() || !gameplayFeatures.isEmpty() || !genres.isEmpty() || !publishers.isEmpty() || !themes.isEmpty() || !licences.isEmpty()) {
                if(!engineFeatures.isEmpty()){
                    setFeatureAvailableGuiComponents("Engine features:",engineFeatures, panelEngineFeatures, selectedEntriesEngineFeatures, disableEngineFeatureImport);
                }
                if(!gameplayFeatures.isEmpty()){
                    setFeatureAvailableGuiComponents("Gameplay features:",gameplayFeatures, panelGameplayFeatures, selectedEntriesGameplayFeatures, disableGameplayFeatureImport);
                }
                if(!genres.isEmpty()){
                    setFeatureAvailableGuiComponents("Genres:", genres, panelGenres, selectedEntriesGenres, disableGenreImport);
                }
                if(!publishers.isEmpty()){
                    setFeatureAvailableGuiComponents("Publishers:", publishers, panelPublishers, selectedEntriesPublishers, disablePublisherImport);
                }
                if(!themes.isEmpty()){
                    setFeatureAvailableGuiComponents("Themes:", themes, panelThemes, selectedEntriesThemes, disableThemeImport);
                }
                if(!licences.isEmpty()){
                    setFeatureAvailableGuiComponents("Licences:", licences, panelLicences, selectedEntriesLicences, disableLicenceImport);
                }
                JLabel labelStart = new JLabel("The following objects can be imported:");
                JLabel labelEnd = new JLabel("<html>The numbers indicate how many entries,<br>out of the available entries will be imported.<br><br>Tip:<br>If you wish not to import everything,<br>click the button(s) to select what entries should be imported.<br><br>Do you want to start the import process?");
                JCheckBox checkBoxDisableImportPopups = new JCheckBox("Disable popups");
                checkBoxDisableImportPopups.setToolTipText("<html>Check to disable confirm messages that something can be imported");
                JCheckBox checkBoxDisableAlreadyExistPopups = new JCheckBox("Disable already exists popups");
                checkBoxDisableAlreadyExistPopups.setToolTipText("<html>Check to disable popups that something already exists");
                checkBoxDisableAlreadyExistPopups.setSelected(true);
                Object[] params = {labelStart, panelEngineFeatures, panelGameplayFeatures, panelGenres, panelPublishers, panelThemes, panelLicences, labelEnd, checkBoxDisableImportPopups, checkBoxDisableAlreadyExistPopups};

                if(JOptionPane.showConfirmDialog(null, params, "Import ready", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    boolean showMessageDialogs = checkBoxDisableImportPopups.isSelected();
                    boolean showAlreadyExistPopups = checkBoxDisableAlreadyExistPopups.isSelected();
                    boolean errorOccurred = false;
                    if(!importAllFiles(engineFeatures, selectedEntriesEngineFeatures.get(), disableEngineFeatureImport.get(), "engine feature", (string) -> SharingHandler.importEngineFeature(string, !showMessageDialogs), SharingManager.ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occurred wile importing engine features");
                        errorOccurred = true;
                    }
                    if(!importAllFiles(gameplayFeatures, selectedEntriesGameplayFeatures.get(), disableGameplayFeatureImport.get(), "gameplay feature", (string) -> SharingHandler.importGameplayFeature(string, !showMessageDialogs), SharingManager.GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occurred wile importing gameplay features");
                        errorOccurred = true;
                    }
                    if(!importAllFiles(genres, selectedEntriesGenres.get(), disableGenreImport.get(), "genre", (string) -> SharingHandler.importGenre(string, !showMessageDialogs), SharingManager.GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occurred wile importing genres");
                        errorOccurred = true;
                    }
                    if(!importAllFiles(publishers, selectedEntriesPublishers.get(), disablePublisherImport.get(), "publisher", (string) -> SharingHandler.importPublisher(string, !showMessageDialogs), SharingManager.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occurred wile importing publishers");
                        errorOccurred = true;
                    }
                    if(!importAllFiles(themes, selectedEntriesThemes.get(), disableThemeImport.get(), "theme", (string) -> SharingHandler.importTheme(string, !showMessageDialogs), SharingManager.THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occurred wile importing themes");
                        errorOccurred = true;
                    }
                    if(!importAllFiles(licences, selectedEntriesLicences.get(), disableLicenceImport.get(), "licence", (string) -> SharingHandler.importLicence(string, !showMessageDialogs), SharingManager.LICENCE_IMPORT_COMPATIBLE_MOD_VERSIONS, !showAlreadyExistPopups)){
                        LOGGER.info("Error occured wile importing licences");
                        errorOccurred = true;
                    }
                    if(errorOccurred){
                        JOptionPane.showMessageDialog(null, "Error while importing:\nSome features might not be properly imported.\nSee console for further information!", "Error while importing", JOptionPane.ERROR_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Import complete:\nAll features (that did not already exist) have been imported.", "Import complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }else{
                if(someThingsNotCompatible.get()){
                    JOptionPane.showMessageDialog(null, "The folder(s) and it's subfolders do not contain things that could be imported.\nSome mods where found that are not compatible with the current mod tool version", "Unable to import", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "The folder(s) and it's subfolders do not contain things that could be imported.", "Unable to import", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        //Delete the temp .zip extractions
        File tempFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//");
        DataStreamHelper.deleteDirectory(tempFolder);
        WindowMain.checkActionAvailability();
    }

    /**
     * Adds gui components to be displayed in the summary. May only be called by {@link SharingManager#importAll()}
     * @param labelText The label text
     * @param files The array list containing the feature specific files
     * @param panel The panel where the components should be added
     * @param selectedEntries A atomic reference where the return values should be saved
     */
    private static void setFeatureAvailableGuiComponents(String labelText, ArrayList<File> files, JPanel panel, AtomicReference<ArrayList<Integer>> selectedEntries, AtomicBoolean disableImport){
        JLabel label = new JLabel(labelText);
        JButton button = new JButton(files.size() + "/" + files.size());
        disableImport.set(false);
        button.addActionListener(actionEvent -> {
            ArrayList<Integer> arrayList = getSelectedEntries(files);
            selectedEntries.set(arrayList);
            if(arrayList.isEmpty()){
                LOGGER.info("Import disabled for: " + labelText.replaceAll(":", ""));
                disableImport.set(true);
            }else{
                LOGGER.info("Import enabled for: " + labelText.replaceAll(":", ""));
                disableImport.set(false);
            }
            button.setText(selectedEntries.get().size() + "/" + files.size());
        });
        panel.add(label);
        panel.add(button);
    }

    /**
     * Opens a gui where the user can select entries.
     * @param files The files that should be scanned for the name that is displayed in the guis
     * @return Returns a array list containing numbers of selected entries
     */
    private static ArrayList<Integer> getSelectedEntries(ArrayList<File> files){
        ArrayList<String> engineFeatureNames = new ArrayList<>();
        for(File file : files){
            try {
                engineFeatureNames.add(DataStreamHelper.getNameFromFile(file, "UTF_8BOM"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Utils.getSelectedEntries("Select what should be imported:", "Import", Utils.convertArrayListToArray(engineFeatureNames), Utils.convertArrayListToArray(engineFeatureNames),false);
    }

    /**
     * Imports all files that are listed in the files array
     * @param files The array containing the import files
     * @param selectedEntryNumbers If not empty only the files numbers listed in this array are imported
     * @param importNothing If true nothing will be imported.
     * @param importName The name that is written in some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param showAlreadyExistPopups When true a message is displayed that the choose import does already exist
     * @return Returns true when the import was successful. Returns false when something went wrong
     */
    public static boolean importAllFiles(ArrayList<File> files, ArrayList<Integer> selectedEntryNumbers, boolean importNothing, String importName, ReturnValue importFunction, String[] compatibleModToolVersions, boolean showAlreadyExistPopups){
        int currentFile = 0;
        if(!importNothing){
            boolean failed = false;
            for(File file : files){
                if(selectedEntryNumbers.size() == 0){
                    if(!importThings(importName, importFunction, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
                        failed = true;
                    }
                }else{
                    if(selectedEntryNumbers.contains(currentFile)){
                        if(!importThings(importName, importFunction, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
                            failed = true;
                        }
                    }
                }
                currentFile++;
            }
            return failed;
        }else{
            return true;
        }
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
        String[] customLicences = AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet();
        StringBuilder exportList = new StringBuilder();
        exportList.append(getExportListPart(customEngineFeatures, "Engine features"));
        exportList.append(getExportListPart(customGameplayFeatures, "Gameplay features"));
        exportList.append(getExportListPart(customGenres, "Genres"));
        exportList.append(getExportListPart(customPublishers, "Publishers"));
        exportList.append(getExportListPart(customThemes, "Themes"));
        exportList.append(getExportListPart(customLicences, "Licences"));
        if(JOptionPane.showConfirmDialog(null, "The following entries will be exported:\n\n" + exportList.toString(), "Export", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
            StringBuilder failedExports = new StringBuilder();
            try{
                failedExports.append(getExportFailed(SharingHandler::exportEngineFeature, customEngineFeatures, "Engine features"));
                failedExports.append(getExportFailed(SharingHandler::exportGameplayFeature, customGameplayFeatures, "Gameplay features"));
                failedExports.append(getExportFailed(SharingHandler::exportGenre, customGenres, "Genres"));
                failedExports.append(getExportFailed(SharingHandler::exportPublisher, customPublishers, "Publishers"));
                failedExports.append(getExportFailed(SharingHandler::exportTheme, customThemes, "Themes"));
                failedExports.append(getExportFailed(SharingHandler::exportLicence, customLicences, "Licences"));
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
     * @param exportName The name that should be written when a export item is found. Eg. Genre, Theme
     * @return Returns a string containing the entries from the array
     */
    private static String getExportListPart(String[] strings, String exportName){
        StringBuilder stringBuilder = new StringBuilder();
        if(strings.length > 0){
            stringBuilder.append(exportName).append(": ");
        }
        boolean firstCustomGenre = true;
        int currentLineNumber = 1;
        for(String string : strings){
            if(firstCustomGenre){
                firstCustomGenre = false;
            }else{
                stringBuilder.append(", ");
            }
            if(currentLineNumber == 10){
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
     * @param exporter The export function that should be used
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
                    stringBuilder.append(exportName).append(": ");
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

    /**
     * Checks the input file for compatibility with this mod tool version using the input compatible versions array
     * @return Returns true when the file is compatible
     */
    private static boolean isImportCompatible(File inputFile, String[] compatibleVersions){
        try {
            Map<Integer, String> map = DataStreamHelper.getContentFromFile(inputFile, "UTF_8BOM");
            for(Map.Entry entry : map.entrySet()){
                LOGGER.info("Current Value: " + entry.getValue());
                for(String string : compatibleVersions){
                    LOGGER.info("Current compatible version: " + string);
                    if(entry.getValue().toString().replace("[MGT2MT VERSION]", "").equals(string) || Settings.disableSafetyFeatures){
                        return true;
                    }
                }
            }
        } catch (IOException e){
            LOGGER.error("Error while checking " + inputFile.getPath() + " for compatibility with this tool version. The file might be corrupted.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks the input file for compatibility with this mod tool version using the input compatible versions array
     * @return Returns the name that is stored in NAME EN in the input file. Returns false when no name has been found
     */
    private static String getImportName(File inputFile){
        try {
            Map<Integer, String> map = DataStreamHelper.getContentFromFile(inputFile, "UTF_8BOM");
            for(Map.Entry entry : map.entrySet()){
                LOGGER.info("Current Value: " + entry.getValue());
                if(entry.getValue().toString().contains("NAME EN")){
                    return entry.getValue().toString().replace("[NAME EN]", "");
                }
            }
        } catch (IOException e){
            LOGGER.error("Error while checking " + inputFile.getPath() + " for compatibility with this tool version. The file might be corrupted.");
            e.printStackTrace();
        }
        return "false";
    }

    /**
     * Uses the string and checks if the content of that file has already been added to the mod array list. If it does already exist the user is asked if the mod should be added anyway.
     */
    private static void addIfCompatible(String string, ArrayList<File> feature, ArrayList<String> names, String[] compatibleModToolVersions, AtomicBoolean someThingsNotCompatible){
        if(isImportCompatible(new File(string), compatibleModToolVersions)){
            String returnValue = getImportName(new File(string));
            if(names.contains(returnValue)){
                if(JOptionPane.showConfirmDialog(null, "This mod has already been found: " + returnValue + "\n\nDo you want to add this mod to the import list anyway?", "Import already on the list", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    feature.add(new File(string));
                }
            }else{
                if(returnValue != "false"){
                    names.add(returnValue);
                }
                feature.add(new File(string));
            }
        }else{
            someThingsNotCompatible.set(true);
        }
    }
}
