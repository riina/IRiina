package net.cyriaca.riina.misc.iriina.generic.localization;

public class InvalidLocaleException extends Exception {

    public InvalidLocaleException(String locale) {
        super("Locale " + locale + " not found!");
    }
}
