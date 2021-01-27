package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowSettings extends JFrame {

    private JPanel contentPane;
    static WindowSettings frame = new WindowSettings();

    public static void createFrame(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public WindowSettings(){
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 570, 161);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager)null);
        JLabel SettingsText = new JLabel("Settings");
        SettingsText.setFont(new Font("Tahoma", 0, 15));
        SettingsText.setBounds(236, 11, 57, 19);
        this.contentPane.add(SettingsText);

        JLabel lblMinecraftLocation = new JLabel("MGT2 Location:");
        lblMinecraftLocation.setBounds(10, 63, 127, 14);
        this.contentPane.add(lblMinecraftLocation);

        JTextField textFieldMGT2Folder = new JTextField();
        //textFieldMGT2Folder.setEditable(false);
        textFieldMGT2Folder.setText(Settings.mgt2FilePath);
        textFieldMGT2Folder.setBounds(147, 61, 246, 20);
        textFieldMGT2Folder.setColumns(10);
        this.contentPane.add(textFieldMGT2Folder);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowSettings.frame.dispose();
            }
        });
        btnBack.setBounds(10, 92, 89, 23);
        this.contentPane.add(btnBack);

        JButton btnBrowseMinecraftFolder = new JButton("Browse");
        btnBrowseMinecraftFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setMgt2FilePath();
                textFieldMGT2Folder.setText(Settings.mgt2FilePath);
            }
        });
        btnBrowseMinecraftFolder.setBounds(405, 59, 89, 23);
        this.contentPane.add(btnBrowseMinecraftFolder);

        JButton btnResetSettings = new JButton("Reset Settings");
        btnResetSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog((Component)null, "Are you sure?", "Reset Settings", 0) == 0) {
                    Settings.resetSettings();
                    textFieldMGT2Folder.setText(Settings.mgt2FilePath);
                }

            }
        });
        btnResetSettings.setBounds(280, 92, 127, 23);
        this.contentPane.add(btnResetSettings);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.exportSettings();
                WindowSettings.frame.dispose();
            }
        });
        btnSave.setBounds(443, 92, 89, 23);
        this.contentPane.add(btnSave);
        JButton btnLoadSettings = new JButton("Load Settings");
        btnLoadSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int return_value = fileChooser.showOpenDialog((Component)null);
                if (return_value == 0) {
                    Settings.importCustomSettings(fileChooser.getSelectedFile().getPath());
                }
                Settings.exportSettings();
            }
        });
        btnLoadSettings.setBounds(138, 92, 113, 23);
        this.contentPane.add(btnLoadSettings);
    }
}
