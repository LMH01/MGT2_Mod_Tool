package com.github.lmh01.mgt2mt.util;

import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.*;

import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ContentAdministrator;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;

public class ContentUtils {

    /**
     * @param manager The content manager of which the content is that should be selected
     * @param id Atomic integer that stores the selected content id
     * @return A panel that contains a button that, when clicked opens a selection window where the user can select one content entry. The selected value is stored in id.
     */
    public static JPanel getContentSelectionPanel(BaseContentManager manager, AtomicInteger id, String labelTextTK, String buttonTextTK, String buttonToolTipTK) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(I18n.INSTANCE.get(labelTextTK) + ":");
        JButton buttonSelect = new JButton("        " + I18n.INSTANCE.get(buttonTextTK) + "        ");
        buttonSelect.setToolTipText(I18n.INSTANCE.get(buttonToolTipTK));
        buttonSelect.addActionListener(actionEvent -> {
            try {
                manager.analyzeFile();
                JLabel labelChoose = new JLabel(I18n.INSTANCE.get("commonText.select") + " " + manager.getType() + ":");
                JList<String> listAvailableContent = new JList<>(manager.getContentByAlphabet());
                listAvailableContent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listAvailableContent.setLayoutOrientation(JList.VERTICAL);
                listAvailableContent.setVisibleRowCount(-1);
                JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableContent);
                scrollPaneAvailableGenres.setPreferredSize(new Dimension(315, 140));

                Object[] params = {labelChoose, scrollPaneAvailableGenres};

                if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.select") + " " + manager.getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    String currentGenre = listAvailableContent.getSelectedValue();
                    id.set(manager.getContentIdByName(currentGenre));
                    buttonSelect.setText(currentGenre);
                }
            } catch (ModProcessingException e) {
                ContentAdministrator.showException(e);
            }
        });
        panel.add(label);
        panel.add(buttonSelect);
        return panel;
    }
}
