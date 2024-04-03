package org.jhysim.plot.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import org.jhysim.plot.graphic.item.YExtremumLabel;

/**
 * To represent a closed plot
 * @author Sébastien Majerowicz
 */
public class JClosedPlotPanel extends JPanel
{
	private static final long serialVersionUID = -2264594701349259799L;
	protected int padding = 0;
	protected String xTitle = null;
	protected String yTitle = null;

	protected double xmin = 0.0, xmax = 0.0;
	protected double ymin = 0.0, ymax = 0.0;

	protected DecimalFormat dxFormat = null;
	protected DecimalFormat dyFormat = null;

	protected YExtremumLabel yminLabel = null;
	protected YExtremumLabel ymaxLabel = null;

	protected String xminS = null, xmaxS = null;

/**
 * Constructor
 * @param width int width
 * @param height int height
 * @param padding int padding
 * @param bgColor Color background color
 * @param xTitle String x axis title
 * @param xmin double world x minimum
 * @param xmax double world x maximum
 * @param yTitle String y axis title
 * @param ymin double world y minimum
 * @param ymax double world y maximum
 */
	public JClosedPlotPanel (int width, int height, int padding, Color bgColor, String xTitle, double xmin, double xmax,
																				String yTitle, double ymin, double ymax)
	{
		super(true);

		this.setDimension(width,height);
		this.setBackground(bgColor);
		this.setOpaque(true);

		this.padding = padding;
		this.xTitle = xTitle;
		this.xmin = xmin;
		this.xmax = xmax;
		this.yTitle = yTitle;
		this.ymin = ymin;
		this.ymax = ymax;

		this.dxFormat = new DecimalFormat("0.0");
		this.dyFormat = new DecimalFormat("0.0");

		this.xminS = dxFormat.format(this.xmin).replace(',','.');
		this.xmaxS = dxFormat.format(this.xmax).replace(',','.');

		this.yminLabel = new YExtremumLabel(dyFormat.format(this.ymin).replace(',','.'),this.padding,this.getHeight()-this.padding);
		this.ymaxLabel = new YExtremumLabel(dyFormat.format(this.ymax).replace(',','.'),this.padding,this.padding);
	}

/**
 * To initialize the size of this panel
 * @param width int width
 * @param height int height
 */
	public final void setDimension (int width, int height)
	{
		this.setMinimumSize(new Dimension(width,height));
		this.setPreferredSize(new Dimension(width,height));
		this.setSize(width,height);
	}

/**
 * To transform screen coordinates to world coordinates
 * @param x int x coordinate
 * @param y int y coordinate
 * @return double[] x and y world coordinates
 */
	public final double[] getWorldCoordinates (int x, int y)
	{
		double[] wc = new double[2];

		wc[0] = this.xmin + (this.xmax-this.xmin)*
							(double)(x-this.padding)/(double)(this.getWidth()-2*this.padding);
		wc[1] = this.ymin + (this.ymax-this.ymin)*
							(double)(this.getHeight()-this.padding-y)/(double)(this.getHeight()-2*this.padding);

		return wc;
	}

/**
 * To transform world coordinates to screen coordinates
 * @param x double x
 * @param y double y
 * @return int[]
 */
	public final int[] getScreenCoordinates (double x, double y)
	{
		int[] sc = new int[2];

		sc[0] = this.padding+(int)(((double)(this.getWidth()-2*this.padding)*(x-this.xmin))
									/(double)(this.xmax-this.xmin));
		sc[1] = this.getHeight()-this.padding+(int)((double)((this.ymin-y)*(this.getHeight()-2*this.padding))
													/(double)(this.ymax-this.ymin));

		return sc;
	}

/**
 * To know if a point is inside the usefull area
 * @param x int x coordinate
 * @param y int y coordinate
 * @return boolean 
 */
	protected final boolean isInUsefullArea (int x, int y)
	{
		boolean succeed = false;

		if ((x >= this.padding) && (x <= this.getWidth()-this.padding) &&
			(y >= this.padding) && (y <= this.getHeight()-this.padding)) succeed = true;

		return succeed;
	}

/**
 * To paint it
 * @param g Graphics 
 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g;

		g2D.setColor(Color.BLACK);
		g2D.drawRect(this.padding,this.padding,this.getWidth()-2*this.padding,this.getHeight()-2*this.padding);

		g2D.setFont(new Font("Arial",Font.PLAIN,12));
		g2D.drawString(this.xTitle,(int)(0.5*(this.getWidth()-g2D.getFontMetrics().stringWidth(this.xTitle))),
					   this.getHeight()-(int)(0.5*(this.padding-g2D.getFont().getSize())));
		AffineTransform current_af = g2D.getTransform();
		AffineTransform af = new AffineTransform();
		af.translate((int)(0.5*(this.padding+g2D.getFont().getSize())),(int)(0.5*(this.getHeight()+g2D.getFontMetrics().stringWidth(this.yTitle))));
		af.rotate(-0.5*Math.PI);
		af.preConcatenate(current_af);
		g2D.setTransform(af);
		g2D.drawString(this.yTitle,0,0);
		g2D.setTransform(current_af);

		this.ymaxLabel.paint(g2D);
		g2D.drawString(this.xmaxS,this.getWidth()-this.padding-g2D.getFontMetrics().stringWidth(this.xmaxS),this.getHeight()-this.padding+g2D.getFont().getSize()+2);

		if (this.ymin < 0.0)
		{
			int zero = (this.getScreenCoordinates(0.0,0.0))[1];
			g2D.drawLine(this.padding,zero,this.getWidth()-this.padding,zero);
			g2D.drawString("0.0",this.getWidth()-this.padding+2,zero+(int)(0.5*g2D.getFont().getSize()));
		}

		if (this.ymin != this.xmin)
		{
			this.yminLabel.paint(g2D);
			g2D.drawString(this.xminS,this.padding,this.getHeight()-this.padding+g2D.getFont().getSize()+2);
		}
		else
		{
			this.yminLabel.setX(this.padding);
			this.yminLabel.setY(this.getHeight()-this.padding+2);
			this.yminLabel.paint(g2D);
//			g2D.drawString(this.yminLabel.getLabel(),this.padding-g2D.getFontMetrics().stringWidth(this.yminLabel.getLabel())-2,this.getHeight()-this.padding+g2D.getFont().getSize()+2);
		}
	}

/**
 * To initiliaze ymin
 * @param ymin double
 */
	public void setYmin (double ymin)
	{
		this.ymin = ymin;
		this.yminLabel.setLabel(dyFormat.format(this.ymin).replace(',','.'));
	}

/**
 * To initiliaze ymax
 * @param ymax double
 */
	public void setYmax (double ymax)
	{
		this.ymax = ymax;
		this.ymaxLabel.setLabel(dyFormat.format(this.ymax).replace(',','.'));
	}
}