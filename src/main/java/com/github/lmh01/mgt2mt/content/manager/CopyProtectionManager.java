package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.CopyProtection;
import com.github.lmh01.mgt2mt.content.managed.AbstractAdvancedContentManager;
import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.DataLine;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CopyProtectionManager extends AbstractAdvancedContentManager {

    public static final CopyProtectionManager INSTANCE = new CopyProtectionManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", "5.0.0-beta1", "5.0.0", "5.0.1", MadGamesTycoon2ModTool.VERSION};

    private CopyProtectionManager() {
        super("copyProtect", "copy_protect", MGT2Paths.TEXT_DATA.getPath().resolve("CopyProtect.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        return new CopyProtection(map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DATE"),
                Integer.parseInt(map.get("PRICE")),
                Integer.parseInt(map.get("DEV COSTS")));
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> lines = new ArrayList<>();
        lines.add(new DataLine("DATE", true, DataType.STRING));
        lines.add(new DataLine("PRICE", true, DataType.INT));
        lines.add(new DataLine("DEV COSTS", true, DataType.INT));
        return lines;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.copyProtect.addMod.components.textFieldName.initialValue"));
        final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
        JSpinner spinnerCost = WindowHelper.getCostSpinner();
        JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();

        Object[] params = {WindowHelper.getNamePanel(textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerCost, SpinnerType.PRICE), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, SpinnerType.DEVELOPMENT_COST)};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!textFieldName.getText().equals(I18n.INSTANCE.get("mod.copyProtect.addMod.components.textFieldName.initialValue"))) {
                    boolean modAlreadyExists = false;
                    for (String string : getContentByAlphabet()) {
                        if (textFieldName.getText().equals(string)) {
                            modAlreadyExists = true;
                        }
                    }
                    if (!modAlreadyExists) {
                        TranslationManager translationManager = new TranslationManager(mapNameTranslations[0], null);
                        AbstractBaseContent copyProtection = new CopyProtection(textFieldName.getText(), null, translationManager, Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(), (int) spinnerCost.getValue(), (int) spinnerDevelopmentCost.getValue());
                        if (JOptionPane.showConfirmDialog(null, copyProtection.getOptionPaneMessage(), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(copyProtection);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.copyProtect.upperCase") + ": [" + copyProtection.name + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }
}
