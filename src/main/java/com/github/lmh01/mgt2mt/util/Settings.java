package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeSteamLibraries;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.data_stream.ImportSettings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Settings {
    public static Path mgt2Path = null;
    public static Path steamLibraryFolder = null;
    private static final Path MGT2_MOD_MANAGER_PATH;
    public static boolean enableDebugLogging = false;
    public static boolean disableSafetyFeatures = false;
    public static boolean enableCustomFolder = false;
    public static boolean saveLogs = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    public static boolean mgt2FolderIsCorrect = false;
    public static boolean enableDisclaimerMessage = true;
    public static boolean enableGenreNameTranslationInfo = true;
    public static boolean enableGenreDescriptionTranslationInfo = true;
    public static String language = "English";
    public static String updateBranch = "Release";
    public static void resetSettings(){
        setMGT2Folder(false);
        setSettings(false, false, false, false, mgt2Path, true, true, true, "English", "Release", true);
        LOGGER.info("Settings have been reset.");
    }

    static {
        if (MadGamesTycoon2ModTool.isWindows()) {
            MGT2_MOD_MANAGER_PATH = Paths.get(System.getenv("APPDATA") + "/LMH01/MGT2_Mod_Manager");
        } else {
            MGT2_MOD_MANAGER_PATH = Paths.get(System.getProperty("user.home"), ".local", "share", "mgt2_mod_tool");
        }
    }

    /**
     * Sets the settings. This is the only function that can change the settings. The changed values will be written to the settings.txt file.
     * @param enableDebugLogging True when debug logging is on.
     * @param disableSafetyFeatures True when the safety features are disabled.
     * @param mgt2FilePath The custom folder path
     * @param enableCustomFolder True when the custom folder is enabled.
     */
    public static void setSettings(boolean showSuccessDialog, boolean enableDebugLogging, boolean disableSafetyFeatures, boolean enableCustomFolder, Path mgt2FilePath, boolean showDisclaimerMessage, boolean enableGenreNameTranslationInfo, boolean enableGenreDescriptionTranslationInfo, String language, String updateBranch, boolean saveLogs){
        Settings.enableDebugLogging = enableDebugLogging;
        Settings.disableSafetyFeatures = disableSafetyFeatures;
        Settings.enableCustomFolder = enableCustomFolder;
        Settings.enableDisclaimerMessage = showDisclaimerMessage;
        Settings.enableGenreNameTranslationInfo = enableGenreNameTranslationInfo;
        Settings.enableGenreDescriptionTranslationInfo = enableGenreDescriptionTranslationInfo;
        Settings.updateBranch = updateBranch;
        Settings.mgt2Path = mgt2FilePath;
        Settings.saveLogs = saveLogs;
        setLanguage(language);
        validateMGT2Folder(mgt2FilePath, false, true);
        ExportSettings.export();
        LogFile.write("Settings set:");
        LogFile.printCurrentSettings();
        if(showSuccessDialog){
            JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("settings.settingsSaved"));
        }
    }

    /**
     * Imports the settings from file.
     */
    public static void importSettings(){
        if(ImportSettings.Import(Paths.get(MGT2_MOD_MANAGER_PATH + "/settings.txt"))){
            LOGGER.info("Settings have been imported successfully.");
        }
        setLanguage(Settings.language);
    }

    /**
     * Sets the application language
     * @param language The language that should be set
     */
    public static void setLanguage(String language){
        if(language.equals("English")){
            I18n.INSTANCE.setCurrentLocale("en");
        }else if(language.equals("Deutsch")){
            I18n.INSTANCE.setCurrentLocale("de");
        }
        Settings.language = language;
    }

    /**
     * Tris to set the folder where mgt2 is located automatically.
     * If the folder is found all corresponding variables are updated and the settings are exported.
     * If the folder is not found the menus are locked and a message is shown.
     * @param showFolderDetectedMessage True when a message should be displayed if the folder has been found.
     */
    public static void setMGT2Folder(boolean showFolderDetectedMessage){
        Path mgt2Folder = getMGT2FilePath();
        if(mgt2Folder != null){
            steamLibraryFolder = mgt2Folder.getParent().getParent().getParent();
            Settings.mgt2Path = mgt2Folder;
            mgt2FolderIsCorrect = true;
            enableCustomFolder = false;
            if (showFolderDetectedMessage) {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically") + Settings.mgt2Path, I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically.windowTitle"), JOptionPane.INFORMATION_MESSAGE);
            }
            ExportSettings.export();
        }else{
            Settings.mgt2Path = null;
            mgt2FolderIsCorrect = false;
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("settings.mgt2FolderNotFound"));
        }
    }

    /**
     * Searches the steam libraries for the mgt2 folder.
     * Returns the folder path if found.
     * Returns nothing when folder is not found
     */
    public static Path getMGT2FilePath(){
        try {
            ArrayList<String> steamLibraries = AnalyzeSteamLibraries.getSteamLibraries();
            for (String string : steamLibraries) {
                Path currentPath = Paths.get(string, "steamapps", "common", "Mad Games Tycoon 2");
                LOGGER.info("Current Path: " + currentPath);
                if (validateMGT2Folder(currentPath, false, true)) {
                    LOGGER.info("Found MGT2 folder: " + currentPath);
                    return currentPath;
                }//TODO Check if this still works under windows if yes delete commented out part
                /*
                LOGGER.info("Current Path: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                if (validateMGT2Folder(arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\", false, true)) {
                    LOGGER.info("Found MGT2 folder: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                    return arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                }*/
            }
            LOGGER.info("MGT2 folder not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("MGT2 folder not found");
        return null;
    }

    /**
     * Checks if the mgt2 folder is valid
     * If the folder is invalid a message is shown to the user and the steam libraries are searched for mgt2.
     */
    public static void validateMGT2Folder(){
        if(!validateMGT2Folder(mgt2Path, false, true)){
            setMGT2Folder(false);
        }
    }

    /**
     * Checks if the mgt2 folder is valid
     * If the folder is invalid a message is shown to the user
     * @param path The folder that should be checked for MGT2.
     * @return Returns true when the folder is valid, returns false when the folder is invalid.
     */
    public static boolean validateMGT2Folder(Path path, boolean showMessage, boolean setFolderAvailability){
        boolean folderValid;
        LOGGER.info("Checking MGT2 folder validity: " + path);
        if(DataStreamHelper.doesFolderContainFile(path, "Mad Games Tycoon 2.exe")){
            LOGGER.info("MGT2 file path is valid.");
            mgt2FolderIsCorrect = true;
            folderValid = true;
        }else{
            LOGGER.info("MGT2 file path is invalid.");
            if(showMessage){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("settings.mgt2FolderNotFound"));
            }
            mgt2FolderIsCorrect = false;
            enableCustomFolder = false;
            folderValid =  false;
        }
        if(setFolderAvailability){
            WindowMain.setMGT2FolderAvailability(folderValid);
        }
        return folderValid;
    }

    /**
     * It is not recommended to use this function please use {@link ModManagerPaths} instead
     * @return The path to /mgt2_mod_manager
     */
    public static Path getMgt2ModManagerPath() {
        return MGT2_MOD_MANAGER_PATH;
    }
}