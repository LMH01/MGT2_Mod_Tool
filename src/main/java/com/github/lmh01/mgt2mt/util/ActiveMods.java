package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedModOld;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleModOld;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActiveMods {
    public static void showActiveMods() {
        boolean noModsActive = true;
        ArrayList<Object> objects = new ArrayList<>();
        for(AbstractAdvancedModOld advancedMod : ModManager.advancedMods){
            String[] customContent = advancedMod.getBaseAnalyzer().getCustomContentString();
            if(customContent.length > 0){
                JPanel mod = new JPanel();
                JLabel labelMod = new JLabel(advancedMod.getType() + ": ");
                JButton buttonMods = new JButton(Integer.toString(customContent.length));
                buttonMods.addActionListener(actionEvent -> {
                    JList<String> listAvailableGenres = new JList<>(advancedMod.getBaseAnalyzer().getCustomContentString());
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));
                    Object[] params = {labelMod, scrollPaneAvailableGenres};
                    JOptionPane.showMessageDialog(null, params);
                });
                mod.add(labelMod);
                mod.add(buttonMods);
                objects.add(mod);
                noModsActive = false;
            }
        }
        for(AbstractSimpleModOld simpleMod : ModManager.simpleMods){
            String[] customContent;
            if(simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                customContent = ModManager.themeModOld.getAnalyzerEn().getCustomContentString(true);
            }else {
                customContent = simpleMod.getBaseAnalyzer().getCustomContentString(true);
            }
            if(customContent.length > 0){
                JPanel mod = new JPanel();
                JLabel labelMod = new JLabel(simpleMod.getType() + ": ");
                JButton buttonMods = new JButton(Integer.toString(customContent.length));
                buttonMods.addActionListener(actionEvent -> {
                    JList<String> listAvailableGenres;
                    if(simpleMod.getType().equals(I18n.INSTANCE.get("commonText.theme.upperCase"))){
                        listAvailableGenres = new JList<>(ModManager.themeModOld.getAnalyzerEn().getCustomContentString());
                    }else{
                        listAvailableGenres = new JList<>(simpleMod.getBaseAnalyzer().getCustomContentString());
                    }
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));
                    Object[] params = {labelMod, scrollPaneAvailableGenres};
                    JOptionPane.showMessageDialog(null, params);
                });
                mod.add(labelMod);
                mod.add(buttonMods);
                objects.add(mod);
                noModsActive = false;
            }
        }
        if(noModsActive){
            JOptionPane.showMessageDialog(null,I18n.INSTANCE.get("window.showActiveMods.message.firstPart") + System.getProperty("line.separator") + System.getProperty("line.separator") + I18n.INSTANCE.get("dialog.sharingManager.importAll.noModsAvailable"));
        }else{
            JOptionPane.showMessageDialog(null, objects.toArray());
        }
    }
}
