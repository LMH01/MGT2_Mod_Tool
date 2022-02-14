package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.util.HashSet;
import java.util.Objects;

public class ContentEditor {

    /**
     * Opens a window where the user can edit the genre/theme fits
     */
    public static void editGenreThemeFit() throws ModProcessingException {
        JLabel labelExplanation = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.message"));
        JLabel labelThemes = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.themeLabel"));
        JList<String> themeList = WindowHelper.getList(ModManager.themeMod.getContentByAlphabet(), true);
        JScrollPane scrollPaneThemes = WindowHelper.getScrollPane(themeList);
        JLabel labelGenres = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.genreLabel"));
        JList<String> genreList = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), true);
        JScrollPane scrollPaneGenres = WindowHelper.getScrollPane(genreList);
        JComboBox<String> comboBoxOperation = new JComboBox<>();
        comboBoxOperation.setToolTipText(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.comboBox.toolTip"));
        comboBoxOperation.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("commonText.add.upperCase"), I18n.INSTANCE.get("commonText.remove.upperCase")}));
        comboBoxOperation.setSelectedItem(I18n.INSTANCE.get("commonText.add.upperCase"));
        Object[] params = {labelExplanation, labelThemes, scrollPaneThemes, labelGenres, scrollPaneGenres, comboBoxOperation};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.message.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (themeList.getSelectedValuesList().isEmpty() || genreList.getSelectedValuesList().isEmpty()) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.selectThemeGenre"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.confirmMessage"), I18n.INSTANCE.get("frame.title.areYouSure"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        boolean addGenre = !Objects.requireNonNull(comboBoxOperation.getSelectedItem()).toString().equals(I18n.INSTANCE.get("commonText.remove.upperCase"));
                        HashSet<Integer> themeIds = new HashSet<>();
                        for (String string : themeList.getSelectedValuesList()) {
                            themeIds.add(ModManager.themeMod.getPositionOfThemeInFile(string));
                        }
                        for (String string : genreList.getSelectedValuesList()) {
                            ModManager.themeMod.editGenreAllocationAdvanced(ModManager.genreMod.getContentIdByName(string), addGenre, themeIds, false);
                        }
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.success"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
}
