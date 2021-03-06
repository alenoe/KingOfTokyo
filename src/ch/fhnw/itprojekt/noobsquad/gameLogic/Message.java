package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * 
 * @author Simon Zahnd
 * 
 * Das Message Objekt besteht aus einem String und einem beliebigen Objekt
 * Das Message Objekt kann vom Server je nach Wunsch an einen oder mehrere Clients gesendet.
 * Der String teilt dem Client mit um welches Objekt es sich handelt (also welchen cast er durchfuehren muss)
 * und bestimmt welche weiteren Methoden ausgefuehrt werden sollen.
 */

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -4975836913986272099L;
	public String Type;
	public Object Content;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		this.Type = type;
	}

	public Object getContent() {
		return Content;
	}

	public void setContent(Object content) {
		this.Content = content;
	}

	public Message(String type, Object content) {
		super();
		this.Type = type;
		this.Content = content;
	}
}
