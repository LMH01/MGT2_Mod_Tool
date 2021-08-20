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
    public static EngineFeatureModOld engineFeatureModOld = new EngineFeatureModOld();
    public static GameplayFeatureModOld gameplayFeatureModOld = new GameplayFeatureModOld();
    public static GenreModOld genreModOld = new GenreModOld();
    public static LicenceModOld licenceModOld = new LicenceModOld();
    public static PublisherModOld publisherModOld = new PublisherModOld();
    public static ThemeModOld themeModOld = new ThemeModOld();
    public static NpcGamesModOld npcGamesModOld = new NpcGamesModOld();
    public static PlatformModOld platformModOld = new PlatformModOld();
    public static NpcEngineModOld npcEngineModOld = new NpcEngineModOld();
    public static AntiCheatModOld antiCheatModOld = new AntiCheatModOld();
    public static CopyProtectModOld copyProtectModOld = new CopyProtectModOld();
    public static HardwareModOld hardwareModOld = new HardwareModOld();
    public static HardwareFeatureModOld hardwareFeatureModOld = new HardwareFeatureModOld();

    public static AntiCheatMod antiCheatMod = new AntiCheatMod();
    public static CopyProtectMod copyProtectMod = new CopyProtectMod();
    public static EngineFeatureMod engineFeatureMod = new EngineFeatureMod();
    public static GameplayFeatureMod gameplayFeatureMod = new GameplayFeatureMod();
    public static GenreMod genreMod = new GenreMod();
    public static HardwareFeatureMod hardwareFeatureMod = new HardwareFeatureMod();
    public static HardwareMod hardwareMod = new HardwareMod();
    public static LicenceMod licenceMod = new LicenceMod();
    public static NpcEngineMod npcEngineMod = new NpcEngineMod();
    public static NpcGamesMod npcGamesMod = new NpcGamesMod();
    public static PlatformMod platformMod = new PlatformMod();
    public static PublisherMod publisherMod = new PublisherMod();
    public static ThemeMod themeMod = new ThemeMod();

    /**
     * Initializes all mods
     * Calls this method for every mod: initialize
     */
    public static void initializeMods(){
        engineFeatureModOld.initializeMod();
        gameplayFeatureModOld.initializeMod();
        genreModOld.initializeMod();
        licenceModOld.initializeMod();
        publisherModOld.initializeMod();
        themeModOld.initializeMod();
        npcGamesModOld.initializeMod();
        platformModOld.initializeMod();
        npcEngineModOld.initializeMod();
        antiCheatModOld.initializeMod();
        copyProtectModOld.initializeMod();
        hardwareModOld.initializeMod();
        hardwareFeatureModOld.initializeMod();
    }

    /**
     * Analyzes all mods and calls MOD.getAnalyzer().analyze();
     */
    public static void analyzeMods(){
        try {
            gameplayFeatureModOld.getAnalyzer().analyzeFile();
            engineFeatureModOld.getAnalyzer().analyzeFile();
            genreModOld.getAnalyzer().analyzeFile();
            themeModOld.getAnalyzerEn().analyzeFile();
            themeModOld.getAnalyzerGe().analyzeFile();
            publisherModOld.getAnalyzer().analyzeFile();
            licenceModOld.getAnalyzer().analyzeFile();
            npcGamesModOld.getBaseAnalyzer().analyzeFile();
            platformModOld.getBaseAnalyzer().analyzeFile();
            npcEngineModOld.getBaseAnalyzer().analyzeFile();
            antiCheatModOld.getBaseAnalyzer().analyzeFile();
            copyProtectModOld.getBaseAnalyzer().analyzeFile();
            hardwareModOld.getBaseAnalyzer().analyzeFile();
            hardwareFeatureModOld.getBaseAnalyzer().analyzeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
