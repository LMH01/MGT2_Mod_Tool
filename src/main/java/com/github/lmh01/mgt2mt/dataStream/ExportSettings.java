package com.github.lmh01.mgt2mt.dataStream;

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
            logger.debug("Creating settings.txt");
            logger.debug(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
            file.createNewFile();
            logger.debug("Successfully created the file settings.txt");
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            logger.debug("Writing to file...");
            pw.print(Settings.mgt2FilePath + "\n" +
                    Settings.languageToAdd);
            pw.close();
            logger.debug(Settings.mgt2FilePath + "\n" + Settings.languageToAdd);
            logger.debug("Writing to file successfull!");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}