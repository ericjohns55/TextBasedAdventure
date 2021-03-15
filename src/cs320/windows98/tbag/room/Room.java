package cs320.windows98.tbag.room;

import java.util.HashMap;

import cs320.windows98.tbag.items.Item;

public class Room {
	
	// Each room has to have a set items in the room
	private HashMap<String, Item> items;
	
	// Each room has to have a puzzle as well
	
	
	
	
	
	public Room() {
		
		items = new HashMap<String, Item>();
	
	}
	
	
	// How should the items be 
	public void addItem(String identifier, Item item)
	{
		items.put(identifier, item);
	}
	
	
	
	// This is seeing if the item is in the room
	public boolean contains(String identifier) {
		
		// See if the "identifier" coming in is present in the items map
		if (items.containsKey(identifier))
		{
			return true;
		}
		
		else
		{
			return false;
		}
		
	}
	
	
	
}
