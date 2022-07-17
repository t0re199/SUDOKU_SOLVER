package data_structures;

import exceptions.IllegalItemValuesException;

public class EditableItem implements Item
{	
	final int ROW,
			  COL;
	
	protected int value;
	
	public EditableItem(int row, int col)
	{
		if(!checkIndexes(row, col))
		{
			throw new IllegalItemValuesException("Illegal Indexes!");
		}
		value = NO_VALUE;
		this.ROW = row;
		this.COL = col;
	}
	
	public EditableItem(int row, int col, int value)
	{
		if(!checkIndexes(row, col))
		{
			throw new IllegalItemValuesException("Illegal Indexes : "+String.format("<%d,%d>",row, col));
		}
		if(!checkValue(value))
		{
			throw new IllegalItemValuesException("Illegal Value!" + value);
		}
		this.ROW = row;
		this.COL = col;
		this.value = value;
	}
	
	public EditableItem(Item item)
	{
		this(item.getRow(), item.getCol(), item.getValue());
	}
	
	private static final boolean checkIndexes(int row, int col)
	{
		return row >= 0x0 && row < MAX_INDEX &&
			   col >= 0x0 && col < MAX_INDEX;
	}
	
	private static final boolean checkValue(int value)
	{
		return value == NO_VALUE ||
				(value >= 0x1 && value <= 0x9);
	}
	
	public String toString()
	{ 
		return String.format("<%d,%d> = %s", ROW, COL, getStringValue());
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof Item))
		{
			return false;
		}
		if(this == o)
		{
			return true;
		}
		Item item = (Item) o;
		return item.getRow() == ROW && item.getCol() == COL;
	}
	
	public int hashCode()
	{
		return (ROW + COL) * 997;
	}

	@Override
	public int getRow()
	{
		return ROW;
	}

	@Override
	public int getCol()
	{
		return COL;
	}

	@Override
	public int getValue()
	{
		return value;
	}

	@Override
	public void setValue(int value)
	{
		this.value = value;
	}

	@Override
	public String getStringValue()
	{
		return value == NO_VALUE ? "" : value + "";
	}
}
