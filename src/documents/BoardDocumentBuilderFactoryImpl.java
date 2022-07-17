package documents;
import documents.BoardDocumentBuilder;
import documents.BoardDocumentBuilderFactory;
import documents.BoardXmlDocumentBuilder;

public final class BoardDocumentBuilderFactoryImpl implements BoardDocumentBuilderFactory
{
	private static BoardDocumentBuilderFactoryImpl instance;
	
	private BoardDocumentBuilderFactoryImpl()
	{
	}
	
	public static synchronized BoardDocumentBuilderFactoryImpl getInstance()
	{
		if(instance == null)
		{
			instance = new BoardDocumentBuilderFactoryImpl();
		}
		return instance;
	}
	
	@Override
	public BoardDocumentBuilder newBuilder(Type type)
	{
		switch (type)
		{
			case XML:
				return new BoardXmlDocumentBuilder();

			default:
				return null;
		}
	}

}
