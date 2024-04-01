package org.jhysim.gui.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * To filter pre-compiled JAVA format file (.class)
 * @author Sébastien Majerowicz
 */
public class ClassFileFilter extends FileFilter
{
/**
 * Whether the given file is accepted by this filter.
 * @param file File
 */
	public boolean accept (File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		else if (file.getName().endsWith(".class") || file.getName().endsWith(".CLASS"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

/**
 * The description of this filter.
 * @return String
 */
	public String getDescription ()
	{
		return "Pre-compiled JAVA file";
	}
}