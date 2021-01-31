package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.UpdateChecker;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WindowOther extends JFrame {

    static WindowOther frame = new WindowOther();

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

    public WindowOther(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 280);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelTitle = new JLabel("Other");
        labelTitle.setBounds(75, 0, 60, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        JButton buttonCreateBackup = new JButton("Open log");
        buttonCreateBackup.setBounds(10, 40, 175, 23);
        buttonCreateBackup.setToolTipText("Click to open the change log. Shows all changes that have been made to the game files.");
        buttonCreateBackup.addActionListener(ignored -> {
            try {
                if(!ChangeLog.FILE_CHANGES_LOG.exists()){
                    JOptionPane.showMessageDialog(new Frame(), "The file has not been created yet.", "Unable to open log:", JOptionPane.ERROR_MESSAGE);
                }else{
                    Desktop.getDesktop().open(ChangeLog.FILE_CHANGES_LOG);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contentPane.add(buttonCreateBackup);

        JButton buttonOpenBackupFolder = new JButton("Open MGT2 Folder");
        buttonOpenBackupFolder.setBounds(10, 70, 175, 23);
        buttonOpenBackupFolder.setToolTipText("Click to open the MGT2 main folder.");
        buttonOpenBackupFolder.addActionListener(ignored -> {
            try {
                Desktop.getDesktop().open(new File(Settings.mgt2FilePath));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        contentPane.add(buttonOpenBackupFolder);

        JButton buttonShowGenres = new JButton("Show active genres");
        buttonShowGenres.setBounds(10, 100, 175, 23);
        buttonShowGenres.setToolTipText("Click to see a list of currently active genres. The number behind the genre number is the genre id.");
        buttonShowGenres.addActionListener(ignored -> {
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
        });
        contentPane.add(buttonShowGenres);

        JButton buttonOpenGenresTxtFile = new JButton("Open genres.txt");
        buttonOpenGenresTxtFile.setBounds(10, 130, 175, 23);
        buttonOpenGenresTxtFile.setToolTipText("Click to open the MGT2 main folder.");
        buttonOpenGenresTxtFile.addActionListener(ignored -> {
            try {
                if(!Utils.fileGenres.exists()){
                    JOptionPane.showMessageDialog(null, "The Genres.txt file could not be opened.\nFile not found: Please check if your mgt2 folder is set correctly", "Unable to open Genres.txt", JOptionPane.ERROR_MESSAGE);
                }else{
                    Desktop.getDesktop().open(Utils.fileGenres);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contentPane.add(buttonOpenGenresTxtFile);

        JButton buttonOpenGithubPage = new JButton("Open Github");
        buttonOpenGithubPage.setBounds(10, 160, 175, 23);
        buttonOpenGithubPage.setToolTipText("Click to open the Github repository for this project.");
        buttonOpenGithubPage.addActionListener(ignored -> {
            try {
                Utils.openGithubPage();
            } catch (Exception e) {
                Utils.showErrorMessage(2, e);
                e.printStackTrace();
            }
        });
        contentPane.add(buttonOpenGithubPage);

        JButton buttonCheckForUpdates = new JButton("Check for updates");
        buttonCheckForUpdates.setBounds(10, 190, 175, 23);
        buttonCheckForUpdates.setToolTipText("Click to check this tool for updates.");
        buttonCheckForUpdates.addActionListener(ignored -> {
            UpdateChecker.checkForUpdates(true);
        });
        contentPane.add(buttonCheckForUpdates);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 220, 89, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(ignored -> {
            MainWindow.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);
    }
}
