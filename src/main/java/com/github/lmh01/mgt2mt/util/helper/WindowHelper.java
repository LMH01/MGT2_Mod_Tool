package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.content.managed.types.TypeEnum;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.settings.SafetyFeature;
import com.github.lmh01.mgt2mt.util.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contains functions that are helpful when specific parts should be added to a window
 */
public class WindowHelper {

    /**
     * @param textField The name text area
     * @return Returns a new panel containing the components of the name
     */
    public static JPanel getNamePanel(JTextField textField) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param textField The description text area
     * @return Returns a new panel containing the components of the description
     */
    public static JPanel getDescriptionPanel(JTextField textField) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.description") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param textField The description text area
     * @return Returns a new panel containing the components of the manufacturer
     */
    public static JPanel getManufacturerPanel(JTextField textField) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.manufacturer") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param spinner  The unlock year spinner
     * @param comboBox The unlock month combo box
     * @return Returns a new panel containing the components of the unlock date
     */
    public static JPanel getUnlockDatePanel(JComboBox<String> comboBox, JSpinner spinner) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.unlockDate") + ":");
        panel.add(label);
        panel.add(comboBox);
        panel.add(spinner);
        return panel;
    }

    /**
     * @param comboBox The type combo box
     * @return Returns a new panel containing the components of the type combo box
     */
    public static JPanel getTypePanel(JComboBox<String> comboBox) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        panel.add(label);
        panel.add(comboBox);
        return panel;
    }

    /**
     * @param spinner     The spinner that should be added to the panel
     * @param spinnerType The spinner type. Indicates what is written in the name
     * @return Returns a new panel containing the components of the type
     */
    public static JPanel getSpinnerPanel(JSpinner spinner, SpinnerType spinnerType) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(spinnerType.getTypeName()));
        panel.add(spinner);
        return panel;
    }

    /**
     * @return Returns a new panel containing the spinner and a label next to the spinner
     */
    public static JPanel getSpinnerPanel(JSpinner spinner, String labelTranslationKey) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(I18n.INSTANCE.get(labelTranslationKey) + ": "));
        panel.add(spinner);
        return panel;
    }

    /**
     * @return Returns a new spinner set with the values for the cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getCostSpinner() {
        return getBaseSpinner("commonText.cost.spinner.toolTip", 30000, 0, 1000000, 5000);
    }

    /**
     * @return Returns a new spinner set with the values for the dev cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getDevCostSpinner() {
        return getBaseSpinner("commonText.developmentCost.spinner", 35000, 0, 1000000, 5000);
    }

    public static JSpinner getDevKitCostSpinner() {
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.researchCost.toolTip", 50000, 1000, 1000000, 1000);
    }

    public static JSpinner getComplexitySpinner() {
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.complexity.toolTip", 0, 0, 2, 1);
    }

    public static JSpinner getUnitsSpinner() {
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.units.toolTip", 1000000, 100000, 1000000000, 100000);
    }

    /**
     * @return Returns a new spinner set with the values for the research point cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getResearchPointSpinner() {
        return getBaseSpinner("commonText.researchPointCost.spinner.toolTip", 500, 0, 6000, 100);
    }

    /**
     * @return Returns a new spinner set with the values for the research cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getResearchCostSpinner() {
        return getBaseSpinner("commonText.researchCost.spinner.toolTip", 50000, 0, 1000000, 5000);
    }

    /**
     * @return Returns a new spinner set with the values for the unlock year spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getUnlockYearSpinner() {
        JSpinner spinner = new JSpinner();
        if (Settings.safetyFeatures.get(SafetyFeature.UNLOCK_SPINNERS)) {
            spinner.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinner.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(true);
        } else {
            spinner.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinner.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        }
        return spinner;
    }

    /**
     * @return Returns a new combo box set with the values for the unlock month. The following is already initialized: Model, selected item and tooltip.
     */
    public static JComboBox<String> getUnlockMonthComboBox() {
        return getComboBox(Months.class, "commonText.unlockMonth.toolTip", Months.JAN.getTypeName());
    }

    /**
     * @param mapTranslationsAdded The map that should contain the translations
     * @param translationsAdded    The atomic boolean that contains if name translations have been added previously
     * @param type                 Determines for what type the button is. 0 = Name translations; 1 = Description translations; 2 = Manufacturer translations
     * @return Returns a new button with tool tip, label and action listener already initialized. This button is used to add translations to a mod
     */
    public static JButton getAddTranslationsButton(Map<String, String>[] mapTranslationsAdded, AtomicBoolean translationsAdded, int type) {
        String buttonLabel;
        String buttonToolTipTranslationsAdded;
        String buttonToolTipAddTranslations;
        switch (type) {
            case 0:
                buttonLabel = I18n.INSTANCE.get("commonText.addNameTranslations");
                buttonToolTipTranslationsAdded = I18n.INSTANCE.get("commonText.addNameTranslations.added");
                buttonToolTipAddTranslations = I18n.INSTANCE.get("commonText.addNameTranslations.toolTip");
                break;
            case 1:
                buttonLabel = I18n.INSTANCE.get("commonText.addDescriptionTranslations");
                buttonToolTipTranslationsAdded = I18n.INSTANCE.get("commonText.addDescriptionTranslations.added");
                buttonToolTipAddTranslations = I18n.INSTANCE.get("commonText.addDescriptionTranslations.toolTip");
                break;
            case 2:
                buttonLabel = I18n.INSTANCE.get("mod.platform.addPlatform.components.button.addManufacturerTranslations.label");
                buttonToolTipTranslationsAdded = I18n.INSTANCE.get("commonText.addNameTranslations.added");
                buttonToolTipAddTranslations = I18n.INSTANCE.get("commonText.addNameTranslations.toolTip");
                break;
            default:
                throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-1; Was: " + type);
        }
        JButton button = new JButton(buttonLabel);
        button.setToolTipText(buttonToolTipAddTranslations);
        button.addActionListener(actionEvent -> {
            if (!translationsAdded.get()) {
                mapTranslationsAdded[0] = TranslationManager.getTranslationsMap();
                if (mapTranslationsAdded[0].size() > 0) {
                    translationsAdded.set(true);
                    button.setText(buttonToolTipTranslationsAdded);
                } else {
                    button.setText(buttonLabel);
                }
            } else {
                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION) {
                    mapTranslationsAdded[0] = TranslationManager.getTranslationsMap();
                    if (mapTranslationsAdded[0].size() > 0) {
                        button.setText(buttonToolTipTranslationsAdded);
                        translationsAdded.set(true);
                    } else {
                        button.setText(buttonLabel);
                    }
                }
            }
        });
        return button;
    }

    /**
     * @param c                     The enum class that contains the values
     * @param toolTipTranslationKey The translation key for the tool tip of the combo box
     * @param selectedItem          The item that should be selected
     * @param <E>                   An enum that implements the interface {@link TypeEnum}
     * @return A new {@link JComboBox}.
     */
    public static <E extends Enum<?> & TypeEnum> JComboBox<String> getComboBox(Class<E> c, String toolTipTranslationKey, String selectedItem) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setToolTipText(I18n.INSTANCE.get(toolTipTranslationKey));
        ArrayList<String> modelContent = new ArrayList<>();
        for (E o : c.getEnumConstants()) {
            modelContent.add(o.getTypeName());
        }
        Collections.sort(modelContent);
        String[] model = new String[modelContent.size()];
        modelContent.toArray(model);
        comboBox.setModel(new DefaultComboBoxModel<>(model));
        comboBox.setSelectedItem(selectedItem);
        return comboBox;
    }

    /**
     * @return Returns a new spinner set with the values for the tech level spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getTechLevelSpinner() {
        return getBaseSpinner("commonText.techLevel.spinner.toolTip", 1, 1, 9, 1);
    }

    /**
     * @return Returns a new spinner set with the values for the point spinners. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getPointSpinner() {
        return getBaseSpinner("commonText.points.spinner.toolTip", 100, 0, 3000, 5);
    }

    /**
     * @return Returns a new spinner set with the values for the profit share spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getProfitShareSpinner() {
        return getBaseSpinner("mod.npcEngine.addMod.components.spinner.share.toolTip", 5, 0, 10, 1);
    }

    /**
     * @return Returns a new spinner with the input settings
     */
    public static JSpinner getBaseSpinner(String toolTipTranslationKey, int value, int minValue, int maxValue, int stepSize) {
        JSpinner spinner = new JSpinner();
        spinner.setToolTipText(I18n.INSTANCE.get(toolTipTranslationKey));
        setSpinnerComponents(spinner, value, minValue, maxValue, stepSize);
        return spinner;
    }

    /**
     * Sets the following:
     * The spinner model with the input values.
     * The tool tip text will be modified to include a information about the values that can be entered into the spinner.
     * If the safety features are disabled the spinner model will be unlocked.
     */
    private static void setSpinnerComponents(JSpinner spinner, int value, int minValue, int maxValue, int stepSize) {
        String currentToolTipText = spinner.getToolTipText();
        spinner.setToolTipText("<html>["
                + I18n.INSTANCE.get("commonText.range")
                + ":  "
                + Utils.convertIntToString(minValue)
                + " - "
                + Utils.convertIntToString(maxValue)
                + "; "
                + I18n.INSTANCE.get("commonText.default")
                + ": "
                + Utils.convertIntToString(value)
                + "]"
                + "<br>"
                + currentToolTipText);
        if (Settings.safetyFeatures.get(SafetyFeature.UNLOCK_SPINNERS)) {
            spinner.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(true);
        } else {
            spinner.setModel(new SpinnerNumberModel(value, minValue, maxValue, stepSize));
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        }
    }

    /**
     * @return Returns a new scroll pane with the input parameters
     */
    public static JScrollPane getScrollPane(JList<String> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(315, 140));
        return scrollPane;
    }

    /**
     * @param listContent             The content that should be written into the list
     * @param allowMultipleSelections True if multiple selection is allowed
     * @return Returns a new list with the input parameters
     */
    public static JList<String> getList(String[] listContent, boolean allowMultipleSelections) {
        JList<String> list = new JList<>(listContent);
        if (allowMultipleSelections) {
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        return list;
    }

    /**
     * @param listContent  The content that should be displayed in the list
     * @param listHeadline The string that should be written above the list when the button is clicked
     * @return Returns a new button that displays a list when clicked.
     */
    public static JButton getListDisplayButton(String buttonLabel, String[] listContent, String listHeadline) {
        JButton button = new JButton(buttonLabel);
        button.addActionListener(actionEvent -> {
            JLabel label = new JLabel(listHeadline);
            JList<String> list = getList(listContent, false);
            JScrollPane scrollPane = getScrollPane(list);
            Object[] params = {label, scrollPane};
            JOptionPane.showMessageDialog(null, params);
        });
        return button;
    }

    /**
     * Adds the following action to the button:
     * When the button is clicked a message is displayed where the user can select an entry for a list.
     *
     * @param labelText The label of the button and the message that is written above the list when the button is clicked
     * @param list      The list that should be displayed
     * @param action    The action that should be performed when the user clicks ok
     */
    public static void setListButtonAction(JButton button, String labelText, JList<String> list, ActionListener action) {
        button.addActionListener(actionEvent -> {
            JLabel label = new JLabel(labelText);
            JScrollPane scrollPane = getScrollPane(list);
            Object[] params = {label, scrollPane};
            if (JOptionPane.showConfirmDialog(null, params, labelText, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                action.actionPerformed(actionEvent);
            }
        });
    }
}
