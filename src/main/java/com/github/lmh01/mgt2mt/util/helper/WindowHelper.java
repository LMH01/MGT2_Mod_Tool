package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseModOld;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contains functions that are helpful when specific parts should be added to a window
 */
public class WindowHelper {

    /**
     * @param baseMod The base mod
     * @param textField The name text area
     * @return Returns a new panel containing the components of the name
     */
    public static JPanel getNamePanel(AbstractBaseModOld baseMod, JTextField textField){//TODO Diese Funktion löschen
        JPanel panel = new JPanel();
        JLabel label = new JLabel(baseMod.getType() + " " + I18n.INSTANCE.get("commonText.name") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param baseMod The base mod
     * @param textField The name text area
     * @return Returns a new panel containing the components of the name
     */
    public static JPanel getNamePanel(AbstractBaseMod baseMod, JTextField textField){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(baseMod.getType() + " " + I18n.INSTANCE.get("commonText.name") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param textField The description text area
     * @return Returns a new panel containing the components of the description
     */
    public static JPanel getDescriptionPanel(JTextField textField){
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
    public static JPanel getManufacturerPanel(JTextField textField){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.manufacturer") + ":");
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /**
     * @param spinner The unlock year spinner
     * @param comboBox The unlock month combo box
     * @return Returns a new panel containing the components of the unlock date
     */
    public static JPanel getUnlockDatePanel(JComboBox<String> comboBox, JSpinner spinner){
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
    public static JPanel getTypePanel(JComboBox<String> comboBox){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        panel.add(label);
        panel.add(comboBox);
        return panel;
    }

    /**
     * @param spinner The spinner that should be added to the panel
     * @param type Declares what string should be used for the label. For further information look into the function
     * @return Returns a new panel containing the components of the type
     */
    public static JPanel getSpinnerPanel(JSpinner spinner, int type){
        JPanel panel = new JPanel();
        JLabel label;
        switch (type){
            case 0:
                label = new JLabel(I18n.INSTANCE.get("commonText.gameplay") + ":");
                break;
            case 1:
                label = new JLabel(I18n.INSTANCE.get("commonText.graphic") + ":");
                break;
            case 2:
                label = new JLabel(I18n.INSTANCE.get("commonText.sound") + ":");
                break;
            case 3:
                label = new JLabel(I18n.INSTANCE.get("commonText.tech") + ":");
                break;
            case 4:
                label = new JLabel(I18n.INSTANCE.get("commonText.techLevel") + ":");
                break;
            case 5:
                label = new JLabel(I18n.INSTANCE.get("commonText.researchCost") + ":");
                break;
            case 6:
                label = new JLabel(I18n.INSTANCE.get("commonText.researchPointCost") + ":");
                break;
            case 7:
                label = new JLabel(I18n.INSTANCE.get("commonText.developmentCost") + ":");
                break;
            case 8:
                label = new JLabel(I18n.INSTANCE.get("commonText.price") + ":");
                break;
            case 9:
                label = new JLabel(I18n.INSTANCE.get("commonText.profitShare") + " (in %):");
                break;
            case 10:
                label = new JLabel(I18n.INSTANCE.get("commonText.complexity") + ":");
                break;
            case 11:
                label = new JLabel(I18n.INSTANCE.get("commonText.units") + ":");
                break;
            case 12:
                label = new JLabel(I18n.INSTANCE.get("commonText.marketShare") + ":");
                break;
            default: throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-8; Was: " + type);
        }
        panel.add(label);
        panel.add(spinner);
        return panel;
    }

    /**
     * @param spinner The spinner that should be used
     * @param labelTranslationKey The translation key for the label that should be added
     * @return Returns a new panel containing the label and the translation key
     */
    public static JPanel getSpinnerPanel(JSpinner spinner, String labelTranslationKey){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get(labelTranslationKey));
        panel.add(label);
        panel.add(spinner);
        return panel;
    }

    /**
     * @return Returns a new spinner set with the values for the cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getCostSpinner(){
        return getBaseSpinner("commonText.cost.spinner.toolTip", 30000, 0, 1000000, 5000);
    }

    /**
     * @return Returns a new spinner set with the values for the dev cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getDevCostSpinner(){
        return getBaseSpinner("commonText.developmentCost.spinner", 35000, 0, 1000000, 5000);
    }

    public static JSpinner getDevKitCostSpinner(){
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.researchCost.toolTip", 50000, 1000, 1000000, 1000);
    }

    public static JSpinner getComplexitySpinner(){
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.complexity.toolTip", 0,0, 2, 1);
    }

    public static JSpinner getUnitsSpinner(){
        return getBaseSpinner("mod.platform.addPlatform.components.spinner.units.toolTip",1000000,100000, 1000000000, 100000);
    }

    public static JSpinner getMarketShareSpinner(){
        return getBaseSpinner("mod.publisher.addMod.optionPaneMessage.spinner.marketShare.toolTip",50, 1, 100, 1);
    }

    /**
     * @return Returns a new spinner set with the values for the research point cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getResearchPointSpinner(){
        return getBaseSpinner("commonText.researchPointCost.spinner.toolTip", 500, 0, 10000, 100);
    }

    /**
     * @return Returns a new spinner set with the values for the research cost spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getResearchCostSpinner(){
        return getBaseSpinner("commonText.researchCost.spinner.toolTip", 50000, 0, 1000000, 5000);
    }

    /**
     * @return Returns a new spinner set with the values for the unlock year spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getUnlockYearSpinner(){
        JSpinner spinner = new JSpinner();
        if(Settings.disableSafetyFeatures){
            spinner.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinner.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(true);
        }else{
            spinner.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
            spinner.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(false);
        }
        return spinner;
    }

    /**
     * @return Returns a new combo box set with the values for the unlock month. The following is already initialized: Model, selected item and tooltip.
     */
    public static JComboBox<String> getUnlockMonthComboBox(){
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
        comboBox.setSelectedItem("JAN");
        comboBox.setToolTipText(I18n.INSTANCE.get("commonText.unlockMonth.toolTip"));
        return comboBox;
    }

    /**
     * @param mapTranslationsAdded The map that should contain the translations
     * @param translationsAdded The atomic boolean that contains if name translations have been added previously
     * @param type Determines for what type the button is. 0 = Name translations; 1 = Description translations; 2 = Manufacturer translations
     * @return Returns a new button with tool tip, label and action listener already initialized. This button is used to add translations to a mod
     */
    public static JButton getAddTranslationsButton(Map<String, String>[] mapTranslationsAdded, AtomicBoolean translationsAdded, int type){
        String buttonLabel;
        String buttonToolTipTranslationsAdded;
        String buttonToolTipAddTranslations;
        switch(type){
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
            default: throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-1; Was: " + type);
        }
        JButton button = new JButton(buttonLabel);
        button.setToolTipText(buttonToolTipAddTranslations);
        button.addActionListener(actionEvent -> {
            if(!translationsAdded.get()){
                mapTranslationsAdded[0] = TranslationManager.getTranslationsMap();
                if(mapTranslationsAdded[0].size() > 0){
                    translationsAdded.set(true);
                    button.setText(buttonToolTipTranslationsAdded);
                }else{
                    button.setText(buttonLabel);
                }
            }else{
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                    mapTranslationsAdded[0] = TranslationManager.getTranslationsMap();
                    if(mapTranslationsAdded[0].size() > 0){
                        button.setText(buttonToolTipTranslationsAdded);
                        translationsAdded.set(true);
                    }else{
                        button.setText(buttonLabel);
                    }
                }
            }
        });
        return button;
    }

    /**
     * @param type Determines what values will be used for the initialized items. 0 = Engine feature types; 1 = Gameplay feature types; 2 = Console types
     * @return Returns a new combo box with tooltip, label, model and selected item already initialized.
     */
    public static JComboBox<String> getTypeComboBox(int type){
        JComboBox<String> comboBox = new JComboBox<>();
        switch(type){
            case 0:
                comboBox.setToolTipText(I18n.INSTANCE.get("mod.engineFeature.addMod.components.type.toolTip"));
                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Graphic", "Sound", "Artificial Intelligence", "Physics"}));
                comboBox.setSelectedItem("Graphic");
                break;
            case 1:
                comboBox.setToolTipText(I18n.INSTANCE.get("mod.gameplayFeature.addMod.components.type.toolTip"));
                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Graphic", "Sound", "Physics", "Gameplay", "Control", "Multiplayer"}));
                comboBox.setSelectedItem("Graphic");
                break;
            case 2:
                comboBox.setToolTipText(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.toolTip"));
                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"), I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.console"), I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.handheld"), I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.cellPhone"), I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.arcadeSystemBoard")}));
                comboBox.setSelectedItem(I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type.computer"));
                break;
            case 3:
                comboBox.setToolTipText(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.comboBox.toolTip"));
                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("commonText.add.upperCase"), I18n.INSTANCE.get("commonText.remove.upperCase")}));
                comboBox.setSelectedItem(I18n.INSTANCE.get("commonText.add.upperCase"));
                break;
            case 4:
                comboBox.setToolTipText(I18n.INSTANCE.get("mod.hardware.addMod.components.comboBox.type.toolTip"));
                ArrayList<String> modelContent = new ArrayList<>();
                for(int i=0; i<10; i++){
                    modelContent.add(ModManager.hardwareModOld.getHardwareTypeNameById(i));
                }
                String[] model = new String[modelContent.size()];
                modelContent.toArray(model);
                comboBox.setModel(new DefaultComboBoxModel<>(model));
                comboBox.setSelectedItem(ModManager.hardwareModOld.getHardwareTypeNameById(0));
                break;
            default: throw new IllegalArgumentException("The input for the function type is invalid! Valid: 0-4; Was: " + type);
        }
        return comboBox;
    }

    /**
     * @return Returns a new spinner set with the values for the tech level spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getTechLevelSpinner(){
        return getBaseSpinner("commonText.techLevel.spinner.toolTip", 1, 1,8, 1);
    }

    /**
     * @return Returns a new spinner set with the values for the point spinners. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getPointSpinner(){
        return getBaseSpinner("commonText.points.spinner.toolTip", 100, 0, 4500, 5);
    }

    /**
     * @return Returns a new spinner set with the values for the profit share spinner. The following is already initialized: Min/max value, step size and tooltip.
     */
    public static JSpinner getProfitShareSpinner(){
        return getBaseSpinner("mod.npcEngine.addMod.components.spinner.share.toolTip", 10, 0, 10, 1);
    }

    /**
     * @return Returns a new spinner with the input settings
     */
    public static JSpinner getBaseSpinner(String toolTipTranslationKey, int value, int minValue, int maxValue, int stepSize){
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
    private static void setSpinnerComponents(JSpinner spinner, int value, int minValue, int maxValue, int stepSize){
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
        if(Settings.disableSafetyFeatures){
            spinner.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(true);
        }else{
            spinner.setModel(new SpinnerNumberModel(value, minValue, maxValue, stepSize));
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(false);
        }
    }

    /**
     * @return Returns a new scroll pane with the input parameters
     */
    public static JScrollPane getScrollPane(JList<String> list){
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(315,140));
        return scrollPane;
    }

    /**
     * @param listContent The content that should be written into the list
     * @param allowMultipleSelections True if multiple selection is allowed
     * @return Returns a new list with the input parameters
     */
    public static JList<String> getList(String[] listContent, boolean allowMultipleSelections){
        JList<String> list = new JList<>(listContent);
        if(allowMultipleSelections){
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }else{
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        return list;
    }

    /**
     * @param listContent The content that should be displayed in the list
     * @param listHeadline The string that should be written above the list when the button is clicked
     * @return Returns a new button that displays a list when clicked.
     */
    public static JButton getListDisplayButton(String buttonLabel, String[] listContent, String listHeadline){
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
}
