package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.dataStream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.dataStream.EditGenreFile;
import com.github.lmh01.mgt2mt.util.NewGenreManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddNewGenre extends JFrame {

    /*TODO Make Add New Genre feature working
    *  When a new genre has been added the programs should ask the user if he wants to implement it into the NPC_Games list
    *  So basically like in WindowAddGenreToGames,
    *   The feature should either: Be a step by step "click" through process or directly in one window.
    *   Using variant one would mean that you will get a summary at the end and that you can click if you want to change something.
    *   When you are satisfied you can click one button to apply the new genre
    *   When you complete one step you should be able to jump back and forth.
    *   Button could be "Add new genre (step by step)" -> I think that this is the better way to go.
    *   Each of those steps will get a own Window.jar file
    *       Currently working on this! Top Priority to get it working -> then commit
    *
    * */
    private JPanel contentPane;
    static WindowAddNewGenre frame = new WindowAddNewGenre();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddNewGenre() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 170);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Add genre");
        SettingsText.setFont(new Font("Tahoma", 0, 15));
        SettingsText.setBounds(60, 11, 150, 19);
        contentPane.add(SettingsText);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("Start step by step guide");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 50, 175, 23);
        buttonOpenAddGenreToGamesWindow.setToolTipText("Click to add a genre to MGT2 by using a step by step guide.");
        buttonOpenAddGenreToGamesWindow.addActionListener((ignored) -> {
            AnalyzeExistingGenres.analyzeExistingGenres();
            NewGenreManager.addGenre();
            frame.dispose();
        });
        contentPane.add(buttonOpenAddGenreToGamesWindow);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(100, 110, 85, 23);
        buttonSettings.addActionListener(e -> {
            WindowSettings.createFrame();
        });
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener((ignored) -> {
            WindowAvailableMods.createFrame();
            frame.dispose();
        });
        btnBack.setBounds(10, 82, 80, 23);
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener((ignored) -> System.exit(0));
        btnQuit.setBounds(10, 110, 80, 23);
        contentPane.add(btnQuit);
    }
}
