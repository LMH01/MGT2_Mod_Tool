package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EditLicenceFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditLicenceFile.class);
    public static boolean addLicence(Map<String, String> map) throws IOException {
        return editLicenceFile(true, null, map);
    }
    public static boolean removeLicence(String licenceName) throws IOException {
        boolean returnValue = editLicenceFile(false, licenceName, null);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.licence") + " - " + licenceName);
        return returnValue;
    }
    private static boolean editLicenceFile(boolean addLicence, String licenceName, Map<String, String> newLicenceMap) throws IOException {
        AnalyzeManager.licenceAnalyzer.analyzeFile();
        File licenceFile = Utils.getLicenceFile();
        if(Settings.disableSafetyFeatures){
            LOGGER.info("Deleting old publisher file.");
        }
        if(licenceFile.exists()){
            licenceFile.delete();
            licenceFile.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(licenceFile), StandardCharsets.UTF_8));
        bw.write("\ufeff");
        if(Settings.enableDebugLogging){
            LOGGER.info("Writing contents of list to file: " + licenceFile.getPath());
        }
        boolean firstLine = true;
        for(int i=1; i<=AnalyzeManager.licenceAnalyzer.getFileContent().size(); i++){
            if(addLicence){
                if(!firstLine){
                    bw.write(System.getProperty("line.separator"));
                }else{
                    firstLine = false;
                }
                bw.write(AnalyzeManager.licenceAnalyzer.getFileContent().get(i));
            }else{
                if(!AnalyzeManager.licenceAnalyzer.getFileContent().get(i).replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim().equals(licenceName)){
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }else{
                        firstLine = false;
                    }
                    bw.write(AnalyzeManager.licenceAnalyzer.getFileContent().get(i));
                }
            }
        }
        if(addLicence){
            bw.write(System.getProperty("line.separator"));
            bw.write(newLicenceMap.get("NAME") + " " + newLicenceMap.get("TYPE"));
            ChangeLog.addLogEntry(35, newLicenceMap.get("NAME"));
        }else{
            ChangeLog.addLogEntry(36, licenceName);
        }
        bw.close();
        return true;
    }
}
