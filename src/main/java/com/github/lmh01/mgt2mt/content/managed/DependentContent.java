package com.github.lmh01.mgt2mt.content.managed;

import java.util.Map;

public interface DependentContent {

    /**
     * Changes the values in the map to the correct export values.
     * IMPORTANT: This function does not change the values depending on the already existing values.
     * The member variables are used to change the map.
     * Gives the option to change the export map.
     * Should be used when the content requires dependencies.
     */
    void changeExportMap(Map<String, String> map) throws ModProcessingException;
}
