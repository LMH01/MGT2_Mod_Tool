package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class WindowSettings extends JFrame {

    static final WindowSettings FRAME = new WindowSettings();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowSettings.class);
    private static boolean customFolderSetAndValid = false;
    private static boolean unsavedChanges = false;
    static String inputFolder = "";//This string contains the current mgt2folder when the program is started
    private static String outputFolder = "";//This string contains the folder path that should be set
    JComboBox comboBoxMGT2FolderOperation = new JComboBox();
    JComboBox comboBoxLanguage = new JComboBox();
    JComboBox comboBoxUpdateChannel = new JComboBox();
    JCheckBox checkBoxDisableSafety = new JCheckBox(I18n.INSTANCE.get("window.settings.safetyFeatures.checkBoxText"));
    JCheckBox checkBoxDebugMode = new JCheckBox(I18n.INSTANCE.get("window.settings.debugMode.checkBoxText"));
    JCheckBox checkBoxSaveLogs = new JCheckBox(I18n.INSTANCE.get("window.settings.checkBox.saveLogs"));
    AtomicBoolean doNotPerformComboBoxActionListener = new AtomicBoolean(false);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
                FRAME.loadCurrentSelections();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowSettings(){
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 343, 290);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel SettingsText = new JLabel(I18n.INSTANCE.get("window.settings.windowTitle"));
        SettingsText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        SettingsText.setBounds(137, 11, 100, 19);
        contentPane.add(SettingsText);

        checkBoxDebugMode.setBounds(20, 40, 250, 23);
        checkBoxDebugMode.setToolTipText(I18n.INSTANCE.get("window.settings.debugMode.checkBoxToolTip"));
        checkBoxDebugMode.addActionListener(e -> {
            LOGGER.info("checkBoxDebugMode action: " + e.getActionCommand());
            unsavedChanges = checkBoxDebugMode.isSelected() != Settings.enableDebugLogging;
        });
        contentPane.add(checkBoxDebugMode);

        checkBoxDisableSafety.setBounds(20, 70, 250, 23);
        checkBoxDisableSafety.setToolTipText(I18n.INSTANCE.get("window.settings.safetyFeatures.checkBoxToolTip"));
        checkBoxDisableSafety.addActionListener(e -> {
            LOGGER.info("checkBoxDisableSafety action: " + e.getActionCommand());
            if(checkBoxDisableSafety.isSelected()){
                checkBoxDisableSafety.setSelected(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.safetyFeatures.warning"), I18n.INSTANCE.get("window.settings.safetyFeatures.warning.title"), JOptionPane.YES_NO_OPTION) == 0);
            }
            unsavedChanges = checkBoxDisableSafety.isSelected() != Settings.disableSafetyFeatures;
        });
        contentPane.add(checkBoxDisableSafety);

        checkBoxSaveLogs.setBounds(20, 100, 250, 23);
        checkBoxSaveLogs.setToolTipText(I18n.INSTANCE.get("window.settings.checkBox.saveLogs.toolTip"));
        checkBoxSaveLogs.addActionListener(actionEvent -> unsavedChanges = checkBoxSaveLogs.isSelected() != Settings.saveLogs);
        contentPane.add(checkBoxSaveLogs);

        JLabel labelLanguage = new JLabel(I18n.INSTANCE.get("window.settings.language.label"));
        labelLanguage.setBounds(20, 133, 127, 14);
        contentPane.add(labelLanguage);

        comboBoxLanguage.setBounds(117, 130, 100, 23);
        comboBoxLanguage.setToolTipText(I18n.INSTANCE.get("window.settings.language.comboBox.toolTip"));
        comboBoxLanguage.addActionListener(actionEvent -> {
            if(!Objects.equals(Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString(), Settings.language)){
                if(Objects.equals(comboBoxLanguage.getSelectedItem().toString(), "Deutsch")){
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.language.informationMessage") + System.getProperty("line.separator") + I18n.INSTANCE.get("window.settings.language.informationMessageTranslationIncomplete"));
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.language.informationMessage"));
                }
                unsavedChanges = true;
            }else{
                unsavedChanges = false;
            }
        });
        contentPane.add(comboBoxLanguage);

        JLabel labelUpdateBranch = new JLabel(I18n.INSTANCE.get("window.settings.updateChannel.label"));
        labelUpdateBranch.setBounds(20,158, 127, 14);
        contentPane.add(labelUpdateBranch);

        comboBoxUpdateChannel.setBounds(117, 155, 100, 23);
        comboBoxUpdateChannel.setToolTipText(I18n.INSTANCE.get("window.settings.updateChannel.toolTip"));
        comboBoxUpdateChannel.addActionListener(actionEvent -> unsavedChanges = !Objects.equals(comboBoxUpdateChannel.getSelectedItem().toString(), Settings.updateBranch));
        contentPane.add(comboBoxUpdateChannel);

        JLabel lblMGT2Location = new JLabel(I18n.INSTANCE.get("window.settings.mgt2location.label"));
        lblMGT2Location.setBounds(20, 183, 127, 14);
        contentPane.add(lblMGT2Location);

        AtomicBoolean automaticWasLastSelectedOption = new AtomicBoolean(!Settings.enableCustomFolder);
        AtomicBoolean manualWasLastSelectedOption = new AtomicBoolean(Settings.enableCustomFolder);
        comboBoxMGT2FolderOperation.setBounds(117, 180, 100, 23);
        comboBoxMGT2FolderOperation.addActionListener(e -> {
            LOGGER.info("comboBoxMGT2FolderOperation action: " + e.getActionCommand());
            LOGGER.info("doNotPerformComboBoxActionListener: " + doNotPerformComboBoxActionListener.get());
            LOGGER.info("Objects.equals(comboBoxMGT2FolderOperation.getSelectedItem(), \"Manual\")" + Objects.equals(comboBoxMGT2FolderOperation.getSelectedItem(), "Manual"));
            LOGGER.info("manualWasLastSelectedOption.get(): " + manualWasLastSelectedOption.get());
            LOGGER.info("customFolderSetAndValid: " + customFolderSetAndValid);
            if(!doNotPerformComboBoxActionListener.get()){
                if(Objects.equals(comboBoxMGT2FolderOperation.getSelectedItem(), "Manual") && automaticWasLastSelectedOption.get()){
                    if(!customFolderSetAndValid){
                        try {
                            automaticWasLastSelectedOption.set(false);
                            manualWasLastSelectedOption.set(true);
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
                            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                            fileChooser.setDialogTitle(I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.title"));
                            fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                            int return_value = fileChooser.showOpenDialog(null);
                            if(return_value == JFileChooser.APPROVE_OPTION){
                                String mgt2Folder = fileChooser.getSelectedFile().getPath();
                                if(Settings.validateMGT2Folder(mgt2Folder, false, false)){
                                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderSet"));
                                    outputFolder = mgt2Folder;
                                    customFolderSetAndValid = true;
                                    automaticWasLastSelectedOption.set(false);
                                    manualWasLastSelectedOption.set(true);
                                    unsavedChanges = true;
                                }else{
                                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderInvalid"), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderInvalid.title"), JOptionPane.ERROR_MESSAGE);
                                    comboBoxMGT2FolderOperation.setSelectedItem("Automatic");
                                    automaticWasLastSelectedOption.set(true);
                                    manualWasLastSelectedOption.set(false);
                                    customFolderSetAndValid = false;
                                    unsavedChanges = false;
                                }
                            }else{
                                comboBoxMGT2FolderOperation.setSelectedItem("Automatic");
                                automaticWasLastSelectedOption.set(true);
                                manualWasLastSelectedOption.set(false);
                                unsavedChanges = false;
                            }
                            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }else if (comboBoxMGT2FolderOperation.getSelectedItem().equals("Automatic") && manualWasLastSelectedOption.get()){
                    String mgt2Folder = Settings.getMGT2FilePath();
                    if(!mgt2Folder.isEmpty()){
                        outputFolder = mgt2Folder;
                        customFolderSetAndValid = false;
                        unsavedChanges = true;
                        automaticWasLastSelectedOption.set(true);
                        manualWasLastSelectedOption.set(false);
                    }else{
                        if(customFolderSetAndValid){
                            manualWasLastSelectedOption.set(true);
                            automaticWasLastSelectedOption.set(false);
                            comboBoxMGT2FolderOperation.setSelectedItem("Manual");
                            Settings.validateMGT2Folder(Settings.mgt2FilePath, false, true);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.mgt2location.automaticSelected.folderNotFound"));
                        }
                    }
                }
            }
        });
        contentPane.add(comboBoxMGT2FolderOperation);

        JButton buttonResetCustomFolder = new JButton(I18n.INSTANCE.get("window.settings.reset.label"));
        buttonResetCustomFolder.setBounds(230, 180, 89, 23);
        buttonResetCustomFolder.setToolTipText(I18n.INSTANCE.get("window.settings.reset.button.toolTip"));
        buttonResetCustomFolder.addActionListener(actionEvent -> {
            LOGGER.info("input folder: " + inputFolder);
            LOGGER.info("output folder: " + outputFolder);
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.mgt2location.resetFolder"), I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                boolean performTasks = false;
                if(Settings.getMGT2FilePath().isEmpty() && customFolderSetAndValid){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.noAutomaticFolderFound"), I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        performTasks = true;
                    }
                }else{
                    performTasks = true;
                }
                if(performTasks){
                    Settings.setMGT2Folder(true);
                    outputFolder = Settings.mgt2FilePath;
                    Settings.enableCustomFolder = false;
                    doNotPerformComboBoxActionListener.set(true);
                    automaticWasLastSelectedOption.set(true);
                    manualWasLastSelectedOption.set(false);
                    customFolderSetAndValid = false;
                    comboBoxMGT2FolderOperation.setSelectedItem("Automatic");
                    FRAME.dispose();
                    createFrame();
                }
            }
        });
        contentPane.add(buttonResetCustomFolder);

        JButton btnBack = new JButton(I18n.INSTANCE.get("button.back"));
        btnBack.setBounds(10, 222, 69, 23);
        btnBack.setToolTipText(I18n.INSTANCE.get("window.settings.button.back.toolTip"));
        btnBack.addActionListener(actionEvent -> {
            if(unsavedChanges){
                String unsavedChanges = getChangesInSettings(checkBoxDebugMode, checkBoxDisableSafety, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs);
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.changesNotSaved.part1") + "\n\n" + unsavedChanges + "\n" + I18n.INSTANCE.get("window.settings.changesNotSaved.part2"), I18n.INSTANCE.get("window.settings.changesNotSaved.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    setCurrentSettings(checkBoxDebugMode, checkBoxDisableSafety, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs);
                    WindowMain.checkActionAvailability();
                    customFolderSetAndValid = false;
                    WindowSettings.FRAME.dispose();
                    Backup.createInitialBackup();
                }
            }
            unsavedChanges = false;
            if(Settings.mgt2FolderIsCorrect){
                WindowMain.checkActionAvailability();
            }
            WindowSettings.FRAME.dispose();
        });
        contentPane.add(btnBack);

        JButton btnResetSettings = new JButton(I18n.INSTANCE.get("window.settings.button.resetSettings.label"));
        btnResetSettings.setBounds(90, 222, 127, 23);
        btnResetSettings.setToolTipText(I18n.INSTANCE.get("window.settings.button.resetSettings.toolTip"));
        btnResetSettings.addActionListener(actionEvent -> {
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonBodies.areYouSure"), I18n.INSTANCE.get("window.settings.button.resetSettings.label"), JOptionPane.YES_NO_OPTION) == 0) {
                Settings.resetSettings();
                doNotPerformComboBoxActionListener.set(true);
                checkBoxDebugMode.setSelected(false);
                checkBoxDisableSafety.setSelected(false);
                customFolderSetAndValid = false;
                comboBoxMGT2FolderOperation.setSelectedItem("Automatic");
                automaticWasLastSelectedOption.set(true);
                manualWasLastSelectedOption.set(false);
                unsavedChanges = false;
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("window.settings.button.resetSettings.restored"));
                FRAME.dispose();
                createFrame();
            }
        });
        contentPane.add(btnResetSettings);

        JButton btnSave = new JButton(I18n.INSTANCE.get("window.settings.button.save.label"));
        btnSave.setBounds(230, 222, 89, 23);
        btnSave.setToolTipText(I18n.INSTANCE.get("window.settings.button.save.toolTip"));
        btnSave.addActionListener(actionEvent -> {
            String unsavedChangesList = getChangesInSettings(checkBoxDebugMode, checkBoxDisableSafety, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs);
            if(unsavedChangesList.isEmpty()){
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.button.save.nothingToSave"));
            }else{
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.button.save.saveSettings") + "\n\n" + unsavedChangesList, I18n.INSTANCE.get("window.settings.button.save.saveSettings.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    setCurrentSettings(checkBoxDebugMode, checkBoxDisableSafety, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs);
                    WindowMain.checkActionAvailability();
                    Backup.createInitialBackup();
                    unsavedChanges = false;
                    customFolderSetAndValid = false;
                    FRAME.dispose();
                    createFrame();
                }
            }
        });
        contentPane.add(btnSave);
    }

    private void loadCurrentSelections(){
        inputFolder = Settings.mgt2FilePath;
        outputFolder = Settings.mgt2FilePath;
        doNotPerformComboBoxActionListener.set(false);
        if(Settings.enableCustomFolder && Settings.mgt2FolderIsCorrect){
            comboBoxMGT2FolderOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Manual", "Automatic"}));
        }else{
            comboBoxMGT2FolderOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Automatic", "Manual"}));
        }
        if(Settings.language.equals("English")){
            comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[]{"English", "Deutsch"}));
        }else if(Settings.language.equals("Deutsch")){
            comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[]{"Deutsch", "English"}));
        }
        if(Settings.updateBranch.equals("Release")){
            comboBoxUpdateChannel.setModel(new DefaultComboBoxModel<>(new String[]{"Release", "Alpha"}));
        }else{
            comboBoxUpdateChannel.setModel(new DefaultComboBoxModel<>(new String[]{"Alpha", "Release"}));
        }
        checkBoxDebugMode.setSelected(Settings.enableDebugLogging);
        checkBoxDisableSafety.setSelected(Settings.disableSafetyFeatures);
        checkBoxSaveLogs.setSelected(Settings.saveLogs);
        comboBoxMGT2FolderOperation.setToolTipText(I18n.INSTANCE.get("window.settings.mgt2location.toolTip") + " " + Settings.mgt2FilePath);
        if(Settings.mgt2FolderIsCorrect){
            checkBoxDisableSafety.setEnabled(true);
        }else{
            comboBoxMGT2FolderOperation.setToolTipText(I18n.INSTANCE.get("window.settings.mgt2location.toolTip") + " " + I18n.INSTANCE.get("window.settings.mgt2location.toolTip.notSet"));
        }
        customFolderSetAndValid = Settings.enableCustomFolder;
    }

    /**
     * Applies the local changes in the settings to the global settings by calling Settings.setSettings(...)
     * @param checkBoxDebugMode The debug mode checkbox
     * @param checkBoxDisableSafety The disable safety features checkbox
     */
    private static void setCurrentSettings(JCheckBox checkBoxDebugMode,JCheckBox checkBoxDisableSafety, JComboBox comboBoxLanguage, JComboBox comboBoxUpdateBranch, JCheckBox checkBoxSaveLogs){
        Settings.setSettings(true, checkBoxDebugMode.isSelected(),checkBoxDisableSafety.isSelected(), customFolderSetAndValid, outputFolder, Settings.enableDisclaimerMessage, Settings.enableGenreNameTranslationInfo, Settings.enableGenreDescriptionTranslationInfo, Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString(), Objects.requireNonNull(comboBoxUpdateBranch.getSelectedItem()).toString(), checkBoxSaveLogs.isSelected());
    }

    /**
     * @param checkBoxDebugMode The debug mode checkbox
     * @param checkBoxDisableSafety The disable safety features checkbox
     * @return Returns the changes that have been made to the settings
     */
    private static String getChangesInSettings(JCheckBox checkBoxDebugMode,JCheckBox checkBoxDisableSafety, JComboBox comboBoxLanguage, JComboBox comboBoxUpdateBranch, JCheckBox checkBoxSaveLogs){
        StringBuilder unsavedChanges = new StringBuilder();
        if(Settings.enableDebugLogging != checkBoxDebugMode.isSelected()){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.debugLogging")).append(" ").append(Settings.enableDebugLogging).append(" -> ").append(checkBoxDebugMode.isSelected()).append(System.getProperty("line.separator"));
        }
        if(Settings.disableSafetyFeatures != checkBoxDisableSafety.isSelected()){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.disableSafetyFeatures")).append(" ").append(Settings.disableSafetyFeatures).append(" -> ").append(checkBoxDisableSafety.isSelected()).append(System.getProperty("line.separator"));
        }
        if(!inputFolder.equals(outputFolder)){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.mgt2Folder")).append(" ").append(Settings.mgt2FilePath).append(" -> ").append(outputFolder).append(System.getProperty("line.separator"));
        }
        if(!Settings.language.equals(Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString())){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.language")).append(" ").append(Settings.language).append(" -> ").append(comboBoxLanguage.getSelectedItem().toString()).append(System.getProperty("line.separator"));
        }
        if(!Settings.updateBranch.equals(Objects.requireNonNull(comboBoxUpdateBranch.getSelectedItem()).toString())){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.updateChannel")).append(" ").append(Settings.updateBranch).append(" -> ").append(comboBoxUpdateBranch.getSelectedItem().toString()).append(System.getProperty("line.separator"));
        }
        if(Settings.saveLogs != checkBoxSaveLogs.isSelected()){
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.saveLogs")).append(" ").append(Settings.saveLogs).append(" -> ").append(checkBoxSaveLogs.isSelected()).append(System.getProperty("line.separator"));
        }
        return unsavedChanges.toString();
    }
}
