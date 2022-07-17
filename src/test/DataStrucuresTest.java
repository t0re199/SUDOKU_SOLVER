package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data_structures.Board;
import data_structures.BoardImpl;
import data_structures.EditableItem;
import data_structures.Item;
import exceptions.IllegalItemValuesException;

class DataStrucuresTest
{
	private static Board board;
	private static Random random = new Random();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		board = new BoardImpl();
	}

	@After
	static void tearDownAfterClass() throws Exception
	{
		board.makeUnmodifiable();
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				Item item = board.get(i, j);
				if(item.getValue() != Item.NO_VALUE)
				{
					assertTrue(item.isFinal());
				}
			}
		}
	}

	@Test
	void test()
	{
		for(int i = 0x0; i < 0x200; i++)
		{
			int row = random.nextInt(0x9);
			int col = random.nextInt(0x9);
			int shuffle = random.nextInt(2);
			int value = shuffle == 0x0 ? random.nextInt(500) : (random.nextInt(0x9) + 0x1);
			try
			{
				Item item = new EditableItem(row, col, value);
				
				board.set(item);
				
				assertEquals(row, item.getRow());
				assertEquals(col, item.getCol());
				assertEquals(value, item.getValue());
				
				assertEquals(item, board.get(row, col));
			}
			catch (IllegalItemValuesException e) 
			{
			}
			catch (Exception e) 
			{
				fail(e.getMessage());
			}
		}
	}
}