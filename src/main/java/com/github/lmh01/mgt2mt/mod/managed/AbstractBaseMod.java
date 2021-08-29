package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

//TODO Alle funktionen hier drin nach Nutzen sortieren (wenn alles fertig)
// Auch noch einmal drüber schauen, welche funktionen private oder protected sein sollten
// Auch in AbstractAdvancedMod und AbstractSimpleMod nachschauen!

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

    {
        modMenuItems = getInitialModMenuItems();
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
    private JMenuItem getInitialExportMenuItem(){
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> exportMenuItemAction());
        return menuItem;
    }

    /**
     * This function is called when the button add mod is clicked.
     * Creates a backup and analyzes the mod files before the action is performed.
     */
    private void doAddModMenuItemAction() {
        startModThread(() -> {
            analyzeFile();
            openAddModGui();
        }, "runnableAdd" + getType().replaceAll("\\s+",""));
    }

    /**
     * This function is called when the button remove mod is clicked.
     * Analyzes the game file before the action is performed.
     */
    private void removeModMenuItemAction() {
        startModThread(() -> {
            analyzeFile();
            OperationHelper.process(this::removeMod, getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
        }, "runnableRemove" + getType().replaceAll("\\s+",""));
    }

    /**
     * This function is called when the button export mod is clicked.
     * Analyzes the game file before the action is performed.
     */
    private void exportMenuItemAction() {
        startModThread(() -> {
            analyzeFile();
            OperationHelper.process((string) -> exportMod(string, false), getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
        }, "runnableExport" + getType().replaceAll("\\s+",""));
    }

    /**
     * @return Returns an array that contains all custom contents
     */
    public final String[] getCustomContentString() throws ModProcessingException{
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
                if(!disableTextAreaMessage){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureFound") + " " + s);
                }
            }
            ProgressBarHelper.increment();
        }
        if(!disableTextAreaMessage){
            TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureComplete"));
        }
        ProgressBarHelper.resetProgressBar();
        try{
            Collections.sort(arrayListCustomContent);
        }catch(NullPointerException ignored){//TODO schauen, ob hier vielleicht auch noch ein throw für ModProcessingException hinzugefügt werden sollte

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
     */
    public abstract String[] getContentByAlphabet() throws ModProcessingException;

    /**
     * @return The size of the default content file
     */
    public abstract int getFileContentSize();

    /**
     * @param name The name
     * @return The id for the specified name.
     * @throws ModProcessingException When the name does not exist
     */
    public abstract int getContentIdByName(String name) throws ModProcessingException;

    /**
     * Starts a thread that can catch a {@link ModProcessingException}.
     * If that exception is caught an error message displayed and printed into the text area. The thread will terminate.
     */
    public static void startModThread(ModAction action, String threadName) {
        Thread thread = new Thread(() -> {
            try {
                action.run();
            } catch (ModProcessingException e) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.firstPart") + " " + threadName);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.secondPart"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.thirdPart"));
                TextAreaHelper.printStackTrace(e);
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.modProcessingException.firstPart")  + " " + threadName + "\n" + I18n.INSTANCE.get("commonText.reason") + " " + e.getMessage().replace(" - ", "\n - "), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                LOGGER.info("Error in thread: " + threadName + "; Reason: " + e.getMessage());
            }
        });
        ThreadHandler.startThread(thread, threadName);
    }

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
        LOGGER.info("Initializing mod: " + getType());
        ModManager.mods.add(getMod());
    }

    /**
     * Adds a new mod to the file
     * @param t This map/string contains the values that should be printed to the file
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @throws ModProcessingException when {@literal <T>} is not valid
     */
    public abstract <T> void addMod(T t) throws ModProcessingException;

    /**
     * Removes the input mod from the text file
     * @param name The mod name that should be removed
     */
    public abstract void removeMod(String name) throws ModProcessingException;

    /**
     * @return Returns the mod that should be initialized
     */
    public abstract AbstractBaseMod getMod();

    /**
     * The type indicates what is written in the log, textArea, progress bar, windows and JOptionPanes.
     * E.g. gameplay feature
     * @return Returns the mod name
     */
    public final String getType() {
        return getType(false);
    }

    /**
     * The type indicates what is written in the log, textArea, progress bar, windows and JOptionPanes.
     * E.g. Gameplay feature
     * @return Returns the mod name with a capital letter
     */
    public final String getType(boolean capitalLetters) {
        if(capitalLetters) {
            return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase");
        } else {
            return I18n.INSTANCE.get("commonText." + getMainTranslationKey());
        }
    }

    /**
     * Eg. Genres, Engine features
     * @return Returns the mod name in plural
     */
    public final String getTypePlural() {
        return I18n.INSTANCE.get("commonText." + getMainTranslationKey() + ".upperCase.plural");
    }

    /**
     * Creates a backup of the mod files specific to this mod
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
     * @throws ModProcessingException If custom content string could not be returned
     */
    public final void setMainMenuButtonAvailability() throws ModProcessingException {
        String[] customContentString = getCustomContentString(true);
        for(JMenuItem menuItem : getModMenuItems()){
            if(menuItem.getText().replace("R", "r").replace("A", "a").contains(I18n.INSTANCE.get("commonText.remove"))){
                if(customContentString.length > 0){
                    menuItem.setEnabled(true);
                    menuItem.setToolTipText("");
                }else{
                    menuItem.setEnabled(false);
                    menuItem.setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
                }
            }
        }
        if(customContentString.length > 0){
            getExportMenuItem().setEnabled(true);
            getExportMenuItem().setToolTipText("");
        }else{
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
    protected abstract String getGameFileName();//TODO schauen, ob backups noch unter windows erstellt werden können (weil Dateiname der Mods geändert)(gild aber nur für AntiCheat und CopyProtect)

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
    public final int getFreeId(){
        return getMaxId()+1;
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
     * This function will then add the mod and call {@link AbstractBaseMod#addMod(Object)}.
     * @throws ModProcessingException If something went wrong
     */
    protected abstract void openAddModGui() throws ModProcessingException;//TODO Diese Funktion wird später umgeschrieben, sodass eingaben auch in die Felder geladen werden können

    /**
     * This is called inside of {@link AbstractBaseMod#openAddModGui()}
     * @param t This map/string contains the values that are used to create the option pane message
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @throws ModProcessingException when {@literal <T>} is not valid
     * @return The objects that should be displayed in the option pane
     */
    protected abstract <T> String getOptionPaneMessage(T t) throws ModProcessingException;

    /**
     * Writes a log message to the console using the (logger) of the abstract mod class
     * @param log The message that should be written
     */
    protected abstract void sendLogMessage(String log);

    /**
     * @return The charset in which the {@link AbstractBaseMod#getGameFile()} is written.
     */
    protected abstract Charset getCharset();

    /**
     * Exports the mod
     * @param name The name for the mod that should be exported
     * @param exportAsRestorePoint True when the mod should be exported as restore point. False otherwise
     * @return Returns true when the mod has been exported successfully. Returns false when the mod has already been exported.
     */
    public abstract boolean exportMod(String name, boolean exportAsRestorePoint) throws ModProcessingException;

    /**
     * Imports the mod.
     * @param importFolderPath The path for the folder where the import files are stored
     * @return Returns "true" when the mod has been imported successfully. Returns "false" when the mod already exists. Returns mod tool version of import mod when mod is not compatible with current mod tool.
     */
    public abstract String importMod(Path importFolderPath, boolean showMessages) throws ModProcessingException;//TODO Remove IOException and let only the ModProcessingException get thrown

    /**
     * @return The type name in caps
     * Eg. GAMEPLAY FEATURE, ENGINE FEATURE, GENRE
     */
    public abstract String getTypeCaps();

    /**
     * @return The export/import file name under which the mod can be found
     * Eg. gameplayFeature.txt, engineFeature.txt
     */
    public abstract String getImportExportFileName();

    /**
     * @return The name of the mod export folder
     */
    public String getExportFolder() {
        return getType();
    }
}