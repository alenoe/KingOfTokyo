package ch.fhnw.itprojekt.noobsquad.server.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ch.fhnw.itprojekt.noobsquad.server.splashScreen.Splash_Controller;
import ch.fhnw.itprojekt.noobsquad.server.splashScreen.Splash_Model;
import ch.fhnw.itprojekt.noobsquad.server.splashScreen.Splash_View;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_Controller;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_Model;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_View;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * Parts of this code are modified or added by Raphael Denz. (2015 - Team NoobSquad)
 * 
 * @author Brad Richards
 * @author Raphael Denz
 */
public class JavaFX_App_Template extends Application {
	private static JavaFX_App_Template mainProgram; // singleton
	private Server_View serverView;
	private Splash_View splashView;

	private ServiceLocator serviceLocator; // resources, after initialization

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Note: This method is called on the main thread, not the JavaFX
	 * Application Thread. This means that we cannot display anything to the
	 * user at this point. Since we want to show a splash screen, this means
	 * that we cannot do any real initialization here.
	 * 
	 * This implementation ensures that the application is a singleton; only one
	 * per JVM-instance. On client installations this is not necessary (each
	 * application runs in its own JVM). However, it can be important on server
	 * installations.
	 * 
	 * Why is it important that only one instance run in the JVM? Because our
	 * initialized resources are a singleton - if two programs instances were
	 * running, they would use (and overwrite) each other's resources!
	 */
	@Override
	public void init() {
		if (mainProgram == null) {
			mainProgram = this;
		} else {
			Platform.exit();
		}
	}

	/**
	 * This method is called after init(), and is called on the JavaFX
	 * Application Thread, so we can display a GUI. We have two GUIs: a splash
	 * screen and the application. Both of these follow the MVC model.
	 * 
	 * We first display the splash screen. The model is where all initialization
	 * for the application takes place. The controller updates a progress-bar in
	 * the view, and (after initialization is finished) calls the startApp()
	 * method in this class.
	 */
	@Override
	public void start(Stage primaryStage) {
		// Create and display the splash screen and model
		Splash_Model splashModel = new Splash_Model();
		splashView = new Splash_View(primaryStage, splashModel);
		new Splash_Controller(this, splashModel, splashView);
		splashView.start();

		splashModel.initialize();
	}

	/**
	 * This method is called when the splash screen has finished initializing
	 * the application. The initialized resources are in a ServiceLocator
	 * singleton. Our task is to now create the application MVC components, to
	 * hide the splash screen, and to display the application GUI.
	 * 
	 * Multitasking note: This method is called from an event-handler in the
	 * Splash_Controller, which means that it is on the JavaFX Application
	 * Thread, which means that it is allowed to work with GUI components.
	 * http://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
	 */

	public void startApp() {
		Stage serverStage = new Stage();
		// Create and display the splash screen and model

		// Resources are now initialized
		serviceLocator = ServiceLocator.getServiceLocator();

		// Initialize the application MVC components. Note that these components
		// can only be initialized now, because they may depend on the
		// resources initialized by the splash screen
		Server_Model serverModel = new Server_Model();
		serverView = new Server_View(serverStage, serverModel);
		new Server_Controller(this, serverModel, serverView);
		serverView.start();

		splashView.stop();
		splashView = null;

		// Display the splash screen and begin the initialization
	}

	/**
	 * The stop method is the opposite of the start method. It provides an
	 * opportunity to close down the program, including GUI components. If the
	 * start method has never been called, the stop method may or may not be
	 * called.
	 * 
	 * Make the GUI invisible first. This prevents the user from taking any
	 * actions while the program is ending.
	 */
	@Override
	public void stop() {

		if (serverView != null) {
			// Make the view invisible
			serverView.stop();
		}
		// More cleanup code as needed

		serviceLocator.getLogger().info("Application terminated");
		System.exit(0);
	}

	// Static getter for a reference to the main program object
	protected static JavaFX_App_Template getMainProgram() {
		return mainProgram;
	}
}
