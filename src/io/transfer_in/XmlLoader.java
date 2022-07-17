package io.transfer_in;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data_structures.Board;
import data_structures.BoardImpl;
import data_structures.Item;
import data_structures.Subsection;
import data_structures.SubsectionImpl;

public class XmlLoader implements Loader
{
private File src;
	
	public XmlLoader(File src)
	{
		if(src == null || !src.exists() || src.isDirectory())
		{
			throw new IllegalArgumentException();
		}
		this.src = src;
	}
	
	public XmlLoader(String src)
	{
		this(new File(src));
	}

	@Override
	public Board load() throws IOException
	{
		Board board = new BoardImpl();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(src);
			
			NodeList subsections = document.getElementsByTagName("subsection");
			
			for(int i = 0x0; i < subsections.getLength(); i++)
			{
				int id = Integer.parseInt(((Element) subsections.item(i)).getAttribute("id"));
				
				Subsection subsection = new SubsectionImpl(id, null);
				
				NodeList elements = subsections.item(i).getChildNodes();
				
				for(int j = 0x0; j < elements.getLength(); j++)
				{
					Node node = elements.item(j);
					if(node.getNodeType() == Node.ELEMENT_NODE)
					{
						Item item = Item.parseXmlElement((Element) node, subsection);
						board.set(item);
					}
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			throw new Error(e);
		}
		catch (SAXException e)
		{
			throw new Error(e);
		}
		return board;
	}
}
