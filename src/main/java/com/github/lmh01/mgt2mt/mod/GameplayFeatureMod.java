package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GameplayFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GameplayFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GameplayFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.*;
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
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0"};
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase");
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
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
            final ArrayList<Integer>[] badGenreIds = new ArrayList[]{new ArrayList<>()};
            final ArrayList<Integer>[] goodGenreIds = new ArrayList[]{new ArrayList<>()};
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
            comboBoxFeatureType.setToolTipText("Select what type your gameplay feature should be");
            comboBoxFeatureType.setModel(new DefaultComboBoxModel<>(new String[]{"Graphic", "Sound", "Physics", "Gameplay", "Controls", "Multiplayer"}));
            comboBoxFeatureType.setSelectedItem("Graphic");
            panelType.add(labelSelectType);
            panelType.add(comboBoxFeatureType);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel("Unlock Month:");
            JComboBox comboBoxUnlockMonth = new JComboBox();
            comboBoxUnlockMonth.setToolTipText("This is the month when your gameplay feature will be unlocked.");
            comboBoxUnlockMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            comboBoxUnlockMonth.setSelectedItem("JAN");
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel("Unlock Year:");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2999]<br>This is the year when your gameplay feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your gameplay feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
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
            spinnerResearchPoints.setToolTipText("<html>[Range: 0 - 10.000; Default: 500]<br>Number of required research points to research that gameplay feature.");
            spinnerDevelopmentCost.setToolTipText("<html>[Range: 0 - 100.000; Default: 35000]<br>Set the development cost for a game with your gameplay feature.<br>This cost will be added when developing a game with this gameplay feature.");
            spinnerPrice.setToolTipText("<html>[Range: 0 - 1.000.000; Default: 50000]<br>This is the research cost, it is being payed when researching this gameplay feature.");
            if(Settings.disableSafetyFeatures){
                spinnerResearchPoints.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
                spinnerPrice.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
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
            spinnerGameplay.setToolTipText("<html>[Range: 0 - 300; Default: 10]<br>The amount of gameplay points that are added when a game is developed with this feature.");
            spinnerGraphic.setToolTipText("<html>[Range: 0 - 300; Default: 10]<br>The amount of graphic points that are added when a game is developed with this feature.");
            spinnerSound.setToolTipText("<html>[Range: 0 - 300; Default: 10]<br>The amount of sound points that are added when a game is developed with this feature.");
            spinnerTech.setToolTipText("<html>[Range: 0 - 300; Default: 10]<br>The amount of tech points that are added when a game is developed with this feature.");
            if(Settings.disableSafetyFeatures){
                spinnerGameplay.setModel(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 5));
                spinnerGraphic.setModel(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 5));
                spinnerSound.setModel(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 5));
                spinnerTech.setModel(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 5));
                ((JSpinner.DefaultEditor)spinnerGameplay.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerGraphic.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerSound.getEditor()).getTextField().setEditable(true);
                ((JSpinner.DefaultEditor)spinnerTech.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerGameplay.setModel(new SpinnerNumberModel(10, 0, 300, 5));
                spinnerGraphic.setModel(new SpinnerNumberModel(10, 0, 300, 5));
                spinnerSound.setModel(new SpinnerNumberModel(10, 0, 300, 5));
                spinnerTech.setModel(new SpinnerNumberModel(10, 0, 300, 5));
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

            JButton buttonBadGenres = new JButton("Select Bad Genres");
            buttonBadGenres.setToolTipText("Click to select what genres don't work good with your gameplay feature");
            buttonBadGenres.addActionListener(actionEvent -> {
                badGenreIds[0] = Utils.getSelectedEntries("Select the genre(s) that don't work with your gameplay feature", "Choose genre(s)", ModManager.genreMod.getAnalyzer().getContentByAlphabet(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
                if(badGenreIds[0].size() != 0){
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if(Settings.disableSafetyFeatures || !mutualEntries){
                        buttonBadGenres.setText("Bad Genres Selected");
                        JOptionPane.showMessageDialog(null, "Bad genres selected!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to save bad genres:\n\nGood genres list already contains (some) selected entries.", "Unable to save", JOptionPane.ERROR_MESSAGE);
                        badGenreIds[0].clear();
                    }
                }else{
                    buttonBadGenres.setText("Select Bad Genres");
                }
            });
            JButton buttonGoodGenres = new JButton("Select Good Genres");
            buttonGoodGenres.addActionListener(actionEvent -> {
                goodGenreIds[0] = Utils.getSelectedEntries("Select the genre(s) that work with your gameplay feature", "Choose genre(s)", ModManager.genreMod.getAnalyzer().getContentByAlphabet(), ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
                if(goodGenreIds[0].size() != 0){
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if(Settings.disableSafetyFeatures || !mutualEntries){
                        buttonGoodGenres.setText("Good Genres Selected");
                        JOptionPane.showMessageDialog(null, "Good genres selected!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to save good genres:\n\nBad genres list already contains (some) selected entries.", "Unable to save", JOptionPane.ERROR_MESSAGE);
                        goodGenreIds[0].clear();
                    }
                }else{
                    buttonGoodGenres.setText("Select Good Genres");
                }
            });

            JCheckBox checkBoxCompatibleWithArcadeCabinets = new JCheckBox("Arcade cabinet compatibility");
            checkBoxCompatibleWithArcadeCabinets.setToolTipText("<html>Select to enable compatibility with arcade cabinets for this gameplay feature.<br>If disabled this gameplay feature does not work on arcade cabinets.");
            checkBoxCompatibleWithArcadeCabinets.setSelected(true);

            JCheckBox checkBoxCompatibleWithMobile = new JCheckBox("Mobile compatibility");
            checkBoxCompatibleWithMobile.setToolTipText("<html>Select to enable compatibility with mobile devices for this gameplay feature.<br>If disabled this gameplay feature does not work on mobile devices.");
            checkBoxCompatibleWithMobile.setSelected(true);

            Object[] params = {panelName, buttonAddNameTranslations, panelDescription, buttonAddDescriptionTranslations, panelType, panelUnlockMonth, panelUnlockYear, panelResearchPoints, panelDevelopmentCost, panelPrice, panelGameplay, panelGraphic, panelSound, panelTech, buttonBadGenres, buttonGoodGenres, checkBoxCompatibleWithArcadeCabinets, checkBoxCompatibleWithMobile};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, "Add Gameplay Feature", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(textFieldName.getText().isEmpty() || textFieldName.getText().equals("ENTER FEATURE NAME") || textFieldDescription.getText().isEmpty() || textFieldDescription.getText().equals("ENTER FEATURE DESCRIPTION")){
                        JOptionPane.showMessageDialog(null, "Unable to add gameplay feature: please enter a name/description first!", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                    }else{
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
                        newGameplayFeature.put("TYP", Integer.toString(getGameplayFeatureTypeByName(comboBoxFeatureType.getSelectedItem().toString())));
                        newGameplayFeature.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString());
                        newGameplayFeature.put("RES POINTS", spinnerResearchPoints.getValue().toString());
                        newGameplayFeature.put("PRICE", spinnerPrice.getValue().toString());
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
                            ModManager.gameplayFeatureMod.getEditor().addMod(newGameplayFeature);
                            JOptionPane.showMessageDialog(null, "Gameplay feature: [" + newGameplayFeature.get("NAME EN") + "] has been added successfully!", "Gameplay feature added", JOptionPane.INFORMATION_MESSAGE);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.gameplayFeature") + " - " + newGameplayFeature.get("NAME EN"));
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while adding gameplay feature:\n\n" + e.getMessage(), "Error while adding gameplay feature", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getFileName() {
        return "gameplayFeature.txt";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//GameplayFeatures.txt");
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
