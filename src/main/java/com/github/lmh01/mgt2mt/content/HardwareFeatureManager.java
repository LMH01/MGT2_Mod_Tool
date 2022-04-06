package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareFeatureManager extends AbstractAdvancedContentManager {

    public static final HardwareFeatureManager INSTANCE = new HardwareFeatureManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private HardwareFeatureManager() {
        super("hardwareFeature", "hardware_feature", "default_hardware_features.txt", MGT2Paths.TEXT_DATA.getPath().resolve("HardwareFeatures.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
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
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        return new HardwareFeature(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManagerNew(map),
                map.get("DESC EN"),
                map.get("DATE"),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("QUALITY")),
                map.containsKey("ONLY_STATIONARY"),
                map.containsKey("NEEDINTERNET")
        );
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
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

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getNamePanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerQuality, SpinnerType.QUALITY), checkBoxNeedsInternet, checkBoxOnlyStationary};
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
                        AbstractBaseContent hardwareFeature = new HardwareFeature(
                                textFieldName.getText(),
                                null,
                                new TranslationManagerNew(mapNameTranslations[0], mapDescriptionTranslation[0]),
                                textFieldDescription.getText(),
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                Integer.parseInt(spinnerResearchPoints.getValue().toString()),
                                Integer.parseInt(spinnerCost.getValue().toString()),
                                Integer.parseInt(spinnerDevelopmentCost.getValue().toString()),
                                Integer.parseInt(spinnerQuality.getValue().toString()),
                                checkBoxOnlyStationary.isSelected(),
                                checkBoxNeedsInternet.isBorderPaintedFlat()
                                );
                        if (JOptionPane.showConfirmDialog(null, hardwareFeature.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(hardwareFeature);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardwareFeature.upperCase") + ": [" + hardwareFeature.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
}
