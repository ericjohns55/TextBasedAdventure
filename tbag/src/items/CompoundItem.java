package items;

import java.util.HashMap;

public class CompoundItem extends Item {
	private Inventory inventory;
	private String breakIdentifier;
	private boolean breakable;
	
	public CompoundItem(String name, double weight, boolean breakable, String breakIdentifier) {
		super(name, weight);
		this.inventory = new Inventory();
		this.breakable = breakable;
		this.breakIdentifier = breakIdentifier;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public String getBreakIdentifier() {
		return breakIdentifier;
	}

	public void setBreakIdentifier(String breakIdentifier) {
		this.breakIdentifier = breakIdentifier;
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
}
