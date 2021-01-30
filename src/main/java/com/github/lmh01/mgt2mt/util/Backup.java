package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ChangeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Backup {
    private static Logger logger = LoggerFactory.getLogger(Backup.class);
    private static String currentTimeAndDay;

    /**
     * Creates a backup from each of the files that could be modified with this tool
     * @param fileToBackup This is the file from which a backup should be created.
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    public static boolean createBackup(File fileToBackup) {
        currentTimeAndDay = Utils.getCurrentDateTime();
        ChangeLog.addLogEntry(5, fileToBackup.getName());
        File backupFileFolder = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + currentTimeAndDay + "//");
        File fileLatestBackupOfInputFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + fileToBackup.getName()+ ".latestBackup");
        if(fileLatestBackupOfInputFile.exists()){
            if(!backupFileFolder.exists()){//Creates directory if backup directory for specified time does not exist
                backupFileFolder.mkdirs();
            }
            logger.info(fileToBackup.getName() +  " backup already exists. Moving old backup into storage folder");
            File fileBackupSaved = new File(backupFileFolder.getPath() + "\\" + fileToBackup.getName());
            if(fileBackupSaved.exists()){//If the backup file already exists it will be deleted. (The backup file in the backup folder with timestamp) Maybe change the formatting of currentTimeAndDay to a format where seconds are also used to prevent this deletion.
                fileBackupSaved.delete();
            }
            fileLatestBackupOfInputFile.renameTo(fileBackupSaved);
        }
        File fileBackupFile = new File(Utils.getMGT2DataPath() + fileToBackup.getName());
        try {
            Files.copy(Paths.get(fileBackupFile.getPath()), Paths.get(fileLatestBackupOfInputFile.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Creates a backup of each file that can be edited with this tool.
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    public static boolean createFullBackup(){
        if(Backup.createBackup(Utils.fileGenres) && Backup.createBackup(Utils.fileNpcGames)){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param backupFile This is the file that contains the backup.
     * @param backupFileFolder This is the folder where the backup should be placed.
     * @param fileToBackup This is the file from which a backup should be created.
     * @param formatting The formatting that should be used [UTF8 BOM] or [UTF-16]
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    private static boolean readAndWrite(File backupFile, File backupFileFolder, File fileToBackup, String formatting){
        try {//TODO Remove this function
            logger.info("Reading contents of file: " + fileToBackup + " and writing to backup file: " + backupFile);
            if(!backupFileFolder.exists()){
                backupFileFolder.mkdirs();
            }
            if(!backupFile.exists()){
                backupFile.createNewFile();
            }
            BufferedWriter bw;
            BufferedReader br;
            if(formatting.equals("UTF8-BOM")){
                logger.info("formatting is UTF8-BOM");
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(backupFile), StandardCharsets.UTF_8));
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToBackup), StandardCharsets.UTF_8));
                bw.write("\ufeff");
            }else{//formatting.equals("UTF-16LE") would be the condition if i add another backup file with different formatting.
                logger.info("formatting is UTF-16LE");
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(backupFile), StandardCharsets.UTF_16LE));
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToBackup), StandardCharsets.UTF_16LE));
            }
            String currentLine;
            boolean firstLine = true;
            while((currentLine = br.readLine()) != null) {
                if(firstLine) {
                    //currentLine = removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(Settings.enableDebugLogging) {
                    logger.info("currentLine: " + currentLine);
                }
                bw.write(currentLine + System.getProperty("line.separator"));
                bw.flush();
            }
            bw.close();
            br.close();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
