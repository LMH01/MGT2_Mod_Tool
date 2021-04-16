package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFile.class);
    private static BufferedWriter bw;
    private static File logFile;

    static {
        try {
            logFile = new File(Settings.MGT2_MOD_MANAGER_PATH + Utils.getCurrentDateTime() + ".log");
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
            File latestLog = new File(Settings.MGT2_MOD_MANAGER_PATH + "latest.log");
            if(latestLog.exists()){
                latestLog.delete();
            }
            Files.copy(Paths.get(logFile.getPath()), Paths.get(latestLog.getPath()));
            if(Settings.saveLogs){
                File storageFile = new File(Settings.MGT2_MOD_MANAGER_PATH + "//logs//" + logFile.getName());
                File storageDirectory = new File(Settings.MGT2_MOD_MANAGER_PATH + "//logs//");
                if(!storageDirectory.exists()){
                    storageDirectory.mkdirs();
                }
                Files.move(Paths.get(logFile.getPath()), Paths.get(storageFile.getPath()));
            }
            logFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
