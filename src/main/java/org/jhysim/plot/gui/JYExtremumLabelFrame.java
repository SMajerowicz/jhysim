package org.jhysim.plot.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.plot.graphic.JDefineProfilePanel;

import org.jhysim.plot.graphic.item.YExtremumLabel;

import org.jhysim.plot.gui.button.JYExtremumLabelChangeButton;

/**
 * To edit and view y extremum value
 * @author Sébastien Majerowicz
 */
public class JYExtremumLabelFrame extends JFrame implements ActionListener, Killable, WindowListener
{
	private static final long serialVersionUID = -2604345989984489739L;

	private JDefineProfilePanel profilePanel = null;

	private boolean isMax = false;
	private YExtremumLabel yExtLabel = null;

	private JTextField valueTextField = null;

	private JYExtremumLabelChangeButton changeButton = null;
	private JCancelFrameButton cancelButton = null;

/**
 * Constructor
 * @param profilePanel JDefineProfilePanel
 * @param yExtLabel YExtremumLabel
 * @param isMax boolean is the maximum (true) or not (false)
 */
	public JYExtremumLabelFrame (JDefineProfilePanel profilePanel, YExtremumLabel yExtLabel, boolean isMax)
	{
		super ();

		this.profilePanel = profilePanel;

		this.yExtLabel = yExtLabel;
		this.isMax = isMax;

		this.valueTextField = new JTextField(this.yExtLabel.getLabel(),10);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel textPanel = new JPanel(layout);

		JLabel label = new JLabel("Value",JLabel.LEFT);
		constraints.gridx = 0;
		constraints.gridy = 0;
		layout.setConstraints(label,constraints);
		textPanel.add(label);
		constraints.gridx = 1;
		constraints.gridy = 0;
		layout.setConstraints(this.valueTextField,constraints);
		textPanel.add(this.valueTextField);

		this.changeButton = new JYExtremumLabelChangeButton(this,"Change",null,KeyEvent.VK_H);
		this.changeButton.addActionListener(this);
		this.cancelButton = new JCancelFrameButton(this,"Cancel",null,KeyEvent.VK_C);
		this.cancelButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(this.changeButton);
		buttonPanel.add(this.cancelButton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(textPanel,BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if (!this.isMax)
		{
			this.setTitle("Y axis minimum");
		}
		else
		{
			this.setTitle("Y axis maximum");
		}
		this.pack();
		this.setResizable(false);
	}

/**
 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
 */
	public void windowOpened(WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
 */
	public void windowClosing(WindowEvent e)
	{
		this.kill();
	}

/**
 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
 */
	public void windowClosed(WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
 */
	public void windowIconified(WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
 */
	public void windowDeiconified(WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
 */
	public void windowActivated(WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
 */
	public void windowDeactivated(WindowEvent e)
	{}

/**
 * @see org.application.gui.frame.Killable#kill()
 */
	public void kill()
	{
		this.dispose();
	}

/**
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed(ActionEvent e)
	{
		CommandPattern command = (CommandPattern)e.getSource();
		command.execute();
	}

/**
 * To retrieve the value
 * @return String
 */
	public final String getValue ()
	{
		return this.valueTextField.getText();
	}

/**
 * To change the value of the extremum
 * @param y double value
 */
	public final void setYExtremumValue (double y)
	{
		if (!this.isMax)
		{
			this.profilePanel.setYmin(y);
		}
		else
		{
			this.profilePanel.setYmax(y);
		}
		this.profilePanel.repaint();
	}
}