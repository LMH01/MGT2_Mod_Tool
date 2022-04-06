package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.Platform;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.managed.types.PlatformType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlatformManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final PlatformManager INSTANCE = new PlatformManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private PlatformManager() {
        super("platform", "platform", "default_platforms.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Platforms.txt").toFile(), StandardCharsets.UTF_8);
    }

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
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        String endDate = null;
        if (map.containsKey("DATE END")) {
            endDate = map.get("DATE END");
        }
        ArrayList<PlatformImage> platformImages = new ArrayList<>();
        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            // Add required gameplay features
            if (entry.getKey().contains("NEED-")) {
                requiredGameplayFeatures.add(Integer.parseInt(entry.getValue()));
            }
            // Add platform images
            if (entry.getKey().contains("PIC-") && !entry.getKey().contains("YEAR")) {
                int id = Integer.parseInt(entry.getKey().replaceAll("[^0-9]", ""));
                Integer date = null;
                if (id != 1) {
                    date = Integer.parseInt(map.get(entry.getKey() + " YEAR"));
                }
                platformImages.add(new PlatformImage(
                        id,
                        date,
                        new Image(null, MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue()).toFile())));
            }
        }
        boolean hasInternet = false;
        if (map.get("INTERNET").equals("1")) {
            hasInternet = true;
        }
        Map<String, String> manufacturerTranslations = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("MANUFACTURER " + string)) {
                    manufacturerTranslations.put("MANUFACTURER " + string, entry.getValue());
                }
            }
        }
        return new Platform(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("MANUFACTURER EN"),
                manufacturerTranslations,
                map.get("DATE"),
                endDate,
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("TECHLEVEL")),
                Integer.parseInt(map.get("UNITS")),
                platformImages,
                requiredGameplayFeatures,
                Integer.parseInt(map.get("COMPLEX")),
                hasInternet,
                PlatformType.getFromId(Integer.parseInt(map.get("TYP"))),
                map.containsKey("STARTPLATFORM")
                );
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Map<String, String> transformedMap = Utils.transformObjectMapToStringMap(map);
        String endDate = null;
        if (transformedMap.containsKey("DATE END")) {
            endDate = transformedMap.get("DATE END");
        }
        ArrayList<PlatformImage> platformImages = new ArrayList<>();
        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
        for (Map.Entry<String, String> entry : transformedMap.entrySet()) {
            // Add required gameplay features
            if (entry.getKey().contains("NEED-")) {
                requiredGameplayFeatures.add(GameplayFeatureManager.INSTANCE.getImportHelperMap().getContentIdByName(entry.getValue()));
            }
            // Add platform images
            if (entry.getKey().contains("platform_image_")) {
                int id = Integer.parseInt(entry.getKey().replaceAll("[^0-9]", ""));
                Integer date = null;
                if (id != 1) {
                    date = Integer.parseInt(transformedMap.get("PIC- " + id + " YEAR"));
                }
                platformImages.add(new PlatformImage(
                        id,
                        date,
                        new Image(assetsFolder.resolve(entry.getValue()).toFile(), MGT2Paths.PLATFORM_ICONS.getPath().resolve(entry.getValue()).toFile())));
            }
        }
        boolean hasInternet = false;
        if (transformedMap.get("INTERNET").equals("1")) {
            hasInternet = true;
        }
        Map<String, String> manufacturerTranslations = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            for (Map.Entry<String, String> entry : transformedMap.entrySet()) {
                if (entry.getKey().equals("MANUFACTURER " + string)) {
                    manufacturerTranslations.put("MANUFACTURER " + string, entry.getValue());
                }
            }
        }
        return new Platform(
                transformedMap.get("NAME EN"),
                getIdFromMap(transformedMap),
                new TranslationManager(transformedMap),
                transformedMap.get("MANUFACTURER EN"),
                manufacturerTranslations,
                transformedMap.get("DATE"),
                endDate,
                Integer.parseInt(transformedMap.get("PRICE")),
                Integer.parseInt(transformedMap.get("DEV COSTS")),
                Integer.parseInt(transformedMap.get("TECHLEVEL")),
                Integer.parseInt(transformedMap.get("UNITS")),
                platformImages,
                requiredGameplayFeatures,
                Integer.parseInt(transformedMap.get("COMPLEX")),
                hasInternet,
                PlatformType.getFromId(Integer.parseInt(transformedMap.get("TYP"))),
                transformedMap.containsKey("STARTPLATFORM")
        );
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        analyzeFile();
        GenreManager.INSTANCE.analyzeFile();
        GameplayFeatureManager.INSTANCE.analyzeFile();
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
        JList<String> listAvailableGameplayFeatures = WindowHelper.getList(GameplayFeatureManager.INSTANCE.getContentByAlphabet(), true);
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
                            // Setup end date
                            String endDate = null;
                            if (checkBoxEnableEndDate.isSelected()) {
                                endDate = Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxEndDateMonth.getSelectedItem()).toString()) + " " + spinnerEndYear.getValue().toString();
                            }

                            // Setup platform images
                            ArrayList<PlatformImage> platformImages = new ArrayList<>();
                            if (pictureMap.isEmpty()) {
                                // User did not select custom image
                                for (PlatformType platformType : PlatformType.values()) {
                                    if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(platformType.getTypeName())) {
                                        platformImages.add(new PlatformImage(1, 1976, new Image(MGT2Paths.PLATFORM_ICONS.getPath().resolve(platformType.getDefaultImage()).toFile(), MGT2Paths.PLATFORM_ICONS.getPath().resolve(textFieldName.getText() + "_pic_" + 1 + ".png").toFile())));
                                    }
                                }
                            } else {
                                // User added custom images
                                ArrayList<Integer> pictureYears = new ArrayList<>();
                                for (Map.Entry<Integer, File> entry : pictureMap.entrySet()) {
                                    pictureYears.add(entry.getKey());
                                }
                                Collections.sort(pictureYears);
                                int pictureNumber = 1;
                                for (Integer integer : pictureYears) {
                                    platformImages.add(new PlatformImage(pictureNumber, integer, new Image(pictureMap.get(integer), MGT2Paths.PLATFORM_ICONS.getPath().resolve(textFieldName.getText() + "_pic_" + pictureNumber + ".png").toFile())));
                                    pictureNumber++;
                                }
                            }


                            // Setup required gameplay features
                            ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
                            if (listAvailableGameplayFeatures.getSelectedValuesList().isEmpty()) {
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"))) {
                                    requiredGameplayFeatures.add(44);
                                }
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"))) {
                                    requiredGameplayFeatures.add(45);
                                }
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"))) {
                                    requiredGameplayFeatures.add(45);
                                    requiredGameplayFeatures.add(56);
                                }
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"))) {
                                    requiredGameplayFeatures.add(56);
                                }
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard"))) {
                                    requiredGameplayFeatures.add(59);
                                }
                            } else {
                                int currentNeededGameplayFeatureNumber = 1;
                                for (String string : listAvailableGameplayFeatures.getSelectedValuesList()) {
                                    requiredGameplayFeatures.add(GameplayFeatureManager.INSTANCE.getContentIdByName(string));
                                    currentNeededGameplayFeatureNumber++;
                                }
                            }

                            // Setup platform type
                            PlatformType pT = null;
                            for (PlatformType platformType : PlatformType.values()) {
                                if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(platformType.getTypeName())) {
                                    pT = platformType;
                                }
                            }
                            Platform platform = new Platform(
                                    textFieldName.getText(),
                                    null,
                                    new TranslationManager(mapNameTranslations[0], null),
                                    textFieldManufacturer.getText(),
                                    mapManufacturerTranslations[0],
                                    Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                    endDate,
                                    Integer.parseInt(spinnerDevKitCost.getValue().toString()),
                                    Integer.parseInt(spinnerDevelopmentCost.getValue().toString()),
                                    Integer.parseInt(spinnerTechLevel.getValue().toString()),
                                    Integer.parseInt(spinnerUnits.getValue().toString()),
                                    platformImages,
                                    requiredGameplayFeatures,
                                    Integer.parseInt(spinnerComplexity.getValue().toString()),
                                    checkBoxInternet.isSelected(),
                                    pT,
                                    checkBoxStartplatform.isBorderPaintedFlat()
                            );
                            if (JOptionPane.showConfirmDialog(null, platform.getOptionPaneMessage(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                addContent(platform);
                                ContentAdministrator.modAdded(platform.name, getType());
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
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> dependencies = new ArrayList<>();
        dependencies.add(GameplayFeatureManager.INSTANCE);
        return dependencies;
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
}
