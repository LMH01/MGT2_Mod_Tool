package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

public class MadGamesTycoon2ModTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    public static final String VERSION = "1.9.0-dev";//Version numbers that include "dev" are not checked for updates / tool will notify if update is available
    public static final String CURRENT_RELEASE_VERSION = "1.8.3b";//When this version number has been detected as the newest release version the update available message is held back
    public static void main(String[] args) throws IOException {
        if(Settings.importSettings()){
            LOGGER.info("Settings have been imported.");
            if(!DataStreamHelper.doesFolderContainFile(Settings.mgt2FilePath, "Mad Games Tycoon 2.exe")){
                LOGGER.info("The MGT2 file path is invalid.");
                Settings.setMgt2Folder(false);
            }
            Settings.madGamesTycoonFolderIsCorrect = true;
            //If settings do not exist they will automatically be reset inside ImportSettings.import()
        }
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        UpdateChecker.checkForUpdates(false);
        Locale locale = new Locale("en","US");//Sets the language to english
        JOptionPane.setDefaultLocale(locale);
        WindowMain.createFrame();
        Backup.createInitialBackup();//Creates a initial backup when it does not already exist.
        AnalyzeExistingGenres.analyzeGenreFile();
        WindowMain.checkActionAvailability();
    }
}