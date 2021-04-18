package com.github.lmh01.mgt2mt.data_stream.editor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface BaseEditor {

    /**
     * Analyzes the file that should be modified
     */
    void analyzeFile() throws IOException;

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

    /**
     * @return Returns a list where the original file content is stored in
     */
    List<Map<String, String>> getFileContent() throws IOException;

    /**
     * Writes the values that are stored in the map to the file
     */
    void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;

    /**
     * Returns -1 when name does not exist.
     * @param name The name
     * @return Returns the id for the specified name.
     */
    int getContentIdByName(String name);
}
