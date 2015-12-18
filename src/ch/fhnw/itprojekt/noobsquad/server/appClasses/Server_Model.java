package ch.fhnw.itprojekt.noobsquad.server.appClasses;

/**
 * 
 * @author Simon Zahnd
 * 
 * Hier empfängt der Server Clients.
 * Enthält die Broadcast Methoden. (Objekte an einen oder mehrere Clients senden)
 * Nutzt Gamelogik. (executePlay(), tokyoCheck(), winnerCheck())
 * Methode stopServerSocket() informiert die Clients wenn der Server nicht mehr laeuft.
 * 
 */

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import ch.fhnw.itprojekt.noobsquad.abstractClasses.Model;
import ch.fhnw.itprojekt.noobsquad.server.clientCommunication.ClientConnection;
import ch.fhnw.itprojekt.noobsquad.gameLogic.*;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;

import javafx.application.Platform;

public class Server_Model extends Model {

	// -----------------------------------------------------------------------------------
	// Server / Client
	private ServerSocket socketConnection;
	private Socket pipe;
	private static int client_id = 0;
	ServiceLocator sl;
	Logger logger;

	// -----------------------------------------------------------------------------------
	// Lists save the current clients and the gamescore from the dices and
	// players
	private ArrayList<ClientConnection> clientList = new ArrayList<ClientConnection>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<DiceM> diceMList = new ArrayList<DiceM>();
	private DiceM diceM = new DiceM(6);
	private ArrayList<Button_Lock_Unlock> pressedBtnList = new ArrayList<Button_Lock_Unlock>();

	// -----------------------------------------------------------------------------------
	// Wuerfel erstellen.
	// Fuer jeden Wuerfel ein Button_Lock_Unlock Objekt erstellen.
	public Server_Model() {
		diceMList.add(diceM);
		sl = ServiceLocator.getServiceLocator();
		for (int i = 0; i < 6; i++) {
			Button_Lock_Unlock p = new Button_Lock_Unlock();
			pressedBtnList.add(p);
		}

		sl.getLogger().info("Server_Model initialisiert!");
	}

	// -----------------------------------------------------------------------------------
	// Auf zwei Clients warten. Für jeden Client eine ClientConnection starten.
	public void startServer(int port, Logger logger) {
		this.logger = logger;
		int socketCounter = 0;
		try {
			socketConnection = new ServerSocket(port);

			logger.info("Port: " + port + " : Server wartet auf Clients...");

			while (!socketConnection.isClosed()) {
				if (socketCounter < 2) {
					pipe = socketConnection.accept();

					logger.info(client_id + ". Client hinzugefuegt " + pipe.toString());
					ClientConnection ct = new ClientConnection(client_id, pipe, this);
					clientList.add(ct);
					Thread t = new Thread(ct);
					t.setDaemon(true);
					t.start();
					client_id++;
					socketCounter++;
				} else {
					logger.info("Port: " + port + " Spiel kann losgehen.");
					break;
				}
			}
		} catch (BindException e) {
			logger.info("Der Port ist schon besetzt, w�hlen Sie einen freien Port aus.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Port " + port
					+ " ist schon besetzt oder ist fehlerhaft. Bitte einen anderen gueltigen/offenen Port waehlen.");
		}
	}

	// -----------------------------------------------------------------------------------
	// broadcast Methods -> Objekt an einen oder an beide Clients senden.

	public synchronized void broadcastToAll(String type, Object o) {
		for (ClientConnection ct : clientList) {
			ct.sendMsg(type, o);
		}
	}

	public synchronized void broadcastToOne(int i, String type, Object o) {
		clientList.get(i).sendMsg(type, o);
	}

	// -----------------------------------------------------------------------------------
	// getter und setter Methoden fuer den ClientMessageHandler

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public ArrayList<DiceM> getdiceMList() {
		return diceMList;
	}

	public ArrayList<Button_Lock_Unlock> getBtnpressedList() {
		return pressedBtnList;
	}

	// -----------------------------------------------------------------------------------
	// GameLogic Methods

	// Dritter Wurf auswerten
	public void executePlay(int currentPlayer) {
		PlayM play = new PlayM(currentPlayer, playerList, diceMList.get(0));
		playerList = play.isFinished();
	}

	//Hat jemand Tokyo besetzt?
	public void tokyoCheck(DiceM dm, int id) {
		if (playerList.get(0).getInTokyo() == false && playerList.get(1).getInTokyo() == false
				&& dm.getNumberOfDiceWithValue(4) >= 1) {
			playerList.get(id).setInTokyo(true);
		}
	}

	//Nach jedem Spielzug überprüfen ob jemand gewonnen hat.
	public int winnerCheck() {
		if (playerList.get(0).getLifePoints() <= 0 || playerList.get(1).getVictoryPoints() >= 20) {
			logger.info("Spiel ist beendet: Spieler 2 hat gewonnen!");
			return 1;
		} else if (playerList.get(1).getLifePoints() <= 0 || playerList.get(0).getVictoryPoints() >= 20) {
			logger.info("Spiel ist beendet: Spieler 1 hat gewonnen!");
			return 2;

		} else {
			return 0;
		}
	}

	//-----------------------------------------------------------------------------------
	//neuen Player erstellen
	public void newPlayer(String userName) {
		Player player = new Player(userName);
		playerList.add(player);
	}

	//-----------------------------------------------------------------------------------
	//Server stoppen und die Clients informieren.
	public void stopServerSocket() {
		try {
			if (!(socketConnection == null && socketConnection.isClosed())) {
				if (clientList.size() != 0) {
					for (ClientConnection ct : clientList) {
						ct.sendMsg("quit", -1);
					}
				}
			}
		} catch (NullPointerException e) {
			Platform.exit();
		}
		Platform.exit();
	}
}
