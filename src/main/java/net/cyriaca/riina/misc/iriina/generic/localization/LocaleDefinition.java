package net.cyriaca.riina.misc.iriina.generic.localization;

public class LocaleDefinition {

    private String localeId;
    private String localizedLocaleName;
    private String resourcePath;

    public LocaleDefinition(String localeId, String localizedLocaleName, String resourcePath) {
        this.localeId = localeId;
        this.localizedLocaleName = localizedLocaleName;
        this.resourcePath = resourcePath;
    }

    public String getLocaleId() {
        return localeId;
    }

    public String getLocalizedLocaleName() {
        return localizedLocaleName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String toString() {
        return localeId + " - " + localizedLocaleName;
    }
}
