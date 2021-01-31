package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ChangeLog;
import com.github.lmh01.mgt2mt.dataStream.ExportSettings;
import com.github.lmh01.mgt2mt.dataStream.ImportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Settings {
    public static String mgt2FilePath = "";
    public static String mgt2DefaultFilePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Mad Games Tycoon 2\\";
    public static String languageToAdd = "";
    public static final String MGT2_MOD_MANAGER_PATH = System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//";
    public static boolean enableDebugLogging = false;
    public static boolean disableSafetyFeatures = false;
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);
    public static void resetSettings(){
        mgt2FilePath = mgt2DefaultFilePath;
        languageToAdd = "English";
        logger.info("Settings reset.");
        enableDebugLogging = false;
        disableSafetyFeatures = false;
        exportSettings();
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
        JOptionPane.showMessageDialog(null, "To continue please select the Mad Games Tycoon 2 main folder.\n(The folder that contains the .exe file)\n\nHint: go into steam -> left click MGT2 -> Manage -> Browse local files.\n\nNote:\n- If you need help you can hover over the most components in this tool to reveal a tooltip.\n- If you encounter a bug please report it over on github.\n (Github can be accessed fia the Other menu)", "Welcome to MGT2 Mod Tool", JOptionPane.INFORMATION_MESSAGE);
        boolean correctFolder = false;
        boolean breakLoop = false;
        File mgt2DefaultFilePathFile = new File(mgt2DefaultFilePath);
        if(mgt2DefaultFilePathFile.exists()){
            if(testFolderForMGT2Exe(mgt2DefaultFilePath)){
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
                    fileChooser.setDialogTitle("Choose 'Mad Games Tycoon 2.exe':");
                    fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                    int return_value = fileChooser.showOpenDialog(null);
                    if(return_value == 0){
                        mgt2FilePath = fileChooser.getSelectedFile().getPath();
                        if(testFolderForMGT2Exe(mgt2FilePath)){
                            logger.info("File path: " + mgt2FilePath);
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

    /**
     * @param mgt2Folder The folder that should be tested if its the mgt2 folder.
     * @return Returns true when the input file is the MGT2 folder.
     */
    public static boolean testFolderForMGT2Exe(String mgt2Folder){
        File file = new File(mgt2Folder);
        if(file.exists()){
            File[] filesInFolder = file.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if(filesInFolder[i].getName().equals("Mad Games Tycoon 2.exe")){
                    return true;
                }
                System.out.println(filesInFolder[i].getName());
            }
        }
        return false;
    }
}