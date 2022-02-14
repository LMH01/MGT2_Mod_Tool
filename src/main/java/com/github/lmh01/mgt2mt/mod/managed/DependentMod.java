package com.github.lmh01.mgt2mt.mod.managed;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class contains methods that help to create a mod that requires dependencies.
 */
public interface DependentMod {

    /**
     * Replaces all occurrences of the missingDependency in the map with the replacement.
     * This function is used to change the map when a dependency is missing
     *
     * @param map
     * @param missingDependency
     * @param replacement
     * @throws ModProcessingException
     */
    void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException;

    /**
     * @return An array list that contains all the dependencies of the mod
     */
    ArrayList<AbstractBaseMod> getDependencies();

    /**
     * Analyses all dependencies of this mod
     *
     * @throws ModProcessingException If analysis of a mod fails
     */
    default void analyzeDependencies() throws ModProcessingException {
        for (AbstractBaseMod mod : getDependencies()) {
            mod.analyzeFile();
        }
    }

    /**
     * Returns a map that contains the dependencies of the mod. This map is printed into the export file.
     * This function should be overwritten by each mod that needs dependencies.
     * Map coding: key = modName | value = hash set of the required mods that belong to the modName
     *
     * @param t   This map/string contains the values that will be used to create the dependency map
     * @param <T> Should be either {@literal Map<String, String>} or {@literal String}
     * @return A map that contains the dependencies for the mod that should be exported
     * @throws ModProcessingException When something went wrong while creating the dependency map or when {@literal <T>} is not valid.
     */
    <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException;
}
