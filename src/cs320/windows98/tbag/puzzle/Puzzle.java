package cs320.windows98.tbag.puzzle;


public class Puzzle {
	private String description;
	private String hint;
	private String update;
	
	public Puzzle(String description, String hint, String update) {
		this.description = description;
		this.hint = hint;
		this.update = update;
	}
	
	public String getDescription() 
	{
		String output = description;
		return output;
	}
	
	public String getHint() 
	{
		String output = hint;
		return output;
	}
	
	//what it says after puzzle is solved
	public String getUpdate() 
	{
		String output = update;
		return output;
	}
	
	
}
