package exceptions;

public class IllegalItemValuesException extends RuntimeException
{
	private static final long serialVersionUID = -3683455949565118824L;

	public IllegalItemValuesException(String string)
	{
		super(string);
	}
}