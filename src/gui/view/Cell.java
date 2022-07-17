package gui.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data_structures.Item;
import gui.KeyListenerAdapter;
import gui.View;
import gui.com.Mediator;
import gui.com.MediatorImpl;
import gui.com.data.Message;
import gui.com.data.PlainMessage;

public final class Cell extends JTextField implements View, FocusListener, KeyListenerAdapter
{	
	private static final long serialVersionUID = -1360938698623752404L;
	
	private static final Font FONT = new Font("Tahoma", Font.BOLD, 12);
	
	private Mediator mediator = MediatorImpl.getInstance();
	
	private Item item;
	
	private boolean isValid = true;
	
	public Cell(Item item)
	{
		if(item == null)
		{
			throw new IllegalArgumentException();
		}
		this.item = item;
		setFont(FONT);
		cellSetup();
		addFocusListener(this);
		addKeyListener(this);
	}
	
	private final void cellSetup()
	{
		boolean isFinal = item.isFinal();
		Color foregroundColor = isFinal ? Color.BLUE : Color.BLACK;
		Color backgroundColor = isFinal ? Color.GRAY : Color.WHITE;
		setEditable(!isFinal);
		setForeground(foregroundColor);
		setBackground(backgroundColor);
		setHorizontalAlignment(JTextField.CENTER);
		setText(item.getStringValue());
		checkValidity();
	}
	
	public void setItem(Item item)
	{
		if(item == null)
		{
			throw new IllegalArgumentException();
		}
		this.item = item;
		cellSetup();
	}
	
	private void showInvalidItemDialog()
	{
		JOptionPane.showMessageDialog(this, "Only numbers between 1 and 9 allowed!", 
										"Invalid Value", JOptionPane.ERROR_MESSAGE);
	}
	
	public int getRow()
	{
		return item.getRow();
	}
	
	public int getCol()
	{
		return item.getCol();
	}
	
	public boolean isValid()
	{
		return isValid;
	}

	@Override
	public void focusGained(FocusEvent e)
	{
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		checkValidity();
	}
	
	private void checkValidity()
	{
		String txt = getText();
		if(!txt.equals(""))
		{
			if(!txt.matches("[1-9]"))
			{
				setBackground(Color.RED);
				showInvalidItemDialog();
				isValid = false;
			}
			else
			{
				if(!item.isFinal())
				{
					setBackground(Color.WHITE);
					item.setValue(Integer.parseInt(txt));
					isValid = true;
				}
			}
		}
		else
		{
			setBackground(Color.WHITE);
			isValid = true;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_UP:
				sendMessage(new PlainMessage(this, gui.com.data.Event.PREVIOUS_ROW));
			break;
			
			case KeyEvent.VK_DOWN:
				sendMessage(new PlainMessage(this, gui.com.data.Event.NEXT_ROW));
			break;
			
			case KeyEvent.VK_RIGHT:
				sendMessage(new PlainMessage(this, gui.com.data.Event.NEXT_COL));
			break;
			
			case KeyEvent.VK_LEFT:
				sendMessage(new PlainMessage(this, gui.com.data.Event.PREVIOUS_COL));
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