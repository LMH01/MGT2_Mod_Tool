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
    public static EngineFeatureMod engineFeatureMod = new EngineFeatureMod();
    public static GameplayFeatureMod gameplayFeatureMod = new GameplayFeatureMod();
    public static GenreMod genreMod = new GenreMod();
    public static LicenceMod licenceMod = new LicenceMod();
    public static PublisherMod publisherMod = new PublisherMod();
    public static ThemeMod themeMod = new ThemeMod();
    public static NpcGamesMod npcGamesMod = new NpcGamesMod();
    public static PlatformMod platformMod = new PlatformMod();
    public static NpcEngineMod npcEngineMod = new NpcEngineMod();
    public static AntiCheatMod antiCheatMod = new AntiCheatMod();
    public static CopyProtectMod copyProtectMod = new CopyProtectMod();
    public static HardwareMod hardwareMod = new HardwareMod();
    public static HardwareFeatureMod hardwareFeatureMod = new HardwareFeatureMod();

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
        antiCheatMod.initializeMod();
        copyProtectMod.initializeMod();
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
            antiCheatMod.getBaseAnalyzer().analyzeFile();
            copyProtectMod.getBaseAnalyzer().analyzeFile();
            hardwareMod.getBaseAnalyzer().analyzeFile();
            hardwareFeatureMod.getBaseAnalyzer().analyzeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
