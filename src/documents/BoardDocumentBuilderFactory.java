package documents;

public interface BoardDocumentBuilderFactory
{
	static enum Type
	{
		XML
	}
	
	BoardDocumentBuilder newBuilder(Type type);
}
