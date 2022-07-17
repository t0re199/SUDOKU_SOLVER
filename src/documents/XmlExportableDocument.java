package documents;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XmlExportableDocument implements ExportableDocument
{
	private final Document document;
	
	public XmlExportableDocument(Document document)
	{
		if(document == null)
		{
			throw new IllegalArgumentException();
		}
		this.document = document;
	}

	@Override
	public void export(File file) throws IOException
	{
		if(file == null)
		{
			throw new IllegalArgumentException();
		}
		
		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);

			transformer.transform(source, result);
		}
		catch (TransformerFactoryConfigurationError |
			   TransformerException e)
		{
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}
