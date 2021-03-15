package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Backup {
    private static final Logger LOGGER = LoggerFactory.getLogger(Backup.class);
    private static String latestBackupFolderName = "";
    public static final String BACKUP_FOLDER_PATH = System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//";
    public static final File FILE_SAVE_GAME_FOLDER = new File(System.getenv("USERPROFILE") + "\\appdata\\locallow\\Eggcode\\Mad Games Tycoon 2\\");

    /**
     * Creates a backup of a given file.
     * @param fileToBackup This is the file from which a backup should be created.
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
        String currentTimeAndDay = Utils.getCurrentDateTime();
        boolean initialBackupAlreadyExists = false;
        latestBackupFolderName = currentTimeAndDay;
        File backupFileFolder = new File(BACKUP_FOLDER_PATH + currentTimeAndDay + "//");
        File directoryBackup = new File(BACKUP_FOLDER_PATH);
        if(!directoryBackup.exists()){
            directoryBackup.mkdirs();
        }
        File fileLatestBackupOfInputFile;
        if(initialBackup){
            fileLatestBackupOfInputFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + fileToBackup.getName()+ ".initialBackup");
            if(fileLatestBackupOfInputFile.exists()){
                if(Settings.enableDebugLogging){
                    LOGGER.info("Initial backup of file already exists: " + fileLatestBackupOfInputFile.getPath());
                }
                initialBackupAlreadyExists = true;
            }
        }else{
            fileLatestBackupOfInputFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + fileToBackup.getName()+ ".latestBackup");
        }
        if(!initialBackupAlreadyExists){
            if(fileLatestBackupOfInputFile.exists()){
                if(!backupFileFolder.exists()){//Creates directory if backup directory for specified time does not exist
                    backupFileFolder.mkdirs();
                }
                LOGGER.info(fileToBackup.getName() +  " backup already exists. Moving old backup into storage folder");
                File fileBackupSaved = new File(backupFileFolder.getPath() + "\\" + fileToBackup.getName());
                if(fileBackupSaved.exists()){//If the backup file already exists it will be deleted. (The backup file in the backup folder with timestamp) Maybe change the formatting of currentTimeAndDay to a format where seconds are also used to prevent this deletion.
                    LOGGER.info("The file inside the storage folder does already exist. deleting...");
                    fileBackupSaved.delete();
                }
                fileLatestBackupOfInputFile.renameTo(fileBackupSaved);
            }
            File fileBackupFile = new File(fileToBackup.getPath());
            Files.copy(Paths.get(fileBackupFile.getPath()), Paths.get(fileLatestBackupOfInputFile.getPath()));
            ChangeLog.addLogEntry(5, fileToBackup.getName());
        }
    }

    /**
     * Restores either a complete initial backup or a complete latest backup
     * @param initialBackup If true the initial backup will be restored. If false the latest backup will be restored.
     * @param showMessages Set true when messages should be displayed to the user
     */
    public static void restoreBackup(boolean initialBackup, boolean showMessages){
        try {
            LOGGER.info("Restoring backup.");
            File fileGenresBackup;
            File fileNpcGamesBackup;
            File filePublisherBackup;
            File fileGameplayFeaturesBackup;
            File fileEngineFeaturesBackup;
            if(initialBackup){
                fileGenresBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Genres.txt.initialBackup");
                fileNpcGamesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.initialBackup");
                filePublisherBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Publisher.txt.initialBackup");
                fileGameplayFeaturesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//GameplayFeatures.txt.initialBackup");
                fileEngineFeaturesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//EngineFeatures.txt.initialBackup");
            }else{
                fileGenresBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Genres.txt");
                fileNpcGamesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//NpcGames.txt");
                filePublisherBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Publisher.txt");
                fileGameplayFeaturesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//GameplayFeatures.txt");
                fileEngineFeaturesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//EngineFeatures.txt");
            }
            Files.copy(Paths.get(fileGenresBackup.getPath()), Paths.get(Utils.getGenreFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileNpcGamesBackup.getPath()), Paths.get(Utils.getNpcGamesFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(filePublisherBackup.getPath()), Paths.get(Utils.getPublisherFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileGameplayFeaturesBackup.getPath()), Paths.get(Utils.getGameplayFeaturesFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileEngineFeaturesBackup.getPath()), Paths.get(Utils.getEngineFeaturesFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            restoreThemeFileBackups(initialBackup);
            if(initialBackup){
                ChangeLog.addLogEntry(8);
                if(showMessages){
                    JOptionPane.showMessageDialog(null, "The initial backup has been restored.", "Backup restored", JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                ChangeLog.addLogEntry(9);
                if(showMessages){
                    JOptionPane.showMessageDialog(null, "The latest backup has been restored.", "Backup restored", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            if(initialBackup){
                ChangeLog.addLogEntry(10, exception.getMessage());
                if(showMessages){
                    JOptionPane.showMessageDialog(null, "The initial backup could not be restored.\nThe initial backup file(s) are missing.\n\nException:\n" + exception.getMessage(), "Restoring failed", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                ChangeLog.addLogEntry(11, exception.getMessage());
                if(showMessages){
                    JOptionPane.showMessageDialog(null, "The latest backup could not be restored.\nThe latest backup file(s) are missing.\n\nException:\n" + exception.getMessage(), "Restoring failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Restores the save game backup for the input save game slot
     * @param saveGameSlot The slot where the save game is saved that should be restored
     */
    public static void restoreSaveGameBackup(int saveGameSlot) throws IOException {
        File saveGameBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//savegame" + saveGameSlot + ".txt");
        if (!saveGameBackup.exists()) {
            saveGameBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//savegame" + saveGameSlot + ".txt.initialBackup");
        }
        Files.copy(Paths.get(saveGameBackup.getPath()), Paths.get(Utils.getSaveGameFile(saveGameSlot).getPath()), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Restores all theme file backups
     */
    private static void restoreThemeFileBackups(boolean initialBackup) throws IOException {
        for(int i=0; i<TranslationManager.TRANSLATION_KEYS.length; i++){
            File currentBackupFile;
            if(initialBackup){
                currentBackupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt.initialBackup");
            }else{
                currentBackupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt");
            }
            Files.copy(Paths.get(currentBackupFile.getPath()), Paths.get(Utils.getThemeFile(i).getPath()), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("File " + currentBackupFile.getPath() + " has been restored.");
        }
    }

    public static void deleteAllBackups() throws IOException {
        File backupFolder = new File(BACKUP_FOLDER_PATH);
        if(backupFolder.exists()){
            Files.walk(Paths.get(backupFolder.getPath()))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        ChangeLog.addLogEntry(12);
    }

    /**
     * Creates a backup of each file that can be edited with this tool.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createFullBackup() throws IOException {
       Backup.createBackup(Utils.getGenreFile());
       Backup.createBackup(Utils.getNpcGamesFile());
       Backup.createBackup(Utils.getPublisherFile());
       Backup.createBackup(Utils.getGameplayFeaturesFile());
       Backup.createBackup(Utils.getEngineFeaturesFile());
       backupSaveGames(false);
       createThemeFilesBackup(false);
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup(){
        try{
            Backup.createBackup(Utils.getGenreFile(), true);
            Backup.createBackup(Utils.getNpcGamesFile(), true);
            Backup.createBackup(Utils.getThemesGeFile(), true);
            Backup.createBackup(Utils.getPublisherFile(), true);
            Backup.createBackup(Utils.getGameplayFeaturesFile(), true);
            Backup.createBackup(Utils.getEngineFeaturesFile(), true);
            backupSaveGames(true);
            createThemeFilesBackup(true);
            ChangeLog.addLogEntry(6);
            return "";
        }catch(IOException e) {
            LOGGER.error("Unable to create initial backup: " + e.getMessage());
            ChangeLog.addLogEntry(7, e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Create a backup of each save game.
     */
    public static void backupSaveGames(boolean initialBackup) throws IOException {
        if(Backup.FILE_SAVE_GAME_FOLDER.exists()){
            File[] filesInFolder = Backup.FILE_SAVE_GAME_FOLDER.listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if(filesInFolder[i].getName().contains("savegame")){
                    File backupFile = new File(filesInFolder[i].getPath());
                    if(Settings.enableDebugLogging){
                        LOGGER.info("Savefile to backup found: " + backupFile);
                    }
                    if(initialBackup){
                        createBackup(backupFile, true);
                    }else{
                        createBackup(backupFile);
                    }
                }
                if(Settings.enableDebugLogging){
                    LOGGER.info("File [" + i + "] in folder: " + filesInFolder[i].getName());
                }
            }
        }
    }

    /**
     * Creates a backup of each Theme file.
     * @param initialBackup True if this is the initial backup.
     */
    public static void createThemeFilesBackup(boolean initialBackup) throws IOException {
        for(int i=0; i<TranslationManager.TRANSLATION_KEYS.length; i++){
            Backup.createBackup(Utils.getThemeFile(i), initialBackup);
        }
    }
}
