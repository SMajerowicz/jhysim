package org.jhysim;

import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import javax.xml.parsers.ParserConfigurationException;

import org.jhysim.gui.frame.Killable;

import org.jhysim.gui.menuitem.JHySimDefineSimulationParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimDefineSimulatorParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimOpenSimulationParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimOpenSimulatorParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimPauseMenuItem;
import org.jhysim.gui.menuitem.JHySimPlayMenuItem;
import org.jhysim.gui.menuitem.JHySimPlugNewSimulationMenuItem;
import org.jhysim.gui.menuitem.JHySimQuitMenuItem;
import org.jhysim.gui.menuitem.JHySimResumeMenuItem;
import org.jhysim.gui.menuitem.JHySimSaveDataMenuItem;
import org.jhysim.gui.menuitem.JHySimSaveSimulationParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimSaveSimulatorParametersMenuItem;
import org.jhysim.gui.menuitem.JHySimShowDataMenuItem;
import org.jhysim.gui.menuitem.JHySimStopMenuItem;
import org.jhysim.gui.menuitem.JHySimUnplugSimulationMenuItem;
import org.jhysim.gui.menuitem.JHySimAnalyseDataMenuItem;

import org.jhysim.gui.timer.ProgressTimer;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.simulation.Simulation;
import org.jhysim.simulation.Simulator;

import org.jhysim.gui.JVisualizeProfilesFrame;

import org.jhysim.simulation.io.XmlPluggedSimulationFile;

import org.jhysim.simulation.methods.NumericalSchema;

import org.xml.sax.SAXException;

/**
 * Software which allows to explore 1D hydrodynamics simulations
 * @author Sébastien Majerowicz 
 */
public class JHySim extends JFrame implements ActionListener, Killable, Simulation, WindowListener
{
	private static final long serialVersionUID = 4348789202666451257L;

	public final static String NAME = "JHySim"; 

	public final static String PLUGGED_SIMULATIONS_FILENAME = "resources/simulations/plugged_simulations.xml";

	private JHySimQuitMenuItem quitMenuItem = null;

	private JHySimDefineSimulationParametersMenuItem defineSimulationMenuItem = null;
	private JHySimOpenSimulationParametersMenuItem openSimulationMenuItem = null;
	private JHySimSaveSimulationParametersMenuItem saveSimulationMenuItem = null;
	private JHySimPlugNewSimulationMenuItem plugSimulationMenuItem = null;
	private JHySimUnplugSimulationMenuItem unplugSimulationMenuItem = null;

	private JHySimDefineSimulatorParametersMenuItem defineSimulatorMenuItem = null;
	private JHySimOpenSimulatorParametersMenuItem openSimulatorMenuItem = null;
	private JHySimSaveSimulatorParametersMenuItem saveSimulatorMenuItem = null;
	private JHySimPlayMenuItem playMenuItem = null;
	private JHySimPauseMenuItem pauseMenuItem = null;
	private JHySimResumeMenuItem resumeMenuItem = null;
	private JHySimStopMenuItem stopMenuItem = null;
	private JHySimShowDataMenuItem showDataMenuItem = null;
	private JHySimAnalyseDataMenuItem visualizeDataMenuItem = null;
	private JHySimSaveDataMenuItem saveDataMenuItem = null;

	private JProgressBar progressBar = null;
	private Timer progressTimer = null;

	private Simulator simulator = null;
	private NumericalSchema schema = null;

/**
 * Constructor
 */
	public JHySim ()
	{
		super();

		this.quitMenuItem = new JHySimQuitMenuItem("Quit",null,KeyEvent.VK_Q,this);
		this.quitMenuItem.addActionListener(this);

		JMenu fileMenu = new JMenu("File",false);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(this.quitMenuItem);

		this.defineSimulationMenuItem = new JHySimDefineSimulationParametersMenuItem("Define",null,KeyEvent.VK_D,this);
		this.defineSimulationMenuItem.addActionListener(this);
		this.openSimulationMenuItem = new JHySimOpenSimulationParametersMenuItem("Open",null,KeyEvent.VK_O,this);
		this.openSimulationMenuItem.addActionListener(this);
		this.saveSimulationMenuItem = new JHySimSaveSimulationParametersMenuItem("Save",null,KeyEvent.VK_S,this);
		this.saveSimulationMenuItem.addActionListener(this);
		this.plugSimulationMenuItem = new JHySimPlugNewSimulationMenuItem("Plug a new one",null,KeyEvent.VK_P,this);
		this.plugSimulationMenuItem.addActionListener(this);
		this.unplugSimulationMenuItem = new JHySimUnplugSimulationMenuItem("Unplug",null,KeyEvent.VK_U,this);
		this.unplugSimulationMenuItem.addActionListener(this);

		JMenu simulationMenu = new JMenu("Simulations",false);
		simulationMenu.setMnemonic(KeyEvent.VK_S);
		simulationMenu.add(this.defineSimulationMenuItem);
		simulationMenu.insertSeparator(1);
		simulationMenu.add(this.openSimulationMenuItem);
		simulationMenu.add(this.saveSimulationMenuItem);
		simulationMenu.insertSeparator(4);
		simulationMenu.add(this.plugSimulationMenuItem);
		simulationMenu.add(this.unplugSimulationMenuItem);

		this.defineSimulatorMenuItem = new JHySimDefineSimulatorParametersMenuItem("Define parameters",null,KeyEvent.VK_D,this);
		this.defineSimulatorMenuItem.addActionListener(this);
		this.openSimulatorMenuItem = new JHySimOpenSimulatorParametersMenuItem("Open",null,KeyEvent.VK_O,this);
		this.openSimulatorMenuItem.addActionListener(this);
		this.saveSimulatorMenuItem = new JHySimSaveSimulatorParametersMenuItem("Save",null,KeyEvent.VK_S,this);
		this.saveSimulatorMenuItem.addActionListener(this);
		this.playMenuItem = new JHySimPlayMenuItem("Play",null,KeyEvent.VK_P,this);
		this.playMenuItem.addActionListener(this);
		this.pauseMenuItem = new JHySimPauseMenuItem("Pause",null,KeyEvent.VK_A,this);
		this.pauseMenuItem.addActionListener(this);
		this.resumeMenuItem = new JHySimResumeMenuItem("Resume",null,KeyEvent.VK_R,this);
		this.resumeMenuItem.addActionListener(this);
		this.stopMenuItem = new JHySimStopMenuItem("Stop",null,KeyEvent.VK_T,this);
		this.stopMenuItem.addActionListener(this);
		this.showDataMenuItem = new JHySimShowDataMenuItem("Show Data",null,KeyEvent.VK_H,this);
		this.showDataMenuItem.addActionListener(this);
		this.visualizeDataMenuItem = new JHySimAnalyseDataMenuItem("Analyse Data",null,KeyEvent.VK_A,this);
		this.visualizeDataMenuItem.addActionListener(this);
		this.saveDataMenuItem = new JHySimSaveDataMenuItem("Save Data",null,KeyEvent.VK_A,this);
		this.saveDataMenuItem.addActionListener(this);

		JMenu calculationMenu = new JMenu("Calculations",false);
		calculationMenu.setMnemonic(KeyEvent.VK_C);
		calculationMenu.add(this.defineSimulatorMenuItem);
		calculationMenu.insertSeparator(1);
		calculationMenu.add(this.openSimulatorMenuItem);
		calculationMenu.add(this.saveSimulatorMenuItem);
		calculationMenu.insertSeparator(4);
		calculationMenu.add(this.playMenuItem);
		calculationMenu.add(this.pauseMenuItem);
		calculationMenu.add(this.resumeMenuItem);
		calculationMenu.add(this.stopMenuItem);
		calculationMenu.insertSeparator(9);
		calculationMenu.add(this.showDataMenuItem);
		calculationMenu.add(this.visualizeDataMenuItem);
		calculationMenu.add(this.saveDataMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(simulationMenu);
		menuBar.add(calculationMenu);

		this.setJMenuBar(menuBar);

		this.progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		this.progressBar.setBorderPainted(true);
		this.progressBar.setStringPainted(true);

		this.progressTimer = new ProgressTimer(100,this);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle(JHySim.NAME);
		this.pack();
	}

/**
 * main method
 * @param args String[]
 */
	public static void main (String[] args)
	{
		JHySim jhysim = new JHySim();
		jhysim.setVisible(true);
	}

/**
 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
 */
	public void windowOpened (WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
 */
	public void windowClosing (WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
 */
	public void windowClosed (WindowEvent e)
	{
		this.kill();
	}

/**
 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
 */
	public void windowIconified (WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
 */
	public void windowDeiconified (WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
 */
	public void windowActivated (WindowEvent e)
	{}

/**
 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
 */
	public void windowDeactivated (WindowEvent e)
	{}

/**
 * @see org.application.gui.frame.Killable#kill()
 */
	public void kill ()
	{
		if ((simulator != null) && (simulator.isRunning())) this.simulator.kill();

		this.dispose();
		System.exit(0);
	}

/**
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed (ActionEvent e)
	{
		CommandPattern command = (CommandPattern)e.getSource();
		command.execute();
	}

/**
 * To show the progess bar 
 */
	public final void showProgressBar ()
	{
		this.progressBar.setValue(this.progressBar.getMinimum());
		this.progressTimer.start();

		this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		this.getContentPane().add(this.progressBar);

		this.getContentPane().validate();
		this.getContentPane().repaint();

		this.pack();
	}

/**
 * To update the progress bar
 */
	public final void updateProgressBar ()
	{
		if (this.simulator.isRunning() || this.simulator.isPaused())
		{
			this.progressBar.setValue(this.simulator.getDoneJobPercent());
		}
		else
		{
			this.hideProgressBar();
		}
	}

/**
 * To hide the progress bar 
 */
	public final void hideProgressBar ()
	{
		this.progressTimer.stop();

		if (this.simulator != null)
		{
			JVisualizeProfilesFrame vFrame = this.simulator.getVisualizationFrame();
			this.simulator = new Simulator(this.schema,this.simulator.getsavingDelay(),this.simulator.getmaxTimeSteps(),this.simulator.isDisplaySimulation(),this.simulator.getdisplayUpdatingTimeDelay());
			this.simulator.setVisualizationFrame(vFrame);
		}

		this.getContentPane().removeAll();
		this.getContentPane().setLayout(null);

		this.pack();
	}

/**
 * To retrieve the numerical schema
 * @return NumericalSchema
 * @see org.simulation.Simulation#getSchema
 */
	public final NumericalSchema getSchema ()
	{
		return this.schema;
	}
/**
 * To initialize the numerical schema
 * @param schema NumericalSchema
 * @see org.simulation.Simulation#setSchema(org.simulation.methods.NumericalSchema)
 */
	public void setSchema (NumericalSchema schema)
	{
		this.schema = schema;
		if (this.simulator != null) this.simulator.setSchema(this.schema);
	}
/**
 * To retrieve the simulator
 * @return Simulator
 * @see org.simulation.Simulation#getSimulator
 */
	public final Simulator getSimulator ()
	{
		return this.simulator;
	}
/**
 * To initialize the simulator
 * @param simulator Simulator
 * @see org.simulation.Simulation#setSimulator(org.simulation.Simulator)
 */
	public void setSimulator (Simulator simulator)
	{
		this.simulator = simulator;
	}

/**
 * To find all the available simulations in terms of package+class names
 * @param xmlfilename String XML file name where are the informations
 * @return String[] array of string
 * @see org.simulation.Simulation#retrieveAvailableSimulations(java.lang.String)
 */
	public String[] retrieveAvailableSimulations (String xmlfilename)
	{
		XmlPluggedSimulationFile jpsf = new XmlPluggedSimulationFile(xmlfilename);
		String[] sims = null;

		try
		{
			sims = jpsf.extractSimulations();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}

		return sims;
	}
/**
 * To find the available simulation names
 * @param classNames String[] package+class names
 * @return NumericalSchema[] array of simulations
 */
	public NumericalSchema[] retrieveAvailableSimulationClasses (String[] classNames)
	{
		NumericalSchema[] simulations = new NumericalSchema[classNames.length];

		for (int i = 0 ; i < classNames.length ; i++)
		{
			try
			{
				Class<?> newclass = Class.forName(classNames[i]);
				simulations[i] = (NumericalSchema)newclass.getDeclaredConstructor().newInstance();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		return simulations;
	}
}