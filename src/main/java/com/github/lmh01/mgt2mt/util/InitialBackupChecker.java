package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InitialBackupChecker {

    private static final Path initialBackupVersion = ModManagerPaths.MAIN.getPath().resolve("initial_backup_version.toml");

    /**
     * Checks if the initial backup is up-to-date
     *
     * @return True if initial backup is up-to-date. False otherwise
     */
    public static boolean checkIfUpToDate() throws IOException {
        if (Files.exists(initialBackupVersion)) {
            Toml toml = new Toml().read(initialBackupVersion.toFile());
            return toml.getLong("initialBackupVersion") == (long) getLastUpdatedInt();
        } else {
            setInitialBackupVersion(getLastUpdatedInt());
            return true;
        }
    }

    /**
     * Updates the initial backup version
     */
    public static void updateInitialBackupVersion() {
        try {
            setInitialBackupVersion(getLastUpdatedInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the initial backup version and writes it to the file
     *
     * @param lastUpdated The version number that should be written
     */
    private static void setInitialBackupVersion(int lastUpdated) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("initialBackupVersion", lastUpdated);
        TomlWriter tomlWriter = new TomlWriter();
        tomlWriter.write(map, initialBackupVersion.toFile());
    }

    /**
     * Analyses the appmanifest_1342330.acf file
     *
     * @return The value that is stored in the field "LastUpdted"
     */
    private static int getLastUpdatedInt() throws IOException {
        File file = Settings.mgt2Path.getParent().getParent().resolve("appmanifest_1342330.acf").toFile();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("LastUpdated")) {
                return Integer.parseInt(line.replaceAll("[^0-9]", ""));
            }
        }
        return 0;
    }
}
