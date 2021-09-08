package com.github.lmh01.mgt2mt.util.manager;

public enum ExportType {

    /**
     * Export all mods into one asset's folder and one .toml file and place them in the restore point folder
     */
    RESTORE_POINT("restore_point"),

    /**
     * Export all mods into separate folders and place them in the export folder
     */
    ALL_SINGLE("single_export"),

    /**
     * Export all mods into one asset's folder and one .toml file and place them in the export folder
     */
    ALL_BUNDLED("export_bundled");

    private final String string;

    ExportType(String string) {
        this.string = string;
    }

    public String getTypeName() {
        return string;
    }
}
