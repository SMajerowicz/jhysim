package org.jhysim.pattern;

import java.util.ArrayList;

/**
 * Interface necessary to implements Observer Design Pattern
 * @author Sébastien Majerowicz
 */
public interface ObservablePattern
{
/**
 * To retrieve all the observers
 * @return ArrayList
 */
	public ArrayList<ObserverPattern> getObservers ();
/**
 * To add a observer
 * @param observer ObserverPattern
 */
	public void addObserver (ObserverPattern observer);
/**
 * To remove an observer
 * @param observer ObserverPattern
 */
	public void removeObserver (ObserverPattern observer);
/**
 * To remove all the observers
 */
	public void removeAllObservers ();
/**
 * To notify all observers that they must be updated
 */
	public void notifyObservers ();
}