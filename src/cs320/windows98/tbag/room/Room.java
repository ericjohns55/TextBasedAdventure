package cs320.windows98.tbag.room;

import java.util.HashMap;

import cs320.windows98.tbag.items.Item;

public class Room {
	
	// Each room has to have a set items in the room
	private HashMap<String, Item> items;
	private HashMap<String, Room> exits;
	private String description; 
	private int roomID;
	
	// Each room has to have a puzzle as well
	

	public Room(String description, int roomID) {
		items = new HashMap<String, Item>();
		exits = new HashMap<String, Room>();
		this.description = description;
		this.roomID = roomID;
	}
	
	
	// How should the items be 
	public void addItem(String identifier, Item item)
	{
		items.put(identifier, item);
	}

	// Returns roomID
	public int getRoomID()
	{
		return roomID;
  }
	//Returns the rooms description
	public String getDescription() 
	{
		return description;
	}

	//sets the rooms exit
	public void setExit(String direction, Room neighbor)
	{
		exits.put(direction, neighbor);
	}
	
	//gets the rooms exit
	public Room getExit(String direction)
	{
		return exits.get(direction);
	}
	
	
	// This is seeing if the item is in the room
	public boolean contains(String identifier) {
		// See if the "identifier" coming in is present in the items map
		return items.containsKey(identifier);
		
	}

}