package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.util.settings.Settings;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum ModManagerPaths {

    /**
     * Path to /mgt2_mod_tool/
     */
    MAIN(Settings.getMgt2ModManagerPath()),

    /**
     * Path to /mgt2_mod_tool/backup
     */
    BACKUP(Settings.getMgt2ModManagerPath().resolve("backup")),

    /**
     * Path to /mgt2_mod_tool/export
     */
    EXPORT(Settings.getMgt2ModManagerPath().resolve("export")),

    /**
     * Path to /mgt2_mod_tool/temp
     */
    TEMP(Settings.getMgt2ModManagerPath().resolve("temp")),

    /**
     * Path to /mgt2_mod_tool/download
     */
    DOWNLOAD(Settings.getMgt2ModManagerPath().resolve("download")),

    /**
     * Path to /mgt2_mod_tool/log
     */
    LOG(Settings.getMgt2ModManagerPath().resolve(Paths.get("log"))),

    /**
     * Path to /mgt2_mod_tool/mod_restore_point/current_restore_point
     */
    CURRENT_RESTORE_POINT(Settings.getMgt2ModManagerPath().resolve("mod_restore_point/current_restore_point")),

    /**
     * Path to /mgt2_mod_tool/mod_restore_point/old_restore_points
     */
    OLD_RESTORE_POINT(Settings.getMgt2ModManagerPath().resolve("mod_restore_point/old_restore_points"));

    private final Path path;

    ModManagerPaths(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public File toFile() {
        return path.toFile();
    }
}
