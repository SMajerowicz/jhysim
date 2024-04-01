package org.jhysim.gui.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * To filter xml format file (.xml)
 * @author Sébastien Majerowicz
 */
public class XmlFileFilter extends FileFilter
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
		else if (file.getName().endsWith(".xml") || file.getName().endsWith(".XML"))
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
		return "Xml file";
	}
}