package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayFeatureMod extends AbstractAdvancedMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("RES POINTS", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("GAMEPLAY", map, bw);
        EditHelper.printLine("GRAPHIC", map, bw);
        EditHelper.printLine("SOUND", map, bw);
        EditHelper.printLine("TECH", map, bw);
        map.putIfAbsent("GOOD", "");
        map.putIfAbsent("BAD", "");
        EditHelper.printLine("GOOD", map, bw);
        EditHelper.printLine("BAD", map, bw);
        if(map.get("NO_ARCADE") != null){
            bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
        }
        if(map.get("NO_MOBILE") != null){
            bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
        }
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
    public AbstractBaseMod getMod() {
        return ModManager.gameplayFeatureMod;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "GameplayFeatures.txt");
    }

    @Override
    protected String getDefaultContentFileName() {
        return "default_gameplay_features.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        try{
            Backup.createBackup(ModManager.gameplayFeatureModOld.getFile());
            ModManager.gameplayFeatureModOld.getAnalyzer().analyzeFile();
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
                ArrayList<Integer> goodGenrePositions =  Utils.getSelectedEntries("Select the genre(s) that don't work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), true);
                ArrayList<Integer> badGenreIdsOut = new ArrayList<>();
                for(Integer integer : goodGenrePositions){
                    badGenreIdsOut.add(ModManager.genreModOld.getAnalyzer().getContentIdByName(ModManager.genreModOld.getAnalyzer().getContentByAlphabet()[integer]));
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
                ArrayList<Integer> goodGenrePositions =  Utils.getSelectedEntries("Select the genre(s) that work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), true);
                ArrayList<Integer> goodGenreIdsOut = new ArrayList<>();
                for(Integer integer : goodGenrePositions){
                    goodGenreIdsOut.add(ModManager.genreModOld.getAnalyzer().getContentIdByName(ModManager.genreModOld.getAnalyzer().getContentByAlphabet()[integer]));
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
                        for(String string : getContentByAlphabet()){
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
                            newGameplayFeature.put("ID", Integer.toString(ModManager.gameplayFeatureModOld.getAnalyzer().getFreeId()));
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
                            boolean addFeature = Summaries.showSummary(ModManager.gameplayFeatureModOld.getSharer().getOptionPaneMessage(newGameplayFeature), I18n.INSTANCE.get("mod.gameplayFeature.addMod.title"));
                            if(addFeature) {
                                Backup.createBackup(getGameFile());
                                ModManager.gameplayFeatureModOld.getEditor().addMod(newGameplayFeature);
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
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        Map<String, String> workingMap = new HashMap<>(map);
        if(!workingMap.get("BAD").matches(".*\\d.*")){
            ArrayList<String> badGenreNames = Utils.getEntriesFromString(workingMap.get("BAD"));
            ArrayList<String> goodGenreNames = Utils.getEntriesFromString(workingMap.get("GOOD"));
            ArrayList<Integer> badGenreIds = new ArrayList<>();
            ArrayList<Integer> goodGenreIds = new ArrayList<>();
            for(String string : badGenreNames){
                try{
                    badGenreIds.add(Integer.parseInt(string));
                }catch(NumberFormatException e){
                    int numberToAdd = ModManager.genreModOld.getAnalyzer().getContentIdByName(string);
                    if(numberToAdd != -1){
                        badGenreIds.add(numberToAdd);
                    }
                }
            }
            for(String string : goodGenreNames){
                try{
                    goodGenreIds.add(Integer.parseInt(string));
                }catch(NumberFormatException e){
                    int numberToAdd = ModManager.genreModOld.getAnalyzer().getContentIdByName(string);
                    if(numberToAdd != -1){
                        goodGenreIds.add(numberToAdd);
                    }
                }
            }
            workingMap.remove("BAD");
            workingMap.remove("GOOD");
            workingMap.put("BAD", Utils.transformArrayListToString(badGenreIds));
            workingMap.put("GOOD", Utils.transformArrayListToString(goodGenreIds));
        }
        StringBuilder badGenresFeatures = new StringBuilder();
        boolean firstBadFeature = true;
        if(workingMap.get("BAD").equals("")){
            badGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        }else{
            for(String string : Utils.getEntriesFromString(workingMap.get("BAD"))){
                if(!firstBadFeature){
                    badGenresFeatures.append(", ");
                }else{
                    firstBadFeature = false;
                }
                badGenresFeatures.append(ModManager.genreModOld.getAnalyzer().getContentNameById(Integer.parseInt(string)));
            }
        }
        StringBuilder goodGenresFeatures = new StringBuilder();
        boolean firstGoodFeature = true;
        if(workingMap.get("GOOD").equals("")){
            goodGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        }else{
            for(String string : Utils.getEntriesFromString(workingMap.get("GOOD"))){
                if(!firstGoodFeature){
                    goodGenresFeatures.append(", ");
                }else{
                    firstGoodFeature = false;
                }
                goodGenresFeatures.append(ModManager.genreModOld.getAnalyzer().getContentNameById(Integer.parseInt(string)));
            }
        }
        String arcadeCompatibility = I18n.INSTANCE.get("commonText.yes");
        String mobileCompatibility = I18n.INSTANCE.get("commonText.yes");
        if(workingMap.get("NO_ARCADE") != null){
            arcadeCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        if(workingMap.get("NO_MOBILE") != null){
            mobileCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        return I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + workingMap.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + workingMap.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + workingMap.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + ModManager.gameplayFeatureModOld.getGameplayFeatureNameByTypeId(Integer.parseInt(workingMap.get("TYP"))) + "\n" +
                I18n.INSTANCE.get("commonText.researchPointCost") + ": " + workingMap.get("RES POINTS") + "\n" +
                I18n.INSTANCE.get("commonText.researchCost") + ": " + workingMap.get("PRICE") + "\n" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + workingMap.get("DEV COSTS") + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.badGenres") + "*\n\n" + badGenresFeatures + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.goodGenres") + "*\n\n" + goodGenresFeatures + "\n" +
                "\n*" + I18n.INSTANCE.get("commonText.points") + "*\n\n" +
                I18n.INSTANCE.get("commonText.gameplay") + ": " + workingMap.get("GAMEPLAY") + "\n" +
                I18n.INSTANCE.get("commonText.graphic") + ": " + workingMap.get("GRAPHIC") + "\n" +
                I18n.INSTANCE.get("commonText.sound") + ": " + workingMap.get("SOUND") + "\n" +
                I18n.INSTANCE.get("commonText.tech") + ": " + workingMap.get("TECH") + "\n" +
                I18n.INSTANCE.get("commonText.arcadeCompatibility") + ": " + arcadeCompatibility + "\n" +
                I18n.INSTANCE.get("commonText.mobileCompatibility") + ": " + mobileCompatibility + "\n";
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
    protected String getTypeCaps() {
        return "GAMEPLAY FEATURE";
    }

    @Override
    public String getImportExportFileName() {
        return "gameplayFeature.txt";
    }

    /**
     * Converts the input string into the respective type number
     * @param featureType The feature type string
     * @return Returns the type number
     */
    public int getGameplayFeatureTypeByName(String featureType) {//TODO rewrite to use enum
        switch (featureType){
            case "Graphic": return 0;
            case "Sound": return 1;
            case "Physics": return 3;
            case "Gameplay": return 4;
            case "Control": return 5;
            case "Multiplayer": return 6;
            default : return 0;
        }
    }

    /**
     * Converts the input in into the respective type name
     * @param typeId The feature type id
     * @return Returns the type name
     */
    public String getGameplayFeatureNameByTypeId(int typeId) {//TODO rewrite to use enum
        switch (typeId){
            case 0 : return "Graphic";
            case 1 : return "Sound";
            case 3 : return "Physics";
            case 4 : return "Gameplay";
            case 5 : return "Control";
            case 6 : return "Multiplayer";
            default : return "Graphic";
        }
    }

    /**
     * Edits the GameplayFeatures.txt file to add genre id to the input gameplay feature
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId The genre id that should be used
     * @param goodFeature True when the id should be added as good to the feature. False when it should be added as bad.
     */
    public void addGenreId(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean goodFeature) throws ModProcessingException {
        editGenreIdAllocation(gameplayFeaturesIdsToEdit, genreId, true, goodFeature);
    }

    /**
     * Edits the GameplayFeatures.txt file to remove genre id from the input gameplay feature
     * @param genreId The genre id that should be used
     */
    public void removeGenreId(int genreId) throws ModProcessingException {
        Set<Integer> set = new HashSet<>();
        editGenreIdAllocation(set, genreId, false, false);
    }

    /**
     * Edits the GameplayFeatures.txt file to add/remove genre ids to/from the input gameplay feature
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId The genre id that should be used
     * @param addGenreId True when the genre id should be added. False when the genre id should be removed.
     * @param goodFeature True when the id should be added as good to the feature. False when it should be added as bad.
     */
    private void editGenreIdAllocation(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean addGenreId, boolean goodFeature) throws ModProcessingException {
        try {
            LOGGER.info("Editing GameplayFeatures.txt file");
            File gameplayFeaturesFile = ModManager.gameplayFeatureModOld.getFile();
            if(gameplayFeaturesFile.exists()){
                gameplayFeaturesFile.delete();
            }
            gameplayFeaturesFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeaturesFile), StandardCharsets.UTF_8));
            bw.write("\ufeff");
            for(Map<String, String> map : ModManager.gameplayFeatureModOld.getAnalyzer().getFileContent()) {
                boolean activeGameplayFeature = false;
                for(Integer integer : gameplayFeaturesIdsToEdit){
                    if(map.get("ID").equals(Integer.toString(integer))){
                        activeGameplayFeature = true;
                    }
                }
                EditHelper.printLine("ID", map, bw);
                EditHelper.printLine("TYP", map, bw);
                TranslationManager.printLanguages(bw, map);
                EditHelper.printLine("DATE", map, bw);
                EditHelper.printLine("RES POINTS", map, bw);
                EditHelper.printLine("PRICE", map, bw);
                EditHelper.printLine("DEV COSTS", map, bw);
                EditHelper.printLine("PIC", map, bw);
                EditHelper.printLine("GAMEPLAY", map, bw);
                EditHelper.printLine("GRAPHIC", map, bw);
                EditHelper.printLine("SOUND", map, bw);
                EditHelper.printLine("TECH", map, bw);
                String mapValueBad = "";
                String mapValueGood = "";
                if(map.get("BAD") != null){
                    mapValueBad = map.get("BAD");
                }
                if(map.get("GOOD") != null){
                    mapValueGood = map.get("GOOD");
                }
                if(activeGameplayFeature || !addGenreId){
                    if(addGenreId){
                        if(goodFeature){
                            bw.write("[GOOD]" + mapValueGood);bw.write("<" + genreId + ">");bw.write(System.getProperty("line.separator"));
                            bw.write("[BAD]" + mapValueBad);bw.write(System.getProperty("line.separator"));
                        }else{
                            bw.write("[GOOD]" + mapValueGood);bw.write(System.getProperty("line.separator"));
                            bw.write("[BAD]" + mapValueBad);bw.write("<" + genreId + ">");bw.write(System.getProperty("line.separator"));
                        }
                    }else{
                        bw.write("[GOOD]" + mapValueGood.replace("<" + genreId + ">", ""));bw.write(System.getProperty("line.separator"));
                        bw.write("[BAD]" + mapValueBad.replace("<" + genreId + ">", ""));bw.write(System.getProperty("line.separator"));
                    }
                }else{
                    bw.write("[GOOD]" + mapValueGood);bw.write(System.getProperty("line.separator"));
                    bw.write("[BAD]" + mapValueBad);bw.write(System.getProperty("line.separator"));
                }
                if(map.get("NO_ARCADE") != null){
                    bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
                }
                if(map.get("NO_MOBILE") != null){
                    bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
                }
                bw.write(System.getProperty("line.separator"));
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType() + ": " + e.getMessage());
        }
    }
}
