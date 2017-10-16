package com.didawn.utils;

import java.util.ResourceBundle;

public class EnumTranslator {

    private final ResourceBundle resourceBundle;

    public EnumTranslator(String resourceBundleName) {
        this.resourceBundle = ResourceBundle.getBundle(resourceBundleName);
    }

    public String translate(Enum e) {
        String key = e.getClass().getSimpleName() + '.' + e.name();
        return this.resourceBundle.getString(key);
    }
}
