package com.github.lmh01.mgt2mt.data_stream.editor;

import com.github.lmh01.mgt2mt.data_stream.analyzer.managed.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.editor.managed.AbstractAdvancedEditor;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.EditHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PublisherEditor extends AbstractAdvancedEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherEditor.class);

    /**
     * Do not use this method!
     * Use {@link PublisherEditor#addMod(Map, String)} instead!
     * @param map The values that stand in this map are used to print the file. This includes the translations.
     * @deprecated Use {@link PublisherEditor#addMod(Map, String)} instead!
     */
    @Override
    @Deprecated
    public void addMod(Map<String, String> map) throws IOException {
        addMod(map, "");
    }

    public void addMod(Map<String, String> map, String publisherImageFilePath) throws IOException {
        super.addMod(map);
        if(!publisherImageFilePath.equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png")){
            File publisherImageFileToCopy = new File(publisherImageFilePath);
            File publisherImageFileTarget = new File(Utils.getCompanyLogosPath() + "//" + map.get("PIC") + ".png");
            Files.copy(Paths.get(publisherImageFileToCopy.getPath()), Paths.get(publisherImageFileTarget.getPath()));
        }
    }

    @Override
    public void removeMod(String name) throws IOException {
        super.removeMod(name);
        int iconId = getPublisherIconIdByName(name);
        if(iconId>146){
            File publisherIcon = new File(Utils.getMGT2CompanyLogosPath() + iconId + ".png");
            LOGGER.info("publisherIcon: " + publisherIcon.getPath());
            if(publisherIcon.exists()){
                publisherIcon.delete();
                LOGGER.info("Image file for publisher " + name + " has been removed.");
            }
        }
    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.publisher");
    }

    @Override
    public File getFileToEdit() {
        return ModManager.publisherMod.getFile();
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        EditHelper.printLine("ID", map, bw);
        TranslationManager.printLanguages(bw, map);
        EditHelper.printLine("DATE", map, bw);
        EditHelper.printLine("PIC", map, bw);
        EditHelper.printLine("DEVELOPER", map, bw);
        EditHelper.printLine("PUBLISHER", map, bw);
        EditHelper.printLine("MARKET", map, bw);
        EditHelper.printLine("SHARE", map, bw);
        EditHelper.printLine("GENRE", map, bw);
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return ModManager.publisherMod.getAnalyzer();
    }

    private int getPublisherIconIdByName(String publisherNameEN){
        int returnValue = 0;
        List<Map<String, String>> list = ModManager.publisherMod.getAnalyzer().getFileContent();
        try{
            for (Map<String, String> map : list) {
                if (map.get("NAME EN").equals(publisherNameEN)) {
                    returnValue = Integer.parseInt(map.get("PIC"));
                }
            }
        }catch(NullPointerException ignored){

        }
        return returnValue;
    }

    /**
     * Removes the specified genre from the publisher file.
     * If the genre is found another genre id is randomly assigned
     * If the genre is not found, nothing happens
     * @param name The genre that should be removed
     */
    public void removeGenre(String name) throws IOException {
        int genreId = ModManager.genreMod.getAnalyzer().getContentIdByName(name);
        getAnalyzer().analyzeFile();
        sendLogMessage("Replacing genre id in publisher file: " + name);
        Charset charset = getCharset();
        File fileToEdit = getFileToEdit();
        if(fileToEdit.exists()){
            fileToEdit.delete();
        }
        fileToEdit.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToEdit), charset));
        if(charset.equals(StandardCharsets.UTF_8)){
            bw.write("\ufeff");
        }
        for(Map<String, String> fileContent : getAnalyzer().getFileContent()){
            if (Integer.parseInt(fileContent.get("GENRE")) == genreId) {
                fileContent.remove("GENRE");
                fileContent.put("GENRE", Integer.toString(ModManager.genreMod.getAnalyzer().getActiveIds().get(Utils.getRandomNumber(0, ModManager.genreMod.getAnalyzer().getActiveIds().size()))));
            }
            printValues(fileContent, bw);
            bw.write(System.getProperty("line.separator"));
        }
        bw.write("[EOF]");
        bw.close();
    }
}
