package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.HardwareFeature;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.DataLine;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HardwareFeatureManager extends AbstractAdvancedContentManager {

    public static final HardwareFeatureManager INSTANCE = new HardwareFeatureManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", MadGamesTycoon2ModTool.VERSION};

    private HardwareFeatureManager() {
        super("hardwareFeature", "hardware_feature", MGT2Paths.TEXT_DATA.getPath().resolve("HardwareFeatures.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        return new HardwareFeature(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DESC EN"),
                map.get("DATE"),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("QUALITY")),
                map.containsKey("ONLY_STATIONARY") && !map.get("ONLY_STATIONARY").equals("false"),
                map.containsKey("NEEDINTERNET") && !map.get("NEEDINTERNET").equals("false")
        );
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("RES POINTS", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("DEV COSTS", true, DataType.INT));
        list.add(new DataLine("QUALITY", true, DataType.INT));
        list.add(new DataLine("ONLY_STATIONARY", false, DataType.EMPTY));
        list.add(new DataLine("NEEDINTERNET", false, DataType.EMPTY));
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("mod.hardwareFeature.addMod.components.textFieldDescription.initialValue"));
        final Map<String, String>[] mapDescriptionTranslation = new Map[]{new HashMap<>()};
        AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslation, descriptionTranslationsAdded, 1);
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

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerQuality, SpinnerType.QUALITY), checkBoxNeedsInternet, checkBoxOnlyStationary};
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
                                new TranslationManager(mapNameTranslations[0], mapDescriptionTranslation[0]),
                                textFieldDescription.getText(),
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                Integer.parseInt(spinnerResearchPoints.getValue().toString()),
                                Integer.parseInt(spinnerCost.getValue().toString()),
                                Integer.parseInt(spinnerDevelopmentCost.getValue().toString()),
                                Integer.parseInt(spinnerQuality.getValue().toString()),
                                checkBoxOnlyStationary.isSelected(),
                                checkBoxNeedsInternet.isSelected()
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

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
