package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowSettings extends JFrame {

    //TODO Lambda expressions
    private JPanel contentPane;
    static WindowSettings frame = new WindowSettings();

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

    public WindowSettings(){
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 570, 200);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager)null);
        JLabel SettingsText = new JLabel("Settings");
        SettingsText.setFont(new Font("Tahoma", 0, 15));
        SettingsText.setBounds(236, 11, 57, 19);
        this.contentPane.add(SettingsText);

        JCheckBox checkBoxDebugMode = new JCheckBox("Enable debug logging");
        checkBoxDebugMode.setBounds(20, 40, 200, 23);
        checkBoxDebugMode.setToolTipText("Check this box to enable debug logging when opening this jar with the console.");
        checkBoxDebugMode.setSelected(Settings.enableDebugLogging);
        contentPane.add(checkBoxDebugMode);

        JCheckBox checkBoxDisableSafety = new JCheckBox("Disable safety features");
        checkBoxDisableSafety.setBounds(20, 70, 200, 23);
        checkBoxDisableSafety.setToolTipText("Check this box to disable the automatic genre id allocation. If checked most spinners won't have a maximum value. Do only enable when you use your own genre id system and you need the spinners to be unlocked.");
        checkBoxDisableSafety.setSelected(Settings.disableSafetyFeatures);
        contentPane.add(checkBoxDisableSafety);

        JLabel lblMinecraftLocation = new JLabel("MGT2 Location:");
        lblMinecraftLocation.setBounds(20, 103, 127, 14);
        this.contentPane.add(lblMinecraftLocation);

        JTextField textFieldMGT2Folder = new JTextField();
        textFieldMGT2Folder.setText(Settings.mgt2FilePath);
        textFieldMGT2Folder.setBounds(147, 101, 246, 20);
        textFieldMGT2Folder.setColumns(10);
        this.contentPane.add(textFieldMGT2Folder);

        JButton buttonBrowseMGT2Folder = new JButton("Browse");
        buttonBrowseMGT2Folder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setMgt2FilePath(false);
                textFieldMGT2Folder.setText(Settings.mgt2FilePath);
            }
        });
        buttonBrowseMGT2Folder.setBounds(405, 99, 89, 23);
        this.contentPane.add(buttonBrowseMGT2Folder);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.exportSettings();
                WindowSettings.frame.dispose();
            }
        });
        btnBack.setBounds(10, 132, 69, 23);
        this.contentPane.add(btnBack);

        JButton btnResetSettings = new JButton("Reset Settings");
        btnResetSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog((Component)null, "Are you sure?", "Reset Settings", 0) == 0) {
                    Settings.resetSettings();
                    textFieldMGT2Folder.setText(Settings.mgt2FilePath);
                    JOptionPane.showMessageDialog(new Frame(), "Settings have been restored to default.");
                    checkBoxDebugMode.setSelected(false);
                    checkBoxDisableSafety.setSelected(false);
                }

            }
        });
        btnResetSettings.setBounds(280, 132, 127, 23);
        this.contentPane.add(btnResetSettings);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.enableDebugLogging = checkBoxDebugMode.isSelected();
                Settings.disableSafetyFeatures = checkBoxDisableSafety.isSelected();
                Settings.exportSettings();
                JOptionPane.showMessageDialog(new Frame(), "Settings saved.");
            }
        });
        btnSave.setBounds(443, 132, 89, 23);
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
        btnLoadSettings.setBounds(138, 132, 113, 23);
        this.contentPane.add(btnLoadSettings);
    }
}
