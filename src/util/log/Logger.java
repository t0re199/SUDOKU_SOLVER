package util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public final class Logger implements Log
{
	private static interface State
	{
		void handlePrint(Object o);
		
		void handleClose() throws Throwable;
	}
	
	private class FILE_MODE implements State
	{

		@Override
		public void handlePrint(Object o)
		{
			pw.print(o);
		}
		
		@Override
		public void handleClose() throws Throwable
		{
			pw.flush();
			pw.close();
		}
	}
	
	private class CONSOLE_MODE implements State
	{

		@Override
		public void handlePrint(Object o)
		{
			ps.print(o);
		}
		
		@Override
		public void handleClose() throws Throwable
		{
			ps.flush();
			ps.close();
		}
	}

	
	private PrintStream ps;
	
	private PrintWriter pw;
	
	private State state;
	
	private boolean debug = false;
	
	public Logger()
	{
		ps = System.out;
		state = new CONSOLE_MODE();
		init();
	}
	
	public Logger(PrintStream ps)
	{
		if(ps == null)
		{
			throw new IllegalArgumentException();
		}
		this.ps = ps;
		state = new CONSOLE_MODE();
		init();
	}
	
	public Logger(File f)
	{
		if(f == null)
		{
			throw new IllegalArgumentException();
		}
		try
		{
			this.pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			state = new FILE_MODE();
			init();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new Error(e);
		}
	}
	
	private void init()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> 
		{
			try
			{
				state.handleClose();
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}));
	}
	
	
	@Override
	public synchronized void print(Object e)
	{
		if(!debug)
		{
			return;
		}
		state.handlePrint(e);
	}
	
	@Override
	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		state.handleClose();
		super.finalize();
	}
}