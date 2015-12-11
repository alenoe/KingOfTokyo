package ch.fhnw.itprojekt.noobsquad.client.login;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Model;
import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 * @author Simon Zahnd
 */
public class Login_Model extends Model{
	
    InetAddress addr;
    
    public Login_Model() {
       
    }
    
    
    //-----------------------------------------------------------------------------------
  	// Current IP
    public String getLokalHost() { // wird in der View direkt aufgerufen.
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr.getHostAddress();
    }
    
    //-----------------------------------------------------------------------------------
  	// Save the Username, IP and Port in the mainClass from the Login
    public void setUpUser(String username, String iP, int port){
		JavaFX_App_Template.setUsername(username);
		JavaFX_App_Template.setIP(iP);
		JavaFX_App_Template.setPort(port);
    }
    
}