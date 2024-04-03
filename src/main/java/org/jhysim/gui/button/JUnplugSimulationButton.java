package org.jhysim.gui.button;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jhysim.gui.JUnplugSimulationFrame;

import org.jhysim.io.XMLPluggedSimulationFile;

import org.jhysim.pattern.CommandPattern;

import org.xml.sax.SAXException;

/**
 * To unplug some simulations
 * @author Sébastien Majerowicz
 */
public class JUnplugSimulationButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = 1864038747012751086L;
	private JUnplugSimulationFrame parent = null;
/**
 * Constructor
 * @param parent JUnplugSimulationFrame parent
 * @param label String
 * @param icon ImageIcon
 * @param mnemonic int 
 */
	public JUnplugSimulationButton (JUnplugSimulationFrame parent, String label, ImageIcon icon, int mnemonic)
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
		String[] simulationClasses = this.parent.getSelectedSimulationClassNames();
		if (simulationClasses.length != 0)
		{
			XMLPluggedSimulationFile xpsf = new XMLPluggedSimulationFile(this.parent.getXmlFilename());
			try
			{
				xpsf.removePluggedSimulations(simulationClasses);
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
			this.parent.kill();
		}
		else
		{
			this.parent.kill();
		}
	}
}