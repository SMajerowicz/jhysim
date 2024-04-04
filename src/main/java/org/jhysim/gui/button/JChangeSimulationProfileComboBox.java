package org.jhysim.gui.button;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.gui.form.JDefineSimulationParametersPanel;

/**
 * To change the selected profile
 * @author Sébastien Majerowicz
 */
public class JChangeSimulationProfileComboBox extends JComboBox implements CommandPattern
{
	private static final long serialVersionUID = -8430495713693712676L;
	private JDefineSimulationParametersPanel parent = null;

/**
 * Constructor
 * @param parent JDefineSimulationParametersPanel
 * @param model DefaultComboBoxModel
 */
	public JChangeSimulationProfileComboBox (JDefineSimulationParametersPanel parent, DefaultComboBoxModel model)
	{
		super(model);

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