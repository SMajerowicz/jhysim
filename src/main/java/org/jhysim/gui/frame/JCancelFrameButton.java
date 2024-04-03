package org.jhysim.gui.frame;

import org.jhysim.pattern.CommandPattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * JButton to kill a frame which implements the Killable interface
 * @author Sébastien Majerowicz
 */
public class JCancelFrameButton extends JButton implements CommandPattern
{
	private static final long serialVersionUID = -1796929291581919579L;
	private Killable kf = null;

/**
 * Constructor
 * @param kf Killable parent frame
 * @param label String button label
 * @param icon ImageIcon button icon
 * @param mnemonic int mnemonic
 */
	public JCancelFrameButton (Killable kf, String label, ImageIcon icon, int mnemonic)
	{
		super(label,icon);

		this.setMnemonic(mnemonic);
		this.kf = kf;

		this.setToolTipText("Close this frame");
	}

/**
 * To execute an action
 */
	public void execute ()
	{
		kf.kill();
	}
}