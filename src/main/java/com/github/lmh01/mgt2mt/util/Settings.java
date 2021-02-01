package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeSteamLibraries;
import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.data_stream.ImportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Settings {
    public static String mgt2FilePath = "";
    //public static final String MGT_2_DEFAULT_FILE_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";//TODO Remove "//" before release because this is the default folder.
    public static final String MGT_2_DEFAULT_FILE_PATH = "F:\\Games\\Steam Games\\steamapps\\common\\Mad Games Tycoon 2\\";
    public static String languageToAdd = "";
    public static final String MGT2_MOD_MANAGER_PATH = System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//";
    public static boolean enableDebugLogging = false;
    public static boolean disableSafetyFeatures = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    public static void resetSettings(){
        mgt2FilePath = MGT_2_DEFAULT_FILE_PATH;
        languageToAdd = "English";
        LOGGER.info("Settings reset.");
        enableDebugLogging = false;
        disableSafetyFeatures = false;
        saveSettings();
    }

    /**
     * Sets the settings
     * @param checkBoxDebugMode The checkbox that returns if debug mode should be enabled.
     * @param checkBoxDisableSafety The checkbox that returns if safety features should be disabled.
     */
    public static void setSettings(JCheckBox checkBoxDebugMode, JCheckBox checkBoxDisableSafety){
        Settings.enableDebugLogging = checkBoxDebugMode.isSelected();
        Settings.disableSafetyFeatures = checkBoxDisableSafety.isSelected();
        Settings.saveSettings();
        JOptionPane.showMessageDialog(new Frame(), "Settings saved.");
    }
    public static void importCustomSettings(String filePath){
        ImportSettings.Import(filePath, true);
    }
    public static boolean importSettings(){
        return ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt", false);
    }

    /**
     * Saves the current settings to file.
     */
    public static  void saveSettings(){
        ExportSettings.export();
    }

    public static void setMgt2FilePath(boolean showMessages){
        try {
            ArrayList<String> arrayListSteamLibraries = AnalyzeSteamLibraries.getSteamLibraries();
            boolean folderFound = false;
            for (String arrayListSteamLibrary : arrayListSteamLibraries) {
                LOGGER.info("Current Path: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                if (Utils.doesFoldercontainFile(arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\", "Mad Games Tycoon 2.exe")) {
                    LOGGER.info("Found MGT2 folder: " + arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                    mgt2FilePath = arrayListSteamLibrary + "\\steamapps\\common\\Mad Games Tycoon 2\\";
                    folderFound = true;
                    if (showMessages) {
                        JOptionPane.showMessageDialog(new Frame(), "Folder set.");
                    }
                }
            }
            if(!folderFound){
                boolean retry = true;
                while(retry){
                    String inputString = JOptionPane.showInputDialog(null, "The Mad Games Tycoon folder could not be found.\nPlease enter the path:", "Unable to find folder.", JOptionPane.WARNING_MESSAGE);
                    if(Utils.doesFoldercontainFile(inputString, "Mad Games Tycoon 2.exe")){
                        LOGGER.info("Found MGT2 folder: " + inputString + "\\steamapps\\common\\Mad Games Tycoon 2\\");
                        JOptionPane.showMessageDialog(new Frame(), "Folder set.");
                        mgt2FilePath = inputString;
                        retry = false;
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), "This is not the MGT2 main folder!\nPlease select the correct folder!\nHint: go into steam -> left click MGT2 -> Manage -> Browse local files -> copy the path from explorer.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}