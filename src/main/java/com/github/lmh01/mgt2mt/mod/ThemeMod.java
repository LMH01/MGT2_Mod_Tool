package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileEnAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileGeAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.ThemeEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.ThemeSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;
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
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action6");
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
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> ThreadHandler.startThread(ThreadHandler.runnableExportThemes, "runnableExportThemes"));
        return menuItem;
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
    public void addModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableAddNewTheme, "runnableAddNewTheme");
    }

    @Override
    public void removeModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableRemoveTheme, "runnableRemoveTheme");
    }

    @Override
    public File getFile() {
        return getFileGe();
    }

    public File getFileGe() {
        return new File(getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt");
    }

    public File getFileEn() {
        return new File(getMGT2TextFolderPath() + "\\EN\\Themes_EN.txt");
    }
}
