package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AntiCheatAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AntiCheatEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AntiCheatSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class AntiCheatMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureMod.class);
    AntiCheatAnalyzer antiCheatAnalyzer = new AntiCheatAnalyzer();
    AntiCheatEditor antiCheatEditor = new AntiCheatEditor();
    AntiCheatSharer antiCheatSharer = new AntiCheatSharer();
    public ArrayList<JMenuItem> antiCheatModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return antiCheatAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return antiCheatEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return antiCheatSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.antiCheatMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return antiCheatModMenuItems;
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//AntiCheat.txt");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.antiCheat.upperCase");
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel(getType() + " " + I18n.INSTANCE.get("commonText.name") + ":");
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.antiCheat.addMod.components.textFieldName.initialValue"));
            panelName.add(labelName);
            panelName.add(textFieldName);

            JButton buttonAddNameTranslations = new JButton(I18n.INSTANCE.get("commonText.addNameTranslations"));
            buttonAddNameTranslations.setToolTipText(I18n.INSTANCE.get("commonText.addNameTranslations.toolTip"));
            buttonAddNameTranslations.addActionListener(actionEvent -> {
                if(!nameTranslationsAdded.get()){
                    mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                    if(mapNameTranslations[0].size() > 0){
                        nameTranslationsAdded.set(true);
                        buttonAddNameTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations.added"));
                    }else{
                        buttonAddNameTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations"));
                    }
                }else{
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                        mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                        if(mapNameTranslations[0].size() > 0){
                            buttonAddNameTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations.added"));
                            nameTranslationsAdded.set(true);
                        }else{
                            buttonAddNameTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations"));
                        }
                    }
                }
            });

            JPanel panelUnlockDate = new JPanel();
            JLabel labelUnlockDate = new JLabel(I18n.INSTANCE.get("commonText.unlockDate") + ":");

            JComboBox comboBoxUnlockMonth = new JComboBox();
            comboBoxUnlockMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            comboBoxUnlockMonth.setSelectedItem("JAN");

            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockDate.add(labelUnlockDate);
            panelUnlockDate.add(comboBoxUnlockMonth);
            panelUnlockDate.add(spinnerUnlockYear);

            JPanel panelCost = new JPanel();
            JLabel labelCost = new JLabel(I18n.INSTANCE.get("commonText.price") + ":");
            JSpinner spinnerCost = new JSpinner();
            spinnerCost.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 1.000.000; " + I18n.INSTANCE.get("commonText.default") + ": 30.000]" + I18n.INSTANCE.get("mod.antiCheat.addMod.components.spinner.cost.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerCost.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE,5000));
                ((JSpinner.DefaultEditor)spinnerCost.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerCost.setModel(new SpinnerNumberModel(30000, 0, 1000000,5000));
                ((JSpinner.DefaultEditor)spinnerCost.getEditor()).getTextField().setEditable(false);
            }
            panelCost.add(labelCost);
            panelCost.add(spinnerCost);

            JPanel panelDevelopmentCost = new JPanel();
            JLabel labelDevelopmentCost = new JLabel(I18n.INSTANCE.get("commonText.developmentCost") + ":");
            JSpinner spinnerDevelopmentCost = new JSpinner();
            spinnerDevelopmentCost.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 1.000.000; " + I18n.INSTANCE.get("commonText.default") + ": 35.000]" + "<br>" + I18n.INSTANCE.get("commonText.developmentCost.spinner"));
            if(Settings.disableSafetyFeatures){
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerDevelopmentCost.setModel(new SpinnerNumberModel(35000, 0, 1000000, 5000));
                ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(false);
            }
            panelDevelopmentCost.add(labelDevelopmentCost);
            panelDevelopmentCost.add(spinnerDevelopmentCost);

            Object[] params = {panelName, buttonAddNameTranslations, panelUnlockDate, panelCost, panelDevelopmentCost};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.antiCheat.addMod.components.textFieldName.initialValue"))){
                        boolean modAlreadyExists = false;
                        for(String string : getBaseAnalyzer().getContentByAlphabet()){
                            if(textFieldName.getText().equals(string)){
                                modAlreadyExists = true;
                            }
                        }
                        if(!modAlreadyExists){
                            Map<String, String> antiCheatMap = new HashMap<>();
                            antiCheatMap.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                            if(!nameTranslationsAdded.get()){
                                antiCheatMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            }else{
                                antiCheatMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                antiCheatMap.put("NAME EN", textFieldName.getText());
                            }
                            antiCheatMap.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
                            antiCheatMap.put("PRICE", spinnerCost.getValue().toString());
                            antiCheatMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(antiCheatMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                getBaseEditor().addMod(antiCheatMap);
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.antiCheat.upperCase") + " - " + antiCheatMap.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.antiCheat.upperCase") + ": [" + antiCheatMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "antiCheat";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.antiCheat.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "antiCheat.txt";
    }
}
