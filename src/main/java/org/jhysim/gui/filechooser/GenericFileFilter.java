package org.jhysim.gui.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * To filter generic format file
 * @author Sébastien Majerowicz
 */
public final class GenericFileFilter extends FileFilter
{
	private String extension = null;
	private String description = null;

/**
 * Constructor
 * @param extension in lower string String
 * @param file format description String
 */
	public GenericFileFilter (String extension, String description)
	{
		this.extension = extension;
		this.description = description;
	}

/**
 * Whether the given file is accepted by this filter
 * @param file File
 */
	public boolean accept (File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		else if (file.getName().endsWith(this.extension) || file.getName().endsWith(this.extension.toUpperCase()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

/**
 * The extension of this filter
 * @return String
 */
	public String getExtension ()
	{
		return this.extension;
	}

/**
 * The description of this filter
 * @return String
 */
	public String getDescription ()
	{
		return this.description;
	}
}