package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.instances.Publisher;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.types.CountryType;
import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.content.managed.types.SpinnerType;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Months;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PublisherManager extends AbstractAdvancedContentManager implements DependentContentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherManager.class);

    public static final PublisherManager INSTANCE = new PublisherManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", MadGamesTycoon2ModTool.VERSION};

    public static final Path defaultPublisherIcon = MGT2Paths.COMPANY_ICONS.getPath().resolve("87.png");

    private PublisherManager() {
        super("publisher", "publisher", MGT2Paths.TEXT_DATA.getPath().resolve("Publisher.txt").toFile(), StandardCharsets.UTF_8);
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) {
        Boolean exclusive = null;
        if (map.containsKey("EXCLUSIVE") && map.get("EXCLUSIVE").equals("true")) {
            exclusive = true;
        }
        return new Publisher(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                map.get("DATE"),
                new Image(MGT2Paths.COMPANY_ICONS.getPath().resolve(map.get("PIC") + ".png").toFile()),
                Boolean.parseBoolean(map.get("DEVELOPER")),
                Boolean.parseBoolean(map.get("PUBLISHER")),
                Integer.parseInt(map.get("MARKET")),
                Integer.parseInt(map.get("SHARE")),
                Integer.parseInt(map.get("GENRE")),
                map.containsKey("ONLYMOBILE"),
                Integer.parseInt(map.get("SPEED")),
                Integer.parseInt(map.get("COMVAL")),
                map.containsKey("NOTFORSALE"),
                CountryType.getFromId(Integer.parseInt(map.get("COUNTRY"))),
                exclusive);
    }

    @Override
    protected List<DataLine> getDataLines() {
        List<DataLine> list = new ArrayList<>();
        list.add(new DataLine("DATE", true, DataType.STRING));
        list.add(new DataLine("PIC", true, DataType.INT));
        list.add(new DataLine("DEVELOPER", true, DataType.BOOLEAN));
        list.add(new DataLine("PUBLISHER", true, DataType.BOOLEAN));
        list.add(new DataLine("MARKET", true, DataType.INT));
        list.add(new DataLine("SHARE", true, DataType.INT));
        list.add(new DataLine("GENRE", true, DataType.INT));
        list.add(new DataLine("ONLYMOBILE", false, DataType.BOOLEAN));
        list.add(new DataLine("SPEED", true, DataType.INT));
        list.add(new DataLine("COMVAL", true, DataType.INT));
        list.add(new DataLine("NOTFORSALE", false, DataType.BOOLEAN));
        list.add(new DataLine("COUNTRY", true, DataType.INT));
        list.add(new DataLine("EXCLUSIVE", false, DataType.BOOLEAN));
        return list;
    }

    @Override
    protected String analyzeSpecialCases(Map<String, String> map) {
        // Check if platform picture is valid
        if (map.containsKey("PIC")) {
            File file = MGT2Paths.COMPANY_ICONS.getPath().resolve(map.get("PIC") + ".png").toFile();
            if (!file.exists()) {
                return String.format(I18n.INSTANCE.get("verifyContentIntegrity.pictureNotFound") + "\n", gameFile.getName(), getType(), map.get("NAME EN"), file.getName(), MGT2Paths.COMPANY_ICONS.getPath());
            }
        }
        return "";
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Map<String, String> transformedMap = Utils.transformObjectMapToStringMap(map);
        Boolean exclusive = null;
        if (transformedMap.containsKey("EXCLUSIVE") && transformedMap.get("EXCLUSIVE").equals("true")) {
            exclusive = true;
        }
        return new Publisher(
                (String) map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManager(map),
                (String) map.get("DATE"),
                new Image(assetsFolder.resolve(getImportImageName("iconName", transformedMap, "publisher_icon.png", (String) map.get("NAME EN"))).toFile(), MGT2Paths.COMPANY_ICONS.getPath().resolve(String.valueOf(CompanyLogoAnalyzer.getFreeLogoNumber() + ".png")).toFile()),
                Boolean.parseBoolean((String) map.get("DEVELOPER")),
                Boolean.parseBoolean((String) map.get("PUBLISHER")),
                Integer.parseInt((String) map.get("MARKET")),
                Integer.parseInt((String) map.get("SHARE")),
                GenreManager.INSTANCE.getImportHelperMap().getContentIdByName((String) map.get("GENRE")),
                map.containsKey("ONLYMOBILE") && map.get("ONLYMOBILE").equals("true"),
                Integer.parseInt((String) map.get("SPEED")),
                Integer.parseInt((String) map.get("COMVAL")),
                map.containsKey("NOTFORSALE") && map.get("NOTFORSALE").equals("true"),
                CountryType.getFromId(Integer.parseInt(transformedMap.get("COUNTRY"))),
                exclusive);
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterName"));

        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();

        AtomicReference<Path> publisherImageFilePath = new AtomicReference<>(defaultPublisherIcon);
        JPanel panelPublisherIcon = new JPanel();
        JLabel labelPublisherIcon = new JLabel(I18n.INSTANCE.get("mod.publisher.icon") + ":");
        JButton buttonBrowseIcon = new JButton(I18n.INSTANCE.get("commonText.browse"));
        buttonBrowseIcon.addActionListener(actionEvent -> {
            try {
                Path imageFilePath = Utils.getImagePath();
                if (imageFilePath.toFile().exists()) {
                    publisherImageFilePath.set(imageFilePath);
                }
            } catch (ModProcessingException e) {
                publisherImageFilePath.set(defaultPublisherIcon);
                e.printStackTrace();
            }
        });
        panelPublisherIcon.add(labelPublisherIcon);
        panelPublisherIcon.add(buttonBrowseIcon);

        JCheckBox checkBoxIsDeveloper = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer"));
        checkBoxIsDeveloper.setSelected(true);
        checkBoxIsDeveloper.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer.toolTip"));

        JCheckBox checkBoxIsPublisher = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher"));
        checkBoxIsPublisher.setSelected(true);
        checkBoxIsPublisher.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher.toolTip"));

        JSpinner spinnerMarketShare = WindowHelper.getBaseSpinner("mod.publisher.addMod.optionPaneMessage.spinner.marketShare.toolTip", 50, 1, 100, 1);
        JSpinner spinnerShare = WindowHelper.getBaseSpinner("mod.publisher.addMod.optionPaneMessage.spinner.share.toolTip", 5, 0, 10, 1);

        AtomicInteger genreID = new AtomicInteger();
        JPanel panelGenre = new JPanel();
        JLabel labelGenre = new JLabel(I18n.INSTANCE.get("commonText.fanBase") + ":");
        JButton buttonSelectGenre = new JButton("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        ");
        buttonSelectGenre.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.toolTip"));
        buttonSelectGenre.addActionListener(actionEvent -> {
            try {
                GenreManager.INSTANCE.analyzeFile();
                JLabel labelChooseGenre = new JLabel(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select"));
                ArrayList<String> availableGenres = new ArrayList<>();
                String[] string;
                string = GenreManager.INSTANCE.getContentByAlphabet();
                for (String string1 : string) {
                    Map<String, String> genreMap = GenreManager.INSTANCE.getSingleContentMap(string1);
                    int genreDate = Integer.parseInt(genreMap.get("DATE").replaceAll("[^0-9]", ""));
                    if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) > genreDate) {
                        availableGenres.add(string1);
                    } else if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) == genreDate) {
                        if (Months.getIdByName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString().replaceAll("[0-9]", "").trim()) >= Months.getIdByName(genreMap.get("DATE").replaceAll("[0-9]", "").trim())) {
                            availableGenres.add(string1);
                        }
                    }
                }
                String[] availableGenresString = new String[availableGenres.size()];
                availableGenres.toArray(availableGenresString);
                JList<String> listAvailableGenres = new JList<>(availableGenresString);
                listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                listAvailableGenres.setVisibleRowCount(-1);
                JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                scrollPaneAvailableGenres.setPreferredSize(new Dimension(315, 140));

                Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.selectGenre"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    if (!listAvailableGenres.isSelectionEmpty()) {
                        String currentGenre = listAvailableGenres.getSelectedValue();
                        genreID.set(GenreManager.INSTANCE.getContentIdByName(currentGenre));
                        buttonSelectGenre.setText(currentGenre);
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select.genreFailure"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (ModProcessingException e) {
                ContentAdministrator.showException(e);
            }
        });
        panelGenre.add(labelGenre);
        panelGenre.add(buttonSelectGenre);

        comboBoxUnlockMonth.addActionListener(e -> buttonSelectGenre.setText("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        "));

        spinnerUnlockYear.addChangeListener(e -> buttonSelectGenre.setText("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        "));

        JCheckBox notForSale = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.notForSale"));
        notForSale.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.notForSale.toolTip"));

        JSpinner speed = WindowHelper.getBaseSpinner("mod.publisher.addMod.spinner.speed.toolTip", 3, 0, 5, 1);

        JSpinner comVal = WindowHelper.getBaseSpinner("mod.publisher.addMod.spinner.comVal.toolTip", 10000000, 2000000, 40000000, 200000);

        JCheckBox onlyMobile = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.onlyMobile"));
        onlyMobile.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.onlyMobile.toolTip"));

        JLabel countryLabel = new JLabel(I18n.INSTANCE.get("commonText.country") + ": ");
        JComboBox<String> country = WindowHelper.getComboBox(CountryType.class, "mod.platform.addPlatform.components.comboBox.country.toolTip", CountryType.USA.getTypeName());
        JPanel countryPanel = new JPanel();
        countryPanel.add(countryLabel);
        countryPanel.add(country);

        Object[] params = {WindowHelper.getNamePanel(textFieldName), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, WindowHelper.getSpinnerPanel(spinnerMarketShare, SpinnerType.MARKET_SHARE), WindowHelper.getSpinnerPanel(spinnerShare, SpinnerType.PROFIT_SHARE), panelGenre, WindowHelper.getSpinnerPanel(speed, "mod.publisher.addMod.spinner.speed"), WindowHelper.getSpinnerPanel(comVal, "mod.publisher.addMod.spinner.comVal"), notForSale, onlyMobile, countryPanel};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                boolean publisherAlreadyExists = false;
                for (String string : getContentByAlphabet()) {
                    if (textFieldName.getText().equals(string)) {
                        publisherAlreadyExists = true;
                    }
                }
                if (publisherAlreadyExists) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (textFieldName.getText().equals("") || textFieldName.getText().equals(I18n.INSTANCE.get("commonText.enterName"))) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), "", JOptionPane.INFORMATION_MESSAGE);
                    textFieldName.setText(I18n.INSTANCE.get("commonText.enterName"));
                } else if (buttonSelectGenre.getText().equals("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        ")) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.selectGenreFirst"), "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
                    int logoId;
                    if (publisherImageFilePath.get().equals(defaultPublisherIcon)) {
                        logoId = 87;
                    } else {
                        CompanyLogoAnalyzer.analyzeLogoNumbers();
                        logoId = CompanyLogoAnalyzer.getFreeLogoNumber();
                    }
                    Publisher publisher = new Publisher(
                            textFieldName.getText(),
                            null,
                            new TranslationManager(),
                            Months.getDataNameByTypeName(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) + " " + spinnerUnlockYear.getValue().toString(),
                            new Image(publisherImageFilePath.get().toFile(), MGT2Paths.COMPANY_ICONS.getPath().resolve(logoId + ".png").toFile()),
                            checkBoxIsDeveloper.isSelected(),
                            checkBoxIsPublisher.isSelected(),
                            Integer.parseInt(spinnerMarketShare.getValue().toString()),
                            Integer.parseInt(spinnerShare.getValue().toString()),
                            genreID.get(),
                            onlyMobile.isSelected(),
                            Integer.parseInt(speed.getValue().toString()),
                            Integer.parseInt(comVal.getValue().toString()),
                            notForSale.isSelected(),
                            CountryType.getFromName(Objects.requireNonNull(country.getSelectedItem()).toString()),
                            null
                    );
                    if (JOptionPane.showConfirmDialog(null, publisher.getOptionPaneMessage(), I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION) {
                        addContent(publisher);
                        ContentAdministrator.modAdded(publisher.name, getType());
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "GENRE");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> map) {
        Map<String, Object> dependencies = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add((String)map.get("GENRE"));
        dependencies.put(GenreManager.INSTANCE.getId(), genres);
        return dependencies;
    }

    /**
     * Removes the specified genre from the publisher file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     *
     * @param name The genre that should be removed
     * @throws ModProcessingException When something went wrong
     */
    public static void removeGenre(String name) throws ModProcessingException {
        try {
            int genreId = GenreManager.INSTANCE.getContentIdByName(name);
            PublisherManager.INSTANCE.analyzeFile();
            DebugHelper.debug(LOGGER, "Replacing genre id in publisher file: " + name);
            Charset charset = PublisherManager.INSTANCE.getCharset();
            File fileToEdit = PublisherManager.INSTANCE.getGameFile();
            if (Files.exists(fileToEdit.toPath())) {
                Files.delete(fileToEdit.toPath());
            }
            Files.createFile(fileToEdit.toPath());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
            if (charset.equals(StandardCharsets.UTF_8)) {
                bw.write("\ufeff");
            }
            for (Map<String, String> fileContent : PublisherManager.INSTANCE.fileContent) {
                try {
                    if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                        fileContent.remove("GENRE");
                        fileContent.put("GENRE", Integer.toString(GenreManager.INSTANCE.getActiveIds().get(Utils.getRandomNumber(0, GenreManager.INSTANCE.getActiveIds().size()))));
                    }
                } catch (NumberFormatException e) {
                    bw.close();
                    throw new ModProcessingException("Genre can not be removed from publisher file", e);
                }
                PublisherManager.INSTANCE.printValues(fileContent, bw);
                bw.write("\r\n");
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing the game file for mod " + INSTANCE.getType(), e);
        }
    }
}
