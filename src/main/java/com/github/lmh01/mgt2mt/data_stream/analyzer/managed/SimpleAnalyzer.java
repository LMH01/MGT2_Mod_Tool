package com.github.lmh01.mgt2mt.data_stream.analyzer.managed;

public interface SimpleAnalyzer {
    /**
     * @return Returns an simple analyzer
     */
    AbstractSimpleAnalyzer getAnalyzer();

    /**
     * @return Returns the custom content string that has been computed previously
     */
    String[] getFinishedCustomContentString();

    /**
     * Sets the custom content string that should be returned when {@link SimpleAnalyzer#getFinishedCustomContentString()} is called.
     */
    void setFinishedCustomContentString(String[] customContent);

    /**
     * Replaces the input string and returns the replaced string
     */
    String getReplacedLine(String inputString);
}
