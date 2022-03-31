package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlatformMod extends AbstractComplexMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("MANUFACTURER " + string)) {
                    bw.write("[MANUFACTURER " + string + "]" + entry.getValue() + "\r\n");
                }
            }
        }
        EditHelper.printLine("DATE", map, bw);
        if (map.containsKey("DATE END")) {
            EditHelper.printLine("DATE END", map, bw);
        }
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        EditHelper.printLine("UNITS", map, bw);
        Map<Integer, String> pictures = new HashMap<>();
        ArrayList<String> pictureChangeYears = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("PIC") && !entry.getKey().contains("YEAR")) {
                pictures.put(Integer.parseInt(entry.getKey().replaceAll("[^0-9]", "")), entry.getValue());
            }
            if (entry.getKey().contains("YEAR")) {
                pictureChangeYears.add("[" + entry.getKey() + "]" + entry.getValue());
            }
        }
        if (map.containsKey("PIC-1")) {
            for (Map.Entry<Integer, String> entry : pictures.entrySet()) {
                bw.write("[PIC-" + entry.getKey() + "]" + entry.getValue());
                bw.write("\r\n");
            }
        } else {
            for (int i = 1; i <= pictureChangeYears.size() + 1; i++) {
                bw.write("[PIC-" + i + "]" + map.get("NAME EN").replaceAll("[0-9]", "").replaceAll("\\s+", "") + "-" + i + ".png");
                bw.write("\r\n");
            }
        }
        Collections.sort(pictureChangeYears);
        for (String pictureChangeYear : pictureChangeYears) {
            bw.write(pictureChangeYear);
            bw.write("\r\n");
        }
        try {
            ArrayList<Integer> gameplayFeatureIds = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().contains("NEED")) {
                    gameplayFeatureIds.add(Integer.parseInt(entry.getValue()));
                }
            }
            int numberOfRunsB = 1;
            for (Integer integer : gameplayFeatureIds) {
                bw.write("[NEED-" + numberOfRunsB + "]" + integer);
                bw.write("\r\n");
                numberOfRunsB++;
            }
        } catch (NumberFormatException e) {
            ArrayList<String> gameplayFeatureNames = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().contains("NEED")) {
                    gameplayFeatureNames.add(entry.getValue());
                }
            }
            int numberOfRunsB = 1;
            for (String string : gameplayFeatureNames) {
                bw.write("[NEED-" + numberOfRunsB + "]" + string);
                bw.write("\r\n");
                numberOfRunsB++;
            }
        }
        EditHelper.printLine("COMPLEX", map, bw);
        EditHelper.printLine("INTERNET", map, bw);
        EditHelper.printLine("TYP", map, bw);
        if (map.containsKey("STARTPLATFORM")) {
            EditHelper.printLine("STARTPLATFORM", map, bw);
        }
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", MadGamesTycoon2ModTool.VERSION};
    }

    @Override
    public String getMainTranslationKey() {
        return "platform";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.platformMod;
    }

    @Override
    public String getExportType() {
        return "platform";
    }

    @Override
    public String getGameFileName() {
        return "Platforms.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_platforms.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        try {
            analyzeFile();
            ModManager.genreMod.analyzeFile();
            ModManager.gameplayFeatureMod.analyzeFile();
            final Map<String, String>[] mapManufacturerTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean manufacturerTranslationsAdded = new AtomicBoolean(false);
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JTextField textFieldManufacturer = new JTextField(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldManufacturer.initialValue"));
            JButton buttonAddManufacturerTranslation = WindowHelper.getAddTranslationsButton(mapManufacturerTranslations, manufacturerTranslationsAdded, 2);
            JComboBox<String> comboBoxFeatureType = WindowHelper.getComboBox(PlatformType.class, "mod.platform.addPlatform.components.comboBox.type.toolTip", PlatformType.COMPUTER.getTypeName());

            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();

            JSpinner spinnerEndYear = new JSpinner();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            spinnerUnlockYear.addChangeListener(e -> setEndYearSpinner(spinnerUnlockYear, spinnerEndYear));

            JComboBox<String> comboBoxEndDateMonth = WindowHelper.getUnlockMonthComboBox();
            comboBoxEndDateMonth.setToolTipText(null);
            comboBoxEndDateMonth.setEnabled(false);

            JPanel panelEndDate = new JPanel();

            JLabel labelEndDate = new JLabel(I18n.INSTANCE.get("mod.platform.addPlatform.components.label.endDate") + ":");
            spinnerEndYear.setEnabled(false);
            setEndYearSpinner(spinnerUnlockYear, spinnerEndYear);
            panelEndDate.add(labelEndDate);
            panelEndDate.add(comboBoxEndDateMonth);
            panelEndDate.add(spinnerEndYear);
            JCheckBox checkBoxEnableEndDate = new JCheckBox(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.enableEndDate"));
            checkBoxEnableEndDate.setSelected(false);
            checkBoxEnableEndDate.addChangeListener(changeListener -> {
                if (checkBoxEnableEndDate.isSelected()) {
                    comboBoxEndDateMonth.setEnabled(true);
                    spinnerEndYear.setEnabled(true);
                } else {
                    comboBoxEndDateMonth.setEnabled(false);
                    spinnerEndYear.setEnabled(false);
                }
            });

            JSpinner spinnerTechLevel = WindowHelper.getTechLevelSpinner();
            JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();
            JSpinner spinnerDevKitCost = WindowHelper.getDevKitCostSpinner();

            JCheckBox checkBoxInternet = new JCheckBox(I18n.INSTANCE.get("commonText.internet"));
            checkBoxInternet.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.internet.toolTip"));

            JSpinner spinnerComplexity = WindowHelper.getComplexitySpinner();
            JSpinner spinnerUnits = WindowHelper.getUnitsSpinner();

            JLabel labelGameplayFeatureList = new JLabel("<html>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.selectGameplayFeatures") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
            JList<String> listAvailableGameplayFeatures = WindowHelper.getList(ModManager.gameplayFeatureMod.getContentByAlphabet(), true);
            listAvailableGameplayFeatures.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        listAvailableGameplayFeatures.clearSelection();
                    }
                }
            });
            JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGameplayFeatures);

            AtomicBoolean picturesAdded = new AtomicBoolean(false);
            Map<Integer, File> pictureMap = new HashMap<>();//This map contains all the pictures that should be added for the platform
            JButton buttonAddPictures = new JButton(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"));
            buttonAddPictures.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.toolTip"));
            buttonAddPictures.addActionListener(actionEvent -> {
                boolean continueWithMessage = false;
                if (picturesAdded.get()) {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.pictureAlreadyAdded"), I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.pictureAlreadyAdded.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        continueWithMessage = true;
                        picturesAdded.set(false);
                        pictureMap.clear();
                    }
                } else {
                    continueWithMessage = true;
                }
                if (continueWithMessage) {
                    JTextArea textAreaAddedImages = new JTextArea();
                    textAreaAddedImages.setPreferredSize(new Dimension(315, 140));
                    JLabel labelTextAreaExplanation = new JLabel(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.label.textAreaExplanation"));
                    JScrollPane scrollPane = new JScrollPane(textAreaAddedImages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    JButton buttonAddPicture = new JButton(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture"));
                    AtomicInteger lastYear = new AtomicInteger(1975);
                    final boolean[] firstImage = {true};
                    buttonAddPicture.addActionListener(actionEvent2 -> {
                        boolean continueWithPictures = false;
                        if (pictureMap.size() < 2 || Settings.disableSafetyFeatures) {
                            continueWithPictures = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("frame.title.unableToContinue") + ":<br><br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.maxPicturesSelectedMessage"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                        }
                        if (continueWithPictures) {
                            JButton buttonSelectImage = new JButton(I18n.INSTANCE.get("commonText.selectImage"));
                            AtomicReference<File> imageFile = new AtomicReference<>();
                            JPanel panelChangeDate = new JPanel();
                            JLabel labelChangeDate = new JLabel(I18n.INSTANCE.get(""));
                            JComboBox<String> comboBoxChangeMonth = new JComboBox<>();
                            comboBoxChangeMonth.setEnabled(false);
                            comboBoxChangeMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
                            comboBoxChangeMonth.setSelectedItem("JAN");
                            JSpinner spinnerChangeYear = new JSpinner();
                            spinnerChangeYear.setEnabled(false);
                            if (Settings.disableSafetyFeatures) {
                                spinnerChangeYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture.actionListener.spinnerChangeYear.toolTip"));
                                spinnerChangeYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                                ((JSpinner.DefaultEditor) spinnerEndYear.getEditor()).getTextField().setEditable(true);
                            } else {
                                spinnerChangeYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": " + lastYear.get() + 1 + " - 2050]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture.actionListener.spinnerChangeYear.toolTip"));
                                spinnerChangeYear.setModel(new SpinnerNumberModel(lastYear.get() + 1, lastYear.get() + 1, 2050, 1));
                                ((JSpinner.DefaultEditor) spinnerEndYear.getEditor()).getTextField().setEditable(false);
                            }
                            panelChangeDate.add(labelChangeDate);
                            panelChangeDate.add(comboBoxChangeMonth);
                            panelChangeDate.add(spinnerChangeYear);
                            buttonSelectImage.addActionListener(actionEvent3 -> {
                                try {
                                    File newImageFile = Utils.getImagePath().toFile();
                                    imageFile.set(newImageFile);
                                    if (newImageFile.exists()) {
                                        buttonSelectImage.setText(I18n.INSTANCE.get("commonText.imageSelected"));
                                    } else {
                                        buttonSelectImage.setText(I18n.INSTANCE.get("commonText.selectImage"));
                                    }
                                } catch (ModProcessingException ignored) {

                                }
                            });
                            if (!firstImage[0]) {
                                comboBoxChangeMonth.setEnabled(true);
                                spinnerChangeYear.setEnabled(true);
                            }
                            Object[] params = {buttonSelectImage, panelChangeDate};
                            while (true) {
                                if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.actionListener.button.addPicture"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                                    if (imageFile.get() != null) {
                                        if (firstImage[0]) {
                                            firstImage[0] = false;
                                        } else {
                                            comboBoxChangeMonth.setEnabled(true);
                                            spinnerChangeYear.setEnabled(true);
                                            textAreaAddedImages.append("\r\n");
                                        }
                                        textAreaAddedImages.append(Objects.requireNonNull(comboBoxChangeMonth.getSelectedItem()) + " " + spinnerChangeYear.getValue().toString() + " - " + imageFile.get().getPath());
                                        pictureMap.put(Integer.parseInt(spinnerChangeYear.getValue().toString()), imageFile.get());
                                        lastYear.set(Integer.parseInt(spinnerChangeYear.getValue().toString()));
                                        break;
                                    } else {
                                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.noPictureSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    });
                    Object[] params = {labelTextAreaExplanation, scrollPane, buttonAddPicture};
                    if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION && pictureMap.size() > 0) {
                        picturesAdded.set(true);
                        buttonAddPictures.setText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.picturesSelected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture.picturesSet"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        picturesAdded.set(false);
                        buttonAddPictures.setText(I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addPicture"));
                        pictureMap.clear();
                    }
                }
            });

            JCheckBox checkBoxStartplatform = new JCheckBox(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.startplatform"));
            checkBoxStartplatform.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.checkBox.startplatform.toolTip"));

            Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getManufacturerPanel(textFieldManufacturer), buttonAddManufacturerTranslation, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), checkBoxEnableEndDate, panelEndDate, WindowHelper.getSpinnerPanel(spinnerTechLevel, SpinnerType.TECH_LEVEL), WindowHelper.getSpinnerPanel(spinnerComplexity, SpinnerType.COMPLEXITY), WindowHelper.getSpinnerPanel(spinnerUnits, SpinnerType.UNITS), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerDevKitCost, SpinnerType.PRICE), checkBoxInternet, checkBoxStartplatform, labelGameplayFeatureList, scrollPaneAvailableGenres, buttonAddPictures};
            while (true) {
                if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.platform.addPlatform.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldName.initialValue"))) {
                        if (!textFieldManufacturer.getText().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.textFieldManufacturer.initialValue"))) {
                            boolean modAlreadyExists = false;
                            for (String string : getContentByAlphabet()) {
                                if (textFieldName.getText().equals(string)) {
                                    modAlreadyExists = true;
                                }
                            }
                            if (!modAlreadyExists) {
                                Map<String, String> platformMap = new HashMap<>();
                                Map<Integer, File> finalPictureMap = new HashMap<>();
                                platformMap.put("ID", Integer.toString(getFreeId()));
                                if (!nameTranslationsAdded.get() && !manufacturerTranslationsAdded.get()) {
                                    platformMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                    platformMap.putAll(TranslationManager.getDefaultManufacturerTranslations(textFieldManufacturer.getText()));
                                } else if (!nameTranslationsAdded.get() && manufacturerTranslationsAdded.get()) {
                                    platformMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapManufacturerTranslations[0], "MANUFACTURER"));
                                } else if (nameTranslationsAdded.get() && !manufacturerTranslationsAdded.get()) {
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                    platformMap.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldManufacturer.getText()));
                                } else {
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                    platformMap.putAll(TranslationManager.transformTranslationMap(mapManufacturerTranslations[0], "MANUFACTURER"));
                                    platformMap.put("NAME EN", textFieldName.getText());
                                    platformMap.put("MANUFACTURER EN", textFieldManufacturer.getText());
                                }
                                platformMap.put("DATE", Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString());
                                if (checkBoxEnableEndDate.isSelected()) {
                                    platformMap.put("DATE END", Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxEndDateMonth.getSelectedItem()).toString()) + " " + spinnerEndYear.getValue().toString());
                                }
                                platformMap.put("PRICE", spinnerDevKitCost.getValue().toString());
                                platformMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                                platformMap.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                                platformMap.put("UNITS", spinnerUnits.getValue().toString());
                                if (pictureMap.isEmpty()) {
                                    for (PlatformType platformType : PlatformType.values()) {
                                        if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(platformType.getTypeName())) {
                                            platformMap.put("PIC-1", platformType.getDefaultImage());
                                        }
                                    }
                                } else {
                                    ArrayList<Integer> pictureYears = new ArrayList<>();
                                    for (Map.Entry<Integer, File> entry : pictureMap.entrySet()) {
                                        pictureYears.add(entry.getKey());
                                    }
                                    int pictureNumber = 1;
                                    for (Integer integer : pictureYears) {
                                        finalPictureMap.put(pictureNumber, pictureMap.get(integer));
                                        if (pictureNumber > 1) {
                                            platformMap.put("PIC-" + pictureNumber + " YEAR", Integer.toString(integer));
                                        }
                                        pictureNumber++;
                                    }
                                }
                                if (listAvailableGameplayFeatures.getSelectedValuesList().isEmpty()) {
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"))) {
                                        platformMap.put("NEED-1", "44");
                                    }
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"))) {
                                        platformMap.put("NEED-1", "45");
                                    }
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"))) {
                                        platformMap.put("NEED-1", "45");
                                        platformMap.put("NEED-2", "56");
                                    }
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"))) {
                                        platformMap.put("NEED-1", "56");
                                    }
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard"))) {
                                        platformMap.put("NEED-1", "59");
                                    }
                                } else {
                                    int currentNeededGameplayFeatureNumber = 1;
                                    for (String string : listAvailableGameplayFeatures.getSelectedValuesList()) {
                                        platformMap.put("NEED-" + currentNeededGameplayFeatureNumber, Integer.toString(ModManager.gameplayFeatureMod.getContentIdByName(string)));
                                        currentNeededGameplayFeatureNumber++;
                                    }
                                }
                                platformMap.put("COMPLEX", spinnerComplexity.getValue().toString());
                                if (checkBoxInternet.isSelected()) {
                                    platformMap.put("INTERNET", "1");
                                } else {
                                    platformMap.put("INTERNET", "0");
                                }
                                for (PlatformType platformType : PlatformType.values()) {
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(platformType.getTypeName())) {
                                        platformMap.put("TYP", Integer.toString(platformType.getId()));
                                    }
                                }
                                if (checkBoxStartplatform.isSelected()) {
                                    platformMap.put("STARTPLATFORM", "");
                                }
                                if (JOptionPane.showConfirmDialog(null, getOptionPaneMessage(platformMap), I18n.INSTANCE.get(""), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    createBackup();
                                    addModToFile(platformMap);
                                    addImageFiles(platformMap.get("NAME EN"), finalPictureMap);
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.platform.upperCase") + " - " + platformMap.get("NAME EN"));
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.platform.upperCase") + ": [" + platformMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.platform.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                    break;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.platform.addPlatform.manufacturer.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("commonText.unableToAdd") + getType() + " - " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
        }
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        StringBuilder message = new StringBuilder();
        message.append("<html>");
        message.append(I18n.INSTANCE.get("mod.platform.addPlatform.optionPaneMessage.firstPart")).append("<br><br>");
        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(map.get("NAME EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.manufacturer")).append(": ").append(map.get("MANUFACTURER EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.releaseDate")).append(": ").append(map.get("DATE")).append("<br>");
        if (map.containsKey("DATE END")) {
            message.append(I18n.INSTANCE.get("commonText.productionEnd")).append(": ").append(map.get("DATE END")).append("<br>");
        }
        message.append(I18n.INSTANCE.get("commonText.devKitCost")).append(": ").append(map.get("PRICE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.developmentCost")).append(": ").append(map.get("DEV COSTS")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.techLevel")).append(": ").append(map.get("TECHLEVEL")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.units")).append(": ").append(map.get("UNITS")).append("<br>");
        ArrayList<Integer> gameplayFeatureIds = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                gameplayFeatureIds.add(Integer.parseInt(entry.getValue()));
            }
        }
        StringBuilder neededGameplayFeatures = new StringBuilder();
        int currentGameplayFeature = 0;
        boolean firstGameplayFeature = true;
        for (Integer integer : gameplayFeatureIds) {
            if (firstGameplayFeature) {
                firstGameplayFeature = false;
            } else {
                neededGameplayFeatures.append(", ");
            }
            if (currentGameplayFeature == 8) {
                neededGameplayFeatures.append("<br>");
                currentGameplayFeature = 0;
            }
            neededGameplayFeatures.append(ModManager.gameplayFeatureMod.getContentNameById(integer));
            currentGameplayFeature++;
        }
        message.append(I18n.INSTANCE.get("commonText.neededGameplayFeatures")).append(": ").append(neededGameplayFeatures).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.complexity")).append(": ").append(map.get("COMPLEX")).append("<br>");
        String internetMessageToPrint;
        if (map.get("INTERNET").equals("0")) {
            internetMessageToPrint = Utils.getTranslatedValueFromBoolean(false);
        } else {
            internetMessageToPrint = Utils.getTranslatedValueFromBoolean(true);
        }
        message.append(I18n.INSTANCE.get("commonText.internet")).append(": ").append(internetMessageToPrint).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.type")).append(": ").append(PlatformType.getTypeNameById(Integer.parseInt(map.get("TYP")))).append("<br>");
        String startplatformMessageToPrint;
        if (map.containsKey("STARTPLATFORM")) {
            startplatformMessageToPrint = Utils.getTranslatedValueFromBoolean(true);
        } else {
            startplatformMessageToPrint = Utils.getTranslatedValueFromBoolean(false);
        }
        message.append(I18n.INSTANCE.get("commonText.startplatform")).append(": ").append(startplatformMessageToPrint).append("<br>");
        return message.toString();
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                replaceMapEntry(map, missingDependency, replacement, entry.getKey());
            }
        }
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.gameplayFeatureMod);
        return arrayList;
    }

    @Override
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                map.replace(entry.getKey(), ModManager.gameplayFeatureMod.getContentNameById(Integer.parseInt(entry.getValue())));
            }
        }
        return map;
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        Map<String, String> modMap = transformGenericToMap(t);
        Map<String, Object> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, String> entry : modMap.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                set.add(entry.getValue());
            }
        }
        map.put(ModManager.gameplayFeatureMod.getExportType(), set);
        return map;
    }

    @Override
    public Map<String, String> importImages(Map<String, String> map) throws ModProcessingException {
        Map<String, String> imageMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("pic_")) {
                try {
                    importImage(map, entry.getKey(), MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue()));
                    imageMap.remove("PIC-" + entry.getKey().replaceAll("[^0-9]", ""));
                    imageMap.put("PIC-" + entry.getKey().replaceAll("[^0-9]", ""), entry.getValue());
                } catch (IOException e) {
                    throw new ModProcessingException("Platform image files could not be copied", e);
                }
            }
        }
        return imageMap;
    }

    @Override
    public void removeImageFiles(String name) throws ModProcessingException {
        for (Map.Entry<String, String> entry : getSingleContentMapByName(name).entrySet()) {
            if (entry.getKey().contains("PIC") && !entry.getKey().contains("YEAR")) {
                boolean deleteImage = true;
                for (PlatformType platformType : PlatformType.values()) {
                    if (entry.getValue().equals(platformType.getDefaultImage())) {
                        deleteImage = false;
                        break;
                    }
                }
                if (deleteImage) {
                    Path path = MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue());
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("frame.title.warning") + ": " + I18n.INSTANCE.get("mod.platform.removeImageFiles.failed"));
                    }
                }
            }
        }
    }

    @Override
    public Map<String, String> exportImages(String name, Path assetsFolder) throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : getSingleContentMapByName(name).entrySet()) {
            if (entry.getKey().contains("PIC") && !entry.getKey().contains("YEAR")) {
                String imageName = Utils.convertName(getType()) + "_" + Utils.convertName(name) + "_icon_" + entry.getKey().replaceAll("[^0-9]", "") + ".png";
                map.put("pic_" + entry.getKey().replaceAll("[^0-9]", ""), imageName);
                File outputFile = assetsFolder.resolve(imageName).toFile();
                if (!outputFile.exists()) {
                    try {
                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.exportingImage") + ": " + getType() + " - " + imageName);
                        Files.copy(MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue()), outputFile.toPath());
                    } catch (IOException e) {
                        throw new ModProcessingException("Platform image files could not be copied", e);
                    }
                } else {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.imageNotExported") + ": " + MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue()));
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                replaceImportMapEntry(map, entry.getKey(), ModManager.gameplayFeatureMod);
            }
        }
        return map;
    }

    private void setEndYearSpinner(JSpinner spinnerUnlockYear, JSpinner spinnerEndYear) {
        if (Settings.disableSafetyFeatures) {
            spinnerEndYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.spinner.endYear.toolTip"));
            spinnerEndYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
            ((JSpinner.DefaultEditor) spinnerEndYear.getEditor()).getTextField().setEditable(true);
        } else {
            spinnerEndYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": " + Integer.parseInt(spinnerUnlockYear.getValue().toString()) + 1 + " - 2050]<br>" + I18n.INSTANCE.get("mod.platform.addPlatform.components.spinner.endYear.toolTip"));
            spinnerEndYear.setModel(new SpinnerNumberModel(Integer.parseInt(spinnerUnlockYear.getValue().toString()) + 1, Integer.parseInt(spinnerUnlockYear.getValue().toString()) + 1, 2050, 1));
            ((JSpinner.DefaultEditor) spinnerEndYear.getEditor()).getTextField().setEditable(false);
        }
    }

    /**
     * Adds the image files for the new platform
     *
     * @param platformName The name of the new platform
     * @param imageFiles   The map contains the image files in the following formatting:
     *                     Integer - The position of the image file
     *                     File - The source file that should be added
     */
    public void addImageFiles(String platformName, Map<Integer, File> imageFiles) throws IOException {
        for (Map.Entry<Integer, File> entry : imageFiles.entrySet()) {
            File destinationFile = MGT2Paths.PLATFORM_ICONS.getPath().resolve(platformName.replaceAll("[0-9]", "").replaceAll("\\s+", "") + "-" + entry.getKey() + ".png").toFile();
            Files.copy(Paths.get(entry.getValue().getPath()), Paths.get(destinationFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}