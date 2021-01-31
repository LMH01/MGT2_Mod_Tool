package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.dataStream.ChangeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Backup {
    private static Logger logger = LoggerFactory.getLogger(Backup.class);
    private static String currentTimeAndDay;

    /**
     * Creates a backup of a given file.
     * @param fileToBackup This is the file from which a backup should be created.
     * @return Returns true if backup was successful or when the user decided to continue anyway.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createBackup(File fileToBackup) throws IOException {
        createBackup(fileToBackup, false);
    }
    /**
     * Creates a backup of a given file. And sets it was initial backup
     * @param fileToBackup This is the file from which a backup should be created.
     * @param initialBackup Set true when this is the initial backup.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createBackup(File fileToBackup, boolean initialBackup) throws IOException {
        currentTimeAndDay = Utils.getCurrentDateTime();
        ChangeLog.addLogEntry(5, fileToBackup.getName());
        File backupFileFolder = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + currentTimeAndDay + "//");
        File directoryBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//");
        if(!directoryBackup.exists()){
            directoryBackup.mkdirs();
        }
        File fileLatestBackupOfInputFile;
        if(initialBackup){
            fileLatestBackupOfInputFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + fileToBackup.getName()+ ".initialBackup");
            if(fileLatestBackupOfInputFile.exists()){
                if(JOptionPane.showConfirmDialog(null, "The initial backup of " + fileToBackup.getName() + " already exists.\nDo you want to replace it?", "Initial backup already exists", JOptionPane.YES_NO_OPTION) == 0){
                    fileLatestBackupOfInputFile.delete();
                }
            }
        }else{
            fileLatestBackupOfInputFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + fileToBackup.getName()+ ".latestBackup");
        }
        if(fileLatestBackupOfInputFile.exists()){
            if(!backupFileFolder.exists()){//Creates directory if backup directory for specified time does not exist
                backupFileFolder.mkdirs();
            }
            logger.info(fileToBackup.getName() +  " backup already exists. Moving old backup into storage folder");
            File fileBackupSaved = new File(backupFileFolder.getPath() + "\\" + fileToBackup.getName());
            if(fileBackupSaved.exists()){//If the backup file already exists it will be deleted. (The backup file in the backup folder with timestamp) Maybe change the formatting of currentTimeAndDay to a format where seconds are also used to prevent this deletion.
                fileBackupSaved.delete();
                fileBackupSaved.createNewFile();
            }
            fileLatestBackupOfInputFile.renameTo(fileBackupSaved);
        }
        File fileBackupFile = new File(Utils.getMGT2DataPath() + fileToBackup.getName());
        Files.copy(Paths.get(fileBackupFile.getPath()), Paths.get(fileLatestBackupOfInputFile.getPath()));
    }

    /**
     * Creates a backup of each file that can be edited with this tool.
     * @param initialBackup Set true when this is the initial backup.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createFullBackup(boolean initialBackup) throws IOException {
        if(initialBackup){
            Backup.createBackup(Utils.fileGenres, true);
            Backup.createBackup(Utils.fileNpcGames, true);
            ChangeLog.addLogEntry(6, "");
        }else{
            Backup.createBackup(Utils.fileGenres);
            Backup.createBackup(Utils.fileNpcGames);
        }
    }
}
