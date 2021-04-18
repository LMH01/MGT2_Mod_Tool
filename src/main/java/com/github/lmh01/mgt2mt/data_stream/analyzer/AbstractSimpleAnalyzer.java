package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


/**
 * The simple analyzer is used to analyze files that use this system:
 * [KeyX]ValueX
 * When a blank line is found a new entry is created see {@link DataStreamHelper#parseDataFile(File)}
 */
abstract class AbstractSimpleAnalyzer implements BaseAnalyzer{
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
     * Replaces the input string and returns the replaced string
     */
    public abstract String getReplacedLine(String inputString);
}
