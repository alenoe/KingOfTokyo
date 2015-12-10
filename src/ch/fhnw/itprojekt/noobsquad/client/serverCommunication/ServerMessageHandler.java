package ch.fhnw.itprojekt.noobsquad.client.serverCommunication;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_Model;
import ch.fhnw.itprojekt.noobsquad.gameLogic.DiceM;
import javafx.application.Platform;

public class ServerMessageHandler implements Runnable{
	
	private Board_Model model;
	private Message msg;
	private ServerConnection client;
	
	public ServerMessageHandler(ServerConnection client, Board_Model model, Message msg){
		this.model = model;
		this.msg = msg;
		this.client = client;
		new Thread();
	}

	@Override
	public void run() {
		String k = msg.getType();
		
		switch (k){
		
		case "Player1":
			model.setPlayer1((Player) msg.getContent());
			Platform.runLater(() -> {
				model.postMessage("player1");
			});
			break;
			
			//updated die View und gibt die W�rfel dem "Player 1" frei.
		case "Player2":
			model.setPlayer2((Player) msg.getContent());
      		try {
				client.sendMsg("Spieler1amZug", "Spieler1");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.runLater(() -> {
				model.postMessage("player2");
			});
			break;
		
		//dem Spieler1 werden die Gui Elemente zum w�rfeln freigegeben.	
		case "Spieler1wuerfeln":
			boolean s = (boolean) msg.getContent();
			boolean btnLeaveTokyoShow = model.btnLeaveTokyoEnable(s, model.getPlayer1());

				Platform.runLater(() -> {
					model.postMessage("btnRollsetDisable("+s+")");
					model.postMessage("btnListDicesetDisable(true)");
					model.postMessage("btnLeaveTokyosetDisable("+btnLeaveTokyoShow+")");
				});
				
			break;
		
		//dem Spieler2 werden die Gui Elemente zum w�rfeln freigegeben.	
		case "Spieler2wuerfeln":
			boolean s2 = (boolean) msg.getContent();
			boolean btnLeaveTokyoShow2 = model.btnLeaveTokyoEnable(s2, model.getPlayer2());

			Platform.runLater(() -> {
				model.postMessage("btnRollsetDisable("+s2+")");
				model.postMessage("btnListDicesetDisable(true)");
				model.postMessage("btnLeaveTokyosetDisable("+btnLeaveTokyoShow2+")");
			});
			
			break;
		
		case "Server_hat_gewuerfelt":
			model.setDice((DiceM) msg.getContent());
			Platform.runLater(() -> {
				model.postMessage("dice");
			});			
			break;
			
		case "Server_hat_gewuerfelt3":
			model.setDice((DiceM) msg.getContent());
			Platform.runLater(() -> {
				model.postMessage("dice");
				model.postMessage("btnDicesetDisable(true)");
			});	
			break;
			
		case "Player1execute":
			model.setPlayer1((Player)msg.getContent());
			
			Platform.runLater(() ->{

				if(model.getPlayersRound() == "Player1"){
					model.setPlayersRound("Player2");
					try {
						client.sendMsg("Spieler2amZug", "Spieler2");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(model.getPlayersRound() == "Player2"){
					try {
						client.sendMsg("Spieler1amZug", "Spieler1");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.setPlayersRound("Player1");;
				}
			});
			
			break;
		
		case "Player2execute":

			model.setPlayer2((Player) msg.getContent());;	
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
			});
			model.setDice(null);
			break;
			
		case "Player1updateTokyo":
	
			model.setPlayer1((Player) msg.getContent());;	
			break;
			
		case "Player2updateTokyo":
			
			model.setPlayer2((Player) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				});
			break;
			
		case "Player1lost":

			model.setPlayersRound("");;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player1lost");
				});
			break;
			
		case "Player2won":
			System.out.println("Player2Won");

			model.setPlayersRound("");;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player2won");
				});
			break;
			
		case "Player1won":
			System.out.println("Player1Won");
			model.setPlayersRound("");;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player1won");
				});

			break;
			
		case "Player2lost":				
			System.out.println("Player2lost");
			model.setPlayersRound("");;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player2lost");
				});
			break;

			
		case "LockDice1":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice1:");
			});			
			break;
			
		case "UnlockDice1":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice1:");
			});			
			break;
			
		case "LockDice2":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice2:");
			});			
			break;
			
		case "UnlockDice2":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice2:");
			});			
			break;
		
		case "LockDice3":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice3:");
			});			
			break;
			
		case "UnlockDice3":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice3:");
			});			
			break;
		
		case "LockDice4":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice4:");
			});			
			break;
			
		case "UnlockDice4":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice4:");
			});			
			break;
		
		case "LockDice5":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice5:");
			});			
			break;
			
		case "UnlockDice5":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice5:");
			});			
			break;
		
		case "LockDice6":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("LockbtnDice6:");
			});			
			break;
			
		case "UnlockDice6":			
			model.setDice((DiceM) msg.getContent());;
			
			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice6:");
			});			
			break;
		
		case "Message":
			model.setChatMessage((String) msg.getContent());
			Platform.runLater(() -> {
				model.postMessage("chatMessage");
			});
			
		break;
		
		case "StartChat":
			boolean startChat = (boolean) msg.getContent();

				Platform.runLater(() -> {
					model.postMessage("btnSendsetDisable("+startChat+")");
				});
		}
	}
}
