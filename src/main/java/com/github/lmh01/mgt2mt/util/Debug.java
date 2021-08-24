package com.github.lmh01.mgt2mt.util;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.*;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
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
            BufferedReader br = new BufferedReader(new FileReader(Utils.getMGT2DataPath() + "//NpcGames.txt"));
            Map<Integer, String> map = DataStreamHelper.getContentFromFile(new File(Utils.getMGT2DataPath() + "//NpcGames.txt"), "UTF_16LE");

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
        try {
            LOGGER.info("Engine feature mod custom content: " + ModManager.engineFeatureMod.getCustomContentString().length);
            for (String string : ModManager.engineFeatureMod.getCustomContentString()) {
                LOGGER.info("CC: " + string);
            }
        } catch (ModProcessingException e) {
            e.printStackTrace();
        }

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
}