package org.jhysim.gui.button;

import javax.swing.JRadioButton;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JDefineSimulationParametersFrame;

/**
 * To change the selected simulation panel
 * @author Sébastien Majerowicz
 */
public class JChangeSimulationRadioButton extends JRadioButton implements CommandPattern
{
	private static final long serialVersionUID = 5508155674991615455L;
	private JDefineSimulationParametersFrame parent = null;

/**
 * Constructor
 * @param parent JDefineSimulationParametersFrame parent frame
 * @param text String
 */
	public JChangeSimulationRadioButton(JDefineSimulationParametersFrame parent, String text)
	{
		super(text);

		this.parent = parent;
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		this.parent.changeSelectedSimulationPanel();
	}
}