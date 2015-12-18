package ch.fhnw.itprojekt.noobsquad.client.serverCommunication;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Message;
import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.client.board.Board_Model;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;
import ch.fhnw.itprojekt.noobsquad.gameLogic.DiceM;
import javafx.application.Platform;

/**
 * @author Heiko Meyer
 * Der ServerMessageHandler behandelt die eingehenden Nachrichten des Servers. Je nach Nachricht, wird 
 * die View des Boards aktualisiert und/oder dem Server die Nachricht weitergeleitet. 
 */

public class ServerMessageHandler implements Runnable {

	private Board_Model model;
	private Message msg;
	private ServerConnection client;
	ServiceLocator serviceLocator;

	public ServerMessageHandler(ServerConnection client, Board_Model model, Message msg) {
		this.model = model;
		this.msg = msg;
		this.client = client;
		new Thread();

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("ServerMessageHandler initialized");
	}
	
	/**
	 * Die run-Methode wird von der ServerConnection-Klasse aufgerufen, um die Nachrichten des Servers
	 * zu behandeln. Die Nachrichten werden mit getType entpackt und anschliessend ausgewertet. 
	 * Mit der postMessage-Methode des Boardmodels wird die Änderung dem Controller mitgeteilt, der wiederum die View anpasst. 
	 *
	 */
	@Override
	public void run() {
		String k = msg.getType();

		switch (k) {

		// setzt im Model den Player1 und updated die View
		case "Player1":
			model.setPlayer1((Player) msg.getContent());
			Platform.runLater(() -> {
				model.postMessage("player1");
			});
			break;

			// gibt die Würfel dem Player1 frei und updatet die View
		case "Player2":
			model.setPlayer2((Player) msg.getContent());
			try {
				client.sendMsg("Spieler1amZug", "Spieler1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Platform.runLater(() -> {
				model.postMessage("player2");
			});
			break;

			// dem Spieler1 werden die Gui-Elemente zum Würfeln freigegeben.	
		case "Spieler1wuerfeln":
			boolean s = (boolean) msg.getContent();
			boolean btnLeaveTokyoShow = model.btnLeaveTokyoEnable(s, model.getPlayer1());

			Platform.runLater(() -> {
				model.postMessage("btnRollsetDisable(" + s + ")");
				model.postMessage("btnListDicesetDisable(true)");
				model.postMessage("btnLeaveTokyosetDisable(" + btnLeaveTokyoShow + ")");
			});

			break;

		// dem Spieler2 werden die Gui-Elemente zum Würfeln freigegeben.	
		case "Spieler2wuerfeln":
			boolean s2 = (boolean) msg.getContent();
			boolean btnLeaveTokyoShow2 = model.btnLeaveTokyoEnable(s2, model.getPlayer2());

			Platform.runLater(() -> {
				model.postMessage("btnRollsetDisable(" + s2 + ")");
				model.postMessage("btnListDicesetDisable(true)");
				model.postMessage("btnLeaveTokyosetDisable(" + btnLeaveTokyoShow2 + ")");
			});

			break;

			// 	Die Würfel werden dem Model übergeben und in der View dargestellt
		case "Server_hat_gewuerfelt":
			model.setDice((DiceM) msg.getContent());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {

			}
			Platform.runLater(() -> {
				model.postMessage("dice");
				model.postMessage("btnDicesetDisable(true)");
			});
			break;

		// Je nachdem welcher Spieler am Zug war, werden dem Gegenspieler die Buttons freigegeben.
		// Im Model wird der Player1 aktualisiert	
		case "Player1execute":
			model.setPlayer1((Player) msg.getContent());

			Platform.runLater(() -> {

				if (model.getPlayersRound() == "Player1") {
					model.setPlayersRound("Player2");
					try {
						client.sendMsg("Spieler2amZug", "Spieler2");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (model.getPlayersRound() == "Player2") {
					try {
						client.sendMsg("Spieler1amZug", "Spieler1");
					} catch (Exception e) {
						e.printStackTrace();
					}
					model.setPlayersRound("Player1");
					;
				}
			});

			break;
			
		// Im Model wird Player2 aktualisiert und die View upgedatet
		case "Player2execute":

			model.setPlayer2((Player) msg.getContent());
			;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
			});
			model.setDice(null);
			break;
			
		// Der Status von Player1 "in Tokyo" wird im Model updated. 
		case "Player1updateTokyo":

			model.setPlayer1((Player) msg.getContent());
			;
			break;
			
		// Der Status von Player2 "in Tokyo" wird im Model updatet. Anschliessend wird
		// die View updatet. 
		case "Player2updateTokyo":

			model.setPlayer2((Player) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
			});
			break;
			
		// Im Falle dass Player1 verloren hat
		case "Player1lost":

			model.setPlayersRound("");
			;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player1lost");
			});
			break;

		// Im Falle dass Player2 gewonnen hat	
		case "Player2won":
			System.out.println("Player2won");

			model.setPlayersRound("");
			;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player2won");
			});
			break;
		
		// wie oben	
		case "Player1won":
			System.out.println("Player1won");
			model.setPlayersRound("");
			;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player1won");
			});

			break;

		// wie oben		
		case "Player2lost":
			System.out.println("Player2lost");
			model.setPlayersRound("");
			;
			Platform.runLater(() -> {
				model.postMessage("player1");
				model.postMessage("player2");
				model.postMessage("Player2lost");
			});
			break;
			
			// Würfel werden im Model gesetzt, je nach Message wird der Würfel gelocked oder unlocked
		case "LockDice1":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice1:");
			});
			break;

		case "UnlockDice1":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice1:");
			});
			break;

		case "LockDice2":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice2:");
			});
			break;

		case "UnlockDice2":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice2:");
			});
			break;

		case "LockDice3":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice3:");
			});
			break;

		case "UnlockDice3":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice3:");
			});
			break;

		case "LockDice4":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice4:");
			});
			break;

		case "UnlockDice4":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice4:");
			});
			break;

		case "LockDice5":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice5:");
			});
			break;

		case "UnlockDice5":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("UnlockbtnDice5:");
			});
			break;

		case "LockDice6":
			model.setDice((DiceM) msg.getContent());
			;

			Platform.runLater(() -> {
				model.postMessage("LockbtnDice6:");
			});
			break;

		case "UnlockDice6":
			model.setDice((DiceM) msg.getContent());
			;

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

		// Chat wird gestartet wenn zwei Spieler vorhanden sind. Send-Button wird in der View
		// freigegeben
		case "StartChat":
			boolean startChat = (boolean) msg.getContent();

			Platform.runLater(() -> {
				model.postMessage("btnSendsetDisable(" + startChat + ")");
			});
			break;
			
		// Wenn der Server geschlossen wird, wird die Message "quit" verschickt. Danach wird
		// der Button zur Wiederherstellung der Verbindung freigeschaltet.
		case "quit":
			model.postMessage("Server_quit");
			break;
		}
	}
}
