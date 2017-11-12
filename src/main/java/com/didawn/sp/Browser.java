package com.didawn.sp;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author fabier
 */
@SuppressWarnings("restriction")
public class Browser extends Region {

    final WebView webBrowser = new WebView();
    final WebEngine webEngine = webBrowser.getEngine();

    /**
     *
     * @param url
     */
    public Browser(String url) {
	// apply the styles
	getStyleClass().add("browser");
	// load the web page
	webEngine.load(url);
	// add the web view to the scene
	getChildren().add(webBrowser);
    }

    /**
     *
     */
    @Override
    protected void layoutChildren() {
	double w = getWidth();
	double h = getHeight();
	layoutInArea(webBrowser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    /**
     *
     * @param height
     * @return
     */
    @Override
    protected double computePrefWidth(double height) {
	return 750;
    }

    /**
     *
     * @param width
     * @return
     */
    @Override
    protected double computePrefHeight(double width) {
	return 500;
    }
}
