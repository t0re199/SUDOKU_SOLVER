package util.log;

public interface Log
{
	static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	default void print()
	{
		print("");
	}
	
	default void print(byte e)
	{
		print(Byte.toString(e));
	}
	
	default void print(int e)
	{
		print(Integer.toString(e));
	}
	
	default void print(char e)
	{
		print(Character.toString(e));
	}
	
	default void print(float e)
	{
		print(Float.toString(e));
	}
	
	default void print(double e)
	{
		print(Double.toString(e));
	}
	
	default void println()
	{
		print(LINE_SEPARATOR);
	}
	
	default void println(byte e)
	{
		println(Byte.toString(e));
	}
	
	default void println(int e)
	{
		println(Integer.toString(e));
	}
	
	default void println(char e)
	{
		println(Character.toString(e));
	}
	
	default void println(float e)
	{
		println(Float.toString(e));
	}
	
	default void println(double e)
	{
		println(Double.toString(e));
	}
	
	default void println(Object e)
	{
		print(e.toString() + LINE_SEPARATOR);
	}
	
	void print(Object e);
	
	void setDebug(boolean debug);
	
}
