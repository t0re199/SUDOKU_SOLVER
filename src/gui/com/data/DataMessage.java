package gui.com.data;

public class DataMessage extends MessageDecorator
{
	private Object data;
	
	public DataMessage(Message message, Object data)
	{
		super(message);
		this.data = data;
	}
	
	public Object getData()
	{
		return data;
	}
}