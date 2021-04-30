package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileEnAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileGeAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.ThemeEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.ThemeSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
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
import static com.github.lmh01.mgt2mt.util.Utils.getMGT2TextFolderPath;

public class ThemeMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeMod.class);
    ThemeFileGeAnalyzer themeFileGeAnalyzer = new ThemeFileGeAnalyzer();
    ThemeFileEnAnalyzer themeFileEnAnalyzer = new ThemeFileEnAnalyzer();
    ThemeEditor themeEditor = new ThemeEditor();
    ThemeSharer themeSharer = new ThemeSharer();
    public ArrayList<JMenuItem> themeModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     * This function will analyze the theme file ge.
     */
    public ThemeFileGeAnalyzer getAnalyzerGe(){
        return themeFileGeAnalyzer;
    }

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     * This function will analyze the theme file en.
     */
    public ThemeFileEnAnalyzer getAnalyzerEn(){
        return themeFileEnAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public ThemeEditor getEditor(){
        return themeEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public ThemeSharer getSharer(){
        return themeSharer;
    }

    @Override
    public ThemeFileGeAnalyzer getBaseAnalyzer() {
        return themeFileGeAnalyzer;
    }

    @Override
    public ThemeEditor getBaseEditor() {
        return themeEditor;
    }

    @Override
    public ThemeSharer getBaseSharer() {
        return themeSharer;
    }

    @Override
    public AbstractSimpleMod getSimpleMod() {
        return ModManager.themeMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "2.0.0"};
    }

    @Override
    public String getMainTranslationKey() {
        return "theme";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.theme.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.theme.upperCase.plural");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return themeModMenuItems;
    }

    @Override
    public void menuActionAddMod() {
        try {
            ThemeFileAnalyzer.analyzeThemeFiles();
            final ArrayList<String>[] arrayListThemeTranslations = new ArrayList[]{new ArrayList<>()};
            ArrayList<Integer> arrayListCompatibleGenreIds = new ArrayList<>();
            String[] string = ModManager.genreMod.getAnalyzer().getContentByAlphabet();
            JLabel labelEnterThemeName = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.themeName"));
            JTextField textFieldThemeName = new JTextField();
            JButton buttonAddTranslations = new JButton(I18n.INSTANCE.get("commonText.addNameTranslations"));
            buttonAddTranslations.setToolTipText(I18n.INSTANCE.get("mod.theme.addTheme.components.button.addTranslations.toolTip"));
            buttonAddTranslations.addActionListener(actionEvent2 -> {
                if(!arrayListThemeTranslations[0].isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                        arrayListThemeTranslations[0].clear();
                        arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                    }
                }else{
                    arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                }
            });
            JPanel panelChooseViolenceLevel = new JPanel();
            JLabel labelViolenceLevel = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.violenceLevel"));
            JComboBox comboBoxViolenceLevel = new JComboBox();
            comboBoxViolenceLevel.setToolTipText(I18n.INSTANCE.get("mod.theme.addTheme.components.comboBox.violenceLevel"));
            comboBoxViolenceLevel.setModel(new DefaultComboBoxModel<>(new String[]{"0", "1", "2", "3"}));
            panelChooseViolenceLevel.add(labelViolenceLevel);
            panelChooseViolenceLevel.add(comboBoxViolenceLevel);
            JLabel labelExplainList = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.explainList"));
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddTranslations, panelChooseViolenceLevel, labelExplainList, scrollPaneAvailableGenres};
            ArrayList<String> arrayListCompatibleGenreNames = new ArrayList<>();
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.theme.addTheme.main.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        if(!textFieldThemeName.getText().isEmpty()){
                            if(!ModManager.themeMod.getAnalyzerEn().getFileContent().containsValue(textFieldThemeName.getText()) && !ModManager.themeMod.getAnalyzerGe().getFileContent().containsValue(textFieldThemeName.getText())){
                                if(!textFieldThemeName.getText().matches(".*\\d.*")){
                                    arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                    for(Map<String, String> map : ModManager.genreMod.getAnalyzer().getFileContent()){
                                        for(String name : arrayListCompatibleGenreNames){
                                            if(map.get("NAME EN").equals(name)){
                                                arrayListCompatibleGenreIds.add(Integer.parseInt(map.get("ID")));
                                            }
                                        }
                                    }
                                    Map<String, String> themeTranslations = new HashMap<>();
                                    int currentTranslationKey = 0;
                                    if(arrayListThemeTranslations[0].isEmpty()){
                                        for(String translationKey : TranslationManager.TRANSLATION_KEYS){
                                            themeTranslations.put("NAME " + translationKey, textFieldThemeName.getText());
                                        }
                                    }else{
                                        for(String translation : arrayListThemeTranslations[0]){
                                            themeTranslations.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentTranslationKey], translation);
                                            currentTranslationKey++;
                                        }
                                    }
                                    themeTranslations.put("NAME EN", textFieldThemeName.getText());
                                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + textFieldThemeName.getText(), I18n.INSTANCE.get("mod.theme.addTheme.addTheme.title"), JOptionPane.YES_NO_OPTION) == 0){
                                        Backup.createThemeFilesBackup(false, false);
                                        ModManager.themeMod.getEditor().addMod(themeTranslations, arrayListCompatibleGenreIds, Integer.parseInt(comboBoxViolenceLevel.getSelectedItem().toString()));
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + textFieldThemeName.getText());
                                        JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                        breakLoop = true;
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error4"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error3"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                arrayListCompatibleGenreNames.clear();
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error1"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "theme.txt";
    }

    @Override
    public void setMainMenuButtonAvailability() {
        String[] customContentString = getAnalyzerEn().getCustomContentString(true);
        for(JMenuItem menuItem : getModMenuItems()){
            if(menuItem.getText().replace("R", "r").replace("A", "a").contains(I18n.INSTANCE.get("commonText.remove"))){
                if(customContentString.length > 0){
                    menuItem.setEnabled(true);
                    menuItem.setToolTipText("");
                }else{
                    menuItem.setEnabled(false);
                    menuItem.setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
                }
            }
        }
        if(customContentString.length > 0){
            getExportMenuItem().setEnabled(true);
            getExportMenuItem().setToolTipText("");
        }else{
            getExportMenuItem().setEnabled(false);
            getExportMenuItem().setToolTipText(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod.toolTip"));
        }
    }

    @Override
    public File getFile() {
        return getFileGe();
    }

    @Override
    public void removeModMenuItemAction() {
        Thread thread = new Thread(() -> OperationHelper.process(getBaseEditor()::removeMod, getAnalyzerEn().getCustomContentString(), getAnalyzerEn().getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false));
        ThreadHandler.startThread(thread, "runnableRemove" + getType());
    }

    @Override
    public void exportMenuItemAction() {
        Thread thread = new Thread(() -> {
            OperationHelper.process((string) -> ModManager.themeMod.getSharer().exportMod(string, false), ModManager.themeMod.getAnalyzerEn().getCustomContentString(), ModManager.themeMod.getAnalyzerEn().getContentByAlphabet(), I18n.INSTANCE.get("commonText.theme"), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true);
        });
        ThreadHandler.startThread(thread, "runnableExport" + getType());
    }

    public File getFileGe() {
        return new File(getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt");
    }

    public File getFileEn() {
        return new File(getMGT2TextFolderPath() + "\\EN\\Themes_EN.txt");
    }
}
