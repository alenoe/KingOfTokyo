package ch.fhnw.itprojekt.noobsquad.client.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.net.InetAddress;

import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.client.abstractClasses.Controller;


public class Login_Controller extends Controller<Login_Model, Login_View>{
	
	JavaFX_App_Template main;
	InetAddress addr;
	
    public Login_Controller(JavaFX_App_Template main,Login_Model model, Login_View view) {
        super(model, view);
        
        this.main = main;      
        
        
        // register ourselves to listen for button clicks
        view.btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buttonConnect();
            }
        });
        
        // register ourselves to handle window-closing event
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });

//        serviceLocator = ServiceLocator.getServiceLocator();        
//        serviceLocator.getLogger().info("Application controller initialized");
    }

    public void updateLokalHost(){
    	String lh = model.getLokalHost();
    	view.tfIP.setText(lh);	
    }
    public void buttonConnect(){ 
		String userName = view.tfUsername.getText();
		String iP = view.tfIP.getText();
		int port = Integer.parseInt(view.tfPort.getText());
		model.setUpUser(userName, iP, port);
		main.startSplash();
    }
}

