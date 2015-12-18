package ch.fhnw.itprojekt.noobsquad.client.login;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Model;
import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;

/**
 * @author Simon Zahnd
 * 
 * Dem Controller die aktuelle IP Adresse übergeben.
 * Usernamen, IP und Port in der main Klasse JavaFX_App_Template speichern.
 */
public class Login_Model extends Model {

	InetAddress addr;

	public Login_Model() {

	}

	// -----------------------------------------------------------------------------------
	// Current IP
	public String getLokalHost() { // wird in der View direkt aufgerufen.
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return addr.getHostAddress();
	}

    //-----------------------------------------------------------------------------------
  	//Speichert den Usernamen, die IP und den Port in die main Klasse JavaFX_App_Template.
    //So sind diese Daten Zentral gespeichert und koennen spaeter von der ServerConnection Klasse
    //aufgerufen werden.
	public void setUpUser(String username, String iP, int port) {
		JavaFX_App_Template.setUsername(username);
		JavaFX_App_Template.setIP(iP);
		JavaFX_App_Template.setPort(port);
	}

}