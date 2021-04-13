package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class LogFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFile.class);
    private static BufferedWriter bw;

    static {
        try {
            File logFile = new File(Settings.MGT2_MOD_MANAGER_PATH + "latest.log");
            if(logFile.exists()){
                logFile.delete();
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a log file and writes everything that is sent to {@link com.github.lmh01.mgt2mt.util.helper.TextAreaHelper} to this file
     */
    public static void startLogging(){
        try {
            LOGGER.info("Logging to file is now active!");
            bw.write("Mod tool version: " + MadGamesTycoon2ModTool.VERSION);
            bw.write(System.getProperty("line.separator"));
            bw.write("Current date: " + Utils.getCurrentDateTime());
            bw.write(System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the input string into the log file
     * @param string
     */
    public static void write(String string){
        try {
            bw.write(Utils.getCurrentDateTime() + ": " + string);
            bw.write(System.getProperty("line.separator"));
        } catch (IOException ignored) {

        }
    }

    /**
     * Closes the buffered reader.
     */
    public static void stopLogging(){
        try {
            bw.close();
            LOGGER.info("Logging to file has been stopped!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}