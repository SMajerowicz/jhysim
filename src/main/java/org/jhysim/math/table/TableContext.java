package org.jhysim.math.table;

import java.io.IOException;

import org.jhysim.math.table.pattern.TableAsciiStrategy;
import org.jhysim.math.table.pattern.TableFitsStrategy;
import org.jhysim.math.table.pattern.TableStrategy;

/**
 * Class context according to the Strategy design pattern
 * @author Sébastien Majerowicz
 */
public class TableContext
{
	private String filename = null;

	private TableStrategy strategy = null;

/**
 * Constructor
 * @param String file name
 */
	public TableContext (String filename)
	{
		this.filename = filename;
	}

/**
 * To initialize the file name
 * @param String file name
 */
	public final void setfilename (String filename)
	{
		this.filename = filename;
	}

/**
 * To return the file name
 * @return String file name
 */
	public final String getfilename ()
	{
		return this.filename;
	}

/**
 * To use an ascii file
 */
	public final void setTableAsciiStrategy ()
	{
		this.strategy = new TableAsciiStrategy();
	}

/**
 * To use a fits file
 */
	public final void setTableFitsStrategy ()
	{
		this.strategy = new TableFitsStrategy();
	}

/**
 * To know which strategy is used
 * @param String
 */
	public final String getStrategyName ()
	{
		return strategy.getStrategy();
	}

/**
 * To retrieve a 2D table
 * @throws IOException
 * @param String file name
 * @return DataTable
 */
	public final DataTable getTable () throws IOException
	{
		return this.strategy.getTable(this.filename);
	}
/**
 * To save a 2D Table
 * @throws IOException
 * @param String filename
 * @param DataTable
 */
	public final void saveTable (DataTable table) throws IOException
	{
		this.strategy.saveTable(this.filename,table);
	}
}