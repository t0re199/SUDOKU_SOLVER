package util;

public final class OsUtil
{
	private OsUtil()
	{
		
	}
	
	public static boolean osIsWindows()
	{
		return getOperatingSystem().contains("Windows");
	}
	
	
	public static boolean osIsLinuxOrMacOs()
	{
		return !osIsWindows();
	}
	
	
	public static String getOperatingSystem()
	{
		return System.getProperty("os.name");
	}
	
	
	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}
	
}
