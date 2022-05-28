package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.manager.DefaultContentManager;
import com.moandjiezana.toml.Toml;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ReadDefaultContent {

    public static final Toml toml;

    static {
        toml = new Toml().read(DefaultContentManager.DEFAULT_CONTENT_FILE);
    }

    /**
     * Searches "appdata/roaming/LMH01/MGT2_Mod_Tool/default_content.toml" for the default content name. If found returns the content of that array.
     *
     * @param defaultContentName The name that should be searched in the file.
     * @return Returns an array containing the content of the input file.
     * @throws IOException If the default content file does not exist or could not be read.
     */
    public static String[] getDefault(String defaultContentName) throws IOException {
        return getDefault(defaultContentName, null);
    }

    /**
     * Searches "appdata/roaming/LMH01/MGT2_Mod_Tool/default_content.toml" for the default content name. If found returns the content of that array.
     *
     * @param defaultContentName The name that should be searched in the file.
     * @param replacer           The function that is used to replace specific parts of the input.
     * @return Returns an array containing the content of the input file.
     * @throws IOException If the default content file does not exist or could not be read.
     */
    public static String[] getDefault(String defaultContentName, Replacer replacer) throws IOException {
        ArrayList<Object> arrayList = new ArrayList<>();
        List<Object> list = toml.getList(defaultContentName.replace(".txt", ""));
        if (list == null) {
            Files.delete(DefaultContentManager.DEFAULT_CONTENT_FILE.toPath());
        }
        for (Object obj : list) {
            if (replacer != null) {
                arrayList.add(replacer.replace(obj.toString()));
            } else {
                arrayList.add(obj);
            }
        }
        String[] strings = new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
    }
}
