package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public interface BaseEditor {

    /**
     * Sends a log message into the console
     * Preferably by a logger
     */
    void sendLogMessage(String string);

    /**
     * The analyzer type indicates what is written in the log, textArea, progress bar and JOptionPanes
     * Eg. Gameplay feature
     */
    String getEditorType();

    /**
     * @return Returns the file that should be edited
     */
    File getFileToEdit();

    /**
     * @return Returns the char set in which the file should be written
     */
    Charset getCharset();
}
