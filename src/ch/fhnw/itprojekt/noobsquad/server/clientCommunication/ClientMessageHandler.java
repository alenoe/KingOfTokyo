package ch.fhnw.itprojekt.noobsquad.server.clientCommunication;

/**
 * @author Heiko Meyer
 */

import ch.fhnw.itprojekt.noobsquad.gameLogic.Button_Lock_Unlock;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.server.appClasses.Server_Model;

public class ClientMessageHandler implements Runnable{
	
	private Server_Model model;
	private Message msg;
	private ClientConnection client;
	
	public ClientMessageHandler(ClientConnection client, Server_Model model, Message msg){
		this.model = model;
		this.msg = msg;
		this.client = client;
		
		new Thread();
	}

	@Override
	public void run() {
		String k = msg.getType();
		
		switch (k){
		case "Username":
			model.newPlayer((String) msg.getContent());
			if(client.getConnectionID() == 0){
				model.broadcastToOne(0, "Player1", model.getPlayerList().get(0));

			}else{
				model.broadcastToOne(1, "Player1", model.getPlayerList().get(0));
				model.broadcastToAll("Player2", model.getPlayerList().get(1));
				model.broadcastToAll("StartChat", false);
			}
			
			break;
			
		case "Spieler1amZug":
			model.broadcastToOne(0, "Spieler1wuerfeln", false);
			model.broadcastToOne(1, "Spieler1wuerfeln", true);
			
			break;
			
		case "Spieler2amZug":
			model.broadcastToOne(0, "Spieler2wuerfeln", true);
			model.broadcastToOne(1, "Spieler2wuerfeln", false);
			
			break;
			
		case "Roll1":

			for (Button_Lock_Unlock blu: model.getBtnpressedList()){
				blu.setPressed(false);
			}
				model.getdiceMList().get(0).rollDice();
				model.tokyoCheck(model.getdiceMList().get(0), client.getConnectionID());
				for(int i = 0; i<model.getdiceMList().get(0).getSize(); i++){
					model.getdiceMList().get(0).setLock(i, false);
				}

				model.broadcastToAll("Server_hat_gewuerfelt3", model.getdiceMList().get(0));
			break;
			
		
		case "Roll2":

			for (Button_Lock_Unlock blu: model.getBtnpressedList()){
				blu.setPressed(false);
			}

			model.getdiceMList().get(0).rollDice();
			model.tokyoCheck(model.getdiceMList().get(0), client.getConnectionID());
			for(int i = 0; i<model.getdiceMList().get(0).getSize(); i++){
				model.getdiceMList().get(0).setLock(i, false);
			}
			model.broadcastToAll("Server_hat_gewuerfelt3", model.getdiceMList().get(0));
			break;
			
		case "Roll3":

			for (Button_Lock_Unlock blu: model.getBtnpressedList()){
				blu.setPressed(false);
			}
			
			model.getdiceMList().get(0).rollDice();
			model.tokyoCheck(model.getdiceMList().get(0), client.getConnectionID());
			for(int i = 0; i<model.getdiceMList().get(0).getSize(); i++){
				model.getdiceMList().get(0).setLock(i, false);
			}
			model.broadcastToAll("Server_hat_gewuerfelt3", model.getdiceMList().get(0));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.executePlay(client.getConnectionID());
			int gameState = model.winnerCheck();
			if(model.winnerCheck() == 0){
			System.out.println(model.getPlayerList().toString());
			model.broadcastToAll("Player1execute", model.getPlayerList().get(0));
			model.broadcastToAll("Player2execute", model.getPlayerList().get(1));
			} else {
				switch (gameState){
				case 1:
					model.broadcastToOne(0, "Player1lost", model.getPlayerList().get(0));
					model.broadcastToOne(1, "Player2won", model.getPlayerList().get(1));
					break;
				case 2:
					model.broadcastToOne(0, "Player1won", model.getPlayerList().get(0));
					model.broadcastToOne(1, "Player2lost", model.getPlayerList().get(1));
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.broadcastToAll("Player1execute", model.getPlayerList().get(0));
				model.broadcastToAll("Player2execute", model.getPlayerList().get(1));
			}

			break;
			
		case "PlayerLeavesTokyo":
			for(Player p : model.getPlayerList()){
				if(p.getInTokyo() == true){
					p.setInTokyo(false);
				} else {
				p.setInTokyo(true);
				}
			}
			
			model.broadcastToAll("Player1updateTokyo", model.getPlayerList().get(0));
			model.broadcastToAll("Player2updateTokyo", model.getPlayerList().get(1));
			
			break;
			
		case "Btn_Lock_Unlock1":
			int btnNumber = (int) msg.getContent();
			
						
			if(!model.getBtnpressedList().get(btnNumber).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber, true);
				model.getBtnpressedList().get(btnNumber).setPressed(true);
				model.broadcastToAll("LockDice1", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber, false);
				model.getBtnpressedList().get(btnNumber).setPressed(false);
				model.broadcastToAll("UnlockDice1", model.getdiceMList().get(0));
			}

			break;
			
		case "Btn_Lock_Unlock2":
			int btnNumber2 = (int) msg.getContent();
						
			if(!model.getBtnpressedList().get(btnNumber2).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber2, true);
				model.getBtnpressedList().get(btnNumber2).setPressed(true);
				
				model.broadcastToAll("LockDice2", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber2).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber2, false);
				model.getBtnpressedList().get(btnNumber2).setPressed(false);
				
				model.broadcastToAll("UnlockDice2", model.getdiceMList().get(0));
			}

			break;
			
		case "Btn_Lock_Unlock3":
			int btnNumber3 = (int) msg.getContent();
						
			if(!model.getBtnpressedList().get(btnNumber3).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber3, true);
				model.getBtnpressedList().get(btnNumber3).setPressed(true);	
				
				model.broadcastToAll("LockDice3", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber3).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber3, false);
				model.getBtnpressedList().get(btnNumber3).setPressed(false);
				
				model.broadcastToAll("UnlockDice3", model.getdiceMList().get(0));
			}

			break;
			
		case "Btn_Lock_Unlock4":
			int btnNumber4 = (int) msg.getContent();
				
			if(!model.getBtnpressedList().get(btnNumber4).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber4, true);
				model.getBtnpressedList().get(btnNumber4).setPressed(true);
				
				
				model.broadcastToAll("LockDice4", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber4).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber4, false);
				model.getBtnpressedList().get(btnNumber4).setPressed(false);
			
				model.broadcastToAll("UnlockDice4", model.getdiceMList().get(0));
			}

			break;
			
		case "Btn_Lock_Unlock5":
			int btnNumber5 = (int) msg.getContent();
						
			if(!model.getBtnpressedList().get(btnNumber5).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber5, true);
				model.getBtnpressedList().get(btnNumber5).setPressed(true);
				
				
				model.broadcastToAll("LockDice5", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber5).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber5, false);
				model.getBtnpressedList().get(btnNumber5).setPressed(false);
				
				model.broadcastToAll("UnlockDice5", model.getdiceMList().get(0));
			}

			break;
			
		case "Btn_Lock_Unlock6":
			int btnNumber6 = (int) msg.getContent();
						
			if(!model.getBtnpressedList().get(btnNumber6).getPressed()){
				model.getdiceMList().get(0).setLock(btnNumber6, true);
				model.getBtnpressedList().get(btnNumber6).setPressed(true);
				
				
				model.broadcastToAll("LockDice6", model.getdiceMList().get(0));
				
			}else{
				model.getdiceMList().get(0).getDie(btnNumber6).getFacePicture();
				model.getdiceMList().get(0).setLock(btnNumber6, false);
				model.getBtnpressedList().get(btnNumber6).setPressed(false);
				
				model.broadcastToAll("UnlockDice6", model.getdiceMList().get(0));
			}

			break;
			
		case "ChatMessage":
			String message = (String) msg.getContent();
			model.broadcastToAll("Message", model.getPlayerList().get(client.getConnectionID()).getName()+": "+message);
			
			break;
			
		}
	}
}
