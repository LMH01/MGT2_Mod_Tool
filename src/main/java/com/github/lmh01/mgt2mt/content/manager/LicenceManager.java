package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.Licence;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class LicenceManager extends AbstractSimpleContentManager implements DependentContentManager {

    public static final LicenceManager INSTANCE = new LicenceManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", MadGamesTycoon2ModTool.VERSION};

    private LicenceManager() {
        super("licence", "licence", MGT2Paths.TEXT_DATA.getPath().resolve("Licence.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    protected String getReplacedLine(String line) {
        return Utils.getFirstPart(line).trim();
    }

    @Override
    protected String isLineValid(String line) {
        int availableTags = 0;
        if (line.contains("<BOOK>")) {
            availableTags++;
        }
        if (line.contains("<MOVIE>")) {
            availableTags++;
        }
        if (line.contains("<SPORT>")) {
            availableTags++;
        }
        if (line.contains("<COMIC>")) {
            availableTags++;
        }
        if (line.contains("<TOY>")) {
            availableTags++;
        }
        if (line.contains("<BOARD>")) {
            availableTags++;
        }
        if (availableTags < 1) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.licenceInvalid.tagMissing"), getReplacedLine(line));
        } else if (availableTags > 1) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.licenceInvalid.tooManyTags"), getReplacedLine(line));
        }
        if (line.contains("G+") || line.contains("G-")) {
            ArrayList<String> ids = Utils.getEntriesFromString(line);
            for (String string : ids) {
                if (string.startsWith("G")) {
                    if (string.charAt(1) == '+') {
                        try {
                            Integer.parseInt(string.replace("G", "").replace("+", ""));
                        } catch (NumberFormatException e) {
                            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.licenceInvalid.goodGenreIdNotNumber"), getReplacedLine(line), string.replace("G", "").replace("+", ""));
                        }
                    } else if (string.charAt(1) == '-') {
                        try {
                            Integer.parseInt(string.replace("G", "").replace("-", ""));
                        } catch (NumberFormatException e) {
                            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.licenceInvalid.badGenreIdNotNumber"), getReplacedLine(line), string.replace("G", "").replace("-", ""));
                        }
                    }
                } else if (string.startsWith("Y")) {
                    try {
                        Integer.parseInt(string.replace("Y", ""));
                    } catch (NumberFormatException e) {
                        return String.format(I18n.INSTANCE.get("verifyContentIntegrity.licenceInvalid.yearNotNumber"), getReplacedLine(line), string.replace("Y", ""));
                    }
                }
            }
        }
        return "";
    }

    @Override
    public String[] getCustomContentString() throws ModProcessingException {
        List<String> custom = Arrays.stream(super.getCustomContentString()).unordered().collect(Collectors.toList());
        custom.remove("Chronicles of Nornio");
        return Utils.transformListToArray(custom);
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JLabel labelAddLicence = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
        JTextField textFieldName = new JTextField();

        JPanel panelType = new JPanel();
        JLabel labelType = new JLabel(I18n.INSTANCE.get("commonText.type") + ":");
        JComboBox<String> comboBoxType = WindowHelper.getComboBox(LicenceType.class, "commonText.type", "mod.licence.addMod.optionPaneMessage.movie");
        panelType.add(labelType);
        panelType.add(comboBoxType);

        JSpinner spinnerReleaseYear = WindowHelper.getUnlockYearSpinner();

        JLabel labelChooseBadGenre = new JLabel(I18n.INSTANCE.get("dialog.sharingHandler.licence.badGenreId") + ":");
        JList<String> listBadGenre = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), false);
        listBadGenre.setToolTipText(I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.badGenreId.tooltip"));
        JScrollPane scrollPaneBadGenre = WindowHelper.getScrollPane(listBadGenre);

        JLabel labelChooseGoodGenre = new JLabel(I18n.INSTANCE.get("dialog.sharingHandler.licence.goodGenreId") + ":");
        JList<String> listGoodGenre = WindowHelper.getList(GenreManager.INSTANCE.getContentByAlphabet(), false);
        listGoodGenre.setToolTipText(I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.goodGenreId.tooltip"));
        JScrollPane scrollPaneGoodGenre = WindowHelper.getScrollPane(listGoodGenre);

        Object[] params = {labelAddLicence, textFieldName, panelType, WindowHelper.getSpinnerPanel(spinnerReleaseYear, "commonText.releaseYear"),
                labelChooseBadGenre, scrollPaneBadGenre, labelChooseGoodGenre, scrollPaneGoodGenre};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (listBadGenre.getSelectedValue() != null && listGoodGenre.getSelectedValue() != null && listBadGenre.getSelectedValue().equals(listGoodGenre.getSelectedValue())) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage.badAndGoodGenreId"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!textFieldName.getText().isEmpty()) {
                    Integer badGenreId = null;
                    if (listBadGenre.getSelectedValue() != null) {
                        badGenreId = GenreManager.INSTANCE.getContentIdByName(listBadGenre.getSelectedValue());
                    }
                    Integer goodGenreId = null;
                    if (listGoodGenre.getSelectedValue() != null) {
                        goodGenreId = GenreManager.INSTANCE.getContentIdByName(listGoodGenre.getSelectedValue());
                    }
                    LicenceType lt = null;
                    for (LicenceType licenceType : LicenceType.values()) {
                        if (Objects.requireNonNull(comboBoxType.getSelectedItem()).toString().equals(licenceType.getTypeName())) {
                            lt = licenceType;
                        }
                    }
                    Licence licence = new Licence(textFieldName.getText(), null, lt,
                            badGenreId,
                            goodGenreId,
                            (Integer) spinnerReleaseYear.getValue());
                    boolean licenceAlreadyExists = false;
                    for (Map.Entry<Integer, String> entry : fileContent.entrySet()) {
                        if (entry.getValue().equals(licence.getLine())) {
                            licenceAlreadyExists = true;
                        }
                    }
                    if (!licenceAlreadyExists) {
                        if (JOptionPane.showConfirmDialog(null, licence.getOptionPaneMessage(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            addContent(licence);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.licence.upperCase") + ": [" + textFieldName.getText() + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("textArea.added") + " " + getType(), JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Integer badGenreId;
        if (map.get("BAD GENRE") == null) {
            badGenreId = null;
        } else {
            badGenreId = GenreManager.INSTANCE.getImportHelperMap().getContentIdByName((String) map.get("BAD GENRE"));
        }
        Integer goodGenreId;
        if (map.get("GOOD GENRE") == null) {
            goodGenreId = null;
        } else {
            goodGenreId = GenreManager.INSTANCE.getImportHelperMap().getContentIdByName((String) map.get("GOOD GENRE"));
        }
        Integer year = (Integer) map.get("year");
        if (map.get("RELEASE YEAR") == null) {
            year = null;
        }
        return new Licence((String) map.get("NAME EN"), null, LicenceType.getTypeByIdentifier((String) map.get("LICENCE TYP")), badGenreId, goodGenreId, year);
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        LicenceType licenceType = null;
        Integer goodGenreId = null;
        Integer badGenreId = null;
        Integer releaseYear = null;
        ArrayList<String> data = Utils.getEntriesFromString(fileContent.get(getContentIdByName(name)));
        for (String string : data) {
            if (string.startsWith("G")) {
                if (string.charAt(1) == '+') {
                    try {
                        goodGenreId = Integer.parseInt(string.replace("G", "").replace("+", ""));
                    } catch (NumberFormatException e) {
                        throw new ModProcessingException("Unable to construct content: Good genre ID is not a number!");
                    }
                } else if (string.charAt(1) == '-') {
                    try {
                        badGenreId = Integer.parseInt(string.replace("G", "").replace("-", ""));
                    } catch (NumberFormatException e) {
                        throw new ModProcessingException("Unable to construct content: Bad genre ID is not a number!");
                    }
                }
            } else if (string.startsWith("Y")) {
                try {
                    releaseYear = Integer.parseInt(string.replace("Y", ""));
                } catch (NumberFormatException e) {
                    throw new ModProcessingException("Unable to construct content: Release year is not a number!");
                }
            } else if (!string.contains("Q")) {
                licenceType = LicenceType.getTypeByIdentifier(string);
            }
        }
        if (licenceType == null) {
            throw new ModProcessingException("Unable to construct content: licence type not found!");
        }
        return new Licence(name, null, licenceType, badGenreId, goodGenreId, releaseYear);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "GOOD GENRE ID");
        replaceMapEntry(map, missingDependency, replacement, "BAD GENRE ID");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> arrayList = new ArrayList<>();
        arrayList.add(GenreManager.INSTANCE);
        return arrayList;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        if (importMap.containsKey("BAD GENRE")) {
            genres.add((String)importMap.get("BAD GENRE"));
        }
        if (importMap.containsKey("GOOD GENRE")) {
            genres.add((String)importMap.get("GOOD GENRE"));
        }
        map.put(GenreManager.INSTANCE.getId(), genres);
        return map;
    }
}
