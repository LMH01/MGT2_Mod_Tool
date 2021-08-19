package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.mod.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;

public class ModManager{
    private static final Logger LOGGER = LoggerFactory.getLogger(ModManager.class);
    public static ArrayList<AbstractSimpleModOld> simpleMods = new ArrayList<>();
    public static ArrayList<AbstractAdvancedModOld> advancedMods = new ArrayList<>();
    public static ArrayList<AbstractBaseMod> mods = new ArrayList<>();
    public static EngineFeatureModOld engineFeatureMod = new EngineFeatureModOld();
    public static GameplayFeatureModOld gameplayFeatureMod = new GameplayFeatureModOld();
    public static GenreModOld genreMod = new GenreModOld();
    public static LicenceModOld licenceMod = new LicenceModOld();
    public static PublisherMod publisherMod = new PublisherMod();
    public static ThemeMod themeMod = new ThemeMod();
    public static NpcGamesModOld npcGamesMod = new NpcGamesModOld();
    public static PlatformMod platformMod = new PlatformMod();
    public static NpcEngineModOld npcEngineMod = new NpcEngineModOld();
    public static AntiCheatModOld antiCheatModOld = new AntiCheatModOld();
    public static CopyProtectModOld copyProtectModOld = new CopyProtectModOld();
    public static HardwareModOld hardwareMod = new HardwareModOld();
    public static HardwareFeatureModOld hardwareFeatureMod = new HardwareFeatureModOld();

    /**
     * Initializes all mods
     * Calls this method for every mod: initialize
     */
    public static void initializeMods(){
        engineFeatureMod.initializeMod();
        gameplayFeatureMod.initializeMod();
        genreMod.initializeMod();
        licenceMod.initializeMod();
        publisherMod.initializeMod();
        themeMod.initializeMod();
        npcGamesMod.initializeMod();
        platformMod.initializeMod();
        npcEngineMod.initializeMod();
        antiCheatModOld.initializeMod();
        copyProtectModOld.initializeMod();
        hardwareMod.initializeMod();
        hardwareFeatureMod.initializeMod();
    }

    /**
     * Analyzes all mods and calls MOD.getAnalyzer().analyze();
     */
    public static void analyzeMods(){
        try {
            gameplayFeatureMod.getAnalyzer().analyzeFile();
            engineFeatureMod.getAnalyzer().analyzeFile();
            genreMod.getAnalyzer().analyzeFile();
            themeMod.getAnalyzerEn().analyzeFile();
            themeMod.getAnalyzerGe().analyzeFile();
            publisherMod.getAnalyzer().analyzeFile();
            licenceMod.getAnalyzer().analyzeFile();
            npcGamesMod.getBaseAnalyzer().analyzeFile();
            platformMod.getBaseAnalyzer().analyzeFile();
            npcEngineMod.getBaseAnalyzer().analyzeFile();
            antiCheatModOld.getBaseAnalyzer().analyzeFile();
            copyProtectModOld.getBaseAnalyzer().analyzeFile();
            hardwareMod.getBaseAnalyzer().analyzeFile();
            hardwareFeatureMod.getBaseAnalyzer().analyzeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
