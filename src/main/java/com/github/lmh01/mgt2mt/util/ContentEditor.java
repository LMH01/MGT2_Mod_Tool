package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.ThemeEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import javax.swing.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

public class ContentEditor {

    /**
     * Opens a window where the user can edit the genre/theme fits
     */
    public static void editGenreThemeFit(){
        JLabel labelExplanation = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.message"));
        JLabel labelThemes = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.themeLabel"));
        JList<String> themeList = WindowHelper.getList(ModManager.themeModOld.getAnalyzerEn().getContentByAlphabet(), true);
        JScrollPane scrollPaneThemes = WindowHelper.getScrollPane(themeList);
        JLabel labelGenres = new JLabel(I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.genreLabel"));
        JList<String> genreList = WindowHelper.getList(ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), true);
        JScrollPane scrollPaneGenres = WindowHelper.getScrollPane(genreList);
        JComboBox<String> comboBoxOperation = WindowHelper.getTypeComboBox(3);
        Object[] params = {labelExplanation, labelThemes, scrollPaneThemes, labelGenres, scrollPaneGenres, comboBoxOperation};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.message.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                if(themeList.getSelectedValuesList().isEmpty() || genreList.getSelectedValuesList().isEmpty()){
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.selectThemeGenre"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }else{
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.confirmMessage"), I18n.INSTANCE.get("frame.title.areYouSure"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        boolean addGenre = !Objects.requireNonNull(comboBoxOperation.getSelectedItem()).toString().equals(I18n.INSTANCE.get("commonText.remove.upperCase"));
                        HashSet<Integer> themeIds = new HashSet<>();
                        for(String string : themeList.getSelectedValuesList()){
                            themeIds.add(ThemeFileAnalyzer.getPositionOfThemeInFile(string));
                        }
                        boolean errorOccurred = false;
                        StringBuilder errors = new StringBuilder();
                        for(String string : genreList.getSelectedValuesList()){
                            try {
                                ThemeEditor themeEditor = new ThemeEditor();
                                themeEditor.editGenreAllocationAdvanced(ModManager.genreModOld.getAnalyzer().getContentIdByName(string), addGenre, themeIds, false);
                            } catch (IOException e) {
                                errorOccurred = true;
                                e.printStackTrace();
                                errors.append(e.getMessage()).append("\n");
                            }
                        }
                        if(errorOccurred){
                            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.error") + "<br><br>" + errors, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.contentEditor.editGenreThemeFit.success"), I18n.INSTANCE.get("frame.title.success"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    }
                }
            }else{
                break;
            }
        }
    }
}
