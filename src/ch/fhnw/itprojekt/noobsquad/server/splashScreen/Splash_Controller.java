package ch.fhnw.itprojekt.noobsquad.server.splashScreen;

import ch.fhnw.itprojekt.noobsquad.server.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Controller;
import javafx.concurrent.Worker;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * Hint: The parameter "JavaFX_App_Template main" can be used to access methods
 * that are located within the class JavaFX_App_Template.
 * 
 * @author Brad Richards
 * @author Raphael Denz
 */
public class Splash_Controller extends Controller<Splash_Model, Splash_View> {

	public Splash_Controller(final JavaFX_App_Template main, Splash_Model model, Splash_View view) {
		super(model, view);

        // We could monitor the progress property and pass it on to the progress bar
        // However, JavaFX can also do this for us: We just bind the progressProperty of the
        // progress bar to the progressProperty of the task.
		view.progress.progressProperty().bind(model.initializer.progressProperty());

        // The stateProperty tells us the status of the task. When the state is SUCCEEDED,
        // the task is finished, so we tell the main program to continue.
        
        // Using a lambda expression
		model.initializer.stateProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == Worker.State.SUCCEEDED)
				main.startApp();
		});
	}
}
