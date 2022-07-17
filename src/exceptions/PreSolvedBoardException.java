package exceptions;

public class PreSolvedBoardException extends RuntimeException
{
	private static final long serialVersionUID = 7987493554032999281L;
	
	public PreSolvedBoardException()
	{
		super("This board il already solved!");
	}
}
