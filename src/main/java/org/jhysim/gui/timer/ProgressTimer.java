package org.jhysim.gui.timer;

import javax.swing.Timer;

import org.jhysim.JHySim;

import org.jhysim.pattern.CommandPattern;

/**
 * To manage the job
 * @author Sébastien Majerowicz
 */
public class ProgressTimer extends Timer implements CommandPattern
{
	private static final long serialVersionUID = 7082013451139866532L;

/**
 * Constructor
 * @param delay int time delay (ms)
 * @param jhysim JHySim listener
 */
	public ProgressTimer (int delay, JHySim jhysim)
	{
		super (delay,jhysim);
	}

/**
 * @see org.application.pattern.CommandPattern#execute()
 */
	public void execute()
	{
		((JHySim)this.getActionListeners()[0]).updateProgressBar();
	}
}