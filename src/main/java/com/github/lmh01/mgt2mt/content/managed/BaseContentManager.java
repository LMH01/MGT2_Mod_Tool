package com.github.lmh01.mgt2mt.content.managed;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public interface BaseContentManager {

    /**
     * Adds the content to the game.
     * This includes editing the text file(s) and copying images if required.
     * Creates a backup beforehand.
     * A text area message is written.
     * Uses {@link BaseContentManager#editTextFiles(AbstractBaseContent, ContentAction)} to add the mod to the text file(s).
     * Uses {@link RequiresPictures#addPictures()} to add the pictures.
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
     * Edits the games text file(s) to add or remove the content.
     * @param content The content that should be removed or added
     * @param action The action that should be performed
     */
    void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException;

    /**
     * Opens the gui where the user can enter the values that are required to add the content to the game.
     * This function should invoke {@link BaseContentManager#addContent(AbstractBaseContent)} when the values have been
     * collected and the user accepts.
     */
    void openAddModGui() throws ModProcessingException;

    /**
     * Constructs the base content from the import map
     * @param map The map that contains the values from which the content should be constructed
     * @param assetsFolder The folder that contains the assets that are required to build the content
     * @return The newly constructed content
     * @throws ModProcessingException When required values are missing from the map and the base content could not be constructed
     */
    AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException;

    /**
     * Uses the game files to construct the base content.
     * @param name The name of the content for which a base content should be constructed
     * @return The newly constructed content
     * @throws ModProcessingException When the content could not be constructed
     */
    AbstractBaseContent constructContentFromName(String name) throws ModProcessingException;

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
     * Returns the default content file name
     */
    String getDefaultContentFileName();

    /**
     * @return The default content for this content type represented as string array
     */
    String[] getDefaultContent();

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
     * @return The export name of this content
     */
    String getExportType();

    /**
     * Creates a backup of the text file for this content
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
     * @param identifier The identifier of this image. Will be appended to the end of the file
     * @param name The name of the content
     */
    String getExportImageName(String identifier, String name);
}
