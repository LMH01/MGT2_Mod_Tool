package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class NPCGameListChanger {
    private static ArrayList<String> npcGameList;
    private static Logger logger = LoggerFactory.getLogger(NPCGameListChanger.class);

    public static void editNPCGameList(int genreID, String operation, int chance){
        logger.info("Beginning file-process...\nusing following arguments:\n" + "\nGenreID: " + genreID + "\nchange: " + chance);
        File npcGameListFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA\\NpcGames.txt");
        Backup.createBackup(false);
        npcGameList = FileContent.getScannedContentAsArrayList(npcGameListFile);
        npcGameListFile.delete();
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
                    ChangeLog.addLogEntry(2, genreID + "");
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
                    ChangeLog.addLogEntry(3, genreID + "");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        npcGameList.clear();
    }
}
