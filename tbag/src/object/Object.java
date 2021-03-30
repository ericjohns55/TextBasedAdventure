package object;

import java.util.HashMap;

import items.Inventory;
import items.Item;

public class Object {
	private String name;
	private String description;
	private boolean isInteractable;
	private Inventory inventory;
	public boolean canHoldItems;
	
	
	public Object(String name, String description, boolean canHoldItems, boolean interactable) {
		this.name = name;
		this.isInteractable = interactable;
		this.description = description;
		this.canHoldItems = canHoldItems;
	}
	
	public boolean getHoldItems() {
		return canHoldItems;
	}
	
	public void setCanHoldItems(boolean canHoldItems) {
		this.canHoldItems = canHoldItems; 
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isInteractable() {
		return isInteractable;
	}
	
	public void setInteractable(boolean isInteractable) {
		this.isInteractable = isInteractable;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
