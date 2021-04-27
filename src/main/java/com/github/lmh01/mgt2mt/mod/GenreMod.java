package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GenreAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GenreEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GenreSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.util.ArrayList;

public class GenreMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreMod.class);
    GenreAnalyzer genreAnalyzer = new GenreAnalyzer();
    GenreEditor genreEditor = new GenreEditor();
    GenreSharer genreSharer = new GenreSharer();
    public ArrayList<JMenuItem> genreModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GenreAnalyzer getAnalyzer(){
        return genreAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GenreEditor getEditor(){
        return genreEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GenreSharer getSharer(){
        return genreSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return genreAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return genreEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return genreSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.genreMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {//TODO getCompatibleModToolVersions aus sharer raus nehmen
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action3");
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.genre.upperCase.plural");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> addModMenuItemAction());
        JMenuItem addRandomGenre = new JMenuItem(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre"));
        addRandomGenre.setToolTipText(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre.toolTip"));
        addRandomGenre.addActionListener(actionEvent -> ThreadHandler.startThread(ThreadHandler.runnableAddRandomizedGenre, "runnableAddRandomizedGenre"));
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(addRandomGenre);
        menuItems.add(removeModItem);
        return menuItems;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return genreModMenuItems;
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public JMenuItem getInitialExportMenuItem() {
        JMenuItem menuItem = new JMenuItem(getTypePlural());
        menuItem.addActionListener(e -> ThreadHandler.startThread(ThreadHandler.runnableExportGenre, "runnableExportGenre"));
        return menuItem;
    }

    @Override
    public void addModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableAddNewGenre, "runnableAddNewGenre");
    }

    @Override
    public void removeModMenuItemAction() {
        ThreadHandler.startThread(ThreadHandler.runnableRemoveGenre, "runnableRemoveGenre");
    }
}
