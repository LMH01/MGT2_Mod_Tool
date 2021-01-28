package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class EditGenreFile {
    private static Logger logger = LoggerFactory.getLogger(EditGenreFile.class);
    private static ArrayList<String> arrayListCurrentGenres = new ArrayList<>();
    private static File fileTempGenreFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt.temp");
    private static File fileGenres = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\Genres.txt");
    public static String removeGenre(int genreId){
        return "success";
    }
    public static void addGenre(){
        try {
            logger.info("Editing Genres.txt and adding new genre...");
            fileTempGenreFile.createNewFile();
            PrintWriter pw = new PrintWriter(fileTempGenreFile);
            Scanner scanner = new Scanner(fileGenres, "utf-8");
            while(scanner.hasNextLine()){
                String currentLine = scanner.nextLine();
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
                    logger.info("Current line: " + currentLine);
                }
            }
            scanner.close();
            JOptionPane.showMessageDialog(null, "Delete genres.txt");
            logger.info("Content of old file has been written to temp file.");
            logger.info("Deleting old file...");
            fileGenres.delete();
            //Print new genre:
            pw.print("[ID]" + NewGenreManager.id + "\n");
            pw.print("[NAME GE]" + NewGenreManager.name + "\n");
            pw.print("[NAME EN]" + NewGenreManager.name + "\n");
            pw.print("[DESC " + NewGenreManager.getLanguageAbbreviation() + "]" + NewGenreManager.description + "\n");
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

            ImageIcon iconGenre = new ImageIcon(NewGenreManager.imageFile.getPath());
            JOptionPane.showMessageDialog(new Frame(), "Genre " + NewGenreManager.name + " with id [" + NewGenreManager.id + "]\nhas been added successfully.", "Genre added", JOptionPane.INFORMATION_MESSAGE, iconGenre);
            WindowAddNewGenre.createFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void scanContentOfFile(File genresFile){
        logger.info("reading contents of file: " + genresFile);
        arrayListCurrentGenres.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(genresFile, "utf-8");
            String currentGenre = "";
            while(scanner.hasNextLine()){
                String currentLine = scanner.nextLine();
                if(currentLine.contains("[ID]")){
                    logger.info("Found new genre...");
                    currentGenre = currentLine + "\n";
                }else if(currentLine.contains("[DESIGN5]")){
                    logger.info("Genre complete.");
                    currentGenre = currentLine + "\n";
                    arrayListCurrentGenres.add(currentGenre);
                }else{
                    currentGenre = currentLine + "\n";
                }
                arrayListCurrentGenres.add(currentLine);
                logger.info("contents of file: " + currentLine);
                scanner.close();
                logger.info("content has been read.");
            }
            logger.info("Genres.txt has been scanned.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
