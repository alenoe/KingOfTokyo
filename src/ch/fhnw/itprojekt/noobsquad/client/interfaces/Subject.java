package ch.fhnw.itprojekt.noobsquad.client.interfaces;

/**
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
