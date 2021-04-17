package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public abstract class AbstractAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAnalyzer.class);
    private static List<Map<String, String>> fileContent;
    private static int maxId;

    /**
     * Analyzes the file and puts its values in the map.
     */
    public void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.parseDataFile(getFileToAnalyze());
        int currentMaxId = 0;
        for (Map<String, String> map : fileContent) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    int currentId = Integer.parseInt(entry.getValue());
                    if (currentMaxId < currentId) {
                        currentMaxId = currentId;
                    }
                }
            }
        }
        maxId = currentMaxId;
        LOGGER.info("Max" + getAnalyzerType() + "Id: " + currentMaxId);
    }
    public abstract File getFileToAnalyze();

    /**
     * The analyzer type indicates what is written in the log, textArea, progress bar and JOptionPanes
     * Eg. Gameplay feature
     */
    public abstract String getAnalyzerType();

    /**
     * The translation key that is specific to the analyzer
     * Eg. GameplayFeature
     */
    public abstract String getMainTranslationKey();

    public List<Map<String, String>> getFileContent(){
        return fileContent;
    }

    /**
     * @return Returns the next free id.
     */
    public int getFreeId(){
        return maxId+1;
    }

    /**
     * @return Returns a string containing all active things sorted by alphabet.
     */
    public String[] getContentByAlphabet(){
        ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
        for(Map<String, String> map : fileContent){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayListAvailableThingsSorted.add(entry.getValue());
                }
            }
        }
        Collections.sort(arrayListAvailableThingsSorted);
        String[] string = new String[arrayListAvailableThingsSorted.size()];
        arrayListAvailableThingsSorted.toArray(string);
        return string;
    }

    public String[] getCustomContentString(){
        return getCustomContentString(false);
    }

    public String[] getCustomContentString(boolean disableTextAreaMessage){
        try{
            String[] contentByAlphabet = getContentByAlphabet();
            ArrayList<String> arrayListCustomContent = new ArrayList<>();
            String[] defaultContent = ReadDefaultContent.getDefault(getDefaultContentFile());
            ProgressBarHelper.initializeProgressBar(defaultContent.length, fileContent.size(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.progressBar"), !disableTextAreaMessage);
            for (String s : contentByAlphabet) {
                boolean isDefaultContent = false;
                for (String contentName : defaultContent) {
                    if (s.equals(contentName)) {
                        isDefaultContent = true;
                        break;
                    }
                }
                if (!isDefaultContent) {
                    arrayListCustomContent.add(s);
                    if(!disableTextAreaMessage){
                        TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureFound") + " " + s);
                    }
                }
                ProgressBarHelper.increment();
            }
            if(!disableTextAreaMessage){
                TextAreaHelper.appendText(I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.customEngineFeatureComplete"));
            }
            ProgressBarHelper.resetProgressBar();
            try{
                Collections.sort(arrayListCustomContent);
            }catch(NullPointerException ignored){

            }
            String[] string = new String[arrayListCustomContent.size()];
            arrayListCustomContent.toArray(string);
            return string;
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
        }
        return new String[]{};
    }

    /**
     * The String containing the default content file
     */
    public abstract String getDefaultContentFile();
    /**
     * Returns -1 when name does not exist.
     * @param name The name
     * @return Returns the id for the specified name.
     */
    public int getContentIdByName(String name){
        int id = -1;
        for(Map<String, String> map : fileContent){
            if(map.get("NAME EN").equals(name)){
                id = Integer.parseInt(map.get("ID"));
            }
        }
        if(id == -1){
            LOGGER.info(getAnalyzerType() + " [" + name + "] does not exist");
        }else{
            LOGGER.info(getAnalyzerType() + " [" + name + "] has been found. Id: " + id);
        }
        return id;
    }


    /**
     * @param contentNameEn The content for which the map should be returned.
     * @return Returns a map containing all values for the specified content.
     */
    public Map<String, String> getSingleContentMapByName(String contentNameEn){
        List<Map<String, String>> list = fileContent;
        Map<String, String> mapSingleContent = null;
        int contentPosition = getContentIdByName(contentNameEn);
        for(int i=0; i<list.size(); i++){
            if(i == contentPosition){
                mapSingleContent = list.get(i);
            }
        }
        return mapSingleContent;
    }
}
