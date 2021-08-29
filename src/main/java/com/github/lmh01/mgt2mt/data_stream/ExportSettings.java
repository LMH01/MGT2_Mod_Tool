package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ExportSettings {//TODO Rewrite to use .toml file
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSettings.class);
    public static void export() {
        try {
            File directory = ModManagerPaths.MAIN.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = ModManagerPaths.MAIN.getPath().resolve("settings.txt").toFile();
            if (file.exists()) {
                file.delete();
            }
            LOGGER.info("Saving settings to file " + file.getPath());
            if(Settings.enableDebugLogging){
                LOGGER.info("Creating settings.txt");
            }
            file.createNewFile();
            if(Settings.enableDebugLogging){ LOGGER.info("Successfully created the file settings.txt"); }
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            if(Settings.enableDebugLogging){ LOGGER.info("Writing to file..."); }
            pw.print(Settings.mgt2Path + "\n" +
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
                LOGGER.info(Settings.mgt2Path.toString());
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