package data_structures;

public class SubsectionImpl implements Subsection
{
	public static final int ROW_MIN = 0x0,
							ROW_MAX = 0x1,
							COL_MIN = 0x2,
							COL_MAX = 0x3;
	
	private static final int[][] INDEXES = new int[0x9][0x4];
	
	private static final int[][] ID_MATRIX = new int[0x3][0x3];
	
	private Item[][] board = new Item[0x9][0x9];
	
	static
	{
		int id = 0x0;
		for(int i = 0x0; i < ID_MATRIX.length; i++)
		{
			for(int j = 0x0; j < ID_MATRIX[0x0].length; j++)
			{
				ID_MATRIX[i][j] = id;
				id++;
			}
		}
		
		for(int i = 0x0; i <= 0x2; i++)
		{
			INDEXES[i][ROW_MIN] = 0x0;
			INDEXES[i][ROW_MAX] = 0x2;
		}
		
		for(int i = 0x3; i <= 0x5; i++)
		{
			INDEXES[i][ROW_MIN] = 0x3;
			INDEXES[i][ROW_MAX] = 0x5;
		}
		
		for(int i = 0x6; i <= 0x8; i++)
		{
			INDEXES[i][ROW_MIN] = 0x6;
			INDEXES[i][ROW_MAX] = 0x8;
		}
		
		//TODO 
		for(int i = 0x0; i <= 0x6; i += 0x3)
		{
			INDEXES[i][COL_MIN] = 0x0;
			INDEXES[i][COL_MAX] = 0x2;
		}
		
		for(int i = 0x1; i <= 0x7; i += 0x3)
		{
			INDEXES[i][COL_MIN] = 0x3;
			INDEXES[i][COL_MAX] = 0x5;
		}
		
		for(int i = 0x2; i <= 0x8; i += 0x3)
		{
			INDEXES[i][COL_MIN] = 0x6;
			INDEXES[i][COL_MAX] = 0x8;
		}
	}
	
	private final int ID;
	
	public SubsectionImpl(int id, Item[][] board)
	{
		ID = id;
		this.board = board;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Subsection))
		{
			return false;
		}
		if(this == o)
		{
			return true;
		}
		return ((Subsection) o).getId() == ID;
	}
	
	@Override
	public int hashCode()
	{
		return ID * 997;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%nSubsection %d%n", ID));
		for(int i = getRowMin(); i <= getRowMax(); i++)
		{
			sb.append("\t[");
			for(int j = getColMin(); j <= getColMax(); j++)
			{
				if(board[i][j] != null)
				{
					sb.append(board[i][j].getStringValue() + " , ");
				}
			}
			sb.setLength(sb.length() - 0x3);
			sb.append("]\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public boolean contains(int element)
	{
		for(int i = getRowMin(); i <= getRowMax(); i++)
		{
			for(int j = getColMin(); j <= getColMax(); j++)
			{
				if(board[i][j].getValue() == element)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getRowMin()
	{
		return INDEXES[ID][ROW_MIN];
	}

	@Override
	public int getRowMax()
	{
		return INDEXES[ID][ROW_MAX];
	}

	@Override
	public int getColMin()
	{
		return INDEXES[ID][COL_MIN];
	}

	@Override
	public int getColMax()
	{
		return INDEXES[ID][COL_MAX];
	}
	
	public static int getSubsectionByItem(Item item)
	{
		return ID_MATRIX[item.getRow() / 3][item.getCol() / 3];
	}
}
