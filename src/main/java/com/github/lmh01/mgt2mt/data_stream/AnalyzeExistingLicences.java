package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeExistingLicences {
    public static Map<Integer, String> existingLicences = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeExistingLicences.class);

    public static void analyze() throws IOException {
        existingLicences = DataStreamHelper.getContentFromFile(Utils.getLicenceFile(), "UTF_8BOM");
    }

    /**
     * @return Returns a string containing the licence names sorted by alphabet
     */
    public static String[] getLicenceNamesByAlphabet(){
        ArrayList<String> licenceNames = new ArrayList<>();
        for(int i=1; i<=existingLicences.size(); i++){
            licenceNames.add(existingLicences.get(i).replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim());
        }
        Collections.sort(licenceNames);
        String[] strings = new String[licenceNames.size()];
        licenceNames.toArray(strings);
        return strings;
    }

    public static String getTypeForLicence(String licenceName){
        for(int i=1; i<=existingLicences.size(); i++){
            String currentLicence = existingLicences.get(i);
            if(currentLicence.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim().equals(licenceName)){
                if(currentLicence.contains("[MOVIE]")){
                    return "[MOVIE]";
                }else if(currentLicence.contains("[BOOK]")){
                    return "[BOOK]";
                }else if(currentLicence.contains("[SPORT]")){
                    return "[SPORT]";
                }
            }
        }
        return "";
    }

    public static String[] getCustomLicenceNamesByAlphabet(){
        try{
            String[] allLicenceNamesByAlphabet = getLicenceNamesByAlphabet();

            ArrayList<String> arrayListCustomLicences = new ArrayList<>();

            ProgressBarHelper.initializeProgressBar(0, allLicenceNamesByAlphabet.length, I18n.INSTANCE.get("progressBar.moddedLicences"), true);
            int currentProgressBarValue = 0;
            for (String s : allLicenceNamesByAlphabet) {
                boolean defaultGenre = false;
                for (String licenceName : ReadDefaultContent.getDefaultLicences()) {
                    if (s.equals(licenceName)) {
                        defaultGenre = true;
                        break;
                    }
                    if(licenceName.equals("Chronicles of Nornio [5]")){
                        defaultGenre = true;
                    }
                }
                if (!defaultGenre) {
                    arrayListCustomLicences.add(s);
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicenceFound") + " " + s);
                }
                currentProgressBarValue++;
                ProgressBarHelper.setValue(currentProgressBarValue);
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicencesComplete"));
            ProgressBarHelper.resetProgressBar();
            arrayListCustomLicences.remove("Chronicles of Nornio [5]");
            String[] string = new String[arrayListCustomLicences.size()];
            arrayListCustomLicences.toArray(string);
            return string;
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error wile scanning licences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * @param name The name that should be searched
     * @return Returns the position of the input name in the file
     */
    public static int getLicenceIdByName(String name){
        for(Map.Entry<Integer, String> entry : existingLicences.entrySet()){
            if(entry.getValue().contains(name)){
                return entry.getKey();
            }
        }
        return -1;
    }
}