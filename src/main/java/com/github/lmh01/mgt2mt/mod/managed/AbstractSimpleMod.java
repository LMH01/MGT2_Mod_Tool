package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractSimpleEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractSimpleSharer;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractSimpleMod implements BaseMod, SimpleMod{
    @Override
    public void initializeMod() {

    }

    @Override
    public void addMod(String lineToWrite) throws IOException {
        getEditor().addMod(lineToWrite);
    }

    @Override
    public void removeMod(String name) throws IOException {
        getEditor().removeMod(name);
    }

    @Override
    public void exportMod(String name, boolean exportAsRestorePoint) {
        getSharer().exportMod(name, exportAsRestorePoint);
    }

    @Override
    public void importMod(String importFolderPath, boolean showMessages) throws IOException {
        getSharer().importMod(importFolderPath, showMessages);
    }

    @Override
    public void analyze() throws IOException {
        getAnalyzer().analyzeFile();
    }

    @Override
    public Map<Integer, String> getFileContent() {
        return getAnalyzer().getFileContent();
    }

    public abstract AbstractSimpleAnalyzer getAnalyzer();

    public abstract AbstractSimpleEditor getEditor();

    public abstract AbstractSimpleSharer getSharer();
}
