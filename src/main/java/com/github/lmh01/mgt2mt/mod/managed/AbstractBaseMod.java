package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to create new mods.
 * Should be used with {@link AbstractAdvancedMod} or {@link AbstractSimpleMod}.
 */
public abstract class AbstractBaseMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseMod.class);

    private final ArrayList<JMenuItem> modMenuItems;
    private final JMenuItem exportMenuItem;
    int maxId = 0;
    String[] defaultContent = {};
    String[] customContent = {};
    Map<String, Integer> importHelperMap = new HashMap<>();

    {
        modMenuItems = getInitialModMenuItems();//TODO See if it works to initialize the default content string in this initializer so that the default content is not always recomputed
        exportMenuItem = getInitialExportMenuItem();
    }


    /**
     * @return Returns a string that contains the compatible mod tool versions
     */
    public abstract String[] getCompatibleModToolVersions();

    /**
     * @return Returns an array list containing new JMenuItems
     */
    private ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> doAddModMenuItemAction());
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(removeModItem);
        return menuItems;
    }

    /**
     * @return Returns a new JMenuItem that is used to be added to the export menu.
     */
    private JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> exportMenuItemAction());
        return menuItem;
    }

    /**
     * This function is called when the button add mod is clicked.
     * Creates a backup and analyzes the mod files before the action is performed.
     */
    private void doAddModMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            openAddModGui();
        }, "runnableAdd" + getType().replaceAll("\\s+", ""));
    }

    /**
     * This function is called when the button remove mod is clicked.
     * Analyzes the game file before the action is performed.
     */
    private void removeModMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            OperationHelper.process(this::removeMod, getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
        }, "runnableRemove" + getType().replaceAll("\\s+", ""));
    }

    /**
     * This function is called when the button export mod is clicked.
     * Analyzes the game file before the action is performed.
     */
    private void exportMenuItemAction() {
        ThreadHandler.startModThread(() -> {
            analyzeFile();
            Path path;
            if (Settings.enableExportStorage) {
                path = ModManagerPaths.EXPORT.getPath().resolve("single/" + Utils.getCurrentDateTime());
            } else {
                path = ModManagerPaths.EXPORT.getPath().resolve("single");
            }
            OperationHelper.process((string) -> SharingManager.exportSingleMod(this, string, path), getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
        }, "runnableExport" + getType().replaceAll("\\s+", ""));
    }

    /**
     * @return Returns an array that contains all custom contents
     */
    public final String[] getCustomContentString() throws ModProcessingException {
        return getCustomContentString(true);
    }

    /**
     * @param disableTextAreaMessage True when the messages should not be written to the text area.
     * @return Returns an array that contains all custom contents
     */
    public String[] getCustomContentString(boolean disableTextAreaMessage) throws ModProcessingException {
        String[] contentByAlphabet = getContentByAlphabet();
        ArrayList<String> arrayListCustomContent = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(getDefaultContent().length, getFileContentSize(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.progressBar"), !disableTextAreaMessage);
        for (String s : contentByAlphabet) {
            boolean isDefaultContent = false;
            for (String contentName : getDefaultContent()) {
                if (s.equals(contentName)) {
                    isDefaultContent = true;
                    break;
                }
            }
            if (!isDefaultContent) {
                arrayListCustomContent.add(s);
                if (!disableTextAreaMessage) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureFound") + " " + s);
                }
            }
            ProgressBarHelper.increment();
        }
        if (!disableTextAreaMessage) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureComplete"));
        }
        ProgressBarHelper.resetProgressBar();
        try {
            Collections.sort(arrayListCustomContent);
        } catch (NullPointerException ignored) {//TODO schauen, ob hier vielleicht auch noch ein throw für ModProcessingException hinzugefügt werden sollte

        }
        String[] string = new String[arrayListCustomContent.size()];
        arrayListCustomContent.toArray(string);
        setFinishedCustomContentString(string);
        return string;
    }

    /**
     * @return The custom content string that has been computed previously
     */
    public String[] getFinishedCustomContentString() {
        return customContent;
    }

    /**
     * Sets the custom content string that should be returned when {@link AbstractBaseMod#getFinishedCustomContentString()} is called.
     */
    public final void setFinishedCustomContentString(String[] customContent) {
        this.customContent = customContent;
    }

    /**
     * @return A string containing all active things sorted by alphabet.
     * @throws ModProcessingException If content can not be read because the file was not analyzed
     */
    public abstract String[] getContentByAlphabet() throws ModProcessingException;

    /**
     * @return The size of the default content file
     */
    public abstract int getFileContentSize();

    /**
     * If the id should be returned for mods that will be imported and that have not been added to the game use {@link AbstractBaseMod#getContentIdByName(String)}.
     *
     * @param name The name
     * @return The id for the specified name.
     * @throws ModProcessingException When the name does not exist
     */
    public abstract int getContentIdByName(String name) throws ModProcessingException;

    /**
     * The translation key that is specific to the mod
     * Eg. gameplayFeature
     */
    public abstract String getMainTranslationKey();

    /**
     * @return Returns the mod menu items that are specific for this mod
     */
    public final ArrayList<JMenuItem> getModMenuItems() {
        return modMenuItems;
    }

    /**
     * @return Returns the mod menu item that should be placed in the export menu
     */
    public final JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    /**
     * Initializes the mod: Adds it to the main window and all the actions
     */
    public final void initializeMod() {
        DebugHelper.debug(LOGGER, "Initializing mod: " + getType());
        ModManager.mods.add(getMod());
    }

    /**
     * Adds a new mod to the file.
     *
     * @param t   This map/string contains the values that should be printed to the file
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @throws ModProcessingException when {@literal <T>} is not valid
     */
    public abstract <T> void addModToFile(T t) throws ModProcessingException;

    /**
     * Removes the input mod from the text file
     *
     * @param name The mod name that should be removed
     */
    protected abstract void removeModFromFile(String name) throws ModProcessingException;

    /**
     * Removes the mod from the game.
     * This includes image files
     *
     * @param name The mod name that should be removed
     */
    public void removeMod(String name) throws ModProcessingException {
        removeModFromFile(name);
    }

    /**
     * @return Returns the mod that should be initialized
     */
    public abstract AbstractBaseMod getMod();

    /**
     * The type indicates what is written in the log, textArea, progress bar, windows and JOptionPanes.
     * E.g. gameplay feature
     *
     * @return Returns the mod name
     */
    public final String getType() {
        return getType(false);
    }

    /**
     * The type indicates what is written in the log, textArea, progress bar, windows and JOptionPanes.
     * E.g. Gameplay feature
     *
     * @return Returns the mod name with a capital letter
     */
    public final String getType(boolean capitalLetters) {
        if (capitalLetters) {
            return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase");
        } else {
            return I18n.INSTANCE.get("commonText." + getMainTranslationKey());
        }
    }

    /**
     * @return What type the mod is. Returns for example: genre, hardware_feature, npc_game. These values are not changed by localisation.
     */
    public abstract String getExportType();

    /**
     * Eg. Genres, Engine features
     *
     * @return Returns the mod name in plural
     */
    public final String getTypePlural() {
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase.plural");
    }

    /**
     * Creates a backup of the mod files specific to this mod
     *
     * @throws ModProcessingException If something went wrong while doing the backup
     */
    public void createBackup() throws ModProcessingException {
        try {
            Backup.createBackup(getGameFile());
        } catch (IOException e) {
            throw new ModProcessingException("Error while creating backup for " + getType() + " mod files: " + e.getMessage());
        }
    }

    /**
     * Sets the main menu buttons to active/disabled depending on if active mods are found
     *
     * @throws ModProcessingException If custom content string could not be returned
     */
    public final void setMainMenuButtonAvailability() throws ModProcessingException {
        String[] customContentString = getCustomContentString(true);
        for (JMenuItem menuItem : getModMenuItems()) {
            if (menuItem.getText().replace("R", "r").replace("A", "a").contains(I18n.INSTANCE.get("commonText.remove"))) {
                if (customContentString.length > 0) {
                    menuItem.setEnabled(true);
                    menuItem.setToolTipText("");
                } else {
                    menuItem.setEnabled(false);
                    menuItem.setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
                }
            }
        }
        if (customContentString.length > 0) {
            getExportMenuItem().setEnabled(true);
            getExportMenuItem().setToolTipText("");
        } else {
            getExportMenuItem().setEnabled(false);
            getExportMenuItem().setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
        }
    }

    /**
     * Analyzes the mod file.
     * Overwriting this function is not recommended without calling super.analyzeFile().
     */
    protected abstract void analyzeFile() throws ModProcessingException;

    /**
     * The file that should be analyzed and edited.
     */
    public File getGameFile() {
        return MGT2Paths.TEXT_DATA.getPath().resolve(getGameFileName()).toFile();
    }

    /**
     * @return The name of the game file. E.g. AntiCheat.txt
     */
    protected abstract String getGameFileName();

    /**
     * Returns the currently highest id
     */
    public final int getMaxId() {
        return maxId;
    }

    /**
     * Sets the maximum id
     */
    protected final void setMaxId(int id) {
        maxId = id;
    }

    /**
     * @return The next free id.
     */
    public final int getFreeId() {
        return getMaxId() + 1;
    }

    /**
     * @return The default content for the mod
     */
    public abstract String[] getDefaultContent();

    /**
     * @return The file name of the file that contains the default content.
     */
    public abstract String getDefaultContentFileName();

    /**
     * Opens a gui where the user can add the new mod.
     * This function will then add the mod and call {@link AbstractBaseMod#addModToFile(Object)}.
     *
     * @throws ModProcessingException If something went wrong
     */
    protected abstract void openAddModGui() throws ModProcessingException;//TODO Diese Funktion wird später umgeschrieben, sodass eingaben auch in die Felder geladen werden können

    /**
     * This is called inside of {@link AbstractBaseMod#openAddModGui()}
     *
     * @param t   This map/string contains the values that are used to create the option pane message
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @return The objects that should be displayed in the option pane
     * @throws ModProcessingException when {@literal <T>} is not valid
     */
    protected abstract <T> String getOptionPaneMessage(T t) throws ModProcessingException;

    /**
     * @return The charset in which the {@link AbstractBaseMod#getGameFile()} is written.
     */
    protected abstract Charset getCharset();

    /**
     * Returns a map containing the content that should be written into the export toml file.
     * To export an advanced mod this method should be called together with {@link AbstractComplexMod#exportImages(String, Path)}.
     * If the export map should be changed use {@link AbstractAdvancedMod#getChangedExportMap(Map, String)}.
     * To change what line is written in the map when a simple mod is exported use {@link AbstractSimpleMod#getReplacedLine(String)}.
     *
     * @param name The name for the mod that should be exported
     * @return Map containing the content that should be written to the export toml file
     * @throws ModProcessingException When something went wrong while copying the image files or creating the map
     */
    public abstract Map<String, Object> getExportMap(String name) throws ModProcessingException;

    /**
     * Returns a map that contains the dependencies of the mod. This map is printed into the export file.
     * This function should be overwritten by each mod that needs dependencies.
     * Map coding: key = modName | value = hash set of the required mods that belong to the modName
     *
     * @param t   This map/string contains the values that will be used to create the dependency map
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @return A map that contains the dependencies for the mod that should be exported
     * @throws ModProcessingException When something went wrong while creating the dependency map or when {@literal <T>} is not valid.
     */
    protected abstract <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException;

    /**
     * Imports the mod to the game.
     * Will edit the game files and import pictures, if needed.
     * {@link AbstractBaseMod#addModToFile(Object)} is used to edit the game file(s).
     *
     * @param map The map that contains the values that are required to import the mod.
     * @throws ModProcessingException If something went wrong while importing the mod
     */
    public abstract void importMod(Map<String, Object> map) throws ModProcessingException;

    /**
     * @return An array list that contains all the dependencies of the mod
     */
    public abstract ArrayList<AbstractBaseMod> getDependencies();

    /**
     * Analyses all dependencies of this mod
     *
     * @throws ModProcessingException If analysis of a mod fails
     */
    public final void analyzeDependencies() throws ModProcessingException {
        for (AbstractBaseMod mod : getDependencies()) {
            mod.analyzeFile();
        }
    }

    /**
     * Initializes the import helper map for this mod with all currently installed mods.
     */
    public abstract void initializeImportHelperMap() throws ModProcessingException;

    /**
     * Adds a new entry to the import helper map.
     * The mod id will be selected automatically.
     *
     * @param name The mod name that should be added to the map.
     */
    public final void addEntryToImportHelperMap(String name) {
        importHelperMap.put(name, getFreeId());
        maxId++;
    }

    /**
     * Returns the mod id for the mod name that is stored in the {@link AbstractBaseMod#importHelperMap}.
     * This includes mods that are not yet added to the game and that have been added to this map prior by using {@link AbstractBaseMod#addEntryToImportHelperMap(String)}.
     * This function should not be confused with {@link AbstractBaseMod#getContentIdByName(String)}.
     *
     * @param name The name of the mod.
     * @return The mod id for the mod name.
     * @throws ModProcessingException If import helper map is empty or the mod name does not exist in the map.
     */
    public final int getModIdByNameFromImportHelperMap(String name) throws ModProcessingException {
        if (!importHelperMap.isEmpty()) {
            try {
                return importHelperMap.get(name);
            } catch (NullPointerException e) {
                throw new ModProcessingException("The mod name " + name + " does not exist in the import map", e);
            }
        } else {
            throw new ModProcessingException("Import helper map is not initialized.", true);
        }
    }

    /**
     * Returns the mod name for the mod id that is stored in the {@link AbstractBaseMod#importHelperMap}.
     * This includes mods that are not yet added to the game and that have been added to this map prior by using {@link AbstractBaseMod#addEntryToImportHelperMap(String)}.
     * This function should not be confused with {@link AbstractBaseMod#getContentIdByName(String)}.
     *
     * @param id The id of the mod.
     * @return The mod id for the mod name.
     * @throws ModProcessingException If import helper map is empty or the mod name does not exist in the map.
     */
    public final String getModNameByIdFromImportHelperMap(int id) throws ModProcessingException {
        if (!importHelperMap.isEmpty()) {
            try {
                for (Map.Entry<String, Integer> entry : importHelperMap.entrySet()) {
                    if (entry.getValue().equals(id)) {
                        return entry.getKey();
                    }
                }
                throw new ModProcessingException("The mod id " + id + " does not exist in the import map");
            } catch (NullPointerException e) {
                throw new ModProcessingException("The mod id " + id + " does not exist in the import map", e);
            }
        } else {
            throw new ModProcessingException("Import helper map is not initialized.", true);
        }
    }
}