package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContentManager;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.DependentContentManager;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.TargetGroup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class NpcIpManager extends AbstractSimpleContentManager implements DependentContentManager {

    public static final NpcIpManager INSTANCE = new NpcIpManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.2.0", MadGamesTycoon2ModTool.VERSION};

    private NpcIpManager() {
        super("npcIp", "npc_ip", "default_npcIps.txt", MGT2Paths.TEXT_DATA.getPath().resolve("NpcIps.txt").toFile(), StandardCharsets.UTF_16LE, compatibleModToolVersions);
    }

    @Override
    protected String getReplacedLine(String line) {
        return Utils.getFirstPart(line).trim();
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
        JList<String> listMainGenre = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), false);
        JList<String> listSubGenre = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), false);
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
        JList<String> listMainTheme = WindowHelper.getList(ModManager.themeMod.getContentByAlphabet(), false);
        JList<String> listSubTheme = WindowHelper.getList(ModManager.themeMod.getContentByAlphabet(), false);
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
        JList<String> listPublisher = WindowHelper.getList(ModManager.publisherMod.getContentByAlphabet(), false);
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
                int mainGenre = ModManager.genreMod.getContentIdByName(listMainGenre.getSelectedValue());
                int mainTheme = ModManager.themeMod.getContentIdByName(listMainTheme.getSelectedValue());
                System.out.printf("Main theme id: %d\n", mainTheme);
                int publisher = ModManager.publisherMod.getContentIdByName(listPublisher.getSelectedValue());
                Integer subGenre = null;
                if (!listSubGenre.isSelectionEmpty()) {
                    subGenre = ModManager.genreMod.getContentIdByName(listSubGenre.getSelectedValue());
                }
                Integer subTheme = null;
                if (!listSubTheme.isSelectionEmpty()) {
                    subTheme = ModManager.themeMod.getContentIdByName(listSubTheme.getSelectedValue());
                }
                analyzeFile();
                NpcIp npcIp = new NpcIp(textFieldName.getText(), null, mainGenre, subGenre, mainTheme, subTheme, TargetGroup.getTargetGroup((String) comboBoxTargetGroup.getSelectedItem()), publisher, Integer.parseInt(spinnerReleaseYear.getValue().toString()), Integer.parseInt(spinnerRating.getValue().toString()));
                if (JOptionPane.showConfirmDialog(null, npcIp.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    addContent(npcIp);
                    ModManager.modAdded(textFieldName.getText(), I18n.INSTANCE.get("commonText.npcIp.upperCase"));
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        String name = (String) map.get("name");
        int genre = Integer.parseInt((String) map.get("genre"));
        Integer subGenre = null;
        if (map.containsKey("sub_genre")) {
            subGenre = Integer.parseInt((String) map.get("sub_genre"));
        }
        int theme = Integer.parseInt((String) map.get("theme"));
        Integer subTheme = null;
        if (map.containsKey("sub_theme")) {
            subTheme = Integer.parseInt((String) map.get("sub_theme"));
        }
        TargetGroup targetGroup = TargetGroup.getTargetGroup((String) map.get("target_group"));
        int publisher = Integer.parseInt((String) map.get("publisher"));
        int releaseYear = Integer.parseInt((String) map.get("release_year"));
        int rating = Integer.parseInt((String) map.get("rating"));
        return new NpcIp(name, null, genre, subGenre, theme, subTheme, targetGroup, publisher, releaseYear, rating);
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
        for (String d : data) {
            if (d.startsWith("P")) {
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
            }  else if (d.startsWith("T")) {
                theme = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("ST")) {
                subTheme = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            }else if (d.startsWith("Y")) {
                releaseYear = Integer.parseInt(d.replaceAll("[^0-9]", ""));
            } else if (d.startsWith("%")) {
                rating = Integer.parseInt(d.replaceAll("%", ""));
            }
        }
        return new NpcIp(name, null, genre, subGenre, theme, subTheme, targetGroup, publisher, releaseYear, rating);
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(GenreManager.INSTANCE);
        arrayList.add(ThemeManager.INSTANCE);
        arrayList.add(PublisherManager.INSTANCE);
        return arrayList;
    }
}
