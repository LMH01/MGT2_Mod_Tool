package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Backup;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WindowBackup extends JFrame {

    static WindowBackup frame = new WindowBackup();

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

    public WindowBackup(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 160);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelTitle = new JLabel("Backup");
        labelTitle.setBounds(75, 0, 60, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        JButton buttonCreateBackup = new JButton("Create Backup");
        buttonCreateBackup.setBounds(10, 40, 175, 23);
        buttonCreateBackup.setToolTipText("Click to create a backup from the files that could be modified with this tool.");
        buttonCreateBackup.addActionListener(ignored -> {
            try {
                Backup.createFullBackup(false);
                JOptionPane.showMessageDialog(new Frame(), "The backup has been created successfully.", "Backup created.", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new Frame(), "Unable to create backup.\nFile not found: Please check if your mgt2 folder is set correctly.\n\nException:\n" + e.getMessage(), "Backup failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPane.add(buttonCreateBackup);

        JButton buttonOpenBackupFolder = new JButton("Open backup folder");
        buttonOpenBackupFolder.setBounds(10, 70, 175, 23);
        buttonOpenBackupFolder.setToolTipText("Click to open the backup folder.");
        buttonOpenBackupFolder.addActionListener(ignored -> {
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
        });
        contentPane.add(buttonOpenBackupFolder);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 100, 89, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(ignored -> {
            MainWindow.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);
    }
}
