package map;

import java.util.HashMap;

import items.Item;
import object.Puzzle;
import object.RoomObject;
import obstacles.Obstacle;

public class Room {
	
	// Each room has to have a set items in the room
	private Puzzle puzzle;
	private HashMap<String, RoomObject> objects;
	private HashMap<String, Obstacle> obstacles;
	private HashMap<String, Item> items;
	private HashMap<String, Room> exits;
	private String description; 
	private int roomID;
	
	// Each room has to have a puzzle as well
	
	public Room(String description, int roomID) {
		objects = new HashMap<String, RoomObject>(); 
		obstacles = new HashMap<String, Obstacle>();
		items = new HashMap<String, Item>();
		exits = new HashMap<String, Room>();
		this.description = description;
		this.roomID = roomID;
	}
	

	public HashMap<String, RoomObject> getAllObjects() { 
		return objects; 
	} 
	 
	public void addObject(String name, RoomObject object) { 
		objects.put(name, object); 
	} 
	 
	public boolean hasObject(String name) { 
		return objects.containsKey(name); 
	} 
	 
	public RoomObject getObject(String name) { 
		return objects.get(name); 
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
		
		if (items != "") {
			output += "\nThis room has a " + items;
		}
		
		// newline so text doesnt overflow on story
		return output + "\n";
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
		String itemString = "";
		
		if (!items.isEmpty()) {
			for (Item item : items.values()) {
				itemString += item.getName() + ", ";
			}
			
			itemString = itemString.substring(0, itemString.length() - 2);
		}
		
		return itemString;
	}
	
	public String listObjects() {
		String objects = "";
		
		if (!objects.isEmpty()) {
			for (RoomObject object : this.objects.values()) {
				objects += object.getName().toLowerCase() + ", ";
			}
			
			objects = objects.substring(0, objects.length() - 2);
		}
		
		return objects;
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


	public Puzzle getPuzzle() {
		return puzzle;
	}


	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

}