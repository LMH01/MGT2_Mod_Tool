package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.UpdateBranch;
import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportSettings {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSettings.class);

    /**
     * @param file The settings file that should be read
     * @return true if import was successful
     */
    public static boolean Import(File file) {
        try {
            LOGGER.info("Importing settings...");
            if (!file.exists()) {
                return false;
            }
            Toml toml = new Toml().read(file);
            Settings.mgt2Path = Paths.get(toml.getString("mgt2Path"));
            Settings.enableDisclaimerMessage = toml.getBoolean("enableDisclaimerMessage");
            Settings.enableDebugLogging = toml.getBoolean("enableDebugLogging");
            Settings.disableSafetyFeatures = toml.getBoolean("disableSafetyFeatures");
            Settings.enableCustomFolder = toml.getBoolean("enableCustomFolder");
            Settings.enableGenreNameTranslationInfo = toml.getBoolean("enableGenreNameTranslationInfo");
            Settings.enableGenreDescriptionTranslationInfo = toml.getBoolean("enableGenreDescriptionTranslationInfo");
            Settings.language = toml.getString("language");
            Settings.updateBranch = UpdateBranch.getUpdateBranch(toml.getString("updateBranch"));
            Settings.saveLogs = toml.getBoolean("saveLogs");
            Settings.enableExportStorage = toml.getBoolean("enableExportStorage");
            Settings.enableInitialBackupCheck = toml.getBoolean("enableInitialBackupCheck");
            return true;
        } catch (RuntimeException e) {
            LOGGER.info("Unable to import settings!:");
            e.printStackTrace();
            return false;
        }
    }
}