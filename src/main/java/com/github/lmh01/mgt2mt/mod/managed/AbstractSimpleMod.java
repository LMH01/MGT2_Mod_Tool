package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractSimpleMod extends AbstractBaseMod implements BaseMod, SimpleMod{

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
}
