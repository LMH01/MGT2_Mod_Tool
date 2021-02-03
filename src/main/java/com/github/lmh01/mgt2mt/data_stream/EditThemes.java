package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class EditThemes {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditThemes.class);

    /**
     * Adds all themes that are currently in this map: MAP_COMPATIBLE_THEMES (NewGenreManager)
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     */
    public static void editGenreAllocation(int genreID, boolean addGenreID) throws IOException {
        File fileTopicsGeTemp = new File(Utils.getMGT2TextFolderPath() + "\\GE\\Themes_GE.txt.temp");
        fileTopicsGeTemp.createNewFile();
        Backup.createBackup(Utils.getThemesGeFile());
        LOGGER.info("Themes_GE.txt.temp has been created");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemesGeFile()), StandardCharsets.UTF_16LE));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTopicsGeTemp), StandardCharsets.UTF_16LE));
        String currentLine;
        int currentLineNumber = 0;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
            }
            if(addGenreID){
                if(NewGenreManager.MAP_COMPATIBLE_THEME_IDS.contains(currentLineNumber)){
                    LOGGER.info(currentLineNumber + " - Y: " + currentLine);
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }
                    bw.write(currentLine + "<" + genreID + ">");
                }else{
                    if(!firstLine){
                        bw.write(System.getProperty("line.separator"));
                    }
                    LOGGER.info(currentLineNumber + " - N: " + currentLine);
                    bw.write(currentLine );
                }
            }else{
                if(!firstLine){
                    bw.write(System.getProperty("line.separator"));
                }
                bw.write(currentLine.replace("<" + genreID + ">", ""));
            }
            firstLine = false;
            currentLineNumber++;
        }
        br.close();
        bw.close();
        Utils.getThemesGeFile().delete();
        fileTopicsGeTemp.renameTo(Utils.getThemesGeFile());
        if(addGenreID){
            ChangeLog.addLogEntry(2, Integer.toString(genreID));
        }else{
            ChangeLog.addLogEntry(3, Integer.toString(genreID));
        }
    }
}
