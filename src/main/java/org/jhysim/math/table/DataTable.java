package org.jhysim.math.table;

import org.jhysim.pattern.ObservablePattern;
import org.jhysim.pattern.ObserverPattern;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class representing a 2D data table
 * @author Sébastien Majerowicz
 */
public class DataTable extends ArrayList<Object> implements ObservablePattern
{
	private static final long serialVersionUID = 4405218625321276000L;

	public static final String NO_UNITY = "None";

	private ArrayList<ObserverPattern> observers = null;//list of observers

	private int nrows = 0;//number of rows
	private ArrayList<String> columnTitles = null;//each column has a name
	private ArrayList<String> columnUnities = null;//each column has an unity

/**
 * Constructor
 */
	public DataTable ()
	{
		super();

		this.observers = new ArrayList<ObserverPattern>(0);

		this.nrows = 0;
		this.columnTitles = new ArrayList<String>(0);
		this.columnUnities = new ArrayList<String>(0);
	}

/**
 * To retrieve the number of columns
 * @return int
 */
	public final int getnrows ()
	{
		return this.nrows;
	}
/**
 * To retrieve the number of columns
 * @return int
 */
	public final int getncols ()
	{
		return this.size();
	}
/**
 * To retrieve the column title array
 * @return ArrayList
 */
	public final ArrayList<String> getcolumnTitles ()
	{
		return this.columnTitles;
	}
/**
 * To initialize a column title
 * @throws IndexOutOfBOundsException
 * @param int index
 * @param String title
 */
	public final void setcolumnTitle (int index, String title) throws IndexOutOfBoundsException
	{
		this.columnTitles.remove(index);
		this.columnTitles.add(index,title);
	}
/**
 * To retrieve a specified column title
 * @throws IndexOutOfBoundsException
 * @return String
 */
	public final String getcolumnTitle (int index) throws IndexOutOfBoundsException
	{
		return (String)this.columnTitles.get(index);
	}
/**
 * To retrieve the column unity name array
 * @return ArrayList
 */
	public final ArrayList<String> getcolumnUnities ()
	{
		return this.columnUnities;
	}
/**
 * To initialize a column unity
 * @throws IndexOutOfBOundsException
 * @param int index
 * @param String unity
 */
	public final void setcolumnUnity (int index, String unity) throws IndexOutOfBoundsException
	{
		this.columnUnities.remove(index);
		this.columnUnities.add(index,unity);
	}
/**
 * To retrieve a specified column unity
 * @throws IndexOutOfBoundsException
 * @return String
 */
	public final String getcolumnUnity (int index) throws IndexOutOfBoundsException
	{
		return (String)this.columnUnities.get(index);
	}

/**
 * To add a new column (this checks that the new column has the same size than the other columns)
 * @param double[]
 * @return boolean everything was ok (true) or not (false)
 */
	public final boolean addColumn (double[] newcolumn)
	{
		boolean succeed = false;

		if ((newcolumn.length != 0) && ((this.nrows == 0) || (newcolumn.length == this.nrows)))
		{
			this.columnTitles.add(String.valueOf(columnTitles.size()));
			this.columnUnities.add(DataTable.NO_UNITY);
			this.add(newcolumn);
			succeed = true;
		}

		if (succeed && (this.nrows == 0))
		{
			this.nrows = newcolumn.length;
		}

		return succeed;
	}
/**
 * To add a new column (this checks that the new column has the same size than the other columns) with its title
 * @param double[]
 * @param String
 * @return boolean everything was ok (true) or not (false)
 */
	public final boolean addColumn (double[] newcolumn, String title)
	{
		boolean succeed = false;

		if ((newcolumn.length != 0) && ((this.nrows == 0) || (newcolumn.length == this.nrows)))
		{
			this.columnTitles.add(title);
			this.columnUnities.add(DataTable.NO_UNITY);
			this.add(newcolumn);
			succeed = true;
		}

		if (succeed && (this.nrows == 0))
		{
			this.nrows = newcolumn.length;
		}

		return succeed;
	}

/**
 * To add a new column (this checks that the new column has the same size than the other columns) with its title and its unity
 * @param double[]
 * @param String title
 * @param String unity
 * @return boolean everything was ok (true) or not (false)
 */
	public final boolean addColumn (double[] newcolumn, String title, String unity)
	{
		boolean succeed = false;

		if ((newcolumn.length != 0) && ((this.nrows == 0) || (newcolumn.length == this.nrows)))
		{
			this.columnTitles.add(title);
			this.columnUnities.add(unity);
			this.add(newcolumn);
			succeed = true;
		}

		if (succeed && (this.nrows == 0))
		{
			this.nrows = newcolumn.length;
		}

		return succeed;
	}

/**
 * To retrieve a specified column
 * @throws IndexOutOfBoundsException
 * @param int index
 * @param the wanted column
 */
	public final double[] getColumn (int index) throws IndexOutOfBoundsException
	{
		return (double[])this.get(index);
	}
/**
 * To initialize the column values
 * @throws IndexOutOfBoundsException
 * @param int index
 * @param the wanted column
 */
	public final void setColumn (int index, double[] values) throws IndexOutOfBoundsException
	{
		this.remove(index);
		this.add(index,values);
	}
/**
 * To remove a specified column
 * @throws IndexOutOfBoundsException
 * @param int index
 */
	public final void removeColumn (int index) throws IndexOutOfBoundsException
	{
		this.columnTitles.remove(index);
		this.columnUnities.remove(index);
		this.remove(index);

		if (this.size() == 0)
		{
			nrows = 0;
		}
	}

/**
 * To add a row at the end
 */
	public final void addRow ()
	{
		double[] values = null;
		double[] oldvalues = null;

		int ncols = this.getncols();

		for (int i = 0 ; i < ncols ; i++)
		{
			oldvalues = this.getColumn(i);
			values = new double[this.nrows+1];
			System.arraycopy(oldvalues,0,values,0,this.nrows);
			this.setColumn(i,values);
		}

		this.nrows++;
	}
/**
 * To add some rows at the end
 * @param int number of rows which will be added
 */
	public final void addRows (int n)
	{
		double[] values = null;
		double[] oldvalues = null;

		int ncols = this.getncols();

		for (int i = 0 ; i < ncols ; i++)
		{
			oldvalues = this.getColumn(i);
			values = new double[this.nrows+n];
			System.arraycopy(oldvalues,0,values,0,this.nrows);
			this.setColumn(i,values);
		}

		this.nrows += n;
	}
/**
 * To insert a row at a given position
 * @throws IndexOutOfBoundsException
 * @param int index
 */
	public final void insertRow (int index) throws IndexOutOfBoundsException
	{
		if ((index < 0) || (index > this.nrows))
		{
			throw new IndexOutOfBoundsException();
		}

		double[] values = null;
		double[] oldvalues = null;

		int ncols = this.getncols();

		for (int i = 0 ; i < ncols ; i++)
		{
			oldvalues = this.getColumn(i);
			values = new double[this.nrows+1];
			System.arraycopy(oldvalues,0,values,0,index);
			System.arraycopy(oldvalues,index+1,values,index+2,oldvalues.length);
			this.setColumn(i,values);
		}

		this.nrows++;
	}
/**
 * To remove a specified row
 * @throws IndexOutOfBoundsException
 * @param int index
 */
	public final void removeRow (int index) throws IndexOutOfBoundsException
	{
		if ((index < 0) || (index >= this.nrows))
		{
			throw new IndexOutOfBoundsException();
		}

		double[] values = null;
		double[] oldvalues = null;

		int ncols = this.getncols();

		for (int i = 0 ; i < ncols ; i++)
		{
			oldvalues = this.getColumn(i);
			values = new double[this.nrows-1];
			System.arraycopy(oldvalues,0,values,0,index);
			System.arraycopy(oldvalues,index+1,values,index,this.nrows-index-1);
			this.setColumn(i,values);
		}

		this.nrows--;
	}

/**
 * To retrieve the value of a given element
 * @throws IndexOutOfBoundsException
 * @param int row index
 * @param int column index
 * @return double
 */
	public final double getValue (int irow, int icol) throws IndexOutOfBoundsException
	{
		return ((double[])this.get(icol))[irow];
	}
/**
 * To retrieve all the data in a 2D array
 * @return double[][]
 */
	public final double[][] getAllValues ()
	{
		int ncols = this.size();
		double[][] result = new double[ncols][nrows];

		for (int i = 0 ; i < ncols ; i++)
		{
			for (int j = 0 ; j < nrows ; j++)
			{
				result[i][j] = this.getValue(i,j);
			}
		}

		return result;
	}
/**
 * To retrieve the maximum value of a given column
 * @throws IndexOutOfBoundsException
 * @param int index
 * @return double
 */
	public final double getMaxValue (int index) throws IndexOutOfBoundsException
	{
		double max = Double.NEGATIVE_INFINITY;

		double[] values = this.getColumn(index);
		int size = values.length;
		for (int i = 0 ; i < size ; i++)
		{
			if (values[i] > max)
			{
				max = values[i];
			}
		}

		return max;
	}
/**
 * To retrieve the minimum value of a given column
 * @throws IndexOutOfBoundsException
 * @param int index
 * @return double
 */
	public final double getMinValue (int index) throws IndexOutOfBoundsException
	{
		double min = Double.POSITIVE_INFINITY;

		double[] values = this.getColumn(index);
		int size = values.length;
		for (int i = 0 ; i < size ; i++)
		{
			if (values[i] < min)
			{
				min = values[i];
			}
		}

		return min;
	}
/**
 * To retrieve the sum of all elements in a specified column
 * @throws IndexOutOfBoundsException
 * @param int index
 * @return double
 */
	public final double getTotal (int index) throws IndexOutOfBoundsException
	{
		double ans = 0.0;

		double[] array = this.getColumn(index);
		int size = array.length;
		for (int i = 0 ; i < size ; i++)
		{
			ans += array[i];
		}

		return ans;
	}
/**
 * To retrieve a statistical momentum from a given column
 * @throws IndexOutOfBoundsException
 * @param column index
 * @param moment order
 * @param value for the centered moment
 * @return double the wanted moment
 */
	public final double getStatisticalMomentum (int index, int order, double value) throws IndexOutOfBoundsException
	{
		double ans = 0.0;

		double power = (double)order;

		double[] array = this.getColumn(index);
		int size = array.length;
		for (int i = 0 ; i < size ; i++)
		{
			ans += Math.pow(array[i],power) - Math.pow(value,power);
		}
		ans /= Math.pow((double)size,(double)power);

		return ans;
	}

/**
 * To retrieve the list of the observers
 * @return ArrayList
 */
	public final ArrayList<ObserverPattern> getObservers ()
	{
		return this.observers;
	}
/**
 * To add an observer
 * @param Observer
 */
	public final void addObserver (ObserverPattern observer)
	{
		this.observers.add(observer);
	}
/**
 * To remove an observer
 * @param ObserverPattern
 */
	public final void removeObserver (ObserverPattern observer)
	{
		this.observers.remove(observer);
	}
/**
 * To remove all the observers
 */
	public final void removeAllObservers ()
	{
		int n = this.observers.size();

		for (int i = 0 ; i < n ; i++)
		{
			((ObserverPattern)this.observers.get(0)).killObserver();
		}

		this.observers = new ArrayList<ObserverPattern>(0);
	}
/**
 * To notify all observers that they must be updated
 */
	public final void notifyObservers ()
	{
		Iterator<ObserverPattern> iterator = this.observers.iterator();
		while (iterator.hasNext())
		{
			((ObserverPattern)iterator.next()).updateObserver();
		}
	}
}