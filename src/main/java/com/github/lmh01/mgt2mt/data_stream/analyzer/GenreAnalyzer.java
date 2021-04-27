package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenreAnalyzer extends AbstractAdvancedAnalyzer{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreAnalyzer.class);
    List<Map<String, String>> fileContent;
    String[] defaultContent = {};
    String[] customContent = {};
    int maxId = 0;

    @Override
    public List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = getAnalyzedFile();
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public void setMaxId(int id) {
        maxId = id;
    }

    @Override
    public File getFileToAnalyze() {
        return Utils.getGenreFile();
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault("default_genres.txt");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    @Override
    public String[] getFinishedCustomContentString() {
        return customContent;
    }

    @Override
    void setFinishedCustomContentString(String[] customContent) {
        this.customContent = customContent;
    }

    public String getContentNameById(int id, boolean changeableLanguage) {
        try {
            List<Map<String, String>> list = getAnalyzedFile();
            for(int i=0; i<list.size(); i++){
                if(list.get(i).get("ID").equals(Integer.toString(id))){
                    if(changeableLanguage){
                        if(Settings.language.equals("English")){
                            return list.get(i).get("NAME EN");
                        }else if(Settings.language.equals("Deutsch")){
                            return list.get(i).get("NAME GE");
                        }
                    }else{
                        return list.get(i).get("NAME EN");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Genre not available";
    }


    /**
     * @param genreId The genre id from which the genre comb names should be transformed
     * @return Returns a list of genre names
     */
    public String getGenreNames(int genreId){
        int genrePositionInList = getPositionInFileContentListById(genreId);
        String genreNumbersRaw = getFileContent().get(genrePositionInList).get("GENRE COMB");
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString());
                if(Settings.enableDebugLogging){
                    sendLogMessage("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    sendLogMessage("currentNumber: " + currentNumber);
                }
            }
            charPosition++;
        }
        String.valueOf(genreNumbersRaw.charAt(1));
        return genreNames.toString();
    }

    /**
     * @param genreNumbersRaw The string containing the genre ids that should be transformed
     * @return Returns a list of genre names
     */
    public String getGenreNames(String genreNumbersRaw){
        StringBuilder genreNames = new StringBuilder();
        int charPosition = 0;
        StringBuilder currentNumber = new StringBuilder();
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPosition)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber.toString().replaceAll("[^0-9]", ""));
                if(Settings.enableDebugLogging){
                    LOGGER.info("genreNumber: " + genreNumber);
                }
                genreNames.append("<").append(ModManager.genreMod.getAnalyzer().getContentNameById(genreNumber)).append(">");
                currentNumber = new StringBuilder();
            }else{
                currentNumber.append(genreNumbersRaw.charAt(charPosition));
                if(Settings.enableDebugLogging){
                    LOGGER.info("currentNumber: " + currentNumber);
                }
            }
            charPosition++;
        }
        return genreNames.toString();
    }

}
