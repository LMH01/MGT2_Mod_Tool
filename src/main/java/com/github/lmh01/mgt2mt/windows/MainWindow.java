package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.dataStream.ImageFileHandler;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.genre.WindowAddGenrePage8;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    static MainWindow frame = new MainWindow();

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

    public MainWindow(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 570, 200);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        setBounds(100, 100, 335, 160);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel labelTitle = new JLabel("Mad Games Tycoon 2 Mod Tool");
        labelTitle.setBounds(60, 0, 260, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        getContentPane().add(labelTitle);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(10, 100, 100, 23);
        buttonQuit.setToolTipText("Click to exit the application.");
        buttonQuit.addActionListener(e -> {
            Settings.exportSettings();
            System.exit(0);
        });
        getContentPane().add(buttonQuit);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(230, 70, 90, 23);
        buttonSettings.setToolTipText("Click to open the settings page.");
        buttonSettings.addActionListener(e -> WindowSettings.createFrame());
        getContentPane().add(buttonSettings);

        JButton buttonChangelog = new JButton("Changelog");
        buttonChangelog.setBounds(120, 100, 100, 23);
        buttonChangelog.setToolTipText("Click to open the changelog and feature page.");
        buttonChangelog.addActionListener(e -> {
            WindowChangelog.createFrame();
            frame.dispose();
        });
        getContentPane().add(buttonChangelog);

        JButton buttonBackup = new JButton("Backup");
        buttonBackup.setBounds(10, 70, 100, 23);
        buttonBackup.setToolTipText("Click to open the backup page.");
        buttonBackup.addActionListener(e -> {
            WindowBackup.createFrame();
            frame.dispose();
        });
        getContentPane().add(buttonBackup);

        JButton buttonOther = new JButton("Other");
        buttonOther.setBounds(120, 70, 100, 23);
        buttonOther.setToolTipText("Click to open the other page.");
        buttonOther.addActionListener(e -> {
            WindowOther.createFrame();
            frame.dispose();
        });
        getContentPane().add(buttonOther);

        JButton buttonAvailableMods = new JButton("Begin");
        buttonAvailableMods.setBounds(10, 40, 310, 23);
        buttonAvailableMods.setToolTipText("Click to open the page with the available modifications");
        buttonAvailableMods.addActionListener(e -> {
            WindowAvailableMods.createFrame();
            frame.dispose();
        });
        getContentPane().add(buttonAvailableMods);

        JLabel labelVersion = new JLabel("V1.0");
        labelVersion.setBounds(268, 100, 150, 23);
        getContentPane().add(labelVersion);

        JLabel labelBy = new JLabel("by LMH01");
        labelBy.setBounds(135, 14, 70, 23);
        getContentPane().add(labelBy);
    }
}
