package org.jhysim.gui;

import javax.swing.JFileChooser;

import org.jhysim.gui.filechooser.GenericFileFilter;

/**
 * To only choose an Xml file
 * @author Sébastien Majerowicz
 */
public class JXmlFileChooser extends JFileChooser
{
	private static final long serialVersionUID = 3040123414504651970L;

/**
 * Constructor
 * @param currentDirectory String current directory
 * @param dialogTitle String dialog title
 */
	public JXmlFileChooser (String currentDirectory, String dialogTitle)
	{
		super(currentDirectory);
		this.setDialogTitle(dialogTitle);

//clear the obvious file filter
		this.removeChoosableFileFilter(this.getAcceptAllFileFilter());
//only files can be selected
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
//add file filters
		this.addChoosableFileFilter(new GenericFileFilter(".xml","Xml files"));
	}
}