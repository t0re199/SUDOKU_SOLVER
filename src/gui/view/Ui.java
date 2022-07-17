package gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.View;
import gui.com.Mediator;
import gui.com.MediatorImpl;
import gui.com.data.DataMessage;
import gui.com.data.Message;
import gui.com.data.PlainMessage;
import gui.com.data.Event;
import gui.controller.ControllerPanel;
import io.FileUtils;
import util.log.Log;
import util.log.LogFactoryImpl;

public class Ui extends JFrame implements View
{
	private static final Log log = LogFactoryImpl.getInstance().logOnConsole();
	
	private static final long serialVersionUID = 6804912915317578960L;
	
	private static final String TITLE = "Sudoku Solver by @t0re199";

	private static final FileNameExtensionFilter sourceFileFilter = new FileNameExtensionFilter("structerd files", "xml");
	
	private final Dimension SCREEN_DIMENSION;
	private final Dimension WINDOW_DIMENSION = new Dimension(500, 550);
	
	private Mediator mediator = MediatorImpl.getInstance();
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu menuFile = new JMenu("File"),
				  menuEdit = new JMenu("Edit");
	
	private JMenuItem openItem = new JMenuItem("Open..."),
					  exportItem = new JMenuItem("Export..."),
					  exportAsItem = new JMenuItem("Export as..."),
					  closeMenuItem = new JMenuItem("Close..."),
					  clearItem = new JMenuItem("Clear...");
	
	private BoardPanel boardPanel;
	private ControllerPanel controllerPanel;
	
	public Ui() 
	{
		setLookAndFeel();
		
		SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
		
		initMenuBar();
		
		setJMenuBar(menuBar);
		
		getContentPane().setLayout(new BorderLayout(0x0, 0x0));
		
		boardPanel = new BoardPanel();
		mediator.addView(boardPanel);
		
		getContentPane().add(boardPanel, BorderLayout.CENTER);
		
		controllerPanel = new ControllerPanel();
		mediator.addView(controllerPanel);
		
		getContentPane().add(controllerPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle(TITLE);
		
		setBounds(SCREEN_DIMENSION.width / 0x2 - WINDOW_DIMENSION.width / 0x2,
				SCREEN_DIMENSION.height / 0x2 - WINDOW_DIMENSION.height / 0x2, 
				WINDOW_DIMENSION.width, WINDOW_DIMENSION.height);
		
		mediator.addView(this);
	}
	
	private static final void setLookAndFeel()
	{	
		try
		{	
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	private final void initMenuBar()
	{
		menuFile.add(openItem);
		menuFile.add(exportItem);
		menuFile.add(exportAsItem);
		menuFile.add(closeMenuItem);
		
		menuEdit.add(clearItem);
		
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		
		openItem.addActionListener(e ->
		{
			File src = getSource();
			if(src != null)
			{
				sendMessage(new DataMessage(new PlainMessage(this, Event.OPEN_SOURCE), src));
			}
		});
		
		clearItem.addActionListener(e -> 
		{
			sendMessage(new PlainMessage(this, Event.CLEAR_BOARD ));
		});
		
		exportItem.addActionListener(e -> 
		{
			sendMessage(new PlainMessage(this, Event.EXPORT_BOARD));
		});
		
		exportAsItem.addActionListener(e -> 
		{
			sendMessage(new PlainMessage(this, Event.EXPORT_BOARD_WITH_NAME));
		});
		
		closeMenuItem.addActionListener(e ->
		{
			System.exit(0x0);
		});
	}
	
	private File getSource()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(FileUtils.getUserHome()));
		fileChooser.setDialogTitle("Select Source File");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(sourceFileFilter);
        int option = fileChooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION)
        {
        	return fileChooser.getSelectedFile();
        }
        return null;
	}

	@Override
	public void sendMessage(Message message)
	{
		mediator.sendMessage(message);
	}
	
	@Override
	public void messageReceived(Message message)
	{
		DataMessage dataMessage = null;
		Event event = message.getEvent();
		log.println("UI received " + message.getEvent());
		switch (event)
		{
			case SHOW_SOLUTION:
				dataMessage = (DataMessage) message;
				int index = (int) dataMessage.getData();
				setCurrentSolution(index);
			break;
			
			case RESOLVE_FINISHED_SUCCESSFULLY:
				dataMessage = (DataMessage) message;
				int solutionsLength = (int) dataMessage.getData();
				if(solutionsLength > 0x1)
				{
					setCurrentSolution(0x0);
				}
			break;
			
			case CLEAR_BOARD:
				setTitle(TITLE);
			break;
			
			default:
			break;
		}
	}
	
	private void setCurrentSolution(int index)
	{
		setTitle(TITLE + String.format(" Solution #%d", ++index));	
	}
	
	public static void main(String[] args)
	{
		Ui boardFrame = new Ui();
		boardFrame.setVisible(true);
	}
}
