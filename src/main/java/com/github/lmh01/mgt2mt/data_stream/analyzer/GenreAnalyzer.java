package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class GenreAnalyzer extends AbstractAdvancedAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreAnalyzer.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public File getFileToAnalyze() {
        return ModManager.genreMod.getFile();
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_genres.txt";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
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
