package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActiveMods {

    /**
     * Opens a window where every active mod is displayed
     *
     * @throws ModProcessingException If the custom content could not be parsed
     */
    public static void showActiveMods() throws ModProcessingException {
        boolean noModsActive = true;
        ArrayList<JPanel> objects = new ArrayList<>();
        for (BaseContentManager manager : ContentAdministrator.contentManagers) {
            String[] customContent = manager.getCustomContentString();
            if (customContent.length > 0) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel(manager.getType() + ": ");
                JButton button = new JButton(Integer.toString(customContent.length));
                button.addActionListener(actionEvent -> {
                    try {
                        JList<String> listAvailableGenres = new JList<>(manager.getCustomContentString());
                        listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                        listAvailableGenres.setVisibleRowCount(-1);
                        JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                        scrollPaneAvailableGenres.setPreferredSize(new Dimension(315, 140));
                        Object[] params = {label, scrollPaneAvailableGenres};
                        JOptionPane.showMessageDialog(null, params);
                    } catch (ModProcessingException e) {
                        ContentAdministrator.showException(e);
                    }
                });
                panel.add(label);
                panel.add(button);
                panel.setName(manager.getType());
                objects.add(panel);
                noModsActive = false;
            }
        }
        if (noModsActive) {
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("window.showActiveMods.message.firstPart") + System.getProperty("line.separator") + System.getProperty("line.separator") + I18n.INSTANCE.get("dialog.sharingManager.importAll.noModsAvailable"));
        } else {
            JOptionPane.showMessageDialog(null, Utils.getSortedModPanels(objects));
        }
    }
}
