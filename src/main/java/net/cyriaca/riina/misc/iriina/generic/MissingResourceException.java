package net.cyriaca.riina.misc.iriina.generic;

public class MissingResourceException extends Exception {

    public MissingResourceException(String resource) {
        super("Resource " + resource + " could not be found!");
    }
}
