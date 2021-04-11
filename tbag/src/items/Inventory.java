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
		this.inventoryID = 0;
	}
	
	public int getInventoryID() {
		return inventoryID;
	}
	
	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}
	
	public void addItem(String identifier, Item toAdd) {
		if (canAddItem(toAdd)) {
			items.put(identifier.toLowerCase(), toAdd);
		}
	}
	
	public Item removeItem(String identifier) {
		if (contains(identifier)) {
			return items.remove(identifier);
		} else {
			return null;
		}
	}
	
	public Item getItem(String identifier) {
		identifier = identifier.toLowerCase();
		return items.containsKey(identifier) ? items.get(identifier) : emptyItem;
	}
	
	public boolean canAddItem(Item toAdd) {
		return getCurrentWeight() + toAdd.getWeight() <= MAX_WEIGHT && toAdd.canBePickedUp();
	}
	
	public double getCurrentWeight() {
		double weight = 0;
		
		for (String key : items.keySet()) {
			weight += items.get(key).getWeight();
		}
		
		return weight;
	}
	
	public boolean isEmpty() {
		return items.size() == 0;
	}
	
	public boolean contains(String identifier) {
		identifier = identifier.toLowerCase();
		return items.containsKey(identifier);
	}
	
	public int getInventorySize() {
		return items.size();
	}
	
	public void emptyInventory() {
		items.clear();
	}

	public String openInventory() {
		String inventory = "";
		
		for (String key : items.keySet())
        {
            String item = key;
            double weight = items.get(key).getWeight();

            inventory += item + " (" + weight + "kgs), ";
        }		
		
		if (!items.isEmpty()) {
			inventory = "You have " + inventory.substring(0, inventory.length() - 2);
		} else {
			inventory = "Your inventory is empty!";
		}
		
		// length -2 removes the extra ", " at the end
		return inventory;
	}
	
	public String listItems() {
		String itemList = "";
		
		for (String key : items.keySet()) {
			itemList += key + ", ";
		}
		
		if (!itemList.isEmpty()) {
			itemList = "This object has a " + itemList.substring(0, itemList.length() - 2);
		} else {
			itemList = "This does not contain any items.";
		}
		
		return itemList;
	}
	
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

	public void consumeItem(String identifier){
		identifier = identifier.toLowerCase();
		if (items.containsKey(identifier)) {
			items.remove(identifier);
			//add in what it does later
		}
		else {
			System.out.println("I don't got that in my bag.");
		}
	}
	
	public boolean contains(Item item) {
		return items.values().contains(item);
	}
	
	public HashMap<String, Item> getAllItems() {
		return items;
	}
}
