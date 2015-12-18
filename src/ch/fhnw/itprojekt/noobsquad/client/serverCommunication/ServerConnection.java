package ch.fhnw.itprojekt.noobsquad.client.serverCommunication;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_Model;

/**
 * 
 * @author Simon Zahnd
 *
 * Hier wird eine Verbindung zum Server aufgebaut.
 * Der Aufruf dazu kommt aus dem Board_Model
 * Einkommende Message Objekte werden zur Auswertung dem ServerMessageHandler uebergeben.
 */

public class ServerConnection implements Runnable {

	private Socket socket;
	private ObjectOutputStream clientOutputStream;
	private ObjectInputStream clientInputStream;
	private ServiceLocator serviceLocator;
	private Board_Model model;

	public ServerConnection(Board_Model model) {
		this.model = model;

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("ServerConnection initialized");
	}

	//-----------------------------------------------------------------------------------
	//Einkommende Message Objekte vom Server werden hier empfangen und an den ServerMessageHandler weitergegeben.
	@Override
	public void run() {

		try {
			clientInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (NullPointerException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Message msg = null;

		while (!this.socket.isClosed()) {
			try {

				msg = (Message) clientInputStream.readObject();
				Runnable messageHandler = new ServerMessageHandler(this, model, msg);
				new Thread(messageHandler).start();

			} catch (ClassNotFoundException e) {
				serviceLocator.getLogger().info("Class was not found: " + e);
			} catch (IOException e) {
				try {
					clientInputStream.close();
				} catch (IOException e1) {
					serviceLocator.getLogger().info("IOException: " + e);
				}
			}
		}
	}


	//-----------------------------------------------------------------------------------
	//Mit dem Server eine Verbindung herstellen. Usernamen an Server senden.
	//Gibt true oder false zurueck: je nach Abfrage im Server_Model wird die Methode enableRestart() aufgerufen. (Connection
	// ist fehlgeschlagen)
	public boolean getServerconnection() {

		System.out.println("controller übergeben!");

		try {
			socket = new Socket(JavaFX_App_Template.getIP(), JavaFX_App_Template.getPort());
			System.out.println("Netzwerkverbindung konnte hergestellt werden");
			clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
			sendMsg("Username", JavaFX_App_Template.getUsername());
		} catch (ConnectException e) {
			serviceLocator.getLogger().info(e.toString());
			serviceLocator.getLogger()
					.info("\n------------------------------------------------------------------------------\n"
							+ "Netzwerkverbindung konnte nicht hergestellt werden:\n" + "KingOfTokyo wird beendet.\n"
							+ "Der ausgewählte Server ist nicht aktiv.\n"
							+ "Wählen Sie eine andere IP, Portnummer oder versuchen Sie es später erneut.\n"
							+ "------------------------------------------------------------------------------");
			return false;
		} catch (Exception e1) {
			System.out.println("Netzwerkverbindung konnte nicht hergestellt werden");
			e1.printStackTrace();
		}
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		return true;
	}


	//-----------------------------------------------------------------------------------
	//Message Objekt an Server senden.
	public synchronized void sendMsg(String type, Object o) throws IOException {

		Message msg = new Message(type, o);
		try {
			clientOutputStream.writeObject(msg);
			clientOutputStream.flush();
			clientOutputStream.reset();
		} catch (SocketException e) {
			model.postMessage("Server_quit");
			clientOutputStream.close();
			serviceLocator.getLogger().info("SocketException: Serververbindung ist abgebrochen.");
		} catch (EOFException e2) {
			e2.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		msg = null;
	}
}
