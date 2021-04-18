package com.github.lmh01.mgt2mt.data_stream.analyzer;

import java.io.File;
import java.io.IOException;

public interface BaseAnalyzer {

    /**
     * analyzes the file
     */
    void analyzeFile() throws IOException;


    /**
     * Returns the currently highest id
     */
    int getMaxId();

    /**
     * Sets the maximum id
     */
    void setMaxId(int id);

    /**
     * The file that should be analyzed
     */
    File getFileToAnalyze();

    /**
     * The analyzer type indicates what is written in the log, textArea, progress bar and JOptionPanes
     * Eg. Gameplay feature
     */
    String getAnalyzerType();

    /**
     * The translation key that is specific to the analyzer
     * Eg. gameplayFeature
     */
    String getMainTranslationKey();

    /**
     * The String containing the default content file
     */
    String getDefaultContentFile();
}
