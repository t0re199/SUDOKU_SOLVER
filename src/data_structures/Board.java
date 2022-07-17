package data_structures;

public interface Board 
{
	Item get(int row, int col);
	
	void set(Item item);
	
	Subsection getSubsectionById(int id);
	
	boolean rowItemsContainsValue(int row, int element);
	
	boolean colItemsContainsValue(int col, int element);
	
	void makeUnmodifiable();
	
	default boolean isSolved()
	{
		for(int i = 0x0; i <= 0x8; i++)
		{
			for(int j = 0x0; j <= 0x8; j++)
			{
				Item item = get(i,j);
				if(!item.isFinal())
				{
					return false;
				}
			}
		}
		return true;
	}
}
