package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.Debug;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

//TODO Alle funktionen hier drin nach Nutzen sortieren (wenn alles fertig)
// Auch noch einmal drüber schauen, welche funktionen private oder protected sein sollten
// Auch in AbstractAdvancedMod und AbstractSimpleMod nachschauen!

/**
 * This class is used to create new mods.
 * Should be used with {@link AbstractAdvancedMod} or {@link AbstractSimpleMod}.
 */
public abstract class AbstractBaseMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdvancedModOld.class);

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
     * This function is called when the button add mod is clicked
     */
    private void doAddModMenuItemAction() {
        startModThread(this::openAddModGui, "runnableAdd" + getType().replaceAll("\\s+",""));
    }

    /**
     * This function is called when the button remove mod is clicked
     */
    private void removeModMenuItemAction() {
        startModThread(() -> {
            OperationHelper.process(this::removeMod, getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false);
        }, "runnableRemove" + getType().replaceAll("\\s+",""));
    }

    /**
     * This function is called when the button export mod is clicked
     */
    private void exportMenuItemAction() {
        startModThread(() -> {
            OperationHelper.process((string) -> exportMod(string, false), getCustomContentString(), getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
        }, "runnableExport" + getType().replaceAll("\\s+",""));
    }

    /**
     * @return Returns an array that contains all custom contents
     */
    public final String[] getCustomContentString(){
        return getCustomContentString(true);
    }

    /**
     * @param disableTextAreaMessage True when the messages should not be written to the text area.
     * @return Returns an array that contains all custom contents
     */
    public final String[] getCustomContentString(boolean disableTextAreaMessage){
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
        }catch(NullPointerException ignored){

        }
        String[] string = new String[arrayListCustomContent.size()];
        arrayListCustomContent.toArray(string);
        setFinishedCustomContentString(string);
        return string;
    }

    /**
     * @return The custom content string that has been computed previously
     */
    public final String[] getFinishedCustomContentString() {
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
    public abstract String[] getContentByAlphabet();

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
     * If that exception is caught an error message is printed into the text area and the thread will terminate.
     */
    private void startModThread(ModAction action, String threadName) {
        Thread thread = new Thread(() -> {
            try {
                action.run();
            } catch (ModProcessingException e) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.firstPart") + " " + threadName);
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.secondPart"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.modProcessingException.thirdPart"));
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                TextAreaHelper.appendText(sw.toString());
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
     * Analyzes the mod file.
     */
    public abstract void analyzeFile() throws IOException;

    /**
     * The file that should be analyzed and edited.
     */
    public abstract File getGameFile();

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
    protected abstract String getDefaultContentFileName();

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
    public abstract String importMod(String importFolderPath, boolean showMessages) throws ModProcessingException, IOException;

    /**
     * @return The type name in caps
     * Eg. GAMEPLAY FEATURE, ENGINE FEATURE, GENRE
     */
    protected abstract String getTypeCaps();

    /**
     * @return The export/import file name under which the mod can be found
     * Eg. gameplayFeature.txt, engineFeature.txt
     */
    public abstract String getImportExportFileName();

    /**
     * @return Returns a string that contains the compatible mod tool versions
     */
    public abstract String[] getCompatibleModToolVersions();

    /**
     * @return The name of the mod export folder
     */
    protected abstract String getExportFolder();
}