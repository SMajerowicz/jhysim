package org.jhysim.gui.slider;

import javax.swing.JSlider;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.gui.JVisualizeProfilesFrame;

/**
 * To select the index of the profiles one wants to show with a horizontal slider
 * @author Sébastien Majerowicz  
 */
public class JShowSelectedProfilesSlider extends JSlider implements CommandPattern 
{
	private static final long serialVersionUID = -7495085322165546170L;
	private JVisualizeProfilesFrame parent = null;

/**
 * Constructor
 * @param parent JVisualizeProfilesFrame parent frame
 * @param min minimum value
 * @param max maximum value
 * @param init initial value (between min and max)
 */
	public JShowSelectedProfilesSlider (JVisualizeProfilesFrame parent, int min, int max, int init)
	{
		super(JSlider.HORIZONTAL,min,max,init);

		this.parent = parent;
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public final void execute()
	{
		this.parent.reloadFrame(this.getValue());
	}
}