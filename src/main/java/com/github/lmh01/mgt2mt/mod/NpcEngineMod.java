package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class NpcEngineMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcEngineMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID",map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        EditHelper.printLine("PLATFORM", map, bw);
        EditHelper.printLine("PRICE", map, bw);
        EditHelper.printLine("SHARE", map, bw);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "npcEngine";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.npcEngineMod;
    }

    @Override
    public File getGameFile() {
        return new File(Utils.getMGT2DataPath() + "NpcEngines.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_npc_engines.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        try{
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"));
            final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
            AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
            JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(mapNameTranslations, nameTranslationsAdded, 0);
            JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();
            JSpinner spinnerShare = WindowHelper.getProfitShareSpinner();
            JSpinner spinnerCost = WindowHelper.getCostSpinner();

            JLabel labelExplainGenreList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectGenre") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
            JList<String> listAvailableGenres = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), true);
            JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGenres);

            JLabel labelExplainPlatformList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcEngine.addMod.components.selectPlatform"));
            JList<String> listAvailablePlatforms = WindowHelper.getList(ModManager.platformMod.getContentByAlphabet(), false);
            JScrollPane scrollPaneAvailablePlatforms = WindowHelper.getScrollPane(listAvailablePlatforms);

            Object[] param = {WindowHelper.getNamePanel(this, textFieldName), buttonAddNameTranslations, WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), WindowHelper.getSpinnerPanel(spinnerShare, 9), WindowHelper.getSpinnerPanel(spinnerCost, 8), labelExplainGenreList, scrollPaneAvailableGenres, labelExplainPlatformList, scrollPaneAvailablePlatforms};
            while(true){
                if(JOptionPane.showConfirmDialog(null, param, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION){
                    if(!textFieldName.getText().equals(I18n.INSTANCE.get("mod.npcEngine.addMod.components.textFieldName.initialValue"))){
                        if(!npcEngineNamesWithoutGenre().contains(textFieldName.getText())){
                            if(!listAvailablePlatforms.getSelectedValuesList().isEmpty()){
                                if(!listAvailableGenres.getSelectedValuesList().isEmpty()){
                                    //Filling base map (Without genre, id and names)
                                    Map<String, String> npcEngine = new HashMap<>();
                                    npcEngine.put("DATE", Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()) + " " + spinnerUnlockYear.getValue().toString());
                                    npcEngine.put("PLATFORM", Integer.toString(ModManager.platformMod.getContentIdByName(listAvailablePlatforms.getSelectedValue())));
                                    npcEngine.put("PRICE", spinnerCost.getValue().toString());
                                    npcEngine.put("SHARE", spinnerShare.getValue().toString());
                                    if(listAvailableGenres.getSelectedValuesList().size() > 1){
                                        npcEngine.put("GENRE", "MULTIPLE SELECTED");
                                        npcEngine.put("NAME EN", textFieldName.getText());
                                        sendLogMessage("Multiple genres have been selected. Displaying multiple engines dialog.");
                                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres") + "<br><br>" + getOptionPaneMessage(npcEngine) + "<br><br>" + I18n.INSTANCE.get("commonText.isThisCorrect"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                            npcEngine.remove("NAME EN");
                                            npcEngine.remove("GENRE");
                                            Backup.createBackup(getGameFile());
                                            ProgressBarHelper.initializeProgressBar(0, listAvailableGenres.getSelectedValuesList().size(), I18n.INSTANCE.get(""));
                                            for(String string : listAvailableGenres.getSelectedValuesList()){
                                                analyzeFile();
                                                npcEngine.put("ID", Integer.toString(getFreeId()));
                                                Map<String, String> newNpcEngineMap = new HashMap<>();
                                                if(!nameTranslationsAdded.get()){
                                                    newNpcEngineMap.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText() + " [" + string + "]"));
                                                }else{
                                                    Map<String, String> newTranslations = new HashMap<>();
                                                    for(Map.Entry<String, String> entry : mapNameTranslations[0].entrySet()){
                                                        newTranslations.put(entry.getKey(), entry.getValue() + " [" + string + "]");
                                                    }
                                                    newNpcEngineMap.putAll(TranslationManager.transformTranslationMap(newTranslations, "NAME"));
                                                    newNpcEngineMap.put("NAME EN", textFieldName.getText() + " [" + string + "]");
                                                }
                                                newNpcEngineMap.putAll(npcEngine);
                                                newNpcEngineMap.put("GENRE", Integer.toString(ModManager.genreMod.getContentIdByName(string)));
                                                addMod(newNpcEngineMap);
                                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase") + " - " + newNpcEngineMap.get("NAME EN"));
                                                ProgressBarHelper.increment();
                                            }
                                            npcEngine.put("NAME EN", textFieldName.getText());
                                            JOptionPane.showMessageDialog(null, listAvailableGenres.getSelectedValuesList().size() + " " + I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.multipleGenres.finished"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                            ProgressBarHelper.resetProgressBar();
                                            break;
                                        }
                                    }else{
                                        npcEngine.put("ID", Integer.toString(getFreeId()));
                                        if(!nameTranslationsAdded.get()){
                                            npcEngine.putAll(TranslationManager.getDefaultNameTranslations(textFieldName.getText()));
                                        }else{
                                            npcEngine.putAll(TranslationManager.transformTranslationMap(mapNameTranslations[0], "NAME"));
                                            npcEngine.put("NAME EN", textFieldName.getText());
                                        }
                                        npcEngine.put("GENRE", Integer.toString(ModManager.genreMod.getContentIdByName(listAvailableGenres.getSelectedValue())));
                                        sendLogMessage("Only a single genre has been selected. Displaying single engine dialog");
                                        if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(npcEngine), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                            createBackup();
                                            addMod(npcEngine);
                                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase") + " - " + npcEngine.get("NAME EN"));
                                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.npcEngine.upperCase") + ": [" + npcEngine.get("NAME EN") + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("commonText.npcEngine.upperCase"), JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        }
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.selectGenreFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.npcEngine.addMod.noPlatformSelected"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    break;
                }
            }
        }catch(IOException e){
            throw new ModProcessingException(I18n.INSTANCE.get("commonText.unableToAdd") + getType() + " - " + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> getChangedImportMap(Map<String, String> map) throws ModProcessingException {
        int genreId = ModManager.genreMod.getContentIdByName(map.get("GENRE"));
        if(genreId == -1){
            map.replace("GENRE", Integer.toString(Utils.getRandomNumber(0, ModManager.genreMod.getFileContent().size()-1)));
        }else{
            map.replace("GENRE", Integer.toString(genreId));
        }
        int platformId = ModManager.platformMod.getContentIdByName(map.get("PLATFORM"));
        if(platformId == -1){
            map.replace("PLATFORM", Integer.toString(Utils.getRandomNumber(0, ModManager.platformMod.getFileContent().size()-1)));
        }else{
            map.replace("PLATFORM", Integer.toString(platformId));
        }
        return super.getChangedImportMap(map);
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        StringBuilder message = new StringBuilder();
        message.append("<html>");
        message.append(I18n.INSTANCE.get("mod.npcEngine.addMod.optionPaneMessage.firstPart")).append("<br><br>");
        message.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(map.get("NAME EN")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.unlockDate")).append(": ").append(map.get("DATE")).append("<br>");
        if(map.get("GENRE").equals("MULTIPLE SELECTED")){
            message.append(I18n.INSTANCE.get("commonText.genre.upperCase")).append(": ").append((map.get("GENRE"))).append("<br>");
        }else{
            message.append(I18n.INSTANCE.get("commonText.genre.upperCase")).append(": ").append(ModManager.genreMod.getContentNameById(Integer.parseInt(map.get("GENRE")))).append("<br>");
        }
        message.append(I18n.INSTANCE.get("commonText.platform.upperCase")).append(": ").append(ModManager.platformMod.getContentNameById(Integer.parseInt(map.get("PLATFORM")))).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.price")).append(": ").append(map.get("PRICE")).append("<br>");
        message.append(I18n.INSTANCE.get("commonText.profitShare")).append(": ").append(map.get("SHARE")).append("<br>");
        return message.toString();
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public String getTypeCaps() {
        return "NPC_ENGINE";
    }

    @Override
    public String getImportExportFileName() {
        return "npcEngine.txt";
    }

    /**
     * @return An ArrayList containing the engine names without the genre names
     */
    private ArrayList<String> npcEngineNamesWithoutGenre() throws ModProcessingException {
        ArrayList<String> strings = new ArrayList<>();
        for(String string : getContentByAlphabet()){
            for(String string2 : ModManager.genreMod.getContentByAlphabet()){
                strings.add(string.replace(" [" + string2 + "]", ""));
            }
        }
        return strings;
    }

    /**
     * Removes the specified genre from the npc engine file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     * @param name The name of the genre that should be removed
     */
    public void removeGenre(String name) throws ModProcessingException {
        try {
            int genreId = ModManager.genreMod.getContentIdByName(name);
            analyzeFile();
            sendLogMessage("Replacing genre id in npc engine file: " + name);
            Charset charset = getCharset();
            File fileToEdit = getGameFile();
            if(fileToEdit.exists()){
                fileToEdit.delete();
            }
            fileToEdit.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if(charset.equals(StandardCharsets.UTF_8)){
                bw.write("\ufeff");
            }
            for(Map<String, String> fileContent : getFileContent()){
                if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                    fileContent.remove("GENRE");
                    fileContent.put("GENRE", Integer.toString(ModManager.genreMod.getActiveIds().get(Utils.getRandomNumber(0, ModManager.genreMod.getActiveIds().size()))));
                }
                printValues(fileContent, bw);
                bw.write(System.getProperty("line.separator"));
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing game file for mod " + getType() + ": " + e.getMessage());
        }
    }
}
