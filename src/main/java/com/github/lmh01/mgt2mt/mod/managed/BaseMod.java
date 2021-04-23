package com.github.lmh01.mgt2mt.mod.managed;

import java.io.IOException;

public interface BaseMod {
    /*Hierüber sollen dann aber auch irgendwie die ganzen menüs im Haupt-Fenster gesetzt werden,
     * wahrscheinlich dann über so eine Funktion hier:*/
    /**
     * Initializes the mod: Adds it to the main window and all the actions
     */
    void initializeMod();

    /**
     * Uses the editor to remove a mod
     */
    void removeMod(String name) throws IOException;

    /**
     * Uses the sharer to export a mod
     */
    void exportMod(String name, boolean exportAsRestorePoint);

    /**
     * Uses the sharer to import amod
     */
    void importMod(String importFolderPath, boolean showMessages) throws IOException;

    /**
     * Uses the analyzer to analyze the file
     */
    void analyze() throws IOException;

    /**
     * @return Returns a string that contains the compatible mod tool versions
     */
    String[] getCompatibleModToolVersions();
}
