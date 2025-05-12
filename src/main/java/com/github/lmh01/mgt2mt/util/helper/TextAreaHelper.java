package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TextAreaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextAreaHelper.class);

    /**
     * Will append the input text to the text area. A line break is written
     */
    public static void appendText(String text) {
        writeText(text);
    }

    /**
     * Will append the input text to the text area. Will also write a warning output in the console.
     */
    public static void appendWarning(String warning) {
        LOGGER.warn("Warning: " + warning);
        writeText(warning);
    }

    public static void appendDebug(String debug) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(debug);
            writeText("[Debug] " + debug);
        }
    }

    /**
     * Prints the stacktrace of the exception into the text area and into the console
     *
     * @param e Exception for which the exception should be printed
     */
    public static void printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        TextAreaHelper.appendText(sw.toString());
    }

    private static void writeText(String text) {
        if (Settings.writeTextAreaOutputToConsole) {
            LOGGER.info(text.trim());
        }
        WindowMain.TEXT_AREA.append(text);
        WindowMain.TEXT_AREA.append(System.getProperty("line.separator"));
        LogFile.write(text);
        WindowMain.TEXT_AREA.setCaretPosition(WindowMain.TEXT_AREA.getDocument().getLength());
    }
}
