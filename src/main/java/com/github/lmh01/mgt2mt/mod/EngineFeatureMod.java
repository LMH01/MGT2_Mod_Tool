package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.EngineFeatureAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.EngineFeatureEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.EngineFeatureSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.util.ArrayList;

public class EngineFeatureMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureMod.class);
    EngineFeatureAnalyzer engineFeatureAnalyzer = new EngineFeatureAnalyzer();
    EngineFeatureEditor engineFeatureEditor = new EngineFeatureEditor();
    EngineFeatureSharer engineFeatureSharer = new EngineFeatureSharer();
    public ArrayList<JMenuItem> engineFeatureModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public EngineFeatureAnalyzer getAnalyzer(){
        return engineFeatureAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public EngineFeatureEditor getEditor(){
        return engineFeatureEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public EngineFeatureSharer getSharer(){
        return engineFeatureSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return engineFeatureAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return engineFeatureEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
            return engineFeatureSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.engineFeatureMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action1");
    }

    @Override
    public String getMainTranslationKey() {
        return "engineFeature";
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.engineFeature.upperCase.plural");
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> ThreadHandler.startThread(ThreadHandler.runnableExportEngineFeatures, "runnableExportEngineFeatures"));
        return menuItem;
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.engineFeature.upperCase");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return engineFeatureModMenuItems;
    }

    @Override
    public void addModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableAddNewEngineFeature,"runnableAddNewEngineFeature");
    }

    @Override
    public void removeModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableRemoveEngineFeature, "runnableRemoveEngineFeature");
    }
}
