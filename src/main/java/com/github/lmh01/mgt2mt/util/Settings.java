package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeSteamLibraries;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.data_stream.ImportSettings;
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
    public static boolean madGamesTycoonFolderIsCorrect = false;
    public static boolean enableDisclaimerMessage = true;
    public static boolean enableGenreNameTranslationInfo = true;
    public static boolean enableGenreDescriptionTranslationInfo = true;
    public static String language = "English";
    public static String updateBranch = "Release";
    public static void resetSettings(){
        setMgt2Folder(false);
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
        if(!customFolderPath.isEmpty()){
            Settings.mgt2FilePath = customFolderPath;
        }
        ExportSettings.export();
        if(showSuccessDialog){
            JOptionPane.showMessageDialog(new Frame(), "Settings saved.");
        }
    }

    /**
     * Imports the settings from file.
     * @return Returns true if settings have been imported successfully.
     */
    public static boolean importSettings(){
        boolean importSuccessful = ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt");
        setLanguage(Settings.language);
        return importSuccessful;
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
     * @param showMessages True when a message should be displayed if the folder has been found.
     */
    public static void setMgt2Folder(boolean showMessages){
        try {
            ArrayList<String> arrayListSteamLibraries = AnalyzeSteamLibraries.getSteamLibraries();
            madGamesTycoonFolderIsCorrect = false;
            for (String arrayListSteamLibrary : arrayListSteamLibraries) {
                LOGGER.info("Current Path: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                if (DataStreamHelper.doesFolderContainFile(arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\", "Mad Games Tycoon 2.exe") && !madGamesTycoonFolderIsCorrect) {
                    LOGGER.info("Found MGT2 folder: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                    steamLibraryFolder = arrayListSteamLibrary;
                    mgt2FilePath = arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                    madGamesTycoonFolderIsCorrect = true;
                    enableCustomFolder = false;
                    if (showMessages) {
                        JOptionPane.showMessageDialog(new Frame(), "Mad Games Tycoon 2 Folder has been set automatically.\n\nLocation:\n" + mgt2FilePath, "Folder detected automatically", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if(!madGamesTycoonFolderIsCorrect){
                JOptionPane.showMessageDialog(null, "The Mad Games Tycoon folder could not be detected.\n\nPlease go into the settings, select \"Manual\" as file folder and choose the path to the \"Mad Games Tycoon 2.exe\" file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}