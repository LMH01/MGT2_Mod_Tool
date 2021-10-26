package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.*;
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

public class GameplayFeatureMod extends AbstractAdvancedDependentMod {

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
        if (map.get("NO_ARCADE") != null) {
            bw.write("[NO_ARCADE]");
            bw.write("\r\n");
        }
        if (map.get("NO_MOBILE") != null) {
            bw.write("[NO_MOBILE]");
            bw.write("\r\n");
        }
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", MadGamesTycoon2ModTool.VERSION};
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
    public String getExportType() {
        return "gameplay_feature";
    }

    @Override
    public String getGameFileName() {
        return "GameplayFeatures.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_gameplay_features.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
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
        JComboBox<String> comboBoxFeatureType = WindowHelper.getComboBox(GameplayFeatureType.class, "mod.gameplayFeature.addMod.components.type.toolTip", GameplayFeatureType.GAMEPLAY.getTypeName());
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
            try {
                ArrayList<Integer> goodGenrePositions = Utils.getSelectedEntriesOld("Select the genre(s) that don't work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreMod.getContentByAlphabet(), ModManager.genreMod.getContentByAlphabet(), true);
                ArrayList<Integer> badGenreIdsOut = new ArrayList<>();
                for (Integer integer : goodGenrePositions) {
                    badGenreIdsOut.add(ModManager.genreMod.getContentIdByName(ModManager.genreMod.getContentByAlphabet()[integer]));
                }
                badGenreIds[0] = badGenreIdsOut;
                if (badGenreIds[0].size() != 0) {
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if (Settings.disableSafetyFeatures || !mutualEntries) {
                        buttonBadGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.selected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.selected"));
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres.unableToSave"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        badGenreIds[0].clear();
                    }
                } else {
                    buttonBadGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectBadGenres"));
                }
            } catch (ModProcessingException e) {
                ModManager.showException(e);
            }
        });
        JButton buttonGoodGenres = new JButton(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres"));
        buttonGoodGenres.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.toolTip"));
        buttonGoodGenres.addActionListener(actionEvent -> {
            try {
                ArrayList<Integer> goodGenrePositions = Utils.getSelectedEntriesOld("Select the genre(s) that work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), ModManager.genreMod.getContentByAlphabet(), ModManager.genreMod.getContentByAlphabet(), true);
                ArrayList<Integer> goodGenreIdsOut = new ArrayList<>();
                for (Integer integer : goodGenrePositions) {
                    try {
                        goodGenreIdsOut.add(ModManager.genreMod.getContentIdByName(ModManager.genreMod.getContentByAlphabet()[integer]));
                    } catch (ModProcessingException e) {
                        ModManager.showException(e);
                    }
                }
                goodGenreIds[0] = goodGenreIdsOut;
                if (goodGenreIds[0].size() != 0) {
                    boolean mutualEntries = Utils.checkForMutualEntries(badGenreIds[0], goodGenreIds[0]);
                    if (Settings.disableSafetyFeatures || !mutualEntries) {
                        buttonGoodGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.selected"));
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.selected"));
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.unableToSave"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        goodGenreIds[0].clear();
                    }
                } else {
                    buttonGoodGenres.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres"));
                }
            } catch (ModProcessingException e) {
                ModManager.showException(e);
            }
        });

        JCheckBox checkBoxCompatibleWithArcadeCabinets = new JCheckBox(I18n.INSTANCE.get("commonText.arcadeCompatibility"));
        checkBoxCompatibleWithArcadeCabinets.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.arcadeCabinetCompatibility.toolTip"));
        checkBoxCompatibleWithArcadeCabinets.setSelected(true);

        JCheckBox checkBoxCompatibleWithMobile = new JCheckBox(I18n.INSTANCE.get("commonText.mobileCompatibility"));
        checkBoxCompatibleWithMobile.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.mobileCompatibility.toolTip"));
        checkBoxCompatibleWithMobile.setSelected(true);

        Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerResearchCost, SpinnerType.RESEARCH_COST), WindowHelper.getSpinnerPanel(spinnerGameplay, SpinnerType.GAMEPLAY), WindowHelper.getSpinnerPanel(spinnerGraphic, SpinnerType.GRAPHIC), WindowHelper.getSpinnerPanel(spinnerSound, SpinnerType.SOUND), WindowHelper.getSpinnerPanel(spinnerTech, SpinnerType.TECH), buttonBadGenres, buttonGoodGenres, checkBoxCompatibleWithArcadeCabinets, checkBoxCompatibleWithMobile};
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
                        Map<String, String> newGameplayFeature = new HashMap<>();
                        if (!nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()) {
                            newGameplayFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newGameplayFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        } else if (!nameTranslationsAdded.get() && descriptionTranslationsAdded.get()) {
                            newGameplayFeature.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                        } else if (nameTranslationsAdded.get() && !descriptionTranslationsAdded.get()) {
                            newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            newGameplayFeature.putAll(TranslationManager.getDefaultDescriptionTranslations(textFieldDescription.getText()));
                        } else {
                            newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            newGameplayFeature.putAll(TranslationManager.transformTranslationMap(mapDescriptionTranslations[0], "DESC"));
                            newGameplayFeature.put("NAME EN", textFieldName.getText());
                            newGameplayFeature.put("DESC EN", textFieldDescription.getText());
                        }
                        newGameplayFeature.put("ID", Integer.toString(getFreeId()));
                        newGameplayFeature.put("DATE", Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString());
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
                        for (GameplayFeatureType gameplayFeatureType : GameplayFeatureType.values()) {
                            if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(gameplayFeatureType.getTypeName())) {
                                newGameplayFeature.put("TYP", Integer.toString(gameplayFeatureType.getId()));
                            }
                        }
                        if (!checkBoxCompatibleWithArcadeCabinets.isSelected()) {
                            newGameplayFeature.put("NO_ARCADE", "");
                        }
                        if (!checkBoxCompatibleWithMobile.isSelected()) {
                            newGameplayFeature.put("NO_MOBILE", "");
                        }
                        boolean addFeature = Summaries.showSummary(getOptionPaneMessage(newGameplayFeature), I18n.INSTANCE.get("mod.gameplayFeature.addMod.title"));
                        if (addFeature) {
                            createBackup();
                            addModToFile(newGameplayFeature);
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.gameplayFeature.upperCase") + " - " + newGameplayFeature.get("NAME EN"));
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.gameplayFeature.upperCase") + ": [" + newGameplayFeature.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        Map<String, String> workingMap = new HashMap<>(map);
        if (!workingMap.get("BAD").matches(".*\\d.*")) {
            ArrayList<String> badGenreNames = Utils.getEntriesFromString(workingMap.get("BAD"));
            ArrayList<String> goodGenreNames = Utils.getEntriesFromString(workingMap.get("GOOD"));
            ArrayList<Integer> badGenreIds = new ArrayList<>();
            ArrayList<Integer> goodGenreIds = new ArrayList<>();
            for (String string : badGenreNames) {
                try {
                    badGenreIds.add(Integer.parseInt(string));
                } catch (NumberFormatException e) {
                    int numberToAdd = ModManager.genreMod.getContentIdByName(string);
                    if (numberToAdd != -1) {
                        badGenreIds.add(numberToAdd);
                    }
                }
            }
            for (String string : goodGenreNames) {
                try {
                    goodGenreIds.add(Integer.parseInt(string));
                } catch (NumberFormatException e) {
                    int numberToAdd = ModManager.genreMod.getContentIdByName(string);
                    if (numberToAdd != -1) {
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
        if (workingMap.get("BAD").equals("")) {
            badGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        } else {
            for (String string : Utils.getEntriesFromString(workingMap.get("BAD"))) {
                if (!firstBadFeature) {
                    badGenresFeatures.append(", ");
                } else {
                    firstBadFeature = false;
                }
                badGenresFeatures.append(ModManager.genreMod.getContentNameById(Integer.parseInt(string)));
            }
        }
        StringBuilder goodGenresFeatures = new StringBuilder();
        boolean firstGoodFeature = true;
        if (workingMap.get("GOOD").equals("")) {
            goodGenresFeatures.append(I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.none"));
        } else {
            for (String string : Utils.getEntriesFromString(workingMap.get("GOOD"))) {
                if (!firstGoodFeature) {
                    goodGenresFeatures.append(", ");
                } else {
                    firstGoodFeature = false;
                }
                goodGenresFeatures.append(ModManager.genreMod.getContentNameById(Integer.parseInt(string)));
            }
        }
        String arcadeCompatibility = I18n.INSTANCE.get("commonText.yes");
        String mobileCompatibility = I18n.INSTANCE.get("commonText.yes");
        if (workingMap.get("NO_ARCADE") != null) {
            arcadeCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        if (workingMap.get("NO_MOBILE") != null) {
            mobileCompatibility = I18n.INSTANCE.get("commonText.no");
        }
        return I18n.INSTANCE.get("mod.gameplayFeature.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + workingMap.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.description") + ": " + workingMap.get("DESC EN") + "\n" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + workingMap.get("DATE") + "\n" +
                I18n.INSTANCE.get("commonText.type") + ": " + GameplayFeatureType.getTypeNameById(Integer.parseInt(workingMap.get("TYP"))) + "\n" +
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
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "GOOD");
        replaceMapEntry(map, missingDependency, replacement, "BAD");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.genreMod);
        return arrayList;
    }

    @Override
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException, NullPointerException, NumberFormatException {
        if (map.containsKey("GOOD")) {
            map.replace("GOOD", ModManager.genreMod.getGenreNames(map.get("GOOD")));
        }
        if (map.containsKey("BAD")) {
            map.replace("BAD", ModManager.genreMod.getGenreNames(map.get("BAD")));
        }
        return map;
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        Map<String, String> modMap = transformGenericToMap(t);
        Map<String, Object> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        if (modMap.containsKey("GOOD")) {
            set.addAll(Utils.getEntriesFromString(modMap.get("GOOD")));
        }
        if (modMap.containsKey("BAD")) {
            set.addAll(Utils.getEntriesFromString(modMap.get("BAD")));
        }
        map.put(ModManager.genreMod.getExportType(), set);
        return map;
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException, NullPointerException, NumberFormatException {
        if (map.containsKey("GOOD")) {
            map.replace("GOOD", getGenreIds(map.get("GOOD")));
        }
        if (map.containsKey("BAD")) {
            map.replace("BAD", getGenreIds(map.get("BAD")));
        }
        return map;
    }

    /**
     * Edits the GameplayFeatures.txt file to add genre id to the input gameplay feature
     *
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId                   The genre id that should be used
     * @param goodFeature               True when the id should be added as good to the feature. False when it should be added as bad.
     */
    public void addGenreId(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean goodFeature) throws ModProcessingException {
        editGenreIdAllocation(gameplayFeaturesIdsToEdit, genreId, true, goodFeature);
    }

    /**
     * Edits the GameplayFeatures.txt file to remove genre id from the input gameplay feature
     *
     * @param genreId The genre id that should be used
     */
    public void removeGenreId(int genreId) throws ModProcessingException {
        Set<Integer> set = new HashSet<>();
        editGenreIdAllocation(set, genreId, false, false);
    }

    /**
     * Edits the GameplayFeatures.txt file to add/remove genre ids to/from the input gameplay feature
     *
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId                   The genre id that should be used
     * @param addGenreId                True when the genre id should be added. False when the genre id should be removed.
     * @param goodFeature               True when the id should be added as good to the feature. False when it should be added as bad.
     */
    private void editGenreIdAllocation(Set<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean addGenreId, boolean goodFeature) throws ModProcessingException {
        analyzeFile();
        try {
            File gameplayFeaturesFile = getGameFile();
            if (gameplayFeaturesFile.exists()) {
                gameplayFeaturesFile.delete();
            }
            gameplayFeaturesFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeaturesFile), StandardCharsets.UTF_8));
            bw.write("\ufeff");
            for (Map<String, String> map : getFileContent()) {
                boolean activeGameplayFeature = false;
                for (Integer integer : gameplayFeaturesIdsToEdit) {
                    if (map.get("ID").equals(Integer.toString(integer))) {
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
                if (map.get("BAD") != null) {
                    mapValueBad = map.get("BAD");
                }
                if (map.get("GOOD") != null) {
                    mapValueGood = map.get("GOOD");
                }
                if (activeGameplayFeature || !addGenreId) {
                    if (addGenreId) {
                        if (goodFeature) {
                            bw.write("[GOOD]" + mapValueGood);
                            bw.write("<" + genreId + ">");
                            bw.write("\r\n");
                            bw.write("[BAD]" + mapValueBad);
                            bw.write("\r\n");
                        } else {
                            bw.write("[GOOD]" + mapValueGood);
                            bw.write("\r\n");
                            bw.write("[BAD]" + mapValueBad);
                            bw.write("<" + genreId + ">");
                            bw.write("\r\n");
                        }
                    } else {
                        bw.write("[GOOD]" + mapValueGood.replace("<" + genreId + ">", ""));
                        bw.write("\r\n");
                        bw.write("[BAD]" + mapValueBad.replace("<" + genreId + ">", ""));
                        bw.write("\r\n");
                    }
                } else {
                    bw.write("[GOOD]" + mapValueGood);
                    bw.write("\r\n");
                    bw.write("[BAD]" + mapValueBad);
                    bw.write("\r\n");
                }
                if (map.get("NO_ARCADE") != null) {
                    bw.write("[NO_ARCADE]");
                    bw.write("\r\n");
                }
                if (map.get("NO_MOBILE") != null) {
                    bw.write("[NO_MOBILE]");
                    bw.write("\r\n");
                }
                bw.write("\r\n");
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
        }
    }

    /**
     * This function uses {@link AbstractAdvancedMod#getModIdByNameFromImportHelperMap(String)} so the import map
     * has to be initialized when thin function is called. This means that the genre ids will be set correctly.
     * @param genreNamesRaw The string containing the genre ids that should be transformed.
     * @return A list of genre names
     * @throws ModProcessingException If import helper throws an exception
     */
    private String getGenreIds(String genreNamesRaw) throws ModProcessingException {
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentString = new StringBuilder();
        for (int i = 0; i < genreNamesRaw.length(); i++) {
            if (String.valueOf(genreNamesRaw.charAt(charPosition)).equals("<")) {
                //Nothing happens
            } else if (String.valueOf(genreNamesRaw.charAt(charPosition)).equals(">")) {
                genreNames.append("<").append(ModManager.genreMod.getModIdByNameFromImportHelperMap(currentString.toString()));
                genreNames.append(">");
                currentString = new StringBuilder();
            } else {
                currentString.append(genreNamesRaw.charAt(charPosition));
            }
            charPosition++;
        }
        return genreNames.toString();
    }
}
