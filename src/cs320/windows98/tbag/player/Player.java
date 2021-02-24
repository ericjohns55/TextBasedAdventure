package cs320.windows98.tbag.player;

import cs320.windows98.tbag.inventory.Inventory;

public class Player {
	private Inventory inventory;
	
	public Player() {
		inventory = new Inventory();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
