package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.Hardware;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.HardwareType;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final HardwareManager INSTANCE = new HardwareManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private HardwareManager() {
        super("hardware", "hardware", "default_hardware.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Hardware.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        if (!(content instanceof Hardware)) {
            throw new ModProcessingException("Unable to edit text files for content of type " + getType() + ": The input content is not instance of Hardware!");
        }
        Hardware hardware = (Hardware)content;
        if (action.equals(ContentAction.ADD_MOD)) {
            try {
                Charset charset = getCharset();
                Path gameFilePath = gameFile.toPath();
                if (Files.exists(gameFilePath)) {
                    Files.delete(gameFilePath);
                }
                Files.createFile(gameFilePath);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                if (charset.equals(StandardCharsets.UTF_8)) {
                    bw.write("\ufeff");
                }
                int currentType = 0;
                boolean firstMap = true;
                for (Map<String, String> fileContent : fileContent) {
                    if (firstMap) {
                        currentType = Integer.parseInt(fileContent.get("TYP"));
                        firstMap = false;
                    }
                    if (currentType != Integer.parseInt(fileContent.get("TYP"))) {
                        currentType = Integer.parseInt(fileContent.get("TYP"));
                        bw.write("////////////////////////////////////////////////////////////////////");
                        bw.write("\r\n");
                        bw.write("\r\n");
                    }
                    printValues(fileContent, bw);
                    bw.write("\r\n");
                }
                if (currentType != hardware.hardwareType.getId()) {
                    bw.write("////////////////////////////////////////////////////////////////////");
                    bw.write("\r\n");
                    bw.write("\r\n");
                }
                printValues(content.getMap(), bw);
                bw.write("\r\n");
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
            }
        } else {
            try {
                int contentId = content.id;
                Charset charset = getCharset();
                Path gameFilePath = gameFile.toPath();
                if (Files.exists(gameFilePath)) {
                    Files.delete(gameFilePath);
                }
                Files.createFile(gameFilePath);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), charset));
                if (charset.equals(StandardCharsets.UTF_8)) {
                    bw.write("\ufeff");
                }
                int currentType = 0;
                boolean firstMap = true;
                for (Map<String, String> fileContent : fileContent) {
                    if (Integer.parseInt(fileContent.get("ID")) != contentId) {
                        if (firstMap) {
                            currentType = Integer.parseInt(fileContent.get("TYP"));
                            firstMap = false;
                        }
                        if (currentType != Integer.parseInt(fileContent.get("TYP"))) {
                            currentType = Integer.parseInt(fileContent.get("TYP"));
                            bw.write("////////////////////////////////////////////////////////////////////");
                            bw.write("\r\n");
                            bw.write("\r\n");
                        }
                        printValues(fileContent, bw);
                        bw.write("\r\n");
                    }
                }
                bw.write("[EOF]");
                bw.close();
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
            }
        }
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        if (map.containsKey("ONLY_HANDHELD")) {
            EditHelper.printLine("ONLY_HANDHELD", map, bw);
        }
        if (map.containsKey("ONLY_STATIONARY")) {
            EditHelper.printLine("ONLY_STATIONARY", map, bw);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                bw.write("[" + entry.getKey() + "]" + entry.getValue() + "\r\n");
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        ArrayList<Integer> requiredGenres = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                requiredGenres.add(Integer.parseInt(entry.getValue()));
            }
        }
        return new Hardware(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DESC EN"),
                map.get("DATE"),
                HardwareType.getFromId(Integer.parseInt(map.get("TYP"))),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("TECHLEVEL")),
                map.containsKey("ONLY_HANDHELD"),
                map.containsKey("ONLY_STATIONARY"),
                requiredGenres
        );
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldDescription.initialValue"));
        final Map<String, String>[] mapDescriptionTranslation = new Map[]{new HashMap<>()};
        AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslation, descriptionTranslationsAdded, 1);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JComboBox<String> comboBoxType = WindowHelper.getComboBox(HardwareType.class, "mod.hardware.addMod.components.comboBox.type.toolTip", HardwareType.CPU.getTypeName());
        JSpinner spinnerResearchPoints = WindowHelper.getResearchPointSpinner();
        JSpinner spinnerCost = WindowHelper.getBaseSpinner("commonText.cost.spinner.toolTip", 500000, 0, 10000000, 10000);
        JSpinner spinnerDevelopmentCost = WindowHelper.getBaseSpinner("commonText.developmentCost.spinner", 1000000, 0, 100000000, 10000);
        JSpinner spinnerTechLevel = WindowHelper.getTechLevelSpinner();

        JComboBox<String> comboBoxExclusivity = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"), I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value2")}));
        comboBoxExclusivity.setSelectedItem(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"));
        comboBoxExclusivity.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.toolTip"));
        comboBoxExclusivity.setEnabled(false);

        AtomicBoolean lastValue = new AtomicBoolean(false);
        JCheckBox checkBoxEnableExclusivity = new JCheckBox(I18n.INSTANCE.get("mod.hardware.addMod.components.checkBox.exclusiveConsole"));
        checkBoxEnableExclusivity.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.checkBox.exclusiveConsole.toolTip"));
        checkBoxEnableExclusivity.addChangeListener(changeListener -> {
            if (checkBoxEnableExclusivity.isSelected() != lastValue.get()) {
                comboBoxExclusivity.setEnabled(checkBoxEnableExclusivity.isSelected());
                lastValue.set(checkBoxEnableExclusivity.isSelected());
            }
        });

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getTypePanel(comboBoxType), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerTechLevel, SpinnerType.TECH_LEVEL), checkBoxEnableExclusivity, comboBoxExclusivity};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue")) && !textFieldDescription.getText().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldDescription.initialValue"))) {
                    boolean modAlreadyExists = false;
                    for (String string : getContentByAlphabet()) {
                        if (textFieldName.getText().equals(string)) {
                            modAlreadyExists = true;
                        }
                    }
                    if (!modAlreadyExists) {
                        HardwareType hT = null;
                        for (HardwareType hardwareType : HardwareType.values()) {
                            if (Objects.requireNonNull(comboBoxType.getSelectedItem()).toString().equals(hardwareType.getTypeName())) {
                                hT = hardwareType;
                            }
                        }
                        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
                        if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("commonText.controller"))) {
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) < 1992) {
                                requiredGameplayFeatures.add(45);
                            } else {
                                requiredGameplayFeatures.add(51);
                            }
                        }
                        if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("commonText.screen"))) {
                            requiredGameplayFeatures.add(45);
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) > 2001) {
                                requiredGameplayFeatures.add(65);
                            }
                        }
                        boolean onlyStationary = false;
                        boolean onlyHandheld = false;
                        if (checkBoxEnableExclusivity.isSelected()) {
                            if (Objects.requireNonNull(comboBoxExclusivity.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"))) {
                                onlyStationary = true;
                            } else {
                                onlyHandheld = true;
                            }
                        }
                        AbstractBaseContent hardware = new Hardware(
                                textFieldName.getText(),
                                null,
                                new TranslationManager(mapNameTranslations[0], mapDescriptionTranslation[0]),
                                textFieldDescription.getText(),
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                hT,
                                Integer.parseInt(spinnerResearchPoints.getValue().toString()),
                                Integer.parseInt(spinnerCost.getValue().toString()),
                                Integer.parseInt(spinnerDevelopmentCost.getValue().toString()),
                                Integer.parseInt(spinnerTechLevel.getValue().toString()),
                                onlyHandheld,
                                onlyStationary,
                                requiredGameplayFeatures
                        );
                        if (JOptionPane.showConfirmDialog(null, hardware.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(hardware);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardware.upperCase") + ": [" + hardware.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameDescriptionFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED-")) {
                replaceMapEntry(map, missingDependency, replacement, entry.getKey());
            }
        }
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(GameplayFeatureManager.INSTANCE);
        return arrayList;
    }
}