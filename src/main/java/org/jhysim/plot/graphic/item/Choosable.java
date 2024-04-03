package org.jhysim.plot.graphic.item;

/**
 * Interface to know if an object can be choosen according to some coordinates
 * @author Sébastien Majerowicz
 */
public interface Choosable
{
/**
 * A certain point is inside or not this object
 * @param x int x-coordinate
 * @param y int y-coordinate
 * @return boolean is inside (true) or not (false)
 */
	public boolean isInside (int x, int y);
}