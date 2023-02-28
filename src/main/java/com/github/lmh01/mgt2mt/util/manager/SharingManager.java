package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.manager.PublisherManager;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.*;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
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
import java.util.List;
import java.util.Map.Entry;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SharingManager {
    //This class contains functions with which it is easy to export/import things
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingManager.class);

    /**
     * Opens a window where the user can select folders that should be searched for mods.
     *
     * @param importType The type of the import.
     * @throws ModProcessingException When something went wrong.
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
     * @throws ModProcessingException When something went wrong.
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
     * @throws ModProcessingException When something went wrong.
     */
    public static void importAll(ImportType importType, Set<Path> paths) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper();
        CompanyLogoAnalyzer.analyzeLogoNumbers();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.start"));

        // Search for toml files
        ArrayList<File> tomlFiles = getTomlFiles(paths);
        // Check if toml files have been found
        if (tomlFiles.isEmpty()) {
            ThreadHandler.startModThread(() -> JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.INFORMATION_MESSAGE), "showNoTomlFoundInformation");
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.noTomlFilesFound"));
            return;
        }

        // Transform toml files to maps
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.totalTomlFilesFound") + ": " + tomlFiles.size());
        ArrayList<Map<String, Object>> modList = transformTomlFilesToMaps(tomlFiles);
        // Check if toml files where found that contain mods
        if (modList.isEmpty()) {
            return;
        }

        // Split the mod list into two lists containing only single/only bundled mods
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.foundCompatibleFiles") + ": " + modList.size());
        ArrayList<Map<String, Object>> singleMods = getFilteredModMaps(modList, false);
        ArrayList<Map<String, Object>> bundledMods = getFilteredModMaps(modList, true);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.singleMods") + ": " + singleMods.size());
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.bundledMods") + ": " + bundledMods.size());
        if (importType.equals(ImportType.RESTORE_POINT)) {
            Uninstaller.uninstallAllMods();
            ContentAdministrator.analyzeContents();
            CompanyLogoAnalyzer.analyzeLogoNumbers();
        }

        Set<Map<String, Object>> modMaps = getImportMaps(singleMods, bundledMods);
        // Check if mods are found
        if (modMaps.isEmpty()) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.noUniqueModsFound"));
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.error.noImportAvailable.var2"), I18n.INSTANCE.get("dialog.sharingManager.importAll.error.windowTitle.var2"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Check if some mods where found that replace default content
        int contentToReplace = countKeyOccurances(modMaps, "replaces");
        
        // Check if some mods where found that overwrite default content
        int amountContentToModify = countKeyOccurances(modMaps, "modifies");
        
        TextAreaHelper.appendText(I18n.INSTANCE.get("commonText.replace") + ": " + contentToReplace);
        TextAreaHelper.appendText(I18n.INSTANCE.get("commonText.modify") + ": " + amountContentToModify);
        TextAreaHelper.appendText(I18n.INSTANCE.get("commonText.add.upperCase") + ": " + (modMaps.size()-contentToReplace-amountContentToModify));

        // Display summary of what mods have been found
        JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part1") + ": " + modMaps.size() + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.modsFound.part2"));
        JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("textArea.importAll.checkBox.customizeImport"));
        checkBox.setToolTipText(I18n.INSTANCE.get("textArea.importAll.checkBox.customizeImport.toolTip"));

        JLabel label2 = new JLabel(String.format("<html><br>" + I18n.INSTANCE.get("textArea.importAll.replaceModsFound"), contentToReplace));
        JCheckBox allowReplacement = new JCheckBox(I18n.INSTANCE.get("textArea.importAll.replaceModsFound.checkBox"));
        allowReplacement.setToolTipText("<html>" + I18n.INSTANCE.get("textArea.importAll.replaceModsFound.checkBox.toolTip"));

        JLabel label3 = new JLabel(String.format("<html><br>" + I18n.INSTANCE.get("textArea.importAll.modificationsFound"), amountContentToModify));
        JCheckBox chkBxAllowModification = new JCheckBox(I18n.INSTANCE.get("textArea.importAll.modificationsFound.checkBox"));
        chkBxAllowModification.setToolTipText("<html>" + I18n.INSTANCE.get("textArea.importAll.modificationsFound.checkBox.toolTip"));

        JComponent[] components;
        if (contentToReplace > 0 && amountContentToModify > 0) {
            components = new JComponent[]{label, checkBox, label2, allowReplacement, label3, chkBxAllowModification};
        } else if (contentToReplace > 0 && amountContentToModify == 0){
            components = new JComponent[]{label, checkBox, label2, allowReplacement};
        } else if (contentToReplace == 0 && amountContentToModify > 0) {
            components = new JComponent[]{label, checkBox, label3, chkBxAllowModification};
        } else {
            components = new JComponent[]{label, checkBox};
        }
        boolean continueImport = false;
        if (importType.equals(ImportType.RESTORE_POINT) || importType.equals(ImportType.REAL_PUBLISHERS)) {
            continueImport = true;
        } else {
            if (JOptionPane.showConfirmDialog(null, components, I18n.INSTANCE.get("frame.title.import"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                continueImport = true;
            }
        }
        // Continue import only when the user accepts it or when a restore point is restored;
        if (!continueImport) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
            return;
        }

        // Ask the user which mods that have been found should be imported
        Set<Map<String, Object>> importMap;
        if (checkBox.isSelected()) {
            importMap = getCustomizedImportMap(importType, modMaps);
        } else {
            importMap = modMaps;
        }
        if (importMap.isEmpty()) {
            if (importType.equals(ImportType.RESTORE_POINT)) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.restorePoint.canceled"));
            } else {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
            }
            return;
        }

        // Check if assets folders exist
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingAssetsFolder"));
        if (!checkAssetsFolders(importMap)) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.assetFoldersMissing"));
            return;
        }

        // Check if some mods will replace or overwrite the same mod, if yes abort import
        // Construct content that should be replaced
        ArrayList<AbstractBaseContent> replacedContent = getReplacedContent(importMap);
        // Construct content that should be modified
        ArrayList<AbstractBaseContent> contentToModify = getContentToModify(importMap);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingConflicts"));
        ArrayList<String> importConflicts = getImportConflicts(contentToModify, replacedContent);
        if (!importConflicts.isEmpty()) {
            JPanel panelImportConflicts = new JPanel();
            panelImportConflicts.setLayout(new BoxLayout(panelImportConflicts, BoxLayout.Y_AXIS));
            JLabel labelImportConflicts = new JLabel(I18n.INSTANCE.get("dialog.sharingManager.importall.importConflics"));
            panelImportConflicts.add(labelImportConflicts);
            panelImportConflicts.add(WindowHelper.getScrollPane(WindowHelper.getList(Utils.convertArrayListToArray(importConflicts), false)));
            JOptionPane.showMessageDialog(null, panelImportConflicts, I18n.INSTANCE.get("dialog.sharingManager.importall.importConflics.title"), JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Unable to import, conflict");
            return;
        }

        // Edit import map to contain the maps for the modified contents.
        // If modification is not allowed, the modification instructions are removed without adding the modified content. 
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.editingImportMap"));
        if (chkBxAllowModification.isSelected()) {
            editImportMap(importMap, contentToModify, true);
        } else {
            editImportMap(importMap, contentToModify, false);
        }

        // Remove content that is modified
        if (chkBxAllowModification.isSelected()) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.removeModifiedContent"));
            for (AbstractBaseContent content : contentToModify) {
                content.contentType.removeContent(content.name);
            }
        } 

        // Remove content that should be replaced
        if (allowReplacement.isSelected()) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.replaceModsFound.removing"));
            for (AbstractBaseContent content : replacedContent) {
                content.contentType.removeContent(content.name);
            }
        }

        // Check if all dependencies are available and replace missing dependencies
        Set<Map<String, Object>> modsChecked = checkDependencies(importMap);
        if (modsChecked == null) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
            return;
        }

        // Set the new mod ids
        setImportHelperMaps(modsChecked);

        // Build the import maps into content that can be added to the game
        Map<BaseContentManager, List<AbstractBaseContent>> contentsToImport = constructImportContents(modsChecked);
        if (contentsToImport == null) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
            return;
        }

        // Check if all image files exist
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingImages"));
        if (!checkImages(contentsToImport)) {
            return;
        }

        // Check if some images already exist
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingExistingImages"));
        if (!checkExistingImages(contentsToImport)) {
            return;
        }

        // Import all the content that has been constructed
        try {
            importContents(contentsToImport);
            if (importType.equals(ImportType.RESTORE_POINT)) {
                TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.restorePoint.restoreSuccessful"), timeHelper.getMeasuredTimeDisplay()));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored"), I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.restorePointSuccessfullyRestored.title"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.importAll.completed"), timeHelper.getMeasuredTimeDisplay()));
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessful"), I18n.INSTANCE.get("dialog.sharingManager.importAll.summary.importSuccessful.title"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ModProcessingException e) {
            e.printStackTrace();
            TextAreaHelper.printStackTrace(e);
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel"));
        }

        // Clear temporary files
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
        Map<BaseContentManager, JPanel> importModPanels = new HashMap<>();
        Map<BaseContentManager, AtomicBoolean> enabledMods = new HashMap<>();
        Map<BaseContentManager, AtomicReference<List<String>>> selectedMods = new HashMap<>();
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            List<String> modNames = new ArrayList<>();
            for (Map<String, Object> map : set) {
                if (map.get("mod_type").equals(manager.getId())) {
                    StringBuilder name = new StringBuilder();
                    name.append(map.get("NAME EN").toString());
                    //if (map.containsKey("replaces")) {
                    //    name.append(" - ").append(I18n.INSTANCE.get("commonText.replaces"))
                    //        .append(" ")
                    //        .append(map.get("replaces"));
                    //} else if (map.containsKey("modifies")) {
                    //    name.append(" - ").append(I18n.INSTANCE.get("commonText.modifies"))
                    //        .append(" ")
                    //        .append(map.get("modifies"));
                    //}
                    modNames.add(name.toString());
                }
            }
            selectedMods.put(manager, new AtomicReference<>(modNames));
            enabledMods.put(manager, new AtomicBoolean(true));
            importModPanels.put(manager, new JPanel());
        }
        for (BaseContentManager mod : ContentAdministrator.contentManagers) {
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
        for (Map.Entry<BaseContentManager, JPanel> entry : importModPanels.entrySet()) {
            if (!selectedMods.get(entry.getKey()).get().isEmpty()) {
                panels.add(entry.getValue());
            }
        }
        params = new Object[]{labelStart, Utils.getSortedModPanels(panels), labelEnd};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.sharingManager.importAll.importReady.message.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Set<Map<String, Object>> modMap = new HashSet<>();
            for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                for (Map<String, Object> map : set) {
                    for (String string : selectedMods.get(manager).get()) {
                        if (string.contains((String)map.get("NAME EN"))) {
                            modMap.add(map);
                        }
                        //if (map.get("NAME EN").equals(string)) {
                        //    modMap.add(map);
                        //}
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
        TimeHelper timeHelper = new TimeHelper();
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
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchComplete") + " " + timeHelper.getMeasuredTimeDisplay() + " " + I18n.INSTANCE.get("commonText.seconds") + "!");
        LOGGER.info("Search for .toml files complete. Took " + timeHelper.getMeasuredTimeDisplay() + " " + I18n.INSTANCE.get("commonText.seconds") + "!");
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

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.accessDeniedError") + ": " + file);
                        DebugHelper.warn(LOGGER, "Unable to search folder for .toml files, access denied: " + file);
                        return FileVisitResult.SKIP_SUBTREE;
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
    private static ArrayList<Map<String, Object>> transformTomlFilesToMaps(ArrayList<File> files) {
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
     * @param singleMods  An array list that contains all maps of the toml instances that are verified to contain a single mod.
     *                    This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}.
     * @param bundledMods An array list that contains all maps of the toml instances that are verified to contain bundled mods
     *                    This map can be returned by {@link SharingManager#getFilteredModMaps(ArrayList, boolean)}
     * @return A set of maps that contain the mod data.
     * If no compatible mods are found an empty map is returned.
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> getImportMaps(ArrayList<Map<String, Object>> singleMods, ArrayList<Map<String, Object>> bundledMods) {
        ProgressBarHelper.initializeProgressBar(0, singleMods.size() + bundledMods.size(), I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.searchingTomlFilesForMods"));
        TimeHelper timeHelper = new TimeHelper();
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
            try {
                map = removeQuoteSymbol(map);
                DebugHelper.debug(LOGGER, "single mod instance found!: " + map.get("name"));
                if (isModToolVersionSupported(map)) {
                    if (!map.containsKey("NAME EN") && (map.containsKey("replaces") || map.containsKey("modifies"))) {
                        mods.add(map);
                    } else if (doesMapContainMod(mods, map.get("name").toString(), map.get("mod_type").toString())) {
                        modsDuplicated++;
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modsDuplicated") + ": " + map.get("mod_type") + " - " + map.get("name"));
                    } else if (doesModExist((String) map.get("name"), (String) map.get("mod_type"))) {
                        modsAlreadyAdded++;
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modAlreadyAdded") + ": " + map.get("mod_type") + " - " + map.get("name"));
                    } else {
                        mods.add(map);
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modCompatible") + ": " + map.get("mod_type") + " - " + map.get("name"));
                    }
                    modsTotal++;
                } else {
                    modsTotal++;
                    modsIncompatible++;
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modNotCompatible.firstPart") + ": " + map.get("mod_type") + " - " + map.get("name") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.secondPart") + ": " + map.get("mod_tool_version") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.thirdPart") + ": " + Arrays.toString(getRequiredModToolVersions((String) map.get("mod_type"))));
                }
            } catch (ModProcessingException | NullPointerException e) {
                LOGGER.info("Found incompatible content map: " + e.getMessage());
                LogFile.printStacktrace(e);
                e.printStackTrace();
            }
            ProgressBarHelper.increment();
        }
        for (Map<String, Object> map : bundledMods) {
            DebugHelper.debug(LOGGER, "bundled mod instance found!");
            try {
                // This map contains all mods
                // The key is the export type and the value is a map of content of that type
                Map<String, Object> mapMods = (Map<String, Object>) map.get("mods");
                for (Map.Entry<String, Object> entry : mapMods.entrySet()) {
                    Map<String, Object> sameTypeMods = (Map<String, Object>) entry.getValue();
                    for (Map.Entry<String, Object> singleMod : sameTypeMods.entrySet()) {
                        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                            if (entry.getKey().equals(manager.getId())) {
                                ProgressBarHelper.increaseMaxValue(((Map<String, Object>) mapMods.get(manager.getId())).size());
                                Map<String, Object> singleModMap = removeQuoteSymbol((Map<String, Object>) singleMod.getValue());
                                if (isModToolVersionSupported(manager.getId(), (String) map.get("mod_tool_version"))) {
                                    singleModMap.put("mod_type", manager.getId());
                                    singleModMap.put("assets_folder", map.get("assets_folder"));
                                    if (!singleModMap.containsKey("NAME EN") && (singleModMap.containsKey("replaces") || singleModMap.containsKey("modifies"))) {
                                        mods.add(singleModMap);
                                    } else if (doesMapContainMod(mods, singleModMap.get("NAME EN").toString(), manager.getId())) {
                                        modsDuplicated++;
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modsDuplicated") + ": " + manager.getId() + " - " + singleModMap.get("NAME EN"));
                                    } else if (doesModExist((String) singleModMap.get("NAME EN"), manager.getId())) {
                                        modsAlreadyAdded++;
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modAlreadyAdded") + ": " + manager.getId() + " - " + singleModMap.get("NAME EN"));
                                    } else {
                                        mods.add(singleModMap);
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modCompatible") + ": " + manager.getId() + " - " + singleModMap.get("NAME EN"));
                                    }
                                    modsTotal++;
                                } else {
                                    modsTotal++;
                                    modsIncompatible++;
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.modNotCompatible.firstPart") + ": " + manager.getId() + " - " + singleModMap.get("NAME EN") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.secondPart") + ": " + map.get("mod_tool_version") + "; " + I18n.INSTANCE.get("textArea.importAll.modNotCompatible.thirdPart") + ": " + Arrays.toString(getRequiredModToolVersions(manager.getId())));
                                }
                            }
                        }
                    }
                    ProgressBarHelper.increment();
                }
            } catch (ClassCastException | ModProcessingException | NullPointerException e) {
                LOGGER.info("Found incompatible content map: ");
                LogFile.printStacktrace(e);
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
        LOGGER.info("Search for mods complete. Found " + modsTotal + " mods after " + timeHelper.getMeasuredTimeDisplay() + " " + I18n.INSTANCE.get("commonText.seconds") + "!");
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
     * False if a folder is missing and the safety features are enabled.
     */
    private static boolean checkAssetsFolders(Set<Map<String, Object>> mods) {
        List<String> invalidAssetFolders = new ArrayList<>();
        for (Map<String, Object> map : mods) {
            Path path = Paths.get(map.get("assets_folder").toString());
            if (!Files.exists(path) && map.containsKey("requires_pictures") && map.get("requires_pictures").equals(true)) {
                if (!invalidAssetFolders.contains(path.toString())) {
                    invalidAssetFolders.add(path.toString());
                }
            }
        }
        if (!invalidAssetFolders.isEmpty() && !Settings.safetyFeatures.get(SafetyFeature.SKIP_ASSETS_FOLDER_CHECK)) {
            JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + "<br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.assetsFoldersMissing"));
            String[] string = new String[invalidAssetFolders.size()];
            invalidAssetFolders.toArray(string);
            JList<String> list = WindowHelper.getList(string, false);
            JScrollPane scrollPane = WindowHelper.getScrollPane(list);
            JComponent[] components = {label, scrollPane};
            JOptionPane.showMessageDialog(null, components, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (Settings.safetyFeatures.get(SafetyFeature.SKIP_ASSETS_FOLDER_CHECK)) {
                DebugHelper.warn(LOGGER, "Check for asset folders is disabled");
            }
            return true;
        }
    }

    /**
     * Checks the mods contained in the set for dependencies and returns a set that is ready to be imported.
     * If a dependency is not found the user is prompted to select a replacement. If no replacement is selected a random replacement will be chosen.
     * The function that replaces the dependency is {@link SharingManager#replaceDependencies(BaseContentManager, Map, BaseContentManager, String)}
     *
     * @param mods The map where the mods are stored in
     * @return A set of maps that contain the mod data. The data is checked for dependencies. Returns null if dependency check has been canceled by the user
     */
    @SuppressWarnings("unchecked")
    private static Set<Map<String, Object>> checkDependencies(Set<Map<String, Object>> mods) throws ModProcessingException {
        ContentAdministrator.analyzeContents();
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        ProgressBarHelper.initializeProgressBar(0, mods.size(), I18n.INSTANCE.get("textArea.importAll.checkingDependencies"));
        Map<BaseContentManager, Map<String, String>> alreadyReplacedDependencies = new HashMap<>();
        boolean showMissingDependencyDialog = true;
        for (Map<String, Object> parentMap : mods) {// parentMap is the current mod map
            for (BaseContentManager parent : ContentAdministrator.contentManagers) {//The parent is the content the map belongs to
                if (!parent.getId().equals(parentMap.get("mod_type").toString())) {
                    continue;
                }
                if (!(parent instanceof DependentContentManager)) {
                    continue;
                }
                // At this point the new method could create the dependencies map
                //Map<String, Object> dependencies = (Map<String, Object>) parentMap.get("dependencies");
                Map<String, Object> dependencies = ((DependentContentManager)parent).getDependencyMapFromImport(parentMap);
                if (dependencies == null) {
                    DebugHelper.warn(LOGGER, "dependency map of " + parent.getType() + " - " + parentMap.get("NAME EN") + " does not exist");
                    continue;
                }
                for (Map.Entry<String, Object> entry : dependencies.entrySet()) {// entry is one entry in the dependency list of the parent
                    for (BaseContentManager child : ContentAdministrator.contentManagers) {//The child is the mod that should be replaced in the parent map
                        if (!entry.getKey().equals(child.getId()) || !((DependentContentManager) parent).getDependencies().contains(child)) {
                            continue;
                        }
                        DebugHelper.debug(LOGGER, I18n.INSTANCE.get("textArea.importAll.requiresDependencies") + ": " + parentMap.get("mod_type") + " - " + parentMap.get("NAME EN") + " - " + parentMap.get("dependencies"));
                        ArrayList<String> arrayList;
                        if (entry.getValue() instanceof ArrayList) {
                            arrayList = (ArrayList<String>)entry.getValue();
                        } else {
                            HashSet<String> hs = (HashSet<String>) entry.getValue();
                            arrayList = (ArrayList<String>)hs.stream().collect(Collectors.toList());
                        }
                        for (String childName : arrayList) {
                            if (doesModExist(childName, child.getId()) || doesMapContainMod(mods, childName, child.getId())) {
                                continue;
                            }
                            String replacement = getReplacedDependency(alreadyReplacedDependencies, childName, child);
                            if (replacement != null) {
                                replaceDependencies((BaseContentManager & DependentContentManager) parent, parentMap, child, childName, replacement);
                                continue;
                            }
                            // Ask the user with which content the missing dependency should be replaced
                            JLabel label1 = new JLabel("<html>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part1") + ":<br><br>" + child.getTypeUpperCase() + " - " + childName + "<br><br>" + I18n.INSTANCE.get("textArea.importAll.dependencyCheck.optionPane.part2"));
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
                                    setReplacedDependency(alreadyReplacedDependencies, child, childName, replaceDependencies((BaseContentManager & DependentContentManager) parent, parentMap, child, childName));
                                } else {
                                    replaceDependencies((BaseContentManager & DependentContentManager) parent, parentMap, child, childName, list.getSelectedValue());
                                    setReplacedDependency(alreadyReplacedDependencies, child, childName, list.getSelectedValue());
                                }
                            } else {
                                return null;
                            }
                        }
                    }
                }
            }
            ProgressBarHelper.increment();
        }
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
    private static void setReplacedDependency(Map<BaseContentManager, Map<String, String>> replacementMap, BaseContentManager child, String childName, String replacement) {
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
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.firstPart") + " " + child.getTypeUpperCase() + " - " + childName + " " + I18n.INSTANCE.get("textArea.importAll.dependencyDoesNotExist.secondPart") + " " + replacement);
    }

    /**
     * Searches the replacement map for the parentName.
     * If the name is found the replacement is returned.
     *
     * @return If the name is found: the replacement. If the name is not found null.
     * @see SharingManager#setReplacedDependency(Map, BaseContentManager, String, String)  Parameters
     */
    private static String getReplacedDependency(Map<BaseContentManager, Map<String, String>> replacementMap, String childName, BaseContentManager child) {
        for (Map.Entry<BaseContentManager, Map<String, String>> entry : replacementMap.entrySet()) {
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
     * @see SharingManager#replaceDependencies(BaseContentManager, Map, BaseContentManager, String, String)  Parameters
     */
    private static <T extends BaseContentManager & DependentContentManager> String replaceDependencies(T parent, Map<String, Object> parentMap, BaseContentManager child, String childName) throws ModProcessingException {
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
     * @throws ModProcessingException If the dependency could not be replaced
     * @see SharingManager#replaceDependencyInMap(BaseContentManager, Map, String, String)
     * @see SharingManager#replaceDependencyInDependencyMap(Map, BaseContentManager, String, String)
     */
    private static <T extends BaseContentManager & DependentContentManager> void replaceDependencies(T parent, Map<String, Object> parentMap, BaseContentManager child, String childName, String replacement) throws ModProcessingException {
        replaceDependencyInMap(parent, parentMap, childName, replacement);
        replaceDependencyInDependencyMap(parentMap, child, childName, replacement);
    }

    /**
     * Replaces the dependency name in the map with the replacement
     *
     * @see SharingManager#replaceDependencies(BaseContentManager, Map, BaseContentManager, String, String) Parameters
     * @see DependentContentManager#replaceMissingDependency(Map, String, String) How the enries are replaced
     */
    private static <T extends BaseContentManager & DependentContentManager> void replaceDependencyInMap(T parent, Map<String, Object> parentMap, String childName, String replacement) throws ModProcessingException {
        LOGGER.info("replacing missing dependencies for mod " + parent.getId());
        parent.replaceMissingDependency(parentMap, childName, replacement);
    }

    /**
     * Replaces the dependency in the dependency map of the mod with the selected replacement
     *
     * @see SharingManager#replaceDependencies(BaseContentManager, Map, BaseContentManager, String, String) Parameters
     */
    @SuppressWarnings("unchecked")
    private static void replaceDependencyInDependencyMap(Map<String, Object> parentMap, BaseContentManager child, String childName, String replacement) {
        LOGGER.info("replacing dependency map");
        if (parentMap.containsKey("dependencies")) {
            Map<String, Object> dependencyMap = (Map<String, Object>) parentMap.get("dependencies");
            try {
                ArrayList<String> modDependencies = (ArrayList<String>) dependencyMap.get(child.getId());
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
                dependencyMap.replace(child.getId(), newModDependencies);
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
            if (map.get("NAME EN").equals(name) && map.get("mod_type").equals(mod_type)) {
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
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            if (manager.getId().equals(modType)) {
                for (String string : manager.getCompatibleModToolVersions()) {
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
        for (BaseContentManager mod : ContentAdministrator.contentManagers) {
            if (mod.getId().equals(modType)) {
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
    private static String[] getRequiredModToolVersions(String modType) {
        for (BaseContentManager mod : ContentAdministrator.contentManagers) {
            if (mod.getId().equals(modType)) {
                Object[] objects = Arrays.stream(mod.getCompatibleModToolVersions()).distinct().toArray();
                return Arrays.asList(objects).toArray(new String[objects.length]);
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
        ProgressBarHelper.initializeProgressBar(0, ContentAdministrator.contentManagers.size(), I18n.INSTANCE.get("textArea.importAll.settingNewModIds"));
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            manager.initializeImportHelperMap();
            for (Map<String, Object> map : mods) {
                if (map.get("mod_type").equals(manager.getId())) {
                    if (map.containsKey("forced_id")) {
                        manager.getImportHelperMap().addEntry(map.get("NAME EN").toString(), Integer.parseInt(map.get("forced_id").toString()));
                    } else {
                        manager.getImportHelperMap().addEntry(map.get("NAME EN").toString());
                    }
                }
            }
            ProgressBarHelper.increment();
        }
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Adds all contents contained in the list to the game.
     */
    private static void importContents(Map<BaseContentManager, List<AbstractBaseContent>> contents) throws ModProcessingException {
        for (Map.Entry<BaseContentManager, List<AbstractBaseContent>> entry : contents.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                entry.getKey().addContents(entry.getValue());
            }
        }
    }

    /**
     * Constructs the contents contained in the map.
     * Returns null if an error occurred while constructing a content and the user canceled the construction process.
     */
    private static Map<BaseContentManager, List<AbstractBaseContent>> constructImportContents(Set<Map<String, Object>> contents) {
        ProgressBarHelper.initializeProgressBar(0, contents.size(), I18n.INSTANCE.get("progressBar.constructingImportContent"));
        TextAreaHelper.appendText(I18n.INSTANCE.get("progressBar.constructingImportContent"));
        TimeHelper timeHelper = new TimeHelper();
        Map<BaseContentManager, List<AbstractBaseContent>> constructedContents = new HashMap<>();
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            List<AbstractBaseContent> baseContents = new ArrayList<>();
            for (Map<String, Object> map : contents) {
                if (map.get("mod_type").equals(manager.getId())) {
                    try {
                        AbstractBaseContent content = manager.constructContentFromImportMap(map, Paths.get((String) map.get("assets_folder")));
                        content.id = manager.getImportHelperMap().getContentIdByName(content.name);
                        baseContents.add(content);
                    } catch (ModProcessingException e) {
                        TextAreaHelper.printStackTrace(e);
                        int result = JOptionPane.showConfirmDialog(null, String.format(I18n.INSTANCE.get("textArea.importAll.errorConstructingContent"), map.get("NAME EN"), manager.getType()), I18n.INSTANCE.get("frame.title.error"), JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.NO_OPTION) {
                            return null;
                        }
                    }
                    ProgressBarHelper.increment();
                }
            }
            constructedContents.put(manager, baseContents);
        }
        TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.constructingContents.duration"), timeHelper.getMeasuredTime() / 1000.0));
        ProgressBarHelper.resetProgressBar();
        return constructedContents;
    }

    /**
     * Checks if all images of contents that require images are available
     * @param contentsToImport The map that contains all contents that should be imported
     * @return True if all images are available, false if not
     */
    private static boolean checkImages(Map<BaseContentManager, List<AbstractBaseContent>> contentsToImport) {
        // Check if all images are available
        StringBuilder missingPictures = new StringBuilder();
        for (Map.Entry<BaseContentManager, List<AbstractBaseContent>> entry : contentsToImport.entrySet()) {
            for (AbstractBaseContent content : entry.getValue()) {
                if (content instanceof RequiresPictures) {
                    try {
                        missingPictures.append(((RequiresPictures) content).externalImagesAvailable());
                    } catch (ModProcessingException e) {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.contentWrongConstructed"));
                        TextAreaHelper.printStackTrace(e);
                        return false;
                    }
                }
            }
        }
        if (missingPictures.length() > 0) {
            String[] problems = missingPictures.toString().split("\n");
            JLabel label = new JLabel("<html>" + I18n.INSTANCE.get("dialog.sharingManager.importThings.error.firstPart") + "<br>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.missingPictures"));
            JList<String> list = WindowHelper.getList(problems, false);
            JScrollPane scrollPane = WindowHelper.getScrollPane(list);
            JComponent[] components = {label, scrollPane};
            JOptionPane.showMessageDialog(null, components, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.picturesMissing"));
            return false;
        }
        return true;
    }

    /**
     * Checks if image files that should be created in the games files already exist.
     * If they do, the user is asked if he wants to overwrite them.
     * @param contentsToImport The map that contains all contents that should be imported
     * @return True when the user overwrote the files, false if not. Overwriting means that the image is deleted and the correct image is copied in {@link SharingManager#importContents(Map)}.
     *          False means that the import is canceled.
     * @throws ModProcessingException If an error occurred while duplicated image files where deleted
     */
    private static boolean checkExistingImages(Map<BaseContentManager, List<AbstractBaseContent>> contentsToImport) throws ModProcessingException {
        boolean removeExistingImages = false;
        for (Map.Entry<BaseContentManager, List<AbstractBaseContent>> entry : contentsToImport.entrySet()) {
            for (AbstractBaseContent content : entry.getValue()) {
                if (content instanceof RequiresPictures) {
                    for (Map.Entry<String, Image> images : ((RequiresPictures) content).getImageMap().entrySet()) {
                        if (Files.exists(images.getValue().gameFile.toPath())) {
                            if (!removeExistingImages) {
                                int result = JOptionPane.showConfirmDialog(null, String.format(I18n.INSTANCE.get("textArea.importAll.imageExists"), images.getValue().gameFile.getPath()), I18n.INSTANCE.get("frame.title.error"), JOptionPane.YES_NO_OPTION);
                                if (result == JOptionPane.YES_OPTION) {
                                    removeExistingImages = true;
                                } else {
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.importAll.cancel") + ": " + I18n.INSTANCE.get("textArea.importAll.picturesAlreadyExist"));
                                    return false;
                                }
                            }
                            try {
                                System.out.println("Deleting " + images.getValue().gameFile.getPath());
                                Files.delete(images.getValue().gameFile.toPath());
                            } catch (IOException e) {
                                throw new ModProcessingException("Unable to delete duplicated images", e);
                            }
                        }
                    }
                }
            }
        }
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
    private static void setFeatureAvailableGuiComponents(String labelText, List<String> set, JPanel panel, AtomicReference<List<String>> selectedEntries, AtomicBoolean disableImport, String frameTitle) {
        JLabel label = new JLabel(labelText);
        JButton button = new JButton(set.size() + "/" + set.size());
        disableImport.set(false);
        button.addActionListener(actionEvent -> {
            List<String> selectedMods = Utils.getSelectedEntries(I18n.INSTANCE.get("dialog.sharingManager.selectImports"), frameTitle, Utils.convertArrayListToArray(new ArrayList<>(set)), Utils.convertArrayListToArray(new ArrayList<>(set)));
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
        panel.setName(labelText);
    }

    /**
     * Counts how many times `key` occures in the set of maps`.
     */
    private static int countKeyOccurances(Set<Map<String, Object>> modMaps, String key) {
        int amount = 0;
        for (Map<String, Object> map : modMaps) {
            String replaces = (String) map.get(key);
            if (replaces == null) {
                continue;
            }
            amount += 1;
        }
        return amount;
    }

    /**
     * Checks the input map for content that replaces default content and returns an array list containing
     * the content that should be removed.
     * @param modMaps The map containing the content that should be checked.
     * @return An ArrayList containing all the content that will be replaced.
     */
    private static ArrayList<AbstractBaseContent> getReplacedContent(Set<Map<String, Object>> modMaps) throws ModProcessingException {
        return getSpecificContent(modMaps, "replaces");
    }

    /**
     * Checks the input map for content that modifies content and returns an array list containing the content that should be modified.
     * @param modMaps The map containing the content that should be checked.
     * @return An ArrayList containing all the content tht will be replaced.
     */
    private static ArrayList<AbstractBaseContent> getContentToModify(Set<Map<String, Object>> modMaps) throws ModProcessingException {
        return getSpecificContent(modMaps, "modifies");
    }

    /**
     * Builds content for all values behind the key in the maps
     * @param modMaps The maps that contain mod data
     * @param mapKey The key under wich the entry can be found
     * @return An ArrayList containing all the specified content
     */
    private static ArrayList<AbstractBaseContent> getSpecificContent(Set<Map<String, Object>> modMaps, String mapKey) throws ModProcessingException {
        ArrayList<AbstractBaseContent> contents = new ArrayList<>();
        for (Map<String, Object> map : modMaps) {
            for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                if (map.get("mod_type").equals(manager.getId())) {
                    try {
                        if (map.get(mapKey) == null && map.get(mapKey) != "") {
                            continue;
                        }
                        AbstractBaseContent c = manager.constructContentFromName((String) map.get(mapKey));
                        c.touches = (String) map.get(mapKey);
                        contents.add(c);
                    } catch (ModProcessingException e) {
                        if (mapKey == "replaces") {
                            DebugHelper.warn(SharingManager.class, "Could not construct content that should be replaced, name is invalid: " + e.getMessage());
                        } else {
                            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("dialog.sharingManager.importAll.contentToModifyMissing") + "<br><br>" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                            DebugHelper.warn(SharingManager.class, "Could not construct content that should be modified, name is invalid: " + e.getMessage());
                            throw new ModProcessingException("Unable to construct content that should be modified!", e);
                        }
                    }
                }
            }
        }
        return contents;
    }

    /**
     * Removes the instructions on how content should be modified from the importMap and adds
     * the map of the modifed content, ready to be added to the game.
     * @param importMaps The set containg all maps that contain content that should be imported
     * @param contentToModify A list of contents that should be modified, used to build the export map that is modified
     * @param addContents If true the contents that have been constructed will be added to the set of import maps. If this is
     *                    not set, only the construction manual is removed.
     */
    private static void editImportMap(Set<Map<String, Object>> importMaps, ArrayList<AbstractBaseContent> contentToModify, boolean addContents) throws ModProcessingException {
        Set<Map<String, Object>> modifiedContents = new HashSet<>();
        Set<Map<String, Object>> modificationInstructions = new HashSet<>();
        HashMap<Path, Path> copiedAssetsFolders = new HashMap<>();
        for (Map<String, Object> map : importMaps) {
            for (AbstractBaseContent content : contentToModify) {
                if (!map.containsKey("modifies")) {
                    continue;
                }
                if (map.get("modifies").equals(content.name)) {
                    // Content to modify found
                    modificationInstructions.add(map);
                    try {
                        Map<String, Object> exportMap = content.getExportMap();
                        exportMap.putAll(map);
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (!exportMap.containsKey(entry.getKey())) {
                                continue;
                            }
                            exportMap.replace(entry.getKey(), entry.getValue());
                        }
                        if (content instanceof RequiresPictures) {
                            // Copy original images to temp folder
                            Path source = Paths.get((String)map.get("assets_folder"));
                            Path destination;
                            if (copiedAssetsFolders.containsKey(source)) {
                                destination = copiedAssetsFolders.get(source);
                            } else {
                                destination = ModManagerPaths.TEMP.getPath().resolve("import_" + Utils.getCurrentDateTime());
                                copiedAssetsFolders.put(source, destination);
                                Files.createDirectories(destination);
                                // Copy new images to temp folder
                                DataStreamHelper.copyDirectory(Paths.get((String)map.get("assets_folder")), destination);
                            }
                            ((RequiresPictures)content).exportPictures(destination);
                            // Change assets folder entry
                            exportMap.replace("assets_folder", destination.toString());
                        }
                        // Set forced id only if content is advanced content
                        if (content instanceof AbstractAdvancedContent) {
                            exportMap.put("forced_id", content.id);
                        }
                        modifiedContents.add(exportMap);
                    } catch (ModProcessingException e) {
                        throw new ModProcessingException("Unable to modify import map!", e);
                    } catch (IOException e) {
                        throw new ModProcessingException("Unable to copy pictures to temp folder!", e);
                    }
                }
            }
        }
        // Remove modification instructions from importMaps
        for (Map<String, Object> map : modificationInstructions) {
            importMaps.remove(map);
        }
        // Add modified contents to importMap
        if (addContents) {
            importMaps.addAll(modifiedContents);
        }
    }

    /**
     * Checks if content that should be modified and content that should be replaced have mutual entries.
     * @return ArrayList that contains conflicts, empty if no conflics are found.
     */
    private static ArrayList<String> getImportConflicts(ArrayList<AbstractBaseContent> contentToModify, ArrayList<AbstractBaseContent> replacedContent) {
        ArrayList<String> conflicts = new ArrayList<>();
        for (AbstractBaseContent c1 : contentToModify) {
            for (AbstractBaseContent c2: replacedContent) {
                if (c1.contentType == c2.contentType && c1.touches.equals(c2.touches)) {
                    conflicts.add(c1.contentType.getType() + " - " + c1.touches);
                }
            }
        }
        return conflicts;
    }

    /**
     * Exports the specified mod to the export folder.
     * Writes a message to the text area if mod export was successful or if mod was already exported
     *
     * @param content The content that should be exported
     * @param folder  The root folder where the mods should be exported to
     * @throws ModProcessingException When something went wrong.
     */
    public static void exportSingleMod(AbstractBaseContent content, Path folder) throws ModProcessingException {
        LOGGER.info("exporting mod: " + content.name);
        Path path = folder.resolve(content.contentType.getId()).resolve(Utils.convertName(content.name));
        String fileName = content.contentType.getId() + "_" + Utils.convertName(content.name) + ".toml";
        if (!Files.exists(path.resolve(fileName)) && !Files.exists(path.resolve(Utils.convertName(content.name) + "/" + fileName))) {
            try {
                Files.createDirectories(path);
                Map<String, Object> map = content.getExportMap();
                map.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
                map.put("type", ExportType.ALL_SINGLE.getTypeName());
                map.put("mod_type", content.contentType.getId());
                map.put("name", content.name);
                TomlWriter tomlWriter = new TomlWriter();
                if (content instanceof RequiresPictures) {
                    Path assets = path.resolve("assets");
                    ((RequiresPictures) content).exportPictures(assets);
                    map.put("requires_pictures", true);
                } else {
                    map.put("requires_pictures", false);
                }
                tomlWriter.write(map, path.resolve(fileName).toFile());
            } catch (IOException e) {
                throw new ModProcessingException("Unable to export mod", e);
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.exported") + " " + content.contentType.getType() + " - " + content.name);
        } else {
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + content.contentType.getType() + " - " + content.name + ": " + I18n.INSTANCE.get("commonText.alreadyExported"));
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
        ArrayList<AbstractBaseContent> contents = new ArrayList<>();
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            if (Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
                if (manager.getContentByAlphabet().length > 0) {
                    contents.addAll(Utils.constructContents(Arrays.asList(manager.getContentByAlphabet()), manager));
                }
            } else {
                if (manager.getCustomContentString().length > 0) {
                    contents.addAll(Utils.constructContents(Arrays.asList(manager.getCustomContentString()), manager));
                }
            }
        }
        export(exportType, contents);
    }

    /**
     * If mods map is not null only the mods that are stored within that map are exported.
     * If export type is anything else all mods will be exported.
     * The text area and the progress bar are used to display progress.
     *
     * @param exportType Determines how and to where the mods should be exported
     * @param contents   The contents that should be exported
     * @throws ModProcessingException If something went wrong while exporting mods
     */
    public static void export(ExportType exportType, ArrayList<AbstractBaseContent> contents) throws ModProcessingException {
        TimeHelper timeHelper = new TimeHelper();
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
            ProgressBarHelper.initializeProgressBar(0, contents.size(), I18n.INSTANCE.get("commonText.exporting"));
            for (AbstractBaseContent content : contents) {
                ProgressBarHelper.setText(I18n.INSTANCE.get("commonText.exporting") + " " + content.contentType.getType() + " - " + content.name);
                exportSingleMod(content, exportFolder);
                ProgressBarHelper.increment();
            }
        } else {
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
                Map<String, Object> mods = new HashMap<>();
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.creatingExportMap"));
                for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                    Map<String, Object> contentTypeMap = new HashMap<>();
                    for (AbstractBaseContent content : contents) {
                        if (content.contentType.equals(manager)) {
                            Map<String, Object> singleModMap = content.getExportMap();
                            if (content instanceof RequiresPictures) {
                                if (!Files.exists(path.resolve("assets"))) {
                                    Files.createDirectories(path.resolve("assets"));
                                    Files.createDirectories(path);
                                }
                                ((RequiresPictures) content).exportPictures(path.resolve("assets"));
                                singleModMap.put("requires_pictures", true);
                            } else {
                                singleModMap.put("requires_pictures", false);
                            }
                            contentTypeMap.put(Utils.convertName(content.name), singleModMap);
                        }
                    }
                    if (!contentTypeMap.isEmpty()) {
                        mods.put(manager.getId(), contentTypeMap);
                    }
                }
                map.put("mods", mods);
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
        String measuredTime = timeHelper.getMeasuredTimeDisplay();
        TextAreaHelper.appendText(String.format(I18n.INSTANCE.get("textArea.exportComplete"), measuredTime));
        LOGGER.info("Exporting mods as " + exportType.getTypeName() + " took " + measuredTime + " " + I18n.INSTANCE.get("commonText.seconds") + "!");
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Opens a window where the user can select what mods should be exported.
     * The selected mods are then exported
     *
     * @throws ModProcessingException When something went wrong.
     * @see SharingManager#export(ExportType, ArrayList) will be used to write the export files.
     */
    public static void exportSelected() throws ModProcessingException {
        Map<BaseContentManager, JPanel> importModPanels = new HashMap<>();
        Map<BaseContentManager, AtomicBoolean> enabledMods = new HashMap<>();
        Map<BaseContentManager, AtomicReference<List<String>>> selectedMods = new HashMap<>();
        for (BaseContentManager mod : ContentAdministrator.contentManagers) {
            List<String> modNames;
            if (Settings.safetyFeatures.get(SafetyFeature.INCLUDE_ORIGINAL_CONTENTS_IN_LISTS)) {
                modNames = Arrays.asList(mod.getContentByAlphabet());
            } else {
                modNames = Arrays.asList(mod.getCustomContentString());
            }
            selectedMods.put(mod, new AtomicReference<>(modNames));
            enabledMods.put(mod, new AtomicBoolean(true));
            importModPanels.put(mod, new JPanel());
        }
        for (BaseContentManager mod : ContentAdministrator.contentManagers) {
            if (!selectedMods.get(mod).get().isEmpty()) {
                setFeatureAvailableGuiComponents(mod.getType(), selectedMods.get(mod).get(), importModPanels.get(mod), selectedMods.get(mod), enabledMods.get(mod), I18n.INSTANCE.get("frame.title.export"));
            }
        }
        JLabel labelStart = new JLabel(I18n.INSTANCE.get("dialog.sharingManager.exportAll.summary.startText"));
        JLabel labelEnd = new JLabel(I18n.INSTANCE.get("dialog.sharingManager.exportAll.summary.endText"));
        ArrayList<JPanel> panels = new ArrayList<>();
        for (Map.Entry<BaseContentManager, JPanel> entry : importModPanels.entrySet()) {
            if (!selectedMods.get(entry.getKey()).get().isEmpty()) {
                panels.add(entry.getValue());
            }
        }
        JCheckBox checkBox = new JCheckBox(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport"));
        checkBox.setToolTipText(I18n.INSTANCE.get("dialog.sharingManager.exportAll.singleExport.toolTip"));
        Object[] params = new Object[]{labelStart, Utils.getSortedModPanels(panels), labelEnd, checkBox};
        if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.sharingManager.exportAll.exportReady.message.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // Construct content
            ArrayList<AbstractBaseContent> contents = new ArrayList<>();
            for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                if (selectedMods.get(manager).get().size() > 0) {
                    contents.addAll(Utils.constructContents(new ArrayList<>(selectedMods.get(manager).get()), manager));
                }
            }
            if (checkBox.isSelected()) {
                export(ExportType.ALL_SINGLE, contents);
            } else {
                export(ExportType.ALL_BUNDLED, contents);
            }
            displayExportSuccessDialog();
        }
    }

    /**
     * Opens a JOptionPane where the user can decide how the mods should be exported: Single or bundled
     *
     * @throws ModProcessingException When something went wrong.
     */
    public static void displayExportModsWindow() throws ModProcessingException {
        ContentAdministrator.analyzeContents();
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

    private static void displayExportSuccessDialog() throws ModProcessingException {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.export.exportSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Desktop.getDesktop().open(ModManagerPaths.EXPORT.toFile());
            } catch (IOException e) {
                throw new ModProcessingException("Unable to open export folder", e);
            }
        }
    }
}
