package org.jhysim.gui.menuitem;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JDefineSimulationParametersFrame;

/**
 * To define the simulation parameters
 * @author Sébastien Majerowicz
 */
public class JHySimDefineSimulationParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = 8570269849448994572L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimDefineSimulationParametersMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		String[] simulationNames = this.jhysim.retrieveAvailableSimulations(JHySim.PLUGGED_SIMULATIONS_FILENAME);
		if (simulationNames != null)
		{
			JDefineSimulationParametersFrame jdspf = JDefineSimulationParametersFrame.getInstance(this.jhysim,simulationNames);
			if (jdspf != null) jdspf.setVisible(true);
		}
		else
		{
			JOptionPane.showMessageDialog(this,"A problem occurred with a parameter file !",JHySim.NAME,JOptionPane.ERROR_MESSAGE);
		}
	}
}