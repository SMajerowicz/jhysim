package org.jhysim.gui.menuitem;

import javax.swing.Icon;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JUnplugSimulationFrame;

/**
 * To plug a new simulation
 * @author Sébastien Majerowicz
 */
public class JHySimUnplugSimulationMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -3705253211808883383L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimUnplugSimulationMenuItem(String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		JUnplugSimulationFrame jusf = JUnplugSimulationFrame.getInstance(jhysim,JHySim.PLUGGED_SIMULATIONS_FILENAME);
		if (jusf != null) jusf.setVisible(true);
	}
}