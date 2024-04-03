package org.jhysim.plot.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Iterator;

import org.jhysim.plot.graphic.item.DataPoint;

import org.jhysim.plot.gui.JDataPointFrame;
import org.jhysim.plot.gui.JYExtremumLabelFrame;

/**
 * To define a profile by clicking on a panel
 * @author Sébastien Majerowicz
 */
public class JDefineProfilePanel extends JClosedPlotPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 4818679036330668954L;
	private final static String XTITLE = "X";
	private final static int NO_DATA_POINT_SELECTED = -1;

	private ArrayList<DataPoint> dataPointList = null;
	private int selectedDataPointIndex = JDefineProfilePanel.NO_DATA_POINT_SELECTED;

/**
 * Constructor
 * @param width int width
 * @param height int height
 * @param padding int padding
 * @param bgColor Color background color
 * @param ymin double y minimum
 * @param ymax double y maximum
 * @param yTitle String y axis title
 */
	public JDefineProfilePanel (int width, int height, int padding, Color bgColor, double ymin, double ymax, String yTitle)
	{
		super(width,height,padding,bgColor,JDefineProfilePanel.XTITLE,0.0,1.0,yTitle,ymin,ymax);

		this.setDimension(width,height);
		this.setBackground(bgColor);
		this.setOpaque(true);

		this.dataPointList = new ArrayList<DataPoint>(0);

		double[] xy = this.getWorldCoordinates(this.padding,this.getHeight()-this.padding);
		this.dataPointList.add(new DataPoint(xy[0],xy[1],this.padding,this.getHeight()-this.padding));
		xy = this.getWorldCoordinates(this.getWidth()-this.padding,this.getHeight()-this.padding);
		this.dataPointList.add(new DataPoint(xy[0],xy[1],this.getWidth()-this.padding,this.getHeight()-this.padding));

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

/**
 * To sort the data point list
 * <b>Only the selected data point must be moved to the right place because only these coordinates may change</b> 
 */
	public final void sortList ()
	{
		int size = this.dataPointList.size();
		DataPoint sdp = null;
		if (this.selectedDataPointIndex != JDefineProfilePanel.NO_DATA_POINT_SELECTED)//where a data point was dragged
		{
			sdp = (DataPoint)this.dataPointList.get(this.selectedDataPointIndex);
			this.dataPointList.remove(this.selectedDataPointIndex);
		}
		else//where a data point is added
		{
			sdp = (DataPoint)this.dataPointList.get(size-1);
			this.dataPointList.remove(size-1);
		}

		size--;//after the removing, the data point list size is minus by 1
		int index = 0;
		while ((index < size) && (sdp.getX() > ((DataPoint)this.dataPointList.get(index)).getX()))
		{
			index++;
		}

		this.dataPointList.add(index,sdp);
	}

/**
 * To retrieve a profile according to a straight line interpolation
 * @param dx double dx
 * @return double[]
 */
	public final double[] getProfile (double dx)
	{
		double[] profile = new double[1+(int)(1.0/dx)];

		double coeff = 0.0;
		DataPoint dp1 = (DataPoint)this.dataPointList.get(0);
		DataPoint dp2 = null;
		double[] wc1 = this.getWorldCoordinates((int)dp1.getX(),(int)dp1.getY());
		double[] wc2 = null;
		int size = this.dataPointList.size();
		int posindex = 0;

		for (int i = 1 ; i < size ; i++)
		{
			dp2 = (DataPoint)this.dataPointList.get(i);
			wc2 = this.getWorldCoordinates((int)dp2.getX(),(int)dp2.getY());
			coeff = (wc2[1]-wc1[1])/(wc2[0]-wc1[0]);

			while (posindex*dx <= wc2[0])
			{
				profile[posindex] = coeff*(posindex*dx-wc1[0]) + wc1[1];
				posindex++;
			}

			dp1 = dp2;
			wc1 = wc2;
		}

		return profile;
	}

/**
 * To intialize the displayed profile according to a double array
 * <b>This procedure finds the gradient changes to put a DataPoint objet at this location</b>
 * @param profile double[]
 */
	public final void setProfile (double[] profile)
	{
		ArrayList<DataPoint> al = new ArrayList<DataPoint>(0);

//first save the first point
		int[] sc = this.getScreenCoordinates(0.0,profile[0]);
		al.add(new DataPoint(0.0,profile[0],sc[0],sc[1]));

//add a point to a change of more than 10%
		double dx = 1.0/(profile.length-1);
		double oldcoeff = (profile[1]-profile[0])/dx;
		double coeff = 0.0;
		for (int i = 1 ; i < profile.length-1 ; i++)
		{
			coeff = (profile[i+1]-profile[i])/dx;
			if ((coeff/oldcoeff > 1.1) || (coeff/oldcoeff < 0.9))
			{
				sc = this.getScreenCoordinates(i*dx,profile[i]);
				al.add(new DataPoint(i*dx,profile[i],sc[0],sc[1]));
				oldcoeff = coeff;
			}
		}

//finally save the last point
		sc = this.getScreenCoordinates(1.0,profile[profile.length-1]);
		al.add(new DataPoint(1.0,profile[profile.length-1],sc[0],sc[1]));

		if (al.size() > 1) this.dataPointList = al;
	}

/**
 * To paint it
 * @param g Graphics 
 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g;

		int size = this.dataPointList.size();
		DataPoint dp1 = (DataPoint)this.dataPointList.get(0), dp2 = null;
		for (int i = 1 ; i < size ; i++)
		{
			dp2 = (DataPoint)this.dataPointList.get(i);
			dp1.paint(g2D);
			g2D.drawLine((int)dp1.getX(),(int)dp1.getY(),(int)dp2.getX(),(int)dp2.getY());
			dp2.paint(g2D);
			dp1 = dp2;
		}
	}

/**
 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
 */
	public void mouseDragged(MouseEvent e)
	{
		if (this.isInUsefullArea(e.getX(),e.getY()) && (this.selectedDataPointIndex != JDefineProfilePanel.NO_DATA_POINT_SELECTED))
		{
			if (this.selectedDataPointIndex == 0)//the first and the last data point must be respectively at 0.0 and 1.0 (the list is previously sorted)
			{
				((DataPoint)this.dataPointList.get(0)).setLocation(this.padding,e.getY());
				double[] wxy = this.getWorldCoordinates(this.padding,e.getY());
				((DataPoint)this.dataPointList.get(0)).setWx(wxy[0]);
				((DataPoint)this.dataPointList.get(0)).setWy(wxy[1]);
			}
			else if (this.selectedDataPointIndex == this.dataPointList.size()-1)
			{
				((DataPoint)this.dataPointList.get(this.dataPointList.size()-1)).setLocation(this.getWidth()-this.padding,e.getY());
				double[] wxy = this.getWorldCoordinates(this.getWidth()-this.padding,e.getY());
				((DataPoint)this.dataPointList.get(this.dataPointList.size()-1)).setWx(wxy[0]);
				((DataPoint)this.dataPointList.get(this.dataPointList.size()-1)).setWy(wxy[1]);
			}
			else
			{
				((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).setLocation(e.getX(),e.getY());
				double[] wxy = this.getWorldCoordinates(e.getX(),e.getY());
				((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).setWx(wxy[0]);
				((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).setWy(wxy[1]);
			}

			((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).notifyObservers();

			this.repaint();
		}
	}
/**
 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
 */
	public void mouseMoved(MouseEvent e)
	{}

/**
 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
 */
	public void mouseClicked(MouseEvent e)
	{
		int ex = e.getX();
		int ey = e.getY();

		if (this.isInUsefullArea(ex,ey) && (e.getClickCount() == 2))
		{
			this.selectedDataPointIndex = JDefineProfilePanel.NO_DATA_POINT_SELECTED;
//seach for a potentially selected data point
			Iterator<DataPoint> iterator = this.dataPointList.iterator();
			int index = 0;

			while (iterator.hasNext())
			{
				if (((DataPoint)iterator.next()).isInside(ex,ey))
				{
					this.selectedDataPointIndex = index;
					break;
				}
				index++;
			}

			if (this.selectedDataPointIndex != JDefineProfilePanel.NO_DATA_POINT_SELECTED)
			{
				DataPoint dp = (DataPoint)this.dataPointList.get(this.selectedDataPointIndex);
				boolean enableX = true;
				if ((this.selectedDataPointIndex == 0) || (this.selectedDataPointIndex == this.dataPointList.size()-1)) enableX = false;
				JDataPointFrame jdpf = new JDataPointFrame(this,dp,enableX,true);
				jdpf.setVisible(true);
				dp.addObserver(jdpf);
			}
		}

		if (!this.isInUsefullArea(ex,ey) && (e.getClickCount() == 2))
		{
//is this click happened on an y extremum ?
			if (this.yminLabel.isInside(ex,ey))
			{
				JYExtremumLabelFrame jyelf = new JYExtremumLabelFrame(this,this.yminLabel,false);
				jyelf.setVisible(true);
			}
			else if (this.ymaxLabel.isInside(ex,ey))
			{
				JYExtremumLabelFrame jyelf = new JYExtremumLabelFrame(this,this.ymaxLabel,true);
				jyelf.setVisible(true);
			}
		}
	}
/**
 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
 */
	public void mousePressed(MouseEvent e)
	{
		int ex = e.getX();
		int ey = e.getY();
//must be in the usefull area
		if (this.isInUsefullArea(ex,ey))
		{
			this.selectedDataPointIndex = JDefineProfilePanel.NO_DATA_POINT_SELECTED;
//seach for a potentially selected data point
			Iterator<DataPoint> iterator = this.dataPointList.iterator();
			int index = 0;

			while (iterator.hasNext())
			{
				if (((DataPoint)iterator.next()).isInside(ex,ey))
				{
					this.selectedDataPointIndex = index;
					break;
				}
				index++;
			}

			if (this.selectedDataPointIndex == JDefineProfilePanel.NO_DATA_POINT_SELECTED)
			{
				double[] xy = this.getWorldCoordinates(ex,ey);
				this.dataPointList.add(new DataPoint(xy[0],xy[1],ex,ey));
				this.sortList();
			}
			else
			{
				((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).select(true);
			}

			this.repaint();
		}
	}
/**
 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
 */
	public void mouseReleased(MouseEvent e)
	{
		if (this.selectedDataPointIndex != JDefineProfilePanel.NO_DATA_POINT_SELECTED)
		{
			((DataPoint)this.dataPointList.get(this.selectedDataPointIndex)).select(false);
			this.sortList();
			this.selectedDataPointIndex = JDefineProfilePanel.NO_DATA_POINT_SELECTED;
			this.repaint();
		}
	}
/**
 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
 */
	public void mouseEntered(MouseEvent e)
	{}
/**
 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
 */
	public void mouseExited(MouseEvent e)
	{}

/**
 * Compute the coordinates for all data points after after a change of the y-axis range 
 */
	private final void recomputeDataPointCoordinates ()
	{
		int[] sc = null;

		Iterator<DataPoint> iterator = this.dataPointList.iterator();
		DataPoint dp = null;

		while (iterator.hasNext())
		{
			dp = (DataPoint)iterator.next();
			sc = this.getScreenCoordinates(dp.getWx(),dp.getWy());
			dp.setLocation(sc[0],sc[1]);
		}
	}

/**
 * To initiliaze ymin
 * @param ymin double
 */
	public final void setYmin (double ymin)
	{
		super.setYmin(ymin);
		this.recomputeDataPointCoordinates();
	}

/**
 * To initiliaze ymax
 * @param ymax double
 */
	public final void setYmax (double ymax)
	{
		super.setYmax(ymax);
		this.recomputeDataPointCoordinates();
	}
}