package documents;

import data_structures.Item;
import data_structures.Subsection;

public interface BoardDocumentBuilder
{
	void openDocument();
	
	void openSubsection(Subsection subsection);
	
	void closeSubsection();
	
	void addItemElement(Item item);
	
	void closeDocument();
	
	ExportableDocument getDocument();
}
