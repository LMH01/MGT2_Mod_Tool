package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private static final JMenu M_42_RESTORE_BACKUP = new JMenu(I18n.INSTANCE.get("window.main.backup.restoreBackup"));
    private static final JMenuItem M_422_RESTORE_LATEST_BACKUP = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup"));
    private static final JMenuItem M_431_CREATE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint"));
    private static final JMenuItem M_432_RESTORE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint"));
    private static final JMenuItem M_44_DELETE_ALL_BACKUPS = new JMenuItem(I18n.INSTANCE.get("window.main.backup.deleteAllBackups"));
    private static final JMenuItem M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.replacePublisher"));
    public static final JProgressBar PROGRESS_BAR = new JProgressBar();
    public static final JTextArea TEXT_AREA = new JTextArea();
    public static final JScrollPane SCROLL_PANE = new JScrollPane(TEXT_AREA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private static boolean modMenusInitialized = false;
    public static void createFrame(){
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
        M_13_UNINSTALL.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableUninstall, "Uninstall"));
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
        M_211_IMPORT_FROM_FILE_SYSTEM.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableImportAll, "runnableImportAll"));
        M_212_IMPORT_FROM_URL.setToolTipText(I18n.INSTANCE.get("window.main.mods.import.importFromURL.toolTip"));
        M_212_IMPORT_FROM_URL.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableImportFromURL, "runnableImportFromURL"));
        M_22_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.mods.npcGamesList.toolTip"));
        M_22_NPC_GAMES_LIST.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableNPCGamesList, "runnableNPCGamesList"));
        M_23_ADD_COMPANY_ICON.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddCompanyIcon, "runnableAddCompanyIcon"));
        m210ShowActiveMods.setToolTipText(I18n.INSTANCE.get("window.main.mods.showActiveMods.toolTip"));
        m210ShowActiveMods.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableShowActiveMods, "runnableShowActiveMods"));
        MB.add(M_2_MODS);
        M_2_MODS.add(M_21_IMPORT);
        initializeModMenus();
        M_2_MODS.add(M_22_NPC_GAMES_LIST);
        M_2_MODS.add(M_23_ADD_COMPANY_ICON);
        M_2_MODS.add(m210ShowActiveMods);
        M_233_CHANGE_GENRE_THEME_FIT.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableEditGenreThemeFit, "EditGenreThemeFit"));
        M_31_EXPORT.add(M_317_EXPORT_ALL);
        M_317_EXPORT_ALL.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportAll, "runnableExportAll"));
        JMenuItem m35 = new JMenuItem(I18n.INSTANCE.get("window.main.share.openExportFolder"));
        m35.addActionListener(actionEvent -> Utils.open(Utils.getMGT2ModToolExportFolder()));
        JMenuItem m36 = new JMenuItem(I18n.INSTANCE.get("window.main.share.deleteAllExport"));
        m36.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableDeleteExports, "runnableDeleteExports"));
        M_3_SHARE.add(M_31_EXPORT);
        M_3_SHARE.add(m35);
        M_3_SHARE.add(m36);
        MB.add(M_3_SHARE);
        JMenu m41 = new JMenu(I18n.INSTANCE.get("window.main.backup.createBackup"));
        JMenu m43RestorePoint = new JMenu(I18n.INSTANCE.get("window.main.backup.modRestorePoint"));
        JMenuItem m411CreateNewInitialBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createNewInitialBackup"));
        m411CreateNewInitialBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createNewInitialBackup.toolTip"));
        m411CreateNewInitialBackup.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateNewInitialBackup, "CreateNewInitialBackup"));
        JMenuItem m412CreateFullBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup"));
        m412CreateFullBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup.toolTip"));
        m412CreateFullBackup.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateFullBackup, "CreateFullBackup"));
        JMenuItem m413BackupSaveGames = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createSaveGameBackup"));
        m413BackupSaveGames.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateSaveGameBackup, "CreateSaveGameBackup"));
        m41.add(m411CreateNewInitialBackup);
        m41.add(m412CreateFullBackup);
        m41.add(m413BackupSaveGames);
        JMenuItem m421RestoreInitialBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup"));
        m421RestoreInitialBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup.toolTip"));
        m421RestoreInitialBackup.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRestoreInitialBackup, "RestoreInitialBackup"));
        M_422_RESTORE_LATEST_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup.toolTip"));
        M_422_RESTORE_LATEST_BACKUP.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRestoreLatestBackup, "RestoreLatestBackup"));
        JMenuItem m423RestoreSaveGameBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup"));
        m423RestoreSaveGameBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup.toolTip"));
        m423RestoreSaveGameBackup.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRestoreSaveGameBackup, "RestoreSaveGameBackup"));
        M_42_RESTORE_BACKUP.add(m421RestoreInitialBackup);
        M_42_RESTORE_BACKUP.add(m423RestoreSaveGameBackup);
        M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint.toolTip"));
        M_431_CREATE_MOD_RESTORE_POINT.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateRestorePoint, "CreateRestorePoint"));
        M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.toolTip"));
        M_432_RESTORE_MOD_RESTORE_POINT.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRestoreToRestorePoint, "RestoreToRestorePoint"));
        m43RestorePoint.add(M_431_CREATE_MOD_RESTORE_POINT);
        m43RestorePoint.add(M_432_RESTORE_MOD_RESTORE_POINT);
        M_44_DELETE_ALL_BACKUPS.setToolTipText(I18n.INSTANCE.get("window.main.backup.deleteAllBackups.toolTip"));
        M_44_DELETE_ALL_BACKUPS.addActionListener(actionEvent -> Backup.deleteAllBackups());
        JMenuItem m45penBackupFolder = new JMenuItem(I18n.INSTANCE.get("window.main.backup.openBackupFolder"));
        m45penBackupFolder.setToolTipText(I18n.INSTANCE.get("window.main.backup.openBackupFolder.toolTip"));
        m45penBackupFolder.addActionListener(actionEvent -> Utils.open(Settings.MGT2_MOD_MANAGER_PATH + "//Backup//"));
        MB.add(M_4_BACKUP);
        M_4_BACKUP.add(m41);
        M_4_BACKUP.add(M_42_RESTORE_BACKUP);
        M_4_BACKUP.add(m43RestorePoint);
        M_4_BACKUP.add(m45penBackupFolder);
        setSafetyFeatureComponents();
        JMenu m51ExperimentalFeatures = new JMenu(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures"));
        m51ExperimentalFeatures.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.toolTip"));
        M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.addActionListener(actionEvent -> ModManager.publisherMod.realPublishers());
        JMenuItem m52OpenGitHubPage = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGithubPage"));
        m52OpenGitHubPage.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m53OpenMGT2Folder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openMGT2Folder"));
        m53OpenMGT2Folder.addActionListener(actionEvent -> Utils.open(Settings.mgt2FilePath));
        JMenuItem m54OpenSaveGameFolder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder"));
        m54OpenSaveGameFolder.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder.toolTip"));
        m54OpenSaveGameFolder.addActionListener(actionEvent -> Utils.open(Backup.FILE_SAVE_GAME_FOLDER.getPath()));
        MB.add(M_5_UTIL);
        M_5_UTIL.add(m51ExperimentalFeatures);
        m51ExperimentalFeatures.add(M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS);
        M_5_UTIL.add(m52OpenGitHubPage);
        M_5_UTIL.add(m53OpenMGT2Folder);
        M_5_UTIL.add(m54OpenSaveGameFolder);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel labelVersion = new JLabel("v" + MadGamesTycoon2ModTool.VERSION);
        JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.quit"));
        buttonQuit.addActionListener(actionEvent -> {
            if(ThreadHandler.getThreadsRunning() > 0){
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.button.quit.taskPerformed"), I18n.INSTANCE.get("window.main.button.quit.taskPerformed.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    disposeFrame();
                }
            }else{
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
    public static void disposeFrame(){
        Debug.test();
        frame.dispose();
        System.exit(0);
    }
    /**
     * Checks if specific actions are available. If they are the buttons will be enabled
     * The following things are also done: Progress bar is reset, auto scrolling gets disabled and menu items are unlocked.
     */
    public static void checkActionAvailability(){
        if(Settings.mgt2FolderIsCorrect){
            try{
                boolean noModsAvailable = true;
                ModManager.analyzeMods();
                boolean noModRestorePointSet = true;
                if(!Settings.disableSafetyFeatures) {
                    if(new File(Utils.getMGT2ModToolModRestorePointFolder()).exists()){
                        noModRestorePointSet = false;
                    }
                }
                for(AbstractSimpleMod simpleMod : ModManager.simpleMods){
                    simpleMod.setMainMenuButtonAvailability();
                    if(!simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                        if(simpleMod.getBaseAnalyzer().getCustomContentString(true).length > 0){
                            noModsAvailable = false;
                        }
                    }else{
                        if(ModManager.themeMod.getAnalyzerEn().getCustomContentString(true).length > 0){
                            noModsAvailable = false;
                        }
                    }
                }
                for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                    advancedMod.setMainMenuButtonAvailability();
                    if(advancedMod.getBaseAnalyzer().getCustomContentString(true).length > 0){
                        noModsAvailable = false;
                    }
                }
                if(ModManager.genreMod.getAnalyzer().getCustomContentString(true).length > 0){
                    M_22_NPC_GAMES_LIST.setEnabled(true);
                    M_22_NPC_GAMES_LIST.setToolTipText("");
                }else{
                    M_22_NPC_GAMES_LIST.setEnabled(false);
                    M_22_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("modManager.genre.windowMain.modButton.removeMod.toolTip"));
                }
                M_432_RESTORE_MOD_RESTORE_POINT.setEnabled(!noModRestorePointSet);
                if(noModsAvailable){
                    M_317_EXPORT_ALL.setEnabled(false);
                    M_317_EXPORT_ALL.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noToExportAvailable"));
                    M_431_CREATE_MOD_RESTORE_POINT.setEnabled(false);
                    M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.notAvailableToCreateRestorePoint"));
                }else{
                    M_317_EXPORT_ALL.setEnabled(true);
                    M_317_EXPORT_ALL.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.exportAvailable"));
                    M_431_CREATE_MOD_RESTORE_POINT.setEnabled(true);
                    M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint.toolTip"));
                }
                if(noModRestorePointSet){
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.notAvailableToolTip"));
                }else{
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText("");
                }
                if(Settings.enableDisclaimerMessage){
                    M_21_IMPORT.setEnabled(false);
                    for(JMenu menu : MOD_MENUS){
                        menu.setEnabled(false);
                        menu.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    }
                    M_22_NPC_GAMES_LIST.setEnabled(false);
                    M_23_ADD_COMPANY_ICON.setEnabled(false);
                    M_233_CHANGE_GENRE_THEME_FIT.setEnabled(false);
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setEnabled(false);
                    M_21_IMPORT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_22_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_23_ADD_COMPANY_ICON.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_233_CHANGE_GENRE_THEME_FIT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                }else{
                    M_21_IMPORT.setEnabled(true);
                    for(JMenu menu : MOD_MENUS){
                        menu.setEnabled(true);
                        menu.setToolTipText("");
                    }
                    M_23_ADD_COMPANY_ICON.setEnabled(true);
                    M_233_CHANGE_GENRE_THEME_FIT.setEnabled(true);
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setEnabled(true);
                    M_21_IMPORT.setToolTipText("");
                    M_23_ADD_COMPANY_ICON.setToolTipText("");
                    M_233_CHANGE_GENRE_THEME_FIT.setToolTipText(I18n.INSTANCE.get("window.main.mods.themes.changeGenreThemeFit.toolTip"));
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.replacePublisher.toolTip"));
                }
            }catch (IndexOutOfBoundsException e){
                LOGGER.info("Error: " + e.getMessage());
                e.printStackTrace();
            }
            WindowMain.lockMenuItems(false);
            setSafetyFeatureComponents();
        }else{
            LOGGER.info("Action availability was not checked because the mgt2 folder is invalid!");
        }
        ProgressBarHelper.resetProgressBar();
    }

    /**
     * Disables all menu items so they can no longer be klicked. Used when a thread is started.
     * @param lock True when the items should be locked. False when the items should be unlocked
     */
    public static void lockMenuItems(boolean lock){
        M_1_FILE.setEnabled(!lock);
        M_2_MODS.setEnabled(!lock);
        M_3_SHARE.setEnabled(!lock);
        M_4_BACKUP.setEnabled(!lock);
        M_5_UTIL.setEnabled(!lock);
        if(lock){
            M_1_FILE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_2_MODS.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_3_SHARE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_4_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_5_UTIL.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
        }else{
            M_1_FILE.setToolTipText("");
            M_2_MODS.setToolTipText("");
            M_3_SHARE.setToolTipText("");
            M_4_BACKUP.setToolTipText("");
            M_5_UTIL.setToolTipText("");
        }
    }

    /**
     * Will disable all menus except File -> CheckForUpdates, About and settings
     */
    public static void setMGT2FolderAvailability(boolean folderAvailable){
        M_12_UPDATE_CHECK.setEnabled(folderAvailable);
        M_13_UNINSTALL.setEnabled(folderAvailable);
        M_2_MODS.setEnabled(folderAvailable);
        M_3_SHARE.setEnabled(folderAvailable);
        M_4_BACKUP.setEnabled(folderAvailable);
        M_5_UTIL.setEnabled(folderAvailable);
        if(folderAvailable){
            M_12_UPDATE_CHECK.setToolTipText("");
            M_13_UNINSTALL.setToolTipText("");
            M_2_MODS.setToolTipText("");
            M_3_SHARE.setToolTipText("");
            M_4_BACKUP.setToolTipText("");
            M_5_UTIL.setToolTipText("");
        }else{
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
    public static void setSafetyFeatureComponents(){
        if(Settings.disableSafetyFeatures){
            M_4_BACKUP.add(M_44_DELETE_ALL_BACKUPS);
            M_42_RESTORE_BACKUP.add(M_422_RESTORE_LATEST_BACKUP);
        }else{
            M_4_BACKUP.remove(M_44_DELETE_ALL_BACKUPS);
            M_42_RESTORE_BACKUP.remove(M_422_RESTORE_LATEST_BACKUP);
        }
    }

    private static final ArrayList<JMenu> MOD_MENUS = new ArrayList<>();

    public static void initializeModMenus(){
        if(!modMenusInitialized){
            if(Settings.useAutomaticModMenus){
                for(AbstractSimpleMod simpleMod : ModManager.simpleMods){//Use this when the menus should be allocated automatically
                    JMenu menu = new JMenu(simpleMod.getTypePlural());
                    for(JMenuItem menuItem : simpleMod.getModMenuItems()){
                        menu.add(menuItem);
                    }
                    MOD_MENUS.add(menu);
                    M_31_EXPORT.add(simpleMod.getExportMenuItem());
                }
                for(AbstractAdvancedMod advancedMod : ModManager.advancedMods){
                    JMenu menu = new JMenu(advancedMod.getTypePlural());
                    for(JMenuItem menuItem : advancedMod.getModMenuItems()){
                        menu.add(menuItem);
                    }
                    MOD_MENUS.add(menu);
                    M_31_EXPORT.add(advancedMod.getExportMenuItem());
                }
                for(JMenu menu : MOD_MENUS){
                    M_2_MODS.add(menu);
                }
            }else{
                //Manual menu allocation:
                JMenu menu = new JMenu(ModManager.genreMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.genreMod.getModMenuItems()){
                    menu.add(menuItem);
                }
                JMenu menu2 = new JMenu(ModManager.themeMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.themeMod.getModMenuItems()){
                    menu2.add(menuItem);
                }
                menu2.add(M_233_CHANGE_GENRE_THEME_FIT);
                JMenu menu3 = new JMenu(ModManager.publisherMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.publisherMod.getModMenuItems()){
                    menu3.add(menuItem);
                }
                JMenu menu4 = new JMenu(ModManager.engineFeatureMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.engineFeatureMod.getModMenuItems()){
                    menu4.add(menuItem);
                }
                JMenu menu5 = new JMenu(ModManager.gameplayFeatureMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.gameplayFeatureMod.getModMenuItems()){
                    menu5.add(menuItem);
                }
                JMenu menu6 = new JMenu(ModManager.licenceMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.licenceMod.getModMenuItems()){
                    menu6.add(menuItem);
                }
                JMenu menu7 = new JMenu(ModManager.npcGamesMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.npcGamesMod.getModMenuItems()){
                    menu7.add(menuItem);
                }
                JMenu menu8 = new JMenu(ModManager.npcEngineMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.npcEngineMod.getModMenuItems()){
                    menu8.add(menuItem);
                }
                JMenu menu9 = new JMenu(ModManager.platformMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.platformMod.getModMenuItems()){
                    menu9.add(menuItem);
                }
                JMenu menu10 = new JMenu(ModManager.antiCheatMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.antiCheatMod.getModMenuItems()){
                    menu10.add(menuItem);
                }
                JMenu menu11 = new JMenu(ModManager.copyProtectMod.getTypePlural());
                for(JMenuItem menuItem : ModManager.copyProtectMod.getModMenuItems()){
                    menu11.add(menuItem);
                }
                MOD_MENUS.add(menu);
                MOD_MENUS.add(menu2);
                MOD_MENUS.add(menu3);
                MOD_MENUS.add(menu4);
                MOD_MENUS.add(menu5);
                MOD_MENUS.add(menu6);
                MOD_MENUS.add(menu7);
                MOD_MENUS.add(menu8);
                MOD_MENUS.add(menu9);
                MOD_MENUS.add(menu10);
                MOD_MENUS.add(menu11);
                M_2_MODS.add(menu);
                M_2_MODS.add(menu2);
                M_2_MODS.add(menu3);
                M_2_MODS.add(menu4);
                M_2_MODS.add(menu5);
                M_2_MODS.add(menu6);
                M_2_MODS.add(menu7);
                M_2_MODS.add(menu8);
                M_2_MODS.add(menu9);
                M_2_MODS.add(menu10);
                M_2_MODS.add(menu11);
                M_31_EXPORT.add(ModManager.genreMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.themeMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.publisherMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.engineFeatureMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.gameplayFeatureMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.licenceMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.npcGamesMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.npcEngineMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.platformMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.antiCheatMod.getExportMenuItem());
                M_31_EXPORT.add(ModManager.copyProtectMod.getExportMenuItem());
            }
            modMenusInitialized = true;
        }
    }

    public static void restoreInitialBackup(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                StringBuilder stringBuilder = new StringBuilder();
                Uninstaller.uninstallAllMods(stringBuilder);
                if(!stringBuilder.toString().isEmpty()){
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored.mods") + "\n\n" + stringBuilder.toString(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.WARNING_MESSAGE);
                }
                Backup.restoreBackup(true, true);
            } catch (IOException e) {
                e.printStackTrace();
                if(Utils.showConfirmDialog(1, e)){
                    Backup.restoreBackup(true, true);
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.notRestored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        checkActionAvailability();
    }
    public static void restoreLatestBackup(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                Backup.restoreBackup(false, true);
            } catch (IOException e) {
                e.printStackTrace();
                if(Utils.showConfirmDialog(1, e)){
                    Backup.restoreBackup(false, true);
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.notRestored"), I18n.INSTANCE.get("dialog.backup.restoreBackup.failed"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private static void openGithubPage(){
        if(JOptionPane.showConfirmDialog(null, "Open Github page?", "Open page?", JOptionPane.YES_NO_OPTION) == 0){
            try {
                Utils.openGithubPage();
            } catch (Exception e) {
                Utils.showErrorMessage(2, e);
                e.printStackTrace();
            }
        }
    }
    private static void openMoreModsPage(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.mods.import.getMoreMods.confirmDialog"), I18n.INSTANCE.get("window.main.mods.import.getMoreMods.confirmDialog.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                Utils.openMoreModsPage();
            } catch (Exception e) {
                Utils.showErrorMessage(2, e);
                e.printStackTrace();
            }
        }
    }
}
