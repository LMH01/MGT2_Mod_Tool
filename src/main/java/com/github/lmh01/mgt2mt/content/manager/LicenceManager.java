package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.Licence;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContentManager;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class LicenceManager extends AbstractSimpleContentManager {

    public static final LicenceManager INSTANCE = new LicenceManager();

    public static final String compatibleModToolVersions[] = new String[]{"4.0.0", MadGamesTycoon2ModTool.VERSION};

    private LicenceManager() {
        super("licence", "licence", "default_licences.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Licence.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    protected String getReplacedLine(String line) {
        return line.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public String[] getCustomContentString() throws ModProcessingException {
        List<String> custom = Arrays.stream(super.getCustomContentString()).unordered().collect(Collectors.toList());
        custom.remove("Chronicles of Nornio [5]");
        return Utils.transformListToArray((ArrayList<String>) custom);
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JLabel labelAddLicence = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        JTextField textFieldName = new JTextField();

        JPanel panelType = new JPanel();
        JLabel labelType = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        JComboBox<String> comboBoxType = new JComboBox<>();
        comboBoxType.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie"), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book"), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport")}));
        panelType.add(labelType);
        panelType.add(comboBoxType);

        Object[] params = {labelAddLicence, textFieldName, panelType};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().isEmpty()) {
                    String identifier = null;
                    if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.movie"))) {
                        identifier = "MOVIE";
                    } else if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.book"))) {
                        identifier = "BOOK";
                    } else if (Objects.equals(comboBoxType.getSelectedItem(), I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.sport"))) {
                        identifier = "SPORT";
                    }
                    Licence licence = new Licence(textFieldName.getText(), null, LicenceType.getTypeByIdentifier(identifier));
                    boolean licenceAlreadyExists = false;
                    for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
                        if (entry.getValue().equals(licence.getLine())) {
                            licenceAlreadyExists = true;
                        }
                    }
                    if (!licenceAlreadyExists) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(I18n.INSTANCE.get("mod.licence.addMod.confirm")).append(":").append("\r\n")
                                .append(textFieldName.getText()).append("\r\n")
                                .append("Type: ").append(comboBoxType.getSelectedItem());
                        if (JOptionPane.showConfirmDialog(null, stringBuilder.toString(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(licence);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.licence.upperCase") + ": [" + textFieldName.getText() + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
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
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        return new Licence((String) map.get("NAME EN"), null, LicenceType.getTypeByIdentifier((String) map.get("LICENCE TYP")));
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        LicenceType licenceType = null;
        String data = fileContent.get(getContentIdByName(name)).replace(name, "");
        if (data.contains("BOOK")) {
            licenceType = LicenceType.BOOK;
        } else if (data.contains("MOVIE")) {
            licenceType = LicenceType.MOVIE;
        } else if (data.contains("SPORT")) {
            licenceType = LicenceType.SPORT;
        }
        return new Licence(name, null, licenceType);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
