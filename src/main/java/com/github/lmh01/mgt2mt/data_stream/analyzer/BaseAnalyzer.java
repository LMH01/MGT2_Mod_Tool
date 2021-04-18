package com.github.lmh01.mgt2mt.data_stream.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaseAnalyzer {

    /**
     * returns that analyzed file
     */
    List<Map<String, String>> getFileContent ();

    /**
     * analyzes the file
     */
    void analyzeFile() throws IOException;

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
