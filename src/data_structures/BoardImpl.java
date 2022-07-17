package data_structures;

public class BoardImpl implements Board
{
	static final int SUBSECTION_NUM = 0x9;
	
	private Subsection[] subsections = new Subsection[SUBSECTION_NUM];

	private Item[][] board = new Item[0x9][0x9];
	
	public BoardImpl(Board board)
	{
		this();
		
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				set(board.get(i, j));
			}
		}
	}
	
	public BoardImpl()
	{
		for(int i = 0; i < SUBSECTION_NUM; i++)
		{
			subsections[i] = new SubsectionImpl(i, board);
		}
		for(int i = 0x0; i < board.length; i++)
		{
			for(int j = 0x0; j < board[0x0].length; j++)
			{
				board[i][j] = new EditableItem(i, j, Item.NO_VALUE);
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Subsection s : subsections)
		{
			sb.append(s+"\n");
		}
		
		return sb.toString();
	}

//	@Override
//	public Item[][] getBoard()
//	{
//		return board;
//	}


	@Override
	public Item get(int row, int col)
	{
//		Item item = board[row][col];
//		if(!item.isFinal())
//		{
//			return new EditableItem(item);
//		}
//		else
//		{
//			return item;
//		}
		return board[row][col];
	}


	@Override
	public void set(Item item)
	{
		if(!item.isFinal())
		{
			board[item.getRow()][item.getCol()] = new EditableItem(item);
		}
		else
		{
			board[item.getRow()][item.getCol()] = item;
		}
	}

	@Override
	public Subsection getSubsectionById(int id)
	{
		return subsections[id];
	}

	@Override
	public boolean rowItemsContainsValue(int row, int element)
	{
		if(!checkIndex(row))
		{
			throw new IllegalArgumentException("Invalid Index!");
		}
		for(int j = 0x0; j < 0x9; j++)
		{
			if(board[row][j].getValue() == element)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean colItemsContainsValue(int col, int element)
	{
		if(!checkIndex(col))
		{
			throw new IllegalArgumentException("Invalid Index!");
		}
		for(int i = 0x0; i < 0x9; i++)
		{
			if(board[i][col].getValue() == element)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Board))
		{
			return false;
		}
		if(this == o)
		{
			return true;
		}
		Board board = (Board) o;
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				Item i1 = board.get(i, j);
				Item i2 = get(i, j);
				
				if(i1.isFinal() && i2.isFinal())
				{
					if(i1.getValue() != i2.getValue())
					{
						return false;
					}
				}
				else if(i1.isFinal() || i2.isFinal())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void makeUnmodifiable()
	{
		for(int i = 0x0; i < board.length; i++)
		{
			for(int j = 0x0; j < board[0x0].length; j++)
			{
				if(board[i][j].getValue() != Item.NO_VALUE && !board[i][j].isFinal())
				{
					board[i][j] = new UnmodifiableItem(board[i][j]);
				}
			}
		}
	}
	
	private static final boolean checkIndex(int index)
	{
		return index >= 0x0 && index < 0x9;
	}
}
