package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractAdvancedMod implements AdvancedMod, BaseMod{

    @Override
    public void initializeMod() {

    }

    @Override
    public void addMod(Map<String, String> map) throws IOException {
        getEditor().addMod(map);
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
    public List<Map<String, String>> getFileContent() {
        return getAnalyzer().getFileContent();
    }

    public abstract AbstractAdvancedAnalyzer getAnalyzer();

    public abstract AbstractAdvancedEditor getEditor();

    public abstract AbstractAdvancedSharer getSharer();
}
