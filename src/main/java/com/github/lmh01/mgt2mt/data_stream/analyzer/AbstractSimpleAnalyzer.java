package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.BaseFunctions;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * The simple analyzer is used to analyze files that use this system:
 * ValueX [Some data]
 * When a blank line is found a new entry is created see {@link DataStreamHelper#parseDataFile(File)}
 */
public abstract class AbstractSimpleAnalyzer implements BaseAnalyzer, BaseFunctions {
    /**
     * returns that analyzed file
     */
    public abstract Map<Integer, String> getFileContent();

    /**
     * @return Returns a string containing all active things sorted by alphabet.
     */
    public String[] getContentByAlphabet(){
        ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            arrayListAvailableThingsSorted.add(getReplacedLine(entry.getValue()));
        }
        Collections.sort(arrayListAvailableThingsSorted);
        String[] string = new String[arrayListAvailableThingsSorted.size()];
        arrayListAvailableThingsSorted.toArray(string);
        return string;
    }

    public String[] getCustomContentString(){
        return getCustomContentString(false);
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
     * Sets the custom content string that should be returned when {@link AbstractSimpleAnalyzer#getFinishedCustomContentString()} is called.
     * @param customContent
     */
    abstract void setFinishedCustomContentString(String[] customContent);

    /**
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);

    /**
     * @param name The content name for which the position should be returned
     * @return Returns the position in the fileContent list where the input id is stored in.
     */
    public int getPositionInFileContentListByName(String name){
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            if(getReplacedLine(entry.getValue()).equals(name)){
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * @return Returns the line content where the name stands
     */
    public String getLine(String name){
        return getFileContent().get(getPositionInFileContentListByName(name));
    }
}
