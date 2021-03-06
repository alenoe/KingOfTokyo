package ch.fhnw.itprojekt.noobsquad.client.main;

import ch.fhnw.itprojekt.noobsquad.client.board.Board_Controller;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_Model;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_View;
import ch.fhnw.itprojekt.noobsquad.client.login.Login_Controller;
import ch.fhnw.itprojekt.noobsquad.client.login.Login_Model;
import ch.fhnw.itprojekt.noobsquad.client.login.Login_View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ch.fhnw.itprojekt.noobsquad.client.splashScreen.Splash_Controller;
import ch.fhnw.itprojekt.noobsquad.client.splashScreen.Splash_Model;
import ch.fhnw.itprojekt.noobsquad.client.splashScreen.Splash_View;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * 
 * Parts of this code are modified or added by Raphael Denz. (2015 - Team NoobSquad)
 * 
 * @author Brad Richards
 * @author Raphael Denz
 */
public class JavaFX_App_Template extends Application {
	private static JavaFX_App_Template mainProgram; // singleton
	private Login_View loginView;
	private Splash_View splashView;
	public static String username;
	public static String iP;
	public static int port;
	private Board_View boardView;

	private ServiceLocator serviceLocator; // resources, after initialization

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Note: This method is called on the main thread, not the JavaFX
	 * Application Thread. 
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
     * Application Thread, so we can display a GUI. We have two initial GUIs:
     * the applications login-window and a splash screen. Both of these follow
     * the MVC model.
     * 
     * We first display the login-window (start()). Here we get all the information
     * needed to connect to the server (or at least where it should). If the user 
     * pushes "connect", the model opens the splash screen (startSplash()). This 
     * model is where all initialization for the application (the Board) takes place.
     * The controller updates a progress-bar in the view, and (after initialization
     * is finished) calls the startApp() method in this class.
     * 
     * If the method restart() is called, the boardView closes and the loginView will
     * be activated again.
     */
	
    // Opening loginView
	@Override
	public void start(Stage primaryStage) {
		// Create and display the splash screen and model
		Login_Model loginModel = new Login_Model();
		loginView = new Login_View(primaryStage, loginModel);
		new Login_Controller(this, loginModel, loginView);
		loginView.start();
	}

    // Closing boardView and reopening loginView
	public void restart() {
		boardView.stop();
		boardView = null;
		Stage stage = new Stage();
		// Create and display the splash screen and model
		Login_Model loginModel = new Login_Model();
		loginView = new Login_View(stage, loginModel);
		new Login_Controller(this, loginModel, loginView);
		loginView.start();
	}

	// Starting splashScreen, closing loginView and initializing resources.
	public void startSplash() {
		Stage splashStage = new Stage();
		// Create and display the splash screen and model
		Splash_Model splashModel = new Splash_Model();
		splashView = new Splash_View(splashStage, splashModel);
		new Splash_Controller(this, splashModel, splashView);
		splashView.start();

		loginView.stop();
		loginView = null;

		// Display the splash screen and begin the initialization
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
		Stage appStage = new Stage();
		// Resources are now initialized

		// Initialize the application MVC components. Note that these components
		// can only be initialized now, because they may depend on the
		// resources initialized by the splash screen

		System.out.println("BLABLA");
		// Create and display the splash screen and model
		Board_Model boardModel = new Board_Model();
		boardView = new Board_View(appStage, boardModel);
		new Board_Controller(mainProgram, boardModel, boardView);

		splashView.stop();
		splashView = null;

		// Resources are now initialized
		serviceLocator = ServiceLocator.getServiceLocator();

		boardView.start();
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
		if (serviceLocator != null) {
			serviceLocator.getConfiguration().save(); // Saving configurations
			if (boardView != null) { // checking if the boardView is still open
				// Make the view invisible
				boardView.stop();
			}
			// More cleanup code as needed
			serviceLocator.getLogger().info("Application terminated");
		} else if (loginView != null) { // checking if the loginView is still open
			// Make the view invisible
			loginView.stop();
			System.out.println("Application terminated");
		}
	}

	// Static getter for a reference to the main program object
	protected static JavaFX_App_Template getMainProgram() {
		return mainProgram;
	}
	
    /**
     * The following parameters are receiving their content
     * from the loginView. Either set from the user or
     * given by standardization. (e.g. standard port).
     * This is important so that we can access these
     * informations later again.
     */
	public static void setUsername(String s) {
		username = s;
	}

	public static String getUsername() {
		return username;
	}

	public static void setIP(String s) {
		iP = s;
	}

	public static String getIP() {
		return iP;
	}

	public static void setPort(int i) {
		port = i;
	}

	public static int getPort() {
		return port;
	}
}
