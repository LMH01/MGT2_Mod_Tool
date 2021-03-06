package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CopyProtectAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.CopyProtectEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.CopyProtectSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class CopyProtectMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyProtectMod.class);
    CopyProtectAnalyzer copyProtectAnalyzer = new CopyProtectAnalyzer();
    CopyProtectEditor copyProtectEditor = new CopyProtectEditor();
    CopyProtectSharer copyProtectSharer = new CopyProtectSharer();
    public ArrayList<JMenuItem> copyModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return copyProtectAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return copyProtectEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return copyProtectSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.copyProtectMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return copyModMenuItems;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.copyProtect.addMod.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JComboBox comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            JSpinner spinnerCost = WindowHelper.getCostSpinner();
            JSpinner spinnerDevelopmentCost = WindowHelper.getDevCostSpinner();

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerCost, 8), WindowHelper.getSpinnerPanel(spinnerDevelopmentCost, 7)};
            while(true){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.copyProtect.addMod.components.textFieldName.initialValue"))){
                        boolean modAlreadyExists = false;
                        for(String string : getBaseAnalyzer().getContentByAlphabet()){
                            if(textFieldName.getText().equals(string)){
                                modAlreadyExists = true;
                            }
                        }
                        if(!modAlreadyExists){
                            Map<String, String> copyProtectMap = new HashMap<>();
                            copyProtectMap.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                            if(!nameTranslationsAdded.get()){
                                copyProtectMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                            }else{
                                copyProtectMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                copyProtectMap.put("NAME EN", textFieldName.getText());
                            }
                            copyProtectMap.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString());
                            copyProtectMap.put("PRICE", spinnerCost.getValue().toString());
                            copyProtectMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                            if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(copyProtectMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                Backup.createBackup(getFile());
                                getBaseEditor().addMod(copyProtectMap);
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.copyProtect.upperCase") + " - " + copyProtectMap.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.copyProtect.upperCase") + ": [" + copyProtectMap.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
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
        return "copyProtect";
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "copyProtect.txt";
    }
}
