package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractSimpleSharer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.OperationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractSimpleMod extends AbstractBaseMod implements BaseFunctions, BaseMod, SimpleMod{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleMod.class);

    @Override
    public final void initializeMod() {
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
    public final void addMod(String lineToWrite) throws IOException {
        getBaseEditor().addMod(lineToWrite);
    }

    @Override
    public final void removeMod(String name) throws IOException {
        getBaseEditor().removeMod(name);
    }

    @Override
    public final void exportMod(String name, boolean exportAsRestorePoint) {
        getBaseSharer().exportMod(name, exportAsRestorePoint);
    }

    @Override
    public final void importMod(String importFolderPath, boolean showMessages) throws IOException {
        getBaseSharer().importMod(importFolderPath, showMessages);
    }

    @Override
    public final void analyze() throws IOException {
        getBaseAnalyzer().analyzeFile();
    }

    @Override
    public final Map<Integer, String> getFileContent() {
        return getBaseAnalyzer().getFileContent();
    }

    @Override
    public final void removeModMenuItemAction() {
        Thread thread = new Thread(() -> OperationHelper.process(getBaseEditor()::removeMod, ModManager.themeMod.getAnalyzerEn().getCustomContentString(), ModManager.themeMod.getAnalyzerEn().getCustomContentString(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.removed"), I18n.INSTANCE.get("commonText.remove"), I18n.INSTANCE.get("commonText.removing"), false));
        ThreadHandler.startThread(thread, "runnableRemove" + getType());
    }

    @Override
    public final void exportMenuItemAction() {
        Thread thread = new Thread(() -> OperationHelper.process((string) -> getBaseSharer().exportMod(string, false), getBaseAnalyzer().getCustomContentString(), getBaseAnalyzer().getContentByAlphabet(), I18n.INSTANCE.get("commonText." + getMainTranslationKey()), I18n.INSTANCE.get("commonText.exported"), I18n.INSTANCE.get("commonText.export"), I18n.INSTANCE.get("commonText.exporting"), true));
        ThreadHandler.startThread(thread, "runnableExport" + getType());
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
