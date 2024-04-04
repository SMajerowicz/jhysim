package org.jhysim.simulation.physics;

import java.util.ArrayList;
import java.util.Iterator;

import org.jhysim.pattern.ObserverPattern;

import org.jhysim.math.table.DataTable;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * This simulates a shock tube where an adiabatic gaz is lying
 * <b>Time is always resolve with an explicit eulerian method, only the space can be resolved with better schemas</b>
 * @author Sébastien Majerowicz
 */
public class AdiabaticShockTube extends NumericalSchema
{
	private ArrayList<ObserverPattern> observers = null;//all the observers

	private double[] density = null;
	private double[] velocity = null;
	private double[] temperature = null;

	private double dt = 0.001;
	private double dx = 0.001;
	private int ncells = 0;

/**
 * Constructor
 */
	public AdiabaticShockTube ()
	{
		super();

		this.name = "Adiabatic Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.datatable = new DataTable();

		this.yExtrema.add(new double[]{0.0,1.0});
		this.yExtrema.add(new double[]{-1.0,1.0});
		this.yExtrema.add(new double[]{0.0,1.0});

		this.observers = new ArrayList<ObserverPattern>(0);
	}

/**
 * Constructor
 * @param profiles ArrayList list of double array = initial profiles (0=density, 1=velocity, 2=temperature)
 * @param variables ArrayList list of Double objects = initial variable value (0=spatial resolution, 1=time interval, 2=smoothing factor) 
 */
	public AdiabaticShockTube (ArrayList<double[]> profiles, ArrayList<Double> variables)
	{
		super();

		this.name = "Adiabatic Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.density = (double[])profiles.get(0);
		this.velocity = (double[])profiles.get(1);
		this.temperature = (double[])profiles.get(2);

		this.ncells = density.length;
		this.dt = ((Double)variables.get(1)).doubleValue();
		this.dx = 1.0/((double)this.ncells-1);

		this.smoothingFactor = ((Double)variables.get(2)).doubleValue();

		this.datatable = new DataTable();

		this.observers = new ArrayList<ObserverPattern>(0);

//	  save the abscisse axis in the data table
		double[] xaxis = new double[this.ncells];
		for (int i = 0 ; i < this.ncells ; i++) xaxis[i] = i*dx;
		this.datatable.addColumn(xaxis,"x");
//	  save the initial profiles
		this.saveVariables(0);

		this.yExtrema.add(new double[]{0.0,1.0});
		this.yExtrema.add(new double[]{-1.0,1.0});
		this.yExtrema.add(new double[]{0.0,1.0});
	}

/**
 * Constructor
 * @param density double[] density profile
 * @param velocity double[] velocity profile
 * @param temperature double[] temperature profile
 * @param dt double time interval between two consecutive steps
 * @param smoothingFactor double smoothing factor
 */
	public AdiabaticShockTube (double[] density, double[] velocity, double[] temperature, double dt, double smoothingFactor)
	{
		super();

		this.name = "Adiabatic Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.density = density;
		this.velocity = velocity;
		this.temperature = temperature;

		this.ncells = density.length;
		this.dt = dt;
		this.dx = 1.0/((double)this.ncells-1);

		this.smoothingFactor = smoothingFactor; 

		this.datatable = new DataTable();

		this.observers = new ArrayList<ObserverPattern>(0);

//save the abscisse axis in the data table
		double[] xaxis = new double[this.ncells];
		for (int i = 0 ; i < this.ncells ; i++) xaxis[i] = i*dx;
		this.datatable.addColumn(xaxis,"x");
//save the initial profiles
		this.saveVariables(0);

		this.yExtrema.add(new double[]{0.0,1.0});
		this.yExtrema.add(new double[]{-1.0,1.0});
		this.yExtrema.add(new double[]{0.0,1.0});
	}

/**
 * To initialize the numerical schema descriptions 
 */
	private final void initNumericalSchemaDescriptions ()
	{
		this.numericalSchemaDescriptions = new String[2];
		this.numericalSchemaDescriptions[0] = "EE for time and CE for space";
	}

/**
 * Initialize all the initial variables
 * @param profiles ArrayList list of double array = initial profiles (0=density, 1=velocity, 2=temperature)
 * @param variables ArrayList list of Double objects = initial variable value (0=spatial resolution, 1=time interval, 2=smoothing factor)
 */
	public final void initVariables (ArrayList<double[]> profiles, ArrayList<Double> variables)
	{
		this.density = (double[])profiles.get(0);
		this.velocity = (double[])profiles.get(1);
		this.temperature = (double[])profiles.get(2);

		this.ncells = density.length;
		this.dt = ((Double)variables.get(1)).doubleValue();
		this.dx = 1.0/((double)this.ncells-1);

		this.smoothingFactor = ((Double)variables.get(2)).doubleValue();

//save the abscisse axis in the data table
			double[] xaxis = new double[this.ncells];
			for (int i = 0 ; i < this.ncells ; i++) xaxis[i] = i*dx;
			this.datatable.addColumn(xaxis,"x");

//save the initial profiles
			this.saveVariables(0);
		}

/**
 * To reset the simulations (must be done between two consecutive jobs)
 */
	public void resetSchema ()
	{
		double[] xaxis = this.datatable.getColumn(0);
		this.density = this.datatable.getColumn(1);
		this.velocity = this.datatable.getColumn(2);
		this.temperature = this.datatable.getColumn(3);

		this.datatable = new DataTable();

//		save the abscisse axis in the data table
		this.datatable.addColumn(xaxis,"x");

//		save the initial profiles
		this.saveVariables(0);
	}

/**
 * To retrieve an initial profile according to an index
 * @param index int profile index (between 0 and the initial number of profiles-1)
 * @param niters int iteration index
 * @return double[]
 * @throws IndexOutOfBoundsException
 */
	public double[] getProfile (int index, int niters) throws IndexOutOfBoundsException
	{
		double[] profile = null; 

		switch (index)
		{
			case 0 :
				profile = this.datatable.getColumn(2*niters+1);
				break;
			case 1 :
				profile = this.datatable.getColumn(2*niters+2);
				break;
			case 2 : 
				profile = this.datatable.getColumn(2*niters+3);
			default :
				throw new IndexOutOfBoundsException();
		}

		return profile;
	}

/**
 * To retrieve the number of profiles
 * @return int
 */
	public int getNProfiles ()
	{
		return 3;
	}

/**
 * @see org.simulation.methods.NumericalSchema#getProfileName(int)
 */
	public String getProfileName(int index) throws IndexOutOfBoundsException
	{
		String profName = null;

		switch (index)
		{
			case 0 :
				profName = "Density";
				break;
			case 1 :
				profName = "Velocity";
				break;
			case 2 :
				profName = "Temperature";
				break;
			default :
				throw new IndexOutOfBoundsException();
		}

		return profName;
	}

/**
 * To retrieve a double parameter according to an index
 * <b>the 0-index parameter must be the spatial resolution</b>
 * @param index int
 * @return double parameter value
 * @throws IndexOutOfBoundsException
 */
	public final double getParameter (int index) throws IndexOutOfBoundsException
	{
		double result = 0.0;

		switch (index)
		{
			case 0 :
				result = this.dx;
				break;
			case 1 :
				result = this.dt;
				break;
			case 2 :
				result = this.smoothingFactor;
				break;
			default :
				throw new IndexOutOfBoundsException();
		}

		return result;
	}

/**
 * To retrieve the number of parameters
 * @return int
 */
	public int getNParameters ()
	{
		return 3;
	}

/**
 * @see org.simulation.methods.NumericalSchema#getParameterName(int)
 */
	public String getParameterName (int index) throws IndexOutOfBoundsException
	{
		String paramName = null;

		switch (index)
		{
			case 0 :
				paramName = "Spatial Resolution";
				break;
			case 1 :
				paramName = "Time interval";
				break;
			case 2 :
				paramName = "Smoothing factor";
				break;
			default :
				throw new IndexOutOfBoundsException();
		}

		return paramName;
	}

/**
 * @see org.simulation.methods.NumericalSchema#getParameterDescription(int)
 */
	public String getParameterDescription (int index) throws IndexOutOfBoundsException
	{
		return "double";
	}

/**
 * To retrieve the results
 * @return DataTable
 */
	public final DataTable getResults ()
	{
		return this.datatable;
	}

/**
 * To save the current occurence of the variables
 * @param id int identification index
 */
	public final void saveVariables (int id)
	{
		double[] var1 = new double[this.ncells];
		System.arraycopy(this.density,0,var1,0,this.ncells);
		double[] var2 = new double[this.ncells];
		System.arraycopy(this.velocity,0,var2,0,this.ncells);
		double[] var3 = new double[this.ncells];
		System.arraycopy(this.temperature,0,var2,0,this.ncells);
		this.datatable.addColumn(var1,"density_"+String.valueOf(id));
		this.datatable.addColumn(var2,"velocity_"+String.valueOf(id));
		this.datatable.addColumn(var3,"temperature_"+String.valueOf(id));
	}

/**
 * To smooth the different variables
 */
	public void smoothVariables ()
	{
		double factor = 1.0-2.0*this.smoothingFactor;
		this.density[0] = factor*this.density[0] + 2*this.smoothingFactor*this.density[1];
		this.velocity[0] = factor*this.velocity[0] + 2*this.smoothingFactor*this.velocity[1];

		for (int i = 1 ; i < this.ncells-1 ; i++)
		{
			this.density[i] = factor*this.density[i] + this.smoothingFactor*(this.density[i-1]+this.density[i+1]);
			this.velocity[i] = factor*this.velocity[i] + this.smoothingFactor*(this.velocity[i-1]+this.velocity[i+1]);
		}

		this.density[this.ncells-1] = factor*this.density[this.ncells-1] + 2*this.smoothingFactor*this.density[this.ncells-2];
		this.velocity[this.ncells-1] = factor*this.velocity[this.ncells-1] + 2*this.smoothingFactor*this.velocity[this.ncells-2];
		this.temperature[this.ncells-1] = factor*this.temperature[this.ncells-1] + 2*this.smoothingFactor*this.temperature[this.ncells-2];
	}

/**
 * @see org.simulation.methods.NumericalSchema#ignitCalculations()
 */
	public final void ignitCalculations ()
	{
		
	}

/**
 * @see org.simulation.methods.NumericalSchema#computeNextTimeStep()
 */
	public void computeNextTimeStep()
	{
		
	}

/**
 * @see org.application.pattern.ObservablePattern#getObservers()
 */
	public ArrayList<ObserverPattern> getObservers ()
	{
		return this.observers;
	}

/**
 * @see org.application.pattern.ObservablePattern#addObserver(org.application.pattern.ObserverPattern)
 */
	public void addObserver (ObserverPattern observer)
	{
		this.observers.add(observer);
	}

/**
 * @see org.application.pattern.ObservablePattern#removeObserver(org.application.pattern.ObserverPattern)
 */
	public void removeObserver (ObserverPattern observer)
	{
		this.observers.remove(observer);
	}

/**
 * @see org.application.pattern.ObservablePattern#removeAllObservers()
 */
	public void removeAllObservers ()
	{
		int n = this.observers.size();

		for (int i = 0 ; i < n ; i++)
		{
			((ObserverPattern)this.observers.get(0)).killObserver();
			this.observers.remove(0);
		}

		this.observers = new ArrayList<ObserverPattern>(0);
	}

/**
 * @see org.application.pattern.ObservablePattern#notifyObservers()
 */
	public void notifyObservers ()
	{
		Iterator<ObserverPattern> iterator = this.observers.iterator();
		while (iterator.hasNext())
		{
			((ObserverPattern)iterator.next()).updateObserver();
		}
	}
}