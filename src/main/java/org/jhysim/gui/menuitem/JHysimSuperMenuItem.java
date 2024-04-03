package org.jhysim.gui.menuitem;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import org.jhysim.JHySim;

/**
 * @author Sébastien Majerowicz
 * Parent of all menu items
 */
public class JHySimSuperMenuItem extends JMenuItem
{
	private static final long serialVersionUID = 3034737784191498668L;
	protected JHySim jhysim = null;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimSuperMenuItem(String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,mnemonic);
		this.setIcon(icon);

		this.jhysim = jhysim;
	}
}