package object;

import items.Inventory;

public class RoomObject {
	private String name;
	private String description;
	private boolean isInteractable;
	private Inventory inventory;
	private boolean canHoldItems;
	private boolean locked;
	
	public RoomObject(String name, String description, boolean canHoldItems, boolean interactable, boolean locked) {
		this.name = name;
		this.isInteractable = interactable;
		this.description = description;
		this.canHoldItems = canHoldItems;
		this.locked = locked;
		this.inventory = new Inventory();
	}
	
	public boolean canHoldItems() {
		return canHoldItems;
	}
	
	public void setCanHoldItems(boolean canHoldItems) {
		this.canHoldItems = canHoldItems; 
	}
	
	public String getDescription() {
		String desc = description;
		
		if (!inventory.isEmpty()) { 
			desc += "\n" + inventory.listItems();
		}
		
		return desc;
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

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
