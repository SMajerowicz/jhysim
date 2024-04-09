package org.jhysim.gui.button;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JDefineSimulationParametersFrame;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * To validate the simulation parameters
 * @author Sébastien Majerowicz
 */
public class JDefineSimulationParametersButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = 4868970659596049854L;
	private JDefineSimulationParametersFrame parent = null;

/**
 * Constructor
 * @param parent JDefineSimulationParameterFrame parent frame
 * @param label String
 * @param icon ImageIcon
 * @param mnemonic int
 */
	public JDefineSimulationParametersButton (JDefineSimulationParametersFrame parent, String label, ImageIcon icon, int mnemonic)
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
		String[] doubles = this.parent.getDoubles();
		Double[] params = new Double[doubles.length];
		ArrayList<double[]> profiles = null;
		boolean succeed = false;

		try
		{
			for (int i = 0 ; i < doubles.length ; i++)
			{
				params[i] = Double.valueOf(doubles[i]);
			}
			profiles = this.parent.getProfiles(params[0].doubleValue());
			succeed = true;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		if (succeed)
		{
			ArrayList<Double> al = new ArrayList<Double>(doubles.length);
			for (int i = 0 ; i < doubles.length ; i++) al.add(params[i]);
			NumericalSchema schema = null;

			try
			{
				Class<?> newclass = Class.forName(this.parent.getSelectedSimulationName());
				schema = (NumericalSchema)newclass.getDeclaredConstructor().newInstance();
				schema.initVariables(profiles,al);
				schema.setchoosenSchema(this.parent.getSelectedNumericalSchema());
				this.parent.getSimulation().setSchema(schema);
				this.parent.kill();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
}