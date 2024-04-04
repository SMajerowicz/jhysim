package org.jhysim.gui.menuitem;

import javax.swing.Icon;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JDefineSimulatorParametersFrame;

/**
 * To open a frame which allows to define the simulator parameters
 * @author Sébastien Majerowicz
 */
public class JHySimDefineSimulatorParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -5697018428895866729L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimDefineSimulatorParametersMenuItem(String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		if ((this.jhysim.getSimulator() == null) || !this.jhysim.getSimulator().isRunning())
		{
			if (this.jhysim.getSchema() != null)
			{
				JDefineSimulatorParametersFrame jdspf = JDefineSimulatorParametersFrame.getInstance(jhysim);
				if (jdspf != null) jdspf.setVisible(true);
			}
		}
	}
}