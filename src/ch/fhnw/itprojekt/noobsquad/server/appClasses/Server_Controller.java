package ch.fhnw.itprojekt.noobsquad.server.appClasses;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.stage.Stage;
//import Server.TextAreaHandler;
import ch.fhnw.itprojekt.noobsquad.server.abstractClasses.Controller;
import ch.fhnw.itprojekt.noobsquad.server.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;

public class Server_Controller extends Controller<Server_Model, Server_View> {
	
	JavaFX_App_Template main;
	private boolean portValid = false;
	InetAddress addr; //This class represents an Internet Protocol (IP) address.

	public Server_Controller(JavaFX_App_Template main, Server_Model model, Server_View view) {
		super(model, view);
		
		this.main = main;
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		
		//-----------------------------------------------------------------------------------
		//Logmessages werden in der TextArea angezeigt.
		view.textAreaHandler.setLevel(Level.INFO);
        Logger defaultLogger = sl.getLogger();
        defaultLogger.addHandler(view.textAreaHandler);
		
        //-----------------------------------------------------------------------------------
      	//start Server
		view.btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try{
								model.startServer(view.tfIP.getText(), Integer.parseInt(view.tfPort.getText()), defaultLogger);
							}catch (NumberFormatException e){
								sl.getLogger().info("Server konnte nicht gestartet werden weil der Port fehlt oder ungültig ist.");
							}	
						}
					}).start();
				}
			});
		
		//-----------------------------------------------------------------------------------
      	//stop Server
		view.btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent e) {
				Platform.exit();
				}
			});
		
		//-----------------------------------------------------------------------------------
		// ChangeListener for the text-property of the port number
		view.tfPort.textProperty().addListener(
				// Parameters of any PropertyChangeListener
				(observable, oldValue, newValue) -> {
					validatePortNumber(newValue);
				});
		view.tfPort.setText("14000");

		//-----------------------------------------------------------------------------------
		// get current IP adress
		try {
			addr = InetAddress.getLocalHost();
			view.tfIP.setText(addr.getHostAddress());
			view.tfIP.setPromptText("Deine jetztige IP: "+addr.getHostAddress());
			view.lblHostName.setText(addr.getHostName());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------
	//is the portnumber possible
	private void validatePortNumber(String newValue) {
		boolean valid = true;

		try {
			int value = Integer.parseInt(newValue);
			if (value < 1 || value > 65535) valid = false;
		} catch (NumberFormatException e) {
			// wasn't an integer
			valid = false;
		}

		// Change text color
		if (valid) {
			view.tfPort.setStyle("-fx-text-inner-color: green;");
		} else {
			view.tfPort.setStyle("-fx-text-inner-color: red;");
		}

		// Save result
		portValid = valid;

		// Enable or disable button, as appropriate
		enableDisableButton();
	}
	
	/**
	 * Enable or disable the Connect button, based on the validity of the two
	 * text controls
	 */
	private void enableDisableButton() {
		boolean valid = portValid;
		view.btnStart.setDisable(!valid);
	}
	
	
}