package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

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
     * Uses the sharer to import a mod
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

    /**
     * This function is called when the add mod button is clicked in the menu
     */
    void menuActionAddMod();

    ArrayList<JMenuItem> getInitialModMenuItems();

    /**
     * The translation key that is specific to the analyzer
     * Eg. gameplayFeature
     */
    String getMainTranslationKey();

    /**
     * @return Returns the mod name in plural
     * Eg. Genres, Engine features
     */
    String getTypePlural();

    /**
     * Sets the availability of the JMenuItem in the main window.
     */
    void setMainMenuButtonAvailability();

    JMenuItem getExportMenuItem();

    /**
     * Returns the export/import file name under which the mod can be found
     * Eg. gameplayFeature.txt, engineFeature.txt
     */
    String getFileName();
}
