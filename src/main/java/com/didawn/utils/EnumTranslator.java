package com.didawn.utils;

import static java.util.ResourceBundle.getBundle;

import java.util.ResourceBundle;

/**
 *
 * @author fabier
 */
public class EnumTranslator {

    private final ResourceBundle resourceBundle;

    /**
     *
     * @param resourceBundleName
     */
    public EnumTranslator(String resourceBundleName) {
	this.resourceBundle = getBundle(resourceBundleName);
    }

    /**
     *
     * @param e
     * @return
     */
    public String translate(Enum<?> e) {
	String key = e.getClass().getSimpleName() + '.' + e.name();
	return this.resourceBundle.getString(key);
    }
}
