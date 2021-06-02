package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.mod.ThemeMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
        createBackup(fileToBackup, false, false);
    }
    /**
     * Creates a backup of a given file. And sets it was initial backup
     * @param fileToBackup This is the file from which a backup should be created.
     * @param initialBackup Set true when this is the initial backup.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createBackup(File fileToBackup, boolean initialBackup, boolean showTextAreaMessages) throws IOException {
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
            if(showTextAreaMessages){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.backup.createBackup.success") + " " + fileToBackup.getPath());
            }
        }
    }

    /**
     * Creates a specified backup.
     * @param type The backup type
     */
    public static void createBackup(String type){
        switch (type) {
            case "full":
                try {
                    Backup.createFullBackup();
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.createBackup.fullBackupCreated"), I18n.INSTANCE.get("dialog.backup.backupCreatedTitle"), JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.unableToCreateBackup") + "\n" + I18n.INSTANCE.get("dialog.backup.unableToCreateBackup.fileNotFound") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + e.getMessage(), I18n.INSTANCE.get("dialog.backup.backupFailedTitle"), JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "save_game":
                try {
                    Backup.backupSaveGames(false);
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.createBackup.saveGamesFileBackupCreated"), I18n.INSTANCE.get("dialog.backup.backupCreatedTitle"), JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.unableToCreateBackup") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + e.getMessage(), I18n.INSTANCE.get("dialog.backup.backupFailedTitle"), JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    /**
     * Restores either a complete initial backup or a complete latest backup
     * @param initialBackup If true the initial backup will be restored. If false the latest backup will be restored.
     * @param showMessages Set true when messages should be displayed to the user
     */
    public static void restoreBackup(boolean initialBackup, boolean showMessages){
        ProgressBarHelper.initializeProgressBar(0, getBackupFiles().size() + TranslationManager.TRANSLATION_KEYS.length, I18n.INSTANCE.get("textArea.backup.restoringBackup"), true);
        try {
            LOGGER.info("Restoring backup.");
            for(File file : getBackupFiles()){
                File backupFile;
                if(initialBackup){
                    backupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + file.getName() + ".initialBackup");
                }else{
                    backupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//" + file.getName());
                }
                Files.copy(Paths.get(backupFile.getPath()), Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);ProgressBarHelper.increment();
            }
            restoreThemeFileBackups(initialBackup);
            if(initialBackup){
                ImageFileHandler.removePublisherIcons();
                if(showMessages){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.restored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.restored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.restored"), JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                if(showMessages){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.restored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.restored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.restored"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            if(initialBackup){
                if(showMessages){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + exception.getMessage(), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if(showMessages){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + exception.getMessage(), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Opens a gui where the user can select which save game backup should be restored
     */
    public static void restoreSaveGameBackup(){
        try {
            ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(Backup.BACKUP_FOLDER_PATH, "savegame");
            Set<String> saveGameSlots = new HashSet<>();
            for(File file : files){
                saveGameSlots.add(file.getName().replaceAll("[^0-9]", ""));
            }
            JLabel label = new JLabel(I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.label"));
            String[] array = saveGameSlots.toArray(new String[0]);
            JList<String> listAvailableThemes = new JList<>(array);
            listAvailableThemes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableSaveGames = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableSaveGames.setPreferredSize(new Dimension(30,60));

            Object[] params = {label, scrollPaneAvailableSaveGames};
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                int saveGameSlotToRestore = Integer.parseInt(listAvailableThemes.getSelectedValue());
                if(JOptionPane.showConfirmDialog(null,  I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.confirmMessage.firstPart") + " " + saveGameSlotToRestore + "\n\n" + I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.confirmMessage.secondPart"), I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    Backup.backupSaveGames(false);
                    Backup.restoreSaveGameBackup(saveGameSlotToRestore);
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.restored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.restored"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        for(int i = 0; i< TranslationManager.TRANSLATION_KEYS.length; i++){
            File currentBackupFile;
            if(initialBackup){
                currentBackupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt.initialBackup");
            }else{
                currentBackupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//" + latestBackupFolderName + "//Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt");
            }
            Files.copy(Paths.get(currentBackupFile.getPath()), Paths.get(Utils.getThemeFile(i).getPath()), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("File " + currentBackupFile.getPath() + " has been restored.");
            ProgressBarHelper.increment();
        }
    }

    /**
     * Deletes all backups after confirmed by the user
     */
    public static void deleteAllBackups(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.deleteAllBackups.mainMessage"), I18n.INSTANCE.get("dialog.backup.deleteAllBackups.mainMessage.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                File backupFolder = new File(BACKUP_FOLDER_PATH);
                if(backupFolder.exists()){
                    Files.walk(Paths.get(backupFolder.getPath()))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.deleteAllBackups.createNewInitialBackupQuestion"), I18n.INSTANCE.get("dialog.backup.deleteAllBackups.createNewInitialBackupQuestion.title"), JOptionPane.YES_NO_OPTION) == 0){
                    String returnValue = Backup.createInitialBackup();
                    if(returnValue.equals("")) {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.initialBackupCreated"), I18n.INSTANCE.get("dialog.backup.initialBackupCreated.title"), JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        System.exit(0);
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.initialBackupNotCreated") + returnValue, I18n.INSTANCE.get("dialog.backup.initialBackupNotCreated.title"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.deleteAllBackups.failed") + "\n" + e.getMessage(), I18n.INSTANCE.get("dialog.backup.deleteAllBackups.failed.title"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Creates a backup of each file that can be edited with this tool.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createFullBackup() throws IOException {
        for(File file : getBackupFiles()){
            Backup.createBackup(file, false, true);
        }
        backupSaveGames(false);
        createThemeFilesBackup(false, true);
    }

    /**
     * @return Returns a array list that contains all files that should be backed up
     * Does not include save games
     * Does not include themes
     */
    private static ArrayList<File> getBackupFiles(){
        ArrayList<File> backupFiles = new ArrayList<>();
        for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
            backupFiles.add(advancedMod.getFile());
        }
        for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
            if(!simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                backupFiles.add(simpleMod.getFile());
            }
        }
        backupFiles.add(Utils.getNpcGamesFile());
        return backupFiles;
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup(){
        return createInitialBackup(false);
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     * @param showTextAreaMessages True if text area messages should be printed
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup(boolean showTextAreaMessages){
        try{
            for(File file : getBackupFiles()){
                Backup.createBackup(file, true, showTextAreaMessages);
            }
            backupSaveGames(true);
            createThemeFilesBackup(true, showTextAreaMessages);
            return "";
        }catch(IOException e) {
            LOGGER.error("Unable to create initial backup: " + e.getMessage());
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
                        createBackup(backupFile, true, false);
                    }else{
                        createBackup(backupFile, false, true);
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
    public static void createThemeFilesBackup(boolean initialBackup, boolean showTextAreaMessages) throws IOException {
        for(int i=0; i<TranslationManager.TRANSLATION_KEYS.length; i++){
            Backup.createBackup(Utils.getThemeFile(i), initialBackup, showTextAreaMessages);
        }
    }

    /**
     * Moves the current initial backup files into a storage folder and creates a new initial backup. Displayes a message to the user beforehand
     */
    public static void createNewInitialBackup(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.message"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            boolean uninstallFailed = false;
            StringBuilder uninstallFailedExplanation = new StringBuilder();
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("textArea.uninstalling"));
            try {
                for(AbstractSimpleMod simpleMod : ModManager.simpleMods) {
                    if(simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                        String[] content = ModManager.themeMod.getAnalyzerEn().getCustomContentString();
                        ProgressBarHelper.increaseMaxValue(content.length);
                        for(String string : content){
                            simpleMod.getBaseEditor().removeMod(string);
                            ProgressBarHelper.increment();
                        }
                    }else{
                        String[] content = simpleMod.getBaseAnalyzer().getCustomContentString();
                        for (String string : content){
                            simpleMod.getBaseEditor().removeMod(string);
                            ProgressBarHelper.increment();
                        }
                    }
                }
                for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                    String[] content = advancedMod.getBaseAnalyzer().getCustomContentString();
                    for (String string : content){
                        advancedMod.getBaseEditor().removeMod(string);
                        ProgressBarHelper.increment();
                    }
                }
            }catch (IOException e){
                uninstallFailed = true;
                uninstallFailedExplanation.append(e.getMessage());
            }
            if(uninstallFailed){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete") + "\n\n" + uninstallFailedExplanation.toString(), I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.title"), JOptionPane.WARNING_MESSAGE);
            }else{
                ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//", ".initialBackup");
                File oldInitialBackupFolder = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//InitialBackups//" + Utils.getCurrentDateTime() + "//");
                oldInitialBackupFolder.mkdirs();
                ProgressBarHelper.initializeProgressBar(0, files.size(), I18n.INSTANCE.get("progressBar.moveOldInitialBackupFiles"), true);
                for(File file : files){
                    File oldInitialBackup = new File(oldInitialBackupFolder.getPath() + "//" + file.getName());
                    try{
                        Files.move(Paths.get(file.getPath()), Paths.get(oldInitialBackup.getPath()));
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.movingFile") + file.getPath() + " -> " + oldInitialBackup.getPath());
                    }catch(IOException ignored){

                    }
                    ProgressBarHelper.increment();
                }
                ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.creatingInitialBackup"));
                String returnValue = Backup.createInitialBackup(true);
                ProgressBarHelper.increment();
                if(returnValue.equals("")){
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.backupSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.backupError") + "<br><br>" + returnValue, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
