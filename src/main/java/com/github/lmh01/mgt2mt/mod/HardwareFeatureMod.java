package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareFeatureMod extends AbstractAdvancedMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareFeatureMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("QUALITY", map, bw);
        if (map.containsKey("ONLY_STATIONARY")) {
            bw.write("[ONLY_STATIONARY]");
            bw.write("\r\n");
        }
        if (map.containsKey("NEEDINTERNET")) {
            bw.write("[NEEDINTERNET]");
            bw.write("\r\n");
        }
    }

     @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "3.0.0-alpha-1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "hardwareFeature";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.hardwareFeatureMod;
    }

    @Override
    public String getExportType() {
        return "hardware_feature";
    }

    @Override
    public String getGameFileName() {
        return "HardwareFeatures.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_hardware_features.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldDescription.initialValue"));
        final Map<String, String>[] mapDescriptionTranslation = new Map[]{new HashMap<>()};
        AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslation, descriptionTranslationsAdded, 0);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JSpinner spinnerResearchPoints = WindowHelper.getResearchPointSpinner();
        JSpinner spinnerCost = WindowHelper.getCostSpinner();
        JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();
        JSpinner spinnerQuality = WindowHelper.getBaseSpinner("commonText.quality.spinner", 2, 0, 100, 1);
        JCheckBox checkBoxNeedsInternet = new JCheckBox(I18n.INSTANCE.get("commonText.needInternet"));
        checkBoxNeedsInternet.setToolTipText(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.checkBoxNeedsInternet.toolTip"));
        JCheckBox checkBoxOnlyStationary = new JCheckBox(I18n.INSTANCE.get("commonText.stationary"));
        checkBoxOnlyStationary.setToolTipText(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.checkBoxOnlyStationary.toolTip"));

        Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getNamePanel(this, textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, 6), WindowHelper.getSpinnerPanel(spinnerCost, 8), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerQuality, "commonText.quality"), checkBoxNeedsInternet, checkBoxOnlyStationary};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldName.initialValue")) && !textFieldDescription.getText().equals(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldDescription.initialValue"))) {
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
                        hardwareFeatureMap.put("QUALITY", spinnerQuality.getValue().toString());
                        if (checkBoxNeedsInternet.isSelected()) {
                            hardwareFeatureMap.put("NEEDINTERNET", "");
                        }
                        if (checkBoxOnlyStationary.isSelected()) {
                            hardwareFeatureMap.put("ONLY_STATIONARY", "");
                        }
                        if (JOptionPane.showConfirmDialog(null, getOptionPaneMessage(hardwareFeatureMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            createBackup();
                            addModToFile(hardwareFeatureMap);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.hardwareFeature.upperCase") + " - " + hardwareFeatureMap.get("NAME EN"));
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardwareFeature.upperCase") + ": [" + hardwareFeatureMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
        StringBuilder lastPart = new StringBuilder();
        lastPart.append(I18n.INSTANCE.get("commonText.needInternet")).append(": ");
        if (map.containsKey("NEEDINTERNET")) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        lastPart.append("<br>").append(I18n.INSTANCE.get("commonText.stationary")).append(": ");
        if (map.containsKey("ONLY_STATIONARY")) {
            lastPart.append(I18n.INSTANCE.get("commonText.yes"));
        } else {
            lastPart.append(I18n.INSTANCE.get("commonText.no"));
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.hardwareFeature.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>" +
                I18n.INSTANCE.get("commonText.quality") + ": " + map.get("QUALITY") + "<br>" +
                lastPart;
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
