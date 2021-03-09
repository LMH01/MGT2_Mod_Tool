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
import java.util.ArrayList;

public class WindowMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowMain.class);
    private static JFrame frame = new JFrame("MGT2 Mod Tool");
    private static JMenuItem m21 = new JMenuItem("Add Genre");
    private static JMenuItem m22 = new JMenuItem("Remove Genre");
    private static JMenuItem m32 = new JMenuItem("Export Genre");
    private static JMenuItem m24 = new JMenuItem("Remove Theme");
    private static JMenuItem m25 = new JMenuItem("NPC_Games_list");
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
        JMenu m2 = new JMenu("Mods");
        m21.addActionListener(actionEvent -> addGenre());
        m22.addActionListener(actionEvent -> removeGenre());
        JMenuItem m23 = new JMenuItem("Add Theme");
        m23.addActionListener(actionEvent -> addTheme());
        m24.addActionListener(actionEvent -> removeTheme());
        m25.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        m25.addActionListener(actionEvent -> npcGameList());
        mb.add(m2);
        m2.add(m21);
        m2.add(m22);
        m2.add(m23);
        m2.add(m24);
        m2.add(m25);
        JMenu m3 = new JMenu("Share");
        JMenuItem m31 = new JMenuItem("Import Genre");
        m31.addActionListener(actionEvent -> importGenre());
        m32.addActionListener(actionEvent -> exportGenre());
        JMenuItem m33 = new JMenuItem("Open Export Folder");
        m33.addActionListener(actionEvent -> openExportFolder());
        m3.add(m31);
        m3.add(m32);
        m3.add(m33);
        mb.add(m3);
        JMenu m4 = new JMenu("Backup");
        JMenuItem m41 = new JMenuItem("Create Backup");
        m41.setToolTipText("Click to create a backup from the files that could be modified with this tool.");
        m41.addActionListener(actionEvent -> createBackup());
        JMenuItem m42 = new JMenuItem("Restore Initial Backup");
        m42.setToolTipText("<html>Click to restore the initial backup that has been created when the mod tool was started for the first time.<br>Your save game backups will not be restored.");
        m42.addActionListener(actionEvent -> restoreInitialBackup());
        JMenuItem m43 = new JMenuItem("Restore Latest Backup");
        m43.setToolTipText("<html>Click to restore the latest backup that has been created.<br>Your save game backups will not be restored.");
        m43.addActionListener(actionEvent -> restoreLatestBackup());
        JMenuItem m44 = new JMenuItem("Delete All Backups");
        m44.setToolTipText("Click to delete all backups that have been created.");
        m44.addActionListener(actionEvent -> deleteAllBackups());
        JMenuItem m45 = new JMenuItem("Open Backup Folder");
        m45.setToolTipText("<html>Click to open the backup folder.<br>All backups that have been created are located here.<br>Use this if you do want to restore a backup manually.");
        m45.addActionListener(actionEvent -> openBackupFolder());
        mb.add(m4);
        m4.add(m41);
        m4.add(m42);
        m4.add(m43);
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
        mb.add(m5);
        m5.add(m51);
        m5.add(m52);
        m5.add(m53);
        m5.add(m54);
        m5.add(m55);
        m5.add(m56);
        m5.add(m57);

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
     * Sets if the entry Add New Genre should be active or disabled
     * @param enabled True when button should be enabled
     */
    public static void setNewGenreButtonStatus(boolean enabled){
        m21.setEnabled(enabled);
    }
    /**
     * Checks if specific actions are available. If they are the buttons will be enabled
     */
    public static void checkActionAvailability(){
        try{
            AnalyzeExistingGenres.analyzeGenreFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            boolean noCustomGenreAvailable = true;
            boolean noCustomThemesAvailable = true;
            if(Settings.disableSafetyFeatures){
                noCustomGenreAvailable = false;
                noCustomThemesAvailable = false;
            }else{
                String[] stringCustomGenres = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                if(stringCustomGenres.length != 0){
                    noCustomGenreAvailable = false;
                }
                if(AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE.size() > 189){
                    noCustomThemesAvailable = false;
                }
            }
            m22.setEnabled(!noCustomGenreAvailable);
            m24.setEnabled(!noCustomThemesAvailable);
            m25.setEnabled(!noCustomGenreAvailable);
            m32.setEnabled(!noCustomGenreAvailable);
            if(noCustomGenreAvailable){
                m22.setToolTipText("Disabled -> No genre to remove available");
                m24.setToolTipText("Disabled -> No theme to remove available");
                m25.setToolTipText("Disabled -> Add a genre first");
                m32.setToolTipText("Disabled -> No genre to export available");
            }else{
                m22.setToolTipText("");
                m24.setToolTipText("");
                m25.setToolTipText("");
                m32.setToolTipText("");
            }
        }catch (IOException e){
            LOGGER.info("Error" + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void addGenre(){
        try {
            setNewGenreButtonStatus(false);
            AnalyzeExistingGenres.analyzeGenreFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            GenreManager.addGenre();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The step by step guide could not be started because the Genres.txt file could not be analyzed.\nPlease check if your mgt2 folder is set correctly.\n\nException: " + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
            setNewGenreButtonStatus(true);
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
                        int numberOfGenresToRemove = listAvailableGenres.getSelectedValuesList().size();
                        String failedGenreRemoves = "";
                        for(int i=0; i<listAvailableGenres.getSelectedValuesList().size(); i++){
                            String currentGenre = listAvailableGenres.getSelectedValuesList().get(i);
                            if(!AnalyzeExistingGenres.analyzeSingleGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre))){
                                LOGGER.info("Genre has not been found!");
                                JOptionPane.showMessageDialog(null, "Unable to remove genre:\nInternal error\nSee console for further information!", "Error", JOptionPane.ERROR_MESSAGE);
                                failedGenreRemoves = failedGenreRemoves + " - Internal error" + currentGenre + System.getProperty("line.separator");
                                exportFailed = true;
                            }else{
                                try{
                                    EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre));
                                }catch (IOException e){
                                    failedGenreRemoves = failedGenreRemoves + currentGenre + " - " + e.getMessage() + System.getProperty("line.separator");
                                    exportFailed = true;
                                }
                            }
                            numberOfGenresToRemove--;
                        }
                        if(numberOfGenresToRemove == 0){
                            if(exportFailed){
                                JOptionPane.showMessageDialog(null, "Something went wrong wile removing genres.\\nThe following genres where not removed:\\n\" + failedGenreRemoves", "Genre removal incomplete", JOptionPane.WARNING_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null, "All selected genres have been exported successfully!", "Genre removal successful", JOptionPane.INFORMATION_MESSAGE);
                            }
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
            AnalyzeExistingGenres.analyzeGenreFile();
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
                                for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.size(); i++){
                                    for (String arrayListCompatibleGenreName : arrayListCompatibleGenreNames) {
                                        if (AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).contains(arrayListCompatibleGenreName)) {
                                            NewThemeManager.arrayListCompatibleGenresForTheme.add(Integer.parseInt(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]", "")));
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
            AnalyzeExistingGenres.analyzeGenreFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            String[] string = AnalyzeExistingThemes.getThemesByAlphabet(true);
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
    private static void npcGameList(){
        try {
            AnalyzeExistingGenres.analyzeGenreFile();
            if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1 > 17 || Settings.disableSafetyFeatures){
                WindowNpcGameList.createFrame();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "There is no new genre that has been added.\nAdd a new genre first fia 'Add new genre'.", "Unable to continue:", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            Utils.showErrorMessage(1, e);
            e.printStackTrace();
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
                            if(!SharingHandler.importGenre(importGenreFolder)){
                                JOptionPane.showMessageDialog(null, "The selected genre already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
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
            AnalyzeExistingGenres.analyzeGenreFile();
            JLabel labelChooseGenre = new JLabel("Select the genre that should be exported:");
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
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

            if(!noGenreToExportAvailable){
                if(JOptionPane.showConfirmDialog(null, params, "Export genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!listAvailableThemes.isSelectionEmpty()){
                        boolean exportFailed = false;
                        boolean multipleGenresToExport = false;
                        if(listAvailableThemes.getSelectedValuesList().size() > 0){
                            multipleGenresToExport = true;
                        }
                        int numberOfGenresToExport = listAvailableThemes.getSelectedValuesList().size();
                        String failedGenreExports = "";
                        for(int i=0; i<listAvailableThemes.getSelectedValuesList().size(); i++){
                            String currentGenre = listAvailableThemes.getSelectedValuesList().get(i);
                            if(!AnalyzeExistingGenres.analyzeSingleGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre))){
                                LOGGER.info("Genre has not been found!");
                                JOptionPane.showMessageDialog(null, "Unable to export genre:\nInternal error\nSee console for further information!", "Error", JOptionPane.ERROR_MESSAGE);
                                failedGenreExports = failedGenreExports + " - Internal error" + currentGenre + System.getProperty("line.separator");
                                exportFailed = true;
                            }else{
                                if(!SharingHandler.exportGenre(AnalyzeExistingGenres.getGenreIdByName(currentGenre), currentGenre)){
                                    if(!multipleGenresToExport){
                                        JOptionPane.showMessageDialog(null, "The selected genre has already been exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                    }
                                    failedGenreExports = failedGenreExports + currentGenre + " - The selected genre has already been exported" + System.getProperty("line.separator");
                                    exportFailed = true;
                                }
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
    private static void createBackup(){
        try {
            Backup.createFullBackup();
            JOptionPane.showMessageDialog(new Frame(), "The backup has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void restoreInitialBackup(){
        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to restore the initial backup?\nAll changes that you have applied to the game files will me lost.\nThe savegame backups will not be restored.\nA backup of the current files will be created.", "Restore backup?", JOptionPane.YES_NO_OPTION) == 0){
            try {
                LOGGER.info("Creating backup beforehand.");
                Backup.createFullBackup();
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
        try {
            AnalyzeExistingGenres.analyzeGenreFile();
            String[] string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();

            JList<String> listAvailableGenres = new JList<>(string);
            listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
            listAvailableGenres.setVisibleRowCount(-1);

            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            JOptionPane.showMessageDialog(null, scrollPaneAvailableGenres, "The following genres are currently active.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            Utils.showErrorMessage(1, e);
            e.printStackTrace();
        }
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
            AnalyzeExistingGenres.analyzeGenreFile();
            if(!AnalyzeExistingGenres.FILE_GENRES_BY_ID_HELP.exists()){
                JOptionPane.showMessageDialog(null, "The help file could not be opened.\nFile not found.", "Unable to open Genres.txt", JOptionPane.ERROR_MESSAGE);
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
                    for(int i=0; i<customGenres.length; i++){
                        try {
                            EditGenreFile.removeGenre(AnalyzeExistingGenres.getGenreIdByName(customGenres[i]));
                            LOGGER.info("Game files have been restored to original.");
                        } catch (IOException e) {
                            LOGGER.info("Genre could not be removed: " + e.getMessage());
                            uninstallFailedExplanation.append("Genre could not be removed: " + e.getMessage()).append(System.getProperty("line.separator"));
                            e.printStackTrace();
                            uninstallFailed = true;
                        }
                    }
                    Backup.restoreBackup(true, false);//This is used to restore the Themes file to its original condition
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
                    configFile.deleteOnExit();;
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
