package ch.fhnw.itprojekt.noobsquad.server.clientCommunication;

/**
 * @author Simon Zahnd
 * 
 *  * Hier wird das Message Objekt vom Client empfangen (ObjectInputstream) und zur Auswertung an den 
 * ClientMessageHandler weitergegeben.
 * 
 * Zudem werden hier die Messages Objekte mit der Methode sendMsg an die Clients versendet (ObjectOutputStream)
 * sendMsg wird in den Broadcast Methoden im ClientMessageHander verwendet.
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.server.clientCommunication.ClientMessageHandler;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_Model;

public class ClientConnection extends Thread {

	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private int id;
	private boolean running;
	private Server_Model model;
	private final Logger logger = Logger.getLogger("");

	public ClientConnection(int id, Socket socket, Server_Model model) throws IOException {
		this.id = id;
		this.model = model;

		serverInputStream = new ObjectInputStream(socket.getInputStream());

		serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	public void run() {
		running = true;
		try {
			listen();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------
	//Message Objekt empfangen und an den ClientMessageHandler weitergeben. 
	public void listen() throws IOException {
		Message msg = null;

		while (running == true) {
			try {
				msg = (Message) serverInputStream.readObject();
				Runnable messageHandler = new ClientMessageHandler(this, model, msg);
				new Thread(messageHandler).start();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				serverInputStream.close();
				logger.info("EOFException: " + model.getPlayerList().get(0).getName()
						+ " ist nicht mehr mit dem Server verbunden.");
				e.printStackTrace();
			} catch (SocketException e) {
				serverInputStream.close();
				logger.info("SocketException: " + model.getPlayerList().get(id).getName()
						+ "hat die Verbindung unterbrochen");
				running = false;
				model.stopServerSocket();
			}
		}
	}

	// -----------------------------------------------------------------------------------
	//Versendet das Message Objekt.
		//Diese Methode wird von den Broadcast Methoden aus dem Model aufgerufen. 
	public synchronized void sendMsg(String type, Object o) {
		try {
			Message hMap = new Message(type, o);
			serverOutputStream.writeObject(hMap);
			serverOutputStream.flush();
			serverOutputStream.reset();
			hMap = null;
		} catch (SocketException e) {
			logger.info("SocketException: Message kann nicht gesendet werden, Verbindung zu "
					+ model.getPlayerList().get(id).getName() + " ist unterbrochen.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------------------------
	//ID des Clients zurueckgeben.
	public int getConnectionID() {
		return this.id;
	}
}
