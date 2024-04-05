package org.jhysim.simulation.physics;

import java.util.ArrayList;
import java.util.Iterator;

import org.jhysim.pattern.ObserverPattern;

import org.jhysim.math.table.DataTable;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * Simulate an isothermal gas inclosed inside a tube. This includes the reflection on the egdes.
 * <b>Time is always resolve with an explicit eulerian method, only the space can be resolved with better schemas</b> 
 * @author Sébastien Majerowicz
 */
public class IsothermalShockTube extends NumericalSchema
{
	private ArrayList<ObserverPattern> observers = null;//all the observers

	private double[] density = null;
	private double[] velocity = null;
	private double soundspeed = 0.0;

	private double[] olddensity = null;
	private double[] oldvelocity = null;

	private double dt = 0.001;
	private double dx = 0.001;
	private int ncells = 0;

	private int ignitionLevel = 0;

/**
 * Constructor
 */
	public IsothermalShockTube ()
	{
		super();

		this.name = "Isothermal Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.datatable = new DataTable();

		this.yExtrema.add(new double[]{0.0,1.0});
		this.yExtrema.add(new double[]{-1.0,1.0});

		this.observers = new ArrayList<ObserverPattern>(0);
	}

/**
 * Constructor
 * @param profiles ArrayList list of double array = initial profiles (0=density, 1=velocity)
 * @param variables ArrayList list of Double objects = initial variable value (0=spatial resolution, 1=sound speed, 2=time interval, 3=smoothing factor) 
 */
	public IsothermalShockTube (ArrayList<double[]> profiles, ArrayList<Double> variables)
	{
		super();

		this.name = "Isothermal Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.density = (double[])profiles.get(0);
		this.velocity = (double[])profiles.get(1);
		this.soundspeed = ((Double)variables.get(1)).doubleValue();

		this.ncells = density.length;
		this.dt = ((Double)variables.get(2)).doubleValue();
		this.dx = 1.0/((double)this.ncells-1);

		this.smoothingFactor = ((Double)variables.get(3)).doubleValue();

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
	}

/**
 * Constructor
 * @param density double[] density profiles
 * @param velocity double[] velocity profiles
 * @param soundspeed double constant sound speed
 * @param dt double time interval between two consecutive steps
 * @param smoothingFactor double smoothing factor
 */
	public IsothermalShockTube (double[] density, double[] velocity, double soundspeed, double dt, double smoothingFactor)
	{
		super();

		this.name = "Isothermal Shock Tube";

		this.initNumericalSchemaDescriptions();

		this.density = density;
		this.velocity = velocity;
		this.soundspeed = soundspeed;

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
	}

/**
 * To initialize the numerical schema descriptions 
 */
	private final void initNumericalSchemaDescriptions ()
	{
		this.numericalSchemaDescriptions = new String[2];
		this.numericalSchemaDescriptions[0] = "EE for time and CE for space";
		this.numericalSchemaDescriptions[1] = "CE for time and space";
	}

/**
 * Initialize all the initial variables
 * @param profiles ArrayList list of double array = initial profiles (0=density, 1=velocity)
 * @param variables ArrayList list of Double objects = initial variable value (0=spatial resolution, 1=sound speed, 2=time interval, 3=smoothing factor)
 */
	public final void initVariables (ArrayList<double[]> profiles, ArrayList<Double> variables)
	{
		this.density = (double[])profiles.get(0);
		this.velocity = (double[])profiles.get(1);
		this.soundspeed = ((Double)variables.get(1)).doubleValue();

		this.ncells = density.length;
		this.dt = ((Double)variables.get(2)).doubleValue();
		this.dx = 1.0/((double)this.ncells-1);

		this.smoothingFactor = ((Double)variables.get(3)).doubleValue();

//		save the abscisse axis in the data table
		double[] xaxis = new double[this.ncells];
		for (int i = 0 ; i < this.ncells ; i++) xaxis[i] = i*dx;
		this.datatable.addColumn(xaxis,"x");

//		save the initial profiles
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
		return 2;
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
			default :
				throw new IndexOutOfBoundsException();
		}

		return profName;
	}

/**
 * To retrieve a double parameter according to an index
 * <b>the 0-index parameter must be the spatial resolution</b>
 * @param index int index
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
				result = this.soundspeed;
				break;
			case 2 :
				result = this.dt;
				break;
			case 3 :
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
		return 4;
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
				paramName = "Sound speed";
				break;
			case 2:
				paramName = "Time interval";
				break;
			case 3 :
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
		this.datatable.addColumn(var1,"density_"+String.valueOf(id));
		this.datatable.addColumn(var2,"velocity_"+String.valueOf(id));
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
	}

/**
 * @see org.simulation.methods.NumericalSchema#ignitCalculations()
 */
	public final void ignitCalculations ()
	{
		switch (this.choosenSchema)
		{
			case 1 :
				this.ignitionLevel = 0;
				break;
		}
	}

/**
 * @see org.simulation.methods.NumericalSchema#computeNextTimeStep()
 */
	public final void computeNextTimeStep ()
	{
		switch (this.choosenSchema)
		{
			case 0 :
				this.useEEtCExMethod();
				break;
			case 1 :
				this.useCEtCExMethod();
				break;
		}
	}

/**
 * Compute new profiles due to a time incrementation by using an eulerian explicit method for the time and
 * a eulerian centered method for the spatial dimension 
 */
	private void useEEtCExMethod ()
	{
//first cell
		double tempd = this.density[0];
		double tempv = this.velocity[0];

		double oldleftdensity = tempd;
		double oldleftvelocity = tempv;

//the density is the previous one minus the matter which goes to the 2nd cell
		this.density[0] -= this.density[1]*0.5*(this.velocity[0]+this.velocity[1])*dt/dx;
		this.velocity[0] = tempd*this.velocity[0] - 
						   (this.density[1]*(this.velocity[1]*this.velocity[1] + this.soundspeed*this.soundspeed) - 
							tempd*(tempv*tempv + this.soundspeed*this.soundspeed))*dt/dx;
		this.velocity[0] /= this.density[0];

//other cells
		for (int i = 1 ; i < this.ncells-1 ; i++)
		{
			tempd = this.density[i];
			tempv = this.velocity[i];

			this.density[i] = this.density[i] - (this.density[i+1]*this.velocity[i+1] -
												 oldleftdensity*oldleftvelocity)*dt*0.5/dx;
			this.velocity[i] = tempd*this.velocity[i] -
							   (this.density[i+1]*(this.velocity[i+1]*this.velocity[i+1] + this.soundspeed*this.soundspeed) - 
								oldleftdensity*(oldleftvelocity*oldleftvelocity + this.soundspeed*this.soundspeed))*dt*0.5/dx;
			this.velocity[i] /= this.density[i];

			oldleftdensity = tempd;
			oldleftvelocity = tempv;
		}
//last cell
//the density is the previous one plus the matter which goes from the n-2 cell
		tempd = this.density[this.ncells-1];
		this.density[this.ncells-1] += oldleftdensity*0.5*(oldleftvelocity+this.velocity[this.ncells-1])*dt/dx;
		this.velocity[this.ncells-1] = tempd*this.velocity[this.ncells-1] - 
											 (tempd*(this.velocity[this.ncells-1]*this.velocity[this.ncells-1] + this.soundspeed*this.soundspeed) - 
											  oldleftdensity*(oldleftvelocity*oldleftvelocity + this.soundspeed*this.soundspeed))*dt/dx;
		this.velocity[this.ncells-1] /= this.density[this.ncells-1];

//case of reflection
		if (this.velocity[0] < 0.0) this.velocity[0] *= -1.0;
		if (this.velocity[this.ncells-1] > 0.0) this.velocity[this.ncells-1] *= -1.0;
	}

/**
 * Compute new profiles by using an eulerian centered method for the time and the spatial dimension 
 */
	private void useCEtCExMethod ()
	{
		if (this.ignitionLevel == 0)
		{
			this.olddensity = new double[this.ncells];
			this.oldvelocity = new double[this.ncells];

			System.arraycopy(this.density,0,this.olddensity,0,this.ncells);
			System.arraycopy(this.velocity,0,this.oldvelocity,0,this.ncells);

			this.useEEtCExMethod();
			this.ignitionLevel++;
		}
		else
		{
//t time variable saving
			double[] d = new double[this.ncells];
			double[] v = new double[this.ncells];

			System.arraycopy(this.density,0,d,0,this.ncells);
			System.arraycopy(this.velocity,0,v,0,this.ncells);

//first cell
			double tempd = this.density[0];
			double tempv = this.velocity[0];

		  	double oldleftdensity = tempd;
		  	double oldleftvelocity = tempv;

//the density is the previous one minus the matter which goes to the 2nd cell
		  	this.density[0] -= this.density[1]*0.5*(this.velocity[0]+this.velocity[1])*dt/dx;
		  	this.velocity[0] = this.olddensity[0]*this.oldvelocity[0] - 
							   (this.density[1]*(this.velocity[1]*this.velocity[1] + this.soundspeed*this.soundspeed) - 
								tempd*(tempv*tempv + this.soundspeed*this.soundspeed))*dt/dx;
		  	this.velocity[0] /= this.density[0];

//other cells
		  	for (int i = 1 ; i < this.ncells-1 ; i++)
		  	{
				tempd = this.density[i];
			  	tempv = this.velocity[i];

			  	this.density[i] = this.olddensity[i] - (this.density[i+1]*this.velocity[i+1] -
														oldleftdensity*oldleftvelocity)*dt/dx;
				this.velocity[i] = this.olddensity[i]*this.oldvelocity[i] -
								   (this.density[i+1]*(this.velocity[i+1]*this.velocity[i+1] + this.soundspeed*this.soundspeed) - 
									oldleftdensity*(oldleftvelocity*oldleftvelocity + this.soundspeed*this.soundspeed))*dt/dx;
				this.velocity[i] /= this.density[i];

				oldleftdensity = tempd;
				oldleftvelocity = tempv;
			}
//last cell
//the density is the previous one plus the matter which goes from the n-2 cell
			tempd = this.density[this.ncells-1];
			this.density[this.ncells-1] += oldleftdensity*0.5*(oldleftvelocity+this.velocity[this.ncells-1])*dt/dx;
			this.velocity[this.ncells-1] = this.olddensity[this.ncells-1]*this.velocity[this.ncells-1] - 
										   (tempd*(this.velocity[this.ncells-1]*this.velocity[this.ncells-1] + this.soundspeed*this.soundspeed) - 
											oldleftdensity*(oldleftvelocity*oldleftvelocity + this.soundspeed*this.soundspeed))*dt/dx;
			this.velocity[this.ncells-1] /= this.density[this.ncells-1];

//case of reflection
			if (this.velocity[0] < 0.0) this.velocity[0] *= -1.0;
			if (this.velocity[this.ncells-1] > 0.0) this.velocity[this.ncells-1] *= -1.0;

//copy the t time variable into the t-dt time
			System.arraycopy(d,0,this.olddensity,0,this.ncells);
			System.arraycopy(v,0,this.oldvelocity,0,this.ncells);
		}
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