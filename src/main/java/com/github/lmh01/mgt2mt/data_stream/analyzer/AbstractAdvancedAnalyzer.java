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

/**
 * The advanced analyzer is used to analyze files that use this system:
 * [KeyX]ValueX
 * When a blank line is found a new entry is created see {@link DataStreamHelper#parseDataFile(File)}
 */
public abstract class AbstractAdvancedAnalyzer implements BaseAnalyzer{
    //

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdvancedAnalyzer.class);

    /**
     * Analyzes the file and puts its values in the map.
     */
    public List<Map<String, String>> getAnalyzedFile() throws IOException {
        List<Map<String, String>> fileContent = DataStreamHelper.parseDataFile(getFileToAnalyze());
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
        setMaxId(currentMaxId);
        LOGGER.info("Max" + getAnalyzerType() + "Id: " + currentMaxId);
        return fileContent;
    }

    /**
     * returns that analyzed file
     */
    public abstract List<Map<String, String>> getFileContent();

    /**
     * @return Returns the next free id.
     */
    public int getFreeId(){
        return getMaxId()+1;
    }

    /**
     * @return Returns a string containing all active things sorted by alphabet.
     */
    public String[] getContentByAlphabet(){
        ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
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
            ProgressBarHelper.initializeProgressBar(defaultContent.length, getFileContent().size(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.progressBar"), !disableTextAreaMessage);
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
     * @param id The id
     * @return Returns the specified content name by id.
     * @throws ArrayIndexOutOfBoundsException Is thrown when the requested content id does not exist in the map.
     */
    public String getContentNameById(int id) throws ArrayIndexOutOfBoundsException{
        return getContentNamesInUse().get(id);
    }

    /**
     * @return Returns an ArrayList containing all names that are in use.
     */
    public ArrayList<String> getContentNamesInUse(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayList.add(entry.getValue());
                }
            }
        }
        return arrayList;
    }

    /**
     * Returns -1 when name does not exist.
     * @param name The name
     * @return Returns the id for the specified name.
     */
    public int getContentIdByName(String name){
        int id = -1;
        for(Map<String, String> map : getFileContent()){
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
        List<Map<String, String>> list = getFileContent();
        Map<String, String> mapSingleContent = null;
        int contentPosition = getContentIdByName(contentNameEn);
        for(int i=0; i<list.size(); i++){
            if(i == contentPosition){
                mapSingleContent = list.get(i);
            }
        }
        return mapSingleContent;
    }

    /**
     * @param genreId The content id for which the position should be returned
     * @return Returns the position in the fileContent list where the input id is stored in.
     */
    public int getPositionInFileContentListById(int genreId){
        for(int i=0; i<getFileContent().size(); i++){
            if(getFileContent().get(i).get("ID").equals(Integer.toString(genreId))){
                return i;
            }
        }
        return -1;
    }
}
