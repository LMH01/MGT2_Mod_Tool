package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GameplayFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GameplayFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GameplayFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class GameplayFeatureMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureMod.class);
    GameplayFeatureAnalyzer gameplayFeatureAnalyzer = new GameplayFeatureAnalyzer();
    GameplayFeatureEditor gameplayFeatureEditor = new GameplayFeatureEditor();
    GameplayFeatureSharer gameplayFeatureSharer = new GameplayFeatureSharer();
    public ArrayList<JMenuItem> gameplayFeatureModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GameplayFeatureAnalyzer getAnalyzer(){
        return gameplayFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GameplayFeatureEditor getEditor(){
        return gameplayFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GameplayFeatureSharer getSharer(){
        return gameplayFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return gameplayFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return gameplayFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return gameplayFeatureSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.gameplayFeatureMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action2");
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return gameplayFeatureModMenuItems;
    }

    @Override
    public void addModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableAddNewGameplayFeature, "runnableAddNewGameplayFeature");
    }

    @Override
    public void removeModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableRemoveGameplayFeature, "runnableRemoveGameplayFeature");
    }

    @Override
    public JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> ThreadHandler.startThread(ThreadHandler.runnableExportGameplayFeatures, "runnableExportGameplayFeatures"));
        return menuItem;
    }

    @Override
    public String getFileName() {
        return "gameplayFeature.txt";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.gameplayFeature.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//GameplayFeatures.txt");
    }
}
