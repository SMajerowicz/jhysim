package org.jhysim.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

/**
 * To manage the parsing of an XML file into a DOM tree or the writing of a DOM tree into an XML file
 * @author Sébastien Majerowicz
 */
public class XmlDomFactory
{
	private String filename = null;

/**
 * Constructor
 * @param filename String file name
 */
	public XmlDomFactory (String filename)
	{
		this.filename = filename;
	}

/**
 * Remove potentially #text Node between a Node and its child in the DOM representation
 * because mixed data is not allowed for region file
 * @param node Node selected node
 */
	protected final void checkNode (Node node)
	{
		Node son = null;
		int s = 0;
		boolean nottext = false;

		if (node.hasChildNodes())
		{
			s = node.getChildNodes().getLength();
			if (s > 1)
			{
				son = node.getFirstChild();
//	  look for one Child != TEXT
				while (son != null)
				{
					if (son.getNodeType() != Node.TEXT_NODE)
					{
						nottext = true;
						break;
					}
					son = son.getNextSibling();
				}

//	  if in every Child, just one is different of a TEXT then remove all the TEXT
				if (nottext)
				{
					son = node.getFirstChild().getNextSibling();

					while (son != null)
					{
						if (son.getPreviousSibling().getNodeType() == Node.TEXT_NODE)
						{
							node.removeChild(son.getPreviousSibling());
						}
						son = son.getNextSibling();
					}

//	  take care about the last
					if (node.getLastChild().getNodeType() == Node.TEXT_NODE)
					{
						node.removeChild(node.getLastChild());
					}
				}
			}

//	  recursivity
			son = node.getFirstChild();
			while (son != null)
			{
				this.checkNode(son);
				son = son.getNextSibling();
			}
		}
	}

/**
 * To parse an XML file into a DOM tree
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 * @param validate boolean validate the document (true) or not (false) during the reading
 * @return Document
 */
	public final Document parse (boolean validate) throws IOException, ParserConfigurationException, SAXException
	{
		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validate);
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new File(this.filename));

		return document;
	}
/**
 * To write a DOM tree into an XML file
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws TransformerConfigurationException
 * @throws TransformerException
 * @param document Document
 * @param dtdfilename String DTD file name
 */
	public final void write (Document document, String dtdfilename) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		transformer.setOutputProperty(OutputKeys.METHOD,"xml");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
		transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
		if (dtdfilename == null)
		{
			transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
		}
		else
		{
			transformer.setOutputProperty(OutputKeys.STANDALONE,"no");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,dtdfilename);
		}
		transformer.transform(new DOMSource(document),new StreamResult(new File(this.filename)));
	}
}