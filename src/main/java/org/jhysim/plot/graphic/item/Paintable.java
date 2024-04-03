package org.jhysim.plot.graphic.item;

import java.awt.Graphics2D;

/**
 * Interface usefull in the case of paintable object
 * @author Sébastien Majerowicz
 */
public interface Paintable
{
/**
 * To draw an object
 * @param g Graphics2D
 */
	public void paint (Graphics2D g);
}