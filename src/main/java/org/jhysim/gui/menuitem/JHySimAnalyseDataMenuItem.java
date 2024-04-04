package org.jhysim.gui.menuitem;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JAnalyseProfilesFrame;

/**
 * To visualize (again) the date after a simulation
 * @author Sébastien Majerowicz
 */
public class JHySimAnalyseDataMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = -7232849834039770987L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimAnalyseDataMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text, icon, mnemonic, jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		if ((this.jhysim.getSimulator() != null) && !this.jhysim.getSimulator().isRunning())
		{
			if ((this.jhysim.getSchema() != null) && (this.jhysim.getSchema().getResults() != null))
			{
				JAnalyseProfilesFrame japf = new JAnalyseProfilesFrame(this.jhysim.getSchema());
				if (japf != null)
				{
					japf.setVisible(true);
					japf.initNFrames();
					japf.showSelectProfileSlider();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,"No data available !",JHySim.NAME,JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}