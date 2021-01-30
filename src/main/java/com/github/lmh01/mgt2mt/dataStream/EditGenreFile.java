package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class EditGenreFile {
    private static Logger logger = LoggerFactory.getLogger(EditGenreFile.class);
    private static File fileTempGenreFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt.temp");

    /**
     * Adds a new genre to the Genres.txt file with the current values that stand in {@link NewGenreManager}
     */
    public static boolean addGenre(){
        try{
            logger.info("Adding new genre...");
            createTempFile();
            logger.info("Deleting old Genres.txt file and writing new file.");
            Utils.fileGenres.delete();
            Utils.fileGenres.createNewFile();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileTempGenreFile), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.fileGenres), StandardCharsets.UTF_8));

            String currentLine;
            boolean firstLine = true;
            bw.write("\ufeff");
            while((currentLine = br.readLine()) != null){
                if(firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(Settings.enableDebugLogging) {
                    logger.info("currentLine: " + currentLine);
                }
                bw.write(currentLine + System.getProperty("line.separator"));
            }
            br.close();
            fileTempGenreFile.delete();
            logger.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
            //Print new genre:
            bw.write("[ID]" + NewGenreManager.id + System.getProperty("line.separator"));
            printLanguages(bw);
            bw.write("[DATE]" + NewGenreManager.unlockMonth + " " + NewGenreManager.unlockYear + System.getProperty("line.separator"));
            bw.write("[RES POINTS]" + NewGenreManager.researchPoints + System.getProperty("line.separator"));
            bw.write("[PRICE]" + NewGenreManager.price + System.getProperty("line.separator"));
            bw.write("[DEV COSTS]" + NewGenreManager.devCost + System.getProperty("line.separator"));
            bw.write("[PIC]" + NewGenreManager.imageFileName + ".png" + System.getProperty("line.separator"));
            bw.write("[TGROUP]" + getTargetGroup() + System.getProperty("line.separator"));
            bw.write("[GAMEPLAY]" + NewGenreManager.gameplay + System.getProperty("line.separator"));
            bw.write("[GRAPHIC]" + NewGenreManager.graphic + System.getProperty("line.separator"));
            bw.write("[SOUND]" + NewGenreManager.sound + System.getProperty("line.separator"));
            bw.write("[CONTROL]" + NewGenreManager.control + System.getProperty("line.separator"));
            bw.write("[GENRE COMB]" + NewGenreManager.getCompatibleGenresByID() + System.getProperty("line.separator"));
            bw.write("[DESIGN1]" + NewGenreManager.design1 + System.getProperty("line.separator"));
            bw.write("[DESIGN2]" + NewGenreManager.design2 + System.getProperty("line.separator"));
            bw.write("[DESIGN3]" + NewGenreManager.design3 + System.getProperty("line.separator"));
            bw.write("[DESIGN4]" + NewGenreManager.design4 + System.getProperty("line.separator"));
            bw.write("[DESIGN5]" + NewGenreManager.design5 + System.getProperty("line.separator"));
            bw.write(System.getProperty("line.separator") + "[EOF]");
            bw.close();
            logger.info("Temp file has been filled. Renaming...");
            return true;
        }catch (IOException e){
            return false;
        }
    }

    /**
     * @param genreId The genre id that should be removed.
     */
    public static String removeGenre(int genreId){
        try {
            logger.info("Removing genre with id [" + genreId + "] from Genres.txt");
            createTempFile();
            logger.info("Deleting old Genres.txt file and writing new file.");
            Utils.fileGenres.delete();
            Utils.fileGenres.createNewFile();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileTempGenreFile), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.fileGenres), StandardCharsets.UTF_8));

            String currentLine;
            boolean firstLine = true;
            int linesToSkip = 0;
            bw.write("\ufeff");
            while((currentLine = br.readLine()) != null){
                if(firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if(currentLine.equals("[ID]" + genreId)){
                    linesToSkip = 36;//This is how many line the genre has that should be removed
                }
                if(linesToSkip>0){
                    while(linesToSkip>0){
                        br.readLine();
                        linesToSkip--;
                    }
                }else{
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }
            bw.write("[EOF]");
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileTempGenreFile.delete();
            logger.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
            return "success";
    }

    /**
     * Creates the temp file Genres.txt.temp which will be read later to add a new genre
     */
    private static void createTempFile() throws IOException {
        if(fileTempGenreFile.exists()){
            fileTempGenreFile.delete();
        }
        fileTempGenreFile.createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.fileGenres), StandardCharsets.UTF_8));
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileTempGenreFile), StandardCharsets.UTF_8));
        logger.info("Writing current content to temp file.");
        String currentLine;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(Settings.enableDebugLogging) {
                logger.info("currentLine: " + currentLine);
            }
            if(!currentLine.equals("[EOF]")){
                bw.write(currentLine + System.getProperty("line.separator"));
            }
        }
        br.close();
        bw.close();
        logger.info("Temp file has been created.");
    }

    private static void printLanguages(BufferedWriter bw) throws IOException {
        bw.write("[NAME EN]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[NAME GE]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[NAME CH]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[NAME TU]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[NAME FR]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC EN]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[DESC GE]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[DESC CH]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[DESC TU]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[DESC FR]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[NAME PB]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC PB]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[NAME HU]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC HU]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[NAME CT]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC CT]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[NAME ES]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC ES]" + NewGenreManager.description + System.getProperty("line.separator"));
        bw.write("[NAME PL]" + NewGenreManager.name + System.getProperty("line.separator"));
        bw.write("[DESC PL]" + NewGenreManager.description + System.getProperty("line.separator"));
    }
    private static String getTargetGroup(){
        String targetGroups = "";
        if(NewGenreManager.targetGroupKid){
            targetGroups = targetGroups + "<KID>";
        }
        if(NewGenreManager.targetGroupTeen){
            targetGroups = targetGroups + "<TEEN>";
        }
        if(NewGenreManager.targetGroupAdult){
            targetGroups = targetGroups + "<ADULT>";
        }
        if(NewGenreManager.targetGroupSenior){
            targetGroups = targetGroups + "<OLD>";
        }
        return targetGroups;
    }
}
