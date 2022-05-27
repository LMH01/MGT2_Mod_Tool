package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CompanyLogoAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyLogoAnalyzer.class);

    static int maxLogoNumber = 0;

    /**
     * Analyzes the logo number and updates {@link CompanyLogoAnalyzer#maxLogoNumber}.
     */
    public static void analyzeLogoNumbers() {
        Path path = MGT2Paths.COMPANY_ICONS.getPath();
        ArrayList<File> companyLogos = DataStreamHelper.getFilesInFolder(path);
        int currentMaxNumber = 0;
        for (File companyLogo : companyLogos) {
            DebugHelper.debug(LOGGER, "current file: " + companyLogo.getPath());
            if (!companyLogo.getName().replaceAll("[^0-9]", "").equals("")) {
                if (Integer.parseInt(companyLogo.getName().replaceAll("[^0-9]", "")) > currentMaxNumber) {
                    currentMaxNumber = Integer.parseInt(companyLogo.getName().replaceAll("[^0-9]", ""));
                }
            }
        }
        maxLogoNumber = currentMaxNumber;
        DebugHelper.debug(LOGGER, "Max logo number: " + maxLogoNumber);
    }

    /**
     * Returns a free logo id.
     * This id is then blocked from being used again.
     */
    public static int getFreeLogoNumber() {
        maxLogoNumber += 1;
        return maxLogoNumber;
    }
}
