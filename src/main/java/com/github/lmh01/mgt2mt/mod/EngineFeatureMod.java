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
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Summaries;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

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
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0"};
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.engineFeature.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "engineFeature.txt";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.engineFeature.upperCase");
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
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);

            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterFeatureName"));
            panelName.add(labelName);
            panelName.add(textFieldName);

            JButton buttonAddNameTranslations = new JButton(I18n.INSTANCE.get("commonText.addNameTranslations"));
            buttonAddNameTranslations.setToolTipText(I18n.INSTANCE.get("commonText.addNameTranslations.toolTip"));
            buttonAddNameTranslations.addActionListener(actionEvent -> {
                if(!nameTranslationsAdded.get()){
                    mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                    if(mapNameTranslations[0].size() > 0){
                        nameTranslationsAdded.set(true);
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("commonText.name") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                        mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                        if(mapNameTranslations[0].size() > 0){
                            nameTranslationsAdded.set(true);
                        }
                    }
                }
            });

            JPanel panelDescription = new JPanel();
            JLabel labelDescription = new JLabel(I18n.INSTANCE.get("commonText.description") + ":");
            JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("commonText.enterDescription"));
            panelDescription.add(labelDescription);
            panelDescription.add(textFieldDescription);

            JButton buttonAddDescriptionTranslations = new JButton(I18n.INSTANCE.get("commonText.addDescriptionTranslations"));
            buttonAddDescriptionTranslations.setToolTipText(I18n.INSTANCE.get("commonText.addDescriptionTranslations.toolTip"));
            buttonAddDescriptionTranslations.addActionListener(actionEvent -> {
                if(!descriptionTranslationsAdded.get()){
                    mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                    if(mapDescriptionTranslations[0].size() > 0){
                        descriptionTranslationsAdded.set(true);
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                        mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                        if(mapDescriptionTranslations[0].size() > 0){
                            descriptionTranslationsAdded.set(true);
                        }
                    }
                }
            });

            JPanel panelType = new JPanel();
            JLabel labelSelectType = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
            JComboBox comboBoxFeatureType = new JComboBox();
            comboBoxFeatureType.setToolTipText(I18n.INSTANCE.get("mod.engineFeature.addMod.components.type.toolTip"));
            comboBoxFeatureType.setModel(new DefaultComboBoxModel<>(new String[]{"Graphic", "Sound", "Artificial Intelligence", "Physics"}));
            comboBoxFeatureType.setSelectedItem("Graphic");
            panelType.add(labelSelectType);
            panelType.add(comboBoxFeatureType);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel(I18n.INSTANCE.get("commonText.unlockMonth") + ":");
            JComboBox comboBoxUnlockMonth = new JComboBox();
            comboBoxUnlockMonth.setToolTipText(I18n.INSTANCE.get("commonText.unlockMonth.toolTip"));
            comboBoxUnlockMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            comboBoxUnlockMonth.setSelectedItem("JAN");
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel(I18n.INSTANCE.get("commonText.unlockYear") + ":");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockYear.add(labelUnlockYear);
            panelUnlockYear.add(spinnerUnlockYear);

            JPanel panelResearchPoints = new JPanel();
            JPanel panelDevelopmentCost = new JPanel();
            JPanel panelPrice = new JPanel();
            JLabel labelResearchPoints = new JLabel(I18n.INSTANCE.get("commonText.researchPointCost") + ":");
            JLabel labelDevelopmentCost = new JLabel(I18n.INSTANCE.get("commonText.developmentCost") + ":");
            JLabel labelPrice = new JLabel(I18n.INSTANCE.get("commonText.researchCost") + ":");
            JSpinner spinnerResearchPoints = new JSpinner();
            JSpinner spinnerDevelopmentCost = new JSpinner();
            JSpinner spinnerPrice = new JSpinner();
            spinnerResearchPoints.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 10.000; " + I18n.INSTANCE.get("commonText.default") + ": 5.000]" + "<br>" + I18n.INSTANCE.get("commonText.researchPointCost.spinner.toolTip"));
            spinnerDevelopmentCost.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 100.000; " + I18n.INSTANCE.get("commonText.default") + ": 35.000]" + "<br>" + I18n.INSTANCE.get("commonText.developmentCost.spinner.toolTip"));
            spinnerPrice.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 1.000.000; " + I18n.INSTANCE.get("commonText.default") + ": 50.000]" + "<br>" + I18n.INSTANCE.get("commonText.researchCost.spinner.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerResearchPoints.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                spinnerPrice.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerPrice.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerResearchPoints.setModel(new SpinnerNumberModel(500, 0, 10000, 100));
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(35000, 0, 100000, 5000));
                spinnerPrice.setModel(new SpinnerNumberModel(50000, 0, 1000000, 5000));
                ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(false);
                ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(false);
                ((JSpinner.DefaultEditor)spinnerPrice.getEditor()).getTextField().setEditable(false);
            }
            panelResearchPoints.add(labelResearchPoints);
            panelResearchPoints.add(spinnerResearchPoints);
            panelDevelopmentCost.add(labelDevelopmentCost);
            panelDevelopmentCost.add(spinnerDevelopmentCost);
            panelPrice.add(labelPrice);
            panelPrice.add(spinnerPrice);

            JPanel panelTechLevel = new JPanel();
            JLabel labelTechLevel = new JLabel(I18n.INSTANCE.get("commonText.techLevel") + ":");
            JSpinner spinnerTechLevel = new JSpinner();
            spinnerTechLevel.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1 - 8; " + I18n.INSTANCE.get("commonText.default") + ": 1]" + "<br>" + I18n.INSTANCE.get("commonText.techLevel.spinner.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerTechLevel.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerTechLevel.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerTechLevel.setModel(new SpinnerNumberModel(1, 1, 8, 1));
                ((JSpinner.DefaultEditor)spinnerTechLevel.getEditor()).getTextField().setEditable(false);
            }
            panelTechLevel.add(labelTechLevel);
            panelTechLevel.add(spinnerTechLevel);

            JPanel panelGameplay = new JPanel();
            JPanel panelGraphic = new JPanel();
            JPanel panelSound = new JPanel();
            JPanel panelTech = new JPanel();
            JLabel labelGameplay = new JLabel(I18n.INSTANCE.get("commonText.gameplay") + ":");
            JLabel labelGraphic = new JLabel(I18n.INSTANCE.get("commonText.graphic") + ":");
            JLabel labelSound = new JLabel(I18n.INSTANCE.get("commonText.sound") + ":");
            JLabel labelTech = new JLabel(I18n.INSTANCE.get("commonText.tech") + ":");
            JSpinner spinnerGameplay = new JSpinner();
            JSpinner spinnerGraphic = new JSpinner();
            JSpinner spinnerSound = new JSpinner();
            JSpinner spinnerTech = new JSpinner();
            spinnerGameplay.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 4.500; " + I18n.INSTANCE.get("commonText.default") + ": 100]" + "<br>" + I18n.INSTANCE.get("commonText.points.spinner.toolTip"));
            spinnerGraphic.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 4.500; " + I18n.INSTANCE.get("commonText.default") + ": 100]" + "<br>" + I18n.INSTANCE.get("commonText.points.spinner.toolTip"));
            spinnerSound.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 4.500; " + I18n.INSTANCE.get("commonText.default") + ": 100]" + "<br>" + I18n.INSTANCE.get("commonText.points.spinner.toolTip"));
            spinnerTech.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 4.500; " + I18n.INSTANCE.get("commonText.default") + ": 100]" + "<br>" + I18n.INSTANCE.get("commonText.points.spinner.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerGameplay.setModel(new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 5));
                spinnerGraphic.setModel(new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 5));
                spinnerSound.setModel(new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 5));
                spinnerTech.setModel(new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 5));
                ((JSpinner.DefaultEditor)spinnerGameplay.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerGraphic.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerSound.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerTech.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerGameplay.setModel(new SpinnerNumberModel(100, 0, 4500, 5));
                spinnerGraphic.setModel(new SpinnerNumberModel(100, 0, 4500, 5));
                spinnerSound.setModel(new SpinnerNumberModel(100, 0, 4500, 5));
                spinnerTech.setModel(new SpinnerNumberModel(100, 0, 4500, 5));
                ((JSpinner.DefaultEditor)spinnerGameplay.getEditor()).getTextField().setEditable(false);
                ((JSpinner.DefaultEditor)spinnerGraphic.getEditor()).getTextField().setEditable(false);
                ((JSpinner.DefaultEditor)spinnerSound.getEditor()).getTextField().setEditable(false);
                ((JSpinner.DefaultEditor)spinnerTech.getEditor()).getTextField().setEditable(false);
            }
            panelGameplay.add(labelGameplay);
            panelGameplay.add(spinnerGameplay);
            panelGraphic.add(labelGraphic);
            panelGraphic.add(spinnerGraphic);
            panelSound.add(labelSound);
            panelSound.add(spinnerSound);
            panelTech.add(labelTech);
            panelTech.add(spinnerTech);

            Object[] params = {panelName, buttonAddNameTranslations, panelDescription, buttonAddDescriptionTranslations, panelType, panelUnlockMonth, panelUnlockYear, panelResearchPoints, panelDevelopmentCost, panelPrice, panelTechLevel, panelGameplay, panelGraphic, panelSound, panelTech};
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
                            newEngineFeature.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString());
                            newEngineFeature.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                            newEngineFeature.put("PRICE", spinnerPrice.getValue().toString());
                            newEngineFeature.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            newEngineFeature.put("TECHLEVEL", spinnerTechLevel.getValue().toString());
                            newEngineFeature.put("PIC", "");
                            newEngineFeature.put("GAMEPLAY", spinnerGameplay.getValue().toString());
                            newEngineFeature.put("GRAPHIC", spinnerGraphic.getValue().toString());
                            newEngineFeature.put("SOUND", spinnerSound.getValue().toString());
                            newEngineFeature.put("TECH", spinnerTech.getValue().toString());
                            boolean addFeature = Summaries.showSummary(ModManager.engineFeatureMod.getSharer().getOptionPaneMessage(newEngineFeature), I18n.INSTANCE.get("mod.engineFeature.addMod.title"));
                            if (addFeature) {
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
        }
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//EngineFeatures.txt");
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
