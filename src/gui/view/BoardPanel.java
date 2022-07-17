package gui.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import backtracking.SudokuSolver;
import data_structures.Board;
import data_structures.BoardImpl;
import data_structures.Item;
import data_structures.Subsection;
import documents.BoardDocumentBuilder;
import documents.BoardDocumentBuilderFactory;
import documents.BoardDocumentBuilderFactory.Type;
import documents.BoardDocumentBuilderFactoryImpl;
import documents.ExportableDocument;
import exceptions.InvalidSudokuBoardException;
import exceptions.PreSolvedBoardException;
import gui.View;
import gui.com.Mediator;
import gui.com.MediatorImpl;
import gui.com.data.DataMessage;
import gui.com.data.Event;
import gui.com.data.Message;
import gui.com.data.PlainMessage;
import io.FileUtils;
import io.transfer_in.XmlLoader;
import io.transfer_out.Externalizer;
import io.transfer_out.XmlExternalizer;
import util.log.Log;
import util.log.LogFactoryImpl;

public final class BoardPanel extends JPanel implements View
{
	private static final Log log = LogFactoryImpl.getInstance().logOnConsole();
	
	private static final long serialVersionUID = -6794300358865304835L;
	
	private static final int MAX_SOL_ALLOWED = 0x14;
	
	private BoardDocumentBuilderFactory builderFactory = BoardDocumentBuilderFactoryImpl.getInstance();
	
	private Mediator mediator = MediatorImpl.getInstance();
	
	private Cell[][] cells = new Cell[0x9][0x9];
	
	private SubsectionPanel[] subsectionPanels = new SubsectionPanel[0x9];
	
	private Board board;
	
	private List<Board> solutions;
	
	
	public BoardPanel()
	{
		board = new BoardImpl();
		
		setLayout(new GridLayout(0x3, 0x3, 0x0, 0x0));
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 0x1);
		
 		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				cells[i][j] = new Cell(board.get(i, j));
				cells[i][j].setBorder(border);
				cells[i][j].setColumns(0x8);
				mediator.addView(cells[i][j]);
			}
		}
		
		for(int i = 0x0; i < subsectionPanels.length; i++)
		{
			subsectionPanels[i] = new SubsectionPanel(board.getSubsectionById(i));
			add(subsectionPanels[i]);
		}
	}
	
	private void setupBoard()
	{
		setupBoard(false);
	}
	
	private void setupBoard(boolean isShowingSolution)
	{
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				Item item = board.get(i, j);
				cells[i][j].setItem(item);
				if(!item.isFinal())
				{
					cells[i][j].setEditable(!isShowingSolution);
				}
			}
		}
	}
	
	private boolean cellsAreValid()
	{
		for(int i = 0x0; i < 0x9; i++)
		{
			for(int j = 0x0; j < 0x9; j++)
			{
				if(!cells[i][j].isValid())
				{
					return false;
				}
			}
		}
		return true;
	}
 
	@Override
	public void messageReceived(Message message)
	{
		log.println("BOARD PANEL received " + message.getEvent());
		switch (message.getEvent())
		{
			case OPEN_SOURCE:
				openBoard(message);
			break;
			
			case CLEAR_BOARD:
				clearBoard();
			break;
			
			case INVOKE_RESOLVE:
				invokeResolve();
			break;
			
			case EXPORT_BOARD:
				FileUtils.export(getExportableDocument());
			break;
			
			case EXPORT_BOARD_WITH_NAME:
				FileUtils.exportWithName(getExportableDocument());
			break;
			
			case SHOW_SOLUTION:
				DataMessage dataMessage = (DataMessage) message;
				int index = (int) dataMessage.getData();
				log.println(index);
				board = solutions.get(index);
				setupBoard(true);
			break;
			
			case NEXT_ROW:
			case PREVIOUS_ROW:
			case NEXT_COL:
			case PREVIOUS_COL:
				setFocusToSelectedItem(message);
			break;

			default:
				break;
		}
	}

	private ExportableDocument getExportableDocument()
	{
		BoardDocumentBuilder builder = builderFactory.newBuilder(Type.XML);
		
		Externalizer externalizer = new XmlExternalizer(board);
		
		externalizer.build(builder);
		
		return builder.getDocument();
	}
	
	private void setFocusToSelectedItem(Message message)
	{
		Cell cell = (Cell) message.getSource();
		Event event = message.getEvent();
		int row = cell.getRow();
		int col = cell.getCol();
		switch (event)
		{
			case NEXT_ROW:
				row = (++row) % 0x9;
			break;
				
			case PREVIOUS_ROW:
				row = row > 0x0 ? --row : 0x8;
			break;
			
			case NEXT_COL:
				col = (++col) % 0x9;
			break;
			
			case PREVIOUS_COL:
				col = col > 0x0 ? --col : 0x8;
			break;

			default:
				throw new IllegalArgumentException();
		}
		cells[row][col].requestFocus();
	}
	
	private int askSolutionNum()
	{
		String str = JOptionPane.showInputDialog(this, "Insert the number of solution to find");
		if(str == null || !str.matches("\\d+"))
		{
			JOptionPane.showMessageDialog(this, "A positive integer was expected;\nfound: " + str, 
					"Invalid input", JOptionPane.ERROR_MESSAGE);
			return -0x1;
		}
		return Integer.parseInt(str);
	}

	
	private void clearBoard()
	{
		board = new BoardImpl();
		solutions = null;
		setupBoard();
		sendMessage(new PlainMessage(this, Event.BOARD_CLEARED));
	}
	
	private void openBoard(Message message)
	{
		DataMessage dataMessage = (DataMessage) message;
		try
		{
			Board b = new XmlLoader((File) dataMessage.getData()).load();
			this.board = b;
			setupBoard();
			sendMessage(new PlainMessage(this, Event.SOURCE_OPENED));
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, "Unable to load board from xml", 
											"Invalid XmlFile", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void invokeResolve()
	{
		if(!cellsAreValid())
		{
			JOptionPane.showMessageDialog(this, "This board contains invalid element(s)",
					"Invalid Board", JOptionPane.ERROR_MESSAGE);
			sendMessage(new PlainMessage(this, Event.INVALID_CELLS));
			return;
		}
		int requestedSolution = askSolutionNum();
		if(requestedSolution <= 0x0)
		{
			sendMessage(new PlainMessage(this, Event.INVALID_REQUESTED_SOLUTION));
			return;
		}
		if(requestedSolution > MAX_SOL_ALLOWED)
		{
			JOptionPane.showMessageDialog(this, "You'll get at most " + MAX_SOL_ALLOWED + " solutions.", 
												" Too many solutions required", JOptionPane.WARNING_MESSAGE);
			requestedSolution = MAX_SOL_ALLOWED;
		}
		Board bck = new BoardImpl(board);
		board.makeUnmodifiable();
		try
		{
			SudokuSolver sudokuSolver = new SudokuSolver(board);
			sudokuSolver.solve(requestedSolution);
			solutions = sudokuSolver.getSolutions();
			int solutionSize = solutions.size();
			if(solutionSize == 0x0)
			{
				JOptionPane.showMessageDialog(this, "This board couldn't be solved!", 
											"Invalid Board", JOptionPane.ERROR_MESSAGE);
				return;
			}
			solutions = sudokuSolver.getSolutions();
			
			if(requestedSolution > solutionSize)
			{
				JOptionPane.showMessageDialog(this, String.format("%d solution(s) of %d found", solutionSize, requestedSolution),
												"Info", JOptionPane.INFORMATION_MESSAGE);
			}
			board = solutions.get(0x0);
			setupBoard(true);
			sendMessage(new DataMessage(new PlainMessage(this, Event.RESOLVE_FINISHED_SUCCESSFULLY), solutionSize));
		}
		catch(InvalidSudokuBoardException e)
		{
			JOptionPane.showMessageDialog(this, "This board contains error(s)!\nCheck it then retry!", 
												"Invalid Board", JOptionPane.ERROR_MESSAGE);
			board = bck;
			setupBoard();
			sendMessage(new PlainMessage(this, Event.INVALID_CELLS));
		}
		catch(PreSolvedBoardException e)
		{
			JOptionPane.showMessageDialog(this, "This board is already solved!", 
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private class SubsectionPanel extends JPanel
	{
		private static final long serialVersionUID = -6616300016915222259L;
		
		public SubsectionPanel(Subsection subsection)
		{
			Border border = BorderFactory.createLineBorder(Color.BLACK, 0x2);
			
			setBorder(border);
			
			int ROW_OFFSET = subsection.getRowMin();
			int COL_OFFSET = subsection.getColMin();
			
			setLayout(new GridLayout(0x3, 0x3, 0x0, 0x0));
			
	 		for(int i = 0x0; i < 0x3; i++)
			{
				for(int j = 0x0; j < 0x3; j++)
				{
					add(cells[i + ROW_OFFSET][j + COL_OFFSET]);
				}
			}
		}
	}

	@Override
	public void sendMessage(Message message)
	{
		mediator.sendMessage(message);
	}
}