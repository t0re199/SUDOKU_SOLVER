package gui.com.data;

import gui.View;

public abstract class MessageDecorator implements Message
{
	private Message message;
	
	public MessageDecorator(Message message)
	{
		if(message == null)
		{
			throw new IllegalArgumentException();
		}
		this.message = message;
	}

	@Override
	public View getSource()
	{
		return message.getSource();
	}

	@Override
	public Event getEvent()
	{
		return message.getEvent();
	}
	
}
