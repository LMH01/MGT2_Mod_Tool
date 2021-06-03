package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.NpcEngineAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.NpcEngineEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.NpcEngineSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class NpcEngineMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcEngineMod.class);
    NpcEngineAnalyzer npcEngineAnalyzer = new NpcEngineAnalyzer();
    NpcEngineEditor npcEngineEditor = new NpcEngineEditor();
    NpcEngineSharer npcEngineSharer = new NpcEngineSharer();
    public ArrayList<JMenuItem> npcEngineModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return npcEngineAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return npcEngineEditor;
    }

    public NpcEngineEditor getEditor(){return npcEngineEditor;}

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return npcEngineSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.npcEngineMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return npcEngineModMenuItems;
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//NpcEngines.txt");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.npcEngine.upperCase");
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            JSpinner spinnerShare = WindowHelper.getProfitShareSpinner();
            JSpinner spinnerCost = WindowHelper.getCostSpinner();

            JLabel labelExplainGenreList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectGenre") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
            JList<String> listAvailableGenres = WindowHelper.getList(ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
            JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGenres);

            JLabel labelExplainPlatformList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectPlatform"));
            JList<String> listAvailablePlatforms = WindowHelper.getList(ModManager.platformMod.getBaseAnalyzer().getContentByAlphabet(), false);
            JScrollPane scrollPaneAvailablePlatforms = WindowHelper.getScrollPane(listAvailablePlatforms);

            Object[] param = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerShare, 9), WindowHelper.getSpinnerPanel(spinnerCost, 8), labelExplainGenreList, scrollPaneAvailableGenres, labelExplainPlatformList, scrollPaneAvailablePlatforms};
            while(true){
                if(JOptionPane.showConfirmDialog(null, param, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"))){
                        if(!npcEngineNamesWithoutGenre().contains(textFieldName.getText())){
                            if(!listAvailablePlatforms.getSelectedValuesList().isEmpty()){
                                if(!listAvailableGenres.getSelectedValuesList().isEmpty()){
                                    //Filling base map (Without genre, id and names)
                                    Map<String, String> npcEngine = new HashMap<>();
                                    npcEngine.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString());
                                    npcEngine.put("PLATFORM", Integer.toString(ModManager.platformMod.getBaseAnalyzer().getContentIdByName(listAvailablePlatforms.getSelectedValue())));
                                    npcEngine.put("PRICE", spinnerCost.getValue().toString());
                                    npcEngine.put("SHARE", spinnerShare.getValue().toString());
                                    if(listAvailableGenres.getSelectedValuesList().size() > 1){
                                        npcEngine.put("GENRE", "MULTIPLE SELECTED");
                                        npcEngine.put("NAME EN", textFieldName.getText());
                                        sendLogMessage("Multiple genres have been selected. Displaying multiple engines dialog.");
                                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres") + "<br><br>" + getBaseSharer().getOptionPaneMessage(npcEngine) + "<br><br>" + I18n.INSTANCE.get("commonText.isThisCorrect"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                            npcEngine.remove("NAME EN");
                                            npcEngine.remove("GENRE");
                                            Backup.createBackup(getFile());
                                            ProgressBarHelper.initializeProgressBar(0, listAvailableGenres.getSelectedValuesList().size(), I18n.INSTANCE.get(""));
                                            for(String string : listAvailableGenres.getSelectedValuesList()){
                                                getBaseAnalyzer().analyzeFile();
                                                npcEngine.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                                                Map<String, String> newNpcEngineMap = new HashMap<>();
                                                if(!nameTranslationsAdded.get()){
                                                    newNpcEngineMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText() + " [" + string + "]"));
                                                }else{
                                                    Map<String, String> newTranslations = new HashMap<>();
                                                    for(Map.Entry<String, String> entry : mapNameTranslations[0].entrySet()){
                                                        newTranslations.put(entry.getKey(), entry.getValue() + " [" + string + "]");
                                                    }
                                                    newNpcEngineMap.putAll(TranslationManager.transformTranslationMap(newTranslations, "NAME"));
                                                    newNpcEngineMap.put("NAME EN", textFieldName.getText() + " [" + string + "]");
                                                }
                                                newNpcEngineMap.putAll(npcEngine);
                                                newNpcEngineMap.put("GENRE", Integer.toString(ModManager.genreMod.getAnalyzer().getContentIdByName(string)));
                                                getBaseEditor().addMod(newNpcEngineMap);
                                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase") + " - " + newNpcEngineMap.get("NAME EN"));
                                                ProgressBarHelper.increment();
                                            }
                                            npcEngine.put("NAME EN", textFieldName.getText());
                                            JOptionPane.showMessageDialog(null, listAvailableGenres.getSelectedValuesList().size() + " " + I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres.finished"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                            ProgressBarHelper.resetProgressBar();
                                            break;
                                        }
                                    }else{
                                        npcEngine.put("ID", Integer.toString(getBaseAnalyzer().getFreeId()));
                                        if(!nameTranslationsAdded.get()){
                                            npcEngine.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                        }else{
                                            npcEngine.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                            npcEngine.put("NAME EN", textFieldName.getText());
                                        }
                                        npcEngine.put("GENRE", Integer.toString(ModManager.genreMod.getAnalyzer().getContentIdByName(listAvailableGenres.getSelectedValue())));
                                        sendLogMessage("Only a single genre has been selected. Displaying single engine dialog");
                                        if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(npcEngine), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                            Backup.createBackup(getFile());
                                            getBaseEditor().addMod(npcEngine);
                                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase") + " - " + npcEngine.get("NAME EN"));
                                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.npcEngine.upperCase") + ": [" + npcEngine.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        }
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.selectGenreFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.noPlatformSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
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
        return "npcEngine";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.npcEngine.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "npcEngine.txt";
    }

    /**
     * @return Returns a ArrayList containing the engine names without the genre names
     */
    private ArrayList<String> npcEngineNamesWithoutGenre(){
        ArrayList<String> strings = new ArrayList<>();
        for(String string : getBaseAnalyzer().getContentByAlphabet()){
            for(String string2 : ModManager.genreMod.getAnalyzer().getContentByAlphabet()){
                strings.add(string.replace(" [" + string2 + "]", ""));
            }
        }
        return strings;
    }
}
