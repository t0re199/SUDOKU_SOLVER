package gui.com.data;

import gui.View;

public interface Message
{
	View getSource();

	Event getEvent();
}
