package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ImportSettings{
    private static Logger logger = LoggerFactory.getLogger(ImportSettings.class);

    public static boolean Import(String fileLocation, boolean importCustomSettings) {
        logger.info("Starting settings import process...");

        try {
            logger.info("Scanning for File '" + fileLocation + "'...");
            File file = new File(fileLocation);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            logger.info("Beginning to import settings from file: " + file);
            int setting = 1;
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                switch(setting) {
                    case 1:
                        Settings.mgt2FilePath = currentLine; break;
                    case 2:
                        Settings.languageToAdd = currentLine; break;
                    case 3:
                        if(currentLine.equals("true")){
                            Settings.enableDebugLogging = true;
                        }else{
                            Settings.enableDebugLogging = false;
                        }
                    case 4:
                        if(currentLine.equals("true")){
                            Settings.disableSafetyFeatures = true;
                        }else{
                            Settings.disableSafetyFeatures = false;
                        }
                }
                logger.info("Imported Setting (" + setting + "): " + currentLine);
                ++setting;
            }

            logger.info("Import Complete!");
            if (importCustomSettings) {
                JOptionPane.showMessageDialog(new Frame(), "Settings loaded Successfully!");
            }
            reader.close();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException var6) {
            var6.printStackTrace();
            logger.info("Unable to import settings: File not found! Using default settings!");
            Settings.resetSettings();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}