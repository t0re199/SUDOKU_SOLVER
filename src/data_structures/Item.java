package data_structures;

import org.w3c.dom.Element;

import exceptions.IllegalItemValuesException;
import exceptions.XmlItemParsingException;

public interface Item
{
	static final int MAX_INDEX = 0x9,
					 MAX_VALUE = 0x9,
					 MIN_VALUE = 0x1,
					 NO_VALUE = Integer.MIN_VALUE;
	
	int getRow();
	
	int getCol();
	
	int getValue();
	
	void setValue(int value);
	
	default boolean isFinal()
	{
		return false;
	}
	
	default String getStringValue()
	{
		return Integer.toString(getValue());
	}
	
	static boolean checkValue(int value)
	{
		return value >= MIN_VALUE && value <= MAX_VALUE;
	}
	
	static Item parseXmlElement(Element element, Subsection subsection)
	{
		int row, col, value = Integer.MAX_VALUE;
		try
		{
			row = subsection.getRowMin() + Integer.parseInt(element.getElementsByTagName("row").item(0x0).getTextContent());
			col = subsection.getColMin() + Integer.parseInt(element.getElementsByTagName("col").item(0x0).getTextContent());
			value = Integer.parseInt(element.getElementsByTagName("value").item(0x0).getTextContent());
		}
		catch (Exception e) 
		{
			throw new XmlItemParsingException();
		}
		try
		{
			return new UnmodifiableItem(row, col, value);
		}
		catch (IllegalItemValuesException e) 
		{
			throw e;
		}
	}
}
