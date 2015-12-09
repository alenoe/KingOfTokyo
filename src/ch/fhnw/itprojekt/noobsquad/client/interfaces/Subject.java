package ch.fhnw.itprojekt.noobsquad.client.interfaces;

public interface Subject {
	
	public void register(Observer obj);
	public void unregister(Observer obj);
	public void notifyObservers();
	public Object getUpdate(Observer obj);

}
