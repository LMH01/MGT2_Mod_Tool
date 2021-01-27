package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAvailableMods extends JFrame {

    //TODO Make this panel look good and add the correct buttons (Back, Exit, Settings and the two "Mod" Buttons)

    //TODO maybe make it possible to add a new feature fia a step by step wizard.
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

        JButton buttonOpenAddGenreToGamesWindow = new JButton("Add genre to NPC_Games");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 80, 175, 23);
        buttonOpenAddGenreToGamesWindow.setToolTipText("Click to add a genre id to the NPC_Games_list.");
        buttonOpenAddGenreToGamesWindow.addActionListener((ignored) -> {
            WindowAddGenreToGames.createFrame();
            frame.dispose();
        });
        contentPane.add(buttonOpenAddGenreToGamesWindow);

        JButton buttonAddGenreWindow = new JButton("Add genre");
        buttonAddGenreWindow.setBounds(10, 50, 175, 23);
        buttonAddGenreWindow.setToolTipText("Click to add a new genre to Mad Games Tycoon 2");
        buttonAddGenreWindow.addActionListener((ignored) -> {
            WindowAddGenreToGames.createFrame();
            frame.dispose();
        });
        contentPane.add(buttonAddGenreWindow);

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
