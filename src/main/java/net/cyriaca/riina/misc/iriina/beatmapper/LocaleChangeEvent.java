package net.cyriaca.riina.misc.iriina.beatmapper;

import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

public class LocaleChangeEvent {

    private Locale newLocale;

    public LocaleChangeEvent(Locale newLocale) {
        this.newLocale = newLocale;
    }

    public Locale getNewLocale() {
        return newLocale;
    }

}
