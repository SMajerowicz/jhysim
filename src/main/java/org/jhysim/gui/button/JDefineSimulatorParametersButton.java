package org.jhysim.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.util.ArrayList;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulator;

import org.jhysim.gui.JDefineSimulatorParametersFrame;

/**
 * To validate the simulator parameters
 * @author Sébastien Majerowicz
 */
public class JDefineSimulatorParametersButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = -3801705646902369977L;
	private JDefineSimulatorParametersFrame parent = null;

/**
 * Constructor
 * @param parent JDefineSimulatorParameterFrame parent frame
 * @param label String
 * @param icon ImageIcon
 * @param mnemonic int
 */
	public JDefineSimulatorParametersButton (JDefineSimulatorParametersFrame parent, String label, ImageIcon icon, int mnemonic)
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
		int nts = 0;
		int sd = 0;
		int dt = 0;

		boolean succeed = false;

		int nVariables = this.parent.getSimulation().getSchema().getNProfiles();
		ArrayList<double[]> yexts = new ArrayList<double[]>(nVariables);

		try
		{
			nts = Integer.parseInt(this.parent.getnTimeSteps());
			sd = Integer.parseInt(this.parent.getsavingDelay());
			if (this.parent.isDisplaySimulation())
			{
				dt = Integer.parseInt(this.parent.getdisplayUpdatingTimeDelay());
				double ymin, ymax;
				for (int i = 0 ; i < nVariables ; i++)
				{
					ymin = Double.parseDouble(this.parent.getYMin(i));
					ymax = Double.parseDouble(this.parent.getYMax(i));
					yexts.add(new double[]{ymin,ymax});
				}
			}
			else
			{
				dt = 10;
			}
			succeed = true;
		}
		catch (NumberFormatException ex)
		{}

		if (succeed && (nts > 0) && (sd > 0) && (dt > 0))
		{
			this.parent.getSimulation().setSimulator(new Simulator(this.parent.getSimulation().getSchema(),sd,nts,this.parent.isDisplaySimulation(),dt));
			if (this.parent.isDisplaySimulation())
			{
				for (int i = 0 ; i < nVariables ; i++)
				{
					this.parent.getSimulation().getSchema().setYProfileExtrema(i,(double[])yexts.get(i));
				}
			}
			this.parent.kill();
		}
	}
}