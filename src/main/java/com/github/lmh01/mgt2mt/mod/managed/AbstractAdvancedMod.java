package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractAdvancedMod extends AbstractBaseMod implements AdvancedMod, BaseFunctions, BaseMod{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdvancedMod.class);

    @Override
    public void initializeMod() {
        LOGGER.info("Initializing advanced mod: " + getType());
        ModManager.advancedMods.add(getAdvancedMod());
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
    public void addMod(Map<String, String> map) throws IOException {
        getBaseEditor().addMod(map);
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
    public List<Map<String, String>> getFileContent() {
        return getBaseAnalyzer().getFileContent();
    }

    /**
     * @return Returns the analyzer that is extended from {@link AbstractAdvancedAnalyzer} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractAdvancedAnalyzer} can be used.
     * If you would like to use all functions for the analyzer use {@code MOD.getAnalyzer} instead.
     */
    public abstract AbstractAdvancedAnalyzer getBaseAnalyzer();

    /**
     * @return Returns the editor that is extended from {@link AbstractAdvancedEditor} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractAdvancedEditor} can be used.
     * If you would like to use all functions for the editor use {@code MOD.getEditor} instead.
     */
    public abstract AbstractAdvancedEditor getBaseEditor();

    /**
     * @return Returns the sharer that is extended from {@link AbstractAdvancedSharer} for the specified mod.
     * Info: when using this method, only the functions from {@link AbstractAdvancedSharer} can be used.
     * If you would like to use all functions for the sharer use {@code MOD.getSharer} instead.
     */
    public abstract AbstractAdvancedSharer getBaseSharer();

    /**
     * @return Returns the advanced mod that is extended from this abstract advanced mod
     */
    public abstract AbstractAdvancedMod getAdvancedMod();
}
