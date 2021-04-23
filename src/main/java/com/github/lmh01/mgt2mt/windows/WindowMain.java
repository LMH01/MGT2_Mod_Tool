package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.util.*;
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
import java.util.List;

public class WindowMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowMain.class);
    private static final JFrame frame = new JFrame(I18n.INSTANCE.get("window.main.tile"));
    private static final JMenu M_1_FILE = new JMenu(I18n.INSTANCE.get("window.main.file"));
    private static final JMenu M_2_MODS = new JMenu(I18n.INSTANCE.get("window.main.mods"));
    private static final JMenu M_3_SHARE = new JMenu(I18n.INSTANCE.get("window.main.share"));
    private static final JMenu M_4_BACKUP = new JMenu(I18n.INSTANCE.get("window.main.backup"));
    private static final JMenu M_5_UTIL = new JMenu(I18n.INSTANCE.get("window.main.utilities"));
    private static final JMenuItem M_12_UPDATE_CHECK = new JMenuItem(I18n.INSTANCE.get("window.main.file.updateCheck"));
    private static final JMenuItem M_13_UNINSTALL = new JMenuItem(I18n.INSTANCE.get("window.main.file.uninstall"));
    private static final JMenu M_21_IMPORT = new JMenu(I18n.INSTANCE.get("window.main.mods.import"));
    private static final JMenu M_22_GENRES = new JMenu(I18n.INSTANCE.get("window.main.mods.genres"));
    private static final JMenu M_23_THEMES = new JMenu(I18n.INSTANCE.get("window.main.mods.themes"));
    private static final JMenu M_24_PUBLISHER = new JMenu(I18n.INSTANCE.get("window.main.mods.publisher"));
    private static final JMenu M_25_ENGINE_FEATURES = new JMenu(I18n.INSTANCE.get("window.main.mods.engineFeatures"));
    private static final JMenu M_26_GAMEPLAY_FEATURES = new JMenu(I18n.INSTANCE.get("window.main.mods.gameplayFeatures"));
    private static final JMenu M_27_LICENCES = new JMenu(I18n.INSTANCE.get("window.main.mods.licences"));
    private static final JMenuItem M_211_IMPORT_FROM_FILE_SYSTEM = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.importFromFileSystem"));
    private static final JMenuItem M_212_IMPORT_FROM_URL = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.importFromURL"));
    private static final JMenuItem M_28_NPC_GAMES_LIST = new JMenuItem(I18n.INSTANCE.get("window.main.mods.npcGamesList"));
    private static final JMenuItem M_29_ADD_COMPANY_ICON = new JMenuItem(I18n.INSTANCE.get("window.main.mods.addCompanyIcon"));
    private static final JMenuItem M_223_REMOVE_GENRE = new JMenuItem(I18n.INSTANCE.get("window.main.mods.genres.removeGenre"));
    private static final JMenuItem M_311_EXPORT_GENRE = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.genre"));
    private static final JMenuItem M_313_EXPORT_THEME = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.theme"));
    private static final JMenuItem M_312_EXPORT_PUBLISHER = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.publisher"));
    private static final JMenuItem M_314_EXPORT_ENGINE_FEATURE = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.engineFeature"));
    private static final JMenuItem M_315_EXPORT_GAMEPLAY_FEATURE = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.gameplayFeature"));
    private static final JMenuItem M_232_REMOVE_THEME = new JMenuItem(I18n.INSTANCE.get("window.main.mods.themes.removeTheme"));
    private static final JMenuItem M_242_REMOVE_PUBLISHER = new JMenuItem(I18n.INSTANCE.get("window.main.mods.publisher.removePublisher"));
    private static final JMenuItem M_262_REMOVE_GAMEPLAY_FEATURE = new JMenuItem(I18n.INSTANCE.get("window.main.mods.gameplayFeatures.removeGameplayFeature"));
    private static final JMenuItem M_252_REMOVE_ENGINE_FEATURE = new JMenuItem(I18n.INSTANCE.get("window.main.mods.engineFeatures.removeEngineFeature"));
    private static final JMenuItem M_272_REMOVE_LICENCE = new JMenuItem(I18n.INSTANCE.get("window.main.mods.licences.removeLicence"));
    private static final JMenuItem M_316_EXPORT_LICENCE = new JMenuItem(I18n.INSTANCE.get("window.main.share.export.licence"));
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
    public static void createFrame(){
        //Creating the Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(680, 500);
        frame.setMinimumSize(new Dimension(250, 150));
        frame.setLocationRelativeTo(null);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenuItem m11 = new JMenuItem(I18n.INSTANCE.get("window.main.file.settings"));
        m11.addActionListener(actionEvent -> WindowSettings.createFrame());
        M_12_UPDATE_CHECK.addActionListener(actionEvent -> UpdateChecker.checkForUpdates(true));
        M_13_UNINSTALL.setToolTipText(I18n.INSTANCE.get("window.main.file.uninstall.toolTip"));
        M_13_UNINSTALL.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableUninstall, "Uninstall"));
        JMenuItem m14About = new JMenuItem(I18n.INSTANCE.get("window.main.file.about"));
        m14About.addActionListener(actionEvent -> About.showAboutPopup());
        mb.add(M_1_FILE);
        M_1_FILE.add(m11);
        M_1_FILE.add(M_12_UPDATE_CHECK);
        M_1_FILE.add(M_13_UNINSTALL);
        M_1_FILE.add(m14About);
        JMenuItem m213GetMoreMods = new JMenuItem(I18n.INSTANCE.get("window.main.mods.import.getMoreMods"));
        JMenuItem m221AddGenre  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.genres.addGenre"));
        JMenuItem m222AddRandomGenre  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre"));
        JMenuItem m231AddTheme  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.themes.addTheme"));
        JMenuItem m241AddPublisher  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.publisher.addPublisher"));
        JMenuItem m251AddEngineFeature  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.engineFeatures.addEngineFeature"));
        JMenuItem m261AddGameplayFeature  = new JMenuItem(I18n.INSTANCE.get("window.main.mods.gameplayFeatures.addGameplayFeature"));
        JMenuItem m271AddLicence = new JMenuItem(I18n.INSTANCE.get("window.main.mods.licences.addLicence"));
        JMenuItem m210ShowActiveMods = new JMenuItem(I18n.INSTANCE.get("window.main.mods.showActiveMods"));
        M_21_IMPORT.add(M_211_IMPORT_FROM_FILE_SYSTEM);
        M_21_IMPORT.add(M_212_IMPORT_FROM_URL);
        M_21_IMPORT.add(m213GetMoreMods);
        M_22_GENRES.add(m221AddGenre);
        M_22_GENRES.add(m222AddRandomGenre);
        M_22_GENRES.add(M_223_REMOVE_GENRE);
        M_23_THEMES.add(m231AddTheme);
        M_23_THEMES.add(M_232_REMOVE_THEME);
        M_24_PUBLISHER.add(m241AddPublisher);
        M_24_PUBLISHER.add(M_242_REMOVE_PUBLISHER);
        M_25_ENGINE_FEATURES.add(m251AddEngineFeature);
        M_25_ENGINE_FEATURES.add(M_252_REMOVE_ENGINE_FEATURE);
        M_26_GAMEPLAY_FEATURES.add(m261AddGameplayFeature );
        M_26_GAMEPLAY_FEATURES.add(M_262_REMOVE_GAMEPLAY_FEATURE);
        M_27_LICENCES.add(m271AddLicence);
        M_27_LICENCES.add(M_272_REMOVE_LICENCE);
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
        m221AddGenre.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewGenre, "runnableAddNewGenre"));
        m222AddRandomGenre.setToolTipText(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre.toolTip"));
        m222AddRandomGenre.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddRandomizedGenre, "runnableAddRandomizedGenre"));
        M_223_REMOVE_GENRE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRemoveGenre, "runnableRemoveGenre"));
        m231AddTheme.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewTheme, "runnableAddNewTheme"));
        M_232_REMOVE_THEME.addActionListener(actionEvent ->  ThreadHandler.startThread(ThreadHandler.runnableRemoveTheme, "runnableRemoveTheme"));
        M_28_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.mods.npcGamesList.toolTip"));
        M_28_NPC_GAMES_LIST.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableNPCGamesList, "runnableNPCGamesList"));
        m241AddPublisher.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewPublisher, "runnableAddNewPublisher"));
        M_242_REMOVE_PUBLISHER.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRemovePublisher, "runnableRemovePublisher"));
        m261AddGameplayFeature .addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewGameplayFeature, "runnableAddNewGameplayFeature"));
        M_262_REMOVE_GAMEPLAY_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRemoveGameplayFeature, "runnableRemoveGameplayFeature"));
        m251AddEngineFeature.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewEngineFeature,"runnableAddNewEngineFeature"));
        M_252_REMOVE_ENGINE_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRemoveEngineFeature, "runnableRemoveEngineFeature"));
        m271AddLicence.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddNewLicence, "runnableAddNewLicence"));
        M_272_REMOVE_LICENCE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableRemoveLicence, "runnableRemoveLicence"));
        M_29_ADD_COMPANY_ICON.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddCompanyIcon, "runnableAddCompanyIcon"));
        m210ShowActiveMods.setToolTipText(I18n.INSTANCE.get("window.main.mods.showActiveMods.toolTip"));
        m210ShowActiveMods.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableShowActiveMods, "runnableShowActiveMods"));
        mb.add(M_2_MODS);
        M_2_MODS.add(M_21_IMPORT);
        M_2_MODS.add(M_22_GENRES);
        M_2_MODS.add(M_23_THEMES);
        M_2_MODS.add(M_24_PUBLISHER);
        M_2_MODS.add(M_25_ENGINE_FEATURES);
        M_2_MODS.add(M_26_GAMEPLAY_FEATURES);
        M_2_MODS.add(M_27_LICENCES);
        M_2_MODS.add(M_28_NPC_GAMES_LIST);
        M_2_MODS.add(M_29_ADD_COMPANY_ICON);
        M_2_MODS.add(m210ShowActiveMods);
        JMenu m31Export = new JMenu(I18n.INSTANCE.get("window.main.share.export"));
        m31Export.add(M_311_EXPORT_GENRE);
        m31Export.add(M_312_EXPORT_PUBLISHER);
        m31Export.add(M_313_EXPORT_THEME);
        m31Export.add(M_314_EXPORT_ENGINE_FEATURE);
        m31Export.add(M_315_EXPORT_GAMEPLAY_FEATURE);
        m31Export.add(M_316_EXPORT_LICENCE);
        m31Export.add(M_317_EXPORT_ALL);
        M_311_EXPORT_GENRE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportGenre, "runnableExportGenre"));
        M_312_EXPORT_PUBLISHER.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportPublisher, "runnableExportPublisher"));
        M_313_EXPORT_THEME.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportThemes, "runnableExportThemes"));
        M_314_EXPORT_ENGINE_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportEngineFeatures, "runnableExportEngineFeatures"));
        M_315_EXPORT_GAMEPLAY_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportGameplayFeatures, "runnableExportGameplayFeatures"));
        M_316_EXPORT_LICENCE.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportLicence, "runnableExportLicence"));
        M_317_EXPORT_ALL.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableExportAll, "runnableExportAll"));
        JMenuItem m35 = new JMenuItem(I18n.INSTANCE.get("window.main.share.openExportFolder"));
        m35.addActionListener(actionEvent -> {
            Utils.open(Utils.getMGT2ModToolExportFolder());
        });
        JMenuItem m36 = new JMenuItem(I18n.INSTANCE.get("window.main.share.deleteAllExport"));
        m36.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableDeleteExports, "runnableDeleteExports"));
        M_3_SHARE.add(m31Export);
        M_3_SHARE.add(m35);
        M_3_SHARE.add(m36);
        mb.add(M_3_SHARE);
        JMenu m41 = new JMenu(I18n.INSTANCE.get("window.main.backup.createBackup"));
        JMenu m43RestorePoint = new JMenu(I18n.INSTANCE.get("window.main.backup.modRestorePoint"));
        JMenuItem m411CreateFullBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup"));
        m411CreateFullBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup.toolTip"));
        m411CreateFullBackup.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateFullBackup, "CreateFullBackup"));
        JMenuItem m414BackupSaveGames = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createSaveGameBackup"));
        m414BackupSaveGames.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableCreateSaveGameBackup, "CreateSaveGameBackup"));
        m41.add(m411CreateFullBackup);
        m41.add(m414BackupSaveGames);
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
        mb.add(M_4_BACKUP);
        M_4_BACKUP.add(m41);
        M_4_BACKUP.add(M_42_RESTORE_BACKUP);
        M_4_BACKUP.add(m43RestorePoint);
        M_4_BACKUP.add(m45penBackupFolder);
        setSafetyFeatureComponents();
        JMenu m51ExperimentalFeatures = new JMenu(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures"));
        m51ExperimentalFeatures.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.toolTip"));
        M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableReplacePublisherWithRealPublishers, "runnableReplacePublisherWithRealPublishers"));
        JMenuItem m52OpenGitHubPage = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGithubPage"));
        m52OpenGitHubPage.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m53OpenMGT2Folder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openMGT2Folder"));
        m53OpenMGT2Folder.addActionListener(actionEvent -> Utils.open(Settings.mgt2FilePath));
        JMenuItem m54OpenSaveGameFolder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder"));
        m54OpenSaveGameFolder.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder.toolTip"));
        m54OpenSaveGameFolder.addActionListener(actionEvent -> Utils.open(Backup.FILE_SAVE_GAME_FOLDER.getPath()));
        mb.add(M_5_UTIL);
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
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.main.button.quit.taskPerformed"), I18n.INSTANCE.get("window.main.button.quit.taskPerformed.title"), JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION){
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
        frame.getContentPane().add(BorderLayout.NORTH, mb);
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
                AnalyzeManager.genreAnalyzer.analyzeFile();
                ThemeFileAnalyzer.analyzeThemeFiles();
                AnalyzeManager.publisherAnalyzer.analyzeFile();
                AnalyzeManager.gameplayFeatureAnalyzer.analyzeFile();
                AnalyzeManager.engineFeatureAnalyzer.analyzeFile();
                AnalyzeManager.licenceAnalyzer.analyzeFile();
                boolean noCustomGenreAvailable = true;
                boolean noCustomThemesAvailable = true;
                boolean noCustomPublishersAvailable = true;
                boolean noCustomGameplayFeaturesAvailable = true;
                boolean noCustomEngineFeaturesAvailable = true;
                boolean noCustomLicencesAvailable = true;
                boolean noModRestorePointSet = true;
                if(Settings.disableSafetyFeatures){
                    noCustomGenreAvailable = false;
                    noCustomThemesAvailable = false;
                    noCustomPublishersAvailable = false;
                    noCustomGameplayFeaturesAvailable = false;
                    noCustomEngineFeaturesAvailable = false;
                    noCustomLicencesAvailable = false;
                }else{
                    if(AnalyzeManager.genreAnalyzer.getCustomContentString(true).length > 0){
                        noCustomGenreAvailable = false;
                    }
                    if(AnalyzeManager.themeFileEnAnalyzer.getFileContent().size() > 215){
                        noCustomThemesAvailable = false;
                    }
                    if(AnalyzeManager.publisherAnalyzer.getCustomContentString(true).length > 0){
                        noCustomPublishersAvailable = false;
                    }
                    if(AnalyzeManager.gameplayFeatureAnalyzer.getCustomContentString(true).length > 0){
                        noCustomGameplayFeaturesAvailable = false;
                    }
                    if(AnalyzeManager.engineFeatureAnalyzer.getCustomContentString(true).length > 0){
                        noCustomEngineFeaturesAvailable = false;
                    }
                    Map<Integer, String> mapLicences = AnalyzeManager.licenceAnalyzer.getFileContent();
                    if(mapLicences.size() > 956){
                        noCustomLicencesAvailable = false;
                    }
                    if(new File(Utils.getMGT2ModToolModRestorePointFolder()).exists()){
                        noModRestorePointSet = false;
                    }
                }
                M_223_REMOVE_GENRE.setEnabled(!noCustomGenreAvailable);
                M_232_REMOVE_THEME.setEnabled(!noCustomThemesAvailable);
                M_28_NPC_GAMES_LIST.setEnabled(!noCustomGenreAvailable);
                M_242_REMOVE_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
                M_311_EXPORT_GENRE.setEnabled(!noCustomGenreAvailable);
                M_312_EXPORT_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
                M_313_EXPORT_THEME.setEnabled(!noCustomThemesAvailable);
                M_252_REMOVE_ENGINE_FEATURE.setEnabled(!noCustomEngineFeaturesAvailable);
                M_314_EXPORT_ENGINE_FEATURE.setEnabled(!noCustomEngineFeaturesAvailable);
                M_262_REMOVE_GAMEPLAY_FEATURE.setEnabled(!noCustomGameplayFeaturesAvailable);
                M_315_EXPORT_GAMEPLAY_FEATURE.setEnabled(!noCustomGameplayFeaturesAvailable);
                M_272_REMOVE_LICENCE.setEnabled(!noCustomLicencesAvailable);
                M_316_EXPORT_LICENCE.setEnabled(!noCustomLicencesAvailable);
                M_432_RESTORE_MOD_RESTORE_POINT.setEnabled(!noModRestorePointSet);
                if(noCustomGenreAvailable){
                    M_223_REMOVE_GENRE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noGenreAvailable"));
                    M_28_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noGenreAvailable"));
                    M_311_EXPORT_GENRE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noGenreAvailable"));
                }else{
                    M_223_REMOVE_GENRE.setToolTipText("");
                    M_28_NPC_GAMES_LIST.setToolTipText("");
                    M_311_EXPORT_GENRE.setToolTipText("");
                }
                if(noCustomEngineFeaturesAvailable && noCustomGameplayFeaturesAvailable && noCustomGenreAvailable && noCustomPublishersAvailable && noCustomThemesAvailable &&noCustomLicencesAvailable){
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
                if(noCustomThemesAvailable){
                    M_232_REMOVE_THEME.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noThemeAvailable"));
                    M_313_EXPORT_THEME.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noThemeAvailable"));
                }else{
                    M_232_REMOVE_THEME.setToolTipText("");
                }
                if(noCustomPublishersAvailable){
                    M_242_REMOVE_PUBLISHER.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noPublisherAvailable"));
                    M_312_EXPORT_PUBLISHER.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noPublisherAvailable"));
                }else{
                    M_242_REMOVE_PUBLISHER.setToolTipText("");
                    M_312_EXPORT_PUBLISHER.setToolTipText("");
                }
                if(noCustomEngineFeaturesAvailable){
                    M_252_REMOVE_ENGINE_FEATURE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noEngineFeatureAvailable"));
                    M_314_EXPORT_ENGINE_FEATURE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noEngineFeatureAvailable"));
                }else{
                    M_252_REMOVE_ENGINE_FEATURE.setToolTipText("");
                    M_314_EXPORT_ENGINE_FEATURE.setToolTipText("");
                }
                if(noCustomGameplayFeaturesAvailable){
                    M_262_REMOVE_GAMEPLAY_FEATURE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noGameplayFeatureAvailable"));
                    M_315_EXPORT_GAMEPLAY_FEATURE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noGameplayFeatureAvailable"));
                }else{
                    M_262_REMOVE_GAMEPLAY_FEATURE.setToolTipText("");
                    M_315_EXPORT_GAMEPLAY_FEATURE.setToolTipText("");
                }
                if(noCustomLicencesAvailable){
                    M_272_REMOVE_LICENCE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noLicenceAvailable"));
                    M_316_EXPORT_LICENCE.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.noLicenceAvailable"));
                }else{
                    M_272_REMOVE_LICENCE.setToolTipText("");
                    M_316_EXPORT_LICENCE.setToolTipText("");
                }
                if(noModRestorePointSet){
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.notAvailableToolTip"));
                }else{
                    M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText("");
                }
                if(Settings.enableDisclaimerMessage){
                    M_21_IMPORT.setEnabled(false);
                    M_22_GENRES.setEnabled(false);
                    M_23_THEMES.setEnabled(false);
                    M_24_PUBLISHER.setEnabled(false);
                    M_25_ENGINE_FEATURES.setEnabled(false);
                    M_26_GAMEPLAY_FEATURES.setEnabled(false);
                    M_27_LICENCES.setEnabled(false);
                    M_28_NPC_GAMES_LIST.setEnabled(false);
                    M_29_ADD_COMPANY_ICON.setEnabled(false);
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setEnabled(false);
                    M_21_IMPORT.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_22_GENRES.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_23_THEMES.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_24_PUBLISHER.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_25_ENGINE_FEATURES.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_26_GAMEPLAY_FEATURES.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_27_LICENCES.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_28_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_29_ADD_COMPANY_ICON.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setToolTipText(I18n.INSTANCE.get("window.main.actionAvailability.acceptMessageFirst"));
                }else{
                    M_21_IMPORT.setEnabled(true);
                    M_22_GENRES.setEnabled(true);
                    M_23_THEMES.setEnabled(true);
                    M_24_PUBLISHER.setEnabled(true);
                    M_25_ENGINE_FEATURES.setEnabled(true);
                    M_26_GAMEPLAY_FEATURES.setEnabled(true);
                    M_27_LICENCES.setEnabled(true);
                    M_29_ADD_COMPANY_ICON.setEnabled(true);
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setEnabled(true);
                    M_21_IMPORT.setToolTipText("");
                    M_22_GENRES.setToolTipText("");
                    M_23_THEMES.setToolTipText("");
                    M_24_PUBLISHER.setToolTipText("");
                    M_25_ENGINE_FEATURES.setToolTipText("");
                    M_26_GAMEPLAY_FEATURES.setToolTipText("");
                    M_27_LICENCES.setToolTipText("");
                    M_29_ADD_COMPANY_ICON.setToolTipText("");
                    M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.replacePublisher.toolTip"));
                }
            }catch (IOException e){
                LOGGER.info("Error" + e.getMessage());
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
