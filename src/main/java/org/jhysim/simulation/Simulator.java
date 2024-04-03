package org.jhysim.simulation;

import org.jhysim.gui.JVisualizeProfilesFrame;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * @author Sébastien Majerowicz
 */
public class Simulator extends Thread
{
	private NumericalSchema schema = null;//current numerical schema

	private int maxTimeSteps = 1000;//maximum of time steps
	private int savingDelay = 1;//delay between two different savings
	private boolean displaySimulation = false;//to display or not the simulation
	private int displayUpdatingTimeDelay = 10;//delay between two display updates in terms of time and in units of ms

	private int simCount = 0;//number of time steps
	private boolean running = false;
	private boolean paused = false;

	private JVisualizeProfilesFrame visualizationFrame = null;

/**
 * Constructor 
 * @param schema NumericalSchema a numerical schema
 * @param savingDelay int saving delay
 * @param maxTimeSteps int maximum of time steps 
 * @param displaySimulation boolean display or not the simulation
 * @param displayUpdatingTimeDelay int pause between two displays in units of ms
 */
	public Simulator (NumericalSchema schema, int savingDelay, int maxTimeSteps, boolean displaySimulation, int displayUpdatingTimeDelay)
	{
		super();

		this.schema = schema;
		this.maxTimeSteps = maxTimeSteps;
		this.savingDelay = savingDelay;
		this.displaySimulation = displaySimulation;
		this.displayUpdatingTimeDelay = displayUpdatingTimeDelay;

		this.simCount = 0;
		this.running = false;
		this.paused = false;
	}

/**
 * To start the thread
 */
	public final void startUp ()
	{
		this.running = true;
		this.simCount = 0;

		this.schema.resetSchema();

		if (this.displaySimulation)
		{
			if (this.visualizationFrame != null) this.visualizationFrame.kill();
			this.visualizationFrame = JVisualizeProfilesFrame.getInstance(this.schema);
			if (this.visualizationFrame != null)
			{ 
				this.schema.addObserver(this.visualizationFrame);
				this.visualizationFrame.setVisible(true);
				this.schema.notifyObservers();
			}
		}

		this.start();
	}
/**
 * To run the thread
 */
	public final void run ()
	{
		long t1 = System.currentTimeMillis(), t2 = 0;//to remove the time consuming during the computation

		this.schema.ignitCalculations();

		while (this.running && (this.simCount < this.maxTimeSteps))
		{
			this.schema.computeNextTimeStep();
			this.schema.smoothVariables();
			this.simCount++;

			if (this.simCount%this.savingDelay == 0)
			{
				this.schema.saveVariables(this.simCount);
			}

			if (this.displaySimulation && (this.simCount%this.savingDelay == 0))
			{
				t2 = System.currentTimeMillis();
				try
				{
					if (t2-t1 < this.displayUpdatingTimeDelay)
					{
						Simulator.sleep((long)this.displayUpdatingTimeDelay-t2+t1);
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				this.schema.notifyObservers();
				t1 = System.currentTimeMillis();
			}

			while (this.paused)
			{
				try
				{
					Simulator.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		this.kill();
	}
/**
 * To stop and kill the thread
 */
	public final void kill ()
	{
		if (this.visualizationFrame != null)
		{
			this.schema.removeObserver(this.visualizationFrame);
			this.visualizationFrame.showSelectProfileSlider();
		}

		this.paused = false;
		this.running = false;
	}
/**
 * To make a pause during the thread
 */
	public final void standBy ()
	{
		this.paused = true;
		this.visualizationFrame.showSelectProfileSlider();
	}
/**
 * To resume the thread
 */
	public final void goOn ()
	{
		this.paused = false;
		this.visualizationFrame.hideSelectProfileSlider();
	}

/**
 * To initialize the maximum of time steps
 * @param maxTimeSteps int
 */
	public final void setmaxTimeSteps (int maxTimeSteps)
	{
		this.maxTimeSteps = maxTimeSteps;
	}
/**
 * To retieve the maximum of time steps
 * @return int
 */
	public final int getmaxTimeSteps ()
	{
		return this.maxTimeSteps;
	}
/**
 * To initialize the saving delay
 * @param savingDelay int
 */
	public final void setsavingDelay (int savingDelay)
	{
		this.savingDelay = savingDelay;
	}
/**
 * To retrieve the value of the saving delay
 * @return int
 */
	public final int getsavingDelay ()
	{
		return this.savingDelay;
	}
/**
 * The simulation can be displayed or not
 * @return boolean
 */
	public final boolean isDisplaySimulation()
	{
		return this.displaySimulation;
	}
/**
 * To display or not the simulation 
 * @param displaySimulation boolean
 */
	public final void setDisplaySimulation (boolean displaySimulation)
	{
		this.displaySimulation = displaySimulation;
	}
/**
 * To initialize the display updating delay in terms of time and in untis of ms
 * @param displayUpdatingTimeDelay int
 */
	public final void setdisplayUpdatingTimeDelay (int displayUpdatingTimeDelay)
	{
		this.displayUpdatingTimeDelay = displayUpdatingTimeDelay;
	}
/**
 * To retrieve the value of the display updating delay in terms of time and in untis of ms
 * @return int
 */
	public final int getdisplayUpdatingTimeDelay ()
	{
		return this.displayUpdatingTimeDelay;
	}
/**
 * To know of this thread is running
 * @return boolean
 */
	public final boolean isRunning ()
	{
		return this.running;
	}
/**
 * To know if this thread is paused
 * @return boolean
 */
	public final boolean isPaused ()
	{
		return this.paused;
	}
/**
 * To retrieve the numerical schema
 * @return NumericalSchema
 */
	public final NumericalSchema getSchema ()
	{
		return this.schema;
	}
/**
 * To initialize the numerical schema
 * @param schema NumericalSchema
 */
	public final void setSchema (NumericalSchema schema)
	{
		this.schema = schema;
	}
/**
 * To retrieve the number of iterations
 * @return int
 */
	public final int getSimCount()
	{
		return simCount;
	}

/**
 * to retrieve the frame of visualization
 * @return JVisualizaProfilesFrame
 */
	public final JVisualizeProfilesFrame getVisualizationFrame()
	{
		return this.visualizationFrame;
	}

/**
 * To initialize the frame of visualization
 * @param visualizationFrame JVisualizeProfilesFrame
 */
	public final void setVisualizationFrame (JVisualizeProfilesFrame visualizationFrame)
	{
		this.visualizationFrame = visualizationFrame;
	}

/**
 * To retrieve the amount of done job in terms of percent
 * @return int
 */
	public final int getDoneJobPercent ()
	{
		return (int)(100.0*(double)this.simCount/(double)this.maxTimeSteps);
	}
}