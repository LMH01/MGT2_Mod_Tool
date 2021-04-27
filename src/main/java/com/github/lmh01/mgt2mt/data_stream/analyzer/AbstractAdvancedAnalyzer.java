package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The advanced analyzer is used to analyze files that use this system:
 * [KeyX]ValueX
 * When a blank line is found a new entry is created see {@link DataStreamHelper#parseDataFile(File)}
 */
public abstract class AbstractAdvancedAnalyzer implements BaseAnalyzer, BaseFunctions {
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
        LOGGER.info("Max" + getType() + "Id: " + currentMaxId);
        return fileContent;
    }

    /**
     * The file that should be analyzed
     */
    public abstract File getFileToAnalyze();

    /**
     * returns that analyzed file
     */
    public abstract List<Map<String, String>> getFileContent();

    /**
     * Returns the currently highest id
     */
    public abstract int getMaxId();

    /**
     * Sets the maximum id
     */
    public abstract void setMaxId(int id);

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
        return getCustomContentString(true);
    }

    /**
     * @param disableTextAreaMessage True when the messages should not be written to the text area.
     * @return Returns a array that contains all custom contents
     */
    public String[] getCustomContentString(boolean disableTextAreaMessage){
        String[] contentByAlphabet = getContentByAlphabet();
        ArrayList<String> arrayListCustomContent = new ArrayList<>();
        ProgressBarHelper.initializeProgressBar(getDefaultContent().length, getFileContent().size(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.progressBar"), !disableTextAreaMessage);
        for (String s : contentByAlphabet) {
            boolean isDefaultContent = false;
            for (String contentName : getDefaultContent()) {
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
        setFinishedCustomContentString(string);
        return string;
    }

    /**
     * @return Returns the custom content string that has been computed previously
     */
    public abstract String[] getFinishedCustomContentString();

    /**
     * Sets the custom content string that should be returned when {@link AbstractAdvancedAnalyzer#getFinishedCustomContentString()} is called.
     * @param customContent
     */
    abstract void setFinishedCustomContentString(String[] customContent);
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
            LOGGER.info(getType() + " [" + name + "] does not exist");
        }else{
            LOGGER.info(getType() + " [" + name + "] has been found. Id: " + id);
        }
        return id;
    }


    /**
     * @param contentNameEn The content for which the map should be returned.
     * @return Returns a map containing all values for the specified content.
     */
    public Map<String, String> getSingleContentMapByName(String contentNameEn){
        List<Map<String, String>> list = getFileContent();
        String idToSearch = Integer.toString(getContentIdByName(contentNameEn));
        Map<String, String> mapSingleContent = null;
        for(Map<String, String> map : list){
            if(map.get("ID").equals(idToSearch)){
                mapSingleContent = map;
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

    /**
     * @return Returns a array list containing all active ids
     */
    public ArrayList<Integer> getActiveIds(){
        ArrayList<Integer> activeIds = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
            try{
                activeIds.add(Integer.parseInt(map.get("ID")));
            }catch(NumberFormatException ignored){

            }
        }
        return activeIds;
    }
}
