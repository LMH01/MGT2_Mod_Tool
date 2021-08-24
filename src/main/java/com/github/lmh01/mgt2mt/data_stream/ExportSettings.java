package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ExportSettings {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSettings.class);
    public static void export() {
        try {
            File directory = Settings.MGT2_MOD_MANAGER_PATH.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //File file = new File(System.getProperty("user.home") + "/.local/share/mgt2_mod_tool/settings.txt");
            File file = Settings.MGT2_MOD_MANAGER_PATH.resolve("settings.txt").toFile();
            if (file.exists()) {
                file.delete();
            }
            LOGGER.info("Saving settings to file " + file.getPath());
            if(Settings.enableDebugLogging){
                LOGGER.info("Creating settings.txt");
                LOGGER.info(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
            }
            file.createNewFile();
            if(Settings.enableDebugLogging){ LOGGER.info("Successfully created the file settings.txt"); }
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            if(Settings.enableDebugLogging){ LOGGER.info("Writing to file..."); }
            pw.print(Settings.mgt2FilePath + "\n" +
                    Settings.enableDisclaimerMessage + "\n" +
                    Settings.enableDebugLogging + "\n" +
                    Settings.disableSafetyFeatures + "\n" +
                    Settings.steamLibraryFolder + "\n" +
                    Settings.enableCustomFolder + "\n" +
                    Settings.enableGenreNameTranslationInfo + "\n" +
                    Settings.enableGenreDescriptionTranslationInfo + "\n" +
                    Settings.language + "\n" +
                    Settings.updateBranch+ "\n" +
                    Settings.saveLogs);
            pw.close();
            if(Settings.enableDebugLogging){
                LOGGER.info(Settings.mgt2FilePath);
                LOGGER.info(Settings.enableDebugLogging + "");
                LOGGER.info(Settings.disableSafetyFeatures + "");
                LOGGER.info("Writing to file successful!");
            }
            LOGGER.info("Settings have been saved.");
            LogFile.write("Settings have been saved to file: " + file.getPath());
        } catch (Exception e) {
            LogFile.write("Something went wrong wile saving the settings: ");
            LogFile.printStacktrace(e);
            e.printStackTrace();
        }

    }
}