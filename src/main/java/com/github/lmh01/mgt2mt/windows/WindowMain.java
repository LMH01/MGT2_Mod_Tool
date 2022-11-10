package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.NPCGameListHandler;
import com.github.lmh01.mgt2mt.util.handler.NewModsHandler;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ImportFromURLHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.RestorePointHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.ImportType;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.util.ArrayList;

public class WindowMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowMain.class);
    private static final JFrame frame = new JFrame(I18n.INSTANCE.get("window.main.tile"));
    private static final JMenuBar MB = new JMenuBar();
    private static final JMenu M_1_FILE = new JMenu(I18n.INSTANCE.get("window.main.file"));
    private static final JMenu M_2_MODS = new JMenu(I18n.INSTANCE.get("window.main.mods"));
    private static final JMenu M_3_SHARE = new JMenu(I18n.INSTANCE.get("window.main.share"));
    private static final JMenu M_4_BACKUP = new JMenu(I18n.INSTANCE.get("window.main.backup"));
    private static final JMenu M_5_UTIL = new JMenu(I18n.INSTANCE.get("window.main.utilities"));
    private static final JMenuItem M_12_UPDATE_CHECK = new JMenuItem(I18n.INSTANCE.get("window.main.file.updateCheck"));
    private static final JMenuItem M_13_UNINSTALL = new JMenuItem(I18n.INSTANCE.get("window.main.file.uninstall"));
    private static final JMenu M_21_IMPORT = new JMenu(I18n.INSTANCE.get("window.main.mods.import"));
    private static final JMenuItem M_233_CHANGE_GENRE_THEME_FIT = new JMenuItem(I18n.INSTANCE.get("window.main.mods.themes.changeGenreThemeFit"));
    private static final JMenuItem M_211_IMPORT_FROM_FILE_SYSTEM = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.importFromFileSystem"));
    private static final JMenuItem M_212_IMPORT_FROM_URL = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.importFromURL"));
    private static final JMenuItem M_22_NPC_GAMES_LIST = new JMenuItem(I18n.INSTANCE.get("window.main.mods.npcGamesList"));
    private static final JMenuItem M_23_ADD_COMPANY_ICON = new JMenuItem(I18n.INSTANCE.get("window.main.mods.addCompanyIcon"));
    private static final JMenu M_31_EXPORT = new JMenu(I18n.INSTANCE.get("window.main.share.export"));
    private static final JMenuItem M_317_EXPORT_ALL = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.exportAll"));
    private static final JMenuItem M_318_EXPORT_SELECTED = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.exportSelected"));
    private static final JMenu M_42_RESTORE_BACKUP = new JMenu(I18n.INSTANCE.get("window.main.backup.restoreBackup"));
    private static final JMenuItem M_422_RESTORE_LATEST_BACKUP = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup"));
    private static final JMenuItem M_431_CREATE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint"));
    private static final JMenuItem M_432_RESTORE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint"));
    private static final JMenuItem M_44_DELETE_ALL_BACKUPS = new JMenuItem(I18n.INSTANCE.get("window.main.backup.deleteAllBackups"));
    public static final JProgressBar PROGRESS_BAR = new JProgressBar();
    public static final JTextArea TEXT_AREA = new JTextArea();
    public static final JScrollPane SCROLL_PANE = new JScrollPane(TEXT_AREA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private static boolean modMenusInitialized = false;

    public static void createFrame() {
        //Creating the Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(680, 500);
        frame.setMinimumSize(new Dimension(250, 150));
        frame.setLocationRelativeTo(null);

        //Creating the MenuBar and adding components
        JMenuItem m11 = new JMenuItem(I18n.INSTANCE.get("window.main.file.settings"));
        m11.addActionListener(actionEvent -> WindowSettings.createFrame());
        M_12_UPDATE_CHECK.addActionListener(actionEvent -> UpdateChecker.checkForUpdates(true));
        M_13_UNINSTALL.setToolTipText(I18n.INSTANCE.get("window.main.file.uninstall.toolTip"));
        M_13_UNINSTALL.addActionListener(actionEvent -> ThreadHandler.startModThread(Uninstaller::uninstall, "Uninstall"));
        JMenuItem m14About = new JMenuItem(I18n.INSTANCE.get("window.main.file.about"));
        m14About.addActionListener(actionEvent -> About.showAboutPopup());
        MB.add(M_1_FILE);
        M_1_FILE.add(m11);
        M_1_FILE.add(M_12_UPDATE_CHECK);
        M_1_FILE.add(M_13_UNINSTALL);
        M_1_FILE.add(m14About);
        JMenuItem m213GetMoreMods = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.getMoreMods"));
        JMenuItem m210ShowActiveMods = new JMenuItem(I18n.INSTANCE.get("window.main.mods.showActiveMods"));
        M_21_IMPORT.add(M_211_IMPORT_FROM_FILE_SYSTEM);
        M_21_IMPORT.add(M_212_IMPORT_FROM_URL);
        M_21_IMPORT.add(m213GetMoreMods);
        M_2_MODS.addActionListener(actionEvent -> Disclaimer.showDisclaimer());
        M_2_MODS.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                Disclaimer.showDisclaimer();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        m213GetMoreMods.setToolTipText(I18n.INSTANCE.get("window.main.mods.import.getMoreMods.toolTip"));
        m213GetMoreMods.addActionListener(actionEvent -> openMoreModsPage());
        M_211_IMPORT_FROM_FILE_SYSTEM.setToolTipText(I18n.INSTANCE.get("window.main.mods.import.importFromFileSystem.toolTip"));
        M_211_IMPORT_FROM_FILE_SYSTEM.addActionListener(actionEvent -> ThreadHandler.startModThread(() -> SharingManager.importAll(ImportType.MANUEL), "ImportAll"));
        M_212_IMPORT_FROM_URL.setToolTipText(I18n.INSTANCE.get("window.main.mods.import.importFromURL.toolTip"));
        M_212_IMPORT_FROM_URL.addActionListener(actionEvent -> ThreadHandler.startModThread(ImportFromURLHelper::importFromURL, "ImportFromURL"));
        M_22_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.mods.npcGamesList.toolTip"));
        M_22_NPC_GAMES_LIST.addActionListener(actionEvent -> ThreadHandler.startModThread(NPCGameListHandler::modifyNPCGameList, "NPCGamesList"));
        M_23_ADD_COMPANY_ICON.addActionListener(actionEvent -> ThreadHandler.startModThread(NewModsHandler::addCompanyIcon, "AddCompanyIcon"));
        m210ShowActiveMods.setToolTipText(I18n.INSTANCE.get("window.main.mods.showActiveMods.toolTip"));
        m210ShowActiveMods.addActionListener(actionEvent -> ThreadHandler.startModThread(ActiveMods::showActiveMods, "ShowActiveMods"));
        MB.add(M_2_MODS);
        M_2_MODS.add(M_21_IMPORT);
        initializeModMenus();
        M_2_MODS.add(M_22_NPC_GAMES_LIST);
        M_2_MODS.add(M_23_ADD_COMPANY_ICON);
        M_2_MODS.add(m210ShowActiveMods);
        M_233_CHANGE_GENRE_THEME_FIT.addActionListener(actionEvent -> ThreadHandler.startModThread(ContentEditor::editGenreThemeFit, "EditGenreThemeFit"));
        M_31_EXPORT.add(M_317_EXPORT_ALL);
        M_31_EXPORT.add(M_318_EXPORT_SELECTED);
        M_317_EXPORT_ALL.addActionListener(actionEvent -> ThreadHandler.startModThread(SharingManager::displayExportModsWindow, "ExportAll"));
        M_318_EXPORT_SELECTED.addActionListener(actionEvent -> ThreadHandler.startModThread(SharingManager::exportSelected, "ExportSelected"));
        JMenuItem m35 = new JMenuItem(I18n.INSTANCE.get("window.main.share.openExportFolder"));
        m35.addActionListener(actionEvent -> Utils.open(ModManagerPaths.EXPORT.getPath()));
        JMenuItem m36 = new JMenuItem(I18n.INSTANCE.get("window.main.share.deleteAllExport"));
        m36.addActionListener(actionEvent -> ThreadHandler.startModThread(Uninstaller::deleteAllExports, "DeleteExports"));
        M_3_SHARE.add(M_31_EXPORT);
        M_3_SHARE.add(m35);
        M_3_SHARE.add(m36);
        MB.add(M_3_SHARE);
        JMenu m41 = new JMenu(I18n.INSTANCE.get("window.main.backup.createBackup"));
        JMenu m43RestorePoint = new JMenu(I18n.INSTANCE.get("window.main.backup.modRestorePoint"));
        JMenuItem m411CreateNewInitialBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createNewInitialBackup"));
        m411CreateNewInitialBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createNewInitialBackup.toolTip"));
        m411CreateNewInitialBackup.addActionListener(actionEvent -> ThreadHandler.startModThread(Backup::createNewInitialBackup, "CreateNewInitialBackup"));
        JMenuItem m412CreateFullBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup"));
        m412CreateFullBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup.toolTip"));
        m412CreateFullBackup.addActionListener(actionEvent -> ThreadHandler.startModThread(() -> Backup.createBackup(BackupType.FULL), "CreateFullBackup"));
        JMenuItem m413BackupSaveGames = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createSaveGameBackup"));
        m413BackupSaveGames.addActionListener(actionEvent -> ThreadHandler.startModThread(() -> Backup.createBackup(BackupType.SAVE_GAME), "CreateSaveGameBackup"));
        m41.add(m411CreateNewInitialBackup);
        m41.add(m412CreateFullBackup);
        m41.add(m413BackupSaveGames);
        JMenuItem m421RestoreInitialBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup"));
        m421RestoreInitialBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup.toolTip"));
        m421RestoreInitialBackup.addActionListener(actionEvent -> ThreadHandler.startModThread(Backup::restoreInitialBackup, "RestoreInitialBackup"));
        M_422_RESTORE_LATEST_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup.toolTip"));
        M_422_RESTORE_LATEST_BACKUP.addActionListener(actionEvent -> ThreadHandler.startModThread(Backup::restoreLatestBackup, "RestoreLatestBackup"));
        JMenuItem m423RestoreSaveGameBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup"));
        m423RestoreSaveGameBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup.toolTip"));
        m423RestoreSaveGameBackup.addActionListener(actionEvent -> ThreadHandler.startModThread(Backup::restoreSaveGameBackup, "RestoreSaveGameBackup"));
        M_42_RESTORE_BACKUP.add(m421RestoreInitialBackup);
        M_42_RESTORE_BACKUP.add(m423RestoreSaveGameBackup);
        M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint.toolTip"));
        M_431_CREATE_MOD_RESTORE_POINT.addActionListener(actionEvent -> ThreadHandler.startModThread(RestorePointHelper::setRestorePoint, "CreateRestorePoint"));
        M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.toolTip"));
        M_432_RESTORE_MOD_RESTORE_POINT.addActionListener(actionEvent -> ThreadHandler.startModThread(RestorePointHelper::restoreToRestorePoint, "RestoreToRestorePoint"));
        m43RestorePoint.add(M_431_CREATE_MOD_RESTORE_POINT);
        m43RestorePoint.add(M_432_RESTORE_MOD_RESTORE_POINT);
        M_44_DELETE_ALL_BACKUPS.setToolTipText(I18n.INSTANCE.get("window.main.backup.deleteAllBackups.toolTip"));
        M_44_DELETE_ALL_BACKUPS.addActionListener(actionEvent -> Backup.deleteAllBackups());
        JMenuItem m45penBackupFolder = new JMenuItem(I18n.INSTANCE.get("window.main.backup.openBackupFolder"));
        m45penBackupFolder.setToolTipText(I18n.INSTANCE.get("window.main.backup.openBackupFolder.toolTip"));
        m45penBackupFolder.addActionListener(actionEvent -> Utils.open(ModManagerPaths.BACKUP.getPath()));
        MB.add(M_4_BACKUP);
        M_4_BACKUP.add(m41);
        M_4_BACKUP.add(M_42_RESTORE_BACKUP);
        M_4_BACKUP.add(m43RestorePoint);
        M_4_BACKUP.add(m45penBackupFolder);
        setSafetyFeatureComponents();
        JMenuItem m52OpenGitHubPage = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGithubPage"));
        m52OpenGitHubPage.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m53OpenMGT2Folder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openMGT2Folder"));
        m53OpenMGT2Folder.addActionListener(actionEvent -> Utils.open(Settings.mgt2Path));
        JMenuItem m54OpenSaveGameFolder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder"));
        m54OpenSaveGameFolder.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder.toolTip"));
        m54OpenSaveGameFolder.addActionListener(actionEvent -> Utils.open(Backup.FILE_SAVE_GAME_FOLDER));
        JMenuItem m55OpenSettingsTomlFile = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openSettingsTomlFile"));
        m55OpenSettingsTomlFile.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openSettingsTomlFile.toolTip"));
        m55OpenSettingsTomlFile.addActionListener(actionEvent -> Utils.open(ModManagerPaths.MAIN.getPath().resolve("settings.toml")));
        JMenuItem m560ReanalyzeGameFiles = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.reanalyzeGameFiles"));
        m560ReanalyzeGameFiles.setToolTipText(I18n.INSTANCE.get("window.main.utilities.reanalyzeGameFiles.toolTip"));
        m560ReanalyzeGameFiles.addActionListener(actionEvent -> reanalyzeGameFiles());
        JMenuItem m570GenerateHelpSheet = new JMenuItem(I18n.INSTANCE.get("Generate help sheet"));
        m570GenerateHelpSheet.setToolTipText(I18n.INSTANCE.get("Generates a help sheet with the correct slider settings for the current game files"));
        m570GenerateHelpSheet.addActionListener(actionEvent -> ThreadHandler.startModThread(HelpSheetGenerator::generate, "HelpSheetGenerator"));
        MB.add(M_5_UTIL);
        M_5_UTIL.add(m52OpenGitHubPage);
        M_5_UTIL.add(m53OpenMGT2Folder);
        M_5_UTIL.add(m54OpenSaveGameFolder);
        M_5_UTIL.add(m55OpenSettingsTomlFile);
        M_5_UTIL.add(m560ReanalyzeGameFiles);
        M_5_UTIL.add(m570GenerateHelpSheet);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel labelVersion = new JLabel("v" + MadGamesTycoon2ModTool.VERSION);
        JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.quit"));
        buttonQuit.addActionListener(actionEvent -> {
            if (ThreadHandler.getThreadsRunning() > 0) {
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.button.quit.taskPerformed"), I18n.INSTANCE.get("window.main.button.quit.taskPerformed.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    disposeFrame();
                }
            } else {
                disposeFrame();
            }
        });
        JLabel labelModCreator = new JLabel("by LMH01");
        // Components Added using Flow Layout
        panel.add(labelVersion);
        panel.add(buttonQuit);
        panel.add(labelModCreator);

        //Progress Bar
        JPanel panelMiddle = new JPanel();
        panelMiddle.setLayout(new BoxLayout(panelMiddle, BoxLayout.Y_AXIS));
        PROGRESS_BAR.setString(I18n.INSTANCE.get("progressBar.idle"));
        PROGRESS_BAR.setStringPainted(true);
        TEXT_AREA.setEditable(false);
        panelMiddle.add(PROGRESS_BAR);
        panelMiddle.add(SCROLL_PANE);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, MB);
        frame.getContentPane().add(BorderLayout.CENTER, panelMiddle);
        frame.setVisible(true);
    }

    public static void disposeFrame() {
        frame.dispose();
        System.exit(0);
    }

    /**
     * Checks if specific actions are available. If they are the buttons will be enabled
     * The following things are also done: Progress bar is reset, auto scrolling gets disabled and menu items are unlocked.
     */
    public static void checkActionAvailability() {
        if (Settings.mgt2FolderIsCorrect) {
            try {
                boolean noModsAvailable = true;
                ContentAdministrator.analyzeContents();
                CompanyLogoAnalyzer.analyzeLogoNumbers();
                boolean noModRestorePointSet = true;
                if (ModManagerPaths.CURRENT_RESTORE_POINT.toFile().exists()) {
                    noModRestorePointSet = false;
                }
                for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                    manager.setMainMenuButtonAvailability();
                    if (manager.getCustomContentString().length > 0) {
                        noModsAvailable = false;
                    }
                }
                if (GenreManager.INSTANCE.getCustomContentString().length > 0) {
                    M_22_NPC_GAMES_LIST.setEnabled(true);
                    M_22_NPC_GAMES_LIST.setToolTipText("");
                } else {
                    M_22_NPC_GAMES_LIST.setEnabled(false);
                    M_22_NPC_GAMES_LIST.setToolTipText(String.format(I18n.INSTANCE.get("modManager.windowMain.modButton.removeMod.toolTip"), GenreManager.INSTANCE.getType()));
                }
                M_432_RESTORE_MOD_RESTORE_POINT.setEnabled(!noModRestorePointSet);
                if (noModsAvailable) {
                    M_317_EXPORT_ALL.setEnabled(false);
                    M_317_EXPORT_ALL.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noToExportAvailable"));
                    M_318_EXPORT_SELECTED.setEnabled(false);
                    M_318_EXPORT_SELECTED.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noToExportAvailable"));
                    M_431_CREATE_MOD_RESTORE_POINT.setEnabled(false);
                    M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.notAvailableToCreateRestorePoint"));
                } else {
                    M_317_EXPORT_ALL.setEnabled(true);
                    M_317_EXPORT_ALL.setToolTipText(I18n.INSTANCE.get("window.main.share.export.exportAll.toolTip"));
                    M_318_EXPORT_SELECTED.setEnabled(true);
                    M_318_EXPORT_SELECTED.setToolTipText(I18n.INSTANCE.get("window.main.share.export.exportSelected.toolTip"));
                    M_431_CREATE_MOD_RESTORE_POINT.setEnabled(true);
                    M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint.toolTip"));
                }
                if (noModRestorePointSet) {
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.notAvailableToolTip"));
                } else {
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText("");
                }
                if (Settings.enableDisclaimerMessage) {
                    M_21_IMPORT.setEnabled(false);
                    for (JMenu menu : MOD_MENUS) {
                        menu.setEnabled(false);
                        menu.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    }
                    M_22_NPC_GAMES_LIST.setEnabled(false);
                    M_23_ADD_COMPANY_ICON.setEnabled(false);
                    M_233_CHANGE_GENRE_THEME_FIT.setEnabled(false);
                    M_21_IMPORT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_22_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_23_ADD_COMPANY_ICON.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_233_CHANGE_GENRE_THEME_FIT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                } else {
                    M_21_IMPORT.setEnabled(true);
                    for (JMenu menu : MOD_MENUS) {
                        menu.setEnabled(true);
                        menu.setToolTipText("");
                    }
                    M_23_ADD_COMPANY_ICON.setEnabled(true);
                    M_233_CHANGE_GENRE_THEME_FIT.setEnabled(true);
                    M_21_IMPORT.setToolTipText("");
                    M_23_ADD_COMPANY_ICON.setToolTipText("");
                    M_233_CHANGE_GENRE_THEME_FIT.setToolTipText(I18n.INSTANCE.get("window.main.mods.themes.changeGenreThemeFit.toolTip"));
                }
            } catch (IndexOutOfBoundsException | ModProcessingException e) {
                TextAreaHelper.printStackTrace(e);
                LOGGER.info("Error: " + e.getMessage());
            }
            WindowMain.lockMenuItems(false);
            setSafetyFeatureComponents();
        } else {
            LOGGER.info("Action availability was not checked because the mgt2 folder is invalid!");
        }
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Disables all menu items so they can no longer be klicked. Used when a thread is started.
     *
     * @param lock True when the items should be locked. False when the items should be unlocked
     */
    public static void lockMenuItems(boolean lock) {
        M_1_FILE.setEnabled(!lock);
        M_2_MODS.setEnabled(!lock);
        M_3_SHARE.setEnabled(!lock);
        M_4_BACKUP.setEnabled(!lock);
        M_5_UTIL.setEnabled(!lock);
        if (lock) {
            M_1_FILE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_2_MODS.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_3_SHARE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_4_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_5_UTIL.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
        } else {
            M_1_FILE.setToolTipText("");
            M_2_MODS.setToolTipText("");
            M_3_SHARE.setToolTipText("");
            M_4_BACKUP.setToolTipText("");
            M_5_UTIL.setToolTipText("");
        }
    }

    /**
     * Will disable all menus except File {@literal ->} CheckForUpdates, About and settings
     *
     * @param folderAvailable True when the mgt2 folder is available
     */
    public static void setMGT2FolderAvailability(boolean folderAvailable) {
        M_12_UPDATE_CHECK.setEnabled(folderAvailable);
        M_13_UNINSTALL.setEnabled(folderAvailable);
        M_2_MODS.setEnabled(folderAvailable);
        M_3_SHARE.setEnabled(folderAvailable);
        M_4_BACKUP.setEnabled(folderAvailable);
        M_5_UTIL.setEnabled(folderAvailable);
        if (folderAvailable) {
            M_12_UPDATE_CHECK.setToolTipText("");
            M_13_UNINSTALL.setToolTipText("");
            M_2_MODS.setToolTipText("");
            M_3_SHARE.setToolTipText("");
            M_4_BACKUP.setToolTipText("");
            M_5_UTIL.setToolTipText("");
        } else {
            M_12_UPDATE_CHECK.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
            M_13_UNINSTALL.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
            M_2_MODS.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
            M_3_SHARE.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
            M_4_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
            M_5_UTIL.setToolTipText(I18n.INSTANCE.get("window.main.mgt2FolderNotFound.toolTip"));
        }
    }

    /**
     * This will cause some menu items to be reloaded, for the safety feature setting to be applied.
     */
    public static void setSafetyFeatureComponents() {
        if (Settings.safetyFeatures.get(SafetyFeature.DISABLE_BACKUP_SECURITY_MECHANISMS)) {
            M_4_BACKUP.add(M_44_DELETE_ALL_BACKUPS);
            M_42_RESTORE_BACKUP.add(M_422_RESTORE_LATEST_BACKUP);
        } else {
            M_4_BACKUP.remove(M_44_DELETE_ALL_BACKUPS);
            M_42_RESTORE_BACKUP.remove(M_422_RESTORE_LATEST_BACKUP);
        }
    }

    private static final ArrayList<JMenu> MOD_MENUS = new ArrayList<>();

    public static void initializeModMenus() {
        if (!modMenusInitialized) {
            for (BaseContentManager manager : ContentAdministrator.contentManagers) {
                JMenu menu = new JMenu(manager.getTypePlural());
                for (JMenuItem menuItem : manager.getModMenuItems()) {
                    menu.add(menuItem);
                }
                if (manager instanceof ThemeManager) {
                    menu.add(M_233_CHANGE_GENRE_THEME_FIT);
                }
                MOD_MENUS.add(menu);
                M_31_EXPORT.add(manager.getExportMenuItem());
            }
            for (JMenu menu : MOD_MENUS) {
                M_2_MODS.add(menu);
            }
            modMenusInitialized = true;
        }
    }

    private static void openGithubPage() {
        if (MadGamesTycoon2ModTool.VERSION.contains("dev")) {
            Debug.test();
        } else {
            if (JOptionPane.showConfirmDialog(null, "Open Github page?", "Open page?", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Utils.openGithubPage();
                } catch (Exception e) {
                    Utils.showErrorMessage(2, e);
                    e.printStackTrace();
                }
            }
        }
    }

    private static void openMoreModsPage() {
        if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.mods.import.getMoreMods.confirmDialog"), I18n.INSTANCE.get("window.main.mods.import.getMoreMods.confirmDialog.title"), JOptionPane.YES_NO_OPTION) == 0) {
            try {
                Utils.openMoreModsPage();
            } catch (Exception e) {
                Utils.showErrorMessage(2, e);
                e.printStackTrace();
            }
        }
    }

    private static void reanalyzeGameFiles() {
        ThreadHandler.startModThread(() -> {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.reanalyzeGameFiles"), I18n.INSTANCE.get("frame.title.areYouSure"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ContentAdministrator.analyzeContents();
                ContentAdministrator.analyzeGameFileIntegrity(true);
            }
        }, "ReanalyzeGameFiles");
    }
}
