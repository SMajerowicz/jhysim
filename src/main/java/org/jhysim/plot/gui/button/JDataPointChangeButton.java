package org.jhysim.plot.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.jhysim.pattern.CommandPattern;
import org.jhysim.plot.gui.JDataPointFrame;

/**
 * To change the coordinates of a data point
 * @author Sébastien Majerowicz
 */
public class JDataPointChangeButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = 975869454113495581L;
	private JDataPointFrame parent = null;

/**
 * Constructor
 * @param parent JDataPointFrame
 * @param label String
 * @param icon ImageIcon
 * @param mnemonic int
 */
	public JDataPointChangeButton (JDataPointFrame parent, String label, ImageIcon icon, int mnemonic)
	{
		super(label,icon);

		this.setMnemonic(mnemonic);
		this.parent = parent;
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		String sx = this.parent.getXCoordinate();
		String sy = this.parent.getYCoordinate();

		double x = 0.0, y = 0.0;
		boolean succeed = true;

		try
		{
			x = Double.parseDouble(sx);
			y = Double.parseDouble(sy);
		}
		catch (NumberFormatException e)
		{
			succeed = false;
			e.printStackTrace();
		}

		if (succeed)
		{
			this.parent.setDataPointCoordinates(x,y);
		}
	}
}