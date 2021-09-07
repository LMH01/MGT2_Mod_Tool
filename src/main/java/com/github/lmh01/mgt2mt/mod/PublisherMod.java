package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ImageFileHandler;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PublisherMod extends AbstractAdvancedMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherMod.class);

    @Override
    protected void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("DEVELOPER", map, bw);
        EditHelper.printLine("PUBLISHER", map, bw);
        EditHelper.printLine("MARKET", map, bw);
        EditHelper.printLine("SHARE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
        if(map.containsKey("ONLYMOBILE")) {
            EditHelper.printLine("ONLYMOBILE", map, bw);
        }
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.6.0", "1.7.0", "1.7.1", "1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "publisher";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.publisherMod;
    }

    @Override
    public String getExportType() {
        return "publisher";
    }

    @Override
    public String getGameFileName() {
        return "Publisher.txt";
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_publisher.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        try {
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterName"));

            JComboBox comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
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

            JSpinner spinnerMarketShare = WindowHelper.getMarketShareSpinner();
            JSpinner spinnerShare = WindowHelper.getProfitShareSpinner();

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
                    for(String string1 : string){
                        Map<String, String> genreMap = ModManager.genreMod.getSingleContentMapByName(string1);
                        int genreDate = Integer.parseInt(genreMap.get("DATE").replaceAll("[^0-9]", ""));
                        if(Integer.parseInt(spinnerUnlockYear.getValue().toString()) > genreDate){
                            availableGenres.add(string1);
                        } else if (Integer.parseInt(spinnerUnlockYear.getValue().toString()) == genreDate) {
                            if(Utils.getNumberForMonth(Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString()) >= Utils.getNumberForMonth(genreMap.get("DATE"))){
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
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                    Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                    if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.selectGenre"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        if(!listAvailableGenres.isSelectionEmpty()){
                            String currentGenre = listAvailableGenres.getSelectedValue();
                            genreID.set(ModManager.genreMod.getContentIdByName(currentGenre));
                            buttonSelectGenre.setText(currentGenre);
                        }else{
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

            Object[] params = {WindowHelper.getNamePanel(this, textFieldName), WindowHelper.getUnlockDatePanel(comboBoxUnlockMonth, spinnerUnlockYear), panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, WindowHelper.getSpinnerPanel(spinnerMarketShare, 12), WindowHelper.getSpinnerPanel(spinnerShare, 9), panelGenre};
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("commonText.add.upperCase") + ": " + getType(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    boolean publisherAlreadyExists = false;
                    for(String string : getContentByAlphabet()){
                        if(textFieldName.getText().equals(string)){
                            publisherAlreadyExists = true;
                        }
                    }
                    if(publisherAlreadyExists){
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.nameAlreadyInUse"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(textFieldName.getText().equals("") || textFieldName.getText().equals(I18n.INSTANCE.get("commonText.enterName"))){
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("modManager.general.enterNameFirst"), "", JOptionPane.INFORMATION_MESSAGE);
                            textFieldName.setText(I18n.INSTANCE.get("commonText.enterName"));
                        }else if(buttonSelectGenre.getText().equals("        " + I18n.INSTANCE.get("commonText.selectGenre") + "        ")){
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.selectGenreFirst"), "", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
                            int logoId;
                            if(publisherImageFilePath.get().equals(ImageFileHandler.defaultPublisherIcon)){
                                logoId = 87;
                            }else{
                                logoId = CompanyLogoAnalyzer.getLogoNumber();
                            }
                            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.firstPart") + "\n\n" +
                                    I18n.INSTANCE.get("commonText.name") + ": " + textFieldName.getText() + "\n" +
                                    I18n.INSTANCE.get("commonText.date") + ": " + Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()) + " " + spinnerUnlockYear.getValue().toString() + "\n" +
                                    I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "\n" +
                                    I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer") + ": " + Utils.getTranslatedValueFromBoolean(checkBoxIsDeveloper.isSelected()) + "\n" +
                                    I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher") + ": " + Utils.getTranslatedValueFromBoolean(checkBoxIsPublisher.isSelected()) + "\n" +
                                    I18n.INSTANCE.get("commonText.marketShare") + ": " + spinnerMarketShare.getValue().toString() + "\n" +
                                    I18n.INSTANCE.get("commonText.share") + ": " + spinnerShare.getValue().toString() + "\n" +
                                    I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + buttonSelectGenre.getText(), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION){
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", Integer.toString(getFreeId()));
                                map.put("NAME EN", textFieldName.getText());
                                map.put("NAME GE", textFieldName.getText());
                                map.put("NAME TU", textFieldName.getText());
                                map.put("NAME FR", textFieldName.getText());
                                map.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
                                map.put("PIC", Integer.toString(logoId));
                                map.put("DEVELOPER", Boolean.toString(checkBoxIsDeveloper.isSelected()));
                                map.put("PUBLISHER", Boolean.toString(checkBoxIsPublisher.isSelected()));
                                map.put("MARKET", spinnerMarketShare.getValue().toString());
                                map.put("SHARE", spinnerShare.getValue().toString());
                                map.put("GENRE", genreID.toString());
                                Backup.createBackup(getGameFile());
                                addModToFile(map);
                                copyPublisherIcon(new File(MGT2Paths.COMPANY_ICONS.getPath() + "/" + map.get("PIC") + ".png"), new File(publisherImageFilePath.toString()));
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.publisher") + " - " + map.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, "Publisher " + map.get("NAME EN") + " has been added successfully", "Publisher added", JOptionPane.INFORMATION_MESSAGE);
                                breakLoop = true;
                            }
                        }
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        Map<String, String> map = transformGenericToMap(t);
        return I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.firstPart") + "\n\n" +
                I18n.INSTANCE.get("commonText.name") + ": " + map.get("NAME EN") + "\n" +
                I18n.INSTANCE.get("commonText.date") + ": " + map.get("DATE") + "\n" +
                I18n.INSTANCE.get("dialog.genreManager.addGenre.pic") + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.developer") + ": " + map.get("DEVELOPER") + "\n" +
                I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.publisher") + ": " + map.get("PUBLISHER") + "\n" +
                I18n.INSTANCE.get("commonText.marketShare") + ": " + map.get("MARKET") + "\n" +
                I18n.INSTANCE.get("commonText.share") + ": " + map.get("SHARE") + "\n" +
                I18n.INSTANCE.get("commonText.genre.upperCase") + ": " + getContentNameById(Integer.parseInt(map.get("GENRE")));
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
        return "PUBLISHER";
    }

    @Override
    public String getImportExportFileName() {
        return "publisher.txt";
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        super.removeModFromFile(name);
        int iconId = getPublisherIconIdByName(name);
        if (iconId > 146) {
            File publisherIcon = MGT2Paths.COMPANY_ICONS.getPath().resolve(Paths.get(iconId + ".png")).toFile();
            LOGGER.info("publisherIcon: " + publisherIcon.getPath());
            if (publisherIcon.exists()) {
                publisherIcon.delete();
                LOGGER.info("Image file for publisher " + name + " has been removed.");
            }
        }
    }

    @Override
    public Map<String, String> getChangedExportMap(Map<String, String> map, String name) throws ModProcessingException {
        try {
            map.replace("GENRE", ModManager.genreMod.getContentNameById(Integer.parseInt(map.get("GENRE"))));
            return map;
        } catch (NumberFormatException | NullPointerException e) {
            throw new ModProcessingException("The export map could not be changed", e);
        }
    }

    @Override
    public Map<String, String> exportImages(String name, Path assetsFolder) throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        String imageName = getType().toLowerCase().replaceAll(" ", "_") + "_" + name.toLowerCase().replaceAll(" ", "_") + "_icon.png";
        File fileExportedPublisherIcon = assetsFolder.resolve(imageName).toFile();
        if(!fileExportedPublisherIcon.exists()){
            try {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.exportingImage") + ": " + getType() + " - " + imageName);
                File filePublisherIconToExport = MGT2Paths.COMPANY_ICONS.getPath().resolve(getSingleContentMapByName(name).get("PIC") + ".png").toFile();
                Files.copy(filePublisherIconToExport.toPath(), fileExportedPublisherIcon.toPath());
            } catch (IOException e) {
                throw new ModProcessingException("Publisher image files could not be copied", e);
            }
        } else {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.imageNotExported") + ": " + MGT2Paths.COMPANY_ICONS.getPath().resolve(getSingleContentMapByName(name).get("PIC") + ".png").toFile());
        }
        map.put("iconName", imageName);
        return map;
    }

    @Override
    public String importMod(Path importFolderPath, boolean showMessages) throws ModProcessingException {
        analyzeFile();
        ModManager.genreMod.analyzeFile();
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + getType());
        File fileToImport = importFolderPath.resolve(getImportExportFileName()).toFile();
        HashMap<String, String> map = new HashMap<>();
        List<Map<String, String>> list;
        try {
            list = DataStreamHelper.parseDataFile(fileToImport);
        } catch (IOException e) {
            throw new ModProcessingException("File could not be parsed '" + fileToImport.getName() + "': " +  e.getMessage());
        }
        map.put("ID", Integer.toString(getFreeId()));
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE")){
                try {
                    map.put("GENRE", Integer.toString(ModManager.genreMod.getContentIdByName(entry.getValue())));
                } catch (ModProcessingException e) {
                    int randomGenreID = Utils.getRandomNumber(0, ModManager.genreMod.getFileContent().size()-1);
                    LOGGER.info("Genre list size: " + ModManager.genreMod.getFileContent().size());
                    map.put("GENRE", Integer.toString(randomGenreID));
                }
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }
        boolean CanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION")) || Settings.disableSafetyFeatures){
                CanBeImported = true;
            }
        }
        if(!CanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.notCompatible") + " " + getType() + " - " + map.get("NAME EN"));
            return getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.firstPart") + ":\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.secondPart") + "\n" + getType() + " " + I18n.INSTANCE.get("sharer.importMod.couldNotBeImported.thirdPart") + " " + map.get("MGT2MT VERSION");
        }
        for(Map<String, String> existingContent : getFileContent()){
            for(Map.Entry<String, String> entry : existingContent.entrySet()){
                if(entry.getValue().equals(map.get("NAME EN"))){
                    sendLogMessage(getType() + " " + I18n.INSTANCE.get("sharer.importMod.alreadyExists.short") + " - " + getType() + " " + I18n.INSTANCE.get("sharer.importMod.nameTaken"));
                    TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.importMod.alreadyExists") + " " + getType() + " - " + map.get("NAME EN"));
                    return "false";
                }
            }
        }
        boolean addFeature = true;
        if(showMessages){
            if(JOptionPane.showConfirmDialog(null, getOptionPaneMessage(map)) != JOptionPane.YES_OPTION){
                addFeature = false;
            }
        }
        File publisherImageFilePath = new File(importFolderPath + "/DATA/icon.png");
        map.put("PIC", Integer.toString(CompanyLogoAnalyzer.getLogoNumber()));
        if(addFeature){
            ModManager.publisherMod.addModToFile(map);
            copyPublisherIcon(new File(MGT2Paths.COMPANY_ICONS.getPath() + "/" + map.get("PIC") + ".png"), new File(publisherImageFilePath.toString()));
            doOtherImportThings(importFolderPath, map.get("NAME EN"));
            if(showMessages){
                JOptionPane.showMessageDialog(null, getType() + " [" + map.get("NAME EN") + "] " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
            }
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("NAME EN"));
        }
        return "true";
    }

    /**
     * Copies source to target
     * @param publisherImageSource New publisher image file that should be copied
     * @param publisherImageTarget The file to what the image file should be copied to
     * @throws ModProcessingException If the copying for the image file fails
     */
    private static void copyPublisherIcon(File publisherImageTarget, File publisherImageSource) throws ModProcessingException{//TODO check if this function still works
        try {
            if(!publisherImageTarget.equals(ImageFileHandler.defaultPublisherIcon.toFile())) {
                Files.copy(Paths.get(publisherImageSource.getPath()), Paths.get(publisherImageTarget.getPath()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new ModProcessingException("Copying image file for new publisher failed: " + e.getMessage(), e);
        }
    }

    /**
     * Searches the image files for the publisher name id and returns it if found
     * @param publisherNameEN The name for the publisher for which the id should be searched
     * @return The id of the publisher icon
     * @throws ModProcessingException If id was not found
     */
    private int getPublisherIconIdByName(String publisherNameEN) throws ModProcessingException{
        List<Map<String, String>> list = ModManager.publisherMod.getFileContent();
        try {
            for (Map<String, String> map : list) {
                if (map.get("NAME EN").equals(publisherNameEN)) {
                    return Integer.parseInt(map.get("PIC"));
                }
            }
        } catch (NullPointerException ignored) {

        }
        throw new ModProcessingException("Publisher icon id was not found for publisher: " + publisherNameEN);
    }

    /**
     * Removes the specified genre from the publisher file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     * @param name The genre that should be removed
     * @throws ModProcessingException When something went wrong
     */
    public void removeGenre(String name) throws ModProcessingException {
        try {
            int genreId = ModManager.genreMod.getContentIdByName(name);
            analyzeFile();
            sendLogMessage("Replacing genre id in publisher file: " + name);
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
                try {
                    if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                        fileContent.remove("GENRE");
                        fileContent.put("GENRE", Integer.toString(ModManager.genreMod.getActiveIds().get(Utils.getRandomNumber(0, ModManager.genreMod.getActiveIds().size()))));
                    }
                } catch (NumberFormatException e) {
                    bw.close();
                    throw new ModProcessingException("Genre can not be removed from publisher file", e);
                }
                printValues(fileContent, bw);
                bw.write("\r\n");
            }
            bw.write("[EOF]");
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while editing the game file for mod " + getType() + ": " + e.getMessage());
        }
    }

    public final String REAL_PUBLISHER_ZIP_URL = "https://www.dropbox.com/s/gkn7y2he1we3fgc/Publishers.zip?dl=1";

    /**
     * Asks the user if he is sure that the existing publishers should be replaced with the real publisher equivalents
     */
    public void realPublishers(){
        ThreadHandler.startModThread(() -> {
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.mainMessage"), "Replace publisher?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                try{
                    ProgressBarHelper.initializeProgressBar(0,1, I18n.INSTANCE.get("progressBar.publisherHelper.replaceWithRealPublishers.initialize"), false, false);
                    Path publisherZip = ModManagerPaths.DOWNLOAD.getPath().resolve("publisher.zip");
                    Path publisherUnzipped = ModManagerPaths.DOWNLOAD.getPath().resolve("publisher");
                    boolean downloadFiles = true;
                    if(publisherUnzipped.toFile().exists()){
                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.zipFileAlreadyExists"), "?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                            downloadFiles = false;
                        }else{
                            DataStreamHelper.deleteDirectory(publisherZip, false);
                            DataStreamHelper.deleteDirectory(publisherUnzipped, false);
                        }
                    }
                    if(downloadFiles){
                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.downloadZip"));
                        DataStreamHelper.downloadZip(REAL_PUBLISHER_ZIP_URL, publisherZip);
                        ProgressBarHelper.resetProgressBar();
                        DataStreamHelper.unzip(publisherZip, publisherUnzipped);
                    }
                    LOGGER.info("Real publisher files are ready.");
                    LOGGER.info("Removing existing publishers...");
                    ProgressBarHelper.initializeProgressBar(0, ModManager.publisherMod.getDefaultContent().length, I18n.INSTANCE.get("progressBar.replacePublisher.removingOriginalPublishers"));
                    for(String string : ModManager.publisherMod.getDefaultContent()){
                        ModManager.publisherMod.removeModFromFile(string);
                        ProgressBarHelper.increment();
                    }
                    LOGGER.info("Original publishers have been removed!");
                    LOGGER.info("Adding new publishers...");
                    ArrayList<File> filesToImport = DataStreamHelper.getFiles(publisherUnzipped.toFile(), "publisher.txt");
                    ProgressBarHelper.initializeProgressBar(0, filesToImport.size(), I18n.INSTANCE.get(""));
                    SharingManager.importAllFiles(filesToImport, new ArrayList<>(), false, "publisher", (string) -> ModManager.publisherMod.importMod(string, false), ModManager.publisherMod.getCompatibleModToolVersions(), new AtomicBoolean(false));
                    ModManager.publisherMod.analyzeFile();
                    if(ModManager.publisherMod.getActiveIds().contains(-1)){
                        ModManager.publisherMod.removeModFromFile("Dummy");
                    }
                    TextAreaHelper.appendText(I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.success").replace("<html>", "").replace("<br>", " "));
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.success"));
                }catch (IOException e){
                    e.printStackTrace();
                    String errorMessageToDisplay;
                    if(e.getMessage().equals("www.dropbox.com")){
                        errorMessageToDisplay = I18n.INSTANCE.get("commonText.noInternet");
                    }else{
                        errorMessageToDisplay = e.getMessage();
                    }
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.somethingWentWrong") + " " + errorMessageToDisplay, I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }, "runnableReplacePublisherWithRealPublishers");
    }
}
