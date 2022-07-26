package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.DevLegend;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.DevLegendType;
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

public class DevLegendsManager extends AbstractSimpleContentManager {

    public static final DevLegendsManager INSTANCE = new DevLegendsManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.3.0", "4.3.1", "4.4.0", MadGamesTycoon2ModTool.VERSION};

    private DevLegendsManager() {
        super("devLegend", "dev_legend", "default_devLegends.txt", MGT2Paths.TEXT_DATA.getPath().resolve("DevLegends.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    protected String getReplacedLine(String line) {
        return Utils.getFirstPart(line).trim();
    }

    @Override
    protected String isLineValid(String line) {
        int availableTags = 0;
        if (line.contains("<D>")) {
            availableTags++;
        }
        if (line.contains("<P>")) {
            availableTags++;
        }
        if (line.contains("<T>")) {
            availableTags++;
        }
        if (line.contains("<S>")) {
            availableTags++;
        }
        if (line.contains("<G>")) {
            availableTags++;
        }
        if (availableTags < 1) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.devLegendInvalid.tagMissing"), getReplacedLine(line));
        } else if (availableTags > 1) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.devLegendInvalid.tooManyTags"), getReplacedLine(line));
        }
        return "";
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JLabel labelAddLicence = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        JTextField textFieldName = new JTextField();

        JPanel panelType = new JPanel();
        JLabel labelType = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        JComboBox<String> comboBoxType = WindowHelper.getComboBox(DevLegendType.class, "mod.devLegend.components.spinner.type.toolTip", "commonText.graphic");
        panelType.add(labelType);
        panelType.add(comboBoxType);

        JCheckBox woman = new JCheckBox(I18n.INSTANCE.get("commonText.female"));
        woman.setToolTipText(I18n.INSTANCE.get("mod.devLegend.components.checkBox.female.toolTip"));

        Object[] params = {labelAddLicence, textFieldName, panelType, woman};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().isEmpty()) {
                    DevLegendType dlt = null;
                    for (DevLegendType devLegendType : DevLegendType.values()) {
                        if (Objects.requireNonNull(comboBoxType.getSelectedItem()).toString().equals(devLegendType.getTypeName())) {
                            dlt = devLegendType;
                        }
                    }
                    DevLegend devLegend = new DevLegend(textFieldName.getText(), null, dlt, woman.isSelected());
                    boolean devLegendExists = false;
                    for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
                        if (entry.getValue().equals(devLegend.getLine())) {
                            devLegendExists = true;
                        }
                    }
                    if (!devLegendExists) {
                        if (JOptionPane.showConfirmDialog(null, devLegend.getOptionPaneMessage(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(devLegend);
                            JOptionPane.showMessageDialog(null, getTypeUpperCase() + ": [" + textFieldName.getText() + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        String line = getLineByName(name);
        ArrayList<String> data = Utils.getEntriesFromString(line);
        DevLegendType type = null;
        boolean woman = false;
        for (String string : data) {
            if (string.equals("D")) {
                type = DevLegendType.DESIGN;
            } else if (string.equals("P")) {
                type = DevLegendType.PROGRAMMING;
            } else if (string.equals("T")) {
                type = DevLegendType.TECHNOLOGY;
            } else if (string.equals("S")) {
                type = DevLegendType.SOUND;
            } else if (string.equals("G")) {
                type = DevLegendType.GRAPHIC;
            } else if (string.equals("f")) {
                woman = true;
            }
        }
        return new DevLegend(name, null, type, woman);
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        String name = (String) map.get("NAME EN");
        return new DevLegend(name, null, DevLegendType.getTypeByIdentifier((String) map.get("TYP")), Boolean.getBoolean((String) map.get("WOMAN")));
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
