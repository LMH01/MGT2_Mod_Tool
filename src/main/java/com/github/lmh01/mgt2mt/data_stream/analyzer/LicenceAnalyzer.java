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
    public String getDefaultContentFile() {
        return "default_licences.txt";
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
        try{
            String[] allLicenceNamesByAlphabet = getContentByAlphabet();

            ArrayList<String> arrayListCustomLicences = new ArrayList<>();

            ProgressBarHelper.initializeProgressBar(0, allLicenceNamesByAlphabet.length, I18n.INSTANCE.get("progressBar.moddedLicences"), !disableTextAreaMessage);
            int currentProgressBarValue = 0;
            for (String s : allLicenceNamesByAlphabet) {
                boolean defaultGenre = false;
                for (String licenceName : ReadDefaultContent.getDefault(getDefaultContentFile(), this::getReplacedLine)) {
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
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("dialog.analyzeExistingLicences.errorWhileScanningLicences") + " " + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
