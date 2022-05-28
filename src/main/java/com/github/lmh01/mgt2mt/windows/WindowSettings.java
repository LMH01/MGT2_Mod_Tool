package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.UpdateBranch;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class WindowSettings extends JFrame {

    static final WindowSettings FRAME = new WindowSettings();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowSettings.class);
    private static boolean customFolderSetAndValid = false;
    private static boolean unsavedChanges = false;
    static Path inputFolder = null;//This string contains the current mgt2folder when the program is started
    private static Path outputFolder = null;//This string contains the folder path that should be set
    private HashMap<SafetyFeature, JCheckBox> safetyFeatureCheckboxes = new HashMap<>();
    final JComboBox<String> comboBoxMGT2FolderOperation = new JComboBox<>();
    final JComboBox<String> comboBoxLanguage = new JComboBox<>();
    final JComboBox<String> comboBoxUpdateChannel = new JComboBox<>();
    final JButton configureSafetyFeatures = new JButton(I18n.INSTANCE.get("window.settings.safetyFeatures.configureButton"));
    final JCheckBox checkBoxExportStorage = new JCheckBox(I18n.INSTANCE.get("window.settings.exportStorage.checkBoxText"));
    final JCheckBox checkBoxSaveLogs = new JCheckBox(I18n.INSTANCE.get("window.settings.checkBox.saveLogs"));
    final AtomicBoolean doNotPerformComboBoxActionListener = new AtomicBoolean(false);

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
                FRAME.loadCurrentSelections();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowSettings() {
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

        checkBoxExportStorage.setBounds(20, 40, 250, 23);
        checkBoxExportStorage.setToolTipText(I18n.INSTANCE.get("window.settings.exportStorage.checkBoxText.toolTip"));
        checkBoxExportStorage.addActionListener(e -> {
            LOGGER.info("checkBoxExportStorage action: " + e.getActionCommand());
            unsavedChanges = checkBoxExportStorage.isSelected() != Settings.enableExportStorage;
        });
        contentPane.add(checkBoxExportStorage);

        checkBoxSaveLogs.setBounds(20, 70, 250, 23);
        checkBoxSaveLogs.setToolTipText(I18n.INSTANCE.get("window.settings.checkBox.saveLogs.toolTip"));
        checkBoxSaveLogs.addActionListener(actionEvent -> unsavedChanges = checkBoxSaveLogs.isSelected() != Settings.saveLogs);
        contentPane.add(checkBoxSaveLogs);

        configureSafetyFeatures.setBounds(20, 100, 250, 23);
        configureSafetyFeatures.setToolTipText(I18n.INSTANCE.get("window.settings.safetyFeatures.configureButton.toolTip"));
        configureSafetyFeatures.addActionListener(e -> {
            HashMap<SafetyFeature, Boolean> safetyFeaturesUnchanged = new HashMap<>();
            for (Map.Entry<SafetyFeature, JCheckBox> entry : safetyFeatureCheckboxes.entrySet()) {
                safetyFeaturesUnchanged.put(entry.getKey(), entry.getValue().isSelected());
            }

            JPanel panel = new JPanel(new GridLayout(0, 1));
            JLabel label = new JLabel(I18n.INSTANCE.get("window.settings.safetyFeatures.configure.label"));
            panel.add(label);
            for (Map.Entry<SafetyFeature, JCheckBox> entry : safetyFeatureCheckboxes.entrySet()) {
                panel.add(entry.getValue());
            }
            panel.add(new JLabel(I18n.INSTANCE.get("window.settings.safetyFeatures.configure.warning")));
            if (JOptionPane.showConfirmDialog(null, panel, I18n.INSTANCE.get("window.settings.safetyFeatures.configure.frame.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                for (Map.Entry<SafetyFeature, Boolean> entry : safetyFeaturesUnchanged.entrySet()) {
                    if (entry.getValue() != safetyFeatureCheckboxes.get(entry.getKey()).isSelected()) {
                        unsavedChanges = true;
                    }
                }
            } else {
                safetyFeatureCheckboxes = getSafetyFeatureCheckboxes();
            }
        });
        contentPane.add(configureSafetyFeatures);

        JLabel labelLanguage = new JLabel(I18n.INSTANCE.get("window.settings.language.label"));
        labelLanguage.setBounds(20, 133, 127, 14);
        contentPane.add(labelLanguage);

        comboBoxLanguage.setBounds(117, 130, 100, 23);
        comboBoxLanguage.setToolTipText(I18n.INSTANCE.get("window.settings.language.comboBox.toolTip"));
        comboBoxLanguage.addActionListener(actionEvent -> {
            if (!Objects.equals(Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString(), Settings.language)) {
                if (Objects.equals(comboBoxLanguage.getSelectedItem().toString(), "Deutsch")) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.language.informationMessage") + System.getProperty("line.separator") + I18n.INSTANCE.get("window.settings.language.informationMessageTranslationIncomplete"));
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.language.informationMessage"));
                }
                unsavedChanges = true;
            } else {
                unsavedChanges = false;
            }
        });
        contentPane.add(comboBoxLanguage);

        JLabel labelUpdateBranch = new JLabel(I18n.INSTANCE.get("window.settings.updateChannel.label"));
        labelUpdateBranch.setBounds(20, 158, 127, 14);
        contentPane.add(labelUpdateBranch);

        comboBoxUpdateChannel.setBounds(117, 155, 100, 23);
        comboBoxUpdateChannel.setToolTipText(I18n.INSTANCE.get("window.settings.updateChannel.toolTip"));
        comboBoxUpdateChannel.addActionListener(actionEvent -> unsavedChanges = !Objects.equals(Objects.requireNonNull(comboBoxUpdateChannel.getSelectedItem()).toString(), Settings.updateBranch.getName()));
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
            if (!doNotPerformComboBoxActionListener.get()) {
                if (Objects.equals(comboBoxMGT2FolderOperation.getSelectedItem(), "Manual") && automaticWasLastSelectedOption.get()) {
                    if (!customFolderSetAndValid) {
                        try {
                            automaticWasLastSelectedOption.set(false);
                            manualWasLastSelectedOption.set(true);
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
                            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                            fileChooser.setDialogTitle(I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.title"));
                            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                            int return_value = fileChooser.showOpenDialog(null);
                            if (return_value == JFileChooser.APPROVE_OPTION) {
                                Path mgt2Folder = fileChooser.getSelectedFile().toPath();
                                if (Settings.validateMGT2Folder(mgt2Folder, false, false)) {
                                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderSet"));
                                    outputFolder = mgt2Folder;
                                    customFolderSetAndValid = true;
                                    automaticWasLastSelectedOption.set(false);
                                    manualWasLastSelectedOption.set(true);
                                    unsavedChanges = true;
                                } else {
                                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderInvalid"), I18n.INSTANCE.get("window.settings.mgt2location.chooseFolder.folderInvalid.title"), JOptionPane.ERROR_MESSAGE);
                                    comboBoxMGT2FolderOperation.setSelectedItem("Automatic");
                                    automaticWasLastSelectedOption.set(true);
                                    manualWasLastSelectedOption.set(false);
                                    customFolderSetAndValid = false;
                                    unsavedChanges = false;
                                }
                            } else {
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
                } else if (comboBoxMGT2FolderOperation.getSelectedItem().equals("Automatic") && manualWasLastSelectedOption.get()) {
                    Path mgt2Folder = Settings.getMGT2FilePath();
                    if (mgt2Folder != null) {
                        outputFolder = mgt2Folder;
                        customFolderSetAndValid = false;
                        unsavedChanges = true;
                        automaticWasLastSelectedOption.set(true);
                        manualWasLastSelectedOption.set(false);
                    } else {
                        if (customFolderSetAndValid) {
                            manualWasLastSelectedOption.set(true);
                            automaticWasLastSelectedOption.set(false);
                            comboBoxMGT2FolderOperation.setSelectedItem("Manual");
                            Settings.validateMGT2Folder(Settings.mgt2Path, false, true);
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
            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.mgt2location.resetFolder"), I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean performTasks = false;
                if ((Settings.getMGT2FilePath() == null) && customFolderSetAndValid) {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.noAutomaticFolderFound"), I18n.INSTANCE.get("window.settings.mgt2location.resetFolder.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        performTasks = true;
                    }
                } else {
                    performTasks = true;
                }
                if (performTasks) {
                    Settings.setMGT2Folder(true);
                    outputFolder = Settings.mgt2Path;
                    Settings.enableCustomFolder = false;
                    Settings.enableExportStorage = true;
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
            if (unsavedChanges) {
                String unsavedChanges = getChangesInSettings(checkBoxExportStorage, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs, safetyFeatureCheckboxes);
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.changesNotSaved.part1") + "\n\n" + unsavedChanges + "\n" + I18n.INSTANCE.get("window.settings.changesNotSaved.part2"), I18n.INSTANCE.get("window.settings.changesNotSaved.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    setCurrentSettings(checkBoxExportStorage, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs, safetyFeatureCheckboxes);
                    WindowMain.checkActionAvailability();
                    customFolderSetAndValid = false;
                    WindowSettings.FRAME.dispose();
                    Backup.createInitialBackup();
                }
            }
            unsavedChanges = false;
            if (Settings.mgt2FolderIsCorrect) {
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
                checkBoxExportStorage.setSelected(false);
                safetyFeatureCheckboxes = getSafetyFeatureCheckboxes();
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
            String unsavedChangesList = getChangesInSettings(checkBoxExportStorage, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs, safetyFeatureCheckboxes);
            if (unsavedChangesList.isEmpty()) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.settings.button.save.nothingToSave"));
            } else {
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("window.settings.button.save.saveSettings") + "\n\n" + unsavedChangesList, I18n.INSTANCE.get("window.settings.button.save.saveSettings.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    setCurrentSettings(checkBoxExportStorage, comboBoxLanguage, comboBoxUpdateChannel, checkBoxSaveLogs, safetyFeatureCheckboxes);
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

    private void loadCurrentSelections() {
        inputFolder = Settings.mgt2Path;
        outputFolder = Settings.mgt2Path;
        doNotPerformComboBoxActionListener.set(false);
        if (Settings.enableCustomFolder && Settings.mgt2FolderIsCorrect) {
            comboBoxMGT2FolderOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Manual", "Automatic"}));
        } else {
            comboBoxMGT2FolderOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Automatic", "Manual"}));
        }
        if (Settings.language.equals("English")) {
            comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[]{"English", "Deutsch"}));
        } else if (Settings.language.equals("Deutsch")) {
            comboBoxLanguage.setModel(new DefaultComboBoxModel<>(new String[]{"Deutsch", "English"}));
        }
        if (Settings.updateBranch.equals(UpdateBranch.RELEASE)) {
            comboBoxUpdateChannel.setModel(new DefaultComboBoxModel<>(new String[]{UpdateBranch.RELEASE.getName(), UpdateBranch.ALPHA.getName()}));
        } else {
            comboBoxUpdateChannel.setModel(new DefaultComboBoxModel<>(new String[]{UpdateBranch.ALPHA.getName(), UpdateBranch.RELEASE.getName()}));
        }
        if (Utils.isAlpha()) {
            comboBoxUpdateChannel.setModel(new DefaultComboBoxModel<>(new String[]{"Alpha", "Release"}));
            comboBoxUpdateChannel.setEnabled(false);
            comboBoxUpdateChannel.setToolTipText(I18n.INSTANCE.get("window.settings.updateChannel.disabled"));
        }
        checkBoxExportStorage.setSelected(Settings.enableExportStorage);
        checkBoxSaveLogs.setSelected(Settings.saveLogs);
        comboBoxMGT2FolderOperation.setToolTipText(I18n.INSTANCE.get("window.settings.mgt2location.toolTip") + " " + Settings.mgt2Path);
        if (Settings.mgt2FolderIsCorrect) {
            configureSafetyFeatures.setEnabled(true);
        } else {
            comboBoxMGT2FolderOperation.setToolTipText(I18n.INSTANCE.get("window.settings.mgt2location.toolTip") + " " + I18n.INSTANCE.get("window.settings.mgt2location.toolTip.notSet"));
        }
        customFolderSetAndValid = Settings.enableCustomFolder;
        // Load current safety features into check boxes
        safetyFeatureCheckboxes = getSafetyFeatureCheckboxes();
    }

    /**
     * Loads the current safety feature settings into a map of checkboxes
     */
    private static HashMap<SafetyFeature, JCheckBox> getSafetyFeatureCheckboxes() {
        HashMap<SafetyFeature, JCheckBox> map = new HashMap<>();
        for (Map.Entry<SafetyFeature, Boolean> entry : Settings.safetyFeatures.entrySet()) {
            JCheckBox current = new JCheckBox(entry.getKey().getName());
            current.setToolTipText(entry.getKey().getToolTip());
            current.setSelected(entry.getValue());
            map.put(entry.getKey(), current);
        }
        return map;
    }

    /**
     * Applies the local changes in the settings to the global settings by calling Settings.setSettings(...)
     */
    private static void setCurrentSettings(JCheckBox checkBoxExportStorage, JComboBox<String> comboBoxLanguage, JComboBox<String> comboBoxUpdateBranch, JCheckBox checkBoxSaveLogs, Map<SafetyFeature, JCheckBox> safetyFeatureCheckboxes) {
        Map<SafetyFeature, Boolean> safetyFeatures = new HashMap<>();
        for (Map.Entry<SafetyFeature, JCheckBox> entry : safetyFeatureCheckboxes.entrySet()) {
            safetyFeatures.put(entry.getKey(), entry.getValue().isSelected());
        }
        Settings.setSettings(true, checkBoxExportStorage.isSelected(), customFolderSetAndValid, outputFolder, Settings.enableDisclaimerMessage, Settings.enableGenreNameTranslationInfo, Settings.enableGenreDescriptionTranslationInfo, Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString(), UpdateBranch.getUpdateBranch(Objects.requireNonNull(comboBoxUpdateBranch.getSelectedItem()).toString()), checkBoxSaveLogs.isSelected(), Settings.enableInitialBackupCheck, safetyFeatures);
    }

    /**
     * @param checkBoxExportStorage The debug mode checkbox
     * @return Returns the changes that have been made to the settings
     */
    private static String getChangesInSettings(JCheckBox checkBoxExportStorage, JComboBox<String> comboBoxLanguage, JComboBox<String> comboBoxUpdateBranch, JCheckBox checkBoxSaveLogs, Map<SafetyFeature, JCheckBox> safetyFeatureCheckboxes) {
        StringBuilder unsavedChanges = new StringBuilder();
        if (Settings.enableExportStorage != checkBoxExportStorage.isSelected()) {
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.exportStorage")).append(" ").append(Utils.getTranslatedValueFromBoolean(Settings.enableExportStorage)).append(" -> ").append(Utils.getTranslatedValueFromBoolean(checkBoxExportStorage.isSelected())).append(System.getProperty("line.separator"));
        }
        if (!inputFolder.equals(outputFolder)) {
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.mgt2Folder")).append(" ").append(Settings.mgt2Path).append(" -> ").append(outputFolder).append(System.getProperty("line.separator"));
        }
        if (!Settings.language.equals(Objects.requireNonNull(comboBoxLanguage.getSelectedItem()).toString())) {
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.language")).append(" ").append(Settings.language).append(" -> ").append(comboBoxLanguage.getSelectedItem().toString()).append(System.getProperty("line.separator"));
        }
        if (!Settings.updateBranch.equals(UpdateBranch.getUpdateBranch(Objects.requireNonNull(comboBoxUpdateBranch.getSelectedItem()).toString()))) {
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.updateChannel")).append(" ").append(Settings.updateBranch.getName()).append(" -> ").append(comboBoxUpdateBranch.getSelectedItem().toString()).append(System.getProperty("line.separator"));
        }
        if (Settings.saveLogs != checkBoxSaveLogs.isSelected()) {
            unsavedChanges.append(I18n.INSTANCE.get("window.settings.changesInSettings.saveLogs")).append(" ").append(Utils.getTranslatedValueFromBoolean(Settings.saveLogs)).append(" -> ").append(Utils.getTranslatedValueFromBoolean(checkBoxSaveLogs.isSelected())).append(System.getProperty("line.separator"));
        }
        boolean firstChangedSafetyFeature = true;
        for (Map.Entry<SafetyFeature, Boolean> entry : Settings.safetyFeatures.entrySet()) {
            if (entry.getValue() != safetyFeatureCheckboxes.get(entry.getKey()).isSelected()) {
                if (firstChangedSafetyFeature) {
                    unsavedChanges.append("\n").append(I18n.INSTANCE.get("window.settings.changesInSettings.safetyFeatures")).append(": ").append(System.getProperty("line.separator"));
                    firstChangedSafetyFeature = false;
                }
                unsavedChanges.append(entry.getKey().getName()).append(": ").append(Utils.getTranslatedValueFromBoolean(entry.getValue())).append(" -> ").append(Utils.getTranslatedValueFromBoolean(safetyFeatureCheckboxes.get(entry.getKey()).isSelected())).append(System.getProperty("line.separator"));
            }
        }
        return unsavedChanges.toString();
    }
}
