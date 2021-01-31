package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.genre.WindowAddGenrePage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WindowBackup extends JFrame {

    static WindowBackup frame = new WindowBackup();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage2.class);

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
        setBounds(100, 100, 200, 250);
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

        JButton buttonRestoreInitialBackup = new JButton("Restore initial backup");
        buttonRestoreInitialBackup.setBounds(10, 70, 175, 23);
        buttonRestoreInitialBackup.setToolTipText("Click to restore the initial backup that has been created when the mod tool was started for the first time.");
        buttonRestoreInitialBackup.addActionListener(actionEvent ->{
            if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to restore the initial backup?\nAll changes that you have applied to the game files will me lost.\nA backup of the current files will be created.", "Restore backup?", JOptionPane.YES_NO_OPTION) == 0){
                try {
                    LOGGER.info("Creating backup beforehand.");
                    Backup.createFullBackup(false);
                    Backup.restoreBackup(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    if(Utils.showConfirmDialog(1, e)){
                        Backup.restoreBackup(true);
                    }else{
                        JOptionPane.showMessageDialog(null, "The initial backup was not restored.", "Restoring failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        contentPane.add(buttonRestoreInitialBackup);

        JButton buttonRestoreLatestBackup = new JButton("Restore latest backup");
        buttonRestoreLatestBackup.setBounds(10, 100, 175, 23);
        buttonRestoreLatestBackup.setToolTipText("Click to restore the latest backup that has been created.");
        buttonRestoreLatestBackup.addActionListener(actionEvent ->{
            if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to restore the latest backup?\nA backup of the current files will be created.", "Restore backup?", JOptionPane.YES_NO_OPTION) == 0){
                try {
                    LOGGER.info("Creating backup beforehand.");
                    Backup.createFullBackup(false);
                    Backup.restoreBackup(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    if(Utils.showConfirmDialog(1, e)){
                        Backup.restoreBackup(false);
                    }else{
                        JOptionPane.showMessageDialog(null, "The latest backup was not restored.", "Restoring failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        contentPane.add(buttonRestoreLatestBackup);

        JButton buttonDeleteBackups = new JButton("Delete all backups");
        buttonDeleteBackups.setBounds(10, 130, 175, 23);
        buttonDeleteBackups.setToolTipText("Click to delete all backups that have been created.");
        buttonDeleteBackups.addActionListener(actionEvent ->{
            if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to delete all backups?", "Delete backup?", JOptionPane.YES_NO_OPTION) == 0){
                try {
                    Backup.deleteAllBackups();
                    JOptionPane.showMessageDialog(null, "All backups have been deleted.", "Backups deleted", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Unable to delete all backups. \n\nException:\n" + e.getMessage(), "Unable to delete backups", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(buttonDeleteBackups);

        JButton buttonOpenBackupFolder = new JButton("Open backup folder");
        buttonOpenBackupFolder.setBounds(10, 160, 175, 23);
        buttonOpenBackupFolder.setToolTipText("Click to open the backup folder. All backups that have been created are located here. Use this if you do want to restore a backup manually.");
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
        btnBack.setBounds(10, 190, 89, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(ignored -> {
            MainWindow.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);
    }
}
