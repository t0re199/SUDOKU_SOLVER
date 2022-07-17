package util.log;

import java.io.File;
import java.io.PrintStream;

public class LogFactoryImpl implements LogFactory
{
	private Log logOnFile,
				logOnConsole,
				logOnStream;
	
	private final boolean DEBUG = false;
	
	private static LogFactoryImpl instance;
	
	private LogFactoryImpl()
	{
	}
	
	public static synchronized LogFactoryImpl getInstance()
	{
		if(instance == null)
		{
			instance = new LogFactoryImpl();
		}
		return instance;
	}
	
	@Override
	public synchronized Log logOnFile(File file)
	{
		if(logOnFile == null)
		{
			logOnFile = new Logger(file);
			logOnFile.setDebug(DEBUG);
		}
		return logOnFile;
	}
	
	@Override
	public Log logOnFile()
	{
		if(logOnFile == null)
		{
			throw new IllegalStateException();
		}
		return logOnFile;
	}

	@Override
	public Log logOnConsole()
	{
		if(logOnConsole == null)
		{
			logOnConsole = new Logger();
			logOnConsole.setDebug(DEBUG);
		}
		return logOnConsole;
	}

	@Override
	public Log logOnStream(PrintStream ps)
	{
		if(logOnStream == null)
		{
			logOnStream = new Logger(ps);
			logOnStream.setDebug(DEBUG);
		}
		return logOnStream;
	}

	@Override
	public Log logOnStream()
	{
		if(logOnStream == null)
		{
			throw new IllegalStateException();
		}
		return logOnStream;
	}
}