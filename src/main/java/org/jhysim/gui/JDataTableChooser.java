package org.jhysim.gui;

import org.jhysim.gui.filechooser.GenericFileFilter;

import javax.swing.JFileChooser;

/**
 * Mimic a JFileChooser to only take into account ascii and fits format file
 * @author Sébastien Majerowicz
 */
public class JDataTableChooser extends JFileChooser
{
	private static final long serialVersionUID = -560920942956246729L;
/**
 * Constructor
 * @param String current directory
 * @param String dialog title
 */
	public JDataTableChooser (String currentDirectory, String dialogtitle)
	{
		super(currentDirectory);
		this.setDialogTitle(dialogtitle);

//clear the obvious file filter
		this.removeChoosableFileFilter(this.getAcceptAllFileFilter());
//only files can be selected
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
//add file filters
		this.addChoosableFileFilter(new GenericFileFilter(".dat","Ascii data file"));
		this.addChoosableFileFilter(new GenericFileFilter(".fits","Fits data file"));
	}

/**
 * To know if the selected file filter were an AsciiFileFilter object
 * @return boolean
 */
	public final boolean isAsciiFileFilterSelected ()
	{
		if (this.getFileFilter().getDescription() == (this.getChoosableFileFilters())[0].getDescription())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
/**
 * To know if the selected file filter were a FitsFileFilter object
 * @return boolean
 */
	public final boolean isFitsFileFilterSelected ()
	{
		if (this.getFileFilter().getDescription() == (this.getChoosableFileFilters())[1].getDescription())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}