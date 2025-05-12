package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.EngineFeature;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.DataLine;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.content.managed.types.EngineFeatureType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Summaries;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EngineFeatureManager extends AbstractAdvancedContentManager {

    public static final EngineFeatureManager INSTANCE = new EngineFeatureManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", "5.0.0-beta1", "5.0.0", MadGamesTycoon2ModTool.VERSION};

    private EngineFeatureManager() {
        super("engineFeature", "engine_feature", MGT2Paths.TEXT_DATA.getPath().resolve("EngineFeatures.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        return new EngineFeature(
                map.get("NAME EN"),
                new TranslationManager(map),
                getIdFromMap(map),
                map.get("DESC EN"),
                EngineFeatureType.getFromId(Integer.parseInt(map.get("TYP"))),
                map.get("DATE"),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("TECHLEVEL")),
                Integer.parseInt(map.get("GAMEPLAY")),
                Integer.parseInt(map.get("GRAPHIC")),
                Integer.parseInt(map.get("SOUND")),
                Integer.parseInt(map.get("TECH"))
        );
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("TYP", true, DataType.INT));
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("RES POINTS", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("DEV COSTS", true, DataType.INT));
        list.add(new DataLine("TECHLEVEL", true, DataType.INT));
        list.add(new DataLine("PIC", false, DataType.UNCHECKED));
        list.add(new DataLine("GAMEPLAY", true, DataType.INT));
        list.add(new DataLine("GRAPHIC", true, DataType.INT));
        list.add(new DataLine("SOUND", true, DataType.INT));
        list.add(new DataLine("TECH", true, DataType.INT));
        return list;
    }

    @Override
    protected String analyzeSpecialCases(Map<String, String> map) {
        try {
            if (map.containsKey("TYP")) {
                try {
                    EngineFeatureType.getFromId(Integer.parseInt(map.get("TYP")));
                } catch (NumberFormatException ignored) {

                }
            }
        } catch (IllegalArgumentException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.typeInvalid") + "\n", getGameFile().getName(), getType(), map.get("NAME EN"), e.getMessage());
        }
        return "";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        createBackup(false);
        analyzeFile();
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterFeatureName"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("commonText.enterDescription"));
        final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslations, descriptionTranslationsAdded, 1);
        JComboBox<String> comboBoxFeatureType = WindowHelper.getComboBox(EngineFeatureType.class, "mod.engineFeature.addMod.components.type.toolTip", EngineFeatureType.GRAPHIC.getTypeName());
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JSpinner spinnerResearchPoints = WindowHelper.getResearchPointSpinner();
        JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();
        JSpinner spinnerResearchCost = WindowHelper.getResearchCostSpinner();
        JSpinner spinnerTechLevel = WindowHelper.getTechLevelSpinner();
        JSpinner spinnerGameplay = WindowHelper.getPointSpinner();
        JSpinner spinnerGraphic = WindowHelper.getPointSpinner();
        JSpinner spinnerSound = WindowHelper.getPointSpinner();
        JSpinner spinnerTech = WindowHelper.getPointSpinner();

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerResearchCost, SpinnerType.RESEARCH_COST), WindowHelper.getSpinnerPanel(spinnerTechLevel, SpinnerType.TECH_LEVEL), WindowHelper.getSpinnerPanel(spinnerGameplay, SpinnerType.GAMEPLAY), WindowHelper.getSpinnerPanel(spinnerGraphic, SpinnerType.GRAPHIC), WindowHelper.getSpinnerPanel(spinnerSound, SpinnerType.SOUND), WindowHelper.getSpinnerPanel(spinnerTech, SpinnerType.TECH)};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().isEmpty() && !textFieldName.getText().equals(I18n.INSTANCE.get("commonText.enterFeatureName")) && !textFieldDescription.getText().isEmpty() && !textFieldDescription.getText().equals(I18n.INSTANCE.get("commonText.enterDescription"))) {
                    boolean modAlreadyExists = false;
                    for (String string : getContentByAlphabet()) {
                        if (textFieldName.getText().equals(string)) {
                            modAlreadyExists = true;
                        }
                    }
                    if (!modAlreadyExists) {
                        TranslationManager translationManager = new TranslationManager(mapNameTranslations[0], mapDescriptionTranslations[0]);
                        EngineFeatureType eft = null;
                        for (EngineFeatureType engineFeatureType : EngineFeatureType.values()) {
                            if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(engineFeatureType.getTypeName())) {
                                eft = engineFeatureType;
                            }
                        }
                        AbstractBaseContent engineFeature = new EngineFeature(
                                textFieldName.getText(),
                                translationManager,
                                null,
                                textFieldDescription.getText(),
                                eft,
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                (int) spinnerResearchPoints.getValue(),
                                (int) spinnerResearchCost.getValue(),
                                (int) spinnerDevelopmentCost.getValue(),
                                (int) spinnerTechLevel.getValue(),
                                (int) spinnerGameplay.getValue(),
                                (int) spinnerGraphic.getValue(),
                                (int) spinnerSound.getValue(),
                                (int) spinnerTech.getValue()
                        );
                        boolean addFeature = Summaries.showSummary(engineFeature.getOptionPaneMessage(), I18n.INSTANCE.get("mod.engineFeature.addMod.title"));
                        if (addFeature) {
                            addContent(engineFeature);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.engineFeature.upperCase") + ": [" + engineFeature.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
