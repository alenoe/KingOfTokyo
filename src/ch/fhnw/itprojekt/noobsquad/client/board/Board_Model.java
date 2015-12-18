package ch.fhnw.itprojekt.noobsquad.client.board;

/**
 * 
 * @author Heiko Meyer
 * Board_Model repräsentiert die Daten und Kernfunktionalität des Boards.
 * Das Board_Model informiert den Controller über Änderungen gemäss Observer-Pattern.
 */

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.client.serverCommunication.ServerConnection;
import ch.fhnw.itprojekt.noobsquad.gameLogic.DiceM;
import ch.fhnw.itprojekt.noobsquad.client.interfaces.Observer;
import ch.fhnw.itprojekt.noobsquad.client.interfaces.Subject;
import ch.fhnw.itprojekt.noobsquad.abstractClasses.Model;
import ch.fhnw.itprojekt.noobsquad.client.supportClasses.ServiceLocator;


public class Board_Model extends Model implements Subject {

	// Observervariabeln
	// ___________________________________________
	private List<Observer> observers;
	private String obsMessage;
	private boolean changed;
	private final Object MUTEX = new Object();
	// __________________________________________

	private ServerConnection serverThread;
	private ServiceLocator serviceLocator;

	private Player player1 = new Player("");
	private Player player2 = new Player("");
	private DiceM d1 = new DiceM(6);
	private int rollCounter = 0;
	private int gameState = 0;

	private String playersRound = "Player1";
	private String chatMessage;

	public Board_Model() {
		observers = new ArrayList<>();
		serverThread = new ServerConnection(this);

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Board model initialized");
	}

	/** 
	 * Getter und Setter für Instanzvariablen
	 */

	public void setRollCounter(int i) {
		this.rollCounter = i;
	}

	public int getRollCounter() {
		return this.rollCounter;
	}

	public void setPlayersRound(String playersRound) {
		this.playersRound = playersRound;
	}

	public String getPlayersRound() {
		return this.playersRound;
	}

	public Player getPlayer(int playerIndex) {
		if (playerIndex == 0) {
			return this.player1;
		} else {
			return this.player2;
		}
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer1() {
		return this.player1;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getPlayer2() {
		return this.player2;
	}

	public void setDice(DiceM dice) {
		this.d1 = dice;
	}

	public DiceM getDice() {
		return this.d1;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	public String getChatMessage() {
		return this.chatMessage;
	}

	public int getGameState() {
		return this.gameState;
	}

	public void setGameState(int state) {
		this.gameState = state;
	}

	public ServerConnection getServerConnection() {
		return serverThread;
	}

	/**
	 * Getter-Methoden fuer Spielvariablen
	 *
	 */

	public boolean btnLeaveTokyoEnable(boolean b, Player p) {
		if (p.getInTokyo() == true && p.getCantLeaveTokyo() == false && b == false) {
			return false;
		} else {
			return true;
		}
	}

	public String getPlayerName(int playerIndex) {
		switch (playerIndex) {
		case 0:
			return this.player1.getName();
		case 1:
			return this.player2.getName();
		default:
			return "";
		}
	}

	public int getPlayerHealthPoints(int playerIndex) {
		switch (playerIndex) {
		case 0:
			return this.player1.getLifePoints();
		case 1:
			return this.player2.getLifePoints();
		default:
			return 0;
		}
	}

	public int getPlayerVictoryPoints(int playerIndex) {
		switch (playerIndex) {
		case 0:
			return this.player1.getVictoryPoints();
		case 1:
			return this.player2.getVictoryPoints();
		default:
			return 0;
		}
	}

	public String getDiePicture(int diceIndex) {
		return this.d1.getDieFacePicture(diceIndex);
	}
	

	/**
	 * 
	 * Serverkommunikations-Methoden
	 * 
	 * Mit der sendMsg-Methode wird jeweils die entsprechende Nachricht
	 * an den Server gesendet.
	 * 
	 */

	public void diceBtnLockUnlock(int diceIndex) {
		try {
			serverThread.sendMsg("Btn_Lock_Unlock" + (diceIndex + 1), diceIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playerLeavesTokyo() {
		try {
			serverThread.sendMsg("PlayerLeavesTokyo", 0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void sendChatMessage(String chatMessage) {
		try {
			serverThread.sendMsg("ChatMessage", chatMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	// Die Roll-Methode repräsentiert 3 Würfe. Nach jedem Wurf wird der Server informiert,
	// damit er würfeln kann. Mit postMessage wird der Observer benachrichtigt, damit die View
	// aktualisiert werden kann.
	
	public boolean roll() {
		switch (rollCounter) {
		case 0:
			try {
				serverThread.sendMsg("Roll1", "Roll1");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			rollCounter++;
			this.postMessage("btnListDicesetDisable(false)");

			return false;
		case 1:
			try {
				serverThread.sendMsg("Roll2", "Roll2");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			rollCounter++;
			return false;
		case 2:
			try {
				serverThread.sendMsg("Roll3", "Roll3");
				this.postMessage("btnListDicesetDisable(true)");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			rollCounter = 0;
			return true;
		default:
			return false;
		}
	}

	public void endGame() {
		try {
			serverThread.sendMsg("GameEnd", "GameEnd");
		} catch (SocketException e) {
			serviceLocator.getLogger().info("SocketException: Der Server ist nicht erreichbar.\n");
		} catch (IOException e) {
			serviceLocator.getLogger().info(e.toString());
		}

	}

	/**
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 * ::::::::::::::::::::::::OBSERVER METHODEN:::::::::::::::::::::::::::
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */

	/**
	 * 
	 * Observermethoden
	 * Die Implementierung wurde �bernommen von http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
	 * Die Observer registrieren sich beim Subject und werden �ber die notifyObservers()-Methode benachrichtigt sich das Update abzuholen.
	 * In der Implementierung sendet der ServerMessageHandler im Namen des Models eine postMsg() an das Subject, welches notifyObservers() aufruft.
	 * Der einzige Observer ist der Board_Controller.
	 * 
	 * @author Alexander Noever
	 */
	// ______________________________________________________________________________________
	//

	/**
	 * Die register(Observer obj) methode registriert das observierende Objekt beim Subjekt und legt sie in die 
	 * ArrayListe ab.
	 */
	@Override
	public void register(Observer obj) {
		if (obj == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!observers.contains(obj))
				observers.add(obj);
		}
	}

	/**
	 * removes an Observer from the ArrayList
	 */
	@Override
	public void unregister(Observer obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}

	/**
	 * benachrichtigt alle Observer, dass sie die update() Methode aufrufen
	 */
	@Override
	public void notifyObservers() {
		List<Observer> observersLocal = null;
		// synchronization is used to make sure any observer registered after
		// message is received is not notified
		synchronized (MUTEX) {
			if (!changed)
				return;
			observersLocal = new ArrayList<>(this.observers);
			this.changed = false;
		}
		for (Observer obj : observersLocal) {
			obj.update();
		}
	}

	/**
	 * gibt das observierte Objekt zurueck.
	 */
	@Override
	public Object getUpdate(Observer obj) {
		return this.obsMessage;
	}

	/**
	 * wenn eine Message gepostet wird, werden alle Observer ueber die notifyObservers() Methode 
	 * benachrichtigt die Message abzuholen.
	 * @param msg
	 */
	// method to post message to the topic
	public synchronized void postMessage(String msg) {
		serviceLocator.getLogger().info("Message Posted to Topic:" + msg);
		this.obsMessage = msg;
		this.changed = true;
		notifyObservers();
	}
}