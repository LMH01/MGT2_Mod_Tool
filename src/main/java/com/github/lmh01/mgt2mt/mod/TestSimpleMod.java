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
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class TestSimpleMod extends AbstractSimpleMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherMod.class);

    @Override
    public String getMainTranslationKey() {
        return "testSimpleMod";
    }

    @Override
    public AbstractBaseMod getMod() {
        return this;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "NpcGames.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_npcGames.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        JLabel labelModName = new JLabel(I18n.INSTANCE.get("mod.npcGames.addMod.components.labelName"));
        JTextField textFieldName = new JTextField();

        JLabel labelExplainList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcGames.addMod.selectGenres") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
        JList<String> listAvailableThemes = WindowHelper.getList(ModManager.genreMod.getAnalyzer().getContentByAlphabet(), true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableThemes);

        Object[] params = {labelModName, textFieldName, labelExplainList, scrollPaneAvailableGenres};
        while(true){
            if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                String newModName = textFieldName.getText();
                if(!newModName.isEmpty()){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        boolean modAlreadyExists = false;
                        for(String string : ModManager.npcGamesMod.getBaseAnalyzer().getContentByAlphabet()){
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
                                genreIds.add(ModManager.genreMod.getAnalyzer().getContentIdByName(string));
                            }
                            Collections.sort(genreIds);
                            for(Integer integer : genreIds){
                                newModLine.append("<").append(integer).append(">");
                            }
                            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(newModLine.toString()), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                try {
                                    Backup.createBackup(getGameFile());
                                    ModManager.npcGamesMod.getBaseEditor().addMod(newModLine.toString());
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
        if (t instanceof String) {
            StringBuilder message = new StringBuilder();
            StringBuilder name = new StringBuilder();
            StringBuilder genreIdsRaw = new StringBuilder();
            StringBuilder genreNamesToDisplay = new StringBuilder();
            ArrayList<Integer> genreIds = new ArrayList<>();
            message.append("<html>");
            boolean nameComplete = false;
            String line = (String) t;
            for(Character character : line.toCharArray()){
                if(!nameComplete){
                    if(character.toString().equals("<")){
                        nameComplete = true;
                        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(name).append("<br>");
                    }
                }
                if(!nameComplete){
                    name.append(character);
                }else{
                    genreIdsRaw.append(character);
                }
            }
            ArrayList<String> genreIdsAsString = Utils.getEntriesFromString(genreIdsRaw.toString());
            for(String string : genreIdsAsString){
                try{
                    genreIds.add(Integer.parseInt(string));
                }catch(NumberFormatException ignored){

                }
            }
            int currentInt = 0;
            boolean firstInt = true;
            for(Integer integer : genreIds){
                if(firstInt){
                    firstInt = false;
                }else{
                    genreNamesToDisplay.append(", ");
                }
                if(currentInt == 8){
                    genreNamesToDisplay.append("<br>");
                    currentInt = 0;
                }
                genreNamesToDisplay.append(ModManager.genreMod.getBaseAnalyzer().getContentNameById(integer));
                currentInt++;
            }
            message.append(ModManager.genreMod.getTypePlural()).append(": ").append(genreNamesToDisplay);
            return message.toString();
        } else {
            throw new ModProcessingException("T is invalid: Should be String", true);
        }
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
        return "TEST_SIMPLE_MOD";
    }

    @Override
    public String getImportExportFileName() {
        return "testSimpleMod.txt";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{"v2.2.3-dev", MadGamesTycoon2ModTool.VERSION};
    }

    @Override
    protected String getExportFolder() {
        return "TEST SIMPLE MOD";
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
}
