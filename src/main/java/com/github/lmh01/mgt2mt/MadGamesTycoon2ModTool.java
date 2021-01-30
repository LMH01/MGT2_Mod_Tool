package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class MadGamesTycoon2ModTool {
    private static final Logger logger = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    private static boolean settingsImported = false;
    public static void main(String[] args) {
        if(!settingsImported){
            if(Settings.importSettings()){
                logger.info("Settings have been imported.");
                if(!Settings.testFolderForMGT2Exe(Settings.mgt2FilePath)){
                    logger.info("The MGT2 file path is invalid.");
                    Settings.setMgt2FilePath(true);
                }
                settingsImported = true;
            }else{
                logger.info("Settings where not imported.");
                Settings.setMgt2FilePath(true);
            }
        }
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        MainWindow.createFrame();
    }
}