package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareMod extends AbstractAdvancedDependentMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareMod.class);

    @Override
    public <T> void addModToFile(T t) throws ModProcessingException {
        try {
            Map<String, String> map = transformGenericToMap(t);
            analyzeFile();
            LOGGER.info("Adding new " + getType() + ": " + map.get("NAME EN"));
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if (fileToEdit.exists()) {
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            int currentType = 0;
            boolean firstMap = true;
            for (Map<String, String> fileContent : getFileContent()) {
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
            if (currentType != Integer.parseInt(map.get("TYP"))) {
                bw.write("////////////////////////////////////////////////////////////////////");
                bw.write("\r\n");
                bw.write("\r\n");
            }
            printValues(map, bw);
            bw.write("\r\n");
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {//TODO catch block schreiben

        }
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        try {
            analyzeFile();
            int modId = getContentIdByName(name);
            LOGGER.info("Removing " + getType() + ": " + name);
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if (fileToEdit.exists()) {
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            int currentType = 0;
            boolean firstMap = true;
            for (Map<String, String> fileContent : getFileContent()) {
                if (Integer.parseInt(fileContent.get("ID")) != modId) {
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
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + name);
        } catch (IOException e) {//TODO catch block schreiben

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
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "3.0.0-alpha-1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "hardware";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.hardwareMod;
    }

    @Override
    public String getExportType() {
        return "hardware";
    }

    @Override
    public String getGameFileName() {
        return "Hardware.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_hardware.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldDescription.initialValue"));
        final Map<String, String>[] mapDescriptionTranslation = new Map[]{new HashMap<>()};
        AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslation, descriptionTranslationsAdded, 0);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JComboBox<String> comboBoxType = WindowHelper.getTypeComboBox(4);
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

        Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getTypePanel(comboBoxType), WindowHelper.getSpinnerPanel(spinnerResearchPoints, 6), WindowHelper.getSpinnerPanel(spinnerCost, 8), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerTechLevel, 4), checkBoxEnableExclusivity, comboBoxExclusivity};
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
                        Map<String, String> hardwareFeatureMap = new HashMap<>();
                        hardwareFeatureMap.put("ID", Integer.toString(getFreeId()));
                        if (!nameTranslationsAdded.get()) {
                            hardwareFeatureMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                        } else {
                            hardwareFeatureMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            hardwareFeatureMap.put("NAME EN", textFieldName.getText());
                        }
                        if (!descriptionTranslationsAdded.get()) {
                            hardwareFeatureMap.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        } else {
                            hardwareFeatureMap.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslation[0], "DESC"));
                            hardwareFeatureMap.put("DESC EN", textFieldDescription.getText());
                        }
                        hardwareFeatureMap.put("DATE", Objects.requireNonNull(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem())) + " " + spinnerUnlockYear.getValue().toString());
                        hardwareFeatureMap.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                        hardwareFeatureMap.put("PRICE", spinnerCost.getValue().toString());
                        hardwareFeatureMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                        hardwareFeatureMap.put("TYP", Integer.toString(getHardwareTypeIdByName(Objects.requireNonNull(comboBoxType.getSelectedItem()).toString())));
                        hardwareFeatureMap.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                        if (comboBoxType.getSelectedItem().equals(I18n.INSTANCE.get("commonText.controller"))) {
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) < 1992) {
                                hardwareFeatureMap.put("NEED-1", "45");
                            } else {
                                hardwareFeatureMap.put("NEED-1", "51");
                            }
                        }
                        if (comboBoxType.getSelectedItem().equals(I18n.INSTANCE.get("commonText.screen"))) {
                            hardwareFeatureMap.put("NEED-1", "45");
                            if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) > 2001) {
                                hardwareFeatureMap.put("NEED-2", "56");
                            }
                        }
                        if (checkBoxEnableExclusivity.isSelected()) {
                            if (Objects.requireNonNull(comboBoxExclusivity.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"))) {
                                hardwareFeatureMap.put("ONLY_STATIONARY", "");
                            } else {
                                hardwareFeatureMap.put("ONLY_HANDHELD", "");
                            }
                        }
                        if (JOptionPane.showConfirmDialog(null, getOptionPaneMessage(hardwareFeatureMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            createBackup();
                            this.addModToFile(hardwareFeatureMap);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.hardware.upperCase") + " - " + hardwareFeatureMap.get("NAME EN"));
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardware.upperCase") + ": [" + hardwareFeatureMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        boolean allowStationaryConsole = true;
        boolean allowPortableConsole = !map.containsKey("ONLY_STATIONARY");
        if (map.containsKey("ONLY_HANDHELD")) {
            allowStationaryConsole = false;
        }
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.stationaryConsole")).append(": ");
        if (allowStationaryConsole) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.portableConsole")).append(": ");
        if (allowPortableConsole) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardware.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + getHardwareTypeNameById(Integer.parseInt(map.get("TYP"))) + "<br>" +
                lastPart;
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "NEED-1");
        replaceMapEntry(map, missingDependency, replacement, "NEED-2");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.gameplayFeatureMod);
        return arrayList;
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                map.replace(entry.getKey(), Integer.toString(ModManager.gameplayFeatureMod.getModIdByNameFromImportHelperMap(entry.getValue())));
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().contains("NEED")) {
                try {
                    map.replace(entry.getKey(), ModManager.gameplayFeatureMod.getContentNameById(Integer.parseInt(entry.getValue())));
                } catch (NumberFormatException e) {
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.warningExportMapNotChangedProperly") + ":");
                    TextAreaHelper.printStackTrace(e);
                }
            }
        }
        return map;
    }

    @Override
    public  <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
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

    /**
     * Converts the input id into the respective type name
     *
     * @param typeName The feature type id
     * @return Returns the type name
     */
    public int getHardwareTypeIdByName(String typeName) {//TODO Rewrite to use enums
        if (typeName.equals(I18n.INSTANCE.get("commonText.cpu"))) {
            return 0;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.gpu"))) {
            return 1;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.ram"))) {
            return 2;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.storage"))) {
            return 3;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.audio"))) {
            return 4;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.cooling"))) {
            return 5;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.gameStorageDevice"))) {
            return 6;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.controller"))) {
            return 7;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.case"))) {
            return 8;
        } else if (typeName.equals(I18n.INSTANCE.get("commonText.screen"))) {
            return 9;
        } else {
            throw new IllegalArgumentException("The input for the function typeName is invalid! For valid inputs see function; Was: " + typeName);
        }
    }

    /**
     * Converts the input id into the respective type name
     *
     * @param typeId The feature type id
     * @return Returns the type name
     */
    public String getHardwareTypeNameById(int typeId) {//TODO Rewrite to use enums
        switch (typeId) {
            case 0:
                return I18n.INSTANCE.get("commonText.cpu");
            case 1:
                return I18n.INSTANCE.get("commonText.gpu");
            case 2:
                return I18n.INSTANCE.get("commonText.ram");
            case 3:
                return I18n.INSTANCE.get("commonText.storage");
            case 4:
                return I18n.INSTANCE.get("commonText.audio");
            case 5:
                return I18n.INSTANCE.get("commonText.cooling");
            case 6:
                return I18n.INSTANCE.get("commonText.gameStorageDevice");
            case 7:
                return I18n.INSTANCE.get("commonText.controller");
            case 8:
                return I18n.INSTANCE.get("commonText.case");
            case 9:
                return I18n.INSTANCE.get("commonText.screen");
            default:
                throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-9; Was: " + typeId);
        }
    }
}
