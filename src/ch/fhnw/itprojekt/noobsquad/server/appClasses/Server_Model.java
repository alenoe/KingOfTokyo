package ch.fhnw.itprojekt.noobsquad.server.appClasses;

/**
 * 
 * @author Simon Zahnd
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import ch.fhnw.itprojekt.noobsquad.abstractClasses.Model;
import ch.fhnw.itprojekt.noobsquad.server.clientCommunication.ClientConnection;
import ch.fhnw.itprojekt.noobsquad.gameLogic.*;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;


public class Server_Model extends Model{

	//-----------------------------------------------------------------------------------
	//Server / Client
    private ServerSocket socketConnection;
    private Socket pipe;
    private static int client_id = 0;
    ServiceLocator sl;
    Logger logger;
   
    
	//-----------------------------------------------------------------------------------
    //Lists save the current clients and the gamescore from the dices and players
    private ArrayList<ClientConnection> clientList =  new ArrayList<ClientConnection> ();
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private ArrayList<DiceM> diceMList = new ArrayList<DiceM>();
    private DiceM diceM = new DiceM(6);
    private ArrayList<Button_Lock_Unlock> pressedBtnList = new ArrayList<Button_Lock_Unlock>();
   
   
    
   public Server_Model(){
	   diceMList.add(diceM);
	   ServiceLocator sl = ServiceLocator.getServiceLocator();
	   for (int i=0; i <6; i++ ){
			Button_Lock_Unlock p = new Button_Lock_Unlock();
			pressedBtnList.add(p);
		}
	   
	   sl.getLogger().info("Server_Model initialisiert!");
   }
   
   
   //-----------------------------------------------------------------------------------
   //Client / Server connection

   public void startServer(int port, Logger logger){
	   this.logger = logger;
	   int socketCounter = 0;
	   try {
         socketConnection = new ServerSocket(port);

         System.out.println(port +": Server wartet auf Clients...");
         logger.info("Port: " + port + " : Server wartet auf Clients...");
         
         while(!socketConnection.isClosed()){
        	 if(socketCounter < 2){
        		 pipe = socketConnection.accept();
        
	        	 System.out.println(client_id + ". Client hinzugefuegt");
	        	 logger.info(client_id + ". Client hinzugefuegt " + pipe.toString());
	        	 ClientConnection ct = new ClientConnection(client_id, pipe, this);
	        	 clientList.add(ct);
	        	 ct.start();
	        	 client_id++;
	        	 socketCounter++;
        	 }else{
        		 logger.info("Port: "+port+" Spiel kann losgehen.");
        		 break;	        	
        	 }
         }    
	   }
		   catch(Exception e) {
			   System.out.println(e);
			   logger.info("Port "+port+ " ist schon besetzt oder ist fehlerhaft. Bitte einen anderen gueltigen/offenen Port waehlen.");
		   }
	   }   
   
   //-----------------------------------------------------------------------------------
   //broadcast Methods -> for sending Messages to one or both clients
      
   public synchronized void broadcastToAll(String type, Object o) {
	   for(ClientConnection ct: clientList) {
		   try {
			ct.sendMsg(type,o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
   }
   
   public synchronized void broadcastToOne(int i, String type, Object o){
	   try {
		clientList.get(i).sendMsg(type, o);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   //-----------------------------------------------------------------------------------
   //acess for the ClientMessageHandeler to get the values and update the values of the dices and players
   
   public ArrayList<Player> getPlayerList(){
	   return playerList;
   }
   public ArrayList<DiceM> getdiceMList(){
	   return diceMList;
   }
   public ArrayList<Button_Lock_Unlock> getBtnpressedList(){
	   return pressedBtnList;
   } 
   
   //-----------------------------------------------------------------------------------
   //GameLogic Methods	
   
   public void executePlay(int currentPlayer){
	   PlayM play = new PlayM(currentPlayer, playerList, diceMList.get(0));
	   playerList = play.isFinished();
   }
   public void tokyoCheck(DiceM dm, int id){
	   if(playerList.get(0).getInTokyo() == false && playerList.get(1).getInTokyo() == false && dm.getNumberOfDiceWithValue(4) >=1){
		   playerList.get(id).setInTokyo(true);
	   }
   }
   public int winnerCheck(){
	   if(playerList.get(0).getLifePoints() <= 0 || playerList.get(1).getVictoryPoints() >= 20){
		   return 1;
	   } else if(playerList.get(1).getLifePoints() <= 0 || playerList.get(0).getVictoryPoints() >= 20){
		   return 2;

	   } else {
		   return 0;
	   }
   }
   
   //-----------------------------------------------------------------------------------
   //new Player	
   public void newPlayer(String userName){
	   Player player = new Player(userName);
	   playerList.add(player);
   }
   
}
