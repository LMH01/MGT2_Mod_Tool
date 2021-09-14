package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.mod.*;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;

public class ModManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModManager.class);
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
    public static void initializeMods() {
        genreMod.initializeMod();
        themeMod.initializeMod();
        publisherMod.initializeMod();
        engineFeatureMod.initializeMod();
        gameplayFeatureMod.initializeMod();
        licenceMod.initializeMod();
        npcGamesMod.initializeMod();
        npcEngineMod.initializeMod();
        platformMod.initializeMod();
        antiCheatMod.initializeMod();
        copyProtectMod.initializeMod();
        hardwareMod.initializeMod();
        hardwareFeatureMod.initializeMod();
        DebugHelper.debug(LOGGER, "Total mods active: " + mods.size());
    }

    /**
     * Analyzes all mods and calls MOD.getAnalyzer().analyze();
     */
    public static void analyzeMods() throws ModProcessingException {
        antiCheatMod.analyzeFile();
        copyProtectMod.analyzeFile();
        engineFeatureMod.analyzeFile();
        gameplayFeatureMod.analyzeFile();
        genreMod.analyzeFile();
        hardwareFeatureMod.analyzeFile();
        hardwareMod.analyzeFile();
        licenceMod.analyzeFile();
        npcEngineMod.analyzeFile();
        npcGamesMod.analyzeFile();
        publisherMod.analyzeFile();
        platformMod.analyzeFile();
        themeMod.analyzeFile();
    }

    /**
     * Shows the following message to the user:
     *
     * @param e The ModProcessingException for wich the message should be displayed
     */
    public static void showException(ModProcessingException e) {
        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("frame.title.error") + ": " + e.getMessage().replace(" - ", "\n - "), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
    }
}
