package com.github.lmh01.mgt2mt.mod.managed;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AdvancedMod {
    /**
     * Uses the analyzer to return the file content
     */
    List<Map<String, String>> getFileContent();

    /**
     * Uses the editor to add a new mod
     */
    void addMod(Map<String, String> map) throws IOException;
}
