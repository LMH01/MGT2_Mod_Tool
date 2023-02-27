package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.GameplayFeature;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
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
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayFeatureManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GameplayFeatureManager INSTANCE = new GameplayFeatureManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1", MadGamesTycoon2ModTool.VERSION};

    private GameplayFeatureManager() {
        super("gameplayFeature", "gameplay_feature", MGT2Paths.TEXT_DATA.getPath().resolve("GameplayFeatures.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        boolean arcade = true;
        if (map.containsKey("NO_ARCADE")) {
            arcade = false;
        }
        boolean mobile = true;
        if (map.containsKey("NO_MOBILE")) {
            mobile = false;
        }
        boolean requiresInternet = false;
        if (map.containsKey("INTERNET")) {
            requiresInternet = true;
        }
        ArrayList<Integer> badGameplayFeatures = new ArrayList<>();
        if (map.containsKey("BAD")) {
            badGameplayFeatures = Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("BAD")));
        }
        ArrayList<Integer> goodGameplayFeatures = new ArrayList<>();
        if (map.containsKey("GOOD")) {
            goodGameplayFeatures = Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("GOOD")));
        }
        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
        if (map.containsKey("NEED_GPF")) {
            requiredGameplayFeatures.add(Integer.parseInt(map.get("NEED_GPF")));
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
                requiredGameplayFeatures,
                mobile,
                arcade,
                requiresInternet
        );
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("TYP", true, DataType.INT));
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("RES POINTS", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("DEV COSTS", true, DataType.INT));
        list.add(new DataLine("PIC", true, DataType.UNCHECKED));
        list.add(new DataLine("GAMEPLAY", true, DataType.INT));
        list.add(new DataLine("GRAPHIC", true, DataType.INT));
        list.add(new DataLine("SOUND", true, DataType.INT));
        list.add(new DataLine("TECH", true, DataType.INT));
        list.add(new DataLine("GOOD", false, DataType.INT_LIST));
        list.add(new DataLine("BAD", false, DataType.INT_LIST));
        list.add(new DataLine("NO_ARCADE", false, DataType.EMPTY));
        list.add(new DataLine("NO_MOBILE", false, DataType.EMPTY));
        list.add(new DataLine("INTERNET", false, DataType.EMPTY));
        list.add(new DataLine("NEED_GPF", false, DataType.INT));
        return list;
    }

    @Override
    protected String analyzeSpecialCases(Map<String, String> map) {
        try {
            if (map.containsKey("TYP")) {
                try {
                    GameplayFeatureType.getFromId(Integer.parseInt(map.get("TYP")));
                } catch (NumberFormatException ignored) {

                }
            }
        } catch (IllegalArgumentException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.typeInvalid") + "\n", getGameFile().getName(), getType(), map.get("NAME EN"), e.getMessage());
        }
        return "";
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        boolean arcade = true;
        if (map.containsKey("NO_ARCADE")) {
            arcade = false;
        }
        boolean mobile = true;
        if (map.containsKey("NO_MOBILE")) {
            mobile = false;
        }
        boolean requiresInternet = false;
        if (map.containsKey("INTERNET")) {
            requiresInternet = true;
        }
        ArrayList<Integer> badGameplayFeatures = new ArrayList<>();
        if (map.containsKey("BAD")) {
            badGameplayFeatures = SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String) map.get("BAD"));
        }
        ArrayList<Integer> goodGameplayFeatures = new ArrayList<>();
        if (map.containsKey("GOOD")) {
            goodGameplayFeatures = SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String) map.get("GOOD"));
        }
        ArrayList<Integer> requiredGameplayFeatures = new ArrayList<>();
        if (map.containsKey("NEED_GPF")) {
            requiredGameplayFeatures.add(Integer.parseInt((String) map.get("NEED_GPF")));
        }
        return new GameplayFeature(
                (String) map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                (String) map.get("DESC EN"),
                GameplayFeatureType.getFromId(Integer.parseInt((String) map.get("TYP"))),
                (String) map.get("DATE"),
                Integer.parseInt((String) map.get("RES POINTS")),
                Integer.parseInt((String) map.get("PRICE")),
                Integer.parseInt((String) map.get("DEV COSTS")),
                Integer.parseInt((String) map.get("GAMEPLAY")),
                Integer.parseInt((String) map.get("GRAPHIC")),
                Integer.parseInt((String) map.get("SOUND")),
                Integer.parseInt((String) map.get("TECH")),
                badGameplayFeatures,
                goodGameplayFeatures,
                requiredGameplayFeatures,
                mobile,
                arcade,
                requiresInternet
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        final ArrayList<Integer>[] badGenreIds = new ArrayList[]{new ArrayList<>()};
        final ArrayList<Integer>[] goodGenreIds = new ArrayList[]{new ArrayList<>()};
        final ArrayList<Integer>[] requiredGameplayFeatures = new ArrayList[]{new ArrayList<>()};
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
        JSpinner spinnerGameplay = WindowHelper.getBaseSpinner("commonText.points.spinner.toolTip", 30, 0, 400, 5);
        JSpinner spinnerGraphic = WindowHelper.getBaseSpinner("commonText.points.spinner.toolTip", 30, 0, 400, 5);
        JSpinner spinnerSound = WindowHelper.getBaseSpinner("commonText.points.spinner.toolTip", 30, 0, 400, 5);
        JSpinner spinnerTech = WindowHelper.getBaseSpinner("commonText.points.spinner.toolTip", 30, 0, 400, 5);

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
                    if (!mutualEntries) {
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
                    if (!mutualEntries) {
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

        JButton buttonSelectRequiredGameplayFeatures = new JButton(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures"));
        buttonSelectRequiredGameplayFeatures.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures.toolTip"));
        buttonSelectRequiredGameplayFeatures.addActionListener(actionEvent -> {
            try {
                List<String> requiredGameplayFeatureNames = Utils.getSelectedEntries(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures.select"), I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures.window"), GameplayFeatureManager.INSTANCE.getContentByAlphabet(), GameplayFeatureManager.INSTANCE.getContentByAlphabet(), false);
                ArrayList<Integer> requiredGameplayFeaturesInternal = new ArrayList<>();
                for (String s : requiredGameplayFeatureNames) {
                    requiredGameplayFeaturesInternal.add(GameplayFeatureManager.INSTANCE.getContentIdByName(s));
                }
                requiredGameplayFeatures[0] = requiredGameplayFeaturesInternal;
                buttonSelectRequiredGameplayFeatures.setText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiredGameplayFeatures") + " (" + I18n.INSTANCE.get("commonText.selected") + ")");
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

        JCheckBox checkBoxRequiresInternet = new JCheckBox(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiresInternet"));
        checkBoxRequiresInternet.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.requiresInternet.toolTip"));

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getDescriptionPanel(textFieldDescription), buttonAddDescriptionTranslations, WindowHelper.getTypePanel(comboBoxFeatureType), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerResearchPoints, SpinnerType.RESEARCH_POINT_COST), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST), WindowHelper.getSpinnerPanel(spinnerResearchCost, SpinnerType.RESEARCH_COST), WindowHelper.getSpinnerPanel(spinnerGameplay, SpinnerType.GAMEPLAY), WindowHelper.getSpinnerPanel(spinnerGraphic, SpinnerType.GRAPHIC), WindowHelper.getSpinnerPanel(spinnerSound, SpinnerType.SOUND), WindowHelper.getSpinnerPanel(spinnerTech, SpinnerType.TECH), buttonBadGenres, buttonGoodGenres, buttonSelectRequiredGameplayFeatures, checkBoxCompatibleWithArcadeCabinets, checkBoxCompatibleWithMobile, checkBoxRequiresInternet};
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
                                (int) spinnerResearchPoints.getValue(),
                                (int) spinnerResearchCost.getValue(),
                                (int) spinnerDevelopmentCost.getValue(),
                                (int) spinnerGameplay.getValue(),
                                (int) spinnerGraphic.getValue(),
                                (int) spinnerSound.getValue(),
                                (int) spinnerTech.getValue(),
                                badGenreIds[0],
                                goodGenreIds[0],
                                requiredGameplayFeatures[0],
                                checkBoxCompatibleWithArcadeCabinets.isSelected(),
                                checkBoxCompatibleWithMobile.isSelected(),
                                checkBoxRequiresInternet.isSelected()
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
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
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
