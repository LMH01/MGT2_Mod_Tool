package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.Importer;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.TimeHelper;
import com.github.lmh01.mgt2mt.util.interfaces.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import com.moandjiezana.toml.TomlWriter;
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
import java.util.concurrent.TimeUnit;
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
     * @param importer The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param importFolder The import folder where the files are stored.
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */

    public static boolean importThings(String importName, Importer importer, String[] compatibleModToolVersions, File importFolder, AtomicBoolean showAlreadyExistMessage){
        return importThings(null, importName, importer, compatibleModToolVersions, false, importFolder, showAlreadyExistMessage);
    }

    /**
     * Opens a gui where the user can select a folder from which the files should be imported. Only supports one type import.
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param importName The name that is written is some JOptionPanes. Eg. genre, publisher, theme
     * @param importer The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param askForImportFolder  If true the user will be asked to select a folder. If false the import folder will be used that passed as argument.
     * @param importFolder The import folder where the files are stored. The folder is used when ask for import folder is false.
     * @param showAlreadyExistMessage When true a message is displayed that the choose import does already exist
     */
    private static boolean importThings(String fileName, String importName, Importer importer, String[] compatibleModToolVersions, boolean askForImportFolder, File importFolder, AtomicBoolean showAlreadyExistMessage){
        try {
            if(askForImportFolder){
                ArrayList<File> importFolders = getImportFolderPath(fileName);
                try{
                    for(File folder : importFolders){
                        analyzeReturnValue(importName, importer.getReturnValue(folder.toPath()), compatibleModToolVersions, showAlreadyExistMessage);
                    }
                }catch(NullPointerException ignored){

                }
            }else{
                analyzeReturnValue(importName, importer.getReturnValue(importFolder.toPath()), compatibleModToolVersions, showAlreadyExistMessage);
            }
            return true;
        }catch(ModProcessingException e) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + " " + importName + ":\n" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.secondPart"));
            TextAreaHelper.printStackTrace(e);
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + " " + importName + ":\n" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @return Selected folders, ready for import
     */
    private static ArrayList<File> getImportFolderPath(String fileName){
        return getImportFolderPath(fileName, false);
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param skipCheckForContent True when the folder should not be checked if it contains the fileName. Useful when the return string should contain all folders.
     * @return Selected folders, ready for import.
     */
    private static ArrayList<File> getImportFolderPath(String fileName, boolean skipCheckForContent){
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
                ArrayList<File> importFolders = new ArrayList<>();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    File importFolder = files[i];
                    if(skipCheckForContent){
                        importFolders.add(importFolder);
                    }else{
                        if(DataStreamHelper.doesFolderContainFile(importFolder.toPath(), fileName)){
                            File fileGenreToImport = new File(importFolder + "/" + fileName);
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
                    JOptionPane.showMessageDialog(null, returnValue + "\n" + I18n.INSTANCE.get("dialog.sharingManager.analyzeReturnValue.supportedVersions") + " " + supportedModToolVersions, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
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
        importAll(false, Paths.get(""));
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     * @param importFromRestorePoint True when this function is called from import point restore. Will change some messages.
     * @param folderPath If not empty, this folder will be used as root folder where the search is started. This will prevent the message to the user that folders should be selected.
     */
    public static void importAll(boolean importFromRestorePoint, Path folderPath){
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.start"));
        ArrayList<File> directories;
        if(folderPath.toString().isEmpty()){
            LOGGER.info("Opening window where the user can select the folders that should be searched for imports");
            directories = getImportFolderPath("import", true);;
        }else{
            directories = new ArrayList<>();
            directories.add(folderPath.toFile());
        }
        Map<AbstractBaseMod, ArrayList<File>> importModFiles = new HashMap<>();
        Map<AbstractBaseMod, ArrayList<String>> importModNames = new HashMap<>();
        for (AbstractBaseMod mod : ModManager.mods) {
            importModFiles.put(mod, new ArrayList<>());
            importModNames.put(mod, new ArrayList<>());
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
                                    //.peek(obj -> TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.indexingFolder.indexed") + " " + obj))
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
                                        for (AbstractBaseMod mod : ModManager.mods) {
                                            if(string.contains(mod.getImportExportFileName())){
                                                addIfCompatible(string, importModFiles.get(mod), importModNames.get(mod), mod.getCompatibleModToolVersions(), someThingsNotCompatible, showDuplicateMessage, addDuplicate);
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
                                                    Path extractedFolder = ModManagerPaths.TEMP.getPath().resolve(Integer.toString(currentZipArchiveNumber.get()));
                                                    DataStreamHelper.unzip(Paths.get(string), extractedFolder);
                                                    ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.scanningDirectories"));
                                                    currentZipArchiveNumber.getAndIncrement();
                                                    directories.add(extractedFolder.toFile());
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
                    for (AbstractBaseMod mod : ModManager.mods) {
                        TextAreaHelper.appendText(mod.getType() + ": " + importModFiles.get(mod).size());
                        importModPanels.put(mod, new JPanel());
                        selectedMods.put(mod, new AtomicReference<>(new ArrayList<>()));
                        disableMods.put(mod, new AtomicBoolean(true));
                    }
                    int importModFilesListsFilled = 0;
                    for(Map.Entry<AbstractBaseMod, ArrayList<File>> entry : importModFiles.entrySet()){
                        if(!entry.getValue().isEmpty()){
                            importModFilesListsFilled++;
                        }
                    }
                    if(importModFilesListsFilled != 0) {
                        for (AbstractBaseMod mod : ModManager.mods) {
                            setFeatureAvailableGuiComponents(mod.getType(),importModFiles.get(mod), importModPanels.get(mod), selectedMods.get(mod), disableMods.get(mod));
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
                                boolean modRemovalFailed = false;
                                try {
                                    modRemovalFailed = Uninstaller.uninstallAllMods(failedUninstalls);
                                } catch (ModProcessingException e) {
                                    TextAreaHelper.printStackTrace(e);
                                    modRemovalFailed = true;
                                }
                                if(modRemovalFailed){
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.notAllModsRemoved") + " " + failedUninstalls);
                                    LOGGER.info("Something went wrong while uninstalling mods, however this should not necessarily impact the uninstall process: " + failedUninstalls);
                                }else{
                                    LOGGER.info("All mods have been removed");
                                }
                            }
                            boolean showMessageDialogs = checkBoxDisableImportPopups.isSelected();
                            AtomicBoolean showAlreadyExistPopups = new AtomicBoolean(checkBoxDisableAlreadyExistPopups.isSelected());
                            boolean errorOccurred = false;
                            ProgressBarHelper.initializeProgressBar(0, getNumberOfModsToImport(selectedMods, importModFiles), I18n.INSTANCE.get("progressBar.importingMods"));
                            for (AbstractBaseMod mod : ModManager.mods) {
                                if(!importAllFiles(importModFiles.get(mod), selectedMods.get(mod).get(), disableMods.get(mod).get(), I18n.INSTANCE.get("commonText." + mod.getMainTranslationKey()), (string) -> mod.importMod(string, !showMessageDialogs), mod.getCompatibleModToolVersions(), showAlreadyExistPopups)){
                                    if(importModFiles.get(mod).size() > 0){
                                        LOGGER.info("Error occurred wile importing " + mod.getType());
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
        if(ModManagerPaths.TEMP.toFile().exists()){
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
    private static ArrayList<Integer> getSelectedEntries(ArrayList<File> files){//TODO hinzufügen, dass wenn abbrechen gedrückt wird, nicht gespeichert wird, was gerade angezeigt wird
        ArrayList<String> listEntries = new ArrayList<>();
        for(File file : files){
            listEntries.add(getImportName(file));
        }
        return Utils.getSelectedEntries(I18n.INSTANCE.get("dialog.sharingManager.selectImports"), I18n.INSTANCE.get("frame.title.import"), Utils.convertArrayListToArray(listEntries), Utils.convertArrayListToArray(listEntries),false);
    }

    /**
     * Imports all files that are listed in the files array
     * @param files The array containing the import files
     * @param selectedEntryNumbers If not empty only the files numbers listed in this array are imported
     * @param importNothing If true nothing will be imported.
     * @param importName The name that is written in some JOptionPanes. Eg. genre, publisher, theme
     * @param importer The function that imports the files
     * @param compatibleModToolVersions A array containing the compatible mod tool versions for the import file
     * @param showAlreadyExistPopups When true a message is displayed that the choose import does already exist
     * @return Returns true when the import was successful. Returns false when something went wrong
     */
    public static boolean importAllFiles(ArrayList<File> files, ArrayList<Integer> selectedEntryNumbers, boolean importNothing, String importName, Importer importer, String[] compatibleModToolVersions, AtomicBoolean showAlreadyExistPopups){
        int currentFile = 0;
        if(!importNothing){
            boolean failed = false;
            for(File file : files){
                if(selectedEntryNumbers.size() == 0){
                    if(importThings(importName, importer, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
                        failed = true;
                    }
                }else{
                    if(selectedEntryNumbers.contains(currentFile)){
                        if(importThings(importName, importer, compatibleModToolVersions, file.getParentFile(), showAlreadyExistPopups)){
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
     * Exports the specified mod to the export folder.
     * Writes a message to the text area if mod export was successful or if mod was already exported
     * @param mod The mod_type the mod belongs to
     * @param name The name of the mod that should be exported
     * @param folder The root folder where the mods should be exported to
     */
    public static void exportSingleMod(AbstractBaseMod mod, String name, Path folder) throws ModProcessingException {
        LOGGER.info("exporting mod: " + name);
        Path path = folder.resolve(mod.getExportType());
        String fileName = mod.getExportType() + "_" + Utils.convertName(name) + ".toml";
        if (!Files.exists(path.resolve(fileName)) && !Files.exists(path.resolve(Utils.convertName(name) + "/" + fileName))) {
            try {
                Files.createDirectories(path);
                Map<String, Object> map = mod.getExportMap(name);
                map.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
                map.put("type", ExportType.ALL_SINGLE.getTypeName());
                map.put("mod_type", mod.getExportType());
                TomlWriter tomlWriter = new TomlWriter();
                if (mod instanceof AbstractSimpleMod) {
                    tomlWriter.write(map, path.resolve(fileName).toFile());
                } else if (mod instanceof AbstractAdvancedMod) {
                    Path singleMod = path.resolve(Utils.convertName(name));
                    Path assets = singleMod.resolve("assets");
                    Files.createDirectories(assets);
                    map.putAll(((AbstractAdvancedMod)mod).exportImages(name, assets));
                    tomlWriter.write(map, singleMod.resolve(fileName).toFile());
                }
            } catch (IOException e) {
                throw new ModProcessingException("Unable to export mod", e);
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exported") + " " + mod.getMainTranslationKey() + " - " + name);
        } else {
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + mod.getMainTranslationKey() + " - " + name + ": " + I18n.INSTANCE.get("commonText.alreadyExported"));
        }
    }

    /**
     * Exports all currently installed mods. It is recommended to start this function inside a thread.
     * The text area and the progress bar are used to display progress.
     * @throws ModProcessingException If something went wrong while exporting mods
     * @param exportType Determines how and to where the mods should be exported
     */
    public static void exportAll(ExportType exportType) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS);
        timeHelper.measureTime();
        if (exportType.equals(ExportType.ALL_SINGLE)) {
            Path exportFolder;
            if (Settings.enableExportStorage) {
                exportFolder = ModManagerPaths.EXPORT.getPath().resolve("single/" + Utils.getCurrentDateTime());
            } else {
                exportFolder = ModManagerPaths.EXPORT.getPath().resolve("single/");
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.all_single.started"));
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("commonText.exporting"));
            for (AbstractBaseMod mod : ModManager.mods) {
                ProgressBarHelper.increaseMaxValue(mod.getCustomContentString().length);
                ProgressBarHelper.setText(I18n.INSTANCE.get("commonText.exporting") + " " + mod.getType());
                for (String string : mod.getCustomContentString()) {
                    exportSingleMod(mod, string, exportFolder);
                    ProgressBarHelper.increment();
                }
            }
        } else {//TODO check that getContentByAlphabet is replaced with getCustomContentString in release version
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.export.creatingExportMap"));
            Path path;
            Map<String, Object> map = new HashMap<>();
            String tomlName;
            if (exportType.equals(ExportType.RESTORE_POINT)) {
                path = ModManagerPaths.CURRENT_RESTORE_POINT.getPath();
                map.put("type", exportType.getTypeName());
                tomlName = "restore_point";
            } else {
                if (Settings.enableExportStorage) {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/" + Utils.getCurrentDateTime());
                } else {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/");
                }
                map.put("type", exportType.getTypeName());
                tomlName = "export";
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.starting"));
            try {
                Files.createDirectories(path.resolve("assets"));
                Files.createDirectories(path);
                TomlWriter tomlWriter = new TomlWriter();
                Map<String, Object> simpleMods = new HashMap<>();
                Map<String, Object> advancedMods = new HashMap<>();
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.creatingExportMap"));
                for (AbstractBaseMod mod : ModManager.mods) {
                    Map<String, Object> modMap = new HashMap<>();
                    ProgressBarHelper.increaseMaxValue(mod.getCustomContentString().length);
                    for (String string : mod.getCustomContentString()) {
                        Map<String, String> singleModMap = mod.getExportMap(string);
                        if (mod instanceof AbstractAdvancedMod) {//This will add the image name(s) to the map if required and copy the image files
                            singleModMap.putAll(((AbstractAdvancedMod)mod).exportImages(string, path.resolve("assets")));
                        }
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.addingEntry") + ": " + mod.getType() + " - " + string);
                        modMap.put(string, singleModMap);
                        ProgressBarHelper.increment();
                    }
                    if (mod instanceof AbstractSimpleMod) {
                        simpleMods.put(mod.getExportType(), modMap);
                    } else {
                        advancedMods.put(mod.getExportType(), modMap);
                    }
                }
                map.put("simple_mods", simpleMods);
                map.put("advanced_mods", advancedMods);
                map.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
                ProgressBarHelper.setText(I18n.INSTANCE.get("textArea.export_compact.writing_toml"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export_compact.writing_toml") + ": " + path.resolve(tomlName + ".toml"));
                try {
                    tomlWriter.write(map, path.resolve(tomlName + ".toml").toFile());
                } catch (NullPointerException e) {
                    throw new ModProcessingException("Unable to write .toml file", e);
                }
            } catch (IOException e) {
                throw new ModProcessingException("Unable to export mods", e);
            }
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.exportComplete"));
        LOGGER.info("Exporting mods as " + exportType.getTypeName() + " took " + timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " milliseconds!");
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Opens a JOptionPane where the user can decide how the mods should be exported: Single or bundled
     */
    public static void displayExportModsWindow() throws ModProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.exportAll.mainMessage.part1"));
        if (!Settings.enableExportStorage) {
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.exportAll.mainMessage.part2"));
        }
        JLabel label = new JLabel(stringBuilder.toString());
        JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport"));
        checkBox.setToolTipText(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport.toolTip"));
        JComponent[] components = {label, checkBox};
        if (JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("commonText.export"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
            if (checkBox.isSelected()) {
                exportAll(ExportType.ALL_SINGLE);
            } else {
                exportAll(ExportType.ALL_BUNDLED);
            }
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.export.exportSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                try {
                    Desktop.getDesktop().open(ModManagerPaths.EXPORT.toFile());
                } catch (IOException e) {
                    throw new ModProcessingException("Unable to open export folder", e);
                }
            }
        }
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
            Map<String, String> map = DataStreamHelper.parseDataFile(inputFile).get(0);
            if(map.get("NAME EN") != null){
                return map.get("NAME EN").replace("[NAME EN]", "");
            }else if (map.get("NAME") != null){
                return map.get("NAME").replace("[NAME]", "");
            }else if (map.get("LINE") != null){
                String output = map.get("LINE");
                LOGGER.info("LINE vor output: " + output);
                for(AbstractBaseMod mod : ModManager.mods){
                    if (mod instanceof AbstractSimpleMod) {
                        output = ((AbstractSimpleMod) mod).getReplacedLine(output);
                    }
                }
                LOGGER.info("Line nach output: " + output);
                return output;
            }else{
                for(String string : TranslationManager.TRANSLATION_KEYS) {
                    if(map.get("NAME " + string) != null){
                        return map.get("NAME " + string).replace("[NAME " + string + "]", "");
                    }
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
                    feature.add(new File(string));
                    someThingsNotCompatible.set(true);
                }
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
