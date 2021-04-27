package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractSimpleMod extends AbstractBaseMod implements BaseFunctions, BaseMod, SimpleMod{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleMod.class);

    @Override
    public void initializeMod() {
        LOGGER.info("Initializing simple mod: " + getType());
        ModManager.simpleMods.add(getSimpleMod());
    }

    @Override
    public void setMainMenuButtonAvailability() {
        String[] customContentString = getBaseAnalyzer().getCustomContentString(true);
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
    public void addMod(String lineToWrite) throws IOException {
        getBaseEditor().addMod(lineToWrite);
    }

    @Override
    public void removeMod(String name) throws IOException {
        getBaseEditor().removeMod(name);
    }

    @Override
    public void exportMod(String name, boolean exportAsRestorePoint) {
        getBaseSharer().exportMod(name, exportAsRestorePoint);
    }

    @Override
    public void importMod(String importFolderPath, boolean showMessages) throws IOException {
        getBaseSharer().importMod(importFolderPath, showMessages);
    }

    @Override
    public void analyze() throws IOException {
        getBaseAnalyzer().analyzeFile();
    }

    @Override
    public Map<Integer, String> getFileContent() {
        return getBaseAnalyzer().getFileContent();
    }

    /**
     * @return Returns the analyzer that is extended from {@link AbstractSimpleAnalyzer} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractSimpleAnalyzer} can be used.
     * If you would like to use all functions for the analyzer use {@code MOD.getAnalyzer} instead.
     */
    public abstract AbstractSimpleAnalyzer getBaseAnalyzer();

    /**
     * @return Returns the editor that is extended from {@link AbstractSimpleEditor} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractSimpleEditor} can be used.
     * If you would like to use all functions for the editor use {@code MOD.getEditor} instead.
     */
    public abstract AbstractSimpleEditor getBaseEditor();

    /**
     * @return Returns the sharer that is extended from {@link AbstractSimpleSharer} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractSimpleSharer} can be used.
     * If you would like to use all functions for the sharer use {@code MOD.getSharer} instead.
     */
    public abstract AbstractSimpleSharer getBaseSharer();

    /**
     * @return Returns the simple mod that is extended from this abstract simple mod
     */
    public abstract AbstractSimpleMod getSimpleMod();
}
