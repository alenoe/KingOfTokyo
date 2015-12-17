package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * Die Dice Klasse funktioniert als Container f�r die Wuerfel und wertet die TreeMap aus.
 * In der Klassenlandschaft dient sie als Schnittstelle einerseits zu Auswertung der W�rfel 
 * f�r die Durchf�hrung der spiellogischen Entscheide als auch zur Anzeige der W�rfel auf
 * der Spieloberfl�che.
 * 
 * @author Alexander Noever
 * 
 */

import java.io.Serializable;
import java.util.TreeMap;

import ch.fhnw.itprojekt.noobsquad.gameLogic.DieM;

public class DiceM implements Serializable {

	private static final long serialVersionUID = -4975836913986272099L;

	public TreeMap<Integer, DieM> diceMap = new TreeMap<Integer, DieM>();

	public DiceM(int numberOfDice) {
		for (int i = 0; i < numberOfDice; i++) {
			this.diceMap.put(i, new DieM(i));
		}
	}

	/**
	 * -------------------------------------------------------------------------
	 * ------------ Die Spiellogischen Methoden der Klasse.
	 */

	/**
	 * Die Anzahl eines bestimmten Wertes aus dem Dice Objekt auslesen. Dies
	 * erm�glicht die Auswertung auf Wuerfelkombinationen, damit der Spielzug
	 * ausgewertet werden kann.
	 * 
	 * @param value
	 * @return counter
	 */
	public int getNumberOfDiceWithValue(int value) {
		int counter = 0;
		for (int i = 0; i < diceMap.size(); i++) {
			if (this.getDieValue(i) == value) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Wuerfelt alle Wuerfel deren Variabel lock nicht auf true gesetzt ist.
	 * Dies wird so gemacht, damit der Spieler die Wuerfel ausw�hlen und sperren
	 * kann, bevor er erneut wuerfelt
	 **/
	public void rollDice() {
		for (int i = 0; i < this.diceMap.size(); i++) {
			DieM tempDie = this.diceMap.get(i);
			if (tempDie.getLock() == false) {
				tempDie.roll();
				this.diceMap.put(i, tempDie);
			}
		}
	}

	/**
	 * -------------------------------------------------------------------------
	 * ------------ Getter und Setter Methoden
	 */

	/**
	 * Getter fuer das WuerfelObjekt.
	 * 
	 * @param id
	 * @return dieM
	 */
	public DieM getDie(int id) {
		return diceMap.get(id);
	}

	/**
	 * Setter fuer den Locked Wert eines bestimmten Wuerfels.
	 * 
	 * @param id
	 * @param state
	 */
	public void setLock(int id, boolean state) {
		try {
			this.getDie(id).setLock(state);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter fuer den Wert eines bestimmten Wuerfels.
	 * 
	 * @param id
	 * @return dieM
	 */
	public int getDieValue(int id) {
		return diceMap.get(id).getValue();
	}

	/**
	 * Getter f�r das WuerfelBild. Unterscheidet, ob der Wuerfel gesperrt ist
	 * oder nicht, um das passende Bild zur�ck zu geben.
	 * 
	 * @param id
	 * @return facePicture
	 */
	public String getDieFacePicture(int id) {
		if (diceMap.get(id).getLock() == false) {
			return diceMap.get(id).getFacePicture();
		} else {
			return diceMap.get(id).getFacePictureLock();
		}
	}

	/**
	 * Getter f�r die L�nge des DiceM Objektes. Wird benutzt f�r gewisse for
	 * Schleifen.
	 * 
	 * @return size
	 */
	public int getSize() {
		return this.diceMap.size();
	}
}
