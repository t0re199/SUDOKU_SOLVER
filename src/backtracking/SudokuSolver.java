package backtracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data_structures.Board;
import data_structures.BoardImpl;
import data_structures.EditableItem;
import data_structures.Item;
import data_structures.Subsection;
import data_structures.SubsectionImpl;
import exceptions.InvalidSudokuBoardException;
import exceptions.PreSolvedBoardException;
import util.log.Log;
import util.log.LogFactoryImpl;

public final class SudokuSolver implements TemplateMethod<Item, Integer>
{
	private static final Log log = LogFactoryImpl.getInstance().logOnConsole();
			
	private Board board;
	
	private Item firstChoisePoint,
				 lastChoisePoint;
	
	private List<Board> solutionFound = new ArrayList<>();
	
	private boolean solved = false;
		
	public SudokuSolver(Board board)
	{
		if(board == null)
		{
			throw new IllegalArgumentException();
		}
		this.board = board;
	}
	
	private final void init()
	{
		initFirstChoisePoint();
		initLastChoisePoint();
	}
	
	@Override
	public void solve(int num_max_soluzioni)
	{
		if(!solved)
		{
			if(!checkBoard())
			{
				throw new InvalidSudokuBoardException();
			}
			if(board.isSolved())
			{
				throw new PreSolvedBoardException();
			}
			init();
			TemplateMethod.super.solve(num_max_soluzioni);
			solved = true;
		}
	}
	
	@Override
	public void solve()
	{
		TemplateMethod.super.solve(0x1);
	}
	
	
	@Override
	public Item firstChoisePoint()
	{
		return new EditableItem(firstChoisePoint);
	}
	
	private void initFirstChoisePoint()
	{
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				Item item = board.get(i, j);
				if(!item.isFinal())
				{
					firstChoisePoint = item;
					return;
				}
			}
		}
		throw new RuntimeException("Unable to find the first choise point!");
	}

	@Override
	public Item nextChoisePoint(Item choisePoint)
	{
	    log.print("Current " + choisePoint);
	    int i = choisePoint.getRow();
	    int j = choisePoint.getCol() + 0x1;
	    if(j > 0x8)
	    {
	        i++;
	        j = 0x0;
	    }
	    while (i <= 0x8)
	    {
	    	Item item = board.get(i, j);
            if(!item.isFinal())
            {
                log.println(" => Next "+item);
                return item;
            }
            j++;
            if(j > 0x8)
            {
                j = 0x0;
                i++;
            }
	    }
	    log.println(" => Next "+lastChoisePoint());
		return lastChoisePoint();	
	}

	@Override
	public Item lastChoisePoint()
	{
		return new EditableItem(lastChoisePoint);
	}
	
	private void initLastChoisePoint()
	{
		for(int i = 0x8; i >= 0x0; i--)
		{
			for(int j = 0x8; j >= 0x0; j--)
			{
				Item item = board.get(i, j);
				if(!item.isFinal())
				{
					lastChoisePoint = item;
					return;
				}
			}
		}
		throw new RuntimeException("Unable to find the last choise point!");
	}

	@Override
	public Integer firstChoise(Item ps)
	{
		return Integer.valueOf(0x1);
	}

	@Override
	public Integer nextChoise(Integer s)
	{
		return s == Item.NO_VALUE ? Integer.valueOf(0x1) : Integer.valueOf(++s);
	}

	@Override
	public Integer lastChoise(Item ps)
	{
		return Integer.valueOf(0x9);
	}

	@Override
	public boolean assignable(Integer choise, Item choisePoint)
	{
		Subsection subsection = board.getSubsectionById(SubsectionImpl.getSubsectionByItem(choisePoint));
		if(subsection.contains(choise))
		{
			return false;
		}
		if(board.rowItemsContainsValue(choisePoint.getRow(), choise))
		{
			return false;
		}
		if(board.colItemsContainsValue(choisePoint.getCol(), choise))
		{
			return false;
		}
		return true;
	}

	@Override
	public void assign(Integer choise, Item choisePoint)
	{
		choisePoint.setValue(choise);
		Subsection s = board.getSubsectionById(SubsectionImpl.getSubsectionByItem(choisePoint));
		board.set(choisePoint);
		log.println(s);
	}

	@Override
	public void deassign(Integer choise, Item choisePoint)
	{
		choisePoint.setValue(Item.NO_VALUE);
		board.set(choisePoint);
	}

	@Override
	public Item previousChoisePoint(Item choisePoint)
	{
	    log.print("Current " + choisePoint);
	    int i = choisePoint.getRow();
	    int j = choisePoint.getCol();
	    int tmp = j - 0x1;
	    if(tmp < 0x0)
	    {
	        i--;
	        tmp = 0x8;
	    }
	    j = tmp;
    	while (i >= 0) 
        {
            Item item = board.get(i, j);
            if(!item.isFinal())
            {
                log.println(" => PREVIOUS "+item);
                return item;
            }
            j--;
            if(j < 0x0)
            {
                j = 0x8;
                i--;
            }
        }
	    log.println(" => PREVIOUS "+firstChoisePoint());
		return firstChoisePoint();	
	}

	@Override
	public Integer lastChoiseAssignedTo(Item choisePoint)
	{
		return Integer.valueOf(choisePoint.getValue());
	}

	@Override
	public void writeSolution(int nr_sol)
	{
		String s = "  _________________  .____ ____   _______________________   \r\n" + 
				" /   _____/\\_____  \\ |    |\\   \\ /   /\\_   _____/\\______ \\  \r\n" + 
				" \\_____  \\  /   |   \\|    | \\   Y   /  |    __)_  |    |  \\ \r\n" + 
				" /        \\/    |    \\    |__\\     /   |        \\ |    `   \\\r\n" + 
				"/_______  /\\_______  /_______ \\___/   /_______  //_______  /\r\n" + 
				"        \\/         \\/        \\/               \\/         \\/ ";
		log.println("\n\n" + nr_sol + "\n\n");
		log.println("\n\n"+ s +"\n\n");
		log.println(board);
		solutionFound.add(new BoardImpl(board));
	}
	
	public List<Board> getSolutions()
	{
		return Collections.unmodifiableList(solutionFound);
	}
	
	private final boolean checkBoard()
	{
		boolean boardIsFine = true;
		for(int i = 0x0; i < 0x9 && boardIsFine; i++)
		{
			for(int j = 0x0; j < 0x9 && boardIsFine; j++)
			{
				Item item = board.get(i, j);
				if(item.getValue() != Item.NO_VALUE)
				{
					Item tmp = new EditableItem(i,j);
					board.set(tmp);
					if(!assignable(item.getValue(), tmp))
					{
						boardIsFine = false;
					}
					board.set(item);
				}
			}
		}
		return boardIsFine;
	}
}