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
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField("ENTER FEATURE NAME");
            panelName.add(labelName);
            panelName.add(textFieldName);

            JButton buttonAddNameTranslations = new JButton("Add name translations");
            buttonAddNameTranslations.setToolTipText("<html>Click to add name translations<br>The value entered in the main text field will be used as the english translation");
            buttonAddNameTranslations.addActionListener(actionEvent -> {
                if(!nameTranslationsAdded.get()){
                    mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                    if(mapNameTranslations[0].size() > 0){
                        nameTranslationsAdded.set(true);
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, "Name translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                        if(mapNameTranslations[0].size() > 0){
                            nameTranslationsAdded.set(true);
                        }
                    }
                }
            });

            JPanel panelDescription = new JPanel();
            JLabel labelDescription = new JLabel("Description:");
            JTextField textFieldDescription = new JTextField("ENTER FEATURE DESCRIPTION");
            panelDescription.add(labelDescription);
            panelDescription.add(textFieldDescription);

            JButton buttonAddDescriptionTranslations = new JButton("Add description translations");
            buttonAddDescriptionTranslations.setToolTipText("<html>Click to add description translations<br>The value entered in the main text field will be used as the english translation");
            buttonAddDescriptionTranslations.addActionListener(actionEvent -> {
                if(!descriptionTranslationsAdded.get()){
                    mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                    if(mapDescriptionTranslations[0].size() > 0){
                        descriptionTranslationsAdded.set(true);
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, "Description translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                        if(mapDescriptionTranslations[0].size() > 0){
                            descriptionTranslationsAdded.set(true);
                        }
                    }
                }
            });

            JPanel panelType = new JPanel();
            JLabel labelSelectType = new JLabel("Type:");
            JComboBox comboBoxFeatureType = new JComboBox();
            comboBoxFeatureType.setToolTipText("Select what type your engine feature should be");
            comboBoxFeatureType.setModel(new DefaultComboBoxModel<>(new String[]{"Graphic", "Sound", "Artificial Intelligence", "Physics"}));
            comboBoxFeatureType.setSelectedItem("Graphic");
            panelType.add(labelSelectType);
            panelType.add(comboBoxFeatureType);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel("Unlock Month:");
            JComboBox comboBoxUnlockMonth = new JComboBox();
            comboBoxUnlockMonth.setToolTipText("This is the month when your engine feature will be unlocked.");
            comboBoxUnlockMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            comboBoxUnlockMonth.setSelectedItem("JAN");
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel("Unlock Year:");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2999]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockYear.add(labelUnlockYear);
            panelUnlockYear.add(spinnerUnlockYear);

            JPanel panelResearchPoints = new JPanel();
            JPanel panelDevelopmentCost = new JPanel();
            JPanel panelPrice = new JPanel();
            JLabel labelResearchPoints = new JLabel("Research points: ");
            JLabel labelDevelopmentCost = new JLabel("Development cost: ");
            JLabel labelPrice = new JLabel("Research cost: ");
            JSpinner spinnerResearchPoints = new JSpinner();
            JSpinner spinnerDevelopmentCost = new JSpinner();
            JSpinner spinnerPrice = new JSpinner();
            spinnerResearchPoints.setToolTipText("<html>[Range: 0 - 10.000; Default: 500]<br>Number of required research points to research that engine feature.");
            spinnerDevelopmentCost.setToolTipText("<html>[Range: 0 - 100.000; Default: 35000]<br>Set the development cost for a game with your engine feature.<br>This cost will be added when developing a game with this engine feature.");
            spinnerPrice.setToolTipText("<html>[Range: 0 - 1.000.000; Default: 50000]<br>This is the research cost, it is being payed when researching this engine feature.");
            if(Settings.disableSafetyFeatures){
                spinnerResearchPoints.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                spinnerPrice.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerPrice.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerResearchPoints.setModel(new SpinnerNumberModel(500, 0, 10000, 100));
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(35000, 0, 100000, 1000));
                spinnerPrice.setModel(new SpinnerNumberModel(50000, 0, 1000000, 1000));
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
            JLabel labelTechLevel = new JLabel("Tech Level:");
            JSpinner spinnerTechLevel = new JSpinner();
            spinnerTechLevel.setToolTipText("<html>[Range: 1 - 8; Default: 1]<br>The tech level of the engine feature.<br>A higher level means a more advanced feature.");
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
            JLabel labelGameplay = new JLabel("Gameplay:");
            JLabel labelGraphic = new JLabel("Graphic:");
            JLabel labelSound = new JLabel("Sound:");
            JLabel labelTech = new JLabel("Tech:");
            JSpinner spinnerGameplay = new JSpinner();
            JSpinner spinnerGraphic = new JSpinner();
            JSpinner spinnerSound = new JSpinner();
            JSpinner spinnerTech = new JSpinner();
            spinnerGameplay.setToolTipText("<html>[Range: 0 - 4500; Default: 10]<br>The amount of gameplay points that are added when a game is developed with this feature.");
            spinnerGraphic.setToolTipText("<html>[Range: 0 - 4500; Default: 10]<br>The amount of graphic points that are added when a game is developed with this feature.");
            spinnerSound.setToolTipText("<html>[Range: 0 - 4500; Default: 10]<br>The amount of sound points that are added when a game is developed with this feature.");
            spinnerTech.setToolTipText("<html>[Range: 0 - 4500; Default: 10]<br>The amount of tech points that are added when a game is developed with this feature.");
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
                if(JOptionPane.showConfirmDialog(null, params, "Add Engine Feature", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(textFieldName.getText().isEmpty() || textFieldName.getText().equals("ENTER FEATURE NAME") || textFieldDescription.getText().isEmpty() || textFieldDescription.getText().equals("ENTER FEATURE DESCRIPTION")){
                        JOptionPane.showMessageDialog(null, "Unable to add engine feature: please enter a name/description first!", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                    }else{
                        Map<String, String> newEngineFeature = new HashMap<>();
                        if(!nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()){
                            newEngineFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newEngineFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        }else if(!nameTranslationsAdded.get() && descriptionTranslationsAdded.get()){
                            newEngineFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                        }else if(nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()){
                            newEngineFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            newEngineFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        }else{
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
                        if(addFeature){
                            ModManager.engineFeatureMod.getEditor().addMod(newEngineFeature);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.engineFeature") + " - " + newEngineFeature.get("NAME EN"));
                            JOptionPane.showMessageDialog(null, "Engine feature: [" + newEngineFeature.get("NAME EN") + "] has been added successfully!", "Engine feature added", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while adding engine feature:\n\n" + e.getMessage(), "Error while adding gameplay feature", JOptionPane.ERROR_MESSAGE);
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
