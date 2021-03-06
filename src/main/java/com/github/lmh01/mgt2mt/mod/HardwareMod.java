package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.HardwareAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.HardwareEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.HardwareSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
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

public class HardwareMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareMod.class);
    HardwareAnalyzer hardwareAnalyzer = new HardwareAnalyzer();
    HardwareEditor hardwareEditor = new HardwareEditor();
    HardwareSharer hardwareSharer = new HardwareSharer();
    public ArrayList<JMenuItem> hardwareModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return hardwareAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return hardwareEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return hardwareSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.hardwareMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return hardwareModMenuItems;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.1.0", "2.1.1"};
    }

    @Override
    public void menuActionAddMod() {
        try{
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
                if(checkBoxEnableExclusivity.isSelected() != lastValue.get()){
                    if(checkBoxEnableExclusivity.isSelected()){
                        comboBoxExclusivity.setEnabled(true);
                    }else{
                        comboBoxExclusivity.setEnabled(false);
                    }
                    lastValue.set(checkBoxEnableExclusivity.isSelected());
                }
            });

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getTypePanel(comboBoxType), WindowHelper.getSpinnerPanel(spinnerResearchPoints, 6), WindowHelper.getSpinnerPanel(spinnerCost, 8), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7), WindowHelper.getSpinnerPanel(spinnerTechLevel, 4), checkBoxEnableExclusivity, comboBoxExclusivity};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldName.initialValue")) && !textFieldDescription.getText().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.textFieldDescription.initialValue"))){
                        boolean modAlreadyExists = false;
                        for(String string : getBaseAnalyzer().getContentByAlphabet()){
                            if(textFieldName.getText().equals(string)){
                                modAlreadyExists = true;
                            }
                        }
                        if(!modAlreadyExists){
                            Map<String, String> hardwareFeatureMap = new HashMap<>();
                            hardwareFeatureMap.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                            if(!nameTranslationsAdded.get()){
                                hardwareFeatureMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            }else{
                                hardwareFeatureMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                hardwareFeatureMap.put("NAME EN", textFieldName.getText());
                            }
                            if(!descriptionTranslationsAdded.get()){
                                hardwareFeatureMap.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                            }else{
                                hardwareFeatureMap.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslation[0], "DESC"));
                                hardwareFeatureMap.put("DESC EN", textFieldDescription.getText());
                            }
                            hardwareFeatureMap.put("DATE", Objects.requireNonNull(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem())).toString() + " " + spinnerUnlockYear.getValue().toString());
                            hardwareFeatureMap.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                            hardwareFeatureMap.put("PRICE", spinnerCost.getValue().toString());
                            hardwareFeatureMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            hardwareFeatureMap.put("TYP", Integer.toString(getHardwareTypeIdByName(Objects.requireNonNull(comboBoxType.getSelectedItem()).toString())));
                            hardwareFeatureMap.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                            if(checkBoxEnableExclusivity.isSelected()){
                                if(Objects.requireNonNull(comboBoxExclusivity.getSelectedItem()).toString().equals(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.exclusiveConsole.value1"))){
                                    hardwareFeatureMap.put("ONLY_STATIONARY", "");
                                }else{
                                    hardwareFeatureMap.put("ONLY_HANDHELD", "");
                                }
                            }
                            if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(hardwareFeatureMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                Backup.createBackup(getFile());
                                getBaseEditor().addMod(hardwareFeatureMap);
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.hardwareFeature.upperCase") + " - " + hardwareFeatureMap.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.hardwareFeature.upperCase") + ": [" + hardwareFeatureMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameDescriptionFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            TextAreaHelper.appendText(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "hardware";
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "Hardware.txt";
    }

    /**
     * Converts the input id into the respective type name
     * @param typeName The feature type id
     * @return Returns the type name
     */
    public int getHardwareTypeIdByName(String typeName){
        if(typeName.equals(I18n.INSTANCE.get("commonText.cpu"))){
            return 0;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.gpu"))){
            return 1;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.ram"))){
            return 2;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.storage"))){
            return 3;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.audio"))){
            return 4;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.cooling"))){
            return 5;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.gameStorageDevice"))){
            return 6;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.controller"))){
            return 7;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.case"))){
            return 8;
        }else if(typeName.equals(I18n.INSTANCE.get("commonText.screen"))){
            return 9;
        }else{
            throw new IllegalArgumentException("The input for the function typeName is invalid! For valid inputs see function; Was: " + typeName);
        }
    }

    /**
     * Converts the input id into the respective type name
     * @param typeId The feature type id
     * @return Returns the type name
     */
    public String getHardwareTypeNameById(int typeId){
        switch (typeId){
            case 0: return I18n.INSTANCE.get("commonText.cpu");
            case 1: return I18n.INSTANCE.get("commonText.gpu");
            case 2: return I18n.INSTANCE.get("commonText.ram");
            case 3: return I18n.INSTANCE.get("commonText.storage");
            case 4: return I18n.INSTANCE.get("commonText.audio");
            case 5: return I18n.INSTANCE.get("commonText.cooling");
            case 6: return I18n.INSTANCE.get("commonText.gameStorageDevice");
            case 7: return I18n.INSTANCE.get("commonText.controller");
            case 8: return I18n.INSTANCE.get("commonText.case");
            case 9: return I18n.INSTANCE.get("commonText.screen");
            default: throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-9; Was: " + typeId);
        }
    }
}
