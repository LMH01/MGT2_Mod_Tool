package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ExportSettings {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSettings.class);
    public static void export() {
        try {
            String directoryName = System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//";
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryName + "/" + "settings.txt");
            LOGGER.info("Saving settings...");
            if(Settings.enableDebugLogging){
                LOGGER.info("Creating settings.txt");
                LOGGER.info(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
            }
            file.createNewFile();
            if(Settings.enableDebugLogging){ LOGGER.info("Successfully created the file settings.txt"); }
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            if(Settings.enableDebugLogging){ LOGGER.info("Writing to file..."); }
            pw.print(Settings.mgt2FilePath + "\n" +
                    Settings.enableAddGenreWarning + "\n" +
                    Settings.enableDebugLogging + "\n" +
                    Settings.disableSafetyFeatures + "\n" +
                    Settings.steamLibraryFolder + "\n" +
                    Settings.enableCustomFolder + "\n" +
                    Settings.enableGenreNameTranslationInfo + "\n" +
                    Settings.enableGenreDescriptionTranslationInfo);
            pw.close();
            if(Settings.enableDebugLogging){
                LOGGER.info(Settings.mgt2FilePath);
                LOGGER.info(Settings.enableDebugLogging + "");
                LOGGER.info(Settings.disableSafetyFeatures + "");
                LOGGER.info("Writing to file successful!");
            }
            LOGGER.info("Settings have been saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}