package org.jhysim.plot.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.plot.gui.JYExtremumLabelFrame;

/**
 * To change the value of an y-axis extremum
 * @author Sébastien Majerowicz
 */
public class JYExtremumLabelChangeButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = -6651875744871496059L;
	private JYExtremumLabelFrame parent = null;

/**
 * Constructor
 * @param parent JDataPointFrame parent frame
 * @param label String
 * @param icon ImageIcon
 * @param mnemonic int
 */
	public JYExtremumLabelChangeButton (JYExtremumLabelFrame parent, String label, ImageIcon icon, int mnemonic)
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
		double y = 0.0;
		boolean succeed = false;

		try
		{
			y = Double.parseDouble(this.parent.getValue());
			succeed = true;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		if (succeed)
		{
			this.parent.setYExtremumValue(y);
		}
	}
}