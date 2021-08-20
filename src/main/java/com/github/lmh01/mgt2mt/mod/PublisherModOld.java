package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.PublisherAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.PublisherEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.PublisherSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedModOld;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PublisherModOld extends AbstractAdvancedModOld {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherModOld.class);
    PublisherAnalyzer publisherAnalyzer = new PublisherAnalyzer();
    PublisherEditor publisherEditor = new PublisherEditor();
    PublisherSharer publisherSharer = new PublisherSharer();
    public ArrayList<JMenuItem> publisherModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public PublisherAnalyzer getAnalyzer(){
        return publisherAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public PublisherEditor getEditor(){
        return publisherEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public PublisherSharer getSharer(){
        return publisherSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return publisherAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return publisherEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return publisherSharer;
    }

    @Override
    public AbstractAdvancedModOld getAdvancedMod() {
        return ModManager.publisherModOld;
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
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return publisherModMenuItems;
    }

    @Override
    public void menuActionAddMod() {
        try {
            ModManager.publisherModOld.getAnalyzer().analyzeFile();
            JTextField textFieldName = new JTextField(I18n.INSTANCE.get("commonText.enterName"));

            JComboBox comboBoxUnlockMonth = WindowHelper.getUnlockMonthComboBox();
            JSpinner spinnerUnlockYear = WindowHelper.getUnlockYearSpinner();

            AtomicReference<String> publisherImageFilePath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
            JPanel panelPublisherIcon = new JPanel();
            JLabel labelPublisherIcon = new JLabel(I18n.INSTANCE.get("mod.publisher.icon") + ":");
            JButton buttonBrowseIcon = new JButton(I18n.INSTANCE.get("commonText.browse"));
            buttonBrowseIcon.addActionListener(actionEvent -> {
                String imageFilePath = Utils.getImagePath();
                if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                    publisherImageFilePath.set(imageFilePath);
                }else{
                    publisherImageFilePath.set(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
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
                    ModManager.genreModOld.getAnalyzer().analyzeFile();
                    JLabel labelChooseGenre = new JLabel(I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select"));
                    ArrayList<String> availableGenres = new ArrayList<>();
                    String[] string;
                    string = ModManager.genreModOld.getAnalyzer().getContentByAlphabet();
                    for(String string1 : string){
                        Map<String, String> genreMap = ModManager.genreModOld.getAnalyzer().getSingleContentMapByName(string1);
                        int genreDate = Integer.parseInt(genreMap.get("DATE").replaceAll("[^0-9]", ""));
                        if(Integer.parseInt(spinnerUnlockYear.getValue().toString()) >= genreDate){
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
                            genreID.set(ModManager.genreModOld.getAnalyzer().getContentIdByName(currentGenre));
                            buttonSelectGenre.setText(currentGenre);
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select.genreFailure"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.publisher.addMod.optionPaneMessage.button.fanBase.select.error") + ":\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
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
                    for(String string : ModManager.publisherModOld.getAnalyzer().getContentByAlphabet()){
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
                            if(publisherImageFilePath.toString().equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png")){
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
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("ID", Integer.toString(ModManager.publisherModOld.getAnalyzer().getFreeId()));
                                hashMap.put("NAME EN", textFieldName.getText());
                                hashMap.put("NAME GE", textFieldName.getText());
                                hashMap.put("NAME TU", textFieldName.getText());
                                hashMap.put("NAME FR", textFieldName.getText());
                                hashMap.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
                                hashMap.put("PIC", Integer.toString(logoId));
                                hashMap.put("DEVELOPER", Boolean.toString(checkBoxIsDeveloper.isSelected()));
                                hashMap.put("PUBLISHER", Boolean.toString(checkBoxIsPublisher.isSelected()));
                                hashMap.put("MARKET", spinnerMarketShare.getValue().toString());
                                hashMap.put("SHARE", spinnerShare.getValue().toString());
                                hashMap.put("GENRE", genreID.toString());
                                Backup.createBackup(getFile());
                                ModManager.publisherModOld.getEditor().addMod(hashMap, publisherImageFilePath.toString());
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.publisher") + " - " + hashMap.get("NAME EN"));
                                JOptionPane.showMessageDialog(null, "Publisher " + hashMap.get("NAME EN") + " has been added successfully", "Publisher added", JOptionPane.INFORMATION_MESSAGE);
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
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "publisher.txt";
    }


    public final String REAL_PUBLISHER_ZIP_URL = "https://www.dropbox.com/s/gkn7y2he1we3fgc/Publishers.zip?dl=1";

    /**
     * Asks the user if he is sure that the existing publishers should be replaced with the real publisher equivalents
     */
    public void realPublishers(){
        Thread thread = new Thread(() -> {
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.mainMessage"), "Replace publisher?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                try{
                    ProgressBarHelper.initializeProgressBar(0,1, I18n.INSTANCE.get("progressBar.publisherHelper.replaceWithRealPublishers.initialize"), false, false);
                    File publisherZip = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher.zip");
                    File publisherUnzipped = new File(Settings.MGT2_MOD_MANAGER_PATH + "Downloads//publisher");
                    boolean downloadFiles = true;
                    if(publisherUnzipped.exists()){
                        if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("publisherHelper.replaceWithRealPublishers.zipFileAlreadyExists"), "?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
                            downloadFiles = false;
                        }else{
                            DataStreamHelper.deleteDirectory(publisherZip, false);
                            DataStreamHelper.deleteDirectory(publisherUnzipped, false);
                        }
                    }
                    if(downloadFiles){
                        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.downloadZip"));
                        DataStreamHelper.downloadZip(REAL_PUBLISHER_ZIP_URL, publisherZip.getPath());
                        ProgressBarHelper.resetProgressBar();
                        DataStreamHelper.unzip(publisherZip.getPath(), publisherUnzipped);
                    }
                    LOGGER.info("Real publisher files are ready.");
                    LOGGER.info("Removing existing publishers...");
                    ProgressBarHelper.initializeProgressBar(0, ModManager.publisherModOld.getAnalyzer().getDefaultContent().length, I18n.INSTANCE.get("progressBar.replacePublisher.removingOriginalPublishers"));
                    for(String string : ModManager.publisherModOld.getAnalyzer().getDefaultContent()){
                        ModManager.publisherModOld.getEditor().removeMod(string);
                        ProgressBarHelper.increment();
                    }
                    LOGGER.info("Original publishers have been removed!");
                    LOGGER.info("Adding new publishers...");
                    ArrayList<File> filesToImport = DataStreamHelper.getFiles(publisherUnzipped, "publisher.txt");
                    ProgressBarHelper.initializeProgressBar(0, filesToImport.size(), I18n.INSTANCE.get(""));
                    SharingManager.importAllFiles(filesToImport, new ArrayList<>(), false, "publisher", (string) -> ModManager.publisherModOld.getSharer().importMod(string, false), ModManager.publisherModOld.getSharer().getCompatibleModToolVersions(), new AtomicBoolean(false));
                    ModManager.publisherModOld.getAnalyzer().analyzeFile();
                    if(ModManager.publisherModOld.getAnalyzer().getActiveIds().contains(-1)){
                        ModManager.publisherModOld.getEditor().removeMod("Dummy");
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
        });
        ThreadHandler.startThread(thread, "runnableReplacePublisherWithRealPublishers");
    }
}
