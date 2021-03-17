package cs320.windows98.tbag.inventory;

import java.util.HashMap;

import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.player.Player;

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

	public void openInventory() {
		for (String key : items.keySet())
        {
            String item = key;
            String weight = items.get(key).getWeight();

            System.out.println(item + " " + weight + "lbs");
        }			
		
	}

	public void dropItem(String identifier) {
		if (items.contains(identifier)) {
			items.remove(identifier);
			Player.getRoom.addItem(identifier);
		}
		else {
			System.out.println("I don't got that in my bag.");
		}
	}

	public void consumeItem(String identifier){
		if (items.contains(identifier) == true) {
			items.remove(identifier);
			//add in what it does later
		}
		else {
			System.out.println("I don't got that in my bag.");
		}
	}
}
