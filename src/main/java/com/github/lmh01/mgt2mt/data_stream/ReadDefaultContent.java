package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.manager.DefaultContentManager;
import com.moandjiezana.toml.Toml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadDefaultContent {

    /**
     * Searches "appdata/roaming/LMH01/MGT2_Mod_Tool/default_content.toml" for the default content name. If found returns the content of that array.
     * @param defaultContentName The name that should be searched in the file.
     * @return Returns an array containing the content of the input file.
     */
    public static String[] getDefault(String defaultContentName) throws IOException {
        return getDefault(defaultContentName, null);
    }

    /**
     * Searches "appdata/roaming/LMH01/MGT2_Mod_Tool/default_content.toml" for the default content name. If found returns the content of that array.
     * @param defaultContentName The name that should be searched in the file.
     * @param replacer The function that is used to replace specific parts of the input.
     * @return Returns an array containing the content of the input file.
     */
    public static String[] getDefault(String defaultContentName, Replacer replacer) throws IOException {
        ArrayList<Object> arrayList = new ArrayList<>();
        Toml toml = new Toml().read(DefaultContentManager.DEFAULT_CONTENT_FILE);
        List<Object> list = toml.getList(defaultContentName.replace(".txt", ""));
        for(Object obj: list) {
            if(replacer != null){
                arrayList.add(replacer.replace(obj.toString()));
            }else{
                arrayList.add(obj);
            }
        }
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }

    /**
     * @return Returns a string containing the contents of the file. The file will be searched in "src/main/resources/default_content".
     * Use {@link ReadDefaultContent#getDefault(String)} instead, if you would like to get the custom content that is saved in appdata.
     */
    public static String[] getDefaultFromSystemResource(String fileName) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("default_content/" + fileName)), StandardCharsets.UTF_8));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            arrayList.add(currentLine);
        }
        reader.close();
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }
}
