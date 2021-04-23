package com.github.lmh01.mgt2mt.mod.managed;

import java.io.IOException;
import java.util.Map;

public interface SimpleMod {

    void addMod(String lineToWrite) throws IOException;

    /**
     * returns that analyzed file
     */
    Map<Integer, String> getFileContent();
}
