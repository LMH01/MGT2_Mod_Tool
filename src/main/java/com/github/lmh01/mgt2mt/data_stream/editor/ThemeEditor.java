package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.ChangeLog;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ThemeEditor extends AbstractSimpleEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeEditor.class);

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return null;
    }

    @Override
    public String getReplacedLine(String inputString) {
        return null;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.theme");
    }

    @Override
    public File getFileToEdit() {
        return null;
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    /**
     * Do not use this method!
     * Use {@link ThemeEditor#addMod(Map, ArrayList, int)} instead!
     * @param lineToWrite The line that should be written
     */
    @Override
    public void addMod(String lineToWrite) throws IOException {
        addMod(null, null, 1);
    }

    /**
     * Adds a new theme to the theme files
     * @param map The map containing the theme translations
     * @param arrayListCompatibleGenres The array list containing the compatible genres
     */
    public void addMod(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, int violenceLevel) throws IOException {
        editThemeFiles(map, arrayListCompatibleGenres, true, 0, violenceLevel);
    }

    @Override
    public boolean removeMod(String name) throws IOException {
        boolean returnValue = editThemeFiles(null, null, false, ThemeFileAnalyzer.getPositionOfThemeInFile(name), 0);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + name);
        return returnValue;
    }

    /**
     * Adds/removes a theme to the theme files
     * @param map The map containing the translations
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     * @param addTheme True when the theme should be added. False when the theme should be removed.
     * @param removeThemePosition The position where the theme is positioned that should be removed.
     * @param violenceLevel This is the number that will be added to the theme entry in the german file. This declares how much the age rating should be influenced when a game is made with this topic
     */
    public boolean editThemeFiles(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, boolean addTheme, int removeThemePosition, int violenceLevel) throws IOException {
        for(String string : TranslationManager.TRANSLATION_KEYS){
            File themeFile = Utils.getThemeFile(string);
            Map<Integer, String> currentThemeFileContent;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_8BOM");
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_16LE");
            }else{
                break;
            }
            if(themeFile.exists()){
                themeFile.delete();
            }
            themeFile.createNewFile();
            BufferedWriter bw;
            if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                bw.write("\ufeff");//Makes the file UTF8 BOM
            }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
            }else{
                break;
            }
            int currentLine = 1;
            boolean firstLine = true;
            for(int i = 0; i< Objects.requireNonNull(currentThemeFileContent).size(); i++){
                if(!firstLine){
                    if(!addTheme){
                        if(currentLine != removeThemePosition) {
                            bw.write(System.getProperty("line.separator"));
                        }
                    }else{
                        bw.write(System.getProperty("line.separator"));
                    }
                }else{
                    firstLine = false;
                }
                if(addTheme || currentLine != removeThemePosition) {
                    bw.write(currentThemeFileContent.get(currentLine));
                }
                currentLine++;
            }
            try{
                if(addTheme){
                    bw.write(System.getProperty("line.separator"));
                    if(string.equals("GE")){
                        StringBuilder genreIdsToPrint = new StringBuilder();
                        genreIdsToPrint.append(" ");
                        bw.write(map.get("NAME GE"));
                        for(Integer genreId : arrayListCompatibleGenres){
                            genreIdsToPrint.append("<").append(genreId).append(">");
                        }
                        if(violenceLevel != 0){
                            genreIdsToPrint.append("<").append("M").append(violenceLevel).append(">");
                        }
                        bw.write(genreIdsToPrint.toString());
                    }else{
                        if(Settings.enableDebugLogging){
                            LOGGER.info("current string: " + string);
                        }
                        bw.write(map.get("NAME " + string));
                    }
                }
            }catch(NullPointerException ignored){

            }
            bw.close();
        }
        return true;
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws IOException {
        ThemeFileAnalyzer.analyzeThemeFiles();
        File fileTopicsGe = Utils.getThemesGeFile();
        if(fileTopicsGe.exists()){
            fileTopicsGe.delete();
        }
        fileTopicsGe.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTopicsGe), StandardCharsets.UTF_16LE));
        Map<Integer, String> map = AnalyzeManager.themeFileGeAnalyzer.getFileContent();
        boolean firstLine = true;
        for(Integer i : map.keySet()){
            if(addGenreID){
                if(compatibleThemeIds.contains(i)){
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(i + " - Y: " + map.get(i));
                    }
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }
                    bw.write(map.get(i) + "<" + genreID + ">");
                }else{
                    if (!firstLine) {
                        bw.write(System.getProperty("line.separator"));
                    }
                    if (Settings.enableDebugLogging) {
                        LOGGER.info(i + " - N: " + map.get(i));
                    }
                    bw.write(map.get(i));
                }
            }else{
                if (!firstLine) {
                    bw.write(System.getProperty("line.separator"));
                }
                bw.write(map.get(i).replace("<" + genreID + ">", ""));
            }
            firstLine = false;
        }
        bw.close();
        if (addGenreID) {
            ChangeLog.addLogEntry(2, Integer.toString(genreID));
        } else {
            ChangeLog.addLogEntry(3, Integer.toString(genreID));
        }
    }
}
