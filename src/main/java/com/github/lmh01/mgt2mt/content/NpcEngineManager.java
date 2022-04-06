package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.DependentContentManager;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
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

public class NpcEngineManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final NpcEngineManager INSTANCE = new NpcEngineManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private NpcEngineManager() {
        super("npcEngine", "npc_engine", "default_npc_engines.txt", MGT2Paths.TEXT_DATA.getPath().resolve("NpcEngines.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        EditHelper.printLine("PLATFORM", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("SHARE", map, bw);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        return new NpcEngine(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManagerNew(map),
                map.get("DATE"),
                Integer.parseInt(map.get("GENRE")),
                Integer.parseInt(map.get("PLATFORM")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("SHARE"))
        );
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        spinnerUnlockYear.setToolTipText(spinnerUnlockYear.getToolTipText() + "<br>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.unlockYear.additionalToolTip"));
        JSpinner spinnerShare = WindowHelper.getProfitShareSpinner();
        JSpinner spinnerCost = WindowHelper.getBaseSpinner("mod.npcEngine.addMod.components.spinner.cost.toolTip", 30000, 0, 1000000, 5000);

        JLabel labelExplainGenreList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectGenre") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
        JList<String> listAvailableGenres = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGenres);

        JLabel labelExplainPlatformList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectPlatform"));
        JList<String> listAvailablePlatforms = WindowHelper.getList(ModManager.platformMod.getContentByAlphabet(), false);
        JScrollPane scrollPaneAvailablePlatforms = WindowHelper.getScrollPane(listAvailablePlatforms);

        Object[] param = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerShare, SpinnerType.PROFIT_SHARE), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), labelExplainGenreList, scrollPaneAvailableGenres, labelExplainPlatformList, scrollPaneAvailablePlatforms};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, param, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"))) {
                    if (!npcEngineNamesWithoutGenre().contains(textFieldName.getText())) {
                        if (!listAvailablePlatforms.getSelectedValuesList().isEmpty()) {
                            if (!listAvailableGenres.getSelectedValuesList().isEmpty()) {
                                if (listAvailableGenres.getSelectedValuesList().size() > 1) {
                                    // Multiple genres have been selected
                                    String values = "<br><br>" +
                                            I18n.INSTANCE.get("commonText.unlockDate") + ": " + Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString() + "<br>" +
                                            I18n.INSTANCE.get("commonText.platform.upperCase") + ": " + listAvailablePlatforms.getSelectedValue() + "<br>" +
                                            I18n.INSTANCE.get("commonText.price") + ": " + spinnerCost.getValue().toString() + "<br>" +
                                            I18n.INSTANCE.get("commonText.profitShare") + ": " + spinnerShare.getValue().toString() + "<br>";;
                                    int result = JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres") + values, I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.NO_OPTION) {
                                        continue;
                                    }
                                    // Add a new npc engine for each genre
                                    for (String genre : listAvailableGenres.getSelectedValuesList()) {
                                        Map<String, String> nameTranslations = new HashMap<>();
                                        for (Map.Entry<String, String> entry : mapNameTranslations[0].entrySet()) {
                                            if (!entry.getValue().isEmpty()) {
                                                nameTranslations.put(entry.getKey(), entry.getValue() + " [" + genre + "]");
                                            }
                                        }
                                        NpcEngine npcEngine = new NpcEngine(
                                                textFieldName.getText() + " [" + genre + "]",
                                                null,
                                                new TranslationManagerNew(nameTranslations, null),
                                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                                GenreManager.INSTANCE.getContentIdByName(genre),
                                                ModManager.platformMod.getContentIdByName(listAvailablePlatforms.getSelectedValue()),
                                                Integer.parseInt(spinnerCost.getValue().toString()),
                                                Integer.parseInt(spinnerShare.getValue().toString())
                                        );
                                        addContent(npcEngine);
                                    }
                                    JOptionPane.showMessageDialog(null, listAvailableGenres.getSelectedValuesList().size() + " " + I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres.finished"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                    break;
                                } else {
                                    // A single genre has been selected
                                    NpcEngine npcEngine = new NpcEngine(
                                            textFieldName.getText(),
                                            null,
                                            new TranslationManagerNew(mapNameTranslations[0], null),
                                            Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                            GenreManager.INSTANCE.getContentIdByName(listAvailableGenres.getSelectedValue()),
                                            ModManager.platformMod.getContentIdByName(listAvailablePlatforms.getSelectedValue()),
                                            Integer.parseInt(spinnerCost.getValue().toString()),
                                            Integer.parseInt(spinnerShare.getValue().toString())
                                    );
                                    int result = JOptionPane.showConfirmDialog(null, npcEngine.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) {
                                        addContent(npcEngine);
                                        showModAddedMessage(npcEngine.name);
                                        break;
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.selectGenreFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.noPlatformSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> dependencies = new ArrayList<>();
        dependencies.add(GenreManager.INSTANCE);
        dependencies.add(PlatformManager.INSTANCE);
        return dependencies;
    }

    /**
     * @return An ArrayList containing the engine names without the genre names
     */
    private ArrayList<String> npcEngineNamesWithoutGenre() throws ModProcessingException {
        ArrayList<String> strings = new ArrayList<>();
        for (String string : getContentByAlphabet()) {
            for (String string2 : ModManager.genreMod.getContentByAlphabet()) {
                strings.add(string.replace(" [" + string2 + "]", ""));
            }
        }
        return strings;
    }
}
