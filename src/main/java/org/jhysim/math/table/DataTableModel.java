package org.jhysim.math.table;

import javax.swing.table.DefaultTableModel;

/**
 * Class observing a data table
 * @author Sébastien Majerowicz
 */
public class DataTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = -6288986219046157060L;
	DataTable table = null;

/**
 * Constructor
 * @param DataTable data table
 */
	public DataTableModel (DataTable table)
	{
		super();
		this.table = table;

		this.update();
	}

/**
 * To retrieve the number of columns
 * @return int
 */
	public final int getColumnCount()
	{
		if (this.table != null)
		{
			return this.table.getncols();
		}
		else
		{
			return 1;
		}
	}
/**
 * To retrieve the number of lines
 * @return int
 */
	public final int getRowCount()
	{
		if (this.table != null)
		{
			return this.table.getnrows();
		}
		else
		{
			return 1;
		}
	}
/**
 * To retrieve a column name
 * @param int index
 * @return String
 */
	public final String getColumnName (int col)
	{
		if (this.table.getcolumnUnity(col) != DataTable.NO_UNITY)
		{
			return this.table.getcolumnTitle(col) + " ("+ this.table.getcolumnUnity(col) + ")";
		}
		else
		{
			return this.table.getcolumnTitle(col);
		}
	}
/**
 * To retrieve a value
 * @throws IndexOutOfBoundsException
 * @param int row index
 * @param int column index
 * @return Object
 */
	public final Object getValueAt (int row, int col) throws IndexOutOfBoundsException
	{
		return Double.valueOf(this.table.getValue(row,col));
	}
/**
 * To retrieve the class of a column
 * @param int
 * @return Class
 */
	public final Class<?> getColumnClass (int c)
	{
		return this.getValueAt(0,c).getClass();
	}
/**
 * To know if a cell is editable
 * @return boolean false in our case
 */
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

/**
 * To update the table model
 */
	public final void update ()
	{
		int ncols = this.table.getncols();
		int nrows = this.table.getnrows();
		Object[][] data = new Object[nrows][ncols];
		Object[] titles = new Object[ncols];

		for (int j = 0 ; j < ncols ; j++)
		{
			for (int i = 0 ; i < nrows ; i++)
			{
				data[i][j] = Double.valueOf(this.table.getValue(i,j));
			}
			titles[j] = this.table.getcolumnTitle(j);
			if (this.table.getcolumnUnity(j) != DataTable.NO_UNITY)
			{
				titles[j] = (String)titles[j] + " (" + this.table.getcolumnUnity(j) + ")";
			}
		}

		this.setDataVector(data,titles);
	}
}