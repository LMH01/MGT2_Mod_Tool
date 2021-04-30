package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PublisherAnalyzer extends AbstractAdvancedAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherAnalyzer.class);
    List<Map<String, String>> fileContent;
    String[] defaultContent = {};
    String[] customContent = {};
    int maxId = 0;

    @Override
    public File getFileToAnalyze() {
        return ModManager.publisherMod.getFile();
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public void setMaxId(int id) {
        maxId = id;
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = getAnalyzedFile();
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.publisher.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "publisher";
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault("default_publisher.txt");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
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
