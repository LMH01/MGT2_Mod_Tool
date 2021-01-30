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
            bw.write("[ID]" + NewGenreManager.id + "\n");
            printLanguages(bw);
            bw.write("[DATE]" + NewGenreManager.unlockMonth + " " + NewGenreManager.unlockYear + "\n");
            bw.write("[RES POINTS]" + NewGenreManager.researchPoints + "\n");
            bw.write("[PRICE]" + NewGenreManager.price + "\n");
            bw.write("[DEV COSTS]" + NewGenreManager.devCost + "\n");
            bw.write("[PIC]" + NewGenreManager.imageFileName + ".png\n");
            bw.write("[TGROUP]" + getTargetGroup() + "\n");
            bw.write("[GAMEPLAY]" + NewGenreManager.gameplay + "\n");
            bw.write("[GRAPHIC]" + NewGenreManager.graphic + "\n");
            bw.write("[SOUND]" + NewGenreManager.sound + "\n");
            bw.write("[CONTROL]" + NewGenreManager.control + "\n");
            bw.write("[GENRE COMB]" + NewGenreManager.getCompatibleGenresByID() + "\n");
            bw.write("[DESIGN1]" + NewGenreManager.design1 + "\n");
            bw.write("[DESIGN2]" + NewGenreManager.design2 + "\n");
            bw.write("[DESIGN3]" + NewGenreManager.design3 + "\n");
            bw.write("[DESIGN4]" + NewGenreManager.design4 + "\n");
            bw.write("[DESIGN5]" + NewGenreManager.design5 + "\n");
            bw.write("\n[EOF]");
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
            int linesToSkip = 38;//This is how many line the genre has that should be removed
            bw.write("\ufeff");
            while((currentLine = br.readLine()) != null){
                if(firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                while(linesToSkip<25){
                    currentLine = br.readLine();
                    linesToSkip++;
                }
                if(currentLine.equals("[EOF]")){

                }else if(currentLine.equals("[ID]" + genreId)){
                    linesToSkip = 0;
                }else{
                    bw.write(currentLine + "\n");
                    logger.info("Current line: " + currentLine);
                }
            }
            br.close();
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
        bw.write("[NAME EN]" + NewGenreManager.name + "\n");
        bw.write("[NAME GE]" + NewGenreManager.name + "\n");
        bw.write("[NAME CH]" + NewGenreManager.name + "\n");
        bw.write("[NAME TU]" + NewGenreManager.name + "\n");
        bw.write("[NAME FR]" + NewGenreManager.name + "\n");
        bw.write("[NAME PB]" + NewGenreManager.name + "\n");
        bw.write("[NAME HU]" + NewGenreManager.name + "\n");
        bw.write("[NAME CT]" + NewGenreManager.name + "\n");
        bw.write("[NAME ES]" + NewGenreManager.name + "\n");
        bw.write("[NAME PL]" + NewGenreManager.name + "\n");

        bw.write("[DESC EN]" + NewGenreManager.description + "\n");
        bw.write("[DESC GE]" + NewGenreManager.description + "\n");
        bw.write("[DESC CH]" + NewGenreManager.description + "\n");
        bw.write("[DESC TU]" + NewGenreManager.description + "\n");
        bw.write("[DESC FR]" + NewGenreManager.description + "\n");
        bw.write("[DESC PB]" + NewGenreManager.description + "\n");
        bw.write("[DESC HU]" + NewGenreManager.description + "\n");
        bw.write("[DESC CT]" + NewGenreManager.description + "\n");
        bw.write("[DESC ES]" + NewGenreManager.description + "\n");
        bw.write("[DESC PL]" + NewGenreManager.description + "\n");
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
