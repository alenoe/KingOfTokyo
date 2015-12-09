package ch.fhnw.itprojekt.noobsquad.server.appClasses;

import javafx.event.ActionEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
//import Server.TextAreaHandler;
import ch.fhnw.itprojekt.noobsquad.server.abstractClasses.Controller;
import ch.fhnw.itprojekt.noobsquad.server.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;

public class Server_Controller extends Controller<Server_Model, Server_View> {
	
	JavaFX_App_Template main;
	InetAddress addr; //This class represents an Internet Protocol (IP) address.

	public Server_Controller(JavaFX_App_Template main, Server_Model model, Server_View view) {
		super(model, view);
		
		this.main = main;
		ServiceLocator sl = ServiceLocator.getServiceLocator();

		view.textAreaHandler.setLevel(Level.INFO);
        Logger defaultLogger = sl.getLogger();
        defaultLogger.addHandler(view.textAreaHandler);
		//Logmessages werden in der TextArea angezeigt.
		
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
		
		view.btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent e) {
				model.closeAllConnections();
				main.stop();
	
				}
			});
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
}