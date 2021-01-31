package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ExportSettings {
    private static Logger logger = LoggerFactory.getLogger(ExportSettings.class);
    public static void export() {
        try {
            String directoryName = System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//";
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryName + "/" + "settings.txt");
            logger.info("Saving settings...");
            if(Settings.enableDebugLogging){
                logger.info("Creating settings.txt");
                logger.info(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
            }
            file.createNewFile();
            if(Settings.enableDebugLogging){ logger.info("Successfully created the file settings.txt"); }
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            if(Settings.enableDebugLogging){ logger.info("Writing to file..."); }
            pw.print(Settings.mgt2FilePath + "\n" +
                    Settings.languageToAdd + "\n" +
                    Settings.enableDebugLogging + "\n" +
                    Settings.disableSafetyFeatures);
            pw.close();
            if(Settings.enableDebugLogging){
                logger.info(Settings.mgt2FilePath);
                logger.info(Settings.languageToAdd);
                logger.info(Settings.enableDebugLogging + "");
                logger.info(Settings.disableSafetyFeatures + "");
                logger.info("Writing to file successful!");
            }
            logger.info("Settings have been saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}