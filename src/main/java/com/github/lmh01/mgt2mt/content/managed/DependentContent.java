package com.github.lmh01.mgt2mt.content.managed;

import java.util.Map;

public interface DependentContent {

    /**
     * Returns a map that contains the dependencies of the mod. This map is printed into the export file.
     * Map coding: key = modName | value = hash set of the required mods that belong to the modName (names not ids).
     *
     * @return A map that contains the dependencies for the mod that should be exported
     * @throws ModProcessingException When something went wrong while creating the dependency map
     */
    Map<String, Object> getDependencyMap() throws ModProcessingException;

    /**
     * Changes the values in the map to the correct export values.
     * IMPORTANT: This function does not change the values depending on the already existing values.
     * The member variables are used to change the map.
     * Gives the option to change the export map.
     * Should be used when the content requires dependencies.
     */
    void changeExportMap(Map<String, String> map) throws ModProcessingException;
}
