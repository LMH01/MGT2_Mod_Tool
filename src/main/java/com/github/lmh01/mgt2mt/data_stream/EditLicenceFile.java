package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
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
        return editLicenceFile(false, licenceName, null);
    }
    private static boolean editLicenceFile(boolean addLicence, String licenceName, Map<String, String> newLicenceMap) throws IOException {
        AnalyzeExistingLicences.analyze();
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
        for(int i=1; i<=AnalyzeExistingLicences.existingLicences.size(); i++){
            if(addLicence){
                if(!firstLine){
                    bw.write(System.getProperty("line.separator"));
                }else{
                    firstLine = false;
                }
                bw.write(AnalyzeExistingLicences.existingLicences.get(i));
            }else{
                if(!AnalyzeExistingLicences.existingLicences.get(i).replace("[MOVIE]", "").replace("[BOOK]", "").replace("[SPORT]", "").trim().equals(licenceName)){
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }else{
                        firstLine = false;
                    }
                    bw.write(AnalyzeExistingLicences.existingLicences.get(i));
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
