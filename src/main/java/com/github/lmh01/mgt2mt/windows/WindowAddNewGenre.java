package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.util.NewGenreManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class WindowAddNewGenre extends JFrame {

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

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Add genre");
        SettingsText.setBounds(60, 11, 150, 19);
        SettingsText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(SettingsText);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("Start step by step guide");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 50, 175, 23);
        buttonOpenAddGenreToGamesWindow.setToolTipText("Click to add a new genre to MGT2 by using a step by step guide.");
        buttonOpenAddGenreToGamesWindow.addActionListener((ignored) -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                NewGenreManager.addGenre();
                frame.dispose();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The step by step guide could not be started because the Genres.txt file could not be analyzed.\nPlease check if your mgt2 folder is set correctly.\n\nException: " + e.getMessage(), "Unable to continue", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
        contentPane.add(buttonOpenAddGenreToGamesWindow);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(100, 110, 85, 23);
        buttonSettings.setToolTipText("Click to open the settings page.");
        buttonSettings.addActionListener(e -> WindowSettings.createFrame());
        contentPane.add(buttonSettings);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 82, 80, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener((ignored) -> {
            WindowAvailableMods.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setBounds(10, 110, 80, 23);
        btnQuit.setToolTipText("Click to exit the application.");
        btnQuit.addActionListener((ignored) -> System.exit(0));
        contentPane.add(btnQuit);
    }
}
