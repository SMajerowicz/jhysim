package org.jhysim.math.table.gui;

import org.jhysim.gui.frame.Killable;

import org.jhysim.pattern.ObserverPattern;

import org.jhysim.math.table.DataTable;
import org.jhysim.math.table.DataTableModel;

import java.awt.BorderLayout;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Class managing the displaying of a DataTable via a JTable
 * @author Sébastien Majerowicz
 */
public class JDataTableFrame extends JFrame implements Killable, ObserverPattern, WindowListener
{
	private static final long serialVersionUID = -2195113700355120145L;
	private DataTable datatable = null;
	private DataTableModel tablemodel = null;

	private JTable jtable = null;

/**
 * Constructeur
 * @param DataTable
 */
	public JDataTableFrame (DataTable datatable)
	{
		super();

		this.datatable = datatable;
		tablemodel = new DataTableModel(this.datatable);

		this.datatable.addObserver(this);//add the observer to the observer list

		jtable = new JTable(tablemodel);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(jtable),BorderLayout.CENTER);

		this.pack();
		this.setTitle("Data table view");
	}

/**
 * To manage the window events
 */
	public void windowClosing (WindowEvent e)
	{
		this.kill();
	}

	public void windowActivated (WindowEvent e)
	{}

	public void windowClosed (WindowEvent e)
	{}

	public void windowDeactivated (WindowEvent e)
	{}

	public void windowDeiconified (WindowEvent e)
	{}

	public void windowIconified (WindowEvent e)
	{}

	public void windowOpened (WindowEvent e)
	{}

/**
 * To kill this frame
 */
	public final void kill ()
	{
		this.datatable.removeObserver(this);//remove the observer from the observer list
		this.dispose();
	}

/**
 * To update the observer
 */
	public final void updateObserver ()
	{
		this.tablemodel.update();
		this.jtable = new JTable(this.tablemodel);
//update the content pane
		this.getContentPane().removeAll();
		this.getContentPane().add(new JScrollPane(jtable),BorderLayout.CENTER);
		this.getContentPane().validate();
		this.getContentPane().repaint();
	}
/**
 * To kill the observer
 */
	public void killObserver ()
	{
		this.kill();
	}

/**
 * To retrieve the multiple selection
 * @return int[] arrays of indices
 */
	public final int[] getMultipleSelection ()
	{
		return this.jtable.getSelectedRows();
	}
}