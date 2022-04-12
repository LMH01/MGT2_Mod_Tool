package com.github.lmh01.mgt2mt.content.managed;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

/**
 * Used to signify that a content manager requires dependencies.
 * Content managers that implement this interface should overwrite {@link BaseContentManager#constructContentFromImportMap(Map, Path)}.
 */
public interface DependentContentManager {

    /**
     * Replaces all occurrences of the missingDependency in the map with the replacement.
     * This function is used to change the map when a dependency is missing
     *
     * @param map The map where the values are replaced
     * @param missingDependency The dependency that should be replaced
     * @param replacement The replacement
     */
    void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException;

    /**
     * @return An array list that contains all the dependencies of the mod
     */
    ArrayList<BaseContentManager> getDependencies();

    /**
     * Analyses all dependencies of this mod
     */
    default void analyzeDependencies() throws ModProcessingException  {
        for (BaseContentManager mod : getDependencies()) {
            mod.analyzeFile();
        }
    }
}
