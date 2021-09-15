package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.util.LogFile;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;

public class DebugHelper {

    /**
     * Sends a log message with the logger if debug logging is enabled
     *
     * @param string The message that should be sent
     */
    public static void debug(Logger logger, String string) {
        if (Settings.enableDebugLogging) {
            logger.info(string);
        }
    }

    /**
     * Sends a log message as warning into the console and writes it into the log file
     *
     * @param string The message that should be sent
     */
    public static void warn(Logger logger, String string) {
        logger.warn(string);
        LogFile.write("Warning: " + string);
    }
}
