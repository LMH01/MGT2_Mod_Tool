package com.github.lmh01.mgt2mt.util.handler;

import com.github.lmh01.mgt2mt.mod.NpcGamesMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class NPCGameListHandler {

    /**
     * Opens a window where the user can modify the npc games list
     */
    public static void modifyNPCGameList() throws ModProcessingException {
        ModManager.genreMod.analyzeFile();
        JLabel labelNPCGameList = new JLabel(I18n.INSTANCE.get("window.npcGamesList.label"));

        JPanel panelChance = new JPanel();
        JLabel labelChance = new JLabel(I18n.INSTANCE.get("window.npcGamesList.label.chance"));
        labelChance.setBounds(10,110,50,23);
        JSpinner spinnerChance = new JSpinner();
        spinnerChance.setModel(new SpinnerNumberModel(20, 1, 100, 1));
        spinnerChance.setToolTipText(I18n.INSTANCE.get("window.npcGamesList.spinner.chance.toolTip"));
        panelChance.add(labelChance);
        panelChance.add(spinnerChance);

        JPanel panelOperation = new JPanel();
        JLabel labelOperation = new JLabel(I18n.INSTANCE.get("window.npcGamesList.label.operation"));
        JComboBox<String> comboBoxOperation = new JComboBox<>();
        comboBoxOperation.setToolTipText(I18n.INSTANCE.get("window.npcGamesList.comboBox.operation.toolTip"));
        comboBoxOperation.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("window.npcGamesList.comboBox.operation.value_1"), I18n.INSTANCE.get("window.npcGamesList.comboBox.operation.value_2")}));
        panelOperation.add(labelOperation);
        panelOperation.add(comboBoxOperation);

        JLabel labelGenre = new JLabel(I18n.INSTANCE.get("window.npcGamesList.label.genre"));

        JList<String> listAvailableOperations = new JList<>(ModManager.genreMod.getCustomContentString());
        listAvailableOperations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableOperations.setLayoutOrientation(JList.VERTICAL);
        listAvailableOperations.setVisibleRowCount(-1);
        JScrollPane scrollPaneAvailableOperations = new JScrollPane(listAvailableOperations);
        scrollPaneAvailableOperations.setPreferredSize(new Dimension(315,140));

        ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.npcGamesList.title"));
        Object[] objects = {labelNPCGameList, panelChance, panelOperation, labelGenre, scrollPaneAvailableOperations};
        while(true){
            if(JOptionPane.showConfirmDialog(null, objects, I18n.INSTANCE.get("window.npcGamesList.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(I18n.INSTANCE.get("window.npcGamesList.confirmDialog.1")).append("\r\n");
                boolean addGenreID;
                if(Objects.equals(comboBoxOperation.getSelectedItem(), I18n.INSTANCE.get("window.npcGamesList.comboBox.operation.value_1"))) {
                    addGenreID = true;
                    stringBuilder.append(I18n.INSTANCE.get("window.npcGamesList.added")).append(" ").append(I18n.INSTANCE.get("window.npcGamesList.to")).append(" ");
                }else{
                    addGenreID = false;
                    stringBuilder.append(I18n.INSTANCE.get("window.npcGamesList.removed")).append(" ").append(I18n.INSTANCE.get("window.npcGamesList.from")).append(" ");
                }
                stringBuilder.append(I18n.INSTANCE.get("window.npcGamesList.confirmDialog.2")).append("\r\n");
                for(String string : listAvailableOperations.getSelectedValuesList()){
                    stringBuilder.append(string).append("\r\n");
                }
                if(!listAvailableOperations.getSelectedValuesList().isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, stringBuilder.toString(), I18n.INSTANCE.get("window.npcGamesList.confirmDialog.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        ProgressBarHelper.increaseMaxValue(listAvailableOperations.getSelectedValuesList().size());
                        ProgressBarHelper.increment();
                        for(String string : listAvailableOperations.getSelectedValuesList()){
                            try {
                                NpcGamesMod.editNPCGames(ModManager.genreMod.getContentIdByName(string), addGenreID, Integer.parseInt(spinnerChance.getValue().toString()));
                                if(addGenreID){
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("commonText.added.upperCase") + " " + string + " " + I18n.INSTANCE.get("window.npcGamesList.to") + " " + I18n.INSTANCE.get("window.npcGamesList.confirmDialog.2").replace(".", ""));
                                }else{
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("commonText.removed.upperCase") + " " + string + " " + I18n.INSTANCE.get("window.npcGamesList.from") + " " + I18n.INSTANCE.get("window.npcGamesList.confirmDialog.2").replace(".", ""));
                                }
                            } catch (ModProcessingException e) {
                                throw new ModProcessingException(I18n.INSTANCE.get("window.npcGamesList.errorOccurred") + ": " + e.getMessage(), e);
                            }
                            ProgressBarHelper.increment();
                        }
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.npcGamesList.success"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.npcGamesList.noGenreSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                break;
            }
        }
    }
}
