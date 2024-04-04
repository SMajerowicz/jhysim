package org.jhysim.gui.menuitem;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import java.util.ArrayList;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JXmlFileChooser;

import org.jhysim.simulation.Simulator;

import org.jhysim.simulation.io.XmlSimulatorParametersFile;

/**
 * To save the simulator parameters into an XML file
 * @author Sébastien Majerowicz
 */
public class JHySimSaveSimulatorParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -2677737238631445144L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimSaveSimulatorParametersMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		Simulator simulator = this.jhysim.getSimulator();
		if ((simulator != null) && !simulator.isRunning())
		{
			JXmlFileChooser jxfc = new JXmlFileChooser(".","Save simulator parameters");
			int save = jxfc.showSaveDialog(this);
			if (save == JXmlFileChooser.APPROVE_OPTION)
			{
				if (this.overwriteFile(jxfc.getSelectedFile()))
				{
					XmlSimulatorParametersFile xspf = new XmlSimulatorParametersFile(jxfc.getSelectedFile().getAbsolutePath());

					int size = simulator.getSchema().getNProfiles();
					ArrayList<double[]> yextlist = new ArrayList<double[]>(size);
					for (int i = 0 ; i < size ; i++)
					{
						yextlist.add(simulator.getSchema().getYProfileExtrema(i));
					}

					try
					{
						xspf.save(simulator.getmaxTimeSteps(),simulator.getsavingDelay(),simulator.isDisplaySimulation(),simulator.getdisplayUpdatingTimeDelay(),yextlist);
					}
					catch (TransformerConfigurationException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					catch (ParserConfigurationException e)
					{
						e.printStackTrace();
					}
					catch (TransformerException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

/**
 * To know if one want to delete overwrite a file or not
 * @param File file
 * @return boolean
 */
	private final boolean overwriteFile (File file)
	{
		boolean succeed = true;
		if (file.exists())
		{
			Object[] options = {"Yes","No"};
			int ans = JOptionPane.showOptionDialog(this,"This file"+"\n"+file.getName()+"\n"+" already exists. Do you want to overwrite it ?","JPlot",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
			if (ans == JOptionPane.NO_OPTION)
			{
				succeed = false;
			}
		}
		return succeed;
	}
}