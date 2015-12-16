package ch.fhnw.itprojekt.noobsquad.client.interfaces;

/**
 * Das Subject Interface laesst Observer sich bei ihm registrieren und benachrichtigt diese falls 
 * Aenderungen vorgenommen werden.
 * Die register(Observer obj)/unregister(Observer obj) Methoden lassen Observer hinzufuegen oder schliessen
 * sie aus der Benachrichtigung aus.
 * 
 * notyfyObservers() benachrichtigt alle Observer, dass es Aenderungen am beobachteten Objekt gibt.
 * 
 * getUpdate(Observer obj) gibt dem Observer Objekt die Aenderung am Subjekt zurueck.
 * 
 *@author Alexander Noever 
 *@author http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
 */

public interface Subject {
	
	public void register(Observer obj);
	public void unregister(Observer obj);
	public void notifyObservers();
	public Object getUpdate(Observer obj);

}
