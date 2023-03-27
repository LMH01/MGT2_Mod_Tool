package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.NpcGame;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NpcGameManager extends AbstractSimpleContentManager implements DependentContentManager {

    public static final NpcGameManager INSTANCE = new NpcGameManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", MadGamesTycoon2ModTool.VERSION};

    private NpcGameManager() {
        super("npcGames", "npc_game", MGT2Paths.TEXT_DATA.getPath().resolve("NpcGames.txt").toFile(), StandardCharsets.UTF_16LE);
    }

    @Override
    protected String getReplacedLine(String line) {
        StringBuilder outputString = new StringBuilder();
        boolean nameComplete = false;
        for (Character character : line.toCharArray()) {
            if (character.toString().equals("<")) {
                nameComplete = true;
            }
            if (!nameComplete) {
                outputString.append(character);
            }
        }
        return outputString.toString().trim();
    }

    @Override
    protected String isLineValid(String line) {
        try {
            Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(line));
        } catch (NumberFormatException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcGameInvalid.formatInvalid"), line, e.getMessage());
        }
        if (Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(line)).isEmpty()) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcGameInvalid.noGenreIds"), line);
        }
        return "";
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JLabel labelModName = new JLabel(I18n.INSTANCE.get("mod.npcGames.addMod.components.labelName"));
        JTextField textFieldName = new JTextField();

        JLabel labelExplainList = new JLabel("<html>" + I18n.INSTANCE.get("mod.npcGames.addMod.selectGenres") + "<br>" + I18n.INSTANCE.get("commonText.scrollExplanation"));
        JList<String> listAvailableThemes = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), true);
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
                            ArrayList<Integer> genreIds = new ArrayList<>();
                            for (String string : listAvailableThemes.getSelectedValuesList()) {
                                genreIds.add(GenreManager.INSTANCE.getContentIdByName(string));
                            }
                            Collections.sort(genreIds);
                            NpcGame npcGame = new NpcGame(textFieldName.getText(), null, genreIds);
                            if (JOptionPane.showConfirmDialog(null, npcGame.getOptionPaneMessage(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                addContent(npcGame);
                                ContentAdministrator.modAdded(npcGame.name, getType());
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
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        String data = getDataForName(name);
        return new NpcGame(name, getContentIdByName(name), Utils.transformStringArrayToIntegerArray(Utils.getEntriesFromString(data)));
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        return new NpcGame((String) map.get("NAME EN"), null, SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String) map.get("GENRES")));
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "GENRES");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> dependencies = new ArrayList<>();
        dependencies.add(GenreManager.INSTANCE);
        return dependencies;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put(GenreManager.INSTANCE.getId(), Utils.getEntriesFromString((String)importMap.get("GENRES")));
        return dependencies;
    }

    /**
     * Modifies the NpcGames.txt file to include/remove a specified genre id
     *
     * @param genreID    The genre id that should be added/removed.
     * @param addGenreID If true the genre will be added to the list. If false the genre will be removed from the list.
     * @param chance     The chance with which the genre should be added to the npc game list 100 = 100%
     */
    public void editNPCGames(int genreID, boolean addGenreID, int chance) throws ModProcessingException {
        try {
            File fileNpcGamesTemp = MGT2Paths.TEXT_DATA.getPath().resolve("NpcGames.txt.temp").toFile();
            Files.createFile(fileNpcGamesTemp.toPath());
            Backup.createBackup(NpcGameManager.INSTANCE.getGameFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(NpcGameManager.INSTANCE.getGameFile()), StandardCharsets.UTF_16LE));
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
            Files.delete(NpcGameManager.INSTANCE.getGameFile().toPath());
            Files.move(fileNpcGamesTemp.toPath(), NpcGameManager.INSTANCE.getGameFile().toPath());
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing npcGames.txt file", e);
        }
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
