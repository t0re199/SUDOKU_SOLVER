package documents;

import java.io.File;
import java.io.IOException;

public interface ExportableDocument
{
	default void export(String path) throws IOException
	{
		export(new File(path));
	}
	
	void export(File file) throws IOException;
}
