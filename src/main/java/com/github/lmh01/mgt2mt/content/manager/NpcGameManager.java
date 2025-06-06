package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.Version;
import com.github.lmh01.mgt2mt.content.instances.NpcGame;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.SequelNumeration;
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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NpcGameManager extends AbstractSimpleContentManager implements DependentContentManager {

    public static final NpcGameManager INSTANCE = new NpcGameManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", "5.0.0-beta1", "5.0.0", "5.0.1", "5.1.0", Version.getVersion()};

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
            for (String entry : Utils.getEntriesFromString(line)) {
                if (entry.startsWith("TG")) {
                    TargetGroup.getTargetGroupById(Integer.parseInt(entry.replace("TG", "")));
                } else if (entry.startsWith("ST")) {
                    Integer.parseInt(entry.replace("ST", ""));
                } else if (entry.startsWith("T")) {
                    Integer.parseInt(entry.replace("T", ""));
                } else if (!entry.equals("ROM") && !entry.equals("NOSPIN") && !entry.equals("ARA")) {
                    Integer.parseInt(entry);
                }
            }
        } catch (IllegalArgumentException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.npcGameInvalid.formatInvalid"), line, e.getMessage());
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
                            NpcGame npcGame = new NpcGame(textFieldName.getText(), null, genreIds ,null, null, null, null, null);//TODO Add collection of remaining values
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
        ArrayList<Integer> genreIds = new ArrayList<>();
        Integer theme = null;
        Integer subTheme = null;
        TargetGroup tg = null;
        Boolean noSpin = false;
        SequelNumeration sn = SequelNumeration.NONE;
        for (String entry : Utils.getEntriesFromString(data)) {
            if (entry.startsWith("TG")) {
                tg = TargetGroup.getTargetGroupById(Integer.parseInt(entry.replace("TG", "")));
            } else if (entry.startsWith("ST")) {
                subTheme = Integer.parseInt(entry.replace("ST", ""));
            } else if (entry.startsWith("T")) {
                theme = Integer.parseInt(entry.replace("T", ""));
            } else if (entry.equals("ROM")) {
                sn = SequelNumeration.ROM;
            } else if (entry.equals("ARA")) {
                sn = SequelNumeration.ARA;
            } else if (entry.equals("NOSPIN")) {
                noSpin = true;
            } else {
                genreIds.add(Integer.parseInt(entry));
            }
        }
        return new NpcGame(name, getContentIdByName(name), genreIds, theme, subTheme, tg, sn, noSpin);
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Integer themeId = null;
        Integer subThemeId = null;
        TargetGroup tg = null;
        SequelNumeration sn = null;
        if (map.containsKey("THEME")) {
            themeId = SharingHelper.getContentIdByNameFromImport(ThemeManager.INSTANCE, map.get("THEME").toString());
        }
        if (map.containsKey("SUB_THEME")) {
            subThemeId = SharingHelper.getContentIdByNameFromImport(ThemeManager.INSTANCE, map.get("SUB_THEME").toString());
        }
        if (map.containsKey("TARGET_GROUP")) {
            tg = TargetGroup.getTargetGroup(map.get("TARGET_GROUP").toString());
        }
        if (map.containsKey("SEQUEL_NUMERATION")) {
            sn = SequelNumeration.getSequelNumeration(map.get("SEQUEL_NUMERATION").toString());
        }
        return new NpcGame((String) map.get("NAME EN"), null, SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String) map.get("GENRES")),
            themeId,
            subThemeId,
            tg,
            sn,
            SharingHelper.getBoolean(map.get("NOSPIN")));
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
            Path fileNpcGamesTemp = MGT2Paths.TEXT_DATA.getPath().resolve("NpcGames.txt.temp");
            if (Files.exists(fileNpcGamesTemp, LinkOption.NOFOLLOW_LINKS)) {
                Files.delete(fileNpcGamesTemp);
            }
            Files.createFile(fileNpcGamesTemp);
            Backup.createBackup(NpcGameManager.INSTANCE.getGameFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(NpcGameManager.INSTANCE.getGameFile()), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNpcGamesTemp.toFile()), StandardCharsets.UTF_8));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (addGenreID) {
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
                    if (randomNum > (100 - chance)) {
                        if (!currentLine.contains(new StringBuilder("<").append(genreID).append(">").toString())) {
                            // only add genre id to the line when the id is not already contained in the line
                            bw.write(currentLine + "<" + genreID + ">" + "\r\n");
                        }
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
            Files.move(fileNpcGamesTemp, NpcGameManager.INSTANCE.getGameFile().toPath());
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing npcGames.txt file", e);
        }
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
