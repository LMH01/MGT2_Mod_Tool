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
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.antiCheat.addMod.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            JSpinner spinnerCost = WindowHelper.getCostSpinner();
            JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerCost, 8), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7)};
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
                            antiCheatMap.put("DATE", Objects.requireNonNull(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem())) + " " + spinnerUnlockYear.getValue().toString());
                            antiCheatMap.put("PRICE", spinnerCost.getValue().toString());
                            antiCheatMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(antiCheatMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                Backup.createBackup(getFile());
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
            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "antiCheat";
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
