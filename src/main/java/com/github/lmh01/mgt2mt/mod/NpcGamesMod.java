package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class NpcGamesMod extends AbstractSimpleMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(NpcGamesMod.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "npcGames";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.npcGamesMod;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "NpcGames.txt");
    }

    @Override
    protected String getDefaultContentFileName() {
        return "default_npcGames.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        JLabel labelModName = new JLabel(I18n.INSTANCE.get("mod.npcGames.addMod.components.labelName"));
        JTextField textFieldName = new JTextField();

        JLabel labelExplainList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcGames.addMod.selectGenres") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
        JList<String> listAvailableThemes = WindowHelper.getList(ModManager.genreModOld.getAnalyzer().getContentByAlphabet(), true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableThemes);

        Object[] params = {labelModName, textFieldName, labelExplainList, scrollPaneAvailableGenres};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                String newModName = textFieldName.getText();
                if(!newModName.isEmpty()){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        boolean modAlreadyExists = false;
                        for(String string : ModManager.npcGamesModOld.getBaseAnalyzer().getContentByAlphabet()){
                            if (newModName.equals(string)) {
                                modAlreadyExists = true;
                                break;
                            }
                        }
                        if(!modAlreadyExists){
                            StringBuilder newModLine = new StringBuilder();
                            newModLine.append(newModName).append(" ");
                            ArrayList<Integer> genreIds = new ArrayList<>();
                            for(String string : listAvailableThemes.getSelectedValuesList()){
                                genreIds.add(ModManager.genreModOld.getAnalyzer().getContentIdByName(string));
                            }
                            Collections.sort(genreIds);
                            for(Integer integer : genreIds){
                                newModLine.append("<").append(integer).append(">");
                            }
                            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(newModLine.toString()), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                try {
                                    Backup.createBackup(getGameFile());
                                    ModManager.npcGamesModOld.getBaseEditor().addMod(newModLine.toString());
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + getType() + " - " + newModName);
                                    JOptionPane.showMessageDialog(null, getType() + " [" + newModName + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"));
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.unableToAdd") + " " + getType() + " - " + I18n.INSTANCE.get("commonText.modAlreadyExists"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.chooseGenreFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                break;
            }
        }
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        String line = (String) t;
        ArrayList<String> genreIdsString = Utils.getEntriesFromString(line);
        ArrayList<Integer> genreIds = new ArrayList<>();
        for(String string : genreIdsString){
            LOGGER.info("String: " + string);
            genreIds.add(Integer.parseInt(string));
        }
        String name = Utils.getFirstPart(line);
        StringBuilder output = new StringBuilder();
        output.append(name);
        for(Integer integer : genreIds){
            try{
                String genreName = ModManager.genreModOld.getAnalyzer().getContentNameById(integer);
                if(!genreName.equals("null")){
                    output.append("<").append(genreName).append(">");
                }
            }catch(ArrayIndexOutOfBoundsException ignored) {

            }
        }
        return output.toString();
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }

    @Override
    protected String getTypeCaps() {
        return "NPC_GAME";
    }

    @Override
    public String getImportExportFileName() {
        return "npcGame.txt";
    }

    @Override
    public String getReplacedLine(String inputString) {
        StringBuilder outputString = new StringBuilder();
        boolean nameComplete = false;
        for(Character character : inputString.toCharArray()){
            if(character.toString().equals("<")){
                nameComplete = true;
            }
            if(!nameComplete){
                outputString.append(character);
            }
        }
        return outputString.toString().trim();
    }

    @Override
    public String getModFileCharset() {
        return "UTF_16LE";
    }

    /**
     * Modifies the NpcGames.txt file to include/remove a specified genre id
     * @param genreID The genre id that should be added/removed.
     * @param addGenreID If true the genre will be added to the list. If false the genre will be removed from the list.
     * @param chance The chance with which the genre should be added to the npc game list 100 = 100%
     */
    public static void editNPCGames(int genreID, boolean addGenreID, int chance) throws ModProcessingException {
        try {
            File fileNpcGamesTemp = new File(Utils.getMGT2DataPath() + "\\NpcGames.txt.temp");
            fileNpcGamesTemp.createNewFile();
            Backup.createBackup(Utils.getNpcGamesFile());
            LOGGER.info("NpcGames.txt.temp has been created");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getNpcGamesFile()), StandardCharsets.UTF_16LE));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNpcGamesTemp), StandardCharsets.UTF_16LE));
            String currentLine;
            while((currentLine = br.readLine()) != null){
                if(addGenreID){
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
                    if(randomNum>(100-chance)){
                        bw.write(currentLine + "<" + genreID + ">" + System.getProperty("line.separator"));
                    }else{
                        bw.write(currentLine + System.getProperty("line.separator"));
                    }
                }else{
                    bw.write(currentLine.replace("<" + genreID + ">", "") + System.getProperty("line.separator"));
                }
            }
            br.close();
            bw.close();
            Utils.getNpcGamesFile().delete();
            fileNpcGamesTemp.renameTo(Utils.getNpcGamesFile());
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing npcGames.txt file: " + e.getMessage());
        }
    }
}
