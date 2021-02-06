package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class WindowAvailableMods extends JFrame {

    static final WindowAvailableMods FRAME = new WindowAvailableMods();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAvailableMods.class);

    public static void createFrame(){
        EventQueue.invokeLater(() ->{
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAvailableMods(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 230);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Available Mods");
        SettingsText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        SettingsText.setBounds(50, 11, 150, 19);
        contentPane.add(SettingsText);

        JButton buttonAddGenreWindow = new JButton("Add genre");
        buttonAddGenreWindow.setBounds(10, 35, 175, 23);
        buttonAddGenreWindow.setToolTipText("Click to add a new genre to Mad Games Tycoon 2");
        buttonAddGenreWindow.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                AnalyzeExistingThemes.analyzeThemeFiles();
                NewGenreManager.addGenre();
                FRAME.dispose();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The step by step guide could not be started because the Genres.txt file could not be analyzed.\nPlease check if your mgt2 folder is set correctly.\n\nException: " + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            FRAME.dispose();
        });
        contentPane.add(buttonAddGenreWindow);

        JButton buttonRemoveGenreWindow = new JButton("Remove genre");
        buttonRemoveGenreWindow.setBounds(10, 60, 175, 23);
        buttonRemoveGenreWindow.setToolTipText("<html>Click to remove a genre by id from Mad Games Tycoon 2.<br>This will also remove the genre from the NpcGames.txt file.");
        buttonRemoveGenreWindow.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                Backup.createBackup(Utils.getGenreFile());
                System.out.println("Array ids: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size());
                if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1 > 17 || Settings.disableSafetyFeatures){
                    SpinnerNumberModel sModel;
                    if(Settings.disableSafetyFeatures){
                        sModel = new SpinnerNumberModel(0, 0, 999, 1);
                    }else{
                        sModel = new SpinnerNumberModel(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1, 18, AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1, 1);
                    }
                    JSpinner spinnerGenreIdToRemove = new JSpinner(sModel);
                    int option = JOptionPane.showOptionDialog(null, spinnerGenreIdToRemove, "Enter genre id that should be removed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (option == JOptionPane.OK_OPTION) {
                        int genreIDToRemove = Integer.parseInt(spinnerGenreIdToRemove.getValue().toString());
                        EditGenreFile.removeGenre(genreIDToRemove);
                        NPCGameListChanger.editNPCGames(genreIDToRemove, false, 0);
                        EditThemeFiles.editGenreAllocation(genreIDToRemove, false);
                        ChangeLog.addLogEntry(4, genreIDToRemove + "");
                        JOptionPane.showMessageDialog(new Frame(), "The genre with id [" + genreIDToRemove + "] has been removed successfully.");
                    }
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "There is no new genre that has been added.\nAdd a new genre first fia 'Add new genre'.", "Unable to continue:", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                Utils.showErrorMessage(1, e);
                e.printStackTrace();
            }
        });
        contentPane.add(buttonRemoveGenreWindow);

        JButton buttonAddTheme = new JButton("Add theme");
        buttonAddTheme.setBounds(10,85, 175, 23);
        buttonAddTheme.setToolTipText("Click to add a new theme to mgt2.");
        buttonAddTheme.addActionListener(actionEvent ->{
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
                                    for(int i = 0; i<listAvailableThemes.getSelectedValuesList().size(); i++){
                                        arrayListCompatibleGenreNames.add(listAvailableThemes.getSelectedValuesList().get(i));
                                    }
                                    for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.size(); i++){
                                        for(int n = 0; n<arrayListCompatibleGenreNames.size(); n++){
                                            if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).contains(arrayListCompatibleGenreNames.get(n))){
                                                NewThemeManager.arrayListCompatibleGenresForTheme.add(Integer.parseInt(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_BY_ID_SORTED.get(i).replaceAll("[^0-9]","")));
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

        });
        contentPane.add(buttonAddTheme);

        JButton buttonRemoveTheme = new JButton("Remove theme");
        buttonRemoveTheme.setBounds(10,110, 175, 23);
        buttonRemoveTheme.setToolTipText("Click to remove a new theme from mgt2.");
        buttonRemoveTheme.addActionListener(actionEvent ->{
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
        });
        contentPane.add(buttonRemoveTheme);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("NPC_Games_list");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 135, 175, 23);
        buttonOpenAddGenreToGamesWindow.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        buttonOpenAddGenreToGamesWindow.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1 > 17 || Settings.disableSafetyFeatures){
                    WindowNpcGameList.createFrame();
                    FRAME.dispose();
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "There is no new genre that has been added.\nAdd a new genre first fia 'Add new genre'.", "Unable to continue:", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                Utils.showErrorMessage(1, e);
                e.printStackTrace();
            }
        });
        contentPane.add(buttonOpenAddGenreToGamesWindow);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(100, 170, 85, 23);
        buttonSettings.setToolTipText("Click to open the settings page.");
        buttonSettings.addActionListener(actionEvent -> WindowSettings.createFrame());
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 170, 80, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(actionEvent -> {
            MainWindow.createFrame();
            FRAME.dispose();
        });
        contentPane.add(btnBack);
    }
}
