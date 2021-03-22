package cs320.windows98.tbag.puzzle;

import cs320.windows98.tbag.items.Item;

public class ItemPuzzle extends Puzzle 
{
	Item solution;
	
	public ItemPuzzle(String description, String hint, String update, Item solution) 
	{
		super(description, hint, update);
		this.solution = solution;
	}
	
	public Item getSolution() 
	{
		return solution;
	}
	
}

	
	