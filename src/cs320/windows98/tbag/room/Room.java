package cs320.windows98.tbag.room;

import java.util.HashMap;

import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.obstacles.Obstacle;

public class Room {
	
	// Each room has to have a set items in the room
	private HashMap<String, Obstacle> obstacles;
	private HashMap<String, Item> items;
	private HashMap<String, Room> exits;
	private String description; 
	private int roomID;
	
	// Each room has to have a puzzle as well
	
	public Room(String description, int roomID) {
		obstacles = new HashMap<String, Obstacle>();
		items = new HashMap<String, Item>();
		exits = new HashMap<String, Room>();
		this.description = description;
		this.roomID = roomID;
	}
	
	public HashMap<String, Obstacle> getAllObstacles() {
		return obstacles;
	}
	
	public void addObstacle(String name, Obstacle obstacle) {
		obstacles.put(name, obstacle);
	}
	
	public boolean hasObstacle(String name) {
		return obstacles.containsKey(name);
	}
	
	public Obstacle getObstacle(String name) {
		return obstacles.get(name);
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
		String output = description;
		String items = listItems();
		
		if (items != "\n") {
			output += items;
		}
		
		return output;
	}

	//sets the rooms exit
	public void addExit(String direction, Room neighbor)
	{
		exits.put(direction, neighbor);
	}
	
	//gets the rooms exit
	public Room getExit(String direction)
	{
		return exits.get(direction);
	}
	
	public boolean hasExit(String direction) {
		return exits.containsKey(direction);
	}
	
	public String listItems() {
		String itemString = "\n";
		
		if (!items.isEmpty()) {
			itemString += "This room has ";
			for (Item item : items.values()) {
				itemString += item.getName() + ", ";
			}
			
			itemString = itemString.substring(0, itemString.length() - 2) + "\n";
		} else {
			itemString = "\nThere does not appear to be any items in this room...";
		}
		
		return itemString;
	}
	
	public Item getItem(String identifier) {
		if (contains(identifier)) {
			return items.get(identifier);
		}  else {
			return null;
		}
	}
	
	public void removeItem(String identifier) {
		if (contains(identifier)) {
			items.remove(identifier);
		}
	}
	
	// This is seeing if the item is in the room
	public boolean contains(String identifier) {
		// See if the "identifier" coming in is present in the items map
		return items.containsKey(identifier);
		
	}

}