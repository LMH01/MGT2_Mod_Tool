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
    public static final File FILE_GENRES_INITIAL_BACKUP = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Genres.txt.initialBackup");
    public static final File FILE_NPC_GAMES_INITIAL_BACKUP = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.initialBackup");
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
     */
    public static void restoreBackup(boolean initialBackup){
        try {
            LOGGER.info("Restoring initial backup.");
            File fileGenresBackup;
            File fileNpcGamesBackup;
            File fileThemesArBackup;
            File fileThemesChBackup;
            File fileThemesCtBackup;
            File fileThemesCzBackup;
            File fileThemesEnBackup;
            File fileThemesEsBackup;
            File fileThemesFrBackup;
            File fileThemesGeBackup;
            File fileThemesHuBackup;
            File fileThemesKoBackup;
            File fileThemesPbBackup;
            File fileThemesPlBackup;
            File fileThemesRuBackup;
            File fileThemesTuBackup;
            if(initialBackup){
                fileGenresBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Genres.txt.initialBackup");
                fileNpcGamesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.initialBackup");
                fileThemesArBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_AR.txt.initialBackup");
                fileThemesChBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_CH.txt.initialBackup");
                fileThemesCtBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_CT.txt.initialBackup");
                fileThemesCzBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_CZ.txt.initialBackup");
                fileThemesEnBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_EN.txt.initialBackup");
                fileThemesEsBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_ES.txt.initialBackup");
                fileThemesFrBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_FR.txt.initialBackup");
                fileThemesGeBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_GE.txt.initialBackup");
                fileThemesHuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_HU.txt.initialBackup");
                fileThemesKoBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_KO.txt.initialBackup");
                fileThemesPbBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_PB.txt.initialBackup");
                fileThemesPlBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_PL.txt.initialBackup");
                fileThemesRuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_RU.txt.initialBackup");
                fileThemesTuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_TU.txt.initialBackup");
            }else{
                fileGenresBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Genres.txt");
                fileNpcGamesBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//NpcGames.txt");
                fileThemesArBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_AR.txt");
                fileThemesChBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_CH.txt");
                fileThemesCtBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_CT.txt");
                fileThemesCzBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_CZ.txt");
                fileThemesEnBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_EN.txt");
                fileThemesEsBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_ES.txt");
                fileThemesFrBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_FR.txt");
                fileThemesGeBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_GE.txt");
                fileThemesHuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_HU.txt");
                fileThemesKoBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_KO.txt");
                fileThemesPbBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_PB.txt");
                fileThemesPlBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_PL.txt");
                fileThemesRuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_RU.txt");
                fileThemesTuBackup = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_TU.txt");
            }
            Files.copy(Paths.get(fileGenresBackup.getPath()), Paths.get(Utils.getGenreFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileNpcGamesBackup.getPath()), Paths.get(Utils.getNpcGamesFile().getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesArBackup.getPath()), Paths.get(Utils.getThemeFile(0).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesChBackup.getPath()), Paths.get(Utils.getThemeFile(1).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesCtBackup.getPath()), Paths.get(Utils.getThemeFile(2).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesCzBackup.getPath()), Paths.get(Utils.getThemeFile(3).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesEnBackup.getPath()), Paths.get(Utils.getThemeFile(4).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesEsBackup.getPath()), Paths.get(Utils.getThemeFile(5).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesFrBackup.getPath()), Paths.get(Utils.getThemeFile(6).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesGeBackup.getPath()), Paths.get(Utils.getThemeFile(7).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesHuBackup.getPath()), Paths.get(Utils.getThemeFile(8).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesKoBackup.getPath()), Paths.get(Utils.getThemeFile(9).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesPbBackup.getPath()), Paths.get(Utils.getThemeFile(10).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesPlBackup.getPath()), Paths.get(Utils.getThemeFile(11).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesRuBackup.getPath()), Paths.get(Utils.getThemeFile(12).getPath()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(fileThemesTuBackup.getPath()), Paths.get(Utils.getThemeFile(13).getPath()), StandardCopyOption.REPLACE_EXISTING);
            if(initialBackup){
                ChangeLog.addLogEntry(8);
                JOptionPane.showMessageDialog(null, "The initial backup has been restored.", "Backup restored", JOptionPane.INFORMATION_MESSAGE);
            }else{
                ChangeLog.addLogEntry(9);
                JOptionPane.showMessageDialog(null, "The latest backup has been restored.", "Backup restored", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            if(initialBackup){
                ChangeLog.addLogEntry(10, exception.getMessage());
                JOptionPane.showMessageDialog(null, "The initial backup could not be restored.\nThe initial backup file(s) are missing.\n\nException:\n" + exception.getMessage(), "Restoring failed", JOptionPane.ERROR_MESSAGE);
            }else{
                ChangeLog.addLogEntry(11, exception.getMessage());
                JOptionPane.showMessageDialog(null, "The latest backup could not be restored.\nThe latest backup file(s) are missing.\n\nException:\n" + exception.getMessage(), "Restoring failed", JOptionPane.ERROR_MESSAGE);
            }

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
       Backup.createBackup(Utils.getThemesGeFile());
       backupSaveGames(FILE_SAVE_GAME_FOLDER, false);
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
            backupSaveGames(FILE_SAVE_GAME_FOLDER, true);
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
     * @param saveGameFolder The folder where the save games are located.
     */
    private static void backupSaveGames(File saveGameFolder, boolean initialBackup) throws IOException {
        if(saveGameFolder.exists()){
            File[] filesInFolder = saveGameFolder.listFiles();
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
        Backup.createBackup(Utils.getThemeFile(0), initialBackup);
        Backup.createBackup(Utils.getThemeFile(1), initialBackup);
        Backup.createBackup(Utils.getThemeFile(2), initialBackup);
        Backup.createBackup(Utils.getThemeFile(3), initialBackup);
        Backup.createBackup(Utils.getThemeFile(4), initialBackup);
        Backup.createBackup(Utils.getThemeFile(5), initialBackup);
        Backup.createBackup(Utils.getThemeFile(6), initialBackup);
        Backup.createBackup(Utils.getThemeFile(7), initialBackup);
        Backup.createBackup(Utils.getThemeFile(8), initialBackup);
        Backup.createBackup(Utils.getThemeFile(9), initialBackup);
        Backup.createBackup(Utils.getThemeFile(10), initialBackup);
        Backup.createBackup(Utils.getThemeFile(11), initialBackup);
        Backup.createBackup(Utils.getThemeFile(12), initialBackup);
        Backup.createBackup(Utils.getThemeFile(13), initialBackup);
    }
}
