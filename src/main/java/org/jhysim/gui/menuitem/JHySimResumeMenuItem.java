package org.jhysim.gui.menuitem;

import javax.swing.Icon;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulator;

/**
 * To resume the simulation
 * @author Sébastien Majerowicz
 */
public class JHySimResumeMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -1887611624677154965L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimResumeMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		Simulator simulator = this.jhysim.getSimulator();
		if ((simulator != null) && (simulator.getSchema() != null) && simulator.isPaused())	simulator.goOn();
	}
}