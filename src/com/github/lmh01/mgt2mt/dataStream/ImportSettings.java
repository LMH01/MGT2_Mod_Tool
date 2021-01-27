package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.helpers.DebugHelper;
import com.github.lmh01.mgt2mt.util.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImportSettings {
    public static void Import(String fileLocation, boolean importCustomSettings) {
        DebugHelper.sendInfo("Starting settings import process...");

        try {
            DebugHelper.sendInfo("Scanning for File '" + fileLocation + "'...");
            File file = new File(fileLocation);
            Scanner scanner = new Scanner(file);
            DebugHelper.sendInfo("Beginning to import settings from file: " + file);
            int setting = 1;

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                switch(setting) {
                    case 1:
                        Settings.mgt2FilePath = line; break;
                    case 2:
                        Settings.languageToAdd = line; break;
                }
                DebugHelper.sendInfo("Imported Setting (" + setting + "): " + line);
                ++setting;
            }

            DebugHelper.sendInfo("Import Complete!");
            if (importCustomSettings) {
                JOptionPane.showMessageDialog(new Frame(), "Settings loaded Successfully!");
            }

            scanner.close();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
            DebugHelper.sendInfo("Unable to import settings: File not found! Using default settings!");
            Settings.resetSettings();
        }

    }
}
