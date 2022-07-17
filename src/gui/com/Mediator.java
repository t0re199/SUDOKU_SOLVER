package gui.com;

import gui.View;
import gui.com.data.Message;

public interface Mediator
{
	void sendMessage(Message message);
	
	void addView(View view);
	
	void removeView(View view);
}
