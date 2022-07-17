package util.log;

import java.io.File;
import java.io.PrintStream;

public interface LogFactory
{
	Log logOnFile(File file);
	
	Log logOnFile();
	
	Log logOnConsole();
	
	Log logOnStream(PrintStream ps);
	
	Log logOnStream();
}
