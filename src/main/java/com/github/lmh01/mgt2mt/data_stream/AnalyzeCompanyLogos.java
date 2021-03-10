package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class AnalyzeCompanyLogos {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeCompanyLogos.class);
    public static int maxLogoNumber = 0;
    public static void analyzeLogos(){
        File companyLogosPath = new File(Utils.getCompanyLogosPath());
        ArrayList<File> companyLogos = Utils.getFilesInFolder(companyLogosPath.getPath());
        int currentMaxNumber = 0;
        for(int i=0; i<companyLogos.size(); i++){
            LOGGER.info("current file: " + companyLogos.get(i).getPath());
            if(!companyLogos.get(i).getName().replaceAll("[^0-9]", "").equals("")){
                if(Integer.parseInt(companyLogos.get(i).getName().replaceAll("[^0-9]", "")) > currentMaxNumber){
                    currentMaxNumber = Integer.parseInt(companyLogos.get(i).getName().replaceAll("[^0-9]", ""));
                }
            }
        }
        maxLogoNumber = currentMaxNumber;
        LOGGER.info("Max logo number: " + maxLogoNumber);
    }
}
