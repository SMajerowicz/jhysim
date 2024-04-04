package org.jhysim.simulation.io;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import java.util.ArrayList;

import org.jhysim.xml.XmlDomFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

/**
 * To manage the simulator parameters
 * @author Sébastien Majerowicz
 */
public class XmlSimulatorParametersFile extends XmlDomFactory
{
	private int maxTimeSteps = 1000;//maximum of time steps
	private int savingDelay = 1;//delay between two different savings
	private boolean displaySimulation = false;//to know if the simulation must be displayed or not
	private int displayUpdatingTimeDelay = 10;//delay between two display updates in terms of time and in units of ms
	private ArrayList<double[]> yExtList = null;//y extremum list

/**
 * Constructor
 * @param filename String file name
 */
	public XmlSimulatorParametersFile (String filename)
	{
		super(filename);
	}

/**
 * To extract all the simulator parameters
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 */
	public final void extractParameters () throws IOException, ParserConfigurationException, SAXException
	{
		Document document = this.parse(false);

		Element root = document.getDocumentElement();
		this.checkNode(root);

		Node node = root.getFirstChild();
		this.maxTimeSteps = Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());
		node = node.getNextSibling();
		this.savingDelay = Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());
		node = node.getNextSibling();
		String ans = node.getAttributes().getNamedItem("value").getNodeValue();
		if (ans.equals("yes"))
		{
			this.displaySimulation = true;
		}
		else
		{
			this.displaySimulation = false;
		}
		node = node.getNextSibling();
		this.displayUpdatingTimeDelay = Integer.parseInt(node.getAttributes().getNamedItem("value").getNodeValue());

		this.yExtList = new ArrayList<double[]>(10);
		node = node.getNextSibling().getFirstChild();
		double ymin, ymax;
		while (node != null)
		{
			ymin = Double.parseDouble(node.getFirstChild().getNodeValue());
			node = node.getNextSibling();
			ymax = Double.parseDouble(node.getFirstChild().getNodeValue());
			this.yExtList.add(new double[]{ymin,ymax});
			node = node.getNextSibling();
		}
	}

/**
 * To write everything define a simulation
 * @param mtd int maximum of time steps
 * @param sd int saving delay
 * @param ds boolean display the simulation or not
 * @param dt int display updating delay
 * @param yexts ArrayList y extremum list
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws TransformerConfigurationException
 * @throws TransformerException
 */
	public final void save (int mtd, int sd, boolean ds, int dt, ArrayList<double[]> yexts) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException
	{
		DocumentBuilderFactory factoryDoc = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoryDoc.newDocumentBuilder();

		Document document = builder.newDocument();

		Element root = document.createElement("simulator");
		document.appendChild(root);
		Element ele = document.createElement("max_time_delay");
		ele.setAttribute("value",String.valueOf(mtd));
		root.appendChild(ele);
		ele = document.createElement("saving_delay");
		ele.setAttribute("value",String.valueOf(sd));
		root.appendChild(ele);
		ele = document.createElement("display_simulation");
		if (ds)
		{
			ele.setAttribute("value",String.valueOf("yes"));
		}
		else
		{
			ele.setAttribute("value",String.valueOf("no"));
		}
		root.appendChild(ele);
		ele = document.createElement("display_updating_time_delay");
		ele.setAttribute("value",String.valueOf(dt));
		root.appendChild(ele);

		ele = document.createElement("y_extrema");
		int size = yexts.size();
		double[] yext = null;
		Element subele = null;
		for (int i = 0 ; i < size ; i++)
		{
			yext = (double[])yexts.get(i);
			subele = document.createElement("ymin");
			subele.appendChild(document.createTextNode(String.valueOf(yext[0])));
			ele.appendChild(subele);
			subele = document.createElement("ymax");
			subele.appendChild(document.createTextNode(String.valueOf(yext[1])));
			ele.appendChild(subele);
			root.appendChild(ele);
		}

		this.write(document,null);
	}

/**
 * The simulation can be displayed or not
 * @return boolean
 */
	public final boolean isDisplaySimulation()
	{
		return this.displaySimulation;
	}
/**
 * To retrieve the display updating time delay
 * @return int
 */
	public final int getDisplayUpdatingTimeDelay()
	{
		return this.displayUpdatingTimeDelay;
	}
/**
 * To retrieve the maximum of time steps
 * @return int
 */
	public final int getMaxTimeSteps()
	{
		return this.maxTimeSteps;
	}
/**
 * To retrieve the saving delay
 * @return int
 */
	public final int getSavingDelay()
	{
		return this.savingDelay;
	}
/**
 * To retrieve the y extremum list
 * @return ArrayList
 */
	public final ArrayList<double[]> getYExtList()
	{
		return yExtList;
	}
}