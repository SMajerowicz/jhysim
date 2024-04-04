package org.jhysim.simulation.io;

import java.io.IOException;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jhysim.xml.XmlDomFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

/**
 * To extract informations about parameters on a previous simulation
 * @author Sébastien Majerowicz
 */
public class XmlSimulationParametersFile extends XmlDomFactory
{
	private String simulationName = null; 

	private int numSchema = 0;
	private ArrayList<double[]> profiles = null;
	private ArrayList<double[]> yExtrema = null;
	private ArrayList<Double> parameters = null;

/**
 * Constructor
 * @param filename String file name 
 */
	public XmlSimulationParametersFile (String filename)
	{
		super(filename);

		this.simulationName = "None";
	}
/**
 * Constructor
 * @param simulationName String simulation name
 * @param filename String file name 
 */
	public XmlSimulationParametersFile (String simulationName, String filename)
	{
		super(filename);

		this.simulationName = simulationName;
	}

/**
 * To extract everything define a simulation
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException 
 */
	public final void extractParameters () throws IOException, ParserConfigurationException, SAXException
	{
		Document document = null;
		document = this.parse(false);

		Element root = document.getDocumentElement();
		this.checkNode(root);

		this.simulationName = root.getNodeName();

//search for the chosen numerical schema
		Node ele = root.getFirstChild();
		this.numSchema = Integer.parseInt(ele.getAttributes().getNamedItem("value").getNodeValue());
		ele = ele.getNextSibling();
//search for profiles
		this.profiles = new ArrayList<double[]>(0);
		this.yExtrema = new ArrayList<double[]>(0); 
		Node oele = null;
		double[] prof = null;
		double ymin = Double.MAX_VALUE, ymax = Double.MIN_VALUE;
		int index = 0;
		while (ele.getNodeName().equals("profile"))
		{
			oele = ele.getFirstChild();
//search for cell value
			prof = new double[ele.getChildNodes().getLength()];
			ymin = Double.MAX_VALUE;
			ymax = Double.MIN_VALUE;

			while (oele != null)
			{
				prof[index] = Double.parseDouble(oele.getAttributes().getNamedItem("value").getNodeValue());

				if (prof[index] > ymax)
				{
					ymax = prof[index];
				}
				else if (prof[index] < ymin)
				{
					ymin = prof[index];
				}

				index++;
				oele = oele.getNextSibling();
			}

			this.profiles.add(prof);

			if ((Math.abs(ymin) < 1.0E-20) && (Math.abs(ymax) < 1.0E-20))
			{
				ymin = -0.5;
				ymax = 0.5;
			}
			else
			{
				if (ymin < 0.0)
				{
					ymin = 1.5*ymin;
				}
				else
				{
					ymin = 0.5*ymin;
				}
		
				if (ymax < 0.0)
				{
					ymin = 0.5*ymax;
				}
				else
				{
					ymax = 1.5*ymax;
				}
			}

			this.yExtrema.add(new double[]{ymin,ymax});

			index = 0;
			ele = ele.getNextSibling();
		}

//search for double type parameters
		this.parameters = new ArrayList<Double>(0);
		while ((ele != null) && ele.getNodeName().equals("parameter"))
		{
			this.parameters.add(new Double(ele.getAttributes().getNamedItem("value").getNodeValue()));

			ele = ele.getNextSibling();
		}
	}

/**
 * To write everything define a simulation
 * @param numSchema int numerical schema
 * @param profiles ArrayList list of double array representing initial profiles
 * @param parameters ArrayList list of double type parameters
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws TransformerConfigurationException
 * @throws TransformerException
 */
	public final void save (int numSchema, ArrayList<double[]> profiles, ArrayList<Double> parameters) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException
	{
		DocumentBuilderFactory factoryDoc = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoryDoc.newDocumentBuilder();

		Document document = builder.newDocument();

		Element root = document.createElement(this.simulationName);
		document.appendChild(root);

//first save the numerical schema
		Element ele = document.createElement("schema");
		ele.setAttribute("value",String.valueOf(numSchema));
		root.appendChild(ele);

//then save all the profiles
		Element oele = null;
		double[] prof = null;
		int size = profiles.size();
		for (int i = 0 ; i < size ; i++)
		{
			ele = document.createElement("profile");

			prof = (double[])profiles.get(i);
			for (int j = 0 ; j < prof.length ; j++)
			{
				oele = document.createElement("cell");
				oele.setAttribute("value",String.valueOf(prof[j]));
				ele.appendChild(oele);
			}

			root.appendChild(ele);
		}

//finally save all the double type parameters
		size = parameters.size();
		for (int i = 0 ; i < size ; i++)
		{
			ele = document.createElement("parameter");
			ele.setAttribute("value",String.valueOf(((Double)parameters.get(i)).doubleValue()));
			root.appendChild(ele);
		}

		this.write(document,null);
	}

/**
 * To retrieve the numerical Schema
 * @return int
 */
	public final int getNumSchema()
	{
		return this.numSchema;
	}
/**
 * To retrieve the simulation name
 * @return String
 */
	public final String getSimulationName()
	{
		return this.simulationName;
	}
/**
 * To retrieve the parameter values
 * @return ArrayList
 */
	public final ArrayList<Double> getParameters()
	{
		return this.parameters;
	}
/**
 * To retrieve a list of double type array corresponding to profiles
 * @return ArrayList
 */
	public final ArrayList<double[]> getProfiles()
	{
		return this.profiles;
	}
/**
 * To retrieve a list of two-element double type array corresponding to profile y extrema
 * @return ArrayList
 */
	public final ArrayList<double[]> getYExtrema ()
	{
		return this.yExtrema;
	}
}