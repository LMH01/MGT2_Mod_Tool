package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.content.managed.types.DataType;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Represents a single data line in the games .txt file.
 * This does not store the value of this line, this class is just used to specify what data should be and is used and to
 * organize things in {@link AbstractBaseContentManager} and {@link BaseContentManager}.
 */
public class DataLine {

    final Logger logger = LoggerFactory.getLogger(DataLine.class);

    /**
     * The key of this data entry, this the value between the [] in the games files.
     */
    final String key;

    /**
     * true = this data entry is required for the game to work
     * false = this data entry is optional and not required for the game to work
     */
    final boolean required;

    /**
     * The type of which this data line is.
     */
    final DataType dataType;

    /**
     * Constructs a new data line
     *
     * @param key      {@link DataLine#key}
     * @param required {@link DataLine#required}
     * @param dataType How this data line should be interpreted. If this interpretation fails an integrity violation
     *                 will be shown at startup.
     */
    public DataLine(String key, boolean required, DataType dataType) {
        this.key = key;
        this.required = required;
        this.dataType = dataType;
    }

    /**
     * Uses the map to retrieve the value of this data line. The retrieved value is the written by the buffered writer.
     *
     * @param map The map that contains the value behind this data line
     * @param bw  The buffered writer that writes the message
     */
    public void print(Map<String, String> map, BufferedWriter bw) throws IOException {
        if (map.containsKey(key)) {
            bw.write("[" + key + "]" + map.get(key));
            bw.write("\r\n");
        } else {
            if (required) {
                DebugHelper.warn(logger, "The value for key " + key + " does not exist in the map, the value was not printed. This can lead to problems!");
            }
        }
    }
}
