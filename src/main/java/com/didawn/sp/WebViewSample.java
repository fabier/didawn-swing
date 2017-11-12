package com.didawn.sp;

import static javafx.scene.paint.Color.web;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fabier
 */
@SuppressWarnings("restriction")
public class WebViewSample extends Application {

    private Scene scene;

    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
	// create the scene
	stage.setTitle("Authentication");
	Parameters parameters = getParameters();
	scene = new Scene(new Browser(parameters.getRaw().get(0)), 750, 500, web("#bdbdbd"));
	stage.setScene(scene);
	stage.show();
    }
}
