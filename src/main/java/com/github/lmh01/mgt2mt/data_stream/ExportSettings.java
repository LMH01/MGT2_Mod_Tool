package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.ModManagerPaths;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ExportSettings {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSettings.class);

    /**
     * Exports the current settings to file
     *
     * @param file The file where the settings should be saved to
     */
    public static void export(File file) {
        try {
            LOGGER.info("Saving settings...");
            File directory = ModManagerPaths.MAIN.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            TomlWriter tomlWriter = new TomlWriter();
            Map<String, Object> map = new HashMap<>();
            map.put("mgt2Path", Settings.mgt2Path.toString());
            map.put("enableDisclaimerMessage", Settings.enableDisclaimerMessage);
            map.put("enableDebugLogging", Settings.enableDebugLogging);
            map.put("enableCustomFolder", Settings.enableCustomFolder);
            map.put("enableGenreNameTranslationInfo", Settings.enableGenreNameTranslationInfo);
            map.put("enableGenreDescriptionTranslationInfo", Settings.enableGenreDescriptionTranslationInfo);
            map.put("language", Settings.language);
            map.put("updateBranch", Settings.updateBranch.getName());
            map.put("saveLogs", Settings.saveLogs);
            map.put("enableExportStorage", Settings.enableExportStorage);
            map.put("enableInitialBackupCheck", Settings.enableInitialBackupCheck);
            map.put("writeTextAreaOutputToConsole", Settings.writeTextAreaOutputToConsole);
            for (Map.Entry<SafetyFeature, Boolean> entry : Settings.safetyFeatures.entrySet()) {
                map.put("safety_feature_" + entry.getKey().getIdentifier(), entry.getValue());
            }
            tomlWriter.write(map, file);
            LOGGER.info("Settings have been saved.");
            LogFile.write("Settings have been saved to file: " + file);
        } catch (IOException e) {
            LogFile.write("Something went wrong wile saving the settings: " + e.getMessage());
            LogFile.printStacktrace(e);
            e.printStackTrace();
        }
    }
}