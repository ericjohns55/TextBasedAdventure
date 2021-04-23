package items;

import java.util.HashMap;

public class CompoundItem extends Item {
	private Inventory inventory;
	private Item breakItem;
	private boolean breakable;
	
	private int inventoryID;
	
	public CompoundItem(String name, double weight, boolean breakable, Item breakItem) {
		super(name, weight);
		this.inventory = new Inventory();
		this.breakable = breakable;
		this.breakItem = breakItem;
	}
	
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
		return inventory.getAllItems();
	}

	public int getInventoryID() {
		return inventoryID;
	}

	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}
}
