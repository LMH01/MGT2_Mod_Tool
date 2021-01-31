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
     */
    public static boolean createBackup(File fileToBackup){
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if(JOptionPane.showConfirmDialog(null, "Backup of file " + fileToBackup.getName() + " was unsuccessful.\nDo you want to continue anyway?", "Unable to backup file", JOptionPane.YES_NO_OPTION) == 0){
                return true;
            }else{
                return false;
            }
        }
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
}
