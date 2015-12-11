package ch.fhnw.itprojekt.noobsquad.client.splashScreen;

import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.client.abstractClasses.Controller;
import javafx.concurrent.Worker;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
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
        model.initializer.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                        main.startApp();
                });
    }
}
