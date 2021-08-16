package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.util.Locale;

public class MadGamesTycoon2ModTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    public static final String VERSION = "2.2.0a-dev";//Version numbers that include "dev" are not checked for updates / tool will notify if update is available
    public static final String CURRENT_RELEASE_VERSION = "2.1.2";//When this version number has been detected as the newest release version the update available message is held back
    public static void main(String[] args){
        Settings.importSettings();
        if(Settings.language.equals("English")){
            Locale locale = new Locale("en","US");//Sets the language to english
            JOptionPane.setDefaultLocale(locale);
        }else if(Settings.language.equals("Deutsch")){
            Locale locale = new Locale("de","DE");//Sets the language to english
            JOptionPane.setDefaultLocale(locale);
        }
        LogFile.startLogging();
        Runtime.getRuntime().addShutdownHook(ThreadHandler.getShutdownHookThread());
        ModManager.initializeMods();
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        WindowMain.createFrame();
        Settings.validateMGT2Folder();
        if(Settings.mgt2FolderIsCorrect){
            Backup.createInitialBackup();//Creates a initial backup when it does not already exist.
        }else{
            LOGGER.info("Initial backups where not created/completed because the mgt2 folder is invalid");
        }
        ThreadHandler.threadPerformStartTasks().start();
    }
}