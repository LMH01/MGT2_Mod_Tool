package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.mod.managed.*;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NpcGamesMod extends AbstractSimpleDependentMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(NpcGamesMod.class);

     @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "3.0.0-alpha-1"};
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
    public String getExportType() {
        return "npc_game";
    }

    @Override
    public String getGameFileName() {
        return "NpcGames.txt";
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
        JList<String> listAvailableThemes = WindowHelper.getList(ModManager.genreMod.getContentByAlphabet(), true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableThemes);

        Object[] params = {labelModName, textFieldName, labelExplainList, scrollPaneAvailableGenres};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String newModName = textFieldName.getText();
                if (!newModName.isEmpty()) {
                    if (listAvailableThemes.getSelectedValuesList().size() != 0) {
                        boolean modAlreadyExists = false;
                        for (String string : getContentByAlphabet()) {
                            if (newModName.equals(string)) {
                                modAlreadyExists = true;
                                break;
                            }
                        }
                        if (!modAlreadyExists) {
                            StringBuilder newModLine = new StringBuilder();
                            newModLine.append(newModName).append(" ");
                            ArrayList<Integer> genreIds = new ArrayList<>();
                            for (String string : listAvailableThemes.getSelectedValuesList()) {
                                genreIds.add(ModManager.genreMod.getContentIdByName(string));
                            }
                            Collections.sort(genreIds);
                            for (Integer integer : genreIds) {
                                newModLine.append("<").append(integer).append(">");
                            }
                            if (JOptionPane.showConfirmDialog(null, getOptionPaneMessage(newModLine.toString()), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                createBackup();
                                addModToFile(newModLine.toString());
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + getType() + " - " + newModName);
                                JOptionPane.showMessageDialog(null, getType() + " [" + newModName + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"));
                                break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.unableToAdd") + " " + getType() + " - " + I18n.INSTANCE.get("commonText.modAlreadyExists"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.chooseGenreFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        String line = (String) t;
        ArrayList<String> genreIdsString = Utils.getEntriesFromString(line);
        ArrayList<Integer> genreIds = new ArrayList<>();
        for (String string : genreIdsString) {
            LOGGER.info("String: " + string);
            genreIds.add(Integer.parseInt(string));
        }
        String name = Utils.getFirstPart(line);
        StringBuilder output = new StringBuilder();
        output.append(I18n.INSTANCE.get("commonText.name")).append(": ").append(name).append("\n").append("\n").append(I18n.INSTANCE.get("commonText.genre.upperCase.plural")).append(":").append("\n").append("\n");
        for (Integer integer : genreIds) {
            try {
                String genreName = ModManager.genreMod.getContentNameById(integer);
                if (!genreName.equals("null")) {
                    output.append(genreName).append("\n");
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
        return output.toString();
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "line");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.genreMod);
        return arrayList;
    }

    @Override
    public String getReplacedLine(String inputString) {
        StringBuilder outputString = new StringBuilder();
        boolean nameComplete = false;
        for (Character character : inputString.toCharArray()) {
            if (character.toString().equals("<")) {
                nameComplete = true;
            }
            if (!nameComplete) {
                outputString.append(character);
            }
        }
        return outputString.toString().trim();
    }

    /**
     * Modifies the NpcGames.txt file to include/remove a specified genre id
     *
     * @param genreID    The genre id that should be added/removed.
     * @param addGenreID If true the genre will be added to the list. If false the genre will be removed from the list.
     * @param chance     The chance with which the genre should be added to the npc game list 100 = 100%
     */
    public static void editNPCGames(int genreID, boolean addGenreID, int chance) throws ModProcessingException {
        try {
            File fileNpcGamesTemp = MGT2Paths.TEXT_DATA.getPath().resolve("NpcGames.txt.temp").toFile();
            fileNpcGamesTemp.createNewFile();
            Backup.createBackup(ModManager.npcGamesMod.getGameFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ModManager.npcGamesMod.getGameFile()), StandardCharsets.UTF_16LE));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNpcGamesTemp), StandardCharsets.UTF_16LE));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (addGenreID) {
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
                    if (randomNum > (100 - chance)) {
                        bw.write(currentLine + "<" + genreID + ">" + "\r\n");
                    } else {
                        bw.write(currentLine + "\r\n");
                    }
                } else {
                    bw.write(currentLine.replace("<" + genreID + ">", "") + "\r\n");
                }
            }
            br.close();
            bw.close();
            ModManager.npcGamesMod.getGameFile().delete();
            fileNpcGamesTemp.renameTo(ModManager.npcGamesMod.getGameFile());
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing npcGames.txt file", e);
        }
    }

    @Override
    public String getModifiedExportLine(String exportLine) throws ModProcessingException {
        ArrayList<String> strings = Utils.getEntriesFromString(exportLine);
        StringBuilder output = new StringBuilder();
        output.append(getReplacedLine(exportLine)).append(" ");
        for (String string : strings) {
            try {
                output.append("<").append(ModManager.genreMod.getContentNameById(Integer.parseInt(string))).append(">");
            } catch (NumberFormatException e) {
                LOGGER.info("unable to parse export line: " + exportLine);
                e.printStackTrace();
                return getReplacedLine(exportLine);
            }
        }
        return output.toString();
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>(Utils.getEntriesFromString(transformGenericToString(t)));
        map.put(ModManager.genreMod.getExportType(), genres);
        return map;
    }

    @Override
    public String getChangedImportLine(String importLine) throws ModProcessingException {
        try {
            ArrayList<String> strings = Utils.getEntriesFromString(importLine);
            StringBuilder output = new StringBuilder();
            output.append(getReplacedLine(importLine)).append(" ");
            for (String string : strings) {
                output.append("<").append(ModManager.genreMod.getModIdByNameFromImportHelperMap(string)).append(">");
            }
            return output.toString();
        } catch (NullPointerException e) {
            throw new ModProcessingException("Unable to change import line", e);
        }
    }
}
