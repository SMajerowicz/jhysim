package org.jhysim.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.TitledBorder;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulation;

import org.jhysim.gui.button.JChangeSimulationRadioButton;
import org.jhysim.gui.button.JDefineSimulationParametersButton;
import org.jhysim.gui.forms.JDefineSimulationParametersPanel;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * Frame usefull to choose the simulation
 * @author Sébastien Majerowicz
 */
public class JDefineSimulationParametersFrame extends JFrame implements ActionListener, Killable, WindowListener
{
	private static final long serialVersionUID = 1404024819540639649L;

	private static boolean isInstanced = false;

	private Simulation simulation = null;

	private String[] simulationNames = null;

	private JChangeSimulationRadioButton[] simulationRadioButtons = null;
	private DefaultComboBoxModel[] numSchemaModels = null;
	private JComboBox numSchemaComboBox = null;
	private JPanel defineSimulationPanel = null;
	private JDefineSimulationParametersPanel[] parametersPanels = null; 

	private final static int SIMULATIONS_PER_ROW = 3;

	private JDefineSimulationParametersButton defineButton = null;
	private JCancelFrameButton cancelButton = null;

/**
 * To retrieve the unique instance of this frame
 * @param simulation Simulation
 * @param simulationNames String[] all the available simulations
 * @return JDefineSimulationParametersFrame or null
 */
	public final static JDefineSimulationParametersFrame getInstance (Simulation simulation, String[] simulationNames)
	{
		JDefineSimulationParametersFrame frame = null;

		if (!JDefineSimulationParametersFrame.isInstanced)
		{
			frame = new JDefineSimulationParametersFrame(simulation,simulationNames);
		}

		return frame;
	}

/**
 * Constructor
 * @param simulation Simulation
 * @param simulationNames String[] all the available simulations
 */
	private JDefineSimulationParametersFrame (Simulation simulation, String[] simulationNames)
	{
		super();

		this.simulation = simulation;
		this.simulationNames = simulationNames;

		this.simulationRadioButtons = new JChangeSimulationRadioButton[this.simulationNames.length];
		this.numSchemaModels = new DefaultComboBoxModel[this.simulationNames.length];
		this.parametersPanels = new JDefineSimulationParametersPanel[this.simulationNames.length];

		ButtonGroup simulationBg = new ButtonGroup();

		NumericalSchema[] schemas = this.simulation.retrieveAvailableSimulationClasses(this.simulationNames);
		int sizeprof = 0, sizeparam = 0;
		String[] profnames = null, paramnames = null, paramdesc = null;
		ArrayList yexts = null;
		for (int i = 0 ; i < this.simulationNames.length ; i++)
		{
			sizeprof = schemas[i].getNProfiles();
			profnames = new String[sizeprof];
			yexts = new ArrayList(sizeprof);
			for (int j = 0 ; j < sizeprof ; j++)
			{
				profnames[j] = schemas[i].getProfileName(j);
				yexts.add(schemas[i].getYProfileExtrema(j));
			}

			sizeparam = schemas[i].getNParameters();
			paramnames = new String[sizeparam];
			paramdesc = new String[sizeparam];
			for (int j = 0 ; j < sizeparam ; j++)
			{
				paramnames[j] = schemas[i].getParameterName(j);
				paramdesc[j] = schemas[i].getParameterDescription(j);
			}

			this.simulationRadioButtons[i] = new JChangeSimulationRadioButton(this,schemas[i].getName());
			this.simulationRadioButtons[i].addActionListener(this);
			this.numSchemaModels[i] = new DefaultComboBoxModel(schemas[i].getNumericalSchemaDescriptions());
			this.parametersPanels[i] = new JDefineSimulationParametersPanel(this,profnames,yexts,paramnames,paramdesc);

			simulationBg.add(this.simulationRadioButtons[i]);
		}

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel simulationPanel = new JPanel(layout);

		int iy = 0;
		for (int i = 0 ; i < this.simulationRadioButtons.length ; i++)
		{
			constraints.gridx = i - iy*JDefineSimulationParametersFrame.SIMULATIONS_PER_ROW;
			constraints.gridy = iy;
			layout.setConstraints(this.simulationRadioButtons[i],constraints);
			simulationPanel.add(this.simulationRadioButtons[i]);
			if ((i != 0) && ((i+1)%JDefineSimulationParametersFrame.SIMULATIONS_PER_ROW == 0)) iy++;
		}

		simulationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1),"Simulations",TitledBorder.RIGHT,TitledBorder.TOP));

		this.numSchemaComboBox = new JComboBox(this.numSchemaModels[0]);

		JPanel numPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		numPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1),"Numerical schema",TitledBorder.RIGHT,TitledBorder.TOP));
		numPanel.add(new JLabel("Method : ",JLabel.LEFT));
		numPanel.add(this.numSchemaComboBox);

		this.defineSimulationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.defineSimulationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1),"Selected simulation",TitledBorder.RIGHT,TitledBorder.TOP));

		if (this.simulation.getSchema() == null)
		{
			this.simulationRadioButtons[0].setSelected(true);
			this.defineSimulationPanel.add(this.parametersPanels[0]);
		}
		else
		{
			int index = 0;
			while ((index < this.simulationNames.length) && !this.simulationRadioButtons[index].getText().equals(this.simulation.getSchema().getName())) index++;
			this.simulationRadioButtons[index].setSelected(true);
			this.numSchemaComboBox.setSelectedIndex(this.simulation.getSchema().getchoosenSchema());
			this.defineSimulationPanel.add(this.parametersPanels[index]);
			int size = this.parametersPanels[index].getNProfiles();
			double[] yext = null;
			for (int i = 0 ; i < size ; i++)
			{
				yext = this.simulation.getSchema().getYProfileExtrema(i);
				this.parametersPanels[index].setYExtrema(i,yext[0],yext[1]);
				this.parametersPanels[index].setProfile(i,this.simulation.getSchema().getProfile(i,0));
			}
			size = this.parametersPanels[index].getNDoubles();
			for (int i = 0 ; i < size ; i++)
			{
				this.parametersPanels[index].setDoubleText(i,String.valueOf(this.simulation.getSchema().getParameter(i)));
			}
		}

		this.defineButton = new JDefineSimulationParametersButton(this,"Define",null,KeyEvent.VK_D);
		this.defineButton.addActionListener(this);
		this.cancelButton = new JCancelFrameButton(this,"Cancel",null,KeyEvent.VK_C);
		this.cancelButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(this.defineButton);
		buttonPanel.add(this.cancelButton);

		JPanel upPanel = new JPanel(new BorderLayout());
		upPanel.add(simulationPanel,BorderLayout.NORTH);
		upPanel.add(numPanel,BorderLayout.CENTER);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(upPanel,BorderLayout.NORTH);
		this.getContentPane().add(this.defineSimulationPanel,BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle("Define simulation parameters");
		this.pack();
		this.setResizable(true);

		JDefineSimulationParametersFrame.isInstanced = true;
	}

/**
 * To retrieve the index of the selected radio button
 * @return int
 */
	private final int getSelectedRadioButtonIndex ()
	{
		int index = 0;

		while ((index < this.simulationRadioButtons.length) && !this.simulationRadioButtons[index].isSelected()) index++;

		return index;
	}

/**
 * To retrieve the index of the selected numerical schema
 * @return int
 */
	public final int getSelectedNumericalSchema ()
	{
		return this.numSchemaComboBox.getSelectedIndex();
	}

/**
 * To retrieve the class name of the selected simulation name
 * @return String
 */
	public final String getSelectedSimulationName ()
	{
		int index = this.getSelectedRadioButtonIndex();
		return this.simulationNames[index];
	}

/**
 * To change the selected simulation panel
 */
	public final void changeSelectedSimulationPanel ()
	{
		this.defineSimulationPanel.removeAll();

//search for the selected radio button
		int index = this.getSelectedRadioButtonIndex();

		this.numSchemaComboBox.setModel(this.numSchemaModels[index]);

		this.defineSimulationPanel.add(this.parametersPanels[index],BorderLayout.CENTER);

		this.defineSimulationPanel.validate();
		this.defineSimulationPanel.repaint();

		this.pack();
	}

/**
 * To retrieve all the profiles
 * @param dx double spatial resolution
 * @return ArrayList list of double array
 * @throws NumberFormatException the spatial resolution must be between 0.0 and 1.0 strictly
 */
	public final ArrayList getProfiles (double dx) throws NumberFormatException
	{
		int index = this.getSelectedRadioButtonIndex();
		return this.parametersPanels[index].getProfiles(dx);
	}

/**
 * To retrieve the doubles
 * @return String[]
 */
	public final String[] getDoubles ()
	{
		int index = this.getSelectedRadioButtonIndex();
		return this.parametersPanels[index].getDoubles();
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
		JDefineSimulationParametersFrame.isInstanced = false;
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
}