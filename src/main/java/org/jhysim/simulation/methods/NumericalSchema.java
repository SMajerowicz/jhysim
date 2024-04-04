package org.jhysim.simulation.methods;

import java.util.ArrayList;

import org.jhysim.math.table.DataTable;
import org.jhysim.pattern.ObservablePattern;

/**
 * To use some numerical schemes in order to resolve differential equations
 * @author Sébastien Majerowicz
 */
public abstract class NumericalSchema implements ObservablePattern
{
	protected String name = "None";

	protected String[] numericalSchemaDescriptions = null;

	protected int choosenSchema = 0;

	protected DataTable datatable = null;

	protected ArrayList<double[]> yExtrema = new ArrayList<double[]>(0);//array list of y extrema (double[2])

	protected double smoothingFactor = 0.04;

/**
 * To retrieve the name of the simulation
 * @return String
 */
	public String getName ()
	{
		return this.name;
	}

/**
 * To retrieve the results
 */
	public DataTable getResults ()
	{
		return this.datatable;
	}

/**
 * To initialize all the variables
 * @param profiles ArrayList list of double array = initial profiles
 * @param variables ArrayList list of Double objects = initial variable value
 */
	public abstract void initVariables (ArrayList<double[]> profiles, ArrayList<Double> variables);

/**
 * To reset the simulations (must be done between two consecutive jobs)
 */
	public abstract void resetSchema ();

/**
 * To retrieve an initial profile according to an index
 * @param index int profile index (between 0 and the initial number of profiles-1)
 * @param niters int iteration index
 * @return double[]
 * @throws IndexOutOfBoundsException
 */
	public abstract double[] getProfile (int index, int niters) throws IndexOutOfBoundsException;

/**
 * To retrieve the number of profiles
 * @return int
 */
	public abstract int getNProfiles ();

/**
 * To retrieve the profile name
 * @param index int
 * @return String
 * @throws IndexOutOfBoundsException
 */
	public abstract String getProfileName (int index) throws IndexOutOfBoundsException;

/**
 * To retrieve the y profile extrema
 * @param index int
 * @return double[] two-element array
 * @throws IndexOutOfBoundsException
 */
	public double[] getYProfileExtrema (int index) throws IndexOutOfBoundsException
	{
		return (double[])this.yExtrema.get(index);
	}

/**
 * To intialize the y profile extrema
 * @param index int
 * @param ye double[]
 * @throws IndexOutOfBoundsException
 */
	public void setYProfileExtrema (int index, double[] ye) throws IndexOutOfBoundsException
	{
		this.yExtrema.remove(index);
		this.yExtrema.add(index,ye);
	}

/**
 * To retrieve a double parameter according to an index
 * <b>the 0-index parameter must be the spatial resolution</b>
 * @param index int
 * @return double parameter value
 * @throws IndexOutOfBoundsException
 */
	public abstract double getParameter (int index) throws IndexOutOfBoundsException;

/**
 * To retrieve the number of parameters
 * @return int
 */
	public abstract int getNParameters ();

/**
 * To retrieve the parameter name
 * @param index int
 * @return String
 * @throws IndexOutOfBoundsException
 */
	public abstract String getParameterName (int index) throws IndexOutOfBoundsException;

/**
 * To retrieve the parameter description
 * @param index int
 * @return String
 * @throws IndexOutOfBoundsException
 */
	public abstract String getParameterDescription (int index) throws IndexOutOfBoundsException;

/**
 * To save the usefull variables
 * @param id int identification index
 */
	public abstract void saveVariables (int id);

/**
 * To smooth the different variables
 */
	public abstract void smoothVariables ();

/**
 * To ignit the numerical simulation
 */
	public abstract void ignitCalculations ();

/**
 * To increment the time with a time step by applying the choosen numerical schema 
 */
	public abstract void computeNextTimeStep ();

/**
 * To retrieve the numerical schema description for a given index
 * @throws IndexOutOfBoundsException
 * @param index int
 * @return String
 */
	public final String getNumericalSchemaDescription (int index)
	{
		return this.numericalSchemaDescriptions[index];
	}

/**
 * To retrieve all the numerical schema description
 * @return String[]
 */
	public final String[] getNumericalSchemaDescriptions ()
	{
		return this.numericalSchemaDescriptions;
	} 

/**
 * To retrieve the choosen numerical schema
 * @return int
 */
	public final int getchoosenSchema ()
	{
		return this.choosenSchema;
	}
/**
 * To initialize the numerical schema
 * @param choosenSchema int
 */
	public final void setchoosenSchema (int choosenSchema)
	{
		this.choosenSchema = choosenSchema;
	}
}