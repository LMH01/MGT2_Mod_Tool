package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.GameplayFeature;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.GameplayFeatureType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayFeatureManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GameplayFeatureManager INSTANCE = new GameplayFeatureManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private GameplayFeatureManager() {
        super("gameplayFeature", "gameplay_feature", "default_gameplay_features.txt", MGT2Paths.TEXT_DATA.getPath().resolve("GameplayFeatures.txt").toFile(), StandardCharsets.UTF_8);
    }

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
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        boolean arcade = true;
        if (map.containsKey("NO_ARCADE")) {
            arcade = false;
        }
        boolean mobile = true;
        if (map.containsKey("NO_MOBILE")) {
            mobile = false;
        }
        ArrayList<Integer> badGameplayFeatures = new ArrayList<>();
        if (map.containsKey("BAD")) {
            badGameplayFeatures = Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("BAD")));
        }
        ArrayList<Integer> goodGameplayFeatures = new ArrayList<>();
        if (map.containsKey("BAD")) {
            goodGameplayFeatures = Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("BAD")));
        }
        return new GameplayFeature(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DESC EN"),
                GameplayFeatureType.getFromId(Integer.parseInt(map.get("TYP"))),
                map.get("DATE"),
                Integer.parseInt(map.get("RES POINTS")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")),
                Integer.parseInt(map.get("GAMEPLAY")),
                Integer.parseInt(map.get("GRAPHIC")),
                Integer.parseInt(map.get("SOUND")),
                Integer.parseInt(map.get("TECH")),
                badGameplayFeatures,
                goodGameplayFeatures,
                mobile,
                arcade
        );
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
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
                ArrayList<Integer> goodGenrePositions = Utils.getSelectedEntriesOld("Select the genre(s) that don't work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), GenreManager.INSTANCE.getContentByAlphabet(), GenreManager.INSTANCE.getContentByAlphabet(), true);
                ArrayList<Integer> badGenreIdsOut = new ArrayList<>();
                for (Integer integer : goodGenrePositions) {
                    badGenreIdsOut.add(GenreManager.INSTANCE.getContentIdByName(GenreManager.INSTANCE.getContentByAlphabet()[integer]));
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
                ContentAdministrator.showException(e);
            }
        });
        JButton buttonGoodGenres = new JButton(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres"));
        buttonGoodGenres.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.selectGoodGenres.toolTip"));
        buttonGoodGenres.addActionListener(actionEvent -> {
            try {
                ArrayList<Integer> goodGenrePositions = Utils.getSelectedEntriesOld("Select the genre(s) that work with your gameplay feature", I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.button.genres.title"), GenreManager.INSTANCE.getContentByAlphabet(), GenreManager.INSTANCE.getContentByAlphabet(), true);
                ArrayList<Integer> goodGenreIdsOut = new ArrayList<>();
                for (Integer integer : goodGenrePositions) {
                    try {
                        goodGenreIdsOut.add(GenreManager.INSTANCE.getContentIdByName(GenreManager.INSTANCE.getContentByAlphabet()[integer]));
                    } catch (ModProcessingException e) {
                        ContentAdministrator.showException(e);
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
                ContentAdministrator.showException(e);
            }
        });

        JCheckBox checkBoxCompatibleWithArcadeCabinets = new JCheckBox(I18n.INSTANCE.get("commonText.arcadeCompatibility"));
        checkBoxCompatibleWithArcadeCabinets.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.arcadeCabinetCompatibility.toolTip"));
        checkBoxCompatibleWithArcadeCabinets.setSelected(true);

        JCheckBox checkBoxCompatibleWithMobile = new JCheckBox(I18n.INSTANCE.get("commonText.mobileCompatibility"));
        checkBoxCompatibleWithMobile.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.mobileCompatibility.toolTip"));
        checkBoxCompatibleWithMobile.setSelected(true);

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerResearchCost, SpinnerType.RESEARCH_COST), WindowHelper.getSpinnerPanel(spinnerGameplay, SpinnerType.GAMEPLAY), WindowHelper.getSpinnerPanel(spinnerGraphic, SpinnerType.GRAPHIC), WindowHelper.getSpinnerPanel(spinnerSound, SpinnerType.SOUND), WindowHelper.getSpinnerPanel(spinnerTech, SpinnerType.TECH), buttonBadGenres, buttonGoodGenres, checkBoxCompatibleWithArcadeCabinets, checkBoxCompatibleWithMobile};
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
                        TranslationManager translationManager = new TranslationManager(mapNameTranslations[0], mapDescriptionTranslations[0]);
                        GameplayFeatureType gft = null;
                        for (GameplayFeatureType type : GameplayFeatureType.values()) {
                            if (Objects.requireNonNull(comboBoxFeatureType.getSelectedItem()).toString().equals(type.getTypeName())) {
                                gft = GameplayFeatureType.getFromId(type.getId());
                            }
                        }
                        AbstractBaseContent gameplayFeature = new GameplayFeature(
                                textFieldName.getText(),
                                null,
                                translationManager,
                                textFieldDescription.getText(),
                                gft,
                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                (int)spinnerResearchPoints.getValue(),
                                (int)spinnerResearchCost.getValue(),
                                (int)spinnerDevelopmentCost.getValue(),
                                (int)spinnerGameplay.getValue(),
                                (int)spinnerGraphic.getValue(),
                                (int)spinnerSound.getValue(),
                                (int)spinnerTech.getValue(),
                                badGenreIds[0],
                                goodGenreIds[0],
                                checkBoxCompatibleWithArcadeCabinets.isSelected(),
                                checkBoxCompatibleWithMobile.isSelected()
                        );
                        boolean addFeature = Summaries.showSummary(gameplayFeature.getOptionPaneMessage(), I18n.INSTANCE.get("mod.gameplayFeature.addMod.title"));
                        if (addFeature) {
                            addContent(gameplayFeature);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.gameplayFeature.upperCase") + ": [" + gameplayFeature.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "GOOD");
        replaceMapEntry(map, missingDependency, replacement, "BAD");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }

    /**
     * Edits the GameplayFeatures.txt file to add genre id to the input gameplay feature
     *
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId                   The genre id that should be used
     * @param goodFeature               True when the id should be added as good to the feature. False when it should be added as bad.
     */
    public void addGenreId(ArrayList<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean goodFeature) throws ModProcessingException {
        editGenreIdAllocation(gameplayFeaturesIdsToEdit, genreId, true, goodFeature);
    }

    /**
     * Edits the GameplayFeatures.txt file to remove genre id from the input gameplay feature
     *
     * @param genreId The genre id that should be used
     */
    public void removeGenreId(int genreId) throws ModProcessingException {
        ArrayList<Integer> list = new ArrayList<>();
        editGenreIdAllocation(list, genreId, false, false);
    }

    /**
     * Edits the GameplayFeatures.txt file to add/remove genre ids to/from the input gameplay feature
     *
     * @param gameplayFeaturesIdsToEdit The map containing the gameplay features where the operation should be executed
     * @param genreId                   The genre id that should be used
     * @param addGenreId                True when the genre id should be added. False when the genre id should be removed.
     * @param goodFeature               True when the id should be added as good to the feature. False when it should be added as bad.
     */
    private void editGenreIdAllocation(ArrayList<Integer> gameplayFeaturesIdsToEdit, int genreId, boolean addGenreId, boolean goodFeature) throws ModProcessingException {
        analyzeFile();
        try {
            File gameplayFeaturesFile = getGameFile();
            if (Files.exists(gameplayFeaturesFile.toPath())) {
                Files.delete(gameplayFeaturesFile.toPath());
            }
            Files.createFile(gameplayFeaturesFile.toPath());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameplayFeaturesFile), StandardCharsets.UTF_8));
            bw.write("\ufeff");
            for (Map<String, String> map : GameplayFeatureManager.INSTANCE.fileContent) {
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
            throw new ModProcessingException("Something went wrong while editing game file for mod " + GameplayFeatureManager.INSTANCE.getType(), e);
        }
    }

}