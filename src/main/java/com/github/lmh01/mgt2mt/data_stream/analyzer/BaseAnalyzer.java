package com.github.lmh01.mgt2mt.data_stream.analyzer;

import java.io.IOException;

public interface BaseAnalyzer {

    /**
     * analyzes the file
     */
    void analyzeFile() throws IOException;

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
     * The String[] containing all default values
     */
    String[] getDefaultContent();
}
