package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.DependentContentManager;
import com.github.lmh01.mgt2mt.mod.managed.GameplayFeatureType;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.SpinnerType;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayFeatureManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final GameplayFeatureManager INSTANCE = new GameplayFeatureManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private GameplayFeatureManager() {
        super("gameplayFeature", "gameplay_feature", "default_gameplay_features.txt", MGT2Paths.TEXT_DATA.getPath().resolve("GameplayFeatures.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        EditHelper.printLine("TYP", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
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
        return new GameplayFeature(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManagerNew(map),
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
                Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("BAD"))),
                Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(map.get("GOOD"))),
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
                        TranslationManagerNew translationManager = new TranslationManagerNew(mapNameTranslations[0], mapDescriptionTranslations[0]);
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
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }
}
