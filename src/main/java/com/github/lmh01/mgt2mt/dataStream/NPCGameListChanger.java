package com.github.lmh01.mgt2mt.dataStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class NPCGameListChanger {
    private static ArrayList<String> npcGameList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(NPCGameListChanger.class);

    public static void apply(String filePath, int genreID, String operation, int chance){
        logger.info("Beginning file-process...\nusing following arguments:\nFile Path: " + filePath + "\nGenreID: " + genreID);
        File npcGameListFile = new File(filePath);
        try {
            logger.info("Scanning file...");
            Scanner scanner = new Scanner(npcGameListFile);
            while(scanner.hasNextLine()){
                String currentLine = scanner.nextLine();
                npcGameList.add(currentLine);
                logger.info("Current line: " + currentLine);
            }
            logger.info("File scanned.");
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Frame(), "The chosen file does not exist!\nPlease try again!");
            e.printStackTrace();
        }
        logger.info("Backing up old file before deletion...");
        File backupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.backup");
        backupFile.mkdirs();
        try {
            if(backupFile.exists()){
                if(JOptionPane.showConfirmDialog(null, "A backup file already exists.\nReplace?", "A backup file already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    backupFile.delete();
                    createBackup(new PrintWriter(backupFile));
                    backupFile.createNewFile();
                    logger.info("Deleting old file...");
                    npcGameListFile.delete();
                    logger.info("Deleted old file...");
                }else{
                    File backupFile2 = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.backup" + "(" + LocalDateTime.now().toString().replace(":", "-") + ")");
                    createBackup(new PrintWriter(backupFile2));
                    backupFile2.createNewFile();
                    logger.info("Deleting old file...");
                    npcGameListFile.delete();
                    logger.info("Created new backup file.");
                }

            }else{
                backupFile.createNewFile();
                createBackup(new PrintWriter(backupFile));
                logger.info("Deleting old file...");
                npcGameListFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Creating new file...");
        if(operation.equals("add")){
            try {
                npcGameListFile.createNewFile();
                logger.info("File created.\nWriting to file...");
                try {
                    PrintWriter pw;
                    pw = new PrintWriter(npcGameListFile);
                    for(int i = 0; npcGameList.size()>i; i++){
                        int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
                        if(randomNum>(100-chance)){
                            pw.print(npcGameList.get(i) + "<" + genreID + ">" + "\n");
                        }else{
                            pw.print(npcGameList.get(i) + "\n");
                        }
                    }
                    pw.close();
                    JOptionPane.showMessageDialog(new Frame(), "Genre ID [" + genreID + "] has successfully\nbeen added to the NpcGame list.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                npcGameListFile.createNewFile();
                logger.info("File created.\nWriting to file...");
                PrintWriter pw;
                try {
                    pw = new PrintWriter(npcGameListFile);
                    for(int i = 0; npcGameList.size()>i; i++){
                        pw.print(npcGameList.get(i).replace("<" + genreID + ">", "") + "\n");
                    }
                    pw.close();
                    JOptionPane.showMessageDialog(new Frame(), "Genre ID [" + genreID + "] has successfully\nbeen removed from the NpcGame list.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        npcGameList.clear();


    }
    private static void createBackup(PrintWriter pw){
        for (String s : npcGameList) {
            pw.print(s + "\n");
        }
        pw.close();
        logger.info("Backup file created.");
    }
}
