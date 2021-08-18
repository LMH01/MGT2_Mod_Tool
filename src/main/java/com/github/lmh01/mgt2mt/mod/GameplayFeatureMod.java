package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GameplayFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GameplayFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GameplayFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.*;
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

public class GameplayFeatureMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureMod.class);
    GameplayFeatureAnalyzer gameplayFeatureAnalyzer = new GameplayFeatureAnalyzer();
    GameplayFeatureEditor gameplayFeatureEditor = new GameplayFeatureEditor();
    GameplayFeatureSharer gameplayFeatureSharer = new GameplayFeatureSharer();
    public ArrayList<JMenuItem> gameplayFeatureModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GameplayFeatureAnalyzer getAnalyzer(){
        return gameplayFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GameplayFeatureEditor getEditor(){
        return gameplayFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GameplayFeatureSharer getSharer(){
        return gameplayFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return gameplayFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return gameplayFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return gameplayFeatureSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.gameplayFeatureMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return gameplayFeatureModMenuItems;
    }

    @Override
    public void menuActionAddMod() {
        try{
            Backup.createBackup(ModManager.gameplayFeatureMod.getFile());
            ModManager.gameplayFeatureMod.getAnalyzer().analyzeFile();
            final ArrayList<Integer>[] badGenreIds = new ArrayList[]{new ArrayList<>()};
            final ArrayList<Integer>[] goodGenreIds = new ArrayList[]{new ArrayList<>()};
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterFeatureName"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("commonText.enterDescription"));
            final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddDescriptionTranslations = WindowHelper.getAddTranslationsButton(mapDescriptionTranslations, descriptionTranslationsAdded, 1);
            JComboBox<String> comboBoxFeatureType = WindowHelper.getTypeComboBox(1);
            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            JSpinner spinnerResearchPoints = WindowHelper.getResearchPointSpinner();
            JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();
            JSpinner spinnerResearchCost = WindowHelper.getResearchCostSpinner();
            JSpinner spinnerGameplay = WindowHelper.getPointSpinner();
            JSpinner spinnerGraphic = WindowHelper.getPointSpinner();
            JSpinner spinnerSound = WindowHelper.getPointSpinner();
            JSpinner spinnerTech = WindowHelper.getPointSpinner();

            JButton buttonBadGenres = new JButton(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres"));
            buttonBadGenres.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.toolTip"));
            buttonBadGenres.addActionListener(actionEvent -> {
                ArrayList<Integer> goodGenrePositions =  Utils.getSelectedEntries("Select the genre(s) that don't work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
                ArrayList<Integer> badGenreIdsOut = new ArrayList<>();
                for(Integer integer : goodGenrePositions){
                    badGenreIdsOut.add(ModManager.genreMod.getAnalyzer().getContentIdByName(ModManager.genreMod.getAnalyzer().getContentByAlphabet()[integer]));
                }
                badGenreIds[0] = badGenreIdsOut;
                if(badGenreIds[0].size() != 0){
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if(Settings.disableSafetyFeatures || !mutualEntries){
                        buttonBadGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.selected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.selected"));
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.unableToSave"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        badGenreIds[0].clear();
                    }
                }else{
                    buttonBadGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres"));
                }
            });
            JButton buttonGoodGenres = new JButton(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres"));
            buttonGoodGenres.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.toolTip"));
            buttonGoodGenres.addActionListener(actionEvent -> {
                ArrayList<Integer> goodGenrePositions =  Utils.getSelectedEntries("Select the genre(s) that work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
                ArrayList<Integer> goodGenreIdsOut = new ArrayList<>();
                for(Integer integer : goodGenrePositions){
                    goodGenreIdsOut.add(ModManager.genreMod.getAnalyzer().getContentIdByName(ModManager.genreMod.getAnalyzer().getContentByAlphabet()[integer]));
                }
                goodGenreIds[0] = goodGenreIdsOut;
                if(goodGenreIds[0].size() != 0){
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if(Settings.disableSafetyFeatures || !mutualEntries){
                        buttonGoodGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.selected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.selected"));
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.unableToSave"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        goodGenreIds[0].clear();
                    }
                }else{
                    buttonGoodGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres"));
                }
            });

            JCheckBox checkBoxCompatibleWithArcadeCabinets = new JCheckBox(I18n.INSTANCE.get("commonText.arcadeCompatibility"));
            checkBoxCompatibleWithArcadeCabinets.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.arcadeCabinetCompatibility.toolTip"));
            checkBoxCompatibleWithArcadeCabinets.setSelected(true);

            JCheckBox checkBoxCompatibleWithMobile = new JCheckBox(I18n.INSTANCE.get("commonText.mobileCompatibility"));
            checkBoxCompatibleWithMobile.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.mobileCompatibility.toolTip"));
            checkBoxCompatibleWithMobile.setSelected(true);

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, 6), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerResearchCost, 5), WindowHelper.getSpinnerPanel(spinnerGameplay, 0), WindowHelper.getSpinnerPanel(spinnerGraphic, 1), WindowHelper.getSpinnerPanel(spinnerSound, 2), WindowHelper.getSpinnerPanel(spinnerTech, 3), buttonBadGenres, buttonGoodGenres, checkBoxCompatibleWithArcadeCabinets, checkBoxCompatibleWithMobile};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if (!textFieldName.getText().isEmpty() && !textFieldName.getText().equals(I18n.INSTANCE.get("commonText.enterFeatureName")) && !textFieldDescription.getText().isEmpty() && !textFieldDescription.getText().equals(I18n.INSTANCE.get("commonText.enterDescription"))) {
                        boolean modAlreadyExists = false;
                        for(String string : getBaseAnalyzer().getContentByAlphabet()){
                            if(textFieldName.getText().equals(string)){
                                modAlreadyExists = true;
                            }
                        }
                        if(!modAlreadyExists) {
                            Map<String, String> newGameplayFeature = new HashMap<>();
                            if(!nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()){
                                newGameplayFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                newGameplayFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                            }else if(!nameTranslationsAdded.get() && descriptionTranslationsAdded.get()){
                                newGameplayFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                            }else if(nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()){
                                newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                newGameplayFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                            }else{
                                newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                                newGameplayFeature.put("NAME EN", textFieldName.getText());
                                newGameplayFeature.put("DESC EN", textFieldDescription.getText());
                            }
                            newGameplayFeature.put("ID", Integer.toString(ModManager.gameplayFeatureMod.getAnalyzer().getFreeId()));
                            newGameplayFeature.put("TYP", Integer.toString(getGameplayFeatureTypeByName(Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString())));
                            newGameplayFeature.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()) + " " + spinnerUnlockYear.getValue().toString());
                            newGameplayFeature.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                            newGameplayFeature.put("PRICE", spinnerResearchCost.getValue().toString());
                            newGameplayFeature.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            newGameplayFeature.put("PIC", "");
                            newGameplayFeature.put("GAMEPLAY", spinnerGameplay.getValue().toString());
                            newGameplayFeature.put("GRAPHIC", spinnerGraphic.getValue().toString());
                            newGameplayFeature.put("SOUND", spinnerSound.getValue().toString());
                            newGameplayFeature.put("TECH", spinnerTech.getValue().toString());
                            newGameplayFeature.put("GOOD", Utils.transformArrayListToString(goodGenreIds[0]));
                            newGameplayFeature.put("BAD", Utils.transformArrayListToString(badGenreIds[0]));
                            if(!checkBoxCompatibleWithArcadeCabinets.isSelected()){
                                newGameplayFeature.put("NO_ARCADE", "");
                            }
                            if(!checkBoxCompatibleWithMobile.isSelected()){
                                newGameplayFeature.put("NO_MOBILE", "");
                            }
                            boolean addFeature = Summaries.showSummary(ModManager.gameplayFeatureMod.getSharer().getOptionPaneMessage(newGameplayFeature), I18n.INSTANCE.get("mod.gameplayFeature.addMod.title"));
                            if(addFeature) {
                                Backup.createBackup(getFile());
                                ModManager.gameplayFeatureMod.getEditor().addMod(newGameplayFeature);
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.gameplayFeature.upperCase") + " - " + newGameplayFeature.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.gameplayFeature.upperCase") + ": [" + newGameplayFeature.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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

    @Override
    public String getFileName() {
        return "GameplayFeatures.txt";
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    /**
     * Converts the input string into the respective type number
     * @param featureType The feature type string
     * @return Returns the type number
     */
    public int getGameplayFeatureTypeByName(String featureType){
        switch (featureType){
            case "Graphic": return 0;
            case "Sound": return 1;
            case "Physics": return 3;
            case "Gameplay": return 4;
            case "Control": return 5;
            case "Multiplayer": return 6;
        }
        return 10;
    }

    /**
     * Converts the input in into the respective type name
     * @param typeId The feature type id
     * @return Returns the type name
     */
    public String getGameplayFeatureNameByTypeId(int typeId){
        switch (typeId){
            case 0 : return "Graphic";
            case 1 : return "Sound";
            case 3 : return "Physics";
            case 4 : return "Gameplay";
            case 5 : return "Control";
            case 6 : return "Multiplayer";
        }
        return "";
    }
}
