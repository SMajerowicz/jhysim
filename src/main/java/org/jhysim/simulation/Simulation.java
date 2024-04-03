package org.jhysim.simulation;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * @author Sébastien Majerowicz
 */
public interface Simulation
{
/**
 * To retrieve the numerical schema
 */
	public NumericalSchema getSchema ();
/**
 * To initialise the numerical schema
 * @param schema NumericalSchema
 */
	public void setSchema (NumericalSchema schema);
/**
 * To retrieve the simulator
 * @return Simulator
 */
	public Simulator getSimulator ();
/**
 * To initialize the simulator
 * @param simulator Simulator
 */
	public void setSimulator (Simulator simulator);

/**
 * To find the available simulations in terms of package+class names
 * @param xmlfilename String XML file name where are the informations
 * @return String[] array of string
 */
	public String[] retrieveAvailableSimulations (String xmlfilename);
/**
 * To find the available simulation classes
 * @param classNames String[] package+class names
 * @return NumericalSchema[] array of simulations
 */
	public NumericalSchema[] retrieveAvailableSimulationClasses (String[] classNames);
}