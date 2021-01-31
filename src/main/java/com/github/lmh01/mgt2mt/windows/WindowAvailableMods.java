package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.EditGenreFile;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class WindowAvailableMods extends JFrame {

    static WindowAvailableMods frame = new WindowAvailableMods();

    public static void createFrame(){
        EventQueue.invokeLater(() ->{
            try {
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAvailableMods(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 200);
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
        buttonOpenAddGenreToGamesWindow.addActionListener((ignored) -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                if(AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1 > 17 || Settings.disableSafetyFeatures){
                    WindowAddGenreToGames.createFrame();
                    frame.dispose();
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
        buttonAddGenreWindow.addActionListener((ignored) -> {
            WindowAddNewGenre.createFrame();
            frame.dispose();
        });
        contentPane.add(buttonAddGenreWindow);

        JButton buttonRemoveGenreWindow = new JButton("Remove genre");
        buttonRemoveGenreWindow.setBounds(10, 55, 175, 23);
        buttonRemoveGenreWindow.setToolTipText("Click to remove a genre by id from Mad Games Tycoon 2");
        buttonRemoveGenreWindow.addActionListener((ignored) -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                Backup.createBackup(Utils.fileGenres);
                if(AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1 > 17 || Settings.disableSafetyFeatures){
                    SpinnerNumberModel sModel;
                    if(Settings.disableSafetyFeatures){
                        sModel = new SpinnerNumberModel(18, 18, 999, 1);
                    }else{
                        sModel = new SpinnerNumberModel(AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1, 18, AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1, 1);
                    }
                    JSpinner spinnerGenreIdToRemove = new JSpinner(sModel);
                    int option = JOptionPane.showOptionDialog(null, spinnerGenreIdToRemove, "Enter genre id that should be removed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (option == JOptionPane.OK_OPTION) {
                        if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete the genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] from MGT2?\nNote: Save-files that have already been started with this genre will stay unaffected.", "Remove genre?", JOptionPane.YES_NO_OPTION) == 0){
                            try{
                                EditGenreFile.removeGenre(Integer.parseInt(spinnerGenreIdToRemove.getValue().toString()));
                                JOptionPane.showMessageDialog(new Frame(), "The genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] has been removed successfully.");
                            }catch (IOException e){
                                JOptionPane.showMessageDialog(new Frame(), "The genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] was not removed:\n" + e.getMessage());
                            }
                        }
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
        buttonSettings.setBounds(100, 140, 85, 23);
        buttonSettings.setToolTipText("Click to open the settings page.");
        buttonSettings.addActionListener(e -> WindowSettings.createFrame());
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 112, 80, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener((ignored) -> {
            MainWindow.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setBounds(10, 140, 80, 23);
        btnQuit.setToolTipText("Click to exit the application.");
        btnQuit.addActionListener((ignored) -> System.exit(0));
        contentPane.add(btnQuit);
    }
}
