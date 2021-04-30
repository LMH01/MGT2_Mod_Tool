package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlatformEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformEditor.class);

    @Override
    void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        TranslationManager.printLanguages(bw, map);
        for(String string : TranslationManager.TRANSLATION_KEYS){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("MANUFACTURER " + string)){
                    bw.write("[MANUFACTURER " + string + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
            }
        }
        EditHelper.printLine("DATE",map, bw);
        if(map.containsKey("DATE END")){
            EditHelper.printLine("DATE END",map, bw);
        }
        EditHelper.printLine("PRICE",map, bw);
        EditHelper.printLine("DEV COSTS",map, bw);
        EditHelper.printLine("TECHLEVEL",map, bw);
        EditHelper.printLine("UNITS",map, bw);
        Map<Integer, String> pictures = new HashMap<>();
        ArrayList<String> pictureChangeYears = new ArrayList<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getKey().contains("PIC") && !entry.getKey().contains("YEAR")){
                pictures.put(Integer.parseInt(entry.getKey().replaceAll("[^0-9]", "")), entry.getValue());
            }
            if(entry.getKey().contains("YEAR")){
                pictureChangeYears.add("[" + entry.getKey() + "]" + entry.getValue());
            }
        }
        if(map.containsKey("PIC-1")){
            for(Map.Entry<Integer, String> entry : pictures.entrySet()){
                bw.write("[PIC-" + entry.getKey() + "]" + entry.getValue());
                bw.write(System.getProperty("line.separator"));
            }
        }else{
            for(int i=1; i<=pictureChangeYears.size()+1; i++){
                bw.write("[PIC-" + i + "]" + map.get("NAME EN").replaceAll("[0-9]", "").replaceAll("\\s+","") + "-" + i + ".png");
                bw.write(System.getProperty("line.separator"));
            }
        }
        Collections.sort(pictureChangeYears);
        for(int i=0; i<pictureChangeYears.size(); i++){
            bw.write(pictureChangeYears.get(i));
            bw.write(System.getProperty("line.separator"));
        }
        ArrayList<Integer> gameplayFeatureIds = new ArrayList<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getKey().contains("NEED")){
                gameplayFeatureIds.add(Integer.parseInt(entry.getValue()));
            }
        }
        int numberOfRunsB = 1;
        for(Integer integer : gameplayFeatureIds){
            bw.write("[NEED-" + numberOfRunsB + "]" + integer);
            bw.write(System.getProperty("line.separator"));
            numberOfRunsB++;
        }
        EditHelper.printLine("COMPLEX",map, bw);
        EditHelper.printLine("INTERNET",map, bw);
        EditHelper.printLine("TYP",map, bw);
    }

    /**
     * This call only edits the platform.txt file. If the image icons should be processed as well use {@link PlatformEditor#addMod(Map, Map)}.
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     * @throws IOException
     */
    @Override
    public void addMod(Map<String, String> map) throws IOException {
        super.addMod(map);
    }

    /**
     * Adds a new mod to the file
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     * @param imageFiles This array list contains the image files
     */
    public void addMod(Map<String, String> map, Map<Integer, File> imageFiles) throws IOException {
        addImageFiles(map.get("NAME EN"), imageFiles);
        super.addMod(map);
    }

    /**
     * Removes the mod from the game. Does not include pictures.
     * Use {@link PlatformEditor#removePlatform(String)} to also remove pictures.
     * @param name The mod name that should be removed
     */
    @Override
    public boolean removeMod(String name) throws IOException {
        return super.removeMod(name);
    }

    /**
     * Removes the platform from the game including pictures.
     */
    public boolean removePlatform(String name) throws IOException {
        ArrayList<File> filesToRemove = DataStreamHelper.getFilesInFolderWhiteList(Settings.mgt2FilePath + "//Mad Games Tycoon 2_Data//Extern//Icons_Platforms//", name.replaceAll("[0-9]", "").replaceAll("\\s+","") );
        LOGGER.info("fileToRemove size: " + filesToRemove.size());
        for(File file : filesToRemove){
            LOGGER.info("deleting file: " + file.getPath());
            file.delete();
        }
        return super.removeMod(name);
    }

    /**
     * Adds the image files for the new platform
     * @param platformName The name of the new platform
     * @param imageFiles The map contains the image files in the following formatting:
     *            Integer - The position of the image file
     *            File - The source file that should be added
     */
    public void addImageFiles(String platformName, Map<Integer, File> imageFiles) throws IOException {
        for(Map.Entry<Integer, File> entry : imageFiles.entrySet()){
            File destinationFile = new File(Settings.mgt2FilePath + "//Mad Games Tycoon 2_Data//Extern//Icons_Platforms//" + platformName.replaceAll("[0-9]", "").replaceAll("\\s+","") + "-" + entry.getKey() + ".png");
            Files.copy(Paths.get(entry.getValue().getPath()), Paths.get(destinationFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return ModManager.platformMod.getType();
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.platformMod.getBaseAnalyzer();
    }

    @Override
    public File getFileToEdit() {
        return ModManager.platformMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
