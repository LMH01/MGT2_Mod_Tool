package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class LicenceAnalyzer extends AbstractSimpleAnalyzer implements LicenceAnalyzerInterface{
    Map<Integer, String> fileContent;
    String[] defaultContent = {};

    @Override
    public Map<Integer, String> getFileContent() {
        return fileContent;
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.getContentFromFile(Utils.getLicenceFile(), "UTF_8BOM");
    }

    @Override
    public String getAnalyzerType() {
        return I18n.INSTANCE.get("commonText.licence.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault("default_licences.txt", this::getReplacedLine);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    @Override
    public String getTypeForLicence(String licenceName) {
        for(int i=1; i<=fileContent.size(); i++){
            String currentLicence = fileContent.get(i);
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

    @Override
    public String[] getCustomContentString(boolean disableTextAreaMessage) {
        String[] allLicenceNamesByAlphabet = getContentByAlphabet();

        ArrayList<String> arrayListCustomLicences = new ArrayList<>();

        ProgressBarHelper.initializeProgressBar(0, allLicenceNamesByAlphabet.length, I18n.INSTANCE.get("progressBar.moddedLicences"), !disableTextAreaMessage);
        int currentProgressBarValue = 0;
        for (String s : allLicenceNamesByAlphabet) {
            boolean defaultGenre = false;
            for (String licenceName : getDefaultContent()) {
                if (s.equals(licenceName)) {
                    defaultGenre = true;
                    break;
                }
                if(s.equals("Chronicles of Nornio [5]")){
                    defaultGenre = true;
                }
            }
            if (!defaultGenre) {
                arrayListCustomLicences.add(s);
                if(!disableTextAreaMessage){
                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicenceFound") + " " + s);
                }
            }
            currentProgressBarValue++;
            ProgressBarHelper.setValue(currentProgressBarValue);
        }
        if(!disableTextAreaMessage){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.moddedLicencesComplete"));
        }
        ProgressBarHelper.resetProgressBar();
        arrayListCustomLicences.remove("Chronicles of Nornio [5]");
        String[] string = new String[arrayListCustomLicences.size()];
        arrayListCustomLicences.toArray(string);
        return string;
    }
}
