package com.github.lmh01.dataStream;

import com.github.lmh01.helpers.DebugHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.SimpleFormatter;

public class NPCGameListChanger {
    private static ArrayList<String> npcGameList = new ArrayList<>();
    public static void apply(String filePath, int genreID, String operation, int chance){
        DebugHelper.sendInfo("Beginning file-process...\nusing following arguments:\nFile Path: " + filePath + "\nGenreID: " + genreID);
        File npcGameListFile = new File(filePath);
        try {
            DebugHelper.sendInfo("Scanning file...");
            Scanner scanner = new Scanner(npcGameListFile);
            while(scanner.hasNextLine()){
                String currentLine = scanner.nextLine();
                npcGameList.add(currentLine);
                DebugHelper.sendInfo("Current line: " + currentLine);
            }
            DebugHelper.sendInfo("File scanned.");
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Frame(), "The chosen file does not exist!\nPlease try again!");
            e.printStackTrace();
        }
        DebugHelper.sendInfo("Backing up old file before deletion...");
        File backupFile = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.backup");
        backupFile.mkdirs();
        try {
            if(backupFile.exists()){
                if(JOptionPane.showConfirmDialog(null, "A backup file already exists.\nReplace?", "A backup file already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    backupFile.delete();
                    createBackup(new PrintWriter(backupFile));
                    backupFile.createNewFile();
                    DebugHelper.sendInfo("Deleting old file...");
                    npcGameListFile.delete();
                    DebugHelper.sendInfo("Deleted old file...");
                }else{
                    File backupFile2 = new File(System.getenv("APPDATA") + "//LMH01//MGT2_Mod_Manager//Backup//NpcGames.txt.backup" + "(" + LocalDateTime.now().toString().replace(":", "-") + ")");
                    createBackup(new PrintWriter(backupFile2));
                    backupFile2.createNewFile();
                    DebugHelper.sendInfo("Deleting old file...");
                    npcGameListFile.delete();
                    DebugHelper.sendInfo("Created new backup file.");
                }

            }else{
                backupFile.createNewFile();
                createBackup(new PrintWriter(backupFile));
                DebugHelper.sendInfo("Deleting old file...");
                npcGameListFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DebugHelper.sendInfo("Creating new file...");
        if(operation.equals("add")){
            try {
                npcGameListFile.createNewFile();
                DebugHelper.sendInfo("File created.\nWriting to file...");
                PrintWriter pw = null;
                try {
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
                DebugHelper.sendInfo("File created.\nWriting to file...");
                PrintWriter pw = null;
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
        for(int i = 0; npcGameList.size()>i; i++){
            pw.print(npcGameList.get(i) + "\n");
        }
        pw.close();
        DebugHelper.sendInfo("Backup file created.");
    }
}
