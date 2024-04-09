package org.jhysim.gui.menuitem;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.xml.parsers.ParserConfigurationException;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JXmlFileChooser;

import org.jhysim.simulation.io.XmlSimulationParametersFile;

import org.jhysim.simulation.methods.NumericalSchema;

import org.xml.sax.SAXException;

/**
 * To save the parameters of the current physical simulation
 * @author Sébastien Majerowicz
 */
public class JHySimOpenSimulationParametersMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -8254573091905155767L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimOpenSimulationParametersMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		JXmlFileChooser jxfc = new JXmlFileChooser(".","Open simulation parameters");
		int open = jxfc.showOpenDialog(this);
		if (open == JXmlFileChooser.APPROVE_OPTION)
		{
			XmlSimulationParametersFile xspf = new XmlSimulationParametersFile(jxfc.getSelectedFile().getAbsolutePath());
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

			NumericalSchema schema = null;
			if (succeed)
			{
				succeed = false;
				try
				{
					Class<?> newclass = Class.forName(xspf.getSimulationName());
					schema = (NumericalSchema)newclass.getDeclaredConstructor().newInstance();
					succeed = true;
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (SecurityException e)
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
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}

			if (succeed)
			{
				schema.initVariables(xspf.getProfiles(),xspf.getParameters());
				schema.setchoosenSchema(xspf.getNumSchema());

				ArrayList<double[]> yexts = xspf.getYExtrema();
				int size = yexts.size();
				for (int i = 0 ; i < size ; i++)
				{
					schema.setYProfileExtrema(i,(double[])yexts.get(i));
				}

				this.jhysim.setSchema(schema);
			}
		}
	}
}