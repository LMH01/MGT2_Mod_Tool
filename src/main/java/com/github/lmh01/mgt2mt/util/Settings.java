package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ExportSettings;
import com.github.lmh01.mgt2mt.dataStream.ImportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Settings {
    public static String mgt2FilePath = "";
    public static String mgt2DefaultFilePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";
    public static String languageToAdd = "";
    public static final String MGT2_MOD_MANAGER_PATH = System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//";
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);
    public static void resetSettings(){
        mgt2FilePath = mgt2DefaultFilePath;
        languageToAdd = "English";
        logger.info("Settings reset.");
    }
    public static void importCustomSettings(String filePath){
        ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt", true);
    }
    public static boolean importSettings(){
        return ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt", false);
    }
    public static  void exportSettings(){
        ExportSettings.export();
    }
    public static void setMgt2FilePath(boolean retry){
        boolean correctFolder = false;
        boolean breakLoop = false;
        while(!correctFolder && !breakLoop){
            if(!retry){
                breakLoop = true;
            }
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setDialogTitle("Choose Mad Games Tycoon 2 main folder:");
                fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                int return_value = fileChooser.showOpenDialog(null);
                if(return_value == 0){
                    mgt2FilePath = fileChooser.getSelectedFile().getPath();
                    File mgt2FilePathAsFile = new File(mgt2FilePath);
                    File[] filesInFolder = mgt2FilePathAsFile.listFiles();
                    for (int i = 0; i < filesInFolder.length; i++) {
                        if(filesInFolder[i].getName().equals("Mad Games Tycoon 2.exe")){
                            correctFolder = true;
                        }
                        System.out.println(filesInFolder[i].getName());
                    }
                    if(correctFolder){
                        logger.info("File path: " + mgt2FilePath);
                        JOptionPane.showMessageDialog(new Frame(), "Folder set.");
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), "This is not the MGT2 main folder!\nPlease select the correct folder!\nTipp: go into steam -> left click MGT2 -> Manage -> Browse local files.");
                        mgt2FilePath = mgt2DefaultFilePath;
                    }

                }else if(return_value == JFileChooser.CANCEL_OPTION){
                    mgt2FilePath = mgt2DefaultFilePath;
                    breakLoop = true;
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}