package cs320.windows98.tbag.inventory;

import java.util.HashMap;

import cs320.windows98.tbag.items.Item;

public class Inventory {
	static double MAX_WEIGHT = 50;
	
	private HashMap<String, Item> items;
	private Item emptyItem;
	
	// identifier: create lookup table to check against items
	
	public Inventory() {
		items = new HashMap<String, Item>();
		emptyItem = new Item("emptyItem");
	}
	
	public void addItem(String identifier, Item toAdd) {
		if (canAddItem(toAdd)) {
			items.put(identifier, toAdd);
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
	
	public boolean contains(String identifier) {
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

            inventory += item + " (" + weight + "lbs), ";
        }		
		
		if (!items.isEmpty()) {
			inventory = "You have " + inventory.substring(0, inventory.length() - 2);
		} else {
			inventory = "Your inventory is empty!";
		}
		
		// length -2 removes the extra ", " at the end
		return inventory;
	}

	public void consumeItem(String identifier){
		if (items.containsKey(identifier)) {
			items.remove(identifier);
			//add in what it does later
		}
		else {
			System.out.println("I don't got that in my bag.");
		}
	}
}
