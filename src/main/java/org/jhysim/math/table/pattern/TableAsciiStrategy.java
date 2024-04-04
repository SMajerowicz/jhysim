package org.jhysim.math.table.pattern;

import org.jhysim.math.table.DataTable;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

import java.util.ArrayList;

/**
 * To retrieve a data array from an ascii file
 * @author Sébastien Majerowicz
 */
public class TableAsciiStrategy extends TableStrategy
{
	private final static String NAME = "ASCII";

/**
 * To retrieve the strategy name
 */
	public final String getStrategy ()
	{
		return TableAsciiStrategy.NAME;
	}

/**
 * To retrieve a 2D table
 * @throws IOException
 * @param String file name
 * @return DataTable
 */
	public final DataTable getTable (String filename) throws IOException
	{
		DataTable table = null;

		StreamTokenizer st = new StreamTokenizer(new FileReader(filename));
		st.parseNumbers();//parse only numbers
		st.eolIsSignificant(true);//end of line can be treated

//First, reach for all numbers in some ArrayList objects (more flexible)
//the first line gives some indications (ncols particularly)
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>(0);
		int index = 0;
		while (st.nextToken() != StreamTokenizer.TT_EOL)
		{
			data.add(new ArrayList<Double>(0));
			((ArrayList<Double>)data.get(index)).add(new Double(st.nval));
			index++;
		}

		index = 0;
		while (st.nextToken() != StreamTokenizer.TT_EOF)
		{
			if (st.ttype == StreamTokenizer.TT_EOL)
			{
				index = 0;
			}
			else
			{
				((ArrayList<Double>)data.get(index)).add(new Double(st.nval));
				index++;
			}
		}

//Then, all the numbers are put into a DataTable object
		table = new DataTable();

		int ncols = data.size();
		int nrows = 0;
		double[] darray = null;
		for (int i = 0 ; i < ncols ; i++)
		{
			nrows = ((ArrayList<Double>)data.get(i)).size();
			darray = new double[nrows];
			for (int j = 0 ; j < nrows ; j++)
			{
				darray[j] = ((Double)(((ArrayList<Double>)data.get(i)).get(j))).doubleValue();
			}
			table.addColumn(darray);
		}

		if (table.getncols() == 0)
		{
			table = null;
		}

		return table;
	}
/**
 * To save a 2D Table
 * @throws IOException
 * @param String filename
 * @param DataTable
 */
	public final void saveTable (String filename, DataTable table) throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileOutputStream(filename));

		double[][] array = table.getAllValues();
		int ncols = table.getncols();
		int nrows = table.getnrows();

		StringBuffer sbuf = null;

		for (int i = 0 ; i < nrows ; i++)
		{
			sbuf = new StringBuffer();
			for (int j = 0 ; j < ncols-1 ; j++)
			{
				sbuf.append(array[j][i]).append(' ');
			}
			sbuf.append(array[ncols-1][i]);
			pw.println(sbuf.toString());
		}

		pw.flush();
		pw.close();
	}
}