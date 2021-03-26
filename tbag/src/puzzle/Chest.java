package puzzle;

import java.util.HashMap;

import items.Item;

public class Chest implements Puzzle {
	private String name;
	private String description;
	private boolean isSolved = false;
	private HashMap<String, Item> storedItems = new HashMap<String, Item>();
	private String solution;


	public void addItem(String identifier, Item item)
	{
		item.setCanBePickedUp(false);
		storedItems.put(identifier, item);
	}

	public Item getItem(String identifier) {
		if (contains(identifier)) {
			return storedItems.get(identifier);
		}  else {
			return null;
		}
	}

	public void removeItem(String identifier) {
		if (contains(identifier)) {
			storedItems.remove(identifier);
		}
	}

	public boolean contains(String identifier) {
		return storedItems.containsKey(identifier);
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public boolean solve(String i) 
	{
		String chestString = "\n";

		if(i.equals(solution)) {
			isSolved = true;
			if(!storedItems.isEmpty()) {
				for (Item item : storedItems.values()) {
					item.setCanBePickedUp(true);
					chestString = "The chest is now open. \n"; 
					chestString += "A" + item.getName() + "is in the chest, ";
				}
			}
			else {
				chestString = "This chest is empty.";
			}
		}
		else {
			chestString = "This chest is still locked.";
		}
		return isSolved; 
	}

	public boolean getIsSovled() 
	{
		return isSolved ;
	}


	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}


}