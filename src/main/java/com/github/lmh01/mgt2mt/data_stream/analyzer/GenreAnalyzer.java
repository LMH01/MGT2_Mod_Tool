package com.github.lmh01.mgt2mt.data_stream.analyzer;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreAnalyzer extends AbstractAdvancedAnalyzer implements GenreAnalyzerInterface{
    List<Map<String, String>> fileContent;
    int maxId = 0;

    @Override
    public List<Map<String, String>> getFileContent() {
        return fileContent;
    }

    @Override
    public void analyzeFile() throws IOException {
        fileContent = getAnalyzedFile();
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public void setMaxId(int id) {
        maxId = id;
    }

    @Override
    public File getFileToAnalyze() {
        return Utils.getGenreFile();
    }

    @Override
    public String getAnalyzerType() {
        return I18n.INSTANCE.get("commonText.genre.upperCase");
    }

    @Override
    public String getMainTranslationKey() {
        return "genre";
    }

    @Override
    public String getDefaultContentFile() {
        return "default_genres.txt";
    }

    @Override
    public ArrayList<Integer> getGenreIdsInUse() {
        try {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for(Map<String, String> map : getAnalyzedFile()){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    if(entry.getKey().equals("ID")){
                        arrayList.add(Integer.parseInt(entry.getValue()));
                    }
                }
            }
            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public String getContentNameById(int id, boolean changeableLanguage) {
        try {
            List<Map<String, String>> list = getAnalyzedFile();
            for(int i=0; i<list.size(); i++){
                if(list.get(i).get("ID").equals(Integer.toString(id))){
                    if(changeableLanguage){
                        if(Settings.language.equals("English")){
                            return list.get(i).get("NAME EN");
                        }else if(Settings.language.equals("Deutsch")){
                            return list.get(i).get("NAME GE");
                        }
                    }else{
                        return list.get(i).get("NAME EN");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Genre not available";
    }

}
