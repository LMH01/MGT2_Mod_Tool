package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ChangeLog {
    private static Logger logger = LoggerFactory.getLogger(ChangeLog.class);
    public static final File FILE_CHANGES_LOG = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\changes.log");
    public static final File FILE_CHANGES_LOG_TEMP = new File(Settings.MGT2_MOD_MANAGER_PATH + "\\changes.log.temp");
    public static void addLogEntry(int operation, String textBody){
        try {
            logger.info("Adding new log entry...");
            String currentSystemTime = Utils.getCurrentDateTime();
            PrintWriter pw;
            if(FILE_CHANGES_LOG.exists()){
                logger.info("changes.log already exists. Reading contents and writing to temp file.");
                pw = new PrintWriter(FILE_CHANGES_LOG_TEMP);
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_CHANGES_LOG)));
                String currentLine;
                while((currentLine = br.readLine()) != null){
                    pw.print(currentLine + "\n");
                }
                br.close();
                logger.info("changes.log.temp has been created.");
                logger.info("Deleting old file and renaming changes.log.temp to changes.log");
                printNewLogEntry(operation, textBody, pw, currentSystemTime);
                pw.close();
                FILE_CHANGES_LOG.delete();
                FILE_CHANGES_LOG_TEMP.renameTo(FILE_CHANGES_LOG);
            }else{
                pw = new PrintWriter(FILE_CHANGES_LOG);
                FILE_CHANGES_LOG.createNewFile();
                printNewLogEntry(operation, textBody, pw, currentSystemTime);
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints new log entry into the changes.log file.
     * @param operation What operation has been performed.
     * @param textBody The genre/genre id.
     * @param pw PrintWriter of the file.
     * @param currentSystemTime The string that is being printed before the log entry.
     */
    private static void printNewLogEntry(int operation, String textBody, PrintWriter pw, String currentSystemTime){
        switch(operation){
            case 1: pw.print(currentSystemTime + ": Added genre [" + textBody + "]\n"); break;
            case 2: pw.print(currentSystemTime + ": Added genre with id [" + textBody + "] to the npc games list \n"); break;
            case 3: pw.print(currentSystemTime + ": Removed genre with id [" + textBody + "] from the npc games list \n"); break;
            case 4: pw.print(currentSystemTime + ": Removed genre with id [" + textBody + "]\n"); break;
        }
    }
}
