package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private static final JMenuItem M211ADD_GENRE = new JMenuItem("Add Genre");
    private static final JMenuItem M212REMOVE_GENRE = new JMenuItem("Remove Genre");
    private static final JMenuItem M311EXPORT_GENRE = new JMenuItem("Genre");
    private static final JMenuItem M313EXPORT_THEME = new JMenuItem("Theme");
    private static final JMenuItem M312EXPORT_PUBLISHER = new JMenuItem("Publisher");
    private static final JMenuItem M22REMOVE_THEME = new JMenuItem("Remove Theme");
    private static final JMenuItem M24NPC_GAMES_LIST = new JMenuItem("NPC_Games_list");
    private static final JMenuItem M232REMOVE_PUBLISHER = new JMenuItem("Remove Publisher");
    private static final JMenuItem M314EXPORT_ALL = new JMenuItem("Export All");
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
        m13.addActionListener(actionEvent -> uninstall());
        mb.add(m1);
        m1.add(m11);
        m1.add(m12);
        m1.add(m13);
        JMenu m2Mods = new JMenu("Mods");
        JMenu m21Genres = new JMenu("Genres");
        JMenu m22Themes = new JMenu("Themes");
        JMenu m23Publisher = new JMenu("Publisher");
        JMenuItem m221AddTheme = new JMenuItem("Add Theme");
        JMenuItem m231AddPublisher = new JMenuItem("Add Publisher");
        JMenuItem m213ImportGenre = new JMenuItem("Import Genre");
        JMenuItem m233ImportPublisher = new JMenuItem("Import Publisher");
        m21Genres.add(M211ADD_GENRE);
        m21Genres.add(M212REMOVE_GENRE);
        m21Genres.add(m213ImportGenre);
        m22Themes.add(m221AddTheme);
        m22Themes.add(M22REMOVE_THEME);
        m23Publisher.add(m231AddPublisher);
        m23Publisher.add(M232REMOVE_PUBLISHER);
        m23Publisher.add(m233ImportPublisher);
        M211ADD_GENRE.addActionListener(actionEvent -> addGenre());
        M212REMOVE_GENRE.addActionListener(actionEvent -> removeGenre());
        m221AddTheme.addActionListener(actionEvent -> addTheme());
        M22REMOVE_THEME.addActionListener(actionEvent -> removeTheme());
        m213ImportGenre.addActionListener(actionEvent -> importGenre());
        m233ImportPublisher.addActionListener(actionEvent -> importPublisher());
        M24NPC_GAMES_LIST.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        M24NPC_GAMES_LIST.addActionListener(actionEvent -> npcGameList());
        m231AddPublisher.setToolTipText("Click to add a publisher to MGT2");
        m231AddPublisher.addActionListener(actionEvent -> addPublisher());
        M232REMOVE_PUBLISHER.setToolTipText("Click to remove a publisher from MGT2");
        M232REMOVE_PUBLISHER.addActionListener(actionEvent -> removePublisher());
        JMenuItem m25AddCompanyIcon = new JMenuItem("Add Company Icon");
        m25AddCompanyIcon.addActionListener(actionEvent -> addCompanyIcon());
        mb.add(m2Mods);
        m2Mods.add(m21Genres);
        m2Mods.add(m22Themes);
        m2Mods.add(m23Publisher);
        m2Mods.add(M24NPC_GAMES_LIST);
        m2Mods.add(m25AddCompanyIcon);
        JMenu m3Share = new JMenu("Share");
        JMenu m31Export = new JMenu("Export");
        m31Export.add(M311EXPORT_GENRE);
        m31Export.add(M312EXPORT_PUBLISHER);
        m31Export.add(M313EXPORT_THEME);
        m31Export.add(M314EXPORT_ALL);
        M311EXPORT_GENRE.addActionListener(actionEvent -> exportGenre());
        M312EXPORT_PUBLISHER.addActionListener(actionEvent -> exportPublisher());
        M313EXPORT_THEME.addActionListener(actionEvent -> exportTheme());
        M314EXPORT_ALL.addActionListener(actionEvent -> exportAll());
        JMenuItem m35 = new JMenuItem("Open Export Folder");
        m35.addActionListener(actionEvent -> openExportFolder());
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
        m411CreateFullBackup.addActionListener(actionEvent -> createBackup("full"));
        JMenuItem m412BackupGenresFile = new JMenuItem("Backup Genres File");
        m412BackupGenresFile.addActionListener(actionEvent -> createBackup("genre"));
        JMenuItem m413BackupThemesFiles = new JMenuItem("Backup Theme Files");
        m413BackupThemesFiles.addActionListener(actionEvent -> createBackup("theme"));
        JMenuItem m414BackupSavegames = new JMenuItem("Backup Save Games");
        m414BackupSavegames.addActionListener(actionEvent -> createBackup("save_game"));
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
        m423RestoreSaveGameBackup.addActionListener(actionEvent -> restoreSaveGameBackup());
        m42.add(m421RestoreInitialBackup);
        m42.add(m422RestoreLatestBackup);
        m42.add(m423RestoreSaveGameBackup);
        JMenuItem m44 = new JMenuItem("Delete All Backups");
        m44.setToolTipText("Click to delete all backups that have been created.");
        m44.addActionListener(actionEvent -> deleteAllBackups());
        JMenuItem m45 = new JMenuItem("Open Backup Folder");
        m45.setToolTipText("<html>Click to open the backup folder.<br>All backups that have been created are located here.<br>Use this if you do want to restore a backup manually.");
        m45.addActionListener(actionEvent -> openBackupFolder());
        mb.add(m4);
        m4.add(m41);
        m4.add(m42);
        m4.add(m44);
        m4.add(m45);
        JMenu m5 = new JMenu("Utilities");
        JMenuItem m51 = new JMenuItem("Open Github Page");
        m51.addActionListener(actionEvent -> openGithubPage());
        JMenuItem m52 = new JMenuItem("Open MGT2 Folder");
        m52.addActionListener(actionEvent -> openMGT2Folder());
        JMenuItem m53 = new JMenuItem("Show Active Genres");
        m53.addActionListener(actionEvent -> showActiveGenres());
        JMenuItem m54 = new JMenuItem("Show Active Themes");
        m54.addActionListener(actionEvent -> showActiveThemes());
        JMenuItem m55 = new JMenuItem("Open genres.txt file");
        m55.addActionListener(actionEvent -> openGenresTXTFile());
        JMenuItem m56 = new JMenuItem("Open Genres By Id");
        m56.setToolTipText("<html>Click to open a file where all detected genres are listed by id.<br>Useful if you need the genre id for a function");
        m56.addActionListener(actionEvent -> openGenresByIDFile());
        JMenuItem m57 = new JMenuItem("Open Log File");
        m57.setToolTipText("<html>Click to open the change log.<br>Shows all changes that have been made to the game files.");
        m57.addActionListener(actionEvent -> openLogFile());
        JMenuItem m58 = new JMenuItem("Open save game folder");
        m58.setToolTipText("Click to open the folder where the save games are stored");
        m58.addActionListener(actionEvent -> openSaveGameFolder());
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
            boolean noCustomGenreAvailable = true;
            boolean noCustomThemesAvailable = true;
            boolean noCustomPublishersAvailable = true;
            if(Settings.disableSafetyFeatures){
                noCustomGenreAvailable = false;
                noCustomThemesAvailable = false;
                noCustomPublishersAvailable = false;
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
            }
            M212REMOVE_GENRE.setEnabled(!noCustomGenreAvailable);
            M22REMOVE_THEME.setEnabled(!noCustomThemesAvailable);
            M24NPC_GAMES_LIST.setEnabled(!noCustomGenreAvailable);
            M232REMOVE_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
            M311EXPORT_GENRE.setEnabled(!noCustomGenreAvailable);
            M312EXPORT_PUBLISHER.setEnabled(!noCustomPublishersAvailable);
            M313EXPORT_THEME.setEnabled(!noCustomThemesAvailable);
            if(noCustomGenreAvailable && noCustomPublishersAvailable){
                M314EXPORT_ALL.setEnabled(false);
            }else{
                M314EXPORT_ALL.setEnabled(true);
            }
            if(noCustomGenreAvailable){
                M212REMOVE_GENRE.setToolTipText("Disabled -> No genre to remove available");
                M24NPC_GAMES_LIST.setToolTipText("Disabled -> Add a genre first");
                M311EXPORT_GENRE.setToolTipText("Disabled -> No genre to export available");
            }else if(noCustomGenreAvailable && noCustomPublishersAvailable){
                M314EXPORT_ALL.setToolTipText("Disabled -> Mo genre or publisher to export available");
            }else{
                M212REMOVE_GENRE.setToolTipText("");
                M24NPC_GAMES_LIST.setToolTipText("");
                M311EXPORT_GENRE.setToolTipText("");
                M314EXPORT_ALL.setToolTipText("Click to export all publishers and genres that have been added");
            }
            if(noCustomThemesAvailable){
                M22REMOVE_THEME.setToolTipText("Disabled -> No theme to remove available");
                M313EXPORT_THEME.setToolTipText("Disabled -> No theme to export available");
            }else{
                M22REMOVE_THEME.setToolTipText("");
            }
            if(noCustomPublishersAvailable){
                M232REMOVE_PUBLISHER.setToolTipText("Disabled -> Add publisher first");
                M312EXPORT_PUBLISHER.setToolTipText("Disabled -> No publisher to export available");
            }else{
                M232REMOVE_PUBLISHER.setToolTipText("");
                M312EXPORT_PUBLISHER.setToolTipText("");
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
    private static void removeGenre(){
        try {
            AnalyzeExistingGenres.analyzeGenreFile();
            Backup.createBackup(Utils.getGenreFile());
            boolean noGenreToRemoveAvailable = true;
            JLabel labelChooseGenre = new JLabel("Select the genre(s) that should be removed:");
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
                noGenreToRemoveAvailable = false;
            }else{
                string = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                if(string.length != 0){
                    noGenreToRemoveAvailable = false;
                }
            }
            JList<String> listAvailableGenres = new JList<>(string);
            listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
            listAvailableGenres.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

            if(!noGenreToRemoveAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Remove genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableGenres.isSelectionEmpty()){
                        boolean exportFailed = false;
                        StringBuilder failedGenreRemoves = new StringBuilder();
                        for(String currentGenre : listAvailableGenres.getSelectedValuesList()){
                            try{
                                LOGGER.info("Removing genre: " + currentGenre);
                                ImageFileHandler.removeImageFiles(currentGenre, AnalyzeExistingGenres.getGenreIdByName(currentGenre));
                                AnalyzeExistingGenres.analyzeGenreFile();
                                EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre));
                                EditThemeFiles.editGenreAllocation(AnalyzeExistingGenres.getGenreIdByName(currentGenre), false, null);
                                EditGameplayFeaturesFile.removeGenreId(AnalyzeExistingGenres.getGenreIdByName(currentGenre));
                                NPCGameListChanger.editNPCGames(AnalyzeExistingGenres.getGenreIdByName(currentGenre), false, 0);
                            }catch (IOException e){
                                failedGenreRemoves.append(currentGenre).append(" - ").append(e.getMessage()).append(System.getProperty("line.separator"));
                                exportFailed = true;
                            }
                        }
                        if(exportFailed){
                            JOptionPane.showMessageDialog(null, "Something went wrong wile removing genres.\\nThe following genres where not removed:\\n" + failedGenreRemoves, "Genre removal incomplete", JOptionPane.WARNING_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, "All selected genres have been removed successfully!", "Genre removal successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to remove genre:\nThere is no custom genre that could be removed.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while exporting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        checkActionAvailability();
    }
    private static void addTheme(){
        try {
            AnalyzeExistingThemes.analyzeThemeFiles();
            NewThemeManager.arrayListCompatibleGenresForTheme.clear();
            NewThemeManager.arrayListThemeTranslations.clear();
            String[] string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
            JLabel labelEnterThemeName = new JLabel("Enter the theme name:");
            JTextField textFieldThemeName = new JTextField();
            JButton buttonAddTranslations = new JButton("Add translations");
            buttonAddTranslations.setToolTipText("Click to add translations for your theme.");
            buttonAddTranslations.addActionListener(actionEvent2 -> {
                if(!NewThemeManager.arrayListThemeTranslations.isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, "Theme translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        NewThemeManager.arrayListThemeTranslations.clear();
                        NewThemeManager.arrayListThemeTranslations = TranslationManager.getTranslationsArrayList();
                    }
                }else{
                    NewThemeManager.arrayListThemeTranslations = TranslationManager.getTranslationsArrayList();
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
                                arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                for(Map<String, String> map : AnalyzeExistingGenres.genreList){
                                    for(String name : arrayListCompatibleGenreNames){
                                        if(map.get("NAME EN").equals(name)){
                                            NewThemeManager.arrayListCompatibleGenresForTheme.add(Integer.parseInt(map.get("ID")));
                                        }
                                    }
                                }
                                if(JOptionPane.showConfirmDialog(null, "Do you wan't to add this theme?:\n" + textFieldThemeName.getText(), "Add this theme?", JOptionPane.YES_NO_OPTION) == 0){
                                    Backup.createThemeFilesBackup(false);
                                    NewThemeManager.addNewTheme(textFieldThemeName.getText());
                                    JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                    breakLoop = true;
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
    }
    private static void removeTheme(){
        try {
            AnalyzeExistingThemes.analyzeThemeFiles();
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingThemes.getThemesByAlphabet(true);
            }else{
                string = AnalyzeExistingThemes.getCustomThemesByAlphabet();
            }
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableThemes = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableThemes.setPreferredSize(new Dimension(315,140));
            JLabel labelEnterThemeName = new JLabel("Select the theme from the list that should be removed:");

            Object[] params = {labelEnterThemeName, scrollPaneAvailableThemes};
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Remove theme", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableThemes.getSelectedValue().equals("Pets")){
                        if(JOptionPane.showConfirmDialog(null, "Are you shure that you want to remove this theme?:\n" + listAvailableThemes.getSelectedValue(), "Remove theme?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                            Backup.createThemeFilesBackup(false);
                            NewThemeManager.removeTheme(listAvailableThemes.getSelectedValue());
                            JOptionPane.showMessageDialog(null, "The theme has been removed successfully!");
                            breakLoop = true;
                        }
                    }
                }else{
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkActionAvailability();
    }
    private static void exportTheme(){
        try {
            boolean noThemeToExportAvailable = true;
            JLabel labelChooseTheme = new JLabel("Select the theme(s) that should be exported:");
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingThemes.getThemesByAlphabet(false);
                noThemeToExportAvailable = false;
            }else{
                string = AnalyzeExistingThemes.getCustomThemesByAlphabet();
                if(string.length != 0){
                    noThemeToExportAvailable = false;
                }
            }
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableThemes = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableThemes.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseTheme, scrollPaneAvailableThemes};

            if(!noThemeToExportAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Export theme", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableThemes.isSelectionEmpty()){
                        boolean exportFailed = false;
                        boolean multipleThemesToExport = false;
                        if(listAvailableThemes.getSelectedValuesList().size() > 0){
                            multipleThemesToExport = true;
                        }
                        int numberOfThemesToExport = listAvailableThemes.getSelectedValuesList().size();
                        StringBuilder failedThemeExports = new StringBuilder();
                        for(int i=0; i<listAvailableThemes.getSelectedValuesList().size(); i++){
                            String currentTheme = listAvailableThemes.getSelectedValuesList().get(i);
                            if(!SharingHandler.exportTheme(AnalyzeExistingThemes.getSingleThemeByNameMap(currentTheme))){
                                if(!multipleThemesToExport){
                                    JOptionPane.showMessageDialog(null, "The selected theme has already been exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }
                                failedThemeExports.append(currentTheme).append(" - The selected theme has already been exported").append(System.getProperty("line.separator"));
                                exportFailed = true;
                            }
                            numberOfThemesToExport--;
                        }
                        if(numberOfThemesToExport == 0){
                            if(exportFailed){
                                if(JOptionPane.showConfirmDialog(null, "Something went wrong wile exporting themes.\nThe following themes where not exported:\n" + failedThemeExports + "\n\nDo you want to open the folder where it has been saved?", "Theme exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                    Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                }
                            }else{
                                if(JOptionPane.showConfirmDialog(null, "All selected themes have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Theme exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                    Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a theme first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to export theme:\nThere is no custom theme that could be exported.\nPlease add a theme first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while exporting theme: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
    private static void removePublisher(){
        try {
            AnalyzeExistingPublishers.analyzePublisherFile();
            Backup.createBackup(Utils.getPublisherFile());
            boolean noPublisherToRemoveAvailable = true;
            JLabel labelChooseGenre = new JLabel("Select the publisher(s) that should be removed:");
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingPublishers.getPublisherString();
                noPublisherToRemoveAvailable = false;
            }else{
                string = AnalyzeExistingPublishers.getCustomPublisherString();
                if(string.length != 0){
                    noPublisherToRemoveAvailable = false;
                }
            }
            JList<String> listAvailablePublishers = new JList<>(string);
            listAvailablePublishers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailablePublishers.setLayoutOrientation(JList.VERTICAL);
            listAvailablePublishers.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailablePublishers);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

            if(!noPublisherToRemoveAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Remove publishers", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailablePublishers.isSelectionEmpty()){
                        boolean exportFailed = false;
                        int numberOfPublishersToRemove = listAvailablePublishers.getSelectedValuesList().size();
                        StringBuilder failedPublishersRemoves = new StringBuilder();
                        for(int i=0; i<listAvailablePublishers.getSelectedValuesList().size(); i++){
                            String currentPublisher = listAvailablePublishers.getSelectedValuesList().get(i);
                            try{
                                EditPublishersFile.removePublisher(currentPublisher);
                                ChangeLog.addLogEntry(20, currentPublisher);
                            }catch (IOException e){
                                failedPublishersRemoves.append(currentPublisher).append(" - ").append(e.getMessage()).append(System.getProperty("line.separator"));
                                exportFailed = true;
                            }
                            numberOfPublishersToRemove--;
                        }
                        if(numberOfPublishersToRemove == 0){
                            if(exportFailed){
                                JOptionPane.showMessageDialog(null, "Something went wrong wile removing publishers.\\nThe following publishers where not removed:\n" + failedPublishersRemoves, "Publisher removal incomplete", JOptionPane.WARNING_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null, "All selected publishers have been removed successfully!", "Publisher removal successful", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a publisher first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to remove publisher:\nThere is no custom publisher that could be removed.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while removing publisher: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    private static void importGenre(){
        try {
            AnalyzeExistingGenres.analyzeGenreFile();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setDialogTitle("Choose the folder(s) where the genre.txt file is located.");
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    String importGenreFolder = files[i].getPath();
                    if(Utils.doesFolderContainFile(importGenreFolder, "genre.txt")){
                        File fileGenreToImport = new File(importGenreFolder + "//genre.txt");
                        BufferedReader br = new BufferedReader(new FileReader(fileGenreToImport));
                        String currentLine = br.readLine();
                        br.close();
                        if(currentLine.contains("[MGT2MT VERSION]")){
                            LOGGER.info("File seams to be valid. Beginning import process.");
                            try{
                                String returnValue = SharingHandler.importGenre(importGenreFolder);
                                if(returnValue.equals("false")){
                                    JOptionPane.showMessageDialog(null, "The selected genre already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }else{
                                    if(!returnValue.equals("true")){
                                        StringBuilder supportedModToolVersions = new StringBuilder();
                                        for(String string : SharingHandler.GENRE_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
                                            supportedModToolVersions.append("[").append(string).append("]");
                                        }
                                        JOptionPane.showMessageDialog(null, returnValue + "\nSupported versions: " + supportedModToolVersions.toString(), "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }catch(NullPointerException e){
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Unable to import genre:\nThe file is corrupted or not compatible with the current Mod Manager Version", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "The selected folder does not contain a valid genre.txt file.\nPlease select the correct folder.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "The selected folder does not contain the genre.txt file.\nPlease select the correct folder.");
                    }
                }
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void exportGenre(){
        try {
            boolean noGenreToExportAvailable = true;
            JLabel labelChooseGenre = new JLabel("Select the genre(s) that should be exported:");
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
                noGenreToExportAvailable = false;
            }else{
                string = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                if(string.length != 0){
                    noGenreToExportAvailable = false;
                }
            }
            JList<String> listAvailableGenres = new JList<>(string);
            listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
            listAvailableGenres.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

            if(!noGenreToExportAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Export genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableGenres.isSelectionEmpty()){
                        boolean exportFailed = false;
                        boolean multipleGenresToExport = false;
                        if(listAvailableGenres.getSelectedValuesList().size() > 0){
                            multipleGenresToExport = true;
                        }
                        int numberOfGenresToExport = listAvailableGenres.getSelectedValuesList().size();
                        StringBuilder failedGenreExports = new StringBuilder();
                        for(int i=0; i<listAvailableGenres.getSelectedValuesList().size(); i++){
                            String currentGenre = listAvailableGenres.getSelectedValuesList().get(i);
                            if(!SharingHandler.exportGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre), currentGenre)){
                                if(!multipleGenresToExport){
                                    JOptionPane.showMessageDialog(null, "The selected genre has already been exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }
                                failedGenreExports.append(currentGenre).append(" - The selected genre has already been exported").append(System.getProperty("line.separator"));
                                exportFailed = true;
                            }
                            numberOfGenresToExport--;
                        }
                        if(numberOfGenresToExport == 0){
                            if(exportFailed){
                                if(JOptionPane.showConfirmDialog(null, "Something went wrong wile exporting genres.\nThe following genres where not exported:\n" + failedGenreExports + "\n\nDo you want to open the folder where it has been saved?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                    Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                }
                            }else{
                                if(JOptionPane.showConfirmDialog(null, "All selected genres have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                    Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to export genre:\nThere is no custom genre that could be exported.\nPlease add a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while exporting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private static void importPublisher(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setDialogTitle("Choose the folder(s) where the publisher.txt file is located.");
            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int return_value = fileChooser.showOpenDialog(null);
            if(return_value == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();
                for(int i=0; i<fileChooser.getSelectedFiles().length; i++){
                    String importPublisherFolder = files[i].getPath();
                    if(Utils.doesFolderContainFile(importPublisherFolder, "publisher.txt")){
                        File filePublisherToImport = new File(importPublisherFolder + "//publisher.txt");
                        BufferedReader br = new BufferedReader(new FileReader(filePublisherToImport));
                        String currentLine = br.readLine();
                        String secondLine = br.readLine();
                        br.close();
                        if(currentLine.contains("[MGT2MT VERSION]") && secondLine.contains("[PUBLISHER START]")){
                            LOGGER.info("File seams to be valid. Beginning import process.");
                            String returnValue = SharingHandler.importPublisher(importPublisherFolder);
                            if(!returnValue.equals("true")){
                                if(returnValue.equals("false")){
                                    JOptionPane.showMessageDialog(null, "The selected publisher already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }else{
                                    StringBuilder supportedModToolVersions = new StringBuilder();
                                    for(String string : SharingHandler.PUBLISHER_IMPORT_COMPATIBLE_MOD_TOOL_VERSIONS){
                                        supportedModToolVersions.append("[").append(string).append("]");
                                    }
                                    JOptionPane.showMessageDialog(null, returnValue + "\nSupported versions: " + supportedModToolVersions.toString(), "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "The selected folder does not contain a valid publisher.txt file.\nPlease select the correct folder.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "The selected folder does not contain the publisher.txt file.\nPlease select the correct folder.");
                    }
                }
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void exportPublisher(){
        try {
            AnalyzeExistingPublishers.analyzePublisherFile();
            boolean noPublisherToExportAvailable = true;
            JLabel labelChooseGenre = new JLabel("Select the publisher(s) that should be exported:");
            String[] string;
            if(Settings.disableSafetyFeatures){
                string = AnalyzeExistingPublishers.getPublisherString();
                noPublisherToExportAvailable = false;
            }else{
                string = AnalyzeExistingPublishers.getCustomPublisherString();
                if(string.length != 0){
                    noPublisherToExportAvailable = false;
                }
            }
            JList<String> listAvailablePublishers = new JList<>(string);
            listAvailablePublishers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailablePublishers.setLayoutOrientation(JList.VERTICAL);
            listAvailablePublishers.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailablePublishers);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

            if(!noPublisherToExportAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Export publishers", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailablePublishers.isSelectionEmpty()){
                        boolean exportFailed = false;
                        boolean multipleGenresToExport = false;
                        if(listAvailablePublishers.getSelectedValuesList().size() > 0){
                            multipleGenresToExport = true;
                        }
                        int numberOfPublishersToExport = listAvailablePublishers.getSelectedValuesList().size();
                        StringBuilder failedPublishersExport = new StringBuilder();
                        for(int i=0; i<listAvailablePublishers.getSelectedValuesList().size(); i++){
                            String currentPublisher = listAvailablePublishers.getSelectedValuesList().get(i);
                            try{
                                if(!SharingHandler.exportPublisher(currentPublisher, AnalyzeExistingPublishers.getSinglePublisherByNameMap(currentPublisher))){
                                    if(!multipleGenresToExport){
                                        JOptionPane.showMessageDialog(null, "The selected publisher has already been exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                    }
                                    failedPublishersExport.append(currentPublisher).append(" - The selected publisher has already been exported").append(System.getProperty("line.separator"));
                                    exportFailed = true;
                                }
                            }catch (IOException e){
                                failedPublishersExport.append(currentPublisher).append(" - ").append(e.getMessage()).append(System.getProperty("line.separator"));
                                exportFailed = true;
                            }
                            numberOfPublishersToExport--;
                        }
                        if(numberOfPublishersToExport == 0){
                            if(exportFailed){
                                JOptionPane.showMessageDialog(null, "Something went wrong wile exporting publishers.\nThe following publishers where not exported:\n" + failedPublishersExport, "Publisher removal incomplete", JOptionPane.WARNING_MESSAGE);
                            }else{
                                if(JOptionPane.showConfirmDialog(null, "All selected publishers have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Publisher export successful", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                    Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Please select a publisher first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Unable to export publisher:\nThere is no custom publisher that could be exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while exporting publisher: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        checkActionAvailability();
    }
    private static void exportAll(){
        String customGenres[] = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
        String customPublishers[] = AnalyzeExistingPublishers.getCustomPublisherString();
        StringBuilder exportList = new StringBuilder();
        final int ENTIRES_PER_LINE = 10;
        if(customGenres != null){
            exportList.append("Genres: ");
        }
        boolean firstCustomGenre = true;
        int currentLineNumber = 1;
        for(String string : customGenres){
            if(firstCustomGenre){
                firstCustomGenre = false;
            }else{
                exportList.append(", ");
            }
            if(currentLineNumber == ENTIRES_PER_LINE){
                exportList.append(System.getProperty("line.separator"));
            }
            exportList.append(string);
            currentLineNumber++;
        }
        exportList.append(System.getProperty("line.separator"));
        if(customPublishers != null){
            exportList.append("Publisher: ");
        }
        currentLineNumber = 1;
        boolean firstCustomPublisher = true;
        for(String string : customPublishers){
            if(firstCustomPublisher){
                firstCustomPublisher = false;
            }else{
                exportList.append(", ");
            }
            if(currentLineNumber == ENTIRES_PER_LINE){
                exportList.append(System.getProperty("line.separator"));
            }
            exportList.append(string);
            currentLineNumber++;
        }
        if(JOptionPane.showConfirmDialog(null, "The following enties will be exported:\n\n" + exportList.toString(), "Export", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
            StringBuilder failedExports = new StringBuilder();
            try{
                boolean firstGenreExportFail = true;
                boolean exportFailed = false;
                for(String currentGenre : customGenres){
                    if(!SharingHandler.exportGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre), currentGenre)){
                        if(firstGenreExportFail){
                            failedExports.append("Genres: ");
                        }else{
                            failedExports.append(", ");
                        }
                        failedExports.append(currentGenre);
                        firstGenreExportFail = false;
                        exportFailed = true;
                    }
                }
                boolean firstPublisherExportFail = true;
                for(String currentPublisher : customPublishers){
                    if(!SharingHandler.exportPublisher(currentPublisher, AnalyzeExistingPublishers.getSinglePublisherByNameMap(currentPublisher))){
                        if(firstPublisherExportFail){
                            failedExports.append(System.getProperty("line.separator")).append("Publisher: ");
                        }else{
                            failedExports.append(", ");
                        }
                        failedExports.append(currentPublisher);
                        firstPublisherExportFail = false;
                        exportFailed = true;
                    }
                }
                if(exportFailed){
                    if(JOptionPane.showConfirmDialog(null, "The following entries have not been exported because the where already exported:\n\n" + failedExports.toString() + "\n\nDo you want to open the export folder?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, "All entries have been exported successfully!\n\nDo you want to open the folder where they have been saved?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                    }
                }
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Error while exporting:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    private static void openExportFolder(){
        try {
            File file = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
            if(!file.exists()){
                file.mkdirs();
            }
            Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void deleteAllExports(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete all exports?", "Delete exports?", JOptionPane.YES_NO_OPTION) == 0){
            Utils.deleteDirectory(new File(Utils.getMGT2ModToolExportFolder()));
            JOptionPane.showMessageDialog(null, "All exports have been deleted.");
        }
    }
    private static void createBackup(String type){
        if(type.equals("full")){
            try {
                Backup.createFullBackup();
                JOptionPane.showMessageDialog(new Frame(), "The full backup has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
            }
        }else if(type.equals("genre")){
            try{
                Backup.createBackup(Utils.getGenreFile());
                Backup.createBackup(Utils.getNpcGamesFile());
                JOptionPane.showMessageDialog(new Frame(), "Backup of genre files has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
            }
        }else if(type.equals("theme")){
            try{
                Backup.createThemeFilesBackup(false);
                JOptionPane.showMessageDialog(new Frame(), "Backup of theme files has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
            }
        }else if(type.equals("save_game")){
            try{
                Backup.backupSaveGames(false);
                JOptionPane.showMessageDialog(new Frame(), "Backup of save games has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
            }
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
    private static void restoreSaveGameBackup(){
        try {
            ArrayList<File> files = Utils.getFilesInFolderWhiteList(Backup.BACKUP_FOLDER_PATH, "savegame");
            Set<String> saveGameSlots = new HashSet<>();
            for(File file : files){
                saveGameSlots.add(file.getName().replaceAll("[^0-9]", ""));
            }
            JLabel label = new JLabel("<html>Select the save game slot where the save game is saved,<br>for which the backup should be restored:<br>0 = Auto save");
            String[] array = saveGameSlots.stream().toArray(String[]::new);
            JList<String> listAvailableThemes = new JList<>(array);
            listAvailableThemes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableSaveGames = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableSaveGames.setPreferredSize(new Dimension(30,60));

            Object[] params = {label, scrollPaneAvailableSaveGames};
            if(JOptionPane.showConfirmDialog(null, params, "Restore save game", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                int saveGameSlotToRestore = Integer.parseInt(listAvailableThemes.getSelectedValue());
                if(JOptionPane.showConfirmDialog(null, "Are you sure that you would like to restore the backup for save game " + saveGameSlotToRestore + " ?\n\nThis can not be undone!\nI will not take any responsibility if your save game is getting corrupted!\n\nRestore save game backup?", "Restore save game", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    Backup.backupSaveGames(false);
                    Backup.restoreSaveGameBackup(saveGameSlotToRestore);
                    JOptionPane.showMessageDialog(null, "Save game backup has been restored", "Backup restored", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void deleteAllBackups(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete all backups?", "Delete backup?", JOptionPane.YES_NO_OPTION) == 0){
            try {
                Backup.deleteAllBackups();
                if(JOptionPane.showConfirmDialog(null, "All backups have been deleted.\nDo you wan't to create a new initial backup?", "Backups deleted", JOptionPane.YES_NO_OPTION) == 0){
                    String returnValue = Backup.createInitialBackup();
                    if(returnValue.equals("")) {
                        JOptionPane.showMessageDialog(null, "The initial backup has been created successfully.", "Initial backup", JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(null, "The initial backup was not created:\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + returnValue, "Unable to backup file", JOptionPane.ERROR_MESSAGE);
                        ChangeLog.addLogEntry(7, returnValue);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Unable to delete all backups. \n\nException:\n" + e.getMessage(), "Unable to delete backups", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static void openBackupFolder(){
        try {
            File fileBackFolder = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//");
            if(!fileBackFolder.exists()){
                fileBackFolder.mkdirs();
            }
            Desktop.getDesktop().open(fileBackFolder);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Unable to open folder.\n\nException:\n" + ioException.getMessage(), "Unable to open folder", JOptionPane.ERROR_MESSAGE);
            ioException.printStackTrace();
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
    private static void openMGT2Folder(){
        try {
            Desktop.getDesktop().open(new File(Settings.mgt2FilePath));
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
            String[] string = AnalyzeExistingThemes.getThemesByAlphabet(false);

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
    private static void openGenresTXTFile(){
        try {
            if(!Utils.getGenreFile().exists()){
                JOptionPane.showMessageDialog(null, "The Genres.txt file could not be opened.\nFile not found: Please check if your mgt2 folder is set correctly", "Unable to open Genres.txt", JOptionPane.ERROR_MESSAGE);
            }else{
                Desktop.getDesktop().open(Utils.getGenreFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void openGenresByIDFile(){
        try {
            if(!AnalyzeExistingGenres.FILE_GENRES_BY_ID_HELP.exists()){
                JOptionPane.showMessageDialog(null, "The help file could not be opened.\nFile not found.", "Unable to open help file", JOptionPane.ERROR_MESSAGE);
            }else{
                Desktop.getDesktop().open(AnalyzeExistingGenres.FILE_GENRES_BY_ID_HELP);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    private static void openLogFile(){
        try {
            if(!ChangeLog.FILE_CHANGES_LOG.exists()){
                JOptionPane.showMessageDialog(new Frame(), "The file has not been created yet.", "Unable to open log:", JOptionPane.ERROR_MESSAGE);
            }else{
                Desktop.getDesktop().open(ChangeLog.FILE_CHANGES_LOG);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void openSaveGameFolder(){
        try {
            Desktop.getDesktop().open(new File(String.valueOf(Backup.FILE_SAVE_GAME_FOLDER.toPath())));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    private static void uninstall(){
        JLabel labelDescription = new JLabel("<html>Select what should be removed<br>After uninstalling the program is exited");
        JCheckBox checkboxDeleteBackups = new JCheckBox("Delete Backups");
        checkboxDeleteBackups.setSelected(true);
        checkboxDeleteBackups.setToolTipText("Check to delete all backups upon uninstalling");
        JCheckBox checkboxRevertAllMods = new JCheckBox("Revert all mods");
        checkboxRevertAllMods.setSelected(true);
        checkboxRevertAllMods.setToolTipText("Check to revert all mods upon uninstalling");
        JCheckBox checkboxDeleteConfigFiles = new JCheckBox("Delete config files");
        checkboxDeleteConfigFiles.setSelected(true);
        checkboxDeleteConfigFiles.setToolTipText("Check to delete the config file.");
        JCheckBox checkboxDeleteExports = new JCheckBox("Delete Exports");
        checkboxDeleteExports.setSelected(true);
        checkboxDeleteExports.setToolTipText("Check to delete all exports");
        Object[] params = {labelDescription, checkboxDeleteBackups, checkboxRevertAllMods, checkboxDeleteConfigFiles, checkboxDeleteExports};
        if(JOptionPane.showConfirmDialog(null, params, "Uninstall", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            boolean uninstallFailed = false;
            StringBuilder stringActions = new StringBuilder();
            if(checkboxDeleteBackups.isSelected()){
                stringActions.append("Delete Backups").append(System.getProperty("line.separator"));
            }
            if(checkboxRevertAllMods.isSelected()){
                stringActions.append("Revert All Mods").append(System.getProperty("line.separator"));
            }
            if(checkboxDeleteConfigFiles.isSelected()){
                stringActions.append("Delete config files").append(System.getProperty("line.separator"));
            }
            if(checkboxDeleteExports.isSelected()){
                stringActions.append("Delete exports").append(System.getProperty("line.separator"));
            }
            if(JOptionPane.showConfirmDialog(null, "Warning!\nAre you sure that you wan't to do the following:\n\n" + stringActions + "\nThis can't be reverted!", "Confirm uninstall", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                LOGGER.info("Uninstalling...");
                StringBuilder uninstallFailedExplanation = new StringBuilder();
                if(checkboxRevertAllMods.isSelected()){
                    String[] customGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                    for (String customGenre : customGenres) {
                        try {
                            EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(customGenre));
                            ImageFileHandler.removeImageFiles(customGenre, AnalyzeExistingGenres.getGenreIdByName(customGenre));
                            LOGGER.info("Game files have been restored to original.");
                        } catch (IOException e) {
                            LOGGER.info("Genre could not be removed: " + e.getMessage());
                            uninstallFailedExplanation.append("Genre could not be removed: ").append(e.getMessage()).append(System.getProperty("line.separator"));
                            e.printStackTrace();
                            uninstallFailed = true;
                        }
                    }
                    String[] customPublishers = AnalyzeExistingPublishers.getCustomPublisherString();
                    for (String customPublisher : customPublishers) {
                        try {
                            EditPublishersFile.removePublisher(customPublisher);
                            LOGGER.info("Publisher files have been restored to original.");
                        } catch (IOException e) {
                            LOGGER.info("Publisher could not be removed: " + e.getMessage());
                            uninstallFailedExplanation.append("Publisher could not be removed: ").append(e.getMessage()).append(System.getProperty("line.separator"));
                            e.printStackTrace();
                            uninstallFailed = true;
                        }
                    }
                    Backup.restoreBackup(true, false);//This is used to restore the Themes files to its original condition
                }
                if(checkboxDeleteBackups.isSelected() && checkboxDeleteConfigFiles.isSelected() && checkboxDeleteExports.isSelected()){
                    File modManagerPath = new File(Settings.MGT2_MOD_MANAGER_PATH);
                    Utils.deleteDirectory(modManagerPath);
                }
                if(checkboxDeleteBackups.isSelected()){
                    File backupFolder = new File(Backup.BACKUP_FOLDER_PATH);
                    Utils.deleteDirectory(backupFolder);
                    LOGGER.info("Backups have been deleted.");
                }
                if(checkboxDeleteConfigFiles.isSelected()){
                    File configFile = new File(System.getenv("appdata") + "//LMH01//MGT2_Mod_Manager//settings.txt");
                    configFile.deleteOnExit();
                    LOGGER.info("Settings file has been deleted.");
                }
                if(checkboxDeleteExports.isSelected()){
                    File exportFolder = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
                    Utils.deleteDirectory(exportFolder);
                    LOGGER.info("Exports have been deleted.");
                }
                if(uninstallFailed){
                    JOptionPane.showMessageDialog(null, "There was a problem while uninstalling:\n\n" + uninstallFailedExplanation, "Uninstall incomplete", JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Your selected files have been uninstalled successfully!", "Uninstall successful", JOptionPane.INFORMATION_MESSAGE);
                }
                System.exit(0);
            }
        }
    }
}
