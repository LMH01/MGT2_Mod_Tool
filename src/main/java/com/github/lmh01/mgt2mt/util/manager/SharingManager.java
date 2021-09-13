package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
     * @deprecated This function is no longer used by the new import function
     */
    @Deprecated
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
     * @deprecated This function is no longer used by the new import function
     */
    @Deprecated
    private static boolean importThings(String fileName, String importName, Importer importer, String[] compatibleModToolVersions, boolean askForImportFolder, File importFolder, AtomicBoolean showAlreadyExistMessage){
        try {
            if(askForImportFolder){
                ArrayList<File> importFolders = getImportFolderPathOld(fileName);
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
     * @return Selected folders
     */
    private static Set<Path> getImportFolders() throws ModProcessingException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setDialogTitle(I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.title.version1"));
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
                Set<Path> importFolders = new HashSet<>();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    File importFolder = files[i];
                    importFolders.add(importFolder.toPath());
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                return importFolders;
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            throw new ModProcessingException("Unable to get selected import folders", e);
        }
        return null;
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @return Selected folders, ready for import
     * @deprecated Use {@link SharingManager#getImportFolders()} instead.
     */
    @Deprecated
    private static ArrayList<File> getImportFolderPathOld(String fileName){
        return getImportFolderPathOld(fileName, false);
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     * @param fileName This is the file the tool will search for in the folder. Eg. genre.txt or publisher.txt
     * @param skipCheckForContent True when the folder should not be checked if it contains the fileName. Useful when the return string should contain all folders.
     * @return Selected folders, ready for import.
     * @deprecated Use {@link SharingManager#getImportFolders()} instead.
     */
    @Deprecated
    public static ArrayList<File> getImportFolderPathOld(String fileName, boolean skipCheckForContent){
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
    @Deprecated
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
     * Opens a window where the user can select folders that should be searched for mods.
     * @see SharingManager#importAll(ImportType, Set)
     */
    public static void importAll(ImportType importType) throws ModProcessingException {
        Set<Path> paths = getImportFolders();
        if (paths != null) {
            importAll(importType, getImportFolders());
        }
    }

    /**
     * Searches the path for mods in .toml files.
     * When the search is completed a message is displayed to the user where it can be selected which mod should be imported.
     * When mods are searched it is analyzed if they depend on any other mods and if these mod do exist.
     * @param importType The type of the import.
     * @param path The root folder where the search for mods should be started at. Use {@link SharingManager#importAll(ImportType, Set)} if multiple root folders should be searched.
     */
    public static void importAll(ImportType importType, Path path) throws ModProcessingException {
        Set<Path> paths = new HashSet<>();
        paths.add(path);
        importAll(importType, paths);
    }

    /**
     * Searches the path for mods in .toml files.
     * When the search is completed a message is displayed to the user where it can be selected which mod should be imported.
     * When mods are searched it is analyzed if they depend on any other mods and if these mod do exist.
     * @param importType The type of the import.
     * @param paths The root folders where the search for mods should be started at.
     */
    public static void importAll(ImportType importType, Set<Path> paths) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.start"));
        ArrayList<File> tomlFiles = getTomlFiles(paths);
        if (!tomlFiles.isEmpty()) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.totalTomlFilesFound") + ": " + tomlFiles.size());
            ArrayList<Map<String, Object>> modList = transformTomlFilesToMaps(tomlFiles);
            if (!modList.isEmpty()) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.foundCompatibleFiles") + ": " + modList.size());
                ArrayList<Map<String, Object>> singleMods = getFilteredModMaps(modList, false);
                ArrayList<Map<String, Object>> bundledMods = getFilteredModMaps(modList, true);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.singleMods") + ": " + singleMods.size());
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.bundledMods") + ": " + bundledMods.size());
                if (importType.equals(ImportType.RESTORE_POINT)) {
                    Uninstaller.uninstallAllMods();
                    ModManager.analyzeMods();
                }
                Set<Map<String, Object>> modMaps = getImportMaps(singleMods, bundledMods);
                if (!modMaps.isEmpty()) {
                    /*TODO Change the message to show all detected mods and make the user selected what mods should be imported
                        After that the dependency check and import should be done*/
                    if (JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part1") + ": " + modMaps.size() + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part2"), I18n.INSTANCE.get(""), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        Set<Map<String, Object>> modsChecked = checkDependencies(modMaps);
                        if (modsChecked != null) {
                            setImportHelperMaps(modsChecked);
                            importAllMods(modsChecked);
                            if(importType.equals(ImportType.RESTORE_POINT)){
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.restoreSuccessful"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored"), I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored.title"), JOptionPane.INFORMATION_MESSAGE);
                            }else{
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.completed"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessful"), I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessful.title"), JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                        }
                    } else {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                    }
                } else {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.importCanceled") + ": " + I18n.INSTANCE.get("textArea.importAll.noUniqueModsFound"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2"), I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2"), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.importCanceled") + ": " + I18n.INSTANCE.get("textArea.importAll.noCompatibleTomlFilesFound"));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2"), I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2"), JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            ThreadHandler.startModThread(() -> JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.INFORMATION_MESSAGE), "showNoTomlFoundInformation");
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"));
        }
        if(ModManagerPaths.TEMP.toFile().exists()){
            ThreadHandler.startThread(ThreadHandler.runnableDeleteTempFolder, "runnableDeleteTempFolder");
        }
    }

    /**
     * Searches all folders in the path for toml files.
     * Uses the text area and the progress bar to display progress.
     * If a .zip folder is found it can be unzipped and searched for mods.
     * @return An array list containing the toml files that have been found.
     *          If the import has been canceled the array map will be empty.
     */
    private static ArrayList<File> getTomlFiles(Set<Path> paths) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingForTomlFiles"));
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS);
        timeHelper.measureTime();
        ArrayList<File> tomlFiles = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(0 , 1, I18n.INSTANCE.get("progressBar.scanningDirectories"), false, false);
        JCheckBox checkBoxPreventZipMessage = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.importAll.checkBox.saveOption"));
        checkBoxPreventZipMessage.setSelected(false);
        AtomicBoolean unzipAutomatic = new AtomicBoolean(false);
        AtomicInteger currentZipArchiveNumber = new AtomicInteger();
        AtomicBoolean abortImport = new AtomicBoolean(false);
        for (Path path : paths) {
            for (Path path1 : getTomlFiles(path, abortImport, checkBoxPreventZipMessage, unzipAutomatic, currentZipArchiveNumber)) {
                tomlFiles.add(path1.toFile());
            }
        }
        if (abortImport.get()) {
            return new ArrayList<>();
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchComplete") + " " + timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " ms!");
        return tomlFiles;
    }

    private static Set<Path> getTomlFiles(Path path, AtomicBoolean abortImport, JCheckBox checkBoxPreventZipMessage, AtomicBoolean unzipAutomatic, AtomicInteger currentZipArchiveNumber) throws ModProcessingException {
        Set<Path> tomlFiles = new HashSet<>();
        if (!abortImport.get()) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!abortImport.get()) {
                            if (file.toFile().getName().endsWith(".toml")) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.foundTomlFile") + ": " + file);
                                tomlFiles.add(file);
                            } else if (file.toFile().getName().endsWith(".zip")) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + " " + file);
                                boolean unzipFile = false;
                                if(!checkBoxPreventZipMessage.isSelected()){
                                    JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + "<br><br>" + file + "<br><br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.secondPart"));
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
                                        DataStreamHelper.unzip(file, extractedFolder);
                                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.scanningDirectories"));
                                        currentZipArchiveNumber.getAndIncrement();
                                        tomlFiles.addAll(getTomlFiles(extractedFolder, abortImport, checkBoxPreventZipMessage, unzipAutomatic, currentZipArchiveNumber));
                                    } catch (IOException | ModProcessingException | IllegalArgumentException e) {
                                        TextAreaHelper.printStackTrace(e);
                                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.firstPart") + "\n\n" + file + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage() + "\n\n" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.secondPart"), I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                                            abortImport.set(true);
                                        }
                                    }
                                }
                            }
                            ProgressBarHelper.increment();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (!abortImport.get()) {
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingFolder") + ": " + dir);
                            ProgressBarHelper.increaseMaxValue(dir.getNameCount() - 1);
                        }
                        return super.preVisitDirectory(dir, attrs);
                    }
                });
            } catch (IOException e) {
                throw new ModProcessingException("Error while searching for .toml files", e);
            }
        }
        return tomlFiles;
    }

    /**
     * Analyzes the input toml files to determine if they are files that contain mods.
     * If the toml file is valid it is transformed into a map, which then is put into the array list.
     * This array list is returned by this function.
     * @param files The array list that should be searched for compatible toml files
     * @return An array list that contains maps, which contain mods
     */
    private static ArrayList<Map<String, Object>> transformTomlFilesToMaps(ArrayList<File> files) throws ModProcessingException {
        ArrayList<Map<String, Object>> modList = new ArrayList<>();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.analyzeTomlFiles"));
        ProgressBarHelper.initializeProgressBar(0, files.size(), I18n.INSTANCE.get("textArea.importAll.readingFiles"));
        for (File file : files) {
            try {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.readingToml") + ": " + file);
                Toml toml = new Toml().read(file);
                if (toml.contains("mod_tool_version") && toml.contains("type")) {
                    Map<String, Object> tomlMap = toml.toMap();
                    tomlMap.put("assets_folder", file.getParentFile().toPath().resolve("assets").toString());
                    modList.add(tomlMap);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.tomlNotValid"));
            }
            ProgressBarHelper.increment();
        }
        return modList;
    }

    /**
     * Filters the input array list to return eiter an array that contains only single mods or only bundled mods.
     * @param modList The array list that contains the mods
     * @param bundled If true the function returns an array that contains only compact mods
     *                If false the function returns an array that contains only single mods
     * @return An array list that contains the filtered mods
     */
    private static ArrayList<Map<String, Object>> getFilteredModMaps(ArrayList<Map<String, Object>> modList, boolean bundled) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> map : modList) {
            if (bundled) {
                if (map.get("type").equals(ExportType.ALL_BUNDLED.getTypeName()) || map.get("type").equals(ExportType.RESTORE_POINT.getTypeName())) {
                    list.add(map);
                }
            } else {
                if (map.get("type").equals(ExportType.ALL_SINGLE.getTypeName())) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * Searches the array lists for mods and returns a map that contains the data of the mods that have been found.
     * This function does check if the mods are compatible with this mod tool version.
     * Incompatible mods will not be put into the map.
     * If duplicate mods are found they will not be added to the list again.
     * If a mod is found that is already installed it will not be added to the import list.
     * The progress bar and text area are utilized
     * @param singleMods An array list that contains all toml instances that are verified to contain a single mod.
     *                   This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}.
     * @param bundledMods An array list that contains all toml instances that are verified to contain bundled mods
     *                    This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}
     * @return A set of maps that contain the mod data.
     * If no compatible mods are found an empty map is returned.
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> getImportMaps(ArrayList<Map<String, Object>> singleMods, ArrayList<Map<String, Object>> bundledMods) throws ModProcessingException {
        ProgressBarHelper.initializeProgressBar(0, singleMods.size() + bundledMods.size(), I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        /*
         * ModType = Simple or advanced
         * Map<String, Object> = The map that contains the values for the specific mod.
         * This includes what the mod_type (genre/hardware/etc.) and
         * the dependencies are.
         */
        Set<Map<String, Object>> mods = new HashSet<>();
        int modsTotal = 0;
        int modsDuplicated = 0;
        int modsAlreadyAdded = 0;
        int modsIncompatible = 0;
        for (Map<String, Object> map : singleMods) {//TODO Check why duplicated mods can be added when they are placed in single mods and bundled mods
            if (map.containsKey("\"NAME EN\"")) {
                LOGGER.info("single mod instance found!: " + map.get("\"NAME EN\""));
            } else {
                LOGGER.info("single mod instance found!: " + map.get("line"));
            }
            if (isModToolVersionSupported(map)) {
                if (map.get("base_mod_type").equals("simple") || map.get("base_mod_type").equals("advanced")) {
                    if (doesMapContainMod(mods, map.get("mod_name").toString(), map.get("mod_type").toString())){
                        modsDuplicated++;
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modsDuplicated") + ": " + map.get("mod_type") + " - " + map.get("mod_name"));
                    } else if (doesModExist((String) map.get("mod_name"), (String) map.get("mod_type"))) {
                        modsAlreadyAdded++;
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modAlreadyAdded") + ": " + map.get("mod_type") + " - " + map.get("mod_name"));
                    } else {
                        mods.add(removeQuoteSymbol(map));
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modCompatible") + ": " + map.get("mod_type") + " - " + map.get("mod_name"));
                    }
                    modsTotal++;
                } else {
                    LOGGER.info("Toml instance is invalid: " + map);
                }
            } else {
                modsTotal++;
                modsIncompatible++;
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modNotCompatible.firstPart") + ": " + map.get("mod_type") + " - " + map.get("mod_name") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.secondPart") + ": " + map.get("mod_tool_version") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.thirdPart") + ": " + Arrays.toString(getRequiredModToolVersion((String) map.get("mod_type"))));
            }
            ProgressBarHelper.increment();
        }
        for (Map<String, Object> map : bundledMods) {
            LOGGER.info("bundled mod instance found!");
            try {
                Map<String, Object> simple = (Map<String, Object>) map.get("simple_mods");
                Map<String, Object> advanced = (Map<String, Object>) map.get("advanced_mods");
                for (AbstractBaseMod mod : ModManager.mods) {
                    Map<String, Object> modMap = new HashMap<>();
                    try {
                        modMap.putAll((Map<String, Object>) simple.get(mod.getExportType()));
                    } catch (NullPointerException e) {
                        LOGGER.info("simpleModMap is null; Export type: " + mod.getExportType());
                    }
                    try {
                        modMap.putAll((Map<String, Object>) advanced.get(mod.getExportType()));
                    } catch (NullPointerException e) {
                        LOGGER.info("advancedModMap is null; Export type: " + mod.getExportType());
                    }
                    ProgressBarHelper.increaseMaxValue(modMap.entrySet().size());
                    for (Map.Entry<String, Object> entry : modMap.entrySet()) {
                        Map<String, Object> singleModMap = (Map<String, Object>) entry.getValue();
                        if (isModToolVersionSupported(mod.getExportType(), (String) map.get("mod_tool_version"))) {
                            if (mod instanceof AbstractAdvancedMod) {
                                singleModMap.put("base_mod_type", "advanced");
                            }
                            if (mod instanceof AbstractSimpleMod) {
                                singleModMap.put("base_mod_type", "simple");
                            }
                            singleModMap.put("mod_type", mod.getExportType());
                            singleModMap.put("assets_folder", map.get("assets_folder"));
                            if (doesMapContainMod(mods, singleModMap.get("mod_name").toString(), mod.getExportType())){
                                modsDuplicated++;
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modsDuplicated") + ": " + mod.getExportType() + " - " + singleModMap.get("mod_name"));
                            } else if (doesModExist((String) singleModMap.get("mod_name"), mod.getExportType())) {
                                modsAlreadyAdded++;
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modAlreadyAdded") + ": " + mod.getExportType() + " - " + singleModMap.get("mod_name"));
                            } else {
                                mods.add(removeQuoteSymbol(singleModMap));
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modCompatible") + ": " + mod.getExportType() + " - " + singleModMap.get("mod_name"));
                            }
                            modsTotal++;
                        } else {
                            modsTotal++;
                            modsIncompatible++;
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modNotCompatible.firstPart") + ": " + mod.getExportType() + " - " + singleModMap.get("mod_name") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.secondPart") + ": " + map.get("mod_tool_version") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.thirdPart") + ": " + Arrays.toString(getRequiredModToolVersion(mod.getType())));
                        }
                        ProgressBarHelper.increment();
                    }
                }
            } catch (ClassCastException e) {
                LOGGER.info("Mod instance not compatible because file is not readable!");
                e.printStackTrace();
            }
            ProgressBarHelper.increment();
        }
        int uniqueMods = mods.size();
        ProgressBarHelper.resetProgressBar();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.totalModsFound") + ": " + modsTotal);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.alreadyAdded") + ": " + modsAlreadyAdded);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.incompatible") + ": " + modsIncompatible);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.duplicated") + ": " + modsDuplicated);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.unique") + ": " + uniqueMods);
        LOGGER.info("Found a total of " + modsTotal + " mods");
        if (Settings.enableDebugLogging) {
            for (Map<String, Object> map : mods) {
                LOGGER.info("Name: " + map.get("mod_name") + " | type: " + map.get("base_mod_type"));
            }
        }
        if (uniqueMods > 0) {
            return mods;
        }
        return new HashSet<>();
    }

    /**
     * Removes the quote symbol from the maps keys.
     * @param map The map where the keys should be modified
     * @return A map where the quote symbol is no longer part of the key
     */
    private static Map<String, Object> removeQuoteSymbol(Map<String, Object> map) {
        Map<String, Object> modMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {//Removes the " from the map keys
            modMap.put(entry.getKey().replaceAll("\"", ""), entry.getValue());
        }
        return modMap;
    }

    /**
     * Checks the mods contained in the set for dependencies and returns a set that is ready to be imported.
     * If a dependency is not found the user is prompted to select a replacement. If no replacement is selected a random replacement will be chosen.
     * The function that replaces the dependency is {@link SharingManager#replaceDependency(AbstractBaseMod, String, Map)}.
     * @param mods The map where the mods are stored in
     * @return A set of maps that contain the mod data. The data is checked for dependencies. Returns null if dependency check has been canceled by the user
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> checkDependencies(Set<Map<String, Object>> mods) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        ProgressBarHelper.initializeProgressBar(0, mods.size(), I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        for (Map<String, Object> map2 : mods) {
            if (map2.containsKey("dependencies")) {
                //TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.requiresDependencies") + ": " + map2.get("mod_type") + " - " + map2.get("mod_name") + " - " + map2.get("dependencies"));
                Map<String, Object> dependencies = (Map<String, Object>) map2.get("dependencies");
                for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                    for (AbstractBaseMod mod : ModManager.mods) {
                        if (entry.getKey().equals(mod.getExportType())) {
                            ArrayList<String> arrayList = (ArrayList<String>) entry.getValue();
                            for (String string : arrayList) {
                                if (!doesModExist(string, mod.getExportType()) && !doesMapContainMod(mods, string, mod.getExportType())) {
                                    JPanel panel = new JPanel();
                                    JLabel label1 = new JLabel("<html>" +  I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part1") + ":<br><br>" + mod.getType() + " - " + string + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part2"));
                                    JList<String> list = WindowHelper.getList(mod.getContentByAlphabet(), false);
                                    JScrollPane scrollPane = WindowHelper.getScrollPane(list);
                                    JLabel label2 = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part3"));
                                    panel.add(label1);
                                    panel.add(scrollPane);
                                    panel.add(label2);
                                    JComponent[] components = {label1, scrollPane, label2};
                                    /*TODO Add check box that can be checked to always select a random dependency if one is missing
                                    *  this will then hold the message dialog back*/
                                    if (JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("frame.title.missingDependency"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                                        if (list.isSelectionEmpty()) {
                                            replaceDependency(mod, string, map2);
                                        } else {
                                            replaceDependency(mod, string, map2, list.getSelectedValue());
                                        }
                                    } else {
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ProgressBarHelper.increment();
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.dependencyCheckComplete"));
        return mods;
    }

    /**
     * Replaces the dependency in the map with a random one
     * @param mod The mod the dependency belongs to that should be replaced
     * @param name The dependency name that should be replaced
     * @param map The mod map where the dependency should be replaced in
     */
    @SuppressWarnings("unchecked")
    private static void replaceDependency(AbstractBaseMod mod, String name, Map<String, Object> map) throws ModProcessingException {
        replaceDependency(mod, name, map, mod.getContentByAlphabet()[Utils.getRandomNumber(0, mod.getContentByAlphabet().length)]);
    }

    /**
     * Replaces the dependency in the map with the selected replacement
     * @param mod The mod the dependency belongs to that should be replaced
     * @param name The dependency name that should be replaced
     * @param map The mod map where the dependency should be replaced in
     * @param replacement The name with which the missing dependency should be replaced
     */
    @SuppressWarnings("unchecked")
    private static void replaceDependency(AbstractBaseMod mod, String name, Map<String, Object> map, String replacement) throws ModProcessingException {
        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
            if (entry1.getKey().equals("dependencies")) {
                Map<String, Object> dependencyMap = (Map<String, Object>) entry1.getValue();
                for (AbstractBaseMod mod1 : ModManager.mods) {
                    try {
                        ArrayList<String> modDependencies = (ArrayList<String>) dependencyMap.get(mod1.getExportType());
                        ArrayList<String> newModDependencies = new ArrayList<>();
                        for (String string1 : modDependencies) {
                            if (string1.equals(name)) {
                                newModDependencies.add(replacement);
                            } else {
                                newModDependencies.add(string1);
                            }
                        }
                        dependencyMap.replace(mod1.getExportType(), newModDependencies);
                    } catch (NullPointerException ignored) {

                    }
                }
            } else if (entry1.getValue().toString().contains(name)) {
                map.replace(entry1.getKey(), entry1.getValue().toString().replaceAll(name, replacement));
            }
        }
        for (Map.Entry<String, Object> entry2 : map.entrySet()) {
            LOGGER.info(entry2.getKey() + " | " + entry2.getValue());
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.firstPart") + " " + mod.getType() + " - " + name + " " + I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.secondPart") + " " + replacement);
    }

    /**
     * Checks if the import map contains the mod name under the mod_type
     * @param mods The map that contains the values
     * @param name The name that should be searched
     * @param mod_type The mod type in which the name should be searched
     * @return True if map contains mod under the specified type, false if mod is not found
     */
    private static boolean doesMapContainMod(Set<Map<String, Object>> mods, String name, String mod_type) {
        for (Map<String, Object> map : mods) {
            if (map.get("mod_name").equals(name) && map.get("mod_type").equals(mod_type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the map is compatible with the current mod tool version
     * @param map The map that contains the mod_type and mod_tool_version values
     * @return True if map is compatible, false if map is not compatible
     */
    private static boolean isModToolVersionSupported(Map<String, Object> map){
        return isModToolVersionSupported((String) map.get("mod_type"), (String) map.get("mod_tool_version"));
    }

    /**
     * Checks if the map is compatible with the current mod tool version
     * @param modType The type of the mod
     * @param modToolVersion The version that should be checked
     * @return True if map is compatible, false if map is not compatible
     */
    private static boolean isModToolVersionSupported(String modType, String modToolVersion) {
        for (AbstractBaseMod mod : ModManager.mods) {
            if (mod.getExportType().equals(modType)) {
                for (String string : mod.getCompatibleModToolVersions()) {
                    if (modToolVersion.equals(string)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the input mod already exists in the game.
     * @param modName The name of the mod for which it should be checked if it already exists
     * @param modType The mod type of which the mod is that should be checked
     * @return True if mod is already added, false if mod is not added
     */
    private static boolean doesModExist(String modName, String modType) throws ModProcessingException {
        for (AbstractBaseMod mod : ModManager.mods) {
            if (mod.getExportType().equals(modType)) {
                ArrayList<String> array = new ArrayList<>(Arrays.asList(mod.getContentByAlphabet()));
                if (array.contains(modName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a string that contains the required mod tool versions for the mod type.
     * Should be used together with {@link SharingManager#isModToolVersionSupported(Map)}.
     * @param modType The type of the mod
     * @return A string that contains the required mod tool versions
     */
    private static String[] getRequiredModToolVersion(String modType) {
        for (AbstractBaseMod mod : ModManager.mods) {
            if (mod.getExportType().equals(modType)) {
                return mod.getCompatibleModToolVersions();
            }
        }
        return new String[]{};
    }

    /**
     * Sets all import helper maps for each mod.
     * First the base map is initialized and then the mods that stand in the map are added.
     * When this function has run the import helper map will contain the mod ids for the new mods.
     * @param mods Contains all the mods that will be imported
     */
    private static void setImportHelperMaps(Set<Map<String, Object>> mods) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.settingNewModIds"));
        ProgressBarHelper.initializeProgressBar(0, ModManager.mods.size(), I18n.INSTANCE.get("textArea.importAll.settingNewModIds"));
        for (AbstractBaseMod mod : ModManager.mods) {
            mod.initializeImportHelperMap();
            for (Map<String, Object> map : mods) {
                if (map.get("mod_type").equals(mod.getExportType())) {
                    mod.addEntryToImportHelperMap(map.get("mod_name").toString());
                }
            }
            ProgressBarHelper.increment();
        }
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Imports all mods that are stored in the mods set.
     * The progress bar and text area are used to display progress.
     * This function should only be called by {@link SharingManager#importAll(ImportType, Set)}.
     * @param mods A set of maps that contain the values for the mods that should be imported.
     * @throws ModProcessingException If something went wrong while importing a mod
     */
    private static void importAllMods(Set<Map<String, Object>> mods) throws ModProcessingException {
        ProgressBarHelper.initializeProgressBar(0, mods.size(), I18n.INSTANCE.get("progressBar.importingMods"));
        for (AbstractBaseMod mod : ModManager.mods) {
            for (Map<String, Object> map : mods) {
                if (map.get("mod_type").equals(mod.getExportType())) {
                    try {
                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + mod.getType());
                        mod.importMod(map);
                        ProgressBarHelper.increment();
                    } catch (NullPointerException e) {
                        throw new ModProcessingException("Critical error while importing mod: " + mod.getType() + " - " + map.get("mod_name"), e);
                    }
                }
            }
        }
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     * @deprecated Use {@link SharingManager#importAll(ImportType, Set<Path>)} instead.
     */
    @Deprecated
    public static void importAllOld() {
        importAllOld(false, Paths.get(""));
    }

    /**
     * Opens a gui where the user can select folders where import files are located. When all folders and subfolders are scanned a summary is shown of what can be imported.
     * It is then possible to import everything at once.
     * @param importFromRestorePoint True when this function is called from import point restore. Will change some messages.
     * @param folderPath If not empty, this folder will be used as root folder where the search is started. This will prevent the message to the user that folders should be selected.
     * @deprecated Use {@link SharingManager#importAll(ImportType, Set<Path>)} instead.
     */
    @Deprecated
    public static void importAllOld(boolean importFromRestorePoint, Path folderPath){
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.start"));
        ArrayList<File> directories;
        if(folderPath.toString().isEmpty()){
            LOGGER.info("Opening window where the user can select the folders that should be searched for imports");
            directories = getImportFolderPathOld("import", true);;
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
     * Adds gui components to be displayed in the summary. May only be called by {@link SharingManager#importAllOld()}
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
    @Deprecated
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
        LOGGER.info("exporting mod: " + name);//TODO Fix that an assets folder is only generated if pictures exist
        Path path = folder.resolve(mod.getExportType());
        String fileName = mod.getExportType() + "_" + Utils.convertName(name) + ".toml";
        if (!Files.exists(path.resolve(fileName)) && !Files.exists(path.resolve(Utils.convertName(name) + "/" + fileName))) {
            try {
                Files.createDirectories(path);
                Map<String, Object> map = mod.getExportMap(name);
                map.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
                map.put("type", ExportType.ALL_SINGLE.getTypeName());
                map.put("mod_type", mod.getExportType());
                if (mod instanceof AbstractSimpleMod) {
                    map.put("base_mod_type", "simple");
                } else if (mod instanceof AbstractAdvancedMod) {
                    map.put("base_mod_type", "advanced");
                }
                TomlWriter tomlWriter = new TomlWriter();
                if (mod instanceof AbstractComplexMod) {
                    Path singleMod = path.resolve(Utils.convertName(name));
                    Path assets = singleMod.resolve("assets");
                    Files.createDirectories(assets);
                    map.putAll(((AbstractComplexMod)mod).exportImages(name, assets));
                    tomlWriter.write(map, singleMod.resolve(fileName).toFile());
                } else {
                    tomlWriter.write(map, path.resolve(fileName).toFile());
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
                        Map<String, Object> singleModMap = mod.getExportMap(string);
                        if (mod instanceof AbstractComplexMod) {//This will add the image name(s) to the map if required and copy the image files
                            singleModMap.putAll(((AbstractComplexMod)mod).exportImages(string, path.resolve("assets")));
                        }
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.addingEntry") + ": " + mod.getType() + " - " + string);
                        modMap.put(Utils.convertName(string), singleModMap);
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
