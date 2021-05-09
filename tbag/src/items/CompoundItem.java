package items;

import java.util.HashMap;

public class CompoundItem extends Item {
	private Inventory inventory;
	private Item breakItem;
	private boolean breakable;
	
	private boolean poppable;
	private int inventoryID;
	
	// holds an inventory of items
	
	public CompoundItem(String name, double weight, boolean breakable, Item breakItem) {
		super(name, weight);
		this.inventory = new Inventory();
		this.breakable = breakable;
		this.breakItem = breakItem;
		this.poppable = false;
	}
	
	// getters and setters
	// inherits from items so not much extra
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Item getBreakItem() {
		return breakItem;
	}

	public void setBreakItem(Item breakItem) {
		this.breakItem = breakItem;
	}

	public boolean isBreakable() {
		return breakable;
	}

	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}
	
	public HashMap<String, Item> getItems() {
		return inventory.getAllItems();	// return the list of all items (for DB purposes)
	}

	public int getInventoryID() {
		return inventoryID;
	}

	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}
	
	public boolean isPoppable() {
		return poppable;
	}

	public void setPoppable(boolean poppable) {
		this.poppable = poppable;
	}
}
