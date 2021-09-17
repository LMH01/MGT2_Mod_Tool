package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);

    /**
     * Opens a window where the user can select folders that should be searched for mods.
     *
     * @see SharingManager#importAll(ImportType, Set)
     */
    public static void importAll(ImportType importType) throws ModProcessingException {
        Set<Path> paths = getImportFolders();
        if (paths != null) {
            importAll(importType, paths);
        }
    }

    /**
     * Searches the path for mods in .toml files.
     * When the search is completed a message is displayed to the user where it can be selected which mod should be imported.
     * When mods are searched it is analyzed if they depend on any other mods and if these mod do exist.
     *
     * @param importType The type of the import.
     * @param path       The root folder where the search for mods should be started at. Use {@link SharingManager#importAll(ImportType, Set)} if multiple root folders should be searched.
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
     *
     * @param importType The type of the import.
     * @param paths      The root folders where the search for mods should be started at.
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
                    JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part1") + ": " + modMaps.size() + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part2"));
                    JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("textArea.importAll.checkBox.customizeImport"));
                    checkBox.setToolTipText(I18n.INSTANCE.get("textArea.importAll.checkBox.customizeImport.toolTip"));
                    JComponent[] components = {label, checkBox};
                    boolean continueImport = false;
                    if (importType.equals(ImportType.RESTORE_POINT) || importType.equals(ImportType.REAL_PUBLISHERS)) {
                        continueImport = true;
                    } else {
                        if (JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("frame.title.import"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            continueImport = true;
                        }
                    }
                    if (continueImport) {
                        Set<Map<String, Object>> importMap;
                        if (checkBox.isSelected()) {
                            importMap = getCustomizedImportMap(importType, modMaps);
                        } else {
                            importMap = modMaps;
                        }
                        if (!importMap.isEmpty()) {
                            if (checkAssetsFolders(importMap)) {
                                Set<Map<String, Object>> modsChecked = checkDependencies(importMap);
                                if (modsChecked != null) {
                                    setImportHelperMaps(modsChecked);
                                    if (importAllMods(modsChecked)) {
                                        if (importType.equals(ImportType.RESTORE_POINT)) {
                                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.restoreSuccessful"));
                                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored"), I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored.title"), JOptionPane.INFORMATION_MESSAGE);
                                        } else {
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
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.assetFoldersMissing"));
                            }
                        } else {
                            if (importType.equals(ImportType.RESTORE_POINT)) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
                            } else {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                            }
                        }
                    } else {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
                    }
                } else {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.noUniqueModsFound"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2"), I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2"), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.noCompatibleTomlFilesFound"));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2"), I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2"), JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            ThreadHandler.startModThread(() -> JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.INFORMATION_MESSAGE), "showNoTomlFoundInformation");
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"));
        }
        if (ModManagerPaths.TEMP.toFile().exists()) {
            ThreadHandler.startThread(ThreadHandler.runnableDeleteTempFolder, "runnableDeleteTempFolder");
        }
    }

    /**
     * Opens a gui to the user where they can select what mods should be added to the game.
     * The mods that can be chosen are placed in the set.
     *
     * @param set The set where the mods are placed in
     * @return A new set that contains only the mods that have been selected by the user
     */
    private static Set<Map<String, Object>> getCustomizedImportMap(ImportType importType, Set<Map<String, Object>> set) {
        Map<AbstractBaseMod, JPanel> importModPanels = new HashMap<>();
        Map<AbstractBaseMod, AtomicBoolean> enabledMods = new HashMap<>();
        Map<AbstractBaseMod, AtomicReference<Set<String>>> selectedMods = new HashMap<>();
        for (AbstractBaseMod mod : ModManager.mods) {
            Set<String> modNames = new HashSet<>();
            for (Map<String, Object> map : set) {
                if (map.get("mod_type").equals(mod.getExportType())) {
                    modNames.add(map.get("mod_name").toString());
                }
            }
            selectedMods.put(mod, new AtomicReference<>(modNames));
            enabledMods.put(mod, new AtomicBoolean(true));
            importModPanels.put(mod, new JPanel());
        }
        for (AbstractBaseMod mod : ModManager.mods) {
            if (!selectedMods.get(mod).get().isEmpty()) {
                setFeatureAvailableGuiComponents(mod.getType(), selectedMods.get(mod).get(), importModPanels.get(mod), selectedMods.get(mod), enabledMods.get(mod), I18n.INSTANCE.get("frame.title.import"));
            }
        }
        String labelStartText;
        String labelEndText;
        if (importType.equals(ImportType.RESTORE_POINT)) {
            labelStartText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.startText.var1");
            labelEndText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.endText.var1");
        } else {
            labelStartText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.startText.var2");
            labelEndText = I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.endText.var2");
        }
        JLabel labelStart = new JLabel(labelStartText);
        JLabel labelEnd = new JLabel(labelEndText);
        Object[] params;
        ArrayList<JPanel> panels = new ArrayList<>();
        for (Map.Entry<AbstractBaseMod, JPanel> entry : importModPanels.entrySet()) {
            panels.add(entry.getValue());
        }
        Object[] modPanels = panels.toArray(new Object[0]);
        params = new Object[]{labelStart, modPanels, labelEnd};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.sharingManager.importAll.importReady.message.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Set<Map<String, Object>> modMap = new HashSet<>();
            for (AbstractBaseMod mod : ModManager.mods) {
                for (Map<String, Object> map : set) {
                    for (String string : selectedMods.get(mod).get()) {
                        if (map.get("mod_name").equals(string)) {
                            modMap.add(map);
                        }
                    }
                }
            }
            return modMap;
        } else {
            return new HashSet<>();
        }
    }

    /**
     * This function will prompt the user to choose a folder where the files to import are located
     *
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
                    if (f.getName().contains(".zip")) {
                        return true;
                    }
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return I18n.INSTANCE.get("dialog.sharingManager.getImportFolderPath.fileChooser.filterDescription");
                }
            };
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(fileFilter);
            int return_value = fileChooser.showOpenDialog(null);
            if (return_value == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                Set<Path> importFolders = new HashSet<>();
                for (int i = 0; i < fileChooser.getSelectedFiles().length; i++) {
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
     * Searches all folders in the path for toml files.
     * Uses the text area and the progress bar to display progress.
     * If a .zip folder is found it can be unzipped and searched for mods.
     *
     * @return An array list containing the toml files that have been found.
     * If the import has been canceled the array map will be empty.
     */
    private static ArrayList<File> getTomlFiles(Set<Path> paths) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingForTomlFiles"));
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS, true);
        ArrayList<File> tomlFiles = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.scanningDirectories"), false, false);
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
        LOGGER.info("Search for .toml files complete. Took " + timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " ms!");
        return tomlFiles;
    }

    /**
     * Searches all folders in the path for .toml files and returns a set of toml files
     */
    private static Set<Path> getTomlFiles(Path path, AtomicBoolean abortImport, JCheckBox checkBoxPreventZipMessage, AtomicBoolean unzipAutomatic, AtomicInteger currentZipArchiveNumber) throws ModProcessingException {
        Set<Path> tomlFiles = new HashSet<>();
        if (!abortImport.get()) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (!abortImport.get()) {
                            if (file.toFile().getName().endsWith(".toml")) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.foundTomlFile") + ": " + file);
                                tomlFiles.add(file);
                            } else if (file.toFile().getName().endsWith(".zip")) {
                                TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + " " + file);
                                boolean unzipFile = false;
                                if (!checkBoxPreventZipMessage.isSelected()) {
                                    JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.firstPart") + "<br><br>" + file + "<br><br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.secondPart"));
                                    Object[] obj = {label, checkBoxPreventZipMessage};
                                    if (JOptionPane.showConfirmDialog(null, obj, I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                        unzipFile = true;
                                        if (checkBoxPreventZipMessage.isSelected()) {
                                            unzipAutomatic.set(true);
                                        }
                                    }
                                } else {
                                    if (unzipAutomatic.get()) {
                                        unzipFile = true;
                                    }
                                }
                                if (unzipFile) {
                                    try {
                                        Path extractedFolder = ModManagerPaths.TEMP.getPath().resolve(Integer.toString(currentZipArchiveNumber.get()));
                                        DataStreamHelper.unzip(file, extractedFolder);
                                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.scanningDirectories"));
                                        currentZipArchiveNumber.getAndIncrement();
                                        tomlFiles.addAll(getTomlFiles(extractedFolder, abortImport, checkBoxPreventZipMessage, unzipAutomatic, currentZipArchiveNumber));
                                    } catch (IOException | ModProcessingException | IllegalArgumentException e) {
                                        TextAreaHelper.printStackTrace(e);
                                        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.firstPart") + "\n\n" + file + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage() + "\n\n" + I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.message.secondPart"), I18n.INSTANCE.get("dialog.sharingManager.importAll.zipArchiveFound.error.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
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
     *
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
     *
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
     *
     * @param singleMods  An array list that contains all toml instances that are verified to contain a single mod.
     *                    This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}.
     * @param bundledMods An array list that contains all toml instances that are verified to contain bundled mods
     *                    This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}
     * @return A set of maps that contain the mod data.
     * If no compatible mods are found an empty map is returned.
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> getImportMaps(ArrayList<Map<String, Object>> singleMods, ArrayList<Map<String, Object>> bundledMods) throws ModProcessingException {
        ProgressBarHelper.initializeProgressBar(0, singleMods.size() + bundledMods.size(), I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS, true);
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
        for (Map<String, Object> map : singleMods) {
            if (map.containsKey("\"NAME EN\"")) {
                DebugHelper.debug(LOGGER, "single mod instance found!: " + map.get("\"NAME EN\""));
            } else {
                DebugHelper.debug(LOGGER, "single mod instance found!: " + map.get("line"));
            }
            if (isModToolVersionSupported(map)) {
                if (map.get("base_mod_type").equals("simple") || map.get("base_mod_type").equals("advanced") || map.get("base_mod_type").equals("complex")) {
                    if (doesMapContainMod(mods, map.get("mod_name").toString(), map.get("mod_type").toString())) {
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
            DebugHelper.debug(LOGGER, "bundled mod instance found!");
            try {
                Map<String, Object> simple = (Map<String, Object>) map.get("simple_mods");
                Map<String, Object> advanced = (Map<String, Object>) map.get("advanced_mods");
                for (AbstractBaseMod mod : ModManager.mods) {
                    Map<String, Object> modMap = new HashMap<>();
                    try {
                        modMap.putAll((Map<String, Object>) simple.get(mod.getExportType()));
                    } catch (NullPointerException e) {
                        DebugHelper.debug(LOGGER, "simpleModMap is null; Export type: " + mod.getExportType());
                    }
                    try {
                        modMap.putAll((Map<String, Object>) advanced.get(mod.getExportType()));
                    } catch (NullPointerException e) {
                        DebugHelper.debug(LOGGER, "advancedModMap is null; Export type: " + mod.getExportType());
                    }
                    ProgressBarHelper.increaseMaxValue(modMap.entrySet().size());
                    for (Map.Entry<String, Object> entry : modMap.entrySet()) {
                        Map<String, Object> singleModMap = (Map<String, Object>) entry.getValue();
                        if (isModToolVersionSupported(mod.getExportType(), (String) map.get("mod_tool_version"))) {
                            if (mod instanceof  AbstractComplexMod) {
                                singleModMap.put("base_mod_type", "complex");
                            } else if (mod instanceof AbstractAdvancedMod) {
                                singleModMap.put("base_mod_type", "advanced");
                            }
                            if (mod instanceof AbstractSimpleMod) {
                                singleModMap.put("base_mod_type", "simple");
                            }
                            singleModMap.put("mod_type", mod.getExportType());
                            singleModMap.put("assets_folder", map.get("assets_folder"));
                            if (doesMapContainMod(mods, singleModMap.get("mod_name").toString(), mod.getExportType())) {
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
        LOGGER.info("Search for mods complete. Found " + modsTotal + " mods after " + timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " ms!");
        if (uniqueMods > 0) {
            return mods;
        }
        return new HashSet<>();
    }

    /**
     * Removes the quote symbol from the maps keys.
     *
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
     * Checks that all the assets folders of the mods exist.
     * If a folder is missing a message is displayed where the missing folders are listed and where the user is notified that the import
     * can not be continued.
     * When the safety features are off the assets' folder check is skipped
     *
     * @param mods The map that contains the mods
     * @return True if all folders exist or when the safety features are disabled.
     *         False if a folder is missing and the safety features are enabled.
     */
    private static boolean checkAssetsFolders(Set<Map<String, Object>> mods) {
        List<String> invalidAssetFolders = new ArrayList<>();
        for (Map<String, Object> map : mods) {
            Path path = Paths.get(map.get("assets_folder").toString());
            if (!Files.exists(path) && map.get("base_mod_type").equals("complex")) {
                if (!invalidAssetFolders.contains(path.toString())) {
                    invalidAssetFolders.add(path.toString());
                }
            }
        }
        if (!invalidAssetFolders.isEmpty() && !Settings.disableSafetyFeatures) {
            JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + "<br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.assetsFoldersMissing"));
            String[] string = new String[invalidAssetFolders.size()];
            invalidAssetFolders.toArray(string);
            JList<String> list = WindowHelper.getList(string, false);
            JScrollPane scrollPane = WindowHelper.getScrollPane(list);
            JComponent[] components = {label, scrollPane};
            JOptionPane.showMessageDialog(null, components, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (Settings.disableSafetyFeatures) {
                DebugHelper.warn(LOGGER, "Check for asset folders is disabled");
            }
            return true;
        }
    }

    /**
     * Checks the mods contained in the set for dependencies and returns a set that is ready to be imported.
     * If a dependency is not found the user is prompted to select a replacement. If no replacement is selected a random replacement will be chosen.
     * The function that replaces the dependency is {@link SharingManager#replaceDependencies(AbstractBaseMod, Map, AbstractBaseMod, String)}
     *
     * @param mods The map where the mods are stored in
     * @return A set of maps that contain the mod data. The data is checked for dependencies. Returns null if dependency check has been canceled by the user
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> checkDependencies(Set<Map<String, Object>> mods) throws ModProcessingException {
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        ProgressBarHelper.initializeProgressBar(0, mods.size(), I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        Map<AbstractBaseMod, Map<String, String>> alreadyReplacedDependencies = new HashMap<>();
        boolean showMissingDependencyDialog = true;
        for (Map<String, Object> parentMap : mods) {
            for (AbstractBaseMod parent : ModManager.mods) {
                if (parent.getExportType().equals(parentMap.get("mod_type").toString())) {
                    if (parent instanceof DependentMod) {//The parent is the mod the map belongs to
                        Map<String, Object> dependencies = (Map<String, Object>) parentMap.get("dependencies");
                        if (dependencies != null) {
                            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                                for (AbstractBaseMod child : ModManager.mods) {//The child is the mod that should be replaced in the parent map
                                    if (entry.getKey().equals(child.getExportType()) && ((DependentMod) parent).getDependencies().contains(child)) {
                                        DebugHelper.debug(LOGGER, I18n.INSTANCE.get("textArea.importAll.requiresDependencies") + ": " + parentMap.get("mod_type") + " - " + parentMap.get("mod_name") + " - " + parentMap.get("dependencies"));
                                        ArrayList<String> arrayList = (ArrayList<String>) entry.getValue();
                                        for (String childName : arrayList) {
                                            if (!doesModExist(childName, child.getExportType()) && !doesMapContainMod(mods, childName, child.getExportType())) {
                                                String replacement = getReplacedDependency(alreadyReplacedDependencies, childName, child);
                                                if (replacement != null) {
                                                    replaceDependencies((AbstractBaseMod & DependentMod) parent, parentMap, child, childName, replacement);
                                                } else {
                                                    JLabel label1 = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part1") + ":<br><br>" + child.getType(true) + " - " + childName + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part2"));
                                                    JList<String> list = WindowHelper.getList(child.getContentByAlphabet(), false);
                                                    JScrollPane scrollPane = WindowHelper.getScrollPane(list);
                                                    JLabel label2 = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part3"));
                                                    JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.checkBox"));
                                                    checkBox.setToolTipText(I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.checkBox.toolTip"));
                                                    JComponent[] components = {label1, scrollPane, label2, checkBox};
                                                    int returnValue = JOptionPane.OK_OPTION;
                                                    if (showMissingDependencyDialog) {
                                                        returnValue = JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("frame.title.missingDependency"), JOptionPane.OK_CANCEL_OPTION);
                                                        showMissingDependencyDialog = !checkBox.isSelected();
                                                    }
                                                    if (returnValue == JOptionPane.OK_OPTION) {
                                                        if (list.isSelectionEmpty()) {
                                                            setReplacedDependency(alreadyReplacedDependencies, child, childName, replaceDependencies((AbstractBaseMod & DependentMod) parent, parentMap, child, childName));
                                                        } else {
                                                            replaceDependencies((AbstractBaseMod & DependentMod) parent, parentMap, child, childName, list.getSelectedValue());
                                                            setReplacedDependency(alreadyReplacedDependencies, child, childName, list.getSelectedValue());
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
                        } else {
                            DebugHelper.warn(LOGGER, "dependency map of " + parent.getType() + " - " + parentMap.get("mod_name") + " does not exist");
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
     * Adds en entry to the replacement map.
     *
     * @param replacementMap The map that contains the replacements
     * @param child          The name of the mod for which the replaced dependency should be set
     * @param childName      The dependency name for which an entry should be added to the map
     * @param replacement    The name with which the missing dependency should be replaced
     */
    private static void setReplacedDependency(Map<AbstractBaseMod, Map<String, String>> replacementMap, AbstractBaseMod child, String childName, String replacement) {
        if (replacementMap.containsKey(child)) {
            LOGGER.info("replacement map already contains mod: " + child.getType() + "; " + childName + " -> " + replacement);
            Map<String, String> map = replacementMap.get(child);
            map.put(childName, replacement);
        } else {
            LOGGER.info("replacement map does not contain mod: " + child.getType() + "; " + childName + " -> " + replacement);
            Map<String, String> map = new HashMap<>();
            map.put(childName, replacement);
            replacementMap.put(child, map);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.firstPart") + " " + child.getType(true) + " - " + childName + " " + I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.secondPart") + " " + replacement);
    }

    /**
     * Searches the replacement map for the parentName.
     * If the name is found the replacement is returned.
     *
     * @return If the name is found: the replacement. If the name is not found null.
     * @see SharingManager#setReplacedDependency(Map, AbstractBaseMod, String, String)  Parameters
     */
    private static String getReplacedDependency(Map<AbstractBaseMod, Map<String, String>> replacementMap, String childName, AbstractBaseMod child) {
        for (Map.Entry<AbstractBaseMod, Map<String, String>> entry : replacementMap.entrySet()) {
            if (entry.getKey().equals(child)) {
                Map<String, String> map = entry.getValue();
                LOGGER.info("returning replaced dependency for mod " + child.getType() + ": " + childName + " -> " + map.getOrDefault(childName, "not yet set"));
                return map.getOrDefault(childName, null);
            }
        }
        return null;
    }

    /**
     * Replaces the dependency in the map with a random one
     *
     * @return The replacement
     * @see SharingManager#replaceDependencies(AbstractBaseMod, Map, AbstractBaseMod, String, String)  Parameters
     */
    @SuppressWarnings("unchecked")
    private static <T extends AbstractBaseMod & DependentMod> String replaceDependencies(T parent, Map<String, Object> parentMap, AbstractBaseMod child, String childName) throws ModProcessingException {
        String replacement = child.getContentByAlphabet()[Utils.getRandomNumber(0, child.getContentByAlphabet().length)];
        replaceDependencies(parent, parentMap, child, childName, replacement);
        return replacement;
    }

    /**
     * Replaces the dependency in the whole map
     *
     * @param parent      The mod the dependency belongs to that should be replaced
     * @param parentMap   The mod map where the dependency should be replaced in
     * @param child       The mod that should be replaced in the parent map
     * @param childName   The name of the mod that should be replaced
     * @param replacement The name with which the missing dependency should be replaced
     * @param <T>         An abstract base mod that needs dependencies
     * @throws ModProcessingException
     * @see SharingManager#replaceDependencyInMap(AbstractBaseMod, Map, String, String)
     * @see SharingManager#replaceDependencyInDependencyMap(Map, AbstractBaseMod, String, String)
     */
    private static <T extends AbstractBaseMod & DependentMod> void replaceDependencies(T parent, Map<String, Object> parentMap, AbstractBaseMod child, String childName, String replacement) throws ModProcessingException {
        replaceDependencyInMap(parent, parentMap, childName, replacement);
        replaceDependencyInDependencyMap(parentMap, child, childName, replacement);
    }

    static int timesRan = 0;

    /**
     * Replaces the dependency name in the map with the replacement
     *
     * @see SharingManager#replaceDependencies(AbstractBaseMod, Map, AbstractBaseMod, String, String)  Parameters
     * @see DependentMod#replaceMissingDependency(Map, String, String) How the enries are replaced
     */
    private static <T extends AbstractBaseMod & DependentMod> void replaceDependencyInMap(T parent, Map<String, Object> parentMap, String childName, String replacement) throws ModProcessingException {
        LOGGER.info("replacing missing dependencies for mod " + parent.getExportType());
        parent.replaceMissingDependency(parentMap, childName, replacement);
    }

    /**
     * Replaces the dependency in the dependency map of the mod with the selected replacement
     *
     * @see SharingManager#replaceDependencies(AbstractBaseMod, Map, AbstractBaseMod, String, String) Parameters
     */
    @SuppressWarnings("unchecked")
    private static void replaceDependencyInDependencyMap(Map<String, Object> parentMap, AbstractBaseMod child, String childName, String replacement) {
        LOGGER.info("replacing dependency map");
        if (parentMap.containsKey("dependencies")) {
            Map<String, Object> dependencyMap = (Map<String, Object>) parentMap.get("dependencies");
            try {
                ArrayList<String> modDependencies = (ArrayList<String>) dependencyMap.get(child.getExportType());
                ArrayList<String> newModDependencies = new ArrayList<>();
                for (String string : modDependencies) {
                    if (string.equals(childName)) {
                        if (!modDependencies.contains(replacement)) {
                            newModDependencies.add(replacement);
                        }
                    } else {
                        newModDependencies.add(string);
                    }
                }
                dependencyMap.replace(child.getExportType(), newModDependencies);
            } catch (NullPointerException ignored) {

            }
        }
    }

    /**
     * Checks if the import map contains the mod name under the mod_type
     *
     * @param mods     The map that contains the values
     * @param name     The name that should be searched
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
     *
     * @param map The map that contains the mod_type and mod_tool_version values
     * @return True if map is compatible, false if map is not compatible
     */
    private static boolean isModToolVersionSupported(Map<String, Object> map) {
        return isModToolVersionSupported((String) map.get("mod_type"), (String) map.get("mod_tool_version"));
    }

    /**
     * Checks if the map is compatible with the current mod tool version
     *
     * @param modType        The type of the mod
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
     *
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
     *
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
     *
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
     *
     * @param mods A set of maps that contain the values for the mods that should be imported.
     * @return True if import was successful. False if import was canceled.
     * @throws ModProcessingException If something went wrong while importing a mod
     */
    private static boolean importAllMods(Set<Map<String, Object>> mods) throws ModProcessingException {
        ProgressBarHelper.initializeProgressBar(0, mods.size(), I18n.INSTANCE.get("progressBar.importingMods"));
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS, true);
        timeHelper.measureTime();
        for (AbstractBaseMod mod : ModManager.mods) {
            for (Map<String, Object> map : mods) {
                if (map.get("mod_type").equals(mod.getExportType())) {
                    try {
                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + mod.getType());
                        mod.importMod(map);
                        ProgressBarHelper.increment();
                    } catch (ModProcessingException e) {
                        TextAreaHelper.printStackTrace(e);
                        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.errorWhileImportingMod.firstPart") + ": " + mod.getType(true) + " - " + map.get("mod_name") + "<br>" + I18n.INSTANCE.get("commonText.reason") + " " + e.getMessage() + "<br><br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.errorWhileImportingMod.secondPart"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                            return false;
                        }
                    }
                }
            }
        }
        ProgressBarHelper.resetProgressBar();
        LOGGER.info("Import completed after " + timeHelper.getMeasuredTime(TimeUnit.MILLISECONDS) + " ms!");
        return true;
    }

    /**
     * Adds gui components to be displayed in the summary.
     *
     * @param labelText       The label text
     * @param set             The set that contains the mod names of the specific mod
     * @param panel           The panel where the components should be added
     * @param selectedEntries An atomic reference where the return values should be saved
     * @param frameTitle      The title that should be displayed when the button is pressed
     */
    private static void setFeatureAvailableGuiComponents(String labelText, Set<String> set, JPanel panel, AtomicReference<Set<String>> selectedEntries, AtomicBoolean disableImport, String frameTitle) {
        JLabel label = new JLabel(labelText);
        JButton button = new JButton(set.size() + "/" + set.size());
        disableImport.set(false);
        button.addActionListener(actionEvent -> {
            Set<String> selectedMods = Utils.getSelectedEntries(I18n.INSTANCE.get("dialog.sharingManager.selectImports"), frameTitle, Utils.convertArrayListToArray(new ArrayList<>(set)), Utils.convertArrayListToArray(new ArrayList<>(set)));
            if (selectedMods != null) {
                selectedEntries.set(selectedMods);
                if (selectedMods.isEmpty()) {
                    LOGGER.info("Import/export disabled for: " + labelText.replaceAll(":", ""));
                    disableImport.set(true);
                } else {
                    LOGGER.info("Import/export enabled for: " + labelText.replaceAll(":", ""));
                    disableImport.set(false);
                }
                button.setText(selectedEntries.get().size() + "/" + set.size());
            }
        });
        panel.add(label);
        panel.add(button);
    }

    /**
     * Exports the specified mod to the export folder.
     * Writes a message to the text area if mod export was successful or if mod was already exported
     *
     * @param mod    The mod_type the mod belongs to
     * @param name   The name of the mod that should be exported
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
                if (mod instanceof AbstractSimpleMod) {
                    map.put("base_mod_type", "simple");
                } else if (mod instanceof AbstractComplexMod) {
                    map.put("base_mod_type", "complex");
                } else if (mod instanceof AbstractAdvancedMod) {
                    map.put("base_mod_type", "advanced");
                }
                TomlWriter tomlWriter = new TomlWriter();
                if (mod instanceof AbstractComplexMod) {
                    Path singleMod = path.resolve(Utils.convertName(name));
                    Path assets = singleMod.resolve("assets");
                    Files.createDirectories(assets);
                    map.putAll(((AbstractComplexMod) mod).exportImages(name, assets));
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
     *
     * @param exportType Determines how and to where the mods should be exported
     * @throws ModProcessingException If something went wrong while exporting mods
     */
    public static void export(ExportType exportType) throws ModProcessingException {
        export(exportType, null);
    }

    /**
     * If mods map is not null only the mods that are stored within that map are exported.
     * If export type is anything else all mods will be exported.
     * The text area and the progress bar are used to display progress.
     *
     * @param exportType Determines how and to where the mods should be exported
     * @throws ModProcessingException If something went wrong while exporting mods
     */
    public static void export(ExportType exportType, Map<AbstractBaseMod, Set<String>> mods) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper(TimeUnit.MILLISECONDS, true);
        timeHelper.measureTime();
        if (exportType.equals(ExportType.ALL_SINGLE)) {
            Path exportFolder;
            if (Settings.enableExportStorage) {
                exportFolder = ModManagerPaths.EXPORT.getPath().resolve("single/" + Utils.getCurrentDateTime());
            } else {
                exportFolder = ModManagerPaths.EXPORT.getPath().resolve("single/");
                if (Files.exists(exportFolder)) {
                    try {
                        DataStreamHelper.deleteDirectory(exportFolder);
                    } catch (IOException e) {
                        throw new ModProcessingException("Unable to delete export folder", e);
                    }
                }
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.all_single.started"));
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("commonText.exporting"));
            for (AbstractBaseMod mod : ModManager.mods) {
                ProgressBarHelper.setText(I18n.INSTANCE.get("commonText.exporting") + " " + mod.getType());
                if (mods != null) {
                    ProgressBarHelper.increaseMaxValue(mods.get(mod).size());
                    for (String string : mods.get(mod)) {
                        exportSingleMod(mod, string, exportFolder);
                        ProgressBarHelper.increment();
                    }
                } else {
                    ProgressBarHelper.increaseMaxValue(mod.getCustomContentString().length);
                    for (String string : mod.getCustomContentString()) {
                        exportSingleMod(mod, string, exportFolder);
                        ProgressBarHelper.increment();
                    }
                }
            }
        } else {//TODO check that getContentByAlphabet is replaced with getCustomContentString in release version
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.export.creatingExportMap"));
            Path path;
            Map<String, Object> map = new HashMap<>();
            String tomlName;
            map.put("type", exportType.getTypeName());
            if (exportType.equals(ExportType.RESTORE_POINT)) {
                path = ModManagerPaths.CURRENT_RESTORE_POINT.getPath();
                tomlName = "restore_point";
            } else {
                if (Settings.enableExportStorage) {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/" + Utils.getCurrentDateTime());
                } else {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/");
                    if (Files.exists(path)) {
                        try {
                            DataStreamHelper.deleteDirectory(path);
                        } catch (IOException e) {
                            throw new ModProcessingException("Unable to delete export folder", e);
                        }
                    }
                }
                tomlName = "export";
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.starting"));
            try {
                TomlWriter tomlWriter = new TomlWriter();
                Map<String, Object> simpleMods = new HashMap<>();
                Map<String, Object> advancedMods = new HashMap<>();
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.creatingExportMap"));
                for (AbstractBaseMod mod : ModManager.mods) {
                    Map<String, Object> modMap = new HashMap<>();
                    ProgressBarHelper.increaseMaxValue(mod.getCustomContentString().length);
                    String[] strings;
                    if (mods != null) {
                        strings = mods.get(mod).toArray(new String[0]);
                    } else {
                        strings = mod.getCustomContentString();
                    }
                    for (String string : strings) {
                        Map<String, Object> singleModMap = mod.getExportMap(string);
                        if (mod instanceof AbstractComplexMod) {//This will add the image name(s) to the map if required and copy the image files
                            if (!Files.exists(path.resolve("assets"))) {
                                Files.createDirectories(path.resolve("assets"));
                                Files.createDirectories(path);
                            }
                            singleModMap.putAll(((AbstractComplexMod) mod).exportImages(string, path.resolve("assets")));
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
                    Path tomlFile = path.resolve(tomlName + ".toml");
                    if (Files.exists(tomlFile)) {
                        Files.delete(tomlFile);
                    }
                    Files.createDirectories(tomlFile.getParent());
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
     * Opens a window where the user can select what mods should be exported.
     * The selected mods are then exported
     *
     * @see SharingManager#export(ExportType, Map) will be used to write the export files.
     */
    public static void exportSelected() throws ModProcessingException {
        Map<AbstractBaseMod, JPanel> importModPanels = new HashMap<>();
        Map<AbstractBaseMod, AtomicBoolean> enabledMods = new HashMap<>();
        Map<AbstractBaseMod, AtomicReference<Set<String>>> selectedMods = new HashMap<>();
        for (AbstractBaseMod mod : ModManager.mods) {
            Set<String> modNames = new HashSet<>(Arrays.asList(mod.getCustomContentString()));
            selectedMods.put(mod, new AtomicReference<>(modNames));
            enabledMods.put(mod, new AtomicBoolean(true));
            importModPanels.put(mod, new JPanel());
        }
        for (AbstractBaseMod mod : ModManager.mods) {
            if (!selectedMods.get(mod).get().isEmpty()) {
                setFeatureAvailableGuiComponents(mod.getType(), selectedMods.get(mod).get(), importModPanels.get(mod), selectedMods.get(mod), enabledMods.get(mod), I18n.INSTANCE.get("frame.title.export"));
            }
        }
        JLabel labelStart = new JLabel(I18n.INSTANCE.get("dialog.sharingManager.exportAll.summary.startText"));
        JLabel labelEnd = new JLabel(I18n.INSTANCE.get("dialog.sharingManager.exportAll.summary.endText"));
        Object[] params;
        ArrayList<JPanel> panels = new ArrayList<>();
        for (Map.Entry<AbstractBaseMod, JPanel> entry : importModPanels.entrySet()) {
            panels.add(entry.getValue());
        }
        JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport"));
        checkBox.setToolTipText(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport.toolTip"));
        Object[] modPanels = panels.toArray(new Object[0]);
        params = new Object[]{labelStart, modPanels, labelEnd, checkBox};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.sharingManager.exportAll.exportReady.message.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Map<AbstractBaseMod, Set<String>> map = new HashMap<>();
            for (AbstractBaseMod mod : ModManager.mods) {
                Set<String> set = new HashSet<>(selectedMods.get(mod).get());
                map.put(mod, set);
            }
            if (checkBox.isSelected()) {
                export(ExportType.ALL_SINGLE, map);
            } else {
                export(ExportType.ALL_BUNDLED, map);
            }
            displayExportSuccessDialog();
        }
    }

    /**
     * Opens a JOptionPane where the user can decide how the mods should be exported: Single or bundled
     */
    public static void displayExportModsWindow() throws ModProcessingException {
        /*TODO Add another check box that reads: Customize export
        *   if selected the user can select which mods should be exported
        *   kinda the same way as its done when importing mods*/
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.exportAll.mainMessage.part1"));
        if (!Settings.enableExportStorage) {
            stringBuilder.append(I18n.INSTANCE.get("dialog.sharingManager.exportAll.mainMessage.part2"));
        }
        JLabel label = new JLabel(stringBuilder.toString());
        JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport"));
        checkBox.setToolTipText(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport.toolTip"));
        JComponent[] components = {label, checkBox};
        if (JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("commonText.export"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            if (checkBox.isSelected()) {
                export(ExportType.ALL_SINGLE);
            } else {
                export(ExportType.ALL_BUNDLED);
            }
            displayExportSuccessDialog();
        }
    }

    private static void displayExportSuccessDialog() throws ModProcessingException{
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.export.exportSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Desktop.getDesktop().open(ModManagerPaths.EXPORT.toFile());
            } catch (IOException e) {
                throw new ModProcessingException("Unable to open export folder", e);
            }
        }
    }
}
