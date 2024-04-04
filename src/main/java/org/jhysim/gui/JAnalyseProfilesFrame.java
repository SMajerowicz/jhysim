package org.jhysim.gui;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * Class allows to analyse profiles from a simulation
 * @author Sébastien Majerowicz
 */
public class JAnalyseProfilesFrame extends JVisualizeProfilesFrame
{
	private static final long serialVersionUID = 1435194701108208370L;

/**
 * Constructor
 * CAN BE AN OBSERVER OF JSHOWPROFILEPANEL OBJECTS
 * @param schema NumericalSchema numerical schema
 */
	public JAnalyseProfilesFrame (NumericalSchema schema)
	{
		super(schema);

		for (int i = 0 ; i < this.nProfiles ; i++)
		{
			this.profilePanels[i].addMouseMotionListener(this.profilePanels[i]);
		}
	}
}