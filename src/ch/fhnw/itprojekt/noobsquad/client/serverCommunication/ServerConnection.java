package ch.fhnw.itprojekt.noobsquad.client.serverCommunication;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import ch.fhnw.itprojekt.noobsquad.client.main.JavaFX_App_Template;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_Model;

public class ServerConnection implements Runnable{
	
	Board_Model model;
	
	private Socket client;
	private static ObjectOutputStream clientOutputStream;
	private ObjectInputStream clientInputStream;
	
	InetAddress addr;
	
	public ServerConnection(Board_Model model){
		this.model = model;
	}
	

	/** implementiert die run() Methode des Runnable Interface **/
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			clientInputStream = new ObjectInputStream(client.getInputStream());
		}catch (NullPointerException e){
		}
		catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  
		Message msg = null;
		
		
		while(!this.client.isClosed()){
			try {

				msg = (Message) clientInputStream.readObject();
				Runnable messageHandler = new ServerMessageHandler(this, model, msg);
				new Thread(messageHandler).start();
				
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IOException e){
				try {
					clientInputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**stellt eine Verbindung zum Server her. Die Werte kommen aus dem JavaFX_App_Template**/
	
	public void getServerconnection(){
			
			System.out.println("controller übergeben!");
			
			try {
	            client = new Socket(JavaFX_App_Template.getIP(), JavaFX_App_Template.getPort());
	            System.out.println("Netzwerkverbindung konnte hergestellt werden");
	            clientOutputStream = new ObjectOutputStream(client.getOutputStream());
	            sendMsg("Username", JavaFX_App_Template.getUsername());
			} catch(Exception e1) {
	            System.out.println("Netzwerkverbindung konnte nicht hergestellt werden");
	            e1.printStackTrace();    
			}
			Thread t = new Thread(this);
			t.start();
		}
	
	/** Methode zum versenden von Messages an den Server **/
	
	public synchronized void sendMsg(String type, Object o) throws IOException {
	
	    Message hMap = new Message(type, o);
	    try{
	    clientOutputStream.writeObject(hMap);
	    clientOutputStream.flush();
	    clientOutputStream.reset();
	    } catch(EOFException e2){
	    	e2.printStackTrace();
	    } catch(IOException e1){
	    	e1.printStackTrace();
	    }
	    hMap = null;
	}
}
