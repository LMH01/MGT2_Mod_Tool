package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ExportSettings;
import com.github.lmh01.mgt2mt.dataStream.ImportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class Settings {
    public static String mgt2FilePath = "";
    public static String languageToAdd = "";
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);
    public static void resetSettings(){
        mgt2FilePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";
        languageToAdd = "English";
        logger.info("Settings reset.");
    }
    public static void importCustomSettings(String filePath){
        ImportSettings.Import(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//settings.txt", true);
    }
    public static void importSettings(){
        ImportSettings.Import(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//settings.txt", false);
    }
    public static  void exportSettings(){
        ExportSettings.export();
    }
    public static void setMgt2FilePath(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == 0){
                mgt2FilePath = fileChooser.getSelectedFile().getPath();
                logger.info("File path: " + mgt2FilePath);
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}