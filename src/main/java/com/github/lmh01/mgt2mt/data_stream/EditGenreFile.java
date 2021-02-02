package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EditGenreFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditGenreFile.class);
    private static final File FILE_TEMP_GENRE_FILE = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt.temp");

    /**
     * Adds a new genre to the Genres.txt file with the current values that stand in {@link NewGenreManager}
     */
    public static void addGenre() throws IOException {
        LOGGER.info("Adding new genre...");
        createTempFile();
        LOGGER.info("Deleting old Genres.txt file and writing new file.");
        Utils.getGenreFile().delete();
        Utils.getGenreFile().createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));

        int currentid = 0;
        String currentLine;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(Settings.enableDebugLogging) {
                LOGGER.info("currentLine: " + currentLine);
            }
            if(currentLine.contains("[ID]")){
                currentid = Integer.parseInt(currentLine.replace("[ID]", ""));
                bw.write(currentLine + System.getProperty("line.separator"));
            }else if(currentLine.contains("[GENRE COMB]")){
                if(NewGenreManager.getCompatibleGenresByID().contains(Integer.toString(currentid))){
                    bw.write(currentLine + "<" + NewGenreManager.id + ">" + System.getProperty("line.separator"));
                    LOGGER.info("Found compatible genre id in file to write: " + currentid);
                }else{
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }else{
                bw.write(currentLine + System.getProperty("line.separator"));
            }
        }
        br.close();
        FILE_TEMP_GENRE_FILE.delete();
        LOGGER.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
        //Print new genre:
        bw.write("[ID]" + NewGenreManager.id + System.getProperty("line.separator"));
        printLanguages(bw);
        bw.write("[DATE]" + NewGenreManager.unlockMonth + " " + NewGenreManager.unlockYear + System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + NewGenreManager.researchPoints + System.getProperty("line.separator"));
        bw.write("[PRICE]" + NewGenreManager.price + System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + NewGenreManager.devCost + System.getProperty("line.separator"));
        if(NewGenreManager.useDefaultImageFile){
            bw.write("[PIC]iconSkill.png" + System.getProperty("line.separator"));
        }else{
            bw.write("[PIC]" + NewGenreManager.imageFileName + ".png" + System.getProperty("line.separator"));
        }
        //noinspection SpellCheckingInspection
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
        LOGGER.info("Temp file has been filled. Renaming...");
    }

    /**
     * @param genreId The genre id that should be removed.
     */
    public static void removeGenre(int genreId) throws IOException {
        LOGGER.info("Removing genre with id [" + genreId + "] from Genres.txt");
        createTempFile();
        LOGGER.info("Deleting old Genres.txt file and writing new file.");
        Utils.getGenreFile().delete();
        Utils.getGenreFile().createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));

        String currentLine;
        boolean firstLine = true;
        int linesToSkip = 0;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            boolean skipNormalWrite = false;
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(currentLine.equals("[ID]" + genreId)){
                linesToSkip = 36;//This is how many line the genre has that should be removed
            }else if(currentLine.contains("[GENRE COMB]")){
                if(currentLine.contains("<" + genreId + ">")){
                    bw.write(currentLine.replace("<" + genreId + ">", "" + System.getProperty("line.separator")));
                    skipNormalWrite = true;
                }
            }
            if(linesToSkip>0){
                while(linesToSkip>0){
                    currentLine = br.readLine();
                    if(currentLine.contains("[NAME EN]")){
                        LOGGER.info("Found [NAME EN] for genre to remove. Trying to remove image files.");
                        String genreName = currentLine.replace("[NAME EN]", "");
                        ImageFileHandler.removeImageFiles(genreName);
                    }
                    if(Settings.enableDebugLogging){
                        LOGGER.info("CurrentLines for genre that should be removed: " + currentLine);
                    }
                    linesToSkip--;
                }
            }else{
                if(!skipNormalWrite){
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }
        }
        bw.write("[EOF]");
        br.close();
        bw.close();
        FILE_TEMP_GENRE_FILE.delete();
        LOGGER.info("All old genres have been copied to new Genres.txt file. Adding new genre to file now...");
    }

    /**
     * Creates the temp file Genres.txt.temp which will be read later to add a new genre
     */
    private static void createTempFile() throws IOException {
        if(FILE_TEMP_GENRE_FILE.exists()){
            FILE_TEMP_GENRE_FILE.delete();
        }
        FILE_TEMP_GENRE_FILE.createNewFile();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getGenreFile()), StandardCharsets.UTF_8));
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FILE_TEMP_GENRE_FILE), StandardCharsets.UTF_8));
        LOGGER.info("Writing current content to temp file.");
        String currentLine;
        boolean firstLine = true;
        bw.write("\ufeff");
        while((currentLine = br.readLine()) != null){
            if(firstLine) {
                currentLine = Utils.removeUTF8BOM(currentLine);
                firstLine = false;
            }
            if(Settings.enableDebugLogging) {
                LOGGER.info("currentLine: " + currentLine);
            }
            if(!currentLine.equals("[EOF]")){
                bw.write(currentLine + System.getProperty("line.separator"));
            }
        }
        br.close();
        bw.close();
        LOGGER.info("Temp file has been created.");
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
