package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import data_structures.Board;
import io.transfer_in.XmlLoader;

class XmlLoaderTest
{
	public static Board loadBoardFromXml(String path) throws IOException
	{
		return new XmlLoader(path).load();
	}
	
	@Test
	void test()
	{
		final String PREFIX = "Source";
		String path = null;
		for(int i = 0x1; i <= 0x6; i++)
		{
			try
			{
				path = PREFIX + String.format("%d.xml", i);
				loadBoardFromXml(path);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
	}
}