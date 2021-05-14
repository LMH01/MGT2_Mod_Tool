package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.NpcGamesAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.NpcGamesEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.NpcGamesSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class NpcGamesMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceMod.class);
    NpcGamesAnalyzer npcGamesAnalyzer = new NpcGamesAnalyzer();
    NpcGamesEditor npcGamesEditor = new NpcGamesEditor();
    NpcGamesSharer npcGamesSharer = new NpcGamesSharer();
    public ArrayList<JMenuItem> npcGamesModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    @Override
    public AbstractSimpleAnalyzer getBaseAnalyzer() {
        return npcGamesAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getBaseEditor() {
        return npcGamesEditor;
    }

    @Override
    public AbstractSimpleSharer getBaseSharer() {
        return npcGamesSharer;
    }

    @Override
    public AbstractSimpleMod getSimpleMod() {
        return ModManager.npcGamesMod;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return npcGamesModMenuItems;
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//NpcGames.txt");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.npcGames.upperCase");
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.2", "2.0.3", "2.0.4", "2.0.5"};
    }

    @Override
    public void menuActionAddMod() {
        JLabel labelModName = new JLabel(I18n.INSTANCE.get("mod.npcGames.addMod.components.labelName"));
        JTextField textFieldName = new JTextField();

        JLabel labelExplainList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcGames.addMod.selectGenres") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
        JList<String> listAvailableThemes = new JList<>(ModManager.genreMod.getAnalyzer().getContentByAlphabet());
        listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
        listAvailableThemes.setVisibleRowCount(-1);
        JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
        scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

        Object[] params = {labelModName, textFieldName, labelExplainList, scrollPaneAvailableGenres};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                String newModName = textFieldName.getText();
                if(!newModName.isEmpty()){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        boolean modAlreadyExists = false;
                        for(String string : ModManager.npcGamesMod.getBaseAnalyzer().getContentByAlphabet()){
                            if (newModName.equals(string)) {
                                modAlreadyExists = true;
                                break;
                            }
                        }
                        if(!modAlreadyExists){
                            StringBuilder newModLine = new StringBuilder();
                            newModLine.append(newModName).append(" ");
                            ArrayList<Integer> genreIds = new ArrayList<>();
                            for(String string : listAvailableThemes.getSelectedValuesList()){
                                genreIds.add(ModManager.genreMod.getAnalyzer().getContentIdByName(string));
                            }
                            Collections.sort(genreIds);
                            for(Integer integer : genreIds){
                                newModLine.append("<").append(integer).append(">");
                            }
                            if(JOptionPane.showConfirmDialog(null, getBaseSharer().getOptionPaneMessage(newModLine.toString()), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                try {
                                    Backup.createBackup(getFile());
                                    ModManager.npcGamesMod.getBaseEditor().addMod(newModLine.toString());
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + getType() + " - " + newModName);
                                    JOptionPane.showMessageDialog(null, getType() + " [" + newModName + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"));
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.unableToAdd") + " " + getType() + " - " + I18n.INSTANCE.get("commonText.modAlreadyExists"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.chooseGenreFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                break;
            }
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "npcGames";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.npcGames.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "npcGame.txt";
    }
}
