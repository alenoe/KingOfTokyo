package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * Die Klasse wird vom Server aufgerufen und mit den übergebenen Werten werden die Player Objekte angepasst.
 * Dem PlayM Objekt werden der DiceM und die Player Objekteübergeben.
 * Auf dem Initialisierten Objekt wird dann die isFinished() Methode übergeben.
 * Danach wird das Objekt nicht mehr verwendet.
 * 
 * @author Alexander Noever
 */

import java.util.ArrayList;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;
import ch.fhnw.itprojekt.noobsquad.server.supportClasses.ServiceLocator;

public class PlayM {

	private Player playerActive;
	private Player playerNotActive;
	private ArrayList<Player> playerList;
	private DiceM dice;

	public PlayM(int currentPlayerIndex, ArrayList<Player> playerList, DiceM dice) {

		this.playerActive = playerList.get(currentPlayerIndex);
		if (currentPlayerIndex == 0) {
			this.playerNotActive = playerList.get(1);
		} else {
			this.playerNotActive = playerList.get(0);
		}
		this.playerList = playerList;
		this.dice = dice;

	}

	/**
	 * -------------------------------------------------------------------------
	 * ------------ Die Spiellogischen Methoden der Klasse.
	 */

	/**
	 * Ruft die getNumberOfDiceWithValue() des DiceM Objektes auf und prüft auf
	 * VictoryPoints(Value 1-3)
	 * 
	 * @return victoryPoints
	 */
	public int getVictoryPoints() {
		int victoryPoints = 0;
		for (int i = 0; i <= 3; i++) {
			int numberOfDiceWithValueI = dice.getNumberOfDiceWithValue(i);
			switch (numberOfDiceWithValueI) {
			case 3:
				victoryPoints = victoryPoints + i;
				break;
			case 4:
				victoryPoints = victoryPoints + i + 1;
				break;
			case 5:
				victoryPoints = victoryPoints + i + 2;
				break;
			case 6:
				victoryPoints = victoryPoints + i + 3;
				break;
			}
		}
		return victoryPoints;
	}

	/**
	 * ruft die getNumberOfDiceWithValue() des DiceM Objektes auf und prüft auf
	 * erhaltene LifePoints(Value 5)
	 * 
	 * @return LifePoints+
	 */
	public int getHeal() {
		int heal = dice.getNumberOfDiceWithValue(5);
		return heal;
	}

	/**
	 * ruft die getNumberOfDiceWithValue() des DiceM Objektes auf und prüft auf
	 * erhaltene LifePoints(Value 4)
	 * 
	 * @return damage
	 */
	public int getDamage() {
		int damage = dice.getNumberOfDiceWithValue(4);
		return damage;
	}

	/**
	 * Die methode wertet die Würfel aus und verändert anhand der Position und
	 * vorangegangener Ereignisse die Spielerwerte.
	 * 
	 * @return ArrayList<Player>
	 */
	public ArrayList<Player> isFinished() {

		// Schaden wird dem nicht aktiven Spieler hinzugefügt.
		if (playerNotActive.getLifePoints() > playerNotActive.getLifePoints() - getDamage()) {
			playerNotActive.setLifePoints(playerNotActive.getLifePoints() - getDamage());
			playerNotActive.setCantLeaveTokyo(false);
		} else {
			playerNotActive.setLifePoints(playerNotActive.getLifePoints() - getDamage());
			playerNotActive.setCantLeaveTokyo(true);
		}

		// Verteilung der Herzeinheiten als LifePoints für den aktiven Spieler.
		if (playerActive.getInTokyo() == false) {
			playerActive.setLifePoints(playerActive.getLifePoints() + getHeal());
		}

		// Wenn der Spieler in Tokyo ist, erhält er für diese Runde seine zwei
		// Bonuspunkte.
		if (playerActive.getInTokyo() == true) {
			playerActive.setVictoryPoints(getVictoryPoints() + 2);

			// Falls nicht erhält er seine gewürfelten Punkte.
		} else {
			playerActive.setVictoryPoints(getVictoryPoints());
		}

		// Beendet den Spielzug und gibt die SpielerListe zurück.
		return playerList;
	}
}
