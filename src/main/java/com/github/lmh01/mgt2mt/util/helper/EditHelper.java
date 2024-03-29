package com.github.lmh01.mgt2mt.util.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class EditHelper {
    /**
     * Prints one line using the buffered writer.
     * They output fill be like this: [mapKey]mapValue
     * A line break is written.
     * The line is only written if the key exists in the map.
     *
     * @param mapKey The key that should be printed to the file
     * @param map    The map where the values are stored
     */
    public static void printLine(String mapKey, Map<String, String> map, BufferedWriter bw) throws IOException {
        if (map.containsKey(mapKey)) {
            bw.write("[" + mapKey + "]" + map.get(mapKey));
            bw.write("\r\n");
        }
    }
}
