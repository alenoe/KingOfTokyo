package ch.fhnw.itprojekt.noobsquad.client.interfaces;

/**
 * 
 * @author Alexander Noever
 * @author http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
 */

public interface Observer {
	
	public void update();
	public void setSubject(Subject sub);

}
