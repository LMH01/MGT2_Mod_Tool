package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class LicenceAnalyzer extends AbstractSimpleAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceAnalyzer.class);
    Map<Integer, String> fileContent;
    String[] defaultContent = {};
    String[] customContent = {};

    @Override
    public Map<Integer, String> getFileContent() {
        return fileContent;
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim();
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.getContentFromFile(ModManager.licenceMod.getFile(), "UTF_8BOM");
    }

    @Override
    public String getType() {
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

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.licenceMod.getAnalyzer();
    }

    @Override
    public String[] getFinishedCustomContentString() {
        return customContent;
    }

    @Override
    public void setFinishedCustomContentString(String[] customContent) {
        this.customContent = customContent;
    }
}
