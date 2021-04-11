package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WindowMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowMain.class);
    private static final JFrame frame = new JFrame(I18n.INSTANCE.get("window.main.tile"));
    private static final JMenu M_1_FILE = new JMenu(I18n.INSTANCE.get("window.main.file"));
    private static final JMenu M_2_MODSS = new JMenu(I18n.INSTANCE.get("window.main.mods"));
    private static final JMenu M_3_SHARE = new JMenu(I18n.INSTANCE.get("window.main.share"));
    private static final JMenu M_4_BACKUP = new JMenu(I18n.INSTANCE.get("window.main.backup"));
    private static final JMenu M_5_UTIL = new JMenu(I18n.INSTANCE.get("window.main.utilities"));
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
    private static final JMenuItem M_431_CREATE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint"));
    private static final JMenuItem M_432_RESTORE_MOD_RESTORE_POINT = new JMenuItem(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint"));
    private static final JMenuItem M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.replacePublisher"));
    public static final JProgressBar PROGRESS_BAR = new JProgressBar();
    public static final JTextArea TEXT_AREA = new JTextArea();
    public static final JScrollPane SCROLL_PANE = new JScrollPane(TEXT_AREA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public static void createFrame(){
        //Creating the Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setMinimumSize(new Dimension(270, 250));
        frame.setLocationRelativeTo(null);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenuItem m11 = new JMenuItem(I18n.INSTANCE.get("window.main.file.settings"));
        m11.addActionListener(actionEvent -> WindowSettings.createFrame());
        JMenuItem m12 = new JMenuItem(I18n.INSTANCE.get("window.main.file.updateCheck"));
        m12.addActionListener(actionEvent -> UpdateChecker.checkForUpdates(true));
        JMenuItem m13 = new JMenuItem(I18n.INSTANCE.get("window.main.file.uninstall"));
        m13.setToolTipText(I18n.INSTANCE.get("window.main.file.uninstall.toolTip"));
        m13.addActionListener(actionEvent -> ThreadHandler.startThread("runnableUninstall"));
        JMenuItem m14About = new JMenuItem(I18n.INSTANCE.get("window.main.file.about"));
        m14About.addActionListener(actionEvent -> About.showAboutPopup());
        mb.add(M_1_FILE);
        M_1_FILE.add(m11);
        M_1_FILE.add(m12);
        M_1_FILE.add(m13);
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
        M_2_MODSS.addActionListener(actionEvent -> Disclaimer.showDisclaimer());
        M_2_MODSS.addMenuListener(new MenuListener() {
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
        M_211_IMPORT_FROM_FILE_SYSTEM.addActionListener(actionEvent -> ThreadHandler.startThread("runnableImportAll"));
        M_212_IMPORT_FROM_URL.setToolTipText(I18n.INSTANCE.get("window.main.mods.import.importFromURL.toolTip"));
        M_212_IMPORT_FROM_URL.addActionListener(actionEvent -> ThreadHandler.startThread("runnableImportFromURL"));
        m221AddGenre.addActionListener(actionEvent -> addGenre());
        m222AddRandomGenre.setToolTipText(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre.toolTip"));
        m222AddRandomGenre.addActionListener(actionEvent -> GenreHelper.addRandomizedGenre());
        M_223_REMOVE_GENRE.addActionListener(actionEvent -> OperationHelper.process(EditGenreFile::removeGenre, AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), "genre", "removed", "Remove", false));
        m231AddTheme.addActionListener(actionEvent -> addTheme());
        M_232_REMOVE_THEME.addActionListener(actionEvent ->  OperationHelper.process(EditThemeFiles::removeTheme, AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), "theme", "removed", "Remove", false));
        M_28_NPC_GAMES_LIST.setToolTipText(I18n.INSTANCE.get("window.main.mods.npcGamesList.toolTip"));
        M_28_NPC_GAMES_LIST.addActionListener(actionEvent -> npcGameList());
        m241AddPublisher.addActionListener(actionEvent -> addPublisher());
        M_242_REMOVE_PUBLISHER.addActionListener(actionEvent -> OperationHelper.process(EditPublishersFile::removePublisher, AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), "publisher", "removed", "Remove", false));
        m261AddGameplayFeature .addActionListener(actionEvent -> GameplayFeatureHelper.addGameplayFeature());
        M_262_REMOVE_GAMEPLAY_FEATURE.addActionListener(actionEvent -> OperationHelper.process(EditGameplayFeaturesFile::removeGameplayFeature, AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), "gameplay feature", "removed", "Remove", false));
        m251AddEngineFeature.addActionListener(actionEvent -> EngineFeatureHelper.addEngineFeature());
        M_252_REMOVE_ENGINE_FEATURE.addActionListener(actionEvent -> OperationHelper.process(EditEngineFeaturesFile::removeEngineFeature, AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), "engine feature", "removed", "Remove", false));
        m271AddLicence.addActionListener(actionEvent -> LicenceHelper.addLicence());
        M_272_REMOVE_LICENCE.addActionListener(actionEvent -> OperationHelper.process(EditLicenceFile::removeLicence, AnalyzeExistingLicences.getCustomLicenceNamesByAlphabet(), AnalyzeExistingLicences.getLicenceNamesByAlphabet(), "licence", "removed", "Remove", false));
        M_29_ADD_COMPANY_ICON.addActionListener(actionEvent -> addCompanyIcon());
        m210ShowActiveMods.setToolTipText(I18n.INSTANCE.get("window.main.mods.showActiveMods.toolTip"));
        m210ShowActiveMods.addActionListener(actionEvent -> ThreadHandler.startThread("runnableShowActiveMods"));
        mb.add(M_2_MODSS);
        M_2_MODSS.add(M_21_IMPORT);
        M_2_MODSS.add(M_22_GENRES);
        M_2_MODSS.add(M_23_THEMES);
        M_2_MODSS.add(M_24_PUBLISHER);
        M_2_MODSS.add(M_25_ENGINE_FEATURES);
        M_2_MODSS.add(M_26_GAMEPLAY_FEATURES);
        M_2_MODSS.add(M_27_LICENCES);
        M_2_MODSS.add(M_28_NPC_GAMES_LIST);
        M_2_MODSS.add(M_29_ADD_COMPANY_ICON);
        M_2_MODSS.add(m210ShowActiveMods);
        JMenu m31Export = new JMenu(I18n.INSTANCE.get("window.main.share.export"));
        m31Export.add(M_311_EXPORT_GENRE);
        m31Export.add(M_312_EXPORT_PUBLISHER);
        m31Export.add(M_313_EXPORT_THEME);
        m31Export.add(M_314_EXPORT_ENGINE_FEATURE);
        m31Export.add(M_315_EXPORT_GAMEPLAY_FEATURE);
        m31Export.add(M_316_EXPORT_LICENCE);
        m31Export.add(M_317_EXPORT_ALL);
        M_311_EXPORT_GENRE.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportGenre"));
        M_312_EXPORT_PUBLISHER.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportPublisher"));
        M_313_EXPORT_THEME.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportThemes"));
        M_314_EXPORT_ENGINE_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportEngineFeatures"));
        M_315_EXPORT_GAMEPLAY_FEATURE.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportGameplayFeatures"));
        M_316_EXPORT_LICENCE.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportLicence"));
        M_317_EXPORT_ALL.addActionListener(actionEvent -> ThreadHandler.startThread("runnableExportAll"));
        JMenuItem m35 = new JMenuItem(I18n.INSTANCE.get("window.main.share.openExportFolder"));
        m35.addActionListener(actionEvent -> {
            Utils.open(Utils.getMGT2ModToolExportFolder());
        });
        JMenuItem m36 = new JMenuItem(I18n.INSTANCE.get("window.main.share.deleteAllExport"));
        m36.addActionListener(actionEvent -> ThreadHandler.startThread("runnableDeleteExports"));
        M_3_SHARE.add(m31Export);
        M_3_SHARE.add(m35);
        M_3_SHARE.add(m36);
        mb.add(M_3_SHARE);
        JMenu m41 = new JMenu(I18n.INSTANCE.get("window.main.backup.createBackup"));
        JMenu m42 = new JMenu(I18n.INSTANCE.get("window.main.backup.restoreBackup"));
        JMenu m43RestorePoint = new JMenu(I18n.INSTANCE.get("window.main.backup.modRestorePoint"));
        JMenuItem m411CreateFullBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup"));
        m411CreateFullBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.createBackup.createFullBackup.toolTip"));
        m411CreateFullBackup.addActionListener(actionEvent -> Backup.createBackup("full"));
        JMenuItem m412BackupGenresFile = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createGenreBackup"));
        m412BackupGenresFile.addActionListener(actionEvent -> Backup.createBackup("genre"));
        JMenuItem m413BackupThemesFiles = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createThemeFilesBackup"));
        m413BackupThemesFiles.addActionListener(actionEvent -> Backup.createBackup("theme"));
        JMenuItem m414BackupSaveGames = new JMenuItem(I18n.INSTANCE.get("window.main.backup.createBackup.createSaveGameBackup"));
        m414BackupSaveGames.addActionListener(actionEvent -> Backup.createBackup("save_game"));
        m41.add(m411CreateFullBackup);
        m41.add(m412BackupGenresFile);
        m41.add(m413BackupThemesFiles);
        m41.add(m414BackupSaveGames);
        JMenuItem m421RestoreInitialBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup"));
        m421RestoreInitialBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreInitialBackup.toolTip"));
        m421RestoreInitialBackup.addActionListener(actionEvent -> restoreInitialBackup());
        JMenuItem m422RestoreLatestBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup"));
        m422RestoreLatestBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreLatestBackup.toolTip"));
        m422RestoreLatestBackup.addActionListener(actionEvent -> restoreLatestBackup());
        JMenuItem m423RestoreSaveGameBackup = new JMenuItem(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup"));
        m423RestoreSaveGameBackup.setToolTipText(I18n.INSTANCE.get("window.main.backup.restoreBackup.restoreSaveGameBackup.toolTip"));
        m423RestoreSaveGameBackup.addActionListener(actionEvent -> Backup.restoreSaveGameBackup());
        m42.add(m421RestoreInitialBackup);
        m42.add(m422RestoreLatestBackup);
        m42.add(m423RestoreSaveGameBackup);
        M_431_CREATE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.createModRestorePoint.toolTip"));
        M_431_CREATE_MOD_RESTORE_POINT.addActionListener(actionEvent -> RestorePointHelper.setRestorePoint());
        M_432_RESTORE_MOD_RESTORE_POINT.setToolTipText(I18n.INSTANCE.get("window.main.backup.modRestorePoint.restoreModRestorePoint.toolTip"));
        M_432_RESTORE_MOD_RESTORE_POINT.addActionListener(actionEvent -> RestorePointHelper.restoreToRestorePoint());
        m43RestorePoint.add(M_431_CREATE_MOD_RESTORE_POINT);
        m43RestorePoint.add(M_432_RESTORE_MOD_RESTORE_POINT);
        JMenuItem m44DeleteAllBackups = new JMenuItem(I18n.INSTANCE.get("window.main.backup.deleteAllBackups"));
        m44DeleteAllBackups.setToolTipText(I18n.INSTANCE.get("window.main.backup.deleteAllBackups.toolTip"));
        m44DeleteAllBackups.addActionListener(actionEvent -> Backup.deleteAllBackups());
        JMenuItem m45penBackupFolder = new JMenuItem(I18n.INSTANCE.get("window.main.backup.openBackupFolder"));
        m45penBackupFolder.setToolTipText(I18n.INSTANCE.get("window.main.backup.openBackupFolder.toolTip"));
        m45penBackupFolder.addActionListener(actionEvent -> Utils.open(Settings.MGT2_MOD_MANAGER_PATH + "//Backup//"));
        mb.add(M_4_BACKUP);
        M_4_BACKUP.add(m41);
        M_4_BACKUP.add(m42);
        M_4_BACKUP.add(m43RestorePoint);
        if(Settings.disableSafetyFeatures){
            M_4_BACKUP.add(m44DeleteAllBackups);
        }
        M_4_BACKUP.add(m45penBackupFolder);
        JMenu m51ExperimentalFeatures = new JMenu(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures"));
        m51ExperimentalFeatures.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.toolTip"));
        M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.addActionListener(actionEvent -> ThreadHandler.startThread("runnableReplacePublisherWithRealPublishers"));
        JMenuItem m52OpenGitHubPage = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGithubPage"));
        m52OpenGitHubPage.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m53OpenMGT2Folder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openMGT2Folder"));
        m53OpenMGT2Folder.addActionListener(actionEvent -> Utils.open(Settings.mgt2FilePath));
        JMenuItem m54ShowActiveGenres = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.showActiveGenres"));
        m54ShowActiveGenres.addActionListener(actionEvent -> showActiveGenres());
        JMenuItem m55ShowActiveThemes = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.showActiveThemes"));
        m55ShowActiveThemes.addActionListener(actionEvent -> showActiveThemes());
        JMenuItem m56OpenGenreFile = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGenresTXTFile"));
        m56OpenGenreFile.addActionListener(actionEvent -> Utils.open(Utils.getGenreFile().getPath()));
        JMenuItem m57OpenGenresById = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openGenresById"));
        m57OpenGenresById.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openGenresById.toolTip"));
        m57OpenGenresById.addActionListener(actionEvent -> Utils.open(AnalyzeExistingGenres.FILE_GENRES_BY_ID_HELP.getPath()));
        JMenuItem m58OpenLogFile = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openLogFile"));
        m58OpenLogFile.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openLogFile.toolTip"));
        m58OpenLogFile.addActionListener(actionEvent -> Utils.open(ChangeLog.FILE_CHANGES_LOG.getPath()));
        JMenuItem m59OpenSaveGameFolder = new JMenuItem(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder"));
        m59OpenSaveGameFolder.setToolTipText(I18n.INSTANCE.get("window.main.utilities.openSaveGameFolder.toolTip"));
        m59OpenSaveGameFolder.addActionListener(actionEvent -> Utils.open(Backup.FILE_SAVE_GAME_FOLDER.getPath()));
        mb.add(M_5_UTIL);
        M_5_UTIL.add(m51ExperimentalFeatures);
        m51ExperimentalFeatures.add(M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS);
        M_5_UTIL.add(m52OpenGitHubPage);
        M_5_UTIL.add(m53OpenMGT2Folder);
        M_5_UTIL.add(m54ShowActiveGenres);
        M_5_UTIL.add(m55ShowActiveThemes);
        M_5_UTIL.add(m56OpenGenreFile);
        M_5_UTIL.add(m57OpenGenresById);
        M_5_UTIL.add(m58OpenLogFile);
        M_5_UTIL.add(m59OpenSaveGameFolder);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel labelVersion = new JLabel("v" + MadGamesTycoon2ModTool.VERSION);
        JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.quit"));
        buttonQuit.addActionListener(actionEvent -> disposeFrame());
        JLabel labelModCreator = new JLabel("by LMH01");
        // Components Added using Flow Layout
        panel.add(labelVersion);
        panel.add(buttonQuit);
        panel.add(labelModCreator);

        //Progress Bar
        JPanel panelMiddle = new JPanel();
        panelMiddle.setLayout(new BoxLayout(panelMiddle, BoxLayout.Y_AXIS));
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
        frame.dispose();
        System.exit(0);
    }
    /**
     * Checks if specific actions are available. If they are the buttons will be enabled
     */
    public static void checkActionAvailability(){
        try{
            AnalyzeExistingGenres.analyzeGenreFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            AnalyzeExistingPublishers.analyzePublisherFile();
            AnalyzeExistingGameplayFeatures.analyzeGameplayFeatures();
            AnalyzeExistingEngineFeatures.analyzeEngineFeatures();
            AnalyzeExistingLicences.analyze();
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
                if(AnalyzeExistingGenres.genreList.size() > AnalyzeExistingGenres.DEFAULT_GENRES.length){
                    noCustomGenreAvailable = false;
                }
                if(AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE.size() > AnalyzeExistingThemes.DEFAULT_THEMES.length){
                    noCustomThemesAvailable = false;
                }
                List<Map<String, String>> list = AnalyzeExistingPublishers.getListMap();
                if(list.size() > 71){
                    noCustomPublishersAvailable = false;
                }
                List<Map<String, String>> currentGameplayFeatures = AnalyzeExistingGameplayFeatures.gameplayFeatures;
                if(currentGameplayFeatures.size() > 61){
                    noCustomGameplayFeaturesAvailable = false;
                }
                List<Map<String, String>> currentEngineFeatures = AnalyzeExistingEngineFeatures.engineFeatures;
                if(currentEngineFeatures.size() > 58){
                    noCustomEngineFeaturesAvailable = false;
                }
                Map<Integer, String> mapLicences = AnalyzeExistingLicences.existingLicences;
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
                M_28_NPC_GAMES_LIST.setEnabled(true);
                M_29_ADD_COMPANY_ICON.setEnabled(true);
                M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setEnabled(true);
                M_21_IMPORT.setToolTipText("");
                M_22_GENRES.setToolTipText("");
                M_23_THEMES.setToolTipText("");
                M_24_PUBLISHER.setToolTipText("");
                M_25_ENGINE_FEATURES.setToolTipText("");
                M_26_GAMEPLAY_FEATURES.setToolTipText("");
                M_27_LICENCES.setToolTipText("");
                M_28_NPC_GAMES_LIST.setToolTipText("");
                M_29_ADD_COMPANY_ICON.setToolTipText("");
                M_511_REPLACE_PUBLISHERS_WITH_REAL_PUBLISHERS.setToolTipText(I18n.INSTANCE.get("window.main.utilities.experimentalFeatures.replacePublisher.toolTip"));
            }
        }catch (IOException e){
            LOGGER.info("Error" + e.getMessage());
            e.printStackTrace();
        }
        TextAreaHelper.resetAutoScroll();
        ProgressBarHelper.resetProgressBar();
        lockMenuItems(false);

    }

    /**
     * Disables all menu items so they can no longer be klicked. Used when a thread is started.
     * @param lock True when the items should be locked. False when the items should be unlocked
     */
    public static void lockMenuItems(boolean lock){
        M_1_FILE.setEnabled(!lock);
        M_2_MODSS.setEnabled(!lock);
        M_3_SHARE.setEnabled(!lock);
        M_4_BACKUP.setEnabled(!lock);
        M_5_UTIL.setEnabled(!lock);
        if(lock){
            M_1_FILE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_2_MODSS.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_3_SHARE.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_4_BACKUP.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
            M_5_UTIL.setToolTipText(I18n.INSTANCE.get("window.main.lockMenuItems"));
        }else{
            M_1_FILE.setToolTipText("");
            M_2_MODSS.setToolTipText("");
            M_3_SHARE.setToolTipText("");
            M_4_BACKUP.setToolTipText("");
            M_5_UTIL.setToolTipText("");
        }
    }

    private static void addGenre(){
        try {
            //AnalyzeExistingGenres.analyzeGenreFileDeprecated();
            AnalyzeExistingGenres.analyzeGenreFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            GenreManager.startStepByStepGuide();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The step by step guide could not be started because the Genres.txt file could not be analyzed.\nPlease check if your mgt2 folder is set correctly.\n\nException: " + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
            checkActionAvailability();
            e.printStackTrace();
        }
    }
    private static void addTheme(){
        try {
            AnalyzeExistingThemes.analyzeThemeFiles();
            final ArrayList<String>[] arrayListThemeTranslations = new ArrayList[]{new ArrayList<>()};
            ArrayList<Integer> arrayListCompatibleGenreIds = new ArrayList<>();
            String[] string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
            JLabel labelEnterThemeName = new JLabel("Enter the theme name:");
            JTextField textFieldThemeName = new JTextField();
            JButton buttonAddTranslations = new JButton("Add translations");
            buttonAddTranslations.setToolTipText("Click to add translations for your theme.");
            buttonAddTranslations.addActionListener(actionEvent2 -> {
                if(!arrayListThemeTranslations[0].isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, "Theme translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        arrayListThemeTranslations[0].clear();
                        arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                    }
                }else{
                    arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                }
            });
            JPanel panelChooseViolenceLevel = new JPanel();
            JLabel labelViolenceLevel = new JLabel("Choose the violence level:");
            JComboBox comboBoxViolenceLevel = new JComboBox();
            comboBoxViolenceLevel.setToolTipText("<html>This declares how much the age rating should be influenced when a game is made with this topic<br>0 - The theme will not influence your age rating<br>1-3 - The higher the number the more the age rating of your game with this topic will be influenced");
            comboBoxViolenceLevel.setModel(new DefaultComboBoxModel<>(new String[]{"0", "1", "2", "3"}));
            panelChooseViolenceLevel.add(labelViolenceLevel);
            panelChooseViolenceLevel.add(comboBoxViolenceLevel);
            JLabel labelExplainList = new JLabel("<html>Chose what genres should work good together<br>with your theme.<br>(Tip: Hold STRG and click with your mouse)");
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddTranslations, panelChooseViolenceLevel, labelExplainList, scrollPaneAvailableGenres};
            ArrayList<String> arrayListCompatibleGenreNames = new ArrayList<>();
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Add new theme", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        if(!textFieldThemeName.getText().isEmpty()){
                            if(!AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.containsValue(textFieldThemeName.getText()) && !AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE.containsValue(textFieldThemeName.getText())){
                                if(!textFieldThemeName.getText().matches(".*\\d.*")){
                                    arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                    for(Map<String, String> map : AnalyzeExistingGenres.genreList){
                                        for(String name : arrayListCompatibleGenreNames){
                                            if(map.get("NAME EN").equals(name)){
                                                arrayListCompatibleGenreIds.add(Integer.parseInt(map.get("ID")));
                                            }
                                        }
                                    }
                                    Map<String, String> themeTranslations = new HashMap<>();
                                    int currentTranslationKey = 0;
                                    if(arrayListThemeTranslations[0].isEmpty()){
                                        for(String translationKey : TranslationManager.TRANSLATION_KEYS){
                                            themeTranslations.put("NAME " + translationKey, textFieldThemeName.getText());
                                        }
                                    }else{
                                        for(String translation : arrayListThemeTranslations[0]){
                                            themeTranslations.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentTranslationKey], translation);
                                            currentTranslationKey++;
                                        }
                                    }
                                    themeTranslations.put("NAME EN", textFieldThemeName.getText());
                                    if(JOptionPane.showConfirmDialog(null, "Do you wan't to add this theme?:\n" + textFieldThemeName.getText(), "Add this theme?", JOptionPane.YES_NO_OPTION) == 0){
                                        Backup.createThemeFilesBackup(false);
                                        EditThemeFiles.addTheme(themeTranslations, arrayListCompatibleGenreIds, Integer.parseInt(comboBoxViolenceLevel.getSelectedItem().toString()));
                                        JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                        breakLoop = true;
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "Unable to add theme:\nThe theme name can not contain numbers!", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Unable to add theme:\nThe selected name is already in use.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                                arrayListCompatibleGenreNames.clear();
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Unable to add theme:\nPlease enter a name for your theme.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to add theme:\nPlease select at least one genre.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to add theme:\n\n" + e.getMessage(), "Error while adding theme", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        WindowMain.checkActionAvailability();
    }
    private static void npcGameList(){
        try {
            AnalyzeExistingGenres.analyzeGenreFile();
            if(AnalyzeExistingGenres.genreList.size()-1 > 17 || Settings.disableSafetyFeatures){
                WindowNpcGameList.createFrame();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "There is no new genre that has been added.\nAdd a new genre first fia 'Add new genre'.", "Unable to continue:", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            Utils.showErrorMessage(1, e);
            e.printStackTrace();
        }
    }
    private static void addPublisher(){
        try {
            AnalyzeExistingPublishers.analyzePublisherFile();
            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField("---------Enter Name---------");
            panelName.add(labelName);
            panelName.add(textFieldName);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel("Unlock Month:");
            JComboBox comboBoxUnlockMonth = new JComboBox(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel("Unlock Year:");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - MAX INTEGER VALUE]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockYear.add(labelUnlockYear);
            panelUnlockYear.add(spinnerUnlockYear);

            AtomicReference<String> publisherImageFilePath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
            JPanel panelPublisherIcon = new JPanel();
            JLabel labelPublisherIcon = new JLabel("Publisher Icon:");
            JButton buttonBrowseIcon = new JButton("Browse");
            buttonBrowseIcon.addActionListener(actionEvent -> {
                String imageFilePath = Utils.getImagePath();
                if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                    publisherImageFilePath.set(imageFilePath);
                }else{
                    publisherImageFilePath.set(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
                }
            });
            panelPublisherIcon.add(labelPublisherIcon);
            panelPublisherIcon.add(buttonBrowseIcon);

            JCheckBox checkBoxIsDeveloper = new JCheckBox("Developer");
            checkBoxIsDeveloper.setSelected(true);
            checkBoxIsDeveloper.setToolTipText("When enabled: The company can release their own games");

            JCheckBox checkBoxIsPublisher = new JCheckBox("Publisher");
            checkBoxIsPublisher.setSelected(true);
            checkBoxIsPublisher.setToolTipText("When enabled: The company can release games for you (publish them)");

            JPanel panelMarketShare = new JPanel();
            JLabel labelMarketShare = new JLabel("Market Share:");
            JSpinner spinnerMarketShare = new JSpinner();
            spinnerMarketShare.setToolTipText("<html>[Range: 1 - 100]<br>This is how much market share your publisher has");
            if(Settings.disableSafetyFeatures){
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, 100, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(false);
            }
            panelMarketShare.add(labelMarketShare);
            panelMarketShare.add(spinnerMarketShare);

            JPanel panelShare = new JPanel();
            JLabel labelShare = new JLabel("Share:");
            JSpinner spinnerShare = new JSpinner();
            spinnerShare.setToolTipText("<html>[Range: 1 - 10]<br>Set how much money should be earned by one sell");
            if(Settings.disableSafetyFeatures){
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, 10, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(false);
            }
            panelShare.add(labelShare);
            panelShare.add(spinnerShare);

            AtomicInteger genreID = new AtomicInteger();
            JPanel panelGenre = new JPanel();
            JLabel labelGenre = new JLabel("Fan base:");
            JButton buttonSelectGenre = new JButton("        Select genre        ");
            buttonSelectGenre.setToolTipText("Click to select what genre the fan base of your publisher likes the most");
            buttonSelectGenre.addActionListener(actionEvent -> {
                try {
                    AnalyzeExistingGenres.analyzeGenreFile();
                    JLabel labelChooseGenre = new JLabel("Select what main genre your publisher should have:");
                    String[] string;
                    string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
                    JList<String> listAvailableGenres = new JList<>(string);
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                    Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                    if(JOptionPane.showConfirmDialog(null, params, "Select genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        if(!listAvailableGenres.isSelectionEmpty()){
                            String currentGenre = listAvailableGenres.getSelectedValue();
                            genreID.set(AnalyzeExistingGenres.getGenreIdByName(currentGenre));
                            buttonSelectGenre.setText(currentGenre);
                        }else{
                            JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while selecting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
            panelGenre.add(labelGenre);
            panelGenre.add(buttonSelectGenre);

            Object[] params = {panelName, panelUnlockMonth, panelUnlockYear, panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, panelMarketShare, panelShare, panelGenre};
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Add Publisher", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    boolean publisherAlreadyExists = false;
                    for(String string : AnalyzeExistingPublishers.getPublisherString()){
                        if(textFieldName.getText().equals(string)){
                            publisherAlreadyExists = true;
                        }
                    }
                    if(publisherAlreadyExists){
                        JOptionPane.showMessageDialog(null, "Unable to add publisher: The publisher name does already exist!", I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(textFieldName.getText().equals("") || textFieldName.getText().equals("---------Enter Name---------")){
                            JOptionPane.showMessageDialog(null, "Please enter a name first!", "", JOptionPane.INFORMATION_MESSAGE);
                            textFieldName.setText("---------Enter Name---------");
                        }else if(buttonSelectGenre.getText().equals("        Select genre        ")){
                            JOptionPane.showMessageDialog(null, "Please select a genre first!", "", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
                            int logoId;
                            if(publisherImageFilePath.toString().equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png")){
                                logoId = 87;
                            }else{
                                logoId = AnalyzeCompanyLogos.getLogoNumber();
                            }
                            if(JOptionPane.showConfirmDialog(null, "Add this publisher?\n" +
                                    "\nName: " + textFieldName.getText() +
                                    "\nDate: " + Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString() +
                                    "\nPic: See top left" +
                                    "\nDeveloper: " + checkBoxIsDeveloper.isSelected() +
                                    "\nPublisher: " + checkBoxIsPublisher.isSelected() +
                                    "\nMarketShare: " + spinnerMarketShare.getValue().toString() +
                                    "\nShare: " + spinnerShare.getValue().toString() +
                                    "\nGenre: " + buttonSelectGenre.getText(), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION){
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("ID", Integer.toString(AnalyzeExistingPublishers.maxPublisherID +1));
                                hashMap.put("NAME EN", textFieldName.getText());
                                hashMap.put("NAME GE", textFieldName.getText());
                                hashMap.put("NAME TU", textFieldName.getText());
                                hashMap.put("NAME FR", textFieldName.getText());
                                hashMap.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
                                hashMap.put("PIC", Integer.toString(logoId));
                                hashMap.put("DEVELOPER", Boolean.toString(checkBoxIsDeveloper.isSelected()));
                                hashMap.put("PUBLISHER", Boolean.toString(checkBoxIsPublisher.isSelected()));
                                hashMap.put("MARKET", spinnerMarketShare.getValue().toString());
                                hashMap.put("SHARE", spinnerShare.getValue().toString());
                                hashMap.put("GENRE", genreID.toString());
                                EditPublishersFile.addPublisher(hashMap, publisherImageFilePath.toString());
                                JOptionPane.showMessageDialog(null, "Publisher " + hashMap.get("NAME EN") + " has been added successfully", "Publisher added", JOptionPane.INFORMATION_MESSAGE);
                                ChangeLog.addLogEntry(19, hashMap.get("NAME EN"));
                                breakLoop = true;
                            }
                        }
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while adding publisher:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        WindowMain.checkActionAvailability();
    }
    private static void addCompanyIcon(){
        String imageFilePath = Utils.getImagePath();
        File imageFileSource = new File(imageFilePath);
        if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
            File targetImage = new File(Utils.getMGT2CompanyLogosPath() + "//" + AnalyzeCompanyLogos.getLogoNumber() + ".png");
            try {
                Files.copy(Paths.get(imageFileSource.getPath()), Paths.get(targetImage.getPath()));
                JOptionPane.showMessageDialog(null, "Image has been added.", "Image added", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to add image file:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Unable to add image file: Unknown error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void restoreInitialBackup(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.initialBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                for (String customGenre : customGenres) {
                    try {
                        EditGenreFile.removeGenre(customGenre);
                        LOGGER.info("Game files have been restored to original.");
                    } catch (IOException e) {
                        LOGGER.info("Genre could not be removed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                String[] customPublishers = AnalyzeExistingPublishers.getCustomPublisherString();
                for (String customPublisher : customPublishers) {
                    try {
                        EditPublishersFile.removePublisher(customPublisher);
                        LOGGER.info("Publisher files have been restored to original.");
                    } catch (IOException e) {
                        LOGGER.info("Publisher could not be removed: " + e.getMessage());
                        e.printStackTrace();
                    }
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
    private static void restoreLatestBackup(){
        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.backup.restoreBackup.latestBackup.message"), I18n.INSTANCE.get("dialog.backup.restoreBackup.title"), JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                Backup.restoreBackup(false, true);
                ChangeLog.addLogEntry(9);
            } catch (IOException e) {
                e.printStackTrace();
                if(Utils.showConfirmDialog(1, e)){
                    Backup.restoreBackup(false, true);
                    ChangeLog.addLogEntry(9);
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
    private static void showActiveGenres(){
        String[] string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();

        JList<String> listAvailableGenres = new JList<>(string);
        listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
        listAvailableGenres.setVisibleRowCount(-1);

        JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
        scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

        JOptionPane.showMessageDialog(null, scrollPaneAvailableGenres, "The following genres are currently active.", JOptionPane.INFORMATION_MESSAGE);
    }
    private static void showActiveThemes(){
        try {
            AnalyzeExistingThemes.analyzeThemeFiles();
            String[] string = AnalyzeExistingThemes.getThemesByAlphabet();

            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);

            JScrollPane scrollPaneAvailableThemes = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableThemes.setPreferredSize(new Dimension(315,140));

            JOptionPane.showMessageDialog(null, scrollPaneAvailableThemes, "The following themes are currently active.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            Utils.showErrorMessage(1, e);
            e.printStackTrace();
        }
    }
}
