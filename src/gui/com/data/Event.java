package gui.com.data;

public enum Event
{
	OPEN_SOURCE
	{
		@Override 
		public boolean hasData()
		{
			return true;
		}
	},
	
	SOURCE_OPENED,
	
	CLEAR_BOARD,
	
	BOARD_CLEARED,
	
	SHOW_SOLUTION
	{
		@Override 
		public boolean hasData()
		{
			return true;
		}
	},
	
	EXPORT_BOARD,
	
	EXPORT_BOARD_WITH_NAME,
	
	INVOKE_RESOLVE,
	
	RESOLVE_FINISHED_SUCCESSFULLY
	{
		@Override 
		public boolean hasData()
		{
			return true;
		}
	},
	
	NEXT_ROW,
	
	PREVIOUS_ROW,
	
	NEXT_COL,
	
	PREVIOUS_COL,
	
	INVALID_CELLS,
	
	INVALID_REQUESTED_SOLUTION;
	
	public boolean hasData()
	{
		return false;
	}
}

