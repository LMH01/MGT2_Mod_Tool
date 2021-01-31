package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.data_stream.ImportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Settings {
    public static String mgt2FilePath = "";
    public static final String MGT_2_DEFAULT_FILE_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";
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
        exportSettings();
    }

    /**
     * Sets the settings
     * @param checkBoxDebugMode The checkbox that returns if debug mode should be enabled.
     * @param checkBoxDisableSafety The checkbox that returns if safety features should be disabled.
     */
    public static void setSettings(JCheckBox checkBoxDebugMode, JCheckBox checkBoxDisableSafety){
        Settings.enableDebugLogging = checkBoxDebugMode.isSelected();
        Settings.disableSafetyFeatures = checkBoxDisableSafety.isSelected();
        Settings.exportSettings();
        JOptionPane.showMessageDialog(new Frame(), "Settings saved.");
    }
    public static void importCustomSettings(String filePath){
        ImportSettings.Import(filePath, true);
    }
    public static boolean importSettings(){
        return ImportSettings.Import(MGT2_MOD_MANAGER_PATH + "//settings.txt", false);
    }
    public static  void exportSettings(){
        ExportSettings.export();
    }
    public static void setMgt2FilePath(boolean retry){
        JOptionPane.showMessageDialog(null, "To continue please select the Mad Games Tycoon 2 main folder.\n(The folder that contains the .exe file)\n\nHint: go into steam -> left click MGT2 -> Manage -> Browse local files.\n\nNote:\n- If you need help you can hover over the most components in this tool to reveal a tooltip.\n- If you encounter a bug please report it over on github.\n (Github can be accessed fia the main menu)", "Welcome to MGT2 Mod Tool", JOptionPane.INFORMATION_MESSAGE);
        boolean correctFolder = false;
        boolean breakLoop = false;
        File mgt2DefaultFilePathFile = new File(MGT_2_DEFAULT_FILE_PATH);
        if(mgt2DefaultFilePathFile.exists()){
            if(Utils.doesFoldercontainFile(mgt2FilePath, "Mad Games Tycoon 2.exe")){
                JOptionPane.showMessageDialog(new Frame(), "MGT2 folder has been detected.");
            }
        }else{
            while(!correctFolder && !breakLoop){
                if(!retry){
                    breakLoop = true;
                }
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
                    JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                    fileChooser.setDialogTitle("Choose 'Mad Games Tycoon 2' main folder:");
                    fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                    int return_value = fileChooser.showOpenDialog(null);
                    if(return_value == 0){
                        mgt2FilePath = fileChooser.getSelectedFile().getPath();
                        if(Utils.doesFoldercontainFile(mgt2FilePath, "Mad Games Tycoon 2.exe")){
                            LOGGER.info("File path: " + mgt2FilePath);
                            correctFolder = true;
                            JOptionPane.showMessageDialog(new Frame(), "Folder set.");
                            try{
                                Backup.createFullBackup(true);
                                JOptionPane.showMessageDialog(null, "The initial backup has been created successfully.", "Initial backup", JOptionPane.INFORMATION_MESSAGE);
                            }catch(IOException e){
                                JOptionPane.showMessageDialog(null, "The initial backup was not created:\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + e.getMessage(), "Unable to backup file", JOptionPane.ERROR_MESSAGE);
                                ChangeLog.addLogEntry(7, e.getMessage());
                            }
                        }else{
                            JOptionPane.showMessageDialog(new Frame(), "This is not the MGT2 main folder!\nPlease select the correct folder!\nHint: go into steam -> left click MGT2 -> Manage -> Browse local files.");
                            mgt2FilePath = MGT_2_DEFAULT_FILE_PATH;
                        }
                    }else if(return_value == JFileChooser.CANCEL_OPTION){
                        mgt2FilePath = MGT_2_DEFAULT_FILE_PATH;
                        breakLoop = true;
                    }
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}