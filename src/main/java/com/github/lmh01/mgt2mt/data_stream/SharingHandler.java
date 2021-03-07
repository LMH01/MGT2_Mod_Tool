package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SharingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingHandler.class);

    /**
     * Exports the specified genre.
     * @param genreId
     * @param genreName
     * @return Returns true when the genre has been exported successfully. Returns false when the genre has already been exported.
     * @throws IOException
     */
    public static boolean exportGenre(int genreId, String genreName) throws IOException {
        final String EXPORTED_GENRE_MAIN_FOLDER_PATH = Settings.MGT2_MOD_MANAGER_PATH + "//Export//" + genreName;
        final String EXPORTED_GENRE_DATA_FOLDER_PATH = EXPORTED_GENRE_MAIN_FOLDER_PATH + "//DATA//";
        File fileDataFolder = new File(EXPORTED_GENRE_DATA_FOLDER_PATH);
        File fileExportedGenre = new File(EXPORTED_GENRE_MAIN_FOLDER_PATH + "//genre.txt");
        File fileExportedGenreIcon = new File(EXPORTED_GENRE_DATA_FOLDER_PATH + "//icon.png");
        File fileGenreIconToExport = new File(Utils.getMGT2GenreIconsPath() + GenreManager.MAP_SINGLE_GENRE.get("[PIC]"));
        File fileGenreScreenshotsToExport = new File(Utils.getMGT2ScreenshotsPath() + genreId);
        if(!fileExportedGenreIcon.exists()){
            fileDataFolder.mkdirs();
        }
        if(fileExportedGenreIcon.exists()){
            return false;
        }
        if(Settings.enableDebugLogging){
            LOGGER.info("Copying image files to export folder...");
        }
        Files.copy(Paths.get(fileGenreIconToExport.getPath()),Paths.get(fileExportedGenreIcon.getPath()));
        Utils.copyDirectory(fileGenreScreenshotsToExport.toPath().toString(), EXPORTED_GENRE_DATA_FOLDER_PATH + "//screenshots//");
        fileExportedGenre.createNewFile();
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileExportedGenre), StandardCharsets.UTF_8));
        bw.write("\ufeff");//Makes the file UTF8-BOM
        bw.print("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
        bw.print("[GENRE START]" + System.getProperty("line.separator"));
        bw.print("[NAME AR]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME AR]") + System.getProperty("line.separator"));
        bw.print("[NAME CH]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME CH]") + System.getProperty("line.separator"));
        bw.print("[NAME CT]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME CT]") + System.getProperty("line.separator"));
        bw.print("[NAME EN]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]") + System.getProperty("line.separator"));
        bw.print("[NAME ES]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME ES]") + System.getProperty("line.separator"));
        bw.print("[NAME FR]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME FR]") + System.getProperty("line.separator"));
        bw.print("[NAME GE]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME GE]") + System.getProperty("line.separator"));
        bw.print("[NAME HU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME HU]") + System.getProperty("line.separator"));
        bw.print("[NAME IT]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME IT]") + System.getProperty("line.separator"));
        bw.print("[NAME KO]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME KO]") + System.getProperty("line.separator"));
        bw.print("[NAME PB]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME PB]") + System.getProperty("line.separator"));
        bw.print("[NAME PL]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME PL]") + System.getProperty("line.separator"));
        bw.print("[NAME RU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME RU]") + System.getProperty("line.separator"));
        bw.print("[NAME TU]" + GenreManager.MAP_SINGLE_GENRE.get("[NAME TU]") + System.getProperty("line.separator"));
        bw.print("[DESC AR]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC AR]") + System.getProperty("line.separator"));
        bw.print("[DESC CH]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC CH]") + System.getProperty("line.separator"));
        bw.print("[DESC CT]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC CT]") + System.getProperty("line.separator"));
        bw.print("[DESC EN]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC EN]") + System.getProperty("line.separator"));
        bw.print("[DESC ES]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC ES]") + System.getProperty("line.separator"));
        bw.print("[DESC FR]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC FR]") + System.getProperty("line.separator"));
        bw.print("[DESC GE]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC GE]") + System.getProperty("line.separator"));
        bw.print("[DESC HU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC HU]") + System.getProperty("line.separator"));
        bw.print("[DESC IT]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC IT]") + System.getProperty("line.separator"));
        bw.print("[DESC KO]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC KO]") + System.getProperty("line.separator"));
        bw.print("[DESC PB]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC PB]") + System.getProperty("line.separator"));
        bw.print("[DESC PL]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC PL]") + System.getProperty("line.separator"));
        bw.print("[DESC RU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC RU]") + System.getProperty("line.separator"));
        bw.print("[DESC TU]" + GenreManager.MAP_SINGLE_GENRE.get("[DESC TU]") + System.getProperty("line.separator"));
        bw.print("[DATE]" + GenreManager.MAP_SINGLE_GENRE.get("[DATE]") + System.getProperty("line.separator"));
        bw.print("[RES POINTS]" + GenreManager.MAP_SINGLE_GENRE.get("[RES POINTS]") + System.getProperty("line.separator"));
        bw.print("[PRICE]" + GenreManager.MAP_SINGLE_GENRE.get("[PRICE]") + System.getProperty("line.separator"));
        bw.print("[DEV COSTS]" + GenreManager.MAP_SINGLE_GENRE.get("[DEV COSTS]") + System.getProperty("line.separator"));
        bw.print("[TGROUP]" + GenreManager.MAP_SINGLE_GENRE.get("[TGROUP]") + System.getProperty("line.separator"));
        bw.print("[GAMEPLAY]" + GenreManager.MAP_SINGLE_GENRE.get("[GAMEPLAY]") + System.getProperty("line.separator"));
        bw.print("[GRAPHIC]" + GenreManager.MAP_SINGLE_GENRE.get("[GRAPHIC]") + System.getProperty("line.separator"));
        bw.print("[SOUND]" + GenreManager.MAP_SINGLE_GENRE.get("[SOUND]") + System.getProperty("line.separator"));
        bw.print("[CONTROL]" + GenreManager.MAP_SINGLE_GENRE.get("[CONTROL]") + System.getProperty("line.separator"));
        bw.print("[GENRE COMB]" + getGenreNames() + System.getProperty("line.separator"));
        bw.print("[DESIGN1]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN1]") + System.getProperty("line.separator"));
        bw.print("[DESIGN2]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN2]") + System.getProperty("line.separator"));
        bw.print("[DESIGN3]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN3]") + System.getProperty("line.separator"));
        bw.print("[DESIGN4]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN4]") + System.getProperty("line.separator"));
        bw.print("[DESIGN5]" + GenreManager.MAP_SINGLE_GENRE.get("[DESIGN5]") + System.getProperty("line.separator"));
        bw.print("[GENRE END]");
        bw.close();
        ChangeLog.addLogEntry(17, GenreManager.MAP_SINGLE_GENRE.get("[NAME EN]"));
        return true;
    }

    private static String getGenreNames(){
        String genreNumbersRaw = GenreManager.MAP_SINGLE_GENRE.get("[GENRE COMB]");
        String genreNames = "";
        int charPositon = 0;
        String currentNumber = "";
        for(int i = 0; i<genreNumbersRaw.length(); i++){
            if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals("<")){
                //Nothing happens
            }else if(String.valueOf(genreNumbersRaw.charAt(charPositon)).equals(">")){
                int genreNumber = Integer.parseInt(currentNumber);
                LOGGER.info("genreNumber: " + genreNumber);
                currentNumber = "";
                genreNames = genreNames + "<" + AnalyzeExistingGenres.getGenreNameById(genreNumber) + ">";
            }else{
                currentNumber = currentNumber + genreNumbersRaw.charAt(charPositon);
                LOGGER.info("currentNumber: " + currentNumber);
            }
            charPositon++;
        }
        String.valueOf(genreNumbersRaw.charAt(1));
        return genreNames;
    }
}
