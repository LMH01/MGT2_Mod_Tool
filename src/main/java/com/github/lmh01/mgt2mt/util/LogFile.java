package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class LogFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFile.class);
    private static BufferedWriter bw;
    public static File logFile;

    static {
        try {
            logFile = ModManagerPaths.MAIN.getPath().resolve(Utils.getCurrentDateTime() + ".log").toFile();
            File mainFolder = ModManagerPaths.MAIN.getPath().toFile();
            if (!mainFolder.exists()) {
                mainFolder.mkdirs();
            }
            if (logFile.exists()) {
                logFile.delete();
            }
            logFile.createNewFile();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a log file and writes everything that is sent to {@link com.github.lmh01.mgt2mt.util.helper.TextAreaHelper} to this file
     */
    public static void startLogging() {
        try {
            LOGGER.info("Logging to file is now active!");
            bw.write("Mod tool version: " + MadGamesTycoon2ModTool.VERSION);
            bw.write(System.getProperty("line.separator"));
            bw.write("OS: " + System.getProperty("os.name"));
            bw.write(System.getProperty("line.separator"));
            bw.write("Current date: " + Utils.getCurrentDateTime());
            bw.write(System.getProperty("line.separator"));
            bw.write("Current settings:");
            bw.write(System.getProperty("line.separator"));
            printCurrentSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the stacktrace to file
     * @param e The exception
     */
    public static void printStacktrace(Exception e) {
        e.printStackTrace(new PrintWriter(bw));
    }

    /**
     * Writes the current settings to the file
     */
    public static void printCurrentSettings() {
        try {
            bw.write("Language: " + Settings.language + "\n" +
                    "MGT2 file path: " + Settings.mgt2Path + "\n" +
                    "Steam library folder: " + Settings.steamLibraryFolder + "\n" +
                    "Update branch: " + Settings.updateBranch + "\n" +
                    "Enable disclaimer message: " + Settings.enableDisclaimerMessage + "\n" +
                    "Enable debug logging: " + Settings.enableDebugLogging + "\n" +
                    "Enable custom folder: " + Settings.enableCustomFolder + "\n" +
                    "Enable genre name translation info: " + Settings.enableGenreNameTranslationInfo + "\n" +
                    "Enable genre description translation info: " + Settings.enableGenreDescriptionTranslationInfo + "\n" +
                    "Save logs: " + Settings.saveLogs + "\n" +
                    "Enable export storage: " + Settings.enableExportStorage + "\n" +
                    "Write text area output to console: " + Settings.writeTextAreaOutputToConsole);
            bw.write(System.getProperty("line.separator"));
            for (Map.Entry<SafetyFeature, Boolean> entry : Settings.safetyFeatures.entrySet()) {
                bw.write(entry.getKey().getIdentifier() + ": " + entry.getValue());
                bw.write(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the input string into the log file
     * @param string The message to write
     */
    public static void write(String string) {
        try {
            String logOut = Utils.getCurrentDateTime() + ": " + string;
            bw.write(logOut);
            bw.write(System.getProperty("line.separator"));
            if (Settings.enableDebugLogging || string.contains("Exception")) {
                LOGGER.info("LogOut: " + logOut);
            }
        } catch (IOException ignored) {

        }
    }

    /**
     * Closes the buffered reader.
     */
    public static void stopLogging() {
        try {
            bw.close();
            LOGGER.info("Logging to file has been stopped!");
            File latestLog = ModManagerPaths.MAIN.getPath().resolve("latest.log").toFile();
            if (latestLog.exists()) {
                latestLog.delete();
            }
            Files.copy(Paths.get(logFile.getPath()), Paths.get(latestLog.getPath()));
            if (Settings.saveLogs) {
                File storageFile = ModManagerPaths.LOG.getPath().resolve(logFile.getName()).toFile();
                if (!ModManagerPaths.LOG.getPath().toFile().exists()) {
                    ModManagerPaths.LOG.getPath().toFile().mkdirs();
                }
                Files.move(Paths.get(logFile.getPath()), Paths.get(storageFile.getPath()));
            }
            logFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
