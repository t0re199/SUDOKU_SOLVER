package io.transfer_out;

import javax.xml.transform.TransformerFactoryConfigurationError;

import data_structures.Board;
import data_structures.Item;
import data_structures.Subsection;
import documents.BoardDocumentBuilder;

public class XmlExternalizer implements Externalizer
{
	private final Board board;
	
	public XmlExternalizer(Board board)
	{
		if(board == null)
		{
			throw new IllegalArgumentException();
		}
		this.board = board;
	}
	
	@Override
	public void build(BoardDocumentBuilder builder)
	{
		if(builder == null)
		{
			throw new IllegalArgumentException();
		}
		
		try
		{
			builder.openDocument();
			
			for(int i = 0x0; i < 0x9; i++)
			{
				Subsection s = board.getSubsectionById(i);
				
				builder.openSubsection(s);
				
				for(int row = 0x0; row < 0x3; row++)
				{
					for(int col = 0x0; col < 0x3; col++)
					{
						Item item = board.get(row + s.getRowMin(), col + s.getColMin());
						builder.addItemElement(item);
					}
				}
				builder.closeSubsection();
			}
			builder.closeDocument();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
			throw new Error(e);
		}
	}
}
