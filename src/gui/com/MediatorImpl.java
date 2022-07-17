package gui.com;

import java.util.HashSet;

import gui.View;
import gui.com.data.Message;

public class MediatorImpl implements Mediator
{
	private static MediatorImpl instance;
	
	private HashSet<View> views = new HashSet<>();
	
	private MediatorImpl()
	{
	}
	
	public static synchronized MediatorImpl getInstance()
	{
		if(instance == null)
		{
			instance = new MediatorImpl();
		}
		return instance;
	}

	@Override
	public void sendMessage(Message message)
	{
		View src = message.getSource();
		for(View v : views)
		{
			if(v != src)
			{
				v.messageReceived(message);
			}
		}
	}

	@Override
	public void addView(View view)
	{
		views.add(view);
	}

	@Override
	public void removeView(View view)
	{
		views.remove(view);
	}
}