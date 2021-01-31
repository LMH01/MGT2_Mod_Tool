package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Settings;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowSettings extends JFrame {

    static final WindowSettings FRAME = new WindowSettings();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowSettings(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 570, 200);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel("Settings");
        SettingsText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        SettingsText.setBounds(236, 11, 57, 19);
        contentPane.add(SettingsText);

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
        contentPane.add(lblMinecraftLocation);

        JTextField textFieldMGT2Folder = new JTextField();
        textFieldMGT2Folder.setBounds(147, 101, 246, 20);
        textFieldMGT2Folder.setText(Settings.mgt2FilePath);
        textFieldMGT2Folder.setColumns(10);
        contentPane.add(textFieldMGT2Folder);

        JButton buttonBrowseMGT2Folder = new JButton("Browse");
        buttonBrowseMGT2Folder.setBounds(405, 99, 89, 23);
        buttonBrowseMGT2Folder.setToolTipText("Click to open a folder choosing dialog to change the MGT2 folder.");
        buttonBrowseMGT2Folder.addActionListener(actionEvent -> {
            Settings.setMgt2FilePath(false);
            textFieldMGT2Folder.setText(Settings.mgt2FilePath);
        });
        contentPane.add(buttonBrowseMGT2Folder);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 132, 69, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(actionEvent -> {
            Settings.saveSettings();
            WindowSettings.FRAME.dispose();
        });
        contentPane.add(btnBack);

        JButton btnResetSettings = new JButton("Reset Settings");
        btnResetSettings.setBounds(280, 132, 127, 23);
        btnResetSettings.setToolTipText("Click to reset the settings to default values.");
        btnResetSettings.addActionListener(actionEvent -> {
            if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Reset Settings", JOptionPane.YES_NO_OPTION) == 0) {
                Settings.resetSettings();
                textFieldMGT2Folder.setText(Settings.mgt2FilePath);
                JOptionPane.showMessageDialog(new Frame(), "Settings have been restored to default.");
                checkBoxDebugMode.setSelected(false);
                checkBoxDisableSafety.setSelected(false);
            }

        });
        contentPane.add(btnResetSettings);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(443, 132, 89, 23);
        btnSave.setToolTipText("Click to save the current settings.");
        btnSave.addActionListener(actionEvent -> {
            if(checkBoxDisableSafety.isSelected()){
                if(JOptionPane.showConfirmDialog(null, "Are you sure that you wan't to disable the safety features?\nThis could lead to problems.", "Disable safety features?", JOptionPane.YES_NO_OPTION) == 0){
                    Settings.setSettings(checkBoxDebugMode,checkBoxDisableSafety);
                }
            }else{
                Settings.setSettings(checkBoxDebugMode,checkBoxDisableSafety);
            }
        });
        contentPane.add(btnSave);

        JButton btnLoadSettings = new JButton("Load Settings");
        btnLoadSettings.setBounds(138, 132, 113, 23);
        btnLoadSettings.setToolTipText("Click to load settings from file.");
        btnLoadSettings.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            int return_value = fileChooser.showOpenDialog(null);
            if (return_value == 0) {
                Settings.importCustomSettings(fileChooser.getSelectedFile().getPath());
            }
            Settings.saveSettings();
        });
        contentPane.add(btnLoadSettings);
    }
}
