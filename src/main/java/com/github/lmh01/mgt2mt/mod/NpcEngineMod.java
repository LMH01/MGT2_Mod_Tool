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
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.0"};
    }

    @Override
    public void menuActionAddMod() {
        try{
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel(getType() + " " + I18n.INSTANCE.get("commonText.name") + ":");
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"));
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
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2999]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip") + "<br>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.unlockYear.additionalToolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2999, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 1976 - 2050]<br>" + I18n.INSTANCE.get("commonText.unlockYear.toolTip") + "<br>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.unlockYear.additionalToolTip"));
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockDate.add(labelUnlockDate);
            panelUnlockDate.add(comboBoxUnlockMonth);
            panelUnlockDate.add(spinnerUnlockYear);

            JPanel panelShare = new JPanel();
            JLabel labelShare = new JLabel(I18n.INSTANCE.get("commonText.profitShare") + " (in %):");
            JSpinner spinnerShare = new JSpinner();
            spinnerShare.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 100; " + I18n.INSTANCE.get("commonText.default") + ": 10]" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.spinner.share.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerShare.setModel(new SpinnerNumberModel(0, 0, 100,1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerShare.setModel(new SpinnerNumberModel(10, 0, 50,1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(false);
            }
            panelShare.add(labelShare);
            panelShare.add(spinnerShare);

            JPanel panelCost = new JPanel();
            JLabel labelCost = new JLabel(I18n.INSTANCE.get("commonText.price") + ":");
            JSpinner spinnerCost = new JSpinner();
            spinnerCost.setToolTipText("<html>[" + I18n.INSTANCE.get("commonText.range") + ": 0 - 1.000.000; " + I18n.INSTANCE.get("commonText.default") + ": 30.000]" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.spinner.cost.toolTip"));
            if(Settings.disableSafetyFeatures){
                spinnerCost.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE,5000));
                ((JSpinner.DefaultEditor)spinnerCost.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerCost.setModel(new SpinnerNumberModel(30000, 0, 1000000,5000));
                ((JSpinner.DefaultEditor)spinnerCost.getEditor()).getTextField().setEditable(false);
            }
            panelCost.add(labelCost);
            panelCost.add(spinnerCost);

            JLabel labelExplainGenreList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectGenre") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
            JList<String> listAvailableGenres = new JList<>(ModManager.genreMod.getAnalyzer().getContentByAlphabet());
            listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
            listAvailableGenres.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            JLabel labelExplainPlatformList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectPlatform"));
            JList<String> listAvailablePlatforms = new JList<>(ModManager.platformMod.getBaseAnalyzer().getContentByAlphabet());
            listAvailablePlatforms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listAvailablePlatforms.setLayoutOrientation(JList.VERTICAL);
            listAvailablePlatforms.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailablePlatforms = new JScrollPane(listAvailablePlatforms);
            scrollPaneAvailablePlatforms.setPreferredSize(new Dimension(315,140));

            Object[] param = {panelName, buttonAddNameTranslations, panelUnlockDate, panelShare, panelCost, labelExplainGenreList, scrollPaneAvailableGenres, labelExplainPlatformList, scrollPaneAvailablePlatforms};
            while(true){
                if(JOptionPane.showConfirmDialog(null, param, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"))){
                        if(!npcEngineNamesWithoutGenre().contains(textFieldName.getText())){
                            if(!listAvailablePlatforms.getSelectedValuesList().isEmpty()){
                                if(!listAvailableGenres.getSelectedValuesList().isEmpty()){
                                    //Filling base map (Without genre, id and names)
                                    Map<String, String> npcEngine = new HashMap<>();
                                    npcEngine.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
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
            String inputString = string;
            for(String string2 : ModManager.genreMod.getAnalyzer().getContentByAlphabet()){
                strings.add(inputString.replace(" [" + string2 + "]", ""));
            }
        }
        return strings;
    }
}
