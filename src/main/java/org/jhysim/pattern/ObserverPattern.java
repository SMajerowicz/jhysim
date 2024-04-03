package org.jhysim.pattern;

/**
 * Interface necessary to implements Observer Design Pattern
 * @author Sébastien Majerowicz
 */
public interface ObserverPattern
{
/**
 * To update the observer
 */
	public void updateObserver ();
/**
 * To kill the observer
 */
	public void killObserver ();
}