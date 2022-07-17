package documents;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data_structures.Item;
import data_structures.Subsection;

public class BoardXmlDocumentBuilder implements BoardDocumentBuilder
{
	private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder docBuilder;
	private Document doc;
	private Element rootElement;

	private ExportableDocument exportableDocument;
	
	private Subsection currentSubsection;
	private Element currentSubsectionElement;
	private boolean currentSubsectionIsVoid = true;
	
	private boolean done = false;
	
	public BoardXmlDocumentBuilder()
	{
		try
		{
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			throw new Error(e);
		}
	}
	
	@Override
	public void openDocument()
	{
		if(done)
		{
			throw new RuntimeException("Document has already been created!");
		}
		
		rootElement = doc.createElement("board");
		doc.appendChild(rootElement);
	}
	
	@Override
	public void openSubsection(Subsection subsection)
	{
		if(done)
		{
			throw new RuntimeException("Document has already been created!");
		}
		
		currentSubsection = subsection;
		currentSubsectionIsVoid = true;
		currentSubsectionElement =  doc.createElement("subsection");
		currentSubsectionElement.setAttribute("id", subsection.getId() + "");
	}

	@Override
	public void closeSubsection()
	{
		if(done)
		{
			throw new RuntimeException("Document has already been created!");
		}
		
		if(!currentSubsectionIsVoid)
		{
			rootElement.appendChild(currentSubsectionElement);
		}
		currentSubsection = null;
		currentSubsectionElement = null;
	}

	@Override
	public void addItemElement(Item item)
	{
		if(done)
		{
			throw new RuntimeException("Document has already been created!");
		}
		
		if(item.getValue() != Item.NO_VALUE)
		{
			currentSubsectionIsVoid = false;
			currentSubsectionElement.appendChild(buildElementItem(item));
		}
	}
	
	private Element buildElementItem(Item item)
	{
		if(done)
		{
			throw new RuntimeException("Document has already been created!");
		}
		
		Element itemElement = doc.createElement("element");
		
		Element rowElement = doc.createElement("row");
		rowElement.appendChild(doc.createTextNode(item.getRow() - currentSubsection.getRowMin() + ""));
		itemElement.appendChild(rowElement);
		
		Element colElement = doc.createElement("col");
		colElement.appendChild(doc.createTextNode(item.getCol() - currentSubsection.getColMin() + ""));
		itemElement.appendChild(colElement);
		
		Element valueElement = doc.createElement("value");
		valueElement.appendChild(doc.createTextNode(item.getStringValue()));
		itemElement.appendChild(valueElement);
		
		return itemElement;
	}
	
	@Override
	public void closeDocument()
	{
		if(done)
		{
			throw new RuntimeException("Document has already been built!");
		}
		done = true;
		exportableDocument = new XmlExportableDocument(doc);
	}

	@Override
	public ExportableDocument getDocument()
	{
		if(!done)
		{
			throw new IllegalStateException();
		}
		return exportableDocument;
	}
}
