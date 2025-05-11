package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.NpcIp;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.SequelNumeration;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NpcIpManager extends AbstractSimpleContentManager implements DependentContentManager {

    public static final NpcIpManager INSTANCE = new NpcIpManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", MadGamesTycoon2ModTool.VERSION};

    private NpcIpManager() {
        super("npcIp", "npc_ip", MGT2Paths.TEXT_DATA.getPath().resolve("NpcIPs.txt").toFile(), StandardCharsets.UTF_16LE);
    }

    @Override
    protected String getReplacedLine(String line) {
        return Utils.getFirstPart(line).trim();
    }

    @Override
    protected String isLineValid(String line) {
        if (line.contains("P") && line.contains("G") && line.contains("T") && line.contains("Y") && line.contains("TG")) {
            try {
                constructContentFromName(getReplacedLine(line));
            } catch (ModProcessingException | NumberFormatException | NullPointerException e) {
                if (e instanceof NumberFormatException) {
                    return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcIpInvalid.invalidNumber"), line, e.getMessage());
                } else if (e instanceof NullPointerException) {
                    return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcIpInvalid.formatInvalid"), getReplacedLine(line));
                } else {
                    return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcIpInvalid.unknownError"), line, e.getMessage());
                }
            }
        } else {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcIpInvalid.formatInvalid"), getReplacedLine(line));
        }
        return "";
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.npcIp.addMod.components.textFieldName.initialValue"));
        JLabel labelReleaseYear = new JLabel(I18n.INSTANCE.get("mod.npcIp.addMod.components.label.releaseYear") + ":");
        JSpinner spinnerReleaseYear = WindowHelper.getUnlockYearSpinner();
        JPanel panelReleaseYear = new JPanel();
        panelReleaseYear.add(labelReleaseYear);
        panelReleaseYear.add(spinnerReleaseYear);
        JLabel labelTargetGroup = new JLabel(I18n.INSTANCE.get("commonText.targetGroup") + ":");
        JComboBox<String> comboBoxTargetGroup = WindowHelper.getComboBox(TargetGroup.class, "commonText.targetGroup", I18n.INSTANCE.get("commonText.all"));
        JPanel panelTargetGroup = new JPanel();
        panelTargetGroup.add(labelTargetGroup);
        panelTargetGroup.add(comboBoxTargetGroup);
        JLabel labelRating = new JLabel(I18n.INSTANCE.get("mod.npcIp.addMod.components.spinner.rating") + ":");
        JSpinner spinnerRating = WindowHelper.getBaseSpinner("mod.npcIp.addMod.components.spinner.rating", 50, 0, 100, 5);
        spinnerRating.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.spinner.rating.toolTip"));
        JPanel panelRating = new JPanel();
        panelRating.add(labelRating);
        panelRating.add(spinnerRating);
        JList<String> listMainGenre = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), false);
        JList<String> listSubGenre = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), false);
        JButton buttonMainGenre = new JButton(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre.initial"));
        WindowHelper.setListButtonAction(buttonMainGenre, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre.initial") + ":", listMainGenre, e -> {
            if (Objects.equals(listSubGenre.getSelectedValue(), listMainGenre.getSelectedValue())) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre.genreAlreadySelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                listMainGenre.clearSelection();
                buttonMainGenre.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre.initial"));
            } else {
                buttonMainGenre.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre") + ": " + listMainGenre.getSelectedValue());
                buttonMainGenre.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainGenre") + ": " + listMainGenre.getSelectedValue());
            }
        });
        JButton buttonSubGenre = new JButton(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre.initial"));
        WindowHelper.setListButtonAction(buttonSubGenre, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre.initial") + ":", listSubGenre, e -> {
            if (Objects.equals(listMainGenre.getSelectedValue(), listSubGenre.getSelectedValue())) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre.genreAlreadySelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                listSubGenre.clearSelection();
                buttonSubGenre.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre.initial"));
            } else {
                buttonSubGenre.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre") + ": " + listSubGenre.getSelectedValue());
                buttonSubGenre.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre") + ": " + listMainGenre.getSelectedValue());
            }
        });
        JList<String> listMainTheme = WindowHelper.getList(ThemeManager.INSTANCE.getContentByAlphabet(), false);
        JList<String> listSubTheme = WindowHelper.getList(ThemeManager.INSTANCE.getContentByAlphabet(), false);
        JButton buttonMainTheme = new JButton(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme.initial"));
        WindowHelper.setListButtonAction(buttonMainTheme, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme.initial") + ":", listMainTheme, e -> {
            if (Objects.equals(listSubTheme.getSelectedValue(), listMainTheme.getSelectedValue())) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme.themeAlreadySelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                listMainTheme.clearSelection();
                buttonMainTheme.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme.initial"));
            } else {
                buttonMainTheme.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme") + ": " + listMainTheme.getSelectedValue());
                buttonMainGenre.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.mainTheme") + ": " + listMainGenre.getSelectedValue());
            }
        });
        JButton buttonSubTheme = new JButton(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme.initial"));
        WindowHelper.setListButtonAction(buttonSubTheme, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme.initial") + ":", listSubTheme, e -> {
            if (Objects.equals(listMainTheme.getSelectedValue(), listSubTheme.getSelectedValue())) {
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme.themeAlreadySelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                listSubTheme.clearSelection();
                buttonSubTheme.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme.initial"));
            } else {
                buttonSubTheme.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme") + ": " + listSubTheme.getSelectedValue());
                buttonSubTheme.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme") + ": " + listMainGenre.getSelectedValue());
            }
        });
        JList<String> listPublisher = WindowHelper.getList(PublisherManager.INSTANCE.getContentByAlphabet(), false);
        JButton buttonPublisher = new JButton(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher.initial"));
        WindowHelper.setListButtonAction(buttonPublisher, I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher.initial") + ":", listPublisher, e -> {
            buttonPublisher.setText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher") + ": " + listPublisher.getSelectedValue());
            buttonMainGenre.setToolTipText(I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher") + ": " + listMainGenre.getSelectedValue());
        });
        Object[] params = {WindowHelper.getNamePanel(textFieldName), panelReleaseYear, panelTargetGroup, panelRating, buttonMainGenre, buttonSubGenre, buttonMainTheme, buttonSubTheme, buttonPublisher};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (textFieldName.getText().equals(I18n.INSTANCE.get("mod.npcIp.addMod.components.textFieldName.initialValue"))) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (listMainGenre.isSelectionEmpty() || listMainTheme.isSelectionEmpty() || listPublisher.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcIp.addMod.somethingNotSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                // Setup to add mod
                int mainGenre = GenreManager.INSTANCE.getContentIdByName(listMainGenre.getSelectedValue());
                int mainTheme = ThemeManager.INSTANCE.getContentIdByName(listMainTheme.getSelectedValue());
                int publisher = PublisherManager.INSTANCE.getContentIdByName(listPublisher.getSelectedValue());
                Integer subGenre = null;
                if (!listSubGenre.isSelectionEmpty()) {
                    subGenre = GenreManager.INSTANCE.getContentIdByName(listSubGenre.getSelectedValue());
                }
                Integer subTheme = null;
                if (!listSubTheme.isSelectionEmpty()) {
                    subTheme = ThemeManager.INSTANCE.getContentIdByName(listSubTheme.getSelectedValue());
                }
                analyzeFile();
                NpcIp npcIp = new NpcIp(textFieldName.getText(), null, mainGenre, subGenre, mainTheme, subTheme, TargetGroup.getTargetGroup((String) comboBoxTargetGroup.getSelectedItem()), publisher, Integer.parseInt(spinnerReleaseYear.getValue().toString()), Integer.parseInt(spinnerRating.getValue().toString()), null);
                if (JOptionPane.showConfirmDialog(null, npcIp.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    addContent(npcIp);
                    ContentAdministrator.modAdded(textFieldName.getText(), I18n.INSTANCE.get("commonText.npcIp.upperCase"));
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        String line = getLineByName(name);
        ArrayList<String> data = Utils.getEntriesFromString(line);
        Integer genre = null;
        Integer subGenre = null;
        Integer theme = null;
        Integer subTheme = null;
        Integer publisher = null;
        Integer releaseYear = null;
        Integer rating = null;
        TargetGroup targetGroup = null;
        SequelNumeration sn = null;
        for (String d : data) {
            if (d.startsWith("P") && !d.equals("PLSTATIC")) {
                publisher = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("G")) {
                genre = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("SG")) {
                subGenre = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("TG")) {
                int tgId = Integer.parseInt(d.replaceAll("[^0-9]", ""));
                for (TargetGroup tg : TargetGroup.values()) {
                    if (tg.getId() == tgId) {
                        targetGroup = tg;
                    }
                }
            } else if (d.startsWith("T")) {
                theme = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("ST")) {
                subTheme = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("Y")) {
                releaseYear = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("%")) {
                rating = Integer.parseInt(d.replaceAll("%", ""));
            } else if (d.equals("ROM")) {
                sn = SequelNumeration.ROM;
            } else if (d.equals("ARA")) {
                sn = SequelNumeration.ARA;
            }
        }
        if (genre == null || theme == null || publisher == null || releaseYear == null || rating == null) {
            throw new ModProcessingException("Unable to construct content named " + name + ". Reason: missing data.");
        }
        return new NpcIp(name, null, genre, subGenre, theme, subTheme, targetGroup, publisher, releaseYear, rating, sn);
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        String name = (String) map.get("NAME EN");
        int genre = SharingHelper.getContentIdByNameFromImport(GenreManager.INSTANCE, (String) map.get("genre"));
        Integer subGenre = null;
        if (map.containsKey("sub_genre")) {
            subGenre = SharingHelper.getContentIdByNameFromImport(GenreManager.INSTANCE, (String) map.get("sub_genre"));
        }
        int theme = SharingHelper.getContentIdByNameFromImport(ThemeManager.INSTANCE, (String) map.get("theme"));
        Integer subTheme = null;
        if (map.containsKey("sub_theme")) {
            subTheme = SharingHelper.getContentIdByNameFromImport(ThemeManager.INSTANCE, (String) map.get("sub_theme"));
        }
        TargetGroup targetGroup = TargetGroup.getTargetGroup((String) map.get("target_group"));
        int publisher = SharingHelper.getContentIdByNameFromImport(PublisherManager.INSTANCE, (String) map.get("publisher"));
        int releaseYear = Integer.parseInt((String) map.get("release_year"));
        int rating = Integer.parseInt((String) map.get("rating"));
        SequelNumeration sn = SequelNumeration.NONE;
        if (map.containsKey("SEQUEL_NUMERATION")) {
            sn = SequelNumeration.getSequelNumeration(map.get("SEQUEL_NUMERATION").toString());
        }
        return new NpcIp(name, null, genre, subGenre, theme, subTheme, targetGroup, publisher, releaseYear, rating, sn);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "genre");
        replaceMapEntry(map, missingDependency, replacement, "theme");
        replaceMapEntry(map, missingDependency, replacement, "publisher");
        if (map.containsKey("sub_genre")) {
            replaceMapEntry(map, missingDependency, replacement, "sub_genre");
        }
        if (map.containsKey("sub_theme")) {
            replaceMapEntry(map, missingDependency, replacement, "sub_genre");
        }
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(GenreManager.INSTANCE);
        arrayList.add(ThemeManager.INSTANCE);
        arrayList.add(PublisherManager.INSTANCE);
        return arrayList;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add((String)importMap.get("genre"));
        if (importMap.containsKey("sub_genre")) {
            genres.add((String)importMap.get("sub_genre"));
        }
        map.put(GenreManager.INSTANCE.getId(), genres);
        Set<String> themes = new HashSet<>();
        themes.add((String)importMap.get("theme"));
        if (importMap.containsKey("sub_theme")) {
            themes.add((String)importMap.get("sub_theme"));
        }
        map.put(ThemeManager.INSTANCE.getId(), themes);
        Set<String> publisher = new HashSet<>();
        publisher.add((String)importMap.get("publisher"));
        map.put(PublisherManager.INSTANCE.getId(), publisher);
        return map;
    }
}
