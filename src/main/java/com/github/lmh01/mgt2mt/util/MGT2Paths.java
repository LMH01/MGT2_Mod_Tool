package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.util.settings.Settings;

import java.nio.file.Path;

public enum MGT2Paths {

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern
     */
    EXTERN(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/Text
     */
    TEXT(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/Text")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/Text/DATA
     */
    TEXT_DATA(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/Text/DATA")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/CompanyLogos
     */
    COMPANY_ICONS(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/CompanyLogos")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/Icons_Genres
     */
    GENRE_ICONS(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/Icons_Genres")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/Icons_Platforms
     */
    PLATFORM_ICONS(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/Icons_Platforms")),

    /**
     * Path to /Mad Games Tycoon 2_Data/Extern/Screenshots
     */
    GENRE_SCREENSHOTS(Settings.mgt2Path.resolve("Mad Games Tycoon 2_Data/Extern/Screenshots"));

    private final Path path;

    MGT2Paths(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
