package ch.fhnw.itprojekt.noobsquad.gameLogic;

import java.util.ArrayList;

import ch.fhnw.itprojekt.noobsquad.gameLogic.Player;

public class PlayM {
	
	public static final int MAX_VP=20;
	public static final int MAX_HP=10;
	
	private boolean playEnd;
	private Player playerActive;
	private Player playerNotActive;
	private ArrayList<Player> playerList;
	private DiceM dice;
	
	public PlayM(int currentPlayerIndex, ArrayList<Player> playerList, DiceM dice){
		this.playEnd = false;
		this.playerActive = playerList.get(currentPlayerIndex);
		if(currentPlayerIndex == 0){
			this.playerNotActive = playerList.get(1);
		} else{
			this.playerNotActive = playerList.get(0);
		}
		this.playerList = playerList;
		this.dice = dice;
	}
	
	public int getVictoryPoints(){
		int victoryPoints = 0;
		for(int i = 0; i <= 3; i++){
			int numberOfDiceWithValueI = dice.getNumberOfDiceWithValue(i);
			switch(numberOfDiceWithValueI){
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
		} return victoryPoints;		
	}
	
	public int getHeal(){
		int heal = dice.getNumberOfDiceWithValue(5);			
		return heal;	
	}
	
	public int getDamage(){
		int damage = dice.getNumberOfDiceWithValue(4);
		return damage;
	}
	
	public void setPlayEnd(){
	}
	
	public ArrayList<Player> isFinished(){
		
		//Schaden wird dem Gegner hinzugefügt.
		if(playerNotActive.getLifePoints() > playerNotActive.getLifePoints() - getDamage()){
			playerNotActive.setLifePoints(playerNotActive.getLifePoints() - getDamage());
			playerNotActive.setCantLeaveTokyo(false);
		} else {
			playerNotActive.setLifePoints(playerNotActive.getLifePoints() - getDamage());
			playerNotActive.setCantLeaveTokyo(true);
		}
		
		//Verteilung der Herzeinheiten als Lebenspunkte für den Spieler.
		if(playerActive.getInTokyo() == false){
				playerActive.setLifePoints(playerActive.getLifePoints() + getHeal());				
			}

		//Wenn der Spieler in Tokyo ist, erhält er für diese Runde seine zwei Bonuspunkte.
		if(playerActive.getInTokyo() == true){
			playerActive.setVictoryPoints(getVictoryPoints() + 2);
		//Falls nicht erhält er seine gewürfelten Punkte.	
		} else {
			playerActive.setVictoryPoints(getVictoryPoints());
		}
		
		//Beendet den Spielzug
		this.playEnd = true;
		return playerList;
	}
	
	public boolean getPlayEnd(){
		return playEnd;
	}
}
