package items;

import java.util.HashMap;

public class Inventory {
	static double MAX_WEIGHT = 50;
	
	private HashMap<String, Item> items;
	private Item emptyItem;
	private int inventoryID;
	
	// identifier: create lookup table to check against items
	
	public Inventory() {
		items = new HashMap<String, Item>();
		emptyItem = new Item("emptyItem");
	}
	
	public int getInventoryID() {
		return inventoryID;
	}
	
	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}
	
	// add item with identifier to hash map
	
	public void addItem(String identifier, Item toAdd) {
		if (canAddItem(toAdd)) {
			items.put(identifier.toLowerCase(), toAdd);
		}
	}
	
	// check that item exists then remove with identifier
	
	public Item removeItem(String identifier) {
		if (contains(identifier)) {
			return items.remove(identifier);
		} else {
			return null;
		}
	}
	
	// you would be surprised (DB purposes)
	@SuppressWarnings("unlikely-arg-type")
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	// grab item using identifier
	public Item getItem(String identifier) {
		identifier = identifier.toLowerCase();
		return items.containsKey(identifier) ? items.get(identifier) : emptyItem;
	}
	
	// make sure it fits into max weight
	public boolean canAddItem(Item toAdd) {
		return getCurrentWeight() + toAdd.getWeight() <= MAX_WEIGHT && toAdd.canBePickedUp();
	}
	
	// calculate the current weight by looping through items and summing them
	public double getCurrentWeight() {
		double weight = 0;
		
		for (String key : items.keySet()) {
			weight += items.get(key).getWeight();
		}
		
		return weight;
	}
	
	// check if empty
	public boolean isEmpty() {
		return items.size() == 0;
	}
	
	// check if inventory contains an identifier (case insensitive)
	public boolean contains(String identifier) {
		identifier = identifier.toLowerCase();
		return items.containsKey(identifier);
	}
	
	// check if item ID is in inventory (used for puzzles involving specific items)
	public boolean contains(int id) {
		for (Item item : items.values()) {
			if (item.getItemID() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	// return size of inventory
	public int getInventorySize() {
		return items.size();
	}
	
	// delete all items from inventory
	public void emptyInventory() {
		items.clear();
	}

	public String openInventory() {
		String inventory = "";
		
		for (String key : items.keySet()) {	// loop through all items and format them to print out nicely
            String item = key;
            double weight = items.get(key).getWeight();

            inventory += item + " (" + weight + "kgs), ";
        }		
		
		if (!items.isEmpty()) {
			// length -2 removes the extra ", " at the end
			inventory = "You have " + inventory.substring(0, inventory.length() - 2);
		} else {
			inventory = "Your inventory is empty!";
		}
		
		return inventory;
	}
	
	// list all items; loop through them and add to a list
	public String listItems() {
		String itemList = "";
		
		for (String key : items.keySet()) {
			itemList += key + ", ";
		}
		
		if (!itemList.isEmpty()) {
			itemList = "This object has a " + itemList.substring(0, itemList.length() - 2);	// remove extra ", " at end
		} else {
			itemList = "This does not contain any items.";
		}
		
		return itemList;
	}
	
	// same as list items, but list the items in an object (noun)
	public String listItems(String noun) {
		String itemList = "";
		
		for (String key : items.keySet()) {
			itemList += key + ", ";
		}
		
		if (!itemList.isEmpty()) {
			itemList = "This " + noun + " has a " + itemList.substring(0, itemList.length() - 2);
		} else {
			itemList = "This " + noun + "does not contain any items.";
		}
		
		return itemList;
	}

	// consume an item by removing it from the inventory
	public void consumeItem(String identifier){
		identifier = identifier.toLowerCase();
		if (items.containsKey(identifier)) {
			items.remove(identifier);
		} else {
			System.out.println("I don't got that in my bag.");
		}
	}
	
	// check if item exists
	public boolean contains(Item item) {
		return items.values().contains(item);
	}
	
	// return map for grab/drop all purposes
	public HashMap<String, Item> getAllItems() {
		return items;
	}
}
