package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * Die Player Objekte werden vom Server erstellt, sobald ein Client sich verbindet.
 * In dem PlayerObjekt werden die Spiellogischen Variablen gespeichert und angepasst.
 * @author Alexander Noever
 */

import java.io.Serializable;

//Das Playerobjekt kann innerhalb des Messageobjektes versendet werden (serialisiert).

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4975836913986272099L;
	private final int MAX_LP = 10;
	private final int MIN_LP = 0;
	private final int MAX_VP = 20;
	private final int MIN_VP = 0;

	private int lifePoints;
	private int victoryPoints;
	private String playerName;
	private boolean inTokyo;
	private boolean cantLeaveTokyo;

	public Player(String playerName) {
		this.lifePoints = MAX_LP;
		this.victoryPoints = MIN_VP;
		this.playerName = playerName;
		this.inTokyo = false;
		this.cantLeaveTokyo = true;
	}

	/**
	 * -------------------------------------------------------------------------
	 * ------------ Getter und Setter Methoden
	 */

	// Getter fuer den Namen des Spielers
	public String getName() {
		return this.playerName;
	}

	// Getter und Setter f�r die Lebenspunkte
	public int getLifePoints() {
		return this.lifePoints;
	}

	/**
	 * Der Setter pr�ft ebenfalls ob der Spieler innerhalb des Maximums, bzw.
	 * Minimums liegt.
	 * 
	 * @param Lebenspunkte
	 */
	public void setLifePoints(int i) {
		if (i >= 10) {
			this.lifePoints = MAX_LP;
		} else if (i <= 0) {
			this.lifePoints = MIN_LP;
		} else {
			this.lifePoints = i;
		}
	}

	// Getter und Setter f�r die Siegespunkte
	public int getVictoryPoints() {
		return this.victoryPoints;
	}

	/**
	 * Der Setter pr�ft ebenfalls ob der Spieler innerhalb des Maximums, bzw.
	 * Minimums liegt.
	 * 
	 * @param Lebenspunkte
	 */
	public void setVictoryPoints(int i) {

		if (this.victoryPoints + i >= MAX_VP) {
			System.out.println(this.playerName + " hat gewonnen.");
			this.victoryPoints = MAX_VP;
		} else {
			this.victoryPoints += i;
		}
	}

	// Getter und Setter fuer den inTokyo Status
	public boolean getInTokyo() {
		return this.inTokyo;
	}

	public void setInTokyo(boolean tokyo) {
		if (tokyo == true) {
			this.inTokyo = tokyo;
			this.victoryPoints = victoryPoints + 1;
		} else {
			this.inTokyo = tokyo;
		}
	}

	// Getter und Setter f�r den CantLeaveTokyo Status
	/**
	 * Wird zur Ueberpruefung verwendet, ob ein Spieler Tokyo verlassen darf
	 * 
	 * @return
	 */
	public boolean getCantLeaveTokyo() {
		return cantLeaveTokyo;
	}

	public void setCantLeaveTokyo(boolean cantLeaveTokyo) {
		this.cantLeaveTokyo = cantLeaveTokyo;
	}

	/**
	 * _________________________________________________________________________________
	 */

	// toString Methode f�r die Klasse
	public String toString() {
		return "Player: " + this.playerName + " LifePoints: " + this.lifePoints + " VictoryPoints: "
				+ this.victoryPoints + " inTokyo: " + this.inTokyo + " Can't Leave Tokyo: " + this.cantLeaveTokyo;
	}
}
