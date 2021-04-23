package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.mod.*;

public class ModManager {
    public static EngineFeatureMod engineFeatureMod = new EngineFeatureMod();
    public static GameplayFeatureMod gameplayFeatureMod = new GameplayFeatureMod();
    public static GenreMod genreMod = new GenreMod();
    public static LicenceMod licenceMod = new LicenceMod();
    public static PublisherMod publisherMod = new PublisherMod();
    public static ThemeMod themeMod = new ThemeMod();

    /**
     * Initializes all mods
     * Calls this method for every mod: initialize
     */
    public static void initializeMods(){
        engineFeatureMod.initializeMod();
    }
}
