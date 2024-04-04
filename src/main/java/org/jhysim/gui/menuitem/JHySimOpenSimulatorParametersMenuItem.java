package org.jhysim.gui.menuitem;

import java.io.IOException;

import java.util.ArrayList;

import javax.swing.Icon;

import javax.xml.parsers.ParserConfigurationException;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JXmlFileChooser;

import org.jhysim.simulation.Simulator;

import org.jhysim.simulation.io.XmlSimulatorParametersFile;

import org.xml.sax.SAXException;

/**
 * To open an XML file containing simulator parameters
 * @author Sébastien Majerowicz
 */
public class JHySimOpenSimulatorParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = 6609352053048308842L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimOpenSimulatorParametersMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
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
			JXmlFileChooser jxfc = new JXmlFileChooser(".","Open simulator parameters");
			int open = jxfc.showOpenDialog(this);
			if (open == JXmlFileChooser.APPROVE_OPTION)
			{
				XmlSimulatorParametersFile xspf = new XmlSimulatorParametersFile(jxfc.getSelectedFile().getAbsolutePath());
				boolean succeed = false;
				try
				{
					xspf.extractParameters();
					succeed = true;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{
					e.printStackTrace();
				}
				catch (SAXException e)
				{
					e.printStackTrace();
				}

				if (succeed)
				{
					this.jhysim.setSimulator(new Simulator(this.jhysim.getSchema(),xspf.getSavingDelay(),xspf.getMaxTimeSteps(),xspf.isDisplaySimulation(),xspf.getDisplayUpdatingTimeDelay()));

					ArrayList<double[]> yextlist = xspf.getYExtList();
					int size = yextlist.size();
					for (int i = 0 ; i< size ; i++)
					{
						this.jhysim.getSimulator().getSchema().setYProfileExtrema(i,(double[])yextlist.get(i));
					}
				}
			}
		}
	}
}