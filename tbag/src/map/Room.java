package map;

import java.util.HashMap;

import items.Inventory;
import items.Item;
import puzzle.Puzzle;
import actor.NPC;

public class Room {
	// Each room has to have a set items in the room
	private Puzzle puzzle;
	private HashMap<String, RoomObject> objects;
	private NPC npc = null;
	private String description; 
	private Inventory inventory;
	private int roomID;
	private int inventoryID;
	private Connections connections;

	private boolean canSee;

	// Each room has to have a puzzle as well

	public Room(String description, int roomID) {
		this.objects = new HashMap<String, RoomObject>(); 
		this.description = description;
		this.roomID = roomID;
		this.canSee = true;
		this.inventory = new Inventory();
		this.inventoryID = inventory.getInventoryID(); 
		this.connections = new Connections(roomID);
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

	public void addItem(String identifier, Item item) {
		inventory.addItem(identifier, item);
	}

	// Returns roomID
	public int getRoomID() {
		return roomID;
	}

	//Returns the rooms description
	public String getDescription()  {
		String output = description;
		String items = listItems();	// print out description and items list

		if (items != "") {
			output += "\nThis room has a " + items;
		}

		// newline so text doesnt overflow on story
		return output + "\n";
	}

	//sets the rooms exit
	public void addExit(String direction, Room destination) {
		connections.addConnection(direction, destination.getRoomID());
	}

	//gets the rooms exit
	public int getExit(String direction) {
		return connections.getConnection(direction);
	}

	public boolean hasExit(String direction) {
		return connections.hasConnection(direction);
	}

	// loop through inventory if not empty and add a comma between each item
	public String listItems() {
		String itemString = "";

		if (!inventory.isEmpty()) {
			for (Item item : inventory.getAllItems().values()) {
				itemString += item.getName() + ", ";
			}

			itemString = itemString.substring(0, itemString.length() - 2);	// remove last comma and space
		}

		return itemString;
	}

	public String listObjects() {
		String objects = "";

		if (!objects.isEmpty()) {
			for (RoomObject object : this.objects.values()) {
				objects += object.getName().toLowerCase() + ", ";	// same thing as list items but with objects
			}

			objects = objects.substring(0, objects.length() - 2);
		}

		return objects;
	}

	public Item getItem(String identifier) {
		if (hasItem(identifier)) {
			return inventory.getItem(identifier);	// grab item if in inventory
		}  else {
			return null;
		}
	}

	public void removeItem(String identifier) {
		if (hasItem(identifier)) {	// make sure it is in the inventory first
			inventory.removeItem(identifier);
		}
	}

	// This is seeing if the item is in the room
	public boolean hasItem(String identifier) {
		// See if the "identifier" coming in is present in the items map
		return inventory.contains(identifier);
	}

	public boolean hasItems() {
		return !inventory.isEmpty();
	}
	
	// getters and setters for room state

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	public void setCanSee(boolean canSee) {
		this.canSee = canSee;
	}

	public boolean getCanSee() {
		return canSee;
	}

	public void addNpc(NPC npc) {
		this.npc = npc;
	}

	public NPC getNpc() {
		return npc;
	}

	public boolean hasNpc()  {
		return npc != null;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getInventoryID() {
		return inventoryID;
	}

	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}

	public Connections getConnections() {
		return connections;
	}

	public void setConnections(Connections connections) {
		this.connections = connections;
	}	

	public int getConnectionsID() {
		return connections.getConnectionID();
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}