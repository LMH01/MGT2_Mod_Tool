package com.github.lmh01.mgt2mt.mod.managed;

/**
 * The different types a mod can be constructed
 */
public enum ModConstructionType {

    /**
     * Symbolizes that the mod should be constructed from the import line.
     */
    IMPORT,

    /**
     * Symbolizes that the mod should be constructed from the game files.
     */
    GAME_FILES,
}
