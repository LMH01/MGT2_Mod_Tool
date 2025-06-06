package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class ContentEditor {

    /**
     * Opens a window where the user can edit the genre/theme fits
     *
     * @throws ModProcessingException Something went wrong while editing the genre theme fit
     */
    public static void editGenreThemeFit() throws ModProcessingException {
        JLabel labelExplanation = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.message"));
        JLabel labelThemes = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.themeLabel"));
        JList<String> themeList = WindowHelper.getList(ThemeManager.INSTANCE.getContentByAlphabet(), true);
        JScrollPane scrollPaneThemes = WindowHelper.getScrollPane(themeList);
        JLabel labelGenres = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.genreLabel"));
        JList<String> genreList = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), true);
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
                        ArrayList<Integer> themeIds = new ArrayList<>();
                        for (String string : themeList.getSelectedValuesList()) {
                            themeIds.add(ThemeManager.INSTANCE.getContentIdByName(string));
                        }
                        for (String string : genreList.getSelectedValuesList()) {
                            ThemeManager.editGenreAllocationAdvanced(GenreManager.INSTANCE.getContentIdByName(string), addGenre, themeIds, false);
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

    public static void editGenreSubGenreFit() throws ModProcessingException {
        JLabel labelExplanation = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.message"));
        JLabel labelGenresThemes = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.themeLabel"));
        JList<String> genreList = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), true);
        JScrollPane scrollPaneThemes = WindowHelper.getScrollPane(genreList);
        JLabel labelSubGenres = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.genreLabel"));
        JList<String> subGenreList = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), true);
        JScrollPane scrollPaneGenres = WindowHelper.getScrollPane(subGenreList);
        JComboBox<String> comboBoxOperation = new JComboBox<>();
        comboBoxOperation.setToolTipText(I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.comboBox.toolTip"));
        comboBoxOperation.setModel(new DefaultComboBoxModel<>(new String[]{I18n.INSTANCE.get("commonText.add.upperCase"), I18n.INSTANCE.get("commonText.remove.upperCase")}));
        comboBoxOperation.setSelectedItem(I18n.INSTANCE.get("commonText.add.upperCase"));
        Object[] params = {labelExplanation, labelGenresThemes, scrollPaneThemes, labelSubGenres, scrollPaneGenres, comboBoxOperation};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.message.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (genreList.getSelectedValuesList().isEmpty() || subGenreList.getSelectedValuesList().isEmpty()) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.selectThemeGenre"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.confirmMessage"), I18n.INSTANCE.get("frame.title.areYouSure"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        boolean addGenre = !Objects.requireNonNull(comboBoxOperation.getSelectedItem()).toString().equals(I18n.INSTANCE.get("commonText.remove.upperCase"));
                        ArrayList<Integer> genreIds = new ArrayList<>();
                        for (String string : genreList.getSelectedValuesList()) {
                            genreIds.add(GenreManager.INSTANCE.getContentIdByName(string));
                        }
                        ArrayList<Integer> subGenreIds = new ArrayList<>();
                        for (String string : subGenreList.getSelectedValuesList()) {
                            subGenreIds.add(GenreManager.INSTANCE.getContentIdByName(string));
                        }
                        GenreManager.editSubGenres(genreIds, subGenreIds, addGenre);
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreSubGenreFit.success"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
}
