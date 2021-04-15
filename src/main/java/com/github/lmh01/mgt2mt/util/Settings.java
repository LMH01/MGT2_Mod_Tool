package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeSteamLibraries;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.data_stream.ImportSettings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Settings {
    public static String mgt2FilePath = "";
    public static String steamLibraryFolder = "";
    public static final String MGT2_MOD_MANAGER_PATH = System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//";
    public static boolean enableDebugLogging = false;
    public static boolean disableSafetyFeatures = false;
    public static boolean enableCustomFolder = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    public static boolean mgt2FolderIsCorrect = false;
    public static boolean enableDisclaimerMessage = true;
    public static boolean enableGenreNameTranslationInfo = true;
    public static boolean enableGenreDescriptionTranslationInfo = true;
    public static String automaticMGT2FilePath = ""; //This string is set when setMGT2Folder is called
    public static boolean searchedForAutomaticMGT2FilePath = false; //True when setMGT2Folder is called
    public static String language = "English";
    public static String updateBranch = "Release";
    public static void resetSettings(){
        setMGT2Folder(false);
        setSettings(false, false, false, false, "", true, true, true, "English", "Release");
        LOGGER.info("Settings have been reset.");
    }

    /**
     * Sets the settings. This is the only function that can change the settings. The changed values will be written to the settings.txt file.
     * @param enableDebugLogging True when debug logging is on.
     * @param disableSafetyFeatures True when the safety features are disabled.
     * @param customFolderPath The custom folder path
     * @param enableCustomFolder True when the custom folder is enabled.
     */
    public static void setSettings(boolean showSuccessDialog, boolean enableDebugLogging, boolean disableSafetyFeatures, boolean enableCustomFolder, String customFolderPath, boolean showDisclaimerMessage, boolean enableGenreNameTranslationInfo, boolean enableGenreDescriptionTranslationInfo, String language, String updateBranch){
        Settings.enableDebugLogging = enableDebugLogging;
        Settings.disableSafetyFeatures = disableSafetyFeatures;
        Settings.enableCustomFolder = enableCustomFolder;
        Settings.enableDisclaimerMessage = showDisclaimerMessage;
        Settings.enableGenreNameTranslationInfo = enableGenreNameTranslationInfo;
        Settings.enableGenreDescriptionTranslationInfo = enableGenreDescriptionTranslationInfo;
        Settings.updateBranch = updateBranch;
        setLanguage(language);
        Settings.mgt2FilePath = customFolderPath;
        ExportSettings.export();
        if(showSuccessDialog){
            JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("settings.settingsSaved"));
        }
    }

    /**
     * Imports the settings from file.
     */
    public static void importSettings(){
        if(ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt")){
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
     * Sets the folder where Mad Games Tycoon 2 is located.
     * For that all steam libraries are searched if they contain MadGamesTycoon2.exe
     * If the folder has been found the folder is set.
     * If the folder has not been found a error message is shown.
     * @param showFolderDetectedMessage True when a message should be displayed if the folder has been found.
     */
    /*public static void setMgt2Folder(boolean showFolderDetectedMessage){//TODO DELETE
        try {
            ArrayList<String> arrayListSteamLibraries = AnalyzeSteamLibraries.getSteamLibraries();
            mgt2FolderIsCorrect = false;
            searchedForAutomaticMGT2FilePath = true;
            boolean currentMgt2FolderIsCorrect = false;
            for (String arrayListSteamLibrary : arrayListSteamLibraries) {
                LOGGER.info("Current Path: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                if (validateMGT2Folder(arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\", false) && !currentMgt2FolderIsCorrect) {
                    LOGGER.info("Found MGT2 folder: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                    steamLibraryFolder = arrayListSteamLibrary;
                    automaticMGT2FilePath = arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                    mgt2FilePath = arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                    currentMgt2FolderIsCorrect = true;
                    mgt2FolderIsCorrect = true;
                    enableCustomFolder = false;
                    if (showFolderDetectedMessage) {
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically") + mgt2FilePath, I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically.windowTitle"), JOptionPane.INFORMATION_MESSAGE);
                    }
                    ExportSettings.export();
                }
            }
            if(!mgt2FolderIsCorrect){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("settings.mgt2FolderNotFound"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Tris to set the folder where mgt2 is located automatically.
     * If the folder is found all corresponding variables are updated and the settings are exported.
     * If the folder is not found nothing is done.
     * @param showFolderDetectedMessage True when a message should be displayed if the folder has been found.
     */
    public static void setMGT2Folder(boolean showFolderDetectedMessage){
        String mgt2Folder = getMGT2FilePath();
        if(!mgt2Folder.isEmpty()){
            steamLibraryFolder = mgt2Folder.replace("\\steamapps\\common\\Mad Games Tycoon 2\\", "");
            automaticMGT2FilePath = mgt2Folder;
            mgt2FilePath = mgt2Folder;
            mgt2FolderIsCorrect = true;
            enableCustomFolder = false;
            if (showFolderDetectedMessage) {
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically") + mgt2FilePath, I18n.INSTANCE.get("settings.mgt2FolderSetAutomatically.windowTitle"), JOptionPane.INFORMATION_MESSAGE);
            }
            ExportSettings.export();
        }else{
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("settings.mgt2FolderNotFound"));
        }
    }

    /**
     * Searches the steam libraries for the mgt2 folder.
     * Returns the folder if found.
     * Returns nothing when folder is not found
     */
    public static String getMGT2FilePath(){
        try {
            ArrayList<String> arrayListSteamLibraries = AnalyzeSteamLibraries.getSteamLibraries();
            boolean currentMgt2FolderIsCorrect = false;
            for (String arrayListSteamLibrary : arrayListSteamLibraries) {
                LOGGER.info("Current Path: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                if (validateMGT2Folder(arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\", false) && !currentMgt2FolderIsCorrect) {
                    LOGGER.info("Found MGT2 folder: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                    return arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                }
            }
            LOGGER.info("MGT2 folder not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("MGT2 folder not found");
        return "";
    }

    /**
     * Checks if the mgt2 folder is valid
     * If the folder is invalid a message is shown to the user
     */
    public static void validateMGT2Folder(){
        validateMGT2Folder(mgt2FilePath, true);
    }

    /**
     * Checks if the mgt2 folder is valid
     * If the folder is invalid a message is shown to the user
     * @param folderPath The folder that should be checked for MGT2.
     * @return Returns true when the folder is valid, returns false when the folder is invalid.
     */
    public static boolean validateMGT2Folder(String folderPath, boolean showMessage){
        boolean folderValid;
        LOGGER.info("Checking MGT2 folder validity: " + folderPath);
        if(DataStreamHelper.doesFolderContainFile(folderPath, "Mad Games Tycoon 2.exe")){
            LOGGER.info("MGT2 file path is valid.");
            mgt2FolderIsCorrect = true;
            folderValid = true;
        }else{
            LOGGER.info("MGT2 file path is invalid.");
            if(showMessage){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("settings.mgt2FolderNotFound"));
            }
            mgt2FolderIsCorrect = false;
            folderValid =  false;
        }
        WindowMain.setMGT2FolderAvailability(folderValid);
        return folderValid;
    }
}