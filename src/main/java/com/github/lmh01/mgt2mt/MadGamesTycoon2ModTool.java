package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.OSType;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Locale;

public class MadGamesTycoon2ModTool {
    public static final OSType OS_TYPE;
    private static final Logger LOGGER = LoggerFactory.getLogger(MadGamesTycoon2ModTool.class);
    public static final String VERSION = "4.7.0";//Version numbers that include "dev" are not checked for updates / tool will notify if update is available
    public static final String CURRENT_RELEASE_VERSION = "4.7.0";//When this version number has been detected as the newest release version the update available message is held back

    static {
        if (System.getProperty("os.name").contains("Linux")) {
            OS_TYPE = OSType.LINUX;
        } else {
            OS_TYPE = OSType.WINDOWS;
        }
        LOGGER.info("MGT2_Mod_Tool is running under " + System.getProperty("os.name"));
    }

    public static void main(String[] args) {
        Settings.importSettings();
        if (Settings.language.equals("English")) {
            Locale locale = new Locale("en", "US");//Sets the language to english
            JOptionPane.setDefaultLocale(locale);
        } else if (Settings.language.equals("Deutsch")) {
            Locale locale = new Locale("de", "DE");//Sets the language to english
            JOptionPane.setDefaultLocale(locale);
        }
        LogFile.startLogging();
        Runtime.getRuntime().addShutdownHook(ThreadHandler.getShutdownHookThread());
        ContentAdministrator.initializeContentManagers();
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        WindowMain.createFrame();
        Settings.validateMGT2Folder();
        if (Settings.mgt2FolderIsCorrect) {
            try {
                ThemeManager.INSTANCE.writeCustomThemeFile();
            } catch (ModProcessingException e) {
                TextAreaHelper.printStackTrace(e);
                e.printStackTrace();
            }
            Backup.createInitialBackup();//Creates an initial backup when it does not already exist.
        } else {
            LOGGER.info("Initial backups where not created/completed because the mgt2 folder is invalid");
        }
        ThreadHandler.threadPerformStartTasks().start();
    }

    /**
     * @return True if os is Linux
     */
    public static boolean isWindows() {
        return OS_TYPE.equals(OSType.WINDOWS);
    }
}
