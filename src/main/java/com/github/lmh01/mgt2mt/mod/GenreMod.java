package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.GenreAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.ThemeFileAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.data_stream.editor.GenreEditor;
import com.github.lmh01.mgt2mt.data_stream.sharer.managed.AbstractAdvancedSharer;
import com.github.lmh01.mgt2mt.data_stream.sharer.GenreSharer;
import com.github.lmh01.mgt2mt.mod.managed.AbstractAdvancedMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.lmh01.mgt2mt.util.Utils.getMGT2DataPath;

public class GenreMod extends AbstractAdvancedMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenreMod.class);
    GenreAnalyzer genreAnalyzer = new GenreAnalyzer();
    GenreEditor genreEditor = new GenreEditor();
    GenreSharer genreSharer = new GenreSharer();
    public ArrayList<JMenuItem> genreModMenuItems = getInitialModMenuItems();
    public JMenuItem exportMenuItem = getInitialExportMenuItem();

    /**
     * @return Returns the analyzer for the mod.
     * Using this function you can use all specific functions for this analyzer.
     */
    public GenreAnalyzer getAnalyzer(){
        return genreAnalyzer;
    }

    /**
     * @return Returns the editor for the mod.
     * Using this function you can use all specific functions for this editor.
     */
    public GenreEditor getEditor(){
        return genreEditor;
    }

    /**
     * @return Returns the sharer for the mod.
     * Using this function you can use all specific functions for this sharer.
     */
    public GenreSharer getSharer(){
        return genreSharer;
    }

    @Override
    public AbstractAdvancedAnalyzer getBaseAnalyzer() {
        return genreAnalyzer;
    }

    @Override
    public AbstractAdvancedEditor getBaseEditor() {
        return genreEditor;
    }

    @Override
    public AbstractAdvancedSharer getBaseSharer() {
        return genreSharer;
    }

    @Override
    public AbstractAdvancedMod getAdvancedMod() {
        return ModManager.genreMod;
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.3b","1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "2.0.0"};
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.genre.upperCase");
    }

    @Override
    public String getTypePlural() {
        return I18n.INSTANCE.get("commonText.genre.upperCase.plural");
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public ArrayList<JMenuItem> getInitialModMenuItems() {
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenuItem addModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.addMod"));
        addModItem.addActionListener(actionEvent -> menuActionAddMod());
        JMenuItem addRandomGenre = new JMenuItem(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre"));
        addRandomGenre.setToolTipText(I18n.INSTANCE.get("window.main.mods.genres.addRandomGenre.toolTip"));
        addRandomGenre.addActionListener(actionEvent -> addRandomizedGenre());
        JMenuItem removeModItem = new JMenuItem(I18n.INSTANCE.get("modManager." + getMainTranslationKey() + ".windowMain.modButton.removeMod"));
        removeModItem.addActionListener(actionEvent -> removeModMenuItemAction());
        menuItems.add(addModItem);
        menuItems.add(addRandomGenre);
        menuItems.add(removeModItem);
        return menuItems;
    }

    @Override
    public ArrayList<JMenuItem> getModMenuItems() {
        return genreModMenuItems;
    }

    @Override
    public void menuActionAddMod() {
        try {
            ModManager.genreMod.getAnalyzer().analyzeFile();
            ThemeFileAnalyzer.analyzeThemeFiles();
            GenreManager.startStepByStepGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JMenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    @Override
    public String getFileName() {
        return "genre.txt";
    }

    @Override
    public File getFile() {
        return new File(getMGT2DataPath() + "//Genres.txt");
    }

    public void addRandomizedGenre(){
        Thread thread = new Thread(() -> {
            try{
                ModManager.genreMod.getAnalyzer().analyzeFile();
                final Map<String, String>[] mapNameTranslations = new Map[]{new HashMap<>()};
                final Map<String, String>[] mapDescriptionTranslations = new Map[]{new HashMap<>()};
                AtomicReference<ArrayList<File>> screenshotFiles = new AtomicReference<>(new ArrayList<>());
                AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
                AtomicBoolean descriptionTranslationsAdded = new AtomicBoolean(false);
                AtomicReference<String> iconPath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");

                JPanel panelName = new JPanel();
                JLabel labelName = new JLabel(I18n.INSTANCE.get("commonText.name") + ":");
                JTextField textFieldName = new JTextField(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.enterGenreName"));
                panelName.add(labelName);
                panelName.add(textFieldName);

                JButton buttonAddNameTranslations = new JButton(I18n.INSTANCE.get("commonText.addNameTranslations"));
                buttonAddNameTranslations.setToolTipText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.button.addNameTranslations.toolTip"));
                buttonAddNameTranslations.addActionListener(actionEvent -> {
                    if(!nameTranslationsAdded.get()){
                        mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                        nameTranslationsAdded.set(true);
                        buttonAddNameTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations.added"));
                    }else{
                        if(JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("commonText.name.var1") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                            mapNameTranslations[0] = TranslationManager.getTranslationsMap();
                            nameTranslationsAdded.set(true);
                        }
                    }
                });

                JPanel panelDescription = new JPanel();
                JLabel labelDescription = new JLabel(I18n.INSTANCE.get("commonText.description") + ":");
                JTextField textFieldDescription = new JTextField(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.enterGenreDescription"));
                panelDescription.add(labelDescription);
                panelDescription.add(textFieldDescription);

                JButton buttonAddDescriptionTranslations = new JButton(I18n.INSTANCE.get("commonText.addDescriptionTranslations"));
                buttonAddDescriptionTranslations.setToolTipText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.button.addDescriptionTranslations.toolTip"));
                buttonAddDescriptionTranslations.addActionListener(actionEvent -> {
                    if(!descriptionTranslationsAdded.get()){
                        mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                        buttonAddDescriptionTranslations.setText(I18n.INSTANCE.get("commonText.addNameTranslations.added"));
                        descriptionTranslationsAdded.set(true);
                    }else{
                        if(JOptionPane.showConfirmDialog(null, "<html>" + I18n.INSTANCE.get("commonText.description.var1") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                            mapDescriptionTranslations[0] = TranslationManager.getTranslationsMap();
                            descriptionTranslationsAdded.set(true);
                        }
                    }
                });

                JButton buttonSetIcon = new JButton(I18n.INSTANCE.get("commonText.addGenreIcon"));
                buttonSetIcon.setToolTipText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.button.addGenreIcon.toolTip"));
                buttonSetIcon.addActionListener(actionEvent -> {
                    iconPath.set(getGenreImageFilePath(false, true, null));
                    if(!iconPath.toString().equals("error")){
                        buttonSetIcon.setText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.addGenreIcon.iconSet"));
                    }else{
                        iconPath.set(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
                        buttonSetIcon.setText(I18n.INSTANCE.get("commonText.addGenreIcon"));
                    }
                });

                JButton buttonSetScreenshots = new JButton(I18n.INSTANCE.get("commonText.addScreenshots"));
                buttonSetScreenshots.setToolTipText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.button.addGenreScreenshots.toolTip"));
                buttonSetScreenshots.addActionListener(actionEvent -> setGenreScreenshots(screenshotFiles, buttonSetScreenshots));

                JCheckBox checkBoxShowSummary = new JCheckBox(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.checkBox.showSummary"));
                checkBoxShowSummary.setToolTipText(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.checkBox.showSummary.toolTip"));

                Object[] params = {panelName, buttonAddNameTranslations, panelDescription, buttonAddDescriptionTranslations, buttonSetIcon, buttonSetScreenshots, checkBoxShowSummary};
                while(true){
                    if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        Map<String, String> map = new HashMap<>();
                        for(Map.Entry<String, String> entry : mapNameTranslations[0].entrySet()){
                            map.put("NAME " + entry.getKey(), entry.getValue());
                        }
                        for(Map.Entry<String, String> entry : mapDescriptionTranslations[0].entrySet()){
                            map.put("DESC " + entry.getKey(), entry.getValue());
                        }

                        //Randomized value allocation
                        String unlockMonth = convertMonthNumberToMonthName(Utils.getRandomNumber(1, 12));
                        int unlockYear = Utils.getRandomNumber(1976, 2010);
                        map.put("ID", Integer.toString(ModManager.genreMod.getAnalyzer().getFreeId()));
                        String genreName = textFieldName.getText();
                        String genreDescription = textFieldDescription.getText();
                        if(textFieldName.getText().equals(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.enterGenreName")) || textFieldName.getText().isEmpty()){
                            genreName = "Randomized Genre";
                        }
                        if(textFieldDescription.getText().equals(I18n.INSTANCE.get("dialog.genreHelper.addRandomizedGenre.enterGenreDescription")) || textFieldDescription.getText().isEmpty()){
                            genreDescription = "Genre created by the MGT2 Mod Tool using random values";
                        }
                        if(map.containsKey("NAME EN")){
                            map.replace("NAME EN", genreName);
                        }else{
                            map.put("NAME EN", genreName);
                        }
                        if(map.containsKey("DESC EN")){
                            map.replace("DESC EN", genreDescription);
                        }else{
                            map.put("DESC EN", genreDescription);
                        }
                        map.put("DATE", unlockMonth + " " + unlockYear);
                        int researchPoints = Utils.roundToNextHundred(Utils.getRandomNumber(0, 1500));
                        int price = Utils.roundToNextThousand(Utils.getRandomNumber(0, 300000));
                        int developmentCost = Utils.roundToNextThousand(Utils.getRandomNumber(0, 10000));
                        map.put("RES POINTS", Integer.toString(researchPoints));
                        map.put("PRICE", Integer.toString(price));
                        map.put("DEV COSTS", Integer.toString(developmentCost));
                        map.put("TGROUP", getRandomTargetGroup());
                        Integer[] workPriority = getRandomWorkPriorityValues();
                        map.put("GAMEPLAY", Integer.toString(workPriority[0]));
                        map.put("GRAPHIC", Integer.toString(workPriority[1]));
                        map.put("SOUND", Integer.toString(workPriority[2]));
                        map.put("CONTROL", Integer.toString(workPriority[3]));
                        map.put("GENRE COMB", getRandomGenreCombs());
                        Integer[] designFocus = getRandomDesignFocusValues();
                        map.put("FOCUS0", Integer.toString(designFocus[0]));
                        map.put("FOCUS1", Integer.toString(designFocus[1]));
                        map.put("FOCUS2", Integer.toString(designFocus[2]));
                        map.put("FOCUS3", Integer.toString(designFocus[3]));
                        map.put("FOCUS4", Integer.toString(designFocus[4]));
                        map.put("FOCUS5", Integer.toString(designFocus[5]));
                        map.put("FOCUS6", Integer.toString(designFocus[6]));
                        map.put("FOCUS7", Integer.toString(designFocus[7]));
                        map.put("ALIGN0", Integer.toString(Utils.getRandomNumber(1, 10)));
                        map.put("ALIGN1", Integer.toString(Utils.getRandomNumber(1, 10)));
                        map.put("ALIGN2", Integer.toString(Utils.getRandomNumber(1, 10)));
                        HashSet<Integer> compatibleThemeIds = getRandomThemeIds();
                        map.put("THEME COMB", getCompatibleThemes(compatibleThemeIds));
                        List<HashSet<Integer>> gameplayFeatures = getRandomGameplayFeatureIds();
                        setGameplayFeatureCompatibility(map, gameplayFeatures.get(0), gameplayFeatures.get(1));
                        File iconFile = new File(iconPath.toString());
                        Backup.createBackup(getFile());
                        if(GenreManager.addGenre(map, map, compatibleThemeIds, gameplayFeatures.get(0), gameplayFeatures.get(1), screenshotFiles.get(),true, iconFile,  checkBoxShowSummary.isSelected())){
                            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.genre") + " - " + genreName);
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.genre") + " [" + genreName + "] " + I18n.INSTANCE.get("commonText.successfullyAdded"), I18n.INSTANCE.get("commonText.genre") + " " + I18n.INSTANCE.get("commonText.added"), JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "<html>" + I18n.INSTANCE.get("commonText.unableToAdd") + getType() + "<br>"  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), I18n.INSTANCE.get("commonText.unableToAdd") + getType(), JOptionPane.ERROR_MESSAGE);
            }
        });
        ThreadHandler.startThread(thread, "runnableAddRandomizedGenre");
    }


    public String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog, JTextField textFieldImagePath) {
        if(useTextFiledPath){
            String textFieldPath = textFieldImagePath.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    if(showDialog){
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                    }
                    return textFieldPath;
                }else{
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            }else{
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                return "error";
            }
        }else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFile.fileChooser"));

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    if(fileChooser.getSelectedFile().getName().contains(".png")){
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFileSet"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return "error";
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                return "error";
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }

    /**
     * Sets the genreScreenshots ArrayList a new array list containing the selected files. Uses {@link GenreMod#getGenreScreenshots()} to get the Array List.
     * @param genreScreenshots The array list that should be set
     * @param button The button of which the text should be changed
     */
    public void setGenreScreenshots(AtomicReference<ArrayList<File>> genreScreenshots, JButton button){
        while(true){
            genreScreenshots.set(getGenreScreenshots());
            if(!genreScreenshots.get().isEmpty()){
                StringBuilder filePaths = new StringBuilder();
                for (File arrayListScreenshotFile : genreScreenshots.get()) {
                    filePaths.append("<br>").append(arrayListScreenshotFile);
                }
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.followingImageFilesHaveBeenAdded.firstPart") + "<br>" + filePaths + "<br><br>" + I18n.INSTANCE.get("commonText.isThisCorrect"), I18n.INSTANCE.get("frame.title.isThisCorrect"), JOptionPane.YES_NO_OPTION) == 0){
                    button.setText(I18n.INSTANCE.get("commonText.screenshots.added"));
                    break;
                }
            }else{
                button.setText(I18n.INSTANCE.get("commonText.addScreenshots"));
                break;
            }
        }
    }

    /**
     * Opens a ui where the user can select image files.
     * @return Returns the image files as ArrayList
     */
    private ArrayList<File> getGenreScreenshots(){
        ArrayList<File> arrayListScreenshotFiles = new ArrayList<>();
        ArrayList<File> arrayListScreenshotFilesSelected = new ArrayList<>();
        JTextField textFieldScreenshotFile = new JTextField();
        JLabel labelMessage = new JLabel(I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.message"));
        JButton buttonBrowse = new JButton(I18n.INSTANCE.get("commonText.browse"));
        AtomicBoolean multipleFilesSelected = new AtomicBoolean(false);
        AtomicInteger numberOfScreenshotsToAdd = new AtomicInteger();
        buttonBrowse.addActionListener(actionEventSmall ->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return I18n.INSTANCE.get("commonText.imageFile.selectionType");
                    }
                };

                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle(I18n.INSTANCE.get("commonText.imageFile.selectPngFiles.fileChooser"));
                fileChooser.setMultiSelectionEnabled(true);

                int return_value = fileChooser.showOpenDialog(null);
                if (return_value == 0) {
                    final int NUMBER_OF_SCREENSHOTS = fileChooser.getSelectedFiles().length;
                    numberOfScreenshotsToAdd.set(NUMBER_OF_SCREENSHOTS);
                    File[] screenshots = fileChooser.getSelectedFiles();
                    if(NUMBER_OF_SCREENSHOTS > 1){
                        multipleFilesSelected.set(true);
                    }
                    boolean failed = false;
                    for(int i=0; i<NUMBER_OF_SCREENSHOTS; i++){
                        if(!failed){
                            if(screenshots[i].getName().contains(".png")){
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                                if(multipleFilesSelected.get()){
                                    arrayListScreenshotFilesSelected.add(screenshots[i]);
                                    textFieldScreenshotFile.setText(I18n.INSTANCE.get("commonText.multipleFilesSelected"));
                                }else{
                                    textFieldScreenshotFile.setText(fileChooser.getSelectedFile().getPath());
                                }
                            }else{
                                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectOnlyPngFile"));
                                failed = true;
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                            }
                        }
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
        Object[] params = {labelMessage,textFieldScreenshotFile, buttonBrowse};
        if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("frame.title.addScreenshot"), JOptionPane.OK_CANCEL_OPTION) == 0){
            String textFieldPath = textFieldScreenshotFile.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    arrayListScreenshotFiles.add(new File(textFieldPath));
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFileAdded"));
                }else{
                    JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.doesNotExist"), I18n.INSTANCE.get("frame.title.fileNotFound"), JOptionPane.ERROR_MESSAGE);
                }
            }else if(multipleFilesSelected.get()){
                for(int i = 0; i< numberOfScreenshotsToAdd.get(); i++){
                    arrayListScreenshotFiles.add(arrayListScreenshotFilesSelected.get(i));
                }
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("dialog.genreHelper.getGenreScreenshots.imageFilesAdded"));
            }else{
                JOptionPane.showMessageDialog(new Frame(), I18n.INSTANCE.get("commonText.imageFile.selectPngFile"));

            }
            return arrayListScreenshotFiles;
        }
        return arrayListScreenshotFiles;
    }

    private String convertMonthNumberToMonthName(int monthNumber){
        switch(monthNumber){
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
        }
        return "JAN";
    }

    private String getRandomTargetGroup(){
        StringBuilder stringBuilder = new StringBuilder();
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<KID>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<TEEN>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<ADULT>");
        }
        if(Utils.getRandomNumber(0, 10) > 5){
            stringBuilder.append("<OLD>");
        }
        return stringBuilder.toString();
    }

    private Integer[] getRandomWorkPriorityValues(){
        int sum = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int numberOfTries = 1;
        while(sum !=100){
            a = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            b = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            c = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            d = Utils.roundToNextFive(Utils.getRandomNumber(5,50));
            sum = a+b+c+d;
            numberOfTries++;
        }
        LOGGER.info("Number of tries to find random work priority values that work: " + numberOfTries);
        return new Integer[]{a, b, c, d};
    }

    private Integer[] getRandomDesignFocusValues(){
        int sum = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int g = 0;
        int h = 0;
        int numberOfTries = 1;
        while(sum != 40){
            a = Utils.getRandomNumber(0, 10);
            b = Utils.getRandomNumber(0, 10);
            c = Utils.getRandomNumber(0, 10);
            d = Utils.getRandomNumber(0, 10);
            e = Utils.getRandomNumber(0, 10);
            f = Utils.getRandomNumber(0, 10);
            g = Utils.getRandomNumber(0, 10);
            h = Utils.getRandomNumber(0, 10);
            sum = a+b+c+d+e+f+g+h;
            numberOfTries++;
        }
        LOGGER.info("Number of tries to find random design focus values that work: " + numberOfTries);
        return new Integer[]{a, b, c, d, e, f, g, h};
    }


    private String getRandomGenreCombs(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer integer : ModManager.genreMod.getAnalyzer().getActiveIds()){
            if(Utils.getRandomNumber(1,10) > 5){
                stringBuilder.append("<").append(integer).append(">");
            }
        }
        return stringBuilder.toString();
    }

    public HashSet<Integer> getRandomThemeIds(){
        HashSet<Integer> hashSet = new HashSet<>();
        for(Integer integer : ThemeFileAnalyzer.getThemeIdsInUse()){
            if(Utils.getRandomNumber(1,100) > 30){
                hashSet.add(integer);
            }
        }
        return hashSet;
    }

    /**
     * @returns Returns a list containing the bad gameplay features in the first HashSet and the good gameplay features in the second HashSet
     */
    private List<HashSet<Integer>> getRandomGameplayFeatureIds(){
        HashSet<Integer> hashSetBadGameplayFeatures = new HashSet<>();
        HashSet<Integer> hashSetGoodGameplayFeatures = new HashSet<>();
        for(String string : ModManager.gameplayFeatureMod.getAnalyzer().getContentByAlphabet()){
            if(Utils.getRandomNumber(1,100) > 80){
                hashSetBadGameplayFeatures.add(ModManager.gameplayFeatureMod.getAnalyzer().getContentIdByName(string)-1);
            }
            if(Utils.getRandomNumber(1,100) > 80){
                hashSetGoodGameplayFeatures.add(ModManager.gameplayFeatureMod.getAnalyzer().getContentIdByName(string)-1);
            }
        }
        Collections.disjoint(hashSetBadGameplayFeatures, hashSetGoodGameplayFeatures);
        hashSetBadGameplayFeatures.removeAll(hashSetGoodGameplayFeatures);
        List<HashSet<Integer>> list = new ArrayList<>();
        list.add(hashSetBadGameplayFeatures);
        list.add(hashSetGoodGameplayFeatures);
        return list;
    }

    private String getCompatibleThemes(HashSet<Integer> themeIds){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Integer, String> entry : ModManager.themeMod.getAnalyzerEn().getFileContent().entrySet()){
            if(themeIds.contains(entry.getKey())){
                stringBuilder.append("<").append(entry.getValue()).append(">");
            }
        }
        return stringBuilder.toString();
    }

    private void setGameplayFeatureCompatibility(Map<String, String> map1, HashSet<Integer> badGameplayFeatures, HashSet<Integer> goodGameplayFeatures){
        StringBuilder gameplayFeaturesBad = new StringBuilder();
        StringBuilder gameplayFeaturesGood = new StringBuilder();
        for(Map<String, String> map : ModManager.gameplayFeatureMod.getAnalyzer().getFileContent()){
            for(Map.Entry<String, String> entry : map.entrySet()){
                for(Integer integer : badGameplayFeatures){
                    if(entry.getKey().equals("ID")){
                        if(entry.getValue().equals(Integer.toString(integer))){
                            gameplayFeaturesBad.append("<").append(ModManager.gameplayFeatureMod.getAnalyzer().getContentNameById(integer)).append(">");
                            if(Settings.enableDebugLogging){
                                LOGGER.info("Gameplay feature bad: " + entry.getKey() + " | " + entry.getValue());
                            }
                        }
                    }
                }
                for(Integer integer : goodGameplayFeatures){
                    if(entry.getKey().equals("ID")){
                        if(entry.getValue().equals(Integer.toString(integer))){
                            gameplayFeaturesGood.append("<").append(ModManager.gameplayFeatureMod.getAnalyzer().getContentNameById(integer)).append(">");
                            if(Settings.enableDebugLogging){
                                LOGGER.info("Gameplay feature good: " + entry.getKey() + " | " + entry.getValue());
                            }
                        }
                    }
                }
            }
        }
        map1.put("GAMEPLAYFEATURE BAD", gameplayFeaturesBad.toString());
        map1.put("GAMEPLAYFEATURE GOOD", gameplayFeaturesGood.toString());
    }
}
