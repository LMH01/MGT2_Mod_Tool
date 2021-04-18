package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.ArrayList;

public class CompanyLogoAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyLogoAnalyzer.class);

    /**
     * @return Returns a free logo number for a company logo.
     */
    public static int getLogoNumber(){
        File companyLogosPath = new File(Utils.getCompanyLogosPath());
        ArrayList<File> companyLogos = DataStreamHelper.getFilesInFolder(companyLogosPath.getPath());
        int currentMaxNumber = 0;
        for (File companyLogo : companyLogos) {
            if (Settings.enableDebugLogging) {
                LOGGER.info("current file: " + companyLogo.getPath());
            }
            if (!companyLogo.getName().replaceAll("[^0-9]", "").equals("")) {
                if (Integer.parseInt(companyLogo.getName().replaceAll("[^0-9]", "")) > currentMaxNumber) {
                    currentMaxNumber = Integer.parseInt(companyLogo.getName().replaceAll("[^0-9]", ""));
                }
            }
        }
        int numberOut = currentMaxNumber+1;
        LOGGER.info("Max logo number: " + numberOut);
        return numberOut;
    }
}
