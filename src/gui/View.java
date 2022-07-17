package gui;

import gui.com.data.Message;

public interface View
{
	default void messageReceived(Message message)
	{
	}
	
	void sendMessage(Message message);
}
