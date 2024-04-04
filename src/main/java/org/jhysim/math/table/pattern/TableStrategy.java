package org.jhysim.math.table.pattern;

import org.jhysim.math.table.DataTable;

import org.jhysim.pattern.StrategyPattern;

import java.io.IOException;

/**
 * To allow the Strategy design pattern
 * @author Sébastien Majerowicz
 */
public abstract class TableStrategy implements StrategyPattern
{
/**
 * To retrieve the strategy name
 */
	public String getStrategy ()
	{
		return "Unknown";
	}
/**
 * To retrieve a 2D table
 * @throws IOException
 * @param String file name
 * @return DataTable
 */
	public abstract DataTable getTable (String filename) throws IOException;
/**
 * To save a 2D Table
 * @throws IOException
 * @param String filename
 * @param DataTable
 */
	public abstract void saveTable (String filename, DataTable table) throws IOException;
}