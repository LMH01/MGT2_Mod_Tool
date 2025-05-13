package com.github.lmh01.mgt2mt.content.managed;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BaseContentManager {

    /**
     * Adds the content to the game.
     * This includes editing the text file(s) and copying images if required.
     * Creates a backup beforehand.
     * A text area message is written.
     * Uses {@link BaseContentManager#editTextFiles(AbstractBaseContent, ContentAction)} to add the mod to the text file(s).
     * Uses {@link RequiresPictures#addPictures()} to add the pictures.
     *
     * @param content The content that should be added to the game
     */
    void addContent(AbstractBaseContent content) throws ModProcessingException;

    /**
     * Removes the content with the name from the game, if it exists.
     * This includes editing the text file(s) and deleting images if required.
     * Creates a backup beforehand.
     * A text area message is written.
     * Uses {@link BaseContentManager#editTextFiles(AbstractBaseContent, ContentAction)} to remove the mod to the text file(s).
     * Uses {@link RequiresPictures#removePictures()} to remove the pictures.
     */
    void removeContent(String name) throws ModProcessingException;

    /**
     * Adds all contents contained within the array list.
     * This will edit the games text file only once.
     *
     * @see BaseContentManager#addContent(AbstractBaseContent) Parameters and description
     */
    void addContents(List<AbstractBaseContent> contents) throws ModProcessingException;

    /**
     * Removes all contents contained within the array list.
     * This wil edit the games text file only once.
     *
     * @see BaseContentManager#removeContent(String) Parameters and description
     */
    void removeContents(List<AbstractBaseContent> contents) throws ModProcessingException;

    /**
     * Edits the games text file(s) to add or remove the content.
     * Use {@link AbstractSimpleContentManager#editTextFiles(List, ContentAction)}
     * or {@link AbstractAdvancedContentManager#editTextFiles(List, ContentAction)} instead if multiple contents should be added removed.
     *
     * @param content The content that should be removed or added
     * @param action  The action that should be performed
     */
    void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException;

    /**
     * Edits the games text file(s) to add or remove content.
     * This will edit the games text file(s) only once.
     *
     * @see BaseContentManager#editTextFiles(AbstractBaseContent, ContentAction) Parameters and description
     */
    void editTextFiles(List<AbstractBaseContent> contents, ContentAction action) throws ModProcessingException;

    /**
     * Opens the gui where the user can enter the values that are required to add the content to the game.
     * This function should invoke {@link BaseContentManager#addContent(AbstractBaseContent)} when the values have been
     * collected and the user accepts.
     */
    void openAddModGui() throws ModProcessingException;

    /**
     * Uses the game files to construct the base content.
     *
     * @param name The name of the content for which a base content should be constructed
     * @return The newly constructed content
     * @throws ModProcessingException When the content could not be constructed
     */
    AbstractBaseContent constructContentFromName(String name) throws ModProcessingException;

    /**
     * Constructs the base content from the import map
     *
     * @param map          The map that contains the values from which the content should be constructed
     * @param assetsFolder The folder that contains the assets that are required to build the content
     * @return The newly constructed content
     * @throws ModProcessingException When required values are missing from the map and the base content could not be constructed
     */
    AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException;

    /**
     * Checks if the integrity of the fileContent is violated.
     * The integrity is violated if an id is missing from an entry, if
     * an id is assigned more than once or if the ID or NAME EN tag are missing from  the map.
     *
     * @return StringBuilder that contains content integrity violations
     */
    String verifyContentIntegrity();

    /**
     * @return Returns a string that contains the compatible mod tool versions
     */
    String[] getCompatibleModToolVersions();

    /**
     * @return The mod menu items specific for this content
     */
    ArrayList<JMenuItem> getModMenuItems();

    /**
     * @return The export menu item specific for this content
     */
    JMenuItem getExportMenuItem();

    /**
     * Sets the main menu buttons to active/disabled depending on if active mods are found
     *
     * @throws ModProcessingException If custom content string could not be returned
     */
    void setMainMenuButtonAvailability() throws ModProcessingException;

    /**
     * Analyzes the games file(s) and stores the file content to be used later with other functions.
     */
    void analyzeFile() throws ModProcessingException;

    /**
     * @return The default content for this content type represented as string array
     */
    String[] getDefaultContent();

    /**
     * @return All content of the files documentation (meaning all lines that start with // and empfy lines in the game files before tha actuall content starts).
     */
    String[] getDocumentationContent();

    /**
     * @return All content for this content type matched id with names.
     */
    Map<String, Integer> getContentIdsByNames();
    
    /**
     * @return The custom content for this content type represented as string array
     */
    String[] getCustomContentString() throws ModProcessingException;

    /**
     * @return The content for this content type sorted by alphabet represented as string array
     */
    String[] getContentByAlphabet() throws ModProcessingException;

    /**
     * @return The content id for the name.
     */
    int getContentIdByName(String name) throws ModProcessingException;

    /**
     * @return The content name for the id.
     */
    String getContentNameById(int id) throws ModProcessingException;

    /**
     * @return The type of this content translated
     */
    String getType();

    /**
     * @return The type of this content with upper case starting letter and translated
     */
    String getTypeUpperCase();

    /**
     * @return The plural type name of this content with upper case starting letter and translated
     */
    String getTypePlural();

    /**
     * @return The unique id of this content
     */
    String getId();

    /**
     * Creates a backup of the text file for this content
     *
     * @throws ModProcessingException When the backup failed
     */
    void createBackup(boolean initialBackup) throws ModProcessingException;

    /**
     * @return The game file
     */
    File getGameFile();

    /**
     * @return The charset in which the game file is written.
     */
    Charset getCharset();

    /**
     * @return The maximum id that is in use
     */
    int getMaxId();

    /**
     * @return A free content id. Increases the maximum id by one.
     */
    int getFreeId();

    /**
     * @return An array list containing all active ids for this mod
     */
    ArrayList<Integer> getActiveIds();

    /**
     * Initializes a fresh import helper map.
     *
     * @see ImportHelperMap
     */
    void initializeImportHelperMap() throws ModProcessingException;

    /**
     * Resets the import helper map
     */
    void resetImportHelperMap();

    /**
     * @return The currently active import helper map
     * @throws ModProcessingException When the import helper map was not initialized
     */
    ImportHelperMap getImportHelperMap() throws ModProcessingException;

    /**
     * Used to get the name of the export image.
     * This should be used to make all exported images consistent.
     * Does not add file extension to name.
     *
     * @param identifier The identifier of this image. Will be appended to the end of the file
     * @param name       The name of the content
     */
    String getExportImageName(String identifier, String name);
    
    /**
     * Used to get the name of the import image.
     * This should be used to read the image name from the import map.
     * Reads whole input including file extension.
     * If the key does not exist `name` and `identifier` are used to construct the default image name.
     * 
     * @param key The map key under which the file name can be found
     * @param map The map that contains the content
     * @param identifier The identifier of this image. Will be appended to the end of the file
     * @param name The name of the content
     * @return The name of the image file that should be imported
     */
    String getImportImageName(String key, Map<String, String> map, String identifier, String name);
}
