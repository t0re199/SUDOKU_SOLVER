package data_structures;

import exceptions.NotAllowedOperationException;

public class UnmodifiableItem extends EditableItem
{

	public UnmodifiableItem(int row, int col, int value)
	{
		super(row, col, value);
	}
	
	public UnmodifiableItem(Item item)
	{
		super(item);
	}
	
	@Override
	public void setValue(int value)
	{
		throw new NotAllowedOperationException();
	}
	
	@Override
	public boolean isFinal()
	{
		return true;
	}
	
	@Override
	public String toString()
	{
		return String.format("{%d,%d} = %d", ROW, COL, value);
	}
}
