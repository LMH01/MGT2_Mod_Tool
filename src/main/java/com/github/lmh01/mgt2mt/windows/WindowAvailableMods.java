package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.dataStream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.dataStream.EditGenreFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAvailableMods extends JFrame {

    private JPanel contentPane;
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
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Available Mods");
        SettingsText.setFont(new Font("Tahoma", 0, 15));
        SettingsText.setBounds(50, 11, 150, 19);
        this.contentPane.add(SettingsText);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("NPC_Games_list");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 80, 175, 23);
        buttonOpenAddGenreToGamesWindow.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        buttonOpenAddGenreToGamesWindow.addActionListener((ignored) -> {
            AnalyzeExistingGenres.analyzeExistingGenres();
            WindowAddGenreToGames.createFrame();
            frame.dispose();
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
            AnalyzeExistingGenres.analyzeExistingGenres();
            SpinnerNumberModel sModel = new SpinnerNumberModel(18, 18, AnalyzeExistingGenres.genreIDsInUse.size()-1, 1);
            JSpinner spinnerGenreIdToRemove = new JSpinner(sModel);
            int option = JOptionPane.showOptionDialog(null, spinnerGenreIdToRemove, "Enter genre id that should be removed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == JOptionPane.CANCEL_OPTION) {
                // user hit cancel
            } else if (option == JOptionPane.OK_OPTION) {
                if(JOptionPane.showConfirmDialog((Component)null, "Are you sure that you wan't to delete the genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] from MGT2?\nNote: Save-files that have already been started with this genre will stay unaffected.", "Remove genre?", 0) == 0){
                    String returnValue = EditGenreFile.removeGenre(Integer.parseInt(spinnerGenreIdToRemove.getValue().toString()));
                    if(returnValue.equals("success")){
                        JOptionPane.showMessageDialog(new Frame(), "The genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] has been removed successfully.");
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), "The genre with id [" + spinnerGenreIdToRemove.getValue().toString() + "] was not removed:\n" + returnValue);
                    }
                }
            }
        });
        contentPane.add(buttonRemoveGenreWindow);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(100, 140, 85, 23);
        buttonSettings.addActionListener(e -> {
            WindowSettings.createFrame();
        });
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener((ignored) -> {
            MadGamesTycoon2ModTool.createFrame();
            frame.dispose();
        });
        btnBack.setBounds(10, 112, 80, 23);
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener((ignored) -> System.exit(0));
        btnQuit.setBounds(10, 140, 80, 23);
        contentPane.add(btnQuit);
    }
}
