package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.mod.managed.EngineFeatureType;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.SpinnerType;
import com.github.lmh01.mgt2mt.util.*;
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

public class EngineFeatureManager extends AbstractAdvancedContentManager {

    public static final EngineFeatureManager INSTANCE = new EngineFeatureManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private EngineFeatureManager() {
        super("engineFeature", "engine_feature", "default_engine_features.txt", MGT2Paths.TEXT_DATA.getPath().resolve("EngineFeatures.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("TECHLEVEL", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        return new EngineFeature(
                map.get("NAME EN"),
                new TranslationManagerNew(map),
                getIdFromMap(map),
                map.get("DESC EN"),
                EngineFeatureType.getFromId(Integer.parseInt("TYP")),
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
    public void openAddModGui() throws ModProcessingException {
        createBackup();
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
                        TranslationManagerNew translationManager = new TranslationManagerNew(mapNameTranslations[0], mapDescriptionTranslations[0]);
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
                                (int)spinnerResearchPoints.getValue(),
                                (int)spinnerResearchCost.getValue(),
                                (int)spinnerDevelopmentCost.getValue(),
                                (int)spinnerTechLevel.getValue(),
                                (int)spinnerGameplay.getValue(),
                                (int)spinnerGraphic.getValue(),
                                (int)spinnerSound.getValue(),
                                (int)spinnerTech.getValue()
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
}
