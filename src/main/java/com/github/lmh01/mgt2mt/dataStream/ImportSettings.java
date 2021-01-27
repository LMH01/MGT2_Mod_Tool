package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImportSettings{
    private static Logger logger = LoggerFactory.getLogger(ImportSettings.class);

    public static void Import(String fileLocation, boolean importCustomSettings) {
        logger.info("Starting settings import process...");

        try {
            logger.info("Scanning for File '" + fileLocation + "'...");
            File file = new File(fileLocation);
            Scanner scanner = new Scanner(file);
            logger.info("Beginning to import settings from file: " + file);
            int setting = 1;

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                switch(setting) {
                    case 1:
                        Settings.mgt2FilePath = line; break;
                    case 2:
                        Settings.languageToAdd = line; break;
                }
                logger.info("Imported Setting (" + setting + "): " + line);
                ++setting;
            }

            logger.info("Import Complete!");
            if (importCustomSettings) {
                JOptionPane.showMessageDialog(new Frame(), "Settings loaded Successfully!");
            }

            scanner.close();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
            logger.info("Unable to import settings: File not found! Using default settings!");
            Settings.resetSettings();
        }

    }
}