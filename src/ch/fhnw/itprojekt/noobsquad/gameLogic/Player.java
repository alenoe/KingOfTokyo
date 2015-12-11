package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * Die Player Objekte werden vom Server erstellt, sobald ein Client sich verbindet.
 * @author Alexander Noever
 */

import java.io.Serializable;




//Das Playerobjekt kann innerhalb des Messageobjektes versendet werden (serialisiert).

public class Player implements Serializable{
		
	private int lifePoints;
	private int victoryPoints;
	private String playerName;
	private boolean inTokyo;
	private boolean cantLeaveTokyo;
	
	public Player(String playerName){
		this.lifePoints = 10;
		this.victoryPoints = 0;
		this.playerName = playerName;
		this.inTokyo = false;
		this.cantLeaveTokyo = true;
	}
	
	public String getName(){
		return this.playerName;
	}
	
	public void setLifePoints(int i){
		if(i >= 10){
			this.lifePoints = 10;
		} else if(i <= 0){
			this.lifePoints = 0;
		} else {
			this.lifePoints = i;			
		}

	}
	
	public int getLifePoints(){
		return this.lifePoints;
	}
	
	public void setVictoryPoints(int i){

		if(this.victoryPoints+i >= 20){
			System.out.println(this.playerName+" hat gewonnen.");
			this.victoryPoints = 20;
		}else{
			this.victoryPoints += i;
		}
	}
	public int getVictoryPoints(){
		return this.victoryPoints;
	}
	public void setInTokyo(boolean tokyo){
		if(tokyo == true){
			this.inTokyo = tokyo;
			this.victoryPoints = victoryPoints+1;
		} else {
			this.inTokyo = tokyo;
		}

	}

	
	public String toString(){
		return "Player: "+this.playerName+" LifePoints: "+this.lifePoints+" VictoryPoints: "+this.victoryPoints+" inTokyo: "+this.inTokyo+" Can't Leave Tokyo: "+this.cantLeaveTokyo;
	}
	public boolean getInTokyo(){
		return this.inTokyo;
	}

	public boolean getCantLeaveTokyo() {
		return cantLeaveTokyo;
	}

	public void setCantLeaveTokyo(boolean cantLeaveTokyo) {
		this.cantLeaveTokyo = cantLeaveTokyo;
	}

}
