package com.github.lmh01.mgt2mt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for localizing strings.
 */
public class I18n {
    public static I18n INSTANCE = new I18n();
    static {
        try {
            INSTANCE.parseLocale("en", ClassLoader.getSystemResourceAsStream("locale/en.txt"));
            INSTANCE.parseLocale("de", ClassLoader.getSystemResourceAsStream("locale/de.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Map<String, Map<String, String>> locale = new HashMap<>();
    String currentLocale = "en";
    String fallbackLocale = "en";

    /**
     * Reads a locale formatted like `key|value`
     * @param language the language ID to save the locale is (for example "en")
     * @param in the stream to read from
     * @throws IOException if an error occurred reading the stream
     */
    public void parseLocale(String language, InputStream in) throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        Map<String, String> map = new HashMap<>();
        String line;
        while ((line = read.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\|", 2);
            map.put(parts[0], parts[1]);
        }

        this.locale.put(language, map);
    }

    /**
     * Sets the locale that will be used to localize keys
     * @param currentLocale the locale to use
     */
    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;
    }

    /**
     * Sets the locale that will be used to localize keys if currentLocale doesn't have them
     * @param fallbackLocale the locale to use
     */
    public void setFallbackLocale(String fallbackLocale) {
        this.fallbackLocale = fallbackLocale;
    }

    /**
     * Localizes the given localization key
     * @param key the key by which the localization should be looked up
     * @return the localized value
     */
    public String get(String key) {
        Map<String, String> loc = locale.getOrDefault(currentLocale, locale.get(fallbackLocale));
        if (loc.containsKey(key))
            return loc.get(key);

        return locale.get(fallbackLocale).getOrDefault(key, key);
    }
}
