package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class EditGenreFile {
    private static Logger logger = LoggerFactory.getLogger(EditGenreFile.class);
    private static File fileTempGenreFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt.temp");
    private static File fileGenres = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");

    /**
     *
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    public static boolean addGenre(){
        try {
            logger.info("Editing Genres.txt and adding new genre...");
            fileTempGenreFile.createNewFile();
            PrintWriter pw = new PrintWriter(fileTempGenreFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileGenres), "utf-8"));
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                if(currentLine.equals("[EOF]")
                        || currentLine.contains("[NAME CH]")
                        || currentLine.contains("[NAME TU]")
                        || currentLine.contains("[NAME CT]")
                        || currentLine.contains("[NAME HU]")
                        || currentLine.contains("[DESC CH]")
                        || currentLine.contains("[DESC TU]")
                        || currentLine.contains("[DESC CT]")
                        || currentLine.contains("[DESC HU]")){
                }else{
                    pw.print(currentLine + "\n");
                    if(Settings.enableDebugLogging){logger.info("Current line: " + currentLine);}
                }
            }
            reader.close();
            logger.info("Content of old file has been written to temp file.");
            logger.info("Deleting old file...");
            fileGenres.delete();
            //Print new genre:
            pw.print("[ID]" + NewGenreManager.id + "\n");
            printLanguages(pw);
            pw.print("[DATE]" + NewGenreManager.unlockMonth + " " + NewGenreManager.unlockYear + "\n");
            pw.print("[RES POINTS]" + NewGenreManager.researchPoints + "\n");
            pw.print("[PRICE]" + NewGenreManager.price + "\n");
            pw.print("[DEV COSTS]" + NewGenreManager.devCost + "\n");
            pw.print("[PIC]" + NewGenreManager.imageFile.getName() + "\n");
            pw.print("[TGROUP]" + getTargetGroup() + "\n");
            pw.print("[GAMEPLAY]" + NewGenreManager.gameplay + "\n");
            pw.print("[GRAPHIC]" + NewGenreManager.graphic + "\n");
            pw.print("[SOUND]" + NewGenreManager.sound + "\n");
            pw.print("[CONTROL]" + NewGenreManager.control + "\n");
            pw.print("[GENRE COMB]" + NewGenreManager.getCompatibleGenresByID() + "\n");
            pw.print("[DESIGN1]" + NewGenreManager.design1 + "\n");
            pw.print("[DESIGN2]" + NewGenreManager.design2 + "\n");
            pw.print("[DESIGN3]" + NewGenreManager.design3 + "\n");
            pw.print("[DESIGN4]" + NewGenreManager.design4 + "\n");
            pw.print("[DESIGN5]" + NewGenreManager.design5 + "\n");
            pw.print("\n[EOF]");
            pw.close();
            logger.info("Temp file has been filled. Renaming...");
            fileTempGenreFile.renameTo(fileGenres);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(), "The genre was not added:\nError while editing Genres.txt\nPlease try again with administrator rights.", "Unable to edit Genres.txt", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public static String removeGenre(int genreId){
        logger.info("Editing Genres.txt and removing genre with id [" + genreId + "]");
        try {
            fileTempGenreFile.createNewFile();
            logger.info("Genres.txt.temp has been created.");
            logger.info("Writing content of Genres.txt except for genre with id + [" + genreId + "] to this file");
            PrintWriter pw = new PrintWriter(fileTempGenreFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileGenres), "utf-8"));
            String currentLine;
            int linesToSkip = 26;
            while((currentLine = reader.readLine()) != null){
                while(linesToSkip<25){
                    currentLine = reader.readLine();
                    linesToSkip++;
                }
                if(currentLine.equals("[EOF]")
                        || currentLine.contains("[NAME CH]")
                        || currentLine.contains("[NAME TU]")
                        || currentLine.contains("[NAME CT]")
                        || currentLine.contains("[NAME HU]")
                        || currentLine.contains("[DESC CH]")
                        || currentLine.contains("[DESC TU]")
                        || currentLine.contains("[DESC CT]")
                        || currentLine.contains("[DESC HU]")){
                }else if(currentLine.equals("[ID]" + genreId)){
                    linesToSkip = 0;
                }else{
                    pw.print(currentLine + "\n");
                    logger.info("Current line: " + currentLine);
                }
            }
            pw.print("[EOF]");
            pw.close();
            reader.close();
            logger.info("Genres.txt.temp has been completed.");
            logger.info("Deleting old file and renaming Genres.txt.temp to Genres.txt");
            fileGenres.delete();
            fileTempGenreFile.renameTo(fileGenres);
            ChangeLog.addLogEntry(4, genreId + "");
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "failed: IOException";
        }

    }
    private static void printLanguages(PrintWriter pw){
        pw.print("[NAME EN]" + NewGenreManager.name + "\n");
        pw.print("[NAME GE]" + NewGenreManager.name + "\n");
        pw.print("[NAME FR]" + NewGenreManager.name + "\n");
        pw.print("[NAME PB]" + NewGenreManager.name + "\n");
        pw.print("[DESC EN]" + NewGenreManager.description + "\n");
        pw.print("[DESC GE]" + NewGenreManager.description + "\n");
        pw.print("[DESC FR]" + NewGenreManager.description + "\n");
        pw.print("[DESC PB]" + NewGenreManager.description + "\n");
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
