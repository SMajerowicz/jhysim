package org.jhysim.gui;

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

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jhysim.gui.button.JDefineSimulatorParametersButton;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulation;

/**
 * To define the simulator parameters
 * @author Sébastien Majerowicz
 */
public class JDefineSimulatorParametersFrame extends JFrame implements ActionListener, Killable, WindowListener
{
	private static final long serialVersionUID = -5975193440165000995L;

	private static boolean isInstanced = false;

	private Simulation simulation = null;

	private JTextField nTimeStepsTextField = null;
	private JTextField savingDelayTextField = null;
	private JCheckBox displayCheckBox = null;
	private JTextField displayTimeTextField = null;

	private JTextField[] variableYMinTextField = null;
	private JTextField[] variableYMaxTextField = null; 

	private JDefineSimulatorParametersButton defineButton = null;
	private JCancelFrameButton cancelButton = null;

/**
 * To retrieve the unique instance of this object
 * @param simulation Simulation
 * @return JDefineSimulatorParametersFrame or null;
 */
	public final static JDefineSimulatorParametersFrame getInstance (Simulation simulation)
	{
		JDefineSimulatorParametersFrame frame = null;

		if (!JDefineSimulatorParametersFrame.isInstanced)
		{
			frame = new JDefineSimulatorParametersFrame(simulation);
		}

		return frame;
	}

/**
 * Constructor
 * @param simulation Simulation
 */
	private JDefineSimulatorParametersFrame (Simulation simulation)
	{
		super();

		this.simulation = simulation;

		this.nTimeStepsTextField = new JTextField(5);
		this.nTimeStepsTextField.setHorizontalAlignment(JTextField.RIGHT);
		this.savingDelayTextField = new JTextField(5);
		this.savingDelayTextField.setHorizontalAlignment(JTextField.RIGHT);
		this.displayCheckBox = new JCheckBox("Display Simulation");
		this.displayTimeTextField = new JTextField(5);
		this.displayTimeTextField.setHorizontalAlignment(JTextField.RIGHT);

		if (this.simulation.getSimulator() != null)
		{
			this.nTimeStepsTextField.setText(String.valueOf(this.simulation.getSimulator().getmaxTimeSteps()));
			this.savingDelayTextField.setText(String.valueOf(this.simulation.getSimulator().getsavingDelay()));
			this.displayCheckBox.setSelected(this.simulation.getSimulator().isDisplaySimulation());
			this.displayTimeTextField.setText(String.valueOf(this.simulation.getSimulator().getdisplayUpdatingTimeDelay()));
		}

		JLabel ntsLabel = new JLabel("Number of time steps",JLabel.LEFT);
		JLabel sdLabel = new JLabel("Saving delay",JLabel.LEFT);
		JLabel dtLabel = new JLabel("Display time delay (ms)",JLabel.LEFT);

		int nVariables = this.simulation.getSchema().getNProfiles();
		JLabel[] variableLabels = new JLabel[nVariables];
		this.variableYMinTextField = new JTextField[nVariables];
		this.variableYMaxTextField = new JTextField[nVariables];

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel dataPanel = new JPanel(layout);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		layout.setConstraints(ntsLabel,constraints);
		dataPanel.add(ntsLabel);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		layout.setConstraints(this.nTimeStepsTextField,constraints);
		dataPanel.add(this.nTimeStepsTextField);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		layout.setConstraints(sdLabel,constraints);
		dataPanel.add(sdLabel);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		layout.setConstraints(this.savingDelayTextField,constraints);
		dataPanel.add(this.savingDelayTextField);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		layout.setConstraints(this.displayCheckBox,constraints);
		dataPanel.add(this.displayCheckBox);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		layout.setConstraints(dtLabel,constraints);
		dataPanel.add(dtLabel);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		layout.setConstraints(this.displayTimeTextField,constraints);
		dataPanel.add(this.displayTimeTextField);

		JLabel yminLabel = new JLabel("Y min",JLabel.CENTER);
		JLabel ymaxLabel = new JLabel("Y max",JLabel.CENTER);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		layout.setConstraints(yminLabel,constraints);
		dataPanel.add(yminLabel);
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		layout.setConstraints(ymaxLabel,constraints);
		dataPanel.add(ymaxLabel);

		double[] yexts = null;

		for (int i = 0 ; i < nVariables ; i++)
		{
			variableLabels[i] = new JLabel(this.simulation.getSchema().getProfileName(i),JLabel.LEFT);
			constraints.gridx = 0;
			constraints.gridy = 5+i;
			constraints.gridwidth = 1;
			layout.setConstraints(variableLabels[i],constraints);
			dataPanel.add(variableLabels[i]);

			yexts = this.simulation.getSchema().getYProfileExtrema(i);
			this.variableYMinTextField[i] = new JTextField(String.valueOf(yexts[0]),8);
			constraints.gridx = 1;
			constraints.gridy = 5+i;
			constraints.gridwidth = 1;
			layout.setConstraints(this.variableYMinTextField[i],constraints);
			dataPanel.add(this.variableYMinTextField[i]);
			this.variableYMaxTextField[i] = new JTextField(String.valueOf(yexts[1]),8);
			constraints.gridx = 2;
			constraints.gridy = 5+i;
			constraints.gridwidth = 1;
			layout.setConstraints(this.variableYMaxTextField[i],constraints);
			dataPanel.add(this.variableYMaxTextField[i]);
		}

		this.defineButton = new JDefineSimulatorParametersButton(this,"Define",null,KeyEvent.VK_D);
		this.defineButton.addActionListener(this);
		this.cancelButton = new JCancelFrameButton(this,"Cancel",null,KeyEvent.VK_C);
		this.cancelButton.addActionListener(this);

		JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonpanel.add(this.defineButton);
		buttonpanel.add(this.cancelButton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(dataPanel,BorderLayout.NORTH);
		this.getContentPane().add(buttonpanel,BorderLayout.SOUTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle("Simulator parameters");
		this.pack();
		this.setResizable(false);

		JDefineSimulatorParametersFrame.isInstanced = true;
	}

/**
 * To retrieve the number of time steps
 * @return String
 */
	public final String getnTimeSteps ()
	{
		return this.nTimeStepsTextField.getText();
	}
/**
 * To retrieve the saving delay
 * @return String
 */
	public final String getsavingDelay ()
	{
		return this.savingDelayTextField.getText();
	}
/**
 * To know if the simulation must be displayed or not
 * @return boolean
 */
	public final boolean isDisplaySimulation ()
	{
		return this.displayCheckBox.isSelected();
	}
/**
 * To retrieve the display updating time delay
 * @return String
 */
	public final String getdisplayUpdatingTimeDelay ()
	{
		return this.displayTimeTextField.getText();
	}
/**
 * To retrieve the simulation
 * @return Simulation
 */
	public final Simulation getSimulation ()
	{
		return this.simulation;
	}

/**
 * To retrieve the ymin value of the i-th profile
 * @param index int
 * @return String
 * @throws IndexOutOfBoundsException
 */
	public final String getYMin (int index) throws IndexOutOfBoundsException
	{
		return this.variableYMinTextField[index].getText();
	}

/**
 * To retrieve the ymax value of the i-th profile
 * @param index int
 * @return String
 * @throws IndexOutOfBoundsException
 */
	public final String getYMax (int index) throws IndexOutOfBoundsException
	{
		return this.variableYMaxTextField[index].getText();
	}

/**
 * @see org.application.gui.frame.Killable#kill()
 */
	public void kill()
	{
		JDefineSimulatorParametersFrame.isInstanced = false;
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
}