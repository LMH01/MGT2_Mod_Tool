package com.github.lmh01.mgt2mt.data_stream;

public interface BaseFunctions {
    /**
     * Sends a log message into the console
     * Preferably by a logger
     */
    void sendLogMessage(String string);

    /**
     * The type indicates what is written in the log, textArea, progress bar and JOptionPanes
     * Eg. Gameplay feature
     */
    String getType();
}
