package ch.fhnw.itprojekt.noobsquad.client.interfaces;

/**
 * Das Observer Interface "beobachtet" ein Subjekt und wird benachrichtigt, wenn sich dieses aendert.
 * Die update() Methode wird verwendet um die Veränderung am Subjekt Objekt zu aktualisieren.
 * Die setSubject() Methode wird verwendet um ein Subject zu setzen, welches beobachtet werden soll.
 * 
 * @author Alexander Noever
 * @author http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
 */

public interface Observer {
	
	public void update();
	public void setSubject(Subject sub);

}
