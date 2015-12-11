package ch.fhnw.itprojekt.noobsquad.server.clientCommunication;

/**
 * @author Simon Zahnd
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import java.util.logging.Logger;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.server.clientCommunication.ClientMessageHandler;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_Model;

public class ClientConnection extends Thread{
	
	private Socket socket;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private int id;
	private Server_Model model;
	private final Logger logger = Logger.getLogger("");


	
	public ClientConnection(int id, Socket socket, Server_Model model) throws IOException{
		this.socket = socket;
		this.id = id;
		this.model = model;
		
		serverInputStream = new    
		ObjectInputStream(socket.getInputStream());
		
		serverOutputStream = new 
		ObjectOutputStream(socket.getOutputStream());
	}
	
	
	public void run(){
		try{
			listen();
		} catch (EOFException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------
	//receive a message from the client and evaluate them in the ClientMessageHandler 
	public void listen()throws IOException {
		Message msg = null;
		
		while(!this.socket.isClosed()){
			try {
				msg = (Message) serverInputStream.readObject();
				Runnable messageHandler = new ClientMessageHandler(this, model, msg);
				new Thread(messageHandler).start();
	
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EOFException e){
				serverInputStream.close();
				logger.info(model.getPlayerList().get(0).getName()+" ist nicht mehr mit dem Server verbunden.");
				e.printStackTrace();
				}
		}
	}
	
	
	
	//-----------------------------------------------------------------------------------
	//sends an Object to the clients
	public synchronized void sendMsg(String type, Object o) throws IOException {
	    Message hMap = new Message(type, o);
	    serverOutputStream.writeObject(hMap);
	    serverOutputStream.flush();
	    serverOutputStream.reset();
	    hMap = null;
	}
	
	
	
	public int getConnectionID(){
		return this.id;
	}
}
