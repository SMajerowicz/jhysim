package org.jhysim.gui.menuitem;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JFileChooser;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jhysim.gui.filechooser.GenericFileFilter;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.io.XmlPluggedSimulationFile;

import org.xml.sax.SAXException;

/**
 * To plug a new simulation
 * @author Sébastien Majerowicz
 */
public class JHySimPlugNewSimulationMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -2368914116093131151L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimPlugNewSimulationMenuItem(String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		JFileChooser jfc = new JFileChooser(".");
		jfc.setDialogTitle("Choose the simulation file");
		jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.addChoosableFileFilter(new GenericFileFilter(".class","Simulation class file"));

		int open = jfc.showOpenDialog(this);
		if (open == JFileChooser.APPROVE_OPTION)
		{
//BE CAREFULL, the new simulation must be placed in the software subdirectory
			String str = jfc.getSelectedFile().getAbsolutePath();
			int epos = str.lastIndexOf('.');
			XmlPluggedSimulationFile xpsf = new XmlPluggedSimulationFile(JHySim.PLUGGED_SIMULATIONS_FILENAME);
			String bstr = (new File(".")).getAbsolutePath();
			try
			{
				xpsf.plugNewSimulation(str.substring(bstr.length()-1,epos).replace(File.separatorChar,'.'));
			}
			catch (TransformerConfigurationException e)
			{
				e.printStackTrace();
			}
			catch (SAXException e)
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