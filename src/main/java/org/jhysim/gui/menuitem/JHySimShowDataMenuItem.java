package org.jhysim.gui.menuitem;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.math.table.gui.JDataTableFrame;

/**
 * To show the data inside a frame
 * @author Sébastien Majerowicz
 */
public class JHySimShowDataMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = 5339111909162732152L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimShowDataMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		if ((this.jhysim.getSimulator() != null) && !this.jhysim.getSimulator().isRunning())
		{
			if ((this.jhysim.getSchema() != null) && (this.jhysim.getSchema().getResults() != null))
			{
				JDataTableFrame jdtf = new JDataTableFrame(this.jhysim.getSchema().getResults());
				jdtf.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(this,"No data are available !",JHySim.NAME,JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}