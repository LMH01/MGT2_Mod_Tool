package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.Image;
import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.mod.managed.SpinnerType;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PublisherManager extends AbstractAdvancedContentManager implements DependentContentManager {

    public static final PublisherManager INSTANCE = new PublisherManager();

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private PublisherManager() {
        super("publisher", "publisher", "default_publisher.txt", MGT2Paths.TEXT_DATA.getPath().resolve("Publisher.txt").toFile(), StandardCharsets.UTF_8, compatibleModToolVersions);
    }

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManagerNew.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("DEVELOPER", map, bw);
        EditHelper.printLine("PUBLISHER", map, bw);
        EditHelper.printLine("MARKET", map, bw);
        EditHelper.printLine("SHARE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        EditHelper.printLine("ONLYMOBILE", map, bw);
        EditHelper.printLine("SPEED", map, bw);
        EditHelper.printLine("COMVAL", map, bw);
        EditHelper.printLine("NOTFORSALE", map, bw);
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        return new Publisher(
                (String) map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManagerNew(map),
                (String) map.get("DATE"),
                new Image(assetsFolder.resolve("publisher_icon").toFile(), null),
                Boolean.getBoolean((String) map.get("DEVELOPER")),
                Boolean.getBoolean((String) map.get("PUBLISHER")),
                Integer.parseInt((String) map.get("MARKET")),
                Integer.parseInt((String) map.get("SHARE")),
                GenreManager.INSTANCE.getImportHelperMap().getContentIdByName((String)map.get("GENRE")),
                map.containsKey("ONLYMOBILE"),
                Integer.parseInt((String) map.get("SPEED")),
                Integer.parseInt((String) map.get("COMVAL")),
                map.containsKey("NOTFORSALE"));
    }

    @Override
    public AbstractBaseContent constructContentFromMap(Map<String, String> map) throws ModProcessingException {
        return new Publisher(
                map.get("NAME EN"),
                getIdFromMap(map),
                new TranslationManagerNew(map),
                map.get("DATE"),
                new Image(MGT2Paths.COMPANY_ICONS.getPath().resolve(map.get("PIC") + ".png").toFile()),
                Boolean.getBoolean(map.get("DEVELOPER")),
                Boolean.getBoolean(map.get("PUBLISHER")),
                Integer.parseInt(map.get("MARKET")),
                Integer.parseInt(map.get("SHARE")),
                Integer.parseInt(map.get("GENRE")),
                map.containsKey("ONLYMOBILE"),
                Integer.parseInt(map.get("SPEED")),
                Integer.parseInt(map.get("COMVAL")),
                map.containsKey("NOTFORSALE"));
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterName"));

        JComboBox<String> comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
        JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();

        AtomicReference<Path> publisherImageFilePath = new AtomicReference<>(ImageFileHandler.defaultPublisherIcon);
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
                publisherImageFilePath.set(ImageFileHandler.defaultPublisherIcon);
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
        JSpinner spinnerShare = WindowHelper.getBaseSpinner("mod.publisher.addMod.optionPaneMessage.spinner.share.toolTip", 10, 0, 10, 1);

        AtomicInteger genreID = new AtomicInteger();
        JPanel panelGenre = new JPanel();
        JLabel labelGenre = new JLabel(I18n.INSTANCE.get("commonText.fanBase") + ":");
        JButton buttonSelectGenre = new JButton("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        ");
        buttonSelectGenre.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.toolTip"));
        buttonSelectGenre.addActionListener(actionEvent -> {
            try {
                ModManager.genreMod.analyzeFile();
                JLabel labelChooseGenre = new JLabel(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select"));
                ArrayList<String> availableGenres = new ArrayList<>();
                String[] string;
                string = ModManager.genreMod.getContentByAlphabet();
                for (String string1 : string) {
                    Map<String, String> genreMap = ModManager.genreMod.getSingleContentMapByName(string1);
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
                        genreID.set(ModManager.genreMod.getContentIdByName(currentGenre));
                        buttonSelectGenre.setText(currentGenre);
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select.genreFailure"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (ModProcessingException e) {
                ModManager.showException(e);
            }
        });
        panelGenre.add(labelGenre);
        panelGenre.add(buttonSelectGenre);

        comboBoxUnlockMonth.addActionListener(e -> buttonSelectGenre.setText("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        "));

        spinnerUnlockYear.addChangeListener(e -> buttonSelectGenre.setText("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        "));

        JCheckBox notForSale = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.notForSale"));
        notForSale.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.notForSale.toolTip"));

        JSpinner speed = WindowHelper.getBaseSpinner("mod.publisher.addMod.spinner.speed.toolTip", 5, 0, 10, 1);

        JSpinner comVal = WindowHelper.getBaseSpinner("mod.publisher.addMod.spinner.comVal.toolTip", 10000000, 10000000, 100000000, 500000);

        JCheckBox onlyMobile = new JCheckBox(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.onlyMobile"));
        onlyMobile.setToolTipText(I18n.INSTANCE.get("mod.publisher.addMod.checkBox.onlyMobile.toolTip"));

        Object[] params = {WindowHelper.getNamePanel(textFieldName), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, WindowHelper.getSpinnerPanel(spinnerMarketShare, SpinnerType.MARKET_SHARE), WindowHelper.getSpinnerPanel(spinnerShare, SpinnerType.PROFIT_SHARE), panelGenre, WindowHelper.getSpinnerPanel(speed, "mod.publisher.addMod.spinner.speed"), WindowHelper.getSpinnerPanel(comVal, "mod.publisher.addMod.spinner.comVal"), notForSale, onlyMobile};
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
                    if (publisherImageFilePath.get().equals(ImageFileHandler.defaultPublisherIcon)) {
                        logoId = 87;
                    } else {
                        logoId = CompanyLogoAnalyzer.getLogoNumber();
                    }
                    Publisher publisher = new Publisher(
                            textFieldName.getText(),
                            null,
                            new TranslationManagerNew(),
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
                            notForSale.isSelected()
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
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }
}
