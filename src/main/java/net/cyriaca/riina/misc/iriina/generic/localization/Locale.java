package net.cyriaca.riina.misc.iriina.generic.localization;

import java.util.TreeMap;

public final class Locale {

    private static final String KEY_LOCALIZED_NAME = "locale_localized_name";
    private TreeMap<String, String> kvp;
    private String localeId;

    public Locale(String localeId) throws IllegalArgumentException {
        if (localeId == null)
            throw new IllegalArgumentException("localeID cannot be null!");
        this.localeId = localeId;
        kvp = new TreeMap<>();
    }

    public String getLocaleId() {
        return localeId;
    }

    public String getLocalizedLocaleName() {
        return getKey(KEY_LOCALIZED_NAME);
    }

    public String getKey(String key) {
        String val = kvp.get(key);
        if (val != null)
            return val;
        else
            return key;
    }

    void setKey(String key, String value) {
        kvp.put(key, value);
    }
}
