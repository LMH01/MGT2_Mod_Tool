package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public class MadGamesTycoon2ModTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    public static final String VERSION = "1.0.0";
    private static boolean settingsImported = false;
    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        UpdateChecker.checkForUpdates(false);
        MainWindow.createFrame();
        if(!settingsImported){
            if(Settings.importSettings()){
                LOGGER.info("Settings have been imported.");
                if(!Utils.doesFoldercontainFile(Settings.mgt2FilePath, "Mad Games Tycoon 2.exe")){

                    LOGGER.info("The MGT2 file path is invalid.");
                    Settings.setMgt2FilePath(true);
                }
                settingsImported = true;
            }else{
                LOGGER.info("Settings where not imported.");
                Settings.setMgt2FilePath(true);
            }
        }
        Settings.saveSettings();
    }
}