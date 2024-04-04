package org.jhysim.gui.menuitem;

import javax.swing.Icon;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

/**
 * @author Sébastien Majerowicz
 * To exit from the software
 */
public class JHySimQuitMenuItem extends JHySimSuperMenuItem implements CommandPattern
{
	private static final long serialVersionUID = 2487645488405179965L;

/**
 * Constructor
 * @param text String
 * @param icon Icon
 * @param mnemonic int
 * @param jhysim JHySim
 */
	public JHySimQuitMenuItem(String text, Icon icon, int mnemonic, JHySim jhysim)
	{
		super(text,icon,mnemonic,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		this.jhysim.kill();
	}
}