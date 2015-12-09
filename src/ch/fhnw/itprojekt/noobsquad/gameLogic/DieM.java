package ch.fhnw.itprojekt.noobsquad.gameLogic;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class DieM implements Serializable{
	private int id, value;
	private boolean lock;
	private String facePicture, facePictureLock; 
	private TreeMap<Integer, String> facePictures = new TreeMap<Integer, String>();
	private TreeMap<Integer, String> facePicturesLock = new TreeMap<Integer, String>();
	
	public DieM(int id){
		initializeFacePictures();
		initializeFacePicturesLock();
		this.id = id;
		this.value = this.roll();
		this.facePicture = this.setFacePicture();
		this.facePictureLock = this.setFacePictureLock();
		this.lock = false;
	}
	
	//Der W�rfel wird neu gew�rfelt und das zugeh�rige Bild wird zugewiesen.
	
	public int roll(){
		this.value = (int) Math.floor(Math.random() * 5) + 1;
		this.setFacePicture();
		this.setFacePictureLock();
		return value;
	}
	
	//Stringausgabe des W�rfels
	
	public String toString(){
		return "ID: " + this.id + ", value: " + this.value + " Anzeige: " + this.facePicture;
	}
	
	//Initialisierung der W�rfelbilder
	
	public TreeMap<Integer, String> initializeFacePictures(){
		facePictures.put(1, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_one.png");
		facePictures.put(2, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_two.png");
		facePictures.put(3, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_three.png");
		facePictures.put(4, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_attack.png");
		facePictures.put(5, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_heart.png");
		return facePictures;
	}
	
	public TreeMap<Integer, String> initializeFacePicturesLock(){
		facePicturesLock.put(1, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_one_ok.png");
		facePicturesLock.put(2, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_two_ok.png");
		facePicturesLock.put(3, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_three_ok.png");
		facePicturesLock.put(4, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_attack_ok.png");
		facePicturesLock.put(5, "ch"+File.separator+"fhnw"+File.separator+"itprojekt"+File.separator+"noobsquad"+File.separator+"client"+File.separator+"pictures"+File.separator+"dice_heart_ok.png");
		return facePicturesLock;
	}
	
	// Lock getter/setter
	
	public boolean getLock(){
		return this.lock;
	}
	
	public void setLock(boolean state){
		this.lock = state;
	}
	
	//Schl�sselfunktion f�r die Lock Variable
	
//	public void removeLock(){
//		this.lock = false;
//	}
	
	//Value getter + setter
	
	public int getValue(){
		return this.value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	//ID getter
	
	public int getID(){
		return this.id;
	}
	
	//FacePicture getter/setter
	
	public String getFacePicture(){
		return this.facePicture;
	}
	
	public String setFacePicture(){
		return this.facePicture = facePictures.get(this.value);
	}
	
	//FacePictureLock getter/setter
	
	public String getFacePictureLock(){
		return this.facePictureLock;
	}
	
	public String setFacePictureLock(){
		return this.facePictureLock = facePicturesLock.get(this.value);
	}
	
/*	public static void main(String[] args) {

		DieM one = new DieM(1);
		DieM two = new DieM(2);
		DieM three = new DieM(3);
		DieM four = new DieM(4);
		
		System.out.println(one.toString()+ "\n" + two.toString() + "\n" + three.toString() + "\n" + four.toString());
		
		one.roll();
		two.roll();
		three.roll();
		four.roll();
		
		System.out.println(one.toString()+ "\n" + two.toString() + "\n" + three.toString() + "\n" + four.toString());
	}*/

}
