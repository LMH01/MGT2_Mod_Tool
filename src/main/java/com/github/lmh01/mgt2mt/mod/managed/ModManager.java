package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.mod.*;
import com.github.lmh01.mgt2mt.util.I18n;
import javax.swing.*;
import java.util.ArrayList;

public class ModManager{
    public static ArrayList<AbstractBaseMod> mods = new ArrayList<>();
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
        antiCheatMod.initializeMod();
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
        AbstractBaseMod.startModThread(() -> {
            gameplayFeatureMod.analyzeFile();
            engineFeatureMod.analyzeFile();
            genreMod.analyzeFile();
            themeMod.analyzeFile();
            publisherMod.analyzeFile();
            licenceMod.analyzeFile();
            npcGamesMod.analyzeFile();
            platformMod.analyzeFile();
            npcEngineMod.analyzeFile();
            antiCheatMod.analyzeFile();
            copyProtectMod.analyzeFile();
            hardwareMod.analyzeFile();
            hardwareFeatureMod.analyzeFile();//TODO Hier mal testen, was passiert, wenn eine Exception auftritt
        }, "AnalyzeAllMods");
    }

    /**
     * Shows the following message to the user:
     * @param e The ModProcessingException for wich the message should be displayed
     */
    public static void showException(ModProcessingException e) {
        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("frame.title.error") + ": " + e.getMessage().replace(" - ", "\n - "), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
    }
}
