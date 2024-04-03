package org.jhysim.plot.graphic.item;

import java.awt.Graphics2D;

/**
 * Class which represents label defining an axis extremum
 * @author Sébastien Majerowicz
 */
public class YExtremumLabel implements Choosable, Paintable
{
	private String label = null;

	private int x,y;

	private boolean dimensionComputed = false;

	private int width = -1;
	private int height = -1;

/**
 * Constructor
 * @param label String extremum value
 * @param x int x-coordinate of the right side
 * @param y int y-coordinate of the right side
 */
	public YExtremumLabel (String label, int x, int y)
	{
		this.label = label;
		this.x = x;
		this.y = y;
	}

/**
 * @see org.plot.graphic.items.Choosable#isInside(int, int)
 */
	public boolean isInside(int x, int y)
	{
		boolean succeed = false;

		if ((x >= this.x-this.width) && (x <= this.x) &&
			(y <= this.y+this.height) && (y >= this.y))
		{
			succeed = true;
		}

		return succeed;
	}

/**
 * @see org.plot.graphic.items.Paintable#paint(java.awt.Graphics2D)
 */
	public void paint(Graphics2D g)
	{
		if (!dimensionComputed)
		{
			this.width = g.getFontMetrics().stringWidth(this.label);
			this.height = g.getFont().getSize();
			this.dimensionComputed = true;
		}

		g.drawString(this.label,this.x-this.width-2,this.y+(int)(0.5f*this.height));
	}
/**
 * To retrieve the label value
 * @return String
 */
	public final String getLabel()
	{
		return this.label;
	}

/**
 * To initialize the label value
 * @param label String label
 */
	public final void setLabel (String label)
	{
		this.label = label;
		this.dimensionComputed = false;
	}

/**
 * To initialize the x-ccordinate of the right side
 * @param x int x
 */
	public final void setX (int x)
	{
		this.x = x;
		this.dimensionComputed = false;
	}

/**
 * To initialize the y-coordinate of the right side
 * @param y int y
 */
	public final void setY (int y)
	{
		this.y = y;
		this.dimensionComputed = false;
	}
}