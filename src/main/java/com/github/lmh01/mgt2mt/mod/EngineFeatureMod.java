package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.EngineFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.EngineFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.EngineFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Summaries;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EngineFeatureMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureMod.class);
    EngineFeatureAnalyzer engineFeatureAnalyzer = new EngineFeatureAnalyzer();
    EngineFeatureEditor engineFeatureEditor = new EngineFeatureEditor();
    EngineFeatureSharer engineFeatureSharer = new EngineFeatureSharer();
    public ArrayList<JMenuItem> engineFeatureModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public EngineFeatureAnalyzer getAnalyzer(){
        return engineFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public EngineFeatureEditor getEditor(){
        return engineFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public EngineFeatureSharer getSharer(){
        return engineFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return engineFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return engineFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
            return engineFeatureSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.engineFeatureMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "EngineFeatures.txt";
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return engineFeatureModMenuItems;
    }

    @Override
    public void menuActionAddMod() {
        try{
            Backup.createBackup(ModManager.engineFeatureMod.getFile());
            ModManager.engineFeatureMod.getAnalyzer().analyzeFile();

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
                        for(String string : getBaseAnalyzer().getContentByAlphabet()){
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
                            newEngineFeature.put("ID", Integer.toString(ModManager.engineFeatureMod.getAnalyzer().getFreeId()));
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
                            boolean addFeature = Summaries.showSummary(ModManager.engineFeatureMod.getSharer().getOptionPaneMessage(newEngineFeature), I18n.INSTANCE.get("mod.engineFeature.addMod.title"));
                            if (addFeature) {
                                Backup.createBackup(getFile());
                                ModManager.engineFeatureMod.getEditor().addMod(newEngineFeature);
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
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Converts the input string into the respective type number
     * @param featureType The feature type string
     * @return Returns the type number
     */
    public int getEngineFeatureTypeByName(String featureType){
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
