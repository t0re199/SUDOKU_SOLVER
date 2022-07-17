package gui.controller;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.View;
import gui.com.Mediator;
import gui.com.MediatorImpl;
import gui.com.data.DataMessage;
import gui.com.data.Event;
import gui.com.data.Message;
import gui.com.data.PlainMessage;
import util.log.Log;
import util.log.LogFactoryImpl;

public class ControllerPanel extends JPanel implements View
{
	private static final Log log = LogFactoryImpl.getInstance().logOnConsole();
	
	private static final long serialVersionUID = -6790034965691019919L;
	
	private Mediator mediator = MediatorImpl.getInstance();
	
	private JButton buttonSolve,
					buttonClear,
					buttonNext,
					buttonPrevious;
	
	private int numSolutionFound = 0x0,
			    currentSolution = 0x0;
	
	public ControllerPanel()
	{
		setLayout(new FlowLayout(FlowLayout.CENTER, 0x4, 0x4));
		
		buttonSolve = new JButton("Solve");
		buttonSolve.addActionListener(e ->
		{
			buttonSolve.setEnabled(false);
			sendMessage(new PlainMessage(this, Event.INVOKE_RESOLVE));
		});
		add(buttonSolve);
		
		buttonClear = new JButton("Clear");
		buttonClear.addActionListener(e ->
		{
			sendMessage(new PlainMessage(this, Event.CLEAR_BOARD));
		});
		add(buttonClear);
		
		buttonPrevious = new JButton("Previous");
		buttonPrevious.addActionListener(e ->
		{
			currentSolution = (currentSolution - 0x1 + numSolutionFound) % numSolutionFound;
			if(currentSolution == 0x0)
			{
				buttonPrevious.setEnabled(false);
			}
			if(!buttonNext.isEnabled() && currentSolution < numSolutionFound - 0x1)
			{
				buttonNext.setEnabled(true);
			}
			sendMessage(new DataMessage(new PlainMessage(ControllerPanel.this, Event.SHOW_SOLUTION), currentSolution));
		});
		add(buttonPrevious);
		
		buttonNext = new JButton("Next");
		buttonNext.addActionListener(e ->
		{
			currentSolution = (++currentSolution) % numSolutionFound;
			if(currentSolution == numSolutionFound - 0x1)
			{
				buttonNext.setEnabled(false);
			}
			if(!buttonPrevious.isEnabled() && currentSolution > 0x0)
			{
				buttonPrevious.setEnabled(true);
			}
			sendMessage(new DataMessage(new PlainMessage(ControllerPanel.this, Event.SHOW_SOLUTION), currentSolution));
		});
		add(buttonNext);
		
		setNavigationButtonStatus(false);
	}
	
	private void setNavigationButtonStatus(boolean flag)
	{
		buttonPrevious.setEnabled(flag);
		buttonNext.setEnabled(flag);
		buttonPrevious.setVisible(flag);
		buttonNext.setVisible(flag);
	}

	@Override
	public void messageReceived(Message message)
	{
		log.println("CONTROL PANE reveived " + message.getEvent());
		switch (message.getEvent())
		{
			case BOARD_CLEARED:
			case SOURCE_OPENED:
				buttonSolve.setEnabled(true);
				setNavigationButtonStatus(false);
			break;
			
			case RESOLVE_FINISHED_SUCCESSFULLY:
				DataMessage dataMessage = (DataMessage) message;
				int solutionsLength = (Integer) dataMessage.getData();
				if(solutionsLength > 0x1)
				{
					numSolutionFound = solutionsLength;
					setNavigationButtonStatus(true);
					currentSolution = 0x0;
					buttonPrevious.setEnabled(false);
				}
			break;
			
			case INVALID_CELLS:
			case INVALID_REQUESTED_SOLUTION:
				buttonSolve.setEnabled(true);
			break;
			
			default:
			break;
		}
	}


	@Override
	public void sendMessage(Message message)
	{
		mediator.sendMessage(message);
	}
}