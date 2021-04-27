package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.LicenceAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.LicenceEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.LicenceSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;

public class LicenceMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceMod.class);
    LicenceAnalyzer licenceAnalyzer = new LicenceAnalyzer();
    LicenceEditor licenceEditor = new LicenceEditor();
    LicenceSharer licenceSharer = new LicenceSharer();
    public ArrayList<JMenuItem> licenceModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public LicenceAnalyzer getAnalyzer(){
        return licenceAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public LicenceEditor getEditor(){
        return licenceEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public LicenceSharer getSharer(){
        return licenceSharer;
    }

    @Override
    public AbstractSimpleAnalyzer getBaseAnalyzer() {
        return licenceAnalyzer;
    }

    @Override
    public AbstractSimpleEditor getBaseEditor() {
        return licenceEditor;
    }

    @Override
    public AbstractSimpleSharer getBaseSharer() {
        return licenceSharer;
    }

    @Override
    public AbstractSimpleMod getSimpleMod() {
        return ModManager.licenceMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action4");
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.licence.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.licence.upperCase.plural");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return licenceModMenuItems;
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> ThreadHandler.startThread(ThreadHandler.runnableExportLicence, "runnableExportLicence"));
        return menuItem;
    }

    @Override
    public void addModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableAddNewLicence, "runnableAddNewLicence");
    }

    @Override
    public void removeModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableRemoveLicence, "runnableRemoveLicence");
    }
}
