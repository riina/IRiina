package net.cyriaca.riina.misc.iriina.editor.ui;

import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

public interface IViewFrame {

    void setupAndShowFrame();

    void cleanupAndHideFrame();

    void localizeStrings(Locale l);
}
