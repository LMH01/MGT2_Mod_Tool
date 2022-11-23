package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for localizing strings.
 */
public class I18n {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18n.class);
    public static final I18n INSTANCE = new I18n();

    static {
        try {
            INSTANCE.parseLocale(Locale.EN.name(), ClassLoader.getSystemResourceAsStream("locale/en.txt"));
            INSTANCE.parseLocale(Locale.DE.name(), ClassLoader.getSystemResourceAsStream("locale/de.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final Map<String, Map<String, String>> locale = new HashMap<>();
    Locale currentLocale = Locale.EN;
    Locale fallbackLocale = Locale.EN;

    /**
     * Reads a locale formatted like `key|value`
     *
     * @param language the language ID to save the locale is (for example "en")
     * @param in       the stream to read from
     * @throws IOException if an error occurred reading the stream
     */
    public void parseLocale(String language, InputStream in) throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        Map<String, String> map = new HashMap<>();
        String line;
        while ((line = read.readLine()) != null) {
            line = line.trim();
            //line = line.replaceAll("ü", "\u00fc");
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\|", 2);
            map.put(parts[0], parts[1]);
        }

        this.locale.put(language, map);
    }

    /**
     * Sets the locale that will be used to localize keys
     *
     * @param currentLocale the locale to use
     */
    public void setCurrentLocale(Locale currentLocale) {
        LOGGER.info("Localisation set: " + currentLocale.name());
        this.currentLocale = currentLocale;
    }

    /**
     * Sets the locale that will be used to localize keys if currentLocale doesn't have them
     *
     * @param fallbackLocale the locale to use
     */
    public void setFallbackLocale(Locale fallbackLocale) {
        LOGGER.info("Fallback localisation set: " + currentLocale.name());
        this.fallbackLocale = fallbackLocale;
    }

    /**
     * Localizes the given localization key
     *
     * @param key the key by which the localization should be looked up
     * @return the localized value
     */
    public String get(String key) {
        Map<String, String> loc = locale.getOrDefault(currentLocale.name(), locale.get(fallbackLocale.name()));
        if (loc.containsKey(key)) {
            String localisation = loc.get(key);
            DebugHelper.debug(LOGGER, "Returned localisation: " + key + " | " + localisation);
            return localisation;
        }
        String fallbackLocalisation = locale.get(fallbackLocale.name()).getOrDefault(key, key);
        LOGGER.warn("Localisation for key [" + key + "] not found. Returning fallback local: " + fallbackLocalisation);
        return fallbackLocalisation;
    }

    /**
     * Returns the current locale
     */
    public Locale getCurrentLocale() {
        return this.currentLocale;
    }
}
