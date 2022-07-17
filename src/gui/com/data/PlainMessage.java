package gui.com.data;

import gui.View;

public class PlainMessage implements Message
{
	private View view;
	private Event event;
	
	public PlainMessage(View view, Event event)
	{
		this.view = view;
		this.event = event;
	}
	
	@Override
	public View getSource()
	{
		return view;
	}

	@Override
	public Event getEvent()
	{
		return event;
	}
}
