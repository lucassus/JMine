package org.lucassus.jmine.enums;

import java.util.Locale;

public enum Language {

    ENGLISH("en", "GB"),
    POLISH("pl", "PL");

    private Locale locale;

    Language(String language, String country) {
        locale = new Locale(language, country);
    }

    public Locale getLocale() {
        return locale;
    }

}
