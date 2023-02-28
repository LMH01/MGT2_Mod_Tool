package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.DefaultContentManager;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.settings.Settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Backup {
    private static final Logger LOGGER = LoggerFactory.getLogger(Backup.class);
    private static String latestBackupFolderName = "";
    public static final Path FILE_SAVE_GAME_FOLDER;

    static {
        if (MadGamesTycoon2ModTool.isWindows()) {
            FILE_SAVE_GAME_FOLDER = Paths.get(System.getenv("USERPROFILE") + "/appdata/locallow/Eggcode/Mad Games Tycoon 2");
        } else {
            FILE_SAVE_GAME_FOLDER = Paths.get(System.getProperty("user.home"), ".local/share/Steam/steamapps/compatdata/1342330/pfx/drive_c/users/steamuser/AppData/LocalLow/Eggcode/Mad Games Tycoon 2");
        }
    }

    /**
     * Creates a backup of a given file.
     *
     * @param fileToBackup This is the file from which a backup should be created.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createBackup(File fileToBackup) throws IOException {
        createBackup(fileToBackup, false, false);
    }

    /**
     * Creates a backup of a given file. And sets it was initial backup
     *
     * @param fileToBackup         This is the file from which a backup should be created.
     * @param initialBackup        Set true when this is the initial backup.
     * @param showTextAreaMessages Set true when messages should be written to the text area.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createBackup(File fileToBackup, boolean initialBackup, boolean showTextAreaMessages) throws IOException {
        DebugHelper.debug(LOGGER, "Creating backup of file: " + fileToBackup.getPath());
        String currentTimeAndDay = Utils.getCurrentDateTime();
        boolean initialBackupAlreadyExists = false;
        latestBackupFolderName = currentTimeAndDay;
        Path backupStorageFolder = ModManagerPaths.BACKUP.getPath().resolve(currentTimeAndDay);
        File directoryBackup = ModManagerPaths.BACKUP.getPath().toFile();
        if (!directoryBackup.exists()) {
            directoryBackup.mkdirs();
        }
        File fileLatestBackupOfInputFile;
        if (initialBackup) {
            fileLatestBackupOfInputFile = ModManagerPaths.BACKUP.getPath().resolve(fileToBackup.getName() + ".initialBackup").toFile();
            if (fileLatestBackupOfInputFile.exists()) {
                DebugHelper.debug(LOGGER, "Initial backup of file already exists: " + fileLatestBackupOfInputFile.getPath());
                initialBackupAlreadyExists = true;
            }
        } else {
            fileLatestBackupOfInputFile = ModManagerPaths.BACKUP.getPath().resolve(fileToBackup.getName() + ".latestBackup").toFile();
        }
        if (!initialBackupAlreadyExists) {
            if (fileLatestBackupOfInputFile.exists()) {
                if (!backupStorageFolder.toFile().exists()) {//Creates directory if backup directory for specified time does not exist
                    backupStorageFolder.toFile().mkdirs();
                }
                DebugHelper.debug(LOGGER, fileToBackup.getName() + " backup already exists. Moving old backup into storage folder");
                File fileBackupSaved = backupStorageFolder.resolve(fileToBackup.getName()).toFile();
                if (fileBackupSaved.exists()) {//If the backup file already exists it will be deleted. (The backup file in the backup folder with timestamp) Maybe change the formatting of currentTimeAndDay to a format where seconds are also used to prevent this deletion.
                    DebugHelper.debug(LOGGER, "The file inside the storage folder does already exist. deleting...");
                    fileBackupSaved.delete();
                }
                fileLatestBackupOfInputFile.renameTo(fileBackupSaved);
            }
            File fileBackupFile = new File(fileToBackup.getPath());
            Files.copy(Paths.get(fileBackupFile.getPath()), Paths.get(fileLatestBackupOfInputFile.getPath()));
            if (showTextAreaMessages) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.backup.createBackup.success") + " " + fileToBackup.getPath());
            }
        }
    }

    /**
     * Creates a specified backup.
     *
     * @param type The backup type
     */
    public static void createBackup(BackupType type) {
        if (type.equals(BackupType.FULL)) {
            try {
                Backup.createFullBackup();
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.createBackup.fullBackupCreated"), I18n.INSTANCE.get("dialog.backup.backupCreatedTitle"), JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.unableToCreateBackup") + "\n" + I18n.INSTANCE.get("dialog.backup.unableToCreateBackup.fileNotFound") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + e.getMessage(), I18n.INSTANCE.get("dialog.backup.backupFailedTitle"), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                Backup.backupSaveGames(false);
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.createBackup.saveGamesFileBackupCreated"), I18n.INSTANCE.get("dialog.backup.backupCreatedTitle"), JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.backup.unableToCreateBackup") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + e.getMessage(), I18n.INSTANCE.get("dialog.backup.backupFailedTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Restores either a complete initial backup or a complete latest backup
     *
     * @param initialBackup If true the initial backup will be restored. If false the latest backup will be restored.
     * @param showMessages  Set true when messages should be displayed to the user
     */
    public static void restoreBackup(boolean initialBackup, boolean showMessages) {
        ProgressBarHelper.initializeProgressBar(0, getBackupFiles().size() + TranslationManager.TRANSLATION_KEYS.length, I18n.INSTANCE.get("textArea.backup.restoringBackup"), true);
        try {
            LOGGER.info("Restoring backup.");
            for (File file : getBackupFiles()) {
                Path backupFile;
                if (initialBackup) {
                    backupFile = ModManagerPaths.BACKUP.getPath().resolve(file.getName() + ".initialBackup");
                } else {
                    backupFile = ModManagerPaths.BACKUP.getPath().resolve(latestBackupFolderName + "/" + file.getName());
                }
                Files.copy(backupFile, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                ProgressBarHelper.increment();
            }
            restoreThemeFileBackups(initialBackup);
            if (initialBackup) {
                restoreImages();
                if (showMessages) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.restored"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.restored.note"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.restored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.restored"), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (showMessages) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.restored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.restored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.restored"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
            LOGGER.info("Backup has been restored");
        } catch (IOException exception) {
            exception.printStackTrace();
            if (initialBackup) {
                if (showMessages) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + exception.getMessage(), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (showMessages) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored"));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored") + "\n\n" + I18n.INSTANCE.get("commonBodies.exception") + "\n" + exception.getMessage(), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Opens a gui where the user can select which save game backup should be restored
     */
    public static void restoreSaveGameBackup() {
        try {
            ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(ModManagerPaths.BACKUP.getPath(), "savegame");
            Set<String> saveGameSlots = new HashSet<>();
            for (File file : files) {
                saveGameSlots.add(file.getName().replaceAll("[^0-9]", ""));
            }
            JLabel label = new JLabel(I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.label"));
            String[] array = saveGameSlots.toArray(new String[0]);
            JList<String> listAvailableThemes = new JList<>(array);
            listAvailableThemes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableSaveGames = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableSaveGames.setPreferredSize(new Dimension(30, 60));

            Object[] params = {label, scrollPaneAvailableSaveGames};
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                int saveGameSlotToRestore = Integer.parseInt(listAvailableThemes.getSelectedValue());
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.confirmMessage.firstPart") + " " + saveGameSlotToRestore + "\n\n" + I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.confirmMessage.secondPart"), I18n.INSTANCE.get("dialog.backup.restoreBackup.saveGameBackup.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
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
     *
     * @param saveGameSlot The slot where the save game is saved that should be restored
     * @throws IOException If the backup could not be restored
     */
    public static void restoreSaveGameBackup(int saveGameSlot) throws IOException {
        File saveGameBackup = ModManagerPaths.BACKUP.getPath().resolve(latestBackupFolderName + "/savegame" + saveGameSlot + ".txt").toFile();
        if (!saveGameBackup.exists()) {
            saveGameBackup = ModManagerPaths.BACKUP.getPath().resolve("/savegame" + saveGameSlot + ".txt.initialBackup").toFile();
        }
        Files.copy(Paths.get(saveGameBackup.getPath()), Paths.get(Utils.getSaveGameFile(saveGameSlot).getPath()), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Restores all theme file backups
     */
    private static void restoreThemeFileBackups(boolean initialBackup) throws IOException {
        for (int i = 0; i < TranslationManager.TRANSLATION_KEYS.length; i++) {
            Path currentBackupFile;
            if (initialBackup) {
                currentBackupFile = ModManagerPaths.BACKUP.getPath().resolve("Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt.initialBackup");
            } else {
                currentBackupFile = ModManagerPaths.BACKUP.getPath().resolve(latestBackupFolderName + "/Themes_" + TranslationManager.TRANSLATION_KEYS[i] + ".txt.initialBackup");
            }
            Files.copy(currentBackupFile, Paths.get(Utils.getThemeFile(i).getPath()), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("File " + currentBackupFile + " has been restored.");
            ProgressBarHelper.increment();
        }
    }

    /**
     * Deletes all backups after confirmed by the user
     */
    public static void deleteAllBackups() {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.deleteAllBackups.mainMessage"), I18n.INSTANCE.get("dialog.backup.deleteAllBackups.mainMessage.title"), JOptionPane.YES_NO_OPTION) == 0) {
            try {
                File backupFolder = ModManagerPaths.BACKUP.getPath().toFile();
                if (backupFolder.exists()) {
                    Files.walk(Paths.get(backupFolder.getPath()))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.deleteAllBackups.createNewInitialBackupQuestion"), I18n.INSTANCE.get("dialog.backup.deleteAllBackups.createNewInitialBackupQuestion.title"), JOptionPane.YES_NO_OPTION) == 0) {
                    String returnValue = Backup.createInitialBackup();
                    if (returnValue.equals("")) {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.initialBackupCreated"), I18n.INSTANCE.get("dialog.backup.initialBackupCreated.title"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.exit(0);
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.initialBackupNotCreated") + returnValue, I18n.INSTANCE.get("dialog.backup.initialBackupNotCreated.title"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
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
     *
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createFullBackup() throws IOException {
        for (File file : getBackupFiles()) {
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
    private static ArrayList<File> getBackupFiles() {
        ArrayList<File> backupFiles = new ArrayList<>();
        for (BaseContentManager content : ContentAdministrator.contentManagers) {
            backupFiles.add(content.getGameFile());
        }
        return backupFiles;
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     *
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup() {
        return createInitialBackup(false);
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     *
     * @param showTextAreaMessages True if text area messages should be printed
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup(boolean showTextAreaMessages) {
        return createInitialBackup(showTextAreaMessages, false);
    }

    /**
     * Creates an initial backup when initial backup does not exist already.
     *
     * @param showTextAreaMessages True if text area messages should be printed
     * @param forceImages Used to force the backup of image files. If the image backup files already exist they will be deleted.
     * @return Returns e.getMessage();
     */
    public static String createInitialBackup(boolean showTextAreaMessages, boolean forceImages) {
        try {
            for (File file : getBackupFiles()) {
                Backup.createBackup(file, true, showTextAreaMessages);
            }
            backupSaveGames(true);
            createThemeFilesBackup(true, showTextAreaMessages);
            backupImages(forceImages);
            return "";
        } catch (IOException e) {
            LOGGER.error("Unable to create initial backup: " + e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Creates a backup of the following paths, if the backup does not already exist:
     * "Extern/CompanyLogos",
     * "Extern/Icons_Genres",
     * "Extern/Icons_Hardware",
     * "Extern/Icons_Platforms",
     * "Extern/Screenshots"
     * Backup is written to {@link ModManagerPaths#PICTURE_BACKUP}
     * Progress bar is utilized.
     * @param force Used to force the backup. If the backup files already exist they will be deleted.
     */
    public static void backupImages(boolean force) throws IOException {
        if (force) {
            if (Files.exists(ModManagerPaths.PICTURE_BACKUP.getPath())) {
                DataStreamHelper.deleteDirectory(ModManagerPaths.PICTURE_BACKUP.getPath());
            }
        }
        if (!Files.exists(ModManagerPaths.PICTURE_BACKUP.getPath())) {
            DataStreamHelper.copyDirectory(MGT2Paths.COMPANY_ICONS.getPath(), ModManagerPaths.PICTURE_BACKUP.getPath().resolve("CompanyLogos"));
            DataStreamHelper.copyDirectory(MGT2Paths.GENRE_ICONS.getPath(), ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Genres"));
            DataStreamHelper.copyDirectory(MGT2Paths.HARDWARE_ICONS.getPath(), ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Hardware"));
            DataStreamHelper.copyDirectory(MGT2Paths.PLATFORM_ICONS.getPath(), ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Platforms"));
            DataStreamHelper.copyDirectory(MGT2Paths.GENRE_SCREENSHOTS.getPath(), ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Screenshots"));
        }
    }

    /**
     * Restores all image backups for the following paths:
     * "Extern/CompanyLogos",
     * "Extern/Icons_Genres",
     * "Extern/Icons_Hardware",
     * "Extern/Icons_Platforms",
     * "Extern/Screenshots"
     * For that all folders will be deleted and the backup will be copied.
     * @throws IOException When the deletion of the directories or the copying fails
     */
    public static void restoreImages() throws IOException {
        DataStreamHelper.deleteDirectory(MGT2Paths.COMPANY_ICONS.getPath());
        DataStreamHelper.deleteDirectory(MGT2Paths.GENRE_ICONS.getPath());
        DataStreamHelper.deleteDirectory(MGT2Paths.HARDWARE_ICONS.getPath());
        DataStreamHelper.deleteDirectory(MGT2Paths.PLATFORM_ICONS.getPath());
        DataStreamHelper.deleteDirectory(MGT2Paths.GENRE_SCREENSHOTS.getPath());
        DataStreamHelper.copyDirectory(ModManagerPaths.PICTURE_BACKUP.getPath().resolve("CompanyLogos"), MGT2Paths.COMPANY_ICONS.getPath());
        DataStreamHelper.copyDirectory(ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Genres"), MGT2Paths.GENRE_ICONS.getPath());
        DataStreamHelper.copyDirectory(ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Hardware"), MGT2Paths.HARDWARE_ICONS.getPath());
        DataStreamHelper.copyDirectory(ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Icons_Platforms"), MGT2Paths.PLATFORM_ICONS.getPath());
        DataStreamHelper.copyDirectory(ModManagerPaths.PICTURE_BACKUP.getPath().resolve("Screenshots"), MGT2Paths.GENRE_SCREENSHOTS.getPath());
    }

    /**
     * Create a backup of each save game.
     *
     * @param initialBackup True if this is the initial backup
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void backupSaveGames(boolean initialBackup) throws IOException {
        if (Backup.FILE_SAVE_GAME_FOLDER.toFile().exists()) {
            File[] filesInFolder = Backup.FILE_SAVE_GAME_FOLDER.toFile().listFiles();
            for (int i = 0; i < Objects.requireNonNull(filesInFolder).length; i++) {
                if (filesInFolder[i].getName().contains("savegame")) {
                    File backupFile = new File(filesInFolder[i].getPath());
                    if (initialBackup) {
                        createBackup(backupFile, true, false);
                    } else {
                        createBackup(backupFile, false, true);
                    }
                }
            }
        }
    }

    /**
     * Creates a backup of each Theme file.
     *
     * @param initialBackup        True if this is the initial backup.
     * @param showTextAreaMessages True if text area messages should be printed.
     * @throws IOException Throws IOException when backup was not successful.
     */
    public static void createThemeFilesBackup(boolean initialBackup, boolean showTextAreaMessages) throws IOException {
        for (int i = 0; i < TranslationManager.TRANSLATION_KEYS.length; i++) {
            Backup.createBackup(Utils.getThemeFile(i), initialBackup, showTextAreaMessages);
        }
    }

    /**
     * Moves the current initial backup files into a storage folder and creates a new initial backup. Displays a message to the user beforehand
     */
    public static void createNewInitialBackup() {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.message"), I18n.INSTANCE.get("frame.title.information"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            boolean uninstallFailed = false;
            StringBuilder uninstallFailedExplanation = new StringBuilder();
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("textArea.uninstalling"));
            try {
                for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                    String[] content = manager.getCustomContentString();
                    for (String string : content) {
                        manager.removeContent(string);
                        ProgressBarHelper.increment();
                    }
                }
            } catch (ModProcessingException e) {
                uninstallFailed = true;
                uninstallFailedExplanation.append(e.getMessage());
            }
            boolean continueWithBackup = false;
            if (uninstallFailed) {
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.uninstall.uninstallIncomplete") + "\n\n" + uninstallFailedExplanation + "\n\n" + I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.question"), I18n.INSTANCE.get("window.uninstall.uninstallIncomplete.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    continueWithBackup = true;
                }
            } else {
                continueWithBackup = true;
            }
            if (continueWithBackup) {
                ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(ModManagerPaths.BACKUP.getPath(), ".initialBackup");
                File oldInitialBackupFolder = ModManagerPaths.BACKUP.getPath().resolve("InitialBackups/" + Utils.getCurrentDateTime()).toFile();
                oldInitialBackupFolder.mkdirs();
                ProgressBarHelper.initializeProgressBar(0, files.size(), I18n.INSTANCE.get("progressBar.moveOldInitialBackupFiles"), true);
                for (File file : files) {
                    File oldInitialBackup = new File(oldInitialBackupFolder.getPath() + "/" + file.getName());
                    try {
                        Files.move(Paths.get(file.getPath()), Paths.get(oldInitialBackup.getPath()));
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.movingFile") + file.getPath() + " -> " + oldInitialBackup.getPath());
                    } catch (IOException ignored) {

                    }
                    ProgressBarHelper.increment();
                }
                ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.creatingInitialBackup"));
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.verifyGameFilesNow"), I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.verifyGameFilesNow.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    String returnValue = Backup.createInitialBackup(true, true);
                    ProgressBarHelper.increment();
                    if (returnValue.equals("")) {
                        InitialBackupChecker.updateInitialBackupVersion();
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.backupSuccessful"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.createNewInitialBackup.backupError") + "<br><br>" + returnValue, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            // Rewrite default content file to conain content that might not be marked as default content
            try {
                ContentAdministrator.analyzeContents();
                DefaultContentManager.writeNewDefaultContentFile();
            } catch (ModProcessingException e) {
                e.printStackTrace();
            }
            // Update max icon id
            updateMaxIconId();
        }
    }

    public static void restoreInitialBackup() {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0) {
            try {
                LOGGER.info("Creating backup before restoring initial backup");
                Backup.createFullBackup();
                if (ContentAdministrator.areModsActive()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    Uninstaller.uninstallAllMods(stringBuilder);
                    if (!stringBuilder.toString().isEmpty()) {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored.mods") + "\n\n" + stringBuilder, I18n.INSTANCE.get("frame.title.error"), JOptionPane.WARNING_MESSAGE);
                        Backup.restoreBackup(true, true);
                    }
                } else {
                    Backup.restoreBackup(true, true);
                }
            } catch (IOException | ModProcessingException e) {
                e.printStackTrace();
                if (Utils.showConfirmDialog(1, e)) {
                    Backup.restoreBackup(true, true);
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void restoreLatestBackup() {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0) {
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                Backup.restoreBackup(false, true);
            } catch (IOException e) {
                e.printStackTrace();
                if (Utils.showConfirmDialog(1, e)) {
                    Backup.restoreBackup(false, true);
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Checks the {@link MGT2Paths#COMPANY_ICONS} folder to determine what the max icon id is.
     * The determined value is then written into the settings file.
     * This function is only called when the initial backup is created.
     */
    private static void updateMaxIconId() {
        ArrayList<File> files = DataStreamHelper.getFilesInFolderBlackList(MGT2Paths.COMPANY_ICONS.getPath(), ".meta");
        int maxPictureId = -1;
        for (File file : files) {
            if (file.getPath().contains(".png")) {
                maxPictureId += 1;
            }
        }
        System.out.println("Max picture id: " + maxPictureId);
        Settings.maxPictureId = maxPictureId;
        ExportSettings.export(Settings.settingsFile);
    }
}
