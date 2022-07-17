package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import backtracking.SudokuSolver;
import data_structures.Board;
import exceptions.InvalidSudokuBoardException;
import exceptions.PreSolvedBoardException;

class SolvingTest
{

	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
	}
	
	@Test
	void test()
	{
		final String PREFIX = "Source";
		String path = null;
		Board board = null;
		SudokuSolver solver = null;
		for(int i = 0x1; i <= 0x6; i++)
		{
			try
			{
				path = PREFIX + String.format("%d.xml", i);
				board = XmlLoaderTest.loadBoardFromXml(path);
				solver = new SudokuSolver(board);
				solver.solve();
			}
			catch(PreSolvedBoardException e)
			{
				assertTrue(board.isSolved());
			}
			catch (IOException | InvalidSudokuBoardException e) 
			{
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
	}

}
