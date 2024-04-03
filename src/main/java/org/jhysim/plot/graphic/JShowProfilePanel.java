package org.jhysim.plot.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * To show a given profile
 * @author Sébastien Majerowicz
 */
public class JShowProfilePanel extends JClosedPlotPanel implements MouseMotionListener
{
	private static final long serialVersionUID = 6494296024223809038L;
	private double[] data = null;//real data (y-coordinate)
	private int[] screenxdata = null;//data on screen (x-coordinate)
	private int[] screenydata = null;//data on screen (y-coordinate)
	private double dx = 0.0;

	private int dindex = 0;//to not display all the data point but only the ones which coincides with a pixel

	private int xc, yc;//component coordinates of the mouse when it moves inside the usefull area
	private double dataX;//real data

/**
 * Constructor
 * @param width int
 * @param height int
 * @param padding int
 * @param bgColor Color
 * @param dx double
 * @param yTitle String
 * @param ymin double
 * @param ymax double
 */
	public JShowProfilePanel (int width, int height, int padding, Color bgColor, double dx, String yTitle, double ymin, double ymax)
	{
		super(width,height,padding,bgColor,"X",0.0,1.0,yTitle,ymin,ymax);

		this.setDimension(width,height);
		this.setBackground(bgColor);
		this.setOpaque(true);

		this.dx = dx;

		xc = -1;
		yc = -1;
	}

/**
 * To paint it
 * @param g Graphics 
 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g;

		if (this.data != null)
		{
			g.setColor(Color.BLACK);
			for (int i = 1 ; i < this.screenydata.length ; i++)
			{
				g2D.drawLine(this.screenxdata[i],this.screenydata[i-1],this.screenxdata[i],this.screenydata[i]);
			}
		}

		if ((xc != -1) && (yc != -1))
		{
			g.setColor(Color.BLUE);
			g.drawLine(this.padding,yc,this.getWidth()-this.padding,yc);
			g.drawLine(xc,this.padding,xc,this.getHeight()-this.padding);
		}
	}

/**
 * To update the data
 * @param data double[]
 */
	public final void setData (double[] data)
	{
		this.data = data;

		this.dindex = this.data.length/(this.getWidth() - 2*this.padding);

		int[] tempxdata = new int[this.data.length];
		int[] tempydata = new int[this.data.length];
		int[] xy = this.getScreenCoordinates(0.0,this.data[0]);
		tempxdata[0] = xy[0];
		tempydata[0] = xy[1];
		int index = 1;
		for (int i = 1 ; i < this.data.length ; i += dindex)
		{
			xy = this.getScreenCoordinates(i*this.dx,this.data[i]);
			tempxdata[index] = xy[0];
			tempydata[index] = xy[1];
			index++;
		}

		this.screenxdata = new int[index];
		this.screenydata = new int[index];
		System.arraycopy(tempxdata,0,this.screenxdata,0,index);
		System.arraycopy(tempydata,0,this.screenydata,0,index);
	}

/**
 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
 */
	public void mouseDragged (MouseEvent ev)
	{}

/**
 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
 */
	public void mouseMoved (MouseEvent ev)
	{
		int x = ev.getX();
		int y = ev.getY();

		if ((x > this.padding) && (x < this.getWidth()-this.padding)
			&& (y >= this.padding) && (y <= this.getHeight()-this.padding))
		{
//algorithm :
//for the mouse (screen) x-coordinate = xc : retrieve the data real y
//for the data real y, retrieve the data screen y-coordinate = yc
			double[] realXY = this.getWorldCoordinates(x,y);
			this.dataX = realXY[0];
			double dataY = this.data[(int)(this.dataX*this.data.length)];
			int[] xy = this.getScreenCoordinates(0,dataY);
			this.xc = x;
			this.yc = xy[1];

			this.repaint();
		}
		else
		{
			int oldxc = xc, oldyc = yc;
//to only repaint once
			if ((oldxc != -1) && (oldyc != -1)) this.repaint();
			this.xc = -1;
			this.yc = -1;
		}
	}
}