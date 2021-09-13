package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Summaries;
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

public class EngineFeatureMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureMod.class);

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
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.3.0"};
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.engineFeatureMod;
    }

    @Override
    public String getExportType() {
        return "engine_feature";
    }

    @Override
    public String getGameFileName() {
        return "EngineFeatures.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_engine_features.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
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
        JComboBox<String> comboBoxFeatureType = WindowHelper.getTypeComboBox(0);
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

        Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, 6), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerResearchCost, 5), WindowHelper.getSpinnerPanel(spinnerTechLevel, 4), WindowHelper.getSpinnerPanel(spinnerGameplay, 0), WindowHelper.getSpinnerPanel(spinnerGraphic, 1), WindowHelper.getSpinnerPanel(spinnerSound, 2), WindowHelper.getSpinnerPanel(spinnerTech, 3)};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                if(!textFieldName.getText().isEmpty() && !textFieldName.getText().equals(I18n.INSTANCE.get("commonText.enterFeatureName")) && !textFieldDescription.getText().isEmpty() && !textFieldDescription.getText().equals(I18n.INSTANCE.get("commonText.enterDescription"))) {
                    boolean modAlreadyExists = false;
                    for(String string : getContentByAlphabet()){
                        if(textFieldName.getText().equals(string)){
                            modAlreadyExists = true;
                        }
                    }
                    if(!modAlreadyExists) {
                        Map<String, String> newEngineFeature = new HashMap<>();
                        if (!nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()) {
                            newEngineFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newEngineFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        } else if (!nameTranslationsAdded.get() && descriptionTranslationsAdded.get()) {
                            newEngineFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                        } else if (nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()) {
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            newEngineFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        } else {
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                            newEngineFeature.put("NAME EN", textFieldName.getText());
                            newEngineFeature.put("DESC EN", textFieldDescription.getText());
                        }
                        newEngineFeature.put("ID", Integer.toString(getFreeId()));
                        newEngineFeature.put("TYP", Integer.toString(getEngineFeatureTypeByName(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString())));
                        newEngineFeature.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()) + " " + spinnerUnlockYear.getValue().toString());
                        newEngineFeature.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                        newEngineFeature.put("PRICE", spinnerResearchCost.getValue().toString());
                        newEngineFeature.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                        newEngineFeature.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                        newEngineFeature.put("PIC", "");
                        newEngineFeature.put("GAMEPLAY", spinnerGameplay.getValue().toString());
                        newEngineFeature.put("GRAPHIC", spinnerGraphic.getValue().toString());
                        newEngineFeature.put("SOUND", spinnerSound.getValue().toString());
                        newEngineFeature.put("TECH", spinnerTech.getValue().toString());
                        boolean addFeature = Summaries.showSummary(getOptionPaneMessage(newEngineFeature), I18n.INSTANCE.get("mod.engineFeature.addMod.title"));
                        if (addFeature) {
                            createBackup();
                            addModToFile(newEngineFeature);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.engineFeature.upperCase") + " - " + newEngineFeature.get("NAME EN"));
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.engineFeature.upperCase") + ": [" + newEngineFeature.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameDescriptionFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                break;
            }
        }
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        return I18n.INSTANCE.get("sharer.engineFeature.optionPaneMessage.main") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + map.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + ModManager.engineFeatureMod.getEngineFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + map.get("RES POINTS") + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + map.get("PRICE") + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "\n" +
                I18n.INSTANCE.get("commonText.techLevel") + ": " + map.get("TECHLEVEL") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + map.get("GAMEPLAY") + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + map.get("GRAPHIC") + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + map.get("SOUND") + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + map.get("TECH") + "\n";
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    protected <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        return new HashMap<>();
    }

    @Override
    public String getTypeCaps() {
        return "ENGINE_FEATURE";
    }

    @Override
    public String getImportExportFileName() {
        return "engineFeature.txt";
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        return new ArrayList<>();
    }

    /**
     * Converts the input string into the respective type number
     * @param featureType The feature type string
     * @return Returns the type number
     */
    public int getEngineFeatureTypeByName(String featureType){//TODO rewrite to use enum
        switch (featureType){
            case "Graphic": return 0;
            case "Sound": return 1;
            case "Artificial Intelligence": return 2;
            case "Physics": return 3;
        }
        return 10;
    }

    /**
     * Converts the input id into the respective type name
     * @param typeId The feature type id
     * @return Returns the type name
     */
    public String getEngineFeatureNameByTypeId(int typeId){
        switch (typeId){
            case 0: return "Graphic";
            case 1: return "Sound";
            case 2: return "Artificial Intelligence";
            case 3: return "Physics";
        }
        return "";
    }
}
