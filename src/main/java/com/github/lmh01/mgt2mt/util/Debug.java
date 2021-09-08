package com.github.lmh01.mgt2mt.util;
import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.ExportType;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This file contains functions that are used for debug purposes only
 */
public class Debug {//TODO Calls zu debug aus richtigem code rausnehmen (wenn bereit für release)
    private static final Logger LOGGER = LoggerFactory.getLogger(Debug.class);

    public static ArrayList<AbstractBaseMod> mods = new ArrayList<>();
    static ThemeMod themeMod = new ThemeMod();

    public static void writeHelpFile(){
        try{
            File file = new File("D://Temp//npcGames2.txt");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            BufferedReader br = new BufferedReader(new FileReader(MGT2Paths.TEXT_DATA.getPath() + "//NpcGames.txt"));
            Map<Integer, String> map = DataStreamHelper.getContentFromFile(new File(MGT2Paths.TEXT_DATA.getPath() + "//NpcGames.txt"), "UTF_16LE");

            ModManager.npcGamesMod.analyzeFile();
            assert map != null;
            for(Map.Entry<Integer, String> entry : map.entrySet()){
                try{
                    bw.write(entry.getValue());
                    bw.write("\n");
                }catch(NullPointerException ignored){

                }
            }
            /*for(Map.Entry<Integer, String> entry : ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
                bw.write(entry.getValue());
                bw.write("\n");
            }*/
            bw.close();
        }catch (IOException | ModProcessingException ignored){

        }
    }
    public static void initializeMods() {
        mods.add(themeMod);
        try {
            ModManager.genreMod.analyzeFile();
            themeMod.analyzeFile();
            /*for (String string : testSimpleMod.getContentByAlphabet()) {
                LOGGER.info("Content by Alphabet: " + string);
            }*/
            /*for (String string : testSimpleMod.getCustomContentString()) {
                LOGGER.info("Custom content: " + string);
            }*/
        } catch (ModProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void importModTest() {
        LOGGER.info("importModTest has been called");
        File file = new File("D:\\Temp\\theme.txt");
        if (file.exists()) {
            ArrayList<File> importFiles = new ArrayList<>();
            importFiles.add(file);
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(0);
            SharingManager.importAllFiles(importFiles, integers, false, "AdvancedTestMod", (string) -> {
                try {
                    return themeMod.importMod(string, true);
                } catch (ModProcessingException e) {
                    e.printStackTrace();
                    return "";
                }
            }, themeMod.getCompatibleModToolVersions(), new AtomicBoolean(true));
        }
    }

    public static void test(){
        test3();
        /*ThreadHandler.startModThread(() -> {
            SharingManager.exportSingleMod(ModManager.genreMod, "Strategy");
        }, "exportMod");*/
        //ThreadHandler.startModThread(() -> SharingManager.exportAll(ExportType.ALL_BUNDLED), "ExportBundledMods");
        //ThreadHandler.startModThread(() -> SharingManager.exportAll(ExportType.ALL_SINGLE), "ExportSingleMods");
        //ThreadHandler.startModThread(() -> SharingManager.exportAll(ExportType.RESTORE_POINT), "ExportRestorePoint");
        //tomlTest();
        /*Path path = Paths.get(System.getProperty("user.home"), ".local", "share", "mgt2_mod_tool");
        LOGGER.info("Path: " + path);
        File file = new File(System.getProperty("user.home") + ".local/share/mgt2_mod_tool");
        LOGGER.info("File.getPath(): " + file.getPath());*/

        /*try {
            Enumeration<URL> enumeration = ClassLoader.getSystemResources("");
            for (URL url : Collections.list(enumeration)) {
                LOGGER.info("url: " + url.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        try {
            LOGGER.info("Engine feature mod custom content: " + ModManager.engineFeatureMod.getCustomContentString().length);
            for (String string : ModManager.engineFeatureMod.getCustomContentString()) {
                LOGGER.info("CC: " + string);
            }
        } catch (ModProcessingException e) {
            e.printStackTrace();
        }*/

        //writeHelpFile();
        /*AbstractAdvancedMod testMod1 = new TestMod1();
        AbstractSimpleMod testMod2 = new TestMod2();
        ArrayList<AbstractBaseMod> mods = new ArrayList<>();
        mods.add(testMod1);
        mods.add(testMod2);

        for(AbstractBaseMod mod : mods){
            if(mod.getType().equals(ModType.ADVANCED_MOD.toString())){
                List<Map<String, String>> list = ((AbstractAdvancedMod) mod.getMod()).getFileContent();
                LOGGER.info("Mod is a advanced mod!");
            }else if(mod.getType().equals(ModType.SIMPLE_MOD.toString())){
                Map<Integer, String> list = ((AbstractSimpleMod) mod.getMod()).getFileContent();
                LOGGER.info("Mod is a simple mod!");
            }
        }*/
        /*try {
            AnalyzeManager.publisherAnalyzer.analyzeFile();
            Map<String, String> map = AnalyzeManager.publisherAnalyzer.getSingleContentMapByName("Ubisoft");
            LOGGER.info("Id for publiser: " + map.get("ID"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private static void tomlTest() {//This function contains tests on how the toml file formatting for the export/import rework could work
        ThreadHandler.startModThread(() -> {
            //Inserting the values into the toml map (mainMap)
            //The maps are put into each other so that the toml file is more organized
            Map<String, Object> mainMap = new HashMap<>();
            Map<String, Object> simpleMods = new HashMap<>();
            Map<String, Object> advancedMods = new HashMap<>();
            for (AbstractBaseMod mod : ModManager.mods) {
                Map<Integer, Object> modMap = new HashMap<>();
                int modNumber = 0;
                if (mod instanceof AbstractSimpleMod) {
                    for (String string : mod.getContentByAlphabet()) {
                        modMap.put(modNumber, ((AbstractSimpleMod) mod).getLine(string));
                        modNumber++;
                    }
                    simpleMods.put(mod.getType().replaceAll(" ", "_").toLowerCase(), modMap);
                }
                if (mod instanceof AbstractAdvancedMod) {
                    for (String string : mod.getContentByAlphabet()) {
                        modMap.put(modNumber, ((AbstractAdvancedMod) mod).getSingleContentMapByName(string));
                        modNumber++;
                    }
                    advancedMods.put(mod.getType().replaceAll(" ", "_").toLowerCase(), modMap);
                }
            }
            mainMap.put("simple_mods", simpleMods);
            mainMap.put("advanced_mods", advancedMods);
            mainMap.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
            try {
                //Writing the toml file
                File tomlFile = new File("D:/Temp/mods.toml");
                tomlFile.delete();
                TomlWriter tomlWriter = new TomlWriter();
                tomlWriter.write(mainMap, tomlFile);
                LOGGER.info("A new default content toml file has been created successfully!");
            } catch(IOException e) {
                LOGGER.info("A problem occurred while writing a new default content toml file: " + e.getMessage());
                e.printStackTrace();
            }
            LOGGER.info("reading toml");
            Toml toml = new Toml().read(new File("D:/Temp/mods.toml"));

            //Entries can be retrieved either this way
            Map<String, Object> map = toml.toMap();
            Map<String, Object> simpleMods2 = (Map<String, Object>) map.get("advanced_mods");
            Map<String, Object> advancedMods2 = (Map<String, Object>) map.get("simple_mods");
            for (Map.Entry<String, Object> entry : simpleMods2.entrySet()) {
                LOGGER.info("simple mod: " + entry.getKey());
            }
            for (Map.Entry<String, Object> entry : advancedMods2.entrySet()) {
                LOGGER.info("advanced mod: " + entry.getKey());
            }

            //Or this way
            LOGGER.info("price: " + toml.getString("advanced_mods.anti_cheat.1.PRICE"));
            LOGGER.info("licence: " + toml.getString("simple_mods.licence.20"));
            LOGGER.info("mod_tool_version: " + toml.getString("mod_tool_version"));
        }, "write test mods.toml file");
    }

    private static void test2() {
        BClass obj = new BClass();
        obj.aMap.put("item", 1);

        TomlWriter tomlWriter = new TomlWriter.Builder()
                .indentValuesBy(2)
                .indentTablesBy(4)
                .padArrayDelimitersBy(3)
                .build();

        String tomlString = tomlWriter.write(obj);
        LOGGER.info(tomlString);
    }

    private static void test3() {
        ThreadHandler.startModThread(() -> {
            ExportType exportType = ExportType.ALL_BUNDLED;
            ProgressBarHelper.initializeProgressBar(0, 1, I18n.INSTANCE.get("progressBar.export.creatingExportMap"));
            Path path;
            Map<String, Object> map = new HashMap<>();
            String tomlName;
            if (exportType.equals(ExportType.RESTORE_POINT)) {
                path = ModManagerPaths.CURRENT_RESTORE_POINT.getPath();
                map.put("type", exportType.getTypeName());
                tomlName = "restore_point";
            } else {
                if (Settings.enableExportStorage) {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/" + Utils.getCurrentDateTime());
                } else {
                    path = ModManagerPaths.EXPORT.getPath().resolve("bundled/");
                }
                map.put("type", exportType.getTypeName());
                tomlName = "export";
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.starting"));
            try {
                Files.createDirectories(path.resolve("assets"));
                Files.createDirectories(path);
                TomlWriter tomlWriter = new TomlWriter();
                Map<String, Object> simpleMods = new HashMap<>();
                Map<String, Object> advancedMods = new HashMap<>();
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.creatingExportMap"));
                Map<String, Object> modMap = new HashMap<>();
                AbstractAdvancedMod mod = ModManager.npcEngineMod;
                ProgressBarHelper.increaseMaxValue(mod.getContentByAlphabet().length);
                for (String string : mod.getContentByAlphabet()) {
                    Map<String, Object> singleModMap = mod.getExportMap(string);
                    for (Map.Entry<String, Object> entry : singleModMap.entrySet()) {
                        LOGGER.info(entry.getKey() + " | " + entry.getValue());
                    }
                    //This will add the image name(s) to the map if required and copy the image files
                    singleModMap.putAll(mod.exportImages(string, path.resolve("assets")));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.addingEntry") + ": " + mod.getType() + " - " + string);
                    modMap.put(string, singleModMap);
                    ProgressBarHelper.increment();
                }
                advancedMods.put(mod.getExportType(), modMap);
                map.put("simple_mods", simpleMods);
                map.put("advanced_mods", advancedMods);
                map.put("mod_tool_version", MadGamesTycoon2ModTool.VERSION);
                ProgressBarHelper.setText(I18n.INSTANCE.get("textArea.export_compact.writing_toml"));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export_compact.writing_toml") + ": " + path.resolve(tomlName + ".toml"));
                try {
                    tomlWriter.write(map, path.resolve(tomlName + ".toml").toFile());
                } catch (NullPointerException e) {
                    throw new ModProcessingException("Unable to write .toml file", e);
                }
            } catch (IOException e) {
                throw new ModProcessingException("Unable to export mods", e);
            }
        }, "test");
    }
}

class BClass {
    Map<String, Integer> aMap = new HashMap<>();
}