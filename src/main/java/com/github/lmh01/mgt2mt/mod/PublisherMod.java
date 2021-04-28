package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.PublisherAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.PublisherEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.PublisherSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.SharingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class PublisherMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherMod.class);
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
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.publisherMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.6.0", "1.7.0", "1.7.1", "1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public void menuActionAddMod() {
        LOGGER.info("Action5");
    }

    @Override
    public String getMainTranslationKey() {
        return "publisher";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.publisher.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.publisher.upperCase.plural");
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
    public void addModMenuItemAction() {
        try {
            ModManager.publisherMod.getAnalyzer().analyzeFile();
            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField("---------Enter Name---------");
            panelName.add(labelName);
            panelName.add(textFieldName);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel("Unlock Month:");
            JComboBox comboBoxUnlockMonth = new JComboBox(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel("Unlock Year:");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - MAX INTEGER VALUE]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockYear.add(labelUnlockYear);
            panelUnlockYear.add(spinnerUnlockYear);

            AtomicReference<String> publisherImageFilePath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
            JPanel panelPublisherIcon = new JPanel();
            JLabel labelPublisherIcon = new JLabel("Publisher Icon:");
            JButton buttonBrowseIcon = new JButton("Browse");
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

            JCheckBox checkBoxIsDeveloper = new JCheckBox("Developer");
            checkBoxIsDeveloper.setSelected(true);
            checkBoxIsDeveloper.setToolTipText("When enabled: The company can release their own games");

            JCheckBox checkBoxIsPublisher = new JCheckBox("Publisher");
            checkBoxIsPublisher.setSelected(true);
            checkBoxIsPublisher.setToolTipText("When enabled: The company can release games for you (publish them)");

            JPanel panelMarketShare = new JPanel();
            JLabel labelMarketShare = new JLabel("Market Share:");
            JSpinner spinnerMarketShare = new JSpinner();
            spinnerMarketShare.setToolTipText("<html>[Range: 1 - 100]<br>This is how much market share your publisher has");
            if(Settings.disableSafetyFeatures){
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, 100, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(false);
            }
            panelMarketShare.add(labelMarketShare);
            panelMarketShare.add(spinnerMarketShare);

            JPanel panelShare = new JPanel();
            JLabel labelShare = new JLabel("Share:");
            JSpinner spinnerShare = new JSpinner();
            spinnerShare.setToolTipText("<html>[Range: 1 - 10]<br>Set how much money should be earned by one sell");
            if(Settings.disableSafetyFeatures){
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, 10, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(false);
            }
            panelShare.add(labelShare);
            panelShare.add(spinnerShare);

            AtomicInteger genreID = new AtomicInteger();
            JPanel panelGenre = new JPanel();
            JLabel labelGenre = new JLabel("Fan base:");
            JButton buttonSelectGenre = new JButton("        Select genre        ");
            buttonSelectGenre.setToolTipText("Click to select what genre the fan base of your publisher likes the most");
            buttonSelectGenre.addActionListener(actionEvent -> {
                try {
                    ModManager.genreMod.getAnalyzer().analyzeFile();
                    JLabel labelChooseGenre = new JLabel("Select what main genre your publisher should have:");
                    String[] string;
                    string = ModManager.genreMod.getAnalyzer().getContentByAlphabet();
                    JList<String> listAvailableGenres = new JList<>(string);
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                    Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                    if(JOptionPane.showConfirmDialog(null, params, "Select genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        if(!listAvailableGenres.isSelectionEmpty()){
                            String currentGenre = listAvailableGenres.getSelectedValue();
                            genreID.set(ModManager.genreMod.getAnalyzer().getContentIdByName(currentGenre));
                            buttonSelectGenre.setText(currentGenre);
                        }else{
                            JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while selecting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
            panelGenre.add(labelGenre);
            panelGenre.add(buttonSelectGenre);

            Object[] params = {panelName, panelUnlockMonth, panelUnlockYear, panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, panelMarketShare, panelShare, panelGenre};
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Add Publisher", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    boolean publisherAlreadyExists = false;
                    for(String string : ModManager.publisherMod.getAnalyzer().getContentByAlphabet()){
                        if(textFieldName.getText().equals(string)){
                            publisherAlreadyExists = true;
                        }
                    }
                    if(publisherAlreadyExists){
                        JOptionPane.showMessageDialog(null, "Unable to add publisher: The publisher name does already exist!", I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(textFieldName.getText().equals("") || textFieldName.getText().equals("---------Enter Name---------")){
                            JOptionPane.showMessageDialog(null, "Please enter a name first!", "", JOptionPane.INFORMATION_MESSAGE);
                            textFieldName.setText("---------Enter Name---------");
                        }else if(buttonSelectGenre.getText().equals("        Select genre        ")){
                            JOptionPane.showMessageDialog(null, "Please select a genre first!", "", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
                            int logoId;
                            if(publisherImageFilePath.toString().equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png")){
                                logoId = 87;
                            }else{
                                logoId = CompanyLogoAnalyzer.getLogoNumber();
                            }
                            if(JOptionPane.showConfirmDialog(null, "Add this publisher?\n" +
                                    "\nName: " + textFieldName.getText() +
                                    "\nDate: " + Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString() +
                                    "\nPic: See top left" +
                                    "\nDeveloper: " + checkBoxIsDeveloper.isSelected() +
                                    "\nPublisher: " + checkBoxIsPublisher.isSelected() +
                                    "\nMarketShare: " + spinnerMarketShare.getValue().toString() +
                                    "\nShare: " + spinnerShare.getValue().toString() +
                                    "\nGenre: " + buttonSelectGenre.getText(), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION){
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("ID", Integer.toString(ModManager.publisherMod.getAnalyzer().getFreeId()));
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
                                ModManager.publisherMod.getEditor().addMod(hashMap, publisherImageFilePath.toString());
                                JOptionPane.showMessageDialog(null, "Publisher " + hashMap.get("NAME EN") + " has been added successfully", "Publisher added", JOptionPane.INFORMATION_MESSAGE);
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.publisher") + " - " + hashMap.get("NAME EN"));
                                breakLoop = true;
                            }
                        }
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while adding publisher:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//Publisher.txt");
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
                    ProgressBarHelper.initializeProgressBar(0, ModManager.publisherMod.getAnalyzer().getDefaultContent().length, I18n.INSTANCE.get("progressBar.replacePublisher.removingOriginalPublishers"));
                    for(String string : ModManager.publisherMod.getAnalyzer().getDefaultContent()){
                        ModManager.publisherMod.getEditor().removeMod(string);
                        ProgressBarHelper.increment();
                    }
                    LOGGER.info("Original publishers have been removed!");
                    LOGGER.info("Adding new publishers...");
                    ArrayList<File> filesToImport = DataStreamHelper.getFiles(publisherUnzipped, "publisher.txt");
                    ProgressBarHelper.initializeProgressBar(0, filesToImport.size(), I18n.INSTANCE.get(""));
                    SharingManager.importAllFiles(filesToImport, new ArrayList<>(), false, "publisher", (string) -> ModManager.publisherMod.getSharer().importMod(string, false), ModManager.publisherMod.getSharer().getCompatibleModToolVersions(), new AtomicBoolean(false));
                    ModManager.publisherMod.getAnalyzer().analyzeFile();
                    if(ModManager.publisherMod.getAnalyzer().getActiveIds().contains(-1)){
                        ModManager.publisherMod.getEditor().removeMod("Dummy");
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
