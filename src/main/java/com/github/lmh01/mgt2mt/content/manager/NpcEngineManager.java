package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.NpcEngine;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NpcEngineManager extends AbstractAdvancedContentManager implements DependentContentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NpcEngineManager.class);

    public static final NpcEngineManager INSTANCE = new NpcEngineManager();

    public static final String compatibleModToolVersions[] = new String[]{"4.0.0", MadGamesTycoon2ModTool.VERSION};

    private NpcEngineManager() {
        super("npcEngine", "npc_engine", "default_npc_engines.txt", MGT2Paths.TEXT_DATA.getPath().resolve("NpcEngines.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        return new NpcEngine(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DATE"),
                Integer.parseInt(map.get("GENRE")),
                Integer.parseInt(map.get("PLATFORM")),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("SHARE"))
        );
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("GENRE", true, DataType.INT));
        list.add(new DataLine("PLATFORM", true, DataType.INT));
        list.add(new DataLine("PRICE", true, DataType.INT));
        list.add(new DataLine("SHARE", true, DataType.INT));
        return list;
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        return new NpcEngine(
                (String) map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                (String) map.get("DATE"),
                SharingHelper.getContentIdByNameFromImport(GenreManager.INSTANCE, (String) map.get("GENRE")),
                SharingHelper.getContentIdByNameFromImport(PlatformManager.INSTANCE, (String) map.get("PLATFORM")),
                Integer.parseInt((String) map.get("PRICE")),
                Integer.parseInt((String) map.get("SHARE"))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
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
        JList<String> listAvailableGenres = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGenres);

        JLabel labelExplainPlatformList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectPlatform"));
        JList<String> listAvailablePlatforms = WindowHelper.getList(PlatformManager.INSTANCE.getContentByAlphabet(), false);
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
                                                new TranslationManager(nameTranslations, null),
                                                Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                                GenreManager.INSTANCE.getContentIdByName(genre),
                                                PlatformManager.INSTANCE.getContentIdByName(listAvailablePlatforms.getSelectedValue()),
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
                                            new TranslationManager(mapNameTranslations[0], null),
                                            Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                                            GenreManager.INSTANCE.getContentIdByName(listAvailableGenres.getSelectedValue()),
                                            PlatformManager.INSTANCE.getContentIdByName(listAvailablePlatforms.getSelectedValue()),
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
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "GENRE");
        replaceMapEntry(map, missingDependency, replacement, "PLATFORM");
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
            for (String string2 : GenreManager.INSTANCE.getContentByAlphabet()) {
                strings.add(string.replace(" [" + string2 + "]", ""));
            }
        }
        return strings;
    }

    /**
     * Removes the specified genre from the npc engine file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     *
     * @param name The name of the genre that should be removed
     */
    public void removeGenre(String name) throws ModProcessingException {
        try {
            int genreId = GenreManager.INSTANCE.getContentIdByName(name);
            analyzeFile();
            DebugHelper.debug(LOGGER, "Replacing genre id in npc engine file: " + name);
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if (Files.exists(fileToEdit.toPath())) {
                Files.delete(fileToEdit.toPath());
            }
            Files.createFile(fileToEdit.toPath());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            for (Map<String, String> fileContent : fileContent) {
                if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                    fileContent.remove("GENRE");
                    fileContent.put("GENRE", Integer.toString(GenreManager.INSTANCE.getActiveIds().get(Utils.getRandomNumber(0, GenreManager.INSTANCE.getActiveIds().size()))));
                }
                printValues(fileContent, bw);
                bw.write("\r\n");
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType(), e);
        }
    }
}
