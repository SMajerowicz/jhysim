package org.jhysim.gui.menuitem;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.math.table.DataTable;
import org.jhysim.math.table.TableContext;
import org.jhysim.gui.JDataTableChooser;

/**
 * To save the simulation results
 * @author Sébastien Majerowicz
 */
public class JHySimSaveDataMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimSaveDataMenuItem (String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		if ((this.jhysim.getSimulator() != null) && !this.jhysim.getSimulator().isRunning())
		{
			DataTable datatable = null;
			if (this.jhysim.getSchema() != null)
			{
				datatable = this.jhysim.getSchema().getResults();
			}

			if (datatable != null)
			{
				JDataTableChooser jdtc = new JDataTableChooser(".","Save the data as");
				int save = jdtc.showSaveDialog(this);
				if (save == JDataTableChooser.APPROVE_OPTION)
				{
					if (this.overwriteFile(jdtc.getSelectedFile()))
					{
						TableContext tc = new TableContext(jdtc.getSelectedFile().getAbsolutePath());
						if (jdtc.isAsciiFileFilterSelected())
						{
							tc.setTableAsciiStrategy();
						}
						else if (jdtc.isFitsFileFilterSelected())
						{
							tc.setTableFitsStrategy();
						}

						try
						{
							tc.saveTable(datatable);
						}
						catch (IOException ex)
						{
							JOptionPane.showMessageDialog(this,"A problem occured during the file saving !",JHySim.NAME,JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,"No data are available !",JHySim.NAME,JOptionPane.ERROR_MESSAGE);
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
			int ans = JOptionPane.showOptionDialog(this,"This file"+"\n"+file.getName()+"\n"+" already exists. Do you want to overwrite it ?",
												   JHySim.NAME,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
			if (ans == JOptionPane.NO_OPTION)
			{
				succeed = false;
			}
		}
		return succeed;
	}
}