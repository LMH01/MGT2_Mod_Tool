package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.EditGenreFile;
import com.github.lmh01.mgt2mt.data_stream.NPCGameListChanger;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class WindowAvailableMods extends JFrame {

    static final WindowAvailableMods FRAME = new WindowAvailableMods();

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
        setBounds(100, 100, 200, 170);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Available Mods");
        SettingsText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        SettingsText.setBounds(50, 11, 150, 19);
        contentPane.add(SettingsText);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("NPC_Games_list");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 80, 175, 23);
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

        JButton buttonAddGenreWindow = new JButton("Add genre");
        buttonAddGenreWindow.setBounds(10, 30, 175, 23);
        buttonAddGenreWindow.setToolTipText("Click to add a new genre to Mad Games Tycoon 2");
        buttonAddGenreWindow.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
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
        buttonRemoveGenreWindow.setBounds(10, 55, 175, 23);
        buttonRemoveGenreWindow.setToolTipText("Click to remove a genre by id from Mad Games Tycoon 2. This will also remove the genre from the NpcGames.txt file.");
        buttonRemoveGenreWindow.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                Backup.createBackup(Utils.getGenreFile());
                System.out.println("Array ids: " + AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size());
                if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size() == 19 && !Settings.disableSafetyFeatures){
                    //This is executed when only one genre has been added to the game.
                    if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete the genre with id [18] from MGT2?\nNote: Save-files that have already been started with this genre will stay unaffected.", "Remove genre?", JOptionPane.YES_NO_OPTION) == 0){
                        try{
                            EditGenreFile.removeGenre(18);
                            NPCGameListChanger.editNPCGames(18, false, 0);
                            JOptionPane.showMessageDialog(new Frame(), "The genre with id [18] has been removed successfully.");
                        }catch (IOException e){
                            JOptionPane.showMessageDialog(new Frame(), "The genre with id [18] was not removed:\n" + e.getMessage());
                        }
                    }
                }else if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1 > 17 || Settings.disableSafetyFeatures){
                    SpinnerNumberModel sModel;
                    if(Settings.disableSafetyFeatures){
                        sModel = new SpinnerNumberModel(18, 18, 999, 1);
                    }else{
                        sModel = new SpinnerNumberModel(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1, 18, AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size()-1, 1);
                    }
                    JSpinner spinnerGenreIdToRemove = new JSpinner(sModel);
                    int option = JOptionPane.showOptionDialog(null, spinnerGenreIdToRemove, "Enter genre id that should be removed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (option == JOptionPane.OK_OPTION) {//TODO Check here if genre id that should be removed exists in the Genres.txt file. If not say that said genre does not exists and that it thus cant be removed.
                        int genreIDToRemove = Integer.parseInt(spinnerGenreIdToRemove.getValue().toString());
                        EditGenreFile.removeGenre(genreIDToRemove);
                        NPCGameListChanger.editNPCGames(genreIDToRemove, false, 0);
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

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(100, 110, 85, 23);
        buttonSettings.setToolTipText("Click to open the settings page.");
        buttonSettings.addActionListener(actionEvent -> WindowSettings.createFrame());
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 110, 80, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(actionEvent -> {
            MainWindow.createFrame();
            FRAME.dispose();
        });
        contentPane.add(btnBack);
    }
}
