package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AntiCheatMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(AntiCheatMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("DEV COSTS", map, bw);
    }

    @Override
    public String getMainTranslationKey() {
        return "antiCheat";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.antiCheatMod;
    }

    @Override
    public String getExportType() {
        return "anti_cheat";
    }

    @Override
    public String getGameFileName() {
        return "AntiCheat.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_anti_cheat.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
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
                    for(String string : getContentByAlphabet()){
                        if(textFieldName.getText().equals(string)){
                            modAlreadyExists = true;
                        }
                    }
                    if(!modAlreadyExists){
                        Map<String, String> antiCheatMap = new HashMap<>();
                        antiCheatMap.put("ID", Integer.toString(getFreeId()));
                        if(!nameTranslationsAdded.get()){
                            antiCheatMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                        }else{
                            antiCheatMap.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                            antiCheatMap.put("NAME EN", textFieldName.getText());
                        }
                        antiCheatMap.put("DATE", Objects.requireNonNull(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem())) + " " + spinnerUnlockYear.getValue().toString());
                        antiCheatMap.put("PRICE", spinnerCost.getValue().toString());
                        antiCheatMap.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
                        if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(antiCheatMap), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            createBackup();
                            addModToFile(antiCheatMap);
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
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        return "<html>" +
                I18n.INSTANCE.get("mod.antiCheat.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "<br>" +
                I18n.INSTANCE.get("commonText.unlockDate") + ": " + map.get("DATE") + "<br>" +
                I18n.INSTANCE.get("commonText.price") + ": " + map.get("PRICE") + "<br>" +
                I18n.INSTANCE.get("commonText.developmentCost") + ": " + map.get("DEV COSTS") + "<br>";
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public String getTypeCaps() {
        return "ANTI_CHEAT";
    }

    @Override
    public String getImportExportFileName() {
        return "antiCheat.txt";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }
}
