package org.jhysim.gui.menuitem;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JXmlFileChooser;

import org.jhysim.simulation.io.XmlSimulationParametersFile;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * To save the parameters of the current physical simulation
 * @author Sébastien Majerowicz
 */
public class JHySimSaveSimulationParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -69419437298379732L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimSaveSimulationParametersMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		NumericalSchema schema = this.jhysim.getSchema();
		if (schema != null) 
		{
			JXmlFileChooser jxfc = new JXmlFileChooser(".","Save simulation parameters");
			int save = jxfc.showSaveDialog(this);
			if (save == JXmlFileChooser.APPROVE_OPTION)
			{
				if (this.overwriteFile(jxfc.getSelectedFile()))
				{
					XmlSimulationParametersFile xspf = new XmlSimulationParametersFile(schema.getClass().getName(),jxfc.getSelectedFile().getAbsolutePath());

					int size = schema.getNProfiles();
					ArrayList<double[]> profs = new ArrayList<double[]>(size);
					for (int i = 0 ; i < size ; i++)
					{
						profs.add(schema.getProfile(i,0));
					}

					size = schema.getNParameters();
					ArrayList<Double> params = new ArrayList<Double>(size);
					for (int i = 0 ; i < size ; i++)
					{
						params.add(Double.valueOf(schema.getParameter(i)));
					}

					try
					{
						xspf.save(schema.getchoosenSchema(),profs,params);
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