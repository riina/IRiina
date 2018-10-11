package net.cyriaca.riina.misc.iriina.generic.localization;

import net.cyriaca.riina.misc.iriina.generic.MissingResourceException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LocaleSystem {

    public static final String DEFAULT_LOCALE_ID = "EN_us";
    private static final String LIST_LOC = "lang/locales.json";
    private static Locale instance = null;
    private static Map<String, LocaleDefinition> localeDefinitions = null;

    public static Locale getInstance() {
        if (instance == null) {
            try {
                instance = loadLocale(DEFAULT_LOCALE_ID);
            } catch (InvalidLocaleException | MissingResourceException e) {
                e.printStackTrace();
                System.exit(1131);
            }
        }
        return instance;
    }

    public static Locale loadLocale(String localeId) throws InvalidLocaleException, MissingResourceException {
        if (localeId == null)
            throw new IllegalArgumentException("Locale ID cannot be null!");
        instance = new Locale(localeId);
        LocaleDefinition ld = getLocales().get(localeId);
        if (ld == null) {
            System.err.println("Invalid locale used!");
            System.exit(100);
        }
        String file = ld.getResourcePath();
        if (file == null)
            throw new InvalidLocaleException(localeId);
        InputStream stream = null;
        try {
            stream = LocaleSystem.class.getModule().getResourceAsStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream == null)
            throw new MissingResourceException(localeId);
        JsonReader reader = Json.createReader(stream);
        JsonObject obj = reader.readObject();
        Set<String> s = obj.keySet();
        String[] arr = new String[s.size()];
        s.toArray(arr);
        for (String anArr : arr) {
            instance.setKey(anArr, obj.getString(anArr));
        }
        return instance;
    }

    public static Map<String, LocaleDefinition> getLocales() {
        if (localeDefinitions == null) {
            localeDefinitions = new TreeMap<>();
            InputStream stream = null;
            try {
                stream = LocaleSystem.class.getModule().getResourceAsStream(LIST_LOC);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (stream == null) {
                System.err.println("Fatal error: valid locale list resource " + LIST_LOC + " could not be found!");
                System.exit(1131);
            }
            JsonReader reader = Json.createReader(stream);
            JsonArray arr = reader.readArray();
            for (int i = 0; i < arr.size(); i++) {
                JsonObject obj = arr.getJsonObject(i);
                String localeId = obj.getString("id");
                String localizedLocaleName = obj.getString("localizedLocaleName");
                String resourcePath = obj.getString("resourcePath");
                if (localeId == null) {
                    System.err.println("Fatal error: incomplete locale definition, has no localeId!");
                    System.exit(11);
                } else if (localizedLocaleName == null) {
                    System.err.println("Fatal error: incomplete locale definition, has no localizedLocaleName!");
                    System.exit(12);
                } else if (resourcePath == null) {
                    System.err.println("Fatal error: incomplete locale definition, has no resourcePath!");
                    System.exit(13);
                }
                LocaleDefinition def = new LocaleDefinition(localeId, localizedLocaleName, resourcePath);
                localeDefinitions.put(localeId, def);
            }
        }
        return Collections.unmodifiableMap(localeDefinitions);
    }

    public static String[] getValidLocales() {
        Set<String> s = getLocales().keySet();
        String[] out = new String[s.size()];
        s.toArray(out);
        return out;
    }

}
