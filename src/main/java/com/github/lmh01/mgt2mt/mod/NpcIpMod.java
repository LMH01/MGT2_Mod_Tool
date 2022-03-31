package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NpcIpMod extends AbstractSimpleDependentMod {
    /*
    * Game file bracket meaning:
    * P = Publisher
    * G = Genre
    * SG = Sub-Genre
    * T = Topic
    * ST = Sub-Topic
    * TG = Target Group
    * Y = Release Year
    * % = IP Rating
    * */

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{"3.2.0", MadGamesTycoon2ModTool.VERSION};
    }

    @Override
    public String getMainTranslationKey() {
        return "npcIp";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.npcIpMod;
    }

    @Override
    public String getExportType() {
        return "npc_ip";
    }

    @Override
    protected String getGameFileName() {
        return "NpcIPs.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_npcIps.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
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
                int publisher = ModManager.publisherMod.getContentIdByName(listPublisher.getSelectedValue());
                Integer subGenre = null;
                if (!listSubGenre.isSelectionEmpty()) {
                    subGenre = ModManager.genreMod.getContentIdByName(listSubGenre.getSelectedValue());
                }
                Integer subTheme = null;
                if (!listSubTheme.isSelectionEmpty()) {
                    subTheme = ModManager.themeMod.getContentIdByName(listSubTheme.getSelectedValue());
                }
                NpcIp npcIp = new NpcIp(textFieldName.getText(), mainGenre, subGenre, mainTheme, subTheme, TargetGroup.getTargetGroup((String) comboBoxTargetGroup.getSelectedItem()), publisher, Integer.parseInt(spinnerReleaseYear.getValue().toString()), Integer.parseInt(spinnerRating.getValue().toString()));
                if (JOptionPane.showConfirmDialog(null, getOptionPaneMessage(npcIp), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    addModToFile(npcIp.getLine());
                    ModManager.modAdded(textFieldName.getText(), I18n.INSTANCE.get("commonText.npcIp.upperCase"));
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Returns the option pane message for the given mod.
     * @param npcIp The npcIp that holds the values
     */
    private String getOptionPaneMessage(NpcIp npcIp) throws ModProcessingException {
        String subGenre = "none";
        if (npcIp.subGenre != null) {
            subGenre = ModManager.genreMod.getContentNameById(npcIp.subGenre);
        }
        String subTheme = "none";
        if (npcIp.subTheme != null) {
            subGenre = ModManager.themeMod.getContentNameById(npcIp.subTheme);
        }
        System.out.printf("Theme id: %s\n", npcIp.theme);
        return "<html>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + npcIp.name + "<br>" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + ModManager.genreMod.getContentNameById(npcIp.genre) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subGenre") + ": " + subGenre + "<br>" +
                I18n.INSTANCE.get("commonText.theme.upperCase") + ": " + ModManager.themeMod.getContentNameById(npcIp.theme) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.subTheme") + ": " + subTheme + "<br>" +
                I18n.INSTANCE.get("commonText.targetGroup") + ": " + npcIp.targetGroup.getTypeName() + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.button.publisher") + ": " + ModManager.publisherMod.getContentNameById(npcIp.publisher) + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.label.releaseYear") + ": " + npcIp.releaseYear + "<br>" +
                I18n.INSTANCE.get("mod.npcIp.addMod.components.spinner.rating") + ": " + npcIp.rating;
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR NPC IP MOD. Use {@link NpcIpMod#getOptionPaneMessage(NpcIp)} instead.
     */
    @Override
    @Deprecated
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        String line = transformGenericToString(t);
        throw new ModProcessingException("Not yet implemented!");
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }

    @Override
    public String getReplacedLine(String inputString) {
        return Utils.getFirstPart(inputString).trim();
    }

    @Override
    public String getModifiedExportLine(String exportLine) throws ModProcessingException {
        NpcIp npcIp = new NpcIp(exportLine, ModConstructionType.GAME_FILES);
        return npcIp.getExportLine();
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "line");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.genreMod);
        arrayList.add(ModManager.themeMod);
        arrayList.add(ModManager.publisherMod);
        return arrayList;
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        String line = transformGenericToString(t);
        NpcIp npcIp = new NpcIp(line, ModConstructionType.GAME_FILES);
        return npcIp.getDependencyMap();
    }
}
