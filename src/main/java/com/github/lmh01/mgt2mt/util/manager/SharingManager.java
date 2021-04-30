package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);

    /**
     * Uses the import function to import the content of the import folder.
     * @param importName The name that is written in some JOptionPanes. Eg. genre, publisher, theme
     * @param importFunction The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFolder The import folder where the files are stored.
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */

    public static boolean importThings(String importName, ReturnValue importFunction, String[] compatibleModToolVersions, File importFolder, AtomicBoolean showAlreadyExistMessage){
        return importThings(null, importName, importFunction, compatibleModToolVersions, false, importFolder, showAlreadyExistMessage);
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
    private static boolean importThings(String fileName, String importName, ReturnValue importFunction, String[] compatibleModToolVersions, boolean askForImportFolder, File importFolder, AtomicBoolean showAlreadyExistMessage){
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
            TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + " " + importName + ":\n" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.secondPart"));
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + " " + importName + ":\n" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Prompts the user to select folders
     * @return Returns the folders as files.
     */
    public static ArrayList<File> getFoldersAsFile(){
        ArrayList<String> arrayList = getImportFolderPath("import", true);
        ArrayList<File> files = new ArrayList<>();
        try{
            for(String string : Objects.requireNonNull(arrayList)){
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
                fileChooser.setDialogTitle(I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.title.version1"));
            }else{
                fileChooser.setDialogTitle(I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.title.version2.firstPart") + " " + fileName + " " + I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.title.version2.secondPart"));
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
                    return I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.filterDescription");
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
                                JOptionPane.showMessageDialog(null,  I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.noValidFile.firstPart.var1")+ " " + fileName + " " + I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.noValidFile.secondPart"));
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.noValidFile.firstPart.var2") + " " + fileName + I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.noValidFile.secondPart"));
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
    private static void analyzeReturnValue(String importName, String returnValue, String[] compatibleModToolVersions, AtomicBoolean showAlreadyExistMessage){
        try{
            if(returnValue.equals("false")){
                if(!showAlreadyExistMessage.get()){
                    JLabel label = new JLabel(I18n.INSTANCE.get("processor.alreadyProcessed.firstPart") + " " + importName + " " + I18n.INSTANCE.get("dialog.sharingManager.analyzeReturnValue.alreadyExists") + ".");
                    JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.guiComponents.checkBox.disableAlreadyExistsPopups.toolTip"));
                    Object[] obj = {label, checkBox};
                    JOptionPane.showMessageDialog(null, obj, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    if(checkBox.isSelected()){
                        showAlreadyExistMessage.set(true);
                    }
                }
            }else{
                if(!returnValue.equals("true")){
                    StringBuilder supportedModToolVersions = new StringBuilder();
                    for(String string : compatibleModToolVersions){
                        supportedModToolVersions.append("[").append(string).append("]");
                    }
                    JOptionPane.showMessageDialog(null, returnValue + "\n" + I18n.INSTANCE.get("dialog.sharingManager.analyzeReturnValue.supportedVersions") + " " + supportedModToolVersions.toString(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2") + " " + importName + ":\n" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     */
    public static void importAll() {
        importAll(false, "");
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     * @param importFromRestorePoint True when this function is called from import point restore. Will change some messages.
     * @param folderPath If not empty, this folder will be used as root folder where the search is started. This will prevent the message to the user that folders should be selected.
     */
    public static void importAll(boolean importFromRestorePoint, String folderPath){
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.start"));
        ArrayList<File> directories;
        if(folderPath.isEmpty()){
            LOGGER.info("Opening window where the user can select the folders that should be searched for imports");
            directories = getFoldersAsFile();
        }else{
            directories = new ArrayList<>();
            directories.add(new File(folderPath));
        }
        Map<AbstractBaseMod, ArrayList<File>> importModFiles = new HashMap<>();
        Map<AbstractBaseMod, ArrayList<String>> importModNames = new HashMap<>();
        for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
            importModFiles.put(advancedMod, new ArrayList<>());
            importModNames.put(advancedMod, new ArrayList<>());
        }
        for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
            importModFiles.put(simpleMod, new ArrayList<>());
            importModNames.put(simpleMod, new ArrayList<>());
        }
        AtomicBoolean someThingsNotCompatible = new AtomicBoolean(false);
        AtomicInteger currentZipArchiveNumber = new AtomicInteger();
        boolean errorWhileScanning = false;
        AtomicBoolean unzipAutomatic = new AtomicBoolean(false);
        AtomicBoolean showDuplicateMessage = new AtomicBoolean(true);
        AtomicBoolean addDuplicate = new AtomicBoolean(false);
        AtomicBoolean abortImport = new AtomicBoolean(false);
        JCheckBox checkBoxPreventZipMessage = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.checkBox.saveOption"));
        checkBoxPreventZipMessage.setSelected(false);
        if(directories != null){
            try {
                LOGGER.info("Scanning selected directories for compatible mods");
                ProgressBarHelper.initializeProgressBar(0, directories.size(), I18n.INSTANCE.get("progressBar.scanningDirectories"), false, false);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.scanningDirectories"));
                boolean askedForAbortImport = false;
                for(int i=0; i<directories.size(); i++){
                    if(!abortImport.get()) {
                        File file = directories.get(i);
                        Path start = Paths.get(file.getPath());
                        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
                            ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importAll.indexing"));
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.indexingFolder") + " " + start);
                            List<String> collect = stream
                                    .map(obj -> {
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.indexingFolder.indexed") + " " + obj);
                                        return String.valueOf(obj);
                                    })
                                    .sorted()
                                    .collect(Collectors.toList());
                            ProgressBarHelper.increaseMaxValue(collect.size());
                            ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.scanningDirectories"));
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.indexingComplete"));
                            if(directories.size() > 2000 || collect.size() > 10000 || WindowMain.PROGRESS_BAR.getValue() > 10000){
                                if(!askedForAbortImport){
                                    LOGGER.info("Directories: " + directories.size());
                                    LOGGER.info("List: " + collect.size());
                                    LOGGER.info("Progress bar: " + WindowMain.PROGRESS_BAR.getValue());
                                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.manyImportFilesFound"), I18n.INSTANCE.get("dialog.sharingManager.importAll.manyImportFilesFound.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                        askedForAbortImport = true;
                                    }else{
                                        abortImport.set(true);
                                    }
                                }
                            }
                            if(!abortImport.get()){
                                collect.forEach((string) -> {
                                    if(!abortImport.get()){
                                        if(string.endsWith(".zip")){
                                            TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + " " + string);
                                        }else{
                                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.currentPath") + " " + string);
                                        }
                                        for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                                            if(string.contains(advancedMod.getFileName())){
                                                addIfCompatible(string, importModFiles.get(advancedMod), importModNames.get(advancedMod), advancedMod.getBaseSharer().getCompatibleModToolVersions(), someThingsNotCompatible, showDuplicateMessage, addDuplicate);
                                            }
                                        }
                                        for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                                            if(string.contains(simpleMod.getFileName())){
                                                addIfCompatible(string, importModFiles.get(simpleMod), importModNames.get(simpleMod), simpleMod.getBaseSharer().getCompatibleModToolVersions(), someThingsNotCompatible, showDuplicateMessage, addDuplicate);
                                            }
                                        }
                                        if(string.endsWith(".zip")){
                                            boolean unzipFile = false;
                                            if(!checkBoxPreventZipMessage.isSelected()){
                                                JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + "<br><br>" + string + "<br><br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.secondPart"));
                                                Object[] obj = {label, checkBoxPreventZipMessage};
                                                if(JOptionPane.showConfirmDialog(null, obj, I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                                    unzipFile = true;
                                                    if(checkBoxPreventZipMessage.isSelected()){
                                                        unzipAutomatic.set(true);
                                                    }
                                                }
                                            }else{
                                                if(unzipAutomatic.get()){
                                                    unzipFile = true;
                                                }
                                            }
                                            if(unzipFile){
                                                try {
                                                    File extractedFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//" + currentZipArchiveNumber);
                                                    DataStreamHelper.unzip(string, extractedFolder);
                                                    ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.scanningDirectories"));
                                                    currentZipArchiveNumber.getAndIncrement();
                                                    directories.add(extractedFolder);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.firstPart") + "\n\n" + string + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage() + "\n\n" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.secondPart"), I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                                                        abortImport.set(true);
                                                    }
                                                }
                                            }
                                        }
                                        if(Settings.enableDebugLogging){
                                            LOGGER.info("current file: " + string);
                                        }
                                        ProgressBarHelper.increment();
                                    }
                                });
                                ProgressBarHelper.increment();
                            }
                        }
                    }
                }
                Backup.createFullBackup();
            } catch (IOException e)  {
                errorWhileScanning = true;
                TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importAll.error.general").replace("<html>", "").replace("<br>", " ") + " " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.general") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }catch (NullPointerException ignored){

            }
            if(!abortImport.get()){
                if(!errorWhileScanning){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.scanningDirectories.complete.firstPart") + " " + I18n.INSTANCE.get("textArea.importAll.scanningDirectories.complete.secondPart") + " " + Utils.convertSecondsToTime(ProgressBarHelper.getProgressBarTimer()) + "; " + I18n.INSTANCE.get("textArea.importAll.scanningDirectories.complete.thirdPart"));
                    ProgressBarHelper.resetProgressBar();
                    Map<AbstractBaseMod, JPanel> importModPanels = new HashMap<>();
                    Map<AbstractBaseMod, AtomicReference<ArrayList<Integer>>> selectedMods = new HashMap<>();
                    Map<AbstractBaseMod, AtomicBoolean> disableMods = new HashMap<>();
                    for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                        TextAreaHelper.appendText(advancedMod.getType() + ": " + importModFiles.get(advancedMod).size());
                        importModPanels.put(advancedMod, new JPanel());
                        selectedMods.put(advancedMod, new AtomicReference<>(new ArrayList<>()));
                        disableMods.put(advancedMod, new AtomicBoolean(true));
                    }
                    for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                        TextAreaHelper.appendText(simpleMod.getType() + ": " + importModFiles.get(simpleMod).size());
                        importModPanels.put(simpleMod, new JPanel());
                        selectedMods.put(simpleMod, new AtomicReference<>(new ArrayList<>()));
                        disableMods.put(simpleMod, new AtomicBoolean(true));
                    }
                    int importModFilesListsFilled = 0;
                    for(Map.Entry<AbstractBaseMod, ArrayList<File>> entry : importModFiles.entrySet()){
                        if(!entry.getValue().isEmpty()){
                            importModFilesListsFilled++;
                        }
                    }
                    if(importModFilesListsFilled != 0) {
                        for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                            setFeatureAvailableGuiComponents(advancedMod.getType(),importModFiles.get(advancedMod), importModPanels.get(advancedMod), selectedMods.get(advancedMod), disableMods.get(advancedMod));
                        }
                        for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                            setFeatureAvailableGuiComponents(simpleMod.getType(),importModFiles.get(simpleMod), importModPanels.get(simpleMod), selectedMods.get(simpleMod), disableMods.get(simpleMod));
                        }
                        String labelStartText;
                        String labelEndText;
                        String disableImportPopupsText;
                        String importErredMessage;
                        String importErredMessageTitle;
                        String importSuccessfulMessage;
                        String importSuccessfulMessageTitle;
                        if(importFromRestorePoint){
                            labelStartText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.startText.var1");
                            labelEndText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.endText.var1");
                            disableImportPopupsText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.disableImportPopupsText.var1");
                            importErredMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importErredMessage.var1");
                            importErredMessageTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importErredMessageTitle.var1");
                            importSuccessfulMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessfulMessage.var1");
                            importSuccessfulMessageTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessfulMessageTitle.var1");
                        }else{
                            labelStartText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.startText.var2");
                            labelEndText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.endText.var2");
                            disableImportPopupsText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.disableImportPopupsText.var2");
                            importErredMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importErredMessage.var2");
                            importErredMessageTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importErredMessageTitle.var2");
                            importSuccessfulMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessfulMessage.var2");
                            importSuccessfulMessageTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessfulMessageTitle.var2");
                        }
                        JLabel labelStart = new JLabel(labelStartText);
                        JLabel labelEnd = new JLabel(labelEndText);
                        JCheckBox checkBoxDisableImportPopups = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.guiComponents.checkBox.disableImportPopups"));
                        checkBoxDisableImportPopups.setToolTipText(disableImportPopupsText);
                        JCheckBox checkBoxDisableAlreadyExistPopups = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.guiComponents.checkBox.disableAlreadyExistsPopups"));
                        checkBoxDisableAlreadyExistPopups.setToolTipText(I18n.INSTANCE.get("dialog.sharingManager.importAll.guiComponents.checkBox.disableAlreadyExistsPopups.toolTip"));
                        checkBoxDisableAlreadyExistPopups.setSelected(true);
                        Object[] params;
                        checkBoxDisableImportPopups.setSelected(true);
                        ArrayList<JPanel> panels = new ArrayList<>();
                        for(Map.Entry<AbstractBaseMod, JPanel> entry : importModPanels.entrySet()){
                            panels.add(entry.getValue());
                        }
                        Object[] modPanels = panels.toArray(new Object[0]);
                        if(importFromRestorePoint){
                            params = new Object[]{labelStart, modPanels, labelEnd, checkBoxDisableImportPopups};
                        }else{
                            params = new Object[]{labelStart, modPanels, labelEnd, checkBoxDisableImportPopups, checkBoxDisableAlreadyExistPopups};
                        }
                        LOGGER.info("Showing dialog where the user can select what should be imported");
                        if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.sharingManager.importAll.importReady.message.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            if(importFromRestorePoint){
                                StringBuilder failedUninstalls = new StringBuilder();
                                boolean modRemovalFailed = Uninstaller.uninstallAllMods(failedUninstalls);
                                if(modRemovalFailed){
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.notAllModsRemoved") + " " + failedUninstalls.toString());
                                    LOGGER.info("Something went wrong while uninstalling mods, however this should not necessarily impact the uninstall process: " + failedUninstalls.toString());
                                }else{
                                    LOGGER.info("All mods have been removed");
                                }
                            }
                            boolean showMessageDialogs = checkBoxDisableImportPopups.isSelected();
                            AtomicBoolean showAlreadyExistPopups = new AtomicBoolean(checkBoxDisableAlreadyExistPopups.isSelected());
                            boolean errorOccurred = false;
                            ProgressBarHelper.initializeProgressBar(0, getNumberOfModsToImport(selectedMods, importModFiles), I18n.INSTANCE.get("progressBar.importingMods"));
                            for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                                if(!importAllFiles(importModFiles.get(advancedMod), selectedMods.get(advancedMod).get(), disableMods.get(advancedMod).get(), I18n.INSTANCE.get("commonText." + advancedMod.getMainTranslationKey()), (string) -> advancedMod.getBaseSharer().importMod(string, !showMessageDialogs), advancedMod.getBaseSharer().getCompatibleModToolVersions(), showAlreadyExistPopups)){
                                    if(importModFiles.get(advancedMod).size() > 0){
                                        LOGGER.info("Error occurred wile importing " + advancedMod.getType());
                                        errorOccurred = true;
                                    }
                                }
                            }
                            for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                                if(!importAllFiles(importModFiles.get(simpleMod), selectedMods.get(simpleMod).get(), disableMods.get(simpleMod).get(), I18n.INSTANCE.get("commonText." + simpleMod.getMainTranslationKey()), (string) -> simpleMod.getBaseSharer().importMod(string, !showMessageDialogs), simpleMod.getBaseSharer().getCompatibleModToolVersions(), showAlreadyExistPopups)){
                                    LOGGER.info("Error occurred wile importing " + simpleMod.getType());
                                    if(importModFiles.get(simpleMod).size() > 0){
                                        LOGGER.info("Error occurred wile importing " + simpleMod.getType());
                                        errorOccurred = true;
                                    }
                                }
                            }
                            if(errorOccurred){
                                TextAreaHelper.appendText(importErredMessage.replace("<html>", "").replace("<br>", "\n"));
                                JOptionPane.showMessageDialog(null, importErredMessage, importErredMessageTitle, JOptionPane.ERROR_MESSAGE);
                            }else{
                                if(importFromRestorePoint){
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.restoreSuccessful"));
                                }else{
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.completed"));
                                }
                                JOptionPane.showMessageDialog(null, importSuccessfulMessage, importSuccessfulMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else{
                            if(importFromRestorePoint){
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
                            }else{
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                            }
                        }
                    }else{
                        String noImportAvailableMessage;
                        String noImportAvailableButIncompatibleModsFound;
                        String windowTitle;
                        if(importFromRestorePoint){
                            noImportAvailableMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var1");
                            noImportAvailableButIncompatibleModsFound = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailableButIncompatibleModsFound.var1");
                            windowTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var1");
                        }else{
                            noImportAvailableMessage = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2");
                            noImportAvailableButIncompatibleModsFound = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailableButIncompatibleModsFound.var2");
                            windowTitle = I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2");
                        }
                        if(someThingsNotCompatible.get()){
                            JOptionPane.showMessageDialog(null, noImportAvailableButIncompatibleModsFound, windowTitle, JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, noImportAvailableMessage, windowTitle, JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }else{
                    if(importFromRestorePoint){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
                    }else{
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                    }
                }
            }else{
                if(importFromRestorePoint){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
                }else{
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                }
            }
        }else{
            if(importFromRestorePoint){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
            }else{
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
            }
        }
        //Delete the temp .zip extractions
        ProgressBarHelper.resetProgressBar();
        File tempFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Temp//");
        if(tempFolder.exists()){
            ThreadHandler.startThread(ThreadHandler.runnableDeleteTempFolder, "runnableDeleteTempFolder");
        }
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
        ArrayList<String> listEntries = new ArrayList<>();
        for(File file : files){
            try {
                listEntries.add(DataStreamHelper.getNameFromFile(file, "UTF_8BOM"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Utils.getSelectedEntries("Select what should be imported:", "Import", Utils.convertArrayListToArray(listEntries), Utils.convertArrayListToArray(listEntries),false);
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
    public static boolean importAllFiles(ArrayList<File> files, ArrayList<Integer> selectedEntryNumbers, boolean importNothing, String importName, ReturnValue importFunction, String[] compatibleModToolVersions, AtomicBoolean showAlreadyExistPopups){
        int currentFile = 0;
        if(!importNothing){
            boolean failed = false;
            for(File file : files){
                if(selectedEntryNumbers.size() == 0){
                    if(importThings(importName, importFunction, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
                        failed = true;
                    }
                }else{
                    if(selectedEntryNumbers.contains(currentFile)){
                        if(importThings(importName, importFunction, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
                            failed = true;
                        }
                    }
                }
                ProgressBarHelper.increment();
                currentFile++;
            }
            return failed;
        }else{
            return true;
        }
    }

    /**
     * Exports all available things when the user accepts.
     * @param exportAsRestorePoint When true all detected mods will be exported as restore point. This means that no message is displayed to the user and that the folder is different
     */
    public static void exportAll(boolean exportAsRestorePoint){
        boolean exportFiles = false;
        if(exportAsRestorePoint){
            exportFiles = true;
        }else{
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.exportAll.mainMessage"), I18n.INSTANCE.get("commonText.export"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                exportFiles = true;
            }
        }
        if(exportFiles){
            StringBuilder failedExports = new StringBuilder();
            try{
                for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                    failedExports.append(getExportFailed((string) -> advancedMod.getBaseSharer().exportMod(string, exportAsRestorePoint), advancedMod.getBaseAnalyzer().getFinishedCustomContentString(), advancedMod.getType()));
                }

                for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                    if(simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                        failedExports.append(getExportFailed((string) -> simpleMod.getBaseSharer().exportMod(string, exportAsRestorePoint), ModManager.themeMod.getAnalyzerEn().getFinishedCustomContentString(), simpleMod.getType()));
                    }else{
                        failedExports.append(getExportFailed((string) -> simpleMod.getBaseSharer().exportMod(string, exportAsRestorePoint), simpleMod.getBaseAnalyzer().getCustomContentString(), simpleMod.getType()));
                    }
                }
                if(failedExports.toString().isEmpty()){
                    if(exportAsRestorePoint){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.export.restorePointSuccessful"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.export.restorePointSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.export.exportSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                            Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                        }
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null,  I18n.INSTANCE.get("dialog.export.alreadyExported1") + ":\n\n" + failedExports.toString() + "\n" + I18n.INSTANCE.get("dialog.export.alreadyExported2") + "\n\n", I18n.INSTANCE.get("frame.title.success"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        if(exportAsRestorePoint){
                            Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Mod_Restore_Point//Current_Restore_Point//"));
                        }else{
                            Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                        }
                    }
                }
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.export.error") + ": " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
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
                currentLineNumber = 0;
            }
            stringBuilder.append(string);
            currentLineNumber++;
        }
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }

    /**
     * Uses the input exporter to export a list of entries. This function may only be called by {@link SharingManager#exportAll(boolean)} )}.
     * @param exporter The export function that should be used
     * @param strings The array containing the entries
     * @param exportName The name that should be written when a error occurs. Eg. Genre, Theme
     * @return Returns a string of errors if something failed to export
     */
    private static String getExportFailed(Exporter exporter, String[] strings, String exportName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstExportFailed = true;
        boolean exportFailed = false;
        ProgressBarHelper.initializeProgressBar(0, strings.length, I18n.INSTANCE.get("progressBar.startingExport") + " " + exportName);
        int currentProgressBarValue = 0;
        int currentExportFailed = 1;
        for(String string : strings){
            if(exporter.export(string)){
                if(firstExportFailed){
                    stringBuilder.append(exportName).append(": ");
                }else{
                    stringBuilder.append(", ");
                }
                if(currentExportFailed == 10){
                    currentExportFailed = 1;
                    stringBuilder.append(System.getProperty("line.separator"));
                }
                stringBuilder.append(string);
                firstExportFailed = false;
                exportFailed = true;
                currentExportFailed++;
            }
            currentProgressBarValue++;
            ProgressBarHelper.setValue(currentProgressBarValue);
        }
        TextAreaHelper.appendText(exportName + " " + I18n.INSTANCE.get("textArea.exportComplete"));
        ProgressBarHelper.resetProgressBar();
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
            for(Map.Entry entry : Objects.requireNonNull(map).entrySet()){
                if(Settings.enableDebugLogging){
                    LOGGER.info("Current Value: " + entry.getValue());
                }
                for(String string : compatibleVersions){
                    if(Settings.enableDebugLogging){
                        LOGGER.info("Current compatible version: " + string);
                    }
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
            for(Map.Entry entry : Objects.requireNonNull(map).entrySet()){
                if(Settings.enableDebugLogging){
                    LOGGER.info("Current Value: " + entry.getValue());
                }
                if(entry.getValue().toString().contains("NAME EN")){
                    return entry.getValue().toString().replace("[NAME EN]", "");
                }
                if(entry.getValue().toString().contains("NAME")){
                    String returnString = entry.getValue().toString();
                    for(String string : TranslationManager.TRANSLATION_KEYS) {
                        returnString.replace("[NAME " + string + "]", "");
                    }
                    returnString.replace("[NAME]", "");
                    return returnString;
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
     * @param showMessage The atomic boolean that stores if the already on import list message should be shown or not
     * @param addDuplicate The atomic boolean that stores if the duplicate mod should be added to the import list or not
     */
    private static void addIfCompatible(String string, ArrayList<File> feature, ArrayList<String> names, String[] compatibleModToolVersions, AtomicBoolean someThingsNotCompatible, AtomicBoolean showMessage, AtomicBoolean addDuplicate){
        if(isImportCompatible(new File(string), compatibleModToolVersions)){
            String returnValue = getImportName(new File(string));
            if(names.contains(returnValue)){
                JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.addIfCompatible.alreadyFound.firstPart") + " " + returnValue + "<br><br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.addIfCompatible.alreadyFound.secondPart"));
                JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.checkBox.saveOption"));
                Object[] obj = {label, checkBox};
                if(showMessage.get()){
                    if(JOptionPane.showConfirmDialog(null, obj, I18n.INSTANCE.get("dialog.sharingManager.importAll.addIfCompatible.alreadyFound.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        feature.add(new File(string));
                        addDuplicate.set(true);
                    }
                    if(checkBox.isSelected()){
                        showMessage.set(false);
                    }
                }else{
                    if(addDuplicate.get()){
                        feature.add(new File(string));
                    }
                }
            }else{
                if(!returnValue.equals("false")){
                    names.add(returnValue);
                }
                feature.add(new File(string));
            }
        }else{
            someThingsNotCompatible.set(true);
        }
    }

    private static int getNumberOfModsToImport(Map<AbstractBaseMod, AtomicReference<ArrayList<Integer>>> selectedMods, Map<AbstractBaseMod, ArrayList<File>> importModFiles){
        int maxValue = 0;
        for(Map.Entry<AbstractBaseMod, AtomicReference<ArrayList<Integer>>> entry : selectedMods.entrySet()){
            if(entry.getValue().get().size() > 0){
                maxValue = maxValue + entry.getValue().get().size();
            }else{
                maxValue = maxValue + importModFiles.get(entry.getKey()).size();
            }
        }
        return maxValue;
    }
}
