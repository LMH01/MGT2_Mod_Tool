package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class ThemeFileGeAnalyzer extends AbstractSimpleAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeFileGeAnalyzer.class);
    Map<Integer, String> fileContent;
    String[] defaultContent = {};
    String[] customContent = {};

    @Override
    public Map<Integer, String> getFileContent() {
        return fileContent;
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_themes_ge.txt";
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getReplacedLine(String inputString) {
        String replacedLine = inputString.replace("<M1>", "").replace("<M2>", "").replace("<M3>", "").replace("<M4>", "").replace("<M5>", "");
        return replacedLine.replaceAll("<", "").replaceAll(">", "");
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.getContentFromFile(Utils.getThemeFile("GE"), "UTF_16LE");
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.theme.upperCase") + " GE";
    }

    @Override
    public String getMainTranslationKey() {
        return "themeGe";
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault(getDefaultContentFileName());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return ModManager.themeModOld.getAnalyzerGe();
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
