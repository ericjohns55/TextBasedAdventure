package cs320.windows98.tbag.puzzle;


public class StringPuzzle extends Puzzle 
{
	String solution;
	
	public StringPuzzle(String description, String hint, String update, String solution) 
	{
		super(description, hint, update);
		this.solution = solution;
	}
	
	public String getSolution() 
	{
		return solution;
	}
	
}

	
	