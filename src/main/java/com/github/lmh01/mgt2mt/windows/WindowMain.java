package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
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
    private static final JFrame frame = new JFrame("MGT2 Mod Tool");
    private static final JMenuItem M221ADD_GENRE = new JMenuItem("Add Genre");
    private static final JMenuItem M222REMOVE_GENRE = new JMenuItem("Remove Genre");
    private static final JMenuItem M311EXPORT_GENRE = new JMenuItem("Genre");
    private static final JMenuItem M313EXPORT_THEME = new JMenuItem("Theme");
    private static final JMenuItem M312EXPORT_PUBLISHER = new JMenuItem("Publisher");
    private static final JMenuItem M314EXPORT_ENGINE_FEATURE = new JMenuItem("Engine Feature");
    private static final JMenuItem M315EXPORT_GAMEPLAY_FEATURE = new JMenuItem("Gameplay Feature");
    private static final JMenuItem M232REMOVE_THEME = new JMenuItem("Remove Theme");
    private static final JMenuItem M27NPC_GAMES_LIST = new JMenuItem("NPC_Games_list");
    private static final JMenuItem M242REMOVE_PUBLISHER = new JMenuItem("Remove Publisher");
    private static final JMenuItem M262REMOVE_GAMEPLAY_FEATURE = new JMenuItem("Remove Gameplay Feature");
    private static final JMenuItem M252REMOVE_ENGINE_FEATURE = new JMenuItem("Remove Engine Feature");
    private static final JMenuItem M316EXPORT_ALL = new JMenuItem("Export All");
    public static void createFrame(){
        //Creating the Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setMinimumSize(new Dimension(270, 150));
        frame.setLocationRelativeTo(null);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenuItem m11 = new JMenuItem("Settings");
        m11.addActionListener(actionEvent -> WindowSettings.createFrame());
        JMenuItem m12 = new JMenuItem("Check For Updates");
        m12.addActionListener(actionEvent -> UpdateChecker.checkForUpdates(true));
        JMenuItem m13 = new JMenuItem("Uninstall");
        m13.setToolTipText("<html>Includes options with which all mod manager files<br> can be removed and all changes to the game files can be reverted.");
        m13.addActionListener(actionEvent -> Uninstaller.uninstall());
        mb.add(m1);
        m1.add(m11);
        m1.add(m12);
        m1.add(m13);
        JMenu m2Mods = new JMenu("Mods");
        JMenu m22Genres = new JMenu("Genres");
        JMenu m23Themes = new JMenu("Themes");
        JMenu m24Publisher = new JMenu("Publisher");
        JMenu m26GameplayFeatures = new JMenu("Gameplay Features");
        JMenu m25EngineFeatures = new JMenu("Engine Features");
        JMenuItem m21ImportAll = new JMenuItem("Import All");
        JMenuItem m231AddTheme = new JMenuItem("Add Theme");
        JMenuItem m233ImportTheme = new JMenuItem("Import Theme");
        JMenuItem m241AddPublisher = new JMenuItem("Add Publisher");
        JMenuItem m223ImportGenre = new JMenuItem("Import Genre");
        JMenuItem m243ImportPublisher = new JMenuItem("Import Publisher");
        JMenuItem m251AddEngineFeature = new JMenuItem("Add Engine Feature");
        JMenuItem m253ImportEngineFeature = new JMenuItem("Import Engine Feature");
        JMenuItem m261AddGameplayFeature = new JMenuItem("Add Gameplay Feature");
        JMenuItem m263ImportGameplayFeature = new JMenuItem("Import Gameplay Feature");
        m22Genres.add(M221ADD_GENRE);
        m22Genres.add(M222REMOVE_GENRE);
        m22Genres.add(m223ImportGenre);
        m23Themes.add(m231AddTheme);
        m23Themes.add(M232REMOVE_THEME);
        m23Themes.add(m233ImportTheme);
        m24Publisher.add(m241AddPublisher);
        m24Publisher.add(M242REMOVE_PUBLISHER);
        m24Publisher.add(m243ImportPublisher);
        m25EngineFeatures.add(m251AddEngineFeature);
        m25EngineFeatures.add(M252REMOVE_ENGINE_FEATURE);
        m25EngineFeatures.add(m253ImportEngineFeature);
        m26GameplayFeatures.add(m261AddGameplayFeature);
        m26GameplayFeatures.add(M262REMOVE_GAMEPLAY_FEATURE);
        m26GameplayFeatures.add(m263ImportGameplayFeature);
        m21ImportAll.setToolTipText("Click to select a folder where all your files are located that should be imported");
        m21ImportAll.addActionListener(actionEvent -> SharingManager.importAll());
        M221ADD_GENRE.addActionListener(actionEvent -> addGenre());
        M222REMOVE_GENRE.addActionListener(actionEvent -> OperationHelper.process(EditGenreFile::removeGenre, AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), "genre", "removed", "Remove", false));
        m231AddTheme.addActionListener(actionEvent -> addTheme());
        M232REMOVE_THEME.addActionListener(actionEvent ->  OperationHelper.process(EditThemeFiles::removeTheme, AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), "theme", "removed", "Remove", false));
        m233ImportTheme.addActionListener(actionEvent -> SharingManager.importThings("theme.txt", "theme", (string) -> SharingHandler.importTheme(string, true), SharingManager.THEME_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS));
        m223ImportGenre.addActionListener(actionEvent -> SharingManager.importThings("genre.txt", "genre", (string) -> SharingHandler.importGenre(string, true), SharingManager.GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS));
        m243ImportPublisher.addActionListener(actionEvent -> SharingManager.importThings("publisher.txt", "publisher", (string) -> SharingHandler.importPublisher(string, true), SharingManager.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS));
        m253ImportEngineFeature.addActionListener(actionEvent -> SharingManager.importThings("engineFeature.txt", "engine feature", (string) -> SharingHandler.importEngineFeature(string, true), SharingManager.ENGINE_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS));
        m263ImportGameplayFeature.addActionListener(actionEvent -> SharingManager.importThings("gameplayFeature.txt", "gameplay feature", (string) -> SharingHandler.importGameplayFeature(string, true), SharingManager.GAMEPLAY_FEATURE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS));
        M27NPC_GAMES_LIST.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        M27NPC_GAMES_LIST.addActionListener(actionEvent -> npcGameList());
        m241AddPublisher.setToolTipText("Click to add a publisher to MGT2");
        m241AddPublisher.addActionListener(actionEvent -> addPublisher());
        M242REMOVE_PUBLISHER.setToolTipText("Click to remove a publisher from MGT2");
        M242REMOVE_PUBLISHER.addActionListener(actionEvent -> OperationHelper.process(EditPublishersFile::removePublisher, AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), "publisher", "removed", "Remove", false));
        m261AddGameplayFeature.addActionListener(actionEvent -> GameplayFeatureHelper.addGameplayFeature());
        M262REMOVE_GAMEPLAY_FEATURE.addActionListener(actionEvent -> OperationHelper.process(EditGameplayFeaturesFile::removeGameplayFeature, AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), "gameplay feature", "removed", "Remove", false));
        m251AddEngineFeature.addActionListener(actionEvent -> EngineFeatureHelper.addEngineFeature());
        M252REMOVE_ENGINE_FEATURE.addActionListener(actionEvent -> OperationHelper.process(EditEngineFeaturesFile::removeEngineFeature, AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), "engine feature", "removed", "Remove", false));
        JMenuItem m28AddCompanyIcon = new JMenuItem("Add Company Icon");
        m28AddCompanyIcon.addActionListener(actionEvent -> addCompanyIcon());
        mb.add(m2Mods);
        m2Mods.add(m21ImportAll);
        m2Mods.add(m22Genres);
        m2Mods.add(m23Themes);
        m2Mods.add(m24Publisher);
        m2Mods.add(m25EngineFeatures);
        m2Mods.add(m26GameplayFeatures);
        m2Mods.add(M27NPC_GAMES_LIST);
        m2Mods.add(m28AddCompanyIcon);
        JMenu m3Share = new JMenu("Share");
        JMenu m31Export = new JMenu("Export");
        m31Export.add(M311EXPORT_GENRE);
        m31Export.add(M312EXPORT_PUBLISHER);
        m31Export.add(M313EXPORT_THEME);
        m31Export.add(M314EXPORT_ENGINE_FEATURE);
        m31Export.add(M315EXPORT_GAMEPLAY_FEATURE);
        m31Export.add(M316EXPORT_ALL);
        M311EXPORT_GENRE.addActionListener(actionEvent -> OperationHelper.process(SharingHandler::exportGenre, AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId(), AnalyzeExistingGenres.getGenresByAlphabetWithoutId(), "genre", "exported", "Export", true));
        M312EXPORT_PUBLISHER.addActionListener(actionEvent -> OperationHelper.process(SharingHandler::exportPublisher, AnalyzeExistingPublishers.getCustomPublisherString(), AnalyzeExistingPublishers.getPublisherString(), "publisher", "exported", "Export", true));
        M313EXPORT_THEME.addActionListener(actionEvent -> OperationHelper.process(SharingHandler::exportTheme, AnalyzeExistingThemes.getCustomThemesByAlphabet(), AnalyzeExistingThemes.getThemesByAlphabet(), "themes", "exported", "Export", true));
        M314EXPORT_ENGINE_FEATURE.addActionListener(actionEvent -> OperationHelper.process(SharingHandler::exportEngineFeature, AnalyzeExistingEngineFeatures.getCustomEngineFeaturesString(), AnalyzeExistingEngineFeatures.getEngineFeaturesByAlphabet(), "engine feature", "exported", "Export", true));
        M315EXPORT_GAMEPLAY_FEATURE.addActionListener(actionEvent -> OperationHelper.process(SharingHandler::exportGameplayFeature, AnalyzeExistingGameplayFeatures.getCustomGameplayFeaturesString(), AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet(), "gameplay feature", "exported", "Export", true));
        M316EXPORT_ALL.addActionListener(actionEvent -> SharingManager.exportAll());
        JMenuItem m35 = new JMenuItem("Open Export Folder");
        m35.addActionListener(actionEvent -> Utils.open(Utils.getMGT2ModToolExportFolder()));
        JMenuItem m36 = new JMenuItem("Delete all exports");
        m36.addActionListener(actionEvent -> deleteAllExports());
        m3Share.add(m31Export);
        m3Share.add(m35);
        m3Share.add(m36);
        mb.add(m3Share);
        JMenu m4 = new JMenu("Backup");
        JMenu m41 = new JMenu("Create Backup");
        JMenu m42 = new JMenu("Restore Backup");
        JMenuItem m411CreateFullBackup = new JMenuItem("Create Full Backup");
        m411CreateFullBackup.setToolTipText("Click to create a backup from the files that could be modified with this tool.");
        m411CreateFullBackup.addActionListener(actionEvent -> Backup.createBackup("full"));
        JMenuItem m412BackupGenresFile = new JMenuItem("Backup Genres File");
        m412BackupGenresFile.addActionListener(actionEvent -> Backup.createBackup("genre"));
        JMenuItem m413BackupThemesFiles = new JMenuItem("Backup Theme Files");
        m413BackupThemesFiles.addActionListener(actionEvent -> Backup.createBackup("theme"));
        JMenuItem m414BackupSavegames = new JMenuItem("Backup Save Games");
        m414BackupSavegames.addActionListener(actionEvent -> Backup.createBackup("save_game"));
        m41.add(m411CreateFullBackup);
        m41.add(m412BackupGenresFile);
        m41.add(m413BackupThemesFiles);
        m41.add(m414BackupSavegames);
        JMenuItem m421RestoreInitialBackup = new JMenuItem("Restore Initial Backup");
        m421RestoreInitialBackup.setToolTipText("<html>Click to restore the initial backup that has been created when the mod tool was started for the first time.<br>Your save game backups will not be restored.");
        m421RestoreInitialBackup.addActionListener(actionEvent -> restoreInitialBackup());
        JMenuItem m422RestoreLatestBackup = new JMenuItem("Restore Latest Backup");
        m422RestoreLatestBackup.setToolTipText("<html>Click to restore the latest backup that has been created.<br>Your save game backups will not be restored.");
        m422RestoreLatestBackup.addActionListener(actionEvent -> restoreLatestBackup());
        JMenuItem m423RestoreSaveGameBackup = new JMenuItem("Restore Save Game Backup");
        m423RestoreSaveGameBackup.setToolTipText("Click to select a save game for which the backup should be restored.");
        m423RestoreSaveGameBackup.addActionListener(actionEvent -> Backup.restoreSaveGameBackup());
        m42.add(m421RestoreInitialBackup);
        m42.add(m422RestoreLatestBackup);
        m42.add(m423RestoreSaveGameBackup);
        JMenuItem m44 = new JMenuItem("Delete All Backups");
        m44.setToolTipText("Click to delete all backups that have been created.");
        m44.addActionListener(actionEvent -> Backup.deleteAllBackups());
        JMenuItem m45 = new JMenuItem("Open Backup Folder");
        m45.setToolTipText("<html>Click to open the backup folder.<br>All backups that have been created are located here.<br>Use this if you do want to restore a backup manually.");
        m45.addActionListener(actionEvent -> Utils.open(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
        mb.add(m4);
        m4.add(m41);
        m4.add(m42);
        m4.add(m44);
        m4.add(m45);
        JMenu m5 = new JMenu("Utilities");
        JMenuItem m51 = new JMenuItem("Open Github Page");
        m51.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m52 = new JMenuItem("Open MGT2 Folder");
        m52.addActionListener(actionEvent -> Utils.open(Settings.mgt2FilePath));
        JMenuItem m53 = new JMenuItem("Show Active Genres");
        m53.addActionListener(actionEvent -> showActiveGenres());
        JMenuItem m54 = new JMenuItem("Show Active Themes");
        m54.addActionListener(actionEvent -> showActiveThemes());
        JMenuItem m55 = new JMenuItem("Open genres.txt file");
        m55.addActionListener(actionEvent -> Utils.open(Utils.getGenreFile().getPath()));
        JMenuItem m56 = new JMenuItem("Open Genres By Id");
        m56.setToolTipText("<html>Click to open a file where all detected genres are listed by id.<br>Useful if you need the genre id for a function");
        m56.addActionListener(actionEvent -> Utils.open(AnalyzeExistingGenres.FILE_GENRES_BY_ID_HELP.getPath()));
        JMenuItem m57 = new JMenuItem("Open Log File");
        m57.setToolTipText("<html>Click to open the change log.<br>Shows all changes that have been made to the game files.");
        m57.addActionListener(actionEvent -> Utils.open(ChangeLog.FILE_CHANGES_LOG.getPath()));
        JMenuItem m58 = new JMenuItem("Open save game folder");
        m58.setToolTipText("Click to open the folder where the save games are stored");
        m58.addActionListener(actionEvent -> Utils.open(Backup.FILE_SAVE_GAME_FOLDER.getPath()));
        mb.add(m5);
        m5.add(m51);
        m5.add(m52);
        m5.add(m53);
        m5.add(m54);
        m5.add(m55);
        m5.add(m56);
        m5.add(m57);
        m5.add(m58);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel labelVersion = new JLabel("v" + MadGamesTycoon2ModTool.VERSION);
        JButton buttonQuit = new JButton("Quit");
        buttonQuit.addActionListener(actionEvent -> disposeFrame());
        JLabel labelModCreator = new JLabel("by LMH01");
        // Components Added using Flow Layout
        panel.add(labelVersion);
        panel.add(buttonQuit);
        panel.add(labelModCreator);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
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
            boolean noCustomGenreAvailable = true;
            boolean noCustomThemesAvailable = true;
            boolean noCustomPublishersAvailable = true;
            boolean noCustomGameplayFeaturesAvailable = true;
            boolean noCustomEngineFeaturesAvailable = true;
            if(Settings.disableSafetyFeatures){
                noCustomGenreAvailable = false;
                noCustomThemesAvailable = false;
                noCustomPublishersAvailable = false;
                noCustomGameplayFeaturesAvailable = false;
                noCustomEngineFeaturesAvailable = false;
            }else{
                String[] stringCustomGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                if(stringCustomGenres.length != 0){
                    noCustomGenreAvailable = false;
                }
                if(AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE.size() > 189){
                    noCustomThemesAvailable = false;
                }
                List<Map<String, String>> list = AnalyzeExistingPublishers.getListMap();
                if(list.size() > 71){
                    noCustomPublishersAvailable = false;
                }
                List<Map<String, String>> currentGameplayFeatures = AnalyzeExistingGameplayFeatures.gameplayFeatures;
                if(currentGameplayFeatures.size() > 58){
                    noCustomGameplayFeaturesAvailable = false;
                }
                List<Map<String, String>> currentEngineFeatures = AnalyzeExistingEngineFeatures.engineFeatures;
                if(currentEngineFeatures.size() > 58){
                    noCustomEngineFeaturesAvailable = false;
                }
            }
            M222REMOVE_GENRE.setEnabled(!noCustomGenreAvailable);
            M232REMOVE_THEME.setEnabled(!noCustomThemesAvailable);
            M27NPC_GAMES_LIST.setEnabled(!noCustomGenreAvailable);
            M242REMOVE_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
            M311EXPORT_GENRE.setEnabled(!noCustomGenreAvailable);
            M312EXPORT_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
            M313EXPORT_THEME.setEnabled(!noCustomThemesAvailable);
            M252REMOVE_ENGINE_FEATURE.setEnabled(!noCustomEngineFeaturesAvailable);
            M314EXPORT_ENGINE_FEATURE.setEnabled(!noCustomEngineFeaturesAvailable);
            M262REMOVE_GAMEPLAY_FEATURE.setEnabled(!noCustomGameplayFeaturesAvailable);
            M315EXPORT_GAMEPLAY_FEATURE.setEnabled(!noCustomGameplayFeaturesAvailable);
            if(noCustomEngineFeaturesAvailable && noCustomGameplayFeaturesAvailable && noCustomGenreAvailable && noCustomPublishersAvailable && noCustomThemesAvailable){
                M316EXPORT_ALL.setEnabled(false);
            }else{
                M316EXPORT_ALL.setEnabled(true);
            }
            if(noCustomGenreAvailable){
                M222REMOVE_GENRE.setToolTipText("Disabled -> No genre to remove available");
                M27NPC_GAMES_LIST.setToolTipText("Disabled -> Add a genre first");
                M311EXPORT_GENRE.setToolTipText("Disabled -> No genre to export available");
            }else if(noCustomGenreAvailable && noCustomPublishersAvailable && noCustomThemesAvailable){
                M316EXPORT_ALL.setToolTipText("Disabled -> Mo genre, theme or publisher to export available");
            }else{
                M222REMOVE_GENRE.setToolTipText("");
                M27NPC_GAMES_LIST.setToolTipText("");
                M311EXPORT_GENRE.setToolTipText("");
                M316EXPORT_ALL.setToolTipText("Click to export all publishers and genres that have been added");
            }
            if(noCustomThemesAvailable){
                M232REMOVE_THEME.setToolTipText("Disabled -> No theme to remove available");
                M313EXPORT_THEME.setToolTipText("Disabled -> No theme to export available");
            }else{
                M232REMOVE_THEME.setToolTipText("");
            }
            if(noCustomPublishersAvailable){
                M242REMOVE_PUBLISHER.setToolTipText("Disabled -> Add publisher first");
                M312EXPORT_PUBLISHER.setToolTipText("Disabled -> No publisher to export available");
            }else{
                M242REMOVE_PUBLISHER.setToolTipText("");
                M312EXPORT_PUBLISHER.setToolTipText("");
            }
            if(noCustomEngineFeaturesAvailable){
                M252REMOVE_ENGINE_FEATURE.setToolTipText("Disabled -> Add engine feature first");
                M314EXPORT_ENGINE_FEATURE.setToolTipText("Disabled -> Add engine feature first");
            }else{
                M252REMOVE_ENGINE_FEATURE.setToolTipText("");
                M314EXPORT_ENGINE_FEATURE.setToolTipText("");
            }
            if(noCustomGameplayFeaturesAvailable){
                M262REMOVE_GAMEPLAY_FEATURE.setToolTipText("Disabled -> Add gameplay feature first");
                M315EXPORT_GAMEPLAY_FEATURE.setToolTipText("Disabled -> Add gameplay feature first");
            }else{
                M262REMOVE_GAMEPLAY_FEATURE.setToolTipText("");
                M315EXPORT_GAMEPLAY_FEATURE.setToolTipText("");
            }
        }catch (IOException e){
            LOGGER.info("Error" + e.getMessage());
            e.printStackTrace();
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
            JLabel labelExplainList = new JLabel("<html>Chose what genres should work good together<br>with your theme.<br>(Tip: Hold STRG and click with your mouse)");
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddTranslations, labelExplainList, scrollPaneAvailableGenres};
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
                                        EditThemeFiles.addTheme(themeTranslations, arrayListCompatibleGenreIds);
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
        checkActionAvailability();
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
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 0, Integer.MAX_VALUE, 1));
            }else{
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
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
            checkBoxIsPublisher.setToolTipText("When enabled: The company can release game for you (publish)");

            JPanel panelMarketShare = new JPanel();
            JLabel labelMarketShare = new JLabel("Market Share:");
            JSpinner spinnerMarketShare = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
            }else{
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, 100, 1));
            }
            panelMarketShare.add(labelMarketShare);
            panelMarketShare.add(spinnerMarketShare);

            JPanel panelShare = new JPanel();
            JLabel labelShare = new JLabel("Share:");
            JSpinner spinnerShare = new JSpinner();
            spinnerShare.setToolTipText("Set how much money should be earned by one sell");
            if(Settings.disableSafetyFeatures){
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
            }else{
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, 10, 1));
            }
            panelShare.add(labelShare);
            panelShare.add(spinnerShare);

            AtomicInteger genreID = new AtomicInteger();
            JPanel panelGenre = new JPanel();
            JLabel labelGenre = new JLabel("Genre:");
            JButton buttonSelectGenre = new JButton("        Select genre        ");
            buttonSelectGenre.addActionListener(actionEvent -> {
                try {
                    AnalyzeExistingGenres.analyzeGenreFile();
                    JLabel labelChooseGenre = new JLabel("Select the genre that should be compatible:");
                    String[] string;
                    string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
                    JList<String> listAvailableGenres = new JList<>(string);
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                    Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                    if(JOptionPane.showConfirmDialog(null, params, "Select main genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
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
                            hashMap.put("ID", Integer.toString(AnalyzeExistingPublishers.maxThemeID+1));
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
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while adding publisher:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        checkActionAvailability();
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
    private static void deleteAllExports(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete all exports?", "Delete exports?", JOptionPane.YES_NO_OPTION) == 0){
            Utils.deleteDirectory(new File(Utils.getMGT2ModToolExportFolder()));
            JOptionPane.showMessageDialog(null, "All exports have been deleted.");
        }
    }
    private static void restoreInitialBackup(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to restore the initial backup?\nAll changes that you have applied to the game files will be lost.\nThe savegame backups will not be restored.\nA backup of the current files will be created.", "Restore backup?", JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
                String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                for (String customGenre : customGenres) {
                    try {
                        EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(customGenre));
                        ImageFileHandler.removeImageFiles(customGenre, AnalyzeExistingGenres.getGenreIdByName(customGenre));
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
                    JOptionPane.showMessageDialog(null, "The initial backup was not restored.", "Restoring failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        checkActionAvailability();
    }
    private static void restoreLatestBackup(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to restore the latest backup?\nThe savegame backups will not be restored.\nA backup of the current files will be created.", "Restore backup?", JOptionPane.YES_NO_OPTION) == 0){
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
                    JOptionPane.showMessageDialog(null, "The latest backup was not restored.", "Restoring failed", JOptionPane.ERROR_MESSAGE);
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
