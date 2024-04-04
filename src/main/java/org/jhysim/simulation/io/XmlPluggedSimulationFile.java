package org.jhysim.simulation.io;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jhysim.xml.XmlDomFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

/**
 * To manage the plugged simulations
 * @author Sébastien Majerowicz
 */
public class XmlPluggedSimulationFile extends XmlDomFactory
{
/**
 * Constructor
 * @param filename String
 */
	public XmlPluggedSimulationFile (String filename)
	{
		super(filename);
	}

/**
 * To extract all the plugged simulations 
 * @return String[] simulation class names
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 */
	public final String[] extractSimulations () throws IOException, ParserConfigurationException, SAXException
	{
		String[] sims = null;

		Document document = this.parse(false);

		Element root = document.getDocumentElement();
		this.checkNode(root);

		Node node = root.getFirstChild();
		sims = new String[root.getChildNodes().getLength()];
		int index = 0;
		while (node != null)
		{
			sims[index] = node.getFirstChild().getNodeValue();
			index++;
			node = node.getNextSibling();
		}

		return sims;
	}

/**
 * To plug a new simulation
 * @param className String class name
 * @throws SAXException
 * @throws TransformerConfigurationException
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws TransformerException
 */
	public final void plugNewSimulation (String className) throws SAXException, TransformerConfigurationException, IOException, ParserConfigurationException, TransformerException
	{
//first parse the xml file to get the DOM tree
		Document document = this.parse(false);

//then, add the new simulation to the DOM tree
		Element root = document.getDocumentElement();
		this.checkNode(root);

		Element element = document.createElement("simulation");
		element.appendChild(document.createTextNode(className));

		root.appendChild(element);

//finally, write the document
		this.write(document,null);
	}

/**
 * To remove some plugged simulations
 * @param simulations String[]
 * @throws SAXException
 * @throws TransformerConfigurationException
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws TransformerException
 */
	public final void removePluggedSimulations (String[] simulations) throws SAXException, TransformerConfigurationException, IOException, ParserConfigurationException, TransformerException
	{
//first parse the xml file to get the DOM tree
		Document document = this.parse(false);

//then, remove the selected simulations from the DOM tree
		Element root = document.getDocumentElement();
		this.checkNode(root);

		Node node = null;
//the for loop is necessary to recompute the root child size after every loop
		for (int i = 0 ; i < root.getChildNodes().getLength() ; i++)
		{
			node = root.getChildNodes().item(i);
			for (int j = 0 ; j < simulations.length ; j++)
			{
				if (node.getFirstChild().getNodeValue().equals(simulations[j]))
				{
					root.removeChild(node);
					break;
				}
			}
		}

//finally the document is saved
		this.write(document,null);
	}
}