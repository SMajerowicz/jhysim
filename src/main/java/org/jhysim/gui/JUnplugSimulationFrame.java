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
import javax.swing.JPanel;

import org.jhysim.gui.button.JUnplugSimulationButton;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulation;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * To unplug a simulation
 * @author Sébastien Majerowicz
 */
public class JUnplugSimulationFrame extends JFrame implements ActionListener, Killable, WindowListener
{
	private static final long serialVersionUID = -6864011411004692840L;

	private static boolean isInstanced = false;

	private final static int SIMULATIONS_PER_ROW = 3;

	private Simulation simulation = null;
	private String xmlfilename = null;

	private String[] simulationClasses = null;

	private JCheckBox[] simulationCheckBoxes = null;

	private JUnplugSimulationButton unplugButton = null;
	private JCancelFrameButton cancelButton = null;

/**
 * To retrieve the unique instance of this frame
 * @param simulation Simulation
 * @param xmlfilename String xml file name 
 * @return JUnplugSimulationFrame or null
 */
	public final static JUnplugSimulationFrame getInstance (Simulation simulation, String xmlfilename)
	{
		JUnplugSimulationFrame frame = null;

		if (!JUnplugSimulationFrame.isInstanced)
		{
			frame = new JUnplugSimulationFrame(simulation,xmlfilename);
		}

		return frame;
	}

/**
 * Constructor
 * @param simulation Simulation
 * @param xmlfilename String xml file name 
 */
	private JUnplugSimulationFrame (Simulation simulation, String xmlfilename)
	{
		super();

		this.simulation = simulation;
		this.xmlfilename = xmlfilename;

		this.simulationClasses = this.simulation.retrieveAvailableSimulations(this.xmlfilename);

		NumericalSchema[] schemas = this.simulation.retrieveAvailableSimulationClasses(this.simulationClasses);

		this.simulationCheckBoxes = new JCheckBox[this.simulationClasses.length];

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel simulationPanel = new JPanel(layout);

		int iy = 0;
		for (int i = 0 ; i < this.simulationClasses.length ; i++)
		{
			this.simulationCheckBoxes[i] = new JCheckBox(schemas[i].getName(),false);
			constraints.gridx = i - iy*JUnplugSimulationFrame.SIMULATIONS_PER_ROW;
			constraints.gridy = iy;
			layout.setConstraints(this.simulationCheckBoxes[i],constraints);
			simulationPanel.add(this.simulationCheckBoxes[i]);
			if ((i != 0) && ((i+1)%JUnplugSimulationFrame.SIMULATIONS_PER_ROW == 0)) iy++;
		}

		this.unplugButton = new JUnplugSimulationButton(this,"Unplug",null,KeyEvent.VK_U);
		this.unplugButton.addActionListener(this);
		this.cancelButton = new JCancelFrameButton(this,"Cancel",null,KeyEvent.VK_C);
		this.cancelButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(this.unplugButton);
		buttonPanel.add(this.cancelButton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(simulationPanel,BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle("Unplug simulations");
		this.pack();
		this.setResizable(false);

		JUnplugSimulationFrame.isInstanced = true;
	}

/**
 * To retrieve the selected simulation class names
 */
	public final String[] getSelectedSimulationClassNames ()
	{
		String[] results = null;

//first the array size must be determined
		int size = 0;
		for (int i = 0 ; i < this.simulationClasses.length ; i++)
		{
			if (this.simulationCheckBoxes[i].isSelected())
			{
				size++;
			}
		}

		results = new String[size];

//then, the array is filled
		size = 0;
		for (int i = 0 ; i < this.simulationClasses.length ; i++)
		{
			if (this.simulationCheckBoxes[i].isSelected())
			{
				results[size] = this.simulationClasses[i]; 
			}
		}

		return results;
	}

/**
 * To retrieve the xml file name
 * @return String
 */
	public final String getXmlFilename()
	{
		return this.xmlfilename;
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
		JUnplugSimulationFrame.isInstanced = false;
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