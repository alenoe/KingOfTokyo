package ch.fhnw.itprojekt.noobsquad.client.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Controller;

/**
 * 
 * @author Simon Zahnd
 *
 */

public class Login_Controller extends Controller<Login_Model, Login_View> {

	JavaFX_App_Template main;
	private boolean usernameValid = true;
	private boolean ipValid = true;
	private boolean portValid = true;
	private String k1 = "";
	private String k2 = "";
	private String k3 = "";

	public Login_Controller(JavaFX_App_Template main, Login_Model model, Login_View view) {
		super(model, view);

		this.main = main;
		view.btnConnect.setDisable(true);

		// -----------------------------------------------------------------------------------
		// Connect with the server.
		view.btnConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				buttonConnect();
			}
		});

		// -----------------------------------------------------------------------------------
		// Close window.
		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});

		// -----------------------------------------------------------------------------------
		// ChangeListener for the text-property of the Username
		view.tfUsername.textProperty().addListener(
				// Parameters of any PropertyChangeListener
				(observable, oldValue, newValue) -> {
					validateUsername(newValue);
				});

		// -----------------------------------------------------------------------------------
		// ChangeListener for the text-property of the IP
		view.tfIP.textProperty().addListener(
				// Parameters of any PropertyChangeListener
				(observable, oldValue, newValue) -> {
					validateIP(newValue);
				});

		view.tfIP.setText(model.getLokalHost());

		// -----------------------------------------------------------------------------------
		// ChangeListener for the text-property of the port number
		view.tfPort.textProperty().addListener(
				// Parameters of any PropertyChangeListener
				(observable, oldValue, newValue) -> {
					validatePortNumber(newValue);
				});

		// -----------------------------------------------------------------------------------
		// register ourselves to handle window-closing event
		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});

		view.tfPort.setText("14000");
	}

	// -----------------------------------------------------------------------------------
	// is there a Username
	private void validateUsername(String newValue) {
		view.btnConnect.setDisable(true);
		view.tfUsername.setStyle("-fx-text-inner-color: red;");
		k1 = view.tfUsername.getText();
		int kb = k1.length();
		if (kb > 1 && kb <= 30 && k1.length() > 0) {
			view.tfUsername.setStyle("-fx-text-inner-color: green;");
			usernameValid = false;
			enableDisableButton();
		}
	}

	// -----------------------------------------------------------------------------------
	// is the IP possible
	private void validateIP(String newValue) {
		view.btnConnect.setDisable(true);
		k2 = view.tfIP.getText();
		view.tfIP.setStyle("-fx-text-inner-color: red;");
		if (view.tfIP.getText().length() != 0) {
			view.tfIP.setStyle("-fx-text-inner-color: green;");
			ipValid = false;
			enableDisableButton();
		}
	}

	// -----------------------------------------------------------------------------------
	// is the portnumber possible
	private void validatePortNumber(String newValue) {
		view.btnConnect.setDisable(true);
		k3 = view.tfPort.getText();
		view.tfPort.setStyle("-fx-text-inner-color: red;");
		try {
			int value = Integer.parseInt(newValue);
			if (value >= 1 && value <= 65535) {
				view.tfPort.setStyle("-fx-text-inner-color: green;");
				portValid = false;
				enableDisableButton();
			}
		} catch (NumberFormatException e) {
			// wasn't an integer
		}
	}

	private void enableDisableButton() {
		if (!usernameValid) {

			if (!portValid) {

				if (!ipValid) {
					if (k1.length() > 0 && k2.length() > 0 && k3.length() > 0) {
						view.btnConnect.setDisable(false);
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------------------------
	// saves the Username, IP and Port in the mainClass and starts the
	// SplashScreen
	public void buttonConnect() {
		String userName = view.tfUsername.getText();
		String iP = view.tfIP.getText();
		int port = Integer.parseInt(view.tfPort.getText());
		model.setUpUser(userName, iP, port);
		main.startSplash();
	}
}