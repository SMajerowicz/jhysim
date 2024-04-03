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

import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.CommandPattern;
import org.jhysim.pattern.ObserverPattern;
import org.jhysim.plot.gui.button.JDataPointChangeButton;
import org.jhysim.plot.graphic.JDefineProfilePanel;

import org.jhysim.plot.graphic.item.DataPoint;

/**
 * To edit and view data point properties
 * @author Sébastien Majerowicz
 */
public class JDataPointFrame extends JFrame implements ActionListener, Killable, ObserverPattern, WindowListener
{
	private static final long serialVersionUID = -5528946182761874236L;

	private JDefineProfilePanel profilePanel = null;

	private DataPoint dataPoint = null;

	private DecimalFormat dxFormat = null;
	private JTextField xTextField = null;
	private DecimalFormat dyFormat = null;
	private JTextField yTextField = null;

	private JDataPointChangeButton changeButton = null;
	private JCancelFrameButton cancelButton = null;

/**
 * Constructor
 * @param profilePanel JDefineProfilePanel
 * @param dataPoint DataPoint
 * @param enableX boolean
 * @param enableY boolean
 */
	public JDataPointFrame (JDefineProfilePanel profilePanel, DataPoint dataPoint, boolean enableX, boolean enableY)
	{
		super ();

		this.profilePanel = profilePanel;

		this.dataPoint = dataPoint;

		this.dxFormat = new DecimalFormat("0.00#");

		double wy = this.dataPoint.getWy();
		if (Math.abs(wy) > 10.0)
		{
			this.dyFormat = new DecimalFormat("#");
		}
		else if (Math.abs(wy) < 0.1)
		{
			StringBuffer pattern = new StringBuffer("0.0");
			int index = 1;
			while (wy < 0.1/Math.pow(10.0,(double)index))
			{
				pattern.append('0');
				index++;
			}
			pattern.append('#');
			this.dyFormat = new DecimalFormat(pattern.toString());
		}
		else
		{
			this.dyFormat = new DecimalFormat("0.0#");
		}

		this.xTextField = new JTextField(String.valueOf(this.dataPoint.getWx()),10);
		this.xTextField.setEnabled(enableX);
		this.yTextField = new JTextField(String.valueOf(this.dataPoint.getWy()),10);
		this.yTextField.setEnabled(enableY);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel textPanel = new JPanel(layout);

		JLabel xLabel = new JLabel("X",JLabel.LEFT);
		constraints.gridx = 0;
		constraints.gridy = 0;
		layout.setConstraints(xLabel,constraints);
		textPanel.add(xLabel);
		constraints.gridx = 1;
		constraints.gridy = 0;
		layout.setConstraints(this.xTextField,constraints);
		textPanel.add(this.xTextField);
		JLabel yLabel = new JLabel("Y",JLabel.LEFT);
		constraints.gridx = 0;
		constraints.gridy = 1;
		layout.setConstraints(yLabel,constraints);
		textPanel.add(yLabel);
		constraints.gridx = 1;
		constraints.gridy = 1;
		layout.setConstraints(this.yTextField,constraints);
		textPanel.add(this.yTextField);

		this.changeButton = new JDataPointChangeButton(this,"Change",null,KeyEvent.VK_H);
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

		this.setTitle("Data properties");
		this.pack();
		this.setResizable(false);
	}

/**
 * @see org.application.pattern.ObserverPattern#updateObserver()
 */
	public void updateObserver()
	{
		this.xTextField.setText(this.dxFormat.format(this.dataPoint.getWx()).replace(',','.'));
		this.yTextField.setText(this.dyFormat.format(this.dataPoint.getWy()).replace(',','.'));
	}

/**
 * @see org.application.pattern.ObserverPattern#killObserver()
 */
	public void killObserver()
	{
		this.kill();
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
		this.dataPoint.removeObserver(this);
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
 * To initialize the data point
 * @param dataPoint DataPoint
 */
	public final void setDataPoint (DataPoint dataPoint)
	{
		this.dataPoint = dataPoint;
	}

/**
 * To retrieve the x-ccordinate
 * @return String
 */
	public final String getXCoordinate ()
	{
		return this.xTextField.getText();
	}

/**
 * To retrieve the y-ccordinate
 * @return String
 */
	public final String getYCoordinate ()
	{
		return this.yTextField.getText();
	}

/**
 * To change the value of the data point and update the profile panel in the same time
 * @param x double x-coordinate
 * @param y double y-coordinate 
 */
	public final void setDataPointCoordinates (double x, double y)
	{
		this.dataPoint.setWx(x);
		this.dataPoint.setWy(y);
		int[] sc = this.profilePanel.getScreenCoordinates(x,y);
		this.dataPoint.setLocation(sc[0],sc[1]);
		this.profilePanel.sortList();
		this.profilePanel.repaint();
	}
}