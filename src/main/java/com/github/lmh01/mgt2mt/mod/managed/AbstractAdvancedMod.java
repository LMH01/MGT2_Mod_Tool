package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.data_stream.ReadDefaultContent;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//TODO Klasse aufräumen -> Funktionen schön sortieren

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files with the system "[Key]Value".
 */
public abstract class AbstractAdvancedMod extends AbstractBaseMod {

    List<Map<String, String>> fileContent;

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addMod(T t) throws ModProcessingException {
        try {
            //This map contains the contents of the mod that should be added
            Map<String, String> map = (Map<String, String>) t;
            analyzeFile();
            sendLogMessage("Adding new " + getType() + ": " + map.get("NAME EN"));
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
                printValues(fileContent, bw);
                bw.write(System.getProperty("line.separator"));
            }
            printValues(map, bw);
            bw.write(System.getProperty("line.separator"));
            bw.write("[EOF]");
            bw.close();
        } catch (ClassCastException e) {
            throw new ModProcessingException("T is invalid: Should be Map<String, String>", true);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while performing an IO operation: " + e.getMessage());
        }
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {
        try {
            analyzeFile();
            int modId = getContentIdByName(name);
            sendLogMessage("Removing " + getType() + ": " + name);
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
                if (Integer.parseInt(fileContent.get("ID")) != modId) {
                    printValues(fileContent, bw);
                    bw.write(System.getProperty("line.separator"));
                }
            }
            bw.write("[EOF]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + getType() + " - " + name);
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while performing an IO operation: " + e.getMessage());
        }
    }

    @Override
    public final void analyzeFile() throws IOException {
        fileContent = DataStreamHelper.parseDataFile(getGameFile());
        int currentMaxId = 0;
        for (Map<String, String> map : fileContent) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("ID")) {
                    try{
                        int currentId = Integer.parseInt(entry.getValue());
                        if (currentMaxId < currentId) {
                            currentMaxId = currentId;
                        }
                    }catch(NumberFormatException e){
                        throw new IOException(I18n.INSTANCE.get("errorMessages.gameFileCorrupted"));
                    }
                }
            }
        }
        setMaxId(currentMaxId);
    }

    /**
     * @return The analyzed file content: {@literal List<Map<String, String>>}
     */
    public final List<Map<String, String>> getFileContent(){
        return fileContent;
    }

    @Override
    public String[] getContentByAlphabet() {
        ArrayList<String> arrayListAvailableThingsSorted = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME EN")){
                    arrayListAvailableThingsSorted.add(entry.getValue());
                }
            }
        }
        Collections.sort(arrayListAvailableThingsSorted);
        String[] string = new String[arrayListAvailableThingsSorted.size()];
        arrayListAvailableThingsSorted.toArray(string);
        return string;
    }

    @Override
    public int getFileContentSize() {
        return getFileContent().size();
    }

    @Override
    public final int getContentIdByName(String name) throws ModProcessingException{
        for(Map<String, String> map : getFileContent()){
            if(map.get("NAME EN").equals(name)){
                return Integer.parseInt(map.get("ID"));
            }
        }
        throw new ModProcessingException("The mod id for mod '" + name + "' was not found");
    }

    /**
     * @param contentNameEn The content for which the map should be returned.
     * @return A map containing all values for the specified content.
     */
    public Map<String, String> getSingleContentMapByName(String contentNameEn) throws ModProcessingException {
        List<Map<String, String>> list = getFileContent();
        String idToSearch = Integer.toString(getContentIdByName(contentNameEn));
        Map<String, String> mapSingleContent = null;
        for(Map<String, String> map : list){
            if(map.get("ID").equals(idToSearch)){
                mapSingleContent = map;
            }
        }
        return mapSingleContent;
    }

    /**
     * @param genreId The content id for which the position should be returned
     * @return The position in the fileContent list where the input id is stored in.
     */
    public int getPositionInFileContentListById(int genreId) throws ModProcessingException {
        for(int i=0; i<getFileContent().size(); i++){
            if(getFileContent().get(i).get("ID").equals(Integer.toString(genreId))){
                return i;
            }
        }
        throw new ModProcessingException("The genre id '" + genreId + "' was not found in the file content list!");
    }

    /**
     * @return An array list containing all active ids for this mod
     */
    public ArrayList<Integer> getActiveIds() {
        ArrayList<Integer> activeIds = new ArrayList<>();
        for(Map<String, String> map : getFileContent()){
            try{
                activeIds.add(Integer.parseInt(map.get("ID")));
            } catch (NumberFormatException ignored){

            }
        }
        return activeIds;
    }

    @Override
    public String[] getDefaultContent() {
        if(defaultContent.length == 0){
            try {
                defaultContent = ReadDefaultContent.getDefault(getDefaultContentFileName());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles") + " " + e.getMessage(), I18n.INSTANCE.get("analyzer." + getMainTranslationKey() + ".getCustomContentString.errorWhileScanningDefaultFiles"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return defaultContent;
    }

    /**
     * This function is called by {@link AbstractAdvancedMod#addMod(Object)}. The values that are stored in the map will be written to the file by the buffered writer.
     * @throws IOException Is thrown if something went wrong when the file is being written
     */
    protected abstract void printValues(Map<String, String> map, BufferedWriter bw) throws IOException;
}
