package de.hsharz.abgabeverwaltung;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Language {

    public static final String    UNDEFIND       = "undefined";
    public static final Locale    DEFAULT_LOCALE = Locale.GERMAN;

    private static ResourceBundle bundle;
    private static Locale         locale         = DEFAULT_LOCALE;

    public static void changeLocale(final Locale newLocale) {
        locale = Objects.requireNonNull(newLocale);
        bundle = ResourceBundle.getBundle("localization.MyResource", locale);
    }

    public static String getString(final String text) {
        if (getBundle().containsKey(text)) {
            return getBundle().getString(text);
        }
        return UNDEFIND;
    }

    private static ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("localization.MyResource", locale);
        }
        return bundle;
    }

}
