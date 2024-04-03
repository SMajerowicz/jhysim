package org.jhysim.plot.graphic.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Iterator;

import org.jhysim.pattern.ObservablePattern;
import org.jhysim.pattern.ObserverPattern;

/**
 * Represents a point on a panel
 * @author Sébastien Majerowicz
 */
public class DataPoint extends Point implements Choosable, ObservablePattern, Paintable
{
	private static final long serialVersionUID = 2180975954121023953L;

	private final static int RADIUS = 5;

	private double wx, wy;//world coordinates

	private boolean selected = false;

	private ArrayList<ObserverPattern> observers = null;

/**
 * Constructor
 * @param wx double world x coordinate
 * @param wy double world y coordinate
 * @param x int screen x coordinate
 * @param y int screen y coordinate
 */
	public DataPoint(double wx, double wy, int x, int y)
	{
		super(x,y);

		this.wx = wx;
		this.wy = wy;

		this.observers = new ArrayList<ObserverPattern>(0);
	}

/**
 * @see org.plot.graphic.items.Paintable#paint(java.awt.Graphics2D)
 */
	public void paint(Graphics2D g)
	{
		if (!selected)
		{
			g.setColor(Color.BLUE);
		}
		else
		{
			g.setColor(Color.RED);
		}
		g.fillOval(this.x-DataPoint.RADIUS,this.y-DataPoint.RADIUS,2*DataPoint.RADIUS,2*DataPoint.RADIUS);
		g.setColor(Color.RED);
		g.drawOval(this.x-DataPoint.RADIUS,this.y-DataPoint.RADIUS,2*DataPoint.RADIUS,2*DataPoint.RADIUS);
	}

/**
 * To select or not this point
 * @param selected boolean
 */
	public final void select (boolean selected)
	{
		this.selected = selected;
	}
/**
 * To know whether this point is selected or not
 * @return boolean
 */
	public final boolean isSelected ()
	{
		return this.isSelected();
	}

/**
 * @see org.plot.graphic.items.Choosable#isInside(int, int)
 */
	public boolean isInside (int x, int y)
	{
		boolean succeed = false;

		if (this.distance(x,y) < DataPoint.RADIUS)
		{
			succeed = true;
		}

		return succeed;
	}

/**
 * To retrieve the world x-coordinate
 * @return double
 */
	public final double getWx()
	{
		return wx;
	}

/**
 * To initialize the world x-coordinate
 * @param wx double
 */
	public final void setWx (double wx)
	{
		this.wx = wx;
	}

/**
 * To retrieve the world y-coordinate
 * @return double
 */
	public final double getWy()
	{
		return wy;
	}

/**
 * To intialize the world x-coordinate
 * @param wy double
 */
	public final void setWy (double wy)
	{
		this.wy = wy;
	}

/**
 * @see org.application.pattern.ObservablePattern#getObservers()
 */
	public ArrayList<ObserverPattern> getObservers()
	{
		return this.observers;
	}

/**
 * @see org.application.pattern.ObservablePattern#addObserver(org.application.pattern.ObserverPattern)
 */
	public void addObserver (ObserverPattern observer)
	{
		this.observers.add(observer);
	}

/**
 * @see org.application.pattern.ObservablePattern#removeObserver(org.application.pattern.ObserverPattern)
 */
	public void removeObserver (ObserverPattern observer)
	{
		this.observers.remove(observer);
	}

/**
 * @see org.application.pattern.ObservablePattern#removeAllObservers()
 */
	public void removeAllObservers()
	{
		int n = this.observers.size();

		for (int i = 0 ; i < n ; i++)
		{
			((ObserverPattern)this.observers.get(0)).killObserver();
			this.observers.remove(0);
		}

		this.observers = new ArrayList<ObserverPattern>(0);
	}

/**
 * @see org.application.pattern.ObservablePattern#notifyObservers()
 */
	public void notifyObservers()
	{
		Iterator<ObserverPattern> iterator = this.observers.iterator();
		while (iterator.hasNext())
		{
			((ObserverPattern)iterator.next()).updateObserver();
		}
	}
}